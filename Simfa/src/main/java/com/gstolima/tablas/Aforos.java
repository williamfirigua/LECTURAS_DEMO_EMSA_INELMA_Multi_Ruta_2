package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/// <summary>
/// Descripcion breve del archivo a generar...aforos
/// </summary>
public class Aforos {
    String sep = ";";
    String aforos_numeroacta;
    String aforos_codigoaforo;
    String aforos_cantidad;
    String aforos_w;
    String aforos_tipoconexion;
    String aforos_lector;
    String aforos_codigocuenta;
    String aforos_ciclo;
    String aforos_descripcionaforo;
    String aforos_mes;
    String aforos_anno;
    String aforos_CRNL;

    public String getaforos_NUMEROACTA() {
        return aforos_numeroacta;
    }

    public void setaforos_numeroacta(String aforos_numeroacta) {
        this.aforos_numeroacta = aforos_numeroacta;
    }

    public String getaforos_CODIGOAFORO() {
        return aforos_codigoaforo;
    }

    public void setaforos_codigoaforo(String aforos_codigoaforo) {
        this.aforos_codigoaforo = aforos_codigoaforo;
    }

    public String getaforos_CANTIDAD() {
        return aforos_cantidad;
    }

    public void setaforos_cantidad(String aforos_cantidad) {
        this.aforos_cantidad = aforos_cantidad;
    }

    public String getaforos_W() {
        return aforos_w;
    }

    public void setaforos_w(String aforos_w) {
        this.aforos_w = aforos_w;
    }

    public String getaforos_TIPOCONEXION() {
        return aforos_tipoconexion;
    }

    public void setaforos_tipoconexion(String aforos_tipoconexion) {
        this.aforos_tipoconexion = aforos_tipoconexion;
    }

    public String getaforos_LECTOR() {
        return aforos_lector;
    }

    public void setaforos_lector(String aforos_lector) {
        this.aforos_lector = aforos_lector;
    }

    public String getaforos_CODIGOCUENTA() {
        return aforos_codigocuenta;
    }

    public void setaforos_codigocuenta(String aforos_codigocuenta) {
        this.aforos_codigocuenta = aforos_codigocuenta;
    }

    public String getaforos_CICLO() {
        return aforos_ciclo;
    }

    public void setaforos_ciclo(String aforos_ciclo) {
        this.aforos_ciclo = aforos_ciclo;
    }

    public String getaforos_DESCRIPCIONAFORO() {
        return aforos_descripcionaforo;
    }

    public void setaforos_descripcionaforo(String aforos_descripcionaforo) {
        this.aforos_descripcionaforo = aforos_descripcionaforo;
    }

    public String getaforos_MES() {
        return aforos_mes;
    }

    public void setaforos_mes(String aforos_mes) {
        this.aforos_mes = aforos_mes;
    }

    public String getaforos_ANNO() {
        return aforos_anno;
    }

    public void setaforos_anno(String aforos_anno) {
        this.aforos_anno = aforos_anno;
    }

    public String getaforos_CRNL() {
        return aforos_CRNL;
    }

    public void setaforos_CRNL(String aforos_CRNL) {
        this.aforos_CRNL = aforos_CRNL;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_Aforos;
    public static final int LONGITUD_REGISTRO = 111;//110se adiciona un caracter a cuenta para celsia valle
    public int total_Aforos;
    public int ultimo_Aforos;
    int encontro_Aforos;
    String buscar_Aforos;
    String texto;
    RandomAccessFile rFile;


    public Boolean abrir_Aforos(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Aforos = nombreArchivo;
        return abrir(archivo_Aforos);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Aforos = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }


    public void Cerrar_Aforos() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean escribir_Aforos(int posicion) {
        posicion_Aforos(posicion, LONGITUD_REGISTRO);
        rellenar_Aforos();
        String texto = aforos_numeroacta + sep + aforos_codigoaforo + sep + aforos_cantidad + sep + aforos_w + sep + aforos_tipoconexion + sep +
                aforos_lector + sep + aforos_codigocuenta + sep + aforos_ciclo + sep + aforos_descripcionaforo + sep + aforos_mes + sep +
                aforos_anno + sep + aforos_CRNL;
        try {
            //int x= texto.length();
            if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                rFile.writeBytes(texto);
                System.out.println("Los Datos fueron grabados correctamente");
                return true;
            } else {
                System.out.println("Se presento problema al escribir en el archivo; longitud Errada: tamano texto: " + texto.length() + texto);
                return false;
            }
        } catch (IOException ioe) {
            System.out.println("Se presento problema al escribir en el archivo Longitud Errada: " + LONGITUD_REGISTRO);
            return false;
        }
    }


    public void rellenar_Aforos() {
        try {
            aforos_numeroacta = String.format("%-15s", aforos_numeroacta);
            aforos_codigoaforo = String.format("%-3s", aforos_codigoaforo);
            aforos_cantidad = String.format("%-3s", aforos_cantidad);
            aforos_w = String.format("%-6s", aforos_w);
            aforos_tipoconexion = String.format("%-1s", aforos_tipoconexion);
            aforos_lector = String.format("%-11s", aforos_lector);
            aforos_codigocuenta = String.format("%-10s", aforos_codigocuenta);
            aforos_ciclo = String.format("%-3s", aforos_ciclo);
            aforos_descripcionaforo = String.format("%-40s", aforos_descripcionaforo);//Ax: ojo este campo se usa para guardar enviado o no en su ultimo caracter, pero este 'objeto' no se usa en menuliquidacion, se usa generica
            aforos_mes = String.format("%-2s", aforos_mes);
            aforos_anno = String.format("%-4s", aforos_anno);
            aforos_CRNL = "\r\n";//= String.format("%-2s", aforos_CRNL);
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo aforos.dat..");
            e.printStackTrace();
        }

        return;
    }


    public void posicion_Aforos(int registro, int tamano) {
        try {
            rFile.seek((registro - 1) * tamano);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_Aforos(int registro) {
        String[] campos;
        encontro_Aforos = 0;
        posicion_Aforos(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setaforos_numeroacta(texto.substring(0, 15));
            setaforos_codigoaforo(texto.substring(16, 19));
            setaforos_cantidad(texto.substring(20, 23));
            setaforos_w(texto.substring(24, 30));
            setaforos_tipoconexion(texto.substring(31, 32));
            setaforos_lector(texto.substring(33, 44));
            setaforos_codigocuenta(texto.substring(45, 55));
            setaforos_ciclo(texto.substring(56, 59));
            setaforos_descripcionaforo(texto.substring(60,100));
            setaforos_mes(texto.substring(101, 103));
            setaforos_anno(texto.substring(104, 108));
            setaforos_CRNL(texto.substring(109, 111));
            ultimo_Aforos = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_Aforos(String codigo) {
        encontro_Aforos = 0;
        for (int i = 0; i < (int) (total_Aforos); i++) {
            lectura_Aforos(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(aforos_codigoaforo.trim())) {
                encontro_Aforos = i + 1;
                posicion_Aforos(i + 1, LONGITUD_REGISTRO);
                i = total_Aforos + 10;
            } else {
                encontro_Aforos = 0;
            }
        }
        return;
    }


    public void buscarbinario_Aforos(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Aforos;
        int b = 0;
        lectura_Aforos(1);
        String uno = codigo.trim();
        String otro = aforos_codigoaforo.trim();
        if (otro.equals(uno)) {
            posicion_Aforos(i + 1, LONGITUD_REGISTRO);
            encontro_Aforos = 1;
        } else {
            lectura_Aforos(total_Aforos);
            otro = aforos_codigoaforo.trim().equals("") ? "0" : aforos_codigoaforo.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_Aforos = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Aforos(i + 1);
                    otro = aforos_codigoaforo.trim();
                    if (otro.equals(uno)) {
                        encontro_Aforos = i;
                        posicion_Aforos(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Aforos(i + 2);
                            otro = aforos_codigoaforo.trim();
                            if (uno == otro) {
                                encontro_Aforos = i;
                                posicion_Aforos(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Aforos = 0;
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


    public void buscarbinarioChar_Aforos(String codigo) {
        int salir = 0;
        int i = 0;
        int t = total_Aforos;
        int b = 0;
        lectura_Aforos(1);
        String uno = codigo.toUpperCase().trim();
        String otro = aforos_codigoaforo.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_Aforos(i + 1, LONGITUD_REGISTRO);
            encontro_Aforos = 1;
        } else {
            lectura_Aforos(total_Aforos);
            otro = aforos_codigoaforo.trim().equals("") ? "0" : aforos_codigoaforo.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                encontro_Aforos = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_Aforos(i + 1);
                    otro = aforos_codigoaforo.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        encontro_Aforos = i;
                        posicion_Aforos(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_Aforos(i + 2);
                            otro = aforos_codigoaforo.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                encontro_Aforos = i;
                                posicion_Aforos(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_Aforos = 0;
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
