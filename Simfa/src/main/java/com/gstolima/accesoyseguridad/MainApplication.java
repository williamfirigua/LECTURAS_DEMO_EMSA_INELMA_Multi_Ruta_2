package com.gstolima.accesoyseguridad;

import android.app.Application;

import com.gstolima.captureException.LogEventos;
import com.gstolima.captureException.myExceptionHandler;
import com.gstolima.comunicaciones.RealmConfig;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Instalado aqui (y no solo en MenuDeLiquidacion) para cubrir crashes en cualquier pantalla,
        // incluida la camara y el arranque tras una muerte del proceso. Va primero para que
        // alcance a registrar cualquier falla del arranque.
        Thread.setDefaultUncaughtExceptionHandler(new myExceptionHandler(this));

        // Antes de que cualquier pantalla o CRUD pida una instancia de Realm: fija la version
        // de esquema y la migracion, para que agregar campos no obligue a desinstalar la app.
        RealmConfig.instalar(this);

        final Application app = this;
        new Thread(() -> LogEventos.registrarSalidasPrevias(app), "log-salidas-previas").start();
    }
}