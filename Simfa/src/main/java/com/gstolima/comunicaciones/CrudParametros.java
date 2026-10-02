package com.gstolima.comunicaciones;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;

/**
 * CrudParametros - CRUD Realm para la tabla de parámetros de configuración
 *
 * Estructura de la tabla 'Parametro':
 * id|nomParametro  |valor1           |valor2|valor3|
 * --+--------------+-----------------+------+------+
 *  1|Enrutador     |ENRUTADOR_CELSIA |      |      |
 *  2|carpetaFotos  |FOTOGRAFIAS      |CIC   |ENT   |
 *  3|tablas_basicas|TERMINAL         |      |      |
 *  4|soporte       |ARCHIVOSSOPORTE  |      |      |
 *  5|PathEnrutador |E:/              |      |      |
 *  6|jornada       |CONTROL INICIO   |5     |19    |
 *  7|tiempo        |CONTROL INTERVALO|10000 |      |
 *
 * @author Global Solutions & Service S.A.S.
 */
public class CrudParametros {

    private static final String TAG = "CrudParametros";

    public CrudParametros(Context ctx) {
        Realm.init(ctx);
    }

    /**
     * Inserta un nuevo parámetro
     */
    public static String setParametro(Parametro param) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Parametro p = realm.createObject(Parametro.class, param.getId());
            p.setNomParametro(param.getNomParametro());
            p.setValor1(param.getValor1());
            p.setValor2(param.getValor2());
            p.setValor3(param.getValor3());
            realm.commitTransaction();
            return "INSERCCION|ID:" + p.getId() + " EXITOSA";
        } catch (Exception e) {
            Log.e(TAG, "Error insertando parámetro: " + e.getMessage());
            return "ERROR|" + e.toString();
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Actualiza un parámetro existente
     */
    public static boolean updateParametro(Parametro param) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Parametro p = realm.where(Parametro.class).equalTo("id", param.getId()).findFirst();
            if (p != null) {
                p.setNomParametro(param.getNomParametro());
                p.setValor1(param.getValor1());
                p.setValor2(param.getValor2());
                p.setValor3(param.getValor3());
                realm.insertOrUpdate(p);
                realm.commitTransaction();
                return true;
            } else {
                realm.commitTransaction();
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando parámetro: " + e.getMessage());
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Obtiene un parámetro por su ID
     */
    public static Parametro getParametro(int id) {
        Parametro datos = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            Parametro p = realm.where(Parametro.class).equalTo("id", id).findFirst();
            if (p != null) datos = realm.copyFromRealm(p);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo parámetro id=" + id + ": " + e.getMessage());
        }
        return datos;
    }

    /**
     * Obtiene un parámetro por su nombre
     */
    public static Parametro getParametroPorNombre(String nombre) {
        Parametro datos = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            Parametro p = realm.where(Parametro.class).equalTo("nomParametro", nombre).findFirst();
            if (p != null) datos = realm.copyFromRealm(p);
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo parámetro nombre=" + nombre + ": " + e.getMessage());
        }
        return datos;
    }

    /**
     * Obtiene todos los parámetros
     */
    public static java.util.List<Parametro> getAllParametros() {
        java.util.List<Parametro> lista = new java.util.ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            lista = realm.copyFromRealm(realm.where(Parametro.class).findAll());
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo todos los parámetros: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el valor de la jornada (hora inicio y fin)
     * @return int[] con {horaInicio, horaFin} o null si no existe
     */
    public static int[] getJornada() {
        Parametro p = getParametro(6); // id=6 es jornada
        if (p != null && "jornada".equals(p.getNomParametro())) {
            try {
                int inicio = Integer.parseInt(p.getValor2().trim());
                int fin = Integer.parseInt(p.getValor3().trim());
                return new int[]{inicio, fin};
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parseando jornada: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Obtiene el intervalo de sincronización en milisegundos
     * @return Intervalo en ms o valor por defecto (120000) si no existe
     */
    public static long getIntervalo() {
        Parametro p = getParametro(7); // id=7 es tiempo
        if (p != null && "tiempo".equals(p.getNomParametro())) {
            try {
                return Long.parseLong(p.getValor2().trim());
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parseando intervalo: " + e.getMessage());
            }
        }
        return 120000L; // Default 2 minutos
    }

    /**
     * Obtiene el nombre del enrutador
     */
    public static String getEnrutador() {
        Parametro p = getParametro(1);
        return (p != null) ? p.getValor1() : "";
    }

    /**
     * Obtiene la ruta del path enrutador
     */
    public static String getPathEnrutador() {
        Parametro p = getParametro(5);
        return (p != null) ? p.getValor1() : "";
    }

    /**
     * Obtiene la carpeta de fotos configurada
     */
    public static String getCarpetaFotos() {
        Parametro p = getParametro(2);
        return (p != null) ? p.getValor1() : "FOTOGRAFIAS";
    }

    /** Valor por defecto y limites del tamano de lote por ciclo. */
    public static final int MAX_REG_ENVIO_DEFAULT = 50;
    public static final int MAX_REG_ENVIO_MIN     = 1;
    public static final int MAX_REG_ENVIO_MAX     = 500;

    /**
     * Lecturas a procesar por ciclo de sincronizacion (parametro id=8).
     *
     * Es distinto del tamano de request HTTP: LecturaSyncService trocea este
     * total en lotes de HTTP_CHUNK_LECTURAS, de modo que subirlo no produce
     * un POST gigante sino mas requests.
     *
     * Siempre acotado a [MIN, MAX]: un valor corrupto en base de datos no debe
     * poder dejar el envio en cero ni reventar el servidor.
     */
    public static int getMaxRegEnvio() {
        Parametro p = getParametro(8);
        if (p == null) return MAX_REG_ENVIO_DEFAULT;
        try {
            int valor = Integer.parseInt(p.getValor2().trim());
            return Math.max(MAX_REG_ENVIO_MIN, Math.min(MAX_REG_ENVIO_MAX, valor));
        } catch (Exception e) {
            Log.w(TAG, "maxRegEnvio invalido, se usa el valor por defecto");
            return MAX_REG_ENVIO_DEFAULT;
        }
    }

    /**
     * Actualiza el tamano de lote. Hace upsert: si el parametro no existia
     * (instalacion previa a esta version) lo crea.
     */
    public static boolean updateMaxRegEnvio(int maxReg) {
        int valor = Math.max(MAX_REG_ENVIO_MIN, Math.min(MAX_REG_ENVIO_MAX, maxReg));

        Parametro p = getParametro(8);
        if (p == null) {
            Parametro nuevo = new Parametro();
            nuevo.setId(8);
            nuevo.setNomParametro("maxRegEnvio");
            nuevo.setValor1("MAX REGISTROS POR ENVIO");
            nuevo.setValor2(String.valueOf(valor));
            nuevo.setValor3("");
            return setParametro(nuevo) != null;
        }

        p.setValor2(String.valueOf(valor));
        return updateParametro(p);
    }

    /**
     * Inserta los parámetros por defecto si no existen
     */
    public static void insertarParametrosPorDefecto() {
        if (getParametro(1) == null) {
            Parametro p1 = new Parametro();
            p1.setId(1);
            p1.setNomParametro("Enrutador");
            p1.setValor1("ENRUTADOR_EMSA");
            p1.setValor2("");
            p1.setValor3("");
            setParametro(p1);
        }

        if (getParametro(2) == null) {
            Parametro p2 = new Parametro();
            p2.setId(2);
            p2.setNomParametro("carpetaFotos");
            p2.setValor1("FOTOGRAFIAS");
            p2.setValor2("CIC");
            p2.setValor3("ENT");
            setParametro(p2);
        }

        if (getParametro(6) == null) {
            Parametro p6 = new Parametro();
            p6.setId(6);
            p6.setNomParametro("jornada");
            p6.setValor1("CONTROL INICIO");
            p6.setValor2("5");
            p6.setValor3("19");
            setParametro(p6);
        }

        if (getParametro(7) == null) {
            Parametro p7 = new Parametro();
            p7.setId(7);
            p7.setNomParametro("tiempo");
            p7.setValor1("CONTROL INTERVALO");
            p7.setValor2("120000");
            p7.setValor3("");
            setParametro(p7);
        }

        if (getParametro(8) == null) {
            Parametro p8 = new Parametro();
            p8.setId(8);
            p8.setNomParametro("maxRegEnvio");
            p8.setValor1("MAX REGISTROS POR ENVIO");
            p8.setValor2(String.valueOf(MAX_REG_ENVIO_DEFAULT));
            p8.setValor3("");
            setParametro(p8);
        }
    }

    /**
     * Actualiza solo el valor de la jornada
     */
    public static boolean updateJornada(int horaInicio, int horaFin) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Parametro p = realm.where(Parametro.class).equalTo("id", 6).findFirst();
            if (p != null) {
                p.setValor2(String.valueOf(horaInicio));
                p.setValor3(String.valueOf(horaFin));
                realm.insertOrUpdate(p);
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando jornada: " + e.getMessage());
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }

    /**
     * Actualiza solo el valor del intervalo
     */
    public static boolean updateIntervalo(long intervaloMs) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Parametro p = realm.where(Parametro.class).equalTo("id", 7).findFirst();
            if (p != null) {
                p.setValor2(String.valueOf(intervaloMs));
                realm.insertOrUpdate(p);
                realm.commitTransaction();
                return true;
            }
            realm.commitTransaction();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando intervalo: " + e.getMessage());
            return false;
        } finally {
            if (realm != null) realm.close();
        }
    }
}