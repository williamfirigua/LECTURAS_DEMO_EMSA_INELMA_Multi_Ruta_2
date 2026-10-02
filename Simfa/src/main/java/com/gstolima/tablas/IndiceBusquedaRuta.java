package com.gstolima.tablas;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;

/**
 * Indice en memoria de los campos buscables de un archivo de ruta (D+NombreArchivos).
 *
 * Por que: la busqueda recorria el archivo con un seek + readFully POR REGISTRO y armaba
 * varios String de cada uno, en el hilo principal. Con eso no es viable filtrar en cada tecla.
 * Aqui el archivo se lee UNA sola vez, de corrido y con buffer, y se conservan solo los campos
 * que se buscan, ya normalizados. Filtrar pasa a ser un recorrido en memoria (~1 ms con 2000
 * registros), y el archivo original no se toca.
 *
 * Costo aproximado con 2000 registros (caso extremo en campo): ~1 MB de memoria y 30-60 ms de
 * carga. El indice se cachea y se revalida por tamano y fecha del archivo, de modo que si a la
 * cuadrilla le recargan la ruta, se reconstruye solo.
 */
public final class IndiceBusquedaRuta {

    private static final String TAG = "IndiceBusquedaRuta";

    // Mismos campos que usa la busqueda actual. Coinciden con 'opcion' de ModuloBusquedaCuenta.
    public static final int CAMPO_MEDIDOR = 1;
    public static final int CAMPO_CUENTA = 2;
    public static final int CAMPO_RUTA = 3;
    public static final int CAMPO_DIRECCION = 4;
    public static final int CAMPO_NOMBRE = 5;

    // Posiciones dentro del registro, iguales a las de TablaRegistroSalida.
    private static final int OFF_SUSPENDIDO = 0, LEN_SUSPENDIDO = 2;
    private static final int OFF_RUTA = 119, LEN_RUTA = 13;
    private static final int OFF_CUENTA = 133, LEN_CUENTA = 10;
    private static final int OFF_NOMBRE = 182, LEN_NOMBRE = 48;
    private static final int OFF_DIRECCION = 231, LEN_DIRECCION = 64;
    private static final int OFF_MEDIDOR = 300, LEN_MEDIDOR = 20;

    private static final int BUFFER = 64 * 1024;

    private static IndiceBusquedaRuta cache;

    private final String archivo;
    private final long tamanoArchivo;
    private final long fechaArchivo;

    private final String[] cuenta;
    private final String[] medidor;
    private final String[] ruta;
    private final String[] nombre;
    private final String[] direccion;
    private final String[] suspendido;
    // Version normalizada (mayusculas, sin tildes) de cada campo: se compara contra esta.
    private final String[] cuentaN;
    private final String[] medidorN;
    private final String[] rutaN;
    private final String[] nombreN;
    private final String[] direccionN;

    private final int total;

    // Reduccion incremental: al agregar caracteres se filtra sobre las coincidencias previas.
    private String ultimaConsulta = "";
    private int ultimoCampo = 0;
    private int[] ultimasCoincidencias = new int[0];

    private IndiceBusquedaRuta(File f, int total) {
        this.archivo = f.getAbsolutePath();
        this.tamanoArchivo = f.length();
        this.fechaArchivo = f.lastModified();
        this.total = total;
        cuenta = new String[total];
        medidor = new String[total];
        ruta = new String[total];
        nombre = new String[total];
        direccion = new String[total];
        suspendido = new String[total];
        cuentaN = new String[total];
        medidorN = new String[total];
        rutaN = new String[total];
        nombreN = new String[total];
        direccionN = new String[total];
    }

    /**
     * Devuelve el indice del archivo indicado, reutilizando el cacheado si el archivo no cambio.
     * Es una operacion de disco: llamarla fuera del hilo principal.
     */
    public static synchronized IndiceBusquedaRuta obtener(String rutaArchivo) throws IOException {
        File f = new File(rutaArchivo);
        if (!f.exists()) {
            throw new IOException("No existe el archivo de ruta: " + rutaArchivo);
        }
        if (cache != null && cache.vigentePara(f)) {
            return cache;
        }
        cache = construir(f);
        return cache;
    }

    public static synchronized void limpiarCache() {
        cache = null;
    }

    private boolean vigentePara(File f) {
        return archivo.equals(f.getAbsolutePath())
                && tamanoArchivo == f.length()
                && fechaArchivo == f.lastModified();
    }

    private static IndiceBusquedaRuta construir(File f) throws IOException {
        long inicio = System.currentTimeMillis();
        int longitud = TablaRegistroSalida.getLongitudRegistro();
        int total = (int) (f.length() / longitud);

        IndiceBusquedaRuta indice = new IndiceBusquedaRuta(f, total);
        byte[] registro = new byte[longitud];

        try (InputStream in = new BufferedInputStream(new FileInputStream(f), BUFFER)) {
            for (int i = 0; i < total; i++) {
                if (!leerCompleto(in, registro)) {
                    Log.w(TAG, "Archivo truncado en el registro " + (i + 1));
                    break;
                }
                indice.cuenta[i] = campo(registro, OFF_CUENTA, LEN_CUENTA);
                indice.medidor[i] = campo(registro, OFF_MEDIDOR, LEN_MEDIDOR);
                indice.ruta[i] = campo(registro, OFF_RUTA, LEN_RUTA);
                indice.nombre[i] = campo(registro, OFF_NOMBRE, LEN_NOMBRE);
                indice.direccion[i] = campo(registro, OFF_DIRECCION, LEN_DIRECCION);
                indice.suspendido[i] = campo(registro, OFF_SUSPENDIDO, LEN_SUSPENDIDO);

                indice.cuentaN[i] = normalizar(indice.cuenta[i]);
                indice.medidorN[i] = normalizar(indice.medidor[i]);
                indice.rutaN[i] = normalizar(indice.ruta[i]);
                indice.nombreN[i] = normalizar(indice.nombre[i]);
                indice.direccionN[i] = normalizar(indice.direccion[i]);
            }
        }

        Log.i(TAG, "Indice construido: " + total + " registros en "
                + (System.currentTimeMillis() - inicio) + " ms");
        return indice;
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

    private static String campo(byte[] registro, int offset, int longitud) {
        return new String(registro, offset, longitud, StandardCharsets.UTF_8).trim();
    }

    /** Mayusculas y sin tildes, para que "MARIA" encuentre "MARÍA". */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(texto.trim().toUpperCase(), Normalizer.Form.NFD);
        return sinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Devuelve los numeros de registro (base 1) que coinciden.
     *
     * Los cinco campos buscan por COINCIDENCIA: basta con que lo tecleado aparezca en cualquier
     * posicion del campo. Asi el operario no tiene que digitar el numero completo de cuenta ni
     * de medidor, ni acordarse de los ceros de relleno del archivo ("4854" encuentra
     * "0174854005").
     *
     * En cuenta y medidor la lista sale ORDENADA: primero los que empiezan por lo tecleado y
     * despues el resto, en orden de archivo. Con eso, quien si digita el numero completo o sus
     * primeros digitos sigue viendo su registro de primero, igual que antes. Direccion, nombre
     * y ruta se devuelven en orden de archivo, sin tocar.
     *
     * @param maxResultados corte para la lista devuelta; totalCoincidencias() informa cuantas hay.
     */
    public synchronized int[] filtrar(String consulta, int campo, int maxResultados) {
        String buscado = normalizar(consulta);
        if (buscado.isEmpty()) {
            ultimaConsulta = "";
            ultimoCampo = campo;
            ultimasCoincidencias = new int[0];
            return ultimasCoincidencias;
        }

        String[] valores = valoresDe(campo);

        // Si solo se agregaron caracteres al final, basta revisar las coincidencias anteriores:
        // lo que no contiene "174" tampoco puede contener "1748".
        boolean incremental = campo == ultimoCampo
                && !ultimaConsulta.isEmpty()
                && buscado.startsWith(ultimaConsulta);
        int[] candidatos = incremental ? ultimasCoincidencias : null;
        int aRevisar = incremental ? candidatos.length : total;

        int[] encontrados = new int[Math.min(aRevisar, total)];
        int n = 0;
        for (int k = 0; k < aRevisar; k++) {
            int i = incremental ? candidatos[k] - 1 : k;
            String valor = valores[i];
            if (valor != null && valor.contains(buscado)) {
                encontrados[n++] = i + 1;
            }
        }

        ultimaConsulta = buscado;
        ultimoCampo = campo;
        // El cache se guarda SIEMPRE en orden de archivo: es lo que hace valida la reduccion
        // incremental. El orden de presentacion se arma aparte, solo sobre lo que se muestra.
        ultimasCoincidencias = Arrays.copyOf(encontrados, n);

        int tope = Math.min(n, Math.max(0, maxResultados));
        if (campo != CAMPO_CUENTA && campo != CAMPO_MEDIDOR) {
            return Arrays.copyOf(ultimasCoincidencias, tope);
        }
        return prefijosPrimero(ultimasCoincidencias, valores, buscado, tope);
    }

    /**
     * Los registros cuyo campo empieza por lo tecleado van de primeros; el resto conserva el
     * orden de archivo. Dos pasadas sobre las coincidencias, sin ordenamiento ni objetos nuevos:
     * con 2000 registros es despreciable frente a la lectura del archivo.
     */
    private static int[] prefijosPrimero(int[] coincidencias, String[] valores, String buscado, int tope) {
        int[] salida = new int[tope];
        int n = 0;
        for (int registro : coincidencias) {
            if (n == tope) {
                return salida;
            }
            if (valores[registro - 1].startsWith(buscado)) {
                salida[n++] = registro;
            }
        }
        for (int registro : coincidencias) {
            if (n == tope) {
                break;
            }
            if (!valores[registro - 1].startsWith(buscado)) {
                salida[n++] = registro;
            }
        }
        return n == tope ? salida : Arrays.copyOf(salida, n);
    }

    /** Coincidencias totales del ultimo filtrar(), aunque se hayan devuelto menos. */
    public synchronized int totalCoincidencias() {
        return ultimasCoincidencias.length;
    }

    private String[] valoresDe(int campo) {
        switch (campo) {
            case CAMPO_MEDIDOR: return medidorN;
            case CAMPO_RUTA: return rutaN;
            case CAMPO_DIRECCION: return direccionN;
            case CAMPO_NOMBRE: return nombreN;
            case CAMPO_CUENTA:
            default: return cuentaN;
        }
    }

    // Valores originales para mostrar en pantalla (registro en base 1).
    public int total() { return total; }
    public String getCuenta(int registro) { return cuenta[registro - 1]; }
    public String getMedidor(int registro) { return medidor[registro - 1]; }
    public String getRuta(int registro) { return ruta[registro - 1]; }
    public String getNombre(int registro) { return nombre[registro - 1]; }
    public String getDireccion(int registro) { return direccion[registro - 1]; }
    public String getSuspendido(int registro) { return suspendido[registro - 1]; }
}