package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;
import com.gstolima.comunicaciones.EnvioLectura;

/**
 * LecturaRequest - Modelo para enviar lecturas al servidor
 *
 * Mapea los campos de EnvioLectura (basado en EnvioGPS) al formato JSON
 * para enviar por API REST
 *
 * @author Global Solutions & Service S.A.S.
 */
public class LecturaRequest {

    // ID local para tracking (no se envía al servidor)
    private transient long idRealmLocal;

    // ===== Campos principales (mismo orden que EnvioGPS) =====

    @SerializedName("ciclo")
    private String ciclo;

    @SerializedName("municipio")
    private String municipio;

    @SerializedName("seccion")
    private String seccion;

    @SerializedName("departamento")
    private String departamento;

    @SerializedName("cuenta")
    private String cuenta;

    @SerializedName("nro_contador")
    private String nroContador;

    @SerializedName("marca_medidor")
    private String marcaMedidor;

    @SerializedName("ficha_catastral")
    private String fichaCatastral;

    @SerializedName("fecha_hora_lectura")
    private String fechaHoraLectura;

    @SerializedName("hora_impresion")
    private String horaImpresion;

    @SerializedName("lectura_tomada")
    private String lecturaTomada;

    @SerializedName("causa_nolectura")
    private String causaNoLectura;

    @SerializedName("cod_lector")
    private String codLector;

    @SerializedName("terminal")
    private String terminal;

    @SerializedName("periodo_lectura")
    private String periodoLectura;

    @SerializedName("longitud")
    private String longitud;

    @SerializedName("latitud")
    private String latitud;

    @SerializedName("nro_satelites")
    private String nroSatelites;

    @SerializedName("informe")
    private String informe;

    @SerializedName("critica_pda")
    private String criticaPda;

    @SerializedName("estado_envio_original")
    private String estadoEnvioOriginal;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("intentos")
    private String intentos;

    @SerializedName("fecha_hora_satelite")
    private String fechaHoraSatelite;

    @SerializedName("altitud_satelite")
    private String altitudSatelite;

    @SerializedName("distancia")
    private String distancia;

    @SerializedName("lectura_modificada1")
    private String lecturaModificada1;

    @SerializedName("lectura_modificada2")
    private String lecturaModificada2;

    @SerializedName("tiempo")
    private String tiempo;

    @SerializedName("consumo_facturado")
    private String consumoFacturado;

    @SerializedName("lectura_anterior")
    private String lecturaAnterior;

    @SerializedName("estrato")
    private String estrato;

    @SerializedName("uso")
    private String uso;

    @SerializedName("nombre_archivo")
    private String nombreArchivo;

    @SerializedName("id")
    private String idRegistro;

    // Estadísticas
    @SerializedName("total_causas_nolectura")
    private String totalCausasNoLectura;

    @SerializedName("total_lecturas")
    private String totalLecturas;

    @SerializedName("total_consumo_bajo")
    private String totalConsumoBajo;

    @SerializedName("total_consumo_alto")
    private String totalConsumoAlto;

    @SerializedName("total_consumo_normal")
    private String totalConsumoNormal;

    @SerializedName("total_lecturas_iguales")
    private String totalLecturasIguales;

    @SerializedName("total_leidas")
    private String totalLeidas;

    @SerializedName("num_obser")
    private String numObser;

    @SerializedName("num_informes")
    private String numInformes;

    @SerializedName("total_tiempo_prom")
    private String totalTiempoProm;

    @SerializedName("total_distancia_prom")
    private String totalDistanciaProm;

    @SerializedName("codigo_sac")
    private String codigoSac;

    // Campos adicionales
    @SerializedName("anno")
    private String anno;

    @SerializedName("mes")
    private String mes;

    @SerializedName("digito_chequeo")
    private String digitoChequeo;

    @SerializedName("id_contador")
    private String idContador;

    @SerializedName("desc_anomalia")
    private String descAnomalia;

    @SerializedName("desc_comentario")
    private String descComentario;

    @SerializedName("tipo_proceso")
    private String tipoProceso; // "L" = Lectura, "E" = Entrega

    @SerializedName("modulo_trabajo")
    private String moduloTrabajo; // "LECTURAMEDIDORES" o "ENTREGAMEDIDORES"
    public LecturaRequest() {
    }

    /**
     * Construye un LecturaRequest desde un EnvioLectura
     */
    public static LecturaRequest fromEnvioLectura(EnvioLectura envio) {
        LecturaRequest req = new LecturaRequest();

        req.idRealmLocal = envio.getIdRealm();

        // Campos principales
        req.ciclo = envio.getCiclo();
        req.municipio = envio.getMunicipio();
        req.seccion = envio.getSeccion();
        req.departamento = envio.getDepartamento();
        req.cuenta = envio.getCuenta();
        req.nroContador = envio.getNroContador();
        req.marcaMedidor = envio.getMarcaMedidor();
        req.fichaCatastral = envio.getFichaCatastral();
        req.fechaHoraLectura = envio.getFechaHoraLectura();
        req.horaImpresion = envio.getHoraImpresion();
        req.lecturaTomada = envio.getLecturaTomada();
        req.causaNoLectura = envio.getCausaNoLectura();
        req.codLector = envio.getCodLector();
        req.terminal = envio.getTerminal();
        req.periodoLectura = envio.getPeriodoLectura();
        req.longitud = envio.getLongitud();
        req.latitud = envio.getLatitud();
        req.nroSatelites = envio.getNroSatelites();
        req.informe = envio.getInforme();
        req.criticaPda = envio.getCriticaPda();
        req.estadoEnvioOriginal = envio.getEstadoEnvioOriginal();
        req.comentario = envio.getComentario();
        req.intentos = envio.getIntentos();
        req.fechaHoraSatelite = envio.getFechaHoraSatelite();
        req.altitudSatelite = envio.getAltitudSatelite();
        req.distancia = envio.getDistancia();
        req.lecturaModificada1 = envio.getLecturaModificada1();
        req.lecturaModificada2 = envio.getLecturaModificada2();
        req.tiempo = envio.getTiempo();
        req.consumoFacturado = envio.getConsumoFacturado();
        req.lecturaAnterior = envio.getLecturaAnterior();
        req.estrato = envio.getEstrato();
        req.uso = envio.getUso();
        req.nombreArchivo = envio.getNombreArchivo();
        req.idRegistro = envio.getIdRegistro();

        // Estadísticas
        req.totalCausasNoLectura = envio.getTotalCausasNoLectura();
        req.totalLecturas = envio.getTotalLecturas();
        req.totalConsumoBajo = envio.getTotalConsumoBajo();
        req.totalConsumoAlto = envio.getTotalConsumoAlto();
        req.totalConsumoNormal = envio.getTotalConsumoNormal();
        req.totalLecturasIguales = envio.getTotalLecturasIguales();
        req.totalLeidas = envio.getTotalLeidas();
        req.numObser = envio.getNumObser();
        req.numInformes = envio.getNumInformes();
        req.totalTiempoProm = envio.getTotalTiempoProm();
        req.totalDistanciaProm = envio.getTotalDistanciaProm();
        req.codigoSac = envio.getCodigoSac();

        // Campos adicionales
        req.anno = envio.getAnno();
        req.mes = envio.getMes();
        req.digitoChequeo = envio.getDigitoChequeo();
        req.idContador = envio.getIdContador();
        req.descAnomalia = envio.getDescAnomalia();
        req.descComentario = envio.getDescComentario();

        return req;
    }

    // ===================== Getters =====================

    public long getIdRealmLocal() {
        return idRealmLocal;
    }

    public String getCiclo() {
        return ciclo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getCuenta() {
        return cuenta;
    }

    public String getNroContador() {
        return nroContador;
    }

    public String getMarcaMedidor() {
        return marcaMedidor;
    }

    public String getFichaCatastral() {
        return fichaCatastral;
    }

    public String getFechaHoraLectura() {
        return fechaHoraLectura;
    }

    public String getHoraImpresion() {
        return horaImpresion;
    }

    public String getLecturaTomada() {
        return lecturaTomada;
    }

    public String getCausaNoLectura() {
        return causaNoLectura;
    }

    public String getCodLector() {
        return codLector;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getPeriodoLectura() {
        return periodoLectura;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getNroSatelites() {
        return nroSatelites;
    }

    public String getInforme() {
        return informe;
    }

    public String getCriticaPda() {
        return criticaPda;
    }

    public String getEstadoEnvioOriginal() {
        return estadoEnvioOriginal;
    }

    public String getComentario() {
        return comentario;
    }

    public String getIntentos() {
        return intentos;
    }

    public String getFechaHoraSatelite() {
        return fechaHoraSatelite;
    }

    public String getAltitudSatelite() {
        return altitudSatelite;
    }

    public String getDistancia() {
        return distancia;
    }

    public String getLecturaModificada1() {
        return lecturaModificada1;
    }

    public String getLecturaModificada2() {
        return lecturaModificada2;
    }

    public String getTiempo() {
        return tiempo;
    }

    public String getConsumoFacturado() {
        return consumoFacturado;
    }

    public String getLecturaAnterior() {
        return lecturaAnterior;
    }

    public String getEstrato() {
        return estrato;
    }

    public String getUso() {
        return uso;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public String getTotalCausasNoLectura() {
        return totalCausasNoLectura;
    }

    public String getTotalLecturas() {
        return totalLecturas;
    }

    public String getTotalConsumoBajo() {
        return totalConsumoBajo;
    }

    public String getTotalConsumoAlto() {
        return totalConsumoAlto;
    }

    public String getTotalConsumoNormal() {
        return totalConsumoNormal;
    }

    public String getTotalLecturasIguales() {
        return totalLecturasIguales;
    }

    public String getTotalLeidas() {
        return totalLeidas;
    }

    public String getNumObser() {
        return numObser;
    }

    public String getNumInformes() {
        return numInformes;
    }

    public String getTotalTiempoProm() {
        return totalTiempoProm;
    }

    public String getTotalDistanciaProm() {
        return totalDistanciaProm;
    }

    public String getCodigoSac() {
        return codigoSac;
    }

    public String getAnno() {
        return anno;
    }

    public String getMes() {
        return mes;
    }

    public String getDigitoChequeo() {
        return digitoChequeo;
    }

    public String getIdContador() {
        return idContador;
    }

    public String getDescAnomalia() {
        return descAnomalia;
    }

    public String getDescComentario() {
        return descComentario;
    }

    public String getTipoProceso() { return tipoProceso; }
    public void setTipoProceso(String tipoProceso) { this.tipoProceso = tipoProceso; }

    public String getModuloTrabajo() { return moduloTrabajo; }
    public void setModuloTrabajo(String moduloTrabajo) { this.moduloTrabajo = moduloTrabajo; }
}
