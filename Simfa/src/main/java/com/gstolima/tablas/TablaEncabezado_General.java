package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TablaEncabezado_General {
    String sep = "|";
    String General_ciclo;
    String General_municipio;
    String General_seccion;
    String General_LECTOR;
    String General_SUPERVISOR;
    String General_CLAVE_SUPERVISOR;
    String General_TERMINAL;
    String General_VARIOS;
    String General_ObligaCBarras;
    String General_fechaInicial;
    String General_fechaFinal;
    String General_obligaFirma;
    String General_CRNL;

    public String getGeneral_CICLO() {
        return General_ciclo;
    }

    public void setGeneral_ciclo(String General_ciclo) {
        this.General_ciclo = General_ciclo;
    }

    public String getGeneral_MUNICIPIO() {
        return General_municipio;
    }

    public void setGeneral_municipio(String General_municipio) {
        this.General_municipio = General_municipio;
    }

    public String getGeneral_SECCION() {
        return General_seccion;
    }

    public void setGeneral_seccion(String General_seccion) {
        this.General_seccion = General_seccion;
    }

    public String getGeneral_LECTOR() {
        return General_LECTOR;
    }

    public void setGeneral_LECTOR(String General_LECTOR) {
        this.General_LECTOR = General_LECTOR;
    }

    public String getGeneral_SUPERVISOR() {
        return General_SUPERVISOR;
    }

    public void setGeneral_SUPERVISOR(String General_SUPERVISOR) {
        this.General_SUPERVISOR = General_SUPERVISOR;
    }

    public String getGeneral_CLAVE_SUPERVISOR() {
        return General_CLAVE_SUPERVISOR;
    }

    public void setGeneral_CLAVE_SUPERVISOR(String General_CLAVE_SUPERVISOR) {
        this.General_CLAVE_SUPERVISOR = General_CLAVE_SUPERVISOR;
    }

    public String getGeneral_TERMINAL() {
        return General_TERMINAL;
    }

    public void setGeneral_TERMINAL(String General_TERMINAL) {
        this.General_TERMINAL = General_TERMINAL;
    }

    public String getGeneral_VARIOS() {
        return General_VARIOS;
    }

    public void setGeneral_VARIOS(String General_VARIOS) {
        this.General_VARIOS = General_VARIOS;
    }

    public String getGeneral_OBLIGACBARRAS() {
        return General_ObligaCBarras;
    }

    public void setGeneral_ObligaCBarras(String General_ObligaCBarras) {
        this.General_ObligaCBarras = General_ObligaCBarras;
    }

    public String getGeneral_fechaInicial() {
        return General_fechaInicial;
    }

    public void setGeneral_fechaInicial(String general_fechaInicial) {
        General_fechaInicial = general_fechaInicial;
    }

    public String getGeneral_fechaFinal() {
        return General_fechaFinal;
    }

    public void setGeneral_fechaFinal(String general_fechaFinal) {
        General_fechaFinal = general_fechaFinal;
    }

    public String getGeneral_obligaFirma() {
        return General_obligaFirma;
    }

    public void setGeneral_obligaFirma(String general_obligaFirma) {
        General_obligaFirma = general_obligaFirma;
    }

    public String getGeneral_CRNL() {
        return General_CRNL;
    }

    public void setGeneral_CRNL(String General_CRNL) {
        this.General_CRNL = General_CRNL;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_General;

    public String getarchivo_General() {
        return archivo_General;
    }

    public void setarchivo_General(String archivo_General) {
        this.archivo_General = archivo_General;
    }

    static final int LONGITUD_REGISTRO = 81;//se le sumaran dos para incluir campo nuevo
    int total_General;

    public int gettotal_General() {
        return total_General;
    }

    public void settotal_General(int total_General) {
        this.total_General = total_General;
    }

    int ultimo_General;
    int encontro_General;

    public int getencontro_General() {
        return encontro_General;
    }

    public void setencontro_General(int encontro_General) {
        this.encontro_General = encontro_General;
    }

    String buscar_General;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;

    public Boolean abrir_General(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
           // Log.e("Ficheros2", e.getMessage());
            e.printStackTrace();
        }
        //Log.e("error", "fivher2");

        archivo_General = nombreArchivo;
        return abrir(archivo_General);
    }


    private Boolean abrir(String nombre_archivo) {
        //Log.e("error", "fivher3");
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_General = fileSize / LONGITUD_REGISTRO;
            //Log.e("error", "fivher4");
            return true;
        } catch (Exception ex) {
            //Log.e("error", "fivher"+ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_General() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void posicion_General(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (Exception e) {
          //  Log.e("error","no lee "+e.getMessage());
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_General(int registro) {
        String[] campos;
        encontro_General = 0;
      //  Log.e("error","dato0 ");
        posicion_General(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
           // Log.e("error","dato ");
            setGeneral_ciclo(texto.substring(0, 3));
            setGeneral_municipio(texto.substring(4, 6));// ruta con foto (01 tomar fotos, 00 no tomar fotos)
            setGeneral_seccion(texto.substring(7, 10));// manejo zona roja (001 es zona roja, 000 no es zona roja)
            setGeneral_LECTOR(texto.substring(11, 15));
            setGeneral_SUPERVISOR(texto.substring(16, 20)); // municipio
            setGeneral_CLAVE_SUPERVISOR(texto.substring(21, 26));
            setGeneral_TERMINAL(texto.substring(27, 42));
            setGeneral_VARIOS(texto.substring(43, 52));
            setGeneral_ObligaCBarras(texto.substring(53, 54)); // ruta no obliga impresora ( 1 obliga impresora , 0 no obliga impresora)
            setGeneral_fechaInicial(texto.substring(55, 65));
            setGeneral_fechaFinal(texto.substring(66, 76));
            setGeneral_obligaFirma(texto.substring(77, 78));
         //   setGeneral_CRNL(texto.substring(77, 78));
            ultimo_General = registro;
           // Log.e("error","dato "+texto.substring(11, 15));
            //fin estructura
        } catch (Exception e) {
           // Log.e("error","al leer "+e.getMessage());
            e.printStackTrace();
        }
    }
}