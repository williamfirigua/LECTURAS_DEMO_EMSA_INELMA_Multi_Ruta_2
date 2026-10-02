package com.gstolima.moduloanomalias;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
/// <summary>
/// Descripción breve del archivo a generar...AnomaliaDeNoLectura
/// </summary>
public class AnomaliaDeNoLectura {

    private String AnomaliaDeNoLectura_CODIGO;
    private String AnomaliaDeNoLectura_DESCRIPCION;
    private String AnomaliaDeNoLectura_OBLIGAFOTO; //0 NO OBLIGA
    private String AnomaliaDeNoLectura_REQUIEREINFORME; // 0 NO PIDE
    private String AnomaliaDeNoLectura_LECTURA;
    private String AnomaliaDeNoLectura_NroFotos;
    private String AnomaliaDeNoLectura_CRNL;
   // private String AnomaliaDeNoLectura_;
    private String AnomaliaDeNoLectura_INDICADOR;


    public String getanomaliaDeNoLectura_CODIGO() {
        return AnomaliaDeNoLectura_CODIGO;
    }

    public void setAnomaliaDeNoLectura_CODIGO(String AnomaliaDeNoLectura_CODIGO) {
        this.AnomaliaDeNoLectura_CODIGO = AnomaliaDeNoLectura_CODIGO;
    }

    public String getanomaliaDeNoLectura_DESCRIPCION() {
        return AnomaliaDeNoLectura_DESCRIPCION;
    }

    public void setAnomaliaDeNoLectura_DESCRIPCION(String AnomaliaDeNoLectura_DESCRIPCION) {
        this.AnomaliaDeNoLectura_DESCRIPCION = AnomaliaDeNoLectura_DESCRIPCION;
    }

    public String getAnomaliaDeNoLectura_OBLIGAFOTO() {
        return AnomaliaDeNoLectura_OBLIGAFOTO;
    }

    public void setAnomaliaDeNoLectura_OBLIGAFOTO(String AnomaliaDeNoLectura_OBLIGAFOTO) {
        this.AnomaliaDeNoLectura_OBLIGAFOTO = AnomaliaDeNoLectura_OBLIGAFOTO;
    }

    public String getAnomaliaDeNoLectura_REQUIEREINFORME() {
        return AnomaliaDeNoLectura_REQUIEREINFORME;
    }

    public void setAnomaliaDeNoLectura_REQUIEREINFORME(String AnomaliaDeNoLectura_REQUIEREINFORME) {
        this.AnomaliaDeNoLectura_REQUIEREINFORME = AnomaliaDeNoLectura_REQUIEREINFORME;
    }

    public String getAnomaliaDeNoLectura_LECTURA() {
        return AnomaliaDeNoLectura_LECTURA;
    }

    public void setAnomaliaDeNoLectura_LECTURA(String AnomaliaDeNoLectura_LECTURA) {
        this.AnomaliaDeNoLectura_LECTURA = AnomaliaDeNoLectura_LECTURA;
    }

    public String getAnomaliaDeNoLectura_NroFotos() {
        return AnomaliaDeNoLectura_NroFotos;
    }

    public void setAnomaliaDeNoLectura_NroFotos(String AnomaliaDeNoLectura_NroFotos) {
        this.AnomaliaDeNoLectura_NroFotos = AnomaliaDeNoLectura_NroFotos;
    }

    public String getAnomaliaDeNoLectura_CRNL() {
        return AnomaliaDeNoLectura_CRNL;
    }

    public void setAnomaliaDeNoLectura_CRNL(String AnomaliaDeNoLectura_CRNL) {
        this.AnomaliaDeNoLectura_CRNL = AnomaliaDeNoLectura_CRNL;
    }

  /*  public String getAnomaliaDeNoLectura_() {
        return AnomaliaDeNoLectura_;
    }

    public void setAnomaliaDeNoLectura_(String AnomaliaDeNoLectura_) {
        this.AnomaliaDeNoLectura_ = AnomaliaDeNoLectura_;
    }*/

    public String getAnomaliaDeNoLectura_INDICADOR() {
        return AnomaliaDeNoLectura_INDICADOR;
    }

    public void setAnomaliaDeNoLectura_INDICADOR(String anomaliaDeNoLectura_INDICADOR) {
        AnomaliaDeNoLectura_INDICADOR = anomaliaDeNoLectura_INDICADOR;
    }

    BufferedReader fin;
    byte[] byteArray;
    private String archivo_AnomaliaDeNoLectura;
    static final int LONGITUD_REGISTRO = 36;//se agregan dos caracteres 1 numero fotos y el separador
    private int total_AnomaliaDeNoLectura;
    int ultimo_AnomaliaDeNoLectura;
    private int encontro_AnomaliaDeNoLectura;
    String buscar_AnomaliaDeNoLectura;
    String texto;
    RandomAccessFile rFile;
    private final byte[] bufferRegistroCausa =
            new byte[LONGITUD_REGISTRO];

    public Boolean abrir_AnomaliaDeNoLectura(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setArchivo_AnomaliaDeNoLectura(nombreArchivo);
        return abrir(getArchivo_AnomaliaDeNoLectura());
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            setTotal_AnomaliaDeNoLectura(fileSize / LONGITUD_REGISTRO);
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_AnomaliaDeNoLectura() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void posicion_AnomaliaDeNoLectura(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_AnomaliaDeNoLectura(int registro) {
        setEncontro_AnomaliaDeNoLectura(0);
        posicion_AnomaliaDeNoLectura(registro, LONGITUD_REGISTRO);
        try {
            rFile.readFully(bufferRegistroCausa);
            setAnomaliaDeNoLectura_CODIGO(new String(bufferRegistroCausa,0,2, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_DESCRIPCION(new String(bufferRegistroCausa,3,20, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_OBLIGAFOTO(new String(bufferRegistroCausa,24,1, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_REQUIEREINFORME(new String(bufferRegistroCausa,26,1, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_LECTURA(new String(bufferRegistroCausa,28,1, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_INDICADOR(new String(bufferRegistroCausa,30,1, StandardCharsets.UTF_8));
            setAnomaliaDeNoLectura_NroFotos(new String(bufferRegistroCausa,32,1, StandardCharsets.UTF_8));
            ultimo_AnomaliaDeNoLectura = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarAnomaliaDeNoLectura(String codigo) {
        setEncontro_AnomaliaDeNoLectura(0);
        codigo =codigo.trim();
        for (int i = 0; i < (int) (getTotal_AnomaliaDeNoLectura()); i++) {
            lectura_AnomaliaDeNoLectura(i + 1);
            if (codigo.equals(AnomaliaDeNoLectura_CODIGO.trim())) {
                setEncontro_AnomaliaDeNoLectura(i + 1);
                posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                i = getTotal_AnomaliaDeNoLectura() + 10;
            } else {
                setEncontro_AnomaliaDeNoLectura(0);
            }
        }
        return;
    }

    public void buscarSecuencial_AnomaliaDeNoLectura(String codigo) {
        setEncontro_AnomaliaDeNoLectura(0);
        int cod=Integer.parseInt(codigo.trim());

        for (int i = 0; i < (int) (getTotal_AnomaliaDeNoLectura()); i++) {
            lectura_AnomaliaDeNoLectura(i + 1);
            if (cod == Integer.parseInt(AnomaliaDeNoLectura_CODIGO.trim())) {
                setEncontro_AnomaliaDeNoLectura(i + 1);
                posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                i = getTotal_AnomaliaDeNoLectura() + 10;
            } else {
                setEncontro_AnomaliaDeNoLectura(0);
            }
        }
        return;
    }

    public void buscarbinario_AnomaliaDeNoLectura_(String codigo) {
        int salir = 0;
        int i = 0;
        int t = getTotal_AnomaliaDeNoLectura();
        int b = 0;
        lectura_AnomaliaDeNoLectura(1);
        String uno = Integer.parseInt(codigo.trim()) + "";
        String otro = Integer.parseInt(AnomaliaDeNoLectura_CODIGO.trim()) + "";
        if (otro.equals(uno)) {
            posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
            encontro_AnomaliaDeNoLectura = 1;
        } else {
            lectura_AnomaliaDeNoLectura(getTotal_AnomaliaDeNoLectura());
            otro = AnomaliaDeNoLectura_CODIGO.trim().equals("") ? "0" : AnomaliaDeNoLectura_CODIGO.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro))) {
                encontro_AnomaliaDeNoLectura = 0;
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_AnomaliaDeNoLectura(i + 1);
                    otro = AnomaliaDeNoLectura_CODIGO.trim();
                    if (otro.equals(uno)) {
                        encontro_AnomaliaDeNoLectura = i;
                        posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_AnomaliaDeNoLectura(i + 2);
                            otro = AnomaliaDeNoLectura_CODIGO.trim();
                            if (uno == otro) {
                                encontro_AnomaliaDeNoLectura = i;
                                posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                            } else {
                                encontro_AnomaliaDeNoLectura = 0;
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

    public void buscarbinarioChar_AnomaliaDeNoLectura_II(String codigo) {
        setEncontro_AnomaliaDeNoLectura(0);
        for (int i = 0; i < getTotal_AnomaliaDeNoLectura(); i++) {
            lectura_AnomaliaDeNoLectura(i + 1);
            String otro = AnomaliaDeNoLectura_CODIGO.toUpperCase().trim();
            String uno = codigo.toUpperCase().trim();

            if (otro.equals(uno)) {
                setEncontro_AnomaliaDeNoLectura(i + 1);
                posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                i = getTotal_AnomaliaDeNoLectura() + 10;
            } else {
                setEncontro_AnomaliaDeNoLectura(0);
            }
        }
        return;
    }

    public void buscarbinarioChar_AnomaliaDeNoLectura(String codigo) {
        int salir = 0;
        int i = 0;
        int t = getTotal_AnomaliaDeNoLectura();
        int b = 0;
        lectura_AnomaliaDeNoLectura(1);
        String uno = codigo.toUpperCase().trim();
        String otro = AnomaliaDeNoLectura_CODIGO.toUpperCase().trim();
        if (otro.compareTo(uno) == 0) {
            posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
            setEncontro_AnomaliaDeNoLectura(1);
        } else {
            lectura_AnomaliaDeNoLectura(getTotal_AnomaliaDeNoLectura());
            otro = AnomaliaDeNoLectura_CODIGO.trim().equals("") ? "0" : AnomaliaDeNoLectura_CODIGO.toUpperCase().trim();
            if (uno.compareTo(otro) == 1) {
                setEncontro_AnomaliaDeNoLectura(0);
            } else {
                while (salir == 0) {
                    i = (b + t) / 2;
                    lectura_AnomaliaDeNoLectura(i + 1);
                    otro = AnomaliaDeNoLectura_CODIGO.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0) {
                        setEncontro_AnomaliaDeNoLectura(i);
                        posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                        salir = 1;
                    } else {
                        if (b == i) {
                            lectura_AnomaliaDeNoLectura(i + 2);
                            otro = AnomaliaDeNoLectura_CODIGO.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0) {
                                setEncontro_AnomaliaDeNoLectura(i);
                                posicion_AnomaliaDeNoLectura(i + 1, LONGITUD_REGISTRO);
                            } else {
                                setEncontro_AnomaliaDeNoLectura(0);
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

    public String getArchivo_AnomaliaDeNoLectura() {
        return archivo_AnomaliaDeNoLectura;
    }

    public void setArchivo_AnomaliaDeNoLectura(String archivo_AnomaliaDeNoLectura) {
        this.archivo_AnomaliaDeNoLectura = archivo_AnomaliaDeNoLectura;
    }

    public int getEncontro_AnomaliaDeNoLectura() {
        return encontro_AnomaliaDeNoLectura;
    }

    public void setEncontro_AnomaliaDeNoLectura(int encontro_AnomaliaDeNoLectura) {
        this.encontro_AnomaliaDeNoLectura = encontro_AnomaliaDeNoLectura;
    }

    public int getTotal_AnomaliaDeNoLectura() {
        return total_AnomaliaDeNoLectura;
    }

    public void setTotal_AnomaliaDeNoLectura(int total_AnomaliaDeNoLectura) {
        this.total_AnomaliaDeNoLectura = total_AnomaliaDeNoLectura;
    }
}