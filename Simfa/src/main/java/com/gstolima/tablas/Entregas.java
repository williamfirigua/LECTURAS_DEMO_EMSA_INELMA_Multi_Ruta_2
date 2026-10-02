package com.gstolima.tablas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
public class Entregas {
    String sep = ";";
    private String Entregas_CodigoGrupoEntrega;
    private String Entregas_Ciclo;
    private String Entregas_zona;
    private String Entregas_Municipio;
    private String Entregas_Seccion;
    private String Entregas_Ruta;
    private String Entregas_CodCliente;
    private String Entregas_Nombre;
    private String Entregas_Telefono;
    private String Entregas_cantidadCodigos;
    private String Entregas_FechaEntrega;
    private String Entregas_HoraEntrega;
    private String Entregas_Latitud;
    private String Entregas_Longitud;
    private String Entregas_Satelites;
    private String Entregas_Distancia;
    private String Entregas_Lector;
    private String Entregas_CRNL;

    public String getEntregas_CODIGOGRUPOENTREGA() {
        return Entregas_CodigoGrupoEntrega;
    }

    public void setEntregas_CodigoGrupoEntrega(String Entregas_CodigoGrupoEntrega) {
        this.Entregas_CodigoGrupoEntrega = Entregas_CodigoGrupoEntrega;
    }

    public String getEntregas_CICLO() {
        return Entregas_Ciclo;
    }

    public void setEntregas_Ciclo(String Entregas_Ciclo) {
        this.Entregas_Ciclo = Entregas_Ciclo;
    }

    public String getEntregas_ZONA() {
        return Entregas_zona;
    }

    public void setEntregas_zona(String Entregas_zona) {
        this.Entregas_zona = Entregas_zona;
    }

    public String getEntregas_MUNICIPIO() {
        return Entregas_Municipio;
    }

    public void setEntregas_Municipio(String Entregas_Municipio) {
        this.Entregas_Municipio = Entregas_Municipio;
    }

    public String getEntregas_SECCION() {
        return Entregas_Seccion;
    }

    public void setEntregas_Seccion(String Entregas_Seccion) {
        this.Entregas_Seccion = Entregas_Seccion;
    }

    public String getEntregas_RUTA() {
        return Entregas_Ruta;
    }

    public void setEntregas_Ruta(String Entregas_Ruta) {
        this.Entregas_Ruta = Entregas_Ruta;
    }

    public String getEntregas_CODCLIENTE() {
        return Entregas_CodCliente;
    }

    public void setEntregas_CodCliente(String Entregas_CodCliente) {
        this.Entregas_CodCliente = Entregas_CodCliente;
    }

    public String getEntregas_NOMBRE() {
        return Entregas_Nombre;
    }

    public void setEntregas_Nombre(String Entregas_Nombre) {
        this.Entregas_Nombre = Entregas_Nombre;
    }

    public String getEntregas_TELEFONO() {
        return Entregas_Telefono;
    }

    public void setEntregas_Telefono(String Entregas_Telefono) {
        this.Entregas_Telefono = Entregas_Telefono;
    }

    public String getEntregas_CANTIDADCODIGOS() {
        return Entregas_cantidadCodigos;
    }

    public void setEntregas_cantidadCodigos(String Entregas_cantidadCodigos) {
        this.Entregas_cantidadCodigos = Entregas_cantidadCodigos;
    }

    public String getEntregas_FECHAENTREGA() {
        return Entregas_FechaEntrega;
    }

    public void setEntregas_FechaEntrega(String Entregas_FechaEntrega) {
        this.Entregas_FechaEntrega = Entregas_FechaEntrega;
    }

    public String getEntregas_HORAENTREGA() {
        return Entregas_HoraEntrega;
    }

    public void setEntregas_HoraEntrega(String Entregas_HoraEntrega) {
        this.Entregas_HoraEntrega = Entregas_HoraEntrega;
    }

    public String getEntregas_LATITUD() {
        return Entregas_Latitud;
    }

    public void setEntregas_Latitud(String Entregas_Latitud) {
        this.Entregas_Latitud = Entregas_Latitud;
    }

    public String getEntregas_LONGITUD() {
        return Entregas_Longitud;
    }

    public void setEntregas_Longitud(String Entregas_Longitud) {
        this.Entregas_Longitud = Entregas_Longitud;
    }

    public String getEntregas_SATELITES() {
        return Entregas_Satelites;
    }

    public void setEntregas_Satelites(String Entregas_Satelites) {
        this.Entregas_Satelites = Entregas_Satelites;
    }

    public String getEntregas_DISTANCIA() {
        return Entregas_Distancia;
    }

    public void setEntregas_Distancia(String Entregas_Distancia) {
        this.Entregas_Distancia = Entregas_Distancia;
    }

    public String getEntregas_LECTOR() {
        return Entregas_Lector;
    }

    public void setEntregas_Lector(String Entregas_Lector) {
        this.Entregas_Lector = Entregas_Lector;
    }

    public String getEntregas_CRNL() {
        return Entregas_CRNL;
    }

    public void setEntregas_CRNL(String Entregas_CRNL) {
        this.Entregas_CRNL = Entregas_CRNL;
    }

    BufferedReader fin;
    byte[] byteArray;
    String archivo_Entregas;

    public String getarchivo_Entregas() {
        return archivo_Entregas;
    }

    public void setarchivo_Entregas(String archivo_Entregas) {
        this.archivo_Entregas = archivo_Entregas;
    }

    static final int LONGITUD_REGISTRO = 202;//1 se modifica campo cuenta para celsia valle cuenta
    int total_Entregas;

    public int gettotal_Entregas() {
        return total_Entregas;
    }

    public void settotal_Entregas(int total_Entregas) {
        this.total_Entregas = total_Entregas;
    }

    int ultimo_Entregas;
    int encontro_Entregas;

    public int getencontro_Entregas() {
        return encontro_Entregas;
    }

    public void setencontro_Entregas(int encontro_Entregas) {
        this.encontro_Entregas = encontro_Entregas;
    }

    String buscar_Entregas;
    String texto;

    RandomAccessFile rFile;
    private final byte[] bufferRegistroCliente =
            new byte[LONGITUD_REGISTRO];
    public Boolean abrir_Entregas(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_Entregas = nombreArchivo;
        return abrir(archivo_Entregas);
    }

    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            total_Entregas = fileSize / LONGITUD_REGISTRO;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_Entregas() { // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribir_Entregas(int posicion) {
        posicion_Entregas(posicion, LONGITUD_REGISTRO);
        rellenar_Entregas();
        String texto = Entregas_CodigoGrupoEntrega + sep + Entregas_Ciclo + sep + Entregas_zona + sep + Entregas_Municipio + sep + Entregas_Seccion + sep +
                Entregas_Ruta + sep + Entregas_CodCliente + sep + Entregas_Nombre + sep + Entregas_Telefono  +sep + Entregas_cantidadCodigos + sep +
                Entregas_FechaEntrega + sep + Entregas_HoraEntrega + sep + Entregas_Latitud + sep + Entregas_Longitud + sep + Entregas_Satelites + sep +
                Entregas_Distancia + sep + Entregas_Lector + sep + Entregas_CRNL;
        try {
            if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                rFile.writeBytes(texto);
              //  System.out.println("Los Datos fueron grabados correctamente");
                return true;
            } else {
              //  System.out.println("Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                return false;
            }
        } catch (IOException ioe) {
           // System.out.println("Se presento problema al escribir en el archivo Longitud Errada: " + LONGITUD_REGISTRO);
            return false;
        }
    }

    public void rellenar_Entregas() {
        try {
            Entregas_CodigoGrupoEntrega = String.format("%-9s", Entregas_CodigoGrupoEntrega);
            Entregas_Ciclo = String.format("%-3s", Entregas_Ciclo);
            Entregas_zona = String.format("%-2s", Entregas_zona);
            Entregas_Municipio = String.format("%-3s", Entregas_Municipio);
            Entregas_Seccion = String.format("%-3s", Entregas_Seccion);
            Entregas_Ruta = String.format("%-13s", Entregas_Ruta);
            Entregas_CodCliente = String.format("%-10s", Entregas_CodCliente);
            Entregas_Nombre = String.format("%-48s", Entregas_Nombre);
            Entregas_Telefono = String.format("%-20s", Entregas_Telefono );
            Entregas_cantidadCodigos = String.format("%-4s", Entregas_cantidadCodigos);
            Entregas_FechaEntrega = String.format("%-10s", Entregas_FechaEntrega);
            Entregas_HoraEntrega = String.format("%-8s", Entregas_HoraEntrega);
            Entregas_Latitud = String.format("%-16s", Entregas_Latitud);
            Entregas_Longitud = String.format("%-16s", Entregas_Longitud);
            Entregas_Satelites = String.format("%-4s", Entregas_Satelites);
            Entregas_Distancia = String.format("%-10s", Entregas_Distancia);
            Entregas_Lector = String.format("%-4s", Entregas_Lector);
            Entregas_CRNL = "\r\n";
            //Entregas_CRNL = String.format("%-2s", Entregas_CRNL);
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo Entregas.dat..");
            e.printStackTrace();
        }
    }

    public void posicion_Entregas(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Escribir los registros atual en el archivo en posicion
    public void lectura_Entregas(int registro) {

        encontro_Entregas = 0;
        posicion_Entregas(registro, LONGITUD_REGISTRO);
        try {
            rFile.readFully(bufferRegistroCliente);
            setEntregas_CodigoGrupoEntrega(new String(bufferRegistroCliente,0,9, StandardCharsets.UTF_8));
            setEntregas_Ciclo(new String(bufferRegistroCliente,10,3, StandardCharsets.UTF_8));
            setEntregas_zona(new String(bufferRegistroCliente,14,2, StandardCharsets.UTF_8));
            setEntregas_Municipio(new String(bufferRegistroCliente,17,3, StandardCharsets.UTF_8));
            setEntregas_Seccion(new String(bufferRegistroCliente,21,3, StandardCharsets.UTF_8));
            setEntregas_Ruta(new String(bufferRegistroCliente,25,13, StandardCharsets.UTF_8));
            setEntregas_CodCliente(new String(bufferRegistroCliente,39,10, StandardCharsets.UTF_8));
            setEntregas_Nombre(new String(bufferRegistroCliente,50,48, StandardCharsets.UTF_8));
            setEntregas_Telefono(new String(bufferRegistroCliente,99,20, StandardCharsets.UTF_8));
            setEntregas_cantidadCodigos(new String(bufferRegistroCliente,120,4, StandardCharsets.UTF_8));
            setEntregas_FechaEntrega(new String(bufferRegistroCliente,125,10, StandardCharsets.UTF_8));
            setEntregas_HoraEntrega(new String(bufferRegistroCliente,136,8, StandardCharsets.UTF_8));
            setEntregas_Latitud(new String(bufferRegistroCliente,145,16, StandardCharsets.UTF_8));
            setEntregas_Longitud(new String(bufferRegistroCliente,162,16, StandardCharsets.UTF_8));
            setEntregas_Satelites(new String(bufferRegistroCliente,179,4, StandardCharsets.UTF_8));
            setEntregas_Distancia(new String(bufferRegistroCliente,184,10, StandardCharsets.UTF_8));
            setEntregas_Lector(new String(bufferRegistroCliente,195,4, StandardCharsets.UTF_8));
            setEntregas_CRNL(new String(bufferRegistroCliente,200,2, StandardCharsets.UTF_8));

            ultimo_Entregas = registro;
            //fin estructura
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarSecuencial_Entregas(String codigo) {
        encontro_Entregas = 0;
        for (int i = 0; i < (int) (total_Entregas); i++) {
            lectura_Entregas(i + 1);
            if (Integer.parseInt(codigo.trim()) == Integer.parseInt(Entregas_CodCliente.trim())) {
                encontro_Entregas = i + 1;
                posicion_Entregas(i + 1, LONGITUD_REGISTRO);
                i = total_Entregas + 10;
            } else {
                encontro_Entregas = 0;
            }
        }
    }
}
