package com.gstolima.moduloanomalias;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/// <summary>
/// Descripción breve del archivo a generar...CodigosSac
/// </summary>
public class CodigosSac {
    private String CodigosSac_CODIGO;
    private String CodigosSac_DESCRIPCION;
    private String CodigosSac_INDICADOR; //ES INFERERIOR O SUPERIOR
    private String CodigosSac_CRNL;
    //private String CodigosSac_;

    public String getCodigosSac_CODIGO() {
        return CodigosSac_CODIGO;
    }

    public void setCodigosSac_CODIGO(String CodigosSac_CODIGO) {
        this.CodigosSac_CODIGO = CodigosSac_CODIGO;
    }

    public String getCodigosSac_DESCRIPCION() {
        return CodigosSac_DESCRIPCION;
    }

    public void setCodigosSac_DESCRIPCION(String CodigosSac_DESCRIPCION) {
        this.CodigosSac_DESCRIPCION = CodigosSac_DESCRIPCION;
    }

    public String getCodigosSac_INDICADOR() {
        return CodigosSac_INDICADOR;
    }

    public void setCodigosSac_INDICADOR(String CodigosSac_INDICADOR) {
        this.CodigosSac_INDICADOR = CodigosSac_INDICADOR;
    }


    public String getCodigosSac_CRNL() {
        return CodigosSac_CRNL;
    }

    public void setCodigosSac_CRNL(String CodigosSac_CRNL) {
        this.CodigosSac_CRNL = CodigosSac_CRNL;
    }

    BufferedReader fin;
    byte[] byteArray;
    private String archivo_CodigosSac;
    static final int LONGITUD_REGISTRO = 69;
    private int total_CodigosSac;
    int ultimo_CodigosSac;
    private int encontro_CodigosSac;
    String buscar_CodigosSac;
    String texto;
    RandomAccessFile rFile;

    public Boolean abrir_CodigosSac(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setArchivo_CodigosSac(nombreArchivo);
        return abrir(getArchivo_CodigosSac());
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            setTotal_CodigosSac(fileSize / LONGITUD_REGISTRO);
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_CodigosSac() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_CodigosSac(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_CodigosSac(int registro) {
        setEncontro_CodigosSac(0);
        posicion_CodigosSac(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setCodigosSac_CODIGO(texto.substring(0, 3));
            setCodigosSac_DESCRIPCION(texto.substring(4, 64));
            setCodigosSac_INDICADOR(texto.substring(65, 66));
            setCodigosSac_CRNL(texto.substring(67, 69));

            ultimo_CodigosSac = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarCodigosSac(String codigo) {
        setEncontro_CodigosSac(0);
        codigo =codigo.trim();
        for (int i = 0; i < (int) (getTotal_CodigosSac()); i++) {
            lectura_CodigosSac(i + 1);
            if (codigo.equals(CodigosSac_CODIGO.trim())) {
                setEncontro_CodigosSac(i + 1);
                posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
                i = getTotal_CodigosSac() + 10;
            } else {
                setEncontro_CodigosSac(0);
            }
        }
        return;
    }

    public void buscarSecuencial_CodigosSac(String codigo) {
        setEncontro_CodigosSac(0);
        String cod=codigo.trim();

        for (int i = 0; i < (int) (getTotal_CodigosSac()); i++) {
            lectura_CodigosSac(i + 1);
            if (cod.equals(CodigosSac_CODIGO.trim())) {
                setEncontro_CodigosSac(i + 1);
                posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
                i = getTotal_CodigosSac() + 10;
            } else {
                setEncontro_CodigosSac(0);
            }
        }
        return;
    }


    public void buscarbinarioChar_CodigosSac_II(String codigo) {
        setEncontro_CodigosSac(0);
        for (int i = 0; i < getTotal_CodigosSac(); i++) {
            lectura_CodigosSac(i + 1);
            String otro = CodigosSac_CODIGO.toUpperCase().trim();
            String uno = codigo.toUpperCase().trim();

            if (otro.equals(uno)) {
                setEncontro_CodigosSac(i + 1);
                posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
                i = getTotal_CodigosSac() + 10;
            } else {
                setEncontro_CodigosSac(0);
            }
        }
        return;
    }

    public void buscarbinarioChar_CodigosSac(String codigo) {
        int salir = 0;
        int i = 0;
        int t = getTotal_CodigosSac();
        int b = 0;
        lectura_CodigosSac(1);
        String uno = codigo.toUpperCase().trim();
        String otro = CodigosSac_CODIGO.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
            setEncontro_CodigosSac(1);
        } else {
            lectura_CodigosSac(getTotal_CodigosSac());
            otro = CodigosSac_CODIGO.trim().equals("") ? "0" : CodigosSac_CODIGO.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                setEncontro_CodigosSac(0);
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_CodigosSac(i + 1);
                    otro = CodigosSac_CODIGO.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        setEncontro_CodigosSac(i);
                        posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_CodigosSac(i + 2);
                            otro = CodigosSac_CODIGO.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                setEncontro_CodigosSac(i);
                                posicion_CodigosSac(i + 1, LONGITUD_REGISTRO);
                            } else {
                                setEncontro_CodigosSac(0);
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

    public String getArchivo_CodigosSac() {
        return archivo_CodigosSac;
    }

    public void setArchivo_CodigosSac(String archivo_CodigosSac) {
        this.archivo_CodigosSac = archivo_CodigosSac;
    }

    public int getEncontro_CodigosSac() {
        return encontro_CodigosSac;
    }

    public void setEncontro_CodigosSac(int encontro_CodigosSac) {
        this.encontro_CodigosSac = encontro_CodigosSac;
    }

    public int getTotal_CodigosSac() {
        return total_CodigosSac;
    }

    public void setTotal_CodigosSac(int total_CodigosSac) {
        this.total_CodigosSac = total_CodigosSac;
    }
}