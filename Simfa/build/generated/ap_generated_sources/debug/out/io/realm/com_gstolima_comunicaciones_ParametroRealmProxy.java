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
public class com_gstolima_comunicaciones_ParametroRealmProxy extends com.gstolima.comunicaciones.Parametro
    implements RealmObjectProxy, com_gstolima_comunicaciones_ParametroRealmProxyInterface {

    static final class ParametroColumnInfo extends ColumnInfo {
        long idColKey;
        long nomParametroColKey;
        long valor1ColKey;
        long valor2ColKey;
        long valor3ColKey;

        ParametroColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Parametro");
            this.idColKey = addColumnDetails("id", "id", objectSchemaInfo);
            this.nomParametroColKey = addColumnDetails("nomParametro", "nomParametro", objectSchemaInfo);
            this.valor1ColKey = addColumnDetails("valor1", "valor1", objectSchemaInfo);
            this.valor2ColKey = addColumnDetails("valor2", "valor2", objectSchemaInfo);
            this.valor3ColKey = addColumnDetails("valor3", "valor3", objectSchemaInfo);
        }

        ParametroColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ParametroColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ParametroColumnInfo src = (ParametroColumnInfo) rawSrc;
            final ParametroColumnInfo dst = (ParametroColumnInfo) rawDst;
            dst.idColKey = src.idColKey;
            dst.nomParametroColKey = src.nomParametroColKey;
            dst.valor1ColKey = src.valor1ColKey;
            dst.valor2ColKey = src.valor2ColKey;
            dst.valor3ColKey = src.valor3ColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ParametroColumnInfo columnInfo;
    private ProxyState<com.gstolima.comunicaciones.Parametro> proxyState;

    com_gstolima_comunicaciones_ParametroRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ParametroColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.gstolima.comunicaciones.Parametro>(this);
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
    public String realmGet$nomParametro() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nomParametroColKey);
    }

    @Override
    public void realmSet$nomParametro(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nomParametroColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.nomParametroColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nomParametroColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nomParametroColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$valor1() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.valor1ColKey);
    }

    @Override
    public void realmSet$valor1(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.valor1ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.valor1ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.valor1ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.valor1ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$valor2() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.valor2ColKey);
    }

    @Override
    public void realmSet$valor2(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.valor2ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.valor2ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.valor2ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.valor2ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$valor3() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.valor3ColKey);
    }

    @Override
    public void realmSet$valor3(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.valor3ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.valor3ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.valor3ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.valor3ColKey, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "Parametro", false, 5, 0);
        builder.addPersistedProperty(NO_ALIAS, "id", RealmFieldType.INTEGER, Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nomParametro", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "valor1", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "valor2", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "valor3", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ParametroColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ParametroColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Parametro";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Parametro";
    }

    @SuppressWarnings("cast")
    public static com.gstolima.comunicaciones.Parametro createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.gstolima.comunicaciones.Parametro obj = null;
        if (update) {
            Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
            ParametroColumnInfo columnInfo = (ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = Table.NO_MATCH;
            if (!json.isNull("id")) {
                objKey = table.findFirstLong(pkColumnKey, json.getLong("id"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_gstolima_comunicaciones_ParametroRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_gstolima_comunicaciones_ParametroRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.Parametro.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_gstolima_comunicaciones_ParametroRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.Parametro.class, json.getInt("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_gstolima_comunicaciones_ParametroRealmProxyInterface objProxy = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) obj;
        if (json.has("nomParametro")) {
            if (json.isNull("nomParametro")) {
                objProxy.realmSet$nomParametro(null);
            } else {
                objProxy.realmSet$nomParametro((String) json.getString("nomParametro"));
            }
        }
        if (json.has("valor1")) {
            if (json.isNull("valor1")) {
                objProxy.realmSet$valor1(null);
            } else {
                objProxy.realmSet$valor1((String) json.getString("valor1"));
            }
        }
        if (json.has("valor2")) {
            if (json.isNull("valor2")) {
                objProxy.realmSet$valor2(null);
            } else {
                objProxy.realmSet$valor2((String) json.getString("valor2"));
            }
        }
        if (json.has("valor3")) {
            if (json.isNull("valor3")) {
                objProxy.realmSet$valor3(null);
            } else {
                objProxy.realmSet$valor3((String) json.getString("valor3"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.gstolima.comunicaciones.Parametro createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.gstolima.comunicaciones.Parametro obj = new com.gstolima.comunicaciones.Parametro();
        final com_gstolima_comunicaciones_ParametroRealmProxyInterface objProxy = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) obj;
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
            } else if (name.equals("nomParametro")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nomParametro((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nomParametro(null);
                }
            } else if (name.equals("valor1")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$valor1((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$valor1(null);
                }
            } else if (name.equals("valor2")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$valor2((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$valor2(null);
                }
            } else if (name.equals("valor3")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$valor3((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$valor3(null);
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

    static com_gstolima_comunicaciones_ParametroRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class), false, Collections.<String>emptyList());
        io.realm.com_gstolima_comunicaciones_ParametroRealmProxy obj = new io.realm.com_gstolima_comunicaciones_ParametroRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.gstolima.comunicaciones.Parametro copyOrUpdate(Realm realm, ParametroColumnInfo columnInfo, com.gstolima.comunicaciones.Parametro object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.gstolima.comunicaciones.Parametro) cachedRealmObject;
        }

        com.gstolima.comunicaciones.Parametro realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = table.findFirstLong(pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_gstolima_comunicaciones_ParametroRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.gstolima.comunicaciones.Parametro copy(Realm realm, ParametroColumnInfo columnInfo, com.gstolima.comunicaciones.Parametro newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.Parametro) cachedRealmObject;
        }

        com_gstolima_comunicaciones_ParametroRealmProxyInterface unmanagedSource = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) newObject;

        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.idColKey, unmanagedSource.realmGet$id());
        builder.addString(columnInfo.nomParametroColKey, unmanagedSource.realmGet$nomParametro());
        builder.addString(columnInfo.valor1ColKey, unmanagedSource.realmGet$valor1());
        builder.addString(columnInfo.valor2ColKey, unmanagedSource.realmGet$valor2());
        builder.addString(columnInfo.valor3ColKey, unmanagedSource.realmGet$valor3());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_gstolima_comunicaciones_ParametroRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.gstolima.comunicaciones.Parametro object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        long tableNativePtr = table.getNativePtr();
        ParametroColumnInfo columnInfo = (ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$nomParametro = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$nomParametro();
        if (realmGet$nomParametro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nomParametroColKey, objKey, realmGet$nomParametro, false);
        }
        String realmGet$valor1 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor1();
        if (realmGet$valor1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor1ColKey, objKey, realmGet$valor1, false);
        }
        String realmGet$valor2 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor2();
        if (realmGet$valor2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor2ColKey, objKey, realmGet$valor2, false);
        }
        String realmGet$valor3 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor3();
        if (realmGet$valor3 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor3ColKey, objKey, realmGet$valor3, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        long tableNativePtr = table.getNativePtr();
        ParametroColumnInfo columnInfo = (ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.Parametro object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.Parametro) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$nomParametro = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$nomParametro();
            if (realmGet$nomParametro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nomParametroColKey, objKey, realmGet$nomParametro, false);
            }
            String realmGet$valor1 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor1();
            if (realmGet$valor1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor1ColKey, objKey, realmGet$valor1, false);
            }
            String realmGet$valor2 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor2();
            if (realmGet$valor2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor2ColKey, objKey, realmGet$valor2, false);
            }
            String realmGet$valor3 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor3();
            if (realmGet$valor3 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor3ColKey, objKey, realmGet$valor3, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.gstolima.comunicaciones.Parametro object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        long tableNativePtr = table.getNativePtr();
        ParametroColumnInfo columnInfo = (ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
        }
        cache.put(object, objKey);
        String realmGet$nomParametro = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$nomParametro();
        if (realmGet$nomParametro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nomParametroColKey, objKey, realmGet$nomParametro, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nomParametroColKey, objKey, false);
        }
        String realmGet$valor1 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor1();
        if (realmGet$valor1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor1ColKey, objKey, realmGet$valor1, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.valor1ColKey, objKey, false);
        }
        String realmGet$valor2 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor2();
        if (realmGet$valor2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor2ColKey, objKey, realmGet$valor2, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.valor2ColKey, objKey, false);
        }
        String realmGet$valor3 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor3();
        if (realmGet$valor3 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valor3ColKey, objKey, realmGet$valor3, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.valor3ColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        long tableNativePtr = table.getNativePtr();
        ParametroColumnInfo columnInfo = (ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.Parametro object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.Parametro) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$id());
            }
            cache.put(object, objKey);
            String realmGet$nomParametro = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$nomParametro();
            if (realmGet$nomParametro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nomParametroColKey, objKey, realmGet$nomParametro, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nomParametroColKey, objKey, false);
            }
            String realmGet$valor1 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor1();
            if (realmGet$valor1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor1ColKey, objKey, realmGet$valor1, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.valor1ColKey, objKey, false);
            }
            String realmGet$valor2 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor2();
            if (realmGet$valor2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor2ColKey, objKey, realmGet$valor2, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.valor2ColKey, objKey, false);
            }
            String realmGet$valor3 = ((com_gstolima_comunicaciones_ParametroRealmProxyInterface) object).realmGet$valor3();
            if (realmGet$valor3 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valor3ColKey, objKey, realmGet$valor3, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.valor3ColKey, objKey, false);
            }
        }
    }

    public static com.gstolima.comunicaciones.Parametro createDetachedCopy(com.gstolima.comunicaciones.Parametro realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.gstolima.comunicaciones.Parametro unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.gstolima.comunicaciones.Parametro();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.gstolima.comunicaciones.Parametro) cachedObject.object;
            }
            unmanagedObject = (com.gstolima.comunicaciones.Parametro) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_gstolima_comunicaciones_ParametroRealmProxyInterface unmanagedCopy = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) unmanagedObject;
        com_gstolima_comunicaciones_ParametroRealmProxyInterface realmSource = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) realmObject;
        Realm objectRealm = (Realm) ((RealmObjectProxy) realmObject).realmGet$proxyState().getRealm$realm();
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());
        unmanagedCopy.realmSet$nomParametro(realmSource.realmGet$nomParametro());
        unmanagedCopy.realmSet$valor1(realmSource.realmGet$valor1());
        unmanagedCopy.realmSet$valor2(realmSource.realmGet$valor2());
        unmanagedCopy.realmSet$valor3(realmSource.realmGet$valor3());

        return unmanagedObject;
    }

    static com.gstolima.comunicaciones.Parametro update(Realm realm, ParametroColumnInfo columnInfo, com.gstolima.comunicaciones.Parametro realmObject, com.gstolima.comunicaciones.Parametro newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_gstolima_comunicaciones_ParametroRealmProxyInterface realmObjectTarget = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) realmObject;
        com_gstolima_comunicaciones_ParametroRealmProxyInterface realmObjectSource = (com_gstolima_comunicaciones_ParametroRealmProxyInterface) newObject;
        Table table = realm.getTable(com.gstolima.comunicaciones.Parametro.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addInteger(columnInfo.idColKey, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.nomParametroColKey, realmObjectSource.realmGet$nomParametro());
        builder.addString(columnInfo.valor1ColKey, realmObjectSource.realmGet$valor1());
        builder.addString(columnInfo.valor2ColKey, realmObjectSource.realmGet$valor2());
        builder.addString(columnInfo.valor3ColKey, realmObjectSource.realmGet$valor3());

        builder.updateExistingTopLevelObject();
        return realmObject;
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
        com_gstolima_comunicaciones_ParametroRealmProxy aParametro = (com_gstolima_comunicaciones_ParametroRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aParametro.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aParametro.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aParametro.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
