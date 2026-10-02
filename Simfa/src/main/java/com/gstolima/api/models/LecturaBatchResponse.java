package com.gstolima.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Respuesta de POST /lecturas/sync-batch.
 *
 * Mapea 1:1 lo que devuelve LecturaController@syncBatch:
 *
 *   {
 *     "code": 200,
 *     "message": "Procesados: 48, Errores: 2",
 *     "procesados": 48,
 *     "errores": 2,
 *     "resultados": [
 *       { "success": true, "codCuenta": "90012", "idRegistro": 4471,
 *         "status": "OK", "message": "...", "critica": "1" },
 *       ...
 *     ]
 *   }
 *
 * CORRELACIÓN CON EL LOTE ENVIADO
 * El backend recorre las lecturas con un foreach secuencial y hace exactamente
 * un append por entrada, sin filtrar ni reordenar: resultados[i] corresponde a
 * lecturas[i]. La posición es el único vínculo fiable.
 *
 * NO usar idRegistro como clave: en successResult el backend devuelve el id
 * RESUELTO en base de datos, que puede diferir del enviado (fallback por
 * cuenta/periodo, o terminal que manda 0). En errorResult devuelve el enviado.
 * El mismo campo significa cosas distintas según el resultado.
 *
 * NO usar codCuenta como clave: es un eco fiel de lo enviado, pero no es único
 * dentro del lote — una cuenta puede traer varios tipos de medidor en el mismo
 * periodo y la respuesta no incluye el tipo. Sirve como verificación posicional,
 * no como identificador.
 *
 * @author Global Solutions &amp; Service S.A.S.
 */
public class LecturaBatchResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("procesados")
    private int procesados;

    @SerializedName("errores")
    private int errores;

    @SerializedName("resultados")
    private List<Item> resultados;

    public int getCode()          { return code; }
    public String getMessage()    { return message; }
    public int getProcesados()    { return procesados; }
    public int getErrores()       { return errores; }
    public List<Item> getResultados() { return resultados; }

    /**
     * Resultado individual. Construido por successResult()/errorResult() del
     * backend; ambos devuelven el mismo conjunto de campos.
     */
    public static class Item {

        @SerializedName("success")
        private boolean success;

        @SerializedName("codCuenta")
        private String codCuenta;

        @SerializedName("idRegistro")
        private String idRegistro;

        @SerializedName("status")
        private String status;

        @SerializedName("message")
        private String message;

        @SerializedName("critica")
        private String critica;

        /** true en OK, ALREADY_UPDATED y PROTECTED. false solo en ERROR. */
        public boolean isSuccess()    { return success; }

        /** Eco fiel de la cuenta enviada. Se usa como checksum posicional. */
        public String getCodCuenta()  { return codCuenta != null ? codCuenta : ""; }

        public String getIdRegistro() { return idRegistro != null ? idRegistro : ""; }

        /** OK | ALREADY_UPDATED | PROTECTED | ERROR */
        public String getStatus()     { return status != null ? status : ""; }

        public String getMessage()    { return message != null ? message : ""; }
        public String getCritica()    { return critica != null ? critica : ""; }
    }
}