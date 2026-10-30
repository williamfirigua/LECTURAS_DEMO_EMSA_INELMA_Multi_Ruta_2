package com.gstolima.accesoyseguridad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Cola persistente del rastreo GPS.
 *
 * Archivo SQLite propio ("tracking_cola.db"), independiente de Realm: no toca
 * el esquema ni el archivo de lecturas, así que no requiere migración y un
 * problema aquí no puede afectar la cola de lecturas ni la impresión.
 *
 * Un punto se borra SOLO después de que el servidor confirmó el lote. Si el
 * proceso muere, el teléfono se apaga o pasa días sin cobertura, los puntos
 * siguen en disco y se envían cuando vuelva la señal.
 *
 * Thread-safe: SQLiteDatabase serializa el acceso a su única conexión.
 */
final class TrackingStore extends SQLiteOpenHelper {

    private static final String DB_NAME    = "tracking_cola.db";
    private static final int    DB_VERSION = 1;

    private static final String T          = "punto";
    private static final String C_ID       = "id";
    private static final String C_UID      = "uid";
    private static final String C_LAT      = "lat";
    private static final String C_LNG      = "lng";
    private static final String C_PREC     = "precision_m";
    private static final String C_CAPT_MS  = "capturado_ms";

    static final class Punto {
        final long   id;
        final String uid;
        final double lat;
        final double lng;
        final Float  precision;
        final long   capturadoMs;

        Punto(long id, String uid, double lat, double lng, Float precision, long capturadoMs) {
            this.id = id;
            this.uid = uid;
            this.lat = lat;
            this.lng = lng;
            this.precision = precision;
            this.capturadoMs = capturadoMs;
        }
    }

    private static volatile TrackingStore instancia;

    static TrackingStore get(Context ctx) {
        TrackingStore s = instancia;
        if (s == null) {
            synchronized (TrackingStore.class) {
                s = instancia;
                if (s == null) {
                    s = new TrackingStore(ctx.getApplicationContext());
                    instancia = s;
                }
            }
        }
        return s;
    }

    private TrackingStore(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        // WAL: escrituras atómicas y lecturas sin bloquear la inserción de puntos.
        db.enableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + C_UID + " TEXT NOT NULL, "
                + C_LAT + " REAL NOT NULL, "
                + C_LNG + " REAL NOT NULL, "
                + C_PREC + " REAL, "
                + C_CAPT_MS + " INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX idx_punto_capt ON " + T + "(" + C_CAPT_MS + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // v1: sin migraciones. Nunca borrar la tabla aquí: contiene puntos sin enviar.
    }

    void insertar(String uid, double lat, double lng, Float precision, long capturadoMs) {
        ContentValues v = new ContentValues(5);
        v.put(C_UID, uid);
        v.put(C_LAT, lat);
        v.put(C_LNG, lng);
        if (precision != null) v.put(C_PREC, precision);
        v.put(C_CAPT_MS, capturadoMs);
        getWritableDatabase().insertOrThrow(T, null, v);
    }

    /** Los más antiguos primero, para que el servidor reciba la jornada en orden. */
    List<Punto> siguienteLote(int limite) {
        List<Punto> lote = new ArrayList<>(limite);
        try (Cursor c = getReadableDatabase().query(T,
                new String[]{C_ID, C_UID, C_LAT, C_LNG, C_PREC, C_CAPT_MS},
                null, null, null, null, C_ID, String.valueOf(limite))) {
            while (c.moveToNext()) {
                lote.add(new Punto(
                        c.getLong(0),
                        c.getString(1),
                        c.getDouble(2),
                        c.getDouble(3),
                        c.isNull(4) ? null : c.getFloat(4),
                        c.getLong(5)));
            }
        }
        return lote;
    }

    /** Borra exactamente los puntos de un lote confirmado por el servidor. */
    void borrar(List<Punto> lote) {
        if (lote.isEmpty()) return;
        StringBuilder in = new StringBuilder(lote.size() * 6);
        String[] args = new String[lote.size()];
        for (int i = 0; i < lote.size(); i++) {
            in.append(i == 0 ? "?" : ",?");
            args[i] = String.valueOf(lote.get(i).id);
        }
        getWritableDatabase().delete(T, C_ID + " IN (" + in + ")", args);
    }

    /**
     * Acota la cola: descarta puntos capturados antes de minCapturadoMs (el
     * servidor ya no los aceptaría) y, si aun así supera maxFilas, los más
     * antiguos. La posición reciente vale más que la de hace días.
     *
     * @return filas descartadas
     */
    int podar(long minCapturadoMs, int maxFilas) {
        SQLiteDatabase db = getWritableDatabase();
        int borradas = db.delete(T, C_CAPT_MS + " < ?", new String[]{String.valueOf(minCapturadoMs)});
        borradas += db.delete(T,
                C_ID + " <= (SELECT " + C_ID + " FROM " + T
                        + " ORDER BY " + C_ID + " DESC LIMIT 1 OFFSET " + maxFilas + ")",
                null);
        return borradas;
    }
}
