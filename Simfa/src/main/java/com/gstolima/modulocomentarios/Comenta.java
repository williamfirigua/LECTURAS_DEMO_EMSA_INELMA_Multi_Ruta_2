package com.gstolima.modulocomentarios;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Comenta {

    private String Comenta_CODIGO;
    private String Comenta_DESCRIPCION;
    private String Comenta_OBLIGAFOTO;
    private String Comenta_REQUIEREINFORME;
    private String Comenta_FOTOACOLOR;
    private String Comenta_CRNL;

    public String getComenta_CODIGO() {
        return Comenta_CODIGO;
    }

    public void setComenta_CODIGO(String Comenta_CODIGO) {
        this.Comenta_CODIGO = Comenta_CODIGO;
    }

    public String getComenta_DESCRIPCION() {
        return Comenta_DESCRIPCION;
    }

    public void setComenta_DESCRIPCION(String Comenta_DESCRIPCION) {
        this.Comenta_DESCRIPCION = Comenta_DESCRIPCION;
    }

    public String getComenta_OBLIGAFOTO() {
        return Comenta_OBLIGAFOTO;
    }

    public void setComenta_OBLIGAFOTO(String Comenta_OBLIGAFOTO) {
        this.Comenta_OBLIGAFOTO = Comenta_OBLIGAFOTO;
    }

    public String getComenta_REQUIEREINFORME() {
        return Comenta_REQUIEREINFORME;
    }

    public void setComenta_REQUIEREINFORME(String Comenta_REQUIEREINFORME) {
        this.Comenta_REQUIEREINFORME = Comenta_REQUIEREINFORME;
    }

    public String getComenta_FOTOACOLOR() {
        return Comenta_FOTOACOLOR;
    }

    public void setComenta_FOTOACOLOR(String Comenta_FOTOACOLOR) {
        this.Comenta_FOTOACOLOR = Comenta_FOTOACOLOR;
    }

    public String getComenta_CRNL() {
        return Comenta_CRNL;
    }

    public void setComenta_CRNL(String Comenta_CRNL) {
        this.Comenta_CRNL = Comenta_CRNL;
    }

    BufferedReader fin;
    byte[] byteArray;
    String archivo_Comenta;

    public String getarchivo_Comenta() {
        return archivo_Comenta;
    }

    public void setarchivo_Comenta(String archivo_Comenta) {
        this.archivo_Comenta = archivo_Comenta;
    }

    static final int LONGITUD_REGISTRO = 32;
    int total_Comenta;

    public int gettotal_Comenta() {
        return total_Comenta;
    }

    public void settotal_Comenta(int total_Comenta) {
        this.total_Comenta = total_Comenta;
    }

    int ultimo_Comenta;
    int encontro_Comenta;

    public int getencontro_Comenta() {
        return encontro_Comenta;
    }

    public void setencontro_Comenta(int encontro_Comenta) {
        this.encontro_Comenta = encontro_Comenta;
    }

    String buscar_Comenta;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;

    public Boolean abrir_Comenta(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Comenta = nombreArchivo;
        return abrir(archivo_Comenta);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Comenta = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_Comenta() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void posicion_Comenta(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_Comenta(int registro) {
        String[] campos;
        encontro_Comenta = 0;
        posicion_Comenta(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setComenta_CODIGO(texto.substring(0, 2));
            setComenta_DESCRIPCION(texto.substring(3, 23));
            setComenta_OBLIGAFOTO(texto.substring(24, 25));
            setComenta_REQUIEREINFORME(texto.substring(26, 27));
            setComenta_FOTOACOLOR(texto.substring(28, 29));
            setComenta_CRNL(texto.substring(30, 32));
            ultimo_Comenta = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_Comenta(String codigo) {
        encontro_Comenta = 0;
        for (int i = 0; i < (int) (total_Comenta); i++) {
            lectura_Comenta(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(Comenta_CODIGO.trim())) {
                encontro_Comenta = i + 1;
                posicion_Comenta(i + 1, LONGITUD_REGISTRO);
                i = total_Comenta + 10;
            } else {
                encontro_Comenta = 0;
            }
        }
        return;
    }


    public void buscarbinario_Comenta(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Comenta;
        int b = 0;
        lectura_Comenta(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(Comenta_CODIGO.trim()) + "";
        if (otro.equals(uno)) {
            posicion_Comenta(i + 1, LONGITUD_REGISTRO);
            encontro_Comenta = 1;
        } else {
            lectura_Comenta(total_Comenta);
            otro = Comenta_CODIGO.trim().equals("") ? "0" : Comenta_CODIGO.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_Comenta = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Comenta(i + 1);
                    otro = Comenta_CODIGO.trim();
                    if (otro.equals(uno)) {
                        encontro_Comenta = i;
                        posicion_Comenta(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Comenta(i + 2);
                            otro = Comenta_CODIGO.trim();
                            if (uno == otro) {
                                encontro_Comenta = i;
                                posicion_Comenta(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Comenta = 0;
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

    public void buscarbinarioChar_Comenta(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Comenta;
        int b = 0;
        lectura_Comenta(1);
        String uno = codigo.toUpperCase().trim();
        String otro = Comenta_CODIGO.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_Comenta(i + 1, LONGITUD_REGISTRO);
            encontro_Comenta = 1;
        } else {
            lectura_Comenta(total_Comenta);
            otro = Comenta_CODIGO.trim().equals("") ? "0" : Comenta_CODIGO.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_Comenta = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Comenta(i + 1);
                    otro = Comenta_CODIGO.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_Comenta = i;
                        posicion_Comenta(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Comenta(i + 2);
                            otro = Comenta_CODIGO.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_Comenta = i;
                                posicion_Comenta(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Comenta = 0;
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
