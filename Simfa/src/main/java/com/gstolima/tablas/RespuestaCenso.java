package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RespuestaCenso {
    String sep = "|";
    String RespuestaCenso_CodCuenta;
    String RespuestaCenso_NumeroPregunta;
    String RespuestaCenso_TipoPregunta;
    String RespuestaCenso_CodRespuestaCerrada;
    String RespuestaCenso_RespuestaAbierta;
    String RespuestaCenso_FechaCenso;
    String RespuestaCenso_HoraCenso;
    String RespuestaCenso_Aforador;
    String RespuestaCenso_PDA;
    String RespuestaCenso_CRNL;

    public String getRespuestaCenso_CODCUENTA() {
        return RespuestaCenso_CodCuenta;
    }

    public void setRespuestaCenso_CodCuenta(String RespuestaCenso_CodCuenta) {
        this.RespuestaCenso_CodCuenta = RespuestaCenso_CodCuenta;
    }

    public String getRespuestaCenso_NUMEROPREGUNTA() {
        return RespuestaCenso_NumeroPregunta;
    }

    public void setRespuestaCenso_NumeroPregunta(String RespuestaCenso_NumeroPregunta) {
        this.RespuestaCenso_NumeroPregunta = RespuestaCenso_NumeroPregunta;
    }

    public String getRespuestaCenso_TIPOPREGUNTA() {
        return RespuestaCenso_TipoPregunta;
    }

    public void setRespuestaCenso_TipoPregunta(String RespuestaCenso_TipoPregunta) {
        this.RespuestaCenso_TipoPregunta = RespuestaCenso_TipoPregunta;
    }

    public String getRespuestaCenso_CODRESPUESTACERRADA() {
        return RespuestaCenso_CodRespuestaCerrada;
    }

    public void setRespuestaCenso_CodRespuestaCerrada(String RespuestaCenso_CodRespuestaCerrada) {
        this.RespuestaCenso_CodRespuestaCerrada = RespuestaCenso_CodRespuestaCerrada;
    }

    public String getRespuestaCenso_RESPUESTAABIERTA() {
        return RespuestaCenso_RespuestaAbierta;
    }

    public void setRespuestaCenso_RespuestaAbierta(String RespuestaCenso_RespuestaAbierta) {
        this.RespuestaCenso_RespuestaAbierta = RespuestaCenso_RespuestaAbierta;
    }

    public String getRespuestaCenso_FECHACENSO() {
        return RespuestaCenso_FechaCenso;
    }

    public void setRespuestaCenso_FechaCenso(String RespuestaCenso_FechaCenso) {
        this.RespuestaCenso_FechaCenso = RespuestaCenso_FechaCenso;
    }

    public String getRespuestaCenso_HORACENSO() {
        return RespuestaCenso_HoraCenso;
    }

    public void setRespuestaCenso_HoraCenso(String RespuestaCenso_HoraCenso) {
        this.RespuestaCenso_HoraCenso = RespuestaCenso_HoraCenso;
    }

    public String getRespuestaCenso_AFORADOR() {
        return RespuestaCenso_Aforador;
    }

    public void setRespuestaCenso_Aforador(String RespuestaCenso_Aforador) {
        this.RespuestaCenso_Aforador = RespuestaCenso_Aforador;
    }

    public String getRespuestaCenso_PDA() {
        return RespuestaCenso_PDA;
    }

    public void setRespuestaCenso_PDA(String RespuestaCenso_PDA) {
        this.RespuestaCenso_PDA = RespuestaCenso_PDA;
    }

    public String getRespuestaCenso_CRNL() {
        return RespuestaCenso_CRNL;
    }

    public void setRespuestaCenso_CRNL(String RespuestaCenso_CRNL) {
        this.RespuestaCenso_CRNL = RespuestaCenso_CRNL;
    }

    BufferedReader fin;
    byte[] byteArray;
    String archivo_RespuestaCenso;

    public String getarchivo_RespuestaCenso() {
        return archivo_RespuestaCenso;
    }

    public void setarchivo_RespuestaCenso(String archivo_RespuestaCenso) {
        this.archivo_RespuestaCenso = archivo_RespuestaCenso;
    }

    static final int LONGITUD_REGISTRO = 108;
    int total_RespuestaCenso;

    public int gettotal_RespuestaCenso() {
        return total_RespuestaCenso;
    }

    public void settotal_RespuestaCenso(int total_RespuestaCenso) {
        this.total_RespuestaCenso = total_RespuestaCenso;
    }

    int ultimo_RespuestaCenso;
    int encontro_RespuestaCenso;

    public int getencontro_RespuestaCenso() {
        return encontro_RespuestaCenso;
    }

    public void setencontro_RespuestaCenso(int encontro_RespuestaCenso) {
        this.encontro_RespuestaCenso = encontro_RespuestaCenso;
    }

    String buscar_RespuestaCenso;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_RespuestaCenso(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_RespuestaCenso = nombreArchivo;
        return abrir(archivo_RespuestaCenso);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_RespuestaCenso = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_RespuestaCenso() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean escribir_RespuestaCenso(int posicion) {
        posicion_RespuestaCenso(posicion, LONGITUD_REGISTRO);
        rellenar_RespuestaCenso();
        String texto = RespuestaCenso_CodCuenta + sep + RespuestaCenso_NumeroPregunta + sep + RespuestaCenso_TipoPregunta + sep + RespuestaCenso_CodRespuestaCerrada + sep + RespuestaCenso_RespuestaAbierta + sep +
                RespuestaCenso_FechaCenso + sep + RespuestaCenso_HoraCenso + sep + RespuestaCenso_Aforador + sep + RespuestaCenso_PDA + sep + RespuestaCenso_CRNL;
        try {
            if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                rFile.writeBytes(texto);
                System.out.println("Los Datos fueron grabados correctamente");
                return true;
            } else {
                System.out.println("Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                return false;
            }
        } catch (IOException ioe) {
            System.out.println("Se presento problema al escribir en el archivo Longitud Errada: " + LONGITUD_REGISTRO);
            return false;
        }
    }


    public void rellenar_RespuestaCenso() {
        try {
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo RespuestaCenso.dat..");
            e.printStackTrace();
        }

        return;
    }


    public void posicion_RespuestaCenso(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_RespuestaCenso(int registro) {
        String[] campos;
        encontro_RespuestaCenso = 0;
        posicion_RespuestaCenso(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setRespuestaCenso_CodCuenta(texto.substring(0,10));
            setRespuestaCenso_NumeroPregunta(texto.substring(11, 13));
            setRespuestaCenso_TipoPregunta(texto.substring(14, 15));
            setRespuestaCenso_CodRespuestaCerrada(texto.substring(16, 18));
            setRespuestaCenso_RespuestaAbierta(texto.substring(19, 69));
            setRespuestaCenso_FechaCenso(texto.substring(70, 80));
            setRespuestaCenso_HoraCenso(texto.substring(81, 89));
            setRespuestaCenso_Aforador(texto.substring(90, 94));
            setRespuestaCenso_PDA(texto.substring(95, 105));
            setRespuestaCenso_CRNL(texto.substring(106, 108));
            ultimo_RespuestaCenso = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_RespuestaCenso(String codigo) {
        encontro_RespuestaCenso = 0;
        for (int i = 0; i < (int) (total_RespuestaCenso); i++) {
            lectura_RespuestaCenso(i + 1);
            if (codigo.trim() == RespuestaCenso_CodCuenta.trim()) {
                encontro_RespuestaCenso = i + 1;
                posicion_RespuestaCenso(i + 1, LONGITUD_REGISTRO);
                i = total_RespuestaCenso + 10;
            } else {
                encontro_RespuestaCenso = 0;
            }
        }
        return;
    }
}
