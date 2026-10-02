package com.gstolima.comunicaciones;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Configuracion unica de Realm. Se instala una sola vez, al arrancar la app
 * (MainApplication), antes de que cualquier pantalla pida una instancia.
 *
 * Antes no habia configuracion explicita: solo Realm.init() repetido en cada CRUD, lo que
 * dejaba la base en la version 0 sin migracion. Cualquier campo nuevo obligaba a desinstalar.
 */
public final class RealmConfig {

    private static final String TAG = "RealmConfig";

    /** Subir de a uno y agregar el bloque correspondiente en RealmMigracion. */
    public static final long VERSION_ESQUEMA = 1;

    private RealmConfig() {
    }

    public static void instalar(Context context) {
        try {
            Realm.init(context);
            Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                    .name("default.realm")
                    .schemaVersion(VERSION_ESQUEMA)
                    .migration(new RealmMigracion())
                    .build());
            Log.i(TAG, "Realm configurado en la version de esquema " + VERSION_ESQUEMA);
        } catch (Exception e) {
            // Nunca impedir el arranque: los CRUD siguen llamando Realm.init() por su cuenta.
            Log.e(TAG, "No se pudo instalar la configuracion de Realm", e);
        }
    }
}