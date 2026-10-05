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
public class com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy extends com.gstolima.comunicaciones.EnvioCuentaNueva
    implements RealmObjectProxy, com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface {

    static final class EnvioCuentaNuevaColumnInfo extends ColumnInfo {
        long claveRutaColKey;
        long moduloTrabajoColKey;
        long idRealmColKey;
        long cicloColKey;
        long descDeptoColKey;
        long codMunicipioColKey;
        long codSectorColKey;
        long codRutaColKey;
        long cicloRealColKey;
        long direccionColKey;
        long contadorColKey;
        long marcaColKey;
        long tipoMedidorColKey;
        long digitosColKey;
        long lecturaColKey;
        long observacionColKey;
        long informeColKey;
        long codReferenciaColKey;
        long latitudColKey;
        long longitudColKey;
        long altitudColKey;
        long numSatelitesColKey;
        long fechaHoraColKey;
        long lectorColKey;
        long serialColKey;
        long foto1ColKey;
        long foto2ColKey;
        long foto3ColKey;
        long nombreArchivoColKey;
        long estadoEnvioColKey;
        long intentosEnvioColKey;
        long mensajeErrorColKey;
        long fechaCreacionColKey;
        long fechaEnvioColKey;

        EnvioCuentaNuevaColumnInfo(OsSchemaInfo schemaInfo) {
            super(34);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("EnvioCuentaNueva");
            this.claveRutaColKey = addColumnDetails("claveRuta", "claveRuta", objectSchemaInfo);
            this.moduloTrabajoColKey = addColumnDetails("moduloTrabajo", "moduloTrabajo", objectSchemaInfo);
            this.idRealmColKey = addColumnDetails("idRealm", "idRealm", objectSchemaInfo);
            this.cicloColKey = addColumnDetails("ciclo", "ciclo", objectSchemaInfo);
            this.descDeptoColKey = addColumnDetails("descDepto", "descDepto", objectSchemaInfo);
            this.codMunicipioColKey = addColumnDetails("codMunicipio", "codMunicipio", objectSchemaInfo);
            this.codSectorColKey = addColumnDetails("codSector", "codSector", objectSchemaInfo);
            this.codRutaColKey = addColumnDetails("codRuta", "codRuta", objectSchemaInfo);
            this.cicloRealColKey = addColumnDetails("cicloReal", "cicloReal", objectSchemaInfo);
            this.direccionColKey = addColumnDetails("direccion", "direccion", objectSchemaInfo);
            this.contadorColKey = addColumnDetails("contador", "contador", objectSchemaInfo);
            this.marcaColKey = addColumnDetails("marca", "marca", objectSchemaInfo);
            this.tipoMedidorColKey = addColumnDetails("tipoMedidor", "tipoMedidor", objectSchemaInfo);
            this.digitosColKey = addColumnDetails("digitos", "digitos", objectSchemaInfo);
            this.lecturaColKey = addColumnDetails("lectura", "lectura", objectSchemaInfo);
            this.observacionColKey = addColumnDetails("observacion", "observacion", objectSchemaInfo);
            this.informeColKey = addColumnDetails("informe", "informe", objectSchemaInfo);
            this.codReferenciaColKey = addColumnDetails("codReferencia", "codReferencia", objectSchemaInfo);
            this.latitudColKey = addColumnDetails("latitud", "latitud", objectSchemaInfo);
            this.longitudColKey = addColumnDetails("longitud", "longitud", objectSchemaInfo);
            this.altitudColKey = addColumnDetails("altitud", "altitud", objectSchemaInfo);
            this.numSatelitesColKey = addColumnDetails("numSatelites", "numSatelites", objectSchemaInfo);
            this.fechaHoraColKey = addColumnDetails("fechaHora", "fechaHora", objectSchemaInfo);
            this.lectorColKey = addColumnDetails("lector", "lector", objectSchemaInfo);
            this.serialColKey = addColumnDetails("serial", "serial", objectSchemaInfo);
            this.foto1ColKey = addColumnDetails("foto1", "foto1", objectSchemaInfo);
            this.foto2ColKey = addColumnDetails("foto2", "foto2", objectSchemaInfo);
            this.foto3ColKey = addColumnDetails("foto3", "foto3", objectSchemaInfo);
            this.nombreArchivoColKey = addColumnDetails("nombreArchivo", "nombreArchivo", objectSchemaInfo);
            this.estadoEnvioColKey = addColumnDetails("estadoEnvio", "estadoEnvio", objectSchemaInfo);
            this.intentosEnvioColKey = addColumnDetails("intentosEnvio", "intentosEnvio", objectSchemaInfo);
            this.mensajeErrorColKey = addColumnDetails("mensajeError", "mensajeError", objectSchemaInfo);
            this.fechaCreacionColKey = addColumnDetails("fechaCreacion", "fechaCreacion", objectSchemaInfo);
            this.fechaEnvioColKey = addColumnDetails("fechaEnvio", "fechaEnvio", objectSchemaInfo);
        }

        EnvioCuentaNuevaColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new EnvioCuentaNuevaColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final EnvioCuentaNuevaColumnInfo src = (EnvioCuentaNuevaColumnInfo) rawSrc;
            final EnvioCuentaNuevaColumnInfo dst = (EnvioCuentaNuevaColumnInfo) rawDst;
            dst.claveRutaColKey = src.claveRutaColKey;
            dst.moduloTrabajoColKey = src.moduloTrabajoColKey;
            dst.idRealmColKey = src.idRealmColKey;
            dst.cicloColKey = src.cicloColKey;
            dst.descDeptoColKey = src.descDeptoColKey;
            dst.codMunicipioColKey = src.codMunicipioColKey;
            dst.codSectorColKey = src.codSectorColKey;
            dst.codRutaColKey = src.codRutaColKey;
            dst.cicloRealColKey = src.cicloRealColKey;
            dst.direccionColKey = src.direccionColKey;
            dst.contadorColKey = src.contadorColKey;
            dst.marcaColKey = src.marcaColKey;
            dst.tipoMedidorColKey = src.tipoMedidorColKey;
            dst.digitosColKey = src.digitosColKey;
            dst.lecturaColKey = src.lecturaColKey;
            dst.observacionColKey = src.observacionColKey;
            dst.informeColKey = src.informeColKey;
            dst.codReferenciaColKey = src.codReferenciaColKey;
            dst.latitudColKey = src.latitudColKey;
            dst.longitudColKey = src.longitudColKey;
            dst.altitudColKey = src.altitudColKey;
            dst.numSatelitesColKey = src.numSatelitesColKey;
            dst.fechaHoraColKey = src.fechaHoraColKey;
            dst.lectorColKey = src.lectorColKey;
            dst.serialColKey = src.serialColKey;
            dst.foto1ColKey = src.foto1ColKey;
            dst.foto2ColKey = src.foto2ColKey;
            dst.foto3ColKey = src.foto3ColKey;
            dst.nombreArchivoColKey = src.nombreArchivoColKey;
            dst.estadoEnvioColKey = src.estadoEnvioColKey;
            dst.intentosEnvioColKey = src.intentosEnvioColKey;
            dst.mensajeErrorColKey = src.mensajeErrorColKey;
            dst.fechaCreacionColKey = src.fechaCreacionColKey;
            dst.fechaEnvioColKey = src.fechaEnvioColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private EnvioCuentaNuevaColumnInfo columnInfo;
    private ProxyState<com.gstolima.comunicaciones.EnvioCuentaNueva> proxyState;

    com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (EnvioCuentaNuevaColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.gstolima.comunicaciones.EnvioCuentaNueva>(this);
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
    public String realmGet$idRealm() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idRealmColKey);
    }

    @Override
    public void realmSet$idRealm(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'idRealm' cannot be changed after object was created.");
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
    public String realmGet$descDepto() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descDeptoColKey);
    }

    @Override
    public void realmSet$descDepto(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descDeptoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.descDeptoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descDeptoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descDeptoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codMunicipio() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codMunicipioColKey);
    }

    @Override
    public void realmSet$codMunicipio(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codMunicipioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codMunicipioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codMunicipioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codMunicipioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codSector() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codSectorColKey);
    }

    @Override
    public void realmSet$codSector(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codSectorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codSectorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codSectorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codSectorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codRuta() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codRutaColKey);
    }

    @Override
    public void realmSet$codRuta(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codRutaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codRutaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codRutaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codRutaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cicloReal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cicloRealColKey);
    }

    @Override
    public void realmSet$cicloReal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cicloRealColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.cicloRealColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cicloRealColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cicloRealColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$direccion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.direccionColKey);
    }

    @Override
    public void realmSet$direccion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.direccionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.direccionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.direccionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.direccionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$contador() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.contadorColKey);
    }

    @Override
    public void realmSet$contador(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.contadorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.contadorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.contadorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.contadorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$marca() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.marcaColKey);
    }

    @Override
    public void realmSet$marca(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.marcaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.marcaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.marcaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.marcaColKey, value);
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
    public String realmGet$digitos() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.digitosColKey);
    }

    @Override
    public void realmSet$digitos(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.digitosColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.digitosColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.digitosColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.digitosColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lectura() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lecturaColKey);
    }

    @Override
    public void realmSet$lectura(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lecturaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lecturaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lecturaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lecturaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$observacion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.observacionColKey);
    }

    @Override
    public void realmSet$observacion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.observacionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.observacionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.observacionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.observacionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$informe() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.informeColKey);
    }

    @Override
    public void realmSet$informe(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.informeColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.informeColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.informeColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.informeColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codReferencia() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codReferenciaColKey);
    }

    @Override
    public void realmSet$codReferencia(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codReferenciaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codReferenciaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codReferenciaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codReferenciaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$latitud() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.latitudColKey);
    }

    @Override
    public void realmSet$latitud(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.latitudColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.latitudColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.latitudColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.latitudColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$longitud() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.longitudColKey);
    }

    @Override
    public void realmSet$longitud(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.longitudColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.longitudColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.longitudColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.longitudColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$altitud() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.altitudColKey);
    }

    @Override
    public void realmSet$altitud(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.altitudColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.altitudColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.altitudColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.altitudColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$numSatelites() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.numSatelitesColKey);
    }

    @Override
    public void realmSet$numSatelites(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.numSatelitesColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.numSatelitesColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.numSatelitesColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.numSatelitesColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaHora() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaHoraColKey);
    }

    @Override
    public void realmSet$fechaHora(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaHoraColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaHoraColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaHoraColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaHoraColKey, value);
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
    public String realmGet$serial() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serialColKey);
    }

    @Override
    public void realmSet$serial(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serialColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.serialColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serialColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serialColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$foto1() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.foto1ColKey);
    }

    @Override
    public void realmSet$foto1(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.foto1ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.foto1ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.foto1ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.foto1ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$foto2() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.foto2ColKey);
    }

    @Override
    public void realmSet$foto2(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.foto2ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.foto2ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.foto2ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.foto2ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$foto3() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.foto3ColKey);
    }

    @Override
    public void realmSet$foto3(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.foto3ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.foto3ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.foto3ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.foto3ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$nombreArchivo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nombreArchivoColKey);
    }

    @Override
    public void realmSet$nombreArchivo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nombreArchivoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.nombreArchivoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nombreArchivoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nombreArchivoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$estadoEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.estadoEnvioColKey);
    }

    @Override
    public void realmSet$estadoEnvio(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.estadoEnvioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.estadoEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.estadoEnvioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.estadoEnvioColKey, value);
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
    public String realmGet$mensajeError() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.mensajeErrorColKey);
    }

    @Override
    public void realmSet$mensajeError(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.mensajeErrorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.mensajeErrorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.mensajeErrorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.mensajeErrorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public Date realmGet$fechaCreacion() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.fechaCreacionColKey)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.fechaCreacionColKey);
    }

    @Override
    public void realmSet$fechaCreacion(Date value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaCreacionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setDate(columnInfo.fechaCreacionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaCreacionColKey);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.fechaCreacionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public Date realmGet$fechaEnvio() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.fechaEnvioColKey)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.fechaEnvioColKey);
    }

    @Override
    public void realmSet$fechaEnvio(Date value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaEnvioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setDate(columnInfo.fechaEnvioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaEnvioColKey);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.fechaEnvioColKey, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "EnvioCuentaNueva", false, 34, 0);
        builder.addPersistedProperty(NO_ALIAS, "claveRuta", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "moduloTrabajo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "idRealm", RealmFieldType.STRING, Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "ciclo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "descDepto", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codMunicipio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codSector", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codRuta", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "cicloReal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "direccion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "contador", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "marca", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "tipoMedidor", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "digitos", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lectura", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "observacion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "informe", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codReferencia", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "latitud", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "longitud", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "altitud", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "numSatelites", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaHora", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lector", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "serial", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "foto1", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "foto2", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "foto3", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nombreArchivo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "estadoEnvio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "intentosEnvio", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "mensajeError", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaCreacion", RealmFieldType.DATE, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaEnvio", RealmFieldType.DATE, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static EnvioCuentaNuevaColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new EnvioCuentaNuevaColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "EnvioCuentaNueva";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "EnvioCuentaNueva";
    }

    @SuppressWarnings("cast")
    public static com.gstolima.comunicaciones.EnvioCuentaNueva createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.gstolima.comunicaciones.EnvioCuentaNueva obj = null;
        if (update) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
            EnvioCuentaNuevaColumnInfo columnInfo = (EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
            long pkColumnKey = columnInfo.idRealmColKey;
            long objKey = Table.NO_MATCH;
            if (json.isNull("idRealm")) {
                objKey = table.findFirstNull(pkColumnKey);
            } else {
                objKey = table.findFirstString(pkColumnKey, json.getString("idRealm"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("idRealm")) {
                if (json.isNull("idRealm")) {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioCuentaNueva.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioCuentaNueva.class, json.getString("idRealm"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'idRealm'.");
            }
        }

        final com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) obj;
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
        if (json.has("ciclo")) {
            if (json.isNull("ciclo")) {
                objProxy.realmSet$ciclo(null);
            } else {
                objProxy.realmSet$ciclo((String) json.getString("ciclo"));
            }
        }
        if (json.has("descDepto")) {
            if (json.isNull("descDepto")) {
                objProxy.realmSet$descDepto(null);
            } else {
                objProxy.realmSet$descDepto((String) json.getString("descDepto"));
            }
        }
        if (json.has("codMunicipio")) {
            if (json.isNull("codMunicipio")) {
                objProxy.realmSet$codMunicipio(null);
            } else {
                objProxy.realmSet$codMunicipio((String) json.getString("codMunicipio"));
            }
        }
        if (json.has("codSector")) {
            if (json.isNull("codSector")) {
                objProxy.realmSet$codSector(null);
            } else {
                objProxy.realmSet$codSector((String) json.getString("codSector"));
            }
        }
        if (json.has("codRuta")) {
            if (json.isNull("codRuta")) {
                objProxy.realmSet$codRuta(null);
            } else {
                objProxy.realmSet$codRuta((String) json.getString("codRuta"));
            }
        }
        if (json.has("cicloReal")) {
            if (json.isNull("cicloReal")) {
                objProxy.realmSet$cicloReal(null);
            } else {
                objProxy.realmSet$cicloReal((String) json.getString("cicloReal"));
            }
        }
        if (json.has("direccion")) {
            if (json.isNull("direccion")) {
                objProxy.realmSet$direccion(null);
            } else {
                objProxy.realmSet$direccion((String) json.getString("direccion"));
            }
        }
        if (json.has("contador")) {
            if (json.isNull("contador")) {
                objProxy.realmSet$contador(null);
            } else {
                objProxy.realmSet$contador((String) json.getString("contador"));
            }
        }
        if (json.has("marca")) {
            if (json.isNull("marca")) {
                objProxy.realmSet$marca(null);
            } else {
                objProxy.realmSet$marca((String) json.getString("marca"));
            }
        }
        if (json.has("tipoMedidor")) {
            if (json.isNull("tipoMedidor")) {
                objProxy.realmSet$tipoMedidor(null);
            } else {
                objProxy.realmSet$tipoMedidor((String) json.getString("tipoMedidor"));
            }
        }
        if (json.has("digitos")) {
            if (json.isNull("digitos")) {
                objProxy.realmSet$digitos(null);
            } else {
                objProxy.realmSet$digitos((String) json.getString("digitos"));
            }
        }
        if (json.has("lectura")) {
            if (json.isNull("lectura")) {
                objProxy.realmSet$lectura(null);
            } else {
                objProxy.realmSet$lectura((String) json.getString("lectura"));
            }
        }
        if (json.has("observacion")) {
            if (json.isNull("observacion")) {
                objProxy.realmSet$observacion(null);
            } else {
                objProxy.realmSet$observacion((String) json.getString("observacion"));
            }
        }
        if (json.has("informe")) {
            if (json.isNull("informe")) {
                objProxy.realmSet$informe(null);
            } else {
                objProxy.realmSet$informe((String) json.getString("informe"));
            }
        }
        if (json.has("codReferencia")) {
            if (json.isNull("codReferencia")) {
                objProxy.realmSet$codReferencia(null);
            } else {
                objProxy.realmSet$codReferencia((String) json.getString("codReferencia"));
            }
        }
        if (json.has("latitud")) {
            if (json.isNull("latitud")) {
                objProxy.realmSet$latitud(null);
            } else {
                objProxy.realmSet$latitud((String) json.getString("latitud"));
            }
        }
        if (json.has("longitud")) {
            if (json.isNull("longitud")) {
                objProxy.realmSet$longitud(null);
            } else {
                objProxy.realmSet$longitud((String) json.getString("longitud"));
            }
        }
        if (json.has("altitud")) {
            if (json.isNull("altitud")) {
                objProxy.realmSet$altitud(null);
            } else {
                objProxy.realmSet$altitud((String) json.getString("altitud"));
            }
        }
        if (json.has("numSatelites")) {
            if (json.isNull("numSatelites")) {
                objProxy.realmSet$numSatelites(null);
            } else {
                objProxy.realmSet$numSatelites((String) json.getString("numSatelites"));
            }
        }
        if (json.has("fechaHora")) {
            if (json.isNull("fechaHora")) {
                objProxy.realmSet$fechaHora(null);
            } else {
                objProxy.realmSet$fechaHora((String) json.getString("fechaHora"));
            }
        }
        if (json.has("lector")) {
            if (json.isNull("lector")) {
                objProxy.realmSet$lector(null);
            } else {
                objProxy.realmSet$lector((String) json.getString("lector"));
            }
        }
        if (json.has("serial")) {
            if (json.isNull("serial")) {
                objProxy.realmSet$serial(null);
            } else {
                objProxy.realmSet$serial((String) json.getString("serial"));
            }
        }
        if (json.has("foto1")) {
            if (json.isNull("foto1")) {
                objProxy.realmSet$foto1(null);
            } else {
                objProxy.realmSet$foto1((String) json.getString("foto1"));
            }
        }
        if (json.has("foto2")) {
            if (json.isNull("foto2")) {
                objProxy.realmSet$foto2(null);
            } else {
                objProxy.realmSet$foto2((String) json.getString("foto2"));
            }
        }
        if (json.has("foto3")) {
            if (json.isNull("foto3")) {
                objProxy.realmSet$foto3(null);
            } else {
                objProxy.realmSet$foto3((String) json.getString("foto3"));
            }
        }
        if (json.has("nombreArchivo")) {
            if (json.isNull("nombreArchivo")) {
                objProxy.realmSet$nombreArchivo(null);
            } else {
                objProxy.realmSet$nombreArchivo((String) json.getString("nombreArchivo"));
            }
        }
        if (json.has("estadoEnvio")) {
            if (json.isNull("estadoEnvio")) {
                objProxy.realmSet$estadoEnvio(null);
            } else {
                objProxy.realmSet$estadoEnvio((String) json.getString("estadoEnvio"));
            }
        }
        if (json.has("intentosEnvio")) {
            if (json.isNull("intentosEnvio")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'intentosEnvio' to null.");
            } else {
                objProxy.realmSet$intentosEnvio((int) json.getInt("intentosEnvio"));
            }
        }
        if (json.has("mensajeError")) {
            if (json.isNull("mensajeError")) {
                objProxy.realmSet$mensajeError(null);
            } else {
                objProxy.realmSet$mensajeError((String) json.getString("mensajeError"));
            }
        }
        if (json.has("fechaCreacion")) {
            if (json.isNull("fechaCreacion")) {
                objProxy.realmSet$fechaCreacion(null);
            } else {
                Object timestamp = json.get("fechaCreacion");
                if (timestamp instanceof String) {
                    objProxy.realmSet$fechaCreacion(JsonUtils.stringToDate((String) timestamp));
                } else {
                    objProxy.realmSet$fechaCreacion(new Date(json.getLong("fechaCreacion")));
                }
            }
        }
        if (json.has("fechaEnvio")) {
            if (json.isNull("fechaEnvio")) {
                objProxy.realmSet$fechaEnvio(null);
            } else {
                Object timestamp = json.get("fechaEnvio");
                if (timestamp instanceof String) {
                    objProxy.realmSet$fechaEnvio(JsonUtils.stringToDate((String) timestamp));
                } else {
                    objProxy.realmSet$fechaEnvio(new Date(json.getLong("fechaEnvio")));
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.gstolima.comunicaciones.EnvioCuentaNueva createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.gstolima.comunicaciones.EnvioCuentaNueva obj = new com.gstolima.comunicaciones.EnvioCuentaNueva();
        final com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) obj;
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
            } else if (name.equals("idRealm")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$idRealm((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$idRealm(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("ciclo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ciclo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ciclo(null);
                }
            } else if (name.equals("descDepto")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$descDepto((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$descDepto(null);
                }
            } else if (name.equals("codMunicipio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codMunicipio((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codMunicipio(null);
                }
            } else if (name.equals("codSector")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codSector((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codSector(null);
                }
            } else if (name.equals("codRuta")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codRuta((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codRuta(null);
                }
            } else if (name.equals("cicloReal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cicloReal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cicloReal(null);
                }
            } else if (name.equals("direccion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$direccion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$direccion(null);
                }
            } else if (name.equals("contador")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$contador((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$contador(null);
                }
            } else if (name.equals("marca")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$marca((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$marca(null);
                }
            } else if (name.equals("tipoMedidor")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$tipoMedidor((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$tipoMedidor(null);
                }
            } else if (name.equals("digitos")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$digitos((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$digitos(null);
                }
            } else if (name.equals("lectura")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lectura((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lectura(null);
                }
            } else if (name.equals("observacion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$observacion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$observacion(null);
                }
            } else if (name.equals("informe")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$informe((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$informe(null);
                }
            } else if (name.equals("codReferencia")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codReferencia((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codReferencia(null);
                }
            } else if (name.equals("latitud")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$latitud((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$latitud(null);
                }
            } else if (name.equals("longitud")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$longitud((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$longitud(null);
                }
            } else if (name.equals("altitud")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$altitud((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$altitud(null);
                }
            } else if (name.equals("numSatelites")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$numSatelites((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$numSatelites(null);
                }
            } else if (name.equals("fechaHora")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaHora((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaHora(null);
                }
            } else if (name.equals("lector")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lector((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lector(null);
                }
            } else if (name.equals("serial")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serial((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serial(null);
                }
            } else if (name.equals("foto1")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$foto1((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$foto1(null);
                }
            } else if (name.equals("foto2")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$foto2((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$foto2(null);
                }
            } else if (name.equals("foto3")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$foto3((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$foto3(null);
                }
            } else if (name.equals("nombreArchivo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nombreArchivo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nombreArchivo(null);
                }
            } else if (name.equals("estadoEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$estadoEnvio((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$estadoEnvio(null);
                }
            } else if (name.equals("intentosEnvio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$intentosEnvio((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'intentosEnvio' to null.");
                }
            } else if (name.equals("mensajeError")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$mensajeError((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$mensajeError(null);
                }
            } else if (name.equals("fechaCreacion")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$fechaCreacion(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        objProxy.realmSet$fechaCreacion(new Date(timestamp));
                    }
                } else {
                    objProxy.realmSet$fechaCreacion(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("fechaEnvio")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$fechaEnvio(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        objProxy.realmSet$fechaEnvio(new Date(timestamp));
                    }
                } else {
                    objProxy.realmSet$fechaEnvio(JsonUtils.stringToDate(reader.nextString()));
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'idRealm'.");
        }
        return realm.copyToRealmOrUpdate(obj);
    }

    static com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class), false, Collections.<String>emptyList());
        io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy obj = new io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.gstolima.comunicaciones.EnvioCuentaNueva copyOrUpdate(Realm realm, EnvioCuentaNuevaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioCuentaNueva object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.gstolima.comunicaciones.EnvioCuentaNueva) cachedRealmObject;
        }

        com.gstolima.comunicaciones.EnvioCuentaNueva realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
            long pkColumnKey = columnInfo.idRealmColKey;
            String value = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$idRealm();
            long objKey = Table.NO_MATCH;
            if (value == null) {
                objKey = table.findFirstNull(pkColumnKey);
            } else {
                objKey = table.findFirstString(pkColumnKey, value);
            }
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.gstolima.comunicaciones.EnvioCuentaNueva copy(Realm realm, EnvioCuentaNuevaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioCuentaNueva newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.EnvioCuentaNueva) cachedRealmObject;
        }

        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface unmanagedSource = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) newObject;

        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.claveRutaColKey, unmanagedSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, unmanagedSource.realmGet$moduloTrabajo());
        builder.addString(columnInfo.idRealmColKey, unmanagedSource.realmGet$idRealm());
        builder.addString(columnInfo.cicloColKey, unmanagedSource.realmGet$ciclo());
        builder.addString(columnInfo.descDeptoColKey, unmanagedSource.realmGet$descDepto());
        builder.addString(columnInfo.codMunicipioColKey, unmanagedSource.realmGet$codMunicipio());
        builder.addString(columnInfo.codSectorColKey, unmanagedSource.realmGet$codSector());
        builder.addString(columnInfo.codRutaColKey, unmanagedSource.realmGet$codRuta());
        builder.addString(columnInfo.cicloRealColKey, unmanagedSource.realmGet$cicloReal());
        builder.addString(columnInfo.direccionColKey, unmanagedSource.realmGet$direccion());
        builder.addString(columnInfo.contadorColKey, unmanagedSource.realmGet$contador());
        builder.addString(columnInfo.marcaColKey, unmanagedSource.realmGet$marca());
        builder.addString(columnInfo.tipoMedidorColKey, unmanagedSource.realmGet$tipoMedidor());
        builder.addString(columnInfo.digitosColKey, unmanagedSource.realmGet$digitos());
        builder.addString(columnInfo.lecturaColKey, unmanagedSource.realmGet$lectura());
        builder.addString(columnInfo.observacionColKey, unmanagedSource.realmGet$observacion());
        builder.addString(columnInfo.informeColKey, unmanagedSource.realmGet$informe());
        builder.addString(columnInfo.codReferenciaColKey, unmanagedSource.realmGet$codReferencia());
        builder.addString(columnInfo.latitudColKey, unmanagedSource.realmGet$latitud());
        builder.addString(columnInfo.longitudColKey, unmanagedSource.realmGet$longitud());
        builder.addString(columnInfo.altitudColKey, unmanagedSource.realmGet$altitud());
        builder.addString(columnInfo.numSatelitesColKey, unmanagedSource.realmGet$numSatelites());
        builder.addString(columnInfo.fechaHoraColKey, unmanagedSource.realmGet$fechaHora());
        builder.addString(columnInfo.lectorColKey, unmanagedSource.realmGet$lector());
        builder.addString(columnInfo.serialColKey, unmanagedSource.realmGet$serial());
        builder.addString(columnInfo.foto1ColKey, unmanagedSource.realmGet$foto1());
        builder.addString(columnInfo.foto2ColKey, unmanagedSource.realmGet$foto2());
        builder.addString(columnInfo.foto3ColKey, unmanagedSource.realmGet$foto3());
        builder.addString(columnInfo.nombreArchivoColKey, unmanagedSource.realmGet$nombreArchivo());
        builder.addString(columnInfo.estadoEnvioColKey, unmanagedSource.realmGet$estadoEnvio());
        builder.addInteger(columnInfo.intentosEnvioColKey, unmanagedSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.mensajeErrorColKey, unmanagedSource.realmGet$mensajeError());
        builder.addDate(columnInfo.fechaCreacionColKey, unmanagedSource.realmGet$fechaCreacion());
        builder.addDate(columnInfo.fechaEnvioColKey, unmanagedSource.realmGet$fechaEnvio());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.gstolima.comunicaciones.EnvioCuentaNueva object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long tableNativePtr = table.getNativePtr();
        EnvioCuentaNuevaColumnInfo columnInfo = (EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        String primaryKeyValue = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$idRealm();
        long objKey = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
        } else {
            objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        }
        String realmGet$descDepto = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$descDepto();
        if (realmGet$descDepto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descDeptoColKey, objKey, realmGet$descDepto, false);
        }
        String realmGet$codMunicipio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codMunicipio();
        if (realmGet$codMunicipio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codMunicipioColKey, objKey, realmGet$codMunicipio, false);
        }
        String realmGet$codSector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codSector();
        if (realmGet$codSector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codSectorColKey, objKey, realmGet$codSector, false);
        }
        String realmGet$codRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codRuta();
        if (realmGet$codRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codRutaColKey, objKey, realmGet$codRuta, false);
        }
        String realmGet$cicloReal = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$cicloReal();
        if (realmGet$cicloReal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloRealColKey, objKey, realmGet$cicloReal, false);
        }
        String realmGet$direccion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$direccion();
        if (realmGet$direccion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.direccionColKey, objKey, realmGet$direccion, false);
        }
        String realmGet$contador = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$contador();
        if (realmGet$contador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.contadorColKey, objKey, realmGet$contador, false);
        }
        String realmGet$marca = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$marca();
        if (realmGet$marca != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.marcaColKey, objKey, realmGet$marca, false);
        }
        String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$tipoMedidor();
        if (realmGet$tipoMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
        }
        String realmGet$digitos = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$digitos();
        if (realmGet$digitos != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.digitosColKey, objKey, realmGet$digitos, false);
        }
        String realmGet$lectura = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lectura();
        if (realmGet$lectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaColKey, objKey, realmGet$lectura, false);
        }
        String realmGet$observacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$observacion();
        if (realmGet$observacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.observacionColKey, objKey, realmGet$observacion, false);
        }
        String realmGet$informe = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$informe();
        if (realmGet$informe != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
        }
        String realmGet$codReferencia = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codReferencia();
        if (realmGet$codReferencia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codReferenciaColKey, objKey, realmGet$codReferencia, false);
        }
        String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$latitud();
        if (realmGet$latitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
        }
        String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$longitud();
        if (realmGet$longitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
        }
        String realmGet$altitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$altitud();
        if (realmGet$altitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.altitudColKey, objKey, realmGet$altitud, false);
        }
        String realmGet$numSatelites = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$numSatelites();
        if (realmGet$numSatelites != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numSatelitesColKey, objKey, realmGet$numSatelites, false);
        }
        String realmGet$fechaHora = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaHora();
        if (realmGet$fechaHora != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraColKey, objKey, realmGet$fechaHora, false);
        }
        String realmGet$lector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lector();
        if (realmGet$lector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
        }
        String realmGet$serial = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$serial();
        if (realmGet$serial != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serialColKey, objKey, realmGet$serial, false);
        }
        String realmGet$foto1 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto1();
        if (realmGet$foto1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto1ColKey, objKey, realmGet$foto1, false);
        }
        String realmGet$foto2 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto2();
        if (realmGet$foto2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto2ColKey, objKey, realmGet$foto2, false);
        }
        String realmGet$foto3 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto3();
        if (realmGet$foto3 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto3ColKey, objKey, realmGet$foto3, false);
        }
        String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$nombreArchivo();
        if (realmGet$nombreArchivo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
        }
        String realmGet$estadoEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$estadoEnvio();
        if (realmGet$estadoEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, realmGet$estadoEnvio, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$mensajeError = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$mensajeError();
        if (realmGet$mensajeError != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, realmGet$mensajeError, false);
        }
        java.util.Date realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion.getTime(), false);
        }
        java.util.Date realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaEnvio();
        if (realmGet$fechaEnvio != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio.getTime(), false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long tableNativePtr = table.getNativePtr();
        EnvioCuentaNuevaColumnInfo columnInfo = (EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        com.gstolima.comunicaciones.EnvioCuentaNueva object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioCuentaNueva) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$idRealm();
            long objKey = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
            } else {
                objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            }
            String realmGet$descDepto = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$descDepto();
            if (realmGet$descDepto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descDeptoColKey, objKey, realmGet$descDepto, false);
            }
            String realmGet$codMunicipio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codMunicipio();
            if (realmGet$codMunicipio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codMunicipioColKey, objKey, realmGet$codMunicipio, false);
            }
            String realmGet$codSector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codSector();
            if (realmGet$codSector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codSectorColKey, objKey, realmGet$codSector, false);
            }
            String realmGet$codRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codRuta();
            if (realmGet$codRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codRutaColKey, objKey, realmGet$codRuta, false);
            }
            String realmGet$cicloReal = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$cicloReal();
            if (realmGet$cicloReal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloRealColKey, objKey, realmGet$cicloReal, false);
            }
            String realmGet$direccion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$direccion();
            if (realmGet$direccion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.direccionColKey, objKey, realmGet$direccion, false);
            }
            String realmGet$contador = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$contador();
            if (realmGet$contador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.contadorColKey, objKey, realmGet$contador, false);
            }
            String realmGet$marca = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$marca();
            if (realmGet$marca != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.marcaColKey, objKey, realmGet$marca, false);
            }
            String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$tipoMedidor();
            if (realmGet$tipoMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
            }
            String realmGet$digitos = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$digitos();
            if (realmGet$digitos != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.digitosColKey, objKey, realmGet$digitos, false);
            }
            String realmGet$lectura = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lectura();
            if (realmGet$lectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaColKey, objKey, realmGet$lectura, false);
            }
            String realmGet$observacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$observacion();
            if (realmGet$observacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.observacionColKey, objKey, realmGet$observacion, false);
            }
            String realmGet$informe = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$informe();
            if (realmGet$informe != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
            }
            String realmGet$codReferencia = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codReferencia();
            if (realmGet$codReferencia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codReferenciaColKey, objKey, realmGet$codReferencia, false);
            }
            String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$latitud();
            if (realmGet$latitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
            }
            String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$longitud();
            if (realmGet$longitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
            }
            String realmGet$altitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$altitud();
            if (realmGet$altitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.altitudColKey, objKey, realmGet$altitud, false);
            }
            String realmGet$numSatelites = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$numSatelites();
            if (realmGet$numSatelites != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numSatelitesColKey, objKey, realmGet$numSatelites, false);
            }
            String realmGet$fechaHora = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaHora();
            if (realmGet$fechaHora != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraColKey, objKey, realmGet$fechaHora, false);
            }
            String realmGet$lector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lector();
            if (realmGet$lector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
            }
            String realmGet$serial = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$serial();
            if (realmGet$serial != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serialColKey, objKey, realmGet$serial, false);
            }
            String realmGet$foto1 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto1();
            if (realmGet$foto1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto1ColKey, objKey, realmGet$foto1, false);
            }
            String realmGet$foto2 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto2();
            if (realmGet$foto2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto2ColKey, objKey, realmGet$foto2, false);
            }
            String realmGet$foto3 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto3();
            if (realmGet$foto3 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto3ColKey, objKey, realmGet$foto3, false);
            }
            String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$nombreArchivo();
            if (realmGet$nombreArchivo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
            }
            String realmGet$estadoEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$estadoEnvio();
            if (realmGet$estadoEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, realmGet$estadoEnvio, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$mensajeError = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$mensajeError();
            if (realmGet$mensajeError != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, realmGet$mensajeError, false);
            }
            java.util.Date realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion.getTime(), false);
            }
            java.util.Date realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaEnvio();
            if (realmGet$fechaEnvio != null) {
                Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio.getTime(), false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.gstolima.comunicaciones.EnvioCuentaNueva object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long tableNativePtr = table.getNativePtr();
        EnvioCuentaNuevaColumnInfo columnInfo = (EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        String primaryKeyValue = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$idRealm();
        long objKey = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
        } else {
            objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
        }
        String realmGet$descDepto = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$descDepto();
        if (realmGet$descDepto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descDeptoColKey, objKey, realmGet$descDepto, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descDeptoColKey, objKey, false);
        }
        String realmGet$codMunicipio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codMunicipio();
        if (realmGet$codMunicipio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codMunicipioColKey, objKey, realmGet$codMunicipio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codMunicipioColKey, objKey, false);
        }
        String realmGet$codSector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codSector();
        if (realmGet$codSector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codSectorColKey, objKey, realmGet$codSector, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codSectorColKey, objKey, false);
        }
        String realmGet$codRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codRuta();
        if (realmGet$codRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codRutaColKey, objKey, realmGet$codRuta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codRutaColKey, objKey, false);
        }
        String realmGet$cicloReal = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$cicloReal();
        if (realmGet$cicloReal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloRealColKey, objKey, realmGet$cicloReal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cicloRealColKey, objKey, false);
        }
        String realmGet$direccion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$direccion();
        if (realmGet$direccion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.direccionColKey, objKey, realmGet$direccion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.direccionColKey, objKey, false);
        }
        String realmGet$contador = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$contador();
        if (realmGet$contador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.contadorColKey, objKey, realmGet$contador, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.contadorColKey, objKey, false);
        }
        String realmGet$marca = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$marca();
        if (realmGet$marca != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.marcaColKey, objKey, realmGet$marca, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.marcaColKey, objKey, false);
        }
        String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$tipoMedidor();
        if (realmGet$tipoMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, false);
        }
        String realmGet$digitos = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$digitos();
        if (realmGet$digitos != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.digitosColKey, objKey, realmGet$digitos, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.digitosColKey, objKey, false);
        }
        String realmGet$lectura = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lectura();
        if (realmGet$lectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaColKey, objKey, realmGet$lectura, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lecturaColKey, objKey, false);
        }
        String realmGet$observacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$observacion();
        if (realmGet$observacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.observacionColKey, objKey, realmGet$observacion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.observacionColKey, objKey, false);
        }
        String realmGet$informe = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$informe();
        if (realmGet$informe != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.informeColKey, objKey, false);
        }
        String realmGet$codReferencia = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codReferencia();
        if (realmGet$codReferencia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codReferenciaColKey, objKey, realmGet$codReferencia, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codReferenciaColKey, objKey, false);
        }
        String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$latitud();
        if (realmGet$latitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.latitudColKey, objKey, false);
        }
        String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$longitud();
        if (realmGet$longitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.longitudColKey, objKey, false);
        }
        String realmGet$altitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$altitud();
        if (realmGet$altitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.altitudColKey, objKey, realmGet$altitud, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.altitudColKey, objKey, false);
        }
        String realmGet$numSatelites = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$numSatelites();
        if (realmGet$numSatelites != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numSatelitesColKey, objKey, realmGet$numSatelites, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.numSatelitesColKey, objKey, false);
        }
        String realmGet$fechaHora = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaHora();
        if (realmGet$fechaHora != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraColKey, objKey, realmGet$fechaHora, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraColKey, objKey, false);
        }
        String realmGet$lector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lector();
        if (realmGet$lector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lectorColKey, objKey, false);
        }
        String realmGet$serial = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$serial();
        if (realmGet$serial != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serialColKey, objKey, realmGet$serial, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serialColKey, objKey, false);
        }
        String realmGet$foto1 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto1();
        if (realmGet$foto1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto1ColKey, objKey, realmGet$foto1, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.foto1ColKey, objKey, false);
        }
        String realmGet$foto2 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto2();
        if (realmGet$foto2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto2ColKey, objKey, realmGet$foto2, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.foto2ColKey, objKey, false);
        }
        String realmGet$foto3 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto3();
        if (realmGet$foto3 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.foto3ColKey, objKey, realmGet$foto3, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.foto3ColKey, objKey, false);
        }
        String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$nombreArchivo();
        if (realmGet$nombreArchivo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, false);
        }
        String realmGet$estadoEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$estadoEnvio();
        if (realmGet$estadoEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, realmGet$estadoEnvio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$mensajeError = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$mensajeError();
        if (realmGet$mensajeError != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, realmGet$mensajeError, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, false);
        }
        java.util.Date realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion.getTime(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
        }
        java.util.Date realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaEnvio();
        if (realmGet$fechaEnvio != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio.getTime(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long tableNativePtr = table.getNativePtr();
        EnvioCuentaNuevaColumnInfo columnInfo = (EnvioCuentaNuevaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        com.gstolima.comunicaciones.EnvioCuentaNueva object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioCuentaNueva) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            String primaryKeyValue = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$idRealm();
            long objKey = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                objKey = Table.nativeFindFirstNull(tableNativePtr, pkColumnKey);
            } else {
                objKey = Table.nativeFindFirstString(tableNativePtr, pkColumnKey, primaryKeyValue);
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
            }
            String realmGet$descDepto = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$descDepto();
            if (realmGet$descDepto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descDeptoColKey, objKey, realmGet$descDepto, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.descDeptoColKey, objKey, false);
            }
            String realmGet$codMunicipio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codMunicipio();
            if (realmGet$codMunicipio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codMunicipioColKey, objKey, realmGet$codMunicipio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codMunicipioColKey, objKey, false);
            }
            String realmGet$codSector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codSector();
            if (realmGet$codSector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codSectorColKey, objKey, realmGet$codSector, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codSectorColKey, objKey, false);
            }
            String realmGet$codRuta = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codRuta();
            if (realmGet$codRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codRutaColKey, objKey, realmGet$codRuta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codRutaColKey, objKey, false);
            }
            String realmGet$cicloReal = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$cicloReal();
            if (realmGet$cicloReal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloRealColKey, objKey, realmGet$cicloReal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cicloRealColKey, objKey, false);
            }
            String realmGet$direccion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$direccion();
            if (realmGet$direccion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.direccionColKey, objKey, realmGet$direccion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.direccionColKey, objKey, false);
            }
            String realmGet$contador = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$contador();
            if (realmGet$contador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.contadorColKey, objKey, realmGet$contador, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.contadorColKey, objKey, false);
            }
            String realmGet$marca = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$marca();
            if (realmGet$marca != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.marcaColKey, objKey, realmGet$marca, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.marcaColKey, objKey, false);
            }
            String realmGet$tipoMedidor = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$tipoMedidor();
            if (realmGet$tipoMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, realmGet$tipoMedidor, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.tipoMedidorColKey, objKey, false);
            }
            String realmGet$digitos = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$digitos();
            if (realmGet$digitos != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.digitosColKey, objKey, realmGet$digitos, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.digitosColKey, objKey, false);
            }
            String realmGet$lectura = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lectura();
            if (realmGet$lectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaColKey, objKey, realmGet$lectura, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lecturaColKey, objKey, false);
            }
            String realmGet$observacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$observacion();
            if (realmGet$observacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.observacionColKey, objKey, realmGet$observacion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.observacionColKey, objKey, false);
            }
            String realmGet$informe = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$informe();
            if (realmGet$informe != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.informeColKey, objKey, false);
            }
            String realmGet$codReferencia = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$codReferencia();
            if (realmGet$codReferencia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codReferenciaColKey, objKey, realmGet$codReferencia, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codReferenciaColKey, objKey, false);
            }
            String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$latitud();
            if (realmGet$latitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.latitudColKey, objKey, false);
            }
            String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$longitud();
            if (realmGet$longitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.longitudColKey, objKey, false);
            }
            String realmGet$altitud = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$altitud();
            if (realmGet$altitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.altitudColKey, objKey, realmGet$altitud, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.altitudColKey, objKey, false);
            }
            String realmGet$numSatelites = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$numSatelites();
            if (realmGet$numSatelites != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numSatelitesColKey, objKey, realmGet$numSatelites, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.numSatelitesColKey, objKey, false);
            }
            String realmGet$fechaHora = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaHora();
            if (realmGet$fechaHora != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraColKey, objKey, realmGet$fechaHora, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraColKey, objKey, false);
            }
            String realmGet$lector = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$lector();
            if (realmGet$lector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lectorColKey, objKey, realmGet$lector, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lectorColKey, objKey, false);
            }
            String realmGet$serial = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$serial();
            if (realmGet$serial != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serialColKey, objKey, realmGet$serial, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serialColKey, objKey, false);
            }
            String realmGet$foto1 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto1();
            if (realmGet$foto1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto1ColKey, objKey, realmGet$foto1, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.foto1ColKey, objKey, false);
            }
            String realmGet$foto2 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto2();
            if (realmGet$foto2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto2ColKey, objKey, realmGet$foto2, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.foto2ColKey, objKey, false);
            }
            String realmGet$foto3 = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$foto3();
            if (realmGet$foto3 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.foto3ColKey, objKey, realmGet$foto3, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.foto3ColKey, objKey, false);
            }
            String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$nombreArchivo();
            if (realmGet$nombreArchivo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, false);
            }
            String realmGet$estadoEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$estadoEnvio();
            if (realmGet$estadoEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, realmGet$estadoEnvio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$mensajeError = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$mensajeError();
            if (realmGet$mensajeError != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, realmGet$mensajeError, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.mensajeErrorColKey, objKey, false);
            }
            java.util.Date realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion.getTime(), false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
            }
            java.util.Date realmGet$fechaEnvio = ((com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) object).realmGet$fechaEnvio();
            if (realmGet$fechaEnvio != null) {
                Table.nativeSetTimestamp(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, realmGet$fechaEnvio.getTime(), false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioColKey, objKey, false);
            }
        }
    }

    public static com.gstolima.comunicaciones.EnvioCuentaNueva createDetachedCopy(com.gstolima.comunicaciones.EnvioCuentaNueva realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.gstolima.comunicaciones.EnvioCuentaNueva unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.gstolima.comunicaciones.EnvioCuentaNueva();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.gstolima.comunicaciones.EnvioCuentaNueva) cachedObject.object;
            }
            unmanagedObject = (com.gstolima.comunicaciones.EnvioCuentaNueva) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface unmanagedCopy = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) unmanagedObject;
        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface realmSource = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) realmObject;
        Realm objectRealm = (Realm) ((RealmObjectProxy) realmObject).realmGet$proxyState().getRealm$realm();
        unmanagedCopy.realmSet$claveRuta(realmSource.realmGet$claveRuta());
        unmanagedCopy.realmSet$moduloTrabajo(realmSource.realmGet$moduloTrabajo());
        unmanagedCopy.realmSet$idRealm(realmSource.realmGet$idRealm());
        unmanagedCopy.realmSet$ciclo(realmSource.realmGet$ciclo());
        unmanagedCopy.realmSet$descDepto(realmSource.realmGet$descDepto());
        unmanagedCopy.realmSet$codMunicipio(realmSource.realmGet$codMunicipio());
        unmanagedCopy.realmSet$codSector(realmSource.realmGet$codSector());
        unmanagedCopy.realmSet$codRuta(realmSource.realmGet$codRuta());
        unmanagedCopy.realmSet$cicloReal(realmSource.realmGet$cicloReal());
        unmanagedCopy.realmSet$direccion(realmSource.realmGet$direccion());
        unmanagedCopy.realmSet$contador(realmSource.realmGet$contador());
        unmanagedCopy.realmSet$marca(realmSource.realmGet$marca());
        unmanagedCopy.realmSet$tipoMedidor(realmSource.realmGet$tipoMedidor());
        unmanagedCopy.realmSet$digitos(realmSource.realmGet$digitos());
        unmanagedCopy.realmSet$lectura(realmSource.realmGet$lectura());
        unmanagedCopy.realmSet$observacion(realmSource.realmGet$observacion());
        unmanagedCopy.realmSet$informe(realmSource.realmGet$informe());
        unmanagedCopy.realmSet$codReferencia(realmSource.realmGet$codReferencia());
        unmanagedCopy.realmSet$latitud(realmSource.realmGet$latitud());
        unmanagedCopy.realmSet$longitud(realmSource.realmGet$longitud());
        unmanagedCopy.realmSet$altitud(realmSource.realmGet$altitud());
        unmanagedCopy.realmSet$numSatelites(realmSource.realmGet$numSatelites());
        unmanagedCopy.realmSet$fechaHora(realmSource.realmGet$fechaHora());
        unmanagedCopy.realmSet$lector(realmSource.realmGet$lector());
        unmanagedCopy.realmSet$serial(realmSource.realmGet$serial());
        unmanagedCopy.realmSet$foto1(realmSource.realmGet$foto1());
        unmanagedCopy.realmSet$foto2(realmSource.realmGet$foto2());
        unmanagedCopy.realmSet$foto3(realmSource.realmGet$foto3());
        unmanagedCopy.realmSet$nombreArchivo(realmSource.realmGet$nombreArchivo());
        unmanagedCopy.realmSet$estadoEnvio(realmSource.realmGet$estadoEnvio());
        unmanagedCopy.realmSet$intentosEnvio(realmSource.realmGet$intentosEnvio());
        unmanagedCopy.realmSet$mensajeError(realmSource.realmGet$mensajeError());
        unmanagedCopy.realmSet$fechaCreacion(realmSource.realmGet$fechaCreacion());
        unmanagedCopy.realmSet$fechaEnvio(realmSource.realmGet$fechaEnvio());

        return unmanagedObject;
    }

    static com.gstolima.comunicaciones.EnvioCuentaNueva update(Realm realm, EnvioCuentaNuevaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioCuentaNueva realmObject, com.gstolima.comunicaciones.EnvioCuentaNueva newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface realmObjectTarget = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) realmObject;
        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface realmObjectSource = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxyInterface) newObject;
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioCuentaNueva.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addString(columnInfo.claveRutaColKey, realmObjectSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, realmObjectSource.realmGet$moduloTrabajo());
        builder.addString(columnInfo.idRealmColKey, realmObjectSource.realmGet$idRealm());
        builder.addString(columnInfo.cicloColKey, realmObjectSource.realmGet$ciclo());
        builder.addString(columnInfo.descDeptoColKey, realmObjectSource.realmGet$descDepto());
        builder.addString(columnInfo.codMunicipioColKey, realmObjectSource.realmGet$codMunicipio());
        builder.addString(columnInfo.codSectorColKey, realmObjectSource.realmGet$codSector());
        builder.addString(columnInfo.codRutaColKey, realmObjectSource.realmGet$codRuta());
        builder.addString(columnInfo.cicloRealColKey, realmObjectSource.realmGet$cicloReal());
        builder.addString(columnInfo.direccionColKey, realmObjectSource.realmGet$direccion());
        builder.addString(columnInfo.contadorColKey, realmObjectSource.realmGet$contador());
        builder.addString(columnInfo.marcaColKey, realmObjectSource.realmGet$marca());
        builder.addString(columnInfo.tipoMedidorColKey, realmObjectSource.realmGet$tipoMedidor());
        builder.addString(columnInfo.digitosColKey, realmObjectSource.realmGet$digitos());
        builder.addString(columnInfo.lecturaColKey, realmObjectSource.realmGet$lectura());
        builder.addString(columnInfo.observacionColKey, realmObjectSource.realmGet$observacion());
        builder.addString(columnInfo.informeColKey, realmObjectSource.realmGet$informe());
        builder.addString(columnInfo.codReferenciaColKey, realmObjectSource.realmGet$codReferencia());
        builder.addString(columnInfo.latitudColKey, realmObjectSource.realmGet$latitud());
        builder.addString(columnInfo.longitudColKey, realmObjectSource.realmGet$longitud());
        builder.addString(columnInfo.altitudColKey, realmObjectSource.realmGet$altitud());
        builder.addString(columnInfo.numSatelitesColKey, realmObjectSource.realmGet$numSatelites());
        builder.addString(columnInfo.fechaHoraColKey, realmObjectSource.realmGet$fechaHora());
        builder.addString(columnInfo.lectorColKey, realmObjectSource.realmGet$lector());
        builder.addString(columnInfo.serialColKey, realmObjectSource.realmGet$serial());
        builder.addString(columnInfo.foto1ColKey, realmObjectSource.realmGet$foto1());
        builder.addString(columnInfo.foto2ColKey, realmObjectSource.realmGet$foto2());
        builder.addString(columnInfo.foto3ColKey, realmObjectSource.realmGet$foto3());
        builder.addString(columnInfo.nombreArchivoColKey, realmObjectSource.realmGet$nombreArchivo());
        builder.addString(columnInfo.estadoEnvioColKey, realmObjectSource.realmGet$estadoEnvio());
        builder.addInteger(columnInfo.intentosEnvioColKey, realmObjectSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.mensajeErrorColKey, realmObjectSource.realmGet$mensajeError());
        builder.addDate(columnInfo.fechaCreacionColKey, realmObjectSource.realmGet$fechaCreacion());
        builder.addDate(columnInfo.fechaEnvioColKey, realmObjectSource.realmGet$fechaEnvio());

        builder.updateExistingTopLevelObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("EnvioCuentaNueva = proxy[");
        stringBuilder.append("{claveRuta:");
        stringBuilder.append(realmGet$claveRuta() != null ? realmGet$claveRuta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{moduloTrabajo:");
        stringBuilder.append(realmGet$moduloTrabajo() != null ? realmGet$moduloTrabajo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idRealm:");
        stringBuilder.append(realmGet$idRealm() != null ? realmGet$idRealm() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ciclo:");
        stringBuilder.append(realmGet$ciclo() != null ? realmGet$ciclo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{descDepto:");
        stringBuilder.append(realmGet$descDepto() != null ? realmGet$descDepto() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codMunicipio:");
        stringBuilder.append(realmGet$codMunicipio() != null ? realmGet$codMunicipio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codSector:");
        stringBuilder.append(realmGet$codSector() != null ? realmGet$codSector() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codRuta:");
        stringBuilder.append(realmGet$codRuta() != null ? realmGet$codRuta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cicloReal:");
        stringBuilder.append(realmGet$cicloReal() != null ? realmGet$cicloReal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{direccion:");
        stringBuilder.append(realmGet$direccion() != null ? realmGet$direccion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{contador:");
        stringBuilder.append(realmGet$contador() != null ? realmGet$contador() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{marca:");
        stringBuilder.append(realmGet$marca() != null ? realmGet$marca() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tipoMedidor:");
        stringBuilder.append(realmGet$tipoMedidor() != null ? realmGet$tipoMedidor() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{digitos:");
        stringBuilder.append(realmGet$digitos() != null ? realmGet$digitos() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lectura:");
        stringBuilder.append(realmGet$lectura() != null ? realmGet$lectura() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{observacion:");
        stringBuilder.append(realmGet$observacion() != null ? realmGet$observacion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{informe:");
        stringBuilder.append(realmGet$informe() != null ? realmGet$informe() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codReferencia:");
        stringBuilder.append(realmGet$codReferencia() != null ? realmGet$codReferencia() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{latitud:");
        stringBuilder.append(realmGet$latitud() != null ? realmGet$latitud() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{longitud:");
        stringBuilder.append(realmGet$longitud() != null ? realmGet$longitud() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{altitud:");
        stringBuilder.append(realmGet$altitud() != null ? realmGet$altitud() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{numSatelites:");
        stringBuilder.append(realmGet$numSatelites() != null ? realmGet$numSatelites() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaHora:");
        stringBuilder.append(realmGet$fechaHora() != null ? realmGet$fechaHora() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lector:");
        stringBuilder.append(realmGet$lector() != null ? realmGet$lector() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serial:");
        stringBuilder.append(realmGet$serial() != null ? realmGet$serial() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{foto1:");
        stringBuilder.append(realmGet$foto1() != null ? realmGet$foto1() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{foto2:");
        stringBuilder.append(realmGet$foto2() != null ? realmGet$foto2() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{foto3:");
        stringBuilder.append(realmGet$foto3() != null ? realmGet$foto3() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{nombreArchivo:");
        stringBuilder.append(realmGet$nombreArchivo() != null ? realmGet$nombreArchivo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{estadoEnvio:");
        stringBuilder.append(realmGet$estadoEnvio() != null ? realmGet$estadoEnvio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{intentosEnvio:");
        stringBuilder.append(realmGet$intentosEnvio());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{mensajeError:");
        stringBuilder.append(realmGet$mensajeError() != null ? realmGet$mensajeError() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaCreacion:");
        stringBuilder.append(realmGet$fechaCreacion() != null ? realmGet$fechaCreacion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaEnvio:");
        stringBuilder.append(realmGet$fechaEnvio() != null ? realmGet$fechaEnvio() : "null");
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
        com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy aEnvioCuentaNueva = (com_gstolima_comunicaciones_EnvioCuentaNuevaRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aEnvioCuentaNueva.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aEnvioCuentaNueva.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aEnvioCuentaNueva.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
