package com.gstolima.comunicaciones;

/**
 * Calidad de la posición GPS con la que se guardó una lectura (fase 1 del GPS).
 *
 * Viaja de MenuDeLiquidacion a EnvioLectura (Realm) sin pasar por el archivo
 * EnvioGPS, cuyo formato no cambia. Todos los campos son opcionales: null
 * significa "no disponible" y el servidor deja la columna en NULL.
 *
 * Los setters descartan NaN e infinitos: Gson no los serializa y harían fallar
 * el envío de todo el lote.
 */
public class PosicionCalidad {

    public static final String FUENTE_GPS = "GPS";
    public static final String FUENTE_RED = "RED";
    public static final String FUENTE_ULTIMA_CONOCIDA = "ULTIMA_CONOCIDA";
    public static final String FUENTE_PREDIO = "PREDIO";
    public static final String FUENTE_SIN_POSICION = "SIN_POSICION";
    /** Cierre masivo de no leídas: posición del técnico, no medida en el predio. */
    public static final String FUENTE_CIERRE_RUTA = "CIERRE_RUTA";
    /** Guardado de otro registro (no el de pantalla) con la posición actual. */
    public static final String FUENTE_REGISTRO_PREVIO = "REGISTRO_PREVIO";
    /** Navegación por pasos desde el último fix GPS bueno (fase 2). */
    public static final String FUENTE_PDR = "PDR";

    private Float precisionH;
    private Float precisionV;
    private Double altitud;
    private Integer satelitesUsados;
    private Integer edadFixSeg;
    private String fuente;
    private Boolean simulada;
    private Double latPromedio;
    private Double lngPromedio;
    private Float precisionPromedio;
    private Integer nFixes;

    public static PosicionCalidad soloFuente(String fuente) {
        PosicionCalidad c = new PosicionCalidad();
        c.fuente = fuente;
        return c;
    }

    private static Float finito(Float v) {
        return v != null && !v.isNaN() && !v.isInfinite() ? v : null;
    }

    private static Double finito(Double v) {
        return v != null && !v.isNaN() && !v.isInfinite() ? v : null;
    }

    public Float getPrecisionH() { return precisionH; }
    public void setPrecisionH(Float v) { precisionH = finito(v); }

    public Float getPrecisionV() { return precisionV; }
    public void setPrecisionV(Float v) { precisionV = finito(v); }

    public Double getAltitud() { return altitud; }
    public void setAltitud(Double v) { altitud = finito(v); }

    public Integer getSatelitesUsados() { return satelitesUsados; }
    public void setSatelitesUsados(Integer v) { satelitesUsados = v; }

    public Integer getEdadFixSeg() { return edadFixSeg; }
    public void setEdadFixSeg(Integer v) { edadFixSeg = v; }

    public String getFuente() { return fuente; }
    public void setFuente(String v) { fuente = v; }

    public Boolean getSimulada() { return simulada; }
    public void setSimulada(Boolean v) { simulada = v; }

    public Double getLatPromedio() { return latPromedio; }
    public Double getLngPromedio() { return lngPromedio; }

    public void setPromedio(Double lat, Double lng, Float precision, Integer n) {
        lat = finito(lat);
        lng = finito(lng);
        if (lat == null || lng == null) {
            latPromedio = null;
            lngPromedio = null;
            precisionPromedio = null;
        } else {
            latPromedio = lat;
            lngPromedio = lng;
            precisionPromedio = finito(precision);
        }
        nFixes = n;
    }

    public Float getPrecisionPromedio() { return precisionPromedio; }
    public Integer getNFixes() { return nFixes; }
}
