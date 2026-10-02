package io.realm;


import android.util.JsonReader;
import io.realm.ImportFlag;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>(7);
        modelClasses.add(com.gstolima.accesoyseguridad.CrudifNull.class);
        modelClasses.add(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        modelClasses.add(com.gstolima.comunicaciones.Parametro.class);
        modelClasses.add(com.gstolima.comunicaciones.EnvioFoto.class);
        modelClasses.add(com.gstolima.comunicaciones.EnvioLectura.class);
        modelClasses.add(com.gstolima.comunicaciones.General.class);
        modelClasses.add(com.gstolima.comunicaciones.BDComunicaciones.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        Map<Class<? extends RealmModel>, OsObjectSchemaInfo> infoMap = new HashMap<Class<? extends RealmModel>, OsObjectSchemaInfo>(7);
        infoMap.put(com.gstolima.accesoyseguridad.CrudifNull.class, io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.EnvioCuentaNueva.class, io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.Parametro.class, io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.EnvioFoto.class, io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.EnvioLectura.class, io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.General.class, io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.gstolima.comunicaciones.BDComunicaciones.class, io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.getExpectedObjectSchemaInfo());
        return infoMap;
    }

    @Override
    public ColumnInfo createColumnInfo(Class<? extends RealmModel> clazz, OsSchemaInfo schemaInfo) {
        checkClass(clazz);

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.createColumnInfo(schemaInfo);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public String getSimpleClassNameImpl(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return "CrudifNull";
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return "EnvioCuentaNueva";
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return "Parametro";
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return "EnvioFoto";
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return "EnvioLectura";
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return "General";
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return "BDComunicaciones";
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public Class<? extends RealmModel> getClazzImpl(String className) {
        checkClassName(className);

        if (className.equals("CrudifNull")) {
            return com.gstolima.accesoyseguridad.CrudifNull.class;
        }
        if (className.equals("EnvioCuentaNueva")) {
            return com.gstolima.comunicaciones.EnvioCuentaNueva.class;
        }
        if (className.equals("Parametro")) {
            return com.gstolima.comunicaciones.Parametro.class;
        }
        if (className.equals("EnvioFoto")) {
            return com.gstolima.comunicaciones.EnvioFoto.class;
        }
        if (className.equals("EnvioLectura")) {
            return com.gstolima.comunicaciones.EnvioLectura.class;
        }
        if (className.equals("General")) {
            return com.gstolima.comunicaciones.General.class;
        }
        if (className.equals("BDComunicaciones")) {
            return com.gstolima.comunicaciones.BDComunicaciones.class;
        }
        throw getMissingProxyClassException(className);
    }

    @Override
    public boolean hasPrimaryKeyImpl(Class<? extends RealmModel> clazz) {
        return com.gstolima.accesoyseguridad.CrudifNull.class.isAssignableFrom(clazz)
                || com.gstolima.comunicaciones.EnvioCuentaNueva.class.isAssignableFrom(clazz)
                || com.gstolima.comunicaciones.Parametro.class.isAssignableFrom(clazz)
                || com.gstolima.comunicaciones.EnvioFoto.class.isAssignableFrom(clazz)
                || com.gstolima.comunicaciones.EnvioLectura.class.isAssignableFrom(clazz)
                || com.gstolima.comunicaciones.General.class.isAssignableFrom(clazz);
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
                return clazz.cast(new io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_ParametroRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_GeneralRealmProxy());
            }
            if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
                return clazz.cast(new io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy());
            }
            throw getMissingProxyClassException(clazz);
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            com_gstolima_accesoyseguridad_CrudifNullRealmProxy.CrudifNullColumnInfo columnInfo = (com_gstolima_accesoyseguridad_CrudifNullRealmProxy.CrudifNullColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.accesoyseguridad.CrudifNull.class);
            return clazz.cast(io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.accesoyseguridad.CrudifNull) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.EnvioCuentaNuevaColumnInfo columnInfo = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.EnvioCuentaNueva) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            com_gstolima_comunicaciones_ParametroRealmProxy.ParametroColumnInfo columnInfo = (com_gstolima_comunicaciones_ParametroRealmProxy.ParametroColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.Parametro.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.Parametro) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            com_gstolima_comunicaciones_EnvioFotoRealmProxy.EnvioFotoColumnInfo columnInfo = (com_gstolima_comunicaciones_EnvioFotoRealmProxy.EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.EnvioFoto) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            com_gstolima_comunicaciones_EnvioLecturaRealmProxy.EnvioLecturaColumnInfo columnInfo = (com_gstolima_comunicaciones_EnvioLecturaRealmProxy.EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.EnvioLectura) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            com_gstolima_comunicaciones_GeneralRealmProxy.GeneralColumnInfo columnInfo = (com_gstolima_comunicaciones_GeneralRealmProxy.GeneralColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.General.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.General) obj, update, cache, flags));
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            com_gstolima_comunicaciones_BDComunicacionesRealmProxy.BDComunicacionesColumnInfo columnInfo = (com_gstolima_comunicaciones_BDComunicacionesRealmProxy.BDComunicacionesColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.BDComunicaciones.class);
            return clazz.cast(io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.copyOrUpdate(realm, columnInfo, (com.gstolima.comunicaciones.BDComunicaciones) obj, update, cache, flags));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public long insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insert(realm, (com.gstolima.accesoyseguridad.CrudifNull) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioCuentaNueva) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insert(realm, (com.gstolima.comunicaciones.Parametro) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioFoto) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioLectura) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insert(realm, (com.gstolima.comunicaciones.General) object, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insert(realm, (com.gstolima.comunicaciones.BDComunicaciones) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
                io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insert(realm, (com.gstolima.accesoyseguridad.CrudifNull) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioCuentaNueva) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
                io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insert(realm, (com.gstolima.comunicaciones.Parametro) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioFoto) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insert(realm, (com.gstolima.comunicaciones.EnvioLectura) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
                io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insert(realm, (com.gstolima.comunicaciones.General) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
                io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insert(realm, (com.gstolima.comunicaciones.BDComunicaciones) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
                    io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
                    io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
                    io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
                    io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public long insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insertOrUpdate(realm, (com.gstolima.accesoyseguridad.CrudifNull) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioCuentaNueva) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.Parametro) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioFoto) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioLectura) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.General) obj, cache);
        } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.BDComunicaciones) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
                io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insertOrUpdate(realm, (com.gstolima.accesoyseguridad.CrudifNull) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioCuentaNueva) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
                io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.Parametro) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioFoto) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
                io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.EnvioLectura) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
                io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.General) object, cache);
            } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
                io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insertOrUpdate(realm, (com.gstolima.comunicaciones.BDComunicaciones) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
                    io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
                    io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
                    io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
                    io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
                    io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return clazz.cast(io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return clazz.cast(io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return clazz.cast(io.realm.com_gstolima_accesoyseguridad_CrudifNullRealmProxy.createDetachedCopy((com.gstolima.accesoyseguridad.CrudifNull) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.EnvioCuentaNueva) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_ParametroRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.Parametro) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.EnvioFoto) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.EnvioLectura) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_GeneralRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.General) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return clazz.cast(io.realm.com_gstolima_comunicaciones_BDComunicacionesRealmProxy.createDetachedCopy((com.gstolima.comunicaciones.BDComunicaciones) realmObject, 0, maxDepth, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> boolean isEmbedded(Class<E> clazz) {
        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            return false;
        }
        if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            return false;
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> void updateEmbeddedObject(Realm realm, E unmanagedObject, E managedObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) managedObject.getClass().getSuperclass();

        if (clazz.equals(com.gstolima.accesoyseguridad.CrudifNull.class)) {
            throw getNotEmbeddedClassException("com.gstolima.accesoyseguridad.CrudifNull");
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioCuentaNueva.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.EnvioCuentaNueva");
        } else if (clazz.equals(com.gstolima.comunicaciones.Parametro.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.Parametro");
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioFoto.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.EnvioFoto");
        } else if (clazz.equals(com.gstolima.comunicaciones.EnvioLectura.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.EnvioLectura");
        } else if (clazz.equals(com.gstolima.comunicaciones.General.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.General");
        } else if (clazz.equals(com.gstolima.comunicaciones.BDComunicaciones.class)) {
            throw getNotEmbeddedClassException("com.gstolima.comunicaciones.BDComunicaciones");
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

}
