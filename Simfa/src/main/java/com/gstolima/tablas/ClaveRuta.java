package com.gstolima.tablas;

import android.util.Log;

import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * Identidad de una ruta cargada en una carpeta de trabajo, mas su tipo (lecturas o entregas).
 *
 * LA IDENTIDAD sale del archivo NOMBRE, que tiene posiciones fijas
 * (ejemplo "202606009L019010.002"):
 *
 *   pos 0-5   periodo    "202606"  anno(4) + mes(2)
 *   pos 6-8   ciclo      "009"
 *   pos 9     SIEMPRE "L" -- ver la advertencia de abajo
 *   pos 10-13 seccion    "0190"
 *   pos 14-15 municipio  "10"
 *   pos 16    separador  "."
 *   pos 17-19 division   "002"
 *
 * Despues de la posicion 20 el archivo trae relleno (espacios, punto) que se ignora.
 *
 * OJO CON LA POSICION 9: parece indicar el tipo, pero NO lo hace. Comunicaciones.crearArchivoNombre()
 * escribe la "L" fija:  writer.append(CicloReal + "L" + Seccion + ...). Corresponde al prefijo del
 * archivo que baja del servidor ("L019010.002"), que despues renameArchivo() renombra a "D...".
 * Es "L" tanto en lecturas como en entregas.
 *
 * EL TIPO sale del DATO, igual que en el resto del sistema: el campo DESCSECTOR del primer
 * registro del archivo D. Si empieza con "E" es entrega. Es el mismo criterio que usa
 * MenuDeLiquidacion (DESCSECTOR.substring(0,1).equals("E")).
 *
 * POR QUE LAS 20 POSICIONES COMPLETAS COMO IDENTIDAD:
 * Al servidor se le manda ciclo, anno, mes y "L"+predio como parametros sueltos
 * (ObtenerLecturasPendientes). Esas cuatro cosas juntas son exactamente estas 20 posiciones.
 * Usar solo "L"+predio no alcanza: la misma ruta cargada en dos periodos distintos daria la
 * misma clave.
 *
 * La clave se toma VERBATIM (sin recortar) para que sea reproducible: si algun campo viene
 * corto y relleno con espacios, se sigue tomando el mismo rango y la clave sale identica
 * cada vez.
 */
public final class ClaveRuta {

    private static final String TAG = "ClaveRuta";

    /** Longitud de la parte significativa del archivo NOMBRE. */
    public static final int LONGITUD_CLAVE = 20;

    public static final String MODULO_LECTURAS = "CIC";
    public static final String MODULO_ENTREGAS = "ENT";

    /**
     * Las dos carpetas de trabajo. Cualquiera de las dos puede tener una ruta de lecturas o de
     * entregas: la carpeta dice DONDE esta cargada, y el tipo lo dice el dato.
     */
    public static final String CARPETA_1 = "/LECTURAMEDIDORES";
    public static final String CARPETA_2 = "/LECTURAMEDIDORES2";

    // Posicion de DESCSECTOR dentro de un registro del archivo D (ver TablaRegistroSalida).
    private static final int OFF_DESCSECTOR = 86;
    private static final int LEN_DESCSECTOR = 32;

    private final String clave;
    private final boolean esEntrega;

    // Cache de un solo elemento: el NOMBRE se consulta en cada captura (lectura, foto, cuenta
    // nueva) y practicamente nunca cambia durante la jornada. Se revalida por ruta, tamano y
    // fecha, asi que si recargan la ruta el cache se descarta solo.
    private static String cacheRuta;
    private static long cacheTamano;
    private static long cacheFecha;
    private static ClaveRuta cacheClave;

    private ClaveRuta(String clave, boolean esEntrega) {
        this.clave = clave;
        this.esEntrega = esEntrega;
    }

    // ------------------------------------------------------------------ Lectura

    /** La ruta cargada en la carpeta de trabajo activa, o null si no hay. */
    public static ClaveRuta activa() {
        return leerDeCarpeta(VariablesGlobales.directorioactual, VariablesGlobales.getCarpetaLecturas());
    }

    /**
     * Lee la ruta cargada en una carpeta: la identidad del archivo NOMBRE y el tipo del archivo D.
     *
     * @param directorioActual raiz de datos (VariablesGlobales.directorioactual)
     * @param carpeta          CARPETA_1 o CARPETA_2
     * @return la ruta, o null si no hay ruta utilizable (falta NOMBRE, esta malformado,
     *         o no esta el archivo de registros)
     */
    public static synchronized ClaveRuta leerDeCarpeta(String directorioActual, String carpeta) {
        if (directorioActual == null || carpeta == null) {
            return null;
        }
        File dirCarpeta = new File(directorioActual + carpeta);
        File archivoNombre = new File(dirCarpeta, "NOMBRE");

        if (!archivoNombre.exists() || archivoNombre.length() < LONGITUD_CLAVE) {
            return null;
        }

        String ruta = archivoNombre.getAbsolutePath();
        long tamano = archivoNombre.length();
        long fecha = archivoNombre.lastModified();
        if (cacheClave != null && ruta.equals(cacheRuta) && tamano == cacheTamano && fecha == cacheFecha) {
            return cacheClave;
        }

        String texto = leerClave(archivoNombre);
        if (!esValida(texto)) {
            Log.w(TAG, "Clave de ruta invalida en " + ruta + ": [" + texto + "]");
            return null;
        }
        String clave = texto.substring(0, LONGITUD_CLAVE);

        Boolean entrega = leerEsEntrega(dirCarpeta, clave.substring(10, LONGITUD_CLAVE));
        if (entrega == null) {
            // Hay NOMBRE pero no un archivo de registros legible: la carga quedo incompleta.
            // No se cachea, para que vuelva a intentarlo cuando el archivo aparezca.
            Log.w(TAG, "Sin archivo de registros legible en " + dirCarpeta.getAbsolutePath());
            return null;
        }

        ClaveRuta leida = new ClaveRuta(clave, entrega);
        cacheRuta = ruta;
        cacheTamano = tamano;
        cacheFecha = fecha;
        cacheClave = leida;
        return leida;
    }

    /** Descarta el cache. Llamar tras borrar o recargar una ruta. */
    public static synchronized void limpiarCache() {
        cacheRuta = null;
        cacheClave = null;
        cacheTamano = 0;
        cacheFecha = 0;
    }

    private static String leerClave(File archivoNombre) {
        try (InputStream in = new FileInputStream(archivoNombre)) {
            byte[] buffer = new byte[LONGITUD_CLAVE];
            int leidos = 0;
            while (leidos < LONGITUD_CLAVE) {
                int n = in.read(buffer, leidos, LONGITUD_CLAVE - leidos);
                if (n < 0) {
                    return null;
                }
                leidos += n;
            }
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "No se pudo leer " + archivoNombre.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * Tipo de la ruta segun el DESCSECTOR del primer registro del archivo de registros.
     *
     * Busca "D<predio>" y, si todavia no lo han renombrado, "L<predio>" (renameArchivo() hace
     * ese cambio de nombre despues de cargar).
     *
     * @return TRUE entrega, FALSE lectura, null si no hay archivo legible
     */
    private static Boolean leerEsEntrega(File dirCarpeta, String nombrePredio) {
        String predio = nombrePredio.trim();
        for (String prefijo : new String[]{"D", "L"}) {
            File archivo = new File(dirCarpeta, prefijo + predio);
            if (!archivo.exists() || archivo.length() < OFF_DESCSECTOR + LEN_DESCSECTOR) {
                continue;
            }
            try (RandomAccessFile r = new RandomAccessFile(archivo, "r")) {
                r.seek(OFF_DESCSECTOR);
                byte[] buffer = new byte[LEN_DESCSECTOR];
                r.readFully(buffer);
                String descSector = new String(buffer, StandardCharsets.ISO_8859_1).trim();
                return descSector.toUpperCase().startsWith("E");
            } catch (Exception e) {
                Log.e(TAG, "No se pudo leer DESCSECTOR de " + archivo.getAbsolutePath(), e);
            }
        }
        return null;
    }

    /** Valida longitud y que el periodo y el ciclo sean digitos. */
    public static boolean esValida(String texto) {
        if (texto == null || texto.length() < LONGITUD_CLAVE) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------------ Partes

    /** Las 20 posiciones. Es el valor que se guarda en Realm y con el que se compara. */
    public String getClave() {
        return clave;
    }

    /** "202606" (anno + mes). */
    public String getPeriodo() {
        return clave.substring(0, 6);
    }

    public String getAnno() {
        return clave.substring(0, 4);
    }

    public String getMes() {
        return clave.substring(4, 6);
    }

    /** "009". */
    public String getCiclo() {
        return clave.substring(6, 9);
    }

    /** "202606009": lo que el resto del sistema llama CicloReal. */
    public String getCicloReal() {
        return cicloRealDe(clave);
    }

    /** CicloReal a partir de una clave ya guardada (por ejemplo, la que viene de Realm). */
    public static String cicloRealDe(String clave) {
        return esValida(clave) ? clave.substring(0, 9) : "";
    }

    /** "0190". */
    public String getSeccion() {
        return clave.substring(10, 14);
    }

    /** "10". */
    public String getMunicipio() {
        return clave.substring(14, 16);
    }

    /** "002". */
    public String getDivision() {
        return clave.substring(17, 20);
    }

    /** "019010.002": el nombre del predio, sufijo de los archivos D, F, O de la carpeta. */
    public String getNombrePredio() {
        return clave.substring(10, LONGITUD_CLAVE);
    }

    /** "L019010.002": lo que se envia a ObtenerLecturasPendientes. */
    public String getPredioConTipo() {
        return clave.substring(9, LONGITUD_CLAVE);
    }

    /** Del DESCSECTOR del dato, no del archivo NOMBRE. */
    public boolean esEntrega() {
        return esEntrega;
    }

    /** "CIC" para lecturas, "ENT" para entregas. Es el valor que viaja al servidor. */
    public String getModuloTrabajo() {
        return esEntrega ? MODULO_ENTREGAS : MODULO_LECTURAS;
    }

    @Override
    public String toString() {
        return clave + (esEntrega ? " (ENT)" : " (CIC)");
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof ClaveRuta)) return false;
        return clave.equals(((ClaveRuta) otro).clave);
    }

    @Override
    public int hashCode() {
        return clave.hashCode();
    }
}