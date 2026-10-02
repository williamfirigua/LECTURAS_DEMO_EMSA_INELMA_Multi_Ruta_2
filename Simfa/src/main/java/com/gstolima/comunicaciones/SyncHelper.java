package com.gstolima.comunicaciones;

import android.util.Log;

import com.gstolima.tablas.EnvioGPS;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * SyncHelper - Clase helper para guardar lecturas y fotos en Realm
 *
 * Esta clase se usa desde MenuDeLiquidacion para registrar las lecturas
 * y fotos en Realm en lugar de los archivos planos.
 *
 * INTEGRACIÓN EN MenuDeLiquidacion:
 *
 * 1. En guardarDatosAEnviarNuevo() DESPUÉS de configurar misenvios y ANTES de escribir_EnvioGPS():
 *    SyncHelper.guardarLecturaDesdeEnvioGPS(misenvios);
 *
 * 2. En el callback de la foto (después de tomar la foto exitosamente):
 *    SyncHelper.registrarFoto(codCuenta, tipoMedidor, idRegistro, rutaFoto, anno, mes, ciclo, lector, terminal);
 *
 * @author Global Solutions & Service S.A.S.
 */
public class SyncHelper {

    private static final String TAG = "SyncHelper";

    /**
     * Guarda una lectura en Realm copiando todos los campos de EnvioGPS
     *
     * Llamar desde MenuDeLiquidacion.guardarDatosAEnviarNuevo() 
     * DESPUÉS de configurar todos los campos de misenvios
     *
     * @param misenvios Objeto EnvioGPS con los datos de la lectura
     * @return true si se guardó correctamente
     */
    public static boolean guardarLecturaDesdeEnvioGPS(EnvioGPS misenvios) {
        try {
            if (misenvios == null) {
                Log.e(TAG, "EnvioGPS es null");
                return false;
            }
            String cuenta = safe(misenvios.getEnvioGPS_CUENTA());
            String anno   = safe(misenvios.getEnvioGPS_ANNO());
            String mes    = safe(misenvios.getEnvioGPS_MES());
            String tipo   = safe(misenvios.getEnvioGPS_COMENTARIO());

            String estadoEnvio = safe(misenvios.getEnvioGPS_ESTADOENVIO());
            boolean esModificacion = "M".equals(estadoEnvio);

            // Si ya existe en Realm, NO se descarta a ciegas.
            //
            // Antes: cualquier lectura no marcada explícitamente como "M" se
            // descartaba devolviendo true. Una corrección que llegara con estado
            // "N" se perdía en silencio: la app reportaba "guardado" y el dato
            // corregido nunca salía del dispositivo.
            //
            // Ahora: solo es duplicado real si el contenido editable es idéntico.
            // Si difiere (o viene marcada M), es una corrección: se reemplaza y
            // se reenvía como PENDIENTE con semántica de modificación.
            if (CrudEnvioLectura.existe(cuenta, anno, mes, tipo)) {
                EnvioLectura nueva = buildEnvioLectura(misenvios);

                if (esModificacion || difiereDeAlmacenada(nueva, cuenta, anno, mes, tipo)) {
                    nueva.setEstadoEnvioOriginal("M"); // semántica de modificación end-to-end
                    Log.i(TAG, "Corrección detectada, reemplazando en Realm cuenta=" + cuenta);
                    return CrudEnvioLectura.reemplazar(cuenta, anno, mes, tipo, nueva);
                }

                Log.i(TAG, "Lectura idéntica ya existe, omitiendo duplicado real: cuenta=" + cuenta);
                return true;
            }

            // Construir y guardar como nueva lectura pendiente
            EnvioLectura lectura = buildEnvioLectura(misenvios);
            boolean resultado = CrudEnvioLectura.insertar(lectura);

            if (resultado) {
                Log.i(TAG, "Lectura guardada en Realm: cuenta=" + lectura.getCuenta() +
                        ", id=" + lectura.getIdRegistro() + ", estado=" + estadoEnvio);
            } else {
                Log.e(TAG, "Error guardando lectura en Realm");
            }

            return resultado;

        } catch (Exception e) {
            Log.e(TAG, "Error en guardarLecturaDesdeEnvioGPS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra una foto en Realm para envío posterior
     *
     * Llamar desde MenuDeLiquidacion después de capturar una foto exitosamente
     *
     * @param codCuenta Código de cuenta
     * @param tipoMedidor Tipo de medidor (A1, B2, etc) - viene de comentario
     * @param idRegistro ID del registro en regis_lecturas
     * @param rutaFoto Ruta completa del archivo de foto
     * @param anno Año
     * @param mes Mes
     * @param ciclo Ciclo de lectura
     * @param lector Código del lector
     * @param terminal IMEI del dispositivo
     * @return true si se registró correctamente
     */
    /**
     * Construye un EnvioLectura mapeando todos los campos de EnvioGPS.
     * Usado por guardarLecturaDesdeEnvioGPS() tanto para inserts como para
     * correcciones (upsert vía reemplazar()).
     */
    private static EnvioLectura buildEnvioLectura(EnvioGPS misenvios) {
        EnvioLectura lectura = new EnvioLectura();

        // Campos de ubicación
        lectura.setCiclo(safe(misenvios.getEnvioGPS_CICLO()));
        lectura.setMunicipio(safe(misenvios.getEnvioGPS_MUNUNICIPIO()));
        lectura.setSeccion(safe(misenvios.getEnvioGPS_SECCION()));
        lectura.setDepartamento(safe(misenvios.getEnvioGPS_DEPARTAMENTO()));

        // Datos de cuenta y medidor
        lectura.setCuenta(safe(misenvios.getEnvioGPS_CUENTA()));
        lectura.setNroContador(safe(misenvios.getEnvioGPS_NROCONTADOR()));
        lectura.setMarcaMedidor(safe(misenvios.getEnvioGPS_marcamedidor()));
        lectura.setFichaCatastral(safe(misenvios.getEnvioGPS_fichacatastral()));

        // Fecha y hora
        lectura.setFechaHoraLectura(safe(misenvios.getEnvioGPS_FECHAYHORALECTURA()));
        lectura.setHoraImpresion(safe(misenvios.getEnvioGPS_HORAIMPRESION()));

        // Lectura y causa
        lectura.setLecturaTomada(safe(misenvios.getEnvioGPS_LECTURATOMADA()));
        lectura.setCausaNoLectura(safe(misenvios.getEnvioGPS_CAUSADENOLECTURA()));

        // Lector y terminal
        lectura.setCodLector(safe(misenvios.getEnvioGPS_CODLECTOR()));
        lectura.setTerminal(safe(misenvios.getEnvioGPS_TERMINAL()));

        // Periodo y coordenadas
        lectura.setPeriodoLectura(safe(misenvios.getEnvioGPS_PERIODOLECTURA()));
        lectura.setLongitud(safe(misenvios.getEnvioGPS_LONGITUD()));
        lectura.setLatitud(safe(misenvios.getEnvioGPS_LATITUD()));
        lectura.setNroSatelites(safe(misenvios.getEnvioGPS_NROSATELITES()));

        // Informe y crítica
        lectura.setInforme(safe(misenvios.getEnvioGPS_informe()));
        lectura.setCriticaPda(safe(misenvios.getEnvioGPS_CRITICAPDA()));
        lectura.setEstadoEnvioOriginal(safe(misenvios.getEnvioGPS_ESTADOENVIO()));

        // Comentario (tipo medidor) e intentos
        lectura.setComentario(safe(misenvios.getEnvioGPS_COMENTARIO()));
        lectura.setIntentos(safe(misenvios.getEnvioGPS_INTENTOS()));

        // GPS satélite
        lectura.setFechaHoraSatelite(safe(misenvios.getEnvioGPS_FECHAHORASATELITE()));
        lectura.setAltitudSatelite(safe(misenvios.getEnvioGPS_ALTITUDSATELITE()));
        lectura.setDistancia(safe(misenvios.getEnvioGPS_DISTANCIA()));

        // Lecturas modificadas
        lectura.setLecturaModificada1(safe(misenvios.getEnvioGPS_LECTURAMODIFICADA1()));
        lectura.setLecturaModificada2(safe(misenvios.getEnvioGPS_LECTURAMODIFICADA2()));

        // Tiempo, consumo, lectura anterior
        lectura.setTiempo(safe(misenvios.getEnvioGPS_Tiempo()));
        lectura.setConsumoFacturado(safe(misenvios.getEnvioGPS_CONSUMOFACTURADO()));
        lectura.setLecturaAnterior(safe(misenvios.getEnvioGPS_LECTURAANTERIOR()));

        // Estrato, uso, archivo
        lectura.setEstrato(safe(misenvios.getEnvioGPS_estrato()));
        lectura.setUso(safe(misenvios.getEnvioGPS_uso()));
        lectura.setNombreArchivo(safe(misenvios.getEnvioGPS_NOMBREARCHIVO()));

        // ID del registro en BD
        lectura.setIdRegistro(safe(misenvios.getId()));

        // Estadísticas
        lectura.setTotalCausasNoLectura(safe(misenvios.getTotalcausasnolectura()));
        lectura.setTotalLecturas(safe(misenvios.getTotalLecturas()));
        lectura.setTotalConsumoBajo(safe(misenvios.getTotalConsumoBajo()));
        lectura.setTotalConsumoAlto(safe(misenvios.getTotalConsumoAlto()));
        lectura.setTotalConsumoNormal(safe(misenvios.getTotalConsumoNormal()));
        lectura.setTotalLecturasIguales(safe(misenvios.getTotalLecurasIguales()));
        lectura.setTotalLeidas(safe(misenvios.getTotalLeidas()));
        lectura.setNumObser(safe(misenvios.getNumObser()));
        lectura.setNumInformes(safe(misenvios.getNumInformes()));
        lectura.setTotalTiempoProm(safe(misenvios.getTotalTiempoProm()));
        lectura.setTotalDistanciaProm(safe(misenvios.getTotalDistanciaProm()));
        lectura.setCodigoSac(safe(misenvios.getEnvioGPS_CODIGO_SAC()));

        // Campos adicionales
        lectura.setAnno(safe(misenvios.getEnvioGPS_ANNO()));
        lectura.setMes(safe(misenvios.getEnvioGPS_MES()));
        lectura.setDigitoChequeo(safe(misenvios.getEnvioGPS_DIGITOCHEQUEO()));
        lectura.setIdContador(safe(misenvios.getEnvioGPS_IDCONTADOR()));
        lectura.setDescAnomalia(safe(misenvios.getEnvioGPS_DESCANOMALIA()));
        lectura.setDescComentario(safe(misenvios.getEnvioGPS_DESCCOMENTARIO()));

        return lectura;
    }

    public static boolean registrarFoto(String codCuenta, String tipoMedidor, String idRegistro,
                                        String rutaFoto, String anno, String mes,
                                        String ciclo, String lector, String terminal) {
        try {
            // Verificar que el archivo existe
            File archivo = new File(rutaFoto);
            if (!archivo.exists()) {
                Log.e(TAG, "Archivo de foto no existe: " + rutaFoto);
                return false;
            }

            // Usar el método del CRUD
            boolean resultado = CrudEnvioFoto.registrarFoto(
                    safe(codCuenta),
                    safe(tipoMedidor),
                    safe(idRegistro),
                    rutaFoto,
                    safe(anno),
                    safe(mes),
                    safe(ciclo),
                    safe(lector),
                    safe(terminal));

            if (resultado) {
                Log.i(TAG, "Foto registrada en Realm: " + rutaFoto);
            } else {
                Log.e(TAG, "Error registrando foto en Realm");
            }

            return resultado;

        } catch (Exception e) {
            Log.e(TAG, "Error en registrarFoto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra una foto usando datos del EnvioGPS actual
     */
    public static boolean registrarFotoDesdeEnvioGPS(EnvioGPS misenvios, String rutaFoto) {
        return registrarFoto(
                misenvios.getEnvioGPS_CUENTA(),
                misenvios.getEnvioGPS_COMENTARIO(),  // tipo medidor
                misenvios.getId(),
                rutaFoto,
                misenvios.getEnvioGPS_ANNO(),
                misenvios.getEnvioGPS_MES(),
                misenvios.getEnvioGPS_CICLO(),
                misenvios.getEnvioGPS_CODLECTOR(),
                misenvios.getEnvioGPS_TERMINAL()
        );
    }

    /**
     * Registra una foto con nombre específico
     */
    public static boolean registrarFotoConNombre(String nombreFoto, String codCuenta, String tipoMedidor,
                                                 String idRegistro, String rutaFoto, String anno, String mes,
                                                 String ciclo, String lector, String terminal) {
        try {
            EnvioFoto foto = new EnvioFoto();
            foto.setCodCuenta(safe(codCuenta));
            foto.setTipoMedidor(safe(tipoMedidor));
            foto.setIdRegistro(safe(idRegistro));
            foto.setRutaLocal(rutaFoto);
            foto.setAnno(safe(anno));
            foto.setMes(safe(mes));
            foto.setCiclo(safe(ciclo));
            foto.setLector(safe(lector));
            foto.setTerminal(safe(terminal));
            foto.setNombreFoto(nombreFoto);

            // Extraer conteo del nombre (formato: cod_tipo_conteo.jpg)
            String conteo = extraerConteoDeNombre(nombreFoto);
            foto.setConteo(conteo);
            foto.setCampoDestino(foto.determinarCampoDestino());

            return CrudEnvioFoto.insertar(foto);

        } catch (Exception e) {
            Log.e(TAG, "Error en registrarFotoConNombre: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el conteo del nombre de la foto
     * Formato: cod_cuenta_tipo_conteo.jpg → retorna "conteo"
     */
    private static String extraerConteoDeNombre(String nombreFoto) {
        try {
            String sinExtension = nombreFoto.replace(".jpg", "").replace(".jpeg", "").replace(".png", "");
            String[] partes = sinExtension.split("_");
            if (partes.length >= 3) {
                return partes[partes.length - 1]; // Último elemento es el conteo
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extrayendo conteo: " + e.getMessage());
        }
        return "01";
    }

    /**
     * Método helper para manejar nulls
     */
    private static String safe(String value) {
        return value != null ? value : "";
    }

    /**
     * Compara el contenido relevante (lo que el operador puede modificar en
     * terreno) de la lectura nueva contra la ya almacenada en Realm.
     *
     * @return true si difiere o si no hay registro previo con qué comparar
     *         (en cuyo caso conviene reemplazar antes que descartar).
     */
    private static boolean difiereDeAlmacenada(EnvioLectura nueva, String cuenta,
                                               String anno, String mes, String tipo) {
        EnvioLectura prev = CrudEnvioLectura.buscarPorCuentaAnnoMesTipo(cuenta, anno, mes, tipo);
        if (prev == null) return true;

        return !eqTrim(prev.getLecturaTomada(),       nueva.getLecturaTomada())
                || !eqTrim(prev.getLecturaModificada1(), nueva.getLecturaModificada1())
                || !eqTrim(prev.getLecturaModificada2(), nueva.getLecturaModificada2())
                || !eqTrim(prev.getCausaNoLectura(),     nueva.getCausaNoLectura())
                || !eqTrim(prev.getCriticaPda(),         nueva.getCriticaPda())
                || !eqTrim(prev.getInforme(),            nueva.getInforme())
                || !eqTrim(prev.getCodigoSac(),          nueva.getCodigoSac());
    }

    private static boolean eqTrim(String a, String b) {
        return safe(a).trim().equals(safe(b).trim());
    }

    // ===================== Métodos de consulta =====================

    /**
     * Verifica si hay lecturas pendientes de envío
     */
    public static boolean hayLecturasPendientes() {
        return CrudEnvioLectura.contarPendientes() > 0;
    }

    /**
     * Verifica si hay fotos pendientes de envío
     */
    public static boolean hayFotosPendientes() {
        return CrudEnvioFoto.contarPendientes() > 0;
    }

    /**
     * Obtiene el conteo de pendientes
     * @return int[0]=lecturas, int[1]=fotos
     */
    public static int[] contarPendientes() {
        return new int[]{
                CrudEnvioLectura.contarPendientes(),
                CrudEnvioFoto.contarPendientes()
        };
    }

    /**
     * Obtiene estadísticas completas
     * @return String con resumen de estadísticas
     */
    public static String getEstadisticas() {
        int[] lecStats = CrudEnvioLectura.getEstadisticas();
        int[] fotoStats = CrudEnvioFoto.getEstadisticas();

        return String.format(Locale.US,
                "Lecturas: %d pendientes, %d enviadas, %d errores\n" +
                        "Fotos: %d pendientes, %d enviadas, %d errores, %d no encontradas",
                lecStats[0], lecStats[1], lecStats[2],
                fotoStats[0], fotoStats[1], fotoStats[2], fotoStats[3]);
    }

    // ===================== Métodos de mantenimiento =====================

    /**
     * Limpia registros ya enviados (para mantenimiento)
     */
    public static void limpiarEnviados() {
        int lecturasLimpiadas = CrudEnvioLectura.limpiarEnviados();
        int fotosLimpiadas = CrudEnvioFoto.limpiarEnviados();
        Log.i(TAG, "Limpieza: " + lecturasLimpiadas + " lecturas, " + fotosLimpiadas + " fotos");
    }

    /**
     * Reintenta envíos fallidos
     */
    public static void reintentarFallidos() {
        int lecturas = CrudEnvioLectura.reintentar(ModoReenvio.SOLO_ERROR);
        int fotos = CrudEnvioFoto.reintentar(ModoReenvio.SOLO_ERROR);
        Log.i(TAG, "Reintento: " + lecturas + " lecturas, " + fotos + " fotos puestas en pendiente");
    }

    /**
     * Guarda una foto para envío desde la recuperación de ValidarNoEnviados
     *
     * @param rutaLocal Ruta completa del archivo de foto
     * @param cuenta Código de cuenta
     * @param tipoMedidor Tipo de medidor
     * @param idRegistro ID del registro en BD
     * @param anno Año
     * @param mes Mes
     * @param ciclo Ciclo
     * @param campoDestino foto_1, foto_2 o foto_3
     * @return true si se guardó correctamente
     */
    public static boolean guardarFotoParaEnvio(String rutaLocal, String cuenta,
                                               String tipoMedidor, String idRegistro, String anno, String mes,
                                               String ciclo, String campoDestino) {

        // Verificar si ya existe en Realm
        if (CrudEnvioFoto.existeConRuta(rutaLocal)) {
            Log.d("SyncHelper", "Foto ya existe en Realm: " + rutaLocal);
            return true; // Ya existe, no duplicar
        }

        // Crear EnvioFoto
        EnvioFoto foto = new EnvioFoto();
        foto.setRutaLocal(rutaLocal);
        foto.setCodCuenta(cuenta);
        foto.setTipoMedidor(tipoMedidor);
        foto.setIdRegistro(idRegistro);
        foto.setAnno(anno);
        foto.setMes(mes);
        foto.setCiclo(ciclo);
        foto.setCampoDestino(campoDestino);
        foto.setEstadoEnvio(EnvioFoto.ESTADO_PENDIENTE);
        foto.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                .format(new Date()));

        // Guardar en Realm
        return CrudEnvioFoto.guardar(foto);
    }
}