package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * LecturaResponse - Modelo de respuesta del servidor para sincronización de lecturas
 * 
 * @author Global Solutions & Service S.A.S.
 */
public class LecturaResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ResponseData data;

    public LecturaResponse() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    public ResponseData getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    /**
     * Datos de la respuesta
     */
    public static class ResponseData {
        
        @SerializedName("procesados")
        private int procesados;

        @SerializedName("errores")
        private int errores;

        @SerializedName("resultados")
        private List<ResultadoLectura> resultados;

        public int getProcesados() {
            return procesados;
        }

        public int getErrores() {
            return errores;
        }

        public List<ResultadoLectura> getResultados() {
            return resultados;
        }
    }

    /**
     * Resultado individual de cada lectura procesada
     */
    public static class ResultadoLectura {
        
        @SerializedName("cod_cuenta")
        private String codCuenta;

        @SerializedName("id_registro")
        private String idRegistro;

        @SerializedName("status")
        private String status;  // OK, ERROR, EXIST, NOT_FOUND

        @SerializedName("message")
        private String message;

        @SerializedName("critica")
        private String critica;

        public String getCodCuenta() {
            return codCuenta != null ? codCuenta : "";
        }

        public String getIdRegistro() {
            return idRegistro != null ? idRegistro : "";
        }

        public String getStatus() {
            return status != null ? status : "";
        }

        public String getMessage() {
            return message != null ? message : "";
        }

        public String getCritica() {
            return critica != null ? critica : "";
        }

        public boolean isSuccess() {
            return "OK".equals(status) || "EXIST".equals(status);
        }
    }
}
