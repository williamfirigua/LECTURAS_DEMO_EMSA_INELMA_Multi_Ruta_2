package com.gstolima.accesoyseguridad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.gstolima.api.ApiService;
import com.gstolima.api.models.LecturaBatchResponse;
import com.gstolima.api.models.LecturaRequest;
import com.gstolima.api.models.LecturaResponse;
import com.gstolima.api.models.SendFilesResponse;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.comunicaciones.CrudEnvioFoto;
import com.gstolima.tablas.ClaveRuta;
import com.gstolima.comunicaciones.CrudEnvioLectura;
import com.gstolima.comunicaciones.CrudParametros;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;
import com.gstolima.comunicaciones.EnvioFoto;
import com.gstolima.comunicaciones.EnvioLectura;
import com.gstolima.comunicaciones.EnvioCuentaNueva;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * LecturaSyncService - Servicio de sincronización de lecturas, fotos y cuentas nuevas
 * <p>
 * FUNCIONALIDADES:
 * - Sincroniza lecturas pendientes por REST API
 * - Sincroniza fotos pendientes por REST API
 * - Sincroniza cuentas nuevas por REST API
 * - Respeta horario de trabajo configurado en servidor
 * - Envía broadcasts para actualizar UI (progressBar_cyclic, chat icon)
 * - Delay de 10 segundos entre cada acción
 * - Reemplaza la funcionalidad del Reloj3 + realizarTareasHilo()
 * <p>
 * BROADCASTS ENVIADOS:
 * - ACTION_SYNC_STARTED: Cuando inicia sincronización (activar progressBar_cyclic)
 * - ACTION_SYNC_FINISHED: Cuando termina sincronización (desactivar progressBar_cyclic)
 * - ACTION_SYNC_PROGRESS: Progreso de sincronización con mensaje
 * - ACTION_CHAT_STATUS: Estado del chat (para actualizar icono imagenChat)
 * - ACTION_LECTURAS_ENVIADAS: Cantidad de lecturas enviadas
 * - ACTION_FOTOS_ENVIADAS: Cantidad de fotos enviadas
 * - ACTION_CUENTAS_ENVIADAS: Cantidad de cuentas nuevas enviadas
 * <p>
 * USO EN MenuDeLiquidacion:
 * 1. Registrar BroadcastReceiver en onCreate()
 * 2. Mostrar/ocultar progressBar_cyclic según broadcasts
 * 3. Actualizar imagenChat según broadcast CHAT_STATUS
 *
 * @author Global Solutions & Service S.A.S.
 */
public class LecturaSyncService extends Service {

    private static final String TAG = "LecturaSyncService";

    // ===== ACCIONES DE BROADCAST =====
    public static final String ACTION_SYNC_STARTED = "com.gstolima.SYNC_STARTED";
    public static final String ACTION_SYNC_FINISHED = "com.gstolima.SYNC_FINISHED";
    public static final String ACTION_SYNC_PROGRESS = "com.gstolima.SYNC_PROGRESS";
    public static final String ACTION_CHAT_STATUS = "com.gstolima.CHAT_STATUS";
    public static final String ACTION_LECTURAS_ENVIADAS = "com.gstolima.LECTURAS_ENVIADAS";
    public static final String ACTION_FOTOS_ENVIADAS = "com.gstolima.FOTOS_ENVIADAS";
    public static final String ACTION_CUENTAS_ENVIADAS = "com.gstolima.CUENTAS_ENVIADAS";
    public static final String ACTION_REQUEST_CHAT_CALL = "com.gstolima.REQUEST_CHAT_CALL";

    // Acción de despacho inmediato. La envía el menú (o cualquier punto que
    // acabe de encolar registros) para no esperar al tick periódico.
    public static final String ACTION_FORCE_SYNC = "com.gstolima.FORCE_SYNC";

    // ===== EXTRAS DE BROADCAST =====
    public static final String EXTRA_PROGRESS_MESSAGE = "progress_message";
    public static final String EXTRA_PROGRESS_TYPE = "progress_type"; // "lecturas", "fotos", "cuentas"
    public static final String EXTRA_PROGRESS_COUNT = "progress_count";
    public static final String EXTRA_CHAT_ACTIVE = "chat_active";
    public static final String EXTRA_CANTIDAD_ENVIADA = "cantidad_enviada";
    public static final String EXTRA_IS_BUSY = "is_busy";

    public static final String EXTRA_BASE_URL = "base_url";
    public static final String EXTRA_MODULO_TRABAJO = "modulo_trabajo";
    public static final String EXTRA_RUTA_ADMINISTRADOR = "ruta_administrador";
    public static final String EXTRA_CICLO_REAL = "ciclo_real";
    public static final String EXTRA_TIPO_PROCESO = "tipo_proceso";

    public static final String EXTRA_COD_OPERADOR = "cod_operador";

    private String moduloTrabajo = "CIC";
    private String rutaAdministrador = "";
    private String cicloReal = "";
    private String tipoProceso = "L"; // L = Lectura, E = Entrega

    // ===== CONFIGURACIÓN =====
    private static final String CHANNEL_ID = "LecturaSyncChannel";
    private static final int NOTIFICATION_ID = 1001;

    /** Compartido con TrackingLocationService y con la pantalla de configuración. */
    public static final String PREFS_NAME = "SyncServicePrefs";
    public static final String KEY_BASE_URL = "base_url";
    public static final String KEY_TRACKING_API_URL = "tracking_api_url";

    // Intervalo principal de sincronización (2 minutos = 120000ms)
    // Cuando está ocupado se reduce a 30 segundos
    private static final long SYNC_INTERVAL_NORMAL_MS = 120000;
    private static final long SYNC_INTERVAL_BUSY_MS = 30000;

    // Delay entre cada acción de sincronización (10 segundos)
    private static final long DELAY_ENTRE_ACCIONES_MS = 1500;

    // Ventana de coalescencia del despacho inmediato. Una ráfaga de lecturas
    // guardadas seguidas produce UN solo ciclo, no uno por lectura.
    private static final long IMMEDIATE_DEBOUNCE_MS = 2000;

    // Tamaño de lote para envío
    private static final int BATCH_SIZE_LECTURAS = 50;

    // Maximo de lecturas por request HTTP. Acota el tamano del POST
    // INDEPENDIENTEMENTE de cuantas se procesen por ciclo: si maxRegEnvio
    // vale 500, salen en 10 requests de 50 en vez de uno gigante que
    // arriesgaria el max_execution_time del servidor.
    private static final int HTTP_CHUNK_LECTURAS = 50;

    // Lecturas por ciclo. Se recarga desde el parametro maxRegEnvio en
    // cada ciclo, asi que el operador puede ajustarlo sin reinstalar.
    private final AtomicInteger batchLecturas = new AtomicInteger(BATCH_SIZE_LECTURAS);
    private static final int BATCH_SIZE_FOTOS = 10;
    private static final int BATCH_SIZE_CUENTAS = 10;

    // ===== ESTADO =====
    private ScheduledExecutorService scheduler;
    private ApiService apiService;

    /** URL con la que se construyó el Retrofit vigente; permite detectar cambios. */
    private String baseUrlActual = "";

    private AtomicBoolean isSyncing = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    // true mientras haya un ciclo forzado ya programado y aún no ejecutado.
    // Es el mecanismo de debounce: el primer forzado gana el CAS y programa;
    // los siguientes dentro de la ventana no hacen nada.
    private final AtomicBoolean immediatePending = new AtomicBoolean(false);
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // Zona de trabajo
    private AtomicInteger startHour24 = new AtomicInteger(5);
    private AtomicInteger endHour24 = new AtomicInteger(20);

    // Intervalo dinámico (como el INTERVAL del Reloj3)
    private AtomicInteger currentInterval = new AtomicInteger((int) SYNC_INTERVAL_NORMAL_MS);

    // Estadísticas de la sesión
    private int lecturasEnviadasSesion = 0;
    private int fotosEnviadasSesion = 0;
    private int cuentasEnviadasSesion = 0;

    private int codOperador = 0;

    // ===== MÉTODOS ESTÁTICOS PARA CONTROLAR EL SERVICIO =====

    /**
     * Inicia el servicio de sincronización con configuración desde MenuDeLiquidacion
     *
     * @param context           Contexto de la aplicación
     * @param baseUrl           URL base del API (cadenaURLapi de getParamsWs())
     * @param moduloTrabajo     "CIC" o "ENT" (de VariablesGlobales.moduloTrabajo)
     * @param rutaAdministrador Ruta del administrador (de getParamsWs())
     * @param cicloReal         Ciclo actual (CicloReal)
     * @param tipoProceso       "L" para lecturas, "E" para entregas (de VariablesGlobales.getTipoDeRuta())
     */
    public static void start(Context context, String baseUrl, String moduloTrabajo,
                             String rutaAdministrador, String cicloReal,
                             String tipoProceso, int codOperador) {
        Intent intent = new Intent(context, LecturaSyncService.class);
        intent.putExtra(EXTRA_BASE_URL, baseUrl);
        intent.putExtra(EXTRA_MODULO_TRABAJO, moduloTrabajo);
        intent.putExtra(EXTRA_RUTA_ADMINISTRADOR, rutaAdministrador);
        intent.putExtra(EXTRA_CICLO_REAL, cicloReal);
        intent.putExtra(EXTRA_TIPO_PROCESO, tipoProceso);
        intent.putExtra(EXTRA_COD_OPERADOR, codOperador);   // ← NUEVO

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "[start] Error al lanzar LecturaSyncService: " + e.getMessage());
        }
    }

    // Mantener el overload sin codOperador para compatibilidad hacia atrás
    // (en ese caso el tracking no se inicia — requiere codOperador)
    public static void start(Context context, String baseUrl, String moduloTrabajo,
                             String rutaAdministrador, String cicloReal, String tipoProceso) {
        start(context, baseUrl, moduloTrabajo, rutaAdministrador, cicloReal, tipoProceso, 0);
    }

    // Overload mínimo: resuelve la URL desde Realm y el resto desde SharedPreferences.
    // Llamado desde el bloque fallback de MenuDeLiquidacion cuando getParamsWs() falla,
    // y tras grabar la configuración en Configurar_webIp para reconstruir Retrofit.
    public static void start(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            // La URL del API es la de la configuración vigente en Realm; las
            // preferencias son solo respaldo. Antes había una IP embebida como
            // valor por defecto, que con el API en dominio+TLS apunta a un host
            // muerto y deja el servicio reintentando en bucle.
            String baseUrl = resolverBaseUrl(context);
            String moduloTrab = prefs.getString("modulo_trabajo", "CIC");
            String rutaAdmin = prefs.getString("ruta_administrador", "");
            String ciclo = prefs.getString("ciclo_real", "");
            String tipo = prefs.getString("tipo_proceso", "L");
            int codOp = prefs.getInt("cod_operador", 0);

            if (baseUrl.isEmpty()) {
                Log.e(TAG, "[start(context)] Sin URL de API configurada; no se inicia el servicio");
                return;
            }

            start(context, baseUrl, moduloTrab, rutaAdmin, ciclo, tipo, codOp);
        } catch (Exception e) {
            Log.e(TAG, "[start(context)] Error al leer SharedPreferences o lanzar servicio: "
                    + e.getMessage());
        }
    }

    /**
     * Resuelve la URL base del API con precedencia explícita:
     * 1. Perfil activo en Realm (fuente de verdad, editable por el operador).
     * 2. SharedPreferences (respaldo si Realm aún no está disponible).
     *
     * Devuelve cadena vacía si no hay nada configurado. Nunca inventa un host:
     * un valor por defecto inválido produce reintentos infinitos contra un
     * servidor inexistente y enmascara el error real de parametrización.
     */
    static String resolverBaseUrl(Context context) {
        try {
            BDComunicaciones bdc = CrudComunicaciones.getParams();
            if (bdc != null && !bdc.getUrlApi().isEmpty()) {
                return bdc.getUrlApi();
            }
        } catch (Exception e) {
            Log.w(TAG, "[resolverBaseUrl] Realm no disponible: " + e.getMessage());
        }
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_BASE_URL, "");
    }

    /**
     * Detiene el servicio de sincronización
     */
    public static void stop(Context context) {
        Intent intent = new Intent(context, LecturaSyncService.class);
        context.stopService(intent);
    }

    /**
     * Obtiene estadísticas de sincronización
     */
    public static String getEstadisticas() {
        int[] lecStats = CrudEnvioLectura.getEstadisticas();
        int[] fotoStats = CrudEnvioFoto.getEstadisticas();
        int[] cuentaStats = CrudEnvioCuentaNueva.getEstadisticas();

        return String.format(Locale.US,
                "LECTURAS:\n  Pendientes: %d\n  Enviadas: %d\n  Con error: %d\n\n" +
                        "FOTOS:\n  Pendientes: %d\n  Enviadas: %d\n  Con error: %d\n\n" +
                        "CUENTAS NUEVAS:\n  Pendientes: %d\n  Enviadas: %d\n  Con error: %d",
                lecStats[0], lecStats[1], lecStats[2],
                fotoStats[0], fotoStats[1], fotoStats[2],
                cuentaStats[0], cuentaStats[1], cuentaStats[2]);
    }

    /**
     * Configura la URL base del API en preferencias y reinicia el servicio para
     * que reconstruya Retrofit. Llamar desde Configurar_webIp tras grabar.
     *
     * Escribe también tracking_api_url: TrackingLocationService lee esa clave y
     * si queda desincronizada el GPS sigue reportando al host anterior.
     */
    /**
     * Dispara un ciclo de sincronización inmediato sin esperar al tick periódico.
     *
     * Seguro de llamar en ráfaga: el servicio coalesce las peticiones en una
     * ventana de {@link #IMMEDIATE_DEBOUNCE_MS}, así que N lecturas guardadas
     * seguidas producen un solo ciclo. Si el servicio no estuviera vivo, el
     * startService lo levanta y el ciclo inicial cubre lo pendiente.
     */
    public static void forzarSync(Context context) {
        try {
            Intent intent = new Intent(context, LecturaSyncService.class);
            intent.setAction(ACTION_FORCE_SYNC);
            context.startService(intent);
        } catch (Exception e) {
            // startService desde background puede lanzar en Android 8+. No es
            // fatal: el ciclo periódico recogerá los pendientes igual.
            Log.w(TAG, "[forzarSync] No se pudo despachar: " + e.getMessage());
        }
    }

    public static void configurarUrl(Context context, String baseUrl) {
        if (baseUrl == null) baseUrl = "";
        if (!baseUrl.isEmpty() && !baseUrl.endsWith("/")) baseUrl = baseUrl + "/";

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_BASE_URL, baseUrl)
                .putString(KEY_TRACKING_API_URL, baseUrl)
                .apply();

        if (!baseUrl.isEmpty()) {
            start(context);
        }
    }

    // ===== CICLO DE VIDA DEL SERVICIO =====

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Servicio creado");

        createNotificationChannel();

        // [FIX ForegroundServiceDidNotStartInTimeException]
        // startForeground DEBE llamarse lo antes posible en onCreate.
        // Si se llama en onStartCommand (como estaba antes), cualquier operacion
        // pesada anterior (initRetrofit, loadConfiguration) puede agotar los 5 s
        // que Android concede antes de lanzar el crash.
        startForeground(NOTIFICATION_ID, createNotification("Iniciando sincronizacion..."));

        loadConfiguration();

        // initRetrofit() se resuelve contra Realm, no contra el Intent: en un
        // re-arranque por START_STICKY el sistema recrea el servicio sin extras
        // y antes se caía al valor por defecto embebido.
        if (!initRetrofit()) {
            updateNotification("Sin configuracion de API");
            Log.e(TAG, "onCreate: sin URL de API; el servicio queda a la espera de configuracion");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio iniciado");

        // ── Despacho inmediato ────────────────────────────────────────────
        // Se atiende antes que nada y se retorna: este Intent no trae config,
        // así que reprocesar el bloque de abajo sería trabajo inútil (y con
        // extras nulos, destructivo sobre las preferencias).
        if (intent != null && ACTION_FORCE_SYNC.equals(intent.getAction())) {
            requestImmediateSync();
            return START_STICKY;
        }

        // Leer configuración del Intent
        if (intent != null) {
            // Persistir configuración recibida del Intent. La URL del API no se
            // toma del extra como fuente de verdad: se resuelve desde Realm en
            // resolverBaseUrl(), de modo que un Intent viejo (o un re-arranque
            // sin extras) no pueda dejar el servicio apuntando al host anterior.
            String url = intent.getStringExtra(EXTRA_BASE_URL);
            if (url != null && !url.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit()
                        .putString(KEY_BASE_URL, url)
                        .putString(KEY_TRACKING_API_URL, url)
                        .putString("modulo_trabajo", intent.getStringExtra(EXTRA_MODULO_TRABAJO))
                        .putString("ruta_administrador", intent.getStringExtra(EXTRA_RUTA_ADMINISTRADOR))
                        .putString("ciclo_real", intent.getStringExtra(EXTRA_CICLO_REAL))
                        .putString("tipo_proceso", intent.getStringExtra(EXTRA_TIPO_PROCESO))
                        .apply();

                loadConfiguration();
            }

            // Reconstruye Retrofit solo si la URL efectiva cambió.
            reinicializarRetrofitSiCambio();

            // ── leer cod_operador y arrancar tracking ─────────────────────────
            int codOp = intent.getIntExtra(EXTRA_COD_OPERADOR, 0);
            if (codOp > 0) {
                codOperador = codOp;
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                        .putInt("cod_operador", codOperador)
                        .apply();
            } else {
                // Intentar recuperar de SharedPreferences si ya fue guardado
                codOperador = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .getInt("cod_operador", 0);
            }

            // El tracking usa la MISMA URL resuelta que el sync. Antes leía
            // "base_url" de preferencias, que podía conservar el host anterior
            // si nunca se regrabó la configuración.
            String baseUrl = getBaseUrl();

            if (codOperador > 0 && !baseUrl.isEmpty()) {
                Log.i(TAG, "Iniciando TrackingLocationService | cod_operador=" + codOperador);
                // [FIX] Lanzar TrackingLocationService desde el hilo principal
                // para evitar condicion de carrera con el timer de 5 s de startForeground
                final int finalCodOperador = codOperador;
                final String finalBaseUrl = baseUrl;
                mainHandler.post(() -> {
                    try {
                        TrackingLocationService.iniciar(
                                LecturaSyncService.this, finalCodOperador, finalBaseUrl);
                    } catch (Exception ex) {
                        Log.e(TAG, "[onStartCommand] Error iniciando TrackingLocationService: "
                                + ex.getMessage());
                    }
                });
            } else {
                Log.w(TAG, "TrackingLocationService no iniciado: " +
                        "cod_operador=" + codOperador + " baseUrl=" + baseUrl);
            }
        }


        // [FIX] startForeground ya fue llamado en onCreate - solo actualizar notificacion
        updateNotification("Sincronizacion activa");

        if (!isRunning.get()) {
            isRunning.set(true);
            startScheduler();
        }

        return START_STICKY;
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Servicio destruido");

        isRunning.set(false);
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Servicio destruido");

        isRunning.set(false);
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }

        // ── NUEVO: detener rastreo GPS al parar el sync ───────────────────────
        TrackingLocationService.detener(this);
        Log.i(TAG, "TrackingLocationService detenido");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ===== INICIALIZACIÓN =====

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sincronización de Lecturas",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Servicio de sincronización de lecturas, fotos y cuentas nuevas");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification(String message) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("OKINNOM Sync")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_popup_sync)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    private void updateNotification(String message) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, createNotification(message));
        }
    }

    /**
     * Construye el cliente Retrofit.
     *
     * @return true si quedó operativo. Antes cualquier excepción se registraba y
     * el método retornaba en silencio dejando apiService en null, con lo que
     * cada ciclo de sincronización moría con NullPointerException sin indicar
     * la causa real (URL base vacía o mal formada).
     */
    private boolean initRetrofit() {
        String baseUrl = getBaseUrl();

        if (baseUrl == null || baseUrl.isEmpty()) {
            Log.e(TAG, "initRetrofit: no hay URL de API configurada");
            apiService = null;
            return false;
        }

        // Retrofit exige barra final: con "https://host/api" las rutas relativas
        // de ApiService ("lecturas/sync") resolverían contra la raíz del host.
        if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";

        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
            baseUrlActual = baseUrl;
            Log.i(TAG, "Retrofit inicializado con URL: " + baseUrl);
            return true;

        } catch (IllegalArgumentException e) {
            // baseUrl mal formada: es un error de parametrización, no de red.
            Log.e(TAG, "initRetrofit: URL invalida [" + baseUrl + "]: " + e.getMessage());
            apiService = null;
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando Retrofit: " + e.getMessage(), e);
            apiService = null;
            return false;
        }
    }

    /**
     * Reconstruye Retrofit solo si la URL efectiva cambió. Evita recrear el pool
     * de conexiones de OkHttp en cada onStartCommand.
     */
    private void reinicializarRetrofitSiCambio() {
        String nueva = getBaseUrl();
        if (nueva != null && !nueva.isEmpty() && !nueva.equals(baseUrlActual)) {
            Log.i(TAG, "URL de API cambio: " + baseUrlActual + " -> " + nueva);
            initRetrofit();
        }
    }

    private String getBaseUrl() {
        return resolverBaseUrl(this);
    }

    private void loadConfiguration() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        startHour24.set(prefs.getInt("start_hour", 5));
        endHour24.set(prefs.getInt("end_hour", 20));

        // Cargar configuración adicional
        moduloTrabajo = prefs.getString("modulo_trabajo", "CIC");
        rutaAdministrador = prefs.getString("ruta_administrador", "");
        cicloReal = prefs.getString("ciclo_real", "");
        tipoProceso = prefs.getString("tipo_proceso", "L");

        Log.i(TAG, "Configuración cargada:");
        Log.i(TAG, "  - Horario: " + startHour24.get() + ":00 - " + endHour24.get() + ":00");
        Log.i(TAG, "  - Módulo: " + moduloTrabajo);
        Log.i(TAG, "  - Tipo proceso: " + tipoProceso);
        Log.i(TAG, "  - Ciclo: " + cicloReal);
    }

    // ===== SCHEDULER =====

    private void startScheduler() {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Programar ejecución inicial
        scheduleNextRun(0);

        Log.i(TAG, "Scheduler iniciado");
    }

    /**
     * Programa la siguiente ejecución con el intervalo actual
     * Simula el comportamiento del Reloj3 con INTERVAL variable
     */
    private void scheduleNextRun(long delayMs) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.schedule(
                    () -> doWork(false),
                    delayMs,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    /**
     * Programa un ciclo forzado tras la ventana de debounce.
     *
     * El CAS sobre immediatePending garantiza que solo la primera llamada de
     * la ráfaga programe realmente. El flag se libera al entrar en doWork,
     * no al terminarlo, para que un forzado que llegue DURANTE el ciclo pueda
     * programar otro y no se pierda trabajo recién encolado.
     *
     * El ciclo forzado NO reprograma la cadena periódica: el scheduler normal
     * sigue su curso intacto.
     */
    private void requestImmediateSync() {
        if (scheduler == null || scheduler.isShutdown()) {
            Log.w(TAG, "[requestImmediateSync] Scheduler no disponible");
            return;
        }
        if (!immediatePending.compareAndSet(false, true)) {
            Log.d(TAG, "[requestImmediateSync] Ya hay un despacho pendiente, coalescido");
            return;
        }
        Log.i(TAG, "[requestImmediateSync] Despacho inmediato programado");
        scheduler.schedule(() -> doWork(true), IMMEDIATE_DEBOUNCE_MS, TimeUnit.MILLISECONDS);
    }

    // ===== TRABAJO PRINCIPAL (REEMPLAZA realizarTareasHilo) =====

    /**
     * @param forced true si el ciclo lo disparó forzarSync(). Un ciclo forzado:
     *   - ignora la ventana horaria (el operador pidió enviar AHORA),
     *   - omite los delays entre pasos,
     *   - no reprograma la cadena periódica.
     */
    private void doWork(boolean forced) {
        if (forced) {
            // Liberar aquí y no al final: un forzado que llegue mientras este
            // ciclo corre debe poder programar otro.
            immediatePending.set(false);
        }

        if (!isRunning.get()) {
            return;
        }

        // Verificar si ya hay sincronización en curso
        if (isSyncing.get()) {
            Log.d(TAG, "Ya hay una sincronización en curso, reprogramando...");
            if (!forced) {
                scheduleNextRun(SYNC_INTERVAL_BUSY_MS);
            } else {
                // El ciclo en curso ya está enviando; reintentar poco después
                // para cubrir lo que se encoló justo ahora.
                requestImmediateSync();
            }
            return;
        }

        // Sin cliente HTTP no hay nada que hacer. Se reintenta construirlo en
        // cada ciclo: si el operador corrige la URL en Configurar_webIp mientras
        // el servicio está vivo, se recupera solo sin reiniciar la aplicación.
        if (apiService == null && !initRetrofit()) {
            Log.w(TAG, "Sin configuracion de API; ciclo omitido");
            updateNotification("Sin configuracion de API");
            if (!forced) scheduleNextRun(SYNC_INTERVAL_NORMAL_MS);
            return;
        }

        // Verificar horario de trabajo. Un ciclo forzado lo ignora: si el
        // operador pulsa "Enviar lecturas" a las 21:30, se envía.
        if (!forced && !isWithinWorkingHours()) {
            Log.d(TAG, "Fuera de horario de trabajo (" + startHour24.get() + ":00 - " + endHour24.get() + ":00)");
            updateNotification("Fuera de horario de trabajo");
            scheduleNextRun(SYNC_INTERVAL_NORMAL_MS);
            return;
        }

        isSyncing.set(true);

        try {
            // ===== NOTIFICAR INICIO - Activar progressBar_cyclic =====
            sendBroadcastSyncStarted();
            updateNotification("Sincronizando...");

            Log.i(TAG, "===== INICIANDO CICLO DE SINCRONIZACIÓN =====");

            // 1. Cargar configuración del servidor
            Log.d(TAG, "Paso 1: Cargando zona de trabajo del servidor...");
            cargarZonaTrabajoDelServidor();
            esperarDelay(forced);

            // 2. Sincronizar lecturas
            Log.d(TAG, "Paso 2: Sincronizando lecturas...");
            sendBroadcastProgress("Enviando lecturas...", "lecturas", 0);
            cargarBatchSize();
            int lecturasEnviadas = sincronizarLecturas();
            if (lecturasEnviadas > 0) {
                sendBroadcastLecturasEnviadas(lecturasEnviadas);
                lecturasEnviadasSesion += lecturasEnviadas;
            }
            esperarDelay(forced);

            // 3. Sincronizar fotos
            Log.d(TAG, "Paso 3: Sincronizando fotos...");
            sendBroadcastProgress("Enviando fotos...", "fotos", 0);
            int fotosEnviadas = sincronizarFotos();
            if (fotosEnviadas > 0) {
                sendBroadcastFotosEnviadas(fotosEnviadas);
                fotosEnviadasSesion += fotosEnviadas;
            }
            esperarDelay(forced);

            // 4. Sincronizar cuentas nuevas
            Log.d(TAG, "Paso 4: Sincronizando cuentas nuevas...");
            sendBroadcastProgress("Enviando cuentas nuevas...", "cuentas", 0);
            int cuentasEnviadas = sincronizarCuentasNuevas();
            if (cuentasEnviadas > 0) {
                sendBroadcastCuentasEnviadas(cuentasEnviadas);
                cuentasEnviadasSesion += cuentasEnviadas;
            }
            esperarDelay(forced);

            // 5. Actualizar estado del chat y solicitar chatAsyncall()
            // Esto reemplaza la lógica del Reloj3 que llamaba a chatAsyncall()
            Log.d(TAG, "Paso 5: Actualizando estado del chat...");
            actualizarEstadoChatYSolicitarLlamada();

            // Actualizar notificación con resumen
            String resumen = String.format(Locale.US,
                    "Sesión: %d lect, %d fotos, %d cuentas",
                    lecturasEnviadasSesion, fotosEnviadasSesion, cuentasEnviadasSesion);
            updateNotification(resumen);

            Log.i(TAG, "===== CICLO DE SINCRONIZACIÓN COMPLETADO =====");
            Log.i(TAG, resumen);

            // Determinar siguiente intervalo (como hacía Reloj3)
            // Si no hay pendientes, intervalo normal (120s)
            // Si hay pendientes, intervalo corto (30s)
            int pendientes = CrudEnvioLectura.contarPendientes() +
                    CrudEnvioFoto.contarPendientes() +
                    CrudEnvioCuentaNueva.contarPendientes();

            if (pendientes > 0) {
                currentInterval.set((int) SYNC_INTERVAL_BUSY_MS);
                Log.d(TAG, "Hay " + pendientes + " pendientes, próximo ciclo en 30s");
            } else {
                currentInterval.set((int) SYNC_INTERVAL_NORMAL_MS);
                Log.d(TAG, "No hay pendientes, próximo ciclo en 2min");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en doWork: " + e.getMessage());
            e.printStackTrace();
        } finally {
            isSyncing.set(false);

            // ===== NOTIFICAR FIN - Desactivar progressBar_cyclic =====
            sendBroadcastSyncFinished();

            // Un ciclo forzado NO reprograma: la cadena periódica sigue viva
            // por su cuenta. Si reprogramara, cada pulsación del menú crearía
            // una cadena paralela y el servicio acabaría con N timers.
            if (!forced) {
                scheduleNextRun(currentInterval.get());
            }
        }
    }

    /**
     * Espera el delay configurado entre acciones
     */
    /**
     * Espera entre pasos del ciclo. Un ciclo forzado no espera: el operador
     * está mirando la pantalla.
     */
    /**
     * Modulo con el que se capturo la fila. Las filas creadas antes de que el esquema
     * guardara este dato vienen vacias: para esas se usa el global, que es el comportamiento
     * anterior.
     */
    private String moduloDe(String moduloDeLaFila) {
        return (moduloDeLaFila == null || moduloDeLaFila.trim().isEmpty())
                ? moduloTrabajo
                : moduloDeLaFila.trim();
    }

    /**
     * Carpeta destino de una foto en el servidor, con el modulo y el ciclo de SU ruta.
     * Formato: CIC202602090/FOTOGRAFIAS o ENT202602090/FOTOGRAFIAS
     */
    private String rutaDestinoDe(EnvioFoto foto) {
        // El modulo ya viene guardado en la fila (se congela al capturar). El ciclo se saca de
        // la clave de ruta, que son las 20 posiciones del archivo NOMBRE de esa carga.
        String modulo = moduloDe(foto.getModuloTrabajo());
        String ciclo = ClaveRuta.cicloRealDe(foto.getClaveRuta());
        if (ciclo.isEmpty()) {
            ciclo = cicloReal; // filas anteriores a esta version del esquema
        }
        return modulo + ciclo + "/FOTOGRAFIAS";
    }

    private void esperarDelay(boolean forced) {
        if (forced) return;
        try {
            Log.d(TAG, "Esperando " + DELAY_ENTRE_ACCIONES_MS + " ms antes de la siguiente acción...");
            Thread.sleep(DELAY_ENTRE_ACCIONES_MS);
        } catch (InterruptedException e) {
            Log.w(TAG, "Delay interrumpido");
            Thread.currentThread().interrupt();
        }
    }

    // ===== VERIFICACIÓN DE HORARIO =====

    private boolean isWithinWorkingHours() {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);

        int start = startHour24.get();
        int end = endHour24.get();

        return currentHour >= start && currentHour < end;
    }

    /**
     * Recarga el tamano de lote desde el parametro maxRegEnvio (Realm).
     *
     * Se hace por ciclo y no una sola vez al arrancar, para que un cambio en
     * Configurar_webIp surta efecto sin reiniciar el servicio. Si el parametro
     * no existe o es invalido, CrudParametros devuelve el valor por defecto.
     */
    private void cargarBatchSize() {
        try {
            int valor = CrudParametros.getMaxRegEnvio();
            if (valor != batchLecturas.get()) {
                batchLecturas.set(valor);
                Log.i(TAG, "Lecturas por ciclo: " + valor
                        + " (en requests de hasta " + HTTP_CHUNK_LECTURAS + ")");
            }
        } catch (Exception e) {
            Log.w(TAG, "No se pudo leer maxRegEnvio, se mantiene "
                    + batchLecturas.get() + ": " + e.getMessage());
        }
    }

    // ===== SINCRONIZACIÓN DE LECTURAS =====

    private int sincronizarLecturas() {
        int enviadas = 0;

        try {
            List<EnvioLectura> pendientes = CrudEnvioLectura.obtenerPendientes(batchLecturas.get());

            if (pendientes.isEmpty()) {
                Log.d(TAG, "No hay lecturas pendientes");
                return 0;
            }

            Log.i(TAG, "Sincronizando " + pendientes.size() + " lecturas en lotes de " + HTTP_CHUNK_LECTURAS);
            sendBroadcastProgress("Enviando " + pendientes.size() + " lecturas...", "lecturas", pendientes.size());

            for (int desde = 0; desde < pendientes.size(); desde += HTTP_CHUNK_LECTURAS) {
                int hasta = Math.min(desde + HTTP_CHUNK_LECTURAS, pendientes.size());
                enviadas += enviarLoteLecturas(pendientes.subList(desde, hasta));
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en sincronizarLecturas: " + e.getMessage());
        }

        return enviadas;
    }

    /**
     * Envia un trozo del lote a POST /lecturas/sync-batch y aplica el marcado.
     *
     * CORRELACION
     * El backend recorre las lecturas con un foreach secuencial y hace un append
     * por entrada, sin filtrar ni reordenar: resultados[i] corresponde a
     * chunk[i]. La posicion es el unico vinculo fiable — idRegistro no es un eco
     * (en exito devuelve el id resuelto en BD, no el enviado) y codCuenta puede
     * repetirse dentro del lote cuando una cuenta tiene varios tipos de medidor.
     *
     * VALIDACION ANTES DE MARCAR
     * Se comprueba que la cantidad de resultados coincida y que el codCuenta de
     * cada posicion sea el esperado. Si algo no alinea, NO se marca nada: el
     * chunk completo vuelve a PENDIENTE y se reintenta en el siguiente ciclo.
     * Reenviar es inocuo (el backend responde ALREADY_UPDATED), mientras que
     * marcar un registro con el resultado de otro corrompe el estado en silencio.
     *
     * @return cantidad de lecturas marcadas como enviadas
     */
    private int enviarLoteLecturas(List<EnvioLectura> chunk) {
        if (chunk.isEmpty()) return 0;

        int enviadas = 0;

        try {
            List<LecturaRequest> payload = new ArrayList<>(chunk.size());
            for (EnvioLectura lectura : chunk) {
                LecturaRequest request = LecturaRequest.fromEnvioLectura(lectura);
                // Indica al servidor la tabla destino ("CIC" o "ENT"). Se toma el modulo con
                // que se capturo la fila, no el global: con dos rutas cargadas a la vez (una de
                // lecturas y otra de entregas) el global es el de la ruta abierta en ese momento
                // y mandaba los pendientes de la otra a la carpeta equivocada.
                request.setModuloTrabajo(moduloDe(lectura.getModuloTrabajo()));
                payload.add(request);
            }

            Response<LecturaBatchResponse> response =
                    apiService.enviarLecturas(payload).execute();

            if (!response.isSuccessful() || response.body() == null) {
                String error = "HTTP " + response.code();
                Log.w(TAG, "[lote] Fallo de transporte: " + error);
                reencolarChunk(chunk, error);
                return 0;
            }

            LecturaBatchResponse body = response.body();
            List<LecturaBatchResponse.Item> items = body.getResultados();

            if (items == null || items.size() != chunk.size()) {
                String error = "Respuesta desalineada: esperados " + chunk.size()
                        + ", recibidos " + (items == null ? "null" : items.size());
                Log.e(TAG, "[lote] " + error);
                reencolarChunk(chunk, error);
                return 0;
            }

            // Checksum posicional: la cuenta de cada resultado debe ser la que
            // se envio en esa misma posicion.
            for (int i = 0; i < chunk.size(); i++) {
                String esperada = safeTrim(chunk.get(i).getCuenta());
                String recibida = safeTrim(items.get(i).getCodCuenta());
                if (!esperada.equals(recibida)) {
                    String error = "Desalineacion en posicion " + i
                            + ": esperada " + esperada + ", recibida " + recibida;
                    Log.e(TAG, "[lote] " + error);
                    reencolarChunk(chunk, error);
                    return 0;
                }
            }

            // A partir de aqui la correspondencia esta verificada.
            for (int i = 0; i < chunk.size(); i++) {
                EnvioLectura lectura = chunk.get(i);
                LecturaBatchResponse.Item item = items.get(i);

                if (item.isSuccess()) {
                    // OK, ALREADY_UPDATED y PROTECTED son todos exito: el
                    // servidor tiene el dato y no hay que reintentar.
                    CrudEnvioLectura.marcarEnviado(lectura.getIdRealm(), item.getStatus());
                    enviadas++;
                } else {
                    // Error de negocio: reintentarlo tal cual daria el mismo
                    // resultado. Queda en ERROR a la espera de intervencion.
                    CrudEnvioLectura.marcarError(lectura.getIdRealm(), item.getMessage());
                    Log.w(TAG, "[lote] Rechazada cuenta " + lectura.getCuenta()
                            + ": " + item.getMessage());
                }
            }

            Log.i(TAG, "[lote] " + enviadas + "/" + chunk.size() + " enviadas"
                    + " (servidor: " + body.getProcesados() + " ok, " + body.getErrores() + " err)");

        } catch (Exception e) {
            Log.e(TAG, "[lote] Excepcion: " + e.getMessage());
            reencolarChunk(chunk, e.getMessage());
            return 0;
        }

        return enviadas;
    }

    /**
     * Devuelve el chunk completo a PENDIENTE. Se usa ante fallo de transporte o
     * desalineacion: en ambos casos no se sabe que se proceso, asi que se
     * reintenta todo. marcarError2 mantiene el registro en la cola, a diferencia
     * de marcarError que lo deja en ERROR.
     */
    private void reencolarChunk(List<EnvioLectura> chunk, String motivo) {
        for (EnvioLectura lectura : chunk) {
            CrudEnvioLectura.marcarError2(lectura.getIdRealm(), motivo);
        }
    }

    private static String safeTrim(String v) {
        return v == null ? "" : v.trim();
    }

    /*private LecturaRequest crearLecturaRequest(EnvioLectura lectura) {
        LecturaRequest request = new LecturaRequest();

        request.setId(lectura.getIdRegistro());
        request.setCiclo(lectura.getCiclo());
        request.setMunicipio(lectura.getMunicipio());
        request.setSeccion(lectura.getSeccion());
        request.setDepartamento(lectura.getDepartamento());
        request.setCuenta(lectura.getCuenta());
        request.setNroContador(lectura.getNroContador());
        request.setAnno(lectura.getAnno());
        request.setMes(lectura.getMes());
        request.setLecturaTomada(lectura.getLecturaTomada());
        request.setCausaNoLectura(lectura.getCausaNoLectura());
        request.setCodLector(lectura.getCodLector());
        request.setTerminal(lectura.getTerminal());
        request.setLatitud(lectura.getLatitud());
        request.setLongitud(lectura.getLongitud());
        request.setFechaHoraLectura(lectura.getFechaHoraLectura());
        request.setFechaHoraSatelite(lectura.getFechaHoraSatelite());
        request.setCriticaPda(lectura.getCriticaPda());
        request.setEstadoEnvio(lectura.getEstadoEnvioOriginal());
        request.setInforme(lectura.getInforme());
        request.setComentario(lectura.getComentario());
        request.setLecturaAnterior(lectura.getLecturaAnterior());
        request.setConsumoFacturado(lectura.getConsumoFacturado());
        request.setCodigoSac(lectura.getCodigoSac());
        request.setNombreArchivo(lectura.getNombreArchivo());

        return request;
    }*/

    // ===== SINCRONIZACIÓN DE FOTOS =====

    private int sincronizarFotos() {
        int enviadas = 0;

        try {
            List<EnvioFoto> pendientes = CrudEnvioFoto.obtenerPendientes(BATCH_SIZE_FOTOS);

            if (pendientes.isEmpty()) {
                Log.d(TAG, "No hay fotos pendientes");
                return 0;
            }

            Log.i(TAG, "Sincronizando " + pendientes.size() + " fotos...");
            sendBroadcastProgress("Enviando " + pendientes.size() + " fotos...", "fotos", pendientes.size());

            // Construir ruta destino correcta
            // Formato: CIC202602090/FOTOGRAFIAS o ENT202602090/FOTOGRAFIAS
            // La ruta destino se arma por foto (ver rutaDestinoDe): cada una puede venir de
            // una ruta distinta a la que este abierta ahora.

            for (EnvioFoto foto : pendientes) {
                try {
                    File archivo = new File(foto.getRutaLocal());

                    if (!archivo.exists()) {
                        CrudEnvioFoto.marcarArchivoNoEncontrado(foto.getId());
                        Log.w(TAG, "✗ Archivo no encontrado: " + foto.getRutaLocal());
                        continue;
                    }

                    // Preparar multipart
                    RequestBody requestFile = RequestBody.create(
                            MediaType.parse("image/jpeg"),
                            archivo
                    );

                    MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                            "foto",
                            archivo.getName(),
                            requestFile
                    );

                    // Datos adicionales - INCLUYENDO modulo_trabajo Y ruta_destino
                    RequestBody rbCuenta = RequestBody.create(MediaType.parse("text/plain"), foto.getCodCuenta());
                    RequestBody rbTipo = RequestBody.create(MediaType.parse("text/plain"), foto.getTipoMedidor());
                    RequestBody rbId = RequestBody.create(MediaType.parse("text/plain"), foto.getIdRegistro());
                    RequestBody rbAnno = RequestBody.create(MediaType.parse("text/plain"), foto.getAnno());
                    RequestBody rbMes = RequestBody.create(MediaType.parse("text/plain"), foto.getMes());
                    RequestBody rbCiclo = RequestBody.create(MediaType.parse("text/plain"), foto.getCiclo());
                    RequestBody rbCampo = RequestBody.create(MediaType.parse("text/plain"), foto.getCampoDestino());
                    RequestBody rbModulo = RequestBody.create(MediaType.parse("text/plain"), moduloDe(foto.getModuloTrabajo()));
                    RequestBody rbRutaDest = RequestBody.create(MediaType.parse("text/plain"), rutaDestinoDe(foto));

                    Call<SendFilesResponse> call = apiService.uploadFoto(
                            filePart, rbCuenta, rbTipo, rbId, rbAnno, rbMes, rbCiclo, rbCampo, rbModulo, rbRutaDest
                    );

                    Response<SendFilesResponse> response = call.execute();

                    if (response.isSuccessful() && response.body() != null) {
                        SendFilesResponse body = response.body();
                        if (body.isSuccess()) {
                            CrudEnvioFoto.marcarEnviado(foto.getId(),foto.getErrorEnvio());
                            enviadas++;
                            Log.i(TAG, "✓ Foto enviada: " + archivo.getName());
                        } else {
                            CrudEnvioFoto.marcarError(foto.getId(), body.getMessage());
                            Log.w(TAG, "✗ Error servidor: " + body.getMessage());
                        }
                    } else {
                        String error = "HTTP " + response.code();
                        CrudEnvioFoto.marcarError2(foto.getId(), error);
                        Log.w(TAG, "✗ Error HTTP: " + error);
                    }

                    // Delay entre fotos (1 segundo, son archivos grandes)
                    Thread.sleep(1000);

                } catch (Exception e) {
                    CrudEnvioFoto.marcarError(foto.getId(), e.getMessage());
                    Log.e(TAG, "Error enviando foto: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en sincronizarFotos: " + e.getMessage());
        }

        return enviadas;
    }

    // ===== SINCRONIZACIÓN DE CUENTAS NUEVAS =====

    private int sincronizarCuentasNuevas() {
        int enviadas = 0;

        try {
            List<EnvioCuentaNueva> pendientes = CrudEnvioCuentaNueva.obtenerPendientes(BATCH_SIZE_CUENTAS);

            if (pendientes.isEmpty()) {
                Log.d(TAG, "No hay cuentas nuevas pendientes");
                return 0;
            }

            Log.i(TAG, "Sincronizando " + pendientes.size() + " cuentas nuevas...");
            sendBroadcastProgress("Enviando " + pendientes.size() + " cuentas nuevas...", "cuentas", pendientes.size());

            for (EnvioCuentaNueva cuenta : pendientes) {
                try {
                    Call<LecturaResponse> call = apiService.syncCuentaNueva(cuenta);
                    Response<LecturaResponse> response = call.execute();

                    if (response.isSuccessful() && response.body() != null) {
                        LecturaResponse body = response.body();
                        if (body.getCode() == 200 || body.getCode() == 201) {
                            CrudEnvioCuentaNueva.marcarEnviado(cuenta.getIdRealm());
                            enviadas++;
                            Log.i(TAG, "✓ Cuenta nueva enviada: " + cuenta.getContador());
                        } else {
                            CrudEnvioCuentaNueva.marcarError(cuenta.getIdRealm(), body.getMessage());
                            Log.w(TAG, "✗ Error servidor: " + body.getMessage());
                        }
                    } else {
                        String error = "HTTP " + response.code();
                        CrudEnvioCuentaNueva.marcarError2(cuenta.getIdRealm(), error);
                        Log.w(TAG, "✗ Error HTTP: " + error);
                    }

                    Thread.sleep(500);

                } catch (Exception e) {
                    CrudEnvioCuentaNueva.marcarError(cuenta.getIdRealm(), e.getMessage());
                    Log.e(TAG, "Error enviando cuenta nueva: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en sincronizarCuentasNuevas: " + e.getMessage());
        }

        return enviadas;
    }

    // ===== CARGAR ZONA DE TRABAJO =====

    private void cargarZonaTrabajoDelServidor() {
        try {
            Call<ApiService.ZonaTrabajoResponse> call = apiService.getZonaTrabajo();
            Response<ApiService.ZonaTrabajoResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                ApiService.ZonaTrabajoResponse body = response.body();
                if (body.getCode() == 200 && body.getData() != null) {
                    int horaInicio = body.getData().getHoraInicio();
                    int horaFin = body.getData().getHoraFin();

                    if (horaInicio >= 0 && horaInicio <= 23 && horaFin >= 0 && horaFin <= 23) {
                        startHour24.set(horaInicio);
                        endHour24.set(horaFin);

                        // Guardar en SharedPreferences
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        prefs.edit()
                                .putInt("start_hour", horaInicio)
                                .putInt("end_hour", horaFin)
                                .apply();

                        Log.i(TAG, "Zona de trabajo actualizada: " + horaInicio + ":00 - " + horaFin + ":00");
                        // [FIX] actualizarVentana ya usa startService internamente (no startForegroundService)
                        // Se llama desde hilo principal para mayor seguridad
                        final int hi = horaInicio, hf = horaFin;
                        mainHandler.post(() ->
                                TrackingLocationService.actualizarVentana(LecturaSyncService.this, hi, hf));
                        Log.i(TAG, "Ventana propagada a TrackingLocationService: " +
                                horaInicio + ":00 - " + horaFin + ":00");
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Error cargando zona de trabajo (usando valores locales): " + e.getMessage());
        }
    }

    // ===== ESTADO DEL CHAT (REEMPLAZA LÓGICA DE Reloj3) =====

    /**
     * Actualiza el estado del chat y solicita que MenuDeLiquidacion ejecute chatAsyncall()
     * <p>
     * Esto reemplaza la lógica del Reloj3:
     * if (!estaEnvioAlWsOcupado) {
     * INTERVAL = 120000;
     * procesandoenvioenHilos_chat = true;
     * if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99")) {
     * imagenChat.setImageResource(R.drawable.chat_msg_si);
     * } else {
     * imagenChat.setImageResource(R.drawable.chat_msg_no);
     * }
     * chatAsyncall();
     * }
     */
    private void actualizarEstadoChatYSolicitarLlamada() {
        // Verificar si hay pendientes
        int lecturasPendientes = CrudEnvioLectura.contarPendientes();
        int fotosPendientes = CrudEnvioFoto.contarPendientes();
        int cuentasPendientes = CrudEnvioCuentaNueva.contarPendientes();

        boolean hayPendientes = (lecturasPendientes + fotosPendientes + cuentasPendientes) > 0;
        boolean isBusy = isSyncing.get();

        // Enviar broadcast para actualizar icono del chat
        // El Activity debe verificar si SUSPENDIDO == "99" para determinar el icono
        sendBroadcastChatStatus(!hayPendientes, !isBusy);

        // Solicitar que el Activity ejecute chatAsyncall()
        //Log.e("INFO","SERVICIO OCUPADO: " + isBusy);
        sendBroadcastRequestChatCall();
        /*if (!isBusy) {
        }*/
    }

    // ===== BROADCASTS =====

    /**
     * Notifica que inició la sincronización
     * MenuDeLiquidacion debe: progressBar_cyclic.setVisibility(View.VISIBLE)
     */
    private void sendBroadcastSyncStarted() {
        Intent intent = new Intent(ACTION_SYNC_STARTED);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: SYNC_STARTED");
    }

    /**
     * Notifica que terminó la sincronización
     * MenuDeLiquidacion debe: progressBar_cyclic.setVisibility(View.INVISIBLE)
     */
    private void sendBroadcastSyncFinished() {
        Intent intent = new Intent(ACTION_SYNC_FINISHED);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: SYNC_FINISHED");
    }

    /**
     * Notifica progreso de sincronización
     */
    private void sendBroadcastProgress(String message, String type, int count) {
        Intent intent = new Intent(ACTION_SYNC_PROGRESS);
        intent.putExtra(EXTRA_PROGRESS_MESSAGE, message);
        intent.putExtra(EXTRA_PROGRESS_TYPE, type);
        intent.putExtra(EXTRA_PROGRESS_COUNT, count);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: SYNC_PROGRESS - " + message);
    }

    /**
     * Notifica estado del chat para actualizar icono
     * MenuDeLiquidacion debe:
     * - Si active=true && SUSPENDIDO=="99": imagenChat.setImageResource(R.drawable.chat_msg_si)
     * - Si active=false: imagenChat.setImageResource(R.drawable.chat_msg_no)
     */
    private void sendBroadcastChatStatus(boolean active, boolean canChat) {
        Intent intent = new Intent(ACTION_CHAT_STATUS);
        intent.putExtra(EXTRA_CHAT_ACTIVE, active);
        intent.putExtra(EXTRA_IS_BUSY, !canChat);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: CHAT_STATUS active=" + active + ", canChat=" + canChat);
    }

    /**
     * Solicita que MenuDeLiquidacion ejecute chatAsyncall()
     */
    private void sendBroadcastRequestChatCall() {
        Intent intent = new Intent(ACTION_REQUEST_CHAT_CALL);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: REQUEST_CHAT_CALL");
    }

    private void sendBroadcastLecturasEnviadas(int cantidad) {
        Intent intent = new Intent(ACTION_LECTURAS_ENVIADAS);
        intent.putExtra(EXTRA_CANTIDAD_ENVIADA, cantidad);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: LECTURAS_ENVIADAS = " + cantidad);
    }

    private void sendBroadcastFotosEnviadas(int cantidad) {
        Intent intent = new Intent(ACTION_FOTOS_ENVIADAS);
        intent.putExtra(EXTRA_CANTIDAD_ENVIADA, cantidad);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: FOTOS_ENVIADAS = " + cantidad);
    }

    private void sendBroadcastCuentasEnviadas(int cantidad) {
        Intent intent = new Intent(ACTION_CUENTAS_ENVIADAS);
        intent.putExtra(EXTRA_CANTIDAD_ENVIADA, cantidad);
        sendBroadcast(intent);
        Log.d(TAG, "📡 Broadcast: CUENTAS_ENVIADAS = " + cantidad);
    }
}