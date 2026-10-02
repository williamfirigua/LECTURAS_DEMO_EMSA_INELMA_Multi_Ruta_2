package com.gstolima.comunicaciones;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Index;

/**
 * EnvioLectura - Modelo Realm para almacenar lecturas pendientes de envío
 *
 * Estructura basada en EnvioGPS.java (ENVIOSGPRS.SDA)
 * Longitud registro original: 521 bytes
 * Separador: ";"
 *
 * Orden de campos según escribir_EnvioGPS():
 * ciclo;municipio;seccion;departamento;cuenta;NroContador;marcamedidor;fichacatastral;
 * fechayhoralectura;HoraImpresion;lecturatomada;causadenolectura;CodLector;Terminal;
 * PERIODOLECTURA;Longitud;Latitud;NroSatelites;informe;criticapda;EstadoEnvio;comentario;
 * Intentos;FechaHoraSatelite;AltitudSatelite;Distancia;LecturaModificada1;LecturaModificada2;
 * Tiempo;ConsumoFacturado;LecturaAnterior;estrato;uso;NombreArchivo;id;totalcausasnolectura;
 * totalLecturas;totalConsumoBajo;totalConsumoAlto;totalConsumoNormal;totalLecurasIguales;
 * totalLeidas;numObser;numInformes;totalTiempoProm;totalDistanciaProm;CODIGO_SAC;CRNL
 *
 * @author Global Solutions & Service S.A.S.
 */
public class EnvioLectura extends RealmObject {
    // ===== Identidad de la ruta a la que pertenece este registro =====
    // claveRuta: las 20 posiciones del archivo NOMBRE (ver com.gstolima.tablas.ClaveRuta),
    // por ejemplo "202606009L019010.002". Identifica periodo, ciclo, tipo y predio.
    // Permite borrar solo lo de la ruta activa y enviar cada fila con su propio modulo.
    // Vacio en filas creadas antes de esta version del esquema.
    @Index
    private String claveRuta;

    // "CIC" (lecturas) o "ENT" (entregas). Es el valor que viaja al servidor para armar la
    // carpeta destino. Se congela al capturar, en vez de tomar el global del momento del envio.
    private String moduloTrabajo;


    @PrimaryKey
    private long idRealm;                   // ID autoincremental local Realm (no confundir con id del registro)

    // ===== Campos del archivo plano ENVIOSGPRS.SDA (en orden de escritura) =====

    private String ciclo;                   // (3) EnvioGPS_ciclo
    private String municipio;               // (3) EnvioGPS_mununicipio
    private String seccion;                 // (4) EnvioGPS_seccion
    private String departamento;            // (13) EnvioGPS_departamento - ruta

    @Index
    private String cuenta;                  // (10) EnvioGPS_cuenta
    private String nroContador;             // (20) EnvioGPS_NroContador
    private String marcaMedidor;            // (30) EnvioGPS_marcamedidor
    private String fichaCatastral;          // (15) EnvioGPS_fichacatastral
    private String fechaHoraLectura;        // (8) EnvioGPS_fechayhoralectura
    private String horaImpresion;           // (8) EnvioGPS_HoraImpresion
    private String lecturaTomada;           // (9) EnvioGPS_lecturatomada
    private String causaNoLectura;          // (2) EnvioGPS_causadenolectura
    private String codLector;               // (10) EnvioGPS_CodLector
    private String terminal;                // (15) EnvioGPS_Terminal
    private String periodoLectura;          // (20) EnvioGPS_PERIODOLECTURA - YYYYMM...
    private String longitud;                // (20) EnvioGPS_Longitud
    private String latitud;                 // (20) EnvioGPS_Latitud
    private String nroSatelites;            // (3) EnvioGPS_NroSatelites
    private String informe;                 // (60) EnvioGPS_informe
    private String criticaPda;              // (1) EnvioGPS_criticapda
    private String estadoEnvioOriginal;     // (1) EnvioGPS_EstadoEnvio - R/M/N
    private String comentario;              // (2) EnvioGPS_comentario - TIPO DE MEDIDOR
    private String intentos;                // (1) EnvioGPS_Intentos
    private String fechaHoraSatelite;       // (20) EnvioGPS_FechaHoraSatelite
    private String altitudSatelite;         // (10) EnvioGPS_AltitudSatelite
    private String distancia;               // (10) EnvioGPS_Distancia
    private String lecturaModificada1;      // (11) EnvioGPS_LecturaModificada1
    private String lecturaModificada2;      // (11) EnvioGPS_LecturaModificada2
    private String tiempo;                  // (5) EnvioGPS_Tiempo
    private String consumoFacturado;        // (10) EnvioGPS_ConsumoFacturado
    private String lecturaAnterior;         // (10) EnvioGPS_LecturaAnterior
    private String estrato;                 // (2) EnvioGPS_estrato
    private String uso;                     // (2) EnvioGPS_uso
    private String nombreArchivo;           // (12) EnvioGPS_NombreArchivo

    @Index
    private String idRegistro;              // (11) id - ID del registro en regis_lecturas

    // Campos de estadísticas
    private String totalCausasNoLectura;    // (6) totalcausasnolectura
    private String totalLecturas;           // (6) totalLecturas
    private String totalConsumoBajo;        // (6) totalConsumoBajo
    private String totalConsumoAlto;        // (6) totalConsumoAlto
    private String totalConsumoNormal;      // (6) totalConsumoNormal
    private String totalLecturasIguales;    // (6) totalLecurasIguales
    private String totalLeidas;             // (6) totalLeidas
    private String numObser;                // (6) numObser
    private String numInformes;             // (6) numInformes
    private String totalTiempoProm;         // (12) totalTiempoProm
    private String totalDistanciaProm;      // (12) totalDistanciaProm
    private String codigoSac;               // (3) EnvioGPS_CODIGO_SAC

    // Campos adicionales de EnvioGPS que no se escriben pero se usan
    private String anno;                    // EnvioGPS_anno
    private String mes;                     // EnvioGPS_mes
    private String digitoChequeo;           // EnvioGPS_digitochequeo
    private String idContador;              // EnvioGPS_idcontador
    private String descAnomalia;            // EnvioGPS_descanomalia
    private String descComentario;          // EnvioGPS_desccomentario

    // ===== Campos de control de envío (solo Realm) =====

    @Index
    private int estadoEnvio;                // 0=Pendiente, 1=Enviado, 2=Error
    private String fechaCreacion;           // Fecha de creación del registro en Realm
    private String fechaEnvioApi;           // Fecha de envío exitoso por API
    private int intentosEnvio;              // Intentos de envío por API realizados
    private String errorEnvio;              // Último error de envío

    public EnvioLectura() {
    }

    // ===================== Getters y Setters =====================

    public long getIdRealm() {
        return idRealm;
    }

    public void setIdRealm(long idRealm) {
        this.idRealm = idRealm;
    }

    public String getCiclo() {
        return ciclo != null ? ciclo : "";
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getMunicipio() {
        return municipio != null ? municipio : "";
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSeccion() {
        return seccion != null ? seccion : "";
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getDepartamento() {
        return departamento != null ? departamento : "";
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCuenta() {
        return cuenta != null ? cuenta : "";
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNroContador() {
        return nroContador != null ? nroContador : "";
    }

    public void setNroContador(String nroContador) {
        this.nroContador = nroContador;
    }

    public String getMarcaMedidor() {
        return marcaMedidor != null ? marcaMedidor : "";
    }

    public void setMarcaMedidor(String marcaMedidor) {
        this.marcaMedidor = marcaMedidor;
    }

    public String getFichaCatastral() {
        return fichaCatastral != null ? fichaCatastral : "";
    }

    public void setFichaCatastral(String fichaCatastral) {
        this.fichaCatastral = fichaCatastral;
    }

    public String getFechaHoraLectura() {
        return fechaHoraLectura != null ? fechaHoraLectura : "";
    }

    public void setFechaHoraLectura(String fechaHoraLectura) {
        this.fechaHoraLectura = fechaHoraLectura;
    }

    public String getHoraImpresion() {
        return horaImpresion != null ? horaImpresion : "";
    }

    public void setHoraImpresion(String horaImpresion) {
        this.horaImpresion = horaImpresion;
    }

    public String getLecturaTomada() {
        return lecturaTomada != null ? lecturaTomada : "";
    }

    public void setLecturaTomada(String lecturaTomada) {
        this.lecturaTomada = lecturaTomada;
    }

    public String getCausaNoLectura() {
        return causaNoLectura != null ? causaNoLectura : "";
    }

    public void setCausaNoLectura(String causaNoLectura) {
        this.causaNoLectura = causaNoLectura;
    }

    public String getCodLector() {
        return codLector != null ? codLector : "";
    }

    public void setCodLector(String codLector) {
        this.codLector = codLector;
    }

    public String getTerminal() {
        return terminal != null ? terminal : "";
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getPeriodoLectura() {
        return periodoLectura != null ? periodoLectura : "";
    }

    public void setPeriodoLectura(String periodoLectura) {
        this.periodoLectura = periodoLectura;
    }

    public String getLongitud() {
        return longitud != null ? longitud : "";
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud != null ? latitud : "";
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getNroSatelites() {
        return nroSatelites != null ? nroSatelites : "";
    }

    public void setNroSatelites(String nroSatelites) {
        this.nroSatelites = nroSatelites;
    }

    public String getInforme() {
        return informe != null ? informe : "";
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getCriticaPda() {
        return criticaPda != null ? criticaPda : "";
    }

    public void setCriticaPda(String criticaPda) {
        this.criticaPda = criticaPda;
    }

    public String getEstadoEnvioOriginal() {
        return estadoEnvioOriginal != null ? estadoEnvioOriginal : "";
    }

    public void setEstadoEnvioOriginal(String estadoEnvioOriginal) {
        this.estadoEnvioOriginal = estadoEnvioOriginal;
    }

    public String getComentario() {
        return comentario != null ? comentario : "";
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIntentos() {
        return intentos != null ? intentos : "";
    }

    public void setIntentos(String intentos) {
        this.intentos = intentos;
    }

    public String getFechaHoraSatelite() {
        return fechaHoraSatelite != null ? fechaHoraSatelite : "";
    }

    public void setFechaHoraSatelite(String fechaHoraSatelite) {
        this.fechaHoraSatelite = fechaHoraSatelite;
    }

    public String getAltitudSatelite() {
        return altitudSatelite != null ? altitudSatelite : "";
    }

    public void setAltitudSatelite(String altitudSatelite) {
        this.altitudSatelite = altitudSatelite;
    }

    public String getDistancia() {
        return distancia != null ? distancia : "";
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getLecturaModificada1() {
        return lecturaModificada1 != null ? lecturaModificada1 : "";
    }

    public void setLecturaModificada1(String lecturaModificada1) {
        this.lecturaModificada1 = lecturaModificada1;
    }

    public String getLecturaModificada2() {
        return lecturaModificada2 != null ? lecturaModificada2 : "";
    }

    public void setLecturaModificada2(String lecturaModificada2) {
        this.lecturaModificada2 = lecturaModificada2;
    }

    public String getTiempo() {
        return tiempo != null ? tiempo : "";
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getConsumoFacturado() {
        return consumoFacturado != null ? consumoFacturado : "";
    }

    public void setConsumoFacturado(String consumoFacturado) {
        this.consumoFacturado = consumoFacturado;
    }

    public String getLecturaAnterior() {
        return lecturaAnterior != null ? lecturaAnterior : "";
    }

    public void setLecturaAnterior(String lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public String getEstrato() {
        return estrato != null ? estrato : "";
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public String getUso() {
        return uso != null ? uso : "";
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getNombreArchivo() {
        return nombreArchivo != null ? nombreArchivo : "";
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getIdRegistro() {
        return idRegistro != null ? idRegistro : "";
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getTotalCausasNoLectura() {
        return totalCausasNoLectura != null ? totalCausasNoLectura : "";
    }

    public void setTotalCausasNoLectura(String totalCausasNoLectura) {
        this.totalCausasNoLectura = totalCausasNoLectura;
    }

    public String getTotalLecturas() {
        return totalLecturas != null ? totalLecturas : "";
    }

    public void setTotalLecturas(String totalLecturas) {
        this.totalLecturas = totalLecturas;
    }

    public String getTotalConsumoBajo() {
        return totalConsumoBajo != null ? totalConsumoBajo : "";
    }

    public void setTotalConsumoBajo(String totalConsumoBajo) {
        this.totalConsumoBajo = totalConsumoBajo;
    }

    public String getTotalConsumoAlto() {
        return totalConsumoAlto != null ? totalConsumoAlto : "";
    }

    public void setTotalConsumoAlto(String totalConsumoAlto) {
        this.totalConsumoAlto = totalConsumoAlto;
    }

    public String getTotalConsumoNormal() {
        return totalConsumoNormal != null ? totalConsumoNormal : "";
    }

    public void setTotalConsumoNormal(String totalConsumoNormal) {
        this.totalConsumoNormal = totalConsumoNormal;
    }

    public String getTotalLecturasIguales() {
        return totalLecturasIguales != null ? totalLecturasIguales : "";
    }

    public void setTotalLecturasIguales(String totalLecturasIguales) {
        this.totalLecturasIguales = totalLecturasIguales;
    }

    public String getTotalLeidas() {
        return totalLeidas != null ? totalLeidas : "";
    }

    public void setTotalLeidas(String totalLeidas) {
        this.totalLeidas = totalLeidas;
    }

    public String getNumObser() {
        return numObser != null ? numObser : "";
    }

    public void setNumObser(String numObser) {
        this.numObser = numObser;
    }

    public String getNumInformes() {
        return numInformes != null ? numInformes : "";
    }

    public void setNumInformes(String numInformes) {
        this.numInformes = numInformes;
    }

    public String getTotalTiempoProm() {
        return totalTiempoProm != null ? totalTiempoProm : "";
    }

    public void setTotalTiempoProm(String totalTiempoProm) {
        this.totalTiempoProm = totalTiempoProm;
    }

    public String getTotalDistanciaProm() {
        return totalDistanciaProm != null ? totalDistanciaProm : "";
    }

    public void setTotalDistanciaProm(String totalDistanciaProm) {
        this.totalDistanciaProm = totalDistanciaProm;
    }

    public String getCodigoSac() {
        return codigoSac != null ? codigoSac : "";
    }

    public void setCodigoSac(String codigoSac) {
        this.codigoSac = codigoSac;
    }

    // Campos adicionales

    public String getAnno() {
        return anno != null ? anno : "";
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getMes() {
        return mes != null ? mes : "";
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDigitoChequeo() {
        return digitoChequeo != null ? digitoChequeo : "";
    }

    public void setDigitoChequeo(String digitoChequeo) {
        this.digitoChequeo = digitoChequeo;
    }

    public String getIdContador() {
        return idContador != null ? idContador : "";
    }

    public void setIdContador(String idContador) {
        this.idContador = idContador;
    }

    public String getDescAnomalia() {
        return descAnomalia != null ? descAnomalia : "";
    }

    public void setDescAnomalia(String descAnomalia) {
        this.descAnomalia = descAnomalia;
    }

    public String getDescComentario() {
        return descComentario != null ? descComentario : "";
    }

    public void setDescComentario(String descComentario) {
        this.descComentario = descComentario;
    }

    // Campos de control Realm

    public int getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(int estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getFechaCreacion() {
        return fechaCreacion != null ? fechaCreacion : "";
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaEnvioApi() {
        return fechaEnvioApi != null ? fechaEnvioApi : "";
    }

    public void setFechaEnvioApi(String fechaEnvioApi) {
        this.fechaEnvioApi = fechaEnvioApi;
    }

    public int getIntentosEnvio() {
        return intentosEnvio;
    }

    public void setIntentosEnvio(int intentosEnvio) {
        this.intentosEnvio = intentosEnvio;
    }

    public String getErrorEnvio() {
        return errorEnvio != null ? errorEnvio : "";
    }

    public void setErrorEnvio(String errorEnvio) {
        this.errorEnvio = errorEnvio;
    }

    // ===================== Métodos de utilidad =====================

    /**
     * Obtiene el tipo de medidor (está en comentario, 2 caracteres)
     */
    public String getTipoMedidor() {
        return getComentario().trim();
    }

    /**
     * Extrae el año del periodo de lectura (primeros 4 caracteres)
     */
    public String getAnnoFromPeriodo() {
        String p = getPeriodoLectura().trim();
        if (p.length() >= 4) {
            return p.substring(0, 4);
        }
        return getAnno();
    }

    /**
     * Extrae el mes del periodo de lectura (caracteres 5-6)
     */
    public String getMesFromPeriodo() {
        String p = getPeriodoLectura().trim();
        if (p.length() >= 6) {
            return p.substring(4, 6);
        }
        return getMes();
    }

    // ===================== Constantes de estado =====================

    public static final int ESTADO_PENDIENTE = 0;
    public static final int ESTADO_ENVIADO = 1;
    public static final int ESTADO_ERROR = 2;

    // ===== Identidad de la ruta =====

    public String getClaveRuta() {
        return claveRuta != null ? claveRuta : "";
    }

    public void setClaveRuta(String claveRuta) {
        this.claveRuta = claveRuta;
    }

    public String getModuloTrabajo() {
        return moduloTrabajo != null ? moduloTrabajo : "";
    }

    public void setModuloTrabajo(String moduloTrabajo) {
        this.moduloTrabajo = moduloTrabajo;
    }
}