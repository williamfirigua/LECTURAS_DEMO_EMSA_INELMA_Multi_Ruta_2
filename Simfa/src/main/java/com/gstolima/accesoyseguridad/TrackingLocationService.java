package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * TrackingLocationService - Servicio de rastreo GPS para la app de lecturas.
 *
 * Mismo patrón que el SCR (TrackingLocationService) pero adaptado a:
 *  - Paquete: com.gstolima.accesoyseguridad
 *  - Campo: cod_operador (no cod_operario)
 *  - Sin Globales: usa SharedPreferences "SyncServicePrefs" compartido con LecturaSyncService
 *  - baseUrl viene de LecturaSyncService (SharedPreferences)
 *
 * Iniciado/detenido desde LecturaSyncService igual que GrillaSuspensionService
 * inicia TrackingLocationService en el SCR.
 */
public class TrackingLocationService extends Service {

    private static final String TAG = "TrackingLectura";
    private static final String CHANNEL_ID  = "tracking_lectura_channel";
    private static final int    NOTIF_ID    = 3001;

    // ─── SharedPreferences compartido con LecturaSyncService ─────────────────
    private static final String PREFS_NAME = "SyncServicePrefs";

    // Claves para persistir entre reinicios START_STICKY
    private static final String KEY_COD_OPERADOR = "tracking_cod_operador";
    private static final String KEY_API_URL      = "tracking_api_url";

    // ─── Intervalo y batch ───────────────────────────────────────────────────
    private static final long UPDATE_INTERVAL_MS   = 60_000L;  // pedir GPS cada 60 s
    private static final long FASTEST_INTERVAL_MS  = 30_000L;
    private static final int  MAX_BATCH_SIZE        = 10;
    private static final long BATCH_SEND_INTERVAL_MS = 300_000L; // enviar cada 5 min

    // Cota dura de la cola en memoria. Sin ella, un operador sin cobertura
    // acumula ubicaciones indefinidamente (1/min) hasta agotar el heap.
    // Al desbordar se descartan las MÁS ANTIGUAS: para rastreo, la posición
    // reciente vale más que la de hace ocho horas.
    private static final int  MAX_QUEUE_SIZE        = 500;

    // ─── Ventana horaria (sincronizada con LecturaSyncService) ───────────────
    private final AtomicInteger startHour24 = new AtomicInteger(5);
    private final AtomicInteger endHour24   = new AtomicInteger(20);
    private volatile boolean isTracking = false;

    // ─── GPS ─────────────────────────────────────────────────────────────────
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Handler handler;

    // ─── Estado ──────────────────────────────────────────────────────────────
    private List<JSONObject> ubicacionesPendientes = new ArrayList<>();

    // Cliente HTTP único para todo el ciclo de vida del servicio. Antes se
    // construía uno nuevo dentro de cada enviarBatch(), lo que anulaba el
    // connection pool y levantaba un dispatcher + thread pool cada 5 minutos.
    private OkHttpClient httpClient;
    private int    codOperador = 0;
    private String apiUrl      = "";

    // ==================== CICLO DE VIDA ====================

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIF_ID, buildNotification("Rastreo GPS iniciado"));

        handler = new Handler(Looper.getMainLooper());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // Cargar ventana horaria desde SharedPreferences compartido
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        startHour24.set(prefs.getInt("start_hour", 5));
        endHour24.set(prefs.getInt("end_hour", 20));

        Log.i(TAG, "[onCreate] Ventana horaria: " +
                startHour24.get() + ":00 - " + endHour24.get() + ":00");

        // Programar el envío periódico del batch
        handler.postDelayed(enviarBatchTask, BATCH_SEND_INTERVAL_MS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();

            // ── Actualizar ventana horaria en caliente ────────────────────────
            if ("UPDATE_WINDOW".equals(action)) {
                int newStart = intent.getIntExtra("start_hour", startHour24.get());
                int newEnd   = intent.getIntExtra("end_hour",   endHour24.get());
                actualizarVentanaHoraria(newStart, newEnd);
                return START_STICKY;
            }

            // ── Configuración inicial de ventana ──────────────────────────────
            if (intent.hasExtra("start_hour") && intent.hasExtra("end_hour")) {
                startHour24.set(intent.getIntExtra("start_hour", 5));
                endHour24.set(intent.getIntExtra("end_hour", 20));
                Log.i(TAG, "[onStartCommand] Ventana configurada: " +
                        startHour24.get() + ":00 - " + endHour24.get() + ":00");
            }

            // ── cod_operador y apiUrl ─────────────────────────────────────────
            if (intent.hasExtra("cod_operador")) {
                codOperador = intent.getIntExtra("cod_operador", 0);
                // Persistir para sobrevivir reinicio START_STICKY
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                        .putInt(KEY_COD_OPERADOR, codOperador)
                        .apply();
                Log.i(TAG, "[onStartCommand] cod_operador: " + codOperador);
            }

            if (intent.hasExtra("api_url")) {
                apiUrl = intent.getStringExtra("api_url");
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                        .putString(KEY_API_URL, apiUrl)
                        .apply();
                Log.i(TAG, "[onStartCommand] api_url: " + apiUrl);
            }

            // ── Iniciar rastreo si tenemos datos válidos ───────────────────────
            if (codOperador > 0 && apiUrl != null && !apiUrl.isEmpty()) {
                if (isWithinWindowNow()) {
                    Log.i(TAG, "[onStartCommand] Dentro de ventana → iniciando rastreo");
                    iniciarRastreo();
                } else {
                    Log.i(TAG, "[onStartCommand] Fuera de ventana → esperando apertura");
                    long delay = millisUntilWindowOpens();
                    handler.postDelayed(() -> {
                        if (isWithinWindowNow()) {
                            Log.i(TAG, "[Delayed] Ventana abierta → iniciando rastreo");
                            iniciarRastreo();
                        }
                    }, delay);
                }
            } else {
                Log.e(TAG, "cod_operador o api_url no válidos — deteniendo servicio");
                stopSelf();
            }

        } else {
            // Reinicio por START_STICKY — recuperar parámetros persistidos
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            codOperador = prefs.getInt(KEY_COD_OPERADOR, 0);
            apiUrl      = prefs.getString(KEY_API_URL, "");

            Log.i(TAG, "[onStartCommand/null] Reinicio START_STICKY | cod_operador=" +
                    codOperador + " | apiUrl=" + apiUrl);

            if (codOperador > 0 && apiUrl != null && !apiUrl.isEmpty()) {
                iniciarRastreo();
            } else {
                Log.w(TAG, "No hay datos persistidos para reinicio — deteniendo");
                stopSelf();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isTracking = false;
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        handler.removeCallbacksAndMessages(null);
        Log.i(TAG, "[onDestroy] Rastreo GPS detenido");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ==================== RASTREO GPS ====================

    private void iniciarRastreo() {
        if (isTracking) {
            Log.d(TAG, "[iniciarRastreo] Ya activo — ignorando");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "[iniciarRastreo] Sin permiso ACCESS_FINE_LOCATION");
            stopSelf();
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                if (result == null) return;
                for (Location location : result.getLocations()) {
                    procesarUbicacion(location);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                Looper.getMainLooper());

        isTracking = true;
        actualizarNotificacion("GPS activo");
        Log.i(TAG, "[iniciarRastreo] ✓ GPS activado | cod_operador=" + codOperador);
    }

    private void pausarTracking() {
        if (!isTracking) return;
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        isTracking = false;
        actualizarNotificacion("GPS pausado (fuera de horario)");
        Log.i(TAG, "[pausarTracking] Rastreo pausado");
    }

    private void procesarUbicacion(Location location) {
        if (!isWithinWindowNow()) {
            Log.d(TAG, "[procesarUbicacion] Fuera de ventana → pausando");
            pausarTracking();
            return;
        }

        try {
            JSONObject punto = new JSONObject();
            punto.put("lat",       location.getLatitude());
            punto.put("lng",       location.getLongitude());
            punto.put("precision", location.getAccuracy());
            punto.put("timestamp",
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(new Date()));

            // El tamaño se lee DENTRO del bloque: leerlo fuera es una carrera
            // con el hilo de envío, que puede vaciar la lista entremedias.
            final int pendientes;
            synchronized (ubicacionesPendientes) {
                ubicacionesPendientes.add(punto);
                pendientes = ubicacionesPendientes.size();
            }

            Log.d(TAG, String.format("[GPS] %.6f, %.6f (±%.1fm) | pendientes: %d",
                    location.getLatitude(), location.getLongitude(),
                    location.getAccuracy(), pendientes));

            // Envío inmediato si se llena el batch
            if (pendientes >= MAX_BATCH_SIZE) {
                enviarBatch();
            }

        } catch (Exception e) {
            Log.e(TAG, "[procesarUbicacion] Error: " + e.getMessage());
        }
    }

    // ==================== ENVÍO DE UBICACIONES ====================

    private final Runnable enviarBatchTask = new Runnable() {
        @Override
        public void run() {
            if (isWithinWindowNow()) {
                enviarBatch();
            } else {
                Log.d(TAG, "[enviarBatchTask] Fuera de ventana, omitiendo envío");
            }
            handler.postDelayed(this, BATCH_SEND_INTERVAL_MS);
        }
    };

    private void enviarBatch() {
        List<JSONObject> batch;

        synchronized (ubicacionesPendientes) {
            if (ubicacionesPendientes.isEmpty()) return;
            batch = new ArrayList<>(ubicacionesPendientes);
            ubicacionesPendientes.clear();
        }

        final List<JSONObject> batchFinal = batch;

        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                // ← cod_operador (no cod_operario como en el SCR)
                payload.put("cod_operador", codOperador);

                JSONArray array = new JSONArray();
                for (JSONObject u : batchFinal) {
                    array.put(u);
                }
                payload.put("ubicaciones", array);

                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json"),
                        payload.toString()
                );

                // apiUrl ya incluye /api/ — ej: http://host:port/api/
                String url = apiUrl + "tracking/ubicaciones";

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Log.i(TAG, "[enviarBatch] Enviando " + batchFinal.size() +
                        " ubicaciones → " + url);

                // try-with-resources: sin esto la conexión no vuelve al pool.
                try (Response response = httpClient.newCall(request).execute()) {

                    if (response.isSuccessful()) {
                        // body() puede ser null; string() consume el stream una
                        // sola vez, así que se lee a una variable local.
                        String cuerpo = response.body() != null
                                ? response.body().string() : "";
                        Log.i(TAG, "[enviarBatch] ✓ " + batchFinal.size() +
                                " ubicaciones enviadas → " + cuerpo);
                        actualizarNotificacion("GPS activo (sincronizado)");
                    } else {
                        Log.e(TAG, "[enviarBatch] Error HTTP " + response.code());
                        reencolar(batchFinal);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "[enviarBatch] Excepción: " + e.getMessage());
                reencolar(batchFinal);
            }
        }).start();
    }

    /**
     * Devuelve un lote fallido al frente de la cola respetando MAX_QUEUE_SIZE.
     * Si el total desborda la cota, se descartan las ubicaciones más antiguas
     * (las del final de la lista, ya que el lote reencolado va al frente).
     */
    private void reencolar(List<JSONObject> batch) {
        synchronized (ubicacionesPendientes) {
            ubicacionesPendientes.addAll(0, batch);

            int exceso = ubicacionesPendientes.size() - MAX_QUEUE_SIZE;
            if (exceso > 0) {
                ubicacionesPendientes.subList(
                        ubicacionesPendientes.size() - exceso,
                        ubicacionesPendientes.size()
                ).clear();
                Log.w(TAG, "[reencolar] Cola llena, descartadas " + exceso +
                        " ubicaciones antiguas (tope " + MAX_QUEUE_SIZE + ")");
            }
        }
    }

    // ==================== VENTANA HORARIA ====================

    private boolean isWithinWindowNow() {
        int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hora >= startHour24.get() && hora < endHour24.get();
    }

    private long millisUntilWindowOpens() {
        Calendar now  = Calendar.getInstance();
        Calendar open = (Calendar) now.clone();
        open.set(Calendar.HOUR_OF_DAY, startHour24.get());
        open.set(Calendar.MINUTE, 0);
        open.set(Calendar.SECOND, 0);
        open.set(Calendar.MILLISECOND, 0);

        if (open.before(now)) {
            open.add(Calendar.DAY_OF_MONTH, 1);
        }

        long diff = open.getTimeInMillis() - now.getTimeInMillis();
        return diff > 0 ? diff : 60_000L; // mínimo 1 minuto
    }

    /**
     * Actualiza la ventana horaria en caliente (llamado desde LecturaSyncService
     * cuando cargarZonaTrabajoDelServidor() recibe nuevos valores).
     * Mismo patrón que el SCR usa en actualizarVentanaEnTrackingService().
     */
    private void actualizarVentanaHoraria(int newStart, int newEnd) {
        int prevStart = startHour24.get();
        int prevEnd   = endHour24.get();

        if (prevStart == newStart && prevEnd == newEnd) return; // sin cambio

        startHour24.set(newStart);
        endHour24.set(newEnd);

        Log.i(TAG, "[actualizarVentanaHoraria] " +
                prevStart + ":00-" + prevEnd + ":00 → " +
                newStart  + ":00-" + newEnd  + ":00");

        // Reaccionar al cambio de ventana
        if (isWithinWindowNow() && !isTracking) {
            Log.i(TAG, "[actualizarVentanaHoraria] Ahora dentro de ventana → iniciando rastreo");
            iniciarRastreo();
        } else if (!isWithinWindowNow() && isTracking) {
            Log.i(TAG, "[actualizarVentanaHoraria] Ahora fuera de ventana → pausando rastreo");
            pausarTracking();
        }
    }

    // ==================== NOTIFICACIONES ====================

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Rastreo GPS Lecturas",
                    NotificationManager.IMPORTANCE_LOW);
            ch.setDescription("Rastreo GPS del lector en segundo plano");
            NotificationManager mgr = getSystemService(NotificationManager.class);
            if (mgr != null) mgr.createNotificationChannel(ch);
        }
    }

    private Notification buildNotification(String texto) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Rastreo GPS")
                .setContentText(texto)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void actualizarNotificacion(String texto) {
        try {
            NotificationManager mgr =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mgr != null) mgr.notify(NOTIF_ID, buildNotification(texto));
        } catch (Exception e) {
            Log.e(TAG, "[actualizarNotificacion] " + e.getMessage());
        }
    }

    // ==================== API PÚBLICA ====================

    /**
     * Inicia el servicio de rastreo.
     * Llamado desde LecturaSyncService igual que GrillaSuspensionService
     * llama a TrackingLocationService.iniciar() en el SCR.
     *
     * @param ctx          Contexto (el Service puede pasar 'this')
     * @param codOperador  Código del operador de la tabla operadores
     * @param apiUrl       URL base de la API incluyendo /api/ (ej: http://host:port/api/)
     */
    public static void iniciar(Context ctx, int codOperador, String apiUrl) {
        try {
            Intent intent = new Intent(ctx, TrackingLocationService.class);
            intent.putExtra("cod_operador", codOperador);
            intent.putExtra("api_url",      apiUrl);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ctx.startForegroundService(intent);
            } else {
                ctx.startService(intent);
            }

            Log.i("TrackingLectura", "[iniciar] Servicio lanzado | cod_operador=" +
                    codOperador + " | url=" + apiUrl);
        } catch (Exception e) {
            Log.e("TrackingLectura", "[iniciar] Error al lanzar servicio: " + e.getMessage());
        }
    }

    /**
     * Detiene el servicio.
     * Llamado desde LecturaSyncService.onDestroy().
     */
    public static void detener(Context ctx) {
        try {
            ctx.stopService(new Intent(ctx, TrackingLocationService.class));
            Log.i("TrackingLectura", "[detener] Servicio detenido");
        } catch (Exception e) {
            Log.e("TrackingLectura", "[detener] Error: " + e.getMessage());
        }
    }

    /**
     * Actualiza la ventana horaria en caliente sin reiniciar el servicio.
     * LecturaSyncService debe llamar esto después de cargarZonaTrabajoDelServidor().
     */
    /**
     * [FIX ForegroundServiceDidNotStartInTimeException]
     * UPDATE_WINDOW no es un inicio de servicio nuevo, es un mensaje a uno ya corriendo.
     * startForegroundService() exige que el servicio llame startForeground() en 5 s,
     * lo que produce crash si el proceso ya esta inicializado y no pasa por onCreate.
     * Solucion: startService() normal para mensajes internos.
     */
    public static void actualizarVentana(Context ctx, int startHour, int endHour) {
        try {
            Intent intent = new Intent(ctx, TrackingLocationService.class);
            intent.setAction("UPDATE_WINDOW");
            intent.putExtra("start_hour", startHour);
            intent.putExtra("end_hour",   endHour);
            // [FIX] startService (no startForegroundService) para evitar crash de 5 s
            ctx.startService(intent);
            Log.i(TAG, "[actualizarVentana] Ventana enviada: "
                    + startHour + ":00 - " + endHour + ":00");
        } catch (Exception e) {
            Log.e(TAG, "[actualizarVentana] Error: " + e.getMessage());
        }
    }
}