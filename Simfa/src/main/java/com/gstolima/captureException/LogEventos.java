package com.gstolima.captureException;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Punto unico de escritura a LOGEVENTOS.LOG.
 *
 * Resuelve la ruta aunque VariablesGlobales.directorioactual aun no este inicializado
 * (proceso recien recreado), crea la carpeta si no existe y nunca lanza excepciones.
 */
public final class LogEventos {

    private static final String TAG = "LogEventos";
    private static final String ARCHIVO = "LOGEVENTOS.LOG";
    private static final String ANDROID_DATA = "/Android/data/";
    private static final String PREFS = "LogEventosPrefs";
    private static final String KEY_ULTIMA_SALIDA = "ultima_salida_ts";
    private static final int MAX_LINEAS_TRAZA = 150;
    private static final Object LOCK = new Object();

    private LogEventos() {
    }

    public static void registrarError(Context context, String etapa, Throwable error) {
        StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        escribir(context, "[ERROR] " + etapa + "\n" + sw);
    }

    public static void escribir(Context context, String texto) {
        String entrada = "Fecha: " + fechaActual() + "\n"
                + "Equipo: " + Build.MANUFACTURER + " " + Build.MODEL + " | Android " + Build.VERSION.RELEASE
                + " (API " + Build.VERSION.SDK_INT + ")\n"
                + texto + "\n\n";
        Log.e(TAG, entrada);

        synchronized (LOCK) {
            File archivo = resolverArchivo(context);
            if (archivo == null) {
                return;
            }
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
                Log.e(TAG, "No se pudo crear " + carpeta.getAbsolutePath());
                return;
            }
            try (Writer w = new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8)) {
                w.write(entrada);
            } catch (Exception e) {
                Log.e(TAG, "No se pudo escribir " + archivo.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Registra por que murio el proceso en ejecuciones anteriores (crash nativo, ANR,
     * cierre por memoria, etc.). Estos casos NO pasan por UncaughtExceptionHandler,
     * por eso antes no quedaba nada en LOGEVENTOS. Llamar una vez al iniciar la app,
     * fuera del hilo principal.
     */
    public static void registrarSalidasPrevias(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                return;
            }
            SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            long ultimaRegistrada = prefs.getLong(KEY_ULTIMA_SALIDA, 0L);
            long masReciente = ultimaRegistrada;

            List<ApplicationExitInfo> salidas = am.getHistoricalProcessExitReasons(context.getPackageName(), 0, 10);
            for (ApplicationExitInfo info : salidas) {
                if (info.getTimestamp() <= ultimaRegistrada) {
                    continue;
                }
                masReciente = Math.max(masReciente, info.getTimestamp());
                if (!esSalidaAnormal(info.getReason())) {
                    continue;
                }
                escribir(context, "[SALIDA ANORMAL DEL PROCESO]\n"
                        + "Ocurrio: " + formatear(info.getTimestamp()) + "\n"
                        + "Motivo: " + nombreMotivo(info.getReason()) + " (" + info.getReason() + ")\n"
                        + "Estado: " + info.getStatus() + " | Importancia: " + info.getImportance() + "\n"
                        + "PSS: " + info.getPss() + " KB | RSS: " + info.getRss() + " KB\n"
                        + "Descripcion: " + info.getDescription()
                        + leerTraza(info));
            }
            if (masReciente > ultimaRegistrada) {
                prefs.edit().putLong(KEY_ULTIMA_SALIDA, masReciente).apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "registrarSalidasPrevias() fallo", e);
        }
    }

    private static boolean esSalidaAnormal(int motivo) {
        switch (motivo) {
            case ApplicationExitInfo.REASON_CRASH:
            case ApplicationExitInfo.REASON_CRASH_NATIVE:
            case ApplicationExitInfo.REASON_ANR:
            case ApplicationExitInfo.REASON_LOW_MEMORY:
            case ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE:
            case ApplicationExitInfo.REASON_INITIALIZATION_FAILURE:
            case ApplicationExitInfo.REASON_SIGNALED:
            case ApplicationExitInfo.REASON_DEPENDENCY_DIED:
            case ApplicationExitInfo.REASON_OTHER:
                return true;
            default:
                return false;
        }
    }

    private static String nombreMotivo(int motivo) {
        switch (motivo) {
            case ApplicationExitInfo.REASON_CRASH: return "CRASH_JAVA";
            case ApplicationExitInfo.REASON_CRASH_NATIVE: return "CRASH_NATIVO";
            case ApplicationExitInfo.REASON_ANR: return "ANR (app no responde)";
            case ApplicationExitInfo.REASON_LOW_MEMORY: return "SIN_MEMORIA";
            case ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE: return "USO_EXCESIVO_RECURSOS";
            case ApplicationExitInfo.REASON_INITIALIZATION_FAILURE: return "FALLO_INICIALIZACION";
            case ApplicationExitInfo.REASON_SIGNALED: return "SENAL_DEL_SISTEMA";
            case ApplicationExitInfo.REASON_DEPENDENCY_DIED: return "DEPENDENCIA_MURIO";
            default: return "OTRO";
        }
    }

    /** Solo la traza de ANR es texto legible; la de crash nativo es un protobuf binario. */
    private static String leerTraza(ApplicationExitInfo info) {
        if (info.getReason() != ApplicationExitInfo.REASON_ANR) {
            return "";
        }
        try (InputStream in = info.getTraceInputStream()) {
            if (in == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder("\nTraza ANR:\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String linea;
            int n = 0;
            while ((linea = reader.readLine()) != null && n++ < MAX_LINEAS_TRAZA) {
                sb.append(linea).append('\n');
            }
            return sb.toString();
        } catch (Exception e) {
            return "\n(no se pudo leer la traza: " + e.getMessage() + ")";
        }
    }

    private static File resolverArchivo(Context context) {
        String base = VariablesGlobales.directorioactual;
        if ((base == null || base.trim().isEmpty() || "null".equals(base)) && context != null) {
            File externo = context.getExternalFilesDir(null);
            if (externo != null) {
                base = externo.getAbsolutePath();
                int corte = base.indexOf(ANDROID_DATA);
                if (corte > 0) {
                    base = base.substring(0, corte);
                }
            }
        }
        if (base == null || base.trim().isEmpty()) {
            return null;
        }
        return new File(base + VariablesGlobales.getCarpetaLecturas(), ARCHIVO);
    }

    private static String fechaActual() {
        return formatear(System.currentTimeMillis());
    }

    private static String formatear(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(millis));
    }
}