package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CategoriaInforme {
    String sep = "|";
    private String CategoriaInforme_codigo;
    private String CategoriaInforme_descripcion;
    private String CategoriaInforme_fin;

    public String getCategoriaInforme_CODIGO() {
        return CategoriaInforme_codigo;
    }

    public void setCategoriaInforme_codigo(String CategoriaInforme_codigo) {
        this.CategoriaInforme_codigo = CategoriaInforme_codigo;
    }

    public String getCategoriaInforme_DESCRIPCION() {
        return CategoriaInforme_descripcion;
    }

    public void setCategoriaInforme_descripcion(String CategoriaInforme_descripcion) {
        this.CategoriaInforme_descripcion = CategoriaInforme_descripcion;
    }

    public String getCategoriaInforme_FIN() {
        return CategoriaInforme_fin;
    }

    public void setCategoriaInforme_fin(String CategoriaInforme_fin) {
        this.CategoriaInforme_fin = CategoriaInforme_fin;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_CategoriaInforme;

    public String getarchivo_CategoriaInforme() {
        return archivo_CategoriaInforme;
    }

    public void setarchivo_CategoriaInforme(String archivo_CategoriaInforme) {
        this.archivo_CategoriaInforme = archivo_CategoriaInforme;
    }

    static final int LONGITUD_REGISTRO = 56;
    int total_CategoriaInforme;

    public int gettotal_CategoriaInforme() {
        return total_CategoriaInforme;
    }

    public void settotal_CategoriaInforme(int total_CategoriaInforme) {
        this.total_CategoriaInforme = total_CategoriaInforme;
    }

    int ultimo_CategoriaInforme;
    int encontro_CategoriaInforme;

    public int getencontro_CategoriaInforme() {
        return encontro_CategoriaInforme;
    }

    public void setencontro_CategoriaInforme(int encontro_CategoriaInforme) {
        this.encontro_CategoriaInforme = encontro_CategoriaInforme;
    }

    String buscar_CategoriaInforme;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_CategoriaInforme(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_CategoriaInforme = nombreArchivo;
        return abrir(archivo_CategoriaInforme);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_CategoriaInforme = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_CategoriaInforme() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void posicion_CategoriaInforme(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_CategoriaInforme(int registro) {
        String[] campos;
        encontro_CategoriaInforme = 0;
        posicion_CategoriaInforme(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setCategoriaInforme_codigo(texto.substring(0, 2));
            setCategoriaInforme_descripcion(texto.substring(3, 53));
            setCategoriaInforme_fin(texto.substring(54, 56));
            ultimo_CategoriaInforme = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_CategoriaInforme(String codigo) {
        encontro_CategoriaInforme = 0;
        for (int i = 0; i < (int) (total_CategoriaInforme); i++) {
            lectura_CategoriaInforme(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(CategoriaInforme_codigo.trim())) {
                encontro_CategoriaInforme = i + 1;
                posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
                i = total_CategoriaInforme + 10;
            } else {
                encontro_CategoriaInforme = 0;
            }
        }
        return;
    }


    public void buscarbinario_CategoriaInforme(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_CategoriaInforme;
        int b = 0;
        lectura_CategoriaInforme(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(CategoriaInforme_codigo.trim()) + "";
        if (otro.equals(uno)) {
            posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
            encontro_CategoriaInforme = 1;
        } else {
            lectura_CategoriaInforme(total_CategoriaInforme);
            otro = CategoriaInforme_codigo.trim().equals("") ? "0" : CategoriaInforme_codigo.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_CategoriaInforme = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_CategoriaInforme(i + 1);
                    otro = CategoriaInforme_codigo.trim();
                    if (otro.equals(uno)) {
                        encontro_CategoriaInforme = i;
                        posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_CategoriaInforme(i + 2);
                            otro = CategoriaInforme_codigo.trim();
                            if (uno == otro) {
                                encontro_CategoriaInforme = i;
                                posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_CategoriaInforme = 0;
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


    public void buscarbinarioChar_CategoriaInforme(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_CategoriaInforme;
        int b = 0;
        lectura_CategoriaInforme(1);
        String uno = codigo.toUpperCase().trim();
        String otro = CategoriaInforme_codigo.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
            encontro_CategoriaInforme = 1;
        } else {
            lectura_CategoriaInforme(total_CategoriaInforme);
            otro = CategoriaInforme_codigo.trim().equals("") ? "0" : CategoriaInforme_codigo.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_CategoriaInforme = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_CategoriaInforme(i + 1);
                    otro = CategoriaInforme_codigo.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_CategoriaInforme = i;
                        posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_CategoriaInforme(i + 2);
                            otro = CategoriaInforme_codigo.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_CategoriaInforme = i;
                                posicion_CategoriaInforme(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_CategoriaInforme = 0;
                            }
                            salir = 1;
                        } else {
                            if (uno.compareTo(otro) == 1) {
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


