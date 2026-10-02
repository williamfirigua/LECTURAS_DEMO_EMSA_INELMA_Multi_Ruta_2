package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * SendFilesResponse - Respuesta del servidor para envío de fotos
 * 
 * @author Global Solutions & Service S.A.S.
 */
public class SendFilesResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<FotoResult> data;

    public SendFilesResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FotoResult> getData() {
        return data;
    }

    public void setData(List<FotoResult> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    /**
     * Resultado individual de cada foto
     */
    public static class FotoResult {

        @SerializedName("name")
        private String name;

        @SerializedName("status")
        private String status;  // OK, ERROR, EXIST

        @SerializedName("path")
        private String path;

        @SerializedName("campo")
        private String campo;   // foto_1, foto_2, foto_3

        @SerializedName("error")
        private String error;

        public FotoResult() {
        }

        public String getName() {
            return name != null ? name : "";
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status != null ? status : "";
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPath() {
            return path != null ? path : "";
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getCampo() {
            return campo != null ? campo : "";
        }

        public void setCampo(String campo) {
            this.campo = campo;
        }

        public String getError() {
            return error != null ? error : "";
        }

        public void setError(String error) {
            this.error = error;
        }

        public boolean isSuccess() {
            return "OK".equalsIgnoreCase(status) || "EXIST".equalsIgnoreCase(status);
        }
    }
}
