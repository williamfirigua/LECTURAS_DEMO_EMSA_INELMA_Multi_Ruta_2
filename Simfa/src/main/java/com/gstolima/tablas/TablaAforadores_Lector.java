package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
public class TablaAforadores_Lector {

    String sep="|";
    String Lector_CODIGO;
    String Lector_CLAVE;
    String Lector_DESCRIPCION;
    String Lector_ESTADO;
    String Lector_CRNL;

    public String getLector_CODIGO()
    {
        return Lector_CODIGO;
    }

    public void setLector_CODIGO(String Lector_CODIGO)
    {
        this.Lector_CODIGO = Lector_CODIGO;
    }

    public String getLector_CLAVE()
    {
        return Lector_CLAVE;
    }

    public void setLector_CLAVE(String Lector_CLAVE)
    {
        this.Lector_CLAVE = Lector_CLAVE;
    }

    public String getLector_DESCRIPCION()
    {
        return Lector_DESCRIPCION;
    }

    public void setLector_DESCRIPCION(String Lector_DESCRIPCION)
    {
        this.Lector_DESCRIPCION = Lector_DESCRIPCION;
    }

    public String getLector_ESTADO()
    {
        return Lector_ESTADO;
    }

    public void setLector_ESTADO(String Lector_ESTADO)
    {
        this.Lector_ESTADO = Lector_ESTADO;
    }

    public String getLector_CRNL()
    {
        return Lector_CRNL;
    }

    public void setLector_CRNL(String Lector_CRNL)
    {
        this.Lector_CRNL = Lector_CRNL;
    }

    BufferedReader fin;
    byte[] byteArray;
    String archivo_Lector;

    public String getarchivo_Lector()
    {
        return archivo_Lector;
    }

    public void setarchivo_Lector(String archivo_Lector)
    {
        this.archivo_Lector = archivo_Lector;
    }

    static final int LONGITUD_REGISTRO =46;
    int total_Lector;
    public int gettotal_Lector()
    {
        return total_Lector;
    }

    public void settotal_Lector(int total_Lector)
    {
        this.total_Lector = total_Lector;
    }

    int ultimo_Lector;
    int encontro_Lector;

    public int getencontro_Lector()
    {
        return encontro_Lector;
    }

    public void setencontro_Lector(int encontro_Lector)
    {
        this.encontro_Lector = encontro_Lector;
    }
    String buscar_Lector;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;
    private final byte[] bufferRegistroLector =
            new byte[LONGITUD_REGISTRO];
    public Boolean abrir_Lector(String nombreArchivo)
    {
        if(nombreArchivo.length() == 0)
        {
            return false;
        }
        try
        {
            rFile = new RandomAccessFile(nombreArchivo ,"rw");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        archivo_Lector= nombreArchivo;
        return abrir(archivo_Lector);
    }

    private Boolean abrir(String nombre_archivo)
    {
        try
        {
            int fileSize = (int)rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Lector = fileSize/LONGITUD_REGISTRO;
            return true;
        }
        catch (Exception ex)
        {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_Lector()
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

    public void posicion_Lector(int registro,int tamano)
    {
        try
        {
            rFile.seek((registro-1)*tamano);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_Lector(int registro)
    {

        encontro_Lector = 0;
        posicion_Lector(registro, LONGITUD_REGISTRO);
        try
        {
            rFile.readFully(bufferRegistroLector);
            setLector_CODIGO(new String(bufferRegistroLector,0,4, StandardCharsets.UTF_8));
            setLector_CLAVE(new String(bufferRegistroLector,5,5, StandardCharsets.UTF_8));
            setLector_DESCRIPCION(new String(bufferRegistroLector,11,30, StandardCharsets.UTF_8));
            setLector_ESTADO(new String(bufferRegistroLector,42,1, StandardCharsets.UTF_8));
            setLector_CRNL(new String(bufferRegistroLector,44,2, StandardCharsets.UTF_8));

            ultimo_Lector = registro;
            //fin estructura
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void buscarSecuencial_Lector(String codigo)
    {
        encontro_Lector=0;
        for (int i = 0; i< total_Lector; i++)
        {
            lectura_Lector(i+1);
            if (Integer.parseInt(codigo.trim())==Integer.parseInt(Lector_CODIGO.trim()))
            {
                encontro_Lector = i+1;
                posicion_Lector(i+1, LONGITUD_REGISTRO);
                i = total_Lector + 10;
            }
            else
            {
                encontro_Lector = 0;
            }
        }
        return;
    }

    public void buscarbinario_Lector(String codigo)
    {
        int salir=0;
        int i = 0;
        int t = total_Lector;
        int b = 0;
        lectura_Lector(1);
        String uno = Integer.parseInt(codigo.trim())+"";
        String otro = Integer.parseInt(Lector_CODIGO.trim())+"";
        if (otro.equals(uno))
        {
            posicion_Lector(i+1,LONGITUD_REGISTRO);
            encontro_Lector=1;
        }
        else
        {
            lectura_Lector(total_Lector);
            otro =  Lector_CODIGO.trim().equals("")?"0":Lector_CODIGO.trim();
            if ((Integer.parseInt(uno)) > (Integer.parseInt(otro)))
            {
                encontro_Lector=0;
            }
            else
            {
                while(salir==0)
                {
                    i = (b + t)/2;
                    lectura_Lector(i+1);
                    otro = Lector_CODIGO.trim();
                    if (otro.equals(uno))
                    {
                        encontro_Lector=i;
                        posicion_Lector(i+1,LONGITUD_REGISTRO);
                        salir=1;
                    }
                    else
                    {
                        if (b == i)
                        {
                            lectura_Lector(i+2);
                            otro   = Lector_CODIGO.trim();
                            if (uno == otro)
                            {
                                encontro_Lector=i;
                                posicion_Lector(i+1,LONGITUD_REGISTRO);
                            }
                            else
                            {
                                encontro_Lector = 0;
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

    public void buscarbinarioChar_Lector(String codigo)
    {
        int salir=0;
        int i = 0;
        int t = total_Lector;
        int b = 0;
        lectura_Lector(1);
        String uno = codigo.toUpperCase().trim();
        String otro = Lector_CODIGO.toUpperCase().trim();
        if (otro.compareTo(uno) == 0)
        {
            posicion_Lector(i+1,LONGITUD_REGISTRO);
            encontro_Lector=1;
        }
        else
        {
            lectura_Lector(total_Lector);
            otro =  Lector_CODIGO.trim().equals("")?"0":Lector_CODIGO.toUpperCase().trim();
            if (uno.compareTo(otro) == 1 )
            {
                encontro_Lector = 0;
            }
            else
            {
                while(salir==0)
                {
                    i = (b + t)/2;
                    lectura_Lector(i+1);
                    otro = Lector_CODIGO.toUpperCase().trim();
                    if (uno.compareTo(otro) == 0)
                    {
                        encontro_Lector = i;
                        posicion_Lector(i+1,LONGITUD_REGISTRO);
                        salir=1;
                    }
                    else
                    {
                        if (b == i)
                        {
                            lectura_Lector(i+2);
                            otro = Lector_CODIGO.toUpperCase().trim();
                            if (uno.compareTo(otro) == 0)
                            {
                                encontro_Lector=i;
                                posicion_Lector(i+1,LONGITUD_REGISTRO);
                            }
                            else
                            {
                                encontro_Lector=0;
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