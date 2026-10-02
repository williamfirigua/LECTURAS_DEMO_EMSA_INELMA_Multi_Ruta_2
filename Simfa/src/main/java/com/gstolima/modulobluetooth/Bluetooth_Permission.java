package com.gstolima.modulobluetooth;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Ax: SOLO verifica permisos de Bluetooth - NO los solicita.
 *
 * La solicitud real (ActivityCompat.requestPermissions) ya ocurre una única
 * vez, en cascada, en ActLicencia.comprobarPermisos() al arrancar la app
 * (CODIGO_PERMISOS_BLUETOOHSCN / CODIGO_PERMISOS_BLUETOOHCNT), antes de que
 * el operario llegue a GuiAcceso. Duplicar esa solicitud aquí generaría un
 * segundo flujo de consentimiento desincronizado del que ya tiene la app,
 * y un onRequestPermissionsResult adicional compitiendo con el de ActLicencia.
 *
 * Si el permiso falta al llegar a GuiAcceso, algo falló en el onboarding
 * (el operario lo negó en ActLicencia, o lo revocó luego desde Ajustes):
 * se informa y se redirige a Ajustes de la app, no se vuelve a pedir aquí.
 */
public final class Bluetooth_Permission {

    private Bluetooth_Permission() {
    }

    public static boolean check(Activity activity) {
        for (String permiso : permisosRequeridos()) {
            if (ContextCompat.checkSelfPermission(activity, permiso) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static String[] permisosRequeridos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
            return new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }
        return new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }
}
