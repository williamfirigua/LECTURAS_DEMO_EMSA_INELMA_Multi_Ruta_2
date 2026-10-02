package com.gstolima.comunicaciones;

import com.gstolima.tablas.ClaveRuta;
import android.content.Context;
import android.util.Log;

import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * CrudEnvioLectura - CRUD Realm para la tabla de lecturas pendientes de envío
 *
 * @author Global Solutions & Service S.A.S.
 */
public class CrudEnvioLectura {

    private static final String TAG = "CrudEnvioLectura";

    public CrudEnvioLectura(Context ctx) {
        Realm.init(ctx);
    }

    /**
     * Obtiene el siguiente ID disponible
     */
    private static long getNextId() {
        try (Realm realm = Realm.getDefaultInstance()) {
            Number maxId = realm.where(EnvioLectura.class).max("idRealm");
            return (maxId != null) ? maxId.longValue() + 1 : 1;
        }
    }

    /**
     * Inserta una nueva lectura pendiente de envío
     */
    public static boolean insertar(EnvioLectura lectura) {
        Realm realm = Realm.getDefaultInstance();
        try {
            estamparRutaActiva(lectura); // lee NOMBRE: fuera de la transaccion
            realm.beginTransaction();

            lectura.setIdRealm(getNextId());
            lectura.setEstadoEnvio(EnvioLectura.ESTADO_PENDIENTE);
            lectura.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            lectura.setIntentosEnvio(0);

            realm.copyToRealm(lectura);
            realm.commitTransaction();

            Log.i(TAG, "Lectura insertada: cuenta=" + lectura.getCuenta() + " idRealm=" + lectura.getIdRealm());
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error insertando lectura: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Obtiene todas las lecturas pendientes de envío
     */
    public static List<EnvioLectura> getPendientes() {
        List<EnvioLectura> lista = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioLectura> results = realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_PENDIENTE)
                    .findAll();
            lista = realm.copyFromRealm(results);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes: " + e.getMessage());
        }
        return lista;
    }
    //octener todos los registros para crear un plado de ellos
    public static List<EnvioLectura> getTodos() {
        List<EnvioLectura> lista = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioLectura> results = realm.where(EnvioLectura.class)
                    .findAll(); // 🔥 sin filtro
            lista = realm.copyFromRealm(results);
        } catch (Exception e) {
            Log.e("TAG", "Error obteniendo todos: " + e.getMessage());
        }
        return lista;
    }
    public static String generarArchivoBKENVIOS(Context context) {
        List<EnvioLectura> lista = getTodos();

        File ruta = new File(VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/", "BKENVIOSGPRS.SDA");

        if (ruta.exists())
            ruta.delete();

        try (FileWriter writer = new FileWriter(ruta)) {

            for (EnvioLectura item : lista) {
                String Informe1=item.getInforme().trim();
                if (Informe1.length()>250)
                    Informe1 =Informe1.substring(0,250);
                // 🔧 Ajusta esto a tus campos reales
                String linea =
                        String.format("%-3s",item.getCiclo()) + "|"
                                + String.format("%-3s", item.getMunicipio()) + "|"
                                + String.format("%-4s",item.getSeccion()) + "|"
                                + String.format("%-13s", item.getDepartamento()) + "|"
                                + String.format("%-10s", item.getCuenta()) + "|"
                                + String.format("%20s",item.getNroContador()) + "|"
                                + String.format("%30s", item.getMarcaMedidor()) + "|"
                                + String.format("%15s", item.getFichaCatastral()) + "|"
                                +  String.format("%8s", item.getFechaHoraLectura()) + "|"
                                + String.format("%8s", item.getHoraImpresion()) + "|"
                                + String.format("%-9s", item.getLecturaTomada()) + "|"
                                + String.format("%2s", item.getCausaNoLectura()) + "|"
                                + String.format("%-10s", item.getCodLector()) + "|"
                                + String.format("%-15s", item.getTerminal()) + "|"
                                + String.format("%20s", item.getPeriodoLectura()) + "|"
                                + String.format("%-20s", item.getLongitud()) + "|"
                                + String.format("%-20s", item.getLatitud()) + "|"
                                + String.format("%-3s", item.getNroSatelites()) + "|"
                                + String.format("%-250s", Informe1.trim()) + "|"
                                + String.format("%1s", item.getCriticaPda()) + "|"
                                + String.format("%1s", item.getEstadoEnvio()) + "|"
                                + String.format("%1s", item.getComentario()) + "|"
                                + String.format("%1s",item.getIntentos()) + "|"
                                + String.format("%20s", item.getFechaHoraSatelite()) + "|"
                                + String.format("%10s", item.getAltitudSatelite()) + "|"
                                + String.format("%10s", item.getDistancia()) + "|"
                                + String.format("%11s", item.getLecturaModificada1()) + "|"
                                + String.format("%11s", item.getLecturaModificada2()) + "|"
                                + String.format("%5s",item.getTiempo()) + "|"
                                + String.format("%-10s", item.getConsumoFacturado()) + "|"
                                + String.format("%-10s", item.getLecturaAnterior()) + "|"
                                + String.format("%-2s", item.getEstrato()) + "|"
                                + String.format("%-2s", item.getUso()) + "|"
                                + String.format("%12s", item.getNombreArchivo()) + "|"
                                + String.format("%11s", item.getIdRegistro()) + "|"
                                + String.format("%6s", item.getTotalCausasNoLectura()) + "|"
                                + String.format("%6s", item.getTotalLecturas()) + "|"
                                + String.format("%6s", item.getTotalConsumoBajo()) + "|"
                                + String.format("%6s", item.getTotalConsumoAlto()) + "|"
                                + String.format("%6s", item.getTotalConsumoNormal()) + "|"
                                + String.format("%6s",item.getTotalLecturasIguales()) + "|"
                                + String.format("%6s", item.getTotalLeidas()) + "|"
                                + String.format("%6s", item.getNumObser()) + "|"
                                + String.format("%6s", item.getNumInformes()) + "|"
                                + String.format("%12s", item.getTotalTiempoProm()) + "|"
                                + String.format("%12s",item.getTotalDistanciaProm()) + "|"
                                + String.format("%-3s", item.getCodigoSac()) + "|";

                writer.write(linea+"\r\n");
            }
            writer.flush();
            Log.d("ARCHIVO", "Archivo generado en: " + ruta.getAbsolutePath());
            return "OK|" + VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"//BKENVIOSGPRS.SDA";

        } catch (Exception e) {
            Log.e("ARCHIVO", "Error generando archivo: " + e.getMessage());
            return "ERROR|" + e.getMessage();
        }
    }
    /**
     * Obtiene lecturas pendientes con límite (para envío por lotes)
     */
    public static List<EnvioLectura> getPendientes(int limite) {
        List<EnvioLectura> lista = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioLectura> results = realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_PENDIENTE)
                    .sort("idRealm", Sort.ASCENDING)
                    .limit(limite)
                    .findAll();
            lista = realm.copyFromRealm(results);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cuenta las lecturas pendientes de envío
     */
    public static int contarPendientes() {
        try (Realm realm = Realm.getDefaultInstance()) {
            return (int) realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_PENDIENTE)
                    .count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando pendientes: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Marca una lectura como enviada exitosamente
     */
    public static boolean marcarEnviado(long idRealm, String errormsg) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioLectura lectura = realm.where(EnvioLectura.class).equalTo("idRealm", idRealm).findFirst();
            if (lectura != null) {
                lectura.setEstadoEnvio(EnvioLectura.ESTADO_ENVIADO);
                lectura.setFechaEnvioApi(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                lectura.setErrorEnvio(errormsg + "| Enviado posteriormente");
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando enviado: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }
    public static boolean marcarError2(long idRealm, String error) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioLectura lectura = realm.where(EnvioLectura.class).equalTo("idRealm", idRealm).findFirst();
            if (lectura != null) {
                lectura.setEstadoEnvio(EnvioLectura.ESTADO_PENDIENTE);
                lectura.setIntentosEnvio(lectura.getIntentosEnvio() + 1);
                lectura.setErrorEnvio(error);
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando error: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }


    /**
     * Marca una lectura con error de envío
     */
    public static boolean marcarError(long idRealm, String error) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioLectura lectura = realm.where(EnvioLectura.class).equalTo("idRealm", idRealm).findFirst();
            if (lectura != null) {
                lectura.setEstadoEnvio(EnvioLectura.ESTADO_ERROR);
                lectura.setIntentosEnvio(lectura.getIntentosEnvio() + 1);
                lectura.setErrorEnvio(error);
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando error: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Compatibilidad: reencola solo los registros en ERROR.
     * @deprecated usar {@link #reintentar(ModoReenvio)}, que hace explícito el modo.
     */
    public static int reintentarErrores() {
        return reintentar(ModoReenvio.SOLO_ERROR);
    }

    /**
     * Devuelve registros al estado PENDIENTE para que el servicio los reenvíe.
     *
     * @param modo SOLO_ERROR reencola únicamente los fallidos; TODAS reencola
     *             el histórico completo del dispositivo (operación pesada:
     *             una sola transacción sobre todos los registros).
     * @return cantidad de registros que quedaron en PENDIENTE
     */
    public static int reintentar(ModoReenvio modo) {
        Realm realm = Realm.getDefaultInstance();
        int actualizados = 0;
        try {
            realm.beginTransaction();

            RealmResults<EnvioLectura> registros =
                    (modo == ModoReenvio.TODAS)
                            ? realm.where(EnvioLectura.class).findAll()
                            : realm.where(EnvioLectura.class)
                            .equalTo("estadoEnvio", EnvioLectura.ESTADO_ERROR)
                            .findAll();

            for (EnvioLectura lectura : registros) {
                lectura.setEstadoEnvio(EnvioLectura.ESTADO_PENDIENTE);
                actualizados++;
            }

            realm.commitTransaction();
            Log.i(TAG, "Reencoladas " + actualizados + " lecturas (modo " + modo + ")");
        } catch (Exception e) {
            Log.e(TAG, "Error reintentando: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) realm.close();
        }
        return actualizados;
    }

    /**
     * Elimina lecturas ya enviadas (limpieza)
     */
    public static int limpiarEnviados() {
        Realm realm = Realm.getDefaultInstance();
        int eliminados = 0;
        try {
            realm.beginTransaction();
            RealmResults<EnvioLectura> enviados = realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_ENVIADO)
                    .findAll();
            eliminados = enviados.size();
            enviados.deleteAllFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            Log.e(TAG, "Error limpiando enviados: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) realm.close();
        }
        return eliminados;
    }

    /**
     * Busca una lectura por cuenta, año y mes
     */
    public static EnvioLectura buscarPorCuentaAnnoMes(String cuenta, String anno, String mes) {
        EnvioLectura resultado = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            EnvioLectura lectura = realm.where(EnvioLectura.class)
                    .equalTo("cuenta", cuenta)
                    .equalTo("anno", anno)
                    .equalTo("mes", mes)
                    .findFirst();
            if (lectura != null) {
                resultado = realm.copyFromRealm(lectura);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error buscando lectura: " + e.getMessage());
        }
        return resultado;
    }


    /**
     * Igual que buscarPorCuentaAnnoMes pero filtrando también por tipo de medidor
     * (campo 'comentario'). Necesario para comparar contenido en correcciones, ya
     * que una misma cuenta puede tener varios tipos de medidor en el mismo periodo
     * y buscarPorCuentaAnnoMes devolvería cualquiera de ellos.
     *
     * Devuelve una copia desligada de Realm (copyFromRealm), segura de usar
     * fuera de la transacción.
     */
    public static EnvioLectura buscarPorCuentaAnnoMesTipo(String cuenta, String anno,
                                                          String mes, String tipoMedidor) {
        EnvioLectura resultado = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            EnvioLectura lectura = realm.where(EnvioLectura.class)
                    .equalTo("cuenta", cuenta)
                    .equalTo("anno", anno)
                    .equalTo("mes", mes)
                    .equalTo("comentario", tipoMedidor)
                    .findFirst();
            if (lectura != null) {
                resultado = realm.copyFromRealm(lectura);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error buscando lectura por tipo: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Reemplaza una lectura existente por la nueva (upsert para correcciones).
     * Borra el registro anterior y guarda el nuevo con estado PENDIENTE.
     * Se usa cuando EstadoEnvio = "M" (modificación de lectura ya guardada).
     */
    public static boolean reemplazar(String cuenta, String anno, String mes,
                                     String tipoMedidor, EnvioLectura nueva) {
        Realm realm = Realm.getDefaultInstance();
        try {
            estamparRutaActiva(nueva); // lee NOMBRE: fuera de la transaccion
            realm.beginTransaction();

            // Borrar el registro anterior (pendiente, enviado o con error)
            RealmResults<EnvioLectura> anteriores = realm.where(EnvioLectura.class)
                    .equalTo("cuenta", cuenta)
                    .equalTo("anno", anno)
                    .equalTo("mes", mes)
                    .equalTo("comentario", tipoMedidor)
                    .findAll();
            anteriores.deleteAllFromRealm();

            // Insertar la corrección como nuevo pendiente
            nueva.setIdRealm(getNextId());
            nueva.setEstadoEnvio(EnvioLectura.ESTADO_PENDIENTE);
            nueva.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()).format(new Date()));
            nueva.setIntentosEnvio(0);
            realm.copyToRealm(nueva);

            realm.commitTransaction();
            Log.i(TAG, "Lectura reemplazada (corrección M): cuenta=" + cuenta);
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error reemplazando lectura: " + e.getMessage());
            if (realm.isInTransaction()) realm.cancelTransaction();
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Verifica si ya existe una lectura para la cuenta en el período
     */
    public static boolean existe(String cuenta, String anno, String mes, String tipoMedidor) {
        try (Realm realm = Realm.getDefaultInstance()) {
            return realm.where(EnvioLectura.class)
                    .equalTo("cuenta", cuenta)
                    .equalTo("anno", anno)
                    .equalTo("mes", mes)
                    .equalTo("comentario", tipoMedidor) // comentario = tipo_medidor
                    .count() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error verificando existencia: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene estadísticas de envío
     */
    public static int[] getEstadisticas() {
        // [0]=pendientes, [1]=enviados, [2]=errores
        int[] stats = new int[3];
        try (Realm realm = Realm.getDefaultInstance()) {
            stats[0] = (int) realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_PENDIENTE).count();
            stats[1] = (int) realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_ENVIADO).count();
            stats[2] = (int) realm.where(EnvioLectura.class)
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_ERROR).count();
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo estadísticas: " + e.getMessage());
        }
        return stats;
    }

    // ==================== ALIAS PARA COMPATIBILIDAD ====================

    /**
     * Alias de getPendientes(int limite) para compatibilidad
     */
    public static List<EnvioLectura> obtenerPendientes(int limite) {
        return getPendientes(limite);
    }

    /**
     * Marca como enviado usando String (el servicio usa String para idRealm)
     */
    /*public static boolean marcarEnviado(String idRealm) {
        try {
            return marcarEnviado(Long.parseLong(idRealm));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parseando idRealm: " + idRealm);
            return false;
        }
    }*/

    /**
     * Marca como error usando String
     */
    public static boolean marcarError(String idRealm, String error) {
        try {
            return marcarError(Long.parseLong(idRealm), error);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parseando idRealm: " + idRealm);
            return false;
        }
    }
    /**
     * Cuenta TODOS los registros (pendientes + enviados + error)
     * Usado para detectar si Realm está vacío
     */
    public static int contarTodos() {
        try (Realm realm = Realm.getDefaultInstance()) {
            return (int) realm.where(EnvioLectura.class).count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando todos: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Estampa la ruta activa en el registro antes de guardarlo.
     *
     * Se hace en el CRUD, que es el punto unico por el que pasan todas las capturas, en vez
     * de en cada pantalla que inserta. Si el registro ya trae clave (por ejemplo una
     * recuperacion que la reconstruyo), se respeta.
     */
    private static void estamparRutaActiva(EnvioLectura registro) {
        if (registro == null || !registro.getClaveRuta().isEmpty()) {
            return;
        }
        ClaveRuta ruta = ClaveRuta.activa();
        if (ruta == null) {
            Log.w(TAG, "Sin ruta activa legible: el registro queda sin claveRuta");
            return;
        }
        registro.setClaveRuta(ruta.getClave());
        registro.setModuloTrabajo(ruta.getModuloTrabajo());
    }

    // ==================== FILTRADO POR RUTA ====================

    /**
     * Cuenta lo que NO se ha enviado de una ruta: pendientes mas errores.
     *
     * @param claveRuta      clave de la ruta (20 posiciones del archivo NOMBRE)
     * @param incluirSinRuta true para contar tambien las filas sin clave (creadas antes de que
     *                       el esquema guardara la ruta). Se incluyen cuando no hay otra ruta
     *                       cargada a la que pudieran pertenecer.
     */
    public static int contarNoEnviadasDeRuta(String claveRuta, boolean incluirSinRuta) {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<EnvioLectura> q = realm.where(EnvioLectura.class)
                    .beginGroup()
                    .equalTo("estadoEnvio", EnvioLectura.ESTADO_PENDIENTE)
                    .or().equalTo("estadoEnvio", EnvioLectura.ESTADO_ERROR)
                    .endGroup()
                    .beginGroup()
                    .equalTo("claveRuta", claveRuta);
            if (incluirSinRuta) {
                q = q.or().isNull("claveRuta").or().equalTo("claveRuta", "");
            }
            return (int) q.endGroup().count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando no enviadas de la ruta: " + e.getMessage());
            // Ante la duda, informar que hay pendientes: es preferible no dejar borrar.
            return -1;
        }
    }

    /**
     * Borra TODAS las filas de una ruta, sin importar su estado. Se usa al eliminar la
     * informacion de la ruta activa, cuando ya se comprobo que no queda nada por enviar.
     *
     * @return cuantas filas se borraron, o -1 si fallo
     */
    public static int borrarDeRuta(String claveRuta, boolean incluirSinRuta) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            RealmQuery<EnvioLectura> q = realm.where(EnvioLectura.class)
                    .beginGroup()
                    .equalTo("claveRuta", claveRuta);
            if (incluirSinRuta) {
                q = q.or().isNull("claveRuta").or().equalTo("claveRuta", "");
            }
            RealmResults<EnvioLectura> filas = q.endGroup().findAll();
            int cantidad = filas.size();
            filas.deleteAllFromRealm();
            realm.commitTransaction();
            Log.i(TAG, "Borradas " + cantidad + " filas de la ruta " + claveRuta);
            return cantidad;
        } catch (Exception e) {
            Log.e(TAG, "Error borrando filas de la ruta: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return -1;
        } finally {
            realm.close();
        }
    }
}