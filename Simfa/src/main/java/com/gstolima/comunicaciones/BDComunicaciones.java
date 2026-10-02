package com.gstolima.comunicaciones;

import io.realm.RealmObject;

public class BDComunicaciones extends RealmObject {

    private int id;
    private String URL;
    private String paginaWs;
    private String estado;
    private String rutaAdministrador;
    private int httpSeguro;
    private String urlApi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPaginaWs() {
        return paginaWs;
    }

    public void setPaginaWs(String paginaWs) {
        this.paginaWs = paginaWs;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRutaAdministrador() {
        return rutaAdministrador;
    }

    public void setRutaAdministrador(String rutaAdministrador) {
        this.rutaAdministrador = rutaAdministrador;
    }
    public String getUrlApi() {
        String u = urlApi == null ? "" : urlApi.trim();
        return (u.isEmpty() || u.endsWith("/")) ? u : u + "/";
    }

    public void setUrlApi(String urlApi) {
        this.urlApi = urlApi;
    }

    public int gethttpSeguro() {
        return httpSeguro;
    }
    public void sethttpSeguro(int httpSeguro) {
        this.httpSeguro = httpSeguro;
    }
}
