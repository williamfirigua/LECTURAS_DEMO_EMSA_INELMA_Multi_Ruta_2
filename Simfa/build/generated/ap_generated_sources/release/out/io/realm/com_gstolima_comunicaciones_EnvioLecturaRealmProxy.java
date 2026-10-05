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
public class com_gstolima_comunicaciones_EnvioLecturaRealmProxy extends com.gstolima.comunicaciones.EnvioLectura
    implements RealmObjectProxy, com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface {

    static final class EnvioLecturaColumnInfo extends ColumnInfo {
        long claveRutaColKey;
        long moduloTrabajoColKey;
        long idRealmColKey;
        long cicloColKey;
        long municipioColKey;
        long seccionColKey;
        long departamentoColKey;
        long cuentaColKey;
        long nroContadorColKey;
        long marcaMedidorColKey;
        long fichaCatastralColKey;
        long fechaHoraLecturaColKey;
        long horaImpresionColKey;
        long lecturaTomadaColKey;
        long causaNoLecturaColKey;
        long codLectorColKey;
        long terminalColKey;
        long periodoLecturaColKey;
        long longitudColKey;
        long latitudColKey;
        long nroSatelitesColKey;
        long informeColKey;
        long criticaPdaColKey;
        long estadoEnvioOriginalColKey;
        long comentarioColKey;
        long intentosColKey;
        long fechaHoraSateliteColKey;
        long altitudSateliteColKey;
        long distanciaColKey;
        long lecturaModificada1ColKey;
        long lecturaModificada2ColKey;
        long tiempoColKey;
        long consumoFacturadoColKey;
        long lecturaAnteriorColKey;
        long estratoColKey;
        long usoColKey;
        long nombreArchivoColKey;
        long idRegistroColKey;
        long totalCausasNoLecturaColKey;
        long totalLecturasColKey;
        long totalConsumoBajoColKey;
        long totalConsumoAltoColKey;
        long totalConsumoNormalColKey;
        long totalLecturasIgualesColKey;
        long totalLeidasColKey;
        long numObserColKey;
        long numInformesColKey;
        long totalTiempoPromColKey;
        long totalDistanciaPromColKey;
        long codigoSacColKey;
        long annoColKey;
        long mesColKey;
        long digitoChequeoColKey;
        long idContadorColKey;
        long descAnomaliaColKey;
        long descComentarioColKey;
        long estadoEnvioColKey;
        long fechaCreacionColKey;
        long fechaEnvioApiColKey;
        long intentosEnvioColKey;
        long errorEnvioColKey;

        EnvioLecturaColumnInfo(OsSchemaInfo schemaInfo) {
            super(61);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("EnvioLectura");
            this.claveRutaColKey = addColumnDetails("claveRuta", "claveRuta", objectSchemaInfo);
            this.moduloTrabajoColKey = addColumnDetails("moduloTrabajo", "moduloTrabajo", objectSchemaInfo);
            this.idRealmColKey = addColumnDetails("idRealm", "idRealm", objectSchemaInfo);
            this.cicloColKey = addColumnDetails("ciclo", "ciclo", objectSchemaInfo);
            this.municipioColKey = addColumnDetails("municipio", "municipio", objectSchemaInfo);
            this.seccionColKey = addColumnDetails("seccion", "seccion", objectSchemaInfo);
            this.departamentoColKey = addColumnDetails("departamento", "departamento", objectSchemaInfo);
            this.cuentaColKey = addColumnDetails("cuenta", "cuenta", objectSchemaInfo);
            this.nroContadorColKey = addColumnDetails("nroContador", "nroContador", objectSchemaInfo);
            this.marcaMedidorColKey = addColumnDetails("marcaMedidor", "marcaMedidor", objectSchemaInfo);
            this.fichaCatastralColKey = addColumnDetails("fichaCatastral", "fichaCatastral", objectSchemaInfo);
            this.fechaHoraLecturaColKey = addColumnDetails("fechaHoraLectura", "fechaHoraLectura", objectSchemaInfo);
            this.horaImpresionColKey = addColumnDetails("horaImpresion", "horaImpresion", objectSchemaInfo);
            this.lecturaTomadaColKey = addColumnDetails("lecturaTomada", "lecturaTomada", objectSchemaInfo);
            this.causaNoLecturaColKey = addColumnDetails("causaNoLectura", "causaNoLectura", objectSchemaInfo);
            this.codLectorColKey = addColumnDetails("codLector", "codLector", objectSchemaInfo);
            this.terminalColKey = addColumnDetails("terminal", "terminal", objectSchemaInfo);
            this.periodoLecturaColKey = addColumnDetails("periodoLectura", "periodoLectura", objectSchemaInfo);
            this.longitudColKey = addColumnDetails("longitud", "longitud", objectSchemaInfo);
            this.latitudColKey = addColumnDetails("latitud", "latitud", objectSchemaInfo);
            this.nroSatelitesColKey = addColumnDetails("nroSatelites", "nroSatelites", objectSchemaInfo);
            this.informeColKey = addColumnDetails("informe", "informe", objectSchemaInfo);
            this.criticaPdaColKey = addColumnDetails("criticaPda", "criticaPda", objectSchemaInfo);
            this.estadoEnvioOriginalColKey = addColumnDetails("estadoEnvioOriginal", "estadoEnvioOriginal", objectSchemaInfo);
            this.comentarioColKey = addColumnDetails("comentario", "comentario", objectSchemaInfo);
            this.intentosColKey = addColumnDetails("intentos", "intentos", objectSchemaInfo);
            this.fechaHoraSateliteColKey = addColumnDetails("fechaHoraSatelite", "fechaHoraSatelite", objectSchemaInfo);
            this.altitudSateliteColKey = addColumnDetails("altitudSatelite", "altitudSatelite", objectSchemaInfo);
            this.distanciaColKey = addColumnDetails("distancia", "distancia", objectSchemaInfo);
            this.lecturaModificada1ColKey = addColumnDetails("lecturaModificada1", "lecturaModificada1", objectSchemaInfo);
            this.lecturaModificada2ColKey = addColumnDetails("lecturaModificada2", "lecturaModificada2", objectSchemaInfo);
            this.tiempoColKey = addColumnDetails("tiempo", "tiempo", objectSchemaInfo);
            this.consumoFacturadoColKey = addColumnDetails("consumoFacturado", "consumoFacturado", objectSchemaInfo);
            this.lecturaAnteriorColKey = addColumnDetails("lecturaAnterior", "lecturaAnterior", objectSchemaInfo);
            this.estratoColKey = addColumnDetails("estrato", "estrato", objectSchemaInfo);
            this.usoColKey = addColumnDetails("uso", "uso", objectSchemaInfo);
            this.nombreArchivoColKey = addColumnDetails("nombreArchivo", "nombreArchivo", objectSchemaInfo);
            this.idRegistroColKey = addColumnDetails("idRegistro", "idRegistro", objectSchemaInfo);
            this.totalCausasNoLecturaColKey = addColumnDetails("totalCausasNoLectura", "totalCausasNoLectura", objectSchemaInfo);
            this.totalLecturasColKey = addColumnDetails("totalLecturas", "totalLecturas", objectSchemaInfo);
            this.totalConsumoBajoColKey = addColumnDetails("totalConsumoBajo", "totalConsumoBajo", objectSchemaInfo);
            this.totalConsumoAltoColKey = addColumnDetails("totalConsumoAlto", "totalConsumoAlto", objectSchemaInfo);
            this.totalConsumoNormalColKey = addColumnDetails("totalConsumoNormal", "totalConsumoNormal", objectSchemaInfo);
            this.totalLecturasIgualesColKey = addColumnDetails("totalLecturasIguales", "totalLecturasIguales", objectSchemaInfo);
            this.totalLeidasColKey = addColumnDetails("totalLeidas", "totalLeidas", objectSchemaInfo);
            this.numObserColKey = addColumnDetails("numObser", "numObser", objectSchemaInfo);
            this.numInformesColKey = addColumnDetails("numInformes", "numInformes", objectSchemaInfo);
            this.totalTiempoPromColKey = addColumnDetails("totalTiempoProm", "totalTiempoProm", objectSchemaInfo);
            this.totalDistanciaPromColKey = addColumnDetails("totalDistanciaProm", "totalDistanciaProm", objectSchemaInfo);
            this.codigoSacColKey = addColumnDetails("codigoSac", "codigoSac", objectSchemaInfo);
            this.annoColKey = addColumnDetails("anno", "anno", objectSchemaInfo);
            this.mesColKey = addColumnDetails("mes", "mes", objectSchemaInfo);
            this.digitoChequeoColKey = addColumnDetails("digitoChequeo", "digitoChequeo", objectSchemaInfo);
            this.idContadorColKey = addColumnDetails("idContador", "idContador", objectSchemaInfo);
            this.descAnomaliaColKey = addColumnDetails("descAnomalia", "descAnomalia", objectSchemaInfo);
            this.descComentarioColKey = addColumnDetails("descComentario", "descComentario", objectSchemaInfo);
            this.estadoEnvioColKey = addColumnDetails("estadoEnvio", "estadoEnvio", objectSchemaInfo);
            this.fechaCreacionColKey = addColumnDetails("fechaCreacion", "fechaCreacion", objectSchemaInfo);
            this.fechaEnvioApiColKey = addColumnDetails("fechaEnvioApi", "fechaEnvioApi", objectSchemaInfo);
            this.intentosEnvioColKey = addColumnDetails("intentosEnvio", "intentosEnvio", objectSchemaInfo);
            this.errorEnvioColKey = addColumnDetails("errorEnvio", "errorEnvio", objectSchemaInfo);
        }

        EnvioLecturaColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new EnvioLecturaColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final EnvioLecturaColumnInfo src = (EnvioLecturaColumnInfo) rawSrc;
            final EnvioLecturaColumnInfo dst = (EnvioLecturaColumnInfo) rawDst;
            dst.claveRutaColKey = src.claveRutaColKey;
            dst.moduloTrabajoColKey = src.moduloTrabajoColKey;
            dst.idRealmColKey = src.idRealmColKey;
            dst.cicloColKey = src.cicloColKey;
            dst.municipioColKey = src.municipioColKey;
            dst.seccionColKey = src.seccionColKey;
            dst.departamentoColKey = src.departamentoColKey;
            dst.cuentaColKey = src.cuentaColKey;
            dst.nroContadorColKey = src.nroContadorColKey;
            dst.marcaMedidorColKey = src.marcaMedidorColKey;
            dst.fichaCatastralColKey = src.fichaCatastralColKey;
            dst.fechaHoraLecturaColKey = src.fechaHoraLecturaColKey;
            dst.horaImpresionColKey = src.horaImpresionColKey;
            dst.lecturaTomadaColKey = src.lecturaTomadaColKey;
            dst.causaNoLecturaColKey = src.causaNoLecturaColKey;
            dst.codLectorColKey = src.codLectorColKey;
            dst.terminalColKey = src.terminalColKey;
            dst.periodoLecturaColKey = src.periodoLecturaColKey;
            dst.longitudColKey = src.longitudColKey;
            dst.latitudColKey = src.latitudColKey;
            dst.nroSatelitesColKey = src.nroSatelitesColKey;
            dst.informeColKey = src.informeColKey;
            dst.criticaPdaColKey = src.criticaPdaColKey;
            dst.estadoEnvioOriginalColKey = src.estadoEnvioOriginalColKey;
            dst.comentarioColKey = src.comentarioColKey;
            dst.intentosColKey = src.intentosColKey;
            dst.fechaHoraSateliteColKey = src.fechaHoraSateliteColKey;
            dst.altitudSateliteColKey = src.altitudSateliteColKey;
            dst.distanciaColKey = src.distanciaColKey;
            dst.lecturaModificada1ColKey = src.lecturaModificada1ColKey;
            dst.lecturaModificada2ColKey = src.lecturaModificada2ColKey;
            dst.tiempoColKey = src.tiempoColKey;
            dst.consumoFacturadoColKey = src.consumoFacturadoColKey;
            dst.lecturaAnteriorColKey = src.lecturaAnteriorColKey;
            dst.estratoColKey = src.estratoColKey;
            dst.usoColKey = src.usoColKey;
            dst.nombreArchivoColKey = src.nombreArchivoColKey;
            dst.idRegistroColKey = src.idRegistroColKey;
            dst.totalCausasNoLecturaColKey = src.totalCausasNoLecturaColKey;
            dst.totalLecturasColKey = src.totalLecturasColKey;
            dst.totalConsumoBajoColKey = src.totalConsumoBajoColKey;
            dst.totalConsumoAltoColKey = src.totalConsumoAltoColKey;
            dst.totalConsumoNormalColKey = src.totalConsumoNormalColKey;
            dst.totalLecturasIgualesColKey = src.totalLecturasIgualesColKey;
            dst.totalLeidasColKey = src.totalLeidasColKey;
            dst.numObserColKey = src.numObserColKey;
            dst.numInformesColKey = src.numInformesColKey;
            dst.totalTiempoPromColKey = src.totalTiempoPromColKey;
            dst.totalDistanciaPromColKey = src.totalDistanciaPromColKey;
            dst.codigoSacColKey = src.codigoSacColKey;
            dst.annoColKey = src.annoColKey;
            dst.mesColKey = src.mesColKey;
            dst.digitoChequeoColKey = src.digitoChequeoColKey;
            dst.idContadorColKey = src.idContadorColKey;
            dst.descAnomaliaColKey = src.descAnomaliaColKey;
            dst.descComentarioColKey = src.descComentarioColKey;
            dst.estadoEnvioColKey = src.estadoEnvioColKey;
            dst.fechaCreacionColKey = src.fechaCreacionColKey;
            dst.fechaEnvioApiColKey = src.fechaEnvioApiColKey;
            dst.intentosEnvioColKey = src.intentosEnvioColKey;
            dst.errorEnvioColKey = src.errorEnvioColKey;
        }
    }

    private static final String NO_ALIAS = "";
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private EnvioLecturaColumnInfo columnInfo;
    private ProxyState<com.gstolima.comunicaciones.EnvioLectura> proxyState;

    com_gstolima_comunicaciones_EnvioLecturaRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (EnvioLecturaColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.gstolima.comunicaciones.EnvioLectura>(this);
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
    public long realmGet$idRealm() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.idRealmColKey);
    }

    @Override
    public void realmSet$idRealm(long value) {
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
    public String realmGet$municipio() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.municipioColKey);
    }

    @Override
    public void realmSet$municipio(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.municipioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.municipioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.municipioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.municipioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$seccion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.seccionColKey);
    }

    @Override
    public void realmSet$seccion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.seccionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.seccionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.seccionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.seccionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$departamento() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.departamentoColKey);
    }

    @Override
    public void realmSet$departamento(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.departamentoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.departamentoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.departamentoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.departamentoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cuenta() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cuentaColKey);
    }

    @Override
    public void realmSet$cuenta(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cuentaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.cuentaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cuentaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cuentaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$nroContador() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nroContadorColKey);
    }

    @Override
    public void realmSet$nroContador(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nroContadorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.nroContadorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nroContadorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nroContadorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$marcaMedidor() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.marcaMedidorColKey);
    }

    @Override
    public void realmSet$marcaMedidor(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.marcaMedidorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.marcaMedidorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.marcaMedidorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.marcaMedidorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fichaCatastral() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fichaCatastralColKey);
    }

    @Override
    public void realmSet$fichaCatastral(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fichaCatastralColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fichaCatastralColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fichaCatastralColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fichaCatastralColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaHoraLectura() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaHoraLecturaColKey);
    }

    @Override
    public void realmSet$fechaHoraLectura(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaHoraLecturaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaHoraLecturaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaHoraLecturaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaHoraLecturaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$horaImpresion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.horaImpresionColKey);
    }

    @Override
    public void realmSet$horaImpresion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.horaImpresionColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.horaImpresionColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.horaImpresionColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.horaImpresionColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lecturaTomada() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lecturaTomadaColKey);
    }

    @Override
    public void realmSet$lecturaTomada(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lecturaTomadaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lecturaTomadaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lecturaTomadaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lecturaTomadaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$causaNoLectura() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.causaNoLecturaColKey);
    }

    @Override
    public void realmSet$causaNoLectura(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.causaNoLecturaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.causaNoLecturaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.causaNoLecturaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.causaNoLecturaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codLector() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codLectorColKey);
    }

    @Override
    public void realmSet$codLector(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codLectorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codLectorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codLectorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codLectorColKey, value);
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
    public String realmGet$periodoLectura() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.periodoLecturaColKey);
    }

    @Override
    public void realmSet$periodoLectura(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.periodoLecturaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.periodoLecturaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.periodoLecturaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.periodoLecturaColKey, value);
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
    public String realmGet$nroSatelites() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nroSatelitesColKey);
    }

    @Override
    public void realmSet$nroSatelites(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nroSatelitesColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.nroSatelitesColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nroSatelitesColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nroSatelitesColKey, value);
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
    public String realmGet$criticaPda() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.criticaPdaColKey);
    }

    @Override
    public void realmSet$criticaPda(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.criticaPdaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.criticaPdaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.criticaPdaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.criticaPdaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$estadoEnvioOriginal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.estadoEnvioOriginalColKey);
    }

    @Override
    public void realmSet$estadoEnvioOriginal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.estadoEnvioOriginalColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.estadoEnvioOriginalColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.estadoEnvioOriginalColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.estadoEnvioOriginalColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$comentario() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.comentarioColKey);
    }

    @Override
    public void realmSet$comentario(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.comentarioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.comentarioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.comentarioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.comentarioColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$intentos() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.intentosColKey);
    }

    @Override
    public void realmSet$intentos(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.intentosColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.intentosColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.intentosColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.intentosColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fechaHoraSatelite() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaHoraSateliteColKey);
    }

    @Override
    public void realmSet$fechaHoraSatelite(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaHoraSateliteColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaHoraSateliteColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaHoraSateliteColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaHoraSateliteColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$altitudSatelite() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.altitudSateliteColKey);
    }

    @Override
    public void realmSet$altitudSatelite(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.altitudSateliteColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.altitudSateliteColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.altitudSateliteColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.altitudSateliteColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$distancia() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.distanciaColKey);
    }

    @Override
    public void realmSet$distancia(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.distanciaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.distanciaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.distanciaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.distanciaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lecturaModificada1() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lecturaModificada1ColKey);
    }

    @Override
    public void realmSet$lecturaModificada1(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lecturaModificada1ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lecturaModificada1ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lecturaModificada1ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lecturaModificada1ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lecturaModificada2() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lecturaModificada2ColKey);
    }

    @Override
    public void realmSet$lecturaModificada2(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lecturaModificada2ColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lecturaModificada2ColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lecturaModificada2ColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lecturaModificada2ColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$tiempo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.tiempoColKey);
    }

    @Override
    public void realmSet$tiempo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.tiempoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.tiempoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.tiempoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.tiempoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$consumoFacturado() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.consumoFacturadoColKey);
    }

    @Override
    public void realmSet$consumoFacturado(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.consumoFacturadoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.consumoFacturadoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.consumoFacturadoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.consumoFacturadoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$lecturaAnterior() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.lecturaAnteriorColKey);
    }

    @Override
    public void realmSet$lecturaAnterior(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.lecturaAnteriorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.lecturaAnteriorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.lecturaAnteriorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.lecturaAnteriorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$estrato() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.estratoColKey);
    }

    @Override
    public void realmSet$estrato(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.estratoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.estratoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.estratoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.estratoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$uso() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.usoColKey);
    }

    @Override
    public void realmSet$uso(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.usoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.usoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.usoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.usoColKey, value);
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
    public String realmGet$totalCausasNoLectura() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalCausasNoLecturaColKey);
    }

    @Override
    public void realmSet$totalCausasNoLectura(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalCausasNoLecturaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalCausasNoLecturaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalCausasNoLecturaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalCausasNoLecturaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalLecturas() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalLecturasColKey);
    }

    @Override
    public void realmSet$totalLecturas(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalLecturasColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalLecturasColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalLecturasColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalLecturasColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalConsumoBajo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalConsumoBajoColKey);
    }

    @Override
    public void realmSet$totalConsumoBajo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalConsumoBajoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalConsumoBajoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalConsumoBajoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalConsumoBajoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalConsumoAlto() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalConsumoAltoColKey);
    }

    @Override
    public void realmSet$totalConsumoAlto(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalConsumoAltoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalConsumoAltoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalConsumoAltoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalConsumoAltoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalConsumoNormal() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalConsumoNormalColKey);
    }

    @Override
    public void realmSet$totalConsumoNormal(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalConsumoNormalColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalConsumoNormalColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalConsumoNormalColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalConsumoNormalColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalLecturasIguales() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalLecturasIgualesColKey);
    }

    @Override
    public void realmSet$totalLecturasIguales(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalLecturasIgualesColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalLecturasIgualesColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalLecturasIgualesColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalLecturasIgualesColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalLeidas() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalLeidasColKey);
    }

    @Override
    public void realmSet$totalLeidas(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalLeidasColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalLeidasColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalLeidasColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalLeidasColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$numObser() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.numObserColKey);
    }

    @Override
    public void realmSet$numObser(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.numObserColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.numObserColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.numObserColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.numObserColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$numInformes() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.numInformesColKey);
    }

    @Override
    public void realmSet$numInformes(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.numInformesColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.numInformesColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.numInformesColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.numInformesColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalTiempoProm() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalTiempoPromColKey);
    }

    @Override
    public void realmSet$totalTiempoProm(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalTiempoPromColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalTiempoPromColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalTiempoPromColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalTiempoPromColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$totalDistanciaProm() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.totalDistanciaPromColKey);
    }

    @Override
    public void realmSet$totalDistanciaProm(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.totalDistanciaPromColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.totalDistanciaPromColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.totalDistanciaPromColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.totalDistanciaPromColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$codigoSac() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.codigoSacColKey);
    }

    @Override
    public void realmSet$codigoSac(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.codigoSacColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.codigoSacColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.codigoSacColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.codigoSacColKey, value);
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
    public String realmGet$digitoChequeo() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.digitoChequeoColKey);
    }

    @Override
    public void realmSet$digitoChequeo(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.digitoChequeoColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.digitoChequeoColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.digitoChequeoColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.digitoChequeoColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$idContador() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idContadorColKey);
    }

    @Override
    public void realmSet$idContador(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.idContadorColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.idContadorColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.idContadorColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.idContadorColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$descAnomalia() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descAnomaliaColKey);
    }

    @Override
    public void realmSet$descAnomalia(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descAnomaliaColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.descAnomaliaColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descAnomaliaColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descAnomaliaColKey, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$descComentario() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descComentarioColKey);
    }

    @Override
    public void realmSet$descComentario(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descComentarioColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.descComentarioColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descComentarioColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descComentarioColKey, value);
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
    public String realmGet$fechaEnvioApi() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fechaEnvioApiColKey);
    }

    @Override
    public void realmSet$fechaEnvioApi(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fechaEnvioApiColKey, row.getObjectKey(), true);
                return;
            }
            row.getTable().setString(columnInfo.fechaEnvioApiColKey, row.getObjectKey(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fechaEnvioApiColKey);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fechaEnvioApiColKey, value);
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

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder(NO_ALIAS, "EnvioLectura", false, 61, 0);
        builder.addPersistedProperty(NO_ALIAS, "claveRuta", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "moduloTrabajo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "idRealm", RealmFieldType.INTEGER, Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "ciclo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "municipio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "seccion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "departamento", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "cuenta", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nroContador", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "marcaMedidor", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fichaCatastral", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaHoraLectura", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "horaImpresion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lecturaTomada", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "causaNoLectura", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codLector", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "terminal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "periodoLectura", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "longitud", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "latitud", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nroSatelites", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "informe", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "criticaPda", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "estadoEnvioOriginal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "comentario", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "intentos", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaHoraSatelite", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "altitudSatelite", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "distancia", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lecturaModificada1", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lecturaModificada2", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "tiempo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "consumoFacturado", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "lecturaAnterior", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "estrato", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "uso", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "nombreArchivo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "idRegistro", RealmFieldType.STRING, !Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalCausasNoLectura", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalLecturas", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalConsumoBajo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalConsumoAlto", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalConsumoNormal", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalLecturasIguales", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalLeidas", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "numObser", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "numInformes", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalTiempoProm", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "totalDistanciaProm", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "codigoSac", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "anno", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "mes", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "digitoChequeo", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "idContador", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "descAnomalia", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "descComentario", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "estadoEnvio", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaCreacion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "fechaEnvioApi", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "intentosEnvio", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty(NO_ALIAS, "errorEnvio", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static EnvioLecturaColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new EnvioLecturaColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "EnvioLectura";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "EnvioLectura";
    }

    @SuppressWarnings("cast")
    public static com.gstolima.comunicaciones.EnvioLectura createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.gstolima.comunicaciones.EnvioLectura obj = null;
        if (update) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
            EnvioLecturaColumnInfo columnInfo = (EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
            long pkColumnKey = columnInfo.idRealmColKey;
            long objKey = Table.NO_MATCH;
            if (!json.isNull("idRealm")) {
                objKey = table.findFirstLong(pkColumnKey, json.getLong("idRealm"));
            }
            if (objKey != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("idRealm")) {
                if (json.isNull("idRealm")) {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioLectura.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy) realm.createObjectInternal(com.gstolima.comunicaciones.EnvioLectura.class, json.getLong("idRealm"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'idRealm'.");
            }
        }

        final com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) obj;
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
        if (json.has("municipio")) {
            if (json.isNull("municipio")) {
                objProxy.realmSet$municipio(null);
            } else {
                objProxy.realmSet$municipio((String) json.getString("municipio"));
            }
        }
        if (json.has("seccion")) {
            if (json.isNull("seccion")) {
                objProxy.realmSet$seccion(null);
            } else {
                objProxy.realmSet$seccion((String) json.getString("seccion"));
            }
        }
        if (json.has("departamento")) {
            if (json.isNull("departamento")) {
                objProxy.realmSet$departamento(null);
            } else {
                objProxy.realmSet$departamento((String) json.getString("departamento"));
            }
        }
        if (json.has("cuenta")) {
            if (json.isNull("cuenta")) {
                objProxy.realmSet$cuenta(null);
            } else {
                objProxy.realmSet$cuenta((String) json.getString("cuenta"));
            }
        }
        if (json.has("nroContador")) {
            if (json.isNull("nroContador")) {
                objProxy.realmSet$nroContador(null);
            } else {
                objProxy.realmSet$nroContador((String) json.getString("nroContador"));
            }
        }
        if (json.has("marcaMedidor")) {
            if (json.isNull("marcaMedidor")) {
                objProxy.realmSet$marcaMedidor(null);
            } else {
                objProxy.realmSet$marcaMedidor((String) json.getString("marcaMedidor"));
            }
        }
        if (json.has("fichaCatastral")) {
            if (json.isNull("fichaCatastral")) {
                objProxy.realmSet$fichaCatastral(null);
            } else {
                objProxy.realmSet$fichaCatastral((String) json.getString("fichaCatastral"));
            }
        }
        if (json.has("fechaHoraLectura")) {
            if (json.isNull("fechaHoraLectura")) {
                objProxy.realmSet$fechaHoraLectura(null);
            } else {
                objProxy.realmSet$fechaHoraLectura((String) json.getString("fechaHoraLectura"));
            }
        }
        if (json.has("horaImpresion")) {
            if (json.isNull("horaImpresion")) {
                objProxy.realmSet$horaImpresion(null);
            } else {
                objProxy.realmSet$horaImpresion((String) json.getString("horaImpresion"));
            }
        }
        if (json.has("lecturaTomada")) {
            if (json.isNull("lecturaTomada")) {
                objProxy.realmSet$lecturaTomada(null);
            } else {
                objProxy.realmSet$lecturaTomada((String) json.getString("lecturaTomada"));
            }
        }
        if (json.has("causaNoLectura")) {
            if (json.isNull("causaNoLectura")) {
                objProxy.realmSet$causaNoLectura(null);
            } else {
                objProxy.realmSet$causaNoLectura((String) json.getString("causaNoLectura"));
            }
        }
        if (json.has("codLector")) {
            if (json.isNull("codLector")) {
                objProxy.realmSet$codLector(null);
            } else {
                objProxy.realmSet$codLector((String) json.getString("codLector"));
            }
        }
        if (json.has("terminal")) {
            if (json.isNull("terminal")) {
                objProxy.realmSet$terminal(null);
            } else {
                objProxy.realmSet$terminal((String) json.getString("terminal"));
            }
        }
        if (json.has("periodoLectura")) {
            if (json.isNull("periodoLectura")) {
                objProxy.realmSet$periodoLectura(null);
            } else {
                objProxy.realmSet$periodoLectura((String) json.getString("periodoLectura"));
            }
        }
        if (json.has("longitud")) {
            if (json.isNull("longitud")) {
                objProxy.realmSet$longitud(null);
            } else {
                objProxy.realmSet$longitud((String) json.getString("longitud"));
            }
        }
        if (json.has("latitud")) {
            if (json.isNull("latitud")) {
                objProxy.realmSet$latitud(null);
            } else {
                objProxy.realmSet$latitud((String) json.getString("latitud"));
            }
        }
        if (json.has("nroSatelites")) {
            if (json.isNull("nroSatelites")) {
                objProxy.realmSet$nroSatelites(null);
            } else {
                objProxy.realmSet$nroSatelites((String) json.getString("nroSatelites"));
            }
        }
        if (json.has("informe")) {
            if (json.isNull("informe")) {
                objProxy.realmSet$informe(null);
            } else {
                objProxy.realmSet$informe((String) json.getString("informe"));
            }
        }
        if (json.has("criticaPda")) {
            if (json.isNull("criticaPda")) {
                objProxy.realmSet$criticaPda(null);
            } else {
                objProxy.realmSet$criticaPda((String) json.getString("criticaPda"));
            }
        }
        if (json.has("estadoEnvioOriginal")) {
            if (json.isNull("estadoEnvioOriginal")) {
                objProxy.realmSet$estadoEnvioOriginal(null);
            } else {
                objProxy.realmSet$estadoEnvioOriginal((String) json.getString("estadoEnvioOriginal"));
            }
        }
        if (json.has("comentario")) {
            if (json.isNull("comentario")) {
                objProxy.realmSet$comentario(null);
            } else {
                objProxy.realmSet$comentario((String) json.getString("comentario"));
            }
        }
        if (json.has("intentos")) {
            if (json.isNull("intentos")) {
                objProxy.realmSet$intentos(null);
            } else {
                objProxy.realmSet$intentos((String) json.getString("intentos"));
            }
        }
        if (json.has("fechaHoraSatelite")) {
            if (json.isNull("fechaHoraSatelite")) {
                objProxy.realmSet$fechaHoraSatelite(null);
            } else {
                objProxy.realmSet$fechaHoraSatelite((String) json.getString("fechaHoraSatelite"));
            }
        }
        if (json.has("altitudSatelite")) {
            if (json.isNull("altitudSatelite")) {
                objProxy.realmSet$altitudSatelite(null);
            } else {
                objProxy.realmSet$altitudSatelite((String) json.getString("altitudSatelite"));
            }
        }
        if (json.has("distancia")) {
            if (json.isNull("distancia")) {
                objProxy.realmSet$distancia(null);
            } else {
                objProxy.realmSet$distancia((String) json.getString("distancia"));
            }
        }
        if (json.has("lecturaModificada1")) {
            if (json.isNull("lecturaModificada1")) {
                objProxy.realmSet$lecturaModificada1(null);
            } else {
                objProxy.realmSet$lecturaModificada1((String) json.getString("lecturaModificada1"));
            }
        }
        if (json.has("lecturaModificada2")) {
            if (json.isNull("lecturaModificada2")) {
                objProxy.realmSet$lecturaModificada2(null);
            } else {
                objProxy.realmSet$lecturaModificada2((String) json.getString("lecturaModificada2"));
            }
        }
        if (json.has("tiempo")) {
            if (json.isNull("tiempo")) {
                objProxy.realmSet$tiempo(null);
            } else {
                objProxy.realmSet$tiempo((String) json.getString("tiempo"));
            }
        }
        if (json.has("consumoFacturado")) {
            if (json.isNull("consumoFacturado")) {
                objProxy.realmSet$consumoFacturado(null);
            } else {
                objProxy.realmSet$consumoFacturado((String) json.getString("consumoFacturado"));
            }
        }
        if (json.has("lecturaAnterior")) {
            if (json.isNull("lecturaAnterior")) {
                objProxy.realmSet$lecturaAnterior(null);
            } else {
                objProxy.realmSet$lecturaAnterior((String) json.getString("lecturaAnterior"));
            }
        }
        if (json.has("estrato")) {
            if (json.isNull("estrato")) {
                objProxy.realmSet$estrato(null);
            } else {
                objProxy.realmSet$estrato((String) json.getString("estrato"));
            }
        }
        if (json.has("uso")) {
            if (json.isNull("uso")) {
                objProxy.realmSet$uso(null);
            } else {
                objProxy.realmSet$uso((String) json.getString("uso"));
            }
        }
        if (json.has("nombreArchivo")) {
            if (json.isNull("nombreArchivo")) {
                objProxy.realmSet$nombreArchivo(null);
            } else {
                objProxy.realmSet$nombreArchivo((String) json.getString("nombreArchivo"));
            }
        }
        if (json.has("idRegistro")) {
            if (json.isNull("idRegistro")) {
                objProxy.realmSet$idRegistro(null);
            } else {
                objProxy.realmSet$idRegistro((String) json.getString("idRegistro"));
            }
        }
        if (json.has("totalCausasNoLectura")) {
            if (json.isNull("totalCausasNoLectura")) {
                objProxy.realmSet$totalCausasNoLectura(null);
            } else {
                objProxy.realmSet$totalCausasNoLectura((String) json.getString("totalCausasNoLectura"));
            }
        }
        if (json.has("totalLecturas")) {
            if (json.isNull("totalLecturas")) {
                objProxy.realmSet$totalLecturas(null);
            } else {
                objProxy.realmSet$totalLecturas((String) json.getString("totalLecturas"));
            }
        }
        if (json.has("totalConsumoBajo")) {
            if (json.isNull("totalConsumoBajo")) {
                objProxy.realmSet$totalConsumoBajo(null);
            } else {
                objProxy.realmSet$totalConsumoBajo((String) json.getString("totalConsumoBajo"));
            }
        }
        if (json.has("totalConsumoAlto")) {
            if (json.isNull("totalConsumoAlto")) {
                objProxy.realmSet$totalConsumoAlto(null);
            } else {
                objProxy.realmSet$totalConsumoAlto((String) json.getString("totalConsumoAlto"));
            }
        }
        if (json.has("totalConsumoNormal")) {
            if (json.isNull("totalConsumoNormal")) {
                objProxy.realmSet$totalConsumoNormal(null);
            } else {
                objProxy.realmSet$totalConsumoNormal((String) json.getString("totalConsumoNormal"));
            }
        }
        if (json.has("totalLecturasIguales")) {
            if (json.isNull("totalLecturasIguales")) {
                objProxy.realmSet$totalLecturasIguales(null);
            } else {
                objProxy.realmSet$totalLecturasIguales((String) json.getString("totalLecturasIguales"));
            }
        }
        if (json.has("totalLeidas")) {
            if (json.isNull("totalLeidas")) {
                objProxy.realmSet$totalLeidas(null);
            } else {
                objProxy.realmSet$totalLeidas((String) json.getString("totalLeidas"));
            }
        }
        if (json.has("numObser")) {
            if (json.isNull("numObser")) {
                objProxy.realmSet$numObser(null);
            } else {
                objProxy.realmSet$numObser((String) json.getString("numObser"));
            }
        }
        if (json.has("numInformes")) {
            if (json.isNull("numInformes")) {
                objProxy.realmSet$numInformes(null);
            } else {
                objProxy.realmSet$numInformes((String) json.getString("numInformes"));
            }
        }
        if (json.has("totalTiempoProm")) {
            if (json.isNull("totalTiempoProm")) {
                objProxy.realmSet$totalTiempoProm(null);
            } else {
                objProxy.realmSet$totalTiempoProm((String) json.getString("totalTiempoProm"));
            }
        }
        if (json.has("totalDistanciaProm")) {
            if (json.isNull("totalDistanciaProm")) {
                objProxy.realmSet$totalDistanciaProm(null);
            } else {
                objProxy.realmSet$totalDistanciaProm((String) json.getString("totalDistanciaProm"));
            }
        }
        if (json.has("codigoSac")) {
            if (json.isNull("codigoSac")) {
                objProxy.realmSet$codigoSac(null);
            } else {
                objProxy.realmSet$codigoSac((String) json.getString("codigoSac"));
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
        if (json.has("digitoChequeo")) {
            if (json.isNull("digitoChequeo")) {
                objProxy.realmSet$digitoChequeo(null);
            } else {
                objProxy.realmSet$digitoChequeo((String) json.getString("digitoChequeo"));
            }
        }
        if (json.has("idContador")) {
            if (json.isNull("idContador")) {
                objProxy.realmSet$idContador(null);
            } else {
                objProxy.realmSet$idContador((String) json.getString("idContador"));
            }
        }
        if (json.has("descAnomalia")) {
            if (json.isNull("descAnomalia")) {
                objProxy.realmSet$descAnomalia(null);
            } else {
                objProxy.realmSet$descAnomalia((String) json.getString("descAnomalia"));
            }
        }
        if (json.has("descComentario")) {
            if (json.isNull("descComentario")) {
                objProxy.realmSet$descComentario(null);
            } else {
                objProxy.realmSet$descComentario((String) json.getString("descComentario"));
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
        if (json.has("fechaEnvioApi")) {
            if (json.isNull("fechaEnvioApi")) {
                objProxy.realmSet$fechaEnvioApi(null);
            } else {
                objProxy.realmSet$fechaEnvioApi((String) json.getString("fechaEnvioApi"));
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
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.gstolima.comunicaciones.EnvioLectura createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.gstolima.comunicaciones.EnvioLectura obj = new com.gstolima.comunicaciones.EnvioLectura();
        final com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface objProxy = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) obj;
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
                    objProxy.realmSet$idRealm((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'idRealm' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("ciclo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ciclo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ciclo(null);
                }
            } else if (name.equals("municipio")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$municipio((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$municipio(null);
                }
            } else if (name.equals("seccion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$seccion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$seccion(null);
                }
            } else if (name.equals("departamento")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$departamento((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$departamento(null);
                }
            } else if (name.equals("cuenta")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cuenta((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cuenta(null);
                }
            } else if (name.equals("nroContador")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nroContador((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nroContador(null);
                }
            } else if (name.equals("marcaMedidor")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$marcaMedidor((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$marcaMedidor(null);
                }
            } else if (name.equals("fichaCatastral")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fichaCatastral((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fichaCatastral(null);
                }
            } else if (name.equals("fechaHoraLectura")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaHoraLectura((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaHoraLectura(null);
                }
            } else if (name.equals("horaImpresion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$horaImpresion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$horaImpresion(null);
                }
            } else if (name.equals("lecturaTomada")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lecturaTomada((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lecturaTomada(null);
                }
            } else if (name.equals("causaNoLectura")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$causaNoLectura((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$causaNoLectura(null);
                }
            } else if (name.equals("codLector")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codLector((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codLector(null);
                }
            } else if (name.equals("terminal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$terminal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$terminal(null);
                }
            } else if (name.equals("periodoLectura")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$periodoLectura((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$periodoLectura(null);
                }
            } else if (name.equals("longitud")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$longitud((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$longitud(null);
                }
            } else if (name.equals("latitud")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$latitud((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$latitud(null);
                }
            } else if (name.equals("nroSatelites")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nroSatelites((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nroSatelites(null);
                }
            } else if (name.equals("informe")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$informe((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$informe(null);
                }
            } else if (name.equals("criticaPda")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$criticaPda((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$criticaPda(null);
                }
            } else if (name.equals("estadoEnvioOriginal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$estadoEnvioOriginal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$estadoEnvioOriginal(null);
                }
            } else if (name.equals("comentario")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$comentario((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$comentario(null);
                }
            } else if (name.equals("intentos")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$intentos((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$intentos(null);
                }
            } else if (name.equals("fechaHoraSatelite")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaHoraSatelite((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaHoraSatelite(null);
                }
            } else if (name.equals("altitudSatelite")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$altitudSatelite((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$altitudSatelite(null);
                }
            } else if (name.equals("distancia")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$distancia((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$distancia(null);
                }
            } else if (name.equals("lecturaModificada1")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lecturaModificada1((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lecturaModificada1(null);
                }
            } else if (name.equals("lecturaModificada2")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lecturaModificada2((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lecturaModificada2(null);
                }
            } else if (name.equals("tiempo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$tiempo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$tiempo(null);
                }
            } else if (name.equals("consumoFacturado")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$consumoFacturado((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$consumoFacturado(null);
                }
            } else if (name.equals("lecturaAnterior")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lecturaAnterior((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$lecturaAnterior(null);
                }
            } else if (name.equals("estrato")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$estrato((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$estrato(null);
                }
            } else if (name.equals("uso")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$uso((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$uso(null);
                }
            } else if (name.equals("nombreArchivo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$nombreArchivo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$nombreArchivo(null);
                }
            } else if (name.equals("idRegistro")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$idRegistro((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$idRegistro(null);
                }
            } else if (name.equals("totalCausasNoLectura")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalCausasNoLectura((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalCausasNoLectura(null);
                }
            } else if (name.equals("totalLecturas")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalLecturas((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalLecturas(null);
                }
            } else if (name.equals("totalConsumoBajo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalConsumoBajo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalConsumoBajo(null);
                }
            } else if (name.equals("totalConsumoAlto")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalConsumoAlto((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalConsumoAlto(null);
                }
            } else if (name.equals("totalConsumoNormal")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalConsumoNormal((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalConsumoNormal(null);
                }
            } else if (name.equals("totalLecturasIguales")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalLecturasIguales((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalLecturasIguales(null);
                }
            } else if (name.equals("totalLeidas")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalLeidas((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalLeidas(null);
                }
            } else if (name.equals("numObser")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$numObser((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$numObser(null);
                }
            } else if (name.equals("numInformes")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$numInformes((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$numInformes(null);
                }
            } else if (name.equals("totalTiempoProm")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalTiempoProm((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalTiempoProm(null);
                }
            } else if (name.equals("totalDistanciaProm")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$totalDistanciaProm((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$totalDistanciaProm(null);
                }
            } else if (name.equals("codigoSac")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$codigoSac((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$codigoSac(null);
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
            } else if (name.equals("digitoChequeo")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$digitoChequeo((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$digitoChequeo(null);
                }
            } else if (name.equals("idContador")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$idContador((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$idContador(null);
                }
            } else if (name.equals("descAnomalia")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$descAnomalia((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$descAnomalia(null);
                }
            } else if (name.equals("descComentario")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$descComentario((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$descComentario(null);
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
            } else if (name.equals("fechaEnvioApi")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fechaEnvioApi((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fechaEnvioApi(null);
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

    static com_gstolima_comunicaciones_EnvioLecturaRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class), false, Collections.<String>emptyList());
        io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy obj = new io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.gstolima.comunicaciones.EnvioLectura copyOrUpdate(Realm realm, EnvioLecturaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioLectura object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.gstolima.comunicaciones.EnvioLectura) cachedRealmObject;
        }

        com.gstolima.comunicaciones.EnvioLectura realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
            long pkColumnKey = columnInfo.idRealmColKey;
            long objKey = table.findFirstLong(pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
            if (objKey == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(objKey), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.gstolima.comunicaciones.EnvioLectura copy(Realm realm, EnvioLecturaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioLectura newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.gstolima.comunicaciones.EnvioLectura) cachedRealmObject;
        }

        com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface unmanagedSource = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) newObject;

        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.claveRutaColKey, unmanagedSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, unmanagedSource.realmGet$moduloTrabajo());
        builder.addInteger(columnInfo.idRealmColKey, unmanagedSource.realmGet$idRealm());
        builder.addString(columnInfo.cicloColKey, unmanagedSource.realmGet$ciclo());
        builder.addString(columnInfo.municipioColKey, unmanagedSource.realmGet$municipio());
        builder.addString(columnInfo.seccionColKey, unmanagedSource.realmGet$seccion());
        builder.addString(columnInfo.departamentoColKey, unmanagedSource.realmGet$departamento());
        builder.addString(columnInfo.cuentaColKey, unmanagedSource.realmGet$cuenta());
        builder.addString(columnInfo.nroContadorColKey, unmanagedSource.realmGet$nroContador());
        builder.addString(columnInfo.marcaMedidorColKey, unmanagedSource.realmGet$marcaMedidor());
        builder.addString(columnInfo.fichaCatastralColKey, unmanagedSource.realmGet$fichaCatastral());
        builder.addString(columnInfo.fechaHoraLecturaColKey, unmanagedSource.realmGet$fechaHoraLectura());
        builder.addString(columnInfo.horaImpresionColKey, unmanagedSource.realmGet$horaImpresion());
        builder.addString(columnInfo.lecturaTomadaColKey, unmanagedSource.realmGet$lecturaTomada());
        builder.addString(columnInfo.causaNoLecturaColKey, unmanagedSource.realmGet$causaNoLectura());
        builder.addString(columnInfo.codLectorColKey, unmanagedSource.realmGet$codLector());
        builder.addString(columnInfo.terminalColKey, unmanagedSource.realmGet$terminal());
        builder.addString(columnInfo.periodoLecturaColKey, unmanagedSource.realmGet$periodoLectura());
        builder.addString(columnInfo.longitudColKey, unmanagedSource.realmGet$longitud());
        builder.addString(columnInfo.latitudColKey, unmanagedSource.realmGet$latitud());
        builder.addString(columnInfo.nroSatelitesColKey, unmanagedSource.realmGet$nroSatelites());
        builder.addString(columnInfo.informeColKey, unmanagedSource.realmGet$informe());
        builder.addString(columnInfo.criticaPdaColKey, unmanagedSource.realmGet$criticaPda());
        builder.addString(columnInfo.estadoEnvioOriginalColKey, unmanagedSource.realmGet$estadoEnvioOriginal());
        builder.addString(columnInfo.comentarioColKey, unmanagedSource.realmGet$comentario());
        builder.addString(columnInfo.intentosColKey, unmanagedSource.realmGet$intentos());
        builder.addString(columnInfo.fechaHoraSateliteColKey, unmanagedSource.realmGet$fechaHoraSatelite());
        builder.addString(columnInfo.altitudSateliteColKey, unmanagedSource.realmGet$altitudSatelite());
        builder.addString(columnInfo.distanciaColKey, unmanagedSource.realmGet$distancia());
        builder.addString(columnInfo.lecturaModificada1ColKey, unmanagedSource.realmGet$lecturaModificada1());
        builder.addString(columnInfo.lecturaModificada2ColKey, unmanagedSource.realmGet$lecturaModificada2());
        builder.addString(columnInfo.tiempoColKey, unmanagedSource.realmGet$tiempo());
        builder.addString(columnInfo.consumoFacturadoColKey, unmanagedSource.realmGet$consumoFacturado());
        builder.addString(columnInfo.lecturaAnteriorColKey, unmanagedSource.realmGet$lecturaAnterior());
        builder.addString(columnInfo.estratoColKey, unmanagedSource.realmGet$estrato());
        builder.addString(columnInfo.usoColKey, unmanagedSource.realmGet$uso());
        builder.addString(columnInfo.nombreArchivoColKey, unmanagedSource.realmGet$nombreArchivo());
        builder.addString(columnInfo.idRegistroColKey, unmanagedSource.realmGet$idRegistro());
        builder.addString(columnInfo.totalCausasNoLecturaColKey, unmanagedSource.realmGet$totalCausasNoLectura());
        builder.addString(columnInfo.totalLecturasColKey, unmanagedSource.realmGet$totalLecturas());
        builder.addString(columnInfo.totalConsumoBajoColKey, unmanagedSource.realmGet$totalConsumoBajo());
        builder.addString(columnInfo.totalConsumoAltoColKey, unmanagedSource.realmGet$totalConsumoAlto());
        builder.addString(columnInfo.totalConsumoNormalColKey, unmanagedSource.realmGet$totalConsumoNormal());
        builder.addString(columnInfo.totalLecturasIgualesColKey, unmanagedSource.realmGet$totalLecturasIguales());
        builder.addString(columnInfo.totalLeidasColKey, unmanagedSource.realmGet$totalLeidas());
        builder.addString(columnInfo.numObserColKey, unmanagedSource.realmGet$numObser());
        builder.addString(columnInfo.numInformesColKey, unmanagedSource.realmGet$numInformes());
        builder.addString(columnInfo.totalTiempoPromColKey, unmanagedSource.realmGet$totalTiempoProm());
        builder.addString(columnInfo.totalDistanciaPromColKey, unmanagedSource.realmGet$totalDistanciaProm());
        builder.addString(columnInfo.codigoSacColKey, unmanagedSource.realmGet$codigoSac());
        builder.addString(columnInfo.annoColKey, unmanagedSource.realmGet$anno());
        builder.addString(columnInfo.mesColKey, unmanagedSource.realmGet$mes());
        builder.addString(columnInfo.digitoChequeoColKey, unmanagedSource.realmGet$digitoChequeo());
        builder.addString(columnInfo.idContadorColKey, unmanagedSource.realmGet$idContador());
        builder.addString(columnInfo.descAnomaliaColKey, unmanagedSource.realmGet$descAnomalia());
        builder.addString(columnInfo.descComentarioColKey, unmanagedSource.realmGet$descComentario());
        builder.addInteger(columnInfo.estadoEnvioColKey, unmanagedSource.realmGet$estadoEnvio());
        builder.addString(columnInfo.fechaCreacionColKey, unmanagedSource.realmGet$fechaCreacion());
        builder.addString(columnInfo.fechaEnvioApiColKey, unmanagedSource.realmGet$fechaEnvioApi());
        builder.addInteger(columnInfo.intentosEnvioColKey, unmanagedSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.errorEnvioColKey, unmanagedSource.realmGet$errorEnvio());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_gstolima_comunicaciones_EnvioLecturaRealmProxy managedCopy = newProxyInstance(realm, row);
        cache.put(newObject, managedCopy);

        return managedCopy;
    }

    public static long insert(Realm realm, com.gstolima.comunicaciones.EnvioLectura object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        long tableNativePtr = table.getNativePtr();
        EnvioLecturaColumnInfo columnInfo = (EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        }
        String realmGet$municipio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$municipio();
        if (realmGet$municipio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.municipioColKey, objKey, realmGet$municipio, false);
        }
        String realmGet$seccion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$seccion();
        if (realmGet$seccion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.seccionColKey, objKey, realmGet$seccion, false);
        }
        String realmGet$departamento = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$departamento();
        if (realmGet$departamento != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.departamentoColKey, objKey, realmGet$departamento, false);
        }
        String realmGet$cuenta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$cuenta();
        if (realmGet$cuenta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cuentaColKey, objKey, realmGet$cuenta, false);
        }
        String realmGet$nroContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroContador();
        if (realmGet$nroContador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nroContadorColKey, objKey, realmGet$nroContador, false);
        }
        String realmGet$marcaMedidor = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$marcaMedidor();
        if (realmGet$marcaMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, realmGet$marcaMedidor, false);
        }
        String realmGet$fichaCatastral = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fichaCatastral();
        if (realmGet$fichaCatastral != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, realmGet$fichaCatastral, false);
        }
        String realmGet$fechaHoraLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraLectura();
        if (realmGet$fechaHoraLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, realmGet$fechaHoraLectura, false);
        }
        String realmGet$horaImpresion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$horaImpresion();
        if (realmGet$horaImpresion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.horaImpresionColKey, objKey, realmGet$horaImpresion, false);
        }
        String realmGet$lecturaTomada = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaTomada();
        if (realmGet$lecturaTomada != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, realmGet$lecturaTomada, false);
        }
        String realmGet$causaNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$causaNoLectura();
        if (realmGet$causaNoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, realmGet$causaNoLectura, false);
        }
        String realmGet$codLector = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codLector();
        if (realmGet$codLector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codLectorColKey, objKey, realmGet$codLector, false);
        }
        String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$terminal();
        if (realmGet$terminal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
        }
        String realmGet$periodoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$periodoLectura();
        if (realmGet$periodoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, realmGet$periodoLectura, false);
        }
        String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$longitud();
        if (realmGet$longitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
        }
        String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$latitud();
        if (realmGet$latitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
        }
        String realmGet$nroSatelites = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroSatelites();
        if (realmGet$nroSatelites != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, realmGet$nroSatelites, false);
        }
        String realmGet$informe = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$informe();
        if (realmGet$informe != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
        }
        String realmGet$criticaPda = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$criticaPda();
        if (realmGet$criticaPda != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.criticaPdaColKey, objKey, realmGet$criticaPda, false);
        }
        String realmGet$estadoEnvioOriginal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvioOriginal();
        if (realmGet$estadoEnvioOriginal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, realmGet$estadoEnvioOriginal, false);
        }
        String realmGet$comentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$comentario();
        if (realmGet$comentario != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.comentarioColKey, objKey, realmGet$comentario, false);
        }
        String realmGet$intentos = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentos();
        if (realmGet$intentos != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.intentosColKey, objKey, realmGet$intentos, false);
        }
        String realmGet$fechaHoraSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraSatelite();
        if (realmGet$fechaHoraSatelite != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, realmGet$fechaHoraSatelite, false);
        }
        String realmGet$altitudSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$altitudSatelite();
        if (realmGet$altitudSatelite != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, realmGet$altitudSatelite, false);
        }
        String realmGet$distancia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$distancia();
        if (realmGet$distancia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.distanciaColKey, objKey, realmGet$distancia, false);
        }
        String realmGet$lecturaModificada1 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada1();
        if (realmGet$lecturaModificada1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, realmGet$lecturaModificada1, false);
        }
        String realmGet$lecturaModificada2 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada2();
        if (realmGet$lecturaModificada2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, realmGet$lecturaModificada2, false);
        }
        String realmGet$tiempo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$tiempo();
        if (realmGet$tiempo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tiempoColKey, objKey, realmGet$tiempo, false);
        }
        String realmGet$consumoFacturado = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$consumoFacturado();
        if (realmGet$consumoFacturado != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, realmGet$consumoFacturado, false);
        }
        String realmGet$lecturaAnterior = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaAnterior();
        if (realmGet$lecturaAnterior != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, realmGet$lecturaAnterior, false);
        }
        String realmGet$estrato = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estrato();
        if (realmGet$estrato != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estratoColKey, objKey, realmGet$estrato, false);
        }
        String realmGet$uso = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$uso();
        if (realmGet$uso != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.usoColKey, objKey, realmGet$uso, false);
        }
        String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nombreArchivo();
        if (realmGet$nombreArchivo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
        }
        String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRegistro();
        if (realmGet$idRegistro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
        }
        String realmGet$totalCausasNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalCausasNoLectura();
        if (realmGet$totalCausasNoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, realmGet$totalCausasNoLectura, false);
        }
        String realmGet$totalLecturas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturas();
        if (realmGet$totalLecturas != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasColKey, objKey, realmGet$totalLecturas, false);
        }
        String realmGet$totalConsumoBajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoBajo();
        if (realmGet$totalConsumoBajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, realmGet$totalConsumoBajo, false);
        }
        String realmGet$totalConsumoAlto = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoAlto();
        if (realmGet$totalConsumoAlto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, realmGet$totalConsumoAlto, false);
        }
        String realmGet$totalConsumoNormal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoNormal();
        if (realmGet$totalConsumoNormal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, realmGet$totalConsumoNormal, false);
        }
        String realmGet$totalLecturasIguales = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturasIguales();
        if (realmGet$totalLecturasIguales != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, realmGet$totalLecturasIguales, false);
        }
        String realmGet$totalLeidas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLeidas();
        if (realmGet$totalLeidas != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLeidasColKey, objKey, realmGet$totalLeidas, false);
        }
        String realmGet$numObser = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numObser();
        if (realmGet$numObser != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numObserColKey, objKey, realmGet$numObser, false);
        }
        String realmGet$numInformes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numInformes();
        if (realmGet$numInformes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numInformesColKey, objKey, realmGet$numInformes, false);
        }
        String realmGet$totalTiempoProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalTiempoProm();
        if (realmGet$totalTiempoProm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, realmGet$totalTiempoProm, false);
        }
        String realmGet$totalDistanciaProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalDistanciaProm();
        if (realmGet$totalDistanciaProm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, realmGet$totalDistanciaProm, false);
        }
        String realmGet$codigoSac = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codigoSac();
        if (realmGet$codigoSac != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codigoSacColKey, objKey, realmGet$codigoSac, false);
        }
        String realmGet$anno = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$anno();
        if (realmGet$anno != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
        }
        String realmGet$mes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$mes();
        if (realmGet$mes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
        }
        String realmGet$digitoChequeo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$digitoChequeo();
        if (realmGet$digitoChequeo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, realmGet$digitoChequeo, false);
        }
        String realmGet$idContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idContador();
        if (realmGet$idContador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idContadorColKey, objKey, realmGet$idContador, false);
        }
        String realmGet$descAnomalia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descAnomalia();
        if (realmGet$descAnomalia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, realmGet$descAnomalia, false);
        }
        String realmGet$descComentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descComentario();
        if (realmGet$descComentario != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descComentarioColKey, objKey, realmGet$descComentario, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvio(), false);
        String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
        }
        String realmGet$fechaEnvioApi = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaEnvioApi();
        if (realmGet$fechaEnvioApi != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, realmGet$fechaEnvioApi, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$errorEnvio();
        if (realmGet$errorEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
        }
        return objKey;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        long tableNativePtr = table.getNativePtr();
        EnvioLecturaColumnInfo columnInfo = (EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        com.gstolima.comunicaciones.EnvioLectura object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioLectura) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            }
            String realmGet$municipio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$municipio();
            if (realmGet$municipio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.municipioColKey, objKey, realmGet$municipio, false);
            }
            String realmGet$seccion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$seccion();
            if (realmGet$seccion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.seccionColKey, objKey, realmGet$seccion, false);
            }
            String realmGet$departamento = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$departamento();
            if (realmGet$departamento != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.departamentoColKey, objKey, realmGet$departamento, false);
            }
            String realmGet$cuenta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$cuenta();
            if (realmGet$cuenta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cuentaColKey, objKey, realmGet$cuenta, false);
            }
            String realmGet$nroContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroContador();
            if (realmGet$nroContador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nroContadorColKey, objKey, realmGet$nroContador, false);
            }
            String realmGet$marcaMedidor = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$marcaMedidor();
            if (realmGet$marcaMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, realmGet$marcaMedidor, false);
            }
            String realmGet$fichaCatastral = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fichaCatastral();
            if (realmGet$fichaCatastral != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, realmGet$fichaCatastral, false);
            }
            String realmGet$fechaHoraLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraLectura();
            if (realmGet$fechaHoraLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, realmGet$fechaHoraLectura, false);
            }
            String realmGet$horaImpresion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$horaImpresion();
            if (realmGet$horaImpresion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.horaImpresionColKey, objKey, realmGet$horaImpresion, false);
            }
            String realmGet$lecturaTomada = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaTomada();
            if (realmGet$lecturaTomada != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, realmGet$lecturaTomada, false);
            }
            String realmGet$causaNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$causaNoLectura();
            if (realmGet$causaNoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, realmGet$causaNoLectura, false);
            }
            String realmGet$codLector = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codLector();
            if (realmGet$codLector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codLectorColKey, objKey, realmGet$codLector, false);
            }
            String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$terminal();
            if (realmGet$terminal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
            }
            String realmGet$periodoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$periodoLectura();
            if (realmGet$periodoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, realmGet$periodoLectura, false);
            }
            String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$longitud();
            if (realmGet$longitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
            }
            String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$latitud();
            if (realmGet$latitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
            }
            String realmGet$nroSatelites = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroSatelites();
            if (realmGet$nroSatelites != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, realmGet$nroSatelites, false);
            }
            String realmGet$informe = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$informe();
            if (realmGet$informe != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
            }
            String realmGet$criticaPda = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$criticaPda();
            if (realmGet$criticaPda != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.criticaPdaColKey, objKey, realmGet$criticaPda, false);
            }
            String realmGet$estadoEnvioOriginal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvioOriginal();
            if (realmGet$estadoEnvioOriginal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, realmGet$estadoEnvioOriginal, false);
            }
            String realmGet$comentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$comentario();
            if (realmGet$comentario != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.comentarioColKey, objKey, realmGet$comentario, false);
            }
            String realmGet$intentos = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentos();
            if (realmGet$intentos != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.intentosColKey, objKey, realmGet$intentos, false);
            }
            String realmGet$fechaHoraSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraSatelite();
            if (realmGet$fechaHoraSatelite != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, realmGet$fechaHoraSatelite, false);
            }
            String realmGet$altitudSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$altitudSatelite();
            if (realmGet$altitudSatelite != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, realmGet$altitudSatelite, false);
            }
            String realmGet$distancia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$distancia();
            if (realmGet$distancia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.distanciaColKey, objKey, realmGet$distancia, false);
            }
            String realmGet$lecturaModificada1 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada1();
            if (realmGet$lecturaModificada1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, realmGet$lecturaModificada1, false);
            }
            String realmGet$lecturaModificada2 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada2();
            if (realmGet$lecturaModificada2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, realmGet$lecturaModificada2, false);
            }
            String realmGet$tiempo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$tiempo();
            if (realmGet$tiempo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tiempoColKey, objKey, realmGet$tiempo, false);
            }
            String realmGet$consumoFacturado = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$consumoFacturado();
            if (realmGet$consumoFacturado != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, realmGet$consumoFacturado, false);
            }
            String realmGet$lecturaAnterior = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaAnterior();
            if (realmGet$lecturaAnterior != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, realmGet$lecturaAnterior, false);
            }
            String realmGet$estrato = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estrato();
            if (realmGet$estrato != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estratoColKey, objKey, realmGet$estrato, false);
            }
            String realmGet$uso = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$uso();
            if (realmGet$uso != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.usoColKey, objKey, realmGet$uso, false);
            }
            String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nombreArchivo();
            if (realmGet$nombreArchivo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
            }
            String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRegistro();
            if (realmGet$idRegistro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
            }
            String realmGet$totalCausasNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalCausasNoLectura();
            if (realmGet$totalCausasNoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, realmGet$totalCausasNoLectura, false);
            }
            String realmGet$totalLecturas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturas();
            if (realmGet$totalLecturas != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasColKey, objKey, realmGet$totalLecturas, false);
            }
            String realmGet$totalConsumoBajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoBajo();
            if (realmGet$totalConsumoBajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, realmGet$totalConsumoBajo, false);
            }
            String realmGet$totalConsumoAlto = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoAlto();
            if (realmGet$totalConsumoAlto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, realmGet$totalConsumoAlto, false);
            }
            String realmGet$totalConsumoNormal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoNormal();
            if (realmGet$totalConsumoNormal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, realmGet$totalConsumoNormal, false);
            }
            String realmGet$totalLecturasIguales = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturasIguales();
            if (realmGet$totalLecturasIguales != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, realmGet$totalLecturasIguales, false);
            }
            String realmGet$totalLeidas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLeidas();
            if (realmGet$totalLeidas != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLeidasColKey, objKey, realmGet$totalLeidas, false);
            }
            String realmGet$numObser = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numObser();
            if (realmGet$numObser != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numObserColKey, objKey, realmGet$numObser, false);
            }
            String realmGet$numInformes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numInformes();
            if (realmGet$numInformes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numInformesColKey, objKey, realmGet$numInformes, false);
            }
            String realmGet$totalTiempoProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalTiempoProm();
            if (realmGet$totalTiempoProm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, realmGet$totalTiempoProm, false);
            }
            String realmGet$totalDistanciaProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalDistanciaProm();
            if (realmGet$totalDistanciaProm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, realmGet$totalDistanciaProm, false);
            }
            String realmGet$codigoSac = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codigoSac();
            if (realmGet$codigoSac != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codigoSacColKey, objKey, realmGet$codigoSac, false);
            }
            String realmGet$anno = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$anno();
            if (realmGet$anno != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
            }
            String realmGet$mes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$mes();
            if (realmGet$mes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
            }
            String realmGet$digitoChequeo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$digitoChequeo();
            if (realmGet$digitoChequeo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, realmGet$digitoChequeo, false);
            }
            String realmGet$idContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idContador();
            if (realmGet$idContador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idContadorColKey, objKey, realmGet$idContador, false);
            }
            String realmGet$descAnomalia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descAnomalia();
            if (realmGet$descAnomalia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, realmGet$descAnomalia, false);
            }
            String realmGet$descComentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descComentario();
            if (realmGet$descComentario != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descComentarioColKey, objKey, realmGet$descComentario, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvio(), false);
            String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
            }
            String realmGet$fechaEnvioApi = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaEnvioApi();
            if (realmGet$fechaEnvioApi != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, realmGet$fechaEnvioApi, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$errorEnvio();
            if (realmGet$errorEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.gstolima.comunicaciones.EnvioLectura object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey();
        }
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        long tableNativePtr = table.getNativePtr();
        EnvioLecturaColumnInfo columnInfo = (EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        long objKey = Table.NO_MATCH;
        Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm();
        if (primaryKeyValue != null) {
            objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
        }
        if (objKey == Table.NO_MATCH) {
            objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
        }
        cache.put(object, objKey);
        String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$claveRuta();
        if (realmGet$claveRuta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
        }
        String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$moduloTrabajo();
        if (realmGet$moduloTrabajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
        }
        String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$ciclo();
        if (realmGet$ciclo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
        }
        String realmGet$municipio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$municipio();
        if (realmGet$municipio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.municipioColKey, objKey, realmGet$municipio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.municipioColKey, objKey, false);
        }
        String realmGet$seccion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$seccion();
        if (realmGet$seccion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.seccionColKey, objKey, realmGet$seccion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.seccionColKey, objKey, false);
        }
        String realmGet$departamento = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$departamento();
        if (realmGet$departamento != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.departamentoColKey, objKey, realmGet$departamento, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.departamentoColKey, objKey, false);
        }
        String realmGet$cuenta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$cuenta();
        if (realmGet$cuenta != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cuentaColKey, objKey, realmGet$cuenta, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cuentaColKey, objKey, false);
        }
        String realmGet$nroContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroContador();
        if (realmGet$nroContador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nroContadorColKey, objKey, realmGet$nroContador, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nroContadorColKey, objKey, false);
        }
        String realmGet$marcaMedidor = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$marcaMedidor();
        if (realmGet$marcaMedidor != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, realmGet$marcaMedidor, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, false);
        }
        String realmGet$fichaCatastral = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fichaCatastral();
        if (realmGet$fichaCatastral != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, realmGet$fichaCatastral, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, false);
        }
        String realmGet$fechaHoraLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraLectura();
        if (realmGet$fechaHoraLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, realmGet$fechaHoraLectura, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, false);
        }
        String realmGet$horaImpresion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$horaImpresion();
        if (realmGet$horaImpresion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.horaImpresionColKey, objKey, realmGet$horaImpresion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.horaImpresionColKey, objKey, false);
        }
        String realmGet$lecturaTomada = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaTomada();
        if (realmGet$lecturaTomada != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, realmGet$lecturaTomada, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, false);
        }
        String realmGet$causaNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$causaNoLectura();
        if (realmGet$causaNoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, realmGet$causaNoLectura, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, false);
        }
        String realmGet$codLector = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codLector();
        if (realmGet$codLector != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codLectorColKey, objKey, realmGet$codLector, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codLectorColKey, objKey, false);
        }
        String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$terminal();
        if (realmGet$terminal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.terminalColKey, objKey, false);
        }
        String realmGet$periodoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$periodoLectura();
        if (realmGet$periodoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, realmGet$periodoLectura, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, false);
        }
        String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$longitud();
        if (realmGet$longitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.longitudColKey, objKey, false);
        }
        String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$latitud();
        if (realmGet$latitud != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.latitudColKey, objKey, false);
        }
        String realmGet$nroSatelites = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroSatelites();
        if (realmGet$nroSatelites != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, realmGet$nroSatelites, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, false);
        }
        String realmGet$informe = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$informe();
        if (realmGet$informe != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.informeColKey, objKey, false);
        }
        String realmGet$criticaPda = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$criticaPda();
        if (realmGet$criticaPda != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.criticaPdaColKey, objKey, realmGet$criticaPda, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.criticaPdaColKey, objKey, false);
        }
        String realmGet$estadoEnvioOriginal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvioOriginal();
        if (realmGet$estadoEnvioOriginal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, realmGet$estadoEnvioOriginal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, false);
        }
        String realmGet$comentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$comentario();
        if (realmGet$comentario != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.comentarioColKey, objKey, realmGet$comentario, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.comentarioColKey, objKey, false);
        }
        String realmGet$intentos = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentos();
        if (realmGet$intentos != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.intentosColKey, objKey, realmGet$intentos, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.intentosColKey, objKey, false);
        }
        String realmGet$fechaHoraSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraSatelite();
        if (realmGet$fechaHoraSatelite != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, realmGet$fechaHoraSatelite, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, false);
        }
        String realmGet$altitudSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$altitudSatelite();
        if (realmGet$altitudSatelite != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, realmGet$altitudSatelite, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, false);
        }
        String realmGet$distancia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$distancia();
        if (realmGet$distancia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.distanciaColKey, objKey, realmGet$distancia, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.distanciaColKey, objKey, false);
        }
        String realmGet$lecturaModificada1 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada1();
        if (realmGet$lecturaModificada1 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, realmGet$lecturaModificada1, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, false);
        }
        String realmGet$lecturaModificada2 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada2();
        if (realmGet$lecturaModificada2 != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, realmGet$lecturaModificada2, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, false);
        }
        String realmGet$tiempo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$tiempo();
        if (realmGet$tiempo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tiempoColKey, objKey, realmGet$tiempo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tiempoColKey, objKey, false);
        }
        String realmGet$consumoFacturado = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$consumoFacturado();
        if (realmGet$consumoFacturado != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, realmGet$consumoFacturado, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, false);
        }
        String realmGet$lecturaAnterior = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaAnterior();
        if (realmGet$lecturaAnterior != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, realmGet$lecturaAnterior, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, false);
        }
        String realmGet$estrato = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estrato();
        if (realmGet$estrato != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.estratoColKey, objKey, realmGet$estrato, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.estratoColKey, objKey, false);
        }
        String realmGet$uso = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$uso();
        if (realmGet$uso != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.usoColKey, objKey, realmGet$uso, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.usoColKey, objKey, false);
        }
        String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nombreArchivo();
        if (realmGet$nombreArchivo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, false);
        }
        String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRegistro();
        if (realmGet$idRegistro != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idRegistroColKey, objKey, false);
        }
        String realmGet$totalCausasNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalCausasNoLectura();
        if (realmGet$totalCausasNoLectura != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, realmGet$totalCausasNoLectura, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, false);
        }
        String realmGet$totalLecturas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturas();
        if (realmGet$totalLecturas != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasColKey, objKey, realmGet$totalLecturas, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalLecturasColKey, objKey, false);
        }
        String realmGet$totalConsumoBajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoBajo();
        if (realmGet$totalConsumoBajo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, realmGet$totalConsumoBajo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, false);
        }
        String realmGet$totalConsumoAlto = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoAlto();
        if (realmGet$totalConsumoAlto != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, realmGet$totalConsumoAlto, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, false);
        }
        String realmGet$totalConsumoNormal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoNormal();
        if (realmGet$totalConsumoNormal != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, realmGet$totalConsumoNormal, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, false);
        }
        String realmGet$totalLecturasIguales = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturasIguales();
        if (realmGet$totalLecturasIguales != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, realmGet$totalLecturasIguales, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, false);
        }
        String realmGet$totalLeidas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLeidas();
        if (realmGet$totalLeidas != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalLeidasColKey, objKey, realmGet$totalLeidas, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalLeidasColKey, objKey, false);
        }
        String realmGet$numObser = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numObser();
        if (realmGet$numObser != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numObserColKey, objKey, realmGet$numObser, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.numObserColKey, objKey, false);
        }
        String realmGet$numInformes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numInformes();
        if (realmGet$numInformes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.numInformesColKey, objKey, realmGet$numInformes, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.numInformesColKey, objKey, false);
        }
        String realmGet$totalTiempoProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalTiempoProm();
        if (realmGet$totalTiempoProm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, realmGet$totalTiempoProm, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, false);
        }
        String realmGet$totalDistanciaProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalDistanciaProm();
        if (realmGet$totalDistanciaProm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, realmGet$totalDistanciaProm, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, false);
        }
        String realmGet$codigoSac = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codigoSac();
        if (realmGet$codigoSac != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.codigoSacColKey, objKey, realmGet$codigoSac, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.codigoSacColKey, objKey, false);
        }
        String realmGet$anno = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$anno();
        if (realmGet$anno != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.annoColKey, objKey, false);
        }
        String realmGet$mes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$mes();
        if (realmGet$mes != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.mesColKey, objKey, false);
        }
        String realmGet$digitoChequeo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$digitoChequeo();
        if (realmGet$digitoChequeo != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, realmGet$digitoChequeo, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, false);
        }
        String realmGet$idContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idContador();
        if (realmGet$idContador != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idContadorColKey, objKey, realmGet$idContador, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idContadorColKey, objKey, false);
        }
        String realmGet$descAnomalia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descAnomalia();
        if (realmGet$descAnomalia != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, realmGet$descAnomalia, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, false);
        }
        String realmGet$descComentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descComentario();
        if (realmGet$descComentario != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descComentarioColKey, objKey, realmGet$descComentario, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descComentarioColKey, objKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvio(), false);
        String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaCreacion();
        if (realmGet$fechaCreacion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
        }
        String realmGet$fechaEnvioApi = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaEnvioApi();
        if (realmGet$fechaEnvioApi != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, realmGet$fechaEnvioApi, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
        String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$errorEnvio();
        if (realmGet$errorEnvio != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.errorEnvioColKey, objKey, false);
        }
        return objKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        long tableNativePtr = table.getNativePtr();
        EnvioLecturaColumnInfo columnInfo = (EnvioLecturaColumnInfo) realm.getSchema().getColumnInfo(com.gstolima.comunicaciones.EnvioLectura.class);
        long pkColumnKey = columnInfo.idRealmColKey;
        com.gstolima.comunicaciones.EnvioLectura object = null;
        while (objects.hasNext()) {
            object = (com.gstolima.comunicaciones.EnvioLectura) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && !RealmObject.isFrozen(object) && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getObjectKey());
                continue;
            }
            long objKey = Table.NO_MATCH;
            Object primaryKeyValue = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm();
            if (primaryKeyValue != null) {
                objKey = Table.nativeFindFirstInt(tableNativePtr, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
            }
            if (objKey == Table.NO_MATCH) {
                objKey = OsObject.createRowWithPrimaryKey(table, pkColumnKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRealm());
            }
            cache.put(object, objKey);
            String realmGet$claveRuta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$claveRuta();
            if (realmGet$claveRuta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.claveRutaColKey, objKey, realmGet$claveRuta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.claveRutaColKey, objKey, false);
            }
            String realmGet$moduloTrabajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$moduloTrabajo();
            if (realmGet$moduloTrabajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, realmGet$moduloTrabajo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.moduloTrabajoColKey, objKey, false);
            }
            String realmGet$ciclo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$ciclo();
            if (realmGet$ciclo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cicloColKey, objKey, realmGet$ciclo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cicloColKey, objKey, false);
            }
            String realmGet$municipio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$municipio();
            if (realmGet$municipio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.municipioColKey, objKey, realmGet$municipio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.municipioColKey, objKey, false);
            }
            String realmGet$seccion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$seccion();
            if (realmGet$seccion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.seccionColKey, objKey, realmGet$seccion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.seccionColKey, objKey, false);
            }
            String realmGet$departamento = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$departamento();
            if (realmGet$departamento != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.departamentoColKey, objKey, realmGet$departamento, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.departamentoColKey, objKey, false);
            }
            String realmGet$cuenta = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$cuenta();
            if (realmGet$cuenta != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cuentaColKey, objKey, realmGet$cuenta, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cuentaColKey, objKey, false);
            }
            String realmGet$nroContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroContador();
            if (realmGet$nroContador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nroContadorColKey, objKey, realmGet$nroContador, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nroContadorColKey, objKey, false);
            }
            String realmGet$marcaMedidor = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$marcaMedidor();
            if (realmGet$marcaMedidor != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, realmGet$marcaMedidor, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.marcaMedidorColKey, objKey, false);
            }
            String realmGet$fichaCatastral = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fichaCatastral();
            if (realmGet$fichaCatastral != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, realmGet$fichaCatastral, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fichaCatastralColKey, objKey, false);
            }
            String realmGet$fechaHoraLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraLectura();
            if (realmGet$fechaHoraLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, realmGet$fechaHoraLectura, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraLecturaColKey, objKey, false);
            }
            String realmGet$horaImpresion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$horaImpresion();
            if (realmGet$horaImpresion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.horaImpresionColKey, objKey, realmGet$horaImpresion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.horaImpresionColKey, objKey, false);
            }
            String realmGet$lecturaTomada = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaTomada();
            if (realmGet$lecturaTomada != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, realmGet$lecturaTomada, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lecturaTomadaColKey, objKey, false);
            }
            String realmGet$causaNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$causaNoLectura();
            if (realmGet$causaNoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, realmGet$causaNoLectura, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.causaNoLecturaColKey, objKey, false);
            }
            String realmGet$codLector = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codLector();
            if (realmGet$codLector != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codLectorColKey, objKey, realmGet$codLector, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codLectorColKey, objKey, false);
            }
            String realmGet$terminal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$terminal();
            if (realmGet$terminal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.terminalColKey, objKey, realmGet$terminal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.terminalColKey, objKey, false);
            }
            String realmGet$periodoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$periodoLectura();
            if (realmGet$periodoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, realmGet$periodoLectura, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.periodoLecturaColKey, objKey, false);
            }
            String realmGet$longitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$longitud();
            if (realmGet$longitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.longitudColKey, objKey, realmGet$longitud, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.longitudColKey, objKey, false);
            }
            String realmGet$latitud = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$latitud();
            if (realmGet$latitud != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.latitudColKey, objKey, realmGet$latitud, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.latitudColKey, objKey, false);
            }
            String realmGet$nroSatelites = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nroSatelites();
            if (realmGet$nroSatelites != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, realmGet$nroSatelites, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nroSatelitesColKey, objKey, false);
            }
            String realmGet$informe = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$informe();
            if (realmGet$informe != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.informeColKey, objKey, realmGet$informe, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.informeColKey, objKey, false);
            }
            String realmGet$criticaPda = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$criticaPda();
            if (realmGet$criticaPda != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.criticaPdaColKey, objKey, realmGet$criticaPda, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.criticaPdaColKey, objKey, false);
            }
            String realmGet$estadoEnvioOriginal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvioOriginal();
            if (realmGet$estadoEnvioOriginal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, realmGet$estadoEnvioOriginal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.estadoEnvioOriginalColKey, objKey, false);
            }
            String realmGet$comentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$comentario();
            if (realmGet$comentario != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.comentarioColKey, objKey, realmGet$comentario, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.comentarioColKey, objKey, false);
            }
            String realmGet$intentos = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentos();
            if (realmGet$intentos != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.intentosColKey, objKey, realmGet$intentos, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.intentosColKey, objKey, false);
            }
            String realmGet$fechaHoraSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaHoraSatelite();
            if (realmGet$fechaHoraSatelite != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, realmGet$fechaHoraSatelite, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaHoraSateliteColKey, objKey, false);
            }
            String realmGet$altitudSatelite = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$altitudSatelite();
            if (realmGet$altitudSatelite != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, realmGet$altitudSatelite, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.altitudSateliteColKey, objKey, false);
            }
            String realmGet$distancia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$distancia();
            if (realmGet$distancia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.distanciaColKey, objKey, realmGet$distancia, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.distanciaColKey, objKey, false);
            }
            String realmGet$lecturaModificada1 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada1();
            if (realmGet$lecturaModificada1 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, realmGet$lecturaModificada1, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lecturaModificada1ColKey, objKey, false);
            }
            String realmGet$lecturaModificada2 = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaModificada2();
            if (realmGet$lecturaModificada2 != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, realmGet$lecturaModificada2, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lecturaModificada2ColKey, objKey, false);
            }
            String realmGet$tiempo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$tiempo();
            if (realmGet$tiempo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.tiempoColKey, objKey, realmGet$tiempo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.tiempoColKey, objKey, false);
            }
            String realmGet$consumoFacturado = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$consumoFacturado();
            if (realmGet$consumoFacturado != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, realmGet$consumoFacturado, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.consumoFacturadoColKey, objKey, false);
            }
            String realmGet$lecturaAnterior = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$lecturaAnterior();
            if (realmGet$lecturaAnterior != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, realmGet$lecturaAnterior, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.lecturaAnteriorColKey, objKey, false);
            }
            String realmGet$estrato = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estrato();
            if (realmGet$estrato != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.estratoColKey, objKey, realmGet$estrato, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.estratoColKey, objKey, false);
            }
            String realmGet$uso = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$uso();
            if (realmGet$uso != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.usoColKey, objKey, realmGet$uso, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.usoColKey, objKey, false);
            }
            String realmGet$nombreArchivo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$nombreArchivo();
            if (realmGet$nombreArchivo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, realmGet$nombreArchivo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nombreArchivoColKey, objKey, false);
            }
            String realmGet$idRegistro = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idRegistro();
            if (realmGet$idRegistro != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idRegistroColKey, objKey, realmGet$idRegistro, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.idRegistroColKey, objKey, false);
            }
            String realmGet$totalCausasNoLectura = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalCausasNoLectura();
            if (realmGet$totalCausasNoLectura != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, realmGet$totalCausasNoLectura, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalCausasNoLecturaColKey, objKey, false);
            }
            String realmGet$totalLecturas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturas();
            if (realmGet$totalLecturas != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasColKey, objKey, realmGet$totalLecturas, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalLecturasColKey, objKey, false);
            }
            String realmGet$totalConsumoBajo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoBajo();
            if (realmGet$totalConsumoBajo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, realmGet$totalConsumoBajo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoBajoColKey, objKey, false);
            }
            String realmGet$totalConsumoAlto = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoAlto();
            if (realmGet$totalConsumoAlto != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, realmGet$totalConsumoAlto, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoAltoColKey, objKey, false);
            }
            String realmGet$totalConsumoNormal = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalConsumoNormal();
            if (realmGet$totalConsumoNormal != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, realmGet$totalConsumoNormal, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalConsumoNormalColKey, objKey, false);
            }
            String realmGet$totalLecturasIguales = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLecturasIguales();
            if (realmGet$totalLecturasIguales != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, realmGet$totalLecturasIguales, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalLecturasIgualesColKey, objKey, false);
            }
            String realmGet$totalLeidas = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalLeidas();
            if (realmGet$totalLeidas != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalLeidasColKey, objKey, realmGet$totalLeidas, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalLeidasColKey, objKey, false);
            }
            String realmGet$numObser = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numObser();
            if (realmGet$numObser != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numObserColKey, objKey, realmGet$numObser, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.numObserColKey, objKey, false);
            }
            String realmGet$numInformes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$numInformes();
            if (realmGet$numInformes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.numInformesColKey, objKey, realmGet$numInformes, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.numInformesColKey, objKey, false);
            }
            String realmGet$totalTiempoProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalTiempoProm();
            if (realmGet$totalTiempoProm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, realmGet$totalTiempoProm, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalTiempoPromColKey, objKey, false);
            }
            String realmGet$totalDistanciaProm = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$totalDistanciaProm();
            if (realmGet$totalDistanciaProm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, realmGet$totalDistanciaProm, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.totalDistanciaPromColKey, objKey, false);
            }
            String realmGet$codigoSac = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$codigoSac();
            if (realmGet$codigoSac != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.codigoSacColKey, objKey, realmGet$codigoSac, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.codigoSacColKey, objKey, false);
            }
            String realmGet$anno = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$anno();
            if (realmGet$anno != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.annoColKey, objKey, realmGet$anno, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.annoColKey, objKey, false);
            }
            String realmGet$mes = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$mes();
            if (realmGet$mes != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.mesColKey, objKey, realmGet$mes, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.mesColKey, objKey, false);
            }
            String realmGet$digitoChequeo = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$digitoChequeo();
            if (realmGet$digitoChequeo != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, realmGet$digitoChequeo, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.digitoChequeoColKey, objKey, false);
            }
            String realmGet$idContador = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$idContador();
            if (realmGet$idContador != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.idContadorColKey, objKey, realmGet$idContador, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.idContadorColKey, objKey, false);
            }
            String realmGet$descAnomalia = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descAnomalia();
            if (realmGet$descAnomalia != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, realmGet$descAnomalia, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.descAnomaliaColKey, objKey, false);
            }
            String realmGet$descComentario = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$descComentario();
            if (realmGet$descComentario != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descComentarioColKey, objKey, realmGet$descComentario, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.descComentarioColKey, objKey, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.estadoEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$estadoEnvio(), false);
            String realmGet$fechaCreacion = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaCreacion();
            if (realmGet$fechaCreacion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, realmGet$fechaCreacion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaCreacionColKey, objKey, false);
            }
            String realmGet$fechaEnvioApi = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$fechaEnvioApi();
            if (realmGet$fechaEnvioApi != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, realmGet$fechaEnvioApi, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fechaEnvioApiColKey, objKey, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.intentosEnvioColKey, objKey, ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$intentosEnvio(), false);
            String realmGet$errorEnvio = ((com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) object).realmGet$errorEnvio();
            if (realmGet$errorEnvio != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.errorEnvioColKey, objKey, realmGet$errorEnvio, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.errorEnvioColKey, objKey, false);
            }
        }
    }

    public static com.gstolima.comunicaciones.EnvioLectura createDetachedCopy(com.gstolima.comunicaciones.EnvioLectura realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.gstolima.comunicaciones.EnvioLectura unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.gstolima.comunicaciones.EnvioLectura();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.gstolima.comunicaciones.EnvioLectura) cachedObject.object;
            }
            unmanagedObject = (com.gstolima.comunicaciones.EnvioLectura) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface unmanagedCopy = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) unmanagedObject;
        com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface realmSource = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) realmObject;
        Realm objectRealm = (Realm) ((RealmObjectProxy) realmObject).realmGet$proxyState().getRealm$realm();
        unmanagedCopy.realmSet$claveRuta(realmSource.realmGet$claveRuta());
        unmanagedCopy.realmSet$moduloTrabajo(realmSource.realmGet$moduloTrabajo());
        unmanagedCopy.realmSet$idRealm(realmSource.realmGet$idRealm());
        unmanagedCopy.realmSet$ciclo(realmSource.realmGet$ciclo());
        unmanagedCopy.realmSet$municipio(realmSource.realmGet$municipio());
        unmanagedCopy.realmSet$seccion(realmSource.realmGet$seccion());
        unmanagedCopy.realmSet$departamento(realmSource.realmGet$departamento());
        unmanagedCopy.realmSet$cuenta(realmSource.realmGet$cuenta());
        unmanagedCopy.realmSet$nroContador(realmSource.realmGet$nroContador());
        unmanagedCopy.realmSet$marcaMedidor(realmSource.realmGet$marcaMedidor());
        unmanagedCopy.realmSet$fichaCatastral(realmSource.realmGet$fichaCatastral());
        unmanagedCopy.realmSet$fechaHoraLectura(realmSource.realmGet$fechaHoraLectura());
        unmanagedCopy.realmSet$horaImpresion(realmSource.realmGet$horaImpresion());
        unmanagedCopy.realmSet$lecturaTomada(realmSource.realmGet$lecturaTomada());
        unmanagedCopy.realmSet$causaNoLectura(realmSource.realmGet$causaNoLectura());
        unmanagedCopy.realmSet$codLector(realmSource.realmGet$codLector());
        unmanagedCopy.realmSet$terminal(realmSource.realmGet$terminal());
        unmanagedCopy.realmSet$periodoLectura(realmSource.realmGet$periodoLectura());
        unmanagedCopy.realmSet$longitud(realmSource.realmGet$longitud());
        unmanagedCopy.realmSet$latitud(realmSource.realmGet$latitud());
        unmanagedCopy.realmSet$nroSatelites(realmSource.realmGet$nroSatelites());
        unmanagedCopy.realmSet$informe(realmSource.realmGet$informe());
        unmanagedCopy.realmSet$criticaPda(realmSource.realmGet$criticaPda());
        unmanagedCopy.realmSet$estadoEnvioOriginal(realmSource.realmGet$estadoEnvioOriginal());
        unmanagedCopy.realmSet$comentario(realmSource.realmGet$comentario());
        unmanagedCopy.realmSet$intentos(realmSource.realmGet$intentos());
        unmanagedCopy.realmSet$fechaHoraSatelite(realmSource.realmGet$fechaHoraSatelite());
        unmanagedCopy.realmSet$altitudSatelite(realmSource.realmGet$altitudSatelite());
        unmanagedCopy.realmSet$distancia(realmSource.realmGet$distancia());
        unmanagedCopy.realmSet$lecturaModificada1(realmSource.realmGet$lecturaModificada1());
        unmanagedCopy.realmSet$lecturaModificada2(realmSource.realmGet$lecturaModificada2());
        unmanagedCopy.realmSet$tiempo(realmSource.realmGet$tiempo());
        unmanagedCopy.realmSet$consumoFacturado(realmSource.realmGet$consumoFacturado());
        unmanagedCopy.realmSet$lecturaAnterior(realmSource.realmGet$lecturaAnterior());
        unmanagedCopy.realmSet$estrato(realmSource.realmGet$estrato());
        unmanagedCopy.realmSet$uso(realmSource.realmGet$uso());
        unmanagedCopy.realmSet$nombreArchivo(realmSource.realmGet$nombreArchivo());
        unmanagedCopy.realmSet$idRegistro(realmSource.realmGet$idRegistro());
        unmanagedCopy.realmSet$totalCausasNoLectura(realmSource.realmGet$totalCausasNoLectura());
        unmanagedCopy.realmSet$totalLecturas(realmSource.realmGet$totalLecturas());
        unmanagedCopy.realmSet$totalConsumoBajo(realmSource.realmGet$totalConsumoBajo());
        unmanagedCopy.realmSet$totalConsumoAlto(realmSource.realmGet$totalConsumoAlto());
        unmanagedCopy.realmSet$totalConsumoNormal(realmSource.realmGet$totalConsumoNormal());
        unmanagedCopy.realmSet$totalLecturasIguales(realmSource.realmGet$totalLecturasIguales());
        unmanagedCopy.realmSet$totalLeidas(realmSource.realmGet$totalLeidas());
        unmanagedCopy.realmSet$numObser(realmSource.realmGet$numObser());
        unmanagedCopy.realmSet$numInformes(realmSource.realmGet$numInformes());
        unmanagedCopy.realmSet$totalTiempoProm(realmSource.realmGet$totalTiempoProm());
        unmanagedCopy.realmSet$totalDistanciaProm(realmSource.realmGet$totalDistanciaProm());
        unmanagedCopy.realmSet$codigoSac(realmSource.realmGet$codigoSac());
        unmanagedCopy.realmSet$anno(realmSource.realmGet$anno());
        unmanagedCopy.realmSet$mes(realmSource.realmGet$mes());
        unmanagedCopy.realmSet$digitoChequeo(realmSource.realmGet$digitoChequeo());
        unmanagedCopy.realmSet$idContador(realmSource.realmGet$idContador());
        unmanagedCopy.realmSet$descAnomalia(realmSource.realmGet$descAnomalia());
        unmanagedCopy.realmSet$descComentario(realmSource.realmGet$descComentario());
        unmanagedCopy.realmSet$estadoEnvio(realmSource.realmGet$estadoEnvio());
        unmanagedCopy.realmSet$fechaCreacion(realmSource.realmGet$fechaCreacion());
        unmanagedCopy.realmSet$fechaEnvioApi(realmSource.realmGet$fechaEnvioApi());
        unmanagedCopy.realmSet$intentosEnvio(realmSource.realmGet$intentosEnvio());
        unmanagedCopy.realmSet$errorEnvio(realmSource.realmGet$errorEnvio());

        return unmanagedObject;
    }

    static com.gstolima.comunicaciones.EnvioLectura update(Realm realm, EnvioLecturaColumnInfo columnInfo, com.gstolima.comunicaciones.EnvioLectura realmObject, com.gstolima.comunicaciones.EnvioLectura newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface realmObjectTarget = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) realmObject;
        com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface realmObjectSource = (com_gstolima_comunicaciones_EnvioLecturaRealmProxyInterface) newObject;
        Table table = realm.getTable(com.gstolima.comunicaciones.EnvioLectura.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, flags);
        builder.addString(columnInfo.claveRutaColKey, realmObjectSource.realmGet$claveRuta());
        builder.addString(columnInfo.moduloTrabajoColKey, realmObjectSource.realmGet$moduloTrabajo());
        builder.addInteger(columnInfo.idRealmColKey, realmObjectSource.realmGet$idRealm());
        builder.addString(columnInfo.cicloColKey, realmObjectSource.realmGet$ciclo());
        builder.addString(columnInfo.municipioColKey, realmObjectSource.realmGet$municipio());
        builder.addString(columnInfo.seccionColKey, realmObjectSource.realmGet$seccion());
        builder.addString(columnInfo.departamentoColKey, realmObjectSource.realmGet$departamento());
        builder.addString(columnInfo.cuentaColKey, realmObjectSource.realmGet$cuenta());
        builder.addString(columnInfo.nroContadorColKey, realmObjectSource.realmGet$nroContador());
        builder.addString(columnInfo.marcaMedidorColKey, realmObjectSource.realmGet$marcaMedidor());
        builder.addString(columnInfo.fichaCatastralColKey, realmObjectSource.realmGet$fichaCatastral());
        builder.addString(columnInfo.fechaHoraLecturaColKey, realmObjectSource.realmGet$fechaHoraLectura());
        builder.addString(columnInfo.horaImpresionColKey, realmObjectSource.realmGet$horaImpresion());
        builder.addString(columnInfo.lecturaTomadaColKey, realmObjectSource.realmGet$lecturaTomada());
        builder.addString(columnInfo.causaNoLecturaColKey, realmObjectSource.realmGet$causaNoLectura());
        builder.addString(columnInfo.codLectorColKey, realmObjectSource.realmGet$codLector());
        builder.addString(columnInfo.terminalColKey, realmObjectSource.realmGet$terminal());
        builder.addString(columnInfo.periodoLecturaColKey, realmObjectSource.realmGet$periodoLectura());
        builder.addString(columnInfo.longitudColKey, realmObjectSource.realmGet$longitud());
        builder.addString(columnInfo.latitudColKey, realmObjectSource.realmGet$latitud());
        builder.addString(columnInfo.nroSatelitesColKey, realmObjectSource.realmGet$nroSatelites());
        builder.addString(columnInfo.informeColKey, realmObjectSource.realmGet$informe());
        builder.addString(columnInfo.criticaPdaColKey, realmObjectSource.realmGet$criticaPda());
        builder.addString(columnInfo.estadoEnvioOriginalColKey, realmObjectSource.realmGet$estadoEnvioOriginal());
        builder.addString(columnInfo.comentarioColKey, realmObjectSource.realmGet$comentario());
        builder.addString(columnInfo.intentosColKey, realmObjectSource.realmGet$intentos());
        builder.addString(columnInfo.fechaHoraSateliteColKey, realmObjectSource.realmGet$fechaHoraSatelite());
        builder.addString(columnInfo.altitudSateliteColKey, realmObjectSource.realmGet$altitudSatelite());
        builder.addString(columnInfo.distanciaColKey, realmObjectSource.realmGet$distancia());
        builder.addString(columnInfo.lecturaModificada1ColKey, realmObjectSource.realmGet$lecturaModificada1());
        builder.addString(columnInfo.lecturaModificada2ColKey, realmObjectSource.realmGet$lecturaModificada2());
        builder.addString(columnInfo.tiempoColKey, realmObjectSource.realmGet$tiempo());
        builder.addString(columnInfo.consumoFacturadoColKey, realmObjectSource.realmGet$consumoFacturado());
        builder.addString(columnInfo.lecturaAnteriorColKey, realmObjectSource.realmGet$lecturaAnterior());
        builder.addString(columnInfo.estratoColKey, realmObjectSource.realmGet$estrato());
        builder.addString(columnInfo.usoColKey, realmObjectSource.realmGet$uso());
        builder.addString(columnInfo.nombreArchivoColKey, realmObjectSource.realmGet$nombreArchivo());
        builder.addString(columnInfo.idRegistroColKey, realmObjectSource.realmGet$idRegistro());
        builder.addString(columnInfo.totalCausasNoLecturaColKey, realmObjectSource.realmGet$totalCausasNoLectura());
        builder.addString(columnInfo.totalLecturasColKey, realmObjectSource.realmGet$totalLecturas());
        builder.addString(columnInfo.totalConsumoBajoColKey, realmObjectSource.realmGet$totalConsumoBajo());
        builder.addString(columnInfo.totalConsumoAltoColKey, realmObjectSource.realmGet$totalConsumoAlto());
        builder.addString(columnInfo.totalConsumoNormalColKey, realmObjectSource.realmGet$totalConsumoNormal());
        builder.addString(columnInfo.totalLecturasIgualesColKey, realmObjectSource.realmGet$totalLecturasIguales());
        builder.addString(columnInfo.totalLeidasColKey, realmObjectSource.realmGet$totalLeidas());
        builder.addString(columnInfo.numObserColKey, realmObjectSource.realmGet$numObser());
        builder.addString(columnInfo.numInformesColKey, realmObjectSource.realmGet$numInformes());
        builder.addString(columnInfo.totalTiempoPromColKey, realmObjectSource.realmGet$totalTiempoProm());
        builder.addString(columnInfo.totalDistanciaPromColKey, realmObjectSource.realmGet$totalDistanciaProm());
        builder.addString(columnInfo.codigoSacColKey, realmObjectSource.realmGet$codigoSac());
        builder.addString(columnInfo.annoColKey, realmObjectSource.realmGet$anno());
        builder.addString(columnInfo.mesColKey, realmObjectSource.realmGet$mes());
        builder.addString(columnInfo.digitoChequeoColKey, realmObjectSource.realmGet$digitoChequeo());
        builder.addString(columnInfo.idContadorColKey, realmObjectSource.realmGet$idContador());
        builder.addString(columnInfo.descAnomaliaColKey, realmObjectSource.realmGet$descAnomalia());
        builder.addString(columnInfo.descComentarioColKey, realmObjectSource.realmGet$descComentario());
        builder.addInteger(columnInfo.estadoEnvioColKey, realmObjectSource.realmGet$estadoEnvio());
        builder.addString(columnInfo.fechaCreacionColKey, realmObjectSource.realmGet$fechaCreacion());
        builder.addString(columnInfo.fechaEnvioApiColKey, realmObjectSource.realmGet$fechaEnvioApi());
        builder.addInteger(columnInfo.intentosEnvioColKey, realmObjectSource.realmGet$intentosEnvio());
        builder.addString(columnInfo.errorEnvioColKey, realmObjectSource.realmGet$errorEnvio());

        builder.updateExistingTopLevelObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("EnvioLectura = proxy[");
        stringBuilder.append("{claveRuta:");
        stringBuilder.append(realmGet$claveRuta() != null ? realmGet$claveRuta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{moduloTrabajo:");
        stringBuilder.append(realmGet$moduloTrabajo() != null ? realmGet$moduloTrabajo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idRealm:");
        stringBuilder.append(realmGet$idRealm());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ciclo:");
        stringBuilder.append(realmGet$ciclo() != null ? realmGet$ciclo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{municipio:");
        stringBuilder.append(realmGet$municipio() != null ? realmGet$municipio() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{seccion:");
        stringBuilder.append(realmGet$seccion() != null ? realmGet$seccion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{departamento:");
        stringBuilder.append(realmGet$departamento() != null ? realmGet$departamento() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cuenta:");
        stringBuilder.append(realmGet$cuenta() != null ? realmGet$cuenta() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{nroContador:");
        stringBuilder.append(realmGet$nroContador() != null ? realmGet$nroContador() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{marcaMedidor:");
        stringBuilder.append(realmGet$marcaMedidor() != null ? realmGet$marcaMedidor() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fichaCatastral:");
        stringBuilder.append(realmGet$fichaCatastral() != null ? realmGet$fichaCatastral() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaHoraLectura:");
        stringBuilder.append(realmGet$fechaHoraLectura() != null ? realmGet$fechaHoraLectura() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{horaImpresion:");
        stringBuilder.append(realmGet$horaImpresion() != null ? realmGet$horaImpresion() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lecturaTomada:");
        stringBuilder.append(realmGet$lecturaTomada() != null ? realmGet$lecturaTomada() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{causaNoLectura:");
        stringBuilder.append(realmGet$causaNoLectura() != null ? realmGet$causaNoLectura() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codLector:");
        stringBuilder.append(realmGet$codLector() != null ? realmGet$codLector() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{terminal:");
        stringBuilder.append(realmGet$terminal() != null ? realmGet$terminal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{periodoLectura:");
        stringBuilder.append(realmGet$periodoLectura() != null ? realmGet$periodoLectura() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{longitud:");
        stringBuilder.append(realmGet$longitud() != null ? realmGet$longitud() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{latitud:");
        stringBuilder.append(realmGet$latitud() != null ? realmGet$latitud() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{nroSatelites:");
        stringBuilder.append(realmGet$nroSatelites() != null ? realmGet$nroSatelites() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{informe:");
        stringBuilder.append(realmGet$informe() != null ? realmGet$informe() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{criticaPda:");
        stringBuilder.append(realmGet$criticaPda() != null ? realmGet$criticaPda() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{estadoEnvioOriginal:");
        stringBuilder.append(realmGet$estadoEnvioOriginal() != null ? realmGet$estadoEnvioOriginal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{comentario:");
        stringBuilder.append(realmGet$comentario() != null ? realmGet$comentario() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{intentos:");
        stringBuilder.append(realmGet$intentos() != null ? realmGet$intentos() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fechaHoraSatelite:");
        stringBuilder.append(realmGet$fechaHoraSatelite() != null ? realmGet$fechaHoraSatelite() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{altitudSatelite:");
        stringBuilder.append(realmGet$altitudSatelite() != null ? realmGet$altitudSatelite() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{distancia:");
        stringBuilder.append(realmGet$distancia() != null ? realmGet$distancia() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lecturaModificada1:");
        stringBuilder.append(realmGet$lecturaModificada1() != null ? realmGet$lecturaModificada1() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lecturaModificada2:");
        stringBuilder.append(realmGet$lecturaModificada2() != null ? realmGet$lecturaModificada2() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tiempo:");
        stringBuilder.append(realmGet$tiempo() != null ? realmGet$tiempo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{consumoFacturado:");
        stringBuilder.append(realmGet$consumoFacturado() != null ? realmGet$consumoFacturado() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lecturaAnterior:");
        stringBuilder.append(realmGet$lecturaAnterior() != null ? realmGet$lecturaAnterior() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{estrato:");
        stringBuilder.append(realmGet$estrato() != null ? realmGet$estrato() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{uso:");
        stringBuilder.append(realmGet$uso() != null ? realmGet$uso() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{nombreArchivo:");
        stringBuilder.append(realmGet$nombreArchivo() != null ? realmGet$nombreArchivo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idRegistro:");
        stringBuilder.append(realmGet$idRegistro() != null ? realmGet$idRegistro() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalCausasNoLectura:");
        stringBuilder.append(realmGet$totalCausasNoLectura() != null ? realmGet$totalCausasNoLectura() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalLecturas:");
        stringBuilder.append(realmGet$totalLecturas() != null ? realmGet$totalLecturas() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalConsumoBajo:");
        stringBuilder.append(realmGet$totalConsumoBajo() != null ? realmGet$totalConsumoBajo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalConsumoAlto:");
        stringBuilder.append(realmGet$totalConsumoAlto() != null ? realmGet$totalConsumoAlto() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalConsumoNormal:");
        stringBuilder.append(realmGet$totalConsumoNormal() != null ? realmGet$totalConsumoNormal() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalLecturasIguales:");
        stringBuilder.append(realmGet$totalLecturasIguales() != null ? realmGet$totalLecturasIguales() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalLeidas:");
        stringBuilder.append(realmGet$totalLeidas() != null ? realmGet$totalLeidas() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{numObser:");
        stringBuilder.append(realmGet$numObser() != null ? realmGet$numObser() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{numInformes:");
        stringBuilder.append(realmGet$numInformes() != null ? realmGet$numInformes() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalTiempoProm:");
        stringBuilder.append(realmGet$totalTiempoProm() != null ? realmGet$totalTiempoProm() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{totalDistanciaProm:");
        stringBuilder.append(realmGet$totalDistanciaProm() != null ? realmGet$totalDistanciaProm() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{codigoSac:");
        stringBuilder.append(realmGet$codigoSac() != null ? realmGet$codigoSac() : "null");
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
        stringBuilder.append("{digitoChequeo:");
        stringBuilder.append(realmGet$digitoChequeo() != null ? realmGet$digitoChequeo() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idContador:");
        stringBuilder.append(realmGet$idContador() != null ? realmGet$idContador() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{descAnomalia:");
        stringBuilder.append(realmGet$descAnomalia() != null ? realmGet$descAnomalia() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{descComentario:");
        stringBuilder.append(realmGet$descComentario() != null ? realmGet$descComentario() : "null");
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
        stringBuilder.append("{fechaEnvioApi:");
        stringBuilder.append(realmGet$fechaEnvioApi() != null ? realmGet$fechaEnvioApi() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{intentosEnvio:");
        stringBuilder.append(realmGet$intentosEnvio());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{errorEnvio:");
        stringBuilder.append(realmGet$errorEnvio() != null ? realmGet$errorEnvio() : "null");
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
        com_gstolima_comunicaciones_EnvioLecturaRealmProxy aEnvioLectura = (com_gstolima_comunicaciones_EnvioLecturaRealmProxy)o;

        BaseRealm realm = proxyState.getRealm$realm();
        BaseRealm otherRealm = aEnvioLectura.proxyState.getRealm$realm();
        String path = realm.getPath();
        String otherPath = otherRealm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;
        if (realm.isFrozen() != otherRealm.isFrozen()) return false;
        if (!realm.sharedRealm.getVersionID().equals(otherRealm.sharedRealm.getVersionID())) {
            return false;
        }

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aEnvioLectura.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getObjectKey() != aEnvioLectura.proxyState.getRow$realm().getObjectKey()) return false;

        return true;
    }
}
