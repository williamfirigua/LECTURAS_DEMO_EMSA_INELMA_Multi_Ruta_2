package com.gstolima.comunicaciones;

import com.gstolima.tablas.ClaveRuta;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * CrudEnvioFoto - CRUD Realm para la tabla de fotos pendientes de envío
 *
 * @author Global Solutions & Service S.A.S.
 */
public class CrudEnvioFoto {

    private static final String TAG = "CrudEnvioFoto";

    public CrudEnvioFoto(Context ctx) {
        Realm.init(ctx);
    }

    /**
     * Obtiene el siguiente ID disponible
     */
    private static long getNextId() {
        try (Realm realm = Realm.getDefaultInstance()) {
            Number maxId = realm.where(EnvioFoto.class).max("id");
            return (maxId != null) ? maxId.longValue() + 1 : 1;
        }
    }

    /**
     * Obtiene el siguiente conteo de foto para una cuenta
     * CORREGIDO: conteo es String, no se puede usar .max()
     */
    public static String getSiguienteConteo(String codCuenta, String tipoMedidor) {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioFoto> fotos = realm.where(EnvioFoto.class)
                    .equalTo("codCuenta", codCuenta)
                    .equalTo("tipoMedidor", tipoMedidor)
                    .findAll();

            int maxConteo = 0;
            for (EnvioFoto foto : fotos) {
                try {
                    String conteoStr = foto.getConteo();
                    if (conteoStr != null && !conteoStr.isEmpty()) {
                        int conteo = Integer.parseInt(conteoStr.trim());
                        if (conteo > maxConteo) {
                            maxConteo = conteo;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignorar si no es número válido
                }
            }

            return String.format(Locale.US, "%02d", maxConteo + 1);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo conteo: " + e.getMessage());
            return "01";
        }
    }

    /**
     * Inserta una nueva foto pendiente de envío
     */
    public static boolean insertar(EnvioFoto foto) {
        Realm realm = Realm.getDefaultInstance();
        try {
            estamparRutaActiva(foto); // lee NOMBRE: fuera de la transaccion
            realm.beginTransaction();

            foto.setId(getNextId());
            foto.setEstadoEnvio(EnvioFoto.ESTADO_PENDIENTE);
            foto.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            foto.setIntentosEnvio(0);

            // Generar nombre de foto si no tiene
            if (foto.getNombreFoto().isEmpty()) {
                foto.setNombreFoto(foto.generarNombreFoto());
            }

            // Determinar campo destino
            foto.setCampoDestino(foto.determinarCampoDestino());

            realm.copyToRealm(foto);
            realm.commitTransaction();

            Log.i(TAG, "Foto insertada: " + foto.getNombreFoto() + " -> " + foto.getCampoDestino());
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error insertando foto: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Crea y registra una nueva foto
     */
    public static boolean registrarFoto(String codCuenta, String tipoMedidor, String idRegistro,
                                        String rutaLocal, String anno, String mes,
                                        String ciclo, String lector, String terminal) {
        EnvioFoto foto = new EnvioFoto();
        foto.setCodCuenta(codCuenta);
        foto.setTipoMedidor(tipoMedidor);
        foto.setIdRegistro(idRegistro);
        foto.setRutaLocal(rutaLocal);
        foto.setAnno(anno);
        foto.setMes(mes);
        foto.setCiclo(ciclo);
        foto.setLector(lector);
        foto.setTerminal(terminal);
        foto.setConteo(getSiguienteConteo(codCuenta, tipoMedidor));

        return insertar(foto);
    }

    /**
     * Obtiene todas las fotos pendientes de envío
     */
    public static List<EnvioFoto> getPendientes() {
        List<EnvioFoto> lista = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioFoto> results = realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_PENDIENTE)
                    .findAll();
            lista = realm.copyFromRealm(results);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene fotos pendientes con límite y validación de archivo
     */
    public static List<EnvioFoto> getPendientesValidados(int limite) {
        List<EnvioFoto> lista = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<EnvioFoto> results = realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_PENDIENTE)
                    .sort("id", Sort.ASCENDING)
                    .findAll();

            for (EnvioFoto foto : results) {
                if (lista.size() >= limite) break;

                // Verificar que el archivo existe
                File archivo = new File(foto.getRutaLocal());
                if (archivo.exists() && archivo.length() > 0) {
                    lista.add(realm.copyFromRealm(foto));
                } else {
                    // Marcar como no existente
                    realm.beginTransaction();
                    foto.setEstadoEnvio(EnvioFoto.ESTADO_NO_EXISTE);
                    realm.commitTransaction();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo pendientes validados: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cuenta las fotos pendientes de envío
     */
    public static int contarPendientes() {
        try (Realm realm = Realm.getDefaultInstance()) {
            return (int) realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_PENDIENTE)
                    .count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando pendientes: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Cuenta TODOS los registros (pendientes + enviados + error + no existe)
     * Usado para detectar si Realm está vacío
     */
    public static int contarTodos() {
        try (Realm realm = Realm.getDefaultInstance()) {
            return (int) realm.where(EnvioFoto.class).count();
        } catch (Exception e) {
            Log.e(TAG, "Error contando todos: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Marca una foto como enviada exitosamente
     */
    public static boolean marcarEnviado(long id, String mensajeerrr) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioFoto foto = realm.where(EnvioFoto.class).equalTo("id", id).findFirst();
            if (foto != null) {
                foto.setEstadoEnvio(EnvioFoto.ESTADO_ENVIADO);
                foto.setFechaEnvio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                foto.setErrorEnvio(mensajeerrr + " | Enviado posteriormente");
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

    /**
     * Marca una foto con error de envío
     */
    public static boolean marcarError(long id, String error) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioFoto foto = realm.where(EnvioFoto.class).equalTo("id", id).findFirst();
            if (foto != null) {
                foto.setEstadoEnvio(EnvioFoto.ESTADO_ERROR);
                foto.setIntentosEnvio(foto.getIntentosEnvio() + 1);
                foto.setErrorEnvio(error);
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
    public static boolean marcarError2(long id, String error) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioFoto foto = realm.where(EnvioFoto.class).equalTo("id", id).findFirst();
            if (foto != null) {
                foto.setEstadoEnvio(EnvioFoto.ESTADO_PENDIENTE);
                foto.setIntentosEnvio(foto.getIntentosEnvio() + 1);
                foto.setErrorEnvio(error);
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
     * Elimina fotos ya enviadas (limpieza)
     */
    public static int limpiarEnviados() {
        Realm realm = Realm.getDefaultInstance();
        int eliminados = 0;
        try {
            realm.beginTransaction();
            RealmResults<EnvioFoto> enviados = realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_ENVIADO)
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
     * Compatibilidad: reencola solo las fotos en ERROR.
     * @deprecated usar {@link #reintentar(ModoReenvio)}.
     */
    public static int reintentarErrores() {
        return reintentar(ModoReenvio.SOLO_ERROR);
    }

    /**
     * Devuelve fotos al estado PENDIENTE. Antes de reencolar verifica que el
     * archivo siga en disco; si no está, la marca ESTADO_NO_EXISTE en vez de
     * dejarla reintentando indefinidamente contra un fichero borrado.
     *
     * @return cantidad de fotos efectivamente reencoladas (no incluye las que
     *         quedaron marcadas como inexistentes)
     */
    public static int reintentar(ModoReenvio modo) {
        Realm realm = Realm.getDefaultInstance();
        int actualizados = 0;
        int inexistentes = 0;
        try {
            realm.beginTransaction();

            RealmResults<EnvioFoto> registros =
                    (modo == ModoReenvio.TODAS)
                            ? realm.where(EnvioFoto.class).findAll()
                            : realm.where(EnvioFoto.class)
                            .equalTo("estadoEnvio", EnvioFoto.ESTADO_ERROR)
                            .findAll();

            for (EnvioFoto foto : registros) {
                File archivo = new File(foto.getRutaLocal());
                if (archivo.exists()) {
                    foto.setEstadoEnvio(EnvioFoto.ESTADO_PENDIENTE);
                    actualizados++;
                } else {
                    foto.setEstadoEnvio(EnvioFoto.ESTADO_NO_EXISTE);
                    inexistentes++;
                }
            }

            realm.commitTransaction();
            Log.i(TAG, "Reencoladas " + actualizados + " fotos (modo " + modo +
                    "), " + inexistentes + " sin archivo en disco");
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
     * Obtiene estadísticas de envío de fotos
     */
    public static int[] getEstadisticas() {
        // [0]=pendientes, [1]=enviados, [2]=errores, [3]=no_existe
        int[] stats = new int[4];
        try (Realm realm = Realm.getDefaultInstance()) {
            stats[0] = (int) realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_PENDIENTE).count();
            stats[1] = (int) realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_ENVIADO).count();
            stats[2] = (int) realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_ERROR).count();
            stats[3] = (int) realm.where(EnvioFoto.class)
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_NO_EXISTE).count();
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo estadísticas: " + e.getMessage());
        }
        return stats;
    }

    // ==================== ALIAS PARA COMPATIBILIDAD ====================

    /**
     * Alias de getPendientesValidados para compatibilidad con el servicio
     */
    public static List<EnvioFoto> obtenerPendientes(int limite) {
        return getPendientesValidados(limite);
    }

    /**
     * Marca como enviado usando String (el servicio puede usar String)
     */
    /*public static boolean marcarEnviado(String idRealm) {
        try {
            return marcarEnviado(Long.parseLong(idRealm));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parseando id: " + idRealm);
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
            Log.e(TAG, "Error parseando id: " + idRealm);
            return false;
        }
    }

    /**
     * Marca una foto como archivo no encontrado
     */
    public static boolean marcarArchivoNoEncontrado(String idRealm) {
        try {
            return marcarArchivoNoEncontrado(Long.parseLong(idRealm));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parseando id: " + idRealm);
            return false;
        }
    }

    /**
     * Marca una foto como archivo no encontrado (long)
     */
    public static boolean marcarArchivoNoEncontrado(long id) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            EnvioFoto foto = realm.where(EnvioFoto.class).equalTo("id", id).findFirst();
            if (foto != null) {
                foto.setEstadoEnvio(EnvioFoto.ESTADO_NO_EXISTE);
                foto.setErrorEnvio("Archivo no encontrado en disco");
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error marcando archivo no encontrado: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Verifica si ya existe una foto con la ruta local especificada
     */
    public static boolean existeConRuta(String rutaLocal) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            EnvioFoto existing = realm.where(EnvioFoto.class)
                    .equalTo("rutaLocal", rutaLocal)
                    .findFirst();
            return existing != null;
        } catch (Exception e) {
            Log.e(TAG, "Error verificando existencia: " + e.getMessage());
            return false;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Guarda una foto para recuperación desde ValidarNoEnviados
     * A diferencia de insertar(), NO regenera el nombre de la foto
     * porque la foto ya existe en disco con su nombre original
     */
    public static boolean guardar(EnvioFoto foto) {
        Realm realm = Realm.getDefaultInstance();
        try {
            // Verificar si ya existe con esa ruta
            EnvioFoto existente = realm.where(EnvioFoto.class)
                    .equalTo("rutaLocal", foto.getRutaLocal())
                    .findFirst();

            if (existente != null) {
                Log.d(TAG, "Foto ya existe en Realm: " + foto.getRutaLocal());
                return true; // Ya existe, no duplicar
            }

            estamparRutaActiva(foto); // lee NOMBRE: fuera de la transaccion
            realm.beginTransaction();

            foto.setId(getNextId());
            foto.setEstadoEnvio(EnvioFoto.ESTADO_PENDIENTE);
            foto.setIntentosEnvio(0);

            if (foto.getFechaCreacion() == null || foto.getFechaCreacion().isEmpty()) {
                foto.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            }

            // Extraer nombre de foto de la ruta si no tiene
            if (foto.getNombreFoto() == null || foto.getNombreFoto().isEmpty()) {
                File archivo = new File(foto.getRutaLocal());
                foto.setNombreFoto(archivo.getName());
            }

            // Determinar conteo del nombre si no tiene
            if (foto.getConteo() == null || foto.getConteo().isEmpty()) {
                foto.setConteo(extraerConteoDeNombre(foto.getNombreFoto()));
            }

            realm.copyToRealm(foto);
            realm.commitTransaction();

            Log.i(TAG, "Foto guardada para recuperación: " + foto.getNombreFoto());
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error guardando foto: " + e.getMessage());
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Extrae el conteo del nombre de la foto
     * Formato: cuenta_tipoMedidor_conteo.jpg → retorna "conteo"
     * Ejemplo: 123456_A3_01.jpg → retorna "01"
     */
    private static String extraerConteoDeNombre(String nombreFoto) {
        try {
            if (nombreFoto == null || nombreFoto.isEmpty()) {
                return "01";
            }

            // Quitar extensión
            String sinExtension = nombreFoto.toLowerCase()
                    .replace(".jpg", "")
                    .replace(".jpeg", "")
                    .replace(".png", "");

            // Dividir por guion bajo
            String[] partes = sinExtension.split("_");

            // El conteo es el último elemento
            if (partes.length >= 3) {
                return partes[partes.length - 1];
            }

        } catch (Exception e) {
            Log.e(TAG, "Error extrayendo conteo de " + nombreFoto + ": " + e.getMessage());
        }

        return "01";
    }

    /**
     * Estampa la ruta activa en el registro antes de guardarlo.
     *
     * Se hace en el CRUD, que es el punto unico por el que pasan todas las capturas, en vez
     * de en cada pantalla que inserta. Si el registro ya trae clave (por ejemplo una
     * recuperacion que la reconstruyo), se respeta.
     */
    private static void estamparRutaActiva(EnvioFoto registro) {
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
            RealmQuery<EnvioFoto> q = realm.where(EnvioFoto.class)
                    .beginGroup()
                    .equalTo("estadoEnvio", EnvioFoto.ESTADO_PENDIENTE)
                    .or().equalTo("estadoEnvio", EnvioFoto.ESTADO_ERROR)
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
            RealmQuery<EnvioFoto> q = realm.where(EnvioFoto.class)
                    .beginGroup()
                    .equalTo("claveRuta", claveRuta);
            if (incluirSinRuta) {
                q = q.or().isNull("claveRuta").or().equalTo("claveRuta", "");
            }
            RealmResults<EnvioFoto> filas = q.endGroup().findAll();
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

    /**
     * Rutas locales de las fotos de una ruta. Se usa al borrar: permite eliminar exactamente
     * los archivos de esa ruta dentro de DCIM/FOTOGRAFIASL, que es compartida con la otra.
     */
    public static List<String> rutasLocalesDeRuta(String claveRuta, boolean incluirSinRuta) {
        List<String> rutas = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<EnvioFoto> q = realm.where(EnvioFoto.class)
                    .beginGroup()
                    .equalTo("claveRuta", claveRuta);
            if (incluirSinRuta) {
                q = q.or().isNull("claveRuta").or().equalTo("claveRuta", "");
            }
            for (EnvioFoto foto : q.endGroup().findAll()) {
                String ruta = foto.getRutaLocal();
                if (ruta != null && !ruta.trim().isEmpty()) {
                    rutas.add(ruta);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error listando rutas locales de la ruta: " + e.getMessage());
        }
        return rutas;
    }
}