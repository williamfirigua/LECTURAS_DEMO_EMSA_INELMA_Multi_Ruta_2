package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.NativeContext;
import io.realm.internal.OsList;
import io.realm.internal.OsMap;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.OsSet;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.core.NativeRealmAny;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_gstolima_comunicaciones_GeneralRealmProxy extends com.gstolima.comunicaciones.General
    implements RealmObjectProxy, com_gstolima_comunicaciones_GeneralRealmProxyInterface {

    static final class GeneralColumnInfo extends ColumnInfo {
        long idColKey;
        long permisoTrabajoColKey;
        long versionBloqueadaColKey;
        long versionLocalColKey;
        long fechaVerificacionColKey;

        GeneralColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("General");
            this.idColKey = addColumnDetails("id", "id", objectSchemaInfo);
            this.permisoTrabajoColKey = addColumnDetails("permisoTrabajo", "permisoTrabajo", objectSchemaInfo);
            this.versionBloqueadaColKey = addColumnDetails("versionBloqueada", "versionBloqueada", objectSchemaInfo);
            this.versionLocalColKey = addColumnDetails("versionLocal", "versionLocal", objectSchemaInfo);
            this.fechaVerificacionColKey = addColumnDetails("fechaVerificacion", "fechaVerificacion", objectSchemaInfo);
        }

        GeneralColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new GeneralColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final GeneralColumnInfo src = (GeneralColumnInfo) rawSrc;
            final GeneralColumnInfo dst = (GeneralColumnInfo) rawDst;
            dst.idColKey = src.idColKey;
            dst.permisoTrabajoColKey = src.permisoTrabajoColKey;
            dst.versionBloqueadaColKey = src.versionBloqueadaColKey;
            dst.versionLocalColKey = src.versionLocalColKey;
            dst.fechaVerificacionColKey = src.fechaVerificacionColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private GeneralColumnInfo columnInfo;
    private ProxyState<com.gstolima.comunicaciones.General> proxyState;

    com_gstolima_comunicaciones_GeneralRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (GeneralColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.gstolima.comunicaciones.General>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.idColKey);
    }

    @Override
    public void realmSet$id(int value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'id' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$permisoTrabajo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.permisoTrabajoColKey);
    }

    @Override
    public void realmSet$permisoTrabajo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.permisoTrabajoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.permisoTrabajoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.permisoTrabajoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.permisoTrabajoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$versionBloqueada() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.versionBloqueadaColKey);
    }

    @Override
    public void realmSet$versionBloqueada(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.versionBloqueadaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.versionBloqueadaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.versionBloqueadaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.versionBloqueadaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$versionLocal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.versionLocalColKey);
    }

    @Override
    public void realmSet$versionLocal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.versionLocalColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.versionLocalColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.versionLocalColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.versionLocalColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaVerificacion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaVerificacionColKey);
    }

    @Override
    public void realmSet$fechaVerificacion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaVerificacionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaVerificacionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaVerificacionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaVerificacionColKey, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "General", false, 5, 0);
        builder.addPersistedProperty(NO_ALIAS, "id", RealmFieldType.INTEGER, Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "permisoTrabajo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "versionBloqueada", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "versionLocal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaVerificacion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static GeneralColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new GeneralColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "General";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "General";
    }

    @SuppressWarnings("cast")
    public static com.gstolima.comunicaciones.General createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.gstolima.comunicaciones.General obj = null;
        if (update) {
            Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
            GeneralColumnInfo columnInfo = (GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = Table.NO_MATCH;
            if (!json.isNull("id")) {
                objKey = table.findFirstLong(pkColumnKey, json.getLong("id"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_gstolima_comunicaciones_GeneralRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_gstolima_comunicaciones_GeneralRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.General.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_gstolima_comunicaciones_GeneralRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.General.class, json.getInt("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_gstolima_comunicaciones_GeneralRealmProxyInterface objProxy = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) obj;
        if (json.has("permisoTrabajo")) {
            if (json.isNull("permisoTrabajo")) {
                objProxy.realmSet$permisoTrabajo(null);
            } else {
                objProxy.realmSet$permisoTrabajo((String) json.getString("permisoTrabajo"));
            }
        }
        if (json.has("versionBloqueada")) {
            if (json.isNull("versionBloqueada")) {
                objProxy.realmSet$versionBloqueada(null);
            } else {
                objProxy.realmSet$versionBloqueada((String) json.getString("versionBloqueada"));
            }
        }
        if (json.has("versionLocal")) {
            if (json.isNull("versionLocal")) {
                objProxy.realmSet$versionLocal(null);
            } else {
                objProxy.realmSet$versionLocal((String) json.getString("versionLocal"));
            }
        }
        if (json.has("fechaVerificacion")) {
            if (json.isNull("fechaVerificacion")) {
                objProxy.realmSet$fechaVerificacion(null);
            } else {
                objProxy.realmSet$fechaVerificacion((String) json.getString("fechaVerificacion"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.gstolima.comunicaciones.General createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.gstolima.comunicaciones.General obj = new com.gstolima.comunicaciones.General();
        final com_gstolima_comunicaciones_GeneralRealmProxyInterface objProxy = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("id")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$id((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'id' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("permisoTrabajo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$permisoTrabajo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$permisoTrabajo(null);
                }
            } else if (name.equals("versionBloqueada")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$versionBloqueada((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$versionBloqueada(null);
                }
            } else if (name.equals("versionLocal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$versionLocal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$versionLocal(null);
                }
            } else if (name.equals("fechaVerificacion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaVerificacion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaVerificacion(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
        }
        return realm.copyToRealmOrUpdate(obj);
    }

    static com_gstolima_comunicaciones_GeneralRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class), false, Collections.<String>emptyList());
        io.realm.com_gstolima_comunicaciones_GeneralRealmProxy obj = new io.realm.com_gstolima_comunicaciones_GeneralRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.gstolima.comunicaciones.General copyOrUpdate(Realm realm, GeneralColumnInfo columnInfo, com.gstolima.comunicaciones.General object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.General) cachedRealmObject;
        }

        com.gstolima.comunicaciones.General realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = table.findFirstLong(pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_gstolima_comunicaciones_GeneralRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.gstolima.comunicaciones.General copy(Realm realm, GeneralColumnInfo columnInfo, com.gstolima.comunicaciones.General newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.General) cachedRealmObject;
        }

        com_gstolima_comunicaciones_GeneralRealmProxyInterface unmanagedSource = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) newObject;

        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.idColKey, unmanagedSource.realmGet$id());
        builder.addString(columnInfo.permisoTrabajoColKey, unmanagedSource.realmGet$permisoTrabajo());
        builder.addString(columnInfo.versionBloqueadaColKey, unmanagedSource.realmGet$versionBloqueada());
        builder.addString(columnInfo.versionLocalColKey, unmanagedSource.realmGet$versionLocal());
        builder.addString(columnInfo.fechaVerificacionColKey, unmanagedSource.realmGet$fechaVerificacion());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_gstolima_comunicaciones_GeneralRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.gstolima.comunicaciones.General object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        long tableNativePtr = table.getNativePtr();
        GeneralColumnInfo columnInfo = (GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$permisoTrabajo = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$permisoTrabajo();
        if (realmGet$permisoTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, realmGet$permisoTrabajo, false);
        }
        String realmGet$versionBloqueada = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionBloqueada();
        if (realmGet$versionBloqueada != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, realmGet$versionBloqueada, false);
        }
        String realmGet$versionLocal = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionLocal();
        if (realmGet$versionLocal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.versionLocalColKey, objKey, realmGet$versionLocal, false);
        }
        String realmGet$fechaVerificacion = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$fechaVerificacion();
        if (realmGet$fechaVerificacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, realmGet$fechaVerificacion, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        long tableNativePtr = table.getNativePtr();
        GeneralColumnInfo columnInfo = (GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.General object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.General) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$permisoTrabajo = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$permisoTrabajo();
            if (realmGet$permisoTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, realmGet$permisoTrabajo, false);
            }
            String realmGet$versionBloqueada = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionBloqueada();
            if (realmGet$versionBloqueada != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, realmGet$versionBloqueada, false);
            }
            String realmGet$versionLocal = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionLocal();
            if (realmGet$versionLocal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.versionLocalColKey, objKey, realmGet$versionLocal, false);
            }
            String realmGet$fechaVerificacion = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$fechaVerificacion();
            if (realmGet$fechaVerificacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, realmGet$fechaVerificacion, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.gstolima.comunicaciones.General object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        long tableNativePtr = table.getNativePtr();
        GeneralColumnInfo columnInfo = (GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
        }
        cache.put(object, objKey);
        String realmGet$permisoTrabajo = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$permisoTrabajo();
        if (realmGet$permisoTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, realmGet$permisoTrabajo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, false);
        }
        String realmGet$versionBloqueada = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionBloqueada();
        if (realmGet$versionBloqueada != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, realmGet$versionBloqueada, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, false);
        }
        String realmGet$versionLocal = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionLocal();
        if (realmGet$versionLocal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.versionLocalColKey, objKey, realmGet$versionLocal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.versionLocalColKey, objKey, false);
        }
        String realmGet$fechaVerificacion = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$fechaVerificacion();
        if (realmGet$fechaVerificacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, realmGet$fechaVerificacion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        long tableNativePtr = table.getNativePtr();
        GeneralColumnInfo columnInfo = (GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.General object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.General) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$id());
            }
            cache.put(object, objKey);
            String realmGet$permisoTrabajo = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$permisoTrabajo();
            if (realmGet$permisoTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, realmGet$permisoTrabajo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.permisoTrabajoColKey, objKey, false);
            }
            String realmGet$versionBloqueada = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionBloqueada();
            if (realmGet$versionBloqueada != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, realmGet$versionBloqueada, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.versionBloqueadaColKey, objKey, false);
            }
            String realmGet$versionLocal = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$versionLocal();
            if (realmGet$versionLocal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.versionLocalColKey, objKey, realmGet$versionLocal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.versionLocalColKey, objKey, false);
            }
            String realmGet$fechaVerificacion = ((com_gstolima_comunicaciones_GeneralRealmProxyInterface) object).realmGet$fechaVerificacion();
            if (realmGet$fechaVerificacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, realmGet$fechaVerificacion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaVerificacionColKey, objKey, false);
            }
        }
    }

    public static com.gstolima.comunicaciones.General createDetachedCopy(com.gstolima.comunicaciones.General realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.gstolima.comunicaciones.General unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.gstolima.comunicaciones.General();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.gstolima.comunicaciones.General) cachedObject.object;
            }
            unmanagedObject = (com.gstolima.comunicaciones.General) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_gstolima_comunicaciones_GeneralRealmProxyInterface unmanagedCopy = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) unmanagedObject;
        com_gstolima_comunicaciones_GeneralRealmProxyInterface realmSource = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) realmObject;
        Realm objectRealm = (Realm) ((RealmObjectProxy) realmObject).realmGet$proxyState().getRealm$realm();
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());
        unmanagedCopy.realmSet$permisoTrabajo(realmSource.realmGet$permisoTrabajo());
        unmanagedCopy.realmSet$versionBloqueada(realmSource.realmGet$versionBloqueada());
        unmanagedCopy.realmSet$versionLocal(realmSource.realmGet$versionLocal());
        unmanagedCopy.realmSet$fechaVerificacion(realmSource.realmGet$fechaVerificacion());

        return unmanagedObject;
    }

    static com.gstolima.comunicaciones.General update(Realm realm, GeneralColumnInfo columnInfo, com.gstolima.comunicaciones.General realmObject, com.gstolima.comunicaciones.General newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_gstolima_comunicaciones_GeneralRealmProxyInterface realmObjectTarget = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) realmObject;
        com_gstolima_comunicaciones_GeneralRealmProxyInterface realmObjectSource = (com_gstolima_comunicaciones_GeneralRealmProxyInterface) newObject;
        Table table = realm.getTable(com.gstolima.comunicaciones.General.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addInteger(columnInfo.idColKey, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.permisoTrabajoColKey, realmObjectSource.realmGet$permisoTrabajo());
        builder.addString(columnInfo.versionBloqueadaColKey, realmObjectSource.realmGet$versionBloqueada());
        builder.addString(columnInfo.versionLocalColKey, realmObjectSource.realmGet$versionLocal());
        builder.addString(columnInfo.fechaVerificacionColKey, realmObjectSource.realmGet$fechaVerificacion());

        builder.updateExistingTopLevelObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("General = proxy[");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{permisoTrabajo:");
        stringBuilder.append(realmGet$permisoTrabajo() != null ? realmGet$permisoTrabajo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{versionBloqueada:");
        stringBuilder.append(realmGet$versionBloqueada() != null ? realmGet$versionBloqueada() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{versionLocal:");
        stringBuilder.append(realmGet$versionLocal() != null ? realmGet$versionLocal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaVerificacion:");
        stringBuilder.append(realmGet$fechaVerificacion() != null ? realmGet$fechaVerificacion() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long objKey = proxyState.getRow$realm().getObjectKey();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (objKey ^ (objKey >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com_gstolima_comunicaciones_GeneralRealmProxy aGeneral = (com_gstolima_comunicaciones_GeneralRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aGeneral.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aGeneral.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aGeneral.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
