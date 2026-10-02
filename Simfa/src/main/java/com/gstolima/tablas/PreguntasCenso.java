package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PreguntasCenso {
    String sep="|";
    String PreguntasCenso_NumeroPregunta;
    String PreguntasCenso_Pregunta;
    String PreguntasCenso_TipoPregunta;
    String PreguntasCenso_Respuesta1;
    String PreguntasCenso_DescRespuesta1;
    String PreguntasCenso_Respuesta2;
    String PreguntasCenso_DescRespuesta2;
    String PreguntasCenso_Respuesta3;
    String PreguntasCenso_DescRespuesta3;
    String PreguntasCenso_CRNL;
    public String getPreguntasCenso_NUMEROPREGUNTA()
    {
        return PreguntasCenso_NumeroPregunta;
    }
    public void setPreguntasCenso_NumeroPregunta(String PreguntasCenso_NumeroPregunta)
    {
        this.PreguntasCenso_NumeroPregunta = PreguntasCenso_NumeroPregunta;
    }
    public String getPreguntasCenso_PREGUNTA()
    {
        return PreguntasCenso_Pregunta;
    }
    public void setPreguntasCenso_Pregunta(String PreguntasCenso_Pregunta)
    {
        this.PreguntasCenso_Pregunta = PreguntasCenso_Pregunta;
    }
    public String getPreguntasCenso_TIPOPREGUNTA()
    {
        return PreguntasCenso_TipoPregunta;
    }
    public void setPreguntasCenso_TipoPregunta(String PreguntasCenso_TipoPregunta)
    {
        this.PreguntasCenso_TipoPregunta = PreguntasCenso_TipoPregunta;
    }
    public String getPreguntasCenso_RESPUESTA1()
    {
        return PreguntasCenso_Respuesta1;
    }
    public void setPreguntasCenso_Respuesta1(String PreguntasCenso_Respuesta1)
    {
        this.PreguntasCenso_Respuesta1 = PreguntasCenso_Respuesta1;
    }
    public String getPreguntasCenso_DESCRESPUESTA1()
    {
        return PreguntasCenso_DescRespuesta1;
    }
    public void setPreguntasCenso_DescRespuesta1(String PreguntasCenso_DescRespuesta1)
    {
        this.PreguntasCenso_DescRespuesta1 = PreguntasCenso_DescRespuesta1;
    }
    public String getPreguntasCenso_RESPUESTA2()
    {
        return PreguntasCenso_Respuesta2;
    }
    public void setPreguntasCenso_Respuesta2(String PreguntasCenso_Respuesta2)
    {
        this.PreguntasCenso_Respuesta2 = PreguntasCenso_Respuesta2;
    }
    public String getPreguntasCenso_DESCRESPUESTA2()
    {
        return PreguntasCenso_DescRespuesta2;
    }
    public void setPreguntasCenso_DescRespuesta2(String PreguntasCenso_DescRespuesta2)
    {
        this.PreguntasCenso_DescRespuesta2 = PreguntasCenso_DescRespuesta2;
    }
    public String getPreguntasCenso_RESPUESTA3()
    {
        return PreguntasCenso_Respuesta3;
    }
    public void setPreguntasCenso_Respuesta3(String PreguntasCenso_Respuesta3)
    {
        this.PreguntasCenso_Respuesta3 = PreguntasCenso_Respuesta3;
    }
    public String getPreguntasCenso_DESCRESPUESTA3()
    {
        return PreguntasCenso_DescRespuesta3;
    }
    public void setPreguntasCenso_DescRespuesta3(String PreguntasCenso_DescRespuesta3)
    {
        this.PreguntasCenso_DescRespuesta3 = PreguntasCenso_DescRespuesta3;
    }
    public String getPreguntasCenso_CRNL()
    {
        return PreguntasCenso_CRNL;
    }
    public void setPreguntasCenso_CRNL(String PreguntasCenso_CRNL)
    {
        this.PreguntasCenso_CRNL = PreguntasCenso_CRNL;
    }



    BufferedReader fin;
    byte[] byteArray;
    String archivo_PreguntasCenso;
    public String getarchivo_PreguntasCenso()
    {
        return archivo_PreguntasCenso;
    }
    public void setarchivo_PreguntasCenso(String archivo_PreguntasCenso)
    {
        this.archivo_PreguntasCenso = archivo_PreguntasCenso;
    }
    static final int LONGITUD_REGISTRO =220;
    int total_PreguntasCenso;
    public int gettotal_PreguntasCenso()
    {
        return total_PreguntasCenso;
    }
    public void settotal_PreguntasCenso(int total_PreguntasCenso)
    {
        this.total_PreguntasCenso = total_PreguntasCenso;
    }
    int ultimo_PreguntasCenso;
    int encontro_PreguntasCenso;
    public int getencontro_PreguntasCenso()
    {
        return encontro_PreguntasCenso;
    }
    public void setencontro_PreguntasCenso(int encontro_PreguntasCenso)
    {
        this.encontro_PreguntasCenso = encontro_PreguntasCenso;
    }
    String buscar_PreguntasCenso;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;

    public Boolean abrir_PreguntasCenso(String nombreArchivo)
    {
        if(nombreArchivo.length() == 0)
        {
            return false;
        }
        try
        {
            rFile = new RandomAccessFile(nombreArchivo ,"rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        archivo_PreguntasCenso= nombreArchivo;
        return abrir(archivo_PreguntasCenso);
    }

    private Boolean abrir(String nombre_archivo)
    {
        try
        {
            int fileSize = (int)rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_PreguntasCenso = fileSize/LONGITUD_REGISTRO;
            return true;
        }
        catch (Exception ex)
        {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_PreguntasCenso()
    {
// cerrar el archivo abierto
        try
        {
            rFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void posicion_PreguntasCenso(int registro,int tamaño)
    {
        try
        {
            rFile.seek((registro-1)*tamaño);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    //Escribir los registros atual en el archivo en posicion
    public void lectura_PreguntasCenso(int registro)
    {
        String[] campos;
        encontro_PreguntasCenso = 0;
        posicion_PreguntasCenso(registro, LONGITUD_REGISTRO);
        try
        {
            int fileSize = (int)rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setPreguntasCenso_NumeroPregunta(texto.substring(0,2));
            setPreguntasCenso_Pregunta(texto.substring(3,53));
            setPreguntasCenso_TipoPregunta(texto.substring(54,55));
            setPreguntasCenso_Respuesta1(texto.substring(56,58));
            setPreguntasCenso_DescRespuesta1(texto.substring(59,109));
            setPreguntasCenso_Respuesta2(texto.substring(110,112));
            setPreguntasCenso_DescRespuesta2(texto.substring(113,163));
            setPreguntasCenso_Respuesta3(texto.substring(164,166));
            setPreguntasCenso_DescRespuesta3(texto.substring(167,217));
            setPreguntasCenso_CRNL(texto.substring(218,220));
            ultimo_PreguntasCenso = registro;
            //fin estructura
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public void buscarSecuencial_PreguntasCenso(String codigo)
    {
        encontro_PreguntasCenso=0;
        for (int i = 0; i<(int)(total_PreguntasCenso); i++)
        {
            lectura_PreguntasCenso(i+1);
            if (Integer.parseInt(codigo.trim())==Integer.parseInt(PreguntasCenso_NumeroPregunta.trim()))
            {
                encontro_PreguntasCenso = i+1;
                posicion_PreguntasCenso(i+1, LONGITUD_REGISTRO);
                i = total_PreguntasCenso + 10;
            }
            else
            {
                encontro_PreguntasCenso = 0;
            }
        }
        return;
    }


    public void buscarbinario_PreguntasCenso(String codigo)
    {
        int salir=0;
        int i = 0;
        int t = total_PreguntasCenso;
        int b = 0;
        lectura_PreguntasCenso(1);
        String uno = Integer.parseInt(codigo.trim())+"";
        String otro = Integer.parseInt(PreguntasCenso_NumeroPregunta.trim())+"";
        if (otro.equals(uno))
        {
            posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
            encontro_PreguntasCenso=1;
        }
        else
        {
            lectura_PreguntasCenso(total_PreguntasCenso);
            otro =  PreguntasCenso_NumeroPregunta.trim().equals("")?"0":PreguntasCenso_NumeroPregunta.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro)))
            {
                encontro_PreguntasCenso=0;
            }
            else
            {
                while(salir==0)
                {
                    i = (b + t)/2;
                    lectura_PreguntasCenso(i+1);
                    otro = PreguntasCenso_NumeroPregunta.trim();
                    if (otro.equals(uno))
                    {
                        encontro_PreguntasCenso=i;
                        posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
                        salir=1;
                    }
                    else
                    {
                        if (b == i)
                        {
                            lectura_PreguntasCenso(i+2);
                            otro   = PreguntasCenso_NumeroPregunta.trim();
                            if (uno == otro)
                            {
                                encontro_PreguntasCenso=i;
                                posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
                            }
                            else
                            {
                                encontro_PreguntasCenso = 0;
                            }
                            salir=1;
                        }
                        else
                        {
                            if (Integer.parseInt(uno) > Integer.parseInt(otro))
                            {
                                b = i;
                            }
                            else
                            {
                                if (i-b==1)
                                {
                                    b=i;
                                }
                                t = i;
                            }
                        }
                    }
                }
            }
        }
    }



    public void buscarbinarioChar_PreguntasCenso(String codigo)
    {
        int salir=0;
        int i = 0;
        int t = total_PreguntasCenso;
        int b = 0;
        lectura_PreguntasCenso(1);
        String uno = codigo.toUpperCase().trim();
        String otro = PreguntasCenso_NumeroPregunta.toUpperCase().trim();
        if (otro.compareTo(uno) == 0)
        {
            posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
            encontro_PreguntasCenso=1;
        }
        else
        {
            lectura_PreguntasCenso(total_PreguntasCenso);
            otro =  PreguntasCenso_NumeroPregunta.trim().equals("")?"0":PreguntasCenso_NumeroPregunta.toUpperCase().trim();
            if (uno.compareTo(otro) == 1 )
            {
                encontro_PreguntasCenso = 0;
            }
            else
            {
                while(salir==0)
                {
                    i = (b + t)/2;
                    lectura_PreguntasCenso(i+1);
                    otro = PreguntasCenso_NumeroPregunta.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0)
                    {
                        encontro_PreguntasCenso = i;
                        posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
                        salir=1;
                    }
                    else
                    {
                        if (b == i)
                        {
                            lectura_PreguntasCenso(i+2);
                            otro = PreguntasCenso_NumeroPregunta.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0)
                            {
                                encontro_PreguntasCenso=i;
                                posicion_PreguntasCenso(i+1,LONGITUD_REGISTRO);
                            }
                            else
                            {
                                encontro_PreguntasCenso=0;
                            }
                            salir=1;
                        }
                        else
                        {
                            if (uno.compareTo(otro) == 1)
                            {
                                b = i;
                            }
                            else
                            {
                                if (i-b==1)
                                {
                                    b=i;
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

