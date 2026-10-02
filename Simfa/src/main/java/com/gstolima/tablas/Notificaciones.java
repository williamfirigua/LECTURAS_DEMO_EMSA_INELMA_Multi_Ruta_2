package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Notificaciones {
    String sep = "|";
    String Notificaciones_codigo;
    String Notificaciones_descripcion;
    String Notificaciones_crnl;
    String Notificaciones_;

    public String getNotificaciones_CODIGO() {
        return Notificaciones_codigo;
    }

    public void setNotificaciones_codigo(String Notificaciones_codigo) {
        this.Notificaciones_codigo = Notificaciones_codigo;
    }

    public String getNotificaciones_DESCRIPCION() {
        return Notificaciones_descripcion;
    }

    public void setNotificaciones_descripcion(String Notificaciones_descripcion) {
        this.Notificaciones_descripcion = Notificaciones_descripcion;
    }

    public String getNotificaciones_CRNL() {
        return Notificaciones_crnl;
    }

    public void setNotificaciones_crnl(String Notificaciones_crnl) {
        this.Notificaciones_crnl = Notificaciones_crnl;
    }

    public String getNotificaciones_() {
        return Notificaciones_;
    }

    public void setNotificaciones_(String Notificaciones_) {
        this.Notificaciones_ = Notificaciones_;
    }

    BufferedReader fin;
    byte[] byteArray;
    String archivo_Notificaciones;

    public String getarchivo_Notificaciones() {
        return archivo_Notificaciones;
    }

    public void setarchivo_Notificaciones(String archivo_Notificaciones) {
        this.archivo_Notificaciones = archivo_Notificaciones;
    }

    static final int LONGITUD_REGISTRO = 56;
    int total_Notificaciones;

    public int gettotal_Notificaciones() {
        return total_Notificaciones;
    }

    public void settotal_Notificaciones(int total_Notificaciones) {
        this.total_Notificaciones = total_Notificaciones;
    }

    int ultimo_Notificaciones;
    int encontro_Notificaciones;

    public int getencontro_Notificaciones() {
        return encontro_Notificaciones;
    }

    public void setencontro_Notificaciones(int encontro_Notificaciones) {
        this.encontro_Notificaciones = encontro_Notificaciones;
    }

    String buscar_Notificaciones;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;

    public Boolean abrir_Notificaciones(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Notificaciones = nombreArchivo;
        return abrir(archivo_Notificaciones);
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Notificaciones = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_Notificaciones() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_Notificaciones(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_Notificaciones(int registro) {
        String[] campos;
        encontro_Notificaciones = 0;
        posicion_Notificaciones(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setNotificaciones_codigo(texto.substring(0, 2));
            setNotificaciones_descripcion(texto.substring(3, 53));
            setNotificaciones_crnl(texto.substring(54, 56));
            //setNotificaciones_(texto.substring(57, 57));
            ultimo_Notificaciones = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarSecuencial_Notificaciones(String codigo) {
        encontro_Notificaciones = 0;
        try {
            int x = Integer.parseInt(codigo.trim());
            for (int i = 0; i <  (total_Notificaciones); i++) {
                lectura_Notificaciones(i + 1);
                if (x == Integer.parseInt(Notificaciones_codigo.trim())) {
                    encontro_Notificaciones = i + 1;
                    posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
                    i = total_Notificaciones + 10;
                } else {
                    encontro_Notificaciones = 0;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return;
    }


    public void buscarbinario_Notificaciones(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Notificaciones;
        int b = 0;
        lectura_Notificaciones(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(Notificaciones_codigo.trim()) + "";
        if (otro.equals(uno)) {
            posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
            encontro_Notificaciones = 1;
        } else {
            lectura_Notificaciones(total_Notificaciones);
            otro = Notificaciones_codigo.trim().equals("") ? "0" : Notificaciones_codigo.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_Notificaciones = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Notificaciones(i + 1);
                    otro = Notificaciones_codigo.trim();
                    if (otro.equals(uno)) {
                        encontro_Notificaciones = i;
                        posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Notificaciones(i + 2);
                            otro = Notificaciones_codigo.trim();
                            if (uno == otro) {
                                encontro_Notificaciones = i;
                                posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Notificaciones = 0;
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


    public void buscarbinarioChar_Notificaciones(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Notificaciones;
        int b = 0;
        lectura_Notificaciones(1);
        String uno = codigo.toUpperCase().trim();
        String otro = Notificaciones_codigo.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
            encontro_Notificaciones = 1;
        } else {
            lectura_Notificaciones(total_Notificaciones);
            otro = Notificaciones_codigo.trim().equals("") ? "0" : Notificaciones_codigo.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_Notificaciones = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Notificaciones(i + 1);
                    otro = Notificaciones_codigo.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_Notificaciones = i;
                        posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Notificaciones(i + 2);
                            otro = Notificaciones_codigo.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_Notificaciones = i;
                                posicion_Notificaciones(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Notificaciones = 0;
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

