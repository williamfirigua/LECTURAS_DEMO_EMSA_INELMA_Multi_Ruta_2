package com.gstolima.comunicaciones;

import com.gstolima.tablas.ClaveRuta;
import android.content.Context;
import android.util.Log;

import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * CrudEnvioCuentaNueva - Operaciones CRUD para cuentas nuevas en Realm
 *
 * Reemplaza el manejo del archivo CUENTASNUEVA{serial}.SDA
 *
 * @author Global Solutions & Service S.A.S.
 */
public class CrudEnvioCuentaNueva {

    private static final String TAG = "CrudEnvioCuentaNueva";
    private static final int MAX_INTENTOS = 5;

    /**
     * Inserta una nueva cuenta en Realm
     */
    public static boolean insertar(EnvioCuentaNueva cuenta) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            estamparRutaActiva(cuenta); // lee NOMBRE: fuera de la transaccion
            realm.beginTransaction();
            realm.copyToRealm(cuenta);
            realm.commitTransaction();
            Log.i(TAG, "Cuenta nueva insertada: " + cuenta.getContador());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error insertando cuenta nueva: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
    //***obtener todos los registros de la tabla
    //***fin de la toma de todos los registros nuevos
    public static List<EnvioCuentaNueva> getTodos() {
        Realm realm = null;
        List<EnvioCuentaNueva> resultado = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<EnvioCuentaNueva> results = realm.where(EnvioCuentaNueva.class)
                    .findAll();

            // Copiar a lista standalone
            for (EnvioCuentaNueva cuenta : results) {
                resultado.add(realm.copyFromRealm(cuenta));
            }
            return resultado;
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes: " + e.getMessage());
            return resultado;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
    /**
     * Obtiene cuentas pendientes de envío
     */
    public static List<EnvioCuentaNueva> obtenerPendientes(int limite) {
        Realm realm = null;
        List<EnvioCuentaNueva> resultado = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<EnvioCuentaNueva> results = realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "P")
                    .sort("fechaCreacion", Sort.ASCENDING)
                    .limit(limite)
                    .findAll();

            // Copiar a lista standalone
            for (EnvioCuentaNueva cuenta : results) {
                resultado.add(realm.copyFromRealm(cuenta));
            }
            return resultado;
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes: " + e.getMessage());
            return resultado;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
    public static String generarArchivoCUENTASNUEVAS(Context context) {
        List<EnvioCuentaNueva> lista = getTodos();

        File ruta = new File(VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/", "CUENTASNUEVAS.SDA");

        if (ruta.exists())
            ruta.delete();

        try (FileWriter writer = new FileWriter(ruta)) {

            for (EnvioCuentaNueva item : lista) {

                // 🔧 Ajusta esto a tus campos reales
                String linea =
                        String.format("%-3s",item.getCiclo()) + "|"
                                + String.format("%-64s", item.getDireccion()) + "|"
                                + String.format("%-20s",item.getContador()) + "|"
                                + String.format("%-15s", item.getMarca()) + "|"
                                + String.format("%-2s", item.getTipoMedidor()) + "|"
                                + String.format("%1s",item.getDigitos()) + "|"
                                + String.format("%7s", item.getLectura()) + "|"
                                + String.format("%2s", item.getObservacion()) + "|"
                                +  String.format("%3s", item.getCiclo()) + "|"
                                + String.format("%32s", item.getDescDepto()) + "|"
                                + String.format("%-3s", item.getCodMunicipio()) + "|"
                                + String.format("%3s", item.getCodSector()) + "|"
                                + String.format("%-13s", item.getCodRuta()) + "|"
                                + String.format("%-60s", item.getInforme()) + "|"
                                + String.format("%6s", item.getCodReferencia()) + "|"
                                + String.format("%-16s", item.getLatitud()) + "|"
                                + String.format("%-16s", item.getLongitud()) + "|"
                                + String.format("%-20s", item.getFechaHora()) + "|"
                                + String.format("%-10s", item.getAltitud()) + "|"
                                + String.format("%3s", item.getNumSatelites()) + "|"
                                + String.format("%4s", item.getLector()) + "|"
                                + String.format("%30s", item.getFoto1()) + "|"
                                + String.format("%30s",item.getFoto2()) + "|"
                                + String.format("%30s", item.getFoto3()) + "|"
                                + String.format("%10s", item.getCicloReal()) + "|";

                writer.write(linea+"\r\n");
            }
            writer.flush();
            Log.d("ARCHIVO", "Archivo generado en: " + ruta.getAbsolutePath());
            return "OK|" + VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"//CUENTASNUEVAS.SDA";

        } catch (Exception e) {
            Log.e("ARCHIVO", "Error generando archivo nuevas cuentas: " + e.getMessage());
            return "ERROR|" + e.getMessage();
        }
    }
    /**
     * Marca una cuenta como enviada
     */
    public static boolean marcarEnviado(String idRealm) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            EnvioCuentaNueva cuenta = realm.where(EnvioCuentaNueva.class)
                    .equalTo("idRealm", idRealm)
                    .findFirst();

            if (cuenta != null) {
                cuenta.setEstadoEnvio("E");
                cuenta.setFechaEnvio(new Date());
                realm.commitTransaction();
                Log.i(TAG, "Cuenta marcada como enviada: " + idRealm);
                return true;
            }

            realm.cancelTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando como enviado: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Marca una cuenta con error de envío
     */
    public static boolean marcarError(String idRealm, String mensajeError) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            EnvioCuentaNueva cuenta = realm.where(EnvioCuentaNueva.class)
                    .equalTo("idRealm", idRealm)
                    .findFirst();

            if (cuenta != null) {
                cuenta.setIntentosEnvio(cuenta.getIntentosEnvio() + 1);
                cuenta.setMensajeError(mensajeError);

                if (cuenta.getIntentosEnvio() >= MAX_INTENTOS) {
                    cuenta.setEstadoEnvio("X");
                }

                realm.commitTransaction();
                return true;
            }

            realm.cancelTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando error: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
    public static boolean marcarError2(String idRealm, String mensajeError) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            EnvioCuentaNueva cuenta = realm.where(EnvioCuentaNueva.class)
                    .equalTo("idRealm", idRealm)
                    .findFirst();

            if (cuenta != null) {
                cuenta.setIntentosEnvio(cuenta.getIntentosEnvio() + 1);
                cuenta.setMensajeError(mensajeError);

                if (cuenta.getIntentosEnvio() >= MAX_INTENTOS) {
                    cuenta.setEstadoEnvio("X");
                }

                realm.commitTransaction();
                return true;
            }

            realm.cancelTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando error: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Cuenta cuentas nuevas pendientes
     */
    public static int contarPendientes() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            return (int) realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "P")
                    .count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando pendientes: " + e.getMessage());
            return 0;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Obtiene estadísticas
     * @return int[0]=pendientes, int[1]=enviadas, int[2]=errores
     */
    public static int[] getEstadisticas() {
        Realm realm = null;
        int[] stats = new int[3];
        try {
            realm = Realm.getDefaultInstance();
            stats[0] = (int) realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "P")
                    .count();
            stats[1] = (int) realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "E")
                    .count();
            stats[2] = (int) realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "X")
                    .count();
            return stats;
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo estadísticas: " + e.getMessage());
            return stats;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Limpia cuentas ya enviadas
     */
    public static int limpiarEnviados() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<EnvioCuentaNueva> enviadas = realm.where(EnvioCuentaNueva.class)
                    .equalTo("estadoEnvio", "E")
                    .findAll();

            int cantidad = enviadas.size();
            enviadas.deleteAllFromRealm();

            realm.commitTransaction();
            Log.i(TAG, "Limpiados " + cantidad + " registros enviados");
            return cantidad;
        } catch (Exception e) {
            Log.e(TAG, "Error limpiando enviados: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return 0;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Verifica si ya existe una cuenta nueva con ese número de contador
     * Usado por reconstruirTodoDesdeArchivos() para evitar duplicados
     */
    public static boolean existePorContador(String contador) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            return realm.where(EnvioCuentaNueva.class)
                    .equalTo("contador", contador)
                    .count() > 0;
        } catch (Exception e) {
            Log.e(TAG, "existePorContador error: " + e.getMessage());
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Compatibilidad: reencola solo las cuentas en ERROR.
     * @deprecated usar {@link #reintentar(ModoReenvio)}.
     */
    public static int reintentarErrores() {
        return reintentar(ModoReenvio.SOLO_ERROR);
    }

    /**
     * Devuelve cuentas nuevas al estado PENDIENTE ("P"), reseteando el contador
     * de intentos y el mensaje de error.
     *
     * NOTA: en la rama de Valle este método recibía el modo pero lo ignoraba —
     * filtraba siempre por estadoEnvio = "X". Resultado: la opción "Reenviar
     * TODOS" del menú no incluía las cuentas nuevas ya enviadas, pese a
     * prometerlo en el diálogo. Aquí el modo sí se aplica.
     */
    public static int reintentar(ModoReenvio modo) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<EnvioCuentaNueva> registros =
                    (modo == ModoReenvio.TODAS)
                            ? realm.where(EnvioCuentaNueva.class).findAll()
                            : realm.where(EnvioCuentaNueva.class)
                            .equalTo("estadoEnvio", "X")
                            .findAll();

            int cantidad = 0;
            for (EnvioCuentaNueva cuenta : registros) {
                cuenta.setEstadoEnvio("P");
                cuenta.setIntentosEnvio(0);
                cuenta.setMensajeError(null);
                cantidad++;
            }

            realm.commitTransaction();
            Log.i(TAG, "Reencoladas " + cantidad + " cuentas nuevas (modo " + modo + ")");
            return cantidad;
        } catch (Exception e) {
            Log.e(TAG, "Error reintentando: " + e.getMessage());
            if (realm != null && realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return 0;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Estampa la ruta activa en el registro antes de guardarlo.
     *
     * Se hace en el CRUD, que es el punto unico por el que pasan todas las capturas, en vez
     * de en cada pantalla que inserta. Si el registro ya trae clave (por ejemplo una
     * recuperacion que la reconstruyo), se respeta.
     */
    private static void estamparRutaActiva(EnvioCuentaNueva registro) {
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
            RealmQuery<EnvioCuentaNueva> q = realm.where(EnvioCuentaNueva.class)
                    .beginGroup()
                    .equalTo("estadoEnvio", "P")
                    .or().equalTo("estadoEnvio", "X")
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
            RealmQuery<EnvioCuentaNueva> q = realm.where(EnvioCuentaNueva.class)
                    .beginGroup()
                    .equalTo("claveRuta", claveRuta);
            if (incluirSinRuta) {
                q = q.or().isNull("claveRuta").or().equalTo("claveRuta", "");
            }
            RealmResults<EnvioCuentaNueva> filas = q.endGroup().findAll();
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