package com.gstolima.comunicaciones;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * General - Modelo Realm para configuración general del dispositivo.
 *
 * Almacena estado de permiso de trabajo (control de versión) y otros
 * parámetros de uso global que no pertenecen a comunicaciones ni a
 * la tabla de parámetros del servidor.
 *
 * Siempre se usa el registro con id=1.
 *
 * Campos:
 *   permisoTrabajo   "S" = puede trabajar | "N" = bloqueado por versión
 *   versionBloqueada versión de BD que originó el bloqueo, ej: "Ver5.4 26.01.21"
 *   versionLocal     versión del APK instalado al momento de la última verificación
 *   fechaVerificacion fecha/hora de la última consulta exitosa al servidor (yyyy-MM-dd HH:mm)
 *
 * @author Global Solutions & Service S.A.S.
 */
public class General extends RealmObject {

    @PrimaryKey
    private int id;

    /** "S" = puede trabajar, "N" = bloqueado por versión desactualizada */
    private String permisoTrabajo;

    /** Versión vigente en BD que originó el bloqueo, ej: "Ver5.4 26.01.21" */
    private String versionBloqueada;

    /** Versión del APK local al momento de la última verificación */
    private String versionLocal;

    /** Fecha/hora de la última verificación exitosa contra el servidor */
    private String fechaVerificacion;

    public General() {
    }

    // ===== Getters y Setters =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPermisoTrabajo() {
        return permisoTrabajo != null ? permisoTrabajo : "S";
    }
    public void setPermisoTrabajo(String permisoTrabajo) {
        this.permisoTrabajo = permisoTrabajo;
    }

    public String getVersionBloqueada() {
        return versionBloqueada != null ? versionBloqueada : "";
    }
    public void setVersionBloqueada(String versionBloqueada) {
        this.versionBloqueada = versionBloqueada;
    }

    public String getVersionLocal() {
        return versionLocal != null ? versionLocal : "";
    }
    public void setVersionLocal(String versionLocal) {
        this.versionLocal = versionLocal;
    }

    public String getFechaVerificacion() {
        return fechaVerificacion != null ? fechaVerificacion : "";
    }
    public void setFechaVerificacion(String fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }
}
