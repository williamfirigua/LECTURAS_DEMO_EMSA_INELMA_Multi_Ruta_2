package com.gstolima.accesoyseguridad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import com.Util.Utils;
import com.gstolima.modulobluetooth.btPrintFile;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

public class VariablesGlobales {
    // iniciacion de variables globales

    String globalturno;
    String globaloperario;
    String globalanomalia;
    String nombregeneral;
    String nombrepredio;
    String CodigoEmpresa="";
    int globalregistroactual;

    String globalcentrocosto;
    int impresionenlote;

    int contadoractual;
    int clienteactual;
    int haciaadelante = 1;
    int haciaatras = 0;
    int direcciondelectura = 1;
    int aplicafecha = 0;
    String mesactual = "";
    String terminal = "";
    String impresora = "";
    String estado = "";
    int modosupervisor = 0;
    int tipoimpresora = 0;
    double ultimotiempo = 1;
    String mesenformato;
    int ultimoregistro = 0;
    String medidoranterior = "";
    String nuevafecha;
    String formatofinanciero = "";
    int tienedeuda = 0;
    int barrido = 0;
    int nveces = 0;
    double consumoafacturar = 0;
    double valorconsumo = 0;
    double valorconsumo2 = 0;
    String consumoenergiaactual = "";
    String valorfacturadoenergia = "";
    int diashoy = 0;
    double consumoactual = 0;
    public  static  double lactual;
    double cactual;
    double lect2 = -1;
    double lect3 = -1;
    String amd = "";
    String hm = "";
    String fechahoy = "";
    public static String ultimaNovedad = "0000";
    public static int habilitadaimpresora = 0;
    static int numdigitos;

    public static int rangoinicialimpresion;
    public static int rangofinalimpresion;
    public static int opcionmenuseleccion;
    public static int activarcamarafotografica;
    public static int cierreforzadoruta;
    public static String datodebusqueda;
    public static String tipoDeRuta;
    public static int tipodebusqueda;
    public static int intcontroltexto = 1;//Ax guarda el tamaño max de textview de lectura

    // creacion de las variables totales para el resume del proyecto
    int cuentamalajustada = 0;
    double pesosenergia = 0;

    // Inicio variables globales que permiten hacer los contes del sistema
    // actual
    public static int registroactual;
    public static int totallecturas = 0;
    public static int totalcausasnolectura = 0;
    public static int totalnuevos = 0;
    public static int totalinformes = 0;
    //
    public static int totalprediosleidos = 0;
    public static int totalpredioscomentarios = 0;
    public static int totalprediosliquidados = 0;
    public static int totalprediosimpresos = 0;
    public static int totalprediosnofacturados = 0;
    public static int totalimpresiones = 0;
    public static int totalregistrosleidos = 0;
    public static int totalclientesreales = 0; //se crea con el fin de no dejar crear registros nuevos en el sistema movil
    public static String nombreLectorPDA = "";
    // Servicio de Impresion bluetooth
    public static btPrintFile btPrintService = null;
    public static boolean bDiscoveryStarted = false;
    public static BluetoothAdapter mBluetoothAdapter = null;

    public static String remoteDevice;

    // Adicionada en la version para android: direccion mac de la impresora
    // detectada
    public static String printerMacAddress = "";

    public static final int REQUEST_CONNECT_DEVICE = 3;
    public static final int REQUEST_ENABLE_BT = 4;

    // Name of the connected device
    static String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    static ArrayAdapter<String> mConversationArrayAdapter;

    // / FIN VARIABLES STATICAS

    public static String administrador = "912";
    public static String consumoauditoria = "50";
    public static String distanciagps = "200";
    public static String nrodias = "30";
    public static String fechainicial = "20121201";
    public static String fechafinal = "20150130";
    public static String obligafotos = "0";
    public static String obligabarras = "0";
    //nuevo victor para obligar firma
    public static String obligaFirma = "0";


    public static String maximoregaenviar = "20";
    public static String maximovalorentrega = "10000000";
    public static String minimovalorentrega = "10000000";
    double totalconsumoperiodo = 0;

    // Variables configuracion WebService

    public static final String WS_NAMESPACE = "http://tempuri.org/";
    public static final String WS_URL = "http://162.214.76.70:91/Celsia.asmx";
    public static final String WS_VALIDAR_CONEXION = "http://tempuri.org/VALIDAR_CONEXION";
    public static final String WS_METHOD_NAME = "VALIDAR_CONEXION";
    public static final String WS_REGISTRAR_PROGRAMACION_ENRUTADOR = "REGISTRAR_PROGRAMACION_ENRUTADOR";
    public static final String WS_OBTENER_LONGITUD_ARCHIVO = "OBTENER_LONGITUD_ARCHIVO";
    public static final String WS_ENVIAR_DEL_SERVIDOR_AL_PDA = "ENVIAR_DEL_SERVIDOR_AL_PDA";
    public static final String WS_BORRAR_ARCHIVO_DEL_SERVIDOR = "BorrarArchivoDelServidor";

    // fin variables principales para conteos
    // Fin Variables totalizadoras
    // <summary>
    double pesosenergiareactiva = 0;
    double facturapreimpresa = 1;
    int diasdecobro;
    public static String urlGlobal = "";
    public static int imprimirSoloPostal = 0;
    public static Date fechahoraGPS = new Date();

    //nueva
    public static String moduloTrabajo = "";
    public static int totalSoloLecturas = 0;
    public static int totalConsumoNormal = 0;
    public static int totalConsumoAlto = 0;
    public static int totalConsumoBajo = 0;
    public static int totalLecturasIguales = 0;
    public static int totalLecturasNegativas = 0;
    public static int numObservaciones = 0;
    public static int numInformes = 0;
    public static int totalTiempoPromedio = 0;
    public static int totalDistanciaPromedio = 0;
    public static boolean rutaDuplicada = false;


    // Todo: Capturar la hora actual del sistema

    public static String activar_validar_horario;

    public static String getActivar_validar_horario() {
        return activar_validar_horario;
    }

    public static void setActivar_validar_horario(String activar_validar_horario) {
        VariablesGlobales.activar_validar_horario = activar_validar_horario;
    }

    // / <summary>
    public String getGlobalturno() {
        return globalturno;
    }

    public void setGlobalturno(String globalturno) {
        this.globalturno = globalturno;
    }

    public String getGlobaloperario() {
        return globaloperario;
    }

    public void setGlobaloperario(String globaloperario) {
        this.globaloperario = globaloperario;
    }

    public String getGlobalanomalia() {
        return globalanomalia;
    }

    public void setGlobalanomalia(String globalanomalia) {
        this.globalanomalia = globalanomalia;
    }

    public String getNombregeneral() {
        return nombregeneral;
    }

    public void setNombregeneral(String nombregeneral) {
        this.nombregeneral = nombregeneral;
    }

    public String getNombrepredio() {
        return nombrepredio;
    }

    public void setNombrepredio(String nombrepredio) {
        this.nombrepredio = nombrepredio;
    }

    public String getCodigoEmpresa() {
        return CodigoEmpresa;
    }

    public void setCodigoEmpresa(String CodigoEmpresa) {
        this.CodigoEmpresa = CodigoEmpresa;
    }


    public int getGlobalregistroactual() {
        return globalregistroactual;
    }

    public void setGlobalregistroactual(int globalregistroactual) {
        this.globalregistroactual = globalregistroactual;
    }

    public String getGlobalcentrocosto() {
        return globalcentrocosto;
    }

    public void setGlobalcentrocosto(String globalcentrocosto) {
        this.globalcentrocosto = globalcentrocosto;
    }

    public int getImpresionenlote() {
        return impresionenlote;
    }

    public void setImpresionenlote(int impresionenlote) {
        this.impresionenlote = impresionenlote;
    }

    public int getContadoractual() {
        return contadoractual;
    }

    public void setContadoractual(int contadoractual) {
        this.contadoractual = contadoractual;
    }

    public int getClienteactual() {
        return clienteactual;
    }

    public void setClienteactual(int clienteactual) {
        this.clienteactual = clienteactual;
    }

    public int getHaciaadelante() {
        return haciaadelante;
    }

    public void setHaciaadelante(int haciaadelante) {
        this.haciaadelante = haciaadelante;
    }

    public int getHaciaatras() {
        return haciaatras;
    }

    public void setHaciaatras(int haciaatras) {
        this.haciaatras = haciaatras;
    }

    public int getDirecciondelectura() {
        return direcciondelectura;
    }

    public void setDirecciondelectura(int direcciondelectura) {
        this.direcciondelectura = direcciondelectura;
    }

    public int getAplicafecha() {
        return aplicafecha;
    }

    public void setAplicafecha(int aplicafecha) {
        this.aplicafecha = aplicafecha;
    }

    public String getMesactual() {
        return mesactual;
    }

    public void setMesactual(String mesactual) {
        this.mesactual = mesactual;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getModosupervisor() {
        return modosupervisor;
    }

    public void setModosupervisor(int modosupervisor) {
        this.modosupervisor = modosupervisor;
    }

    public int getTipoimpresora() {
        return tipoimpresora;
    }

    public void setTipoimpresora(int tipoimpresora) {
        this.tipoimpresora = tipoimpresora;
    }

    public double getUltimotiempo() {
        return ultimotiempo;
    }

    public void setUltimotiempo(double ultimotiempo) {
        this.ultimotiempo = ultimotiempo;
    }

    public String getMesenformato() {
        return mesenformato;
    }

    public void setMesenformato(String mesenformato) {
        this.mesenformato = mesenformato;
    }

    public int getUltimoregistro() {
        return ultimoregistro;
    }

    public void setUltimoregistro(int ultimoregistro) {
        this.ultimoregistro = ultimoregistro;
    }

    public String getMedidoranterior() {
        return medidoranterior;
    }

    public void setMedidoranterior(String medidoranterior) {
        this.medidoranterior = medidoranterior;
    }

    public String getNuevafecha() {
        return nuevafecha;
    }

    public void setNuevafecha(String nuevafecha) {
        this.nuevafecha = nuevafecha;
    }

    public String getFormatofinanciero() {
        return formatofinanciero;
    }

    public void setFormatofinanciero(String formatofinanciero) {
        this.formatofinanciero = formatofinanciero;
    }

    public int getTienedeuda() {
        return tienedeuda;
    }

    public void setTienedeuda(int tienedeuda) {
        this.tienedeuda = tienedeuda;
    }

    public int getBarrido() {
        return barrido;
    }

    public void setBarrido(int barrido) {
        this.barrido = barrido;
    }

    public int getNveces() {
        return nveces;
    }

    public void setNveces(int nveces) {
        this.nveces = nveces;
    }

    public double getConsumoafacturar() {
        return consumoafacturar;
    }

    public void setConsumoafacturar(double consumoafacturar) {
        this.consumoafacturar = consumoafacturar;
    }

    public String getConsumoenergiaactual() {
        return consumoenergiaactual;
    }

    public void setConsumoenergiaactual(String consumoenergiaactual) {
        this.consumoenergiaactual = consumoenergiaactual;
    }

    public int getDiashoy() {
        return diashoy;
    }

    public void setDiashoy(int diashoy) {
        this.diashoy = diashoy;
    }

    public double getConsumoactual() {
        return consumoactual;
    }

    public void setConsumoactual(double consumoactual) {
        this.consumoactual = consumoactual;
    }

    public double getLactual() {
        return lactual;
    }

    public void setLactual(double lactual) {
        this.lactual = lactual;
    }

    public double getCactual() {
        return cactual;
    }

    public void setCactual(double cactual) {
        this.cactual = cactual;
    }

    public double getLect2() {
        return lect2;
    }

    public void setLect2(double lect2) {
        this.lect2 = lect2;
    }

    public double getLect3() {
        return lect3;
    }

    public void setLect3(double lect3) {
        this.lect3 = lect3;
    }

    public String getAmd() {
        return amd;
    }

    public void setAmd(String amd) {
        this.amd = amd;
    }

    public String getHm() {
        return hm;
    }

    public void setHm(String hm) {
        this.hm = hm;
    }

    public String getFechahoy() {
        return fechahoy;
    }

    public void setFechahoy(String fechahoy) {
        this.fechahoy = fechahoy;
    }

    public static String getUltimaNovedad() {
        return ultimaNovedad;
    }

    public static void setUltimaNovedad(String ultimaNovedad) {
        VariablesGlobales.ultimaNovedad = ultimaNovedad;
    }

    public static int getHabilitadaimpresora() {
        return habilitadaimpresora;
    }

    public static void setHabilitadaimpresora(int habilitadaimpresora) {
        VariablesGlobales.habilitadaimpresora = habilitadaimpresora;
    }

    public static int getNumdigitos() {
        return numdigitos;
    }

    public void setNumdigitos(int numdigitos) {
        this.numdigitos = numdigitos;
    }

    public static int getRangoinicialimpresion() {
        return rangoinicialimpresion;
    }

    public static void setRangoinicialimpresion(int rangoinicialimpresion) {
        VariablesGlobales.rangoinicialimpresion = rangoinicialimpresion;
    }

    public static int getRangofinalimpresion() {
        return rangofinalimpresion;
    }

    public static void setRangofinalimpresion(int rangofinalimpresion) {
        VariablesGlobales.rangofinalimpresion = rangofinalimpresion;
    }

    public static int getOpcionmenuseleccion() {
        return opcionmenuseleccion;
    }

    public static void setOpcionmenuseleccion(int opcionmenuseleccion) {
        VariablesGlobales.opcionmenuseleccion = opcionmenuseleccion;
    }

    public static int getActivarcamarafotografica() {
        return activarcamarafotografica;
    }

    public static void setActivarcamarafotografica(int activarcamarafotografica) {
        VariablesGlobales.activarcamarafotografica = activarcamarafotografica;
    }

    public static int getCierreforzadoruta() {
        return cierreforzadoruta;
    }

    public static void setCierreforzadoruta(int cierreforzadoruta) {
        VariablesGlobales.cierreforzadoruta = cierreforzadoruta;
    }

    public static String getDatodebusqueda() {
        return datodebusqueda;
    }

    public static void setDatodebusqueda(String datodebusqueda) {
        VariablesGlobales.datodebusqueda = datodebusqueda;
    }

    public static String getTipoDeRuta() {
        return tipoDeRuta;
    }

    public static void setTipoDeRuta(String tipoDeRuta) {
        VariablesGlobales.tipoDeRuta = tipoDeRuta;
    }

    public static int getTipodebusqueda() {
        return tipodebusqueda;
    }

    public static void setTipodebusqueda(int tipodebusqueda) {
        VariablesGlobales.tipodebusqueda = tipodebusqueda;
    }

    public int getCuentamalajustada() {
        return cuentamalajustada;
    }

    public void setCuentamalajustada(int cuentamalajustada) {
        this.cuentamalajustada = cuentamalajustada;
    }

    public double getPesosenergia() {
        return pesosenergia;
    }

    public void setPesosenergia(double pesosenergia) {
        this.pesosenergia = pesosenergia;
    }

    public static int getRegistroactual() {
        return registroactual;
    }

    public static void setRegistroactual(int registroactual) {
        VariablesGlobales.registroactual = registroactual;
    }

    public static int getTotallecturas() {
        return totallecturas;
    }

    public static void setTotallecturas(int totallecturas) {
        VariablesGlobales.totallecturas = totallecturas;
    }

    public static int getTotalcausasnolectura() {
        return totalcausasnolectura;
    }

    public static void setTotalcausasnolectura(int totalcausasnolectura) {
        VariablesGlobales.totalcausasnolectura = totalcausasnolectura;
    }

    public static int getTotalnuevos() {
        return totalnuevos;
    }

    public static void setTotalnuevos(int totalnuevos) {
        VariablesGlobales.totalnuevos = totalnuevos;
    }

    public static int getTotalinformes() {
        return totalinformes;
    }

    public static void setTotalinformes(int totalinformes) {
        VariablesGlobales.totalinformes = totalinformes;
    }

    public static int getTotalprediosleidos() {
        return totalprediosleidos;
    }

    public static void setTotalprediosleidos(int totalprediosleidos) {
        VariablesGlobales.totalprediosleidos = totalprediosleidos;
    }


    public static int getTotalClientesReales() {
        return totalclientesreales;
    }

    public static void setTotalClientesReales(int totalclientesreales) {
        VariablesGlobales.totalclientesreales = totalclientesreales;
    }

    public static int getTotalpredioscomentarios() {
        return totalpredioscomentarios;
    }

    public static void setTotalpredioscomentarios(int totalpredioscomentarios) {
        VariablesGlobales.totalpredioscomentarios = totalpredioscomentarios;
    }

    public static int getTotalprediosliquidados() {
        return totalprediosliquidados;
    }

    public static void setTotalprediosliquidados(int totalprediosliquidados) {
        VariablesGlobales.totalprediosliquidados = totalprediosliquidados;
    }

    public static int getTotalprediosimpresos() {
        return totalprediosimpresos;
    }

    public static void setTotalprediosimpresos(int totalprediosimpresos) {
        VariablesGlobales.totalprediosimpresos = totalprediosimpresos;
    }

    public static int getTotalprediosnofacturados() {
        return totalprediosnofacturados;
    }

    public static void setTotalprediosnofacturados(int totalprediosnofacturados) {
        VariablesGlobales.totalprediosnofacturados = totalprediosnofacturados;
    }

    public static int getTotalimpresiones() {
        return totalimpresiones;
    }

    public static void setTotalimpresiones(int totalimpresiones) {
        VariablesGlobales.totalimpresiones = totalimpresiones;
    }

    public static int getTotalregistrosleidos() {
        return totalregistrosleidos;
    }

    public static void setTotalregistrosleidos(int totalregistrosleidos) {
        VariablesGlobales.totalregistrosleidos = totalregistrosleidos;
    }

    public static String getNombreLectorPDA() {
        return nombreLectorPDA;
    }

    public static void setNombreLectorPDA(String nombreLectorPDA) {
        VariablesGlobales.nombreLectorPDA = nombreLectorPDA;
    }

    public static String getAdministrador() {
        return administrador;
    }

    public static void setAdministrador(String administrador) {
        VariablesGlobales.administrador = administrador;
    }

    public static String getConsumoauditoria() {
        return consumoauditoria;
    }

    public static void setConsumoauditoria(String consumoauditoria) {
        VariablesGlobales.consumoauditoria = consumoauditoria;
    }

    public static String getDistanciagps() {
        return distanciagps;
    }

    public static void setDistanciagps(String distanciagps) {
        VariablesGlobales.distanciagps = distanciagps;
    }

    public static String getNrodias() {
        return nrodias;
    }

    public static void setNrodias(String nrodias) {
        VariablesGlobales.nrodias = nrodias;
    }

    public static String getFechainicial() {
        return fechainicial;
    }

    public static void setFechainicial(String fechainicial) {
        VariablesGlobales.fechainicial = fechainicial;
    }

    public static String getFechafinal() {
        return fechafinal;
    }

    public static void setFechafinal(String fechafinal) {
        VariablesGlobales.fechafinal = fechafinal;
    }

//    un cogigo de aprobacion para saltar de un cliente a otro y un codigo para desabilitar la obligatoriedad a la impresora
    public static int ObligaImpresoraSuper=0;
    public static int getObligaImpresoraSuper() {
        return ObligaImpresoraSuper;
    }

    public static void setObligaImpresoraSuper(int ObligaImpresoraSuper) {
        VariablesGlobales.ObligaImpresoraSuper = ObligaImpresoraSuper;
    }

    public static int  ObligaAvansaRetrocede=0;

    public static int getObligaAvansaRetrocede() {
        return ObligaImpresoraSuper;
    }

    public static void setObligaAvansaRetrocede(int ObligaAvansaRetrocede) {
        VariablesGlobales.ObligaAvansaRetrocede = ObligaAvansaRetrocede;
    }

    public static String getObligafotos() {
        return obligafotos;
    }

    public static void setObligafotos(String obligafotos) {
        VariablesGlobales.obligafotos = obligafotos;
    }

    public static String getObligabarras() {
        return obligabarras;
    }

    public static void setObligabarras(String obligabarras) {
        VariablesGlobales.obligabarras = obligabarras;
    }

    //nuevo campo para obligar la firma en ves de enviarla a todos
    public static String getObligaFirma() {
        return obligaFirma;
    }

    public static void setObligaFirma(String obligaFirma) {
        VariablesGlobales.obligaFirma = obligaFirma;
    }



    public static String getMaximoregaenviar() {
        return maximoregaenviar;
    }

    public static void setMaximoregaenviar(String maximoregaenviar) {
        VariablesGlobales.maximoregaenviar = maximoregaenviar;
    }

    public static String getMaximovalorentrega() {
        return maximovalorentrega;
    }

    public static void setMaximovalorentrega(String maximovalorentrega) {
        VariablesGlobales.maximovalorentrega = maximovalorentrega;
    }

    public static String getMinimovalorentrega() {
        return minimovalorentrega;
    }

    public static void setMinimovalorentrega(String minimovalorentrega) {
        VariablesGlobales.minimovalorentrega = minimovalorentrega;
    }

    public double getTotalconsumoperiodo() {
        return totalconsumoperiodo;
    }

    public void setTotalconsumoperiodo(double totalconsumoperiodo) {
        this.totalconsumoperiodo = totalconsumoperiodo;
    }

    public double getPesosenergiareactiva() {
        return pesosenergiareactiva;
    }

    public void setPesosenergiareactiva(double pesosenergiareactiva) {
        this.pesosenergiareactiva = pesosenergiareactiva;
    }

    public double getFacturapreimpresa() {
        return facturapreimpresa;
    }

    public void setFacturapreimpresa(double facturapreimpresa) {
        this.facturapreimpresa = facturapreimpresa;
    }

    public int getDiasdecobro() {
        return diasdecobro;
    }

    public void setDiasdecobro(int diasdecobro) {
        this.diasdecobro = diasdecobro;
    }

    public static String getUrlGlobal() {
        return urlGlobal;
    }

    public static void setUrlGlobal(String urlGlobal) {
        VariablesGlobales.urlGlobal = urlGlobal;
    }

    public static int getImprimirSoloPostal() {
        return imprimirSoloPostal;
    }

    public static void setImprimirSoloPostal(int imprimirSoloPostal) {
        VariablesGlobales.imprimirSoloPostal = imprimirSoloPostal;
    }

    public static Date getFechahoraGPS() {
        return fechahoraGPS;
    }

    public static void setFechahoraGPS(Date fechahoraGPS) {
        VariablesGlobales.fechahoraGPS = fechahoraGPS;
    }

    public int getEspc() {
        return espc;
    }

    public void setEspc(int espc) {
        this.espc = espc;
    }

    public static String getDirectorioactual() {
        return directorioactual;
    }
    public static String getCarpetaLecturas() {
        return CarpetaLecturas;
    }

    public int getActivatransmision() {
        return activatransmision;
    }

    public void setActivatransmision(int activatransmision) {
        this.activatransmision = activatransmision;
    }

    public static int getTotalSoloLecturas() {
        return totalSoloLecturas;
    }

    public static void setTotalSoloLecturas(int totalSoloLecturas) {
        VariablesGlobales.totalSoloLecturas = totalSoloLecturas;
    }

    public double getValorconsumo() {
        return valorconsumo;
    }

    public void setValorconsumo(double valorconsumo) {
        this.valorconsumo = valorconsumo;
    }

    public static int getTotalConsumoNormal() {
        return totalConsumoNormal;
    }

    public static void setTotalConsumoNormal(int totalConsumoNormal) {
        VariablesGlobales.totalConsumoNormal = totalConsumoNormal;
    }

    public static int getTotalConsumoAlto() {
        return totalConsumoAlto;
    }

    public static void setTotalConsumoAlto(int totalConsumoAlto) {
        VariablesGlobales.totalConsumoAlto = totalConsumoAlto;
    }

    public static int getTotalConsumoBajo() {
        return totalConsumoBajo;
    }

    public static void setTotalConsumoBajo(int totalConsumoBajo) {
        VariablesGlobales.totalConsumoBajo = totalConsumoBajo;
    }

    public double getValorconsumo2() {
        return valorconsumo2;
    }

    public void setValorconsumo2(double valorconsumo2) {
        this.valorconsumo2 = valorconsumo2;
    }

    public static int getNumObservaciones() {
        return numObservaciones;
    }

    public static void setNumObservaciones(int numObservaciones) {
        VariablesGlobales.numObservaciones = numObservaciones;
    }

    public static int getNumInformes() {
        return numInformes;
    }

    public static void setNumInformes(int numInformes) {
        VariablesGlobales.numInformes = numInformes;
    }

    public static int getTotalTiempoPromedio() {
        return totalTiempoPromedio;
    }

    public static void setTotalTiempoPromedio(int totalTiempoPromedio) {
        VariablesGlobales.totalTiempoPromedio = totalTiempoPromedio;
    }

    public static int getTotalDistanciaPromedio() {
        return totalDistanciaPromedio;
    }

    public static void setTotalDistanciaPromedio(int totalDistanciaPromedio) {
        VariablesGlobales.totalDistanciaPromedio = totalDistanciaPromedio;
    }

    public int getLimiteimpresion() {
        return limiteimpresion;
    }

    public void setLimiteimpresion(int limiteimpresion) {
        this.limiteimpresion = limiteimpresion;
    }

    public static boolean isRutaDuplicada() {
        return rutaDuplicada;
    }

    public static void setRutaDuplicada(boolean rutaDuplicada) {
        VariablesGlobales.rutaDuplicada = rutaDuplicada;
    }

    // / validacion de fechas
    public String convertirDiasAFecha(long dias, String fecha, String fechahoy) {

        // Se modifica este metodo con utilidades de Java, devuelve fechahoy
        // sumandole la cantidad que llega en dias
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (int) dias);
        fecha = "" + calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.DAY_OF_MONTH);
        return fecha;
    }

    public long calcularNumerodeDias(String fechafuente) {
        String anno, mees, diia;
        int nroannos, nromeeses, nrodiias, correccionmes;
        long nrodias;

        anno = fechafuente.substring(0, 4);
        mees = fechafuente.substring(4, 6);
        diia = fechafuente.substring(6);
        nroannos = Integer.parseInt(anno);
        nromeeses = Integer.parseInt(mees);
        nrodiias = Integer.parseInt(diia);

        nrodias = (long) (nroannos - 2001) * 365 + ((nromeeses - 1) * 30) + nrodiias;

        if (nroannos >= 2000) {
            if (nroannos == 2000) {
                if (nromeeses > 2)
                    ++nrodias;
            } else
                ++nrodias;
        }

        if (nroannos >= 2004) {
            if (nroannos == 2004) {
                if (nromeeses > 2)
                    ++nrodias;
            } else
                ++nrodias;
        }

        if (nroannos >= 2008) {
            if (nroannos == 2008) {
                if (nromeeses > 2)
                    ++nrodias;
            } else
                ++nrodias;
        }

        switch (nromeeses) {
            default:
            case 1:
                correccionmes = 0;
                break;
            case 2:
                correccionmes = 1;
                break;
            case 3:
                correccionmes = -2;
                break;
            case 4:
                correccionmes = 0;
                break;
            case 5:
                correccionmes = 0;
                break;
            case 6:
                correccionmes = 1;
                break;
            case 7:
                correccionmes = 1;
                break;
            case 8:
                correccionmes = 2;
                break;
            case 9:
                correccionmes = 3;
                break;
            case 10:
                correccionmes = 3;
                break;
            case 11:
                correccionmes = 4;
                break;
            case 12:
                correccionmes = 4;
                break;
        }
        nrodias += correccionmes;

        return (nrodias);
    }

    public int evaluarConServicio(String variable) {
        if ((variable.equals("Z"))) {
            return (0);
        }
        return (1);
    }

    public double ejecutarAjusteUnidades(double pesos) {
        long auxiliar = (long) pesos;
        double partedecimal;
        partedecimal = pesos - (double) auxiliar;
        if (partedecimal < (double) 0.0) {
            if (partedecimal <= (double) -0.50)
                pesos = (double) auxiliar - 1;
            else
                pesos = (double) auxiliar;
        } else {
            if (partedecimal >= (double) 0.50)
                pesos = (double) auxiliar + 1;
            else
                pesos = (double) auxiliar;
        }
        return (pesos);
    }

    public String convertirMesLetras(String mes) {
        String MesActual = "NNN";

        try {
            switch (Integer.parseInt(String.format("%02d", Integer.parseInt(mes.trim()))))// (Integer.parseInt(Mes.trim().PadLeft(2,'0')))
            {
                case 1:
                    MesActual = "ENE";
                    break;
                case 2:
                    MesActual = "FEB";
                    break;
                case 3:
                    MesActual = "MAR";
                    break;
                case 4:
                    MesActual = "ABR";
                    break;
                case 5:
                    MesActual = "MAY";
                    break;
                case 6:
                    MesActual = "JUN";
                    break;
                case 7:
                    MesActual = "JUL";
                    break;
                case 8:
                    MesActual = "AGO";
                    break;
                case 9:
                    MesActual = "SEP";
                    break;
                case 10:
                    MesActual = "OCT";
                    break;
                case 11:
                    MesActual = "NOV";
                    break;
                case 12:
                    MesActual = "DIC";
                    break;
            }
        } catch (Exception e) {
            Calendar c = Calendar.getInstance();
            return (convertirMesLetras(c.get(Calendar.MONTH) + 1 + ""));
            //throw new RuntimeException("[VariablesGlobales] convertirMesLetras(): "+ e.getMessage()); //Ax: excepcion personalizada todo: se debe replicar en todo lado
        }
        return (MesActual);
    }

    public String encapsular(String cadenaOrigen, String cadenaEncapsulada) {
        int i = 0;
        int tamTrama = 0;
        String respuesta = "";
        String alfaNumerico = "*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "\n" + "\r";
        // cadenaencapsulada "DCRFVTGBYH"
        String nuevaCadena = "QAZWSX" + cadenaEncapsulada + "NUJM IKOLP*>" + "+=,<-;.:/908?172634|[@" + "\n" + "\r";

        for (i = 0; i < cadenaOrigen.length(); i++) {
            for (tamTrama = 0; tamTrama < alfaNumerico.length(); tamTrama++) {
                if (cadenaOrigen.substring(i, i + 1).equals(alfaNumerico.substring(tamTrama, tamTrama + 1))) {
                    respuesta += nuevaCadena.substring(tamTrama, tamTrama + 1);
                }
            }
        }
        return (respuesta);
    }

    public String desencapsular(String cadenaOrigen, String cadenaEncapsulada) {
        int i = 0;
        int tamTrama = 0;
        String respuesta = "";
        String alfaNuerico = "*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "\n" + "\r";

        String nuevaCadena = "QAZWSX" + cadenaEncapsulada + "NUJM IKOLP*>" + "+=,<-;.:/908?172634[|@" + "\n" + "\r";

        for (i = 0; i < cadenaOrigen.length(); i++) {
            for (tamTrama = 0; tamTrama < nuevaCadena.length(); tamTrama++) {
                if (cadenaOrigen.substring(i, i + 1).equals(nuevaCadena.substring(tamTrama, tamTrama + 1))) {
                    respuesta += alfaNuerico.substring(tamTrama, tamTrama + 1);
                }
            }
        }
        return (respuesta);
    }

    public Boolean validaTecla2(int tipo, KeyEvent e) {
        if (tipo == 1) {
            if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57 || e.getKeyCode() == 8)
                return (false);
            else {
                return (true);
            }
        } else
            return (false);
    }

    String versionpc;
    int espc = 0;
    public static String directorioactual = "";
    public static String CarpetaLecturas = "/LECTURAMEDIDORES";

    public static String vrsDerechos = "";
    public static String versionApp = "";
    public static String archivolog = "";
    public static String directorioBackUp = ""; //Ax: aqui guardara los Backups, si no existe sera igua al: directorioactual
    int activatransmision = 0;
    int limiteimpresion = 1;

    public String getVersionpc() {
        return versionpc;
    }

    public void setVersionpc(String versionpc) {
        this.versionpc = versionpc;
    }

    // serie de variables de impresion
    // lineas de impresion
    public int L1 = 0;
    public int L2 = 0;
    public int L3 = 0;
    public int L4 = 0;
    public int L5 = 0;
    public int L6 = 0;
    public int L7 = 0;
    public int L8 = 0;
    public int L9 = 30;
    public int B1 = 155;
    public int L11 = 260;
    public int L12 = 305;
    public int L13 = 327;
    public int L14 = 377;
    public int L15 = 402;
    public int L16 = 430;
    public int L17 = 456;
    public int L18 = 514;
    public int L19 = 536;
    public int L20 = 561;
    public int L21 = 677;
    // nuevas lineas para programar
    public int L22 = 735;
    public int L23 = 757;
    public int L24 = 777;
    public int L25 = 797;

    // fn nuevas lineas
    // public int L26no= 758;//q paso con esta
    public int L26 = 765;
    public int L27 = 790;
    public int L28 = 810;
    public int L29 = 830;
    public int L30 = 850;
    public int L31 = 875;
    public int L32 = 860;
    public int L33 = 885;
    public int L34 = 1540;
    public int L35 = 1595;
    public int L36 = 1620;
    public int L37 = 1650;
    public int L38 = 1675;
    public int L39 = 1695;

    public int L40 = 1800;
    public int L41 = 1850;
    public int L42 = 1880;
    public int L43 = 2165;

    public int L44 = 245;

    public int L45 = 635;
    public int L46 = 655;
    public int L47 = 1695;
    public int L48 = 0;
    public int L49 = 0;
    public int L50 = 0;
    public int L51 = 0;
    public int L52 = 0;
    public int L53 = 0;
    public int L54 = 0;
    public int L55 = 895;
    public int L56 = 900;

    public int L57 = 980;
    public int L58 = 1000;
    public int L59 = 1020;
    public int L60 = 1040;
    public int L61 = 1065;
    public int L62 = 1090;
    public int L63 = 1115;

    public int L64 = 1200;
    public int L65 = 1220;
    public int L66 = 1245;
    public int L67 = 1265;
    public int L68 = 1290;
    public int L69 = 1310;
    public int L70 = 1330;
    public int L71 = 1355;
    public int L72 = 1375;
    public int L73 = 1400;

    public int L74 = 1170;
    public int L75 = 1205;
    public int L76 = 1240;
    public int L77 = 1380;
    public int L78 = 1480;
    public int L79 = 1505;

    public int L80 = 305;
    public int L81 = 330;
    public int L82 = 1910;
    public int L83 = 1950;
    public int B2 = 2000;
    public int B3 = 2070;
    public int L86 = 2130;
    public int L86_1 = 607;
    public int L86_2 = 643;
    public int L86_3 = 260;
    public int L86_4 = 260;
    public int L86_5 = 1260;
    public int L86_6 = 2160;
    public int L87 = 2;


    // Variables de Impresion
    public String V1 = "! 0 200 200 2265 1";            // 32
    public String V2 = "LABEL";
    public String V3 = "CONTRAST 0";
    public String V4 = "TONE ";
    public String V5 = "SPEED 5";
    public String V6 = "PAGE-WIDTH 800";
    public String V7 = "BAR-SENSE";
    public String V8 = ";// PAGE 0000000008002232";
    public String V9 = "T 4 0 350";
    public String V10 = "B UCCEAN128 0 3 45 490";
    public String V11 = "T 7 0 186";
    // public String V12 = "T 7 0 186";
    // public String V13 = "T 7 0 186";

    public String V12 = "T 7 0 565";
    public String V13 = "T 7 0 590";

    public String V14 = "T 7 0 132";
    public String V15 = "T 7 0 132";
    public String V16 = "T 7 0 132";
    public String V17 = "T 7 0 132";
    public String V18 = "T 7 0 160";
    public String V19 = "T 7 0 160";
    public String V20 = "T 7 0 160";
    public String V21 = "T 7 0 178";
    // nuevas variales
    public String V22 = "T 7 0 550";
    public String V23 = "T 7 0 550";
    public String V24 = "T 7 0 550";
    public String V25 = "T 7 0 550";

    // fin variables
    public String V26 = "T 0 2 374";
    public String V27 = "T 0 2 374";
    public String V28 = "T 0 2 374";
    public String V29 = "T 0 2 374";
    public String V30 = "T 0 2 374";
    public String V31 = "T 0 2 374";
    public String V32 = "T 0 2 620";
    public String V33 = "T 0 2 620";
    //

    public String V34 = "T 7 0 169";

    public String V35 = "T 7 0 60 ";                    // retiro para
    // incluir en los
    // liquidacor
    public String V36 = "T 7 0 60 ";                    // retiro para
    // incluir en los
    // liquidacor

    public String V37 = "T 7 0 10 ";
    public String V38 = "T 7 0 10 ";
    public String V39 = "T 7 0 10 ";

    public String V40 = "T 7 1 455";
    public String V41 = "T 7 0 455";
    public String V42 = "T 7 0 285";
    public String V43 = "T 7 0 66 ";

    public String V44 = "T 7 1 400";
    public String V45 = "T 7 0 0";
    public String V46 = "T 7 0 0";
    public String V47 = "T 7 0 400 ";
    public String V48 = "LINE ";
    public String V49 = "LINE ";
    public String V50 = "LINE ";
    public String V51 = "LINE ";
    public String V52 = "LINE ";
    public String V53 = "LINE ";
    public String V54 = "LINE ";
    public String V55 = "T 0 2 10 ";
    public String V56 = "T 0 2 375";

    public String V57 = "T 7 0 10 ";
    public String V58 = "T 7 0 10 ";
    public String V59 = "T 7 0 10 ";
    public String V60 = "T 7 0 10 ";
    public String V61 = "T 7 0 260";
    public String V62 = "T 7 0 260";
    public String V63 = "T 7 0 260";

    public String V64 = "T 7 0 10 ";
    public String V65 = "T 7 0 10 ";
    public String V66 = "T 7 0 10 ";
    public String V67 = "T 7 0 10 ";
    public String V68 = "T 7 0 10 ";
    public String V69 = "T 7 0 10 ";
    public String V70 = "T 7 0 10 ";
    public String V71 = "T 7 0 10 ";
    public String V72 = "T 7 0 10 ";
    public String V73 = "T 7 0 10 ";

    public String V74 = "T 7 0 600";
    public String V75 = "T 7 0 600";
    public String V76 = "T 7 0 600";
    public String V77 = "T 7 1 520";
    // cambio de ubicacion de los creditos
    public String V78 = "T 7 0 10 ";
    public String V79 = "T 7 0 10 ";
    // fin creditos
    // public String V80 = "T 7 0 565";
    public String V80 = "T 7 0 213";
    public String V81 = "T 7 0 565";
    public String V82 = "T 7 0 475 ";
    public String V83 = "T 7 1 450 ";
    public String V84 = "B UCCEAN128 0 3 60 30 ";
    public String V85 = "B UCCEAN128 0 3 60 400";
    public String V86 = "T 7 0 20";
    public String V86_1 = "T 7 1 360";
    public String V86_2 = "T 7 1 360";
    public String V86_3 = "T 7 0 565";
    //nuevos campos
    public String V86_4 = "T 7 1 430";
    public String V86_5 = "T 7 1 450";
    public String V86_6 = "T 7 1  50";
    //fin nuevos campos
    public String V87 = "PCX 1";// 2 !<fachuine.pcx";
    public String V88 = "FORM";
    public String V89 = "PRINT";

    // FIN VARIABLES DE IMPRESION
    public void crearArchivoImpresion1(String valortexto1, String valortexto2) {
        if (null == valortexto1 || valortexto1.trim().equals(""))
            valortexto1 = "0";
        if (null == valortexto2 || valortexto2.trim().equals(""))
            valortexto2 = "0";

        L1 = 0;
        L2 = 0;
        L3 = 0;
        L4 = 0;
        L5 = 0;
        L6 = 0;
        L7 = 0;
        L8 = 0;
        L9 = 35 + Integer.parseInt(valortexto2);
        B1 = 128 + Integer.parseInt(valortexto2);
        // L11 = 257 + Integer.parseInt(valortexto2);
        L11 = 283 + Integer.parseInt(valortexto2);
        L12 = 283 + Integer.parseInt(valortexto2);
        // L13 = 308 + Integer.parseInt(valortexto2);
        L13 = 538 + Integer.parseInt(valortexto2);
        L14 = 358 + Integer.parseInt(valortexto2);
        L15 = 381 + Integer.parseInt(valortexto2);
        L16 = 407 + Integer.parseInt(valortexto2);
        L17 = 433 + Integer.parseInt(valortexto2);
        L18 = 491 + Integer.parseInt(valortexto2);
        L19 = 513 + Integer.parseInt(valortexto2);
        L20 = 538 + Integer.parseInt(valortexto2);
        L21 = 682 + Integer.parseInt(valortexto2);
        L22 = 735 + Integer.parseInt(valortexto2); // mfes fes
        L23 = 757 + Integer.parseInt(valortexto2); // mdes des
        L24 = 777 + Integer.parseInt(valortexto2); // mdes des
        L25 = 797 + Integer.parseInt(valortexto2); // mdes des
        //
        L26 = 765 + Integer.parseInt(valortexto2);
        L27 = 790 + Integer.parseInt(valortexto2);
        L28 = 810 + Integer.parseInt(valortexto2);
        L29 = 830 + Integer.parseInt(valortexto2);
        L30 = 850 + Integer.parseInt(valortexto2);
        L31 = 875 + Integer.parseInt(valortexto2);
        L32 = 860 + Integer.parseInt(valortexto2);
        L33 = 885 + Integer.parseInt(valortexto2);
        L34 = 1540 + Integer.parseInt(valortexto2);
        L35 = 1595 + Integer.parseInt(valortexto2);
        L36 = 1620 + Integer.parseInt(valortexto2);
        L37 = 1650 + Integer.parseInt(valortexto2);
        L38 = 1675 + Integer.parseInt(valortexto2);
        L39 = 1695 + Integer.parseInt(valortexto2);

        L40 = 1800 + 12 + Integer.parseInt(valortexto2);
        L41 = 1850 + 10 + Integer.parseInt(valortexto2);
        L42 = 1880 + 7 + Integer.parseInt(valortexto2);
        L43 = 2165 + Integer.parseInt(valortexto2);

        L44 = 210 + Integer.parseInt(valortexto2);

        L45 = 615 + Integer.parseInt(valortexto2);
        L46 = 652 + Integer.parseInt(valortexto2);
        L47 = 1695 + Integer.parseInt(valortexto2);

        L48 = 0;
        L49 = 0;
        L50 = 0;
        L51 = 0;
        L52 = 0;
        L53 = 0;
        L54 = 0;
        L55 = 895 + Integer.parseInt(valortexto2);
        L56 = 900 + Integer.parseInt(valortexto2);

        L57 = 980 + Integer.parseInt(valortexto2);
        L58 = 1000 + Integer.parseInt(valortexto2);
        L59 = 1020 + Integer.parseInt(valortexto2);
        L60 = 1040 + Integer.parseInt(valortexto2);
        L61 = 1065 + Integer.parseInt(valortexto2);
        L62 = 1090 + Integer.parseInt(valortexto2);
        L63 = 1115 + Integer.parseInt(valortexto2);

        L64 = 1200 + Integer.parseInt(valortexto2);
        L65 = 1220 + Integer.parseInt(valortexto2);
        L66 = 1245 + Integer.parseInt(valortexto2);
        L67 = 1265 + Integer.parseInt(valortexto2);
        L68 = 1290 + Integer.parseInt(valortexto2);
        L69 = 1310 + Integer.parseInt(valortexto2);
        L70 = 1330 + Integer.parseInt(valortexto2);
        L71 = 1355 + Integer.parseInt(valortexto2);
        L72 = 1375 + Integer.parseInt(valortexto2);
        L73 = 1400 + Integer.parseInt(valortexto2);

        L74 = 1170 + Integer.parseInt(valortexto2);
        L75 = 1205 + Integer.parseInt(valortexto2);
        L76 = 1240 + Integer.parseInt(valortexto2);
        L77 = 1380 + Integer.parseInt(valortexto2);

        L78 = 1480 + Integer.parseInt(valortexto2);// Informacion Credito
        L79 = 1505 + Integer.parseInt(valortexto2);

        // L80 = 283 + Integer.parseInt(valortexto2);
        L80 = 308 + Integer.parseInt(valortexto2);
        L81 = 308 + Integer.parseInt(valortexto2);
        L82 = 1910 + 7 + Integer.parseInt(valortexto2);
        L83 = 1950 + Integer.parseInt(valortexto2);
        B2 = 2000 + Integer.parseInt(valortexto2);
        B3 = 2070 + Integer.parseInt(valortexto2);
        L86 = 2130 + Integer.parseInt(valortexto2);
        L86_1 = 607 + Integer.parseInt(valortexto2);
        L86_2 = 643 + Integer.parseInt(valortexto2);
        L86_3 = 260 + Integer.parseInt(valortexto2);

        L86_4 = 130 + Integer.parseInt(valortexto2);
        L86_5 = 2005 + Integer.parseInt(valortexto2);
        L86_6 = 2070 + Integer.parseInt(valortexto2);
        L87 = 2 + Integer.parseInt(valortexto2);


        // valor intencidad impresion

        String archivoImprimir = directorioactual + "/IMPRESION.LOG";// Todo:
        // verificar
        // si es
        // necesario
        // paso
        // por
        // referencia@"\Impresion.log";

        try {
            File file = new File(archivoImprimir);
            if (file.exists()) {
                file.delete();
            }
            BufferedWriter archivoprint = new BufferedWriter(new FileWriter(file));
            // StreamWriter archivoprint = File.CreateText(archivoImprimir);
            archivoprint.write(V1);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V2);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V3);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V4 + valortexto1);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V5);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V6);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V7);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V8); // variables finales para la impresion
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V9 + String.format("%5s", L9));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V10 + String.format("%5s", B1));// B1.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V11 + String.format("%5s", L11));// 11.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V12 + String.format("%5s", L12));// 12.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V13 + String.format("%5s", L13));// 13.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V14 + String.format("%5s", L14));// 14.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V15 + String.format("%5s", L15));// 15.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V16 + String.format("%5s", L16));// 16.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V17 + String.format("%5s", L17));// 17.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V18 + String.format("%5s", L18));// 18.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V19 + String.format("%5s", L19));// 19.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V20 + String.format("%5s", L20));// 20.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V21 + String.format("%5s", L21));// 21.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            // inicio nuevas variables
            archivoprint.write(V22 + String.format("%5s", L22));// 22.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V23 + String.format("%5s", L23));// 23.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V24 + String.format("%5s", L24));// 24.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V25 + String.format("%5s", L25));// 25.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            // fin de nuevas variables
            archivoprint.write(V26 + String.format("%5s", L26));// 26.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V27 + String.format("%5s", L27));// 27.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V28 + String.format("%5s", L28));// 28.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V29 + String.format("%5s", L29));// 29.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V30 + String.format("%5s", L30));// 30.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V31 + String.format("%5s", L31));// 31.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V32 + String.format("%5s", L32));// 32.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            // archivoprint.write(V30 + String.format("%5s",
            // L30));//30.ToString().Trim().PadRight(5, ' '));
            archivoprint.write(V33 + String.format("%5s", L33));// 33.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            //
            archivoprint.write(V34 + String.format("%5s", L34));// 34.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            //
            archivoprint.write(V35 + String.format("%5s", L35));// 35.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V36 + String.format("%5s", L36));// 36.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V37 + String.format("%5s", L37));// 37.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V38 + String.format("%5s", L38));// 38.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V39 + String.format("%5s", L39));// 39.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V40 + String.format("%5s", L40));// 40.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V41 + String.format("%5s", L41));// 41.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V42 + String.format("%5s", L42));// 42.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V43 + String.format("%5s", L43));// 43.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V44 + String.format("%5s", L44));// 44.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V45 + String.format("%5s", L45));// 45.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V46 + String.format("%5s", L46));// 46.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V47 + String.format("%5s", L47));// 47.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V48);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V49);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V50);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V51);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V52);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V53);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V54);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V55 + String.format("%5s", L55));// 55.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V56 + String.format("%5s", L56));// 56.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V57 + String.format("%5s", L57));// 57.ToString().Trim()+" ");
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V58 + String.format("%5s", L58));// 58.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V59 + String.format("%5s", L59));// 59.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V60 + String.format("%5s", L60));// 60.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V61 + String.format("%5s", L61));// 61.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V62 + String.format("%5s", L62));// 62.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V63 + String.format("%5s", L63));// 63.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V64 + String.format("%5s", L64));// 64.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V65 + String.format("%5s", L65));// 65.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V66 + String.format("%5s", L66));// 66.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V67 + String.format("%5s", L67));// 67.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V68 + String.format("%5s", L68));// 68.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V69 + String.format("%5s", L69));// 69.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V70 + String.format("%5s", L70));// 70.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V71 + String.format("%5s", L71));// 71.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V72 + String.format("%5s", L72));// 72.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V73 + String.format("%5s", L73));// 73.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V74 + String.format("%5s", L74));// 74.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V75 + String.format("%5s", L75));// 75.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V76 + String.format("%5s", L76));// 76.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V77 + String.format("%5s", L77));// 77.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            // movimiento de parametros al moverlos y
            archivoprint.write(V78 + String.format("%5s", L78));// 78.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V79 + String.format("%5s", L79));// 79.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V80 + String.format("%5s", L80));// 80.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V81 + String.format("%5s", L81));// 81.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V82 + String.format("%5s", L82));// 82.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V83 + String.format("%5s", L83));// 83.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V84 + String.format("%5s", B2));// B2.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V85 + String.format("%5d", B3));// B3.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86 + String.format("%5d", L86));// 86.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_1 + String.format("%5d", L86_1));// 86_1.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_2 + String.format("%5d", L86_2));// 86_2.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_3 + String.format("%5d", L86_3));// 86_3.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_4 + String.format("%5d", L86_4));// 86_4.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_5 + String.format("%5d", L86_5));// 86_5.ToString().Trim().PadRight(5,
            // ' '));
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V86_6 + String.format("%5d", L86_6));// 86_6.ToString().Trim().PadRight(5,
            // ' '));


            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V87 + String.format("%5d", L87) + " !<fachuine.pcx");
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V88);
            archivoprint.write("\r\n");//archivoprint.newLine();
            archivoprint.write(V89.trim());
            archivoprint.write("\r\n");//Ax
            archivoprint.flush();
            archivoprint.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // nuevas funciones
    public double evaluarDistanciaAlPredio(double Latitud1, double Longitud1, double Latitud2, double Longitud2) {

        Utils utils = new Utils();//Ax log y utilidades
        File logfile = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/LOGEVENTOS.LOG");
//        utils.Log(logfile, " coordenadas de entrada  "+Longitud2+ "-" +Latitud2 );
//        utils.Log(logfile, " coordenadas que toma para calculo "+Latitud1+ "-" +Longitud1 );


        double xx = Latitud2;
        double yy = Longitud2;

        double degtorad = 0.01745329;
        double radtodeg = 57.29577951;
        double dlong;
        double dvalue;
        double dd;
        double miles;
        double km;
        dlong = Longitud1 - Latitud2;
        Log.e("errord",Longitud2 +"++"+Latitud2+"distancia "+Latitud1+"++"+Longitud1);
        dvalue = (Math.sin(Latitud1 * degtorad) * Math.sin(Longitud2 * degtorad)) + (Math.cos(Latitud1 * degtorad) * Math.cos(Longitud2 * degtorad) * Math.cos(dlong * degtorad));
        dd = Math.acos(dvalue) * radtodeg;
        miles = dd * 69.16;
        km = dd * 111.302;
        return km * 1000;

    }

    //Ax: envia una prueba para saber si la impresora esta activa
    public static boolean prueba_impresora() {

//        if (habilitadaimpresora < 1) {
//            return false;
//        }

        byte[] outputData;
        String trozo = "";
        outputData = trozo.getBytes();

        if (btPrintService.write(outputData)) {
            return true;
        }
        return false;
    }

    //Ax: envia una prueba de impresion, se elimina la crecio y lectura de un archivo (textcabezal.txt)
    public static int prueba_cabeza()// (int donde)
    {
        if (habilitadaimpresora < 1) {
            return 0;
        }

        try {
            String texto = "! 0 200 200 560 1\r\n"
                    + "JOURNAL\r\n"// LABEL
                    + "CONTRAST 0\r\n" + "TONE 0\r\n" + "SPEED 5\r\n" + "PAGE-WIDTH 780\r\n" + "NONE-SENSE\r\n" + "POSTFEED 20\r\n"
                    + ";// PAGE 0000000007800560\r\n" + "LINE 449 154 449 220 341\r\n" + "LINE 0 22 0 88 271\r\n" + "LINE 0 0 0 24 793\r\n"
                    + "LINE 189 88 189 154 341\r\n" + "LINE 0 221 0 287 793\r\n" + "\r\n"// FORM
                    + "PRINT" + "\r\n";
            String trozo = "";
            int tamano = texto.length();
            byte[] outputData;
            int i = 0;

            for (i = 0; i <= texto.length(); ) {
                if (tamano >= 35)
                    trozo = texto.substring(i, (i + 35));
                else
                    trozo = texto.substring(i, (i + tamano));
                outputData = trozo.getBytes();

                tamano -= 35;
                i = i + 35;
                if (!btPrintService.write(outputData)) {
                    return (-1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return (1);
    }

    public String enviarSerialesActivos() {

        String codigosActivos;

        // CodigosActivos =
        // "11356D0086;11357D0220;11356D02AB;11354D0013;11356D02B5;11357D01F4;11357D0214;11357D0129;11357D0213;11356D02A3;12064D0021;12064D0012;11353D0332;11356D0081;"+
        // "11355D01FA;11353D02EA;11354D0015;11356D0015;11347D01BB;11356D0093;11355D015F;11356D0053;11354D0228;11355d02F7;11355d0139;11355D012D;11355D0305;11355D0158;"+
        // "11355D02EE;11355D02FB;12066D0082;11355D01DA;11355D01D3;12065D00F6;11355d01CE;12049D0056;11357d0219;11355D0178;12064D001F;12064D0046;11357D00C8;11357D00DC;"+
        // "11357D0103;11356D0253;11357D00B7;11357d00FB;11358d03B8;11356D003B;11356D005F;11357D00E4;11354D0002;11356d008B;11356D0059;11353D02FB;11354D004F;11353D031A;"+
        // "11354d0008;11356D0050;11353D02F1;11355D01DB;11356D00BC;11356d00A0;11356D00B6;11293D0195;11357D00F1;11293D0199;11293D0173;11359d006C;11293d0165;11358D039A;"+
        // "11356D0291;11293d019D;11356d0248;11293D016D;11356D025C;11356D028F;11356D0262;11293D011E;11356D0279;11355d0135;11355D00ED;11355D00F0;11356D0270;11293D017F;"+
        // "11293D016E;11293D0149;11355D0054;11355d02F0;11355D02D7;11355D00FB;11293D013F;11293D00FD;11354D0308;11293D0150;11293D00F1;11293D00F5;11293D0146;11355D00DF;"+
        // "11293D013C;11293D014A;11354D01B0;11357D016B;11353D02C4;11357D011A;11293D00F2;11357D00B1;11299D00B2;11356D02A0;11353D01CF;11357D00A3;11355D0200;11356D004B;"+
        // "11357D00E6;11356D02BB;11355D=021;11355D0306;11355D0374;11355D01F5;11358D0061;12064D0048;11356D00A6;11358D0075;12064D0015;11358D006F;11353D02C9;12064D002C;"+
        // "11353D02BC;11358D03B9;11354D0193;11358D002C;11357d022F;11357D0277;11357D01BD;11357D01C3;11357D026C;11357D02F5;11358D03FF;11358D0086;11358D03FA;11357D00B8;"+
        // "11357D02A6;11357D028F;11357D027B;11357D00B0;11353D031D;11356D0178;11355D01D6;11357D02ED;11355D003B;11355D0169;11355d004D;11354D0061;11354d029D;11357d01FF;"+
        // "11357D01FC;11356d=042;11359D01B1;11355D02F3;11359D023D;11354D0016;11354D002E;11357D01D8;11357D03E2;11354D002A;11357D01E5;11356D0073;11357D01C5;11354D02A3;"+
        // "11357D01CF;11357D018A;11356D0269;11357D0374;11356D0207;11356D021A;11359D0083;11354D0059;11356D022B;11356D0241;11356D0259;11356D021F;11357D0289;11357D01C6;"+
        // "11354D0298;11354D0147;11356D01A2;11356D014B;11357D010A;11355D0098;11356D02BE;11356D0240;11356D01B6;11356D019E;11356D01F2;11356D01E5;11356d0072;11356D0164;"+
        // "11356d01EE;11359d009B;11356D022D;11338D000E;11356D0182;11357D00C0;11356D0222;11356D0200;11356D0206;11356D0243;11356D025B;11356D020F;11359D0078;11359D0069;"+
        // "11357D009C;11357D0087;11357D00D7;11354D0304;11357D00A0;11353D02C8;11358D0034;11359D028A;11357D00AB;11357D00DE;11357D00D4;11357D00D1;11293D00E1;11356D01FC;"+
        // "11299D00CA;11355D01BC;11293D00DF;11353D0346;11293D00D2;11354D0001;11355D0019;11355D0099;11355D007F;11293D0106;11355D02E9;11272D0014;11355D00B3;11355D007E;"+
        // "11358D0252;11299D0074;11354D0281;11353D02B7;11358D037F;11355D0063;11293D00F8;11357D01F1;11292D012F;11353D02D8;11293D00E7;11294D00E2;11294D007E;11294D0082;"+
        // "11294D00D6;11356D01F7;11355D01E5;11293D0100;11355D019F;11355D01EE;";

        codigosActivos = "11356D0086;11357D0220;11356D02AB;11354D0013;11356D02B5;11357D01F4;11357D0214;11357D0129;11357D0213;11356D02A3;12064D0021;12064D0012;11353D0332;11356D0081;11355D01FA;11353D02EA;11354D0015;11356D0015;11347D01BB;11356D0093;11355D015F;11356D0053;11354D0228;11355d02F7;11355d0139;11355D012D;11355D0305;11355D0158;11355D02EE;"
                + "11355D02FB;12066D0082;11355D01DA;11355D01D3;12065D00F6;11355d01CE;12049D0056;11357d0219;11355D0178;12064D001F;12064D0046;11357D00C8;11357D00DC;11357D0103;11356D0253;11357D00B7;11357d00FB;11358d03B8;11356D003B;11356D005F;11357D00E4;11354D0002;11356d008B;11356D0059;11353D02FB;11354D004F;11353D031A;11354d0008;11356D0050;11353D02F1"
                + "11355D01DB;11356D00BC;11356d00A0;11356D00B6;11293D0195;11357D00F1;11293D0199;11293D0173;11359d006C;11293d0165;11358D039A;11356D0291;11293d019D;11356d0248;11293D016D;11356D025C;11356D028F;11356D0262;11293D011E;11356D0279;11355d0135;11355D00ED;11355D00F0;11356D0270;11293D017F;11293D016E;11293D0149;11355D0054;11355d02F0;11355D02D7"
                + "11355D00FB;11293D013F;11293D00FD;11354D0308;11293D0150;11293D00F1;11293D00F5;11293D0146;11355D00DF;11293D013C;11293D014A;11354D01B0;11357D016B;11353D02C4;11357D011A;11293D00F2;11357D00B1;11299D00B2;11356D02A0;11353D01CF;11357D00A3;11355D0200;11356D004B;11357D00E6;11356D02BB;11355D0021;11355D0306;11355D0374;11355D01F5;11358D0061"
                + "12064D0048;11356D00A6;11358D0075;12064D0015;11358D006F;11353D02C9;12064D002C;11353D02BC;11358D03B9;11354D0193;11358D002C;11357d022F;11357D0277;11357D01BD;11357D01C3;11357D026C;11357D02F5;11358D03FF;11358D0086;11358D03FA;11357D00B8;11357D02A6;11357D028F;11357D027B;11357D00B0;11353D031D;11356D0178;11355D01D6;11357D02ED;11355D003B"
                + "11355D0169;11355d004D;11354D0061;11354d029D;11357d01FF;11357D01FC;11356d0042;11359D01B1;11355D02F3;11359D023D;11354D0016;11354D002E;11357D01D8;11357D03E2;11354D002A;11357D01E5;11356D0073;11357D01C5;11354D02A3;11357D01CF;11357D018A;11356D0269;11357D0374;11356D0207;11356D021A;11359D0083;11354D0059;11356D022B;11356D0241;11356D0259"
                + "11356D021F;11357D0289;11357D01C6;11354D0298;11354D0147;11356D01A2;11356D014B;11357D010A;11355D0098;11356D02BE;11356D0240;11356D01B6;11356D019E;11356D01F2;11356D01E5;11356d0072;11356D0164;11356d01EE;11359d009B;11356D022D;11338D000E;11356D0182;11357D00C0;11356D0222;11356D0200;11356D0206;11356D0243;11356D025B;11356D020F;11359D0078"
                + "11359D0069;11357D009C;11357D0087;11357D00D7;11354D0304;11357D00A0;11353D02C8;11358D0034;11359D028A;11357D00AB;11357D00DE;11357D00D4;11357D00D1;11293D00E1;11356D01FC;11299D00CA;11355D01BC;11293D00DF;11353D0346;11293D00D2;11354D0001;11355D0019;11355D0099;11355D007F;11293D0106;11355D02E9;11272D0014;11355D00B3;11355D007E;11358D0252"
                + "11299D0074;11354D0281;11353D02B7;11358D037F;11355D0063;11293D00F8;11357D01F1;11292D012F;11353D02D8;11293D00E7;11294D00E2;11294D007E;11294D0082;11294D00D6;11356D01F7;11355D01E5;11293D0100;11355D019F;11355D01EE;11357D00F4;11357D0095;11356D00AA;11354D005F;11357D015D;11293D0101;11355D015E;11355D01A3;11355D01F0;11357D03CE;11359D00A9"
                + "11357D010F;11355D01C5;11355D02AC;11356D02CA;11359D01BA;11359D01CE;11353D0353;11358D035C;11357D00B6;11354D0183;11353D0354;11354D00F6;11356D00AF;11353D02FD;11357D0104;11355D01FE;11357D00AC;11357D0419;11357D01FD;11356D029D;11355D0168;11353D01D3;11355D01C0";

        return (codigosActivos);
    }

    public static void copyFile(String fileA, String fileB, boolean deleteFile) {

        InputStream inStream = null;
        OutputStream outStream = null;

        try {

            File afile = new File(fileA);
            File bfile = new File(fileB);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            // delete the original file
            if (deleteFile) {
                afile.delete();
                System.out.println("File was moved successful!");
            } else {
                System.out.println("File is copied successful!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
