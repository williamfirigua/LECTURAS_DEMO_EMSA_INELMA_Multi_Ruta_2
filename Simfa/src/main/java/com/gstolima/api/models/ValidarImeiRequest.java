package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request para validar un equipo contra la tabla `moviles`.
 * El servidor valida: cod_movil = imei AND asignado = 1.
 */
public class ValidarImeiRequest {

    @SerializedName("cod_movil")
    public long codMovil;

    public ValidarImeiRequest(long codMovil) {
        this.codMovil = codMovil;
    }
}