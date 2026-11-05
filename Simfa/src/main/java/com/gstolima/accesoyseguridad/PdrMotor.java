package com.gstolima.accesoyseguridad;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Navegación por pasos (PDR, fase 2 del GPS): último recurso cuando no hay GPS
 * ni red. Desde el último fix GPS bueno estima la posición sumando pasos en la
 * dirección de la brújula.
 *
 * - Pasos: acelerómetro (sin permisos; TYPE_STEP_DETECTOR exige que el técnico
 *   acepte ACTIVITY_RECOGNITION).
 * - Rumbo: TYPE_ROTATION_VECTOR + declinación magnética del lugar.
 * - Se calibra solo con el GPS: el largo del paso (se guarda entre jornadas) y el
 *   sesgo de la brújula (se aprende en cada sesión, no se guarda).
 * - Validación en sombra: mientras hay GPS, un segundo estimador NO se reinicia
 *   en cada fix; cada ~40 m compara su posición con el GPS real y anota el error
 *   en files/pdr_validacion.csv. Así se mide la precisión real sin apagar el GPS.
 *
 * Los sensores corren en un hilo propio: el hilo de la pantalla de lectura e
 * impresión no recibe los ~50 eventos/s del acelerómetro. Los métodos públicos
 * están sincronizados y el aviso de cada paso se publica en el hilo principal.
 *
 * Una sola instancia por proceso ({@link #obtener}): si la pantalla de lectura se
 * cierra y se vuelve a abrir, la nueva usa el mismo motor y conserva el ancla. El
 * tiempo con la pantalla cerrada (sin contar pasos) suma error a 1,5 m/s.
 *
 * La estimación vigente se guarda en el teléfono cada ~5 s: si la app se cierra,
 * Android la mata o el teléfono se apaga, al volver a abrir se retoma desde ahí
 * (mismos límites de 5 min y 200 m) con el tiempo sin sensores sumado como error.
 */
public class PdrMotor implements SensorEventListener {

    public static final String PROVEEDOR = "pdr";

    /** Recibe la nueva estimación tras cada paso, en el hilo principal. */
    public interface Oyente {
        void onPaso();
    }

    private static final String TAG = "PdrMotor";
    private static final String PREFS = "pdr_calibracion";
    private static final String PREFS_ESTADO = "pdr_estado";
    private static final long GUARDAR_ESTADO_CADA_MS = 5_000L;

    // Uso operativo de la estimación
    private static final long EDAD_MAX_ANCLA_MS = 5 * 60 * 1000L;
    private static final double DISTANCIA_MAX_M = 200.0;
    private static final float PRECISION_MAX_ANCLA_M = 15f;
    private static final double ERROR_POR_METRO = 0.08;    // error del rumbo y del largo del paso
    // El error también crece con el TIEMPO: cubre movimiento que no se cuenta como
    // pasos (moto, teléfono en el bolso). Conservador hasta tener validación real.
    private static final double ERROR_POR_SEGUNDO = 0.5;
    // Sin NINGÚN paso desde el ancla no se distingue "quieto" de "en vehículo":
    // el error crece como el de una posición vieja (1,5 m/s, igual que en pantalla).
    private static final double ERROR_POR_SEGUNDO_SIN_PASOS = 1.5;
    // Si el acelerómetro deja de entregar eventos (pantalla apagada, pantalla de
    // lectura cerrada) no se cuentan pasos: ese tiempo suma error como una posición
    // vieja (1,5 m/s). Mientras no lleguen eventos, no hay estimación.
    private static final long HUECO_SENSOR_MS = 2_000L;
    private static final double ERROR_POR_SEGUNDO_HUECO = 1.5;
    // Rumbo confiable: teléfono en la mano, pantalla hacia arriba o hacia el técnico,
    // con la parte superior entre 15° bajo y 75° sobre la horizontal y sin girarlo de
    // lado más de 45°. Casi vertical, de cabeza o de lado es bolsillo o bolso.
    private static final double SUPERIOR_MIN = Math.sin(Math.toRadians(-15));
    private static final double SUPERIOR_MAX = Math.sin(Math.toRadians(75));
    private static final double LADO_MAX = Math.sin(Math.toRadians(45));
    // Pasos dudosos seguidos que se cubren con el último rumbo confiable (un
    // movimiento de la mano). Más que esto es bolsillo o bolso.
    private static final int PASOS_DUDOSOS_TOLERADOS = 4;

    // Detección de pasos (magnitud del acelerómetro sin gravedad, filtrada)
    private static final float UMBRAL_PICO = 1.2f;    // m/s²
    private static final float UMBRAL_VALLE = 0.3f;   // m/s²
    private static final long PASO_MIN_MS = 280L;

    // Calibración
    private static final float LARGO_PASO_INICIAL = 0.70f;
    private static final float LARGO_PASO_MIN = 0.35f;
    private static final float LARGO_PASO_MAX = 1.10f;
    private static final int PASOS_MIN_CALIBRACION = 30;
    private static final float VELOCIDAD_CAMINANDO_MIN = 0.9f;   // m/s, para el sesgo de brújula
    private static final float VELOCIDAD_CAMINANDO_MAX = 2.2f;   // más rápido = vehículo: no aprender
    private static final long TRAMO_MAX_MS = 7_000L;

    // Validación en sombra
    private static final double VALIDACION_DISTANCIA_M = 40.0;
    private static final long VALIDACION_MAX_MS = 90_000L;

    private static PdrMotor instancia;

    private final Context contexto;
    private final SensorManager sensores;
    private final Handler principal = new Handler(Looper.getMainLooper());
    // Hilo daemon y nunca se cierra: la instancia vive lo que viva el proceso.
    private final ExecutorService escritor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "PdrValidacion");
        t.setDaemon(true);
        return t;
    });
    private HandlerThread hiloSensores;
    private volatile Oyente oyente;
    private boolean activo = false;
    private int usuarios = 0;

    // Rumbo
    private final float[] matrizRot = new float[9];
    private double azimutRad = Double.NaN;      // hacia donde apunta el teléfono, norte magnético
    private boolean rumboConfiable = false;     // postura de "teléfono en la mano"
    private double rumboConfiableRad = Double.NaN; // último azimut confiable
    private int pasosDudosos = 0;               // seguidos, sin rumbo confiable
    private double declinacionRad = 0;
    private double sesgoRad;                    // corrección aprendida con el GPS
    private double sesgoCos = 0, sesgoSin = 0;  // promedio vectorial del sesgo; arranca vacío

    // Pasos
    private float filtrado = 0f;
    private boolean enPico = false;
    private long ultimoPasoMs = 0L;
    private long ultimoEventoAcelMs = 0L;
    private float largoPaso;

    // Estimación operativa: se reinicia en cada fix GPS bueno
    private Location ancla;
    private long anclaMs;                       // desde cuándo crece el error por tiempo
    private long vencimientoMs;                 // 5 min después del último GPS real
    private double distanciaPrevia;             // caminado antes de una restauración
    private long ultimoGuardadoMs;
    private double lat, lng, distancia;
    private int pasos;
    private double errorHuecosM;                // tiempo sin sensores desde el ancla
    // Pasos sin dirección (bolsillo): la posición no se mueve y el error crece un
    // largo de paso por cada uno, porque pudo ser en cualquier dirección.
    private double errorSinRumboM;
    private int pasosDesdeFix;                  // para calibrar el largo del paso

    // Calibración del largo del paso
    private Location calibUltimoFix;
    private double calibDistancia;
    private int calibPasos;
    private long calibUltimoFixMs;

    // Validación en sombra: NO se reinicia en cada fix
    private Location vAncla;
    private long vInicioMs;
    private double vLat, vLng, vDistancia;
    private int vPasos;

    /** Motor compartido por todas las pantallas de lectura del proceso. */
    public static synchronized PdrMotor obtener(Context contexto) {
        if (instancia == null) instancia = new PdrMotor(contexto);
        return instancia;
    }

    private PdrMotor(Context contexto) {
        this.contexto = contexto.getApplicationContext();
        this.sensores = (SensorManager) this.contexto.getSystemService(Context.SENSOR_SERVICE);
        SharedPreferences p = this.contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        largoPaso = p.getFloat("largo_paso", LARGO_PASO_INICIAL);
        restaurarEstado();
        // El sesgo de la brújula se aprende en cada sesión: depende de cómo se
        // sostiene el teléfono ese día, y guardarlo arrastraría uno equivocado.
        sesgoRad = 0;
    }

    /**
     * Registra una pantalla que usa el motor. El oyente pasa a ser el suyo.
     * @return false si el teléfono no tiene acelerómetro o vector de rotación.
     */
    public synchronized boolean iniciar(Oyente o) {
        if (activo) {
            usuarios++;
            oyente = o;
            return true;
        }
        if (sensores == null) return false;
        Sensor acel = sensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor rot = sensores.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (acel == null || rot == null) {
            Log.w(TAG, "Sin acelerómetro o vector de rotación: PDR desactivado");
            return false;
        }
        hiloSensores = new HandlerThread("PdrSensores");
        hiloSensores.start();
        Handler h = new Handler(hiloSensores.getLooper());
        sensores.registerListener(this, acel, SensorManager.SENSOR_DELAY_GAME, h);
        sensores.registerListener(this, rot, SensorManager.SENSOR_DELAY_UI, h);
        activo = true;
        usuarios = 1;
        oyente = o;
        Log.i(TAG, "PDR activo. Largo de paso " + largoPaso + " m, sesgo "
                + Math.round(Math.toDegrees(sesgoRad)) + "°"
                + (ancla != null ? ", con ancla previa" : ""));
        return true;
    }

    /** Libera el uso de una pantalla; los sensores se detienen con la última. */
    public synchronized void detener(Oyente o) {
        if (!activo) return;
        if (oyente == o) oyente = null; // no quitar el de una pantalla más nueva
        if (--usuarios > 0) return;
        guardarEstado(true);
        try {
            sensores.unregisterListener(this);
        } catch (Exception ignored) {
        }
        oyente = null;
        if (hiloSensores != null) hiloSensores.quitSafely();
        hiloSensores = null;
        guardarCalibracion();
        activo = false;
    }

    // ==================== GPS ====================

    /** Cada fix GPS: ancla, calibración y validación. Llamar desde UsarGPS. */
    public synchronized void onGps(Location fix) {
        if (fix == null || !fix.hasAccuracy() || fix.getAccuracy() > PRECISION_MAX_ANCLA_M) return;

        GeomagneticField campo = new GeomagneticField((float) fix.getLatitude(),
                (float) fix.getLongitude(), (float) fix.getAltitude(), System.currentTimeMillis());
        declinacionRad = Math.toRadians(campo.getDeclination());

        calibrarSesgo(fix);
        calibrarLargoPaso(fix);
        validarEnSombra(fix);

        ancla = fix;
        anclaMs = SystemClock.elapsedRealtime();
        vencimientoMs = anclaMs + EDAD_MAX_ANCLA_MS;
        distanciaPrevia = 0;
        lat = fix.getLatitude();
        lng = fix.getLongitude();
        distancia = 0;
        pasos = 0;
        errorHuecosM = 0;
        errorSinRumboM = 0;
        pasosDudosos = 0;
        guardarEstado(false);
    }

    /**
     * Posición estimada por pasos desde el último fix GPS bueno, o null si:
     * no hay ancla, tiene más de 5 minutos, se caminaron más de 200 m o el
     * acelerómetro no está entregando eventos.
     *
     * Con el teléfono en el bolsillo los pasos se cuentan pero sin dirección: la
     * posición se queda y el error crece un largo de paso por cada uno. Al volver
     * a la mano se sigue sumando con dirección desde ahí.
     *
     * El error crece con la distancia caminada Y con el tiempo: que no se cuenten
     * pasos no prueba que el técnico esté quieto. Los huecos de sensores (pantalla
     * apagada o cerrada) suman 1,5 m por segundo.
     *
     * Hora y antigüedad son las del ancla GPS: el servidor ve cuánto hace que no
     * hay un punto GPS real.
     */
    public synchronized Location estimacion() {
        if (ancla == null) return null;
        // El GPS se perdió en movimiento (bicicleta, moto): las vibraciones se
        // cuentan como pasos y el avance real no se conoce. No se estima.
        if (ancla.hasSpeed() && ancla.getSpeed() > VELOCIDAD_CAMINANDO_MAX) return null;
        long ahora = SystemClock.elapsedRealtime();
        if (ahora - ultimoEventoAcelMs > HUECO_SENSOR_MS) return null;
        if (ahora > vencimientoMs || distanciaPrevia + distancia > DISTANCIA_MAX_M) return null;
        double error = errorActual(ahora);
        Location l = new Location(PROVEEDOR);
        l.setLatitude(lat);
        l.setLongitude(lng);
        if (ancla.hasAltitude()) l.setAltitude(ancla.getAltitude());
        l.setAccuracy((float) error);
        l.setTime(ancla.getTime());
        l.setElapsedRealtimeNanos(ancla.getElapsedRealtimeNanos());
        return l;
    }

    private double errorActual(long ahora) {
        long edad = ahora - anclaMs;
        return ancla.getAccuracy()
                + Math.max(ERROR_POR_METRO * distancia,
                           (pasos > 0 ? ERROR_POR_SEGUNDO : ERROR_POR_SEGUNDO_SIN_PASOS) * (edad / 1000.0))
                + (pasos > 0 ? 2.0 : 0.0)
                + errorHuecosM
                + errorSinRumboM;
    }

    // ==================== Estado entre reinicios ====================

    /**
     * Guarda la estimación vigente (llamar con el monitor tomado). Hora de pared:
     * el reloj automático es obligatorio en la app, y elapsedRealtime se reinicia
     * al apagar el teléfono. apply() escribe en segundo plano.
     */
    private void guardarEstado(boolean forzar) {
        if (ancla == null) return;
        long ahora = SystemClock.elapsedRealtime();
        if (!forzar && ahora - ultimoGuardadoMs < GUARDAR_ESTADO_CADA_MS) return;
        if (ahora > vencimientoMs) return;
        ultimoGuardadoMs = ahora;
        long pared = System.currentTimeMillis();
        try {
            contexto.getSharedPreferences(PREFS_ESTADO, Context.MODE_PRIVATE).edit()
                    .putLong("lat", Double.doubleToRawLongBits(lat))
                    .putLong("lng", Double.doubleToRawLongBits(lng))
                    .putFloat("error", (float) errorActual(ahora))
                    .putFloat("distancia", (float) (distanciaPrevia + distancia))
                    .putLong("guardado", pared)
                    .putLong("vence", pared + (vencimientoMs - ahora))
                    .putLong("hora_fix", ancla.getTime())
                    .putLong("edad_fix_ms", ahora - ancla.getElapsedRealtimeNanos() / 1_000_000L)
                    .putFloat("altitud", ancla.hasAltitude() ? (float) ancla.getAltitude() : Float.NaN)
                    .putFloat("velocidad", ancla.hasSpeed() ? ancla.getSpeed() : Float.NaN)
                    .putFloat("declinacion", (float) declinacionRad)
                    .apply();
        } catch (Exception ignored) {
        }
    }

    /** Retoma la estimación guardada si sigue vigente (5 min desde el último GPS). */
    private void restaurarEstado() {
        try {
            SharedPreferences p = contexto.getSharedPreferences(PREFS_ESTADO, Context.MODE_PRIVATE);
            if (!p.contains("guardado")) return;
            long pared = System.currentTimeMillis();
            long guardado = p.getLong("guardado", 0L);
            long vence = p.getLong("vence", 0L);
            long hueco = pared - guardado;
            if (hueco < 0 || pared >= vence) return; // reloj atrasado o ya vencida
            long ahora = SystemClock.elapsedRealtime();
            Location l = new Location(PROVEEDOR);
            l.setLatitude(Double.longBitsToDouble(p.getLong("lat", 0L)));
            l.setLongitude(Double.longBitsToDouble(p.getLong("lng", 0L)));
            // Sin sensores durante el hueco: se suma como una posición vieja.
            l.setAccuracy((float) (p.getFloat("error", 0f) + ERROR_POR_SEGUNDO_HUECO * hueco / 1000.0));
            l.setTime(p.getLong("hora_fix", pared));
            long edadFix = p.getLong("edad_fix_ms", 0L) + hueco;
            l.setElapsedRealtimeNanos(Math.max(0L, ahora - edadFix) * 1_000_000L);
            float alt = p.getFloat("altitud", Float.NaN);
            if (!Float.isNaN(alt)) l.setAltitude(alt);
            float vel = p.getFloat("velocidad", Float.NaN);
            if (!Float.isNaN(vel)) l.setSpeed(vel);
            ancla = l;
            anclaMs = ahora;
            vencimientoMs = ahora + (vence - pared);
            distanciaPrevia = p.getFloat("distancia", 0f);
            lat = l.getLatitude();
            lng = l.getLongitude();
            distancia = 0;
            pasos = 0;
            declinacionRad = p.getFloat("declinacion", 0f);
            Log.i(TAG, "Estimación retomada: error " + Math.round(l.getAccuracy())
                    + " m, " + (hueco / 1000) + " s sin sensores");
        } catch (Exception ex) {
            Log.w(TAG, "No se pudo retomar la estimación: " + ex.getMessage());
        }
    }

    // ==================== Sensores (hilo PdrSensores) ====================

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized (this) {
                SensorManager.getRotationMatrixFromVector(matrizRot, e.values);
                actualizarRumbo();
            }
            return;
        }
        if (e.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        long tEvento = SystemClock.elapsedRealtime();
        synchronized (this) {
            if (ultimoEventoAcelMs > 0 && tEvento - ultimoEventoAcelMs > HUECO_SENSOR_MS) {
                // Sin pasos contados en el hueco: se cubre con error, como una posición vieja.
                errorHuecosM += ERROR_POR_SEGUNDO_HUECO * (tEvento - ultimoEventoAcelMs) / 1000.0;
                vAncla = null; // la validación en sombra no puede medir ese tramo
            }
            ultimoEventoAcelMs = tEvento;
        }
        float x = e.values[0], y = e.values[1], z = e.values[2];
        float magnitud = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
        filtrado = 0.8f * filtrado + 0.2f * magnitud;
        long ahora = SystemClock.elapsedRealtime();

        if (!enPico && filtrado > UMBRAL_PICO) {
            enPico = true;
        } else if (enPico && filtrado < UMBRAL_VALLE) {
            enPico = false;
            // Menos de 280 ms desde el anterior es rebote del mismo paso, no otro.
            if (ahora - ultimoPasoMs >= PASO_MIN_MS) {
                ultimoPasoMs = ahora;
                registrarPaso();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // El estado de calibración del magnetómetro no se usa: el sesgo se aprende con el GPS.
    }

    /**
     * Rumbo de marcha a partir de la matriz de rotación (dispositivo → Este, Norte,
     * Arriba). Se usa la proyección horizontal de (eje superior − eje de la
     * pantalla): apunta hacia delante con el teléfono plano, inclinado o casi
     * vertical frente al técnico. El azimut de getOrientation se vuelve inestable
     * por encima de ~60° de inclinación, que es como muchos sostienen el teléfono
     * al caminar.
     */
    private void actualizarRumbo() {
        double superior = matrizRot[7];  // componente vertical del eje superior (Y)
        double lado = matrizRot[6];      // componente vertical del eje lateral (X)
        double pantalla = matrizRot[8];  // componente vertical del eje de la pantalla (Z)
        double este = matrizRot[1] - matrizRot[2];
        double norte = matrizRot[4] - matrizRot[5];
        if (Math.hypot(este, norte) < 0.5) {
            rumboConfiable = false;
            return;
        }
        azimutRad = Math.atan2(este, norte);
        rumboConfiable = superior >= SUPERIOR_MIN && superior <= SUPERIOR_MAX
                && Math.abs(lado) <= LADO_MAX && pantalla > 0;
        if (rumboConfiable) rumboConfiableRad = azimutRad;
    }

    private void registrarPaso() {
        synchronized (this) {
            if (Double.isNaN(azimutRad)) return;
            pasosDesdeFix++;
            if (!rumboConfiable) pasosDudosos++;
            else pasosDudosos = 0;
            boolean conRumbo = rumboConfiable
                    || (pasosDudosos <= PASOS_DUDOSOS_TOLERADOS && !Double.isNaN(rumboConfiableRad));
            if (!rumboConfiable) {
                // Gesto breve o bolsillo: este paso no tiene dirección medida. Los
                // tolerados se suman con el último rumbo confiable, pero igual suman
                // error por si resultan ser el comienzo de un tramo en el bolsillo.
                if (ancla != null) errorSinRumboM += largoPaso;
                vAncla = null; // la validación en sombra solo mide tramos con dirección
            }
            if (ancla != null) {
                distancia += largoPaso;
                pasos++;
            }
            if (conRumbo) {
                double azimut = rumboConfiable ? azimutRad : rumboConfiableRad;
                double rumbo = azimut + declinacionRad + sesgoRad;
                double dN = largoPaso * Math.cos(rumbo);
                double dE = largoPaso * Math.sin(rumbo);
                if (ancla != null) {
                    lat += dN / 111_320d;
                    lng += dE / (111_320d * Math.cos(Math.toRadians(lat)));
                }
                if (vAncla != null) {
                    vLat += dN / 111_320d;
                    vLng += dE / (111_320d * Math.cos(Math.toRadians(vLat)));
                    vDistancia += largoPaso;
                    vPasos++;
                }
            }
        }
        synchronized (this) {
            guardarEstado(false);
        }
        final Oyente o = oyente;
        if (o != null) principal.post(o::onPaso);
    }

    // ==================== Calibración ====================

    /** Rumbo GPS (Doppler) vs brújula mientras se camina con buena señal. */
    private void calibrarSesgo(Location fix) {
        if (Double.isNaN(azimutRad) || !fix.hasBearing() || !fix.hasSpeed()) return;
        // Solo a paso de caminata: en vehículo se aprendería el ángulo del soporte.
        if (fix.getSpeed() < VELOCIDAD_CAMINANDO_MIN || fix.getSpeed() > VELOCIDAD_CAMINANDO_MAX) return;
        if (!rumboConfiable) return;
        if (fix.hasBearingAccuracy() && fix.getBearingAccuracyDegrees() > 20f) return;
        double brujula = azimutRad + declinacionRad;
        double delta = normalizar(Math.toRadians(fix.getBearing()) - brujula);
        if (Math.abs(delta) > Math.toRadians(60)) return; // atípico: giro o perturbación magnética
        sesgoCos = 0.9 * sesgoCos + 0.1 * Math.cos(delta);
        sesgoSin = 0.9 * sesgoSin + 0.1 * Math.sin(delta);
        // Solo se aplica cuando las muestras coinciden (vector promedio largo).
        if (Math.hypot(sesgoCos, sesgoSin) >= 0.3) {
            sesgoRad = Math.atan2(sesgoSin, sesgoCos);
        }
    }

    /**
     * Largo del paso = metros GPS / pasos, sumando SOLO tramos entre dos fixes
     * consecutivos cercanos en el tiempo y caminando: así pasos y metros salen
     * del mismo tramo (antes se contaban pasos de tramos cuyos metros no se sumaban).
     */
    private void calibrarLargoPaso(Location fix) {
        long ahora = SystemClock.elapsedRealtime();
        if (calibUltimoFix != null
                && ahora - calibUltimoFixMs <= TRAMO_MAX_MS
                && fix.hasSpeed() && fix.getSpeed() >= 0.7f && fix.getSpeed() <= VELOCIDAD_CAMINANDO_MAX
                && calibUltimoFix.hasSpeed() && calibUltimoFix.getSpeed() >= 0.7f) {
            double d = calibUltimoFix.distanceTo(fix);
            if (d >= 2.0 && pasosDesdeFix > 0) {
                calibDistancia += d;
                calibPasos += pasosDesdeFix;
            }
        }
        calibUltimoFix = fix;
        calibUltimoFixMs = ahora;
        pasosDesdeFix = 0;
        if (calibPasos >= PASOS_MIN_CALIBRACION) {
            double medido = calibDistancia / calibPasos;
            if (medido >= LARGO_PASO_MIN && medido <= LARGO_PASO_MAX) {
                largoPaso = (float) (0.8 * largoPaso + 0.2 * medido);
            }
            calibDistancia = 0;
            calibPasos = 0;
        }
    }

    private void guardarCalibracion() {
        try {
            contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                    .putFloat("largo_paso", largoPaso)
                    .apply();
        } catch (Exception ignored) {
        }
    }

    // ==================== Validación en sombra ====================

    private void validarEnSombra(Location fix) {
        long ahora = SystemClock.elapsedRealtime();
        if (vAncla == null) {
            reiniciarSombra(fix, ahora);
            return;
        }
        long duracion = ahora - vInicioMs;
        if (vDistancia >= VALIDACION_DISTANCIA_M || duracion >= VALIDACION_MAX_MS) {
            // Solo tramos a pie: en bicicleta o moto la comparación no mide el PDR.
            double velocidadReal = vAncla.distanceTo(fix) / Math.max(1.0, duracion / 1000.0);
            boolean aPie = velocidadReal <= VELOCIDAD_CAMINANDO_MAX
                    && (!fix.hasSpeed() || fix.getSpeed() <= VELOCIDAD_CAMINANDO_MAX)
                    && (!vAncla.hasSpeed() || vAncla.getSpeed() <= VELOCIDAD_CAMINANDO_MAX);
            if (vDistancia >= 10.0 && aPie) {
                float[] r = new float[1];
                Location.distanceBetween(vLat, vLng, fix.getLatitude(), fix.getLongitude(), r);
                anotar(vDistancia, vPasos, r[0], fix.getAccuracy(), duracion);
                guardarCalibracion();
            }
            reiniciarSombra(fix, ahora);
        }
    }

    private void reiniciarSombra(Location fix, long ahora) {
        vAncla = fix;
        vInicioMs = ahora;
        vLat = fix.getLatitude();
        vLng = fix.getLongitude();
        vDistancia = 0;
        vPasos = 0;
    }

    private void anotar(double dist, int nPasos, float errorM, float precisionGps, long duracionMs) {
        final String linea = String.format(Locale.US, "%s;%.1f;%d;%.1f;%.1f;%.1f;%.2f;%.0f;%d%n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()),
                dist, nPasos, errorM, 100.0 * errorM / dist, precisionGps,
                largoPaso, Math.toDegrees(sesgoRad), duracionMs / 1000);
        Log.i(TAG, "[validación] caminado " + Math.round(dist) + " m, error " + Math.round(errorM) + " m");
        try {
            escritor.execute(() -> {
                File dir = contexto.getExternalFilesDir(null);
                if (dir == null) return;
                File f = new File(dir, "pdr_validacion.csv");
                boolean nuevo = !f.exists();
                try (FileWriter w = new FileWriter(f, true)) {
                    if (nuevo) {
                        w.write("fecha;distancia_m;pasos;error_m;error_pct;precision_gps_m;largo_paso_m;sesgo_grados;duracion_s\n");
                    }
                    w.write(linea);
                } catch (Exception ex) {
                    Log.w(TAG, "No se pudo escribir la validación: " + ex.getMessage());
                }
            });
        } catch (Exception ignored) {
            // Executor cerrado (pantalla destruyéndose): se pierde solo esta línea.
        }
    }

    private static double normalizar(double a) {
        while (a > Math.PI) a -= 2 * Math.PI;
        while (a < -Math.PI) a += 2 * Math.PI;
        return a;
    }
}
