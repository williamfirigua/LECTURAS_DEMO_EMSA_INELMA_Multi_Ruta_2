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
import android.os.HandlerThread;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private static final int  MAX_BATCH_SIZE        = 10;   // puntos nuevos que disparan un envío
    private static final long BATCH_SEND_INTERVAL_MS = 300_000L; // enviar cada 5 min

    // ─── Cola persistente (TrackingStore) ────────────────────────────────────
    // Los puntos viven en disco hasta que el servidor confirma el lote: sin
    // cobertura (zona rural) se acumulan días y se envían al volver la señal.
    private static final int  LOTE_ENVIO            = 150;  // puntos por request
    private static final int  MAX_LOTES_POR_CICLO   = 40;   // 6.000 puntos por ciclo como máximo
    // Retención un día menor que la del servidor (tracking.retencion_dias = 10):
    // un punto más viejo lo descartaría el servidor de todos modos.
    private static final long RETENCION_MS          = TimeUnit.DAYS.toMillis(9);
    private static final int  MAX_FILAS_COLA        = 40_000; // ~2 semanas a 1 punto / 30 s
    private static final int  PODAR_CADA_N_PUNTOS   = 100;

    // ─── Ventana horaria (sincronizada con LecturaSyncService) ───────────────
    private final AtomicInteger startHour24 = new AtomicInteger(5);
    private final AtomicInteger endHour24   = new AtomicInteger(20);
    private volatile boolean isTracking = false;

    // ─── GPS ─────────────────────────────────────────────────────────────────
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Handler handler;
    // Los callbacks de GPS y la escritura en disco van en un hilo propio: nada
    // de esto pasa por el hilo principal de la app.
    private HandlerThread ioThread;

    // ─── Estado ──────────────────────────────────────────────────────────────
    private TrackingStore store;
    private ExecutorService envioExecutor;
    private final AtomicBoolean enviando = new AtomicBoolean(false);
    private final AtomicInteger nuevosDesdeEnvio = new AtomicInteger(0);
    private int insertadosDesdePoda = 0; // solo se usa desde ioThread

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

        ioThread = new HandlerThread("TrackingIO");
        ioThread.start();
        store = TrackingStore.get(this);
        envioExecutor = Executors.newSingleThreadExecutor();

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
        // quitSafely: los puntos ya recibidos terminan de escribirse en disco.
        if (ioThread != null) ioThread.quitSafely();
        // shutdown (no shutdownNow): un envío en curso termina y borra solo lo confirmado.
        if (envioExecutor != null) envioExecutor.shutdown();
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
                ioThread.getLooper());

        isTracking = true;
        actualizarNotificacion("GPS activo");
        Log.i(TAG, "[iniciarRastreo] ✓ GPS activado | cod_operador=" + codOperador);
    }

    private void pausarTracking() {
        if (!isTracking) return;
        // Puede llegar encolado desde ioThread después de que la ventana se
        // reabriera e iniciara una sesión nueva: no pausar esa sesión.
        if (isWithinWindowNow()) return;
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
            // Este callback corre en ioThread; iniciar/pausar se hacen siempre en
            // el hilo principal para no competir por locationCallback/isTracking.
            handler.post(this::pausarTracking);
            return;
        }

        try {
            // Hora de la captura (location.getTime), no la de procesamiento: el
            // proveedor puede entregar varias ubicaciones juntas.
            store.insertar(
                    UUID.randomUUID().toString(),
                    location.getLatitude(),
                    location.getLongitude(),
                    location.hasAccuracy() ? location.getAccuracy() : null,
                    location.getTime());

            if (++insertadosDesdePoda >= PODAR_CADA_N_PUNTOS) {
                insertadosDesdePoda = 0;
                // Sin red en zona rural el reloj del teléfono puede desfasarse; si se
                // adelantara días, podar solo con él vaciaría la cola entera.
                // El mínimo de ambos relojes: solo se descarta lo que los dos
                // consideran viejo.
                long referencia = Math.min(System.currentTimeMillis(), location.getTime());
                int descartados = store.podar(referencia - RETENCION_MS, MAX_FILAS_COLA);
                if (descartados > 0) {
                    Log.w(TAG, "[procesarUbicacion] Cola acotada: descartados " + descartados + " puntos antiguos");
                }
            }

            Log.d(TAG, String.format(Locale.US, "[GPS] %.6f, %.6f (±%.1fm)",
                    location.getLatitude(), location.getLongitude(), location.getAccuracy()));

            if (nuevosDesdeEnvio.incrementAndGet() >= MAX_BATCH_SIZE) {
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

    /**
     * Envía la cola persistente en lotes de LOTE_ENVIO, del más antiguo al más
     * reciente. Cada lote se borra del disco solo tras HTTP 2xx con
     * success=true; ante cualquier fallo se detiene y el lote queda para el
     * siguiente ciclo. Reenviar es inocuo: el servidor deduplica por id.
     * Un solo envío a la vez (enviando) y fuera del hilo principal.
     */
    private void enviarBatch() {
        if (envioExecutor == null || envioExecutor.isShutdown()) return;
        if (!enviando.compareAndSet(false, true)) return;
        nuevosDesdeEnvio.set(0);

        try {
            envioExecutor.execute(() -> {
                try {
                    int enviados = 0;
                    for (int i = 0; i < MAX_LOTES_POR_CICLO; i++) {
                        List<TrackingStore.Punto> lote = store.siguienteLote(LOTE_ENVIO);
                        if (lote.isEmpty()) break;
                        if (!enviarLote(lote)) break;
                        store.borrar(lote);
                        enviados += lote.size();
                        if (lote.size() < LOTE_ENVIO) break;
                    }
                    if (enviados > 0) {
                        actualizarNotificacion("GPS activo (sincronizado)");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "[enviarBatch] Excepción: " + e.getMessage());
                } finally {
                    enviando.set(false);
                }
            });
        } catch (Exception e) {
            // RejectedExecutionException si el servicio se está destruyendo.
            enviando.set(false);
        }
    }

    /** @return true solo si el servidor confirmó el lote. */
    private boolean enviarLote(List<TrackingStore.Punto> lote) {
        try {
            // Mismo formato de hora que antes (local, sin zona): el servidor lo
            // interpreta en America/Bogota.
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            JSONArray array = new JSONArray();
            for (TrackingStore.Punto p : lote) {
                JSONObject u = new JSONObject();
                u.put("id",        p.uid);
                u.put("lat",       p.lat);
                u.put("lng",       p.lng);
                if (p.precision != null) u.put("precision", p.precision.doubleValue());
                u.put("timestamp", fmt.format(new Date(p.capturadoMs)));
                array.put(u);
            }

            JSONObject payload = new JSONObject();
            // ← cod_operador (no cod_operario como en el SCR)
            payload.put("cod_operador", codOperador);
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

            // try-with-resources: sin esto la conexión no vuelve al pool.
            try (Response response = httpClient.newCall(request).execute()) {
                String cuerpo = response.body() != null ? response.body().string() : "";
                if (!response.isSuccessful()) {
                    Log.e(TAG, "[enviarLote] Error HTTP " + response.code());
                    return false;
                }
                boolean ok = new JSONObject(cuerpo).optBoolean("success", false);
                if (!ok) {
                    Log.e(TAG, "[enviarLote] Servidor no confirmó el lote: " + cuerpo);
                    return false;
                }
                Log.i(TAG, "[enviarLote] ✓ " + lote.size() + " ubicaciones → " + cuerpo);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "[enviarLote] Excepción: " + e.getMessage());
            return false;
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