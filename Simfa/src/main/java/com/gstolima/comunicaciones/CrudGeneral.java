package com.gstolima.comunicaciones;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

/**
 * CrudGeneral - Operaciones sobre el modelo General en Realm.
 *
 * Siempre trabaja sobre el registro id=1 (singleton del dispositivo).
 * Si no existe lo crea con valores por defecto (permisoTrabajo="S").
 *
 * @author Global Solutions & Service S.A.S.
 */
public class CrudGeneral {

    private static final String TAG = "CrudGeneral";
    private static final int ID = 1;

    public CrudGeneral(Context ctx) {
        Realm.init(ctx);
    }

    // =========================================================================
    // OBTENER
    // =========================================================================

    /**
     * Obtiene el registro General (id=1).
     * Si no existe retorna un objeto con valores por defecto sin persistirlo.
     */
    public static General get() {
        try (Realm realm = Realm.getDefaultInstance()) {
            General g = realm.where(General.class).equalTo("id", ID).findFirst();
            if (g != null) return realm.copyFromRealm(g);
        } catch (Exception e) {
            Log.e(TAG, "get() error: " + e.getMessage());
        }
        // No existe aún → retornar default en memoria
        General defecto = new General();
        defecto.setId(ID);
        defecto.setPermisoTrabajo("S");
        return defecto;
    }

    /**
     * Lee solo el permisoTrabajo.
     * Retorna "S" si no hay registro (fail-open: en caso de duda, dejar trabajar).
     */
    public static String getPermisoTrabajo() {
        try (Realm realm = Realm.getDefaultInstance()) {
            General g = realm.where(General.class).equalTo("id", ID).findFirst();
            if (g != null && g.getPermisoTrabajo() != null && !g.getPermisoTrabajo().isEmpty()) {
                return g.getPermisoTrabajo();
            }
        } catch (Exception e) {
            Log.e(TAG, "getPermisoTrabajo() error: " + e.getMessage());
        }
        return "S";
    }

    /**
     * Lee la versión que originó el bloqueo.
     */
    public static String getVersionBloqueada() {
        try (Realm realm = Realm.getDefaultInstance()) {
            General g = realm.where(General.class).equalTo("id", ID).findFirst();
            if (g != null) return g.getVersionBloqueada();
        } catch (Exception e) {
            Log.e(TAG, "getVersionBloqueada() error: " + e.getMessage());
        }
        return "";
    }

    // =========================================================================
    // ACTUALIZAR PERMISO
    // =========================================================================

    /**
     * Actualiza el permiso de trabajo y la versión asociada.
     *
     * @param permiso          "S" = puede trabajar | "N" = bloqueado
     * @param versionBloqueada versión vigente en BD, ej: "Ver5.4 26.01.21" (o "" si se desbloquea)
     * @param versionLocal     versión del APK instalado
     */
    public static boolean updatePermisoTrabajo(String permiso, String versionBloqueada, String versionLocal) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();

            General g = realm.where(General.class).equalTo("id", ID).findFirst();

            if (g == null) {
                // Primera vez → crear registro
                g = realm.createObject(General.class, ID);
            }

            g.setPermisoTrabajo(permiso);
            g.setVersionBloqueada(versionBloqueada != null ? versionBloqueada : "");
            g.setVersionLocal(versionLocal != null ? versionLocal : "");
            g.setFechaVerificacion(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date())
            );

            realm.commitTransaction();
            Log.i(TAG, "updatePermisoTrabajo: permiso=" + permiso + " versionBD=" + versionBloqueada);
            return true;

        } catch (Exception e) {
            if (realm.isInTransaction()) realm.cancelTransaction();
            Log.e(TAG, "updatePermisoTrabajo() error: " + e.getMessage());
            return false;
        } finally {
            realm.close();
        }
    }
}
