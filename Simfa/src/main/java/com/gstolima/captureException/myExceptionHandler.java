package com.gstolima.captureException;

import android.content.Context;

/**
 * Registra en LOGEVENTOS toda excepcion no capturada y luego delega al manejador del sistema.
 *
 * Idempotente: MenuDeLiquidacion lo instala en cada onCreate; antes cada instalacion envolvia
 * a la anterior y el mismo crash se escribia N veces.
 */
public class myExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public myExceptionHandler(Context context) {
        this.context = context.getApplicationContext();
        Thread.UncaughtExceptionHandler previo = Thread.getDefaultUncaughtExceptionHandler();
        this.defaultHandler = (previo instanceof myExceptionHandler)
                ? ((myExceptionHandler) previo).defaultHandler
                : previo;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            LogEventos.registrarError(context, "CRASH no capturado en hilo '" + thread.getName() + "'", throwable);
        } catch (Throwable ignored) {
            // Nunca impedir que el crash llegue al manejador del sistema.
        }
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }
}