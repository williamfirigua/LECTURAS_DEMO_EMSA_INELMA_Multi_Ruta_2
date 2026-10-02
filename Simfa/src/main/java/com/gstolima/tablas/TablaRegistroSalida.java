
package com.gstolima.tablas;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/// <summary>
/// Descripción breve del archivo a generar...sregist
/// </summary>
public class TablaRegistroSalida {

    String sep = "|";
    String tablaRegistroSalida_SUSPENDIDO;//CORTADO
    String tablaRegistroSalida_SINLECTURA;
    String tablaRegistroSalida_CICLO;
    String tablaRegistroSalida_DESCDEPTO;
    String tablaRegistroSalida_CODMUNICIPIO;
    String tablaRegistroSalida_CODESTADO; //estado de suspension
    String tablaRegistroSalida_CODSECTOR;
    String tablaRegistroSalida_DESCSECTOR;
    String tablaRegistroSalida_ruta;
    String tablaRegistroSalida_CUENTA;
    String tablaRegistroSalida_Clasedeservicio;
    String tablaRegistroSalida_DESCSERV;
    String tablaRegistroSalida_UBICACION;
    String tablaRegistroSalida_Nombre;
    String tablaRegistroSalida_Direccion;
    String tablaRegistroSalida_NROMEDIDORES;
    String tablaRegistroSalida_nrocontador;
    String tablaRegistroSalida_MARCA;
    String tablaRegistroSalida_Tipomedida; //es TIPO_MED
    String tablaRegistroSalida_Factormultipicacion;
    String tablaRegistroSalida_Nroenteros; //S_REGIST_DIGITOS
    String tablaRegistroSalida_Consumopromediocliente; //S_REGIST_PROMEDIO ??
    String tablaRegistroSalida_Lecturaanterior;
    String tablaRegistroSalida_lecturatomada;
    String tablaRegistroSalida_lecturamodificada1;
    String tablaRegistroSalida_lecturamodificada2;
    String tablaRegistroSalida_causadenolectura;
    String tablaRegistroSalida_comentario1;
    String tablaRegistroSalida_fechalectura;
    String tablaRegistroSalida_horalectura;
    String tablaRegistroSalida_intentos;
    String tablaRegistroSalida_modificaciones;
    String tablaRegistroSalida_ULTIMOMEDIDORLEIDO;
    String tablaRegistroSalida_TIEMPO;
    String tablaRegistroSalida_NROIMPRESIONES;
    String tablaRegistroSalida_IMPRESORA;
    String tablaRegistroSalida_TERMINAL;
    String tablaRegistroSalida_LECTOR;
    String tablaRegistroSalida_informe;
    String tablaRegistroSalida_leido;
    String tablaRegistroSalida_OBSERANT;
    String tablaRegistroSalida_CONSERVICIODIRECTO;
    String tablaRegistroSalida_INDOBLIGAFOTO;
    String tablaRegistroSalida_cordenadax;
    String tablaRegistroSalida_cordenaday;
    String tablaRegistroSalida_distanciacalculada;
    String tablaRegistroSalida_CODCATEGORIA;
    String tablaRegistroSalida_SUSPENDIDOCONLECTURA;
    String tablaRegistroSalida_CODNOTIFICACION;
    String tablaRegistroSalida_CODIGOGRUPOENTREGA;
    //nuevos campos para la solucion de critica 2024
    String tablaRegistroSalida_INDICADORCRITICASAC;
    String tablaRegistroSalida_CONSUMO_LIM_INFERIOR;
    String tablaRegistroSalida_CONSUMO_LIM_SUPERIOR;
    String tablaRegistroSalida_CODIGO_SAC;
    String id;
    String tablaRegistroSalida_fin;

    public static int getLongitudRegistro() {

        return LONGITUD_REGISTRO;
    }

    public String gettablaRegistroSalida_SUSPENDIDO() {

        return tablaRegistroSalida_SUSPENDIDO;
    }

    public void settablaRegistroSalida_SUSPENDIDO(String tablaRegistroSalida_SUSPENDIDO) {
        this.tablaRegistroSalida_SUSPENDIDO = tablaRegistroSalida_SUSPENDIDO;
    }

    public String gettablaRegistroSalida_SINLECTURA() {

        return tablaRegistroSalida_SINLECTURA;
    }

    public void settablaRegistroSalida_SINLECTURA(String tablaRegistroSalida_SINLECTURA) {
        this.tablaRegistroSalida_SINLECTURA = tablaRegistroSalida_SINLECTURA;
    }

    public String gettablaRegistroSalida_CICLO() {
        return tablaRegistroSalida_CICLO;
    }

    public void settablaRegistroSalida_CICLO(String tablaRegistroSalida_CICLO) {
        this.tablaRegistroSalida_CICLO = tablaRegistroSalida_CICLO;
    }

    public String gettablaRegistroSalida_DESCDEPTO() {
           return tablaRegistroSalida_DESCDEPTO;
    }

    public void settablaRegistroSalida_DESCDEPTO(String tablaRegistroSalida_DESCDEPTO) {
        this.tablaRegistroSalida_DESCDEPTO = tablaRegistroSalida_DESCDEPTO;
    }

    public String gettablaRegistroSalida_CODMUNICIPIO() {

        return tablaRegistroSalida_CODMUNICIPIO;
    }

    public void settablaRegistroSalida_CODMUNICIPIO(String tablaRegistroSalida_CODMUNICIPIO) {
        this.tablaRegistroSalida_CODMUNICIPIO = tablaRegistroSalida_CODMUNICIPIO;
    }

    public String gettablaRegistroSalida_CODESTADO() {
        return tablaRegistroSalida_CODESTADO;
    }

    public void settablaRegistroSalida_CODESTADO(String tablaRegistroSalida_CODESTADO) {
        this.tablaRegistroSalida_CODESTADO = tablaRegistroSalida_CODESTADO;
    }

    public String gettablaRegistroSalida_CODSECTOR() {
        return tablaRegistroSalida_CODSECTOR;
    }

    public void settablaRegistroSalida_CODSECTOR(String tablaRegistroSalida_CODSECTOR) {
        this.tablaRegistroSalida_CODSECTOR = tablaRegistroSalida_CODSECTOR;
    }

    public String gettablaRegistroSalida_DESCSECTOR() {
        return tablaRegistroSalida_DESCSECTOR;
    }

    public void settablaRegistroSalida_DESCSECTOR(String tablaRegistroSalida_DESCSECTOR) {
        this.tablaRegistroSalida_DESCSECTOR = tablaRegistroSalida_DESCSECTOR;
    }

    public String gettablaRegistroSalida_RUTA() {
        return tablaRegistroSalida_ruta;
    }

    public void settablaRegistroSalida_ruta(String tablaRegistroSalida_ruta) {
        this.tablaRegistroSalida_ruta = tablaRegistroSalida_ruta;
    }

    public String gettablaRegistroSalida_CUENTA() {
        return tablaRegistroSalida_CUENTA;
    }

    public void settablaRegistroSalida_CUENTA(String tablaRegistroSalida_CUENTA) {
        this.tablaRegistroSalida_CUENTA = tablaRegistroSalida_CUENTA;
    }

    public String gettablaRegistroSalida_CLASEDESERVICIO() {
        return tablaRegistroSalida_Clasedeservicio;
    }

    public void settablaRegistroSalida_Clasedeservicio(String tablaRegistroSalida_Clasedeservicio) {
        this.tablaRegistroSalida_Clasedeservicio = tablaRegistroSalida_Clasedeservicio;
    }

    public String gettablaRegistroSalida_DESCSERV() {
        return tablaRegistroSalida_DESCSERV;
    }

    public void settablaRegistroSalida_DESCSERV(String tablaRegistroSalida_DESCSERV) {
        this.tablaRegistroSalida_DESCSERV = tablaRegistroSalida_DESCSERV;
    }

    public String gettablaRegistroSalida_UBICACION() {
        return tablaRegistroSalida_UBICACION;
    }

    public void settablaRegistroSalida_UBICACION(String tablaRegistroSalida_UBICACION) {
        this.tablaRegistroSalida_UBICACION = tablaRegistroSalida_UBICACION;
    }

    public String gettablaRegistroSalida_NOMBRE() {
        return tablaRegistroSalida_Nombre;
    }

    public void settablaRegistroSalida_Nombre(String tablaRegistroSalida_Nombre) {
        this.tablaRegistroSalida_Nombre = tablaRegistroSalida_Nombre;
    }

    public String gettablaRegistroSalida_DIRECCION() {
        return tablaRegistroSalida_Direccion;
    }

    public void settablaRegistroSalida_Direccion(String tablaRegistroSalida_Direccion) {
        this.tablaRegistroSalida_Direccion = tablaRegistroSalida_Direccion;
    }

    public String gettablaRegistroSalida_NROMEDIDORES() {
        return tablaRegistroSalida_NROMEDIDORES;
    }

    public void settablaRegistroSalida_NROMEDIDORES(String tablaRegistroSalida_NROMEDIDORES) {
        this.tablaRegistroSalida_NROMEDIDORES = tablaRegistroSalida_NROMEDIDORES;
    }

    public String gettablaRegistroSalida_NROCONTADOR() {
        return tablaRegistroSalida_nrocontador;
    }

    public void settablaRegistroSalida_nrocontador(String tablaRegistroSalida_nrocontador) {
        this.tablaRegistroSalida_nrocontador = tablaRegistroSalida_nrocontador;
    }

    public String gettablaRegistroSalida_MARCA() {
        return tablaRegistroSalida_MARCA;
    }

    public void settablaRegistroSalida_MARCA(String tablaRegistroSalida_MARCA) {
        this.tablaRegistroSalida_MARCA = tablaRegistroSalida_MARCA;
    }

    public String gettablaRegistroSalida_TIPOMEDIDA() {
        return tablaRegistroSalida_Tipomedida;
    }

    public void settablaRegistroSalida_Tipomedida(String tablaRegistroSalida_Tipomedida) {
        this.tablaRegistroSalida_Tipomedida = tablaRegistroSalida_Tipomedida;
    }

    public String gettablaRegistroSalida_FACTORMULTIPICACION() {
        return tablaRegistroSalida_Factormultipicacion;
    }

    public void settablaRegistroSalida_Factormultipicacion(String tablaRegistroSalida_Factormultipicacion) {
        this.tablaRegistroSalida_Factormultipicacion = tablaRegistroSalida_Factormultipicacion;
    }

    public String gettablaRegistroSalida_NROENTEROS() {
        return tablaRegistroSalida_Nroenteros;
    }

    public void settablaRegistroSalida_Nroenteros(String tablaRegistroSalida_Nroenteros) {
        this.tablaRegistroSalida_Nroenteros = tablaRegistroSalida_Nroenteros;
    }

    public String gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE() {
        return tablaRegistroSalida_Consumopromediocliente;
    }

    public void settablaRegistroSalida_Consumopromediocliente(String tablaRegistroSalida_Consumopromediocliente) {
        this.tablaRegistroSalida_Consumopromediocliente = tablaRegistroSalida_Consumopromediocliente;
    }

    public String gettablaRegistroSalida_LECTURAANTERIOR() {
        return tablaRegistroSalida_Lecturaanterior;
    }

    public void settablaRegistroSalida_Lecturaanterior(String tablaRegistroSalida_Lecturaanterior) {
        this.tablaRegistroSalida_Lecturaanterior = tablaRegistroSalida_Lecturaanterior;
    }

    public String gettablaRegistroSalida_LECTURATOMADA() {
        return tablaRegistroSalida_lecturatomada;
    }

    public void settablaRegistroSalida_lecturatomada(String tablaRegistroSalida_lecturatomada) {
        this.tablaRegistroSalida_lecturatomada = tablaRegistroSalida_lecturatomada;
    }

    public String gettablaRegistroSalida_LECTURAMODIFICADA1() {
        return tablaRegistroSalida_lecturamodificada1;
    }

    public void settablaRegistroSalida_lecturamodificada1(String tablaRegistroSalida_lecturamodificada1) {
        this.tablaRegistroSalida_lecturamodificada1 = tablaRegistroSalida_lecturamodificada1;
    }

    public String gettablaRegistroSalida_LECTURAMODIFICADA2() {
        return tablaRegistroSalida_lecturamodificada2;
    }

    public void settablaRegistroSalida_lecturamodificada2(String tablaRegistroSalida_lecturamodificada2) {
        this.tablaRegistroSalida_lecturamodificada2 = tablaRegistroSalida_lecturamodificada2;
    }

    public String gettablaRegistroSalida_CAUSADENOLECTURA() {
        return tablaRegistroSalida_causadenolectura;
    }

    public void settablaRegistroSalida_causadenolectura(String tablaRegistroSalida_causadenolectura) {
        this.tablaRegistroSalida_causadenolectura = tablaRegistroSalida_causadenolectura;
    }

    public String gettablaRegistroSalida_COMENTARIO1() {
        return tablaRegistroSalida_comentario1;
    }

    public void settablaRegistroSalida_comentario1(String tablaRegistroSalida_comentario1) {
        this.tablaRegistroSalida_comentario1 = tablaRegistroSalida_comentario1;
    }

    public String gettablaRegistroSalida_FECHALECTURA() {
        return tablaRegistroSalida_fechalectura;
    }

    public void settablaRegistroSalida_fechalectura(String tablaRegistroSalida_fechalectura) {
        this.tablaRegistroSalida_fechalectura = tablaRegistroSalida_fechalectura;
    }

    public String gettablaRegistroSalida_HORALECTURA() {
        return tablaRegistroSalida_horalectura;
    }

    public void settablaRegistroSalida_horalectura(String tablaRegistroSalida_horalectura) {
        this.tablaRegistroSalida_horalectura = tablaRegistroSalida_horalectura;
    }

    public String gettablaRegistroSalida_INTENTOS() {
        return tablaRegistroSalida_intentos;
    }

    public void settablaRegistroSalida_intentos(String tablaRegistroSalida_intentos) {
        this.tablaRegistroSalida_intentos = tablaRegistroSalida_intentos;
    }

    public String gettablaRegistroSalida_MODIFICACIONES() {
        return tablaRegistroSalida_modificaciones;
    }

    public void settablaRegistroSalida_modificaciones(String tablaRegistroSalida_modificaciones) {
        this.tablaRegistroSalida_modificaciones = tablaRegistroSalida_modificaciones;
    }

    public String gettablaRegistroSalida_ULTIMOMEDIDORLEIDO() {
        return tablaRegistroSalida_ULTIMOMEDIDORLEIDO;
    }

    public void settablaRegistroSalida_ULTIMOMEDIDORLEIDO(String tablaRegistroSalida_ULTIMOMEDIDORLEIDO) {
        this.tablaRegistroSalida_ULTIMOMEDIDORLEIDO = tablaRegistroSalida_ULTIMOMEDIDORLEIDO;
    }

    public String gettablaRegistroSalida_TIEMPO() {
        return tablaRegistroSalida_TIEMPO;
    }

    public void settablaRegistroSalida_TIEMPO(String tablaRegistroSalida_TIEMPO) {
        this.tablaRegistroSalida_TIEMPO = tablaRegistroSalida_TIEMPO;
    }

    public String gettablaRegistroSalida_NROIMPRESIONES() {
        return tablaRegistroSalida_NROIMPRESIONES;
    }

    public void settablaRegistroSalida_NROIMPRESIONES(String tablaRegistroSalida_NROIMPRESIONES) {
        this.tablaRegistroSalida_NROIMPRESIONES = tablaRegistroSalida_NROIMPRESIONES;
    }

    public String gettablaRegistroSalida_IMPRESORA() {
        return tablaRegistroSalida_IMPRESORA;
    }

    public void settablaRegistroSalida_IMPRESORA(String tablaRegistroSalida_IMPRESORA) {
        this.tablaRegistroSalida_IMPRESORA = tablaRegistroSalida_IMPRESORA;
    }

    public String gettablaRegistroSalida_TERMINAL() {
        return tablaRegistroSalida_TERMINAL;
    }

    public void settablaRegistroSalida_TERMINAL(String tablaRegistroSalida_TERMINAL) {
        this.tablaRegistroSalida_TERMINAL = tablaRegistroSalida_TERMINAL;
    }

    public String gettablaRegistroSalida_LECTOR() {
        return tablaRegistroSalida_LECTOR;
    }

    public void settablaRegistroSalida_LECTOR(String tablaRegistroSalida_LECTOR) {
        this.tablaRegistroSalida_LECTOR = tablaRegistroSalida_LECTOR;
    }

    public String gettablaRegistroSalida_INFORME() {
        return tablaRegistroSalida_informe;
    }

    public void settablaRegistroSalida_informe(String tablaRegistroSalida_informe) {
        this.tablaRegistroSalida_informe = tablaRegistroSalida_informe;
    }

    public String gettablaRegistroSalida_LEIDO() {
        return tablaRegistroSalida_leido;
    }

    public void settablaRegistroSalida_leido(String tablaRegistroSalida_leido) {
        this.tablaRegistroSalida_leido = tablaRegistroSalida_leido;
    }

    public String gettablaRegistroSalida_OBSERANT() {
        return tablaRegistroSalida_OBSERANT;
    }

    public void settablaRegistroSalida_OBSERANT(String tablaRegistroSalida_OBSERANT) {
        this.tablaRegistroSalida_OBSERANT = tablaRegistroSalida_OBSERANT;
    }

    public String gettablaRegistroSalida_CONSERVICIODIRECTO() {
        return tablaRegistroSalida_CONSERVICIODIRECTO;
    }

    public void settablaRegistroSalida_CONSERVICIODIRECTO(String tablaRegistroSalida_CONSERVICIODIRECTO) {
        this.tablaRegistroSalida_CONSERVICIODIRECTO = tablaRegistroSalida_CONSERVICIODIRECTO;
    }

    public String gettablaRegistroSalida_INDOBLIGAFOTO() {
        return tablaRegistroSalida_INDOBLIGAFOTO;
    }

    public void settablaRegistroSalida_INDOBLIGAFOTO(String tablaRegistroSalida_INDOBLIGAFOTO) {
        this.tablaRegistroSalida_INDOBLIGAFOTO = tablaRegistroSalida_INDOBLIGAFOTO;
    }

    public String gettablaRegistroSalida_CORDENADAX() {
        return tablaRegistroSalida_cordenadax;
    }

    public void settablaRegistroSalida_cordenadax(String tablaRegistroSalida_cordenadax) {
        this.tablaRegistroSalida_cordenadax = tablaRegistroSalida_cordenadax;
    }

    public String gettablaRegistroSalida_CORDENADAY() {
        return tablaRegistroSalida_cordenaday;
    }

    public void settablaRegistroSalida_cordenaday(String tablaRegistroSalida_cordenaday) {
        this.tablaRegistroSalida_cordenaday = tablaRegistroSalida_cordenaday;
    }

    public String gettablaRegistroSalida_DISTANCIACALCULADA() {
        return tablaRegistroSalida_distanciacalculada;
    }

    public void settablaRegistroSalida_distanciacalculada(String tablaRegistroSalida_distanciacalculada) {
        this.tablaRegistroSalida_distanciacalculada = tablaRegistroSalida_distanciacalculada;
    }

    public String gettablaRegistroSalida_CODCATEGORIA() {
        return tablaRegistroSalida_CODCATEGORIA;
    }

    public void settablaRegistroSalida_CODCATEGORIA(String tablaRegistroSalida_CODCATEGORIA) {
        this.tablaRegistroSalida_CODCATEGORIA = tablaRegistroSalida_CODCATEGORIA;
    }

    public String gettablaRegistroSalida_SUSPENDIDOCONLECTURA() {
        return tablaRegistroSalida_SUSPENDIDOCONLECTURA;
    }

    public void settablaRegistroSalida_SUSPENDIDOCONLECTURA(String tablaRegistroSalida_SUSPENDIDOCONLECTURA) {
        this.tablaRegistroSalida_SUSPENDIDOCONLECTURA = tablaRegistroSalida_SUSPENDIDOCONLECTURA;
    }

    public String gettablaRegistroSalida_CODNOTIFICACION() {
        return tablaRegistroSalida_CODNOTIFICACION;
    }

    public void settablaRegistroSalida_CODNOTIFICACION(String tablaRegistroSalida_CODNOTIFICACION) {
        this.tablaRegistroSalida_CODNOTIFICACION = tablaRegistroSalida_CODNOTIFICACION;
    }

    public String gettablaRegistroSalida_CODIGOGRUPOENTREGA() {
        return tablaRegistroSalida_CODIGOGRUPOENTREGA;
    }

    public void settablaRegistroSalida_CODIGOGRUPOENTREGA(String tablaRegistroSalida_CODIGOGRUPOENTREGA) {
        this.tablaRegistroSalida_CODIGOGRUPOENTREGA = tablaRegistroSalida_CODIGOGRUPOENTREGA;
    }


    public String gettablaRegistroSalida_INDICADORCRITICASAC() {
        return tablaRegistroSalida_INDICADORCRITICASAC;
    }

    public void settablaRegistroSalida_INDICADORCRITICASAC(String tablaRegistroSalida_INDICADORCRITICASAC) {
        this.tablaRegistroSalida_INDICADORCRITICASAC = tablaRegistroSalida_INDICADORCRITICASAC;
    }


    public String gettablaRegistroSalida_CONSUMO_LIM_INFERIOR() {
        return tablaRegistroSalida_CONSUMO_LIM_INFERIOR;
    }

    public void settablaRegistroSalida_CONSUMO_LIM_INFERIOR(String tablaRegistroSalida_CONSUMO_LIM_INFERIOR) {
        this.tablaRegistroSalida_CONSUMO_LIM_INFERIOR = tablaRegistroSalida_CONSUMO_LIM_INFERIOR;
    }


    public String gettablaRegistroSalida_CONSUMO_LIM_SUPERIOR() {
        return tablaRegistroSalida_CONSUMO_LIM_SUPERIOR;
    }

    public void settablaRegistroSalida_CONSUMO_LIM_SUPERIOR(String tablaRegistroSalida_CONSUMO_LIM_SUPERIOR) {
        this.tablaRegistroSalida_CONSUMO_LIM_SUPERIOR = tablaRegistroSalida_CONSUMO_LIM_SUPERIOR;
    }

    public String gettablaRegistroSalida_CODIGO_SAC() {
        return tablaRegistroSalida_CODIGO_SAC;
    }

    public void settablaRegistroSalida_CODIGO_SAC(String tablaRegistroSalida_CODIGO_SAC) {
        this.tablaRegistroSalida_CODIGO_SAC = tablaRegistroSalida_CODIGO_SAC;
    }

    public String gettablaRegistroSalida_FIN() {
        return tablaRegistroSalida_fin;
    }

    public void settablaRegistroSalida_fin(String tablaRegistroSalida_fin) {
        this.tablaRegistroSalida_fin = tablaRegistroSalida_fin;
    }

    public int getTotal_TablaRegistroSalida() {
        return total_TablaRegistroSalida;
    }

    public void setTotal_TablaRegistroSalida(int total_TablaRegistroSalida) {
        this.total_TablaRegistroSalida = total_TablaRegistroSalida;
    }

    public String getArchivo_TablaRegistroSalida() {
        return archivo_TablaRegistroSalida;
    }

    public void setArchivo_TablaRegistroSalida(String archivo_TablaRegistroSalida) {
        this.archivo_TablaRegistroSalida = archivo_TablaRegistroSalida;
    }

    public int getEncontro_TablaRegistroSalida() {
        return encontro_TablaRegistroSalida;
    }

    public void setEncontro_TablaRegistroSalida(int encontro_TablaRegistroSalida) {
        this.encontro_TablaRegistroSalida = encontro_TablaRegistroSalida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    BufferedReader fin;
    byte[] byteArray;
    private String archivo_TablaRegistroSalida;
    private static final int LONGITUD_REGISTRO = 864;//673se suma un caracter a cuenta para el app de Celsia valle
    private int total_TablaRegistroSalida;
    int ultimo_TablaRegistroSalida;
    private int encontro_TablaRegistroSalida;
    String buscar_TablaRegistroSalida;
    String texto;
    RandomAccessFile rFile;
    private final byte[] bufferRegistro =
            new byte[LONGITUD_REGISTRO];

    public Boolean abrir_TablaRegistroSalida(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            Log.e("error", "entra al metodo eeeeee " + e.getMessage());

            e.printStackTrace();
        }
        setArchivo_TablaRegistroSalida(nombreArchivo);
        return abrir(getArchivo_TablaRegistroSalida());
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            texto = new String(byteArray);
            setTotal_TablaRegistroSalida(fileSize / LONGITUD_REGISTRO);
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_TablaRegistroSalida() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribir_TablaRegistroSalida(int posicion) {
        Log.e("error", " total " + getTotal_TablaRegistroSalida());
        String texto ="";


        if (posicion <= getTotal_TablaRegistroSalida()) {
            posicion_TablaRegistroSalida(posicion, LONGITUD_REGISTRO);
            rellenar_TablaRegistroSalida();
            texto = tablaRegistroSalida_SUSPENDIDO + sep + tablaRegistroSalida_SINLECTURA + sep + tablaRegistroSalida_CICLO + sep + tablaRegistroSalida_DESCDEPTO + sep + tablaRegistroSalida_CODMUNICIPIO + sep +
                    tablaRegistroSalida_CODESTADO + sep + tablaRegistroSalida_CODSECTOR + sep + tablaRegistroSalida_DESCSECTOR + sep + tablaRegistroSalida_ruta + sep + tablaRegistroSalida_CUENTA + sep +
                    tablaRegistroSalida_Clasedeservicio + sep + tablaRegistroSalida_DESCSERV + sep + tablaRegistroSalida_UBICACION + sep + tablaRegistroSalida_Nombre + sep + tablaRegistroSalida_Direccion + sep +
                    tablaRegistroSalida_NROMEDIDORES + sep + tablaRegistroSalida_nrocontador + sep + tablaRegistroSalida_MARCA + sep + tablaRegistroSalida_Tipomedida + sep + tablaRegistroSalida_Factormultipicacion + sep +
                    tablaRegistroSalida_Nroenteros + sep + tablaRegistroSalida_Consumopromediocliente + sep + tablaRegistroSalida_Lecturaanterior + sep + tablaRegistroSalida_lecturatomada + sep + tablaRegistroSalida_lecturamodificada1 + sep +
                    tablaRegistroSalida_lecturamodificada2 + sep + tablaRegistroSalida_causadenolectura + sep + tablaRegistroSalida_comentario1 + sep + tablaRegistroSalida_fechalectura + sep + tablaRegistroSalida_horalectura + sep +
                    tablaRegistroSalida_intentos + sep + tablaRegistroSalida_modificaciones + sep + tablaRegistroSalida_ULTIMOMEDIDORLEIDO + sep + tablaRegistroSalida_TIEMPO + sep + tablaRegistroSalida_NROIMPRESIONES + sep +
                    tablaRegistroSalida_IMPRESORA + sep + tablaRegistroSalida_TERMINAL + sep + tablaRegistroSalida_LECTOR + sep + tablaRegistroSalida_informe + sep + tablaRegistroSalida_leido + sep +
                    tablaRegistroSalida_OBSERANT + sep + tablaRegistroSalida_CONSERVICIODIRECTO + sep + tablaRegistroSalida_INDOBLIGAFOTO + sep + tablaRegistroSalida_cordenadax + sep + tablaRegistroSalida_cordenaday + sep +
                    tablaRegistroSalida_distanciacalculada + sep + tablaRegistroSalida_CODCATEGORIA + sep + tablaRegistroSalida_SUSPENDIDOCONLECTURA + sep + tablaRegistroSalida_CODNOTIFICACION + sep +
                    tablaRegistroSalida_CODIGOGRUPOENTREGA + sep + tablaRegistroSalida_INDICADORCRITICASAC +sep + tablaRegistroSalida_CONSUMO_LIM_INFERIOR + sep +
                    tablaRegistroSalida_CONSUMO_LIM_SUPERIOR + sep + tablaRegistroSalida_CODIGO_SAC + sep + id + sep + tablaRegistroSalida_fin;
            try {
                Log.e("error", texto + " graba " + LONGITUD_REGISTRO);
                Log.e("error", " texto " + texto);
                if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                    rFile.writeBytes(texto);
                    Log.e("error", "Los Datos fueron grabados correctamente");
                    return true;
                } else {
                    /*if (texto.length()>673)
                    {
                        texto = texto.substring(0,671) + tablaRegistroSalida_fin;
                        //si tecorto es un desastre de posibilidad sinmedir el campo

                    }*/
                    if ((texto.length() < LONGITUD_REGISTRO)) {
                        texto=texto.replace("\n","");
                        texto=texto.replace("\r","");
                        texto=String.format("%-"+(LONGITUD_REGISTRO-2)+"s", texto)+"\r\n";
                        rFile.writeBytes(texto);
                    }

                    Log.e("error", "Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                    return false;
                }
            } catch (Exception ioe) {

                Log.e("error", "Se presento problema al escribir en el archivo Longitud Errada: " + ioe.getMessage() + LONGITUD_REGISTRO);
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean escribir_TablaRegistroSalida2(int posicion) {
        Log.e("error", " total " + getTotal_TablaRegistroSalida());
        String texto ="";

            posicion_TablaRegistroSalida(posicion, LONGITUD_REGISTRO);
            rellenar_TablaRegistroSalida();
            texto = tablaRegistroSalida_SUSPENDIDO + sep + tablaRegistroSalida_SINLECTURA + sep + tablaRegistroSalida_CICLO + sep + tablaRegistroSalida_DESCDEPTO + sep + tablaRegistroSalida_CODMUNICIPIO + sep +
                    tablaRegistroSalida_CODESTADO + sep + tablaRegistroSalida_CODSECTOR + sep + tablaRegistroSalida_DESCSECTOR + sep + tablaRegistroSalida_ruta + sep + tablaRegistroSalida_CUENTA + sep +
                    tablaRegistroSalida_Clasedeservicio + sep + tablaRegistroSalida_DESCSERV + sep + tablaRegistroSalida_UBICACION + sep + tablaRegistroSalida_Nombre + sep + tablaRegistroSalida_Direccion + sep +
                    tablaRegistroSalida_NROMEDIDORES + sep + tablaRegistroSalida_nrocontador + sep + tablaRegistroSalida_MARCA + sep + tablaRegistroSalida_Tipomedida + sep + tablaRegistroSalida_Factormultipicacion + sep +
                    tablaRegistroSalida_Nroenteros + sep + tablaRegistroSalida_Consumopromediocliente + sep + tablaRegistroSalida_Lecturaanterior + sep + tablaRegistroSalida_lecturatomada + sep + tablaRegistroSalida_lecturamodificada1 + sep +
                    tablaRegistroSalida_lecturamodificada2 + sep + tablaRegistroSalida_causadenolectura + sep + tablaRegistroSalida_comentario1 + sep + tablaRegistroSalida_fechalectura + sep + tablaRegistroSalida_horalectura + sep +
                    tablaRegistroSalida_intentos + sep + tablaRegistroSalida_modificaciones + sep + tablaRegistroSalida_ULTIMOMEDIDORLEIDO + sep + tablaRegistroSalida_TIEMPO + sep + tablaRegistroSalida_NROIMPRESIONES + sep +
                    tablaRegistroSalida_IMPRESORA + sep + tablaRegistroSalida_TERMINAL + sep + tablaRegistroSalida_LECTOR + sep + tablaRegistroSalida_informe + sep + tablaRegistroSalida_leido + sep +
                    tablaRegistroSalida_OBSERANT + sep + tablaRegistroSalida_CONSERVICIODIRECTO + sep + tablaRegistroSalida_INDOBLIGAFOTO + sep + tablaRegistroSalida_cordenadax + sep + tablaRegistroSalida_cordenaday + sep +
                    tablaRegistroSalida_distanciacalculada + sep + tablaRegistroSalida_CODCATEGORIA + sep + tablaRegistroSalida_SUSPENDIDOCONLECTURA + sep + tablaRegistroSalida_CODNOTIFICACION + sep +
                    tablaRegistroSalida_CODIGOGRUPOENTREGA + sep + tablaRegistroSalida_INDICADORCRITICASAC +sep + tablaRegistroSalida_CONSUMO_LIM_INFERIOR + sep +
                    tablaRegistroSalida_CONSUMO_LIM_SUPERIOR + sep + tablaRegistroSalida_CODIGO_SAC + sep + id + sep + tablaRegistroSalida_fin;
            try {
                Log.e("error", texto + " graba " + LONGITUD_REGISTRO);
                Log.e("error", " texto " + texto);
                if (null != rFile && (texto.length() == LONGITUD_REGISTRO)) {
                    rFile.writeBytes(texto);
                    Log.e("error", "Los Datos fueron grabados correctamente");
                    return true;
                } else {
                    /*if (texto.length()>673)
                    {
                        texto = texto.substring(0,671) + tablaRegistroSalida_fin;
                        //si tecorto es un desastre de posibilidad sinmedir el campo

                    }*/
                    texto=texto.replace("\n","");
                    texto=texto.replace("\r","");
                    texto=String.format("%-"+(LONGITUD_REGISTRO-2)+"s", texto)+"\r\n";
                    rFile.writeBytes(texto);
                    Log.e("error", "Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                    return false;
                }
            } catch (Exception ioe) {

                Log.e("error", "Se presento problema al escribir en el archivo Longitud Errada: " + ioe.getMessage() + LONGITUD_REGISTRO);
                return false;
            }

    }
    public static boolean contieneNoASCII(String input) {
        if (input == null) return false;

        for (char c : input.trim().toCharArray()) {
            if (c > 127) {
                return true; // apenas encuentre uno, retorna
            }
        }
        return false;
    }

    public void rellenar_TablaRegistroSalida() {
        try {

            tablaRegistroSalida_Nombre    = String.format("%-48s", limpiarCaracteres(tablaRegistroSalida_Nombre));;//texto.substring(184, 232)
            tablaRegistroSalida_Direccion = String.format("%-64s", limpiarCaracteres(tablaRegistroSalida_Direccion));;//texto.substring(233, 297)

            tablaRegistroSalida_CODMUNICIPIO = String.format("%-3s", tablaRegistroSalida_CODMUNICIPIO);
            tablaRegistroSalida_CODESTADO = String.format("%-32s", tablaRegistroSalida_CODESTADO.trim());
            tablaRegistroSalida_DESCSECTOR = String.format("%-32s", tablaRegistroSalida_DESCSECTOR.trim());
            tablaRegistroSalida_lecturatomada = String.format("%-11s", tablaRegistroSalida_lecturatomada);
            tablaRegistroSalida_lecturamodificada1 = String.format("%-11s", tablaRegistroSalida_lecturamodificada1);
            tablaRegistroSalida_lecturamodificada2 = String.format("%-11s", tablaRegistroSalida_lecturamodificada2);
            if (tablaRegistroSalida_lecturatomada.trim().equals("null"))
            {
                tablaRegistroSalida_lecturatomada="";
                if (!tablaRegistroSalida_lecturamodificada1.trim().equals(""))
                {
                    tablaRegistroSalida_lecturatomada=tablaRegistroSalida_lecturamodificada1;
                }
            }
            tablaRegistroSalida_causadenolectura = String.format("%-2s", tablaRegistroSalida_causadenolectura);
            tablaRegistroSalida_comentario1 = String.format("%-2s", tablaRegistroSalida_comentario1);
            tablaRegistroSalida_fechalectura = String.format("%-8s", tablaRegistroSalida_fechalectura);
            tablaRegistroSalida_horalectura = String.format("%-6s", tablaRegistroSalida_horalectura);
            tablaRegistroSalida_intentos = String.format("%-1s", tablaRegistroSalida_intentos);
            tablaRegistroSalida_modificaciones = String.format("%-1s", tablaRegistroSalida_modificaciones);
            tablaRegistroSalida_ULTIMOMEDIDORLEIDO = String.format("%-16s", tablaRegistroSalida_ULTIMOMEDIDORLEIDO);
            tablaRegistroSalida_TIEMPO = String.format("%-5s", tablaRegistroSalida_TIEMPO);
            tablaRegistroSalida_NROIMPRESIONES = String.format("%-1s", tablaRegistroSalida_NROIMPRESIONES);
            tablaRegistroSalida_IMPRESORA = String.format("%-3s", tablaRegistroSalida_IMPRESORA);
            tablaRegistroSalida_TERMINAL = String.format("%-15s", tablaRegistroSalida_TERMINAL);
            tablaRegistroSalida_LECTOR = String.format("%-4s", tablaRegistroSalida_LECTOR);
            tablaRegistroSalida_cordenadax = String.format("%16s", tablaRegistroSalida_cordenadax);
            tablaRegistroSalida_cordenaday = String.format("%16s", tablaRegistroSalida_cordenaday);
            if (tablaRegistroSalida_informe.trim().length()>250) {
                tablaRegistroSalida_informe = tablaRegistroSalida_informe.trim().substring(0, 249);
            }

            tablaRegistroSalida_informe = String.format("%-250s", limpiarCaracteres(tablaRegistroSalida_informe));

            tablaRegistroSalida_leido = String.format("%-1s", tablaRegistroSalida_leido);
            tablaRegistroSalida_distanciacalculada = String.format("%-10s", tablaRegistroSalida_distanciacalculada);
            tablaRegistroSalida_SUSPENDIDOCONLECTURA = String.format("%-1s", tablaRegistroSalida_SUSPENDIDOCONLECTURA);
            tablaRegistroSalida_CODNOTIFICACION = String.format("%-2s", tablaRegistroSalida_CODNOTIFICACION);
            tablaRegistroSalida_CODIGOGRUPOENTREGA = String.format("%-9s", tablaRegistroSalida_CODIGOGRUPOENTREGA);
            tablaRegistroSalida_CODCATEGORIA = String.format("%2s", tablaRegistroSalida_CODCATEGORIA);
            tablaRegistroSalida_CODIGO_SAC = String.format("%-3s", tablaRegistroSalida_CODIGO_SAC.trim());

            id = String.format("%11s", id.replace(",","").replace("|","").trim());
            tablaRegistroSalida_fin = String.format("%-2s", "\r\n");//tablaRegistroSalida_fin
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo sregist.dat..");
            e.printStackTrace();
        }
        return;
    }
    public static String limpiarCaracteres(String texto) {
        if (texto == null) {
            return null;
        }
        // 1. Normalizar tildes y caracteres Unicode
        String normalizado = java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD);

        // 2. Eliminar caracteres no ASCII (mayores a 127) o marcas de acento
        normalizado = normalizado.replaceAll("[^\\p{ASCII}]", "");

        // 3. Opcional: limpiar espacios extra
        normalizado = normalizado.trim();

        return normalizado;
    }
    public void posicion_TablaRegistroSalida(int registro, int tamaño) {
        try {
            rFile.seek((long)(registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sanearStrings(String input) {
        try {
            if (input == null) return null;

            // Eliminar espacios al principio y final
            String result = input.trim();

            // Reemplazos de caracteres
            String[][] replacements = {
                    {"á", "a"}, {"à", "a"}, {"ä", "a"}, {"â", "a"}, {"ª", "a"},
                    {"Á", "A"}, {"À", "A"}, {"Â", "A"}, {"Ä", "A"}, {"Ã", "A"}, {"Å", "A"},
                    {"é", "e"}, {"è", "e"}, {"ë", "e"}, {"ê", "e"},
                    {"É", "E"}, {"È", "E"}, {"Ê", "E"}, {"Ë", "E"},
                    {"í", "i"}, {"ì", "i"}, {"ï", "i"}, {"î", "i"},
                    {"Í", "I"}, {"Ì", "I"}, {"Ï", "I"}, {"Î", "I"},
                    {"ó", "o"}, {"ò", "o"}, {"ö", "o"}, {"ô", "o"},
                    {"Ó", "O"}, {"Ò", "O"}, {"Ö", "O"}, {"Ô", "O"},
                    {"ú", "u"}, {"ù", "u"}, {"ü", "u"}, {"û", "u"},
                    {"Ú", "U"}, {"Ù", "U"}, {"Û", "U"}, {"Ü", "U"},
                    {"ñ", "n"}, {"Ñ", "N"},
                    {"ç", "c"}, {"Ç", "C"},
                    {"º", " "}, {"°", " "}, {" ", " "}, {"\u0010", " "}, {"¡", " "},
                    {"?", " "}, {"³", " "}, {"¿", " "}, {"·", " "}, {"´", " "}, {"ª", " "}
            };

            for (String[] pair : replacements) {
                result = result.replace(pair[0], pair[1]);
            }

            return result;

        } catch (Exception e) {
            System.out.println("sanearStrings | Error -> " + e.getMessage());
            return null;
        }
    }


    //Escribir los registros atual en el archivo en posicion
    public void lectura_TablaRegistroSalida(int registro) {

        setEncontro_TablaRegistroSalida(0);



        try {
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            // Leer SOLO el registro necesario
            rFile.readFully(bufferRegistro);

            settablaRegistroSalida_SUSPENDIDO(new String(bufferRegistro, 0, 2, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_SINLECTURA(new String(bufferRegistro, 3, 4, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CICLO(new String(bufferRegistro, 8, 3, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_DESCDEPTO(new String(bufferRegistro, 12, 32, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODMUNICIPIO(new String(bufferRegistro, 45, 3, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODESTADO(new String(bufferRegistro, 49, 32, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODSECTOR(new String(bufferRegistro, 82, 3, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_DESCSECTOR(new String(bufferRegistro, 86, 32, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_ruta( new String(bufferRegistro, 119, 13, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CUENTA(new String(bufferRegistro, 133, 10, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Clasedeservicio(new String(bufferRegistro, 144, 2, StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_DESCSERV( new String(bufferRegistro, 147, 32,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_UBICACION(new String(bufferRegistro, 180, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Nombre(new String(bufferRegistro, 182, 48,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Direccion(new String(bufferRegistro, 231, 64,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_NROMEDIDORES(new String(bufferRegistro, 296, 3,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_nrocontador(new String(bufferRegistro, 300, 20,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_MARCA(new String(bufferRegistro, 321, 3,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Tipomedida(new String(bufferRegistro, 325, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Factormultipicacion(new String(bufferRegistro, 328, 18,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Nroenteros(new String(bufferRegistro, 347, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Consumopromediocliente(new String(bufferRegistro, 349, 16,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_Lecturaanterior(new String(bufferRegistro, 366, 16,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_lecturatomada(new String(bufferRegistro, 383, 11,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_lecturamodificada1(new String(bufferRegistro, 395, 11,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_lecturamodificada2(new String(bufferRegistro, 407, 11,StandardCharsets.ISO_8859_1));

            if (tablaRegistroSalida_lecturatomada.trim().equals("null")) {
                tablaRegistroSalida_lecturatomada = "";
                if (!tablaRegistroSalida_lecturamodificada1.trim().equals("")) {
                    tablaRegistroSalida_lecturatomada =
                            tablaRegistroSalida_lecturamodificada1;
                }
            }

            settablaRegistroSalida_causadenolectura(new String(bufferRegistro, 419, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_comentario1(new String(bufferRegistro, 422, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_fechalectura(new String(bufferRegistro, 425, 8,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_horalectura(new String(bufferRegistro, 434, 6,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_intentos(new String(bufferRegistro, 441, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_modificaciones(new String(bufferRegistro, 443, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_ULTIMOMEDIDORLEIDO(new String(bufferRegistro, 445, 16,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_TIEMPO(new String(bufferRegistro, 462, 5,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_NROIMPRESIONES(new String(bufferRegistro, 468, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_IMPRESORA(new String(bufferRegistro, 470, 3,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_TERMINAL(new String(bufferRegistro, 474, 15,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_LECTOR(new String(bufferRegistro, 490, 4,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_informe(new String(bufferRegistro, 495, 250,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_leido(new String(bufferRegistro, 556+190, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_OBSERANT(new String(bufferRegistro, 558+190, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CONSERVICIODIRECTO(new String(bufferRegistro, 561+190, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_INDOBLIGAFOTO(new String(bufferRegistro, 563+190, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_cordenadax(new String(bufferRegistro, 565+190, 16,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_cordenaday(new String(bufferRegistro, 582+190, 16,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_distanciacalculada(new String(bufferRegistro, 599+190, 10,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODCATEGORIA(new String(bufferRegistro, 610+190, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_SUSPENDIDOCONLECTURA(new String(bufferRegistro, 613+190, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODNOTIFICACION(new String(bufferRegistro, 615+190, 2,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODIGOGRUPOENTREGA(new String(bufferRegistro, 618+190, 9,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_INDICADORCRITICASAC(new String(bufferRegistro, 628+190, 1,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CONSUMO_LIM_INFERIOR(new String(bufferRegistro, 630+190, 12,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CONSUMO_LIM_SUPERIOR(new String(bufferRegistro, 643+190, 12,StandardCharsets.ISO_8859_1));
            settablaRegistroSalida_CODIGO_SAC(new String(bufferRegistro, 656+190, 3,StandardCharsets.ISO_8859_1));
            setId(new String(bufferRegistro, 660+190, 11,StandardCharsets.ISO_8859_1).replace(",", " ").replace("|", " "));
            settablaRegistroSalida_fin("\r\n");

            ultimo_TablaRegistroSalida = registro;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //**cambiar esta funcion superor por lo nueva para ver su comportamiento

    public int lectura_Minima_TablaRegistroSalida(int registro) {
        encontro_TablaRegistroSalida = 0;

        try {
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            // Leer directamente en el buffer reutilizable
            rFile.readFully(bufferRegistro);
            // Convertir SOLO los segmentos necesarios
            settablaRegistroSalida_lecturatomada(new String(bufferRegistro,383, 11, StandardCharsets.UTF_8));
            settablaRegistroSalida_lecturamodificada1(new String(bufferRegistro,395, 11, StandardCharsets.UTF_8));
            if (tablaRegistroSalida_lecturatomada.trim().equals("null")) {
                tablaRegistroSalida_lecturatomada = "";
                if (!tablaRegistroSalida_lecturamodificada1.trim().equals("")) {
                    tablaRegistroSalida_lecturatomada = tablaRegistroSalida_lecturamodificada1;
                }
            }
            settablaRegistroSalida_causadenolectura(new String(bufferRegistro,419, 2, StandardCharsets.UTF_8));
            settablaRegistroSalida_leido(new String(bufferRegistro,746, 1));
            texto = new String(bufferRegistro,672+190, 2, StandardCharsets.UTF_8);

            ultimo_TablaRegistroSalida = registro;
            if (!texto.endsWith("\r\n")) {
                // Ajuste o marcar error          //    System.err.println("Advertencia: registro en " + x + " no termina en CRLF"); // Opcional: forzar CRLF
                ///PONER UN RETORNO QUE NOS INDIQUE EL ERROR
                return -1;
            }
        } catch (Exception d) {
            d.printStackTrace();
            return -2;
        }
        return 1;
    }





    //-------------------------------------------Ax : Bloque creado para Acelerar resumen....
    public void inicializaBloque() {
        try {
            File filepaths = new File(getArchivo_TablaRegistroSalida());
            rdr = new LineNumberReader(new FileReader(filepaths));
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    public LineNumberReader rdr;





    public int lectura_TablaRegistroSalidaX2(int registro) {
        encontro_TablaRegistroSalida = 0;

        try {
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            // Leer directamente en el buffer reutilizable
            rFile.readFully(bufferRegistro);
            // Convertir SOLO los segmentos necesarios
            settablaRegistroSalida_SUSPENDIDO(new String(bufferRegistro,0, 2, StandardCharsets.UTF_8));
            settablaRegistroSalida_CODESTADO(new String(bufferRegistro,49, 32, StandardCharsets.UTF_8));
            settablaRegistroSalida_CUENTA(new String(bufferRegistro,133, 10, StandardCharsets.UTF_8));
            settablaRegistroSalida_Nombre(new String(bufferRegistro,182, 48, StandardCharsets.UTF_8));
            settablaRegistroSalida_Direccion(new String(bufferRegistro,231, 64, StandardCharsets.UTF_8));
            settablaRegistroSalida_lecturatomada(new String(bufferRegistro,383, 11, StandardCharsets.UTF_8));
            settablaRegistroSalida_lecturamodificada1(new String(bufferRegistro,395, 11, StandardCharsets.UTF_8));
            if (tablaRegistroSalida_lecturatomada.trim().equals("null")) {
                tablaRegistroSalida_lecturatomada = "";
                if (!tablaRegistroSalida_lecturamodificada1.trim().equals("")) {
                    tablaRegistroSalida_lecturatomada = tablaRegistroSalida_lecturamodificada1;
                }
            }
            settablaRegistroSalida_causadenolectura(new String(bufferRegistro,419, 2, StandardCharsets.UTF_8));
            settablaRegistroSalida_comentario1(new String(bufferRegistro,422,2, StandardCharsets.UTF_8));
            settablaRegistroSalida_horalectura(new String(bufferRegistro,434, 6, StandardCharsets.UTF_8));
            settablaRegistroSalida_TIEMPO(new String(bufferRegistro,462, 5, StandardCharsets.UTF_8));
            if (gettablaRegistroSalida_TIEMPO().trim().equals("")) {
                tablaRegistroSalida_TIEMPO = "    0";
            }
            settablaRegistroSalida_informe(new String(bufferRegistro,495, 250, StandardCharsets.UTF_8));
            settablaRegistroSalida_leido(new String(bufferRegistro,556+190, 1));
            settablaRegistroSalida_distanciacalculada(new String(bufferRegistro,599+190, 10, StandardCharsets.UTF_8));
            settablaRegistroSalida_CODIGOGRUPOENTREGA(new String(bufferRegistro,618+190, 9, StandardCharsets.UTF_8));
            texto =new String(bufferRegistro,672+190, 2, StandardCharsets.UTF_8);
            boolean ResCaracter = contieneNoASCII(tablaRegistroSalida_informe);
            if (!ResCaracter)
            {
                ResCaracter=contieneNoASCII(tablaRegistroSalida_Nombre);
                if (!ResCaracter) {
                    ResCaracter = contieneNoASCII(tablaRegistroSalida_Direccion);
                }
            }
            if (ResCaracter)
            {
                //como respuesta que en alguno de esos campos hay un caracter malo
                //leeremos todo y escribimos para que nos lo quite
                lectura_TablaRegistroSalida(registro);
                escribir_TablaRegistroSalida(registro);
            }
            ultimo_TablaRegistroSalida = registro;
            if (!texto.endsWith("\r\n")) {
                // Ajuste o marcar error          //    System.err.println("Advertencia: registro en " + x + " no termina en CRLF"); // Opcional: forzar CRLF
                ///PONER UN RETORNO QUE NOS INDIQUE EL ERROR
                return -1;
            }
        } catch (Exception d) {
           String queinsoy=""+registro;
            d.printStackTrace();
            return -2;
        }
        return 1;
    }

 /*   public void lectura_TablaRegistroSalidasecambia(int x) {
        try {
            encontro_TablaRegistroSalida = 0;

            for (String line = null; (line = rdr.readLine()) != null; ) {
                if (rdr.getLineNumber() >= x) {
                    settablaRegistroSalida_CUENTA(line.substring(133, 143));
                    settablaRegistroSalida_lecturatomada(line.substring(383, 394));
                    settablaRegistroSalida_lecturamodificada1(texto.substring(395, 406));
                    if (tablaRegistroSalida_lecturatomada.trim().equals("null"))
                    {
                        tablaRegistroSalida_lecturatomada="";
                        if (!tablaRegistroSalida_lecturamodificada1.trim().equals(""))
                        {
                            tablaRegistroSalida_lecturatomada=tablaRegistroSalida_lecturamodificada1;
                        }
                    }
                    settablaRegistroSalida_causadenolectura(line.substring(419, 421));
                    settablaRegistroSalida_comentario1(line.substring(422, 424));
                    settablaRegistroSalida_horalectura(line.substring(434, 440));
                    settablaRegistroSalida_TIEMPO(line.substring(462, 467));
                    settablaRegistroSalida_informe(line.substring(495, 555));
                    settablaRegistroSalida_leido(line.substring(556, 557));
                    settablaRegistroSalida_distanciacalculada(line.substring(599, 609));
                    ultimo_TablaRegistroSalida = x;
                    return;
                }
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
    }*/
    public int lectura_TablaRegistroSalidaII (int registro) {
        encontro_TablaRegistroSalida = 0;

        try {
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            // Leer directamente en el buffer reutilizable
            rFile.readFully(bufferRegistro);
            // Convertir SOLO los segmentos necesarios
            settablaRegistroSalida_CUENTA(new String(bufferRegistro,133, 10, StandardCharsets.UTF_8));
            settablaRegistroSalida_lecturatomada(new String(bufferRegistro,383, 11, StandardCharsets.UTF_8));
            settablaRegistroSalida_lecturamodificada1(new String(bufferRegistro,395, 11, StandardCharsets.UTF_8));
            if (tablaRegistroSalida_lecturatomada.trim().equals("null"))
            {
                tablaRegistroSalida_lecturatomada="";
                if (!tablaRegistroSalida_lecturamodificada1.trim().equals(""))
                {
                    tablaRegistroSalida_lecturatomada=tablaRegistroSalida_lecturamodificada1;
                }
            }
            settablaRegistroSalida_causadenolectura(new String(bufferRegistro,419, 2, StandardCharsets.UTF_8));
            settablaRegistroSalida_comentario1(new String(bufferRegistro,422, 2, StandardCharsets.UTF_8));
            settablaRegistroSalida_horalectura(new String(bufferRegistro,434, 6, StandardCharsets.UTF_8));
            settablaRegistroSalida_TIEMPO(new String(bufferRegistro,462, 5, StandardCharsets.UTF_8));
            settablaRegistroSalida_informe(new String(bufferRegistro,495, 250, StandardCharsets.UTF_8));
            settablaRegistroSalida_leido(new String(bufferRegistro,556+190, 1, StandardCharsets.UTF_8));
            settablaRegistroSalida_distanciacalculada(new String(bufferRegistro,599+190, 10, StandardCharsets.UTF_8));
            //--------------------------------------///
           // texto = new String(bufferRegistro,672, 2, StandardCharsets.UTF_8);
            ultimo_TablaRegistroSalida = registro;
            if (!texto.endsWith("\r\n")) {
                // Ajuste o marcar error          //    System.err.println("Advertencia: registro en " + x + " no termina en CRLF"); // Opcional: forzar CRLF
                ///PONER UN RETORNO QUE NOS INDIQUE EL ERROR
                return -1;
            }
        } catch (Exception d) {
            String queinsoy=""+registro;
            d.printStackTrace();
            return -2;
        }
        return 1;
    }


/*    public void lectura_TablaRegistroSalida_VI1(int x) {
        try {
            encontro_TablaRegistroSalida = 0;

            for (String line = null; (line = rdr.readLine()) != null; ) {

                if (rdr.getLineNumber() >= x) {
                    settablaRegistroSalida_ruta(line.substring(119, 132));
                    settablaRegistroSalida_CUENTA(line.substring(133, 143));
                    settablaRegistroSalida_Nombre(texto.substring(182, 230));
                    settablaRegistroSalida_Direccion(texto.substring(231, 295));
                    settablaRegistroSalida_nrocontador(line.substring(300, 320));
                    ultimo_TablaRegistroSalida = x;
                    return;
                }
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
    }*/
    public int lectura_TablaRegistroSalida_VI (int registro) {
        encontro_TablaRegistroSalida = 0;

        try {
            // Leer directamente en el buffer reutilizable
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            rFile.readFully(bufferRegistro);
            // Convertir SOLO los segmentos necesarios
            settablaRegistroSalida_ruta(new String(bufferRegistro,119, 13, StandardCharsets.UTF_8));
            settablaRegistroSalida_CUENTA(new String(bufferRegistro,133, 10, StandardCharsets.UTF_8));
            settablaRegistroSalida_Nombre(new String(bufferRegistro,182, 48, StandardCharsets.UTF_8));
            settablaRegistroSalida_Direccion(new String(bufferRegistro,231, 64, StandardCharsets.UTF_8));
            settablaRegistroSalida_nrocontador(new String(bufferRegistro,300, 20, StandardCharsets.UTF_8));
            //--------------------------------------///
            texto = new String(bufferRegistro,672+190, 2, StandardCharsets.UTF_8);
            ultimo_TablaRegistroSalida = registro;
            if (!texto.endsWith("\r\n")) {
                // Ajuste o marcar error
                // System.err.println("Advertencia: registro en " + x + " no termina en CRLF"); // Opcional: forzar CRLF
                // PONER UN RETORNO QUE NOS INDIQUE EL ERROR
                return -1;
            }
        } catch (Exception d) {
            String queinsoy=""+registro;
            d.printStackTrace();
            return -2;
        }
        return 1;
    }

   public int lectura_TablaRegistroSalida_V (int registro) {
        encontro_TablaRegistroSalida = 0;

        try {
            posicion_TablaRegistroSalida(registro, LONGITUD_REGISTRO);
            // Leer directamente en el buffer reutilizable
            rFile.readFully(bufferRegistro);
            // Convertir SOLO los segmentos necesarios
            settablaRegistroSalida_ruta(new String(bufferRegistro,119, 13, StandardCharsets.UTF_8));
            settablaRegistroSalida_Nombre(new String(bufferRegistro,182, 48, StandardCharsets.UTF_8));
            settablaRegistroSalida_Direccion(new String(bufferRegistro,231, 64, StandardCharsets.UTF_8));
            //--------------------------------------///
            texto = new String(bufferRegistro,672+190, 2, StandardCharsets.UTF_8);
            ultimo_TablaRegistroSalida = registro;
            if (!texto.endsWith("\r\n")) {
                // Ajuste o marcar error          //    System.err.println("Advertencia: registro en " + x + " no termina en CRLF"); // Opcional: forzar CRLF
                ///PONER UN RETORNO QUE NOS INDIQUE EL ERROR
                return -1;
            }
        } catch (Exception d) {
            String queinsoy=""+registro;
            d.printStackTrace();
            return -2;
        }
        return 1;
    }



    public void terminaBloque() {
        try {
            rdr.close();
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    public void buscarSecuencial_TablaRegistroSalida(String codigo) {
        setEncontro_TablaRegistroSalida(0);
        for (int i = 0; i < (int) (getTotal_TablaRegistroSalida()); i++) {
            lectura_TablaRegistroSalida(i + 1);

            if (Long.parseLong(codigo.trim()) == Long.parseLong(tablaRegistroSalida_CUENTA.trim())) {
                setEncontro_TablaRegistroSalida(i + 1);
                posicion_TablaRegistroSalida(i + 1, LONGITUD_REGISTRO);
                i = getTotal_TablaRegistroSalida() + 10;
            } else {
                setEncontro_TablaRegistroSalida(0);
            }
        }
        return;
    }

    public int buscarSecuencial_TablaRegistroSalida(String codigo, int campo) {
        String codigo_buscar = "";
        encontro_TablaRegistroSalida = 0;

        for (int i = 0; i < total_TablaRegistroSalida; i++) {
            lectura_TablaRegistroSalida_VI(i + 1);

            if (campo == 0) {//buscarcontador
                codigo_buscar = tablaRegistroSalida_nrocontador.trim();

                if (codigo_buscar.length() > codigo.trim().length())
                    codigo_buscar = codigo_buscar.substring(codigo_buscar.length() - codigo.trim().length(), codigo.trim().length());
            } else if (campo == 1) {//buscar Cuenta
                codigo_buscar = tablaRegistroSalida_CUENTA.trim();//.Substring(0, codigo.trim().length());
                if (codigo_buscar.length() > codigo.trim().length())
                    codigo_buscar = codigo_buscar.substring(codigo_buscar.length() - codigo.trim().length(), codigo.trim().length());
            } else {
                codigo_buscar = tablaRegistroSalida_ruta.trim();
                if (codigo_buscar.length() > codigo.trim().length())
                    codigo_buscar = codigo_buscar.substring(codigo_buscar.length() - codigo.trim().length(), codigo.trim().length());
            }

            if (codigo.trim() == codigo_buscar) {

                encontro_TablaRegistroSalida = 1;
                posicion_TablaRegistroSalida(i + 1, LONGITUD_REGISTRO);
                return (i + 1);
            } else {
                encontro_TablaRegistroSalida = 0;
            }
        }
        return (0);
    }

    public int buscarSecuencial_TablaRegistroSalida_3(String codigo1) {
        encontro_TablaRegistroSalida = 0;
        inicializaBloque();
        //int codigo = Integer.parseInt(codigo1);
//int codigo = Integer.parseInt(codigo1);
        long codigo = Long.parseLong(codigo1.trim());


        for (int i = 0; i < total_TablaRegistroSalida; i++) {
            lectura_TablaRegistroSalida_VI(i + 1);
            long x = Long.parseLong(tablaRegistroSalida_CUENTA.trim());
            if (codigo == x) {
                encontro_TablaRegistroSalida = 1;
                posicion_TablaRegistroSalida(i + 1, LONGITUD_REGISTRO);
                terminaBloque();
                return (i + 1);
            }
        }
        terminaBloque();
        return (0);
    }

    public void buscarSecuencial_TablaRegistroSalida_cuenta(String codigo1) {
        encontro_TablaRegistroSalida = 0;
        inicializaBloque();
        //int codigo = Integer.parseInt(codigo1);
        long codigo = Long.parseLong(codigo1.trim());
        for (int i = 0; i < total_TablaRegistroSalida; i++) {
            lectura_TablaRegistroSalida_VI(i + 1);

            if (codigo == Long.parseLong(tablaRegistroSalida_CUENTA.trim())) {
                setEncontro_TablaRegistroSalida(i + 1);
                posicion_TablaRegistroSalida(i + 1, LONGITUD_REGISTRO);
                i = getTotal_TablaRegistroSalida() + 10;
                return;
            }
        }
        setEncontro_TablaRegistroSalida(0);
        terminaBloque();
    }
}
