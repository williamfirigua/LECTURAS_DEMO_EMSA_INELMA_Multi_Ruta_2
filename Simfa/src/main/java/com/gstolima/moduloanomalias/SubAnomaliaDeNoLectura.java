package com.gstolima.moduloanomalias;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SubAnomaliaDeNoLectura {


    private String SubAnomaliaDeNoLectura_CODIGO;
    private String SubAnomaliaDeNoLectura_DESCRIPCION;

    private String SubAnomaliaDeNoLectura_CRNL;
    private String SubAnomaliaDeNoLectura_;
    private String SubAnomaliaDeNoLectura_INDICADOR;


    public String getSubAnomaliaDeNoLectura_CODIGO() {
        return SubAnomaliaDeNoLectura_CODIGO;
    }

    public void setSubAnomaliaDeNoLectura_CODIGO(String SubAnomaliaDeNoLectura_CODIGO) {
        this.SubAnomaliaDeNoLectura_CODIGO = SubAnomaliaDeNoLectura_CODIGO;
    }

    public String getSubAnomaliaDeNoLectura_DESCRIPCION() {
        return SubAnomaliaDeNoLectura_DESCRIPCION;
    }

    public void setSubAnomaliaDeNoLectura_DESCRIPCION(String SubAnomaliaDeNoLectura_DESCRIPCION) {
        this.SubAnomaliaDeNoLectura_DESCRIPCION = SubAnomaliaDeNoLectura_DESCRIPCION;
    }

    public String getSubAnomaliaDeNoLectura_CRNL() {
        return SubAnomaliaDeNoLectura_CRNL;
    }

    public void setSubAnomaliaDeNoLectura_CRNL(String SubAnomaliaDeNoLectura_CRNL) {
        this.SubAnomaliaDeNoLectura_CRNL = SubAnomaliaDeNoLectura_CRNL;
    }

    public String getSubAnomaliaDeNoLectura_() {
        return SubAnomaliaDeNoLectura_;
    }

    public void setSubAnomaliaDeNoLectura_(String SubAnomaliaDeNoLectura_) {
        this.SubAnomaliaDeNoLectura_ = SubAnomaliaDeNoLectura_;
    }

    public String getSubAnomaliaDeNoLectura_INDICADOR() {
        return SubAnomaliaDeNoLectura_INDICADOR;
    }

    public void setSubAnomaliaDeNoLectura_INDICADOR(String SubAnomaliaDeNoLectura_INDICADOR) {
        SubAnomaliaDeNoLectura_INDICADOR = SubAnomaliaDeNoLectura_INDICADOR;
    }

    BufferedReader fin;
    byte[] byteArray;
    private String archivo_SubAnomaliaDeNoLectura;
    static final int LONGITUD_REGISTRO = 56;
    private int total_SubAnomaliaDeNoLectura;
    int ultimo_SubAnomaliaDeNoLectura;
    private int encontro_SubAnomaliaDeNoLectura;
    String buscar_SubAnomaliaDeNoLectura;
    String texto;
    RandomAccessFile rFile;

    public Boolean abrir_SubAnomaliaDeNoLectura(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/Suscausas.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setArchivo_SubAnomaliaDeNoLectura(nombreArchivo);
        return abrir(getArchivo_SubAnomaliaDeNoLectura());
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            setTotal_SubAnomaliaDeNoLectura(fileSize / LONGITUD_REGISTRO);
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_SubAnomaliaDeNoLectura() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_SubAnomaliaDeNoLectura(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_SubAnomaliaDeNoLectura(int registro) {
        setEncontro_SubAnomaliaDeNoLectura(0);
        posicion_SubAnomaliaDeNoLectura(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setSubAnomaliaDeNoLectura_CODIGO(texto.substring(0, 2));
            setSubAnomaliaDeNoLectura_DESCRIPCION(texto.substring(3, 53));
            setSubAnomaliaDeNoLectura_CRNL(texto.substring(54, 56));
            ultimo_SubAnomaliaDeNoLectura = registro;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     public String getArchivo_SubAnomaliaDeNoLectura() {
        return archivo_SubAnomaliaDeNoLectura;
    }

    public void setArchivo_SubAnomaliaDeNoLectura(String archivo_SubAnomaliaDeNoLectura) {
        this.archivo_SubAnomaliaDeNoLectura = archivo_SubAnomaliaDeNoLectura;
    }

    public int getEncontro_SubAnomaliaDeNoLectura() {
        return encontro_SubAnomaliaDeNoLectura;
    }
    public void setEncontro_SubAnomaliaDeNoLectura(int encontro_SubAnomaliaDeNoLectura) {
        this.encontro_SubAnomaliaDeNoLectura = encontro_SubAnomaliaDeNoLectura;
    }
    public int getTotal_SubAnomaliaDeNoLectura() {
        return total_SubAnomaliaDeNoLectura;
    }
    public void setTotal_SubAnomaliaDeNoLectura(int total_SubAnomaliaDeNoLectura) {
        this.total_SubAnomaliaDeNoLectura = total_SubAnomaliaDeNoLectura;
    }
}
