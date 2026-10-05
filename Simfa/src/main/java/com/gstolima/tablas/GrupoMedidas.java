package com.gstolima.tablas;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cuentas que traen MAS DE UNA MEDIDA en la misma ruta.
 *
 * El caso: una cuenta puede venir con medida Activa y Reactiva (campo NROMEDIDORES, que pese al
 * nombre no es un conteo sino una letra: "A" o "R"), y mas raro, con dos Activas de dos
 * medidores fisicos distintos. Hoy cada registro se cierra e imprime por separado, asi que al
 * cliente le quedan dos tirillas sueltas de la misma cuenta.
 *
 * POR QUE SE AGRUPA POR CUENTA Y NO POR POSICION:
 * En los planos revisados las medidas de una cuenta venian pegadas (registros n y n+1), pero eso
 * es como las arma el generador hoy, no un contrato. Y el ORDEN TAMPOCO es fijo: en el mismo
 * archivo aparecen casos R->A y casos A->R. Apoyarse en la posicion o en el orden romperia con
 * cualquier plano que se genere distinto, asi que el unico criterio es el dato: la cuenta.
 *
 * QUE SE CACHEA Y QUE NO:
 * El indice cuenta -> registros no cambia durante la jornada, asi que se arma una vez y se
 * revalida por tamano y fecha del archivo. El ESTADO de cada registro (campo LEIDO) cambia a
 * cada rato, asi que ese se lee del disco cada vez que se pregunta.
 */
public final class GrupoMedidas {

    private static final String TAG = "GrupoMedidas";

    /** Posiciones dentro del registro, iguales a las de TablaRegistroSalida. */
    private static final int OFF_CUENTA = 133, LEN_CUENTA = 10;
    private static final int OFF_LEIDO = 746, LEN_LEIDO = 1;

    private static final int BUFFER = 64 * 1024;

    private static GrupoMedidas cache;

    private final String archivo;
    private final long tamanoArchivo;
    private final long fechaArchivo;
    private final Map<String, int[]> porCuenta;

    private GrupoMedidas(File f, Map<String, int[]> porCuenta) {
        this.archivo = f.getAbsolutePath();
        this.tamanoArchivo = f.length();
        this.fechaArchivo = f.lastModified();
        this.porCuenta = porCuenta;
    }

    // ------------------------------------------------------------------ Construccion

    /**
     * Indice de la ruta indicada, reutilizando el cacheado si el archivo no cambio.
     * Lee el archivo completo: llamarlo fuera del hilo principal la primera vez.
     */
    public static synchronized GrupoMedidas obtener(String rutaArchivo) throws IOException {
        File f = new File(rutaArchivo);
        if (!f.exists()) {
            throw new IOException("No existe el archivo de ruta: " + rutaArchivo);
        }
        if (cache != null
                && cache.archivo.equals(f.getAbsolutePath())
                && cache.tamanoArchivo == f.length()
                && cache.fechaArchivo == f.lastModified()) {
            return cache;
        }
        cache = construir(f);
        return cache;
    }

    public static synchronized void limpiarCache() {
        cache = null;
    }

    private static GrupoMedidas construir(File f) throws IOException {
        long inicio = System.currentTimeMillis();
        int longitud = TablaRegistroSalida.getLongitudRegistro();
        int total = (int) (f.length() / longitud);

        Map<String, List<Integer>> acumulado = new HashMap<>();
        byte[] registro = new byte[longitud];

        try (InputStream in = new BufferedInputStream(new FileInputStream(f), BUFFER)) {
            for (int i = 0; i < total; i++) {
                if (!leerCompleto(in, registro)) {
                    Log.w(TAG, "Archivo truncado en el registro " + (i + 1));
                    break;
                }
                String cuenta = new String(registro, OFF_CUENTA, LEN_CUENTA,
                        StandardCharsets.ISO_8859_1).trim();
                if (cuenta.isEmpty()) {
                    continue;
                }
                List<Integer> lista = acumulado.get(cuenta);
                if (lista == null) {
                    lista = new ArrayList<>(2);
                    acumulado.put(cuenta, lista);
                }
                lista.add(i + 1); // los registros son base 1
            }
        }

        // Solo se conservan las cuentas con mas de una medida: son las unicas que cambian algo,
        // y asi el mapa queda minusculo (en las rutas revisadas, 4 cuentas de 118).
        Map<String, int[]> grupos = new HashMap<>();
        for (Map.Entry<String, List<Integer>> e : acumulado.entrySet()) {
            List<Integer> lista = e.getValue();
            if (lista.size() < 2) {
                continue;
            }
            int[] arreglo = new int[lista.size()];
            for (int i = 0; i < arreglo.length; i++) {
                arreglo[i] = lista.get(i);
            }
            grupos.put(e.getKey(), arreglo);
        }

        Log.i(TAG, "Indice de medidas: " + total + " registros, " + grupos.size()
                + " cuenta(s) con mas de una medida, en " + (System.currentTimeMillis() - inicio) + " ms");
        return new GrupoMedidas(f, grupos);
    }

    private static boolean leerCompleto(InputStream in, byte[] destino) throws IOException {
        int leidos = 0;
        while (leidos < destino.length) {
            int n = in.read(destino, leidos, destino.length - leidos);
            if (n < 0) {
                return false;
            }
            leidos += n;
        }
        return true;
    }

    // ------------------------------------------------------------------ Consultas

    /** true si esta cuenta trae mas de una medida. */
    public boolean esGrupo(String cuenta) {
        return cuenta != null && porCuenta.containsKey(cuenta.trim());
    }

    /**
     * Registros de la cuenta, base 1, EN EL ORDEN EN QUE VIENEN EN EL PLANO.
     * Devuelve null si la cuenta tiene una sola medida.
     */
    public int[] registrosDe(String cuenta) {
        return cuenta == null ? null : porCuenta.get(cuenta.trim());
    }

    /**
     * Primer registro del grupo que todavia no se ha cerrado, o 0 si ya estan todos.
     * Lee el estado del disco, no de memoria: cambia con cada cierre.
     */
    public int primerPendiente(String cuenta) {
        int[] estado = revisarGrupo(cuenta);
        return estado[0];
    }

    /**
     * Igual que primerPendiente(), pero devuelve 0 mientras la cuenta no se haya EMPEZADO.
     *
     * La diferencia importa para la navegacion. La regla de negocio es "si ya inicio la cuenta
     * tiene que terminarla": mientras no haya cerrado ninguna medida el operario todavia puede
     * saltarse la cuenta entera, y no hay por que amarrarlo. En cambio apenas cierra una, la
     * otra se vuelve obligatoria.
     *
     * Usar este metodo para decidir si se bloquea o se redirige al operario, y primerPendiente()
     * solo para saber si ya se puede imprimir.
     */
    public int pendienteSiYaInicio(String cuenta) {
        int[] estado = revisarGrupo(cuenta);
        return estado[1] == 1 ? estado[0] : 0;
    }

    /** true cuando TODAS las medidas de la cuenta ya quedaron cerradas. */
    public boolean grupoCompleto(String cuenta) {
        return primerPendiente(cuenta) == 0;
    }

    /**
     * Una sola pasada por el archivo para las dos preguntas que se hacen siempre juntas.
     *
     * @return [primerPendiente (0 si no hay), algunaCerrada (1/0)]
     */
    private int[] revisarGrupo(String cuenta) {
        int[] registros = registrosDe(cuenta);
        if (registros == null) {
            return new int[]{0, 0};
        }
        int primerPendiente = 0;
        int algunaCerrada = 0;
        int longitud = TablaRegistroSalida.getLongitudRegistro();

        try (RandomAccessFile r = new RandomAccessFile(archivo, "r")) {
            byte[] buffer = new byte[LEN_LEIDO];
            for (int registro : registros) {
                long posicion = (long) (registro - 1) * longitud + OFF_LEIDO;
                if (posicion + LEN_LEIDO > r.length()) {
                    continue;
                }
                r.seek(posicion);
                r.readFully(buffer);
                if (cerrado(new String(buffer, StandardCharsets.ISO_8859_1))) {
                    algunaCerrada = 1;
                } else if (primerPendiente == 0) {
                    primerPendiente = registro;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "No se pudo revisar el estado del grupo de la cuenta " + cuenta, e);
            return new int[]{0, 0}; // ante la duda se comporta como hoy: no bloquea ni aplaza
        }
        return new int[]{primerPendiente, algunaCerrada};
    }

    /**
     * Mismo criterio que validarEstadoRegistro(): LEIDO vacio o "0" es pendiente, cualquier
     * otro valor ("1", "3", "5", "9"...) significa que el registro ya se cerro, sea con lectura
     * o con causa de no lectura.
     */
    private static boolean cerrado(String leido) {
        String v = leido == null ? "" : leido.trim();
        return !v.isEmpty() && !v.equals("0");
    }
}