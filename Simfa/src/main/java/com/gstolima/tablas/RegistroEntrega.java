package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RegistroEntrega {
    String sep = "|";
    String RegistroEntrega_CodigoGrupoEntrega;
    String RegistroEntrega_Ciclo;
    String RegistroEntrega_zona;
    String RegistroEntrega_Municipio;
    String RegistroEntrega_Seccion;
    String RegistroEntrega_Ruta;
    String RegistroEntrega_CodCliente;
    String RegistroEntrega_Nombre;
    String RegistroEntrega_Telefono;
    String RegistroEntrega_CantidadCodigos;
    String RegistroEntrega_FechaEntrega;
    String RegistroEntrega_HoraEntrega;
    String RegistroEntrega_Latitud;
    String RegistroEntrega_Longitud;
    String RegistroEntrega_Satelites;
    String RegistroEntrega_Distancia;
    String RegistroEntrega_Lector;
    String RegistroEntrega_CRNL;

    public String getRegistroEntrega_CODIGOGRUPOENTREGA() {
        return RegistroEntrega_CodigoGrupoEntrega;
    }

    public void setRegistroEntrega_CodigoGrupoEntrega(String RegistroEntrega_CodigoGrupoEntrega) {
        this.RegistroEntrega_CodigoGrupoEntrega = RegistroEntrega_CodigoGrupoEntrega;
    }

    public String getRegistroEntrega_CICLO() {
        return RegistroEntrega_Ciclo;
    }

    public void setRegistroEntrega_Ciclo(String RegistroEntrega_Ciclo) {
        this.RegistroEntrega_Ciclo = RegistroEntrega_Ciclo;
    }

    public String getRegistroEntrega_ZONA() {
        return RegistroEntrega_zona;
    }

    public void setRegistroEntrega_zona(String RegistroEntrega_zona) {
        this.RegistroEntrega_zona = RegistroEntrega_zona;
    }

    public String getRegistroEntrega_MUNICIPIO() {
        return RegistroEntrega_Municipio;
    }

    public void setRegistroEntrega_Municipio(String RegistroEntrega_Municipio) {
        this.RegistroEntrega_Municipio = RegistroEntrega_Municipio;
    }

    public String getRegistroEntrega_SECCION() {
        return RegistroEntrega_Seccion;
    }

    public void setRegistroEntrega_Seccion(String RegistroEntrega_Seccion) {
        this.RegistroEntrega_Seccion = RegistroEntrega_Seccion;
    }

    public String getRegistroEntrega_RUTA() {
        return RegistroEntrega_Ruta;
    }

    public void setRegistroEntrega_Ruta(String RegistroEntrega_Ruta) {
        this.RegistroEntrega_Ruta = RegistroEntrega_Ruta;
    }

    public String getRegistroEntrega_CODCLIENTE() {
        return RegistroEntrega_CodCliente;
    }

    public void setRegistroEntrega_CodCliente(String RegistroEntrega_CodCliente) {
        this.RegistroEntrega_CodCliente = RegistroEntrega_CodCliente;
    }

    public String getRegistroEntrega_NOMBRE() {
        return RegistroEntrega_Nombre;
    }

    public void setRegistroEntrega_Nombre(String RegistroEntrega_Nombre) {
        this.RegistroEntrega_Nombre = RegistroEntrega_Nombre;
    }

    public String getRegistroEntrega_TELEFONO() {
        return RegistroEntrega_Telefono;
    }

    public void setRegistroEntrega_Telefono(String RegistroEntrega_Telefono) {
        this.RegistroEntrega_Telefono = RegistroEntrega_Telefono;
    }

    public String getRegistroEntrega_CANTIDADCODIGOS() {
        return RegistroEntrega_CantidadCodigos;
    }

    public void setRegistroEntrega_CantidadCodigos(String RegistroEntrega_CantidadCodigos) {
        this.RegistroEntrega_CantidadCodigos = RegistroEntrega_CantidadCodigos;
    }

    public String getRegistroEntrega_FECHAENTREGA() {
        return RegistroEntrega_FechaEntrega;
    }

    public void setRegistroEntrega_FechaEntrega(String RegistroEntrega_FechaEntrega) {
        this.RegistroEntrega_FechaEntrega = RegistroEntrega_FechaEntrega;
    }

    public String getRegistroEntrega_HORAENTREGA() {
        return RegistroEntrega_HoraEntrega;
    }

    public void setRegistroEntrega_HoraEntrega(String RegistroEntrega_HoraEntrega) {
        this.RegistroEntrega_HoraEntrega = RegistroEntrega_HoraEntrega;
    }

    public String getRegistroEntrega_LATITUD() {
        return RegistroEntrega_Latitud;
    }

    public void setRegistroEntrega_Latitud(String RegistroEntrega_Latitud) {
        this.RegistroEntrega_Latitud = RegistroEntrega_Latitud;
    }

    public String getRegistroEntrega_LONGITUD() {
        return RegistroEntrega_Longitud;
    }

    public void setRegistroEntrega_Longitud(String RegistroEntrega_Longitud) {
        this.RegistroEntrega_Longitud = RegistroEntrega_Longitud;
    }

    public String getRegistroEntrega_SATELITES() {
        return RegistroEntrega_Satelites;
    }

    public void setRegistroEntrega_Satelites(String RegistroEntrega_Satelites) {
        this.RegistroEntrega_Satelites = RegistroEntrega_Satelites;
    }

    public String getRegistroEntrega_DISTANCIA() {
        return RegistroEntrega_Distancia;
    }

    public void setRegistroEntrega_Distancia(String RegistroEntrega_Distancia) {
        this.RegistroEntrega_Distancia = RegistroEntrega_Distancia;
    }

    public String getRegistroEntrega_LECTOR() {
        return RegistroEntrega_Lector;
    }

    public void setRegistroEntrega_Lector(String RegistroEntrega_Lector) {
        this.RegistroEntrega_Lector = RegistroEntrega_Lector;
    }

    public String getRegistroEntrega_CRNL() {
        return RegistroEntrega_CRNL;
    }

    public void setRegistroEntrega_CRNL(String RegistroEntrega_CRNL) {
        this.RegistroEntrega_CRNL = RegistroEntrega_CRNL;
    }


    BufferedReader fin;
    byte[] byteArray;
    String archivo_RegistroEntrega;

    public String getarchivo_RegistroEntrega() {
        return archivo_RegistroEntrega;
    }

    public void setarchivo_RegistroEntrega(String archivo_RegistroEntrega) {
        this.archivo_RegistroEntrega = archivo_RegistroEntrega;
    }

    static final int LONGITUD_REGISTRO = 202; //200 Se modifica ciclo a 3 digitos
    int total_RegistroEntrega;

    public int gettotal_RegistroEntrega() {
        return total_RegistroEntrega;
    }

    public void settotal_RegistroEntrega(int total_RegistroEntrega) {
        this.total_RegistroEntrega = total_RegistroEntrega;
    }

    int ultimo_RegistroEntrega;
    int encontro_RegistroEntrega;

    public int getencontro_RegistroEntrega() {
        return encontro_RegistroEntrega;
    }

    public void setencontro_RegistroEntrega(int encontro_RegistroEntrega) {
        this.encontro_RegistroEntrega = encontro_RegistroEntrega;
    }

    String buscar_RegistroEntrega;
    String texto;
//Creacion arreglo uso de archivos;

    RandomAccessFile rFile;


    public Boolean abrir_RegistroEntrega(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_RegistroEntrega = nombreArchivo;
        return abrir(archivo_RegistroEntrega);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_RegistroEntrega = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            //Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_RegistroEntrega() {
// cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribir_RegistroEntrega(int posicion) {
        posicion_RegistroEntrega(posicion, LONGITUD_REGISTRO);
        rellenar_RegistroEntrega();
        String texto = RegistroEntrega_CodigoGrupoEntrega + sep + RegistroEntrega_Ciclo + sep + RegistroEntrega_zona + sep + RegistroEntrega_Municipio + sep + RegistroEntrega_Seccion + sep +
                RegistroEntrega_Ruta + sep + RegistroEntrega_CodCliente + sep + RegistroEntrega_Nombre + sep + RegistroEntrega_Telefono + sep + RegistroEntrega_CantidadCodigos + sep +
                RegistroEntrega_FechaEntrega + sep + RegistroEntrega_HoraEntrega + sep + RegistroEntrega_Latitud + sep + RegistroEntrega_Longitud + sep + RegistroEntrega_Satelites + sep +
                RegistroEntrega_Distancia + sep + RegistroEntrega_Lector + sep + RegistroEntrega_CRNL;
        try {
            if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                rFile.writeBytes(texto);
                System.out.println("Los Datos fueron grabados correctamente");
                return true;
            } else {
                System.out.println("Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                return false;
            }
        } catch (IOException ioe) {
            System.out.println("Se presento problema al escribir en el archivo Longitud Errada: " + LONGITUD_REGISTRO);
            return false;
        }
    }


    public void rellenar_RegistroEntrega() {
        try {
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo RegistroEntrega.dat..");
            e.printStackTrace();
        }

        return;
    }


    public void posicion_RegistroEntrega(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_RegistroEntrega(int registro) {
        String[] campos;
        encontro_RegistroEntrega = 0;
        posicion_RegistroEntrega(registro, LONGITUD_REGISTRO);
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);
            texto = new String(byteArray);
            setRegistroEntrega_CodigoGrupoEntrega(texto.substring(0, 9));
            setRegistroEntrega_Ciclo(texto.substring(10, 13));
            setRegistroEntrega_zona(texto.substring(14, 16));
            setRegistroEntrega_Municipio(texto.substring(17, 20));
            setRegistroEntrega_Seccion(texto.substring(21, 24));
            setRegistroEntrega_Ruta(texto.substring(25, 38));
            setRegistroEntrega_CodCliente(texto.substring(39, 49));
            setRegistroEntrega_Nombre(texto.substring(50, 98));
            setRegistroEntrega_Telefono(texto.substring(99, 119));
            setRegistroEntrega_CantidadCodigos(texto.substring(120, 124));
            setRegistroEntrega_FechaEntrega(texto.substring(125, 135));
            setRegistroEntrega_HoraEntrega(texto.substring(136, 144));
            setRegistroEntrega_Latitud(texto.substring(145, 161));
            setRegistroEntrega_Longitud(texto.substring(162, 178));
            setRegistroEntrega_Satelites(texto.substring(179, 183));
            setRegistroEntrega_Distancia(texto.substring(184, 194));
            setRegistroEntrega_Lector(texto.substring(195, 199));
            setRegistroEntrega_CRNL(texto.substring(200, 202));
            ultimo_RegistroEntrega = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buscarSecuencial_RegistroEntrega(String codigo) {
        encontro_RegistroEntrega = 0;
        for (int i = 0; i < (int) (total_RegistroEntrega); i++) {
            lectura_RegistroEntrega(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(RegistroEntrega_CodigoGrupoEntrega.trim())) {
                encontro_RegistroEntrega = i + 1;
                posicion_RegistroEntrega(i + 1, LONGITUD_REGISTRO);
                i = total_RegistroEntrega + 10;
            } else {
                encontro_RegistroEntrega = 0;
            }
        }
        return;
    }
}
