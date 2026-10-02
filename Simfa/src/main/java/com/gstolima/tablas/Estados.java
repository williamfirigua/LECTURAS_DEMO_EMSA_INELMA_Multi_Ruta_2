package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Estados {
    String sep = "|";
    String Estados_CODIGO;
    String Estados_DESCRIPCION;
    String Estados_CRNL;

    public String getEstados_CODIGO() {
        return Estados_CODIGO;
    }

    public void setEstados_CODIGO(String Estados_CODIGO) {
        this.Estados_CODIGO = Estados_CODIGO;
    }

    public String getEstados_DESCRIPCION() {
        return Estados_DESCRIPCION;
    }

    public void setEstados_DESCRIPCION(String Estados_DESCRIPCION) {
        this.Estados_DESCRIPCION = Estados_DESCRIPCION;
    }

    public String getEstados_CRNL() {
        return Estados_CRNL;
    }

    public void setEstados_CRNL(String Estados_CRNL) {
        this.Estados_CRNL = Estados_CRNL;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_Estados;

    public String getarchivo_Estados() {
        return archivo_Estados;
    }

    public void setarchivo_Estados(String archivo_Estados) {
        this.archivo_Estados = archivo_Estados;
    }

    static final int LONGITUD_REGISTRO = 35;
    int total_Estados;

    public int gettotal_Estados() {
        return total_Estados;
    }

    public void settotal_Estados(int total_Estados) {
        this.total_Estados = total_Estados;
    }

    int ultimo_Estados;
    int encontro_Estados;

    public int getencontro_Estados() {
        return encontro_Estados;
    }

    public void setencontro_Estados(int encontro_Estados) {
        this.encontro_Estados = encontro_Estados;
    }

    String buscar_Estados;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_Estados(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Estados = nombreArchivo;
        return abrir(archivo_Estados);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Estados = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_Estados() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void posicion_Estados(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_Estados(int registro) {
        String[] campos;
        encontro_Estados = 0;
        posicion_Estados(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setEstados_CODIGO(texto.substring(0, 1));
            setEstados_DESCRIPCION(texto.substring(2, 32));
            setEstados_CRNL(texto.substring(33, 35));
            ultimo_Estados = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_Estados(String codigo) {
        encontro_Estados = 0;
        for (int i = 0; i < (int) (total_Estados); i++) {
            lectura_Estados(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(Estados_CODIGO.trim())) {
                encontro_Estados = i + 1;
                posicion_Estados(i + 1, LONGITUD_REGISTRO);
                i = total_Estados + 10;
            } else {
                encontro_Estados = 0;
            }
        }
        return;
    }


    public void buscarbinario_Estados(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Estados;
        int b = 0;
        lectura_Estados(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(Estados_CODIGO.trim()) + "";
        if (otro.equals(uno)) {
            posicion_Estados(i + 1, LONGITUD_REGISTRO);
            encontro_Estados = 1;
        } else {
            lectura_Estados(total_Estados);
            otro = Estados_CODIGO.trim().equals("") ? "0" : Estados_CODIGO.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_Estados = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Estados(i + 1);
                    otro = Estados_CODIGO.trim();
                    if (otro.equals(uno)) {
                        encontro_Estados = i;
                        posicion_Estados(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Estados(i + 2);
                            otro = Estados_CODIGO.trim();
                            if (uno == otro) {
                                encontro_Estados = i;
                                posicion_Estados(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Estados = 0;
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


    public void buscarbinarioChar_Estados(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Estados;
        int b = 0;
        lectura_Estados(1);
        String uno = codigo.toUpperCase().trim();
        String otro = Estados_CODIGO.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_Estados(i + 1, LONGITUD_REGISTRO);
            encontro_Estados = 1;
        } else {
            lectura_Estados(total_Estados);
            otro = Estados_CODIGO.trim().equals("") ? "0" : Estados_CODIGO.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_Estados = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Estados(i + 1);
                    otro = Estados_CODIGO.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_Estados = i;
                        posicion_Estados(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Estados(i + 2);
                            otro = Estados_CODIGO.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_Estados = i;
                                posicion_Estados(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Estados = 0;
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


