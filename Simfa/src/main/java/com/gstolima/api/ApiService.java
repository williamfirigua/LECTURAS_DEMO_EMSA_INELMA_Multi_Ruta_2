package com.gstolima.api;

import com.gstolima.api.models.LecturaRequest;
import com.gstolima.api.models.LecturaBatchResponse;
import com.gstolima.api.models.LecturaResponse;
import com.gstolima.api.models.SendFilesResponse;
import com.gstolima.api.models.ValidarImeiRequest;
import com.gstolima.api.models.ValidarImeiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * ApiService - Interface Retrofit para comunicación con el servidor Laravel
 *
 * Endpoints:
 * - POST /api/lecturas/sync     → Enviar lecturas
 * - POST /api/fotos/upload      → Subir fotos
 * - GET  /api/ping              → Health check
 *
 * @author Global Solutions & Service S.A.S.
 */
public interface ApiService {

    /**
     * Enviar una lectura al servidor
     * POST /api/lecturas/sync
     */
    @POST("lecturas/sync")
    Call<LecturaResponse> enviarLectura(@Body LecturaRequest lectura);

    /**
     * Enviar múltiples lecturas al servidor
     * POST /api/lecturas/sync-batch
     */
    /**
     * Envio por lote. El backend devuelve un resultado por cada lectura
     * enviada, EN EL MISMO ORDEN (foreach secuencial, sin filtrado ni
     * reordenamiento), por lo que la correlacion es POSICIONAL.
     * Ver LecturaBatchResponse para el detalle del contrato.
     */
    @POST("lecturas/sync-batch")
    Call<LecturaBatchResponse> enviarLecturas(@Body List<LecturaRequest> lecturas);

    /**
     * Subir fotos con metadata
     * POST /api/fotos/upload
     *
     * @param files Archivos de imagen
     * @param idRegistro ID del registro en regis_lecturas
     * @param codCuenta Código de cuenta
     * @param anno Año
     * @param mes Mes
     * @param tipoMedidor Tipo de medidor
     */

    @Multipart
    @POST("fotos/upload")
    Call<SendFilesResponse> uploadFoto(
            @Part MultipartBody.Part foto,
            @Part("cuenta") RequestBody cuenta,
            @Part("tipo_medidor") RequestBody tipoMedidor,
            @Part("id_registro") RequestBody idRegistro,
            @Part("anno") RequestBody anno,
            @Part("mes") RequestBody mes,
            @Part("ciclo") RequestBody ciclo,
            @Part("campo_destino") RequestBody campoDestino,
            @Part("modulo_trabajo") RequestBody moduloTrabajo,
            @Part("ruta_destino") RequestBody rutaDestino
    );

    /**
     * Subir una sola foto
     * POST /api/fotos/upload-single
     */
    @Multipart
    @POST("fotos/upload-single")
    Call<SendFilesResponse> subirFoto(
            @Part MultipartBody.Part file,
            @Part("id_registro") RequestBody idRegistro,
            @Part("cod_cuenta") RequestBody codCuenta,
            @Part("anno") RequestBody anno,
            @Part("mes") RequestBody mes,
            @Part("tipo_medidor") RequestBody tipoMedidor,
            @Part("conteo") RequestBody conteo,
            @Part("campo_destino") RequestBody campoDestino
    );

    /**
     * Verificar conectividad
     * GET /api/ping
     */
    @GET("ping")
    Call<PingResponse> ping();

    /**
     * Obtener zona de trabajo (horario de sincronización)
     * GET /api/zona-trabajo
     *
     * Respuesta: { code: 200, data: { fecha, hora_inicio, hora_fin, actualizado } }
     */
    @GET("zona-trabajo")
    Call<ZonaTrabajoResponse> getZonaTrabajo();

    /**
     * Sincronizar una lectura (usado por LecturaSyncService)
     * POST /api/lecturas/sync
     */
    @POST("lecturas/sync")
    Call<LecturaResponse> syncLectura(@Body LecturaRequest lectura);

    /**
     * Subir una foto (usado por LecturaSyncService)
     * POST /api/fotos/upload
     */
    @Multipart
    @POST("fotos/upload")
    Call<SendFilesResponse> uploadFoto(
            @Part MultipartBody.Part foto,
            @Part("cuenta") RequestBody cuenta,
            @Part("tipo_medidor") RequestBody tipoMedidor,
            @Part("id_registro") RequestBody idRegistro,
            @Part("anno") RequestBody anno,
            @Part("mes") RequestBody mes,
            @Part("ciclo") RequestBody ciclo,
            @Part("campo_destino") RequestBody campoDestino
    );

    /**
     * Sincronizar una cuenta nueva
     * POST /api/cuentas-nuevas/sync
     */
    @POST("cuentas-nuevas/sync")
    Call<LecturaResponse> syncCuentaNueva(@Body com.gstolima.comunicaciones.EnvioCuentaNueva cuenta);

    @POST("licencia/validar-imei")
    Call<ValidarImeiResponse> validarImei(@Body ValidarImeiRequest req);
    /**
     * Respuesta del ping
     */
    class PingResponse {
        public int code;
        public String message;
        public String timestamp;
    }

    /**
     * Respuesta de zona de trabajo
     */
    class ZonaTrabajoResponse {
        public int code;
        public String message;
        public ZonaTrabajoData data;

        public int getCode() { return code; }
        public String getMessage() { return message; }
        public ZonaTrabajoData getData() { return data; }
    }

    /**
     * Datos de zona de trabajo
     */
    class ZonaTrabajoData {
        public String fecha;
        public int hora_inicio;
        public int hora_fin;
        public boolean actualizado;

        public String getFecha() { return fecha; }
        public int getHoraInicio() { return hora_inicio; }
        public int getHoraFin() { return hora_fin; }
        public boolean isActualizado() { return actualizado; }
    }

    /**
     * Getter para ZonaTrabajoResponse
     */
    class ZonaTrabajoResponseHelper {
        public static int getCode(ZonaTrabajoResponse response) {
            return response.code;
        }
        public static ZonaTrabajoData getData(ZonaTrabajoResponse response) {
            return response.data;
        }
    }
}