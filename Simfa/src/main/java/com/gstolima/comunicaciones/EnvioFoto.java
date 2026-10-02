package com.gstolima.comunicaciones;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Index;

/**
 * EnvioFoto - Modelo Realm para almacenar fotos pendientes de envío
 *
 * Reemplaza el archivo plano F{NombreArchivos}
 * Las fotos se guardan aquí al capturarlas y se envían por API
 *
 * Patrón de nombre: cod_cuenta + "_" + tipomed + "_" + conteo + ".jpg"
 * Ejemplo: 123456_A1_01.jpg, 123456_A1_02.jpg
 *
 * @author Global Solutions & Service S.A.S.
 */
public class EnvioFoto extends RealmObject {
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
    private long id;                    // ID autoincremental local

    @Index
    private String idRegistro;          // ID del registro en regis_lecturas

    @Index
    private String codCuenta;           // Código de cuenta
    private String anno;                // Año
    private String mes;                 // Mes
    private String tipoMedidor;         // Tipo de medidor (A1, B2, etc)
    private String conteo;              // Consecutivo de la foto (01, 02, 03...)
    private String nombreFoto;          // Nombre completo: cod_cuenta_tipomed_conteo.jpg
    private String rutaLocal;           // Ruta completa en el dispositivo
    private String ciclo;               // Ciclo de lectura
    private String lector;              // Código del lector
    private String terminal;            // IMEI del dispositivo

    // Control de envío
    @Index
    private int estadoEnvio;            // 0=Pendiente, 1=Enviado, 2=Error, 3=NoExiste
    private String fechaCreacion;       // Fecha de creación/captura
    private String fechaEnvio;          // Fecha de envío exitoso
    private int intentosEnvio;          // Intentos de envío realizados
    private String errorEnvio;          // Último error de envío

    // Campo para tracking del campo destino en BD
    private String campoDestino;        // foto_1, foto_2, o foto_3

    public EnvioFoto() {
    }

    // ===================== Getters y Setters =====================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdRegistro() {
        return idRegistro != null ? idRegistro : "";
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getCodCuenta() {
        return codCuenta != null ? codCuenta : "";
    }

    public void setCodCuenta(String codCuenta) {
        this.codCuenta = codCuenta;
    }

    public String getAnno() {
        return anno != null ? anno : "";
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getMes() {
        return mes != null ? mes : "";
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getTipoMedidor() {
        return tipoMedidor != null ? tipoMedidor : "";
    }

    public void setTipoMedidor(String tipoMedidor) {
        this.tipoMedidor = tipoMedidor;
    }

    public String getConteo() {
        return conteo != null ? conteo : "01";
    }

    public void setConteo(String conteo) {
        this.conteo = conteo;
    }

    public String getNombreFoto() {
        return nombreFoto != null ? nombreFoto : "";
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getRutaLocal() {
        return rutaLocal != null ? rutaLocal : "";
    }

    public void setRutaLocal(String rutaLocal) {
        this.rutaLocal = rutaLocal;
    }

    public String getCiclo() {
        return ciclo != null ? ciclo : "";
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getLector() {
        return lector != null ? lector : "";
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    public String getTerminal() {
        return terminal != null ? terminal : "";
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(int estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getFechaCreacion() {
        return fechaCreacion != null ? fechaCreacion : "";
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaEnvio() {
        return fechaEnvio != null ? fechaEnvio : "";
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public int getIntentosEnvio() {
        return intentosEnvio;
    }

    public void setIntentosEnvio(int intentosEnvio) {
        this.intentosEnvio = intentosEnvio;
    }

    public String getErrorEnvio() {
        return errorEnvio != null ? errorEnvio : "";
    }

    public void setErrorEnvio(String errorEnvio) {
        this.errorEnvio = errorEnvio;
    }

    public String getCampoDestino() {
        return campoDestino != null ? campoDestino : "";
    }

    public void setCampoDestino(String campoDestino) {
        this.campoDestino = campoDestino;
    }

    // ===================== Métodos de utilidad =====================

    /**
     * Genera el nombre de la foto según el patrón
     * @return nombre en formato: cod_cuenta_tipomed_conteo.jpg
     */
    public String generarNombreFoto() {
        String cuenta = getCodCuenta().trim();
        String tipo = getTipoMedidor().trim();
        String cont = getConteo().trim();

        // Asegurar que el conteo tenga 2 dígitos
        if (cont.length() == 1) {
            cont = "0" + cont;
        }

        return cuenta + "_" + tipo + "_" + cont + ".jpg";
    }

    /**
     * Determina en qué campo de la BD se debe guardar esta foto
     * foto_1 para conteo 01, foto_2 para conteo 02, foto_3 para conteo >= 03
     * @return "foto_1", "foto_2" o "foto_3"
     */
    public String determinarCampoDestino() {
        try {
            int numConteo = Integer.parseInt(getConteo().trim());
            if (numConteo <= 1) {
                return "foto_1";
            } else if (numConteo == 2) {
                return "foto_2";
            } else {
                return "foto_3"; // Para 3 o más, siempre va en foto_3
            }
        } catch (NumberFormatException e) {
            return "foto_1";
        }
    }

    // ===================== Constantes de estado =====================

    public static final int ESTADO_PENDIENTE = 0;
    public static final int ESTADO_ENVIADO = 1;
    public static final int ESTADO_ERROR = 2;
    public static final int ESTADO_NO_EXISTE = 3;  // El archivo no existe en el dispositivo

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