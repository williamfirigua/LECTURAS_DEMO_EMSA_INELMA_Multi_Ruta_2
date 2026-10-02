package com.gstolima.comunicaciones;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * EnvioCuentaNueva - Modelo Realm para cuentas nuevas pendientes de envío
 *
 * Reemplaza el archivo CUENTASNUEVA{serial}.SDA
 *
 * @author Global Solutions & Service S.A.S.
 */
public class EnvioCuentaNueva extends RealmObject {
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
    private String idRealm;

    // Datos de ubicación
    private String ciclo;
    private String descDepto;
    private String codMunicipio;
    private String codSector;
    private String codRuta;
    private String cicloReal;

    // Datos del cliente/medidor
    private String direccion;
    private String contador;
    private String marca;
    private String tipoMedidor;
    private String digitos;
    private String lectura;
    private String observacion;
    private String informe;
    private String codReferencia;

    // Coordenadas GPS
    private String latitud;
    private String longitud;
    private String altitud;
    private String numSatelites;
    private String fechaHora;

    // Datos del lector/terminal
    private String lector;
    private String serial;

    // Fotos asociadas
    private String foto1;
    private String foto2;
    private String foto3;

    // Nombre del archivo original
    private String nombreArchivo;

    // Control de envío
    private String estadoEnvio;  // P=Pendiente, E=Enviado, X=Error
    private int intentosEnvio;
    private String mensajeError;
    private Date fechaCreacion;
    private Date fechaEnvio;

    public EnvioCuentaNueva() {
        this.idRealm = UUID.randomUUID().toString();
        this.estadoEnvio = "P";
        this.intentosEnvio = 0;
        this.fechaCreacion = new Date();
    }

    // ==================== Getters y Setters ====================

    public String getIdRealm() {
        return idRealm;
    }

    public void setIdRealm(String idRealm) {
        this.idRealm = idRealm;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getDescDepto() {
        return descDepto;
    }

    public void setDescDepto(String descDepto) {
        this.descDepto = descDepto;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getCodSector() {
        return codSector;
    }

    public void setCodSector(String codSector) {
        this.codSector = codSector;
    }

    public String getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(String codRuta) {
        this.codRuta = codRuta;
    }

    public String getCicloReal() {
        return cicloReal;
    }

    public void setCicloReal(String cicloReal) {
        this.cicloReal = cicloReal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipoMedidor() {
        return tipoMedidor;
    }

    public void setTipoMedidor(String tipoMedidor) {
        this.tipoMedidor = tipoMedidor;
    }

    public String getDigitos() {
        return digitos;
    }

    public void setDigitos(String digitos) {
        this.digitos = digitos;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getCodReferencia() {
        return codReferencia;
    }

    public void setCodReferencia(String codReferencia) {
        this.codReferencia = codReferencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getNumSatelites() {
        return numSatelites;
    }

    public void setNumSatelites(String numSatelites) {
        this.numSatelites = numSatelites;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLector() {
        return lector;
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public int getIntentosEnvio() {
        return intentosEnvio;
    }

    public void setIntentosEnvio(int intentosEnvio) {
        this.intentosEnvio = intentosEnvio;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

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