package com.gstolima.comunicaciones;

/**
 * Modo de reencolado para los métodos reintentar() de los Crud de envío.
 *
 * Sustituye al parámetro entero mágico (1 = todas, 2 = solo error) que se usaba
 * antes. Con enteros, dos clases del mismo paquete llegaron a interpretar los
 * valores al revés — CrudEnvioLectura preguntaba "if (Tipo == 2)" y
 * CrudEnvioFoto "if (Tipo == 1)" — y el call site no decía nada:
 * reintentarErrores(1) es indistinguible de reintentarErrores(2) al leerlo.
 *
 * @author Global Solutions &amp; Service S.A.S.
 */
public enum ModoReenvio {

    /**
     * Solo los registros en estado ERROR vuelven a PENDIENTE.
     * Es el modo seguro y el que debe usarse por defecto.
     */
    SOLO_ERROR,

    /**
     * TODOS los registros vuelven a PENDIENTE, incluidos los ya enviados
     * correctamente.
     *
     * Operación destructiva en volumen: reencola el histórico completo del
     * dispositivo y lo reenvía al servidor. Reservado para recuperación
     * (p. ej. el servidor perdió un periodo). Confirmar siempre con el
     * operador mostrando el conteo real antes de invocarlo.
     */
    TODAS
}