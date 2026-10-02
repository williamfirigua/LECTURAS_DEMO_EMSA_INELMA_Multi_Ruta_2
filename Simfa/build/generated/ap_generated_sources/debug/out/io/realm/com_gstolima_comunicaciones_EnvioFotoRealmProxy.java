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
public class com_gstolima_comunicaciones_EnvioFotoRealmProxy extends com.gstolima.comunicaciones.EnvioFoto
    implements RealmObjectProxy, com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface {

    static final class EnvioFotoColumnInfo extends ColumnInfo {
        long claveRutaColKey;
        long moduloTrabajoColKey;
        long idColKey;
        long idRegistroColKey;
        long codCuentaColKey;
        long annoColKey;
        long mesColKey;
        long tipoMedidorColKey;
        long conteoColKey;
        long nombreFotoColKey;
        long rutaLocalColKey;
        long cicloColKey;
        long lectorColKey;
        long terminalColKey;
        long estadoEnvioColKey;
        long fechaCreacionColKey;
        long fechaEnvioColKey;
        long intentosEnvioColKey;
        long errorEnvioColKey;
        long campoDestinoColKey;

        EnvioFotoColumnInfo(OsSchemaInfo schemaInfo) {
            super(20);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("EnvioFoto");
            this.claveRutaColKey = addColumnDetails("claveRuta", "claveRuta", objectSchemaInfo);
            this.moduloTrabajoColKey = addColumnDetails("moduloTrabajo", "moduloTrabajo", objectSchemaInfo);
            this.idColKey = addColumnDetails("id", "id", objectSchemaInfo);
            this.idRegistroColKey = addColumnDetails("idRegistro", "idRegistro", objectSchemaInfo);
            this.codCuentaColKey = addColumnDetails("codCuenta", "codCuenta", objectSchemaInfo);
            this.annoColKey = addColumnDetails("anno", "anno", objectSchemaInfo);
            this.mesColKey = addColumnDetails("mes", "mes", objectSchemaInfo);
            this.tipoMedidorColKey = addColumnDetails("tipoMedidor", "tipoMedidor", objectSchemaInfo);
            this.conteoColKey = addColumnDetails("conteo", "conteo", objectSchemaInfo);
            this.nombreFotoColKey = addColumnDetails("nombreFoto", "nombreFoto", objectSchemaInfo);
            this.rutaLocalColKey = addColumnDetails("rutaLocal", "rutaLocal", objectSchemaInfo);
            this.cicloColKey = addColumnDetails("ciclo", "ciclo", objectSchemaInfo);
            this.lectorColKey = addColumnDetails("lector", "lector", objectSchemaInfo);
            this.terminalColKey = addColumnDetails("terminal", "terminal", objectSchemaInfo);
            this.estadoEnvioColKey = addColumnDetails("estadoEnvio", "estadoEnvio", objectSchemaInfo);
            this.fechaCreacionColKey = addColumnDetails("fechaCreacion", "fechaCreacion", objectSchemaInfo);
            this.fechaEnvioColKey = addColumnDetails("fechaEnvio", "fechaEnvio", objectSchemaInfo);
            this.intentosEnvioColKey = addColumnDetails("intentosEnvio", "intentosEnvio", objectSchemaInfo);
            this.errorEnvioColKey = addColumnDetails("errorEnvio", "errorEnvio", objectSchemaInfo);
            this.campoDestinoColKey = addColumnDetails("campoDestino", "campoDestino", objectSchemaInfo);
        }

        EnvioFotoColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new EnvioFotoColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final EnvioFotoColumnInfo src = (EnvioFotoColumnInfo) rawSrc;
            final EnvioFotoColumnInfo dst = (EnvioFotoColumnInfo) rawDst;
            dst.claveRutaColKey = src.claveRutaColKey;
            dst.moduloTrabajoColKey = src.moduloTrabajoColKey;
            dst.idColKey = src.idColKey;
            dst.idRegistroColKey = src.idRegistroColKey;
            dst.codCuentaColKey = src.codCuentaColKey;
            dst.annoColKey = src.annoColKey;
            dst.mesColKey = src.mesColKey;
            dst.tipoMedidorColKey = src.tipoMedidorColKey;
            dst.conteoColKey = src.conteoColKey;
            dst.nombreFotoColKey = src.nombreFotoColKey;
            dst.rutaLocalColKey = src.rutaLocalColKey;
            dst.cicloColKey = src.cicloColKey;
            dst.lectorColKey = src.lectorColKey;
            dst.terminalColKey = src.terminalColKey;
            dst.estadoEnvioColKey = src.estadoEnvioColKey;
            dst.fechaCreacionColKey = src.fechaCreacionColKey;
            dst.fechaEnvioColKey = src.fechaEnvioColKey;
            dst.intentosEnvioColKey = src.intentosEnvioColKey;
            dst.errorEnvioColKey = src.errorEnvioColKey;
            dst.campoDestinoColKey = src.campoDestinoColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private EnvioFotoColumnInfo columnInfo;
    private ProxyState<com.gstolima.comunicaciones.EnvioFoto> proxyState;

    com_gstolima_comunicaciones_EnvioFotoRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (EnvioFotoColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.gstolima.comunicaciones.EnvioFoto>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$claveRuta() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.claveRutaColKey);
    }

    @Override
    public void realmSet$claveRuta(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.claveRutaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.claveRutaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.claveRutaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.claveRutaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$moduloTrabajo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.moduloTrabajoColKey);
    }

    @Override
    public void realmSet$moduloTrabajo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.moduloTrabajoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.moduloTrabajoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.moduloTrabajoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.moduloTrabajoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.idColKey);
    }

    @Override
    public void realmSet$id(long value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'id' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$idRegistro() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idRegistroColKey);
    }

    @Override
    public void realmSet$idRegistro(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.idRegistroColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.idRegistroColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.idRegistroColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.idRegistroColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codCuenta() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codCuentaColKey);
    }

    @Override
    public void realmSet$codCuenta(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codCuentaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codCuentaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codCuentaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codCuentaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$anno() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.annoColKey);
    }

    @Override
    public void realmSet$anno(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.annoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.annoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.annoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.annoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$mes() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.mesColKey);
    }

    @Override
    public void realmSet$mes(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.mesColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.mesColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.mesColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.mesColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$tipoMedidor() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.tipoMedidorColKey);
    }

    @Override
    public void realmSet$tipoMedidor(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.tipoMedidorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.tipoMedidorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.tipoMedidorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.tipoMedidorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$conteo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.conteoColKey);
    }

    @Override
    public void realmSet$conteo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.conteoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.conteoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.conteoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.conteoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$nombreFoto() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nombreFotoColKey);
    }

    @Override
    public void realmSet$nombreFoto(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nombreFotoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.nombreFotoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nombreFotoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nombreFotoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$rutaLocal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.rutaLocalColKey);
    }

    @Override
    public void realmSet$rutaLocal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.rutaLocalColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.rutaLocalColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.rutaLocalColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.rutaLocalColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$ciclo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cicloColKey);
    }

    @Override
    public void realmSet$ciclo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cicloColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.cicloColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cicloColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cicloColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lector() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lectorColKey);
    }

    @Override
    public void realmSet$lector(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lectorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lectorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lectorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lectorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$terminal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.terminalColKey);
    }

    @Override
    public void realmSet$terminal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.terminalColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.terminalColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.terminalColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.terminalColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$estadoEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.estadoEnvioColKey);
    }

    @Override
    public void realmSet$estadoEnvio(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.estadoEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.estadoEnvioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaCreacion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaCreacionColKey);
    }

    @Override
    public void realmSet$fechaCreacion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaCreacionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaCreacionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaCreacionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaCreacionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaEnvioColKey);
    }

    @Override
    public void realmSet$fechaEnvio(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaEnvioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaEnvioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaEnvioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$intentosEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.intentosEnvioColKey);
    }

    @Override
    public void realmSet$intentosEnvio(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.intentosEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.intentosEnvioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$errorEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.errorEnvioColKey);
    }

    @Override
    public void realmSet$errorEnvio(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.errorEnvioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.errorEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.errorEnvioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.errorEnvioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$campoDestino() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.campoDestinoColKey);
    }

    @Override
    public void realmSet$campoDestino(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.campoDestinoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.campoDestinoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.campoDestinoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.campoDestinoColKey, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "EnvioFoto", false, 20, 0);
        builder.addPersistedProperty(NO_ALIAS, "claveRuta", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "moduloTrabajo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "id", RealmFieldType.INTEGER, Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "idRegistro", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codCuenta", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "anno", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "mes", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "tipoMedidor", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "conteo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nombreFoto", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "rutaLocal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "ciclo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lector", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "terminal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "estadoEnvio", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaCreacion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaEnvio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "intentosEnvio", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "errorEnvio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "campoDestino", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static EnvioFotoColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new EnvioFotoColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "EnvioFoto";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "EnvioFoto";
    }

    @SuppressWarnings("cast")
    public static com.gstolima.comunicaciones.EnvioFoto createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.gstolima.comunicaciones.EnvioFoto obj = null;
        if (update) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
            EnvioFotoColumnInfo columnInfo = (EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = Table.NO_MATCH;
            if (!json.isNull("id")) {
                objKey = table.findFirstLong(pkColumnKey, json.getLong("id"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioFoto.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioFoto.class, json.getLong("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) obj;
        if (json.has("claveRuta")) {
            if (json.isNull("claveRuta")) {
                objProxy.realmSet$claveRuta(null);
            } else {
                objProxy.realmSet$claveRuta((String) json.getString("claveRuta"));
            }
        }
        if (json.has("moduloTrabajo")) {
            if (json.isNull("moduloTrabajo")) {
                objProxy.realmSet$moduloTrabajo(null);
            } else {
                objProxy.realmSet$moduloTrabajo((String) json.getString("moduloTrabajo"));
            }
        }
        if (json.has("idRegistro")) {
            if (json.isNull("idRegistro")) {
                objProxy.realmSet$idRegistro(null);
            } else {
                objProxy.realmSet$idRegistro((String) json.getString("idRegistro"));
            }
        }
        if (json.has("codCuenta")) {
            if (json.isNull("codCuenta")) {
                objProxy.realmSet$codCuenta(null);
            } else {
                objProxy.realmSet$codCuenta((String) json.getString("codCuenta"));
            }
        }
        if (json.has("anno")) {
            if (json.isNull("anno")) {
                objProxy.realmSet$anno(null);
            } else {
                objProxy.realmSet$anno((String) json.getString("anno"));
            }
        }
        if (json.has("mes")) {
            if (json.isNull("mes")) {
                objProxy.realmSet$mes(null);
            } else {
                objProxy.realmSet$mes((String) json.getString("mes"));
            }
        }
        if (json.has("tipoMedidor")) {
            if (json.isNull("tipoMedidor")) {
                objProxy.realmSet$tipoMedidor(null);
            } else {
                objProxy.realmSet$tipoMedidor((String) json.getString("tipoMedidor"));
            }
        }
        if (json.has("conteo")) {
            if (json.isNull("conteo")) {
                objProxy.realmSet$conteo(null);
            } else {
                objProxy.realmSet$conteo((String) json.getString("conteo"));
            }
        }
        if (json.has("nombreFoto")) {
            if (json.isNull("nombreFoto")) {
                objProxy.realmSet$nombreFoto(null);
            } else {
                objProxy.realmSet$nombreFoto((String) json.getString("nombreFoto"));
            }
        }
        if (json.has("rutaLocal")) {
            if (json.isNull("rutaLocal")) {
                objProxy.realmSet$rutaLocal(null);
            } else {
                objProxy.realmSet$rutaLocal((String) json.getString("rutaLocal"));
            }
        }
        if (json.has("ciclo")) {
            if (json.isNull("ciclo")) {
                objProxy.realmSet$ciclo(null);
            } else {
                objProxy.realmSet$ciclo((String) json.getString("ciclo"));
            }
        }
        if (json.has("lector")) {
            if (json.isNull("lector")) {
                objProxy.realmSet$lector(null);
            } else {
                objProxy.realmSet$lector((String) json.getString("lector"));
            }
        }
        if (json.has("terminal")) {
            if (json.isNull("terminal")) {
                objProxy.realmSet$terminal(null);
            } else {
                objProxy.realmSet$terminal((String) json.getString("terminal"));
            }
        }
        if (json.has("estadoEnvio")) {
            if (json.isNull("estadoEnvio")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'estadoEnvio' to null.");
            } else {
                objProxy.realmSet$estadoEnvio((int) json.getInt("estadoEnvio"));
            }
        }
        if (json.has("fechaCreacion")) {
            if (json.isNull("fechaCreacion")) {
                objProxy.realmSet$fechaCreacion(null);
            } else {
                objProxy.realmSet$fechaCreacion((String) json.getString("fechaCreacion"));
            }
        }
        if (json.has("fechaEnvio")) {
            if (json.isNull("fechaEnvio")) {
                objProxy.realmSet$fechaEnvio(null);
            } else {
                objProxy.realmSet$fechaEnvio((String) json.getString("fechaEnvio"));
            }
        }
        if (json.has("intentosEnvio")) {
            if (json.isNull("intentosEnvio")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'intentosEnvio' to null.");
            } else {
                objProxy.realmSet$intentosEnvio((int) json.getInt("intentosEnvio"));
            }
        }
        if (json.has("errorEnvio")) {
            if (json.isNull("errorEnvio")) {
                objProxy.realmSet$errorEnvio(null);
            } else {
                objProxy.realmSet$errorEnvio((String) json.getString("errorEnvio"));
            }
        }
        if (json.has("campoDestino")) {
            if (json.isNull("campoDestino")) {
                objProxy.realmSet$campoDestino(null);
            } else {
                objProxy.realmSet$campoDestino((String) json.getString("campoDestino"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.gstolima.comunicaciones.EnvioFoto createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.gstolima.comunicaciones.EnvioFoto obj = new com.gstolima.comunicaciones.EnvioFoto();
        final com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("claveRuta")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$claveRuta((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$claveRuta(null);
                }
            } else if (name.equals("moduloTrabajo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$moduloTrabajo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$moduloTrabajo(null);
                }
            } else if (name.equals("id")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$id((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'id' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("idRegistro")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$idRegistro((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$idRegistro(null);
                }
            } else if (name.equals("codCuenta")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codCuenta((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codCuenta(null);
                }
            } else if (name.equals("anno")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$anno((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$anno(null);
                }
            } else if (name.equals("mes")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$mes((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$mes(null);
                }
            } else if (name.equals("tipoMedidor")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$tipoMedidor((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$tipoMedidor(null);
                }
            } else if (name.equals("conteo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$conteo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$conteo(null);
                }
            } else if (name.equals("nombreFoto")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nombreFoto((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nombreFoto(null);
                }
            } else if (name.equals("rutaLocal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$rutaLocal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$rutaLocal(null);
                }
            } else if (name.equals("ciclo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ciclo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ciclo(null);
                }
            } else if (name.equals("lector")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lector((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lector(null);
                }
            } else if (name.equals("terminal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$terminal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$terminal(null);
                }
            } else if (name.equals("estadoEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$estadoEnvio((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'estadoEnvio' to null.");
                }
            } else if (name.equals("fechaCreacion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaCreacion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaCreacion(null);
                }
            } else if (name.equals("fechaEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaEnvio((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaEnvio(null);
                }
            } else if (name.equals("intentosEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$intentosEnvio((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'intentosEnvio' to null.");
                }
            } else if (name.equals("errorEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$errorEnvio((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$errorEnvio(null);
                }
            } else if (name.equals("campoDestino")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$campoDestino((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$campoDestino(null);
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

    static com_gstolima_comunicaciones_EnvioFotoRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class), false, Collections.<String>emptyList());
        io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy obj = new io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.gstolima.comunicaciones.EnvioFoto copyOrUpdate(Realm realm, EnvioFotoColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioFoto object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.gstolima.comunicaciones.EnvioFoto) cachedRealmObject;
        }

        com.gstolima.comunicaciones.EnvioFoto realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
            long pkColumnKey = columnInfo.idColKey;
            long objKey = table.findFirstLong(pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.gstolima.comunicaciones.EnvioFoto copy(Realm realm, EnvioFotoColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioFoto newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.EnvioFoto) cachedRealmObject;
        }

        com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface unmanagedSource = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) newObject;

        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.claveRutaColKey, unmanagedSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, unmanagedSource.realmGet$moduloTrabajo());
        builder.addInteger(columnInfo.idColKey, unmanagedSource.realmGet$id());
        builder.addString(columnInfo.idRegistroColKey, unmanagedSource.realmGet$idRegistro());
        builder.addString(columnInfo.codCuentaColKey, unmanagedSource.realmGet$codCuenta());
        builder.addString(columnInfo.annoColKey, unmanagedSource.realmGet$anno());
        builder.addString(columnInfo.mesColKey, unmanagedSource.realmGet$mes());
        builder.addString(columnInfo.tipoMedidorColKey, unmanagedSource.realmGet$tipoMedidor());
        builder.addString(columnInfo.conteoColKey, unmanagedSource.realmGet$conteo());
        builder.addString(columnInfo.nombreFotoColKey, unmanagedSource.realmGet$nombreFoto());
        builder.addString(columnInfo.rutaLocalColKey, unmanagedSource.realmGet$rutaLocal());
        builder.addString(columnInfo.cicloColKey, unmanagedSource.realmGet$ciclo());
        builder.addString(columnInfo.lectorColKey, unmanagedSource.realmGet$lector());
        builder.addString(columnInfo.terminalColKey, unmanagedSource.realmGet$terminal());
        builder.addInteger(columnInfo.estadoEnvioColKey, unmanagedSource.realmGet$estadoEnvio());
        builder.addString(columnInfo.fechaCreacionColKey, unmanagedSource.realmGet$fechaCreacion());
        builder.addString(columnInfo.fechaEnvioColKey, unmanagedSource.realmGet$fechaEnvio());
        builder.addInteger(columnInfo.intentosEnvioColKey, unmanagedSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.errorEnvioColKey, unmanagedSource.realmGet$errorEnvio());
        builder.addString(columnInfo.campoDestinoColKey, unmanagedSource.realmGet$campoDestino());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_gstolima_comunicaciones_EnvioFotoRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.gstolima.comunicaciones.EnvioFoto object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        long tableNativePtr = table.getNativePtr();
        EnvioFotoColumnInfo columnInfo = (EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        }
        String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$idRegistro();
        if (realmGet$idRegistro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
        }
        String realmGet$codCuenta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$codCuenta();
        if (realmGet$codCuenta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codCuentaColKey, objKey, realmGet$codCuenta, false);
        }
        String realmGet$anno = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$anno();
        if (realmGet$anno != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
        }
        String realmGet$mes = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$mes();
        if (realmGet$mes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
        }
        String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$tipoMedidor();
        if (realmGet$tipoMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
        }
        String realmGet$conteo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$conteo();
        if (realmGet$conteo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.conteoColKey, objKey, realmGet$conteo, false);
        }
        String realmGet$nombreFoto = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$nombreFoto();
        if (realmGet$nombreFoto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreFotoColKey, objKey, realmGet$nombreFoto, false);
        }
        String realmGet$rutaLocal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$rutaLocal();
        if (realmGet$rutaLocal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rutaLocalColKey, objKey, realmGet$rutaLocal, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        }
        String realmGet$lector = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$lector();
        if (realmGet$lector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
        }
        String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$terminal();
        if (realmGet$terminal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$estadoEnvio(), false);
        String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
        }
        String realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaEnvio();
        if (realmGet$fechaEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$errorEnvio();
        if (realmGet$errorEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
        }
        String realmGet$campoDestino = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$campoDestino();
        if (realmGet$campoDestino != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.campoDestinoColKey, objKey, realmGet$campoDestino, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        long tableNativePtr = table.getNativePtr();
        EnvioFotoColumnInfo columnInfo = (EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.EnvioFoto object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioFoto) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            }
            String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$idRegistro();
            if (realmGet$idRegistro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
            }
            String realmGet$codCuenta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$codCuenta();
            if (realmGet$codCuenta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codCuentaColKey, objKey, realmGet$codCuenta, false);
            }
            String realmGet$anno = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$anno();
            if (realmGet$anno != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
            }
            String realmGet$mes = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$mes();
            if (realmGet$mes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
            }
            String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$tipoMedidor();
            if (realmGet$tipoMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
            }
            String realmGet$conteo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$conteo();
            if (realmGet$conteo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.conteoColKey, objKey, realmGet$conteo, false);
            }
            String realmGet$nombreFoto = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$nombreFoto();
            if (realmGet$nombreFoto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreFotoColKey, objKey, realmGet$nombreFoto, false);
            }
            String realmGet$rutaLocal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$rutaLocal();
            if (realmGet$rutaLocal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.rutaLocalColKey, objKey, realmGet$rutaLocal, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            }
            String realmGet$lector = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$lector();
            if (realmGet$lector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
            }
            String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$terminal();
            if (realmGet$terminal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$estadoEnvio(), false);
            String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
            }
            String realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaEnvio();
            if (realmGet$fechaEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$errorEnvio();
            if (realmGet$errorEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
            }
            String realmGet$campoDestino = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$campoDestino();
            if (realmGet$campoDestino != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.campoDestinoColKey, objKey, realmGet$campoDestino, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.gstolima.comunicaciones.EnvioFoto object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        long tableNativePtr = table.getNativePtr();
        EnvioFotoColumnInfo columnInfo = (EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
        long pkColumnKey = columnInfo.idColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
        }
        String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$idRegistro();
        if (realmGet$idRegistro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idRegistroColKey, objKey, false);
        }
        String realmGet$codCuenta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$codCuenta();
        if (realmGet$codCuenta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codCuentaColKey, objKey, realmGet$codCuenta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codCuentaColKey, objKey, false);
        }
        String realmGet$anno = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$anno();
        if (realmGet$anno != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.annoColKey, objKey, false);
        }
        String realmGet$mes = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$mes();
        if (realmGet$mes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.mesColKey, objKey, false);
        }
        String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$tipoMedidor();
        if (realmGet$tipoMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, false);
        }
        String realmGet$conteo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$conteo();
        if (realmGet$conteo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.conteoColKey, objKey, realmGet$conteo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.conteoColKey, objKey, false);
        }
        String realmGet$nombreFoto = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$nombreFoto();
        if (realmGet$nombreFoto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreFotoColKey, objKey, realmGet$nombreFoto, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nombreFotoColKey, objKey, false);
        }
        String realmGet$rutaLocal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$rutaLocal();
        if (realmGet$rutaLocal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rutaLocalColKey, objKey, realmGet$rutaLocal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.rutaLocalColKey, objKey, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
        }
        String realmGet$lector = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$lector();
        if (realmGet$lector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lectorColKey, objKey, false);
        }
        String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$terminal();
        if (realmGet$terminal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.terminalColKey, objKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$estadoEnvio(), false);
        String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
        }
        String realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaEnvio();
        if (realmGet$fechaEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$errorEnvio();
        if (realmGet$errorEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.errorEnvioColKey, objKey, false);
        }
        String realmGet$campoDestino = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$campoDestino();
        if (realmGet$campoDestino != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.campoDestinoColKey, objKey, realmGet$campoDestino, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.campoDestinoColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        long tableNativePtr = table.getNativePtr();
        EnvioFotoColumnInfo columnInfo = (EnvioFotoColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioFoto.class);
        long pkColumnKey = columnInfo.idColKey;
        com.gstolima.comunicaciones.EnvioFoto object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioFoto) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$id());
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
            }
            String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$idRegistro();
            if (realmGet$idRegistro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.idRegistroColKey, objKey, false);
            }
            String realmGet$codCuenta = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$codCuenta();
            if (realmGet$codCuenta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codCuentaColKey, objKey, realmGet$codCuenta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codCuentaColKey, objKey, false);
            }
            String realmGet$anno = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$anno();
            if (realmGet$anno != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.annoColKey, objKey, false);
            }
            String realmGet$mes = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$mes();
            if (realmGet$mes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.mesColKey, objKey, false);
            }
            String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$tipoMedidor();
            if (realmGet$tipoMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, false);
            }
            String realmGet$conteo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$conteo();
            if (realmGet$conteo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.conteoColKey, objKey, realmGet$conteo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.conteoColKey, objKey, false);
            }
            String realmGet$nombreFoto = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$nombreFoto();
            if (realmGet$nombreFoto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreFotoColKey, objKey, realmGet$nombreFoto, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nombreFotoColKey, objKey, false);
            }
            String realmGet$rutaLocal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$rutaLocal();
            if (realmGet$rutaLocal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.rutaLocalColKey, objKey, realmGet$rutaLocal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.rutaLocalColKey, objKey, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
            }
            String realmGet$lector = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$lector();
            if (realmGet$lector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lectorColKey, objKey, false);
            }
            String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$terminal();
            if (realmGet$terminal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.terminalColKey, objKey, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$estadoEnvio(), false);
            String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
            }
            String realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$fechaEnvio();
            if (realmGet$fechaEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$errorEnvio();
            if (realmGet$errorEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.errorEnvioColKey, objKey, false);
            }
            String realmGet$campoDestino = ((com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) object).realmGet$campoDestino();
            if (realmGet$campoDestino != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.campoDestinoColKey, objKey, realmGet$campoDestino, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.campoDestinoColKey, objKey, false);
            }
        }
    }

    public static com.gstolima.comunicaciones.EnvioFoto createDetachedCopy(com.gstolima.comunicaciones.EnvioFoto realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.gstolima.comunicaciones.EnvioFoto unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.gstolima.comunicaciones.EnvioFoto();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.gstolima.comunicaciones.EnvioFoto) cachedObject.object;
            }
            unmanagedObject = (com.gstolima.comunicaciones.EnvioFoto) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface unmanagedCopy = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) unmanagedObject;
        com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface realmSource = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) realmObject;
        Realm objectRealm = (Realm) ((RealmObjectProxy) realmObject).realmGet$proxyState().getRealm$realm();
        unmanagedCopy.realmSet$claveRuta(realmSource.realmGet$claveRuta());
        unmanagedCopy.realmSet$moduloTrabajo(realmSource.realmGet$moduloTrabajo());
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());
        unmanagedCopy.realmSet$idRegistro(realmSource.realmGet$idRegistro());
        unmanagedCopy.realmSet$codCuenta(realmSource.realmGet$codCuenta());
        unmanagedCopy.realmSet$anno(realmSource.realmGet$anno());
        unmanagedCopy.realmSet$mes(realmSource.realmGet$mes());
        unmanagedCopy.realmSet$tipoMedidor(realmSource.realmGet$tipoMedidor());
        unmanagedCopy.realmSet$conteo(realmSource.realmGet$conteo());
        unmanagedCopy.realmSet$nombreFoto(realmSource.realmGet$nombreFoto());
        unmanagedCopy.realmSet$rutaLocal(realmSource.realmGet$rutaLocal());
        unmanagedCopy.realmSet$ciclo(realmSource.realmGet$ciclo());
        unmanagedCopy.realmSet$lector(realmSource.realmGet$lector());
        unmanagedCopy.realmSet$terminal(realmSource.realmGet$terminal());
        unmanagedCopy.realmSet$estadoEnvio(realmSource.realmGet$estadoEnvio());
        unmanagedCopy.realmSet$fechaCreacion(realmSource.realmGet$fechaCreacion());
        unmanagedCopy.realmSet$fechaEnvio(realmSource.realmGet$fechaEnvio());
        unmanagedCopy.realmSet$intentosEnvio(realmSource.realmGet$intentosEnvio());
        unmanagedCopy.realmSet$errorEnvio(realmSource.realmGet$errorEnvio());
        unmanagedCopy.realmSet$campoDestino(realmSource.realmGet$campoDestino());

        return unmanagedObject;
    }

    static com.gstolima.comunicaciones.EnvioFoto update(Realm realm, EnvioFotoColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioFoto realmObject, com.gstolima.comunicaciones.EnvioFoto newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface realmObjectTarget = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) realmObject;
        com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface realmObjectSource = (com_gstolima_comunicaciones_EnvioFotoRealmProxyInterface) newObject;
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioFoto.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addString(columnInfo.claveRutaColKey, realmObjectSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, realmObjectSource.realmGet$moduloTrabajo());
        builder.addInteger(columnInfo.idColKey, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.idRegistroColKey, realmObjectSource.realmGet$idRegistro());
        builder.addString(columnInfo.codCuentaColKey, realmObjectSource.realmGet$codCuenta());
        builder.addString(columnInfo.annoColKey, realmObjectSource.realmGet$anno());
        builder.addString(columnInfo.mesColKey, realmObjectSource.realmGet$mes());
        builder.addString(columnInfo.tipoMedidorColKey, realmObjectSource.realmGet$tipoMedidor());
        builder.addString(columnInfo.conteoColKey, realmObjectSource.realmGet$conteo());
        builder.addString(columnInfo.nombreFotoColKey, realmObjectSource.realmGet$nombreFoto());
        builder.addString(columnInfo.rutaLocalColKey, realmObjectSource.realmGet$rutaLocal());
        builder.addString(columnInfo.cicloColKey, realmObjectSource.realmGet$ciclo());
        builder.addString(columnInfo.lectorColKey, realmObjectSource.realmGet$lector());
        builder.addString(columnInfo.terminalColKey, realmObjectSource.realmGet$terminal());
        builder.addInteger(columnInfo.estadoEnvioColKey, realmObjectSource.realmGet$estadoEnvio());
        builder.addString(columnInfo.fechaCreacionColKey, realmObjectSource.realmGet$fechaCreacion());
        builder.addString(columnInfo.fechaEnvioColKey, realmObjectSource.realmGet$fechaEnvio());
        builder.addInteger(columnInfo.intentosEnvioColKey, realmObjectSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.errorEnvioColKey, realmObjectSource.realmGet$errorEnvio());
        builder.addString(columnInfo.campoDestinoColKey, realmObjectSource.realmGet$campoDestino());

        builder.updateExistingTopLevelObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("EnvioFoto = proxy[");
        stringBuilder.append("{claveRuta:");
        stringBuilder.append(realmGet$claveRuta() != null ? realmGet$claveRuta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{moduloTrabajo:");
        stringBuilder.append(realmGet$moduloTrabajo() != null ? realmGet$moduloTrabajo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idRegistro:");
        stringBuilder.append(realmGet$idRegistro() != null ? realmGet$idRegistro() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codCuenta:");
        stringBuilder.append(realmGet$codCuenta() != null ? realmGet$codCuenta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{anno:");
        stringBuilder.append(realmGet$anno() != null ? realmGet$anno() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{mes:");
        stringBuilder.append(realmGet$mes() != null ? realmGet$mes() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tipoMedidor:");
        stringBuilder.append(realmGet$tipoMedidor() != null ? realmGet$tipoMedidor() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{conteo:");
        stringBuilder.append(realmGet$conteo() != null ? realmGet$conteo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{nombreFoto:");
        stringBuilder.append(realmGet$nombreFoto() != null ? realmGet$nombreFoto() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{rutaLocal:");
        stringBuilder.append(realmGet$rutaLocal() != null ? realmGet$rutaLocal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ciclo:");
        stringBuilder.append(realmGet$ciclo() != null ? realmGet$ciclo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lector:");
        stringBuilder.append(realmGet$lector() != null ? realmGet$lector() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{terminal:");
        stringBuilder.append(realmGet$terminal() != null ? realmGet$terminal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{estadoEnvio:");
        stringBuilder.append(realmGet$estadoEnvio());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaCreacion:");
        stringBuilder.append(realmGet$fechaCreacion() != null ? realmGet$fechaCreacion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaEnvio:");
        stringBuilder.append(realmGet$fechaEnvio() != null ? realmGet$fechaEnvio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{intentosEnvio:");
        stringBuilder.append(realmGet$intentosEnvio());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{errorEnvio:");
        stringBuilder.append(realmGet$errorEnvio() != null ? realmGet$errorEnvio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{campoDestino:");
        stringBuilder.append(realmGet$campoDestino() != null ? realmGet$campoDestino() : "null");
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
        com_gstolima_comunicaciones_EnvioFotoRealmProxy aEnvioFoto = (com_gstolima_comunicaciones_EnvioFotoRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aEnvioFoto.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aEnvioFoto.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aEnvioFoto.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
