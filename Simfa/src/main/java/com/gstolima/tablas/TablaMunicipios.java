package com.gstolima.tablas;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/// <summary>
/// Descripcion breve del archivo a generar...tablaMunicipios
/// </summary>

public class TablaMunicipios {  //Ax ya , es TABLAMUNICIPIOS
    static final int LONGITUD_REGISTRO = 57;
    String sep = ";";
    String tablaMunicipios_CODIGO;
    String tablaMunicipios_DESCRIPCION;
    String tablaMunicipios_CRNL;
    BufferedReader fin;
    byte[] byteArray;
    String archivo_TablaMunicipios;
    int total_TablaMunicipios;
    int ultimo_TablaMunicipios;
    int encontro_TablaMunicipios;

    String buscar_TablaMunicipios;
    String texto;
    // File ruta_sd = Environment.getExternalStorageDirectory();
    // File ruta_sd = new
    // File('C:\\Users\\SGlobal\\Documents\\PruebasJava\\CensoSalida.txt');
    RandomAccessFile rFile;


    public String gettablaMunicipios_CODIGO() {
        return tablaMunicipios_CODIGO;
    }

    public void settablaMunicipios_CODIGO(String tablaMunicipios_CODIGO) {
        this.tablaMunicipios_CODIGO = tablaMunicipios_CODIGO;
    }

    public String gettablaMunicipios_DESCRIPCION() {
        return tablaMunicipios_DESCRIPCION;
    }

    public void settablaMunicipios_DESCRIPCION(String tablaMunicipios_DESCRIPCION) {
        this.tablaMunicipios_DESCRIPCION = tablaMunicipios_DESCRIPCION;
    }

    public String gettablaMunicipios_CRNL() {
        return tablaMunicipios_CRNL;
    }



    public void settablaMunicipios_CRNL(String tablaMunicipios_CRNL) {
        this.tablaMunicipios_CRNL = tablaMunicipios_CRNL;
    }

    public int getencontro_TablaMunicipios() {
        return encontro_TablaMunicipios;
    }

    public void setencontro_TablaMunicipios(int encontro_TablaMunicipios) {
        this.encontro_TablaMunicipios = encontro_TablaMunicipios;
    }

    public String getArchivo_TablaMunicipios() {
        return archivo_TablaMunicipios;
    }

    public void setArchivo_TablaMunicipios(String archivo_TablaMunicipios) {
        this.archivo_TablaMunicipios = archivo_TablaMunicipios;
    }
    public Boolean abrir_TablaMunicipios(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); // C:/Users/SGlobal/Documents/Pruebas
            // Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_TablaMunicipios = nombreArchivo;
        return abrir(archivo_TablaMunicipios);
    }
    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_TablaMunicipios = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            // Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_TablaMunicipios() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_TablaMunicipios(int registro, int tamano) {
        try {
            rFile.seek((registro - 1) * tamano);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Escribir los registros atual en el archivo en posicion
    public void lectura_TablaMunicipios(int registro) {
        encontro_TablaMunicipios = 0;
        posicion_TablaMunicipios(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            settablaMunicipios_CODIGO(texto.substring(0, 3));
            settablaMunicipios_DESCRIPCION(texto.substring(4, 54));
            //settablaMunicipios_CRNL(texto.substring(55, 57));
            ultimo_TablaMunicipios = registro;
            Log.e("TAG","lectura entrada Muni II "+gettablaMunicipios_DESCRIPCION().trim());
            // fin estructura
        } catch (IOException e) {
            Log.e("error","lectura entrada Muni II "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void buscarSecuencial_TablaMunicipios(String codigo) {
        encontro_TablaMunicipios = 0;
        int c= Integer.parseInt(codigo.trim());
        for (int i = 0; i <  total_TablaMunicipios; i++) {
            lectura_TablaMunicipios(i + 1);
            if (c == Integer.parseInt(tablaMunicipios_CODIGO.trim())) {
                encontro_TablaMunicipios = i + 1;
                posicion_TablaMunicipios(i + 1, LONGITUD_REGISTRO);
                i = total_TablaMunicipios + 10;
            } else {
                encontro_TablaMunicipios = 0;
            }
        }
        return;
    }


    public void buscarbinario_TablaMunicipios(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_TablaMunicipios;
        int b = 0;
        lectura_TablaMunicipios(1);
        String uno = codigo.trim();
        String otro = tablaMunicipios_CODIGO.trim();
        if (otro.equals(uno)) {
            posicion_TablaMunicipios(i + 1, LONGITUD_REGISTRO);
            encontro_TablaMunicipios = 1;
        } else {
            lectura_TablaMunicipios(total_TablaMunicipios);
            otro = tablaMunicipios_CODIGO.trim().equals("") ? "0" : tablaMunicipios_CODIGO.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_TablaMunicipios = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_TablaMunicipios(i + 1);
                    otro = tablaMunicipios_CODIGO.trim();
                    if (otro.equals(uno)) {
                        encontro_TablaMunicipios = i;
                        posicion_TablaMunicipios(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_TablaMunicipios(i + 2);
                            otro = tablaMunicipios_CODIGO.trim();
                            if (uno == otro) {
                                encontro_TablaMunicipios = i;
                                posicion_TablaMunicipios(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_TablaMunicipios = 0;
                            }
                            salir = 1;
                        } else {
                            if (Integer.parseInt(uno) > Integer.parseInt(otro)) {
                                b = i;
                            } else {
                                if (i - b == 1) {
                                    b = i;
                                }
                                t = i;
                            }
                        }
                    }
                }
            }
        }
    }

}
