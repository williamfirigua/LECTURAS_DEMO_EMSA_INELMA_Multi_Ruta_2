package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Informacion {
    String sep = "|";
    String Informacion_nro_acta;
    String Informacion_informacion;
    String Informacion_cnlf;

    public String getInformacion_NRO_ACTA() {
        return Informacion_nro_acta;
    }

    public void setInformacion_nro_acta(String Informacion_nro_acta) {
        this.Informacion_nro_acta = Informacion_nro_acta;
    }

    public String getInformacion_INFORMACION() {
        return Informacion_informacion;
    }

    public void setInformacion_informacion(String Informacion_informacion) {
        this.Informacion_informacion = Informacion_informacion;
    }

    public String getInformacion_CNLF() {
        return Informacion_cnlf;
    }

    public void setInformacion_cnlf(String Informacion_cnlf) {
        this.Informacion_cnlf = Informacion_cnlf;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_Informacion;

    public String getarchivo_Informacion() {
        return archivo_Informacion;
    }

    public void setarchivo_Informacion(String archivo_Informacion) {
        this.archivo_Informacion = archivo_Informacion;
    }

    static final int LONGITUD_REGISTRO = 802;
    int total_Informacion;

    public int gettotal_Informacion() {
        return total_Informacion;
    }

    public void settotal_Informacion(int total_Informacion) {
        this.total_Informacion = total_Informacion;
    }

    int ultimo_Informacion;
    int encontro_Informacion;

    public int getencontro_Informacion() {
        return encontro_Informacion;
    }

    public void setencontro_Informacion(int encontro_Informacion) {
        this.encontro_Informacion = encontro_Informacion;
    }

    String buscar_Informacion;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_Informacion(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Informacion = nombreArchivo;
        return abrir(archivo_Informacion);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Informacion = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_Informacion() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_Informacion(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_Informacion(int registro) {
        String[] campos;
        encontro_Informacion = 0;
        posicion_Informacion(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setInformacion_nro_acta(texto.substring(0, 15));
            setInformacion_informacion(texto.substring(16, 799));
            setInformacion_cnlf(texto.substring(800, 802));
            ultimo_Informacion = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_Informacion(String codigo) {
        encontro_Informacion = 0;
        for (int i = 0; i < (int) (total_Informacion); i++) {
            lectura_Informacion(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(Informacion_nro_acta.trim()))
            {
                encontro_Informacion = i + 1;
                posicion_Informacion(i + 1, LONGITUD_REGISTRO);
                i = total_Informacion + 10;
            }
            else
            {
                encontro_Informacion = 0;
            }
        }
        return;
    }


    public void buscarbinario_Informacion(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Informacion;
        int b = 0;
        lectura_Informacion(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(Informacion_nro_acta.trim()) + "";
        if (otro.equals(uno)) {
            posicion_Informacion(i + 1, LONGITUD_REGISTRO);
            encontro_Informacion = 1;
        } else {
            lectura_Informacion(total_Informacion);
            otro = Informacion_nro_acta.trim().equals("") ? "0" : Informacion_nro_acta.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_Informacion = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Informacion(i + 1);
                    otro = Informacion_nro_acta.trim();
                    if (otro.equals(uno)) {
                        encontro_Informacion = i;
                        posicion_Informacion(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Informacion(i + 2);
                            otro = Informacion_nro_acta.trim();
                            if (uno == otro) {
                                encontro_Informacion = i;
                                posicion_Informacion(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Informacion = 0;
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


    public void buscarbinarioChar_Informacion(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Informacion;
        int b = 0;
        lectura_Informacion(1);
        String uno = codigo.toUpperCase().trim();
        String otro = Informacion_nro_acta.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_Informacion(i + 1, LONGITUD_REGISTRO);
            encontro_Informacion = 1;
        } else {
            lectura_Informacion(total_Informacion);
            otro = Informacion_nro_acta.trim().equals("") ? "0" : Informacion_nro_acta.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_Informacion = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Informacion(i + 1);
                    otro = Informacion_nro_acta.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_Informacion = i;
                        posicion_Informacion(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Informacion(i + 2);
                            otro = Informacion_nro_acta.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_Informacion = i;
                                posicion_Informacion(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Informacion = 0;
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


