package com.gstolima.tablas;

import android.util.Log;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.charset.StandardCharsets;

/// <summary>
/// Descripción breve del archivo a generar...EnvioGPS
/// </summary>
public class EnvioGPS {

    //falta catastro falta tiempo falta uso marca periodolectura informe
    String sep = ";";
    String EnvioGPS_ciclo;//ciclo
    String EnvioGPS_mununicipio;//zona
    String EnvioGPS_seccion;//ruta
    String EnvioGPS_departamento;//colocar ordenruta
    String EnvioGPS_anno;
    String EnvioGPS_mes;
    String EnvioGPS_cuenta;//cuenta6
    String EnvioGPS_digitochequeo;
    String EnvioGPS_NroContador;//nrocontador
    String EnvioGPS_idcontador;
    String EnvioGPS_lecturatomada;//lectura
    String EnvioGPS_causadenolectura;//anomalia
    String EnvioGPS_descanomalia;
    String EnvioGPS_comentario;//comentario
    String EnvioGPS_desccomentario;
    String EnvioGPS_fechayhoralectura;//fecha lectura
    String EnvioGPS_CodLector;//lector
    String EnvioGPS_primermedidor;
    String EnvioGPS_HoraImpresion;//hora lectura
    String EnvioGPS_NombreArchivo;//normal
    String EnvioGPS_Longitud;//latitud
    String EnvioGPS_Latitud;//longitud
    String EnvioGPS_NroSatelites;//satelites normal
    String EnvioGPS_Distancia;//normal
    String EnvioGPS_FechaHoraSatelite;//normal
    String EnvioGPS_AltitudSatelite;//normal
    String EnvioGPS_Terminal;//terminal
    String EnvioGPS_IndicadorNovedad;
    String EnvioGPS_Indcodbarras;
    String EnvioGPS_Intentos;//intentos
    String EnvioGPS_criticapda;//critica
    //String EnvioGPS_ValorFacturado;
    String EnvioGPS_ConsumoFacturado;//consumo
    String EnvioGPS_SubContribucion;
    String EnvioGPS_Consumo1;
    String EnvioGPS_Consumo2;
    String EnvioGPS_Consumo3;
    String EnvioGPS_NroConceptos;
    String EnvioGPS_IndFacturacion;
    String EnvioGPS_NroFactura;
    String EnvioGPS_FechaVence;
    String EnvioGPS_FechaCorte;
    String EnvioGPS_LecturaModificada1;//normal
    String EnvioGPS_LecturaModificada2;//normal
    String EnvioGPS_Digitos;
    String EnvioGPS_Procesado;
    String EnvioGPS_NumeroConceptos;
    String EnvioGPS_PrimerConcepto;
    String EnvioGPS_CritiaSIEC;//no se
    String EnvioGPS_CodNovedad;
    String EnvioGPS_LecturaAnterior;//normal
    String EnvioGPS_EstadoEnvio;//estado envio R M N
    String id;

    String totalcausasnolectura;
    String totalLecturas;
    String totalConsumoBajo;
    String totalConsumoAlto;
    String totalConsumoNormal;
    String totalLecurasIguales;
    String totalLeidas;
    String numObser;
    String numInformes;
    String totalTiempoProm;
    String totalDistanciaProm;
    //nuevo campo 2024 concepto de critica del consumo
    String EnvioGPS_CODIGO_SAC;

    String EnvioGPS_CRNL;

    //nuevas variables
    private String EnvioGPS_fichacatastral; //""
    private String EnvioGPS_Tiempo; // TIEMPO
    private String EnvioGPS_uso;//  Clasedeservicio
    private String EnvioGPS_marcamedidor; //marca
    private String EnvioGPS_informe; // informe
    private String EnvioGPS_PERIODOLECTURA; // fechalectura (menos 2)  ??
    private String EnvioGPS_estrato;// ????????????


    public String getEnvioGPS_estrato() {
        return EnvioGPS_estrato;
    }

    public void setEnvioGPS_estrato(String envioGPS_estrato) {
        EnvioGPS_estrato = envioGPS_estrato;
    }


    public String getEnvioGPS_CODIGO_SAC() {
        return EnvioGPS_CODIGO_SAC;
    }

    public void setEnvioGPS_CODIGO_SAC(String envioGPS_CODIGO_SAC) {
        EnvioGPS_CODIGO_SAC = envioGPS_CODIGO_SAC;
    }

    public String getEnvioGPS_fichacatastral() {
        return EnvioGPS_fichacatastral;
    }

    public void setEnvioGPS_fichacatastral(String envioGPS_fichacatastral) {
        EnvioGPS_fichacatastral = envioGPS_fichacatastral;
    }

    public String getEnvioGPS_Tiempo() {
        return EnvioGPS_Tiempo;
    }

    public void setEnvioGPS_Tiempo(String envioGPS_Tiempo) {
        EnvioGPS_Tiempo = envioGPS_Tiempo;
    }

    public String getEnvioGPS_uso() {
        return EnvioGPS_uso;
    }

    public void setEnvioGPS_uso(String envioGPS_uso) {
        EnvioGPS_uso = envioGPS_uso;
    }

    public String getEnvioGPS_marcamedidor() {
        return EnvioGPS_marcamedidor;
    }

    public void setEnvioGPS_marcamedidor(String envioGPS_marcamedidor) {
        EnvioGPS_marcamedidor = envioGPS_marcamedidor;
    }

    public String getEnvioGPS_informe() {
        return EnvioGPS_informe;
    }

    public void setEnvioGPS_informe(String envioGPS_informe) {
        EnvioGPS_informe = envioGPS_informe;
    }

    public String getEnvioGPS_PERIODOLECTURA() {
        return EnvioGPS_PERIODOLECTURA;
    }

    public void setEnvioGPS_PERIODOLECTURA(String envioGPS_PERIODOLECTURA) {
        EnvioGPS_PERIODOLECTURA = envioGPS_PERIODOLECTURA;
    }

    //.---------------------------------------------------------------------------------------------

    public String getEnvioGPS_CICLO() {
        return EnvioGPS_ciclo;
    }

    public void setEnvioGPS_ciclo(String EnvioGPS_ciclo) {
        this.EnvioGPS_ciclo = EnvioGPS_ciclo;
    }

    public String getEnvioGPS_MUNUNICIPIO() {
        return EnvioGPS_mununicipio;
    }

    public void setEnvioGPS_mununicipio(String EnvioGPS_mununicipio) {
        this.EnvioGPS_mununicipio = EnvioGPS_mununicipio;
    }

    public String getEnvioGPS_SECCION() {
        return EnvioGPS_seccion;
    }

    public void setEnvioGPS_seccion(String EnvioGPS_seccion) {
        this.EnvioGPS_seccion = EnvioGPS_seccion;
    }

    public String getEnvioGPS_DEPARTAMENTO() {
        return EnvioGPS_departamento;
    }

    public void setEnvioGPS_departamento(String EnvioGPS_departamento) {
        this.EnvioGPS_departamento = EnvioGPS_departamento;
    }

    public String getEnvioGPS_ANNO() {
        return EnvioGPS_anno;
    }

    public void setEnvioGPS_anno(String EnvioGPS_anno) {
        this.EnvioGPS_anno = EnvioGPS_anno;
    }

    public String getEnvioGPS_MES() {
        return EnvioGPS_mes;
    }

    public void setEnvioGPS_mes(String EnvioGPS_mes) {
        this.EnvioGPS_mes = EnvioGPS_mes;
    }

    public String getEnvioGPS_CUENTA() {
        return EnvioGPS_cuenta;
    }

    public void setEnvioGPS_cuenta(String EnvioGPS_cuenta) {
        this.EnvioGPS_cuenta = EnvioGPS_cuenta;
    }

    public String getEnvioGPS_DIGITOCHEQUEO() {
        return EnvioGPS_digitochequeo;
    }

    public void setEnvioGPS_digitochequeo(String EnvioGPS_digitochequeo) {
        this.EnvioGPS_digitochequeo = EnvioGPS_digitochequeo;
    }

    public String getEnvioGPS_NROCONTADOR() {
        return EnvioGPS_NroContador;
    }

    public void setEnvioGPS_NroContador(String EnvioGPS_NroContador) {
        this.EnvioGPS_NroContador = EnvioGPS_NroContador;
    }

    public String getEnvioGPS_IDCONTADOR() {
        return EnvioGPS_idcontador;
    }

    public void setEnvioGPS_idcontador(String EnvioGPS_idcontador) {
        this.EnvioGPS_idcontador = EnvioGPS_idcontador;
    }

    public String getEnvioGPS_LECTURATOMADA() {
        return EnvioGPS_lecturatomada;
    }

    public void setEnvioGPS_lecturatomada(String EnvioGPS_lecturatomada) {
        this.EnvioGPS_lecturatomada = EnvioGPS_lecturatomada;
    }

    public String getEnvioGPS_CAUSADENOLECTURA() {
        return EnvioGPS_causadenolectura;
    }

    public void setEnvioGPS_causadenolectura(String EnvioGPS_causadenolectura) {
        this.EnvioGPS_causadenolectura = EnvioGPS_causadenolectura;
    }

    public String getEnvioGPS_DESCANOMALIA() {
        return EnvioGPS_descanomalia;
    }

    public void setEnvioGPS_descanomalia(String EnvioGPS_descanomalia) {
        this.EnvioGPS_descanomalia = EnvioGPS_descanomalia;
    }

    public String getEnvioGPS_COMENTARIO() {
        return EnvioGPS_comentario;
    }

    public void setEnvioGPS_comentario(String EnvioGPS_comentario) {
        this.EnvioGPS_comentario = EnvioGPS_comentario;
    }

    public String getEnvioGPS_DESCCOMENTARIO() {
        return EnvioGPS_desccomentario;
    }

    public void setEnvioGPS_desccomentario(String EnvioGPS_desccomentario) {
        this.EnvioGPS_desccomentario = EnvioGPS_desccomentario;
    }

    public String getEnvioGPS_FECHAYHORALECTURA() {
        return EnvioGPS_fechayhoralectura;
    }

    public void setEnvioGPS_fechayhoralectura(String EnvioGPS_fechayhoralectura) {
        this.EnvioGPS_fechayhoralectura = EnvioGPS_fechayhoralectura;
    }

    public String getEnvioGPS_CODLECTOR() {
        return EnvioGPS_CodLector;
    }

    public void setEnvioGPS_CodLector(String EnvioGPS_CodLector) {
        this.EnvioGPS_CodLector = EnvioGPS_CodLector;
    }

    public String getEnvioGPS_PRIMERMEDIDOR() {
        return EnvioGPS_primermedidor;
    }

    public void setEnvioGPS_primermedidor(String EnvioGPS_primermedidor) {
        this.EnvioGPS_primermedidor = EnvioGPS_primermedidor;
    }

    public String getEnvioGPS_HORAIMPRESION() {
        return EnvioGPS_HoraImpresion;
    }

    public void setEnvioGPS_HoraImpresion(String EnvioGPS_HoraImpresion) {
        this.EnvioGPS_HoraImpresion = EnvioGPS_HoraImpresion;
    }

    public String getEnvioGPS_NOMBREARCHIVO() {
        return EnvioGPS_NombreArchivo;
    }

    public void setEnvioGPS_NombreArchivo(String EnvioGPS_NombreArchivo) {
        this.EnvioGPS_NombreArchivo = EnvioGPS_NombreArchivo;
    }

    public String getEnvioGPS_LONGITUD() {
        return EnvioGPS_Longitud;
    }

    public void setEnvioGPS_Longitud(String EnvioGPS_Longitud) {
        this.EnvioGPS_Longitud = EnvioGPS_Longitud;
    }

    public String getEnvioGPS_LATITUD() {
        return EnvioGPS_Latitud;
    }

    public void setEnvioGPS_Latitud(String EnvioGPS_Latitud) {
        this.EnvioGPS_Latitud = EnvioGPS_Latitud;
    }

    public String getEnvioGPS_NROSATELITES() {
        return EnvioGPS_NroSatelites;
    }

    public void setEnvioGPS_NroSatelites(String EnvioGPS_NroSatelites) {
        this.EnvioGPS_NroSatelites = EnvioGPS_NroSatelites;
    }

    public String getEnvioGPS_DISTANCIA() {
        return EnvioGPS_Distancia;
    }

    public void setEnvioGPS_Distancia(String EnvioGPS_Distancia) {
        this.EnvioGPS_Distancia = EnvioGPS_Distancia;
    }

    public String getEnvioGPS_FECHAHORASATELITE() {
        return EnvioGPS_FechaHoraSatelite;
    }

    public void setEnvioGPS_FechaHoraSatelite(String EnvioGPS_FechaHoraSatelite) {
        this.EnvioGPS_FechaHoraSatelite = EnvioGPS_FechaHoraSatelite;
    }

    public String getEnvioGPS_ALTITUDSATELITE() {
        return EnvioGPS_AltitudSatelite;
    }

    public void setEnvioGPS_AltitudSatelite(String EnvioGPS_AltitudSatelite) {
        this.EnvioGPS_AltitudSatelite = EnvioGPS_AltitudSatelite;
    }

    public String getEnvioGPS_TERMINAL() {
        return EnvioGPS_Terminal;
    }

    public void setEnvioGPS_Terminal(String EnvioGPS_Terminal) {
        this.EnvioGPS_Terminal = EnvioGPS_Terminal;
    }

    public String getEnvioGPS_INDICADORNOVEDAD() {
        return EnvioGPS_IndicadorNovedad;
    }

    public void setEnvioGPS_IndicadorNovedad(String EnvioGPS_IndicadorNovedad) {
        this.EnvioGPS_IndicadorNovedad = EnvioGPS_IndicadorNovedad;
    }

    public String getEnvioGPS_INDCODBARRAS() {
        return EnvioGPS_Indcodbarras;
    }

    public void setEnvioGPS_Indcodbarras(String EnvioGPS_Indcodbarras) {
        this.EnvioGPS_Indcodbarras = EnvioGPS_Indcodbarras;
    }

    public String getEnvioGPS_INTENTOS() {
        return EnvioGPS_Intentos;
    }

    public void setEnvioGPS_Intentos(String EnvioGPS_Intentos) {
        this.EnvioGPS_Intentos = EnvioGPS_Intentos;
    }

    public String getEnvioGPS_CRITICAPDA() {
        return EnvioGPS_criticapda;
    }

    public void setEnvioGPS_criticapda(String EnvioGPS_criticapda) {
        this.EnvioGPS_criticapda = EnvioGPS_criticapda;
    }

   /* public String getEnvioGPS_VALORFACTURADO() {
        return EnvioGPS_ValorFacturado;
    }

    public void setEnvioGPS_ValorFacturado(String EnvioGPS_ValorFacturado) {
        this.EnvioGPS_ValorFacturado = EnvioGPS_ValorFacturado;
    }*/

    public String getEnvioGPS_CONSUMOFACTURADO() {
        return EnvioGPS_ConsumoFacturado;
    }

    public void setEnvioGPS_ConsumoFacturado(String EnvioGPS_ConsumoFacturado) {
        this.EnvioGPS_ConsumoFacturado = EnvioGPS_ConsumoFacturado;
    }

    public String getEnvioGPS_SUBCONTRIBUCION() {
        return EnvioGPS_SubContribucion;
    }

    public void setEnvioGPS_SubContribucion(String EnvioGPS_SubContribucion) {
        this.EnvioGPS_SubContribucion = EnvioGPS_SubContribucion;
    }

    public String getEnvioGPS_CONSUMO1() {
        return EnvioGPS_Consumo1;
    }

    public void setEnvioGPS_Consumo1(String EnvioGPS_Consumo1) {
        this.EnvioGPS_Consumo1 = EnvioGPS_Consumo1;
    }

    public String getEnvioGPS_CONSUMO2() {
        return EnvioGPS_Consumo2;
    }

    public void setEnvioGPS_Consumo2(String EnvioGPS_Consumo2) {
        this.EnvioGPS_Consumo2 = EnvioGPS_Consumo2;
    }

    public String getEnvioGPS_CONSUMO3() {
        return EnvioGPS_Consumo3;
    }

    public void setEnvioGPS_Consumo3(String EnvioGPS_Consumo3) {
        this.EnvioGPS_Consumo3 = EnvioGPS_Consumo3;
    }

    public String getEnvioGPS_NROCONCEPTOS() {
        return EnvioGPS_NroConceptos;
    }

    public void setEnvioGPS_NroConceptos(String EnvioGPS_NroConceptos) {
        this.EnvioGPS_NroConceptos = EnvioGPS_NroConceptos;
    }

    public String getEnvioGPS_INDFACTURACION() {
        return EnvioGPS_IndFacturacion;
    }

    public void setEnvioGPS_IndFacturacion(String EnvioGPS_IndFacturacion) {
        this.EnvioGPS_IndFacturacion = EnvioGPS_IndFacturacion;
    }

    public String getEnvioGPS_NROFACTURA() {
        return EnvioGPS_NroFactura;
    }

    public void setEnvioGPS_NroFactura(String EnvioGPS_NroFactura) {
        this.EnvioGPS_NroFactura = EnvioGPS_NroFactura;
    }

    public String getEnvioGPS_FECHAVENCE() {
        return EnvioGPS_FechaVence;
    }

    public void setEnvioGPS_FechaVence(String EnvioGPS_FechaVence) {
        this.EnvioGPS_FechaVence = EnvioGPS_FechaVence;
    }

    public String getEnvioGPS_FECHACORTE() {
        return EnvioGPS_FechaCorte;
    }

    public void setEnvioGPS_FechaCorte(String EnvioGPS_FechaCorte) {
        this.EnvioGPS_FechaCorte = EnvioGPS_FechaCorte;
    }

    public String getEnvioGPS_LECTURAMODIFICADA1() {
        return EnvioGPS_LecturaModificada1;
    }

    public void setEnvioGPS_LecturaModificada1(String EnvioGPS_LecturaModificada1) {
        this.EnvioGPS_LecturaModificada1 = EnvioGPS_LecturaModificada1;
    }

    public String getEnvioGPS_LECTURAMODIFICADA2() {
        return EnvioGPS_LecturaModificada2;
    }

    public void setEnvioGPS_LecturaModificada2(String EnvioGPS_LecturaModificada2) {
        this.EnvioGPS_LecturaModificada2 = EnvioGPS_LecturaModificada2;
    }

    public String getEnvioGPS_DIGITOS() {
        return EnvioGPS_Digitos;
    }

    public void setEnvioGPS_Digitos(String EnvioGPS_Digitos) {
        this.EnvioGPS_Digitos = EnvioGPS_Digitos;
    }

    public String getEnvioGPS_PROCESADO() {
        return EnvioGPS_Procesado;
    }

    public void setEnvioGPS_Procesado(String EnvioGPS_Procesado) {
        this.EnvioGPS_Procesado = EnvioGPS_Procesado;
    }

    public String getEnvioGPS_NUMEROCONCEPTOS() {
        return EnvioGPS_NumeroConceptos;
    }

    public void setEnvioGPS_NumeroConceptos(String EnvioGPS_NumeroConceptos) {
        this.EnvioGPS_NumeroConceptos = EnvioGPS_NumeroConceptos;
    }

    public String getEnvioGPS_PRIMERCONCEPTO() {
        return EnvioGPS_PrimerConcepto;
    }

    public void setEnvioGPS_PrimerConcepto(String EnvioGPS_PrimerConcepto) {
        this.EnvioGPS_PrimerConcepto = EnvioGPS_PrimerConcepto;
    }

    public String getEnvioGPS_CRITIASIEC() {
        return EnvioGPS_CritiaSIEC;
    }

    public void setEnvioGPS_CritiaSIEC(String EnvioGPS_CritiaSIEC) {
        this.EnvioGPS_CritiaSIEC = EnvioGPS_CritiaSIEC;
    }

    public String getEnvioGPS_CODNOVEDAD() {
        return EnvioGPS_CodNovedad;
    }

    public void setEnvioGPS_CodNovedad(String EnvioGPS_CodNovedad) {
        this.EnvioGPS_CodNovedad = EnvioGPS_CodNovedad;
    }

    public String getEnvioGPS_LECTURAANTERIOR() {
        return EnvioGPS_LecturaAnterior;
    }

    public void setEnvioGPS_LecturaAnterior(String EnvioGPS_LecturaAnterior) {
        this.EnvioGPS_LecturaAnterior = EnvioGPS_LecturaAnterior;
    }

    public String getEnvioGPS_ESTADOENVIO() {
        return EnvioGPS_EstadoEnvio;
    }

    public void setEnvioGPS_EstadoEnvio(String EnvioGPS_EstadoEnvio) {
        this.EnvioGPS_EstadoEnvio = EnvioGPS_EstadoEnvio;
    }

    public String getTotalConsumoBajo() {
        return totalConsumoBajo;
    }

    public void setTotalConsumoBajo(String totalConsumoBajo) {
        this.totalConsumoBajo = totalConsumoBajo;
    }

    public String getTotalConsumoAlto() {
        return totalConsumoAlto;
    }

    public void setTotalConsumoAlto(String totalConsumoAlto) {
        this.totalConsumoAlto = totalConsumoAlto;
    }

    public String getTotalConsumoNormal() {
        return totalConsumoNormal;
    }

    public void setTotalConsumoNormal(String totalConsumoNormal) {
        this.totalConsumoNormal = totalConsumoNormal;
    }

    public String getTotalLecurasIguales() {
        return totalLecurasIguales;
    }

    public void setTotalLecurasIguales(String totalLecurasIguales) {
        this.totalLecurasIguales = totalLecurasIguales;
    }

    public String getTotalLeidas() {
        return totalLeidas;
    }

    public void setTotalLeidas(String totalLeidas) {
        this.totalLeidas = totalLeidas;
    }

    public String getNumObser() {
        return numObser;
    }

    public void setNumObser(String numObser) {
        this.numObser = numObser;
    }

    public String getNumInformes() {
        return numInformes;
    }

    public void setNumInformes(String numInformes) {
        this.numInformes = numInformes;
    }

    public String getTotalTiempoProm() {
        return totalTiempoProm;
    }

    public void setTotalTiempoProm(String totalTiempoProm) {
        this.totalTiempoProm = totalTiempoProm;
    }

    public String getTotalDistanciaProm() {
        return totalDistanciaProm;
    }

    public void setTotalDistanciaProm(String totalDistanciaProm) {
        this.totalDistanciaProm = totalDistanciaProm;
    }

    public String getEnvioGPS_CRNL() {
        return EnvioGPS_CRNL;
    }

    public void setEnvioGPS_CRNL(String EnvioGPS_CRNL) {
        this.EnvioGPS_CRNL = EnvioGPS_CRNL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalcausasnolectura() {
        return totalcausasnolectura;
    }

    public void setTotalcausasnolectura(String totalcausasnolectura) {
        this.totalcausasnolectura = totalcausasnolectura;
    }

    public String getTotalLecturas() {
        return totalLecturas;
    }

    public void setTotalLecturas(String totalLecturas) {
        this.totalLecturas = totalLecturas;
    }

    Utils utils = new Utils();//Ax log y utilidades
    File logfile = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/LOGEVENTOS.LOG");
    private int _fileSize;
    BufferedReader fin;
    byte[] byteArray;
    public String archivo_EnvioGPS;
    static final int LONGITUD_REGISTRO = 522 + 190;//+ 1 caracter para cuenta ya que en celsia valle cambia
    public int total_EnvioGPS;
    int ultimo_EnvioGPS;
    public int encontro_EnvioGPS;
    String buscar_EnvioGPS;
    String texto;
    //    File ruta_sd = Environment.getExternalStorageDirectory();
    //    File ruta_sd = new File('C:\\Users\\SGlobal\\Documents\\PruebasJava\\CensoSalida.txt');
    RandomAccessFile rFile;
    private final byte[] bufferRegistroCliente =
            new byte[LONGITUD_REGISTRO];

    public Boolean abrir_EnvioGPS(String nombreArchivo) {
        if (nombreArchivo.length() == 0) {
            return false;
        }
        try {
            rFile = new RandomAccessFile(nombreArchivo, "rw"); //C:/Users/SGlobal/Documents/Pruebas Java/CensoSalida.txt
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        archivo_EnvioGPS = nombreArchivo;
        return abrir(archivo_EnvioGPS);
    }


    private Boolean abrir(String nombre_archivo) {
        try {
            _fileSize = (int) rFile.length();//Ax: fileSize

            if (_fileSize > 0) {
                byteArray = new byte[LONGITUD_REGISTRO];//Ax:  fileSize
                rFile.readFully(byteArray, 0, LONGITUD_REGISTRO);//Ax: fileSize
                // texto = new String(byteArray);
                total_EnvioGPS = _fileSize / LONGITUD_REGISTRO;
            } else {
                byteArray = new byte[_fileSize];
                rFile.readFully(byteArray, 0, _fileSize);
                total_EnvioGPS = 0;
            }
            return true;
        } catch (Exception ex) {
            //       Log.e('Ficheros', 'Error al leer fichero desde tarjeta SD');
            ex.printStackTrace();
            return false;
        }
    }

    public void Cerrar_EnvioGPS() {
        // cerrar el archivo abierto
        try {
            rFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribir_EnvioGPS(int posicion) {
        posicion_EnvioGPS(posicion, LONGITUD_REGISTRO);
        rellenar_EnvioGPS();
        String texto = EnvioGPS_ciclo + sep + EnvioGPS_mununicipio + sep + EnvioGPS_seccion + sep + EnvioGPS_departamento + sep + EnvioGPS_cuenta + sep + EnvioGPS_NroContador
                + sep + EnvioGPS_marcamedidor + sep + EnvioGPS_fichacatastral + sep + EnvioGPS_fechayhoralectura + sep + EnvioGPS_HoraImpresion + sep + EnvioGPS_lecturatomada
                + sep + EnvioGPS_causadenolectura + sep + EnvioGPS_CodLector + sep + EnvioGPS_Terminal + sep + EnvioGPS_PERIODOLECTURA + sep + EnvioGPS_Longitud + sep
                + EnvioGPS_Latitud + sep + EnvioGPS_NroSatelites + sep + EnvioGPS_informe + sep + EnvioGPS_criticapda + sep + EnvioGPS_EstadoEnvio + sep + EnvioGPS_comentario
                + sep + EnvioGPS_Intentos + sep + EnvioGPS_FechaHoraSatelite + sep + EnvioGPS_AltitudSatelite + sep + EnvioGPS_Distancia + sep + EnvioGPS_LecturaModificada1 + sep
                + EnvioGPS_LecturaModificada2 + sep + EnvioGPS_Tiempo + sep + EnvioGPS_ConsumoFacturado + sep + EnvioGPS_LecturaAnterior + sep + EnvioGPS_estrato + sep + EnvioGPS_uso
                + sep + EnvioGPS_NombreArchivo + sep + id + sep + totalcausasnolectura + sep + totalLecturas + sep + totalConsumoBajo + sep + totalConsumoAlto + sep + totalConsumoNormal
                + sep + totalLecurasIguales + sep + totalLeidas + sep + numObser + sep + numInformes + sep + totalTiempoProm + sep + totalDistanciaProm+sep+ EnvioGPS_CODIGO_SAC + sep + EnvioGPS_CRNL;
        try {
            int x = texto.length();
            //utils.Log(logfile, "CADENA DE ENVIO :" + texto);
            if (null != rFile && (x == LONGITUD_REGISTRO)) {
                if (EnvioGPS_ciclo !=null && !texto.substring(0,4).trim().equals("")) {
                    rFile.writeBytes(texto);
                    return true;
                } else {
                    utils.Log(logfile, "ERROR escribir_EnvioGPS(), posibles NULLS, EnvioGPS_ciclo:" + EnvioGPS_ciclo);
                }
            } else {
                utils.Log(logfile, "Se presento problema al escribir en el archivo; longitud Errada: tamaño texto: " + texto.length());
                return false;
            }
        } catch (Exception ioe) {
            utils.Log(logfile, "no graba registro2- " + ioe.getMessage() + "-" + texto);
            System.out.println("Se presento problema al escribir en el archivo Longitud Errada: " + LONGITUD_REGISTRO);
            return false;
        }
        return false;
    }

    public void rellenar_EnvioGPS() {
        try {
            EnvioGPS_ciclo = String.format("%-3s", EnvioGPS_ciclo);
            EnvioGPS_mununicipio = String.format("%-3s", EnvioGPS_mununicipio);
            EnvioGPS_seccion = String.format("%-4s", EnvioGPS_seccion);
            EnvioGPS_departamento = String.format("%-13s", EnvioGPS_departamento);
            EnvioGPS_cuenta = String.format("%-10s", EnvioGPS_cuenta);
            EnvioGPS_NroContador = String.format("%20s", EnvioGPS_NroContador);
            EnvioGPS_marcamedidor = String.format("%30s", EnvioGPS_marcamedidor);
            EnvioGPS_fichacatastral = String.format("%15s", EnvioGPS_fichacatastral);
            EnvioGPS_fechayhoralectura = String.format("%8s", EnvioGPS_fechayhoralectura);
            EnvioGPS_HoraImpresion = String.format("%8s", EnvioGPS_HoraImpresion);
            EnvioGPS_lecturatomada = String.format("%-9s", EnvioGPS_lecturatomada);
            EnvioGPS_causadenolectura = String.format("%2s", EnvioGPS_causadenolectura);
            EnvioGPS_CodLector = String.format("%-10s", EnvioGPS_CodLector);
            EnvioGPS_Terminal = String.format("%-15s", EnvioGPS_Terminal);
            EnvioGPS_PERIODOLECTURA = String.format("%20s", EnvioGPS_PERIODOLECTURA);
            EnvioGPS_Longitud = String.format("%-20s", EnvioGPS_Longitud);
            EnvioGPS_Latitud = String.format("%-20s", EnvioGPS_Latitud);
            EnvioGPS_NroSatelites = String.format("%-3s", EnvioGPS_NroSatelites);
            if (EnvioGPS_informe.trim().length()>250) {
                EnvioGPS_informe = EnvioGPS_informe.trim().substring(0, 249);
            }
            EnvioGPS_informe = String.format("%-250s", EnvioGPS_informe);

            EnvioGPS_criticapda = String.format("%1s", EnvioGPS_criticapda);
            EnvioGPS_EstadoEnvio = String.format("%1s", EnvioGPS_EstadoEnvio);
            EnvioGPS_comentario = String.format("%2s", EnvioGPS_comentario);
            EnvioGPS_Intentos = String.format("%1s", EnvioGPS_Intentos);

            if (EnvioGPS_FechaHoraSatelite.trim().length() > 20)
                EnvioGPS_FechaHoraSatelite = EnvioGPS_FechaHoraSatelite.substring(0, 20);
            else
                EnvioGPS_FechaHoraSatelite = String.format("%20s", EnvioGPS_FechaHoraSatelite);

            EnvioGPS_AltitudSatelite = String.format("%10s", EnvioGPS_AltitudSatelite);
            EnvioGPS_Distancia = String.format("%10s", EnvioGPS_Distancia);
            EnvioGPS_LecturaModificada1 = String.format("%11s", EnvioGPS_LecturaModificada1);
            EnvioGPS_LecturaModificada2 = String.format("%11s", EnvioGPS_LecturaModificada2);
            EnvioGPS_Tiempo = String.format("%5s", EnvioGPS_Tiempo);
            EnvioGPS_ConsumoFacturado = String.format("%-10s", EnvioGPS_ConsumoFacturado);
            EnvioGPS_LecturaAnterior = String.format("%-10s", EnvioGPS_LecturaAnterior);
            EnvioGPS_estrato = String.format("%-2s", EnvioGPS_estrato);
            EnvioGPS_uso = String.format("%-2s", EnvioGPS_uso);
            EnvioGPS_NombreArchivo = String.format("%12s", EnvioGPS_NombreArchivo);
            id = String.format("%11s", id);

            totalcausasnolectura = String.format("%6s", totalcausasnolectura);
            totalLecturas = String.format("%6s", totalLecturas);
            totalConsumoBajo = String.format("%6s", totalConsumoBajo);
            totalConsumoAlto = String.format("%6s", totalConsumoAlto);
            totalConsumoNormal = String.format("%6s", totalConsumoNormal);
            totalLecurasIguales = String.format("%6s", totalLecurasIguales);
            totalLeidas = String.format("%6s", totalLeidas);
            numObser = String.format("%6s", numObser);
            numInformes = String.format("%6s", numInformes);
            totalTiempoProm = String.format("%12s", totalTiempoProm);
            totalDistanciaProm = String.format("%12s", totalDistanciaProm);
            EnvioGPS_CODIGO_SAC = String.format("%-3s", EnvioGPS_CODIGO_SAC.trim());
            EnvioGPS_CRNL = "\r\n";//String.format("%-2s", EnvioGPS_CRNL);
        } catch (Exception e) {
            System.out.println("Se presento problema al escribir en el archivo EnvioGPS.dat..");
            e.printStackTrace();
        }
        return;
    }

    public void posicion_EnvioGPS(int registro, int tamaño) {
        try {
            rFile.seek((registro - 1) * tamaño);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Escribir los registros actual en el archivo en posicion
    public void lectura_EnvioGPS(int registro) {
        //String[] campos;
        encontro_EnvioGPS = 0;
        posicion_EnvioGPS(registro, LONGITUD_REGISTRO);
        try {
            //Ax: int fileSize = (int) rFile.length();
            rFile.readFully(bufferRegistroCliente);
            setEnvioGPS_ciclo(new String(bufferRegistroCliente,0,3, StandardCharsets.UTF_8));
            setEnvioGPS_mununicipio(new String(bufferRegistroCliente,4,3, StandardCharsets.UTF_8));
            setEnvioGPS_seccion(new String(bufferRegistroCliente,8,4, StandardCharsets.UTF_8));
            setEnvioGPS_departamento(new String(bufferRegistroCliente,13,13, StandardCharsets.UTF_8));
            setEnvioGPS_cuenta(new String(bufferRegistroCliente,27,10, StandardCharsets.UTF_8));
            setEnvioGPS_NroContador(new String(bufferRegistroCliente,38,20, StandardCharsets.UTF_8));
            setEnvioGPS_marcamedidor(new String(bufferRegistroCliente,59,30, StandardCharsets.UTF_8));
            setEnvioGPS_fichacatastral(new String(bufferRegistroCliente,90,15, StandardCharsets.UTF_8));
            setEnvioGPS_fechayhoralectura(new String(bufferRegistroCliente,106,8, StandardCharsets.UTF_8));
            setEnvioGPS_HoraImpresion(new String(bufferRegistroCliente,115,8, StandardCharsets.UTF_8));
            setEnvioGPS_lecturatomada(new String(bufferRegistroCliente,124,9, StandardCharsets.UTF_8));
            setEnvioGPS_causadenolectura(new String(bufferRegistroCliente,134,2, StandardCharsets.UTF_8));
            setEnvioGPS_CodLector(new String(bufferRegistroCliente,137,10, StandardCharsets.UTF_8));
            setEnvioGPS_Terminal(new String(bufferRegistroCliente,148,15, StandardCharsets.UTF_8));
            setEnvioGPS_PERIODOLECTURA(new String(bufferRegistroCliente,164,20, StandardCharsets.UTF_8));
            setEnvioGPS_Longitud(new String(bufferRegistroCliente,185,20, StandardCharsets.UTF_8));
            setEnvioGPS_Latitud(new String(bufferRegistroCliente,206,20, StandardCharsets.UTF_8));
            setEnvioGPS_NroSatelites(new String(bufferRegistroCliente,227,3, StandardCharsets.UTF_8));
            setEnvioGPS_informe(new String(bufferRegistroCliente,231,250, StandardCharsets.UTF_8));
            setEnvioGPS_criticapda(new String(bufferRegistroCliente,292+190,1, StandardCharsets.UTF_8));
            setEnvioGPS_EstadoEnvio(new String(bufferRegistroCliente,294+190,1, StandardCharsets.UTF_8));
            setEnvioGPS_comentario(new String(bufferRegistroCliente,296+190,2, StandardCharsets.UTF_8));
            setEnvioGPS_Intentos(new String(bufferRegistroCliente,299+190,1, StandardCharsets.UTF_8));
            setEnvioGPS_FechaHoraSatelite(new String(bufferRegistroCliente,301+190,20, StandardCharsets.UTF_8));
            setEnvioGPS_AltitudSatelite(new String(bufferRegistroCliente,322+190,10, StandardCharsets.UTF_8));
            setEnvioGPS_Distancia(new String(bufferRegistroCliente,333+190,10, StandardCharsets.UTF_8));
            setEnvioGPS_LecturaModificada1(new String(bufferRegistroCliente,344+190,11, StandardCharsets.UTF_8));
            setEnvioGPS_LecturaModificada2(new String(bufferRegistroCliente,356+190,11, StandardCharsets.UTF_8));
            setEnvioGPS_Tiempo(new String(bufferRegistroCliente,368+190,5, StandardCharsets.UTF_8));
            setEnvioGPS_ConsumoFacturado(new String(bufferRegistroCliente,374+190,10, StandardCharsets.UTF_8));
            setEnvioGPS_LecturaAnterior(new String(bufferRegistroCliente,385+190,10, StandardCharsets.UTF_8));
            setEnvioGPS_estrato(new String(bufferRegistroCliente,396+190,2, StandardCharsets.UTF_8));
            setEnvioGPS_uso(new String(bufferRegistroCliente,399+190,2, StandardCharsets.UTF_8));
            setEnvioGPS_NombreArchivo(new String(bufferRegistroCliente,402+190,12, StandardCharsets.UTF_8));
            setId(new String(bufferRegistroCliente,415+190,11, StandardCharsets.UTF_8));
            setTotalcausasnolectura(new String(bufferRegistroCliente,427+190,6, StandardCharsets.UTF_8));
            setTotalLecturas(new String(bufferRegistroCliente,434+190,6, StandardCharsets.UTF_8));
            setTotalConsumoBajo(new String(bufferRegistroCliente,441+190,6, StandardCharsets.UTF_8));
            setTotalConsumoAlto(new String(bufferRegistroCliente,448+190,6, StandardCharsets.UTF_8));
            setTotalConsumoNormal(new String(bufferRegistroCliente,455+190,6, StandardCharsets.UTF_8));
            setTotalLecurasIguales(new String(bufferRegistroCliente,462+190,6, StandardCharsets.UTF_8));
            setTotalLeidas(new String(bufferRegistroCliente,469+190,6, StandardCharsets.UTF_8));
            setNumObser(new String(bufferRegistroCliente,476+190,6, StandardCharsets.UTF_8));
            setNumInformes(new String(bufferRegistroCliente,483+190,6, StandardCharsets.UTF_8));
            setTotalTiempoProm(new String(bufferRegistroCliente,490+190,12, StandardCharsets.UTF_8));
            setTotalDistanciaProm(new String(bufferRegistroCliente,503+190,12, StandardCharsets.UTF_8));

            setEnvioGPS_CODIGO_SAC(new String(bufferRegistroCliente,516+190,3, StandardCharsets.UTF_8));
            setEnvioGPS_CRNL(new String(bufferRegistroCliente,520+190,2, StandardCharsets.UTF_8));

            //Log.e("error"," de lectura----"+texto.substring(223, 283));


            ultimo_EnvioGPS = registro;
            //fin estructura
        } catch (Exception e) {
            Log.e("error", "error de lectura----" + e.getMessage());
            e.printStackTrace();
        }
    }

    //Escribir los registros actual en el archivo en posicion
    /*public void lectura_EnvioGPS_Tipo2(int registro) //Ax: es para no escribir toodos los registros que no se necesitan en el modulo consultanoenviados
    {
        encontro_EnvioGPS = 0;
        posicion_EnvioGPS(registro, LONGITUD_REGISTRO);
        try {
            byteArray = new byte[LONGITUD_REGISTRO];
            rFile.read(byteArray);
            texto = new String(byteArray);
            setEnvioGPS_cuenta(texto.substring(27, 37));
            setEnvioGPS_NroContador(texto.substring(38, 58));//Se aumento de 16  20
            setEnvioGPS_EstadoEnvio(texto.substring(294, 295));
            setEnvioGPS_comentario(texto.substring(296, 298));
            ultimo_EnvioGPS = registro;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    //por el momento no es requerido la critica es la misma
    public void BuscarSecuencial_EnvioGPS(String codigo, String contador, String idcontador) {
        encontro_EnvioGPS = 0;
        //buffer_EnvioGPS.Initialize();
        for (int i = 0; i < (int) (total_EnvioGPS); i++) {
            lectura_EnvioGPS(i + 1);

            String idcont = contador.trim().replaceFirst("^0*", "");//rx
            String idcont2 = EnvioGPS_comentario.trim().replaceFirst("^0*", "");//rx
            String NroContador = EnvioGPS_NroContador.trim();//rx
            String tipoMedida = EnvioGPS_comentario.trim().replaceFirst("^0*", "");//rx


            if (codigo.trim().equals(EnvioGPS_cuenta.trim()) && contador.equals(NroContador) && tipoMedida.trim().equals(idcontador.trim())) {//rx
                encontro_EnvioGPS = i + 1;
                posicion_EnvioGPS(i + 1, LONGITUD_REGISTRO);
                i = (total_EnvioGPS) + 10;
            } else {
                encontro_EnvioGPS = 0;
            }
        }
        return;
    }

}//end