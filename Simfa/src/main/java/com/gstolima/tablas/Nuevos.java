package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Nuevos {
    String sep = "|";
    private String Nuevos_codigotra;
    private String Nuevos_leido;
    private String Nuevos_fechaleido;
    private String Nuevos_horaleido;
    private String Nuevos_novedad;
    private String Nuevos_supervisor;
    private String Nuevos_terminal;
    private String Nuevos_turnosel;
    private String Nuevos_fin;

    public String getNuevos_CODIGOTRA() {
        return Nuevos_codigotra;
    }

    public void setNuevos_codigotra(String Nuevos_codigotra) {
        this.Nuevos_codigotra = Nuevos_codigotra;
    }

    public String getNuevos_LEIDO() {
        return Nuevos_leido;
    }

    public void setNuevos_leido(String Nuevos_leido) {
        this.Nuevos_leido = Nuevos_leido;
    }

    public String getNuevos_FECHALEIDO() {
        return Nuevos_fechaleido;
    }

    public void setNuevos_fechaleido(String Nuevos_fechaleido) {
        this.Nuevos_fechaleido = Nuevos_fechaleido;
    }

    public String getNuevos_HORALEIDO() {
        return Nuevos_horaleido;
    }

    public void setNuevos_horaleido(String Nuevos_horaleido) {
        this.Nuevos_horaleido = Nuevos_horaleido;
    }

    public String getNuevos_NOVEDAD() {
        return Nuevos_novedad;
    }

    public void setNuevos_novedad(String Nuevos_novedad) {
        this.Nuevos_novedad = Nuevos_novedad;
    }

    public String getNuevos_SUPERVISOR() {
        return Nuevos_supervisor;
    }

    public void setNuevos_supervisor(String Nuevos_supervisor) {
        this.Nuevos_supervisor = Nuevos_supervisor;
    }

    public String getNuevos_TERMINAL() {
        return Nuevos_terminal;
    }

    public void setNuevos_terminal(String Nuevos_terminal) {
        this.Nuevos_terminal = Nuevos_terminal;
    }

    public String getNuevos_TURNOSEL() {
        return Nuevos_turnosel;
    }

    public void setNuevos_turnosel(String Nuevos_turnosel) {
        this.Nuevos_turnosel = Nuevos_turnosel;
    }

    public String getNuevos_FIN() {
        return Nuevos_fin;
    }

    public void setNuevos_fin(String Nuevos_fin) {
        this.Nuevos_fin = Nuevos_fin;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_Nuevos;

    public String getarchivo_Nuevos() {
        return archivo_Nuevos;
    }

    public void setarchivo_Nuevos(String archivo_Nuevos) {
        this.archivo_Nuevos = archivo_Nuevos;
    }

    static final int LONGITUD_REGISTRO = 41;
    int total_Nuevos;

    public int gettotal_Nuevos() {
        return total_Nuevos;
    }

    public void settotal_Nuevos(int total_Nuevos) {
        this.total_Nuevos = total_Nuevos;
    }

    int ultimo_Nuevos;
    int encontro_Nuevos;

    public int getencontro_Nuevos() {
        return encontro_Nuevos;
    }

    public void setencontro_Nuevos(int encontro_Nuevos) {
        this.encontro_Nuevos = encontro_Nuevos;
    }

    String buscar_Nuevos;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_Nuevos(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Nuevos = nombreArchivo;
        return abrir(archivo_Nuevos);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Nuevos = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_Nuevos() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean escribir_Nuevos(int posicion) {
        posicion_Nuevos(posicion, LONGITUD_REGISTRO);
        rellenar_Nuevos();
        String texto = Nuevos_codigotra + sep + Nuevos_leido + sep + Nuevos_fechaleido + sep + Nuevos_horaleido + sep + Nuevos_novedad + sep +
                Nuevos_supervisor + sep + Nuevos_terminal + sep + Nuevos_turnosel + sep + Nuevos_fin;
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


    public void rellenar_Nuevos() {
        try {
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo Nuevos.dat..");
            e.printStackTrace();
        }

        return;
    }


    public void posicion_Nuevos(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_Nuevos(int registro) {
        String[] campos;
        encontro_Nuevos = 0;
        posicion_Nuevos(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setNuevos_codigotra(texto.substring(0, 6));
            setNuevos_leido(texto.substring(7, 8));
            setNuevos_fechaleido(texto.substring(9, 17));
            setNuevos_horaleido(texto.substring(18, 24));
            setNuevos_novedad(texto.substring(25, 27));
            setNuevos_supervisor(texto.substring(28, 32));
            setNuevos_terminal(texto.substring(33, 35));
            setNuevos_turnosel(texto.substring(36, 38));
            setNuevos_fin(texto.substring(39, 41));
            ultimo_Nuevos = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void buscarSecuencial_Nuevos(String codigo) {
//        encontro_Nuevos = 0;
//        for (int i = 0; i < (int) (total_Nuevos); i++) {
//            lectura_Nuevos(i + 1);
//            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(.trim()))
//            {
//                encontro_Nuevos = i + 1;
//                posicion_Nuevos(i + 1, LONGITUD_REGISTRO);
//                i = total_Nuevos + 10;
//            }
//            else
//            {
//                encontro_Nuevos = 0;
//            }
//        }
//        return;
//    }
}
