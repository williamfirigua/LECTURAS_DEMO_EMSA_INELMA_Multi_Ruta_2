package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TablaRangos {
    String sep = "|";
    String Rangos_RANGOINICIAL;
    String Rangos_RANGOFINAL;
    String Rangos_LEVE;
    String Rangos_MEDIO;
    String Rangos_ALTO;
    String Rangos_OBLIGAFOTOLEVE;
    String Rangos_OBLIGAFOTOALTO;
    String Rangos_OBLIGAFOTOMEDIA;
    String Rangos_CRNL;

    public String getRangos_RANGOINICIAL() {
        return Rangos_RANGOINICIAL;
    }

    public void setRangos_RANGOINICIAL(String Rangos_RANGOINICIAL) {
        this.Rangos_RANGOINICIAL = Rangos_RANGOINICIAL;
    }

    public String getRangos_RANGOFINAL() {
        return Rangos_RANGOFINAL;
    }

    public void setRangos_RANGOFINAL(String Rangos_RANGOFINAL) {
        this.Rangos_RANGOFINAL = Rangos_RANGOFINAL;
    }

    public String getRangos_LEVE() {
        return Rangos_LEVE;
    }

    public void setRangos_LEVE(String Rangos_LEVE) {
        this.Rangos_LEVE = Rangos_LEVE;
    }

    public String getRangos_MEDIO() {
        return Rangos_MEDIO;
    }

    public void setRangos_MEDIO(String Rangos_MEDIO) {
        this.Rangos_MEDIO = Rangos_MEDIO;
    }

    public String getRangos_ALTO() {
        return Rangos_ALTO;
    }

    public void setRangos_ALTO(String Rangos_ALTO) {
        this.Rangos_ALTO = Rangos_ALTO;
    }

    public String getRangos_OBLIGAFOTOLEVE() {
        return Rangos_OBLIGAFOTOLEVE;
    }

    public void setRangos_OBLIGAFOTOLEVE(String Rangos_OBLIGAFOTOLEVE) {
        this.Rangos_OBLIGAFOTOLEVE = Rangos_OBLIGAFOTOLEVE;
    }

    public String getRangos_OBLIGAFOTOALTO() {
        return Rangos_OBLIGAFOTOALTO;
    }

    public void setRangos_OBLIGAFOTOALTO(String Rangos_OBLIGAFOTOALTO) {
        this.Rangos_OBLIGAFOTOALTO = Rangos_OBLIGAFOTOALTO;
    }

    public String getRangos_OBLIGAFOTOMEDIA() {
        return Rangos_OBLIGAFOTOMEDIA;
    }

    public void setRangos_OBLIGAFOTOMEDIA(String Rangos_OBLIGAFOTOMEDIA) {
        this.Rangos_OBLIGAFOTOMEDIA = Rangos_OBLIGAFOTOMEDIA;
    }

    public String getRangos_CRNL() {
        return Rangos_CRNL;
    }

    public void setRangos_CRNL(String Rangos_CRNL) {
        this.Rangos_CRNL = Rangos_CRNL;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_Rangos;

    public String getarchivo_Rangos() {
        return archivo_Rangos;
    }

    public void setarchivo_Rangos(String archivo_Rangos) {
        this.archivo_Rangos = archivo_Rangos;
    }

    static final int LONGITUD_REGISTRO = 38;
    int total_Rangos;

    public int gettotal_Rangos() {
        return total_Rangos;
    }

    public void settotal_Rangos(int total_Rangos) {
        this.total_Rangos = total_Rangos;
    }

    int ultimo_Rangos;
    int encontro_Rangos;

    public int getencontro_Rangos() {
        return encontro_Rangos;
    }

    public void setencontro_Rangos(int encontro_Rangos) {
        this.encontro_Rangos = encontro_Rangos;
    }

    String buscar_Rangos;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;

    public Boolean abrir_Rangos(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Rangos = nombreArchivo;
        return abrir(archivo_Rangos);
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Rangos = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_Rangos() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_Rangos(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_Rangos(int registro) {
        String[] campos;
        encontro_Rangos = 0;
        posicion_Rangos(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setRangos_RANGOINICIAL(texto.substring(0, 8));
            setRangos_RANGOFINAL(texto.substring(9, 17));
            setRangos_LEVE(texto.substring(18, 21));
            setRangos_MEDIO(texto.substring(22, 25));
            setRangos_ALTO(texto.substring(26, 29));
            setRangos_OBLIGAFOTOLEVE(texto.substring(30, 31));
            setRangos_OBLIGAFOTOALTO(texto.substring(32, 33));
            setRangos_OBLIGAFOTOMEDIA(texto.substring(34, 35));
            setRangos_CRNL(texto.substring(36, 38));
            ultimo_Rangos = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodos de busqueda que hay que llevar a parametros ojooo
    public void buscarSecuencial_Rangos(String codigo)
    {
        double cod = Double.parseDouble(codigo);
        encontro_Rangos = 0;
        //buffer_rangos.Initialize();
        for (int i = 0; i < total_Rangos; i++)
        {
            lectura_Rangos(i + 1);

            if (cod >= Double.parseDouble(Rangos_RANGOINICIAL) && cod <= Double.parseDouble(Rangos_RANGOFINAL))
            {
                encontro_Rangos = i + 1;
                posicion_Rangos(i + 1, LONGITUD_REGISTRO);
                i = ((int)(total_Rangos)) + 10;
            }
            else
            {
                encontro_Rangos = 0;
            }
        }
        return;
    }

//    public void buscarSecuencial_Rangos(String codigo) {
//        encontro_Rangos = 0;
//        int x =Integer.parseInt(codigo.trim());
//        for (int i = 0; i < (int) (total_Rangos); i++) {
//            lectura_Rangos(i + 1);
//            if (x == Integer.parseInt(Rangos_RANGOINICIAL.trim())) {
//                encontro_Rangos = i + 1;
//                posicion_Rangos(i + 1, LONGITUD_REGISTRO);
//                i = total_Rangos + 10;
//            } else {
//                encontro_Rangos = 0;
//            }
//        }
//        return;
//    }
}
