package com.gstolima.comunicaciones;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by a on 09/12/2016.
 */

public class Autoupdater {
    /**
     * Objeto contexto para ejecutar el instalador.
     * Se puede buscar otra forma mas "limpia".
     */
    Context context;
    String rutavrs;


    /**
     * Listener que se llamara despues de ejecutar algun AsyncTask.
     */
    Runnable listener;


    /**
     * El código de versión establecido en el AndroidManifest.xml de la versión
     * instalada de la aplicación. Es el valor numérico que usa Android para
     * diferenciar las versiones.
     */
    private int currentVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la versión
     * instalada. Es la cadena de texto que se usa para identificar al versión
     * de cara al usuario.
     */
    private String currentVersionName;

    /**
     * El código de versión establecido en el AndroidManifest.xml de la última
     * versión disponible de la aplicación.
     */
    private int latestVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la última
     * versión disponible.
     */
    private String latestVersionName;

    /**
     * Enlace de descarga directa de la última versión disponible.
     */
    private String downloadURL;

    /**
     * Constructor unico.
     *
     * @param context Contexto sobre el cual se ejecutara el Instalador.
     */
    public Autoupdater(Context context,String rutavrs) {
        this.context = context;
        this.rutavrs = rutavrs;
    }


    /**
     * Método para comparar la versión actual con la última .
     *
     * @return true si hay una versión más nueva disponible que la actual.
     */
    public boolean isNewVersionAvailable() {
        Log.e("return", getLatestVersionCode() + " " + getCurrentVersionCode());
        return getLatestVersionCode() > getCurrentVersionCode();
    }

    /**
     * Devuelve el código de versión actual.
     *
     * @return integer con la version actual
     */
    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    /**
     * Devuelve el nombre de versión actual.
     *
     * @return IDEM
     */
    public String getCurrentVersionName() {
        return currentVersionName;
    }

    /**
     * Devuelve el código de la última versión disponible.
     *
     * @return IDEM
     */
    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    /**
     * Devuelve el nombre de la última versión disponible.
     *
     * @return IDEM
     */
    public String getLatestVersionName() {
        return latestVersionName;
    }



    /**
     * Metodo de Interface.
     * Segundo Metodo a usar.
     * Se encargara, una vez obtenidos los datos de la version mas reciente, y en un hilo separado,
     * de comprobar que haya efectivamente una version mas reciente, descargarla e instalarla.
     * Preparar la aplicacion para ser cerrada y desinstalada despues de este metodo.
     *
     * @param OnFinishRunnable Codigo que se ejecutara tras llamar al instalador.
     *                         Ultimo en ejecutar.
     */
    public void InstallNewVersion(Runnable OnFinishRunnable) {

        listener = OnFinishRunnable;
        downloadInstaller.execute("");
    }



    /**
     * Objeto de AsyncTask encargado de descargar e instalar la ultima version de la aplicacion.
     * No es cancelable.
     */
    private AsyncTask<String, Integer, Intent> downloadInstaller = new AsyncTask<String, Integer, Intent>() {
        @Override
        protected Intent doInBackground(String... strings) {
            try {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/Simfa.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
                //return intent;
                File apk = new File(rutavrs);
                if (!apk.exists()) {
                    Log.e("Autoupdater", "APK no encontrado en: " + rutavrs);
                    return null;
                }


              //  File apk = new File(rutavrs);
                Uri fileUri = FileProvider.getUriForFile(context,"com.gstolima.accesoyseguridad" + ".fileprovider",apk);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                //intent.setDataAndType(fileUri,"application/vnd.android.package-archive");
                intent.setDataAndType(fileUri,"application/vnd.android.package-archive");
                context.startActivity(intent);
            } catch (Exception e) {

                Log.e("error!", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            super.onPostExecute(intent);
            if (intent != null) {
                context.startActivity(intent);  // <- MUÉVELO AQUÍ
            }
            if (listener != null) listener.run();
            listener = null;
            /*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/Simfa");
            File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "Simfa"+getCurrentVersionCode());
            if(file.exists()){
                file.delete();
            }
            file2.delete();*/
        }
    };


}
