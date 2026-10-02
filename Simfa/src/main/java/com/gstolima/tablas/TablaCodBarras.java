package com.gstolima.tablas;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class TablaCodBarras {
    String sep = "|";
    String tablaCodBarras_CUENTA;
    String tablaCodBarras_CODBARRAS;
    String tablaCodBarras_fechahoralectura;
    String tablaCodBarras_horalectura;
    String tablaCodBarras_cordenadax;
    String tablaCodBarras_cordenaday;
    String tablaCodBarras_LECTOR;
    String tablaCodBarras_OBSERVACION;
    String id;
    String tablaCodBarras_fin;

    public static int getLongitudRegistro() {
        return LONGITUD_REGISTRO;
    }

    public String gettablaCodBarras_CUENTA() {
        return tablaCodBarras_CUENTA;
    }
    public void settablaCodBarras_CUENTA(String tablaCodBarras_CUENTA) {
        this.tablaCodBarras_CUENTA = tablaCodBarras_CUENTA;
    }

    public String gettablaCodBarras_CODBARRAS() {
        return tablaCodBarras_CODBARRAS;
    }
    public void settablaCodBarras_CODBARRAS(String tablaCodBarras_CODBARRAS) {
        this.tablaCodBarras_CODBARRAS = tablaCodBarras_CODBARRAS;
    }

    public String gettablaCodBarras_FECHAHORALECTURA() {
        return tablaCodBarras_fechahoralectura;
    }
    public void settablaCodBarras_fechahoralectura(String tablaCodBarras_fechahoralectura) {
        this.tablaCodBarras_fechahoralectura = tablaCodBarras_fechahoralectura;
    }

    /*public String gettablaCodBarras_HORALECTURA() {
        return tablaCodBarras_horalectura;
    }
    public void settablaCodBarras_horalectura(String tablaCodBarras_horalectura) {
        this.tablaCodBarras_horalectura = tablaCodBarras_horalectura;
    }*/

    public String gettablaCodBarras_LECTOR() {
        return tablaCodBarras_LECTOR;
    }
    public void settablaCodBarras_LECTOR(String tablaCodBarras_LECTOR) {
        this.tablaCodBarras_LECTOR = tablaCodBarras_LECTOR;
    }

    public String gettablaCodBarras_OBSERVACION() {
        return tablaCodBarras_OBSERVACION;
    }
    public void settablaCodBarras_OBSERVACION(String tablaCodBarras_OBSERVACION) {
        this.tablaCodBarras_OBSERVACION = tablaCodBarras_OBSERVACION;
    }

    public String gettablaCodBarras_CORDENADAX() {
        return tablaCodBarras_cordenadax;
    }
    public void settablaCodBarras_cordenadax(String tablaCodBarras_cordenadax) {
        this.tablaCodBarras_cordenadax = tablaCodBarras_cordenadax;
    }

    public String gettablaCodBarras_CORDENADAY() {
        return tablaCodBarras_cordenaday;
    }
    public void settablaCodBarras_cordenaday(String tablaCodBarras_cordenaday) {
        this.tablaCodBarras_cordenaday = tablaCodBarras_cordenaday;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String gettablaCodBarras_FIN() {
        return tablaCodBarras_fin;
    }
    public void settablaCodBarras_fin(String tablaCodBarras_fin) {
        this.tablaCodBarras_fin = tablaCodBarras_fin;
    }

    public int getTotal_TablaCodBarras() {
        return total_TablaCodBarras;
    }
    public void setTotal_TablaCodBarras(int total_TablaCodBarras) {
        this.total_TablaCodBarras = total_TablaCodBarras;
    }

    public String getArchivo_TablaCodBarras() {
        return archivo_TablaCodBarras;
    }
    public void setArchivo_TablaCodBarras(String archivo_TablaCodBarras) {
        this.archivo_TablaCodBarras = archivo_TablaCodBarras;
    }

    public int getEncontro_TablaCodBarras() {
        return encontro_TablaCodBarras;
    }
    public void setEncontro_TablaCodBarras(int encontro_TablaCodBarras) {
        this.encontro_TablaCodBarras = encontro_TablaCodBarras;
    }


    BufferedReader fin;
    byte[] byteArray;
    private String archivo_TablaCodBarras;
    private static final int LONGITUD_REGISTRO = 112;//
    private int total_TablaCodBarras;
    int ultimo_TablaCodBarras;
    private int encontro_TablaCodBarras;
    String buscar_TablaCodBarras;
    String texto;
    RandomAccessFile rFile;


    public Boolean abrir_TablaCodBarras(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //MANEJA LOS CODIGOS DE BARRAS
        } catch (FileNotFoundException e) {
            Log.e("error", "entra al metodo eeeeee " + e.getMessage());

            e.printStackTrace();
        }
        setArchivo_TablaCodBarras(nombreArchivo);
        return abrir(getArchivo_TablaCodBarras());
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            setTotal_TablaCodBarras(fileSize / LONGITUD_REGISTRO);
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_TablaCodBarras() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribir_TablaCodBarras(int posicion) {
        Log.e("error", " total " + getTotal_TablaCodBarras());

        if (posicion <= getTotal_TablaCodBarras()) {
            posicion_TablaCodBarras(posicion, LONGITUD_REGISTRO);
            rellenar_TablaCodBarras();
            String texto = tablaCodBarras_CUENTA + sep +
                    tablaCodBarras_CODBARRAS + sep +
                    tablaCodBarras_fechahoralectura + sep +
                    tablaCodBarras_cordenadax + sep + tablaCodBarras_cordenaday + sep +
                    tablaCodBarras_LECTOR + sep + tablaCodBarras_OBSERVACION + sep +
                    id + sep + tablaCodBarras_fin;//tablaCodBarras_horalectura + sep +
            try {
                Log.e("error", texto + " graba " + LONGITUD_REGISTRO);
                if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                    rFile.writeBytes(texto);
                    Log.e("error", "Los Datos fueron grabados correctamente");
                    return true;
                } else {
                    Log.e("error", "Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                    return false;
                }
            } catch (Exception ioe) {
                Log.e("error", "Se presento problema al escribir en el archivo Longitud Errada: " + ioe.getMessage() + LONGITUD_REGISTRO);
                return false;
            }
        } else {
            return false;
        }
    }

    public void rellenar_TablaCodBarras() {
        try {
            tablaCodBarras_CODBARRAS = String.format("%-10s", tablaCodBarras_CODBARRAS);
            tablaCodBarras_fechahoralectura = String.format("%-19s", tablaCodBarras_fechahoralectura);
            //tablaCodBarras_horalectura = String.format("%-6s", tablaCodBarras_horalectura);
            tablaCodBarras_cordenadax = String.format("%20s", tablaCodBarras_cordenadax);
            tablaCodBarras_cordenaday = String.format("%20s", tablaCodBarras_cordenaday);
            tablaCodBarras_LECTOR = String.format("%-10s", tablaCodBarras_LECTOR);
            tablaCodBarras_OBSERVACION = String.format("%-2s", tablaCodBarras_OBSERVACION);
            tablaCodBarras_fin = String.format("%-2s", tablaCodBarras_fin);
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo sregist.dat..");
            e.printStackTrace();
        }
        return;
    }

    public void posicion_TablaCodBarras(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_TablaCodBarras(int registro) {
        setEncontro_TablaCodBarras(0);
        posicion_TablaCodBarras(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            settablaCodBarras_CUENTA(texto.substring(0, 10));
            settablaCodBarras_CODBARRAS(texto.substring(11, 21));
            settablaCodBarras_fechahoralectura(texto.substring(22, 41));
            //settablaCodBarras_horalectura(texto.substring(41, 36));
            settablaCodBarras_cordenadax(texto.substring(42, 62));
            settablaCodBarras_cordenaday(texto.substring(63, 83));
            settablaCodBarras_LECTOR(texto.substring(84, 94));
            settablaCodBarras_OBSERVACION(texto.substring(95, 97));
            setId(texto.substring(98, 109));
            settablaCodBarras_fin(texto.substring(110, 112));

            ultimo_TablaCodBarras = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lectura_Minima_TablaCodBarras(int registro) {

        setEncontro_TablaCodBarras(0);
        posicion_TablaCodBarras(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);

            ultimo_TablaCodBarras = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------Ax : Bloque creado para Acelerar resumen....
    public void inicializaBloque() {
        try {
            File filepaths = new File(getArchivo_TablaCodBarras());
            rdr = new LineNumberReader(new FileReader(filepaths));
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    public LineNumberReader rdr;


    public void terminaBloque() {
        try {
            rdr.close();
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

}
