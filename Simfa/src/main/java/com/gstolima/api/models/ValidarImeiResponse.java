package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Respuesta de validación de equipo contra `moviles`.
 *
 * `autorizado` debe venir true SOLO si existe un registro con
 * cod_movil = imei AND asignado = 1. El cliente decide con ese único campo;
 * `existe` y `asignado` son informativos/diagnóstico (opcionales).
 */
public class ValidarImeiResponse {

    @SerializedName("autorizado")
    public boolean autorizado;

    @SerializedName("existe")
    public Boolean existe;       // opcional (diagnóstico)

    @SerializedName("asignado")
    public Boolean asignado;     // opcional (diagnóstico)

    @SerializedName("mensaje")
    public String mensaje;
}