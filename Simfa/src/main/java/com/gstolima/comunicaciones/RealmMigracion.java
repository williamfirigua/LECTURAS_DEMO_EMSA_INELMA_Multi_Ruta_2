package com.gstolima.comunicaciones;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Migracion del esquema de Realm.
 *
 * POR QUE EXISTE: antes no habia RealmConfiguration ni schemaVersion, solo Realm.init().
 * Con eso, agregar un campo a cualquier modelo reventaba la app al arrancar
 * (RealmMigrationNeededException) en todo equipo que actualizara sin desinstalar.
 * Con esta clase, cada cambio futuro de esquema se resuelve agregando un bloque mas aqui.
 *
 * REGLA AL AGREGAR CAMPOS NUEVOS:
 *  1. Subir VERSION_ESQUEMA en RealmConfig.
 *  2. Agregar aqui un bloque "if (version == N)" que cree los campos y suba version.
 *  3. Nunca modificar un bloque ya publicado: los equipos en campo pueden venir de esa version.
 */
public class RealmMigracion implements RealmMigration {

    private static final String TAG = "RealmMigracion";

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        long version = oldVersion;

        Log.i(TAG, "Migrando Realm de la version " + oldVersion + " a la " + newVersion);

        // ---- v0 -> v1: identidad de ruta y modulo de trabajo en cada fila pendiente ----
        // Antes, el servicio de envio usaba un unico moduloTrabajo global para todo lo que
        // mandaba. Con dos rutas cargadas a la vez (una de lecturas y otra de entregas) los
        // pendientes de una se enviaban con el modulo de la otra, y llegaban a la carpeta
        // equivocada del servidor. Guardando el dato por fila, cada registro se envia con lo
        // que tenia al capturarse.
        if (version == 0) {
            agregarCamposDeRuta(schema, "EnvioLectura");
            agregarCamposDeRuta(schema, "EnvioFoto");
            agregarCamposDeRuta(schema, "EnvioCuentaNueva");
            version++;
        }

        if (version != newVersion) {
            Log.e(TAG, "Quedo sin migrar de la version " + version + " a la " + newVersion);
        }
    }

    /**
     * Agrega claveRuta y moduloTrabajo si no existen. Las filas viejas quedan con estos campos
     * vacios; quien los consume debe tolerarlo y caer al valor global (ver LecturaSyncService).
     */
    private void agregarCamposDeRuta(RealmSchema schema, String nombreModelo) {
        RealmObjectSchema modelo = schema.get(nombreModelo);
        if (modelo == null) {
            Log.w(TAG, "No existe el modelo " + nombreModelo + ", se omite");
            return;
        }
        if (!modelo.hasField("claveRuta")) {
            modelo.addField("claveRuta", String.class)
                    .addIndex("claveRuta")
                    .transform(fila -> fila.setString("claveRuta", ""));
        }
        if (!modelo.hasField("moduloTrabajo")) {
            modelo.addField("moduloTrabajo", String.class)
                    .transform(fila -> fila.setString("moduloTrabajo", ""));
        }
        Log.i(TAG, "Campos de ruta agregados a " + nombreModelo);
    }

    // Dos migraciones distintas con la misma version deben considerarse equivalentes.
    @Override
    public boolean equals(Object otro) {
        return otro instanceof RealmMigracion;
    }

    @Override
    public int hashCode() {
        return RealmMigracion.class.hashCode();
    }
}