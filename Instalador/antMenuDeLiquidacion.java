package com.gstolima.accesoyseguridad;

import java.io.FilenameFilter;
import android.content.Context;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.AsyncResponse;
import com.Util.DatosWS;
import com.Util.Printzpl;
import com.Util.Utils;
import com.Util.UtilsNet;
import com.Util.WSSoap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gstolima.captureException.myExceptionHandler;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;
import com.gstolima.comunicaciones.EnvioCuentaNueva;
import com.gstolima.moduloGPS.ActGPS;
import com.gstolima.moduloMapas.MapsActivity;
import com.gstolima.moduloMapas.NavigationHelper;
import com.gstolima.moduloanomalias.AnomaliaDeNoLectura;
import com.gstolima.moduloanomalias.ModuloDeAnomalias;
import com.gstolima.modulobusquedacuenta.ModuloBusquedaCuenta;
import com.gstolima.modulobusquedacuenta.ModuloConsultaNoEnviados;
import com.gstolima.modulocambiodigitos.ModuloCambioDigitos;
import com.gstolima.modulochat.Insertar;
import com.gstolima.modulocomentarios.Comenta;
import com.gstolima.modulocomentarios.Firma;
import com.gstolima.modulocomentarios.Informe;
import com.gstolima.modulocuentanueva.ModuloCuentaNueva;
import com.gstolima.tablas.EnvioGPS;
import com.gstolima.tablas.Notificaciones;
import com.gstolima.tablas.TablaCodBarras;
import com.gstolima.tablas.TablaEncabezado_General;
import com.gstolima.tablas.TablaRangos;
import com.gstolima.tablas.TablaRegistroSalida;
import com.gstolima.tablas.TablaMunicipios;
import com.gstolima.modulobluetooth.msgTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.view.animation.AnimationUtils;
import android.view.animation.Animation;


import com.gstolima.comunicaciones.SyncHelper;
import com.gstolima.comunicaciones.EnvioLectura;
import com.gstolima.comunicaciones.EnvioFoto;
import com.gstolima.comunicaciones.CrudEnvioLectura;
import com.gstolima.comunicaciones.CrudEnvioFoto;
import com.gstolima.comunicaciones.ModoReenvio;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.gstolima.accesoyseguridad.LecturaSyncService;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import android.view.WindowManager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TableLayout;

import com.gstolima.modulobluetooth.btPrintFile;
import com.gstolima.modulobluetooth.DeviceListActivity;

//NUEVO PARA CONTROLAR LA MANIPULACION DEL RELOJ
import android.provider.Settings;
import android.os.SystemClock;
import androidx.core.content.ContextCompat;
import com.google.android.material.card.MaterialCardView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.gstolima.modulobluetooth.Bluetooth_Permission;
import com.gstolima.modulobluetooth.Bluetooth_Printer;
import com.gstolima.modulobluetooth.Bluetooth_SharedPreferences;

public class MenuDeLiquidacion extends AppCompatActivity implements AsyncResponse, View.OnClickListener {

    public final Logger logger = LoggerFactory.getLogger(MenuDeLiquidacion.class);
    private static final Set<String> CAUSAS_CON_LECTURA_EN_INFORME =
            new HashSet<>(Arrays.asList("3", "23", "33", "35"));

    // Ax: modelo de impresión nuevo (stateless, sin servicio persistente ni
    // archivo intermedio). Ver sección MODELO NUEVO DE IMPRESIÓN al final de
    // esta clase. Reemplaza EscribaArchivoImpresion*/imprimirF2/imprimirESC.
    private final Bluetooth_Printer nuevoImpresor = new Bluetooth_Printer();

    AnomaliaDeNoLectura anomaliaDeLectura = new AnomaliaDeNoLectura();
    TablaMunicipios TablaMunicipio = new TablaMunicipios();
    ApuntadorCliente miApuntador = new ApuntadorCliente();
    TablaEncabezado_General tablaEncabezado = new TablaEncabezado_General();
    AsyncCallChat taskChat = null;
    Comenta tablaComentarios = new Comenta();
    DatosWS wsoap = new DatosWS("", "", "", 0);
    Notificaciones notiFicaciones = new Notificaciones();
    public InputMethodManager imm;
    EditText txtElectura;
    Spinner spinnerMenu;
    Handler handler = new Handler(Looper.getMainLooper());
    Keyboard mKeyboard;
    KeyboardView mKeyboardView;
    private AudioManager audiom;
    private static int INTERVAL = 120000; //Ax: 2 Min.
    public Date fechaEnvioAlWsOcupado;
    public int TecladoNumericoActivo=0;

    public String ControlEspecialZonaRoja = "";
    public String ControlEspecialImpresora = "";
    public boolean scanEntregas = false;//Ax: identifica si la busqueda viene de scanner y entreas para procesar automaticamente
    public boolean tryAlL = false;
    boolean lecturasiguales = false; //Bandera para mostrar el menu...xxx por lecturas iguales
    boolean activaCamara = true;
    // Ax: control de campo autorizado por supervisor (ver OpcionesDeSupervisor). Ambos
    // inician restrictivos por diseño: navegación apagada y obligatoriedad de impresora activa.
    private boolean navegacionRegistrosHabilitada = false;
    private boolean impresoraObligatoriaDeshabilitadaPorSupervisor = false;
    Boolean activaLectorBarras = true;
    boolean banderaChat = false;
    //boolean banderaWsOcupado = false; //Ax: es para inhabilitar acciones mientras se ejecuta asyntask
    public boolean estaEnvioAlWsOcupado = false; //ojo debe ser public
    boolean esSupervisor = false;
    boolean noactforesult = true; //Ax: me indica si vengo del activityresult (MODULORESPUESTA_REQUEST_CODE)
    boolean procesandoenvioenHilos_chat = false;
    boolean salirse = false;
    Boolean variableTomarDatosLectura = false;
    boolean esEntrega = false;//celsia
    boolean ventanaFirma = false;
    boolean causalEntrega = false;
    //boolean reenvio = false;
    boolean tomaFotoBoton = false;
    boolean medidorCero = false;
    String UltimoInforme = "";
    boolean SonBarrasGrupales = false;
    String UltimaBarraLeida = "";
    String Empresa = " INELMA ";
    String Parametro_CUENTA = "912";
    String Parametro_TIPOMEDIDA = "1";

    int NumeroDeFotos = 0;
    Toast toast = null;
    ImageButton btnAdelante;
    ImageButton btnAtras;
    ImageButton btnFotografia;
    ImageButton btnPrimero;
    ImageButton btnUltimo;
    ImageButton btnActivaScanner;
    ImageButton btnAnomalia;
    ImageButton btnBuscar;
    private static final String TAG = "Menu Liquidacion - btprint";
    boolean BorroLecturaOCausa = false;
    ImageView imagenChat;
    ImageView imagenLiquid_1;
    ImageView imagenLiquid_2;
    ImageView imagenLiquid_3;
    ImageView imagenRed;
    ImageView imagenPrinter;
    String MarcaDeImpresora = "ZEBRA";
    public int enviosGPRScantidad = 1; //ojo debe ser public
    int FotoObligatoriaXLectura = 0;
    public int EnviarxBloque = 1; //ojo debe ser public
    int YaMostroMensaje = 0;
    int contFuerzaFotos = 0;
    int msgCorto = 200;
    int msgMedio = 500;
    int msgLargo = 1000;
    int msgFugaz = 50;
    int requierenovedad = 0;
    int conttryall = 0;
    int alterno;
    int apuntadortarifaCT = 0;
    int apuntadortarifareactiva = 0;
    int banderaadicionarNovedad = 0; //Ax: para saber que ya se contesto un alert dialog
    int banderaCuentaContratada = 0; //Ax: para saber que ya se contesto un alert dialog
    public int capRegistroActual = 0;//Captura registro actual que se esta procesando, para los que regresan de otras actividades
    int cc_indlectura;//Ax: Para recuperar datos de procesarlectura
    int cc_primero;//Ax: Para recuperar datos de procesarlectura
    // int conexionGPRSActiva = 1;
    int encenderEstadoActualGPS = 1;
    int ErroresDeEnvio = 0;
    int TOMARNUEVACRITICA = 0;
    int TieneMensajeporvalidar = 0;
    int exitoImagen = 0;//Ax: = 1 si la imagen se tomó
    int fuePromediado = 0;// ESTA VARIABLE NOS INDICARA SI SE PROMEDIO AL MOMENTO DE IMPRIMIR LA FACTURA POR SU CRITOCA ALTA
    int indicadorManual = 0;
    int leyoCoordenadas = 0;
    int predio_temporal = 1;
    //int procesandoenvioenHilos = 0;
    int s_idac = 0; //Guarda el id momentaneamente
    int sinBarSence = 0;
    int tiempoInactivolaImpresora = 0;
    int tipoFotoDigital = 0;
    int trama = 100;
    int vecesImagen = 0;//Ax: Captura la cantidad de reintentos de tomar una foto
    int idEnvioGps = 0;

    //private static final int NOTIF_ALERTA_ID = 0;
    private static int TAKE_PICTURE = 1;
    public final int ACTGPS_REQUEST_CODE = 6683;
    public final int ANOMALIA_REQUEST_CODE = 6676;//Ax para enviar a actividad Anomalias, ESTE NUMERO ES ARBITRARIO
    public final int BUSQUEDA_REQUEST_CODE = 6678;//Ax para Recibir de la actividad de busqueda cerrada, ESTE NUMERO ES ARBITRARIO
    public final int CAPT_COMENTARIO_REQUEST_CODE = 6675;//Ax para Recibir de la actividad del menu capturar comentario cerrada
    public final int CHATINSETAR_REQUEST_CODE = 6682;
    public final int CONFIGFORMATIMP_REQUEST_CODE = 6681;//
    File logPrint;// Ax para Recibir de la actividad del Modulo de Configuracion Formato Impresion
    public final int CONSULTNONVIADS_REQUEST_CODE = 6677;//Ax para Recibir de la actividad del Modulo de Consulta No Enviados
    public final int ENTREGASGRUPALS_REQUEST_CODE = 6680;//Ax para Recibir de la actividad del Modulo de Entrega grupal
    public final int OPCIONESSUPERVI_REQUEST_CODE = 6674;//Ax para Recibir de la actividad del Modulo de Entrega grupal
    public final int INFORME_REQUEST_CODE = 6670;
    public final int CHAT_REQUEST_CODE = 6673;
    public final int DIGITOS_REQUEST_CODE = 6669;
    public final int MAPA = 6700;
    public final int CAPT_FIRMA_REQUEST_CODE = 1010;
    public final int CAMARA_REQUEST_CODE = 1013;
    public int[] posicfotocont;
    public final int CUENTA_NUEVA = 7000;

    public EnvioGPS misenvios = new EnvioGPS();
    public EnvioGPS MisEnviosHilos = new EnvioGPS();
    //public File logfile; //Ax: Es para los log de error
    Resources rs;
    ScrollView scroll; //Ax: para cambiar el tamaño del scrollview

    //private static final String TAG = "Menu Liquidacion - btprint";
    private String name = "";
    public String[] Archivos_CPCAN; //ver ...envios aforos, novedades etc
    String altitudReportadaGPS = "0";
    String ArchivoAEnviarRecibir = ""; //Ax: Ruta servidor del archivo zip por recibir
    String arma_cuentasGps = "";//Ax. guarda datos para la funcion guardarcoordenadas
    String causadenolectura1 = "";
    String causal = "";//Captura causales de lectura que regresan de otras actividades
    String cc_causaact;//Ax: Para recuperar datos de procesarlectura
    String cc_lecturaact;//Ax: Para recuperar datos de procesarlectura
    String Ultimo_Estado = "";
    String CicloReal = "000000000";//Ax: cargados desde archivo nombre
    String Ciclo = "000";//Ax: cargados desde archivo nombre
    String criticaPDA1 = "";
    String DescSect = "";//Ax: captura si algun registro es de Entregas o Lecturas
    String digitos1 = "";
    String Division = "0";
    String esComprimido = "";
    String fechayHoraReportadaGPS = "";//getPhoneDate() + " " + getPhoneHour();
    String idContador1 = "";
    String impresora = "";
    String intentos1 = "";
    String latitudActualGpg = "0.00";
    String lector = "";
    String lecturaAnterior1 = "";
    String lecturaModificada11 = "";
    String lecturaModificada21 = "";
    String lecturaTomada1 = "";
    String longitudActualGpg = "0.00";
    String mensajeNovedad = "";
    String metodo = ""; //Ax: lleva el metodo a ejecutar en Asyntask
    String Municipio = "00";
    String nivelOperador = "";
    public String NombreArchivos = "";
    public String nombredelaImagen = "";//Ax: Captura el nombre del archivo de imagen
    String idFoto = "";
    String cuentaFoto = "";
    public String nombrefoto = ""; //Ax: lleva el nombre de la foto que se creara para despues mandarla a optimizar
    String nroContador1 = "";
    String numeroDeSatelitesGPS = "0";
    String paginaWs = "";
    String respuesta = "";
    String rutaAdministrador = "";
    String rutaCargadaPDA = "";
    String RutaZip = ""; //Ax: Nombre del zip a enviar al servidor
    String s_foto = "0"; //Recupera si foto es obligatorio
    String s_info = ""; //Recupera si informe es obligatorio
    String Seccion = "00";
    String serialPDA = "000000000000000";
    String terminalImei = "";
    String ultimaCriticaLectura = "  ";
    String ultimaLatitud = "75.012";// esperar que me manden las coordenada
    String ultimaLongitud = "0.12";
    String URL = "";
    String valor_alumbradopublico = "0";
    String valor_energia = "0";
    String valorLeidoFactura = "0";
    String velocidadGPS = "0";
    String cn_Ciclo = ""; //Para capturar temporalmente datos como ruta
    String cn_Desc_Depto = "";
    String cn_Cod_Municipio = "";
    String cn_Cod_Sector = "";
    String cn_Cod_Ruta = "";
    String obligaFotografia = "0";
    String hash = "";
    String archivoEFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "ENVIOFOTOS";
    String permiteLectura = "";
    String cadenaURLapi = "";
    String telefono = "";
    int puertoAPI = 0;

    int TamannoPantalla = 780;

    TablaRangos misRangos = new TablaRangos();
    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
    TablaCodBarras infoCodBarras = new TablaCodBarras();


    TableLayout tablaResumen;
    TableLayout tabla_fondo;
    TableRow fila;
    TableRow.LayoutParams layoutCampo;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutValor;

    TextView lblInfoMedida;
    TextView lblpuntero;
    TextView txtLatitud;
    TextView txtLongitud;
    TextView txtMedidorCuenta;
    VariablesGlobales variables = new VariablesGlobales();
    Utils utils = new Utils();

    private String currentPhotoPath;
    private String namePhoto;

    boolean lectModifica = false;
    boolean fotosCuentaNueva = false;
    int NroEnvios = 0;
    int vecesRea = 0;
    int ultimoCont = 0;
    String nameForIntent = "";
int FiltroCero=0;
    int filaseleccionada = 0;
    int CodigoIntent = 0;
    private Uri imageUri;
    CrudComunicaciones crudComuni;
    //Thread handlerTask; //Ax: publico para poder detenerlo
    Dialog dialogloading = null;
    String msgAdmon = "";
    int ejecutaMenuEnvio = 0;

    int ValidandoNoEnv = 0;
    String CuentasProblema = "";
    int Noleidas = 0;
    int LeidasNoenviadas = 0;

    private BroadcastReceiver syncBroadcastReceiver;
    private boolean receiverRegistered = false;
    //nuevo para el menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //------ Teclado Nuevo ---------
    CardView Boton0, Boton1, Boton2, Boton3, Boton4, Boton5, Boton6, Boton7, Boton8, Boton9;
    ImageView Delete;
    TextView Enter;
    SparseArray<String> sparseArray = new SparseArray<>();
    StringBuilder code = new StringBuilder();
    //------- Fin Teclado Nuevo ----

    boolean imprimirTrasFoto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fechaEnvioAlWsOcupado = new Date();
        setContentView(R.layout.activity_menu_de_liquidacion);
        Thread.setDefaultUncaughtExceptionHandler(new myExceptionHandler(getApplicationContext()));
        tabla_fondo = (TableLayout) findViewById(R.id.tabla_fondo);
        txtElectura = (EditText) findViewById(R.id.txtElectura);
        btnActivaScanner = (ImageButton) findViewById(R.id.btnActivaScanner);
        btnAnomalia = (ImageButton) findViewById(R.id.btnAnomalia);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
       /* TableLayout tablaMenu = (TableLayout) ((Activity) context).findViewById(R.id.tablaMenu);

        if (tablaMenu != null) {
            // usar tabla
        } else {
            Log.e("DEBUG", "tablaMenu es NULL");
        }*/
        logPrint = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/PRINTER.LOG");
        if (VariablesGlobales.getTipoDeRuta().equals("E")) {
            setTitle(R.string.title_activity_menu_de_Entregas);
            txtElectura.setHint("ENTER ENTREGA");
        } else {
            setTitle(R.string.title_activity_menu_de_liquidacion);
            //btnActivaScanner.setVisibility(View.GONE);
/*
            if (VariablesGlobales.getObligabarras().equals("1"))
                btnActivaScanner.setVisibility(View.VISIBLE);
            else
                btnActivaScanner.setVisibility(View.INVISIBLE);*/

            btnActivaScanner.setImageResource(R.drawable.certificado);

        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//debemos quitar este teclado y dejar el incluido
      /*  mKeyboard = new Keyboard(this, R.xml.keyboard);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);*/


        //------ Fin Teclado Nuevo -----------------
        //fin de todo el proceso del nuevo teclado


        crudComuni = new CrudComunicaciones(this);

        setGlobalKeyboard();
//nuevo para ver lo de el cursor en pantalla

        AlphaAnimation blink = new AlphaAnimation(0.3f, 1.0f);
        blink.setDuration(500);
        blink.setRepeatMode(Animation.REVERSE);
        blink.setRepeatCount(Animation.INFINITE);
        txtElectura.startAnimation(blink);

        //nuevo para quitar el parpadeo
        txtElectura.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    if (txtElectura.getAnimation() != null) {
                        txtElectura.clearAnimation();
                    }
                } else {
                    if (txtElectura.getAnimation() == null) {
                        txtElectura.startAnimation(blink);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        txtElectura.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && txtElectura.getText().toString().isEmpty()) {
                txtElectura.startAnimation(blink);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/
        ImageView btnMenu = findViewById(R.id.imagenmenu);
        btnMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.END);
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle);

        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_anomalia:
                    opcionMenu(1);
                    break;
                case R.id.nav_cuenta:
                    opcionMenu(2);
                    break;
                case R.id.nav_informe:
                    opcionMenu(3);
                    break;
                case R.id.nav_ant_predio:
                    opcionMenu(4);
                    break;
               /*case R.id.nav_modifica_digitos:
                    opcionMenu(5);
                    break;*/
                case R.id.nav_buscar:
                    opcionMenu(5);
                    break;
                case R.id.nav_gps:
                    opcionMenu(6);
                    break;
                case R.id.nav_mapa:
                    opcionMenu(7);
                    break;
                case R.id.nav_enviar_lecturas:
                    opcionMenu(8);
                    break;

                case R.id.nav_procesar_reenvio:
                    opcionMenu(9);
                    break;
                case R.id.nav_no_enviados:
                    opcionMenu(10);
                    break;
                case R.id.nav_faltantes:
                    opcionMenu(11);
                    break;
                case R.id.nav_enviar_fotos:
                    opcionMenu(12);
                    break;
                case R.id.nav_archivos_cierre:
                    opcionMenu(13);
                    break;
                case R.id.nav_estadistica:
                    opcionMenu(14);
                    break;
                case R.id.nav_mensajeria:
                    opcionMenu(15);
                    break;
                case R.id.nav_cambiar_tamano:
                    opcionMenu(16);
                    break;
                case R.id.nav_reimpresion:
                    opcionMenu(17);
                    break;
                case R.id.nav_selimpresora:
                    opcionMenu(18);
                    break;
                case R.id.nav_tecladonumeros:
                    opcionMenu(19);
                    break;
                case R.id.nav_opciones_supervisor:
                    opcionMenu(20);
                    break;

            }

            drawerLayout.closeDrawers();
            return true;
        });

        scroll = (ScrollView) findViewById(R.id.scroll); //Ax: vista para cambiar el tamaño del scroll

        rs = this.getResources();
        tablaResumen = (TableLayout) findViewById(R.id.tablaResumen);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutCampo = new TableRow.LayoutParams(280, TableRow.LayoutParams.WRAP_CONTENT);
        layoutValor = new TableRow.LayoutParams(1150, TableRow.LayoutParams.WRAP_CONTENT);// 650 valor antiguo 795 fin

        btnFotografia = (ImageButton) findViewById(R.id.btnFotografia);
        txtMedidorCuenta = (TextView) findViewById(R.id.txtMedidorCuenta);
        lblInfoMedida = (TextView) findViewById(R.id.lblInfoMedida);
        lblpuntero = (TextView) findViewById(R.id.lblpuntero);
        txtLatitud = (TextView) findViewById(R.id.txtLatitud);
        txtLongitud = (TextView) findViewById(R.id.txtLongitud);
        lblInfoMedida.setFocusable(false);
        lblpuntero.setFocusable(false);
        txtMedidorCuenta.setFocusable(false);

        imagenChat = (ImageView) findViewById(R.id.imagenChat);
        imagenLiquid_1 = (ImageView) findViewById(R.id.imagenLiquid_1);
        imagenLiquid_2 = (ImageView) findViewById(R.id.imagenLiquid_2);
        imagenLiquid_3 = (ImageView) findViewById(R.id.imagenLiquid_3);

        imagenPrinter = (ImageView) findViewById(R.id.imagenPrinter);
        imagenRed = (ImageView) findViewById(R.id.imagenRed);

        btnAdelante = (ImageButton) findViewById(R.id.btnAdelante);
        btnAtras = (ImageButton) findViewById(R.id.btnAtras);
        btnPrimero = (ImageButton) findViewById(R.id.btnPrimero);
        btnUltimo = (ImageButton) findViewById(R.id.btnUltimo);
        // Ax: por defecto apagados; solo el supervisor los habilita desde OpcionesDeSupervisor



        imagenRed.setImageResource(R.drawable.redinativa); //Ax: colocar el icono de red inactiva
        Bundle bundle = getIntent().getExtras();
        wsoap.delegate = this;//Axx Ax: tarea asincrona para enviar datos aqui a processFinish

        try {
            filaseleccionada = parseStringToInteger(bundle.getString("fila")); //Ax: La fila que tal vez fue seleccionada en resumen, si no, es -1
        } catch (Exception ee) {

        }
        if (filaseleccionada > 0) {
            VariablesGlobales.registroactual = filaseleccionada;
        }

        variables.setGlobaloperario(bundle.getString("operario"));
        lector = bundle.getString("operario");
        terminalImei = bundle.getString("terminal");
        impresora = bundle.getString("impresora");
        VariablesGlobales.directorioactual = bundle.getString("path");

        nivelOperador = bundle.getString("nivelOperador");//kim
        s_foto = "0";
        if (nivelOperador != null && nivelOperador.equals("S")) {
            esSupervisor = true;
        }

        // logfile = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG");
        serialPDA = terminalImei;// getSerialNumber();
        if (serialPDA == null || lector == null) {
            Log.e("Tag null ", "Entra a es nulo");
            esNulo();
        }
        cargarMarcaImpresoraGuardada();

        if (!terminalImei.trim().equals("351007492166830") && !terminalImei.trim().equals("358767141019390")
                && !terminalImei.trim().equals("868995078034628")) {

            btnAdelante.setEnabled(navegacionRegistrosHabilitada);
            btnAtras.setEnabled(navegacionRegistrosHabilitada);
            btnAdelante.setVisibility(View.INVISIBLE);
            btnAtras.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnAdelante.setEnabled(true);
            btnAtras.setEnabled(true);
            btnAdelante.setVisibility(View.VISIBLE);
            btnAtras.setVisibility(View.VISIBLE);
        }

        if (!cargarArchivoNombre()) {
            mensajeT("Problema con la carga de Archivo: Nombre", msgLargo);
            return;
        }
        validarCodigoUsuario();
        aplicarFondoPorNit();
        if (VariablesGlobales.getTipoDeRuta().substring(0, 1).equals("L"))
            getSupportActionBar().setTitle("OKINNOM.READINGS-" + Empresa+" "+VariablesGlobales.versionApp);
        else
            getSupportActionBar().setTitle("OKINNOM.ENTREGAS-" + Empresa+VariablesGlobales.versionApp);

        ObtenerDirectorioBackup();

        if (CargarArchivoCarga())
            mensajeT("No hay Archivo de Soporte...\n //ARCHIVOSLCARGA.CFI", msgLargo);

        if (getParamsWs()) {
            // Determinar módulo de trabajo
            String moduloTrab = VariablesGlobales.moduloTrabajo;

            // Determinar tipo de proceso
            String tipoProceso = VariablesGlobales.getTipoDeRuta(); // "L" o "E"

            // Iniciar servicio con configuración completa
            // DESPUÉS — con cod_operador parseado desde 'lector'
            int codOperadorInt = 0;
            try {
                codOperadorInt = Integer.parseInt(lector.trim());
            } catch (NumberFormatException e) {
                Log.w("MenuDeLiquidacion", "cod_operador no es numérico: " + lector);
            }

            LecturaSyncService.start(
                    this,
                    cadenaURLapi,
                    moduloTrab,
                    rutaAdministrador,
                    CicloReal,
                    tipoProceso,
                    codOperadorInt   // ← cod_operador del lector logueado
            );
        } else {
            // Fallback: iniciar sin configuración (usará valores de SharedPreferences)
            //LecturaSyncService.start(this);
            mensajeT("API sin configurar. Revise Configuracion IP", msgLargo);
        }
        /*if (!seleccionUrl()) {
            mensajeT("NO hay IP configurada! Realice ajustes", msgLargo);
        }*/

        menuDeLiquidacionLoad();
        botonesPantalla();
        // imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       /* txtElectura.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyboard(v);
            }
        });*/


        scroll.getLayoutParams().height = TamannoPantalla;//Para Telefonos pequeños780
        scroll.requestLayout();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // mensajeT("GPS No esta activo", msgLargo);
        }
        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new UsarGPS();
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mlocListener);

        if (!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mensajeT("GPS No esta activo", msgLargo);
            encenderEstadoActualGPS = 0;
        }

        imagenRed.setImageResource(R.drawable.redactiva);
        imagenChat.setImageResource(R.drawable.chat_msg_no);
        txtElectura.setFilters(new InputFilter[]{new InputFilter.LengthFilter(VariablesGlobales.intcontroltexto)});
//        tryAll();

        //Runnable myRunnableThread = new Reloj3();
        //handlerTask = new Thread(myRunnableThread);
        //handlerTask.start();

        logger.info(VariablesGlobales.versionApp);

        File directoriof = new File(Environment.getExternalStorageDirectory() + "/DCIM/FOTOGRAFIASL");
        if (!directoriof.exists()) {
            directoriof.mkdir();
        }
        // ===== INICIAR SERVICIO DE SINCRONIZACIÓN =====
        try {
            // LecturaSyncService.start(this);
            registrarSyncBroadcastReceiver();
            Log.i("MenuDeLiquidacion", "Servicio de sincronización iniciado");
        } catch (Exception e) {
            Log.e("MenuDeLiquidacion", "Error iniciando servicio sync: " + e.getMessage());
            logger.error("MenuDeLiquidacion | Error iniciando servicio sync: " + e.getMessage());
        }

        guardarTamanoPantalla(TamannoPantalla, 0);
        leerTamanoPantalla();

    }// endonCreate

    public void guardarTamanoPantalla(int tamano, int modificar) {//720  1

        try {
            File file = new File(VariablesGlobales.directorioactual + "/PANTALLA.CFG");

            // Crear archivo si no existe
            if (modificar == 1) {
                if (file.exists())
                    file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
                // Sobrescribe el contenido (borra lo anterior)
                FileWriter writer = new FileWriter(file, false);
                writer.write(String.valueOf(tamano));
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int leerTamanoPantalla() {

        int tamano = 0;

        try {
            File file = new File(VariablesGlobales.directorioactual + "/PANTALLA.CFG");
            if (!file.exists()) {
                TamannoPantalla = 720;
                scroll.getLayoutParams().height = TamannoPantalla;//Para Telefonos pequeños780
                scroll.requestLayout();

                return 720; // valor por defecto
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String linea = reader.readLine();

            if (linea != null && !linea.isEmpty()) {
                tamano = Integer.parseInt(linea.trim());
                TamannoPantalla = tamano;
            }

            reader.close();
            scroll.getLayoutParams().height = TamannoPantalla;//Para Telefonos pequeños780
            scroll.requestLayout();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tamano;
    }

    public void aplicarFondoPorNit() {

        String nit = "9995";// variables.getCodigoEmpresa();
        int fondo;
        switch (nit) {
            case "9995"://veritas
                Empresa = " INELMA ";
                fondo = R.color.purplePopHard;

                break;

            case "9991"://hesego
                fondo = R.color.greenPopHard;
                Empresa = " INELMA ";
                break;
            case "9998": //global aca colombia  901288398
                fondo = R.color.grayBlueSoft;
                Empresa = " INELMA ";
                break;
            case "1257"://global gss
                fondo = R.color.bluePopHard;
                Empresa = " GLOBALGSS ";
                break;
            default:
                fondo = R.color.orangeCelsia;
                Empresa = " INELMA-GS&S.sas";
                break;
        }

        // tablaMenu.setBackgroundResource(fondo);


    }

    public void botonesPantalla() {
        try {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            btnActivaScanner.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                 //si es entregas hace esto pero si es lecturas el informe
                    if (VariablesGlobales.getTipoDeRuta().equals("E")) {
                        startBarCodeReader();
                    }
                    else
                    {
                        if (autorizacion()) {
                            FiltroCero=1;
                            capturarAnomaliaTerreno(false, false, true);
                            FiltroCero=0;
                        }
                    }

                }
            });

            btnAnomalia.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (autorizacion()) {
                        capturarAnomaliaTerreno(false, false, true);
                    }
                }
            });


            btnBuscar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ejecutarBusquedaCliente();
                }
            });

            btnFotografia.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FotoObligatoriaXLectura = 0;
                    YaTomoFotoCliente = 0;
                    if (ControlEspecialZonaRoja.trim().equals("000")) {
                        capturaImagenFotografica();
                    } else {
                        mensajeT("✓ " + " registros Marcado:\n" +
                                "  📄 " + "en proceso de lectura" + " \n" +
                                "  📷 " + "en formato papel" + " \n" +
                                "  🆕 " + " No se requiere fotoggrafias\n\n" +
                                "Comunicarse con el supervisor.", msgLargo);
                    }
                }
            });


            imagenChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if ((infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") ||
                            infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P") ||
                            infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")) || infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                        MensajeriaCliente();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("DIRECTORIOFULL", VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/MENSAJES.SDA");
                        Intent i = new Intent(MenuDeLiquidacion.this, com.gstolima.modulochat.Chat.class);
                        i.putExtras(bundle);
                        startActivityForResult(i, CHAT_REQUEST_CODE);
                    }
                }
            });
            imagenPrinter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    seleccionarImpresoraNuevoModelo();
                }
            });
            imagenPrinter.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    olvidarImpresoraNuevoModelo();
                    return true;
                }
            });
            btnAdelante.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    txtElectura.setText("");
                    abrirArchivosDeFacturacion();
                    if (avanzarRegistro() == 0) {
                        infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
                    }
                    visualizarInformacionCliente(0);
                    if (!VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: para agilizar la 'entrega' se coloca un numero en lectura para que  procese de una vez, ahorrar un paso
                        if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") ||
                                infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P") ||
                                infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")) {
                            imagenChat.setImageResource(R.drawable.chat_msg_si);
//                            MensajeriaCliente();
                        }
                        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
                            imagenChat.setImageResource(R.drawable.verificar);
                        }
                    }
                    cerrarArchivosFacturacion();
                    variables.impresora = "   ";
                }
            });


            btnAtras.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    txtElectura.setText("");
                    abrirArchivosDeFacturacion();
                    retrocedeRegistro();
                    visualizarInformacionCliente(0);
                    cerrarArchivosFacturacion();
                    if (!VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: para agilizar la 'entrega' se coloca un numero en lectura para que  procese de una vez, ahorrar un paso
                        if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J") ||
                                infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") ||
                                infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P")) {
                            imagenChat.setImageResource(R.drawable.chat_msg_si);
                            //  MensajeriaCliente();
                        }
                        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
                            imagenChat.setImageResource(R.drawable.verificar);
                        }
                    }
                    variables.impresora = "   ";
                }
            });


            btnPrimero.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    txtElectura.setText("");
                    abrirArchivosDeFacturacion();
                    irPrimerRegistro();
                    visualizarInformacionCliente(0);
                    cerrarArchivosFacturacion();
                    variables.impresora = "   ";
                }
            });

            btnUltimo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    txtElectura.setText("");
                    abrirArchivosDeFacturacion();
                    irUltimoRegistro();
                    visualizarInformacionCliente(0);
                    cerrarArchivosFacturacion();
                    variables.impresora = "   ";
                }
            });

            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posCursor = txtElectura.length();
                    if (posCursor > 0) {
                        txtElectura.setText(txtElectura.getText().delete(posCursor - 1, posCursor));
                        code.setLength(txtElectura.length());
//                    txtLecturaActual.setSelection(posCursor - 1);
                    }
                }
            });

            Enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtLecturaActualKeyPress();

                    /*if (conttryall < 3) {
                        conttryall++;
                        tryAll();//activar codigo
                    }*/
                    code.setLength(0);
                }
            });

        } catch (Exception ex) {
            //utils.Log(logfile,"[MenuDeLiquidacion]botonesPantalla()|ERROR| " + ex.getMessage());
            Log.e("Error Pantalla", "[MenuDeLiquidacion]botonesPantalla()|ERROR| " + String.valueOf(ex));
        }
    }

    /**
     * INICIO FUNCIONES DEL TECLADO NUEVO
     **/

    @Override
    public void onClick(View view) {
        String cadena = sparseArray.get(view.getId());
        code.append(cadena);
        txtElectura.setText(code);
    }

    public void setGlobalKeyboard() {
        try {

            //------ Teclado Nuevo ------
            Boton0 = (CardView) findViewById(R.id.Boton0);
            Boton1 = (CardView) findViewById(R.id.Boton1);
            Boton2 = (CardView) findViewById(R.id.Boton2);
            Boton3 = (CardView) findViewById(R.id.Boton3);
            Boton4 = (CardView) findViewById(R.id.Boton4);
            Boton5 = (CardView) findViewById(R.id.Boton5);
            Boton6 = (CardView) findViewById(R.id.Boton6);
            Boton7 = (CardView) findViewById(R.id.Boton7);
            Boton8 = (CardView) findViewById(R.id.Boton8);
            Boton9 = (CardView) findViewById(R.id.Boton9);
            Delete = (ImageView) findViewById(R.id.delete);
            Enter = (TextView) findViewById(R.id.Btn_ok);

            Boton0.setOnClickListener((OnClickListener) this);
            Boton1.setOnClickListener((OnClickListener) this);
            Boton2.setOnClickListener((OnClickListener) this);
            Boton3.setOnClickListener((OnClickListener) this);
            Boton4.setOnClickListener((OnClickListener) this);
            Boton5.setOnClickListener((OnClickListener) this);
            Boton6.setOnClickListener((OnClickListener) this);
            Boton7.setOnClickListener((OnClickListener) this);
            Boton8.setOnClickListener((OnClickListener) this);
            Boton9.setOnClickListener((OnClickListener) this);
            Delete.setOnClickListener((OnClickListener) this);

            sparseArray.put(R.id.Boton0, "0");
            sparseArray.put(R.id.Boton1, "1");
            sparseArray.put(R.id.Boton2, "2");
            sparseArray.put(R.id.Boton3, "3");
            sparseArray.put(R.id.Boton4, "4");
            sparseArray.put(R.id.Boton5, "5");
            sparseArray.put(R.id.Boton6, "6");
            sparseArray.put(R.id.Boton7, "7");
            sparseArray.put(R.id.Boton8, "8");
            sparseArray.put(R.id.Boton9, "9");


            //------ Fin Teclado Nuevo ---------------------------------
        } catch (Exception ex) {
//                utils.Log(logfile,"[MenuDeLiquidacion]setGlobalKeyboard()|ERROR| " + ex.getMessage());
        }
    }

    /**
     * FIN FUNCIONES DEL TECLADO NUEVO
     **/

    public void openKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null) {

            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {

            playSound(primaryCode);
            switch (primaryCode) {

                case -1:
                    int posCursor = txtElectura.getSelectionStart();
                    if (posCursor > 0) {
                        txtElectura.setText(txtElectura.getText().delete(posCursor - 1, posCursor));
                        txtElectura.setSelection(posCursor - 1);
                    }
                    break;
                case 100: //Ax: Enter
                    txtLecturaActualKeyPress();

                    /*if (conttryall < 3) {
                        conttryall++;
                        tryAll();//activar codigo
                    }*/
                    break;
                default:
                    if (medidorCero) {
                        txtLecturaActualKeyPress();
                    } else {
                        int posCurs0r = txtElectura.getSelectionStart();
                        if (posCurs0r != txtElectura.length()) {
                            txtElectura.getText().insert(txtElectura.getSelectionStart(), "" + primaryCode);
                            txtElectura.setSelection(posCurs0r + 1);
                        } else {
                            txtElectura.setText(txtElectura.getText().toString() + primaryCode);
                            txtElectura.setSelection(txtElectura.length());//Ax: Coloca el cursor al final
                        }
                    }
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

    public void esNulo() {
        try {

            BDifNull bDifNull = new BDifNull(Realm.getDefaultInstance());
            CrudifNull crudifNull = bDifNull.obtenerdatabyid(1);
            filaseleccionada = Integer.parseInt(crudifNull.getFila());
            if (filaseleccionada > 0) {
                VariablesGlobales.registroactual = filaseleccionada;
            }
            variables.setGlobaloperario(crudifNull.getOperario());
            lector = crudifNull.getOperario();
            terminalImei = crudifNull.getImei().trim();
            impresora = crudifNull.getImpresora();
            VariablesGlobales.directorioactual = crudifNull.getPath();
            nivelOperador = crudifNull.getNivelOperador();
            serialPDA = crudifNull.getImei().trim();
            Log.e("Error bd", "fila " + filaseleccionada);
            Log.e("Error bd", "operario " + lector);
            Log.e("Error bd", "terminal " + terminalImei);
            Log.e("Error bd", "impresora " + impresora);
            Log.e("Error bd", "path " + VariablesGlobales.directorioactual);
            Log.e("Error bd", "nivelOperador " + nivelOperador);


        } catch (Exception e) {
            Log.e("Error al nulo", String.valueOf(e));
        }

        //finish();

    }

    private void playSound(int keyCode) {

        audiom = (AudioManager) getSystemService(AUDIO_SERVICE);

        switch (keyCode) {

            case -1:
                audiom.playSoundEffect(AudioManager.FX_FOCUS_NAVIGATION_UP, 1f);
                break;
            case 100:
                audiom.playSoundEffect(AudioManager.FX_KEYPRESS_INVALID, 1f);
                break;
            default:
                audiom.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 1f);
        }
    }

    @Override
    public void onBackPressed() {

        try {

        } catch (Exception ex) {
            salirse = true;
        }
        finish();
    }

    //Ax: trata de obtener la carpeta de Backup en otra SD, sino en otras rutas
    private void ObtenerDirectorioBackup() {
        try {
            String carpetas = NombreArchivos;

            if (carpetas.trim().length() == 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_HH_mm_ss");
                carpetas = simpleDateFormat.format(new Date());
            }

            VariablesGlobales.directorioBackUp = VariablesGlobales.directorioactual + "/DCIM/SIMFABACKUP/" + carpetas + "/";

            File file = new File(VariablesGlobales.directorioBackUp);
            if (!file.exists()) file.mkdirs();

            File filetest = new File(VariablesGlobales.directorioBackUp + "FILETEST.SDA");

            if (!filetest.exists()) {
                if (!utils.EscribirLinea(filetest, carpetas)) { //Ax: si falla la escritura el backup cambia  TODO: usar clase que escribe en SD
                    VariablesGlobales.directorioBackUp = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BACKUP/";
                    new File(VariablesGlobales.directorioBackUp).mkdirs();
                }
            }
            metodo = "comprobarBorradoBackup";
            TaskHelper.execute(new AsyncCallWS(), "comprobarBorradoBackup");
        } catch (Exception e) {
            VariablesGlobales.directorioBackUp = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BACKUP/";

        }
    }

    private void comprobarBorradoBackup() { //Trata de borrar backup de 3 dias atras
        try {
            File directory = new File(VariablesGlobales.directorioBackUp.substring(0, VariablesGlobales.directorioBackUp.lastIndexOf("BACKUP") + 6));

            if (!directory.exists()) return;

            File[] files = directory.listFiles();
            Calendar time = Calendar.getInstance();
            time.add(Calendar.DAY_OF_YEAR, -2);

            for (File file : files) {

                if (file.isDirectory()) {
                    File[] files2 = file.listFiles();
                    if (files2.length == 0) {
                        file.delete();
                    } else {
                        borradoBackups(time, files2);
                    }
                } else {
                    Date lastModified = new Date(file.lastModified());
                    if (lastModified.before(time.getTime())) {
                        file.delete();
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("comprobarBorradoBackup(): " + ex.getMessage());
        }
    }

    private void borradoBackups(Calendar time, File[] files) {
        for (File file : files) {
            Date lastModified = new Date(file.lastModified());//            Date x=time.getTime();            x=x;
            if (lastModified.before(time.getTime())) {
                file.delete();
            }
        }
    }

    public void startBarCodeReader() {

        CameraManager CamManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String[] cameraId = null;//[0]
        try {
            cameraId = CamManager.getCameraIdList();

            CamManager.setTorchMode(String.valueOf(cameraId), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanEntregas = false;
        try {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);//ONE_D_CODE_TYPES
            integrator.setPrompt("CAPTURAR CODIGO DE BARRAS");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(false);
            integrator.initiateScan();
        } catch (Exception ex) {
            logger.info("startBarCodeReader() " + ex.getMessage());
        }
    }

    private void menuDeLiquidacionLoad() {

        tiempoInactivolaImpresora = 0;

        try {
            File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/NOMBRE");
            tablaComentarios.setarchivo_Comenta(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/OBSERVA.TXT");
            RandomAccessFile rFile = new RandomAccessFile(file, "r");
            byte[] byteArray;
            int fileSize = (int) rFile.length();
            byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            rutaCargadaPDA = new String(byteArray);
            rutaCargadaPDA = rutaCargadaPDA.substring(10, 20).trim();
            String archivoL = rutaCargadaPDA;
            variables.setNombregeneral(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "E" + archivoL);
            rFile.close();

        } catch (Exception eX) {
            eX.printStackTrace();
        }

        VariablesGlobales.setActivarcamarafotografica(1);
        abrirArchivosPrincipales();
        if (!VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: para agilizar la 'entrega' se coloca un numero en lectura para que  procese de una vez, ahorrar un paso
            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") ||
                    infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P") ||
                    infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")
            ) {
                imagenChat.setImageResource(R.drawable.chat_msg_si);
                // MensajeriaCliente();
            }
            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
                imagenChat.setImageResource(R.drawable.verificar);
                MensajeriaCliente();
            }
        }
        cambiarValoresimpresion();
    }

    /*private void envioFacturacionHilos() {
        try {
            Log.e("errorCN", "envioFacturacionHilos " + estaEnvioAlWsOcupado);
            if (!estaEnvioAlWsOcupado) {

                File file_nuevoEnviosGprsda = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA");
                File file_EnviosGprsda = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA");
                File cuentasEnvio = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "CUENTASNUEVA" + serialPDA + ".SDA");

                if (!file_nuevoEnviosGprsda.exists()) {
                    if (file_EnviosGprsda.exists()) {
                        //file_EnviosGprsda.renameTo(file_nuevoEnviosGprsda);//Ax: aqui  borra file_EnviosGprsda
                        boolean renamed = file_EnviosGprsda.renameTo(file_nuevoEnviosGprsda);
                        if (!renamed) {
                            copiarArchivo(file_EnviosGprsda, file_nuevoEnviosGprsda);
                            file_EnviosGprsda.delete();
                        }
                    } else {
                        //if (!cuentasEnvio.exists())
                        return;
                    }
                } else {
                    if (file_EnviosGprsda.exists())
                        GuardarEnvioHilos();
                }
                try {
                    Log.e("errorCN", "bandera " + estaEnvioAlWsOcupado);
                    if (!estaEnvioAlWsOcupado) {
                        estaEnvioAlWsOcupadoTrue();

                        if (procesandoenvioenHilos_chat) {
                            taskChat.cancel(true);
                        }
                        TaskHelper.execute(new AsyncCallWS(), "ENVIARFACTURACIONCOLECCION");
                    }
                } catch (Exception exc) {
                    estaEnvioAlWsOcupado = false;
                    //logger.info("error a enviar 1.1; " + exc.getMessage());
                    if (exc.getMessage() != null && !exc.getMessage().contains("Can't toast on a") && !exc.getMessage().contains("Can't create handler inside")) {
                        // Solo se loguea si el error NO contiene "Can't toast on a"
                        logger.info("GuardarEnvioHilos; " + exc.getMessage());


                    } else {
                        // Ignorar o loguear de forma controlada
                        Log.w("APP_LOG", "error a enviar 1.1 - Se evitó el error del Toast en hilo secundario");
                    }
                }
            }
        } catch (Exception exc1) {
            //logger.info("error a enviar 1.2; " + exc1.getMessage());
            if (exc1.getMessage() != null && !exc1.getMessage().contains("Can't toast on a") && !exc1.getMessage().contains("Can't create handler inside")) {
                // Solo se loguea si el error NO contiene "Can't toast on a"
                logger.info("GuardarEnvioHilos; " + exc1.getMessage());


            } else {
                // Ignorar o loguear de forma controlada
                Log.w("APP_LOG", "error a enviar 1.2 - Se evitó el error del Toast en hilo secundario");
            }
        }
    }

    private void GuardarEnvioHilos() { //Ax: comprobar si escribe bien
        String envioGPRSerial = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA"; //Ax: si tiene datos,escribe ultima linea
        String envioGPRS = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA";//Ax. lo que ahy aqui lo escribe en el de arriba

        File file_envioGPRS = new File(envioGPRS);

        try {
            if (!utils.CrearCopiaSin(envioGPRS, envioGPRSerial)) { //sin sobreescribir
                mensajeT("No se creo copia de envioGPRSerial", msgCorto);//To do trow
            } else
                file_envioGPRS.delete();
        } catch (Exception e) {
            if (e.getMessage() != null && !e.getMessage().contains("Can't toast on a") && !e.getMessage().contains("Can't create handler inside")) {
                // Solo se loguea si el error NO contiene "Can't toast on a"
                logger.info("GuardarEnvioHilos; " + e.getMessage());
                mensajeT("Error en GuardarEnvioHilos " + e.getMessage(), msgMedio);

            } else {
                // Ignorar o loguear de forma controlada
                Log.w("APP_LOG", "Se evitó el error del Toast en hilo secundario");
            }


            return;
        }
    }*/

    /*
        public void CrearArchivoEnvio_2(String ArchivoNombre) {
            try {
                //funsion que parte enviosgrrs serial y va enviandopor partes
                //nuevo por victor tratar que una linea duplivada no se envie al servidor
                String UltimaLinaGuardada = "";
                File Archivo = new File(ArchivoNombre);

                if (!Archivo.exists()) {
                    mensajeT("No existe el archivo de envio", msgMedio);
                }
                File ArchivoEnv = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOPART" + serialPDA + ".SDA");
                if (!ArchivoEnv.exists()) {
                    ArchivoEnv.createNewFile();
                }
                File CopiaGPRS = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/COPIAGPRS" + serialPDA + ".SDA");
                if (!CopiaGPRS.exists()) {
                    CopiaGPRS.createNewFile();
                }

                FileReader stream3 = new FileReader(ArchivoNombre);
                BufferedReader reader = new BufferedReader(stream3);
                String linea = "";
                String dato;
                int cont = 0;
                int maximoregs = parseStringToInteger(variables.maximoregaenviar.trim());
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ArchivoEnv));
                OutputStreamWriter osw1 = new OutputStreamWriter(new FileOutputStream(CopiaGPRS));

                while ((linea = reader.readLine()) != null) {
                    if (!UltimaLinaGuardada.equals(linea)) {
                        UltimaLinaGuardada = linea;
                        dato = linea + "\r\n";

                        cont++;
                        if (cont <= maximoregs) {
                            osw.write(dato);
                        }
                        if (cont > maximoregs) {
                            osw1.write(dato);
                        }
                    }
                }
                osw.flush();
                osw.close();
                osw1.flush();
                osw1.close();
                reader.close();

                if (Archivo.exists()) {
                    Archivo.delete();
                    File Original = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA");
                    ArchivoEnv.renameTo(Original);
                }
            } catch (Exception e) {
                logger.info("CrearArchivoEnvio():" + e);
                Log.e("Tag", "CrearArchivoEnvio|Error: " + e);
            }
        }
    */
    /*public void CrearArchivoEnvio(String archivoOrigen) {

        final int TAM_ESPERADO_1 = 519;
        final int TAM_ESPERADO_2 = 521;
        final int CAMPOS_ESPERADOS = 48; // AJUSTA ESTE VALOR

        File origen = new File(archivoOrigen);
        if (!origen.exists()) {
            mensajeT("No existe el archivo de envío " + archivoOrigen, msgMedio);
            return;
        }

        try {
            int maxRegs = parseStringToInteger(variables.maximoregaenviar.trim());

            File archivoEnv = new File(VariablesGlobales.directorioactual +
                    VariablesGlobales.getCarpetaLecturas()+"/ENVIOPART" + serialPDA + ".SDA");

            File copiaGprs = new File(VariablesGlobales.directorioactual +
                    VariablesGlobales.getCarpetaLecturas()+"/COPIAGPRS" + serialPDA + ".SDA");

            File copiaXXX = new File(VariablesGlobales.directorioactual +
                    VariablesGlobales.getCarpetaLecturas()+"/ARCHIVO_MALO.SDA");

            BufferedReader br = new BufferedReader(new FileReader(origen));
            BufferedWriter bwEnv = new BufferedWriter(new FileWriter(archivoEnv, false));
            BufferedWriter bwGprs = new BufferedWriter(new FileWriter(copiaGprs, false));
            BufferedWriter bwXXX = new BufferedWriter(new FileWriter(copiaXXX, true));


            String linea;
            int contador = 0;

            while ((linea = br.readLine()) != null) {
                linea = linea.replace('|', ';');
                int len = linea.length();

                // 1️⃣ Validar tamaño
                if (len != TAM_ESPERADO_1 && len != TAM_ESPERADO_2) {

                    bwXXX.write(linea + "\r\n");
                    //bwXXX.newLine();
                    continue;
                }

                // 2️⃣ Validar campos
                String[] campos = linea.split(";", -1);
                if (campos.length != CAMPOS_ESPERADOS) {
                    bwXXX.write(linea + "\r\n");
                    //bwXXX.newLine();
                    continue;
                }

                // 3️⃣ Registro válido
                contador++;
                if (contador <= maxRegs) {
                    bwEnv.write(linea + "\r\n");
                    //bwEnv.newLine();
                } else {
                    bwGprs.write(linea + "\r\n");
                    //bwGprs.newLine();
                }
            }

            br.close();
            bwEnv.close();
            bwGprs.close();
            bwXXX.close();
            // Renombrar a ENVIOGPRS

            // Renombrar a ENVIOGPRS
            if (origen.exists()) {
                origen.delete();
                File destinoFinal = new File(VariablesGlobales.directorioactual +
                        VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA");

                boolean renamed = archivoEnv.renameTo(destinoFinal);

                if (!renamed) {
                    copiarArchivo(archivoEnv, destinoFinal);
                    archivoEnv.delete();
                }

            }
            if (copiaXXX.exists() && copiaXXX.isFile() && copiaXXX.length() == 0) {
                copiaXXX.delete();
            }

        } catch (Exception e) {
            Log.e("CrearArchivoEnvio", "Error procesando archivo", e);
        }
    }*/

    private void copiarArchivo(File origen, File destino) throws IOException {
        InputStream in = new FileInputStream(origen);
        OutputStream out = new FileOutputStream(destino);

        byte[] buffer = new byte[11264];//8192
        int bytes;

        while ((bytes = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytes);
        }

        in.close();
        out.close();
    }
    //*** ENVIOS AL SERVER POR VICTOR
    /**
     * VERSIÓN REFACTORIZADA - EnviarAlServidorFacturacion
     * <p>
     * MEJORAS IMPLEMENTADAS:
     * 1. Eliminada duplicación de código de restauración
     * 2. Manejo robusto de archivos con validaciones
     * 3. Control de reintentos con límites claros
     * 4. Logging mejorado para debugging
     * 5. Separación de responsabilidades en métodos auxiliares
     */

// ============================================================================
// CONSTANTES DE CONFIGURACIÓN
// ============================================================================
    private static final int MAX_REINTENTOS_POR_FALTA_CONEXION = 3;
    private static final int MAX_REINTENTOS_ENVIO_BLOQUE = 10; // Para envío por bloques
    private static final String ARCHIVO_ENVIO_TEMPLATE = VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS%s.SDA";
    private static final String ARCHIVO_COPIA_TEMPLATE = VariablesGlobales.getCarpetaLecturas() + "/COPIAGPRS%s.SDA";
    private static final String ARCHIVO_CUENTAS_TEMPLATE = VariablesGlobales.getCarpetaLecturas() + "/CUENTASNUEVA%s.SDA";

    // Variables de control de estado
    private int contadorReintentosConexion = 0;
    private int contadorEnviosBloques = 0;

    // ============================================================================
// MÉTODO PRINCIPAL REFACTORIZADO
// ============================================================================
    /*private void EnviarAlServidorFacturacion() throws IOException {

        Log.i("ENVIO", "=== INICIO EnviarAlServidorFacturacion ===");

        // 1. INICIALIZAR RUTAS DE ARCHIVOS
        String archivoEnvio = VariablesGlobales.directorioactual + String.format(ARCHIVO_ENVIO_TEMPLATE, serialPDA);
        String archivoCopia = VariablesGlobales.directorioactual + String.format(ARCHIVO_COPIA_TEMPLATE, serialPDA);
        String archivoCuentasNuevas = VariablesGlobales.directorioactual + String.format(ARCHIVO_CUENTAS_TEMPLATE, serialPDA);

        MisEnviosHilos.archivo_EnvioGPS = archivoEnvio;

        try {
            VerIniciarEnvio();

            // 2. VALIDAR SI HAY DATOS PARA ENVIAR
            if (!validarArchivoParaEnvio(archivoEnvio)) {
                Log.i("ENVIO", "No hay datos para enviar o archivo vacío");

                FinalVerIniciarEnvio();
                return;
            }
            int totalRegistros = MisEnviosHilos.total_EnvioGPS;
            int maximoRegistros = parseStringToInteger_ENVIO(variables.maximoregaenviar.trim());
            if (totalRegistros > maximoRegistros) {
                // 3. PREPARAR ENVÍO (Dividir en bloques si es necesario)
                if (EnviarxBloque == 1) {
                    prepararEnvioPorBloques(archivoEnvio, archivoCopia);
                }
            }
            // 4. EJECUTAR ENVÍO
            boolean envioExitoso = ejecutarEnvio(archivoEnvio, archivoCopia, archivoCuentasNuevas);

            // 5. PROCESAR RESULTADO
            procesarResultadoEnvio(envioExitoso, archivoEnvio, archivoCopia, archivoCuentasNuevas);

        } catch (Exception ex) {
            Log.e("ERROR", "[EnviarAlServidorFacturacion] Error general: " + ex.getMessage());
            logger.info("[EnviarAlServidorFacturacion] Error: " + ex.getMessage());

            // RESTAURAR ARCHIVOS EN CASO DE ERROR CRÍTICO
            restaurarArchivosCasoError(archivoEnvio, archivoCopia);

        } finally {
            // SIEMPRE CERRAR RECURSOS
            cerrarRecursos();
            Log.i("ENVIO", "=== FIN EnviarAlServidorFacturacion ===");
            FinalVerIniciarEnvio();
        }
    }*/

    public void VerIniciarEnvio() {
        runOnUiThread(() -> {
            ProgressBar img = findViewById(R.id.progressBar_cyclic);
            img.setVisibility(View.VISIBLE);
        });
    }

    public void FinalVerIniciarEnvio() {
        runOnUiThread(() -> {
            ProgressBar img = findViewById(R.id.progressBar_cyclic);
            img.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    this,
                    "Envío finalizado correctamente",
                    Toast.LENGTH_SHORT
            ).show();
        });

    }


// ============================================================================
// MÉTODOS AUXILIARES - VALIDACIÓN Y PREPARACIÓN
// ============================================================================

    /**
     * Valida si el archivo existe y tiene datos para enviar
     */
    private boolean validarArchivoParaEnvio(String archivoEnvio) {
        if (!MisEnviosHilos.abrir_EnvioGPS(archivoEnvio)) {
            Log.w("ENVIO", "No se pudo abrir archivo de envío: " + archivoEnvio);
            return false;
        }

        int totalRegistros = MisEnviosHilos.total_EnvioGPS;
        Log.i("ENVIO", "Total registros a enviar: " + totalRegistros);

        if (totalRegistros <= 0) {
            MisEnviosHilos.Cerrar_EnvioGPS();
            return false;
        }

        return true;
    }

    /**
     * Prepara el envío por bloques cuando el archivo excede el máximo permitido
     */
    /*private void prepararEnvioPorBloques(String archivoEnvio, String archivoCopia) {

        int maximoRegistros = parseStringToInteger_ENVIO(variables.maximoregaenviar.trim());

        if (maximoRegistros <= 0) {
            Log.w("ENVIO", "maximoregaenviar no configurado, enviando archivo completo");
            return;
        }

        int totalRegistros = MisEnviosHilos.total_EnvioGPS;

        if (totalRegistros <= maximoRegistros) {
            Log.i("ENVIO", "Registros dentro del límite, no se requiere división");
            return;
        }

        // Verificar si ya existe un archivo COPIA (envío anterior interrumpido)
        File fileCopia = new File(archivoCopia);
        File fileEnvio = new File(archivoEnvio);

        if (!fileCopia.exists()) {
            // CREAR DIVISIÓN: Primera vez que se divide el archivo
            Log.i("ENVIO", "Dividiendo archivo. Total: " + totalRegistros + ", Máximo: " + maximoRegistros);

            try {
                CrearArchivoEnvio(archivoEnvio); // Este método debería crear la división
                MisEnviosHilos.archivo_EnvioGPS = archivoEnvio;

                // Calcular número de envíos necesarios
                NroEnvios = (int) Math.ceil((double) totalRegistros / maximoRegistros);
                contadorEnviosBloques = 0;

                Log.i("ENVIO", "Archivo dividido. Número de envíos requeridos: " + NroEnvios);

            } catch (Exception ex) {
                Log.e("ERROR", "Error al crear división de archivo: " + ex.getMessage());
                NroEnvios = 0;
            }

        } else {
            // Ya existe COPIA, continuando envío interrumpido
            Log.i("ENVIO", "Detectado envío interrumpido, continuando desde COPIA existente");

            if (NroEnvios == 0) {
                NroEnvios = (int) Math.ceil((double) totalRegistros / maximoRegistros);
            }
        }
    }*/

// ============================================================================
// MÉTODOS AUXILIARES - EJECUCIÓN DE ENVÍO
// ============================================================================

    /**
     * Ejecuta el envío del archivo al servidor
     *
     * @return true si el envío fue exitoso, false en caso contrario
     */
    private boolean ejecutarEnvio(String archivoEnvio, String archivoCopia, String archivoCuentasNuevas) {
        File nombreZip = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" +
                "Tpl" + (Municipio + Seccion + Division) + ".zip");
        try {
            // Crear ZIP con archivos a enviar


            ArrayList<File> archivosAZipear = new ArrayList<>();
            archivosAZipear.add(new File(archivoEnvio));

            // Agregar archivo de cuentas nuevas si existe
            File fileCuentasNuevas = new File(archivoCuentasNuevas);
            if (fileCuentasNuevas.exists()) {
                archivosAZipear.add(fileCuentasNuevas);
                Log.i("ENVIO", "Incluyendo archivo de cuentas nuevas en el envío");
            }

            // ENVIAR AL SERVIDOR
            String rutaDestino = rutaAdministrador + VariablesGlobales.moduloTrabajo + CicloReal;
            Log.i("ENVIO", "Enviando a: " + rutaDestino);

            String resultado = EnviarArchivo(rutaDestino, nombreZip, archivosAZipear, trama);

            // EVALUAR RESULTADO
            if (resultado.equals("ok")) {
                Log.i("ENVIO", "✓ Envío exitoso");
                contadorReintentosConexion = 0; // Resetear contador de reintentos
                // Limpiar ZIP temporal
                if (nombreZip.exists()) {
                    nombreZip.delete();
                }
                return true;
            } else {
                Log.w("ENVIO", "✗ Error en envío: " + resultado);
                logger.info("EnviarArchivo() resultado: " + resultado);

                // Determinar si es error de conexión
                if (esErrorDeConexion(resultado)) {
                    contadorReintentosConexion++;
                    Log.w("ENVIO", "Error de conexión. Intento " + contadorReintentosConexion +
                            " de " + MAX_REINTENTOS_POR_FALTA_CONEXION);
                }
                // Limpiar ZIP temporal
                if (nombreZip.exists()) {
                    nombreZip.delete();
                }
                return false;
            }

        } catch (Exception ex) {
            Log.e("ERROR", "Excepción durante envío: " + ex.getMessage());
            logger.info("ejecutarEnvio() excepción: " + ex.getMessage());
            contadorReintentosConexion++;
            // Limpiar ZIP temporal
            if (nombreZip.exists()) {
                nombreZip.delete();
            }
            return false;
        }
    }

    /**
     * Determina si el error es por falta de conexión
     */
    private boolean esErrorDeConexion(String mensajeError) {
        if (mensajeError == null)
            return false;

        String m = mensajeError.toLowerCase();

        return m.contains("sin conexion") ||
                m.contains("problemas") ||
                m.contains("timeout") ||
                m.contains("networkexception") ||
                m.contains("error");

    }


// ============================================================================
// MÉTODOS AUXILIARES - PROCESAMIENTO DE RESULTADOS
// ============================================================================

    /**
     * Procesa el resultado del envío (éxito o fallo)
     */
    /*private void procesarResultadoEnvio(boolean envioExitoso, String archivoEnvio,
                                        String archivoCopia, String archivoCuentasNuevas) {

        if (envioExitoso) {
            procesarEnvioExitoso(archivoEnvio, archivoCopia, archivoCuentasNuevas);
        } else {
            procesarEnvioFallido(archivoEnvio, archivoCopia);
        }
    }*/

    /**
     * Procesa un envío exitoso: limpia archivos y continúa con bloques si es necesario
     */
    /*private void procesarEnvioExitoso(String archivoEnvio, String archivoCopia, String archivoCuentasNuevas) {

        Log.i("ENVIO", "Procesando envío exitoso...");

        try {
            // 1. GUARDAR BACKUP del archivo enviado
            GuardarBackupEnvio(archivoEnvio);

            // 2. ELIMINAR archivo de envío ya procesado
            File fileEnvio = new File(archivoEnvio);
            if (fileEnvio.exists()) {
                boolean eliminado = fileEnvio.delete();
                Log.i("ENVIO", "Archivo ENVIOGPRS eliminado: " + eliminado);
            }

            // 3. SI EXISTE COPIA, renombrarla como nuevo ENVIOGPRS
            File fileCopia = new File(archivoCopia);
            if (fileCopia.exists() && !fileEnvio.exists()) {

                boolean renombrado = fileCopia.renameTo(fileEnvio);

                if (!renombrado) {
                    // Si falla el rename, copiar manualmente
                    Log.w("ENVIO", "Rename falló, copiando manualmente");
                    copiarArchivo_ENVIO(fileCopia, fileEnvio);
                    fileCopia.delete();
                }

                Log.i("ENVIO", "COPIAGPRS restaurada como ENVIOGPRS");
            }

            // 4. ELIMINAR archivo de cuentas nuevas
            File fileCuentasNuevas = new File(archivoCuentasNuevas);
            if (fileCuentasNuevas.exists()) {
                fileCuentasNuevas.delete();
                Log.i("ENVIO", "Archivo de cuentas nuevas eliminado");
            }

            // 5. CONTINUAR CON SIGUIENTE BLOQUE si es necesario
            if (EnviarxBloque == 1 && NroEnvios > 0) {
                contadorEnviosBloques++;

                if (contadorEnviosBloques < NroEnvios && contadorEnviosBloques < MAX_REINTENTOS_ENVIO_BLOQUE) {
                    Log.i("ENVIO", "Continuando con bloque " + (contadorEnviosBloques + 1) + " de " + NroEnvios);
                    EnviarAlServidorFacturacion(); // RECURSIÓN CONTROLADA
                } else {
                    Log.i("ENVIO", "Envío por bloques completado o límite alcanzado");
                    resetearContadores();
                }
            } else {
                resetearContadores();
            }

            // Marcar envío exitoso
            enviosGPRScantidad = 1;

        } catch (Exception ex) {
            Log.e("ERROR", "Error procesando envío exitoso: " + ex.getMessage());
            logger.info("procesarEnvioExitoso() error: " + ex.getMessage());
        }
    }*/

    /**
     * Procesa un envío fallido: restaura archivos y decide si reintentar
     */
    /*private void procesarEnvioFallido(String archivoEnvio, String archivoCopia) {

        Log.w("ENVIO", "Procesando envío fallido...");

        try {
            // RESTAURAR: Unir COPIA + ENVIO si existen ambos
            restaurarArchivosDesdeBackup(archivoEnvio, archivoCopia);

            // DECIDIR SI REINTENTAR O SALIR
            if (contadorReintentosConexion >= MAX_REINTENTOS_POR_FALTA_CONEXION) {
                Log.w("ENVIO", "Límite de reintentos alcanzado. Saliendo...");
                enviosGPRScantidad = 5; // Código de error
                resetearContadores();
                return;
            }

            // REINTENTAR si aún hay oportunidades
            if (EnviarxBloque == 1 && contadorEnviosBloques < MAX_REINTENTOS_ENVIO_BLOQUE) {
                Log.i("ENVIO", "Reintentando envío...");
                try {
                    Thread.sleep(2000); // Esperar 2 segundos antes de reintentar
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                EnviarAlServidorFacturacion(); // REINTENTAR
            } else {
                Log.w("ENVIO", "No se reintentará. Esperando próximo llamado del hilo.");
                enviosGPRScantidad = 5;
                resetearContadores();
            }

        } catch (Exception ex) {
            Log.e("ERROR", "Error procesando envío fallido: " + ex.getMessage());
            logger.info("procesarEnvioFallido() error: " + ex.getMessage());
            resetearContadores();
        }
    }*/

// ============================================================================
// MÉTODOS AUXILIARES - MANEJO DE ARCHIVOS
// ============================================================================

    /**
     * Restaura el archivo ENVIOGPRS desde COPIAGPRS cuando hay error
     * CONSOLIDA los registros que estaban en ambos archivos
     */
    private void restaurarArchivosDesdeBackup(String archivoEnvio, String archivoCopia) {

        File fileCopia = new File(archivoCopia);
        File fileEnvio = new File(archivoEnvio);

        if (!fileCopia.exists()) {
            Log.i("ENVIO", "No hay archivo COPIA para restaurar");
            return;
        }

        Log.i("ENVIO", "Restaurando archivos desde backup...");

        try {
            // Si ENVIOGPRS existe, ANEXAR COPIA al final (modo append)
            if (fileEnvio.exists()) {
                anexarArchivo(fileCopia, fileEnvio);
                Log.i("ENVIO", "Archivo COPIA anexado a ENVIOGPRS");
            } else {
                // Si no existe ENVIOGPRS, simplemente renombrar COPIA
                boolean renombrado = fileCopia.renameTo(fileEnvio);
                if (!renombrado) {
                    copiarArchivo(fileCopia, fileEnvio);
                }
                Log.i("ENVIO", "Archivo COPIA renombrado a ENVIOGPRS");
            }

            // ELIMINAR COPIA después de restaurar
            if (fileCopia.exists()) {
                fileCopia.delete();
            }

        } catch (Exception ex) {
            Log.e("ERROR", "Error restaurando archivos: " + ex.getMessage());
            logger.info("restaurarArchivosDesdeBackup() error: " + ex.getMessage());
        }
    }

    /**
     * Anexa el contenido de un archivo fuente al final de un archivo destino
     */
    private void anexarArchivo(File archivoFuente, File archivoDestino) throws IOException {

        try (FileInputStream fis = new FileInputStream(archivoFuente);
             FileOutputStream fos = new FileOutputStream(archivoDestino, true)) { // append = true

            byte[] buffer = new byte[11264];//8192
            int bytesLeidos;

            while ((bytesLeidos = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesLeidos);
            }

            fos.flush();
            Log.i("ENVIO", "Archivo anexado exitosamente");
        }
    }

    /**
     * Copia un archivo de origen a destino (sin append)
     */
    private void copiarArchivo_ENVIO(File origen, File destino) throws IOException {

        try (FileInputStream fis = new FileInputStream(origen);
             FileOutputStream fos = new FileOutputStream(destino, false)) { // NO append

            byte[] buffer = new byte[11264];//8192
            int bytesLeidos;

            while ((bytesLeidos = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesLeidos);
            }

            fos.flush();
            Log.i("ENVIO", "Archivo copiado exitosamente");
        }
    }

    /**
     * Restaura archivos en caso de error crítico (llamado desde catch general)
     */
    private void restaurarArchivosCasoError(String archivoEnvio, String archivoCopia) {
        try {
            restaurarArchivosDesdeBackup(archivoEnvio, archivoCopia);
            resetearContadores();
        } catch (Exception ex) {
            Log.e("ERROR", "Error crítico restaurando archivos: " + ex.getMessage());
        }
    }

// ============================================================================
// MÉTODOS AUXILIARES - LIMPIEZA Y CONTROL
// ============================================================================

    /**
     * Cierra recursos abiertos
     */
    private void cerrarRecursos() {
        try {
            MisEnviosHilos.Cerrar_EnvioGPS();
            Log.i("ENVIO", "Recursos cerrados");
        } catch (Exception ex) {
            Log.e("ERROR", "Error cerrando recursos: " + ex.getMessage());
        }
    }

    /**
     * Resetea contadores de control
     */
    private void resetearContadores() {
        vecesRea = 0;
        NroEnvios = 0;
        contadorEnviosBloques = 0;
        // NO resetear contadorReintentosConexion aquí - solo cuando hay éxito
        Log.i("ENVIO", "Contadores reseteados");
    }

    /**
     * Método helper para parsear enteros de forma segura
     */
    private int parseStringToInteger_ENVIO(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception ex) {
            return 0;
        }
    }

    //FIN DEL NUEVO PROCEDIMIENTO DE ENVIOS POR VICTOR


    public String EnviarArchivo(String rutaAGrabarTraer, File nombreZipPorCrear, ArrayList ArchivosAEnviar, int trama) {
        String respuestaWeb;

        String estado = "0";

        if (VariablesGlobales.moduloTrabajo.equals("ENT")) {
            estado = "E";
        } else {
            estado = "0";
        }

        respuestaWeb = utils.CreaZip(nombreZipPorCrear.getAbsolutePath(), ArchivosAEnviar);

        if (!respuestaWeb.contains("✓")) return ("Error comprimiendo archivo: " + respuestaWeb);

        if (nombreZipPorCrear.length() <= 0) return "Error comprimiendo archivo 0";

        try {
            WSSoap wsoaps = new WSSoap(URL, paginaWs);
            hash = utils.Md5Hash(nombreZipPorCrear.getAbsolutePath());

            try {
                //[!] Ax: verifica Si existe Directorio Archivo en server ------------------------------------------------
                respuestaWeb = wsoaps.VerificarSiexisteDirectorioArchivo("VerificarSiexiste_Directorio_Archivo", rutaAGrabarTraer, nombreZipPorCrear.getName());
                Log.e("INFO", "VerificarSiexiste_Directorio_Archivo -> " + respuestaWeb);

                if (!respuestaWeb.equals("true"))
                    return "Sin Conexion o Sin directorio \nde trabajo:\n" + rutaAGrabarTraer + " " + respuestaWeb;

            } catch (Exception ex) {
                return "Sin Conexion o Sin directorio \nde trabajo.:\n" + rutaAGrabarTraer + " " + respuestaWeb;
            }
            //[!] Ax: envia archivo al server ----------------------------------------------------------------------------
            respuestaWeb = wsoaps.TerminalToServerReceive("Terminal_ToServerReceive_2", rutaAGrabarTraer, parseStringToInteger(esComprimido), nombreZipPorCrear.getAbsolutePath(), trama);//String met odo, String rutaAGrabar,  int comprimido, String rutaArchivofull, int trama

            Log.e("INFO", "Terminal_ToServerReceive_2 -> " + respuestaWeb);
            if (!(respuestaWeb.equals("4") || respuestaWeb.equals("1")))
                return "Error Enviar Archivo Terminal_ToServerReceive_2: " + respuestaWeb;

            respuestaWeb = wsoaps.Descomprimir("Descomprime", rutaAGrabarTraer, rutaAGrabarTraer + "\\" + nombreZipPorCrear.getName(), hash, NombreArchivos);

            Log.e("INFO", "Descomprime -> " + respuestaWeb);
            if (!respuestaWeb.equals("true")) {
                return "Error Directorio Donde Descomprimir: " + respuestaWeb;
            }
            respuestaWeb = rutaAGrabarTraer.substring(rutaAGrabarTraer.lastIndexOf('/') + 1);
            rutaAGrabarTraer = rutaAGrabarTraer.substring(0, rutaAGrabarTraer.lastIndexOf('/'));

            //respuestaWeb = (rutaAGrabarTraer.substring(rutaAGrabarTraer.lastIndexOf("\\") + 1, rutaAGrabarTraer.length())) + ""; // Se convierte en :  @"CIC" + ciclo + \DescargasEnvioGPRS
            //rutaAGrabarTraer = rutaAGrabarTraer.replace(respuestaWeb, "");//Ax: Se convierte en : c:\\enrutadorsimfa  (ojo lowercase)

            Log.e("INFO", "InsertarDatos rutaAGrabarTraer -> " + rutaAGrabarTraer);
            Log.e("INFO", "InsertarDatos respuestaWeb -> " + respuestaWeb);
            Log.e("INFO", "InsertarDatos serialPDA -> " + serialPDA);
            Log.e("INFO", "InsertarDatos estado -> " + estado);

            respuestaWeb = wsoaps.InsertarDatosTablasTemporales("InsertarDatos", rutaAGrabarTraer, respuestaWeb, serialPDA, estado);

            Log.e("INFO", "InsertarDatos -> " + respuestaWeb);

            if (!respuestaWeb.equals("1")) { //Ax: aqui se graba el error en evaluacion del mensaje del ws
                logger.info("EnviarArchivo()InsertarDatosTablasTemporales " + respuestaWeb);
                return "Error: " + respuestaWeb;
            }

        } catch (Exception ex) {
            logger.info("EnviarArchivo() ex: " + ex.getMessage());
            return "Problemas EnviarArchivo()" + ex.getMessage();
        }
        return "ok";
    }


    private void GuardarBackupEnvio(String nuevoEnvioGPRS) {//nuevo modelo de envio por arreglos directos a la base de datos hacer un backup del archivo enviado

        String backupEnvioGPRS = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKENVIOSGPRS.SDA";
        //String backupEnvioGPRS = VariablesGlobales.directorioBackUp  + "/BKENVIOSGPRS.SDA";///posible estar asi

        utils.CrearCopiaSin(nuevoEnvioGPRS, backupEnvioGPRS); //Crea una copiA sin sobreescribir

        File file_backupEnvioGPRS_2 = new File(VariablesGlobales.directorioBackUp + "BKENVIOSGPRS.SDA");

        File file_backupEnvioGPRS = new File(backupEnvioGPRS);

        if (file_backupEnvioGPRS_2.exists()) {
            file_backupEnvioGPRS_2.delete();
        }

        utils.CrearCopia(file_backupEnvioGPRS.getAbsolutePath(), file_backupEnvioGPRS_2.getAbsolutePath());//-----------------------

        File cuentasEnvio = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "CUENTASNUEVA" + serialPDA + ".SDA");

        if (cuentasEnvio.exists()) {
            cuentasEnvio.delete();
        }
//        File fco_cobro_IPSM = new File(VariablesGlobales.directorioBackUp + "CO_COBRO.SDA");
//
//        if (fco_cobro_IPSM.exists())
//            fco_cobro_IPSM.delete();
//
//        File fco_cobro = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/CO_COBRO.SDA");
//        utils.CrearCopia(fco_cobro.getAbsolutePath(), fco_cobro_IPSM.getAbsolutePath());
//
//        File fNovedades_IPSM = new File(VariablesGlobales.directorioBackUp + "NOVEDADES.SDA");
//
//        if (fNovedades_IPSM.exists())
//            fNovedades_IPSM.delete();
//
//        File fNovedades = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOVEDADES.SDA");
//
//        if (fNovedades.exists())
//            utils.CrearCopia(fNovedades.getAbsolutePath(), fNovedades_IPSM.getAbsolutePath());
//
//        File fAforos_IPSM = new File(VariablesGlobales.directorioBackUp + "AFOROS.SDA");
//
//        if (fAforos_IPSM.exists())
//            fAforos_IPSM.delete();
//
//        File fAforos = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/AFOROS.SDA");
//
//        if (fAforos.exists())
//            utils.CrearCopia(fAforos.getAbsolutePath(), fAforos_IPSM.getAbsolutePath());
    }

    int convertirFormatoDeFecha(String amd, String buff, String mesenformato, String tipo) {
        String mes;
        variables.mesenformato = "";
        if (tipo.equals("AMD")) {

            buff = variables.amd.substring(4, 6);
            mes = variables.amd.substring(4, 6);

        } else if (tipo.equals("D/M/A")) {

            buff = amd.substring(3, 5);
            mes = amd.substring(3, 5);

        } else {

            buff = variables.amd.substring(0, 2);
            mes = variables.amd.substring(0, 2);
        }

        switch (parseStringToInteger(mes)) {
            case 1:
                buff = "/ENE/";
                variables.mesenformato = "ENERO/";
                break;
            case 2:
                buff = "/FEB/";
                variables.mesenformato = "FEBRERO   /";
                break;
            case 3:
                buff = "/MAR/";
                variables.mesenformato = "MARZO     /";
                break;
            case 4:
                buff = "/ABR/";
                variables.mesenformato = "ABRIL     /";
                break;
            case 5:
                buff = "/MAY/";
                variables.mesenformato = "MAYO      /";
                break;
            case 6:
                buff = "/JUN/";
                variables.mesenformato = "JUNIO     /";
                break;
            case 7:
                buff = "/JUL/";
                variables.mesenformato = "JULIO     /";
                break;
            case 8:
                buff = "/AGO/";
                variables.mesenformato = "AGOSTO    /";
                break;
            case 9:
                buff = "/SEP/";
                variables.mesenformato = "SEPTIEMBRE/";
                break;
            case 10:
                buff = "/OCT/";
                variables.mesenformato = "OCTUBRE   /";
                break;
            case 11:
                buff = "/NOV/";
                variables.mesenformato = "NOVIEMBRE /";
                break;
            case 12:
                buff = "/DIC/";
                variables.mesenformato = "DICIEMBRE /";
                break;
            default:
                buff = variables.amd.substring(4, 6);
                return 0;
        }

        if (tipo.equals("AMD")) {

            variables.fechahoy = variables.amd.substring(6, 8) + buff + variables.amd.substring(0, 4); /*
             * Se extraen los campos de  anno, mes y d�a de la
             * variable amd
             */
            variables.mesenformato = variables.mesenformato + "   " + variables.amd.substring(4, 6); /*
             * Se extraen los campos de  anno, mes y d�a de la			             * variable amd
             */
        } else if (tipo.equals("D/M/A")) {
            variables.fechahoy = amd.substring(0, 2) + buff + amd.substring(6, 10); /*
             * Se extraen los campos de  anno, mes y d�a de la			              * variable amd
             */
            variables.mesenformato = variables.mesenformato + "   " + amd.substring(8, 10); /*
             * Se extraen los campos de  anno, mes y d�a de la			              * variable amd			              */
        } else {
            variables.fechahoy = variables.amd.substring(6, 8) + buff + variables.amd.substring(0, 4); /*
             * Se extraen los campos de  anno, mes y d�a de la			             * variable amd			             */
            mesenformato = mesenformato + "   " + variables.amd.substring(4, 6); /*
             * Se extraen los campos de  anno, mes y d�a de la			             * variable amd			             */
        }
        return 1;
    }

    private int validarEstadoRegistroMinimo(int numero, String tipovalidacion) {

        infoRegistroSalida.lectura_Minima_TablaRegistroSalida(numero);
        //infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() != 0
        //infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() != 0
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {//(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0") || infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000"))
            return 2;
        }
        //infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().length() != 0 && !
        if (!infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {
            //before && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0")
            return 1;
        }
        if (tipovalidacion.equals("E")) {
            procesarEntregaLecturas(1, numero);
            return 2;
        }
        return 0;
    }

    private int validarEstadoRegistro(int numero, String tipovalidacion) {

        infoRegistroSalida.lectura_TablaRegistroSalida(numero);

        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {//infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() != 0
            return 2;
        }

        if (!infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")
            && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("0")) {//before && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0")
            return 1;
        }
        if (tipovalidacion.equals("E")) {
            procesarEntregaLecturas(1, numero);
            return 2;
        }
        return 0;
    }

    private int validarEstadoRegistroNuevo(int numero) {

        infoRegistroSalida.lectura_TablaRegistroSalida(numero);

        //if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() != 0 && (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0") || infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000") || infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("00000"))) {
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            return 2;
        }
        if (!infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("") && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("0")) {//before && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0")
            return 1;
        }

        return (0);
    }

    private int escribirTablasSalida(int vienede) {

        if (VariablesGlobales.getTotalClientesReales() > 0) {

            if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (1)(" + vienede + ")"); //temporal?
                if (infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual)) {
                    return 0;
                }
            }
        } else {
            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (2)(" + vienede + ")"); //temporal?
            if (infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual)) {
                return 0;
            }
        }
        return 1;
    }

    private void tomarFechaSistema(String amd, String hm) {

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        variables.amd = df.format(date);

        Date dt = new Date();
        SimpleDateFormat hf = new SimpleDateFormat("HHmmss");
        String formatteHour = hf.format(dt.getTime());

        variables.hm = formatteHour;
    }

    @SuppressLint("SuspiciousIndentation")
    private void visualizarInformacionCliente(int estado) {

        int estadolectura;
        String puntero = "";
        String mensajepantalla = "";
        String lecturaofacturacion;
        String implote;
        String quesoy = infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDOCONLECTURA().trim();
        if (VariablesGlobales.tipoDeRuta.equals("E") || infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("E")) {
            if (VariablesGlobales.getObligabarras().equals("1") || infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDOCONLECTURA().trim().equals("2")) {
                //if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01")) {
                // mensajeT("visible " + infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().trim(), msgLargo);
              /*  if (!infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES().contains("R")) {
                    btnActivaScanner.setVisibility(View.VISIBLE);
                } else {
                    btnActivaScanner.setVisibility(View.INVISIBLE);
                }*/

            } else {
                //mensajeT("INVISIBLE " + infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().trim(), msgLargo);
                btnActivaScanner.setVisibility(View.INVISIBLE);
            }


        }
        /*if (infoRegistroSalida.gettablaRegistroSalida_UBICACION().trim().equals("U")) {
            btnAdelante.setVisibility(View.INVISIBLE);
            btnAtras.setVisibility(View.INVISIBLE);
            btnAdelante.setEnabled(false);
            btnAtras.setEnabled(false);
        }else
        {
            btnAdelante.setVisibility(View.VISIBLE);
            btnAtras.setVisibility(View.VISIBLE);
            btnAdelante.setEnabled(true);
            btnAtras.setEnabled(true);
        }*/


        borrarListaInformacionCliente();

        if (variables.impresionenlote == 1)
            implote = "IL";
        else
            implote = "";

        if (VariablesGlobales.totalregistrosleidos == infoRegistroSalida.getTotal_TablaRegistroSalida()) {
            estado = 3;
        }
        switch (estado) {
            case 0:
                lecturaofacturacion = "L";
                mensajepantalla = VariablesGlobales.getRegistroactual()  //(variables.contadoractual - parseStringToInteger(infoClienteEntrada.gettablaEntradaClientes_PRIMERMEDIDOR().trim()) + 1)
                        + "- >" + lecturaofacturacion + "-" + implote;

                if ((variables.direcciondelectura == variables.haciaadelante))
                    puntero = "=>>";
                else
                    puntero = "<<=";
                break;
            case 1:// Proceso Verificacion
                puntero = "VERIFICA";
                break;
            case 2:// Proceso Lecturas
                puntero = "LECTURA";
                break;
            case 3:// Proceso Finalizar
                puntero = "CONCLUIDO";
                MostrarAlertDialog("Alerta Seleccion Terminacion", "Ya termino de realizar\n las Lecturas? " + VariablesGlobales.totalregistrosleidos + " / " + infoRegistroSalida.getTotal_TablaRegistroSalida(), "cerrarProcesoFacturacion");
                break;
            case 4:// proceso bloqueo:
                MostrarAlertDialog("Alerta Seleccion Terminacion", "Ya termino de realizar\n la Lecturas? " + VariablesGlobales.totalregistrosleidos + " / " + infoRegistroSalida.getTotal_TablaRegistroSalida(), "cerrarProcesoFacturacion");
                puntero = "TERMINADO";
                break;
        }

        lblpuntero.setText("[" + puntero + " " + VariablesGlobales.registroactual + " / " + infoRegistroSalida.getTotal_TablaRegistroSalida() + "]");

        String temp = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim(); //rx
        temp = temp.replaceFirst("^0*", "");
        txtMedidorCuenta.setText("Medidor: " + temp.trim() + "-" + infoRegistroSalida.gettablaRegistroSalida_MARCA());
        //PRUEBAS//txtMedidorCuenta.setText("" + (utils.parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE()) + utils.parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR()) + 2));

        tablaResumen.refreshDrawableState();
        llenarListaCliente("DIRECCION", infoRegistroSalida.gettablaRegistroSalida_DIRECCION().trim().replace("  ", " ").replace("  ", " ").replace("  ", " "));
        llenarListaCliente("NOMBRE", infoRegistroSalida.gettablaRegistroSalida_NOMBRE().substring(0, 47).trim());

        temp = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();
        llenarListaCliente("CUENTA", temp);
        arma_cuentasGps = temp;

        DescSect = infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).trim();//Ax. cada vez que le el cliente captura el tipo E o L

        if (VariablesGlobales.tipoDeRuta.equals("E")) {
            if (!infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("")) {
                llenarListaCliente("LEIDO", infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim());
            } else {
                llenarListaCliente("NO LEIDO", infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim());
            }
            llenarListaCliente("Secuencia Impresion", infoRegistroSalida.gettablaRegistroSalida_INFORME().trim());
            //sacar aqui el porcentual leidos
            llenarListaCliente("PROCESADOS", "" + VariablesGlobales.totalregistrosleidos + " Rorcentaje: " + (VariablesGlobales.totalregistrosleidos * 100 / infoRegistroSalida.getTotal_TablaRegistroSalida()) + " %");
        } else
            llenarListaCliente("DIGITOS", infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim());

        if (parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim()) != 0) {
            txtElectura.setFilters(new InputFilter[]{new InputFilter.LengthFilter(parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim()))});
        }

        cn_Cod_Ruta = String.format("%4s", infoRegistroSalida.gettablaRegistroSalida_RUTA().trim()).replace(" ", "0");
        ;
        cn_Ciclo = infoRegistroSalida.gettablaRegistroSalida_CICLO();
        cn_Desc_Depto = infoRegistroSalida.gettablaRegistroSalida_DESCDEPTO();
        cn_Cod_Municipio = infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO();
        cn_Cod_Sector = String.format("%3s", infoRegistroSalida.gettablaRegistroSalida_CODSECTOR()).replace(" ", "0");
        llenarListaCliente("SECC-RUTA", cn_Cod_Sector + "-" + cn_Cod_Ruta);

        if (infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim().equals("") || infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim().equals("0")) {
            VariablesGlobales.intcontroltexto = 6;
        } else {
            VariablesGlobales.intcontroltexto = parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim());
        }

        if (terminalImei.trim().equals("351007492166830") || terminalImei.trim().equals("358767141019390")
                || terminalImei.trim().equals("868995078034628")) {
            llenarListaCliente("Historial", "" + infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim() + "-" +
                    infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE().trim() + "-" +
                    infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_INFERIOR().trim() + "-" +
                    infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_SUPERIOR().trim());
        }
        String punt = "";


        if (infoRegistroSalida.gettablaRegistroSalida_UBICACION().trim().equals("U"))
            llenarListaCliente("UBICACION", "URBANO");
        else
            llenarListaCliente("UBICACION", "RURAL");


        String claseServicio = infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO().trim().toUpperCase();

        switch (claseServicio) {

            case "FM":

                // lógica FM
                claseServicio += " Residencial";
                break;

            case "CR":
                claseServicio += " Comercial";
                // lógica CO
                break;

            case "ID":
                claseServicio += " Industrial";
                // lógica IN
                break;

            case "OF":
                claseServicio += " Oficial";
                // lógica OF
                break;
            case "MM":
            case "MO":
                claseServicio += " MacroMed";
                // lógica OF
                break;

            default:
                claseServicio += ", Otro";
                // otros
                break;
        }
        llenarListaCliente("Clase Serv.", claseServicio);

        imagenLiquid_2.setImageResource(android.R.color.transparent);
        imagenLiquid_1.setImageResource(android.R.color.transparent);

        if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") &&
                !infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P") &&
                !infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J") &&
                !infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0X") &&
                !infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99")
        ) {
            imagenChat.setImageResource(R.drawable.chat_msg_no);
        } else {
            imagenChat.setImageResource(R.drawable.chat_msg_si);
        }
        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
            imagenChat.setImageResource(R.drawable.verificar);
        }

        //***OJO NUEVO
        /*imagenChat.setImageResource(android.R.color.background_light);
        imagenChat.setImageResource(R.drawable.chat_msg_si);
        imagenChat.setVisibility(View.VISIBLE);
        imagenLiquid_2.setImageResource(R.drawable.notificacion);
        imagenLiquid_2.setVisibility(View.VISIBLE);*/
        //imagenLiquid_2.setImageResource(R.drawable.enviardatos);

        if (parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_CODNOTIFICACION()) > 0) {
            imagenLiquid_2.setImageResource(R.drawable.notificacion);
            imagenLiquid_2.setVisibility(View.VISIBLE);
            if (notiFicaciones.abrir_Notificaciones(notiFicaciones.getarchivo_Notificaciones())) {
                notiFicaciones.setencontro_Notificaciones(0);
                notiFicaciones.buscarSecuencial_Notificaciones(infoRegistroSalida.gettablaRegistroSalida_CODNOTIFICACION());
                if (notiFicaciones.getencontro_Notificaciones() > 0)
                    puntero = infoRegistroSalida.gettablaRegistroSalida_CODNOTIFICACION() + " ->" + notiFicaciones.getNotificaciones_DESCRIPCION();
                else
                    puntero = infoRegistroSalida.gettablaRegistroSalida_CODNOTIFICACION() + " ->" + "Alerta No Existe en la tabla";
                notiFicaciones.Cerrar_Notificaciones();
            }

            llenarListaCliente("NOTIFICACION", puntero);

            if (estado == 10)

                mensajeT(puntero, msgCorto);
        } else {
            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S")) {
                imagenLiquid_2.setImageResource(R.drawable.cortado);
                imagenLiquid_2.setVisibility(View.VISIBLE);
                puntero = "ALERTA CUENTA SUSPENDIDA";
                llenarListaCliente("ESTADO", puntero);
                if (estado == 10)

                    mensajeT(puntero, msgCorto);
            } else {
                if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")) {
                    imagenLiquid_2.setImageResource(R.drawable.sobre);
                    puntero = "ALERTA CUENTA EN JURIDICA";
                    llenarListaCliente("ESTADO", puntero);
                    if (estado == 10)

                        mensajeT(puntero, msgCorto);
                } else {
                    if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P")) {
                        imagenLiquid_2.setImageResource(R.drawable.obrero);
                        imagenLiquid_2.setVisibility(View.VISIBLE);
                        puntero = "ALERTA CUENTA PROVISIONAL";
                        llenarListaCliente("ESTADO", puntero);
                        if (estado == 10)

                            mensajeT(puntero, msgCorto);
                    } else {
                        if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0X")) {
                            imagenLiquid_2.setImageResource(R.drawable.obrero);
                            imagenLiquid_2.setVisibility(View.VISIBLE);
                            puntero = "ALERTA SELLOS EN REVISION";
                            llenarListaCliente("ESTADO", puntero);
                            if (estado == 10)

                                mensajeT(puntero, msgCorto);
                        } else {
                            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99")) {
                                imagenLiquid_2.setImageResource(R.drawable.obrero);
                                imagenLiquid_2.setVisibility(View.VISIBLE);
                                puntero = "ALERTA CLIENTE ANOMALO";
                                llenarListaCliente("ESTADO", puntero);
                                if (estado == 10)

                                    mensajeT(puntero, msgCorto);
                            } else {
                                llenarListaCliente("ESTADO", "Activo");
                            }
                        }
                    }
                }
            }

        }


        if (!infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("")) {

            llenarListaCliente("Lectura", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim());
        }

        // llenarListaCliente("TIPO MEDIDA", infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA());
        //llenarListaCliente("MARCA", infoRegistroSalida.gettablaRegistroSalida_MARCA().trim());
        if (VariablesGlobales.tipoDeRuta.equals("E")) {
            llenarListaCliente("Tipo E.", infoRegistroSalida.gettablaRegistroSalida_DESCSERV());
        } else {
            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") || infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P")
                    || infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J") || infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0X")) {
                llenarListaCliente("POR VERIFICAR", "CLIENTE CON SUSPENCION O REPORTE ESPECIAL");
            }
        }

        llenarListaCliente("Indicador Med", infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA());
        if (!infoRegistroSalida.gettablaRegistroSalida_OBSERANT().trim().equals(""))
            llenarListaCliente("Obs.Ant", infoRegistroSalida.gettablaRegistroSalida_OBSERANT());


        llenarListaCliente("SENTIDO RUTA", "[" + mensajepantalla + lblpuntero.getText());

        if (infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES().contains("R")) {
            lblInfoMedida.setTextColor(Color.rgb(204, 41, 0));
            lblInfoMedida.setText("RE-ACTIVA");
        } else {
            lblInfoMedida.setTextColor(Color.rgb(0, 154, 0));
            lblInfoMedida.setText("ACTIVA");
        }



        llenarListaCliente("CONTADORES", mensajepantalla);

        estadolectura = validarEstadoRegistro(VariablesGlobales.registroactual, "L");

        if (estadolectura == 1) {
            llenarListaCliente("LECTURA", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim());
            if ((!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")
                    && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91")
                    && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92")
                    && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93"))
                    && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("")) {
                if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
                    anomaliaDeLectura.lectura_AnomaliaDeNoLectura(1);
                    anomaliaDeLectura.setEncontro_AnomaliaDeNoLectura(0);
                    anomaliaDeLectura.buscarAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
                    anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();
                }
                llenarListaCliente("ANOMALIA NO LECT", infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA() + "->"
                        + anomaliaDeLectura.getanomaliaDeNoLectura_DESCRIPCION());
            }

        } else {
            if (estadolectura == 2) {                //realizarla busqueda de la causa si existe un valor

                if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")
                        && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91")
                        && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92")
                        && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93")) {
                    if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
                        anomaliaDeLectura.lectura_AnomaliaDeNoLectura(1);
                        anomaliaDeLectura.setEncontro_AnomaliaDeNoLectura(0);
                        anomaliaDeLectura.buscarAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
                        anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();
                    }

                    llenarListaCliente("ANOMALIA NO LECT", infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA() + "->"
                            + anomaliaDeLectura.getanomaliaDeNoLectura_DESCRIPCION());
                } else
                    llenarListaCliente("CAUSAL TOMADA", infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim() + "->" + "REGISTRADA LA ENTREGA");

            }


            if (!infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals("")) {
                if (!VariablesGlobales.tipoDeRuta.equals("E"))
                    llenarListaCliente("INFORME", infoRegistroSalida.gettablaRegistroSalida_INFORME());
            } else {
                if (!infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                    llenarListaCliente("LECT-ANOM REPORT", "[" + infoRegistroSalida.gettablaRegistroSalida_CODESTADO().trim() + "]--[" +
                            infoRegistroSalida.gettablaRegistroSalida_OBSERANT().trim() + "]");
                }
            }


            llenarListaCliente("GPS ANTERIOR", infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim() + "//" + infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim());

            if (infoRegistroSalida.gettablaRegistroSalida_CODIGO_SAC().trim().length() > 0) {
                llenarListaCliente("CRITICA_SAC", infoRegistroSalida.gettablaRegistroSalida_CODIGO_SAC());
            }

            String mensaje = "MEDIDOR SIN LECTURA";

            if (infoRegistroSalida.gettablaRegistroSalida_SINLECTURA().substring(0, 1).equals("1"))
                mensaje = "LECTURA NO APLICADA ";

           llenarListaCliente("Mensaje", mensaje.trim());
        }


        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("E")) {
            esEntrega = true;
            imagenLiquid_2.setImageResource(R.drawable.entrega);
        } else {
            esEntrega = false;
        }

        if (VariablesGlobales.habilitadaimpresora == 0) {
            imagenPrinter.setImageResource(R.drawable.imagen_prin_apagada);

        } else {
            imagenPrinter.setImageResource(R.drawable.impresora);
        }

        if (infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim().length() != 0 && infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() == 0) {

            if ((infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()).replaceFirst("^0*", "").equals("")) { //rx
                medidorCero = true;
            } else {
                medidorCero = false;
            }
        }

        if (scanEntregas && VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: para agilizar la 'entrega' se coloca un numero en lectura para que  procese de una vez, ahorrar un paso
            //before scanEntregas = false;
            txtElectura.setText("5");
        } else {

            if ((infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V") ||
                    !infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0A")) &&
                    !VariablesGlobales.tipoDeRuta.equals("E")) {//nuevo para verificar cuentas
                imagenChat.setImageResource(R.drawable.verificar);
                if (YaMostroMensaje == 0)
                    MensajeriaCliente();//ir a concordar este mensaje
                YaMostroMensaje = 1;
            }
        }
    }

    private void llenarListaCliente(String campo, String valor) {

        TextView txtCampo;
        TextView txtValor;
        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        txtCampo = new TextView(this);
        txtValor = new TextView(this);
        txtCampo.setText(campo);
        txtCampo.setGravity(Gravity.LEFT);
        txtCampo.setTextAppearance(this, R.style.etiqueta);
        txtCampo.setBackgroundResource(R.drawable.tabla_celda);
        txtCampo.setLayoutParams(layoutCampo);
        txtValor.setText(valor);
        txtValor.setGravity(Gravity.LEFT);
        txtValor.setTextAppearance(this, R.style.campo);
        txtValor.setBackgroundResource(R.drawable.tabla_celda);
        txtValor.setLayoutParams(layoutValor);

        fila.addView(txtCampo);
        fila.addView(txtValor);
        tablaResumen.addView(fila);
    }

    private void borrarListaInformacionCliente() {
        tablaResumen.removeAllViews();
    }

    private int leerInformacionUsuario(int tipo) {

        infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
        return 0;
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Ax: propio de android.support.v7.widget.Toolbar. Desde  res...menu...main.xml // Inflar el menu; esto adiciona items a la actionBar si existe
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }*/
    private static final String ARCHIVO_MARCA_ZEBRA = "marca_ZEBRA.txt";
    private static final String ARCHIVO_MARCA_JAL = "marca_JAL.txt";

    public void opcionMenu(int item) { //tools

        txtElectura.setText("");

        switch (item) {
            case 1: //capturar_anomalia:

                if (autorizacion()) {
                    capturarAnomaliaTerreno(false, false, true);
                }
                break;

            case 2://cuenta nueva: //Ejecutar informe independiente de otros procesos

                Crear_Cuenta_Nueva();

                break;

            case 3://capturar_informe: //Ejecutar informe independiente de otros procesos
                if (!infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                    s_idac = VariablesGlobales.registroactual;
                    abrirInforme(true, UltimoInforme);
                } else {
                    Toast.makeText(getApplicationContext(), "Proceso de verificacion", Toast.LENGTH_LONG).show();
                    String mensaje2 =
                            "📡 ESTADO DE VERIFICACION\n\n" +
                                    "✔ .....EL PRORCESO ES VERIFICAR\n" +
                                    "ℹ️ EL CONSUMO O LA ANOMALIA SE TOMA INFORME AUTOMATICO DESPUES DE LA LECTURA O NOVEDAD.";//obtenerFechaActual()
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Información del ALERTA")
                            .setMessage(mensaje2)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                            .show();
                }
                break;

            case 4://anterior_predio_leido: // ultimo predio leido
                visualizarPredioLeido();
                break;

            case 20: //opciones_supervisor: impresora obligatoria / navegación / cierre forzado
                abrirOpcionesSupervisor();
                break;

/*            case 5://modificar_nro_digitos:

                VariablesGlobales.datodebusqueda = "";

                Bundle bundle = new Bundle();

                bundle.putString("DIRECTORIOACTUAL", "" + VariablesGlobales.getDirectorioactual());
                bundle.putInt("NUMEROREGISTROACTUAL", VariablesGlobales.getRegistroactual());

                Intent di = new Intent(this, ModuloCambioDigitos.class);
                di.putExtras(bundle);

                startActivityForResult(di, DIGITOS_REQUEST_CODE);

                abrirArchivosDeFacturacion();
                infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
                if (!VariablesGlobales.datodebusqueda.trim().equals("")) {
                    infoRegistroSalida.settablaRegistroSalida_informe("CAMBIO DIGITOS " + VariablesGlobales.datodebusqueda.trim());
                    infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                }

                infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
                visualizarInformacionCliente(0);
                cerrarArchivosFacturacion();
                break;*/

            case 5://buscar_cliente:    // realizar la busqueda del cliente
                ejecutarBusquedaCliente();
                break;


            case 6://ayuda_ubicacion_GPS:
                String latDest1 = infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim();
                String lonDest1 = infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim();

                if (!latDest1.isEmpty() && !lonDest1.isEmpty()) {
                    try {
                        Bundle bundleg = new Bundle();
                        bundleg.putString("lactitud", infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim());//.gettablaEntradaClientes_CORDENADAX().trim());
                        bundleg.putString("longitud", infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim());//.gettablaEntradaClientes_CORDENADAY().trim());
                        Intent inten = new Intent(this, ActGPS.class);
                        inten.putExtras(bundleg);
                        startActivityForResult(inten, ACTGPS_REQUEST_CODE);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Las coordenadas de esta cuenta son inválidas.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Esta cuenta no tiene coordenadas registradas.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 7://mapa:
                String latDest = infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim();
                String lonDest = infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim();

                if (!latDest.isEmpty() && !lonDest.isEmpty()) {
                    try {
                        double lat = Double.parseDouble(latDest);
                        double lon = Double.parseDouble(lonDest);
                        // Lanza Waze → Maps → MapsActivity (fallback automático)
                        NavigationHelper.navigateTo(this, lat, lon);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Las coordenadas de esta cuenta son inválidas.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Esta cuenta no tiene coordenadas registradas.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 8: // ENVIAR LECTURAS: reencolar errores + despachar de inmediato
                // Antes esta opción solo ABRÍA la consulta de no enviados: no
                // reencolaba nada ni despachaba. El botón decía "enviar" y no
                // enviaba. Ahora reencola los fallidos y fuerza el ciclo.
                int lecturasError8 = CrudEnvioLectura.reintentar(ModoReenvio.SOLO_ERROR);
                int lecturasPend8 = CrudEnvioLectura.contarPendientes();

                if (lecturasPend8 == 0) {
                    mensajeT("✓ No hay lecturas pendientes de envío", msgMedio);
                } else {
                    LecturaSyncService.forzarSync(this);

                    AlertDialog.Builder b8 = new AlertDialog.Builder(this);
                    b8.setTitle("ENVIANDO LECTURAS");
                    b8.setIcon(R.drawable.ic_launcher);
                    b8.setMessage("📄 Pendientes: " + lecturasPend8 + "\n" +
                            (lecturasError8 > 0 ? "🔄 Reencoladas (error): " + lecturasError8 + "\n" : "") +
                            "\n📡 Despachando ahora...");
                    b8.setPositiveButton("OK", null);
                    b8.setNeutralButton("Ver detalle", (d, w) -> {
                        Bundle bundlea = new Bundle();
                        bundlea.putString("directorioactual", VariablesGlobales.directorioactual);
                        bundlea.putString("serialNumberImei", terminalImei);
                        Intent ita = new Intent(this, ModuloConsultaNoEnviados.class);
                        ita.putExtras(bundlea);
                        startActivityForResult(ita, CONSULTNONVIADS_REQUEST_CODE);
                    });
                    b8.show();
                }
                break;

            case 9: // PROCESAR REENVÍO: elegir alcance y despachar
                AlertDialog.Builder builderReenvio = new AlertDialog.Builder(this);
                builderReenvio.setTitle("REINTENTAR ENVÍOS");
                builderReenvio.setIcon(R.drawable.ic_launcher);

                String[] opcionesReenvio = {
                        "📦 Reenviar TODOS los registros",
                        "⚠️ Reenviar solo registros con ERROR",
                        "❌ Cancelar operación"
                };

                builderReenvio.setItems(opcionesReenvio, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            confirmarReenvioTotal();
                            break;
                        case 1:
                            procesarReenvio(ModoReenvio.SOLO_ERROR);
                            break;
                        default:
                            dialog.dismiss();
                    }
                });
                builderReenvio.show();
                break;

            case 10: // evaluar_noEnviadas_gprs: EVALUAR NO ENVIADAS (Realm)

                enviosGPRScantidad = 1;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("ALERTA EVALUAR ENVIOS");
                builder2.setMessage("DESEA EVALUAR SI TODAS SUS LECTURAS FUERON ENVIADAS? ");
                builder2.setIcon(R.drawable.ic_launcher);

                builder2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        msgAdmon = String.format("%-42s", "VALIDANDO REGISTROS");
                        mensajeAdm(MenuDeLiquidacion.this); // muestra loading
                        metodo = "ValidarNoEnviados";
                        TaskHelper.execute(new AsyncCallWS(), metodo);
                    }
                });
                builder2.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder2.show();
             /*   String MensajeRESP = ValidarNoEnviados_2();

                Toast.makeText(getApplicationContext(), "Proceso de verificacion Concluido", Toast.LENGTH_LONG).show();
                String mensaje2 =
                        "📡 ESTADO DE PROCESO\n\n" +
                                "✔ ....."+MensajeRESP+"\n" +
                                "ℹ️ Si el mensaje es recuperacion positiva ir a enviar lecturas.";//obtenerFechaActual()
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Información del Sistema")
                        .setMessage(mensaje2)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                        .show();*/


                break;

            case 11: { // FALTANTES DE CIERRE Resumen completo de sincronización

                int[] lStats = CrudEnvioLectura.getEstadisticas();
                int[] fStats = CrudEnvioFoto.getEstadisticas();
                int[] cStats = CrudEnvioCuentaNueva.getEstadisticas();

                StringBuilder resumen = new StringBuilder();

                // Lecturas: pendientes / enviadas / errores

                resumen.append("LECTURAS\n");
                resumen.append("  Pendientes : ").append(lStats[0]).append("\n");
                resumen.append("  Enviadas   : ").append(lStats[1]).append("\n");
                resumen.append("  Errores    : ").append(lStats[2]).append("\n");


                resumen.append("______________________________\n");

                // Fotos

                resumen.append("FOTOS\n");
                resumen.append("  Pendientes : ").append(fStats[0]).append("\n");
                resumen.append("  Enviadas   : ").append(fStats[1]).append("\n");
                resumen.append("  Errores    : ").append(fStats[2]).append("\n");


                resumen.append("______________________________\n");

                // Cuentas nuevas

                resumen.append("CUENTAS NUEVAS\n");
                resumen.append("  Pendientes : ").append(cStats[0]).append("\n");
                resumen.append("  Enviadas   : ").append(cStats[1]).append("\n");
                resumen.append("  Errores    : ").append(cStats[2]).append("\n");


                resumen.append("______________________________\n");

                // Archivo de cierre
                File descCierre16 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/O" + NombreArchivos + ".SDA");

                resumen.append("CIERRE: ").append(descCierre16.exists() ? "Enviado ✓" : "Pendiente").append("\n");
                resumen.append("______________________________\n");


                resumen.append("\n").append(VariablesGlobales.versionApp);

                // Determinar si hay errores para ofrecer reintentarprocesar
                boolean hayErrores16 = (lStats[2] + fStats[2] + cStats[2]) > 0;

                AlertDialog.Builder builder16 = new AlertDialog.Builder(MenuDeLiquidacion.this);
                builder16.setTitle("ESTADO DE SINCRONIZACIÓN");
                builder16.setMessage(resumen.toString());
                builder16.setIcon(R.drawable.ic_launcher);
                builder16.setCancelable(true);

                if (hayErrores16) {
                    builder16.setPositiveButton("Reintentar Errores", (d, w) -> {
                        int totalR = CrudEnvioLectura.reintentar(ModoReenvio.SOLO_ERROR)
                                + CrudEnvioFoto.reintentar(ModoReenvio.SOLO_ERROR)
                                + CrudEnvioCuentaNueva.reintentar(ModoReenvio.SOLO_ERROR);
                        if (totalR > 0) LecturaSyncService.forzarSync(this);
                        mensajeT("✓ " + totalR + " registros puestos en cola", msgMedio);
                    });
                    builder16.setNegativeButton("Cerrar", null);
                } else {
                    builder16.setPositiveButton("OK", null);
                }
                builder16.show();
                break;
            }


            case 12: // ENVIAR FOTOS  pendientes - El servicio envía automáticamente
                int fotosPend = CrudEnvioFoto.contarPendientes();
                int[] fotoStats2 = CrudEnvioFoto.getEstadisticas();

                StringBuilder msgFotos = new StringBuilder();
                msgFotos.append("📷 ESTADO DE FOTOS\n");
                msgFotos.append("═══════════════════\n\n");
                msgFotos.append("Pendientes: ").append(fotoStats2[0]).append("\n");
                msgFotos.append("Enviadas: ").append(fotoStats2[1]).append("\n");
                msgFotos.append("Con error: ").append(fotoStats2[2]).append("\n\n");

                if (fotoStats2[0] == 0 && fotoStats2[2] == 0) {
                    msgFotos.append("✓ Todas las fotos fueron enviadas");
                } else if (fotoStats2[0] > 0) {
                    msgFotos.append("📡 El servicio las enviará automáticamente");
                }

                // SIEMPRE usar AlertDialog, nunca Toast para mensajes multilínea
                AlertDialog.Builder fotoBuilder = new AlertDialog.Builder(this);
                fotoBuilder.setTitle("FOTOS PENDIENTES");
                fotoBuilder.setMessage(msgFotos.toString());
                fotoBuilder.setIcon(R.drawable.ic_launcher);

                if (fotoStats2[2] > 0) {
                    fotoBuilder.setPositiveButton("Reintentar Errores", (d, w) -> {
                        int reint = CrudEnvioFoto.reintentar(ModoReenvio.SOLO_ERROR);
                        if (reint > 0) LecturaSyncService.forzarSync(this);
                        mensajeT("✓ " + reint + " fotos puestas en cola", msgMedio);
                    });
                    fotoBuilder.setNegativeButton("Cerrar", null);
                } else {
                    fotoBuilder.setPositiveButton("OK", null);
                }
                fotoBuilder.show();
                break;
            case 13://Enviar Archivos de CIERRE
                enviarArchivos1();
                break;


            case 14://estadistica_proceso:
                try {
                    estadisticasdeproceso();

                } catch (Exception e) {
                    mensajeT("Error al iniciar actividad de Resumen Estadistico" + e.getMessage(), msgLargo);
                }
                break;

            case 15://mensajeria
                Bundle bundlex = new Bundle();
                bundlex.putString("operario", lector);
                bundlex.putString("url", URL);
                bundlex.putString("pagws", paginaWs);
                Intent it = new Intent(this, Insertar.class);
                it.putExtras(bundlex);
                startActivityForResult(it, CHATINSETAR_REQUEST_CODE);
                break;


            case 16://activar tamaño  de pantalla
                if (TamannoPantalla == 780) {
                    TamannoPantalla = 400;
                } else {
                    TamannoPantalla = 780;

                }
                guardarTamanoPantalla(TamannoPantalla, 1);
                leerTamanoPantalla();

                break;
            case 17://Reimprimir constansia
                code.setLength(0);
                try {
                    abrirArchivosDeFacturacion();
                    alterno = VariablesGlobales.registroactual;
                    if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {
                        if (variables.ultimoregistro > 0)
                            VariablesGlobales.registroactual = variables.ultimoregistro;
                        else {
                            //mensaje porque el el predio no esta leido ni el posicionado ni una ultima lectura
                            Toast.makeText(getApplicationContext(), "Proceso de Reimpresion", Toast.LENGTH_LONG).show();
                            String mensaje2 =
                                    "📡 ESTADO DE REIMPRESION\n\n" +
                                            "✔ .....EL PRORCESO ES REIMPRESION\n" +
                                            "ℹ️ EL PREDIO ACTUAL NI EL ANTERIOR HAN SIDO LEIDOS.";//obtenerFechaActual()
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Información del ALERTA")
                                    .setMessage(mensaje2)
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                                    .show();

                            break;
                        }
                    }
                    Log.e("INFO", VariablesGlobales.registroactual + " registros " + variables.ultimoregistro);
                    leerInformacionUsuario(1);
                    visualizarInformacionCliente(0);

                    MostrarAlertDialog("ALERTA DE REIMPRESION", "Desea Reimprimir el Cliente?", "reimpresiondefactura");
                } catch (Exception e) {
                    Log.e("ERROR", "MenuDeAyuda Reimpresion| Error: " + e);
                }

                break;

            case 18://modificar envio de impresion a otro tipo de impresora

            /*    code.setLength(0);
                String MensajeCambioImpresora = "CAMBIAR UNA STARPOS X UNA ZEBRA";
                if (!MarcaDeImpresora.equals("JAL")) {
                    MensajeCambioImpresora = "CAMBIAR UNA ZEBRA X UNA STARPOS";
                }
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("CAMBIAR MODELO DE IMPRESORA");
                builder3.setMessage("DESEA " + MensajeCambioImpresora + "? ");

                builder3.setIcon(R.drawable.ic_launcher);

                builder3.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (MarcaDeImpresora.equals("JAL"))
                            MarcaDeImpresora = "ZEBRA";
                        else
                            MarcaDeImpresora = "JAL";

                    }
                });
                builder3.show();                // fin del proceso de reimpresion


                break;*/
                code.setLength(0);
                String MensajeCambioImpresora = "CAMBIAR UNA STARPOS X UNA ZEBRA";
                if (!MarcaDeImpresora.equals("JAL")) {
                    MensajeCambioImpresora = "CAMBIAR UNA ZEBRA X UNA STARPOS";
                }
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("CAMBIAR MODELO DE IMPRESORA");
                builder3.setMessage("DESEA " + MensajeCambioImpresora + "? ");
                builder3.setIcon(R.drawable.ic_launcher);
                builder3.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (MarcaDeImpresora.equals("JAL"))
                            MarcaDeImpresora = "ZEBRA";
                        else
                            MarcaDeImpresora = "JAL";

                        guardarMarcaImpresoraEnArchivo(MarcaDeImpresora);
                    }
                });
                builder3.show();
                // fin del proceso de reimpresion
                break;
            case 19://Cambio color teclado numerico
                if (TecladoNumericoActivo==0)
                {botonesNegro();
                    TecladoNumericoActivo=1;}
                else
                {botonesAzul();
                    TecladoNumericoActivo=0;}


        }
    }//tools

    //nuevo mover a una funcion el crear la cuenta nueva
    private void Crear_Cuenta_Nueva() {
        Bundle bundlecn = new Bundle();
        bundlecn.putString("directorioactual", "" + VariablesGlobales.getDirectorioactual());
        bundlecn.putString("nombrearchivo", "" + NombreArchivos);
        bundlecn.putString("cnCod_Ruta", infoRegistroSalida.gettablaRegistroSalida_CUENTA());
        bundlecn.putString("cnCiclo", cn_Ciclo);
        bundlecn.putString("cnDesc_Depto", cn_Desc_Depto);
        bundlecn.putString("cnDireccion", infoRegistroSalida.gettablaRegistroSalida_DIRECCION().trim());
        bundlecn.putString("cnCod_Municipio", cn_Cod_Municipio);
        bundlecn.putString("cnCod_Sector", cn_Cod_Sector);
        bundlecn.putString("latitud", txtLongitud.getText().toString().trim());
        bundlecn.putString("longitud", txtLatitud.getText().toString().trim());
        bundlecn.putString("numSatelites", numeroDeSatelitesGPS);
        bundlecn.putString("altitud", altitudReportadaGPS);
        bundlecn.putString("fechaHora", fechayHoraReportadaGPS);
        bundlecn.putString("serial", serialPDA);
        bundlecn.putString("lector", lector);
        bundlecn.putString("CicloReal", CicloReal);

        Intent cn = new Intent(this, ModuloCuentaNueva.class);
        cn.putExtras(bundlecn);
        startActivityForResult(cn, CUENTA_NUEVA);

        //  startActivity(cn);
    }
    //fin de la opcioncuenta nueva

    /**
     * Crea el archivo marcador de la marca seleccionada y borra el del otro
     * tipo, para que en todo momento exista uno solo.
     */
    private void guardarMarcaImpresoraEnArchivo(String marca) {
        File archivoZebra = new File(getFilesDir(), ARCHIVO_MARCA_ZEBRA);
        File archivoJal = new File(getFilesDir(), ARCHIVO_MARCA_JAL);
        try {
            if (marca.equals("ZEBRA")) {
                if (!archivoZebra.exists())
                    archivoZebra.createNewFile();
                if (archivoJal.exists())
                    archivoJal.delete();
            } else {
                if (!archivoJal.exists())
                    archivoJal.createNewFile();
                if (archivoZebra.exists())
                    archivoZebra.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Revisa cual de los dos archivos existe y deja MarcaDeImpresora
     * establecida con esa marca. Llamala en el onCreate() para restaurar
     * la impresora usada en la ultima sesion. Si no existe ninguno (primer
     * uso de la app), deja el valor por defecto que ya traiga la variable.
     */
    private void cargarMarcaImpresoraGuardada() {
        File archivoZebra = new File(getFilesDir(), ARCHIVO_MARCA_ZEBRA);
        File archivoJal = new File(getFilesDir(), ARCHIVO_MARCA_JAL);

        if (archivoZebra.exists()) {
            MarcaDeImpresora = "ZEBRA";
        } else if (archivoJal.exists()) {
            MarcaDeImpresora = "JAL";
        }
        // si no existe ninguno, se deja el valor con el que ya venia MarcaDeImpresora
    }

    /**
     * Reencola registros y fuerza el despacho inmediato.
     *
     * @param modo SOLO_ERROR reencola los fallidos; TODAS reencola el histórico
     *             completo (usar solo vía confirmarReenvioTotal()).
     */
    private void procesarReenvio(ModoReenvio modo) {
        int lecturas = CrudEnvioLectura.reintentar(modo);
        int fotos = CrudEnvioFoto.reintentar(modo);
        int cuentas = CrudEnvioCuentaNueva.reintentar(modo);
        int total = lecturas + fotos + cuentas;

        AlertDialog.Builder resultado = new AlertDialog.Builder(this);
        resultado.setTitle("REENVÍO FINALIZADO");
        resultado.setIcon(R.drawable.ic_launcher);

        if (total > 0) {
            LecturaSyncService.forzarSync(this);
            resultado.setMessage(
                    "✓ " + total + " registros puestos en cola:\n\n" +
                            "📄 Lecturas: " + lecturas + "\n" +
                            "📷 Fotos: " + fotos + "\n" +
                            "🆕 Cuentas nuevas: " + cuentas + "\n\n" +
                            "📡 Despachando ahora...");
        } else {
            resultado.setMessage("No hay registros para procesar.");
        }
        resultado.setPositiveButton("OK", (d, w) -> d.dismiss());
        resultado.show();
    }

    /**
     * Confirmación explícita para el reenvío total.
     * <p>
     * Reencolar TODO reenvía el histórico completo del dispositivo al servidor.
     * Con rutas de más de mil registros eso es una transacción Realm grande y
     * una carga considerable para el backend, así que se muestra el conteo real
     * antes de ejecutar en vez de un "¿está seguro?" genérico.
     */
    private void confirmarReenvioTotal() {
        int totalRegistros = CrudEnvioLectura.contarTodos()
                + CrudEnvioFoto.contarTodos();

        new AlertDialog.Builder(this)
                .setTitle("⚠️ REENVIAR TODO")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Se reenviarán " + totalRegistros + " registros al servidor, " +
                        "incluidos los ya enviados correctamente.\n\n" +
                        "Use esta opción solo si el servidor perdió información.\n\n" +
                        "¿Continuar?")
                .setPositiveButton("Sí, reenviar todo", (d, w) -> procesarReenvio(ModoReenvio.TODAS))
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .show();
    }

    private void estadisticasdeproceso() {
        Bundle bundle2 = new Bundle();
        bundle2.putString("aforador", lector);
        bundle2.putString("directorioActual", VariablesGlobales.directorioactual);
        bundle2.putString("nombrePredio", variables.getNombrepredio());
        bundle2.putString("terminal", terminalImei);
        Intent intent = new Intent(this, ResumenEstadistico.class);
        intent.putExtras(bundle2);
        startActivity(intent);
    }

    public void enviarArchivos1() {

        if (estaEnvioAlWsOcupado) {
            mensajeT("Hay envios de Fotos en proceso...\ninténtelo en unos segundos", msgLargo);
            return;
        }
        estaEnvioAlWsOcupadoTrue();

        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/NOMBRE");
        File file2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LEIDO.TXT");

        if (!file.exists()) {
            mensajeT("Terminal NO tiene\n ruta cargada", msgLargo);
            return;
        }
        enviarArchivos2();
       /* if (!file2.exists()) {
            MostrarAlertDialog("Alerta de transmision de datos!", "LAS RUTAS NO HAN SIDO LEIDAS\n" + "EN SU TOTALIDAD, DESEA CONTINUAR?", "enviarArchivos2");
            return;
        } else {
            enviarArchivos2();
        }*/
    }

    public void enviarArchivos2() {
        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOSGPRS.SDA");

        String EnviosGprsda = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS" + serialPDA + ".SDA";


        String archivoI = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/I" + NombreArchivos;
        File Archivo3 = new File(archivoI);

        if (Archivo3.exists())
            Archivo3.delete();

        VariablesGlobales.copyFile(EnviosGprsda, archivoI, false);
        //-----------------------------------

        CrudEnvioLectura.generarArchivoBKENVIOS(this);


        String archivoJ = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/J" + NombreArchivos;
        File Archivo1 = new File(archivoJ);

        if (Archivo1.exists())
            Archivo1.delete();

        VariablesGlobales.copyFile(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOSGPRS.SDA", archivoJ, false);
        //-----------------------------------------------------------
        String backupEnvioGPRS = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKENVIOSGPRS.SDA";
        String archivoH = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/H" + NombreArchivos;//???
        File Archivo2 = new File(archivoH);

        if (Archivo2.exists())
            Archivo2.delete();

        VariablesGlobales.copyFile(backupEnvioGPRS, archivoH, false);
        //------------------------------------------------------------------------
        String archivoFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "ENVIOFOTOS" + serialPDA + ".SDA";
        String archivoW = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/W" + NombreArchivos;
        File Archivo1F = new File(archivoW);

        if (Archivo1F.exists())
            Archivo1F.delete();

        VariablesGlobales.copyFile(archivoFotos, archivoW, false);

        //------------------------------------------------------------------------
        String backupFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKEnvioFotos.SDA";
        String archivoX = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/X" + NombreArchivos;
        File Archivo1X = new File(archivoX);

        if (Archivo1X.exists())
            Archivo1X.delete();

        VariablesGlobales.copyFile(backupFotos, archivoX, false);

        //------------------------------------------------------------------------ archivo log
        String backupFotosZ = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LOGEVENTOS.log";
        String archivoZ = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/Z" + NombreArchivos;
        File Archivo1Z = new File(archivoX);

        if (Archivo1Z.exists())
            Archivo1Z.delete();

        VariablesGlobales.copyFile(backupFotosZ, archivoZ, false);
        //------------------------------------------------------------------------
        CrudEnvioCuentaNueva.generarArchivoCUENTASNUEVAS(this);
        String CuentaNuevaN = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/CUENTASNUEVAS.SDA";
        String archivoN = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/N" + NombreArchivos;
        File Archivo1N = new File(archivoN);

        if (Archivo1N.exists())
            Archivo1N.delete();

        VariablesGlobales.copyFile(CuentaNuevaN, archivoN, false);
        //------------------------------------------------------------------------

        String backupMaloY = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ARCHIVO_MALO.SDA";
        String archivoY = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/Y" + NombreArchivos;
        File Archivo1Y = new File(archivoY);

        if (Archivo1Y.exists())
            Archivo1Y.delete();

        VariablesGlobales.copyFile(backupMaloY, archivoY, false);
        //------------------------------------------------------------------------
        enviarArchivos3();
        /*if (!file.exists()) {
            MostrarAlertDialog("Alerta de transmision de datos!", "ALERTA! HAY RUTAS SIN\n" + "ENVIAR AL SERVIDOR, DESEA CONTINUAR?", "enviarArchivos3");
            return;
        } else {
            enviarArchivos3();
        }*/
    }

    public void enviarArchivos3() {
        MostrarAlertDialog("Alerta de transmision de datos!", "Desea enviar los datos ?\n" + "", "enviarArchivos4");

        return;
    }

    public String enviarArchivos4_asinc() {
        try {
            String NombreZip = "D" + NombreArchivos + ".ZIP";
            ArchivoAEnviarRecibir = rutaAdministrador.trim() + VariablesGlobales.moduloTrabajo + CicloReal + "\\";
            String RutaAGrabar = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/";

            if (NombreArchivos.equals("") || NombreArchivos == null) {
                mensajeT("Error en el archivo Nombre", msgLargo);
                return "Error en el archivo Nombre";
            }
            WSSoap wsoap = new WSSoap(URL, paginaWs);

//            if (!wsoap.verificarWs("VALIDAR_CONEXION").equals("1")) {
//                return "Sin respuesta del Sevidor\n Verifique su conexión";
//            }

            String respuesta = wsoap.VerificarSiexisteDirectorioArchivo("VerificarSiexiste_Directorio_Archivo", ArchivoAEnviarRecibir, NombreZip);//Ax: Se comprueba si existe la ruta destino remota

            if (!respuesta.equals("true")) {
                return "Error No se ha encontrado destino en el servidor!";
            }

            String ext = NombreArchivos.substring(NombreArchivos.indexOf("."), NombreArchivos.length());

            if (Integer.parseInt(esComprimido) == 1) { //Ax: Se procede a crear el ZIP

                ArrayList filestoZip = new ArrayList();
                File f = new File(RutaAGrabar);//Ax: ruta donde buscar los archivos
                File[] files = f.listFiles();//array de Files de la carpeta

                for (int i = 0; i < files.length; i++) {

                    File file = files[i];
                    String noPermidtido1 = "E" + NombreArchivos; //Ax: archivos que no se envian : M25160.034  E....
                    String noPermidtido2 = "M" + NombreArchivos;

                    if (!file.isDirectory() && file.getName().toUpperCase().endsWith(ext) && !file.getName().toUpperCase().equals(noPermidtido1) && !file.getName().toUpperCase().equals(noPermidtido2)) {
                        filestoZip.add(file);
                    }
                }

                String comprimir = utils.CreaZip(RutaAGrabar + NombreZip, filestoZip);
                long tamanoArchivo = (new File(RutaAGrabar + NombreZip).length());

                if (!comprimir.contains("✓") || tamanoArchivo == 0) {
                    return "Error Comprimiendo \n Archivos por enviar";
                }
            }
            hash = utils.Md5Hash(RutaAGrabar + NombreZip);

            respuesta = wsoap.TerminalToServerReceive("Terminal_ToServerReceive_2", ArchivoAEnviarRecibir, 1, RutaAGrabar + NombreZip, trama);//Enviar Datos al servidor

            if (!(respuesta.equals("4") || respuesta.equals("1"))) {
                return "Error Enviar Archivo al servidor";
            }
            respuesta = wsoap.Descomprimir("Descomprime", ArchivoAEnviarRecibir, ArchivoAEnviarRecibir + NombreZip, hash, NombreArchivos);

            if (!respuesta.equals("true")) {
                return " Error descomprimir remoto, intentelo de nuevo";
            }

            File file = new File(RutaAGrabar + NombreZip);

            if (file.exists()) file.delete();

        } catch (Exception ex) {
            logger.info("enviarArchivos4_asinc() " + ex.getMessage());
            return ("Error: al enviar \n\n, ....Intente de nuevo");
        } finally {
            estaEnvioAlWsOcupado = false;
        }
        return "Proceso subir archivos Exitoso! ✓";
    }

 /*   public String EnviarFotografiasBucle_2() {//para enviar todas, llama varias veces a los metodos de enviar fotos y para si falla 3 veces
        String msg = "";
        contFuerzaFotos++;
        try {
            if (estaEnvioAlWsOcupado) {
                return "Hay envios de Fotos en proceso...\ninténtelo en unos segundos";
            }
            boolean seguir = true;
            int errors = 0;
            int somsacsalisrop = 0;
            while (seguir) {
                somsacsalisrop++;

                msg = EnviarFotografiasAsinc(false);

                if (msg.contains("✓")) {
                    int temp = fotosFaltantes();
                    if (temp < 1) { //Ax: puede ser 0 o error -1
                        seguir = false;
                        msg = "Enviado correctamente ✓";
                    }
                } else {
                    errors++;
                    if (errors > 2) {
                        seguir = false;
                        if (msg.contains("__")) {
                            msg = msg.replace("__", "");
                            seguir = false;
                        } else {
                            msg = "Han ocurrido inconvenientes\n\n" + msg;
                        }
                    }
                }
//PARA QUITAR SOLO VOY A MONTAR EL ENVIO DE UNA TRAMA DE 25 FOTOS A VER SOMO NOS VA
                // seguir = false;
                if (somsacsalisrop > 250) seguir = false;
            }
        } catch (Exception ex) {
            logger.info("EnviarFotografiasBucle() " + ex.getMessage());
            return "Falló el envio:\n" + ex.getMessage();
        } finally {
            // metodo = "EnviarFotografiasBucle";
        }
        return msg;
    }*/

    /*public void EnviarFotografias() {
        try {
            Log.e("error", "bandera " + estaEnvioAlWsOcupado);
            if (estaEnvioAlWsOcupado) {
                return;
            }
            Log.e("error", "bandera 1 " + estaEnvioAlWsOcupado);

            TaskHelper.execute(new AsyncCallWS(), "EnviarFotosServer");

        } catch (Exception ex) {
            Log.e("error", "bandera " + ex.getMessage());
            logger.info("EnviarFotografias()" + ex.getMessage());
        }
    }*/

    public void mensajeAdm(Activity activity) {

        dialogloading = new Dialog(activity);
        dialogloading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogloading.setCancelable(false);
        dialogloading.setContentView(R.layout.loading_activity);
        TextView textView = dialogloading.findViewById(R.id.txt_loadAct);
        textView.setText(msgAdmon);//"EVALUANDO ESTADISTICAS
        dialogloading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogloading.show();
    }

    //** Proceso nuevo y organizado para enciar las fotos en coleccion modificado por Victor
    /*public String enviarFotografiasAsinc(boolean noMensg) {

        if (estaEnvioAlWsOcupado) {
            return "Hay envíos en proceso... espere";
        }

        String rutaFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/F" + NombreArchivos;
        String nuevoEnvioFotos = archivoEFotos + serialPDA + ".SDA";
        String backupEnvioFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/BKEnvioFotos.SDA";

        // Validaciones iniciales
        if (!validarArchivosExisten(rutaFotos, backupEnvioFotos, nuevoEnvioFotos)) {
            return "Nada por enviar";
        }

        try {
            estaEnvioAlWsOcupadoTrue();//enviarFotografiasAsinc

            // Preparar archivo destino
            File archivoDestino = new File(nuevoEnvioFotos);
            if (!prepararArchivoDestino(archivoDestino)) {
                return "No se pudo preparar archivo destino";
            }

            // Procesar fotografías
            ResultadoProceso resultado = procesarFotografias(rutaFotos, backupEnvioFotos, archivoDestino);

            if (!resultado.exitoso) {
                return resultado.mensaje;
            }

            if (resultado.fotosList.isEmpty()) {
                return "No hay fotografías por enviar __";
            }

            // Crear ZIP
            String rutaZip = crearZipFotografias(resultado.fotosList, archivoDestino);
            if (rutaZip == null) {
                return "Error al crear archivo ZIP";
            }

            // Validar tamaño ZIP
            File zipFile = new File(rutaZip);
            if (zipFile.length() < 8000) {
                zipFile.delete();
                return "ZIP generado es muy pequeño (<8KB)";
            }

            return EnviarFotosgServerAsync(noMensg);

        } catch (IOException e) {
            logger.error("Error I/O al procesar fotografías: " + e.getMessage(), e);
            return "Error procesando archivos";
        } catch (Exception e) {
            logger.error("Error inesperado al enviar fotografías: " + e.getMessage(), e);
            return "Error inesperado al procesar fotografías";
        } finally {
            estaEnvioAlWsOcupado = false;
        }
    }*/

    private boolean validarArchivosExisten(String rutaFotos, String backup, String nuevo) {
        File fFotos = new File(rutaFotos);
        File fBackup = new File(backup);

        return fFotos.exists() && (fBackup.exists() || new File(nuevo).exists());
    }

    private boolean prepararArchivoDestino(File archivo) {
        try {
            if (archivo.exists() && !archivo.delete()) {
                logger.warn("No se pudo eliminar archivo existente: " + archivo.getPath());
                return false;
            }
            return archivo.createNewFile();
        } catch (IOException e) {
            logger.error("Error preparando archivo destino", e);
            return false;
        }
    }


    private boolean esLineaBackupValida(String lineaBackup) {
        if (lineaBackup == null) {
            return false;
        }

        // Longitud esperada sin CRLF
        if (lineaBackup.length() != 73) {
            return false;
        }

        String[] campos = lineaBackup.split(";", -1);
        return campos.length == 4;
    }

    private boolean esLineaFotoPendiente(String linea) {
        if (linea == null || linea.isEmpty()) {
            return false;
        }
        char ultimoChar = linea.charAt(linea.length() - 1);
        return ultimoChar == 'X' || ultimoChar == 'x' || ultimoChar == '_';
    }


    // Clase auxiliar interna
    private static class ResultadoProceso {
        boolean exitoso;
        String mensaje;
        List<String> fotosList;

        ResultadoProceso(boolean exitoso, String mensaje, List<String> fotosList) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.fotosList = fotosList != null ? fotosList : new ArrayList<>();
        }
    }


    public void borradoDcim() {
        try { // Ax: Trata de borrar las fotoS en el directorios propios de Android para fotos, estas fotos no se usan y son pesadas

            File p = new File(VariablesGlobales.directorioactual + "/DCIM/Camera/");
            if (p.isDirectory()) {
                File[] files = p.listFiles();
                for (File f : files) {
                    if (f.getAbsolutePath().toLowerCase().endsWith(".jpg")) {
                        if (!f.delete()) {
                            logger.info("borradoDcim() " + f.getAbsolutePath());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Error al borrar fotografia de /DCIM/Camera/");
        }
    }

    //***nueva por victor recuperar los no enviados
    //   java


    private String ValidarNoEnviados() {
        try {
            Log.i("INFO", "=== INICIO ValidarNoEnviados ===");

            // ===== DETECTAR SI REALM ESTÁ VACÍO =====
            int totalEnRealm = CrudEnvioLectura.contarTodos();
            int totalFotosEnRealm = CrudEnvioFoto.contarTodos();

            Log.i("INFO", "Total en Realm: " + totalEnRealm + " lecturas, " + totalFotosEnRealm + " fotos");

            // Si Realm está vacío, hacer reconstrucción total
            if (totalEnRealm == 0 && totalFotosEnRealm == 0) {
                Log.i("INFO", "Realm vacío - iniciando reconstrucción total desde archivos");
                return reconstruirTodoDesdeArchivos();
            }

            // ===== FLUJO NORMAL: REALM TIENE DATOS =====

            ResultadoValidacion resultadoReconstruccion = null;

            // Paso 1: Validar archivos de envío pendientes
            String validacionEnvios = validarArchivosPendientes();
            if (validacionEnvios != null) {
                Log.w("INFO", "Archivos pendientes: " + validacionEnvios);
            }

            // Paso 2: Preparar archivo de backup de envíos
            File archivoNoEnviadosF = prepararArchivoBackupEnvios();
            if (archivoNoEnviadosF == null) {
                Log.e("INFO", "No se pudo preparar archivo backup envíos");
            }

            // Paso 3: Preparar archivo de backup de fotos
            File archivoBackupFotos = prepararArchivoBackupFotos();
            if (archivoBackupFotos == null) {
                Log.e("INFO", "No se pudo preparar archivo backup fotos");
            }

            // Paso 4: Procesar registros (reconstruir pendientes que no estén en backup)
            if (archivoNoEnviadosF != null && archivoBackupFotos != null) {
                Log.i("INFO", "Procesando registros no enviados...");
                resultadoReconstruccion = procesarRegistrosNoEnviados(archivoNoEnviadosF, archivoBackupFotos);

                if (resultadoReconstruccion.cuentasProblema != null &&
                        !resultadoReconstruccion.cuentasProblema.isEmpty()) {
                    logger.warn("Cuentas con problemas: " + resultadoReconstruccion.cuentasProblema);
                    CuentasProblema = resultadoReconstruccion.cuentasProblema;
                }
            }

            // ===== FASE 2: ESCANEAR FOTOS PENDIENTES =====
            int fotosEscaneadas = escanearYRecuperarTodasLasFotos();

            // ===== FASE 3: CONSULTAR ESTADÍSTICAS DE REALM =====

            int lecturasPendientes = CrudEnvioLectura.contarPendientes();
            int fotosPendientes = CrudEnvioFoto.contarPendientes();
            int cuentasPendientes = CrudEnvioCuentaNueva.contarPendientes();

            int[] lecStats = CrudEnvioLectura.getEstadisticas();
            int[] fotoStats = CrudEnvioFoto.getEstadisticas();
            int[] cuentaStats = CrudEnvioCuentaNueva.getEstadisticas();

            int lecturasReintentadas = CrudEnvioLectura.reintentar(ModoReenvio.SOLO_ERROR);
            int fotosReintentadas = CrudEnvioFoto.reintentar(ModoReenvio.SOLO_ERROR);
            int cuentasReintentadas = CrudEnvioCuentaNueva.reintentar(ModoReenvio.SOLO_ERROR);

            // ValidarNoEnviados ya reencoló y recuperó de archivos: despachar
            // de inmediato en vez de esperar hasta 2 min al tick periódico.
            if (lecturasReintentadas + fotosReintentadas + cuentasReintentadas > 0) {
                LecturaSyncService.forzarSync(MenuDeLiquidacion.this);
            }

            int totalReintentados = lecturasReintentadas + fotosReintentadas + cuentasReintentadas;

            // ===== FASE 4: CONSTRUIR MENSAJE =====

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✓ VALIDACIÓN COMPLETADA\n\n");

            if (resultadoReconstruccion != null &&
                    (resultadoReconstruccion.leidasNoEnviadas > 0 || resultadoReconstruccion.fotosRecuperadas > 0)) {
                mensaje.append("🔄 RECUPERADOS:\n");
                mensaje.append("   Lecturas: ").append(resultadoReconstruccion.leidasNoEnviadas).append("\n");
                mensaje.append("   Fotos: ").append(resultadoReconstruccion.fotosRecuperadas).append("\n\n");
            }

            if (fotosEscaneadas > 0) {
                mensaje.append("📸 Fotos encontradas en carpeta: ").append(fotosEscaneadas).append("\n\n");
            }

            mensaje.append("📄 LECTURAS EN REALM:\n");
            mensaje.append("   Pendientes: ").append(lecturasPendientes).append("\n");
            mensaje.append("   Enviadas: ").append(lecStats[1]).append("\n");
            mensaje.append("   Con error: ").append(lecStats[2]).append("\n\n");

            mensaje.append("📷 FOTOS EN REALM:\n");
            mensaje.append("   Pendientes: ").append(fotosPendientes).append("\n");
            mensaje.append("   Enviadas: ").append(fotoStats[1]).append("\n");
            mensaje.append("   Con error: ").append(fotoStats[2]).append("\n\n");

            mensaje.append("🆕 CUENTAS NUEVAS:\n");
            mensaje.append("   Pendientes: ").append(cuentasPendientes).append("\n");
            mensaje.append("   Enviadas: ").append(cuentaStats[1]).append("\n");
            mensaje.append("   Con error: ").append(cuentaStats[2]).append("\n\n");

            if (totalReintentados > 0) {
                mensaje.append("🔄 Reintentados: ").append(totalReintentados).append("\n");
            }

            mensaje.append("\n📡 El servicio enviará automáticamente");

            String resultado = mensaje.toString();
            Log.i("MenuDeLiquidacion", resultado);
            logger.info(resultado);

            return resultado;

        } catch (Exception ex) {
            Log.e("INFO", "[MenuDeLiquidacion]ValidarNoEnviados|ERROR| " + ex.getMessage());
            logger.error("[MenuDeLiquidacion]ValidarNoEnviados|ERROR| " + ex.getMessage(), ex);
            return "ERROR: " + ex.getMessage();
        }
    }


// ========================================================================
// MÉTODOS DE VALIDACIÓN INICIAL
// ========================================================================

    /**
     * Valida si existen archivos pendientes de envío
     */
    private String validarArchivosPendientes() {
        // Archivo 1: ENVIOSGPRS.SDA
        File archivo1 = new File(VariablesGlobales.directorioactual +
                VariablesGlobales.getCarpetaLecturas() + "/ENVIOSGPRS.SDA");
        if (archivo1.exists()) {
            if (archivo1.length() == 0) {
                archivo1.delete();
            } else {
                return "Aún existen registros sin enviar\n" +
                        "Por favor enviarlos antes de esta verificación\n" +
                        archivo1.getAbsolutePath();
            }
        }

        // Archivo 2: ENVIOGPRS[SERIAL].SDA
        File archivo2 = new File(VariablesGlobales.directorioactual +
                VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS" + serialPDA + ".SDA");
        if (archivo2.exists()) {
            if (archivo2.length() == 0) {
                archivo2.delete();
            } else {
                return "Aún existen registros sin enviar\n" +
                        "Por favor enviarlos antes de esta verificación\n" +
                        archivo2.getAbsolutePath();
            }
        }

        return null; // No hay archivos pendientes
    }

    /**
     * Prepara el archivo de backup de envíos
     */
    private File prepararArchivoBackupEnvios() {
        return prepararArchivoBackup(
                VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKENVIOSGPRS.SDA",
                "BKENVIOSGPRS.SDA"
        );
    }

    /**
     * Prepara el archivo de backup de fotos
     */
    private File prepararArchivoBackupFotos() {
        return prepararArchivoBackup(
                VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKEnvioFotos.SDA",
                "BKEnvioFotos.SDA"
        );
    }

    /**
     * Método genérico para preparar archivos de backup
     */
    private File prepararArchivoBackup(String ruta, String nombreArchivo) {
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            try {
                if (!archivo.createNewFile()) {
                    logger.error("No se pudo crear archivo " + nombreArchivo);
                    return null;
                }
                logger.info("Archivo creado: " + nombreArchivo);
            } catch (IOException e) {
                logger.error("Error creando " + nombreArchivo + ": " + e.getMessage(), e);
                return null;
            }
        }

        return archivo;
    }

// ========================================================================
// PROCESAMIENTO DE REGISTROS
// ========================================================================

    /**
     * Procesa todos los registros para validar no enviados y fotos
     */
    private ResultadoValidacion procesarRegistrosNoEnviados(File archivoNoEnviadosF,
                                                            File archivoBackupFotos) {
        ResultadoValidacion resultado = new ResultadoValidacion();

        misenvios.archivo_EnvioGPS = archivoNoEnviadosF.getAbsolutePath();

        if (!misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS)) {
            logger.error("No se pudo abrir archivo de envíos GPS");
            return resultado;
        }

        try {
            abrirArchivosDeFacturacion();
            int numRegistroOriginal = VariablesGlobales.registroactual;
            VariablesGlobales.registroactual = 1;

            int totalRegistros = infoRegistroSalida.getTotal_TablaRegistroSalida();
            logger.info("Procesando " + totalRegistros + " registros...");

            while (VariablesGlobales.registroactual <= totalRegistros) {
                leerInformacionUsuario(1);

                String fechaLectura = infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim();
                String leido = infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim();
                String cuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();

                // ❌ SI NO TIENE FECHA DE LECTURA = NO LEÍDA
                if (fechaLectura.isEmpty()) {
                    resultado.noLeidas++;
                    VariablesGlobales.registroactual++;
                    continue;
                }

                // ❌ SI NO TIENE CAMPO "LEIDO" = CUENTA PROBLEMA
                if (leido.isEmpty()) {
                    resultado.cuentasProblema += cuenta + "/";
                    VariablesGlobales.registroactual++;
                    continue;
                }

                // ✅ TIENE FECHA Y LEIDO - PROCESAR

                // Sincronizar lectura modificada con lectura tomada
                sincronizarLecturaModificada();

                // 🔍 BUSCAR SI YA FUE ENVIADA
                String nroContador = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();
                String tipoMedida = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA();

                misenvios.BuscarSecuencial_EnvioGPS(cuenta, nroContador, tipoMedida);

                // ✅ SI NO ESTÁ EN ENVIOS (encontro_EnvioGPS == 0) = RECUPERAR
                if (misenvios.encontro_EnvioGPS == 0) {
                    resultado.leidasNoEnviadas++;

                    misenvios.Cerrar_EnvioGPS();

                    // 📝 GUARDAR EL REGISTRO NO ENVIADO
                    guardarRegistroNoEnviado();

                    // Reabrir el archivo para seguir buscando
                    misenvios.archivo_EnvioGPS = archivoNoEnviadosF.getAbsolutePath();
                    misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS);

                    logger.info("Registro recuperado: Cuenta " + cuenta +
                            " | Contador: " + nroContador);
                }

                // 📸 VALIDAR Y RECUPERAR FOTOGRAFÍAS
                if (validarYRecuperarFotografia(archivoBackupFotos, cuenta)) {
                    resultado.fotosRecuperadas++;
                }

                VariablesGlobales.registroactual++;
            }

            misenvios.Cerrar_EnvioGPS();
            VariablesGlobales.registroactual = numRegistroOriginal;

        } catch (Exception e) {
            logger.error("Error procesando registros: " + e.getMessage(), e);
        } finally {
            try {
                if (misenvios != null) {
                    misenvios.Cerrar_EnvioGPS();
                }
            } catch (Exception e) {
                logger.error("Error cerrando archivos: " + e.getMessage(), e);
            }
        }

        return resultado;
    }

    /**
     * Sincroniza la lectura modificada con la lectura tomada si es necesario
     */
    private void sincronizarLecturaModificada() {
        String lecturaModificada = infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim();
        String lecturaTomada = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();

        if (!lecturaModificada.isEmpty() && lecturaTomada.isEmpty()) {
            infoRegistroSalida.settablaRegistroSalida_lecturatomada(lecturaModificada);
            infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
        }
    }

    /**
     * 📝 GUARDA UN REGISTRO QUE NO HA SIDO ENVIADO
     * Extrae todos los datos del registro actual y llama a guardarDatosAEnviarNuevo()
     */
    private void guardarRegistroNoEnviado() {
        // Obtener todos los datos del registro actual
        String nroContador = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();
        String idContador = infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES();
        String lecturaTomada = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();

        // Formatear campos con padding
        String causaNolectura = formatearCampo(
                infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim(), 2, "0");

        String intentos = formatearCampo(
                infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim(), 1, "0");

        String lecturaModificada1 = formatearCampo(
                infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim(), 10, "0");

        String lecturaModificada2 = formatearCampo(
                infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim(), 10, "0");

        String digitos = formatearCampo(
                infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim(), 1, "0");

        String criticaPDA = infoRegistroSalida.gettablaRegistroSalida_LEIDO();

        String lecturaAnterior = String.valueOf(
                parseStringToInteger_1(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR()));

        // 🔍 Obtener código de barras si aplica
        String codBarras = "";
        if (VariablesGlobales.getObligabarras().equals("1")) {
            codBarras = obtenerCodigoBarras();
        }

        // 💾 LLAMAR A LA FUNCIÓN QUE GUARDA EN EL ARCHIVO
        guardarDatosAEnviarNuevo(
                nroContador,
                idContador,
                lecturaTomada,
                causaNolectura,
                intentos,
                lecturaModificada1,
                lecturaModificada2,
                digitos,
                criticaPDA,
                lecturaAnterior,
                99999,
                codBarras
        );
    }

    /**
     * Formatea un campo con padding de ceros o espacios
     */
    private String formatearCampo(String valor, int longitud, String caracter) {
        if (valor == null || valor.isEmpty()) {
            valor = "";
        }
        return String.format("%1$" + longitud + "s", valor).replace(" ", caracter);
    }

    /**
     * Obtiene el código de barras del archivo correspondiente
     */
    private String obtenerCodigoBarras() {
        String codBarras = "";

        try {
            String archivoBarras = VariablesGlobales.directorioactual +
                    VariablesGlobales.getCarpetaLecturas() + "/S" + NombreArchivos;
            File file = new File(archivoBarras);

            if (file.exists()) {
                infoCodBarras.setArchivo_TablaCodBarras(archivoBarras);

                if (infoCodBarras.abrir_TablaCodBarras(archivoBarras)) {
                    infoCodBarras.lectura_TablaCodBarras(VariablesGlobales.registroactual);
                    codBarras = infoCodBarras.gettablaCodBarras_CODBARRAS();
                    infoCodBarras.Cerrar_TablaCodBarras();
                }
            }
        } catch (Exception e) {
            logger.error("Error obteniendo código de barras: " + e.getMessage(), e);
        }

        return codBarras;
    }

    /**
     * Convierte un String a Integer, manejando valores vacíos o inválidos
     */
    private int parseStringToInteger_1(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            logger.warn("Valor no numérico: " + valor);
            return 0;
        }
    }

// ========================================================================
// VALIDACIÓN Y RECUPERACIÓN DE FOTOGRAFÍAS
// ========================================================================

    /**
     * ✅ Valida y recupera TODAS las fotografías de una cuenta
     * Busca fotos con patrón: CUENTA_TIPOMEDIDA_01.jpg, CUENTA_TIPOMEDIDA_02.jpg, etc.
     */

//*******mirar este caso
    // VALIDACIÓN Y RECUPERACIÓN DE FOTOGRAFÍAS
// ========================================================================

    /**
     * ✅ Valida y recupera TODAS las fotografías de una cuenta
     * Busca fotos con patrón: CUENTA_TIPOMEDIDA_01.jpg, CUENTA_TIPOMEDIDA_02.jpg, etc.
     */
    private boolean validarYRecuperarFotografia(File archivoBackupFotos, String cuenta) {
        try {
            String tipoMedida = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA();
            String directorioFotos = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/";

            // Patrón de búsqueda: CUENTA_TIPOMEDIDA_
            String patronBusqueda = cuenta.trim() + "_" + tipoMedida + "_";

            File directorio = new File(directorioFotos);
            if (!directorio.exists() || !directorio.isDirectory()) {
                return false;
            }

            // Obtener todos los archivos del directorio
            File[] todosLosArchivos = directorio.listFiles();

            if (todosLosArchivos == null || todosLosArchivos.length == 0) {
                return false;
            }

            // Filtrar manualmente las fotos que coincidan
            List<File> fotosEncontradas = new ArrayList<File>();
            for (File archivo : todosLosArchivos) {
                String nombre = archivo.getName();
                if (nombre.startsWith(patronBusqueda) && nombre.toLowerCase().endsWith(".jpg")) {
                    fotosEncontradas.add(archivo);
                }
            }

            if (fotosEncontradas.size() == 0) {
                return false; // No hay fotos para esta cuenta
            }

            boolean alMenosUnaRecuperada = false;

            // Procesar cada foto encontrada
            for (File archivoFoto : fotosEncontradas) {
                String nombreFoto_1 = archivoFoto.getName();

                // Verificar si ya está en el backup
                if (fotoYaEstaEnBackup(archivoBackupFotos, nombreFoto_1)) {
                    continue; // Ya registrada
                }

                // Agregar al backup
                if (agregarFotoAlBackup(archivoBackupFotos, nombreFoto_1, cuenta)) {
                    alMenosUnaRecuperada = true;
                }
            }

            return alMenosUnaRecuperada;

        } catch (Exception e) {
            logger.error("Error validando fotografías para cuenta " + cuenta + ": " +
                    e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica si una foto ya está registrada en el backup
     * Formato de línea en archivo: CUENTA;CONTADOR;NOMBREFOTO.jpg;
     * Ejemplo: 2020063;723696;723696_A3_01.jpg;
     */
    private boolean fotoYaEstaEnBackup(File archivoBackup, String nombreFoto_1) {
        if (!archivoBackup.exists() || archivoBackup.length() == 0) {
            return false;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(archivoBackup));
            String linea;

            while ((linea = reader.readLine()) != null) {
                // Extraer el nombre de la foto de la línea
                String fotoEnLinea = extraerNombreFotoDeLinea(linea);

                if (fotoEnLinea != null && fotoEnLinea.equals(nombreFoto_1)) {
                    return true; // Foto ya existe en el backup
                }
            }

            return false; // No encontrada

        } catch (IOException e) {
            logger.error("Error verificando foto en backup: " + e.getMessage(), e);
            return true; // En caso de error, asumir que existe para evitar duplicados
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Error cerrando reader: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * ✅ VERSIÓN RECOMENDADA - Simple y eficiente
     * Extrae el nombre de la foto de una línea del archivo
     * Formato esperado: CUENTA;CONTADOR;NOMBREFOTO.jpg;
     * Ejemplo: 2020063;723696;723696_A3_01.jpg;
     * Retorna: 723696_A3_01.jpg
     */
    private String extraerNombreFotoDeLinea(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        try {
            // Dividir por punto y coma
            String[] partes = linea.split(";");

            // La foto está en la tercera posición (índice 2)
            // Formato: CUENTA;CONTADOR;NOMBREFOTO.jpg;
            if (partes.length >= 3) {
                String nombreFoto_1 = partes[2].trim();

                // Validar que no esté vacío y termine en .jpg
                if (!nombreFoto_1.isEmpty() && nombreFoto_1.toLowerCase().endsWith(".jpg")) {
                    return nombreFoto_1;
                }
            }

        } catch (Exception e) {
            logger.warn("Error procesando línea del backup de fotos: " + linea);
        }

        return null;
    }


    private boolean agregarFotoAlBackup(File archivoBackup, String nombreFoto_1, String cuenta) {
        try {
            // Obtener datos adicionales del registro actual
            String nroContador = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();
            String tipoMedidor = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA();
            String anno = variables.amd.substring(0, 4);
            String mes = variables.amd.substring(4, 6);
            String ciclo = infoRegistroSalida.gettablaRegistroSalida_CICLO();
            idFoto = infoRegistroSalida.getId();

            // Construir línea en formato: ID;CUENTA;NOMBREFOTO.jpg;
            String lineaFoto = String.format("%11s", idFoto) + ";" +
                    String.format("%10s", cuenta) + ";" +
                    String.format("%-50s", nombreFoto_1.trim());

            // Escribir al archivo (backup tradicional)
            Utils utils = new Utils();
            utils.EscribirLinea(archivoBackup, lineaFoto + ";\r\n");

            // ===== NUEVO: GUARDAR EN REALM =====
            String rutaLocal = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + nombreFoto_1;
            File archivoFoto = new File(rutaLocal);

            if (archivoFoto.exists()) {
                // Determinar campo destino basado en el nombre (cuenta_tipo_01.jpg)
                String campoDestino = determinarCampoDestinoFoto(nombreFoto_1);

                boolean guardadoEnRealm = SyncHelper.guardarFotoParaEnvio(
                        rutaLocal,           // Ruta local del archivo
                        cuenta,              // Código de cuenta
                        tipoMedidor,         // Tipo de medidor
                        idFoto,              // ID del registro
                        anno,                // Año
                        mes,                 // Mes
                        ciclo,               // Ciclo
                        campoDestino         // foto_1, foto_2 o foto_3
                );

                if (guardadoEnRealm) {
                    Log.i("MenuDeLiquidacion", "Foto guardada en Realm: " + nombreFoto_1);
                } else {
                    Log.w("MenuDeLiquidacion", "No se pudo guardar foto en Realm: " + nombreFoto_1);
                }
            }
            // ===== FIN GUARDAR EN REALM =====

            logger.info("Foto recuperada: " + nombreFoto_1 + " | Cuenta: " + cuenta +
                    " | Contador: " + nroContador);

            return true;

        } catch (Exception e) {
            logger.error("Error agregando foto al backup (" + nombreFoto_1 + "): " +
                    e.getMessage(), e);
            return false;
        }
    }

    /**
     * Determina el campo destino (foto_1, foto_2, foto_3) basado en el nombre de la foto
     * Formato: cuenta_tipoMedidor_secuencia.jpg (ej: 123456_A3_01.jpg)
     */
    private String determinarCampoDestinoFoto(String nombreFoto) {
        try {
            String nombreUpper = nombreFoto.toUpperCase();

            // Si contiene FM es firma
            if (nombreUpper.contains("FM")) {
                return "firma";
            }

            // Si contiene CN es cuenta nueva (no se guarda en BD)
            if (nombreUpper.contains("CN")) {
                return "";
            }

            // Parsear nombre: 123456_A1_01.jpg
            String sinExtension = nombreFoto.substring(0, nombreFoto.lastIndexOf('.'));
            String[] partes = sinExtension.split("_");

            if (partes.length >= 3) {
                int secuencia = Integer.parseInt(partes[partes.length - 1]);
                if (secuencia <= 1) {
                    return "foto_1";
                } else if (secuencia == 2) {
                    return "foto_2";
                } else {
                    return "foto_3";
                }
            }

            // Fallback: revisar si termina en 1.JPG, 2.JPG
            if (nombreUpper.endsWith("1.JPG")) {
                return "foto_1";
            } else if (nombreUpper.endsWith("2.JPG")) {
                return "foto_2";
            }

        } catch (Exception e) {
            Log.w("MenuDeLiquidacion", "Error determinando campo destino foto: " + e.getMessage());
        }

        // Por defecto
        return "foto_1";
    }

// ========================================================================
// CLASE AUXILIAR PARA RESULTADOS
// ========================================================================

    /**
     * Clase auxiliar para retornar resultados de la validación
     */
    private static class ResultadoValidacion {
        int noLeidas = 0;
        int leidasNoEnviadas = 0;
        int fotosRecuperadas = 0;
        String cuentasProblema = "";
    }
    // fin por victor de los no enviados

    ///******************************************************************************
//se clausura el original ya que se entra a evaluar si el nombre de la foto tambien falta
    private String ValidarNoEnviados_2() {   //validar lo leido contra el backup de enviados y si no esta ahi reproducir dicho registro
        try {
            Log.e("INFO", "entra a ValidarNoEnviados1");
            String msg = "";
            String Codbarras = "";
            File Archivo1 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOSGPRS.SDA");
            if (Archivo1.exists()) {
                if (Archivo1.length() == 0) {
                    Archivo1.delete();
                } else {
                    if (dialogloading.isShowing()) {
                        dialogloading.dismiss();
                    }
                    //mensajeT("Aun Existen Registros sin Enviar\nPor favor enviarlos antes de esta verificacion\n"
                    //        + VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA", msgLargo);
                    //return;
                    msg = "Aun Existen Registros sin Enviar\nPor favor enviarlos antes de esta verificacion\n"
                            + VariablesGlobales.directorioactual + "VariablesGlobales.getCarpetaLecturas()+\"/ENVIOSGPRS.SDA";
                    return msg;
                }
            }

            File Archivo2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS" + serialPDA + ".SDA");

            if (Archivo2.exists()) {
                if (Archivo2.length() == 0) {
                    Archivo2.delete();
                } else {

                    msg = "Aun Existen Registros sin Enviar\nPor favor enviarlos antes de esta verificacion\n"
                            + VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS" + serialPDA + ".SDA";
                    return msg;
                }

            }

            File ArchivoNoEnviadosF = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/BKENVIOSGPRS.SDA");

            if (!ArchivoNoEnviadosF.exists()) {
                //mensajeT("Aun No Existe Backup de Envios...Se buscara si hay cuentas leidas sin enviar", msgCorto);
                try {
                    ArchivoNoEnviadosF.createNewFile();
                } catch (Exception e) {
                    logger.info("[MenuDeLiquidacion]ValidarNoEnviados() error Creando " + ArchivoNoEnviadosF.getAbsolutePath());
                    //mensajeT("Error al crear BkEnviosGPRS.", msgCorto);
                    //return;
                    msg = "Error al crear BkEnviosGPRS";
                    return msg;
                }
            }


            misenvios.archivo_EnvioGPS = ArchivoNoEnviadosF.getAbsolutePath();

            if (misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS)) {
                abrirArchivosDeFacturacion();
                int NumRegistro = VariablesGlobales.registroactual;
                VariablesGlobales.registroactual = 1;

                while (VariablesGlobales.registroactual <= infoRegistroSalida.getTotal_TablaRegistroSalida()) {
                    leerInformacionUsuario(1);// valida no enviados

                    if (!infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim().equals("")) {

                        if (!infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {
                            if (!infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim().equals("")
                                    && infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("")) {
                                infoRegistroSalida.settablaRegistroSalida_lecturatomada(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1());
                                infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                            }

                            misenvios.BuscarSecuencial_EnvioGPS(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA());//Ax: antes era gettablaRegistroSalida_CONSECUTIVO

                            if (misenvios.encontro_EnvioGPS == 0) {

                                LeidasNoenviadas++;
                                misenvios.Cerrar_EnvioGPS();
                                //este es el proceso de entrar a grabr la informacion en el archivo de envios ya que no se han enviado
                                nroContador1 = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();
                                idContador1 = infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES();
                                lecturaTomada1 = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();
                                String x = String.format("%1$2s", infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim()).replace(" ", "0");
                                causadenolectura1 = x;

                                x = String.format("%1$1s", infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim()).replace(" ", "0");
                                intentos1 = x;

                                x = String.format("%1$10s", infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim()).replace(" ", "0");
                                lecturaModificada11 = x;

                                x = String.format("%1$10s", infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim()).replace(" ", "0");
                                lecturaModificada21 = x;

                                x = String.format("%1$1s", infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim()).replace(" ", "0");
                                digitos1 = x;

                                criticaPDA1 = infoRegistroSalida.gettablaRegistroSalida_LEIDO();
                                lecturaAnterior1 = "" + parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR());
                                //if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01")) {
                                if (VariablesGlobales.getObligabarras().equals("1")) {
                                    infoCodBarras.setArchivo_TablaCodBarras(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "S" + NombreArchivos);
                                    File file = new File(infoCodBarras.getArchivo_TablaCodBarras());
                                    if (file.exists()) {
                                        infoCodBarras.abrir_TablaCodBarras(infoCodBarras.getArchivo_TablaCodBarras());
                                        infoCodBarras.lectura_TablaCodBarras(VariablesGlobales.registroactual);
                                        Codbarras = infoCodBarras.gettablaCodBarras_CODBARRAS();
                                        infoCodBarras.Cerrar_TablaCodBarras();
                                    }
                                    //}
                                }
                                guardarDatosAEnviarNuevo(nroContador1, idContador1, lecturaTomada1, causadenolectura1, intentos1, lecturaModificada11, lecturaModificada21, digitos1, criticaPDA1, lecturaAnterior1, 99999, Codbarras);
                                misenvios.archivo_EnvioGPS = ArchivoNoEnviadosF.getAbsolutePath();
                                misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS);
                            }
                        } else {
                            //almacenar cuentas problema
                            CuentasProblema += infoRegistroSalida.gettablaRegistroSalida_CUENTA() + "/";
                        }
                    } else
                        Noleidas++;
                    VariablesGlobales.registroactual++;
                }

                if (!CuentasProblema.trim().equals("")) {
                    //se comenta porq muestra las ue no se han leido y es ilogico que haga esto
                    // mensajeT("Cuentas Problema:\n" + CuentasProblema, msgMedio);
                }

                misenvios.Cerrar_EnvioGPS();
                VariablesGlobales.registroactual = NumRegistro;
                //leerInformacionUsuario(1);
                //visualizarInformacionCliente(0);
                //cerrarArchivosFacturacion();
            }
            //mensajeT("PROCESO CONCLUIDO CON:\n" + "No Leidas: " + Noleidas + "\n" + "Recuperadas para Enviar: " + LeidasNoenviadas, msgLargo);
            msg = "VALIDACION REALIZADA ...Recuperadas..." + LeidasNoenviadas;
            return msg;

        } catch (Exception ex) {
            logger.info("[MenuDeLiquidacion]ValidarNoEnviados|ERROR| " + ex.getMessage());
            return "ERROR: " + ex.getMessage();
        }
    }

    public void capturaImagenFotografica() {
        abrirArchivosDeFacturacion();
        tomaFotoBoton = false; //tomaFotoBoton = true que la foto de forma manual no se vea como la que cubre la necesitad de la foto es porque el quizo
        // esto si vamos a registrar si la foto se tomo o no se tomo
        if (VariablesGlobales.tipoDeRuta.trim().equals("E")) {
            if (ControlEspecialZonaRoja.trim().equals("000")) {
                ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 0, "E", 1);
            }
        } else {
            if (ControlEspecialZonaRoja.trim().equals("000")) {
                ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 0, "", 1);
            }
        }

        cerrarArchivosFacturacion();
    }

    private void visualizarPredioLeido() {

        if (variables.ultimoregistro < 1) {
            mensajeT("No existe un ultimo predio leido", msgCorto);
            return;
        }

        abrirArchivosDeFacturacion();

        alterno = VariablesGlobales.registroactual;
        VariablesGlobales.registroactual = variables.ultimoregistro;
        leerInformacionUsuario(1);
        visualizarInformacionCliente(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta de modificar lectura");
        builder.setMessage("Desea Reprocesar el Cliente?");

        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                cerrarArchivosFacturacion();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                VariablesGlobales.registroactual = alterno;
                leerInformacionUsuario(1);
                visualizarInformacionCliente(0);
                cerrarArchivosFacturacion();
            }
        });
        builder.show();
    }

    private void abrirArchivosDeFacturacion() {
        infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());
    }

    private void cerrarArchivosFacturacion() {
        infoRegistroSalida.Cerrar_TablaRegistroSalida();
    }

    private void abrirArchivosPrincipales() {

        String mensaje = "";

        anomaliaDeLectura.setArchivo_AnomaliaDeNoLectura(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/CAUSAS.TXT");
        notiFicaciones.setarchivo_Notificaciones(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/NOTIFICACIONES.TXT");
        misRangos.setarchivo_Rangos(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/RANGOS.TXT");
        infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "D" + NombreArchivos);
        // infoCodBarras.setArchivo_TablaCodBarras(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "S" + NombreArchivos);

        int faltanarchivos = 0;
        File file = new File(misRangos.getarchivo_Rangos());//infoRegistroEntrada.getArchivo_TablaRegistroDeEntrada()

        if (file.exists()) {
            file = new File(infoRegistroSalida.getArchivo_TablaRegistroSalida());
            if (file.exists()) {

                abrirArchivosDeFacturacion();
                inicializarVariablesSistema();
                ejecutarResumen(3);
                variables.numdigitos = 0;
                visualizarInformacionCliente(0);
                variables.direcciondelectura = variables.haciaadelante;
                faltanarchivos = 1;
                cerrarArchivosFacturacion();
            } else
                mensaje = "No existe:\n" + infoRegistroSalida.getArchivo_TablaRegistroSalida();
        } else
            mensaje = "No existe:" + misRangos.getarchivo_Rangos();//infoRegistroEntrada.getArchivo_TablaRegistroDeEntrada();

        if (faltanarchivos == 0) {
            mensajeT(mensaje + " \n Debe Cargar Archivos...", msgLargo);
            cerrarProcesoFacturacion();
        }
        return;
    }

    private void abriryEscribirBarras(String stiker) {

        String mensaje = "";

        infoCodBarras.setArchivo_TablaCodBarras(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "S" + NombreArchivos);
        int faltanarchivos = 0;
        File file = new File(infoCodBarras.getArchivo_TablaCodBarras());
        if (file.exists()) {
            capturarCoordenadasPredio();
            infoCodBarras.abrir_TablaCodBarras(infoCodBarras.getArchivo_TablaCodBarras());
            infoCodBarras.lectura_TablaCodBarras(VariablesGlobales.registroactual);
            infoCodBarras.settablaCodBarras_CODBARRAS(stiker);
            tomarFechaSistema(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA(), infoRegistroSalida.gettablaRegistroSalida_HORALECTURA());
            infoCodBarras.settablaCodBarras_fechahoralectura(variables.amd.substring(0, 4) + "-" +
                    variables.amd.substring(4, 6) + "-" +
                    variables.amd.substring(6, 8) + " " +
                    variables.hm.substring(0, 2) + ":" +
                    variables.hm.substring(2, 4) + ":" +
                    variables.hm.substring(4, 6));
            //infoCodBarras.settablaCodBarras_horalectura(variables.hm);

            infoCodBarras.settablaCodBarras_LECTOR(String.format("%1$4s", lector));

            if (txtLatitud.getText().toString().trim().length() > 20) {
                infoCodBarras.settablaCodBarras_cordenadax(txtLatitud.getText().toString().trim().substring(0, 20));
            } else {
                infoCodBarras.settablaCodBarras_cordenadax(txtLatitud.getText().toString().trim());
            }
            if (txtLongitud.getText().toString().trim().length() > 20) {
                infoCodBarras.settablaCodBarras_cordenaday(txtLongitud.getText().toString().trim().substring(0, 20));
            } else {
                infoCodBarras.settablaCodBarras_cordenaday(txtLongitud.getText().toString().trim());//"1234567890123456789012"
            }
            infoCodBarras.settablaCodBarras_OBSERVACION(infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1());
            infoCodBarras.escribir_TablaCodBarras(VariablesGlobales.registroactual);
            //incluir los datos para guardar y cerrar de nuevo aqui solo se escribe
            infoCodBarras.Cerrar_TablaCodBarras();

        } else
            mensaje = "No existe:\n" + infoCodBarras.getArchivo_TablaCodBarras();

        return;
    }

    private void ejecutarResumen(int TipoResumen) {

        if (VariablesGlobales.registroactual == 0) {
            VariablesGlobales.registroactual = 1;
        }

        leerInformacionUsuario(1);//ax antes 3
        visualizarInformacionCliente(1);//ax antes 3
        apuntadortarifareactiva = 0;
        apuntadortarifaCT = 0;
    }

    private void inicializarVariablesSistema() {

        variables.mesenformato = "";
        variables.fechahoy = "";
        variables.terminal = "001";
        variables.impresora = "   ";
        variables.modosupervisor = 0;
        variables.tipoimpresora = 1;
        variables.ultimotiempo = 1;
        variables.barrido = 1;
        tomarFechaSistema(variables.amd, variables.hm);
        convertirFormatoDeFecha(variables.amd, variables.fechahoy, variables.mesenformato, "AMD");
        variables.diashoy = (int) variables.calcularNumerodeDias(variables.amd);
        VariablesGlobales.totalimpresiones = 0;
    }

    private void cerrarProcesoFacturacion() {

        try {
            cerrarArchivosFacturacion();

            final File g = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LEIDO.TXT");
            if (!g.exists()) {
                utils.EscribirLinea(g, " ");
            }

            if (estaEnvioAlWsOcupado) {
                mensajeT("El sistema esta en proceso de envio de datos al servidor no se puede salir del sistema", msgCorto);
                return;
            }

            /*File archivo1 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA");

            if (archivo1.length() > 0) {

                if (!estaEnvioAlWsOcupado) {
                    envioFacturacionHilos();
                }
            } else {
                archivo1.delete();
            }*/

//            if (!estaEnvioAlWsOcupado ) {
//                opcionMenu(13);
//            }
            //opcionMenu(16);
            finish();
        } catch (Exception e) {
            mensajeT("Problema cerrando Formulario de Lecturas", msgLargo);
            e.printStackTrace();
        }
    }

    private String getPhoneDate() {//Retorna la fecha actual en formato dd/MM/yyyy

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); //Ax. mes aparentemente ok
        String formatteDate = df.format(date);

        return formatteDate;
    }

    public static String getPhoneHour() {//Retorna la fecha Actual del celular en formato 'HH:mm:ss'

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteHour = df.format(dt.getTime());
        return formatteHour;
    }

    private void capturarAnomaliaTerreno(boolean filter, boolean filter2, boolean menu) {

        if (VariablesGlobales.habilitadaimpresora == 0
                && !impresoraObligatoriaDeshabilitadaPorSupervisor
                && !VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
            /*if ( ControlEspecialZonaRoja.trim().equals("000") && !new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/SINIMPRESORA.TXT").exists()) {
                mostrarDialogoAlerta("ALERTA DEL SISTEMA", "IMPRESORA NO VINCULADA, este sistema requiere la impresora activa y vinculada con el movil");
                return;
            }*/

            if (ControlEspecialZonaRoja.trim().equals("000")) {
                if (ControlEspecialImpresora.equals("1")) {
                    if (!terminalImei.trim().equals("351007492166830") && !terminalImei.trim().equals("358767141019390")
                            && !terminalImei.trim().equals("868995078034628")) {
                        mostrarDialogoAlerta("ALERTA DEL SISTEMA", "IMPRESORA NO VINCULADA, este sistema requiere la impresora activa y vinculada con el movil");

                        return;
                    }
                }
            }
        }

        if (infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim().length() != 0) {

            if ((infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()).replaceFirst("^0*", "").equals(""))  //rx
                filter = true;
        }
        AnomaliaDeNoLectura anomaliaDeNoLectura = new AnomaliaDeNoLectura();
        anomaliaDeNoLectura.setArchivo_AnomaliaDeNoLectura(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/CAUSAS.TXT");

        try {
            abrirArchivosDeFacturacion();
            if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {

                VariablesGlobales.datodebusqueda = "";

                try {
                    TOMARNUEVACRITICA = 0;
                    String filtro = "";// before Z,4,M,P,H,G
                    /*if (filter) {
                        filtro = "Z,4,M,P,Y";
                    } else {*/
                        if (filter2) {
                            if (!infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO().trim().toUpperCase().equals("MM") && !infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO().trim().toUpperCase().equals("MO")) {
                                filtro = "0,3,7,22,23,34,40";
                            } else {
                                filtro = "0,17";
                            }
                        }


                    if (FiltroCero==1)
                    {
                        filtro = "0";
                    }
                    UltimoInforme = "";
                    //Ax: Abre el modulo y se identifica con el ID (ANOMALIA_REQUEST_CODE), que mas adelante se recupera valor en 'onActivityResult' para procesar la anomalia
                    Bundle bundle = new Bundle();
                    bundle.putString("filtro", filtro);
                    bundle.putBoolean("lecturasIguales", lecturasiguales);
                    bundle.putInt("registroActual", VariablesGlobales.registroactual);
                    bundle.putBoolean("ISMENU", menu); //Indica si se dio clic desde el menu, para poder salir en caso erroneo
                    bundle.putBoolean("medidorCero", medidorCero);//UltimoInforme
                    bundle.putString("UltimoInforme", UltimoInforme);//
                    bundle.putInt("TOMARNUEVACRITICA", TOMARNUEVACRITICA);
                    Intent intent = new Intent(MenuDeLiquidacion.this, ModuloDeAnomalias.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ANOMALIA_REQUEST_CODE);

                } catch (Exception e) {
                    mensajeT("Error al iniciar actividad de Anomalias" + e.getMessage(), msgLargo);
                    //  Toast.makeText(getApplicationContext(), "Error al iniciar actividad de Anomalias" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                if (permiteLectura.trim().equals("1")) {
                    mensajeT("Digite lectura \n", msgMedio);
                } else
                    mensajeT("Cuenta ya esta Leida y Facturada \n" + "Prohibido\n" + "Modificar Factura\n", msgMedio);
            }
            cerrarArchivosFacturacion();

        } catch (Exception e) {
            mensajeT("Error con el controlador en causa de no lectura", msgLargo);
            e.printStackTrace();
        }
    }
   int YaEvaluoIgual=0;
    private void procesarAnomaliaSeleccionada(String anomalia, int registroA) {
        causal = "";

        if (anomalia != null) {
            VariablesGlobales.datodebusqueda = anomalia.trim();
        } else {
            mensajeT("Anomalia de \n" + "Lectura no fue registrada\n", msgMedio);
            return;
        }
        if (UltimoInforme == null) {
            UltimoInforme = "";
        }


        if (VariablesGlobales.datodebusqueda.trim().length() > 0) {

            if (!VariablesGlobales.getTipoDeRuta().equals("E")) {

                if (lecturasiguales || (TOMARNUEVACRITICA > 1)) {//nuevo para la critica sap|| (TOMARNUEVACRITICA > 1)

                    Log.e("INFO", "NOTA:" + VariablesGlobales.datodebusqueda);
                    if (TOMARNUEVACRITICA < 2) {
                        infoRegistroSalida.settablaRegistroSalida_causadenolectura(VariablesGlobales.datodebusqueda);
                    } else {
                        infoRegistroSalida.settablaRegistroSalida_CODIGO_SAC(VariablesGlobales.datodebusqueda);
                    }
                    tomarFechaSistema("", "");
                    infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
                    infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
                    if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()==0) {
                        infoRegistroSalida.settablaRegistroSalida_informe(UltimoInforme.trim().replace("0", ""));
                    }
                    else
                    {
                        String sumarinforme =   UltimoInforme.trim();//infoRegistroSalida.gettablaRegistroSalida_INFORME().trim()+", "+
                        if (sumarinforme.length()<251)
                        {
                            infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.trim());
                        }
                        else{
                            infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.substring(0,250));
                        }
                    }
                    if (cc_lecturaact.trim().equals("null") || cc_lecturaact.trim().equals("")) {
                        if (!infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim().equals("")) {
                            cc_lecturaact = infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim();
                        }
                    }
                    if (VariablesGlobales.getTotalClientesReales() > 0) {
                        if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (4)"); //temporal?
                            infoRegistroSalida.escribir_TablaRegistroSalida(registroA);//before VariablesGlobales.registroactual
                            //cambniar esto para que entre es con el proceso de  escribir

                            //procesarLectura(1, 0, cc_lecturaact, "XY");//sigue donde se quedo
                          //  YaEvaluoIgual=1;
                         //   procesarLectura(0, 0, cc_lecturaact, VariablesGlobales.datodebusqueda.trim());//sigue donde se quedo

                        }
                    } else {
                        //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (5)"); //temporal?
                        infoRegistroSalida.escribir_TablaRegistroSalida(registroA);//before  VariablesGlobales.registroactual
                        //procesarLectura(1, 0, cc_lecturaact, "XY");//sigue donde se quedo
                       // YaEvaluoIgual=1;
                        //procesarLectura(0, 0, cc_lecturaact, VariablesGlobales.datodebusqueda.trim());//sigue donde se quedo
                    }
                    TOMARNUEVACRITICA = 0;
                    lecturasiguales = false;
                    cc_lecturaact = "";
                    YaTomoFotoCliente = 1;
                    if (ControlEspecialZonaRoja.trim().equals("000")) {
                        ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 1, "E", 1);
                    }
                } else if (permiteLectura.equals("1")) {
                    infoRegistroSalida.lectura_TablaRegistroSalida(registroA);//before VariablesGlobales.registroactual

                    if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()==0) {
                        infoRegistroSalida.settablaRegistroSalida_informe(UltimoInforme.trim().replace("0", ""));
                    }
                    else
                    {
                        String sumarinforme =   UltimoInforme.trim();//infoRegistroSalida.gettablaRegistroSalida_INFORME().trim()+", "+
                        if (sumarinforme.length()<251)
                        {
                            infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.trim());
                        }
                        else{
                            infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.substring(0,250));
                        }
                    }
                    infoRegistroSalida.settablaRegistroSalida_causadenolectura(VariablesGlobales.datodebusqueda);
                    tomarFechaSistema("", "");
                    infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
                    infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);


                    if (VariablesGlobales.getTotalClientesReales() > 0) {
                        if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (6)"); //temporal?
                            infoRegistroSalida.escribir_TablaRegistroSalida(registroA);//before  VariablesGlobales.registroactual
                        }
                    } else {
                        //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (7)"); //temporal?
                        infoRegistroSalida.escribir_TablaRegistroSalida(registroA);//before  VariablesGlobales.registroactual
                    }
                    visualizarInformacionCliente(0);//nuevo mostrar

                } else {
                    if (ingresarAnomaliaNoLectura(0, VariablesGlobales.datodebusqueda.trim()) == 1) {
                        variables.nveces = 1;
                        procesarLectura(0, 0, "0", VariablesGlobales.datodebusqueda.trim());

                    } else
                        mensajeT("Anomalia de \n" + "Lectura no existe en la tabla\n", msgMedio);
                }
            } else {
                if (ingresarAnomaliaNoLectura(0, VariablesGlobales.datodebusqueda.trim()) == 1) {
                    causalEntrega = true;
                    variables.nveces = 1;
                    procesarLectura(0, 0, "", VariablesGlobales.datodebusqueda.trim());

                } else
                    mensajeT("Anomalia de \n" + "Lectura no existe en la tabla\n", msgMedio);
            }
        }

    }

    public void ProcesarAnomalias() {
        String MiMensajeCausa="";
        abrirArchivosDeFacturacion();

        if (UltimoInforme == null) {
            UltimoInforme = "";
        }
        MiMensajeCausa = UltimoInforme;
        procesarAnomaliaSeleccionada(causal, s_idac);
        causal = "";
        cerrarArchivosFacturacion();

        if (s_info.equals("1")) {
            s_idac = VariablesGlobales.registroactual;
            abrirInforme(false, MiMensajeCausa);
        }
    }

    public void MostrarAlertDialog(String titulo, String mensaje, final String NombreMetodo) { //Crea un alertDialog con si-no y espera hasta un clic SI o No

        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuDeLiquidacion.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) { //Esto es para reusar este metodo 'MostrarAlertDialog' con mas llamados en 'NombreMetodo'
                    case "ProcesarAnomalias":
                        ProcesarAnomalias();
                        break;
                    case "ProcesarLectura":
                        procesarLectura(1, 0, txtElectura.getText().toString().trim(), "XY");
                        activaLectorBarras = true;
                        cerrarArchivosFacturacion();
                        break;

                    case "procesarLectura2": //Para Cuenta Contratada
                        banderaCuentaContratada = 1;
                        procesarLectura(cc_indlectura, cc_primero, cc_lecturaact, cc_causaact);
                        banderaCuentaContratada = 0;
                        break;
                    case "adicionarNovedad":
                        banderaadicionarNovedad = 1;
                        adicionarNovedad(2, 0);
                        banderaadicionarNovedad = 0;
                        break;

                    /*case "validarconexion":
                        enviosGPRScantidad = 1;
                        envioFacturacionHilos();
                        break;*/

                    case "eliminacausa":
                        elimina_causa();
                        break;

                    case "eliminalectura":
                        elimina_lectura();
                        break;
                    case "cerrarProcesoFacturacion":
                        cerrarProcesoFacturacion();
                        break;
                    case "EnviarFotografiasBucle":
                        ejecutaMenuEnvio = 1;
                        /*if (handlerTask.isAlive()) {
                            handlerTask.interrupt();
                        }*/
                        File file_nuevoEnviosGprsda = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOGPRS" + serialPDA + ".SDA");
                        File file_EnviosGprsda = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/ENVIOSGPRS.SDA");
                        if (file_nuevoEnviosGprsda.exists()) {
                            if (file_nuevoEnviosGprsda.length() <= 0) {
                                file_nuevoEnviosGprsda.delete();
                            }
                        }
                        if (file_EnviosGprsda.exists()) {
                            if (file_EnviosGprsda.length() <= 0) {
                                file_EnviosGprsda.delete();
                            }
                        }
                        estaEnvioAlWsOcupado = false;
                        //VariablesGlobales.btPrintService.stop();
                        msgAdmon = String.format("%-42s", "ENVIANDO FOTOGRAFIAS");
                        mensajeAdm(MenuDeLiquidacion.this); // No enviados
                        metodo = "EnviarFotografiasBucle";//Ax: se usa para el dialog
                        TaskHelper.execute(new AsyncCallWS(), "EnviarFotografiasBucle");

                        break;

                    case "enviarArchivos2":
                        enviarArchivos2();
                        break;
                    case "enviarArchivos3":
                        enviarArchivos3();
                        break;
                    case "reimpresiondefactura":
                        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("L")) {
                            //  EscribaArchivoImpresion("");
                            if (MarcaDeImpresora.equals("ZEBRA")) {
                                if (VariablesGlobales.habilitadaimpresora == 1) {
                                    EscribaArchivoImpresion("");
                                }
                            }
                            else {
                                //nuevo metodo de imprimir en las jal

                                //finish();
                                if (VariablesGlobales.habilitadaimpresora == 1)
                                {
                                    Bitmap logo = BitmapFactory.decodeResource(
                                            getResources(),
                                            R.drawable.ic_lectura //.hobobyn2 debe montar el logo de emsa a blanco y negro
                                    );
                                    EscribaArchivoImpresionESC(logo);

                                }
                            }

                        } else {
                            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V"))
                                EscribaArchivoImpresion_Visita("");
                            else {
                                mensajeT("NO ESTA MARCADO PARA VERIFICAR: \n DEBE VERIFICAR DATOS " + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + "**" + infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1), msgLargo);
                                break;
                            }
                        }
                        txtElectura.setText("");
                        abrirArchivosDeFacturacion();
                        if (avanzarRegistro() == 0) {
                            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
                        }
                        visualizarInformacionCliente(0);
                        cerrarArchivosFacturacion();
                        variables.impresora = "   ";
                        break;
                    case "enviarArchivos4":
                        try {
                            ejecutaMenuEnvio = 1;
                            metodo = "enviarArchivos4_asinc";//Ax: se usa para el dialog
                            msgAdmon = String.format("%-42s", "ENVIANDO ARCHIVOS");
                            mensajeAdm(MenuDeLiquidacion.this); // No enviados
                            TaskHelper.execute(new AsyncCallWS(), "enviarArchivos4_asinc");
                        } catch (Exception ex) {
                            mensajeT("Error en enviarArchivos4_asinc: \n" + ex.getMessage(), msgLargo);
                            estaEnvioAlWsOcupado = false;
                            logger.info("MostrarAlertDialog() enviarArchivos4 " + ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) {
                    case "ProcesarAnomalias":
                        causal = "";
                        break;
                    case "ProcesarLectura":
                        activaLectorBarras = true;
                        cerrarArchivosFacturacion();
                        break;
                    case "procesarLectura2": //Para Cuenta Contratada
                        banderaCuentaContratada = -1;
                        procesarLectura(cc_indlectura, cc_primero, cc_lecturaact, cc_causaact);
                        banderaCuentaContratada = 0;
                        break;
                    case "adicionarNovedad":
                        banderaadicionarNovedad = -1;
                        adicionarNovedad(1, 0);
                        banderaadicionarNovedad = 0;
                        break;
                    case "validarconexion":
                        estaEnvioAlWsOcupado = false;
                        break;
                    case "enviarArchivos2":
                        estaEnvioAlWsOcupado = false;
                        break;
                    case "enviarArchivos3":
                        estaEnvioAlWsOcupado = false;
                        break;

                    case "enviarArchivos4":
                        estaEnvioAlWsOcupado = false;
                        break;
                    default:
                        break;
                }
            }
        });

        builder.create().show();
    }

    private int ingresarAnomaliaNoLectura(int consumoafacturar, String causaact) {

        String causa2 = causaact;

        variables.consumoactual = 0;

        if (parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_INTENTOS()) > 5) { //Ax:se cambia Integer.getInteger
            mensajeT("Numero de Intentos\n ya fueron ejecutadas\nAcceso no permitido", msgCorto);
            return (0);
        }

        if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length() > 0
                && infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().length() > 0) {
            mensajeT("El cliente ya tiene anomalia\ndebe eliminar anomalia para cambiar", msgCorto);
            return (0);
        }

        tomarFechaSistema("", "");
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

        infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);

        if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
            anomaliaDeLectura.lectura_AnomaliaDeNoLectura(1);
            tomarFechaSistema("", "");
            infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
            infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

            anomaliaDeLectura.buscarAnomaliaDeNoLectura(causa2);
        }
        if (anomaliaDeLectura.getEncontro_AnomaliaDeNoLectura() == 0) {
            mensajeT("Anomalia no existe en la Tabla\n" + causa2, msgMedio);
            return 0;
        }
        NumeroDeFotos = parseStringToInteger(anomaliaDeLectura.getAnomaliaDeNoLectura_NroFotos());

        Parametro_CUENTA = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();
        Parametro_TIPOMEDIDA = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA().trim();

        infoRegistroSalida.settablaRegistroSalida_causadenolectura(anomaliaDeLectura.getanomaliaDeNoLectura_CODIGO());///aqui esta error ax
        anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();

        if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()==0) {
            infoRegistroSalida.settablaRegistroSalida_informe(UltimoInforme.trim().replace("0", ""));
        }
        else
        {
            String sumarinforme =   UltimoInforme.trim();//infoRegistroSalida.gettablaRegistroSalida_INFORME().trim()+", "+
            if (sumarinforme.length()<251)
            {
                infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.trim());
            }
            else{
                infoRegistroSalida.settablaRegistroSalida_informe(sumarinforme.substring(0,250));
            }
        }
        if (anomaliaDeLectura.getAnomaliaDeNoLectura_LECTURA().equals("0")) {
            infoRegistroSalida.settablaRegistroSalida_lecturatomada("000000000");
        }
        if (VariablesGlobales.getTotalClientesReales() > 0) {
            if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (8)"); //temporal?
                infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
            }
        } else {
            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (9)"); //temporal?
            infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
        }
        tomarFechaSistema("", "");

        variables.estado = "5";

        tomarFechaSistema(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA(), infoRegistroSalida.gettablaRegistroSalida_HORALECTURA());
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

        if (VariablesGlobales.tipoDeRuta.equals("E") &&
                !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99") &&
                !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91") &&
                !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92") &&
                !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93")
        ) {
            VariablesGlobales.totalcausasnolectura++;
        } else if (VariablesGlobales.tipoDeRuta.equals("L")) {
            VariablesGlobales.totalcausasnolectura++;
        }

        return 1;
    }

    private void ejecutarBusquedaCliente() {

        try {
            VariablesGlobales.datodebusqueda = "";
            VariablesGlobales.tipodebusqueda = 0;

            //Ax: abre intent ModuloBusquedaCuenta y cuando vuelve se identifica con el ID (BUSQUEDA_REQUEST_CODE)
            Bundle bundle = new Bundle();
            Intent intent = new Intent(MenuDeLiquidacion.this, ModuloBusquedaCuenta.class);
            bundle.putString("NombreArchivos", NombreArchivos); //este lleva el nombre del archivo con la extension
            intent.putExtras(bundle);
            startActivityForResult(intent, BUSQUEDA_REQUEST_CODE);

        } catch (Exception e) {
            mensajeT("Problema en el modulo de busqueda", msgCorto);
            e.printStackTrace();
        }
    }

    private int buscarCuentaMedidor(String medidor, int modeloBusqueda) {

        int ultimoregistro;

        abrirArchivosDeFacturacion();

        ultimoregistro = VariablesGlobales.registroactual;

        if (VariablesGlobales.tipodebusqueda == 2) {

            if (!buscarApuntadorCliente(medidor)) {

                infoRegistroSalida.setEncontro_TablaRegistroSalida(0);
            } else {
                infoRegistroSalida.setEncontro_TablaRegistroSalida(parseStringToInteger(miApuntador.getapuntadorCliente_APUNTADOR()));
            }
        } else {
            infoRegistroSalida.setEncontro_TablaRegistroSalida(parseStringToInteger(VariablesGlobales.datodebusqueda));
        }

        if (infoRegistroSalida.getEncontro_TablaRegistroSalida() > 0) {
            if (VariablesGlobales.tipodebusqueda == 1) {//Ax: antes VariablesGlobales.tipodebusqueda != 2
                VariablesGlobales.registroactual = infoRegistroSalida.getEncontro_TablaRegistroSalida();//infoMedidorEntrada.getEncontro_TablaMedidorEntrada();
                leerInformacionUsuario(1);
            } else {
                VariablesGlobales.registroactual = infoRegistroSalida.getEncontro_TablaRegistroSalida();//infoMedidorEntrada.getEncontro_TablaMedidorEntrada();
                leerInformacionUsuario(3);
            }
            visualizarInformacionCliente(0);
            cerrarArchivosFacturacion();

            if (VariablesGlobales.tipoDeRuta.equals("E")) {
                realizarEntregaPredio();
                variables.impresora = "   ";
            }
        } else {
            VariablesGlobales.registroactual = ultimoregistro;
            leerInformacionUsuario(1);
            variables.impresora = "   ";
            mensajeT("No existe: " + medidor, msgMedio);
            cerrarArchivosFacturacion();
        }
        return (0);
    }

    private boolean buscarApuntadorCliente(String apuntador) {// buscar el codigo o apuntador por el nombre de cuenta
        if (apuntador.length() >= 9) {
            if (apuntador.length() > 9)
                apuntador = apuntador.substring(0, 9);

            miApuntador.archivo_ApuntadorCliente = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/IDCLIENTES.TXT";
            File file = new File(miApuntador.archivo_ApuntadorCliente);

            if (file.exists()) {
                if (miApuntador.abrir_ApuntadorCliente(miApuntador.archivo_ApuntadorCliente)) {
                    miApuntador.buscarbinario_ApuntadorCliente(apuntador);
                    if (miApuntador.encontro_ApuntadorCliente > 0) {
                        miApuntador.Cerrar_ApuntadorCliente();
                        return true;
                    }
                    miApuntador.Cerrar_ApuntadorCliente();
                }
            }
        }
        return false;
    }

    private boolean realizarEntregaPredio() {

        if (!autorizacion()) {
            txtElectura.setText("");
            return false;
        }

        if (VariablesGlobales.tipoDeRuta.equals("E")) {

            if (!variables.impresora.substring(0, 2).equals("LE") && infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim().equals("")) {
                mensajeT("NO SE HA LEIDO \nEL CODIGO DE BARRAS\n" + VariablesGlobales.obligabarras, msgCorto);
                txtElectura.setText("");
                return false;
            }

            activaLectorBarras = false;
            abrirArchivosDeFacturacion();

            if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {

                validarEstadoRegistroNuevo(variables.getGlobalregistroactual());
                procesarLectura(0, 0, "", "99");
                banderaCuentaContratada = 0;
            } else
                mensajeT("Error De ENTREGA\n" + "Cliente ya ENTREGADO\n" + "PROSIGA CON LA PROXIMA CUENTA", msgMedio);

            activaLectorBarras = true;
            cerrarArchivosFacturacion();
            txtElectura.setText("");
            return true;
        }
        return false;
    }

    private int retrocedeRegistro() {

        int tmpreg = VariablesGlobales.registroactual - 1;
        YaTomoFotoCliente = 0;
        s_foto="0";
       // NumeroDeFotos = 0;
        variables.direcciondelectura = variables.haciaatras;
        variables.lactual = 0;
        variables.consumoactual = 0;
        variables.nveces = 0;
        tomaFotoBoton = false;
        YaMostroMensaje = 0;
        imagenLiquid_3.setImageResource(android.R.color.transparent);
        code.setLength(0);
        BorroLecturaOCausa = false;
        while (tmpreg > 0) {
            if (validarEstadoRegistroMinimo(tmpreg, "L") == 0) {
                VariablesGlobales.registroactual = tmpreg;
                leerInformacionUsuario(1);
                visualizarInformacionCliente(1);
                return 1;
            }
            tmpreg--;
        }


        leerInformacionUsuario(1);
        visualizarInformacionCliente(1);

        return 0;
    }

    private int avanzarRegistro() {

        int tmpreg = VariablesGlobales.registroactual + 1;
        YaTomoFotoCliente = 0;
       // NumeroDeFotos = 0;
        s_foto="0";
        variables.direcciondelectura = variables.haciaadelante;
        variables.lactual = 0;
        variables.consumoactual = 0;
        imagenLiquid_3.setImageResource(android.R.color.transparent);
        variables.nveces = 0;
        variables.cactual = 0;
        tomaFotoBoton = false;
        int x = infoRegistroSalida.getTotal_TablaRegistroSalida();
        code.setLength(0);
        YaMostroMensaje = 0;
        BorroLecturaOCausa = false;
        while (tmpreg <= x) {
            int v = validarEstadoRegistroMinimo(tmpreg, "L");

            if (v == 0) {

                VariablesGlobales.registroactual = tmpreg;
                leerInformacionUsuario(1);
                visualizarInformacionCliente(1);
                return (1);
            }
            ++tmpreg;
        }

        return (0);
    }

    private int irUltimoRegistro() {
        int tmpreg = VariablesGlobales.registroactual;

        VariablesGlobales.registroactual = infoRegistroSalida.getTotal_TablaRegistroSalida() + 1;
        if (retrocedeRegistro() == 0) {
            VariablesGlobales.registroactual = tmpreg;
            return 0;
        }
        return (1);
    }

    private int irPrimerRegistro() {

        int tmpreg = VariablesGlobales.registroactual;

        VariablesGlobales.registroactual = 0;

        if (avanzarRegistro() == 0) {
            VariablesGlobales.registroactual = tmpreg;
            return (0);
        }
        return (1);
    }

    @SuppressLint("NewApi")
    private void txtLecturaActualKeyPress() {

        if (txtElectura.getText().toString().equals("") && !VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
            return;
        }
        if (!txtElectura.getText().toString().matches("^[0-9]*$")) {
            txtElectura.setText("");
            return;
        }
        if (lblpuntero.getText().toString().contains("O")) {

            if (lblpuntero.getText().toString().trim().substring(1, 9).equals("CONCLUIDO")) {
                visualizarInformacionCliente(4);
                txtElectura.setText("");
                return;
            }

            if (lblpuntero.getText().toString().trim().substring(1, 9).equals("TERMINADO")) { //Ax aca nunca entra
                visualizarInformacionCliente(3);
                txtElectura.setText("");

                final File nombreDeArchivo = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LEIDO.TXT");


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alerta Seleccion Terminacion");
                builder.setMessage("Ya termino de realizar\n la Facturacion? " + VariablesGlobales.totalprediosleidos + " /" + infoRegistroSalida.getTotal_TablaRegistroSalida());

                builder.setIcon(R.drawable.ic_launcher);

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            RandomAccessFile writer = new RandomAccessFile(nombreDeArchivo, "rw");
                            writer.close();
                            cerrarProcesoFacturacion();
                            return;

                        } catch (FileNotFoundException e) {

                            e.printStackTrace();

                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }

        if (!autorizacion()) {
            txtElectura.setText("");
            return;
        }

        if (VariablesGlobales.getTipoDeRuta().trim().equals("E")) {

            activaLectorBarras = false;
            abrirArchivosDeFacturacion();

            if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {

                valorLeidoFactura = "" + (Double.parseDouble(VariablesGlobales.minimovalorentrega.trim()) + 1);
                procesarLectura(0, 0, "", "99");
            } else {
                mensajeT("Error De ENTREGA\n" + "Cliente ya ENTREGADO\n" + "PROSIGA CON LA PROXIMA CUENTA", msgMedio);
            }

            activaLectorBarras = true;
            cerrarArchivosFacturacion();
            txtElectura.setText("");
            return;
        } else {


            activaLectorBarras = false;
            abrirArchivosDeFacturacion();

            if (permiteLectura.equals("1")) {
                procesarLectura(1, 0, txtElectura.getText().toString().trim(), "XY");

            } else {
                if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {
                    Log.e("INFO", "[MenuDeLiquidacion]txtKeyPressed()| validarEstadoRegistro = 0");
                    if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("") && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {
                        MostrarAlertDialog("MODIFICAR LECTURA", "Cliente no se logro cerrar\nDesea ingresar nuevamente la lectura?\n" + infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA() + " ", "eliminalectura");// elimina_lectura();
                    } else {
                        if ((infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()).replaceFirst("^0*", "").equals("")) { //rx

                            capturarAnomaliaTerreno(true, false, false);//true, se activa el filtro para solo mostrar algunas anomalias
                        } else {
                            procesarLectura(1, 0, txtElectura.getText().toString().trim(), "XY");
                        }
                    }


                } else {
                    if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") && infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("5")) {
                        MostrarAlertDialog("MODIFICAR CAUSA", "Desea ELIMINAR LA CAUSA?\n" + infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA() + " ", "eliminacausa");// elimina_causa();

                    } else {
                        MostrarAlertDialog("MODIFICAR LECTURA", "Desea ELIMINAR LA LECTURA?\n" + infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA() + " ", "eliminalectura");// elimina_lectura();
                    }
                }
            }
            activaLectorBarras = true;
            cerrarArchivosFacturacion();
            txtElectura.setText("");
            return;
        }
    }

    private void elimina_lectura() {

        abrirArchivosDeFacturacion();
        infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
        //resta la critica segun la obtenida
        restarVariablesConteo(infoRegistroSalida.gettablaRegistroSalida_LEIDO());

        if (infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim().length() > 0) {
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada2(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA());
        } else {
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada1(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA());
        }

        infoRegistroSalida.settablaRegistroSalida_lecturatomada("         ");
        infoRegistroSalida.settablaRegistroSalida_leido(" ");


        if (infoRegistroSalida.gettablaRegistroSalida_MODIFICACIONES().equals(" ")) {
            infoRegistroSalida.settablaRegistroSalida_modificaciones("0");
        }
        infoRegistroSalida.settablaRegistroSalida_intentos("1");
        infoRegistroSalida.settablaRegistroSalida_causadenolectura("  ");
        infoRegistroSalida.settablaRegistroSalida_leido(" ");
        infoRegistroSalida.settablaRegistroSalida_modificaciones(Integer.toString(parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_MODIFICACIONES()) + 1).trim());

        tomarFechaSistema("", "");
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

        infoRegistroSalida.settablaRegistroSalida_modificaciones(Integer.toString(parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_MODIFICACIONES()) + 1).trim());
       // infoRegistroSalida.settablaRegistroSalida_intentos("1");
        tomarFechaSistema("", "");
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
        --VariablesGlobales.totalprediosleidos;
        --VariablesGlobales.totallecturas;
        --VariablesGlobales.totalSoloLecturas;
        --VariablesGlobales.totalregistrosleidos;
        escribirTablasSalida(1);
        cerrarArchivosFacturacion();
        lectModifica = true;
        txtElectura.setText("");
        BorroLecturaOCausa = true;
    }

    private void elimina_causa() {

        abrirArchivosDeFacturacion();
        infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);

        infoRegistroSalida.settablaRegistroSalida_lecturatomada("         ");
        infoRegistroSalida.settablaRegistroSalida_intentos("1");
        infoRegistroSalida.settablaRegistroSalida_causadenolectura("  ");
        infoRegistroSalida.settablaRegistroSalida_leido(" ");

        if (infoRegistroSalida.gettablaRegistroSalida_MODIFICACIONES().equals(" ")) {
            infoRegistroSalida.settablaRegistroSalida_modificaciones("0");
        }
        infoRegistroSalida.settablaRegistroSalida_modificaciones(Integer.toString(parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_MODIFICACIONES()) + 1).trim());

        tomarFechaSistema("", "");
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
        --VariablesGlobales.totalprediosleidos;
        --VariablesGlobales.totalcausasnolectura;
        --VariablesGlobales.totalregistrosleidos;

        escribirTablasSalida(2);
        cerrarArchivosFacturacion();
        lectModifica = true;
        txtElectura.setText("");
        BorroLecturaOCausa = true;
    }

    // procesos de validacion de las 9 de la mannana con el retorno del falso
    int YaTomoFotoCliente=0;
    private int procesarLectura(int indlectura, int primero, String lecturaact, String causaact) { //78001
        imprimirTrasFoto = false;
        try {
            Utils.escribirLogEnRaiz("Cuenta " +infoRegistroSalida.gettablaRegistroSalida_CUENTA() +" INDICADOR "+indlectura + ", PRIMERO "+ primero + " LECTURA " + lecturaact+ ", CAUSA " +  causaact+" Fecha "+getPhoneDate().toString());
            if (!ubicacionHabilitada()) {
                validarGPS();
            }
     /*       if (causaact.equals("0"))
            {
                  if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("")) {
                      return 0;
                  }

                  if (parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()) == parseStringToDouble(lecturaact))
                  {
                      indlectura=0;
                  }

            }*/

            if (VariablesGlobales.habilitadaimpresora == 0
                    && !impresoraObligatoriaDeshabilitadaPorSupervisor
                    && !VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
                /*if ( ControlEspecialZonaRoja.trim().equals("000") && !new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/SINIMPRESORA.TXT").exists()) {
                    mostrarDialogoAlerta("ALERTA DEL SISTEMA", "IMPRESORA NO VINCULADA");
                    return 0;
                }*/

                if (ControlEspecialZonaRoja.trim().equals("000")) {
                    if (ControlEspecialImpresora.equals("1")) {
                        if (!terminalImei.trim().equals("351007492166830") && !terminalImei.trim().equals("358767141019390")
                                && !terminalImei.trim().equals("868995078034628")) {
                            mostrarDialogoAlerta("ALERTA DEL SISTEMA", "IMPRESORA NO VINCULADA");
                            return 0;
                        }
                    }
                }
            }

            if (!fechaAutomaticaActiva(this)) {
                //alerta grave el reloj esta ciendo manipulado esta apagado el sistema automatico
                logger.info("AUTO_TIME OFF ** " + "ALTERACION DE RELOJ");
                Log.d(TAG, "📡 AUTO_TIME OFF=" + fechayHoraReportadaGPS + ", ALTERACION DE RELOJ=" + fechayHoraReportadaGPS);
                VerMensajeria();

                return 0;
            }


            if (VariablesGlobales.getTipoDeRuta().trim().equals("E") && !ventanaFirma) {
                if (VariablesGlobales.getObligaFirma().equals("1") || infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0Y")) {
                    activityFirma();
                    return 0;
                }
            }
            validarCodigoUsuario();
            if (!VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
                if (VariablesGlobales.getObligabarras().equals("1")) {

                    if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01")) {
                        if (SonBarrasGrupales == true) {
                            if (UltimaBarraLeida.trim().equals("")) {
                                mensajeT("Error... SE REQUIERE LEER EL CODIGO DE BARRAS PARA EL GRUPO DE MEDIDORES EN ANAQUEL", msgLargo);
                                return 0;
                            }
                        } else {
                            if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") &&
                                    infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim().equals("")
                                    || (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("Z9") ||
                                    infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim().equals("Z9"))) {

                                if (variables.impresora.trim().equals("") || variables.impresora.trim().equals("01")) {
                                    variables.nveces = 0;
                                    if (txtElectura.getHint() == "COD. BARRAS") {
                                        txtElectura.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                                        variables.impresora = "LE5";
                                        txtElectura.setHint("LECTURA");
                                        abriryEscribirBarras(lecturaact);
                                        mensajeT("Error... INGRESE DE NUEVO POR FAVOR LA LECTURA", msgLargo);
                                        return 0;
                                    } else {
                                        BDifNull bdl = new BDifNull(Realm.getDefaultInstance());
                                        CrudifNull cedbyid = bdl.obtenerdatabyid(1);
                                        bdl.actualizarfila(cedbyid, String.valueOf(VariablesGlobales.registroactual));
                                        mensajeT("Error... SE REQUIERE LEER EL CODIGO DE BARRAS O UNA ANOMALIA SIN CODIGO", msgLargo);
                                        return 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            infoRegistroSalida.settablaRegistroSalida_intentos(("" + variables.nveces).trim());
            if (!lecturasiguales && !variables.estado.equals("5")) {
                variables.estado = "3";
            }
            double t2;

            tomarFechaSistema(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA(), infoRegistroSalida.gettablaRegistroSalida_HORALECTURA());
            infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
            infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
            variables.diashoy = (int) (variables.calcularNumerodeDias(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA()));

            if (variables.diasdecobro == 0)
                variables.diasdecobro = 1;

            if (indlectura == 1) {

                if (!lecturasiguales) {

                    if (LecturaRegistroNuevo(primero, variables.nveces, variables.lactual, variables.estado, lecturaact, variables.consumoactual) == 0)//evaluarLecturaRegistro esta es la critica
                    {
                        if (lecturasiguales || TOMARNUEVACRITICA > 1) {//

                            cc_lecturaact = lecturaact;
                            //aqui lecturasiguales
                            Ultimo_Estado = variables.estado;
                            try {
                                VariablesGlobales.totallecturas++;
                                s_idac = VariablesGlobales.registroactual;
                                capturarAnomaliaTerreno(false, true, false);//true, se activa el filtro para solo mostrar algunas anomalias
                            } catch (Exception e) {
                                mensajeT("Error al iniciar actividad de capturar anomalia" + e.getMessage(), msgLargo);
                            }
                            //return 0;
                        }
                        else {
                            if (indicadorManual == 0) // nuevo para cuando se le da automatico si esta en 1
                            {
                                //creo es aqui donde se debe de incluir lo de la foto para los criticados y puedan hacer la verificacion sobre la foto
                                if (!variables.estado.equals("") && !variables.estado.equals("3") && YaTomoFotoCliente == 0) {
                                    YaTomoFotoCliente = 1;
                                    if (ControlEspecialZonaRoja.trim().equals("000")) {
                                        obligaFotografia = "0";
                                        ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 1, "E", 1);
                                    }

                                }
                                txtElectura.setText("");
                                //return 0;
                            }
                        }
                        //Ax: Este proceso despues de critica detecta lecturas iguales y abre la ventana de comentarios, una vez llena, vuelve a procesarlectura
                        //se clausura para que no solicite anomalias SAC 29/04/2026 solicitado en reuniones


                        return 0;

                    }
                }

                VariablesGlobales.totallecturas++;
                infoRegistroSalida.settablaRegistroSalida_lecturatomada(String.format("%1$10s", lecturaact));
                infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);

            } else {
                if (!infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals(" ") && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("0")) {// (!infoClienteSalida.gettablaClienteSalida_INDFACTURACION().equals(" ")) && (!infoClienteSalida.gettablaClienteSalida_INDFACTURACION().equals("L"))) {
                    if (indicadorManual == 0) // nuevo para cuando se le da automatico si esta en 1
                    {
                        mensajeT("Error en lectura\nCuenta ya esta registrada\ncomo leida", msgMedio);
                        return 0;
                    }
                }

                if (causaact.equals("XY")) {
                    if (indicadorManual == 0) // nuevo para cuando se le da automatico si esta en 1
                    {
                        mensajeT("Tiene Procesos de Anomalia debe salir", msgMedio);
                        return 0;
                    }
                } else {
                    if (!Ultimo_Estado.equals(""))
                        variables.estado = Ultimo_Estado;
                    infoRegistroSalida.settablaRegistroSalida_lecturatomada(String.format("%1$10s", lecturaact));//nuevo aqui
                    if (VariablesGlobales.tipoDeRuta.equals("E")) {//infoClienteEntrada.gettablaEntradaClientes_FACTURADOMICILIADA().trim().equals("E") ||  ojo o
                        variables.estado = "5";
                        if (infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim().equals("")) {
                            variables.impresora = " 00";
                            infoRegistroSalida.settablaRegistroSalida_IMPRESORA(variables.impresora);
                        }

                        if (!telefono.trim().equals("")) {
                            String imformeA = infoRegistroSalida.gettablaRegistroSalida_INFORME().trim();
                            infoRegistroSalida.settablaRegistroSalida_informe(imformeA + telefono.trim());
                        }

                        if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("")) {
                            infoRegistroSalida.settablaRegistroSalida_causadenolectura("99");
                            infoRegistroSalida.settablaRegistroSalida_lecturatomada("000000000");
                        }
                        infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);

                    } else {
                        infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);// infoClienteSalida.settablaClienteSalida_INDFACTURACION("L");
                    }
                }

                VariablesGlobales.totallecturas++;
                infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);
            }

            if (variables.impresora.substring(0, 2).equals("LE"))
                infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("1");
            else
                infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("0");
            lecturasiguales = false;
            TOMARNUEVACRITICA = 0;
            evaluarDistancia();


            tomarFechaSistema(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA(), infoRegistroSalida.gettablaRegistroSalida_HORALECTURA());
            infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
            infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

            variables.ultimoregistro = VariablesGlobales.registroactual;
            infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
            infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
            infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);

            if (infoRegistroSalida.gettablaRegistroSalida_ULTIMOMEDIDORLEIDO().equals(variables.medidoranterior)) {
                infoRegistroSalida.settablaRegistroSalida_ULTIMOMEDIDORLEIDO(variables.medidoranterior);
                variables.medidoranterior = infoRegistroSalida.gettablaRegistroSalida_CUENTA();
            }
            t2 = (Double.parseDouble(variables.hm.substring(0, 2)) * 3600) + (Double.parseDouble(variables.hm.substring(2, 4)) * 60) + (Double.parseDouble(variables.hm.substring(4, 6)));

            if (variables.totalregistrosleidos == 0) {
                infoRegistroSalida.settablaRegistroSalida_TIEMPO("10");
            } else {

                if (variables.ultimotiempo > t2) {
                    if ((variables.ultimotiempo - t2) > 40000)
                        infoRegistroSalida.settablaRegistroSalida_TIEMPO("10");
                    else
                        infoRegistroSalida.settablaRegistroSalida_TIEMPO(parseStringToInteger((Math.abs(variables.ultimotiempo - t2) + "").trim()) + "");

                } else {
                    if ((t2 - variables.ultimotiempo) > 40000)
                        infoRegistroSalida.settablaRegistroSalida_TIEMPO("10");
                    else
                        infoRegistroSalida.settablaRegistroSalida_TIEMPO(parseStringToInteger((Math.abs(t2 - variables.ultimotiempo) + "").trim()) + "");
                }
            }
            variables.ultimotiempo = t2;
            infoRegistroSalida.settablaRegistroSalida_LECTOR(String.format("%4s", lector.trim()));
            infoRegistroSalida.settablaRegistroSalida_TERMINAL(terminalImei.trim());
            if (infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim().equals("")) {
                infoRegistroSalida.settablaRegistroSalida_IMPRESORA(String.format("%1$3s", impresora));
            }
            infoRegistroSalida.settablaRegistroSalida_intentos(("" + variables.nveces).trim());
            escribirTablasSalida(3);

            imagenLiquid_3.setImageResource(android.R.color.transparent);


// ...

            String causa = infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim();

            if (CAUSAS_CON_LECTURA_EN_INFORME.contains(causa)) {
                String sumaInforme = infoRegistroSalida.gettablaRegistroSalida_INFORME().trim()
                        + " / " + infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();

                if (sumaInforme.length() > 250) {
                    sumaInforme = sumaInforme.substring(0, 250);
                }
                infoRegistroSalida.settablaRegistroSalida_informe(sumaInforme);
            }

                if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {
                infoRegistroSalida.settablaRegistroSalida_leido("3");
                escribirTablasSalida(4);
            }
           // YaTomoFotoCliente=0;
            // proceso nuevo de novedades fotos para entrega
            if ((infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")
                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91")
                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92")
                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93"))
                    && VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: aqui no entra por anomalia
                // nuevo proceso si la factura es superior al valor indicado se reporta para evaluarlo

                // if (Double.parseDouble(valorLeidoFactura) > Double.parseDouble(VariablesGlobales.maximovalorentrega) || Double.parseDouble(valorLeidoFactura) < Double.parseDouble(VariablesGlobales.minimovalorentrega.trim())) {
                mensajeNovedad = "";
                if ((!tomaFotoBoton && infoRegistroSalida.gettablaRegistroSalida_INDOBLIGAFOTO().trim().equals("1"))) {
                    FotoObligatoriaXLectura = 1;
                    if (ControlEspecialZonaRoja.trim().equals("000") && YaTomoFotoCliente==0) {
                        ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 1, "E", 1);
                    }
                }
                //}

            } else // fin e inicio del proceso entrega a certificar

                if ((!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") &&
                        !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99") &&
                        !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91") &&
                        !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92") &&
                        !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93"))
                        || !infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("3")) { //Segun victor esta parte esta bien (tolima)

                    if (indicadorManual == 0) {// nuevo para cuando se le da  // automatico si esta en 1

                        mensajeNovedad = "";

                        if (!VariablesGlobales.tipoDeRuta.equals("E"))//!infoClienteEntrada.gettablaEntradaClientes_FACTURADOMICILIADA().trim().equals("E"))
                        {

                            if (!tomaFotoBoton) {
                                FotoObligatoriaXLectura = 1;
                                if (ControlEspecialZonaRoja.trim().equals("000") && YaTomoFotoCliente==0) {
                                    if ((s_foto.equals("1") && !infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals(""))
                                            || !infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("3")
                                            || infoRegistroSalida.gettablaRegistroSalida_INDOBLIGAFOTO().trim().equals("1"))
                                        ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 1, "", 3);//Ax: el ultimo parametro es las veces que insiste en tomar la foto
                                }
                            }
                        }
                    }
                } else {

                    if ((!infoRegistroSalida.gettablaRegistroSalida_OBSERANT().trim().equals("") && infoRegistroSalida.gettablaRegistroSalida_OBSERANT().trim() != infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().substring(0, 1))
                            || obligaFotografia.equals("1")
                            || infoRegistroSalida.gettablaRegistroSalida_INDOBLIGAFOTO().equals("1") || BorroLecturaOCausa == true) {
                        if (!tomaFotoBoton) {
                            FotoObligatoriaXLectura = 1;
                            if (ControlEspecialZonaRoja.trim().equals("000") && YaTomoFotoCliente==0) {
                                ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 0, "", 1);
                            }
                        }
                    } else {

                        if (s_foto.equals("1") || infoRegistroSalida.gettablaRegistroSalida_INDOBLIGAFOTO().trim().equals("1") || BorroLecturaOCausa == true) {
                            if (!tomaFotoBoton) {
                                FotoObligatoriaXLectura = 1;
                                if (ControlEspecialZonaRoja.trim().equals("000") && YaTomoFotoCliente==0) {
                                    ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 0, "", 1);
                                }
                            }
                        }
                    }
                }// fin del proceso para el consumo cero

            variableTomarDatosLectura = true;
            tiempoYDistanciaPromedio();

            if (VariablesGlobales.tipoDeRuta.equals("E") && infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")
                    && infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91")
                    && infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92")
                    && infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93")) {
                variables.totalSoloLecturas++;
            }
            if (!variables.estado.equals("5")) {
                variables.totalSoloLecturas++;
            }
            variables.totalregistrosleidos++;
            actualizarVariablesConteo();
            YaTomoFotoCliente=0;
            //sera pertinente aqui que si es verificacion no realice procesar 2 y valla es a tomar el informe y luego el informe es el que lo retome a procesar2???
            procesarLectura2(lecturaact);

            UltimoInforme = "";
            Ultimo_Estado = "";
            variables.nveces = 0;
        } catch (NumberFormatException ex) {
            logger.info("procesarLectura() " + ex.getMessage());
            mensajeT("Error al Procesar Lectura.", msgMedio);
            noactforesult = true;
            txtElectura.setText("");

        } catch (Exception e) {
            logger.info("procesarLectura(). " + e.getMessage());
            mensajeT("Error Error al Procesar Lectura", msgMedio);
            noactforesult = true;
            txtElectura.setText("");
        }
        return (0);
    }

    private int procesarLectura2(String lecturaact2) {

        try {
            long resultado = 0;

            if (requierenovedad == 1) {
                adicionarNovedad(2, fuePromediado);
            }
            requierenovedad = 0;

            resultado = validarEstadoRegistroNuevo(VariablesGlobales.registroactual);//(variables.clienteactual, false, "L");


            variableTomarDatosLectura = false;
            if (resultado > 0 || VariablesGlobales.tipoDeRuta.trim().equals("E")) {

                valorLeidoFactura = "0";
                VariablesGlobales.totalprediosleidos++; // ax otra vez??
                guardarCoordenadas();

                //este seria el punto de guardar los grupales
                if (!VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
                    if (SonBarrasGrupales == true && !UltimaBarraLeida.trim().equals("")) {
                        variables.impresora = "LE1";
                        txtElectura.setHint("LECTURA");
                        abriryEscribirBarras(UltimaBarraLeida);
                    }
                }

                // nuevo proceso para identificar la ultima cuenta en proceso de liquidacion
                File archivoValidador = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/VALIDADORIMPRESION.SDA");

                if (archivoValidador.exists()) {
                    archivoValidador.delete();
                }
                try {
                    RandomAccessFile rFile = new RandomAccessFile(archivoValidador, "rw");
                    rFile.writeBytes(infoRegistroSalida.gettablaRegistroSalida_CUENTA() + ";"
                            + String.format("%1$6s", ("" + VariablesGlobales.registroactual).trim()));
                    rFile.close();
                } catch (IOException ioe) {

                    try {
                        if (archivoValidador.exists())
                            archivoValidador.delete();
                    } catch (Exception e) {

                    }
                }
                fuePromediado = 0;// en esta parte se incluye la parte donde no se imprime ni liquida la factura

                if (nombreRuta().equals("9999999")) { // cambiar el estado del proceso porque la informacion se manda para entregas
                    infoRegistroSalida.settablaRegistroSalida_leido("5");
                    escribirTablasSalida(5);

                    if (!nivelOperador.equals("S")) {
                        if (!tomaFotoBoton) {
                            FotoObligatoriaXLectura = 1;
                            if (ControlEspecialZonaRoja.trim().equals("000") && YaTomoFotoCliente==0) {
                                ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 1, "S", 1);
                            }
                        }
                    }
                }

                apuntadortarifaCT = 0;
                apuntadortarifareactiva = 0;
                guardarEnvioGPRSNuevo(1, 0);
                tomaFotoBoton = false;
                //nueva para impresion en putumayo se paro por que no usan impresoras en girardor si se usa
                //PONER CONTROL SI ESE INDICADOR DE IMPRESORA NO ESTA ACTIVO NO ENVIAR
                String Estedessector = infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1);
                /*if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0,1).equals("L"))
                    {
                      //  EscribaArchivoImpresion("");
                        //nueva para impresion en putumayo se paro por que no usan impresoras en girardor si se usa
                        if (MarcaDeImpresora.equals("ZEBRA"))
                            EscribaArchivoImpresion("");
                        else {
                            //nuevo metodo de imprimir en las jal
                            Bitmap logo = BitmapFactory.decodeResource(
                                    getResources(),
                                    R.drawable.ic_lectura  ///aqui va el logo de hobo o de emsa
                            );
                            //finish();
                            EscribaArchivoImpresionESC(logo);
                        }

                    }
                else {
                    if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                        //este cambio es para que el operador de Verificaciones solo aqui vaya y capture
                        s_idac = VariablesGlobales.registroactual;
                        abrirInforme(false);
                    }
                }*/
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("L")) {
                   // if (!imprimirTrasFoto) {                          // <-- NUEVO
                        if (ControlEspecialZonaRoja.trim().equals("000")) {
                            if (ControlEspecialImpresora.equals("1")) {
                                 if (MarcaDeImpresora.equals("ZEBRA")) {
                                        if (VariablesGlobales.habilitadaimpresora == 1) {
                                            EscribaArchivoImpresion("");
                                        }
                                 }
                                else {
                                        if (VariablesGlobales.habilitadaimpresora == 1)
                                        {
                                            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_lectura);
                                            EscribaArchivoImpresionESC(logo);
                                        }
                                }
                            }
                        }
                  //  }                                                 // <-- NUEVO
                } else {
                    if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                        s_idac = VariablesGlobales.registroactual;
                        abrirInforme(false, UltimoInforme);
                    }
                }

                variables.impresora = "   ";
                ultimaCriticaLectura = "  ";
                VariablesGlobales.ultimaNovedad = "0000";

                valor_energia = "0";
                valor_alumbradopublico = "0";

                if (archivoValidador.exists()) {// nuevo proceso de borrado si se imprimio lo ultimo
                    archivoValidador.delete();
                }
                try {
                    if (VariablesGlobales.totalprediosleidos >= 10) {
                        File archivoLeidos = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LEYOMASDE10.TXT");
                        if (!archivoLeidos.exists()) {
                            RandomAccessFile rFile = new RandomAccessFile(archivoLeidos, "rw");
                            rFile.writeBytes(getPhoneDate());
                            rFile.close();
                        }
                    }
                } catch (Exception e) {
                }
                if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("12")) {
                    Crear_Cuenta_Nueva();
                }
                ventanaFirma = false;
                telefono = "";
                if (!scanEntregas) {
                    if ((variables.direcciondelectura == variables.haciaadelante)) {
                        if (avanzarRegistro() == 0)
                            retrocedeRegistro();
                    } else {
                        if (retrocedeRegistro() == 0)
                            avanzarRegistro();
                    }
                }
                scanEntregas = false;

                variables.numdigitos = 0;

                if (VariablesGlobales.totalregistrosleidos == infoRegistroSalida.getTotal_TablaRegistroSalida()) {// before totalprediosleidos
                    mensajeT("AAA. Proceso de Lecturas concluido ", msgLargo);
                    visualizarInformacionCliente(3);
                } else
                    visualizarInformacionCliente(0);
            } else {
                VariablesGlobales.registroactual = (int) (resultado);
                leerInformacionUsuario(1);

                if (VariablesGlobales.totalregistrosleidos == infoRegistroSalida.getTotal_TablaRegistroSalida()) {// before totalprediosleidos
                    mensajeT("BBB. Proceso de Lecturas concluido", msgLargo);
                    visualizarInformacionCliente(3);
                } else
                    visualizarInformacionCliente(0);
            }
        } catch (NumberFormatException ex) {
            logger.info("procesarLectura2() " + ex.getMessage());
            mensajeT("Error Procesar Lectura, con anomalias", msgMedio);
            noactforesult = true;
        } catch (Exception e) {
            logger.info("procesarLectura2(). " + e.getMessage());
            mensajeT("Error Procesar Lectura, con anomalias", msgMedio);
            noactforesult = true;
        }
        if (!VariablesGlobales.tipoDeRuta.equals("E")) { //Ax: para agilizar la 'entrega' se coloca un numero en lectura para que  procese de una vez, ahorrar un paso
            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99")) {
                imagenChat.setImageResource(R.drawable.chat_msg_si);
                //  MensajeriaCliente();
            }
            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V") && !VariablesGlobales.tipoDeRuta.equals("E")) {
                imagenChat.setImageResource(R.drawable.verificar);
                MensajeriaCliente();
            }
        }
        txtElectura.setText("");
        return (0);
    }

    //Ax: Devuelve double de String para evitar tanto try cacth en el codigo
    public double parseStringToDouble(String x) {
        try {
            double y = Double.parseDouble(x.trim());
            return y;
        } catch (NumberFormatException e) {
            logger.info("parseStringToDouble() " + e.getMessage());
            throw new RuntimeException("Error.Double parseo " + x);
        } catch (Exception e) {
            logger.info("parseStringToDouble(). " + e.getMessage());
            throw new RuntimeException("Error.Double parseo " + x);
        }
    }

    //Ax: Devuelve entero de String para evitar tanto try cacth en el codigo
    public int parseStringToInteger(String x) {

        try {

            if (x == null)
                return 0;

            x = x.trim();

            if (x.isEmpty())
                return 0;

            if (!x.matches("-?\\d+(\\.\\d+)?")) {
                logger.info("Valor no numérico: " + x);
                return 0;
            }

            if (x.contains(".")) {
                return (int) parseStringToDouble(x);
            }

            return Integer.parseInt(x);

        } catch (Exception e) {

            logger.info(e.getMessage());
            return 0;
        }
    }

    private void guardarEnvioGPRSNuevo(int procedimiento, int ID) {
        String CodBarras = "";
        //if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01"))
        String Micuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA();
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {//se agrega nuevo porque se identifico que cuando se toma el informe y antes de cerrar la cuenta se envia a transmitir y eso no se debe de hacer
            return;
        }
        if (VariablesGlobales.getObligabarras().equals("1")) {
            infoCodBarras.setArchivo_TablaCodBarras(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "S" + NombreArchivos);
            File file = new File(infoCodBarras.getArchivo_TablaCodBarras());
            if (file.exists()) {

                infoCodBarras.abrir_TablaCodBarras(infoCodBarras.getArchivo_TablaCodBarras());
                if (ID == 0)
                    infoCodBarras.lectura_TablaCodBarras(VariablesGlobales.registroactual);
                else
                    infoCodBarras.lectura_TablaCodBarras(ID);

                CodBarras = infoCodBarras.gettablaCodBarras_CODBARRAS();
                infoCodBarras.Cerrar_TablaCodBarras();
                //mensajeT("[ENTRE A CAPTURAR COD-BARRAS-->" +CodBarras +"###"+ "S" + NombreArchivos, msgLargo);
            }
            //mensajeT("[ENTRE A CAPTURAR COD-BARRAS-->" +CodBarras , msgLargo);
        }


        guardarDatosAEnviarNuevo(infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR(),
                infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES(),
                infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim(),
                infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA(),
                infoRegistroSalida.gettablaRegistroSalida_INTENTOS(),
                infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1(),
                infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2(),
                infoRegistroSalida.gettablaRegistroSalida_NROENTEROS(),
                infoRegistroSalida.gettablaRegistroSalida_LEIDO(),
                ("" + parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR())),
                misenvios.total_EnvioGPS, CodBarras);
    }
    public void preguntaCodigoOperador() {
        try {
            BDifNull bDifNull = new BDifNull(Realm.getDefaultInstance());
            CrudifNull crudifNull = bDifNull.obtenerdatabyid(1);
            variables.setGlobaloperario(crudifNull.getOperario());
            lector = crudifNull.getOperario();
            terminalImei = crudifNull.getImei().trim();
            impresora = crudifNull.getImpresora();
            VariablesGlobales.directorioactual = crudifNull.getPath();
            nivelOperador = crudifNull.getNivelOperador();
            serialPDA = crudifNull.getImei().trim();
            Log.e("Error bd", "fila " + filaseleccionada);
            Log.e("Error bd", "operario " + lector);
            Log.e("Error bd", "terminal " + terminalImei);
            Log.e("Error bd", "impresora " + impresora);
            Log.e("Error bd", "path " + VariablesGlobales.directorioactual);
            Log.e("Error bd", "nivelOperador " + nivelOperador);
        } catch (Exception e) {
           // utils.Log(logfile, "[MenuDeLiquidacion]esNulo() | ERROR ->" + e.getMessage());
            Log.e("Error al nulo", String.valueOf(e));
        }

        //finish();

    }

    private void guardarDatosAEnviarNuevo(String NroContador, String IdContador, String LecturaTomada, String causadenolectura, String Intentos, String LecturaModificada1, String LecturaModificada2, String Digitos, String criticaPDA, String lecturaanterior, int procedimiento, String CodBarras) {
        variables.datodebusqueda = "";
        String Voyaqui = "-- 0";

        int operVar = Integer.parseInt(variables.getGlobaloperario().isEmpty() ? "0" : variables.getGlobaloperario());
        if(infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().isEmpty() || operVar == 0){
            preguntaCodigoOperador();
        }
        if (infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().equals("") && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals(""))
            infoRegistroSalida.settablaRegistroSalida_LECTOR(String.format("%4s", lector.trim()));


        if (!infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().equals("") || !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {

            try {
                misenvios.setEnvioGPS_ciclo(infoRegistroSalida.gettablaRegistroSalida_CICLO());
                misenvios.setEnvioGPS_mununicipio(infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO());
                misenvios.setEnvioGPS_seccion(infoRegistroSalida.gettablaRegistroSalida_CODSECTOR());
                misenvios.setEnvioGPS_departamento(infoRegistroSalida.gettablaRegistroSalida_RUTA());
                Voyaqui = "-- 2";
                misenvios.setEnvioGPS_anno(variables.amd.substring(0, 4));
                misenvios.setEnvioGPS_mes(variables.amd.substring(4, 6));
                misenvios.setEnvioGPS_cuenta(infoRegistroSalida.gettablaRegistroSalida_CUENTA());
                misenvios.setEnvioGPS_NroContador(NroContador);

                if (infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA().contains("R")) {
                    IdContador = "3";
                }
                Voyaqui = "-- 3";
                if (IdContador.trim().equals(""))
                    misenvios.setEnvioGPS_idcontador("0");
                else
                    misenvios.setEnvioGPS_idcontador(IdContador);

                misenvios.setEnvioGPS_lecturatomada(LecturaTomada.trim());

                if (causadenolectura.equals("00")) {
                    causadenolectura = "";
                }
                //else 'if (causadenolectura.trim().contains("0")) {
                //    'causadenolectura = causadenolectura.replace("0", " ");
                //}

                misenvios.setEnvioGPS_causadenolectura(causadenolectura);
                if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals(""))
                    misenvios.setEnvioGPS_descanomalia("LECTURA EXITOSA");
                else {
                    anomaliaDeLectura.setEncontro_AnomaliaDeNoLectura(0);
                    if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
                        anomaliaDeLectura.lectura_AnomaliaDeNoLectura(1);
                        anomaliaDeLectura.buscarAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim());
                        anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();
                    }

                    if (anomaliaDeLectura.getEncontro_AnomaliaDeNoLectura() > 0) {
                        String temp = anomaliaDeLectura.getanomaliaDeNoLectura_DESCRIPCION().trim();
                        temp = String.format("%1$-50s", temp).substring(0, 30);
                        misenvios.setEnvioGPS_descanomalia(temp);
                    } else
                        misenvios.setEnvioGPS_descanomalia("Causa Sin Descripcion");
                }
                Voyaqui = "-- 4";
                String temp1 = infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1();

                if (VariablesGlobales.tipoDeRuta.equals("E")) {
                    misenvios.setEnvioGPS_comentario(infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim());
                } else
                    misenvios.setEnvioGPS_comentario(infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA());

                misenvios.setEnvioGPS_desccomentario(temp1 + "-" + variables.datodebusqueda);

                String fec = infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim();
                fec = fec.substring(6, 8) + fec.substring(4, 6) + fec.substring(0, 4);
                misenvios.setEnvioGPS_fechayhoralectura(fec);

                Voyaqui = "-- 5";
                Log.e("INFO", "Operador movil:" + infoRegistroSalida.gettablaRegistroSalida_LECTOR() + "@");
                if (infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().equals("")) {
                    infoRegistroSalida.settablaRegistroSalida_LECTOR(String.format("%4s", variables.getGlobaloperario().trim()));
                    misenvios.setEnvioGPS_CodLector(String.format("%10s", variables.getGlobaloperario().trim()));
                } else {
                    ///String.format("%-4s", infoRegistroSalida.gettablaRegistroSalida_LECTOR()
                    misenvios.setEnvioGPS_CodLector(String.format("%10s", variables.getGlobaloperario().trim()));
                }

                misenvios.setEnvioGPS_primermedidor("1");
                misenvios.setEnvioGPS_HoraImpresion(infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(0, 2) + ":" + infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(2, 4) + ":" + infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(4, 6));
                Log.e("INFO", "ID REGISTRO SALIDA:" + infoRegistroSalida.getId().trim() + "@");
                misenvios.setId(infoRegistroSalida.getId().trim());

                if (VariablesGlobales.isRutaDuplicada()) {
                    String RutaModificada = rutaCargadaPDA.trim();
                    misenvios.setEnvioGPS_NombreArchivo("L" + RutaModificada);
                } else {
                    misenvios.setEnvioGPS_NombreArchivo("L" + rutaCargadaPDA);
                }
                Voyaqui = "-- 6";

                if (procedimiento == 99999) {
                    misenvios.setEnvioGPS_Longitud(infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim());
                    misenvios.setEnvioGPS_Latitud(infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim());
                    misenvios.setEnvioGPS_NroSatelites("4");
                } else {
                    if (txtLatitud.getText().toString().trim().length() > 20) {
                        misenvios.setEnvioGPS_Longitud(txtLatitud.getText().toString().trim().substring(0, 19));
                    } else {
                        misenvios.setEnvioGPS_Longitud(txtLatitud.getText().toString().trim());
                    }
                    if (txtLongitud.getText().toString().trim().length() > 20) {
                        misenvios.setEnvioGPS_Latitud(txtLongitud.getText().toString().trim().substring(0, 19));
                    } else {
                        misenvios.setEnvioGPS_Latitud(txtLongitud.getText().toString().trim());
                    }
                    misenvios.setEnvioGPS_NroSatelites(numeroDeSatelitesGPS);
                }

                Voyaqui = "-- 7";
                misenvios.setEnvioGPS_Distancia(infoRegistroSalida.gettablaRegistroSalida_DISTANCIACALCULADA());

                if (fechayHoraReportadaGPS.trim() == "") {
                    misenvios.setEnvioGPS_FechaHoraSatelite(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                } else {
                    misenvios.setEnvioGPS_FechaHoraSatelite(fechayHoraReportadaGPS);
                }

                if (altitudReportadaGPS.trim().length() > 10) {
                    altitudReportadaGPS = altitudReportadaGPS.trim().substring(0, 9);
                }
                altitudReportadaGPS = altitudReportadaGPS.contains(",") ? altitudReportadaGPS.replace(",", ".") : altitudReportadaGPS;
                Voyaqui = "-- 8";
                altitudReportadaGPS = "" + parseStringToInteger(altitudReportadaGPS);

                misenvios.setEnvioGPS_AltitudSatelite(altitudReportadaGPS);
                misenvios.setEnvioGPS_Terminal(terminalImei);

                if (VariablesGlobales.ultimaNovedad.equals("0000"))
                    misenvios.setEnvioGPS_IndicadorNovedad("N");
                else
                    misenvios.setEnvioGPS_IndicadorNovedad("S");

                if (!variables.impresora.substring(0, 2).equals("LE") || infoRegistroSalida.gettablaRegistroSalida_NROIMPRESIONES().trim().equals("0"))
                    misenvios.setEnvioGPS_Indcodbarras("N");
                else
                    misenvios.setEnvioGPS_Indcodbarras("S");
                Voyaqui = "-- 9";
                misenvios.setEnvioGPS_Intentos(infoRegistroSalida.gettablaRegistroSalida_INTENTOS());
                misenvios.setEnvioGPS_criticapda(criticaPDA);

                int x = 0;

                if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("") && VariablesGlobales.tipoDeRuta.equals("E")) {
                    x = parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR());
                } else {
                    if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("") &&
                            !infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim().equals("")) {
                        infoRegistroSalida.settablaRegistroSalida_lecturatomada(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1());
                    }
                    Voyaqui += "** " + infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA() + "//**" + infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR();
                    if (!criticaPDA.trim().equals("5"))
                        x = Math.abs(parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA()) - parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR()));
                    else
                        x = 0;
                }

                String temp2 = x + "";
                temp2 = String.format("%1$10s", temp2).replace(" ", "0");
                misenvios.setEnvioGPS_ConsumoFacturado(temp2);
                Voyaqui = "-- 10";
                misenvios.setEnvioGPS_SubContribucion("0");

                misenvios.setEnvioGPS_Consumo1("0");
                misenvios.setEnvioGPS_Consumo2("0");
                misenvios.setEnvioGPS_Consumo3("0");

                misenvios.setEnvioGPS_NroConceptos("0");

                if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals(""))
                    misenvios.setEnvioGPS_IndFacturacion("N");
                else
                    misenvios.setEnvioGPS_IndFacturacion(infoRegistroSalida.gettablaRegistroSalida_LEIDO());

                String temp4 = "0";
                temp4 = String.format("%1$15s", temp4).replace(" ", "0");
                misenvios.setEnvioGPS_NroFactura(temp4);

                temp4 = "01/01/2017";
                temp4 = String.format("%1$8s", temp4).replace(" ", "0");
                Voyaqui = "-- 11";
                if (temp4.equals("00000000")) {
                    Calendar calenda = Calendar.getInstance();
                    String dia = calenda.get(Calendar.DAY_OF_MONTH) + "";
                    String anno = calenda.get(Calendar.YEAR) + "";
                    String mes = (calenda.get(Calendar.MONTH) + 1) + "";
                    dia = String.format("%1$2s", dia).replace(" ", "0");
                    anno = String.format("%1$4s", anno).replace(" ", "0");
                    mes = String.format("%1$2s", mes).replace(" ", "0");
                    misenvios.setEnvioGPS_FechaVence(dia + "/" + mes + "/" + anno);
                    misenvios.setEnvioGPS_FechaCorte(dia + "/" + mes + "/" + anno);
                } else {
                    String temp5 = "01/03/2023";
                    temp5 = String.format("%1$8s", temp5).replace(" ", "0");
                    misenvios.setEnvioGPS_FechaVence(temp5);
                    temp5 = "01/07/2024";
                    temp5 = String.format("%1$8s", temp5).replace(" ", "0");
                    misenvios.setEnvioGPS_FechaCorte(temp5);
                }
                Voyaqui = "-- 12";
                misenvios.setEnvioGPS_LecturaModificada1(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim());
                misenvios.setEnvioGPS_LecturaModificada2(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim());

                misenvios.setEnvioGPS_Digitos(Digitos);

                String dc = infoRegistroSalida.gettablaRegistroSalida_CUENTA().substring(6, 9);
                dc = String.format("%1$3s", dc).replace(" ", "0");

                misenvios.setEnvioGPS_digitochequeo(dc);
                misenvios.setEnvioGPS_Procesado("N");

                misenvios.setEnvioGPS_NumeroConceptos("0");
                misenvios.setEnvioGPS_PrimerConcepto("0");

                misenvios.setEnvioGPS_CritiaSIEC(ultimaCriticaLectura);
                misenvios.setEnvioGPS_CodNovedad(variables.ultimaNovedad);

                String temp6 = lecturaanterior.trim();
                temp6 = String.format("%1$10s", temp6).replace(" ", "0");
                misenvios.setEnvioGPS_LecturaAnterior(temp6);
                Voyaqui = "-- 13";
                if (lectModifica) {
                    lectModifica = false;
                    misenvios.setEnvioGPS_EstadoEnvio("M");
                } else {
                    misenvios.setEnvioGPS_EstadoEnvio("N");
                }

                misenvios.setEnvioGPS_fichacatastral(VariablesGlobales.versionApp);

                misenvios.setEnvioGPS_Tiempo(infoRegistroSalida.gettablaRegistroSalida_TIEMPO());

                if (infoRegistroSalida.gettablaRegistroSalida_UBICACION().trim().equals("1")) {
                    misenvios.setEnvioGPS_uso(infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO().trim());
                } else {
                    misenvios.setEnvioGPS_uso(infoRegistroSalida.gettablaRegistroSalida_UBICACION().trim());
                }
                misenvios.setEnvioGPS_marcamedidor(infoRegistroSalida.gettablaRegistroSalida_MARCA().trim());
                String Informeleido = infoRegistroSalida.gettablaRegistroSalida_INFORME();

                Voyaqui = "-- 14";
                if (!infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals(""))
                    misenvios.setEnvioGPS_informe(infoRegistroSalida.gettablaRegistroSalida_INFORME().trim());
                else {

                    misenvios.setEnvioGPS_informe("");

                }
                misenvios.setEnvioGPS_PERIODOLECTURA(infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim());
                misenvios.setEnvioGPS_CODIGO_SAC(infoRegistroSalida.gettablaRegistroSalida_CODIGO_SAC().trim());
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                    misenvios.setEnvioGPS_estrato("V");
                } else {
                    if (VariablesGlobales.getObligabarras().equals("0"))
                        misenvios.setEnvioGPS_estrato("2");
                    else {
                        if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().trim().equals("00"))
                            misenvios.setEnvioGPS_estrato("9");
                        else {
                            if (variables.impresora.trim().equals("LE5"))
                                misenvios.setEnvioGPS_estrato("3");
                            else
                                misenvios.setEnvioGPS_estrato("1");
                        }
                    }
                }
                Voyaqui = "-- 15";
                misenvios.setTotalcausasnolectura("" + variables.totalcausasnolectura);
                misenvios.setTotalLecturas("" + variables.totalSoloLecturas);
                misenvios.setTotalConsumoBajo("" + variables.totalConsumoBajo);
                misenvios.setTotalConsumoAlto("" + variables.totalConsumoAlto);
                misenvios.setTotalConsumoNormal("" + variables.totalConsumoNormal);
                misenvios.setTotalLecurasIguales("" + variables.totalLecturasIguales);
                misenvios.setTotalLeidas("" + variables.totalregistrosleidos);
                misenvios.setNumObser("" + variables.numObservaciones);
                misenvios.setNumInformes("" + variables.numInformes);
                misenvios.setTotalTiempoProm("" + variables.totalTiempoPromedio);
                Voyaqui = "-- 16";
                if (VariablesGlobales.getObligabarras().equals("0")) {
                    misenvios.setTotalDistanciaProm("" + variables.totalDistanciaPromedio);
                } else {
                    misenvios.setTotalDistanciaProm(CodBarras);
                }

                // ===== GUARDAR EN REALM (REEMPLAZA ARCHIVO PLANO) =====
                try {
                    boolean guardado = SyncHelper.guardarLecturaDesdeEnvioGPS(misenvios);
                    if (guardado) {
                        Log.i("MenuDeLiquidacion", "Lectura guardada en Realm: cuenta=" + misenvios.getEnvioGPS_CUENTA());
                        logger.info("Lectura guardada en Realm: cuenta=" + misenvios.getEnvioGPS_CUENTA());

                        // Disparo inmediato, como el modelo SOAP antiguo. El
                        // servicio coalesce la ráfaga: N lecturas seguidas = 1 ciclo.
                        LecturaSyncService.forzarSync(this);
                    } else {
                        Log.e("MenuDeLiquidacion", "No se pudo guardar lectura en Realm");
                        logger.error("No se pudo guardar lectura en Realm: cuenta=" + misenvios.getEnvioGPS_CUENTA());
                        mensajeT("Error guardando lectura en base de datos", msgMedio);
                    }
                    //*nuevo para guardar la informacion en el backup de envios en el disco alterno
                    misenvios.setEnvioGPS_CRNL("\r\n");
                    misenvios.archivo_EnvioGPS = VariablesGlobales.directorioBackUp + "ENVIOSGPRS.SDA";

                    File fileeg = new File(misenvios.archivo_EnvioGPS);

                    if (fileeg.length() == 0) {
                        fileeg.delete();
                    }

                    if (!fileeg.exists()) {
                        try {
                            fileeg.createNewFile();
                        } catch (Exception e) {
                            logger.info(" No se pudo crear:" + fileeg.getAbsolutePath());
                            e.printStackTrace();
                        }
                    }

                    if (misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS)) {
                        misenvios.escribir_EnvioGPS(misenvios.total_EnvioGPS + 1);
                        misenvios.total_EnvioGPS++;
                        idEnvioGps = misenvios.total_EnvioGPS;
                        misenvios.Cerrar_EnvioGPS();
/*                        Calendar calenda = Calendar.getInstance();
                        String dia = calenda.get(Calendar.DAY_OF_MONTH) + "";
                        dia = String.format("%1$2s", dia).replace(" ", "0");

                        String miArchivoOrigen = variables.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA";
                        String miArchivoDestino = VariablesGlobales.directorioBackUp + "CopiaRespaldoLecturas_" + dia + ".sda";
                        boolean resultado = copiarArchivo(miArchivoOrigen, miArchivoDestino);*/
                    }
                    //fin del guardado en el alterno


                } catch (Exception e) {
                    Log.e("MenuDeLiquidacion", "Error guardando en Realm: " + e.getMessage());
                    logger.error("Error guardando en Realm: " + e.getMessage() + Voyaqui);
                    mensajeT("Error guardando lectura: " + e.getMessage(), msgMedio);
                }
                // ===== FIN GUARDAR EN REALM =====

            } catch (Exception ex) {
                logger.info("no Crea envio gprs" + ex.getMessage() + Voyaqui);
                mensajeT("[MenuDeLiquidacion]guardarDatosAEnviarNuevo();" + ex.getMessage() + Voyaqui, msgLargo);
            }
        }
    }

    public static boolean copiarArchivo(String rutaOrigen, String rutaDestino) {
        File origen = new File(rutaOrigen);
        File destino = new File(rutaDestino);

        try {
            // Si no existe el archivo origen
            if (!origen.exists()) {
                System.out.println("El archivo origen no existe: " + rutaOrigen);
                return false;
            }

            // Si ya existe en el destino, lo borramos
            if (destino.exists()) {
                destino.delete();
            }

            // Crear directorios del destino si no existen
            File parentDir = destino.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Copiar contenido
            FileInputStream in = new FileInputStream(origen);
            FileOutputStream out = new FileOutputStream(destino);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            System.out.println("Archivo copiado correctamente a: " + rutaDestino);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String nombreRuta() {

        File archivo1 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/NOMBRE");
        String archivocargado = "";

        if (archivo1.exists()) {
            try {

                RandomAccessFile rFile = new RandomAccessFile(archivo1, "r");
                byte[] byteArray;
                int fileSize = (int) rFile.length();
                byteArray = new byte[fileSize];
                rFile.readFully(byteArray, 0, fileSize);
                archivocargado = new String(byteArray);

                archivocargado = archivocargado.substring(5, 14);//esta en 7 pero debe se 12

                rFile.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return archivocargado;
    }

    private void setRespuesta(String string) {
        respuesta = string;
    }

    private void adicionarNovedad(int tipo, int fuepromediado) {

        String causalCuentaNueva = "";
        String causalCuenta = infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA();

        String cuentanueva = infoRegistroSalida.gettablaRegistroSalida_CUENTA();

        if (tipo == 1) { //Ax: solo el menu envia 1

            // realizar una pregunta si es una cuenta nueva
            if (banderaadicionarNovedad == 0) {

                MostrarAlertDialog("Alerta de Novedad", "Novedad de Control\n\n[SI]  = Es Cuenta Nueva\n[NO] = Novedad Especifica de la Cuenta\n\n cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA(), "adicionarNovedad");
                return;
            } else {

                if (banderaadicionarNovedad == 1) {
                    setRespuesta("Si");
                }
                if (banderaadicionarNovedad == -1) {
                    setRespuesta("No");
                }
            }

            Calendar calendar = Calendar.getInstance();

            String hora = String.format("%1$2s", calendar.get(Calendar.HOUR_OF_DAY)).replace(" ", "0");
            String minutos = String.format("%1$2s", calendar.get(Calendar.MINUTE)).replace(" ", "0");
            String segundos = String.format("%1$2s", calendar.get(Calendar.SECOND)).replace(" ", "0");

            // cuenta = "999" + hora + minutos + segundos;
            causalCuenta = "77";
            causalCuentaNueva = "Cuenta Nueva...............";
            mensajeNovedad = "CUENTA NUEVA";

            if (respuesta.equals("Si")) {

                cuentanueva = "999" + hora + minutos + segundos;
            } else if (respuesta.equals("No")) {

                causalCuenta = "89";
                causalCuentaNueva = "Cuenta Con Novedad Especial";
                mensajeNovedad = "NOVEDAD ESPECIAL";
            }

        } else {
            causalCuenta = "88";
            causalCuentaNueva = "Novedad incluida al cliente";
        }

        // SI EL SISTEMA ES UNA NOVEDAD DE ENTREGA TOMAR EL CODIGO DE ESTA
        if (VariablesGlobales.tipoDeRuta.trim().equals("E"))//infoClienteEntrada.gettablaEntradaClientes_FACTURADOMICILIADA().trim().equals("E"))
            mensajeNovedad = "NOVEDAD ENTREGA";

        String archivo = "NOVEDADES.SDA";
        VariablesGlobales.ultimaNovedad = "0000";
    }

    private void cambiarValoresimpresion() {
        String archivoFormato = VariablesGlobales.directorioactual + "/VALORESFORMATO.LOG";
        String valortexto1 = "-10";
        String valortexto2 = "0";
        String valortexto3 = "0";

        File file = new File(archivoFormato);

        RandomAccessFile archivoLineas;
        byte[] byteArray;
        String texto;

        try {
            if (!file.exists()) {
                archivoLineas = new RandomAccessFile(file, "rw");
                archivoLineas.seek(archivoLineas.length());
                archivoLineas.writeBytes(valortexto1 + "\r\n");
                archivoLineas.writeBytes(valortexto2 + "\r\n");
                archivoLineas.writeBytes(valortexto3 + "\r\n");
                archivoLineas.close();
            }
            // si existe sacar los calores requeridos del archivo guardado
            else {
                RandomAccessFile leerArchivo = new RandomAccessFile(file, "rw");

                leerArchivo.seek(0);
                valortexto1 = leerArchivo.readLine().trim();
                // leerArchivo.seek(valortexto1.length());
                valortexto2 = leerArchivo.readLine().trim();
                // leerArchivo.seek(valortexto2.length());
                sinBarSence = parseStringToInteger(leerArchivo.readLine());
                leerArchivo.close();
            }
        } catch (NumberFormatException e) {

            e.printStackTrace();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        variables.crearArchivoImpresion1(valortexto1, valortexto2);
    }

//    private void estadoRespuesta(String mensaje) {
//
//        try {
//            File archivoErrores = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ERRORESPRESENTADOSENENVIOS.SDA");
//
//            if (!archivoErrores.exists()) {
//                RandomAccessFile escribirErrores = new RandomAccessFile(archivoErrores, "rw");
//                escribirErrores.writeBytes(mensaje);
//                escribirErrores.close();
//            } else {
//                RandomAccessFile escribirErrores = new RandomAccessFile(archivoErrores, "rw");
//                escribirErrores.seek(escribirErrores.length());
//                escribirErrores.writeBytes(mensaje);
//                escribirErrores.close();
//            }
//        } catch (IOException e) {
//            mensajeT("No se Pudo escribir en el archivo de Errores " + mensaje, msgCorto);
//        }
//    }

    private void ejecutarProcesoDeFoto(String nombre, String tipomed, int D2Digital, int directorio, String tipoProceso, int veces) {

        if (!activaCamara) return;

        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("E")) {//nuevo tolima celsia
            veces = 10;
        }
        vecesImagen = veces;

        if (variables.espc == 0) {

            activaLectorBarras = false;
            String directorioF = "/DCIM/FOTOGRAFIASL/";

            String existeFoto;
            // String nombreImagen;

            // Calendar calendar = Calendar.getInstance();
            File foto1;
            File foto2;

            try {
                String Separador = "_";
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                    Separador = "V";

                    //  existeFoto = VariablesGlobales.directorioactual + directorioF + nombre + "_" + tipomed + Separador;//calendar.get(Calendar.YEAR)

                }

//"_" + tipomed.trim()
                existeFoto = VariablesGlobales.directorioactual + directorioF + nombre.trim() + Separador;//calendar.get(Calendar.YEAR)

                foto1 = new File(existeFoto.trim() + "0" + ".jpg");
                namePhoto = nombre.trim() + Separador + "0";//+ "_" + tipomed.trim()

                if (foto1.exists()) {

                    // boolean salir = true;
                    int conteo = 1;

                    while (conteo < 10) {

                        foto2 = new File(existeFoto.trim() + (String.format("%1$1s", conteo).replace(" ", "0")) + ".jpg"); //Ax:  303123_A1_02.jpg
                        namePhoto = nombre.trim() + Separador + (String.format("%1$1s", conteo).replace(" ", "0"));
//+ "_" + tipomed.trim()
                        if (!foto2.exists()) { //Ax: Si la foto no existe, puedo tomar ese nombre
                            break;
                        }
                        conteo++;
                    }
                }

                VariablesGlobales.activarcamarafotografica = 1;

                if (!s_info.equals("1")) { //Ax: predomina el informe, antes que este mensaje

                    //  if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals(""))
                    //   infoRegistroSalida.settablaRegistroSalida_informe("DEBE TOMAR FOTOGRAFIA");
                }
             //   escribirTablasSalida(6);


                if (D2Digital == 0) {


                    nameForIntent = namePhoto + ".jpg";
                    nombredelaImagen = VariablesGlobales.directorioactual + directorioF + namePhoto + ".jpg"; //Ax: variable global para guardar nombre de imagen para el metodo II
                    Log.e("INFO", "[ejecutarProcesoDeFoto] cuentaFoto -> " + nombre);
                    Log.e("INFO", "[ejecutarProcesoDeFoto] idFoto -> " + infoRegistroSalida.getId());
                    idFoto = infoRegistroSalida.getId();
                    cuentaFoto = nombre;

                    capRegistroActual = VariablesGlobales.registroactual;//Ax: variable global que captura registro actual
                    ejecutarProcesoDeFotoII(veces);//va a tomar foto mientras el codigo continua

                } else {
                    mensajeT("Proceso a color esta en desarrollo", msgCorto);
                }

             //   escribirTablasSalida(7);

            } catch (Exception ex) {
                mensajeT("PROBLEMA EN ESCRIBIR LA FOTOGRAFIA...\n " + ex.getMessage(), msgLargo);
                logger.info("ejecutarProcesoDeFoto() " + ex.getMessage());
            }

            VariablesGlobales.activarcamarafotografica = 0;
            activaLectorBarras = true;
        }
    }

    private void ejecutarProcesoDeFotoII(int veces) {//Ax este metodo se ejecuta la primera en 'ejecutarProcesoDeFoto' y de nuevo cuando la camara se cierra (LLamado desde onActivityResult ) veces es la cantidad de reintentos de tomar foto
        try {
            if (veces > 0) {
                Uri output = Uri.fromFile(new File(nombredelaImagen));
                File x = new File(nombredelaImagen);

                vecesImagen--;
                imprimirTrasFoto = !tomaFotoBoton;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.e("error", "entra a foto ");
                    Log.e("INFO", "MenuDeLiquidacion|SDK MAYOR |entra a foto: " + nameForIntent);
                    nwdispatchTakePictureIntent();
                    nombrefoto = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + x.getName();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (!nombredelaImagen.trim().isEmpty()) {
                        // Uri output = Uri.fromFile(new File(nombredelaImagen));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                        startActivityForResult(intent, TAKE_PICTURE);

                        nombrefoto = output.getPath();
                    } else {
                        utils.MensajeTime("Ha ocurrido un error, debe Tomar Foto de Nuevo", "TOMAR FOTO DE NUEVO", this, 8);
                        logger.info("sin nombre de foto. otra var fotos:" + nombrefoto);
                    }
                }
            } else {
                vecesImagen = 0;

                if (exitoImagen > 0) { //la foto si existe (aparentemente)
                   /* if (YaImprimio == 0) {
                        if (YaTomoFotoCliente==0) {
                            if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().length()>0) {
                                imprimirLecturaTrasFoto();
                            }
                        }
                    }
                    YaImprimio = 0;*/
                    try {
                        TaskHelper.execute(new AsyncCallWS(), "ProcesaFotosAsync");
                    } catch (Exception ex) {
                        logger.info("ejecutarProcesoDeFotoII() " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            mensajeT("ERROR: Proceso Modulo de Fotografia II", msgCorto);
        }
    }

    private void imprimirLecturaTrasFoto() {
        try {
            if (!imprimirTrasFoto) return;                 // foto por botón u otra: no imprime
            imprimirTrasFoto = false;                      // consumir bandera

            if (VariablesGlobales.tipoDeRuta.trim().equals("E")) return;   // entregas no
            //if (VariablesGlobales.habilitadaimpresora == 0 && !new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/SINIMPRESORA.TXT").exists()) return;        // impresora off
            if (VariablesGlobales.habilitadaimpresora == 0) return;        // impresora off

            abrirArchivosDeFacturacion();
            infoRegistroSalida.lectura_TablaRegistroSalida(capRegistroActual);  // el cliente de la foto

            String sector = infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().trim();
            if (sector.length() > 0 && sector.substring(0, 1).equals("L")) {
                if (ControlEspecialZonaRoja.trim().equals("000")) {
                    if (ControlEspecialImpresora.equals("1")) {
                        if (MarcaDeImpresora.equals("ZEBRA")) {
                            if (VariablesGlobales.habilitadaimpresora == 1) {
                                EscribaArchivoImpresion("");
                            }
                        }
                         else {
                                if (VariablesGlobales.habilitadaimpresora == 1)
                                {
                                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_lectura);
                                    EscribaArchivoImpresionESC(logo);
                                }
                        }
                    }
                }
            }

            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual); // restaura
            cerrarArchivosFacturacion();
        } catch (Exception ex) {
            logger.info("imprimirLecturaTrasFoto() " + ex.getMessage());
        }
    }

    /*public String ejecutarProcesoDeFoto3() {
        String return_msg = " ✓ | ";
        String nombretempfoto = "";

        try {
            if (nombrefoto.trim().isEmpty()) {
                return " | ";
            } else {
                nombretempfoto = new File(nombrefoto).getName();
                nombretempfoto = nombretempfoto.substring(0, nombretempfoto.indexOf("_")); //Ax: sacar cuenta del nombre de la foto
            }
            Utils utils2 = new Utils();
            String mensajefoto = utils2.ReduceImagen2(nombrefoto, getPhoneDate() + "-" + getPhoneHour() + " " + txtLatitud.getText().toString() + " " + txtLongitud.getText().toString());

            File nombrefotoFile = new File(nombrefoto);

            if (!mensajefoto.trim().equals("")) {
                nombrefotoFile.delete();
                logger.info(mensajefoto + ", cuenta " + nombretempfoto + ", Registro:" + capRegistroActual + ", Foto:" + nombrefoto);
                return_msg = " |" + nombretempfoto;
            } else {

                //Ax: en este punto se procede a hacer envio de la foto, se crea el archivo F y se procede a enviar
                File foto = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "F" + NombreArchivos);

                if (!nombrefoto.trim().isEmpty()) {

                    String cadenaFoto = String.format("%11s", idFoto) + ";" + String.format("%9s", cuentaFoto) + ";" + String.format("%1$-50s", nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length())) + ";\r\n";
                    if (!utils2.leerArchivoFotos(foto, nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length()))) {
                        //utils2.EscribirLinea(new File(archivoEFotos + serialPDA + ".SDA"), cadenaFoto);
                        int longitud = cadenaFoto.length();
                        if (cadenaFoto != null && (longitud >= 73 && longitud <= 75))
                            utils2.EscribirLinea(new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "BKEnvioFotos.SDA"), cadenaFoto);
                        else
                        {
                            cadenaFoto = "    9999999" + ";" + "999999999" + ";" + String.format("%1$-50s", nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length())) + ";\r\n";
                            utils2.EscribirLinea(new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "BKEnvioFotos.SDA"), cadenaFoto);
                        }

                        //before 25
                        if (!utils2.EscribirLinea(foto, String.format("%1$-30s", nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length())) + ";X\r\n")) {
                            logger.info("Problema Grabando Nombre de Foto" + mensajefoto);
                            return_msg = " |" + nombretempfoto;
                        }

                        // ===== NUEVO: REGISTRAR FOTO EN REALM PARA SINCRONIZACIÓN REST =====
                        try {
                            String tipoMedidor = "";
                            String anno = "";
                            String mes = "";
                            String ciclo = "";

                            // Extraer datos del nombre de foto: CUENTA_TIPOMEDIDOR_NN.jpg
                            String nombreSinExt = nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1);
                            if (nombreSinExt.contains("_")) {
                                String[] partes = nombreSinExt.replace(".jpg", "").split("_");
                                if (partes.length >= 2) {
                                    tipoMedidor = partes[1]; // A1, A2, R1, etc.
                                }
                            }

                            // Obtener año/mes del período actual
                            if (variables.amd != null && variables.amd.length() >= 6) {
                                anno = variables.amd.substring(0, 4);
                                mes = variables.amd.substring(4, 6);
                            }

                            // Obtener ciclo
                            if (infoRegistroSalida != null) {
                                ciclo = infoRegistroSalida.gettablaRegistroSalida_CICLO();
                            }

                            // Registrar en Realm
                            SyncHelper.registrarFoto(
                                    cuentaFoto,                          // cuenta
                                    tipoMedidor,                         // tipo medidor
                                    idFoto,                              // id registro
                                    nombrefoto,                          // ruta completa de la foto
                                    anno,                                // año
                                    mes,                                 // mes
                                    ciclo,                               // ciclo
                                    variables.getGlobaloperario(),       // lector
                                    terminalImei                         // terminal
                            );

                            Log.i("MenuDeLiquidacion", "Foto registrada en Realm: " + nombreSinExt);
                            // La foto se toma DESPUES de despachar la lectura
                            // (el intent de camara tarda segundos), asi que la
                            // ventana de debounce ya cerro: sin esto la foto
                            // espera al tick periodico, hasta 2 min.
                            LecturaSyncService.forzarSync(this);
                        } catch (Exception e) {
                            Log.e("MenuDeLiquidacion", "Error registrando foto en Realm: " + e.getMessage());
                            logger.info("MenuDeLiquidacion | Error registrando foto en Realm: " + e.getMessage());
                            // No interrumpe el flujo - el archivo F ya se guardó
                        }
                        // ===== FIN NUEVO =====
                    }

                    if (nombrefotoFile.length() > 400000) {
                        utils2.ReduceImagen2(nombrefoto, "");//Ax: intento de bajarle a algunas fotos que se escapan al proceso de reduccion
                    }

                    utils.CrearCopia(nombrefotoFile.getAbsolutePath(), VariablesGlobales.directorioBackUp + nombrefotoFile.getName());

                } else {
                    return_msg = " |" + nombretempfoto;
                }
            }
        } catch (Exception ex) {
            logger.info("ejecutarProcesoDeFoto3 " + ex.getMessage());
            return_msg = " |" + nombretempfoto;
        }
        return return_msg;
    }
    */
    int YaImprimio = 0;

    public String ejecutarProcesoDeFoto3() {
        String return_msg = " ✓ | ";
        String nombretempfoto = "";

        try {
            if (nombrefoto.trim().isEmpty()) {
                return " | ";
            } else {
                nombretempfoto = new File(nombrefoto).getName();
                nombretempfoto = nombretempfoto.substring(0, nombretempfoto.indexOf("_"));
            }
            Utils utils2 = new Utils();
            String mensajefoto = utils2.ReduceImagen2(nombrefoto, getPhoneDate() + "-" + getPhoneHour() + " " + txtLatitud.getText().toString() + " " + txtLongitud.getText().toString());

            File nombrefotoFile = new File(nombrefoto);

            if (!mensajefoto.trim().equals("")) {
                nombrefotoFile.delete();
                logger.info(mensajefoto + ", cuenta " + nombretempfoto + ", Registro:" + capRegistroActual + ", Foto:" + nombrefoto);
                return_msg = " |" + nombretempfoto;
            } else {
                // ===== GUARDAR FOTO EN REALM (REEMPLAZA ARCHIVO PLANO) =====
                if (!nombrefoto.trim().isEmpty()) {
                    try {
                        String tipoMedidor = "";
                        String anno = "";
                        String mes = "";
                        String ciclo = "";

                        // Extraer tipo de medidor del nombre: CUENTA_TIPOMEDIDOR_NN.jpg
                        String nombreSinExt = nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1);
                        if (nombreSinExt.contains("_")) {
                            String[] partes = nombreSinExt.replace(".jpg", "").split("_");
                            if (partes.length >= 2) {
                                tipoMedidor = partes[1];
                            }
                        }

                        // Obtener año/mes del período actual
                        /*if (variables.amd != null && variables.amd.length() >= 6) {
                            anno = variables.amd.substring(0, 4);
                            mes = variables.amd.substring(4, 6);
                        }*/
                        String cadenafecha = infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim();
                        anno = cadenafecha.substring(0, 4);
                        mes = cadenafecha.substring(4);

                        // Obtener ciclo
                        if (infoRegistroSalida != null) {
                            ciclo = infoRegistroSalida.gettablaRegistroSalida_CICLO();
                        }
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |cuentaFoto:" + cuentaFoto);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |tipoMedidor:" + tipoMedidor);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |idFoto:" + idFoto);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |nombrefoto:" + nombrefoto);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |anno:" + anno);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |mes:" + mes);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |ciclo:" + ciclo);
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |variables.getGlobaloperario():" + variables.getGlobaloperario());
                        Log.e("INFO", "[MenuDeLiquidacion]ejecutarProcesoDeFoto3 |terminalImei:" + terminalImei);

                        // Registrar en Realm
                        boolean guardado = SyncHelper.registrarFoto(
                                cuentaFoto,
                                tipoMedidor,
                                idFoto,
                                nombrefoto,
                                anno,
                                mes,
                                ciclo,
                                variables.getGlobaloperario(),
                                terminalImei
                        );

                        if (guardado) {
                            Log.i("MenuDeLiquidacion", "Foto registrada en Realm: " + nombreSinExt);
                            logger.info("Foto registrada en Realm: " + nombreSinExt);
                            LecturaSyncService.forzarSync(this);
                        } else {
                            Log.e("MenuDeLiquidacion", "No se pudo registrar foto en Realm");
                            logger.error("No se pudo registrar foto en Realm: " + nombreSinExt);
                        }
                    } catch (Exception e) {
                        Log.e("MenuDeLiquidacion", "Error registrando foto en Realm: " + e.getMessage());
                        logger.error("Error registrando foto en Realm: " + e.getMessage());
                    }

                    // Reducir foto si es muy grande
                    if (nombrefotoFile.length() > 400000) {
                        utils2.ReduceImagen2(nombrefoto, "");
                    }
                    FotoObligatoriaXLectura = 0; //nuevo para abortar la foto
                    // Crear copia de respaldo
                    utils.CrearCopia(nombrefotoFile.getAbsolutePath(), VariablesGlobales.directorioBackUp + nombrefotoFile.getName());
                } else {
                    return_msg = " |" + nombretempfoto;
                }
                // ===== FIN GUARDAR FOTO EN REALM =====
                if (NumeroDeFotos > 1) {
                    //traer en el historico estos parametros.. por prueba envio estos del predio que realmente siguio
                    YaImprimio = 1;
                    ejecutarProcesoDeFoto(Parametro_CUENTA, Parametro_TIPOMEDIDA, tipoFotoDigital, 0, "", 1);
                    NumeroDeFotos = 0;
                }

            }
        } catch (Exception ex) {
            logger.info("ejecutarProcesoDeFoto3 " + ex.getMessage());
            return_msg = " |" + nombretempfoto;
        }
        return return_msg;
    }


    private boolean evaluarDistancia() {

        try {  // SE CLAUSURA y SE PONE LA FUNCION POR MEDIDOR ANTES DE LIQUIDAR EL ARCHIVO
            if (indicadorManual == 0) {

                if (encenderEstadoActualGPS == 1) {
                    if (txtLatitud.getText().toString().trim().equals("0.00") || txtLatitud.getText().toString().trim().equals("0") || txtLatitud.getText().toString().trim().equals("")) {
                        capturarCoordenadasPredio();
                    }
                    if (txtLongitud.getText().toString().trim().equals("0.00") || txtLongitud.getText().toString().trim().equals("0") || txtLongitud.getText().toString().trim().equals("")) {
                        capturarCoordenadasPredio();
                    }
                    ultimaLongitud = txtLongitud.getText().toString();

                    if (reportarDistancia() < 1) {
                        return false;
                    }
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.info("error evAluardistancia 1 " + ex.getMessage());
            return false;
        }
        return false;
    }

    private void guardarCoordenadas() {  //Ax: Esto se debe hacer para entregas tambien

        try {
            String texto;
            String cuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA();
            String longitud = txtLongitud.getText().toString().trim();
            String latitud = txtLatitud.getText().toString().trim();

            if (longitud.length() > 12) {
                longitud = longitud.substring(0, 12);
                txtLongitud.setText(longitud);
            }
            if (latitud.length() > 12) {
                latitud = latitud.substring(0, 12);
                txtLatitud.setText(latitud);
            }

            texto = String.format("%1$-10s", cuenta.trim()) + ";" + String.format("%1$12s", longitud.trim()) + ";" + String.format("%1$12s", latitud.trim()) + ";" + String.format("%1$2s", numeroDeSatelitesGPS.trim()) + ";" + "\r\n";

            File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "G" + NombreArchivos);
            utils.EscribirLinea(file, texto);
        } catch (Exception ex) {
            logger.info("guardarCoordenadas() " + ex.getMessage());
            mensajeT("Problemas al guardar Coordenadas", msgLargo);
        }
    }

    private int reportarDistancia() {

        String longitud;
        String latitud;

        try {
            if (infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim().equals(""))
                infoRegistroSalida.settablaRegistroSalida_cordenadax("0");
            if (infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim().equals(""))
                infoRegistroSalida.settablaRegistroSalida_cordenaday("0");

            if (txtLongitud.getText().toString().trim().equals("") || txtLongitud.getText().toString().trim().equals("00"))//00 antes era "Desconocida"
                longitud = "0";
            else
                longitud = txtLongitud.getText().toString().trim();

            if (txtLatitud.getText().toString().trim().equals("") || txtLatitud.getText().toString().trim().equals("00"))//antes era "Desconocida"
                latitud = "0";
            else
                latitud = txtLatitud.getText().toString().trim();

            int distancia;
            if (parseStringToDouble(longitud) == 0 || parseStringToDouble(latitud) == 0) {
                distancia = 150;
            } else {
                distancia = (int) variables.evaluarDistanciaAlPredio(parseStringToDouble(longitud), parseStringToDouble(latitud), parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim()), parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim()));
            }

            infoRegistroSalida.settablaRegistroSalida_distanciacalculada(Integer.toString(distancia).trim());

            if (VariablesGlobales.getTotalClientesReales() > 0) {
                if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                    //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (10)"); //temporal?
                    infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                }
            } else {
                //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (11)"); //temporal?
                infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
            }
            if (distancia > 250) {
                if ( ControlEspecialZonaRoja.trim().equals("000")) {
                    if ((Double.parseDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim()) != 0) && (parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim()) <= 2) && (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals(""))) {

                        mensajeT("FAVOR UBIQUESE FRENTE AL MEDIDOR", msgMedio);//ESTA CUENTA SE REGISTRO SIN ESTAR UBICADO EN EL PREDIO\nSE RECODIFICARA LA UBICACION \n DEL  PREDIO");
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            logger.info("reportarDistancia() " + e.getMessage());
            mensajeT("No Hay coordenadas iniciales", msgMedio);
            return 0;
        }
        return 1;
    }

    private void capturarCoordenadasPredio() {

        if (encenderEstadoActualGPS == 1) {

            if (txtLatitud.getText().toString().trim().equals("0") || txtLatitud.getText().toString().trim().equals("0.00")) {
                txtLatitud.setText(longitudActualGpg.trim().replace(",", "."));
            }

            if (txtLongitud.getText().toString().trim().equals("0") || txtLongitud.getText().toString().trim().equals("0.00"))
                txtLongitud.setText(latitudActualGpg.trim().replace(",", "."));
        } else {

            txtLatitud.setText("0.00");
            txtLongitud.setText("0.00");
            numeroDeSatelitesGPS = "0";
            velocidadGPS = "0";
            Calendar calendar = Calendar.getInstance();
            fechayHoraReportadaGPS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        }
    }

    private int LecturaRegistroNuevo(int primero, int N_veces, double l_actual, String Estado, String lectura_act, double c_actual) {

        int diales, contador_diales;
        double l_minima_2, l_maxima_3, l_minima_1, potencia;
        double l_anterior, promedio, l_cadena;
        String cadena;
        int giro_del_registro;
        double diferencia;
        imagenLiquid_1.setImageResource(android.R.color.transparent);//ApagaImagenes();
        imagenLiquid_2.setImageResource(android.R.color.transparent);
        imagenLiquid_3.setImageResource(android.R.color.transparent);
        TOMARNUEVACRITICA = 0;
        obligaFotografia = "0";

        if (variables.nveces <= 0)
            if (infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim().equals(""))
                variables.nveces = 0;
            else
                variables.nveces = parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_INTENTOS());

        if (variables.nveces >= 3) { //Ax este numero es del numero de intentos, abajo hay 2 mas
            mensajeT("Error ...\n" + "NUMERO DE\n" + "REINTENTOS MAYOR\n" + "AL MAXIMO PERMITIDO\n", msgMedio);//402.
            return (0);
        }

        if (infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE().trim().equals(""))
            infoRegistroSalida.settablaRegistroSalida_Consumopromediocliente("       0");

        if (infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim().equals(""))
            infoRegistroSalida.settablaRegistroSalida_Lecturaanterior("       0");

        l_anterior = parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR());
        promedio = parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE());

        if (variables.numdigitos == 0) {
            if (!infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim().equals(""))
                diales = parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_NROENTEROS());
            else
                diales = 7;
        } else {
            diales = variables.numdigitos;
            if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals(""))
                infoRegistroSalida.settablaRegistroSalida_informe("CAMBIO DE DIGITOS " + diales);
        }
        if (diales == 0) {            //mensajeT("MAL NUMERO ENTEROS", msgFugaz);
            diales = 7;
        }
        potencia = 1;
        contador_diales = diales;

        while (contador_diales > 0) {
            --contador_diales;
            potencia = potencia * 10;
        }
        visualizarInformacionCliente(2);// mostrar_cliente(2);
        diferencia = (double) 0;

        if (variables.nveces < 3) { //Ax este numero es del numero de intentos,
            if (lectura_act.length() == 0) //la lectura no debe ser cero
            {
                cadena = "0";//lectura_act;
            } else {
                ++variables.nveces;
                cadena = lectura_act;

            }
            variables.lactual = parseStringToDouble(cadena);

            if (infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim().equals("")) {
                infoRegistroSalida.settablaRegistroSalida_lecturamodificada1(cadena);
                escribirTablasSalida(8);
            } else {
                if (infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim().equals("")) {
                    infoRegistroSalida.settablaRegistroSalida_lecturamodificada2(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1());
                    infoRegistroSalida.settablaRegistroSalida_lecturamodificada1(cadena);
                    escribirTablasSalida(9);
                }
            }

            boolean ec = false;

           /* if (misRangos.abrir_Rangos(misRangos.getarchivo_Rangos())) {
                ec = true;
                misRangos.setencontro_Rangos(0);
                misRangos.buscarSecuencial_Rangos(String.format("%1$10s", infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE().trim()).replace(' ', '0'));
            }*/

         /*   if (misRangos.getencontro_Rangos() >= 1) {
                double valorMedio1 = (parseStringToDouble(misRangos.getRangos_ALTO()) - parseStringToDouble(misRangos.getRangos_MEDIO())) / 2;
                double valorMedio2 = (parseStringToDouble(misRangos.getRangos_MEDIO()) - parseStringToDouble(misRangos.getRangos_LEVE())) / 2;

                l_maxima_3 = l_anterior + promedio * ((parseStringToDouble(misRangos.getRangos_ALTO()) / 100));
                l_maxima_2 = l_anterior + promedio * (((parseStringToDouble(misRangos.getRangos_ALTO()) - valorMedio1) / 100));
                l_maxima_1 = l_anterior + promedio * ((parseStringToDouble(misRangos.getRangos_MEDIO()) / 100));
                l_minima_1 = l_anterior + promedio * (((parseStringToDouble(misRangos.getRangos_LEVE()) + valorMedio2) / 100));
                l_minima_2 = l_anterior + promedio * ((parseStringToDouble(misRangos.getRangos_LEVE()) / 100));
            } else {*/


            String tempLimt = infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_SUPERIOR();
            if (parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_SUPERIOR()) == 0) {
                double rspLim = (promedio * 1.8);
                infoRegistroSalida.settablaRegistroSalida_CONSUMO_LIM_SUPERIOR(String.valueOf(rspLim));
            }
            obligaFotografia = "0";
            l_maxima_3 = l_anterior + (parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_SUPERIOR()));

            l_minima_2 = l_anterior + (parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CONSUMO_LIM_INFERIOR()));

            infoRegistroSalida.settablaRegistroSalida_CONSUMO_LIM_SUPERIOR(tempLimt);
            //    }
            giro_del_registro = 0;

            if (variables.lactual < l_anterior) {
                giro_del_registro = 1;
            }
            //nueva critica

            if (variables.lactual == l_anterior) {
                ++(variables.nveces);
                predio_temporal = 1;
                lecturasiguales = true;
                if ((variables.lect2 == variables.lactual && variables.nveces >= 2) ) {

                    variables.estado = "2";  // LECTURAS IGUALES consumo muy bajo.
                    if (VariablesGlobales.getTotalClientesReales() > 0) {
                        if (VariablesGlobales.registroactual <= VariablesGlobales.getTotalClientesReales()) {
                            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (14)"); //temporal?
                            infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                        }
                    } else {
                        //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (15)"); //temporal?
                        infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                    }
                    variables.lect2 = -1;
                    imagenLiquid_1.setImageResource(android.R.color.transparent);
                    imagenLiquid_2.setImageResource(android.R.color.transparent);
                    imagenLiquid_3.setImageResource(android.R.color.transparent);
                    return (1);
                } else {
                    variables.estado = "2";  // LECTURAS IGUALES consumo muy bajo.
                    imagenLiquid_3.setImageResource(R.drawable.imagen_lecturas_iguales);
                }
            } else {
                if ((variables.lactual >= l_minima_2 && variables.lactual <= l_maxima_3)) {//|| infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE().trim().equals("0") || infoRegistroSalida.gettablaRegistroSalida_CONSUMOPROMEDIOCLIENTE().trim().equals("")
                    TOMARNUEVACRITICA = 0;
                    obligaFotografia = "0";
                    variables.estado = "3";
                    variables.lect2 = -1;
                    return (1);
                } else {
                    if (variables.lactual <= l_minima_2) {
                        variables.estado = "9";

                        imagenLiquid_3.setImageResource(R.drawable.imagen_consumo_bajo);
                    } else {
                        if (variables.lactual > l_maxima_3) {
                            variables.estado = "1";
                            if (misRangos.getencontro_Rangos() > 0)
                                obligaFotografia = misRangos.getRangos_OBLIGAFOTOALTO();
                            imagenLiquid_3.setImageResource(R.drawable.imagen_consumo_muy_alto);
                        }
                    }
                }
            }
            //}
            //if (ec)
            //    misRangos.Cerrar_Rangos();

            if (giro_del_registro > 0) {//before && !variables.estado.trim().equals("9")
                imagenLiquid_3.setImageResource(R.drawable.imagen_negativo);
                variables.estado = "7";
            }
            //Lectura Menor a la anterior

            if (variables.lect2 != variables.lactual) {
                variables.lect2 = variables.lactual;

                if (variables.nveces > 2) //Ax este numero es del numero de intentos,
                    return (1);
                return (0);
            } else {
                variables.lect2 = -1;
                return (1);
            }
        }
        return (0);
    }


    private int procesarEntregaLecturas(int tipoEntrega, int numero) {

        long resultado;
        double t2;
        variables.ultimoregistro = VariablesGlobales.registroactual;
        VariablesGlobales.registroactual = numero;
        leerInformacionUsuario(1);

        if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("")) {
            infoRegistroSalida.settablaRegistroSalida_causadenolectura("99");
        }
        infoRegistroSalida.settablaRegistroSalida_lecturatomada("000000000");
        tomarFechaSistema("", "");
        infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
        infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
        infoRegistroSalida.settablaRegistroSalida_TERMINAL(terminalImei.trim());
        variables.estado = "5";
        infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);

        infoRegistroSalida.settablaRegistroSalida_lecturatomada(String.format("%1$9s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()));
        variables.estado = "5";
        variables.nveces++;
        infoRegistroSalida.settablaRegistroSalida_intentos(("" + variables.nveces).trim());
        ultimaLatitud = txtLatitud.getText().toString();
        ultimaLongitud = txtLongitud.getText().toString();
        infoRegistroSalida.settablaRegistroSalida_leido(variables.estado);
        infoRegistroSalida.settablaRegistroSalida_LECTOR(lector.trim());
        infoRegistroSalida.settablaRegistroSalida_TERMINAL(terminalImei.trim());
        infoRegistroSalida.settablaRegistroSalida_IMPRESORA(impresora);

        escribirTablasSalida(10);
        leerInformacionUsuario(1);
        return 1;
    }

    private boolean autorizacion() {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HHmm");
        String horaPuntual = df.format(dt.getTime());

        int cuentasleidas = 1;// identificar el numero de cuentas a validar antes de las 9:00

        if (VariablesGlobales.getActivar_validar_horario().trim().equals("0")) { //Ax:siempre va a ser cero
            return true;
        }

        if (infoRegistroSalida.gettablaRegistroSalida_NOMBRE().substring(47, 48).equals("U"))
            cuentasleidas = 10;

        if (VariablesGlobales.totalprediosleidos < cuentasleidas && parseStringToInteger(horaPuntual) > 900) {
            File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LEYOMASDE10.TXT");

            if (file.exists()) {
                return true;
            }
            try {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
                String fecha = df2.format(dt.getTime());

                File archivoAdmin2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/LECTURAADMINISTRADA.TXT");

                if (!archivoAdmin2.exists()) {
                    mensajeT("No Tiene Autorizacion\n Para trabajar en este Horario... \n Consulte con el Administrador", msgLargo);
                    // Toast.makeText(getApplicationContext(), "No Tiene Autorizacion\n Para trabajar en este Horario... \n Consulte con el Administrador", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (archivoAdmin2.exists()) {
                        RandomAccessFile archivoAdmin = new RandomAccessFile(archivoAdmin2, "r");
                        byte[] byteArray;
                        int fileSize = (int) archivoAdmin.length();
                        byteArray = new byte[fileSize];
                        archivoAdmin.readFully(byteArray, 0, fileSize);
                        String datosLeido = new String(byteArray);
                        archivoAdmin.close();

                        if (Double.parseDouble(datosLeido) == Double.parseDouble(fecha))
                            return true;
                        else {
                            mensajeT("Terminal con Autorizacion\n Desactualizada favor... \nAutorizar de nuevo", msgLargo);
                            // Toast.makeText(getApplicationContext(), "Terminal con Autorizacion\n Desactualizada favor... \nAutorizar de nuevo", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                mensajeT("Problemas en Validacion de Horario Permitido", msgMedio);
                // Toast.makeText(getApplicationContext(), "Problemas en Validacion de Horario Permitido", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPT_FIRMA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //estariamos guardando aqui la informacion de la firmafoto
                String tipoMedidor = "";
                String anno = "";
                String mes = "";
                String ciclo = "";
                String cadenafecha = infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim();
                anno = cadenafecha.substring(0, 4);
                mes = cadenafecha.substring(4);
                ciclo = infoRegistroSalida.gettablaRegistroSalida_CICLO();
                cuentaFoto = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();
                idFoto = infoRegistroSalida.getId();
                nombrefoto = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "_FM_01.jpg";
                // Registrar en Realm
                boolean guardado = SyncHelper.registrarFoto(
                        cuentaFoto,
                        tipoMedidor,//ya
                        idFoto,
                        nombrefoto,
                        anno,//ya
                        mes,//ya
                        ciclo,//ya
                        variables.getGlobaloperario(),//ya
                        terminalImei//ya
                );

                if (guardado) {
                    Log.i("MenuDeLiquidacion", "Firma registrada en Realm: " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "_FM_01.jpg");
                    logger.info("Firma registrada en Realm: " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "_FM_01.jpg");
                    LecturaSyncService.forzarSync(this);
                } else {
                    Log.e("MenuDeLiquidacion", "No se pudo registrar firma en Realm");
                    logger.error("No se pudo registrar firma en Realm: " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "_FM_01.jpg");
                }

                telefono = s_info = data.getStringExtra("telefono");
                abrirArchivosDeFacturacion();
                if (causalEntrega) {
                    procesarLectura(0, 0, "", VariablesGlobales.datodebusqueda.trim());
                } else {
                    procesarLectura(0, 0, "", "99");
                }
                cerrarArchivosFacturacion();
                causalEntrega = false;
            }
        } else if (requestCode == TAKE_PICTURE) {//Respuesta Tomar fotos en liq.

            if (resultCode == RESULT_OK) {
                Log.e("error", "data " + data);
                if (data != null) {

                    if (data.hasExtra("output")) {
                        name = data.getParcelableExtra("output");
                    }
                }
                new MediaScannerConnectionClient() {   // Para guardar la imagen en la galería, utilizamos una conexión a un MediaScanner
                    private MediaScannerConnection msc = null;

                    {
                        msc = new MediaScannerConnection(getApplicationContext(), this);
                        msc.connect();
                    }

                    public void onMediaScannerConnected() {
                        msc.scanFile(name, null);
                    }

                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                    }
                };
                vecesImagen = 0;
                exitoImagen = 1;
                if (YaTomoFotoCliente == 1)//alerta donde poner esta opcion
                {
                    YaTomoFotoCliente = 0;
                    return;
                }

               /* if (NumeroDeFotos>1)
                {
                    NumeroDeFotos = NumeroDeFotos - 1;
                    vecesImagen = NumeroDeFotos;
                }*/
                ejecutarProcesoDeFotoII(vecesImagen);//Ax: idealmente se tomo la foto
            } else {//Ax: No se tomo foto

                if (vecesImagen > 0) {
                    mensajeT("No se tomo foto, pruebe de nuevo", msgMedio);
                }
                if (YaTomoFotoCliente == 1)//alerta donde poner esta opcion
                {
                    YaTomoFotoCliente = 0;
                    return;
                }
                vecesImagen = 1;
              /*  if (NumeroDeFotos>1)
                {
                    vecesImagen = NumeroDeFotos;
                }*/

                ejecutarProcesoDeFotoII(vecesImagen);
            }


        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {//Respuesta Codigo de barras. SCAN_CODE

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null) {
                String barcode = scanResult.getContents();

                if (barcode != null) {
                    setScannedInfo(barcode);
                } else {
                    mensajeT("No se obtuvo ningun codigo...", msgCorto);
                }
            } else {
                mensajeT("No se obtuvo ningun codigo...", msgCorto);
            }
        } else if (requestCode == VariablesGlobales.REQUEST_ENABLE_BT) {

        } else if (requestCode == CAPT_COMENTARIO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                s_info = data.getStringExtra("info"); //Recupera si informe es obligatorio
                s_foto = data.getStringExtra("foto"); //Recupera si foto es obligatorio
                s_idac = VariablesGlobales.registroactual;
                abrirArchivosDeFacturacion();//Ax refresca los datos actuales que deberia hacer en [CaptCom01] para poder saber si se metio algun codigo de comentario
                infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);

                if (s_foto.equals("1")) {
                    if (!tomaFotoBoton) {
                        if (ControlEspecialZonaRoja.trim().equals("000")) {
                            ejecutarProcesoDeFoto(infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim(), infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA(), tipoFotoDigital, 0, "", 1);
                        }
                    }
                }
                cerrarArchivosFacturacion();

                if (s_info.equals("1")) {
                    abrirInforme(false, UltimoInforme);
                }

                if (lecturasiguales || TOMARNUEVACRITICA > 0) {//|| TOMARNUEVACRITICA > 0 SE CLAUSURA PARA NO OPERAR POR EL MOMENTO
                    abrirArchivosDeFacturacion();
                    procesarLectura(1, 0, cc_lecturaact, "XY");//sigue donde se quedo
                    cerrarArchivosFacturacion();
                    lecturasiguales = false;
                    TOMARNUEVACRITICA = 0;
                    cc_lecturaact = "";
                }
            } else {
                s_info = "0";
                s_foto = "0";
                s_idac = 0;
            }

        } else if (requestCode == ANOMALIA_REQUEST_CODE) {
            permiteLectura = "0";
            medidorCero = false;
            if (resultCode == RESULT_OK) {

                causal = data.getStringExtra("snumero"); //Recupera el numero de anomalia
                s_info = data.getStringExtra("info"); //Recupera si informe es obligatorio
                s_foto = data.getStringExtra("foto"); //Recupera si foto es obligatorio
                permiteLectura = data.getStringExtra("permiteLectura"); //Recupera si permiteLectura
                s_idac = parseStringToInteger(data.getStringExtra("registroActual")); //Recupera si permiteLectura
                UltimoInforme = data.getStringExtra("UltimoInforme"); //Recupera el numero de anomalia
                // s_idac = VariablesGlobales.registroactual;
                ProcesarAnomalias();
            } else {
                s_info = "0";
                s_foto = "0";
                permiteLectura = "0";
                s_idac = 0;
            }
        } else if (requestCode == INFORME_REQUEST_CODE) { //Actividad informe
            if (resultCode == RESULT_OK) {
                try {
                    String categoria = data.getStringExtra("catg");
                    String sinfo = data.getStringExtra("info"); //Recupera el mensaje
                    s_idac = parseStringToInteger(data.getStringExtra("registroActual")); //Recupera si permiteLectura
                    ingresarInforme(sinfo, categoria, s_idac, permiteLectura.trim());
                } catch (Exception ex) {
                    logger.info("onActivityResult() INFORME_REQUEST_CODE " + ex.toString());
                }
            }
        } else if (requestCode == BUSQUEDA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    VariablesGlobales.datodebusqueda = data.getStringExtra("idcliente");

                    VariablesGlobales.tipodebusqueda = parseStringToInteger(data.getStringExtra("opcion"));

                    if (!VariablesGlobales.datodebusqueda.equals("")) // realizar Busqueda
                        buscarCuentaMedidor(VariablesGlobales.datodebusqueda.trim(), 0);
                } catch (Exception ex) {
                    logger.info("onActivityResult() BUSQUEDA_REQUEST_CODE " + ex.toString());
                }
            }
        } else if (requestCode == CONSULTNONVIADS_REQUEST_CODE) {//Modulo de ConsultaNoEnviados

            if (resultCode == RESULT_OK && data != null) {

                try {
                    // abrirArchivosDeFacturacion();
                    ProbarWS();

                } catch (Exception ex) {
                    logger.info("onActivityResult() CONSULTNONVIADS_REQUEST_CODE " + ex.toString());
                }
            }

        } else if (requestCode == OPCIONESSUPERVI_REQUEST_CODE) {//Opciones de supervisor

            if (resultCode == RESULT_OK && data != null) {

                try {
                    boolean navegacionPrevia = navegacionRegistrosHabilitada;
                    navegacionRegistrosHabilitada = data.getBooleanExtra("navegacionHabilitada", navegacionRegistrosHabilitada);
                    if (navegacionRegistrosHabilitada) {
                        btnAdelante.setVisibility(View.VISIBLE);
                        btnAtras.setVisibility(View.VISIBLE);
                        btnAtras.setEnabled(navegacionRegistrosHabilitada);
                       btnAdelante.setEnabled(navegacionRegistrosHabilitada);
                    }
                    else {
                        if (!terminalImei.trim().equals("351007492166830") && !terminalImei.trim().equals("358767141019390")
                                && !terminalImei.trim().equals("868995078034628")) {
                            btnAdelante.setVisibility(View.INVISIBLE);
                            btnAtras.setVisibility(View.INVISIBLE);
                            btnAtras.setEnabled(navegacionRegistrosHabilitada);
                            btnAdelante.setEnabled(navegacionRegistrosHabilitada);
                        }
                        else
                        {
                            btnAdelante.setVisibility(View.VISIBLE);
                            btnAtras.setVisibility(View.VISIBLE);
                            btnAtras.setEnabled(true);
                            btnAdelante.setEnabled(true);
                        }
                    }


                    boolean desactivarImpresoraObligatoria = data.getBooleanExtra("desactivarImpresoraObligatoria", false);
                    boolean asignanolectura = data.getBooleanExtra("asignanolectura", false);//Ax: Asigna no lectura
                    String asignanocausa = data.getStringExtra("asignanocausa");
                    String supervisorAutoriza = data.getStringExtra("supervisorAutoriza");

                    // Ax: registro de auditoría - toda acción supervisada queda trazada en el log
                    // (logger, no Log.d) con operario, terminal y quién autorizó.
                    if (desactivarImpresoraObligatoria && !impresoraObligatoriaDeshabilitadaPorSupervisor) {
                        impresoraObligatoriaDeshabilitadaPorSupervisor = true;
                        //logger.warn(
                                Utils.escribirLogEnRaiz("[SUPERVISOR] Obligatoriedad de impresora DESACTIVADA en campo | Autorizado por: "
                                + supervisorAutoriza + " | Operario: " + lector + " | Terminal: " + terminalImei
                                + " | Registro actual: " + VariablesGlobales.registroactual);


                    }

                    if (navegacionRegistrosHabilitada != navegacionPrevia) {
                        //logger.warn(
                                Utils.escribirLogEnRaiz("[SUPERVISOR] Avanzar/Retroceder " + (navegacionRegistrosHabilitada ? "HABILITADO" : "DESHABILITADO")
                                + " | Autorizado por: " + supervisorAutoriza + " | Operario: " + lector);
                    }

                    if (asignanolectura) {
                        //logger.warn(
                                Utils.escribirLogEnRaiz("[SUPERVISOR] Cierre forzado de ruta ejecutado | Causa: " + asignanocausa
                                + " | Autorizado por: " + supervisorAutoriza + " | Operario: " + lector
                                + " | Terminal: " + terminalImei);
                        cerrarRutaConNoLectura(asignanocausa);
                    }

                } catch (Exception ex) {
                    //logger.error(
                            Utils.escribirLogEnRaiz("onActivityResult() OPCIONESSUPERVI_REQUEST_CODE. " + ex.toString());
                }
            }
        } else if (requestCode == CONFIGFORMATIMP_REQUEST_CODE) {//Configuracion Formato Impresion

            if (resultCode == RESULT_OK && data != null) {

                try {
                    cambiarValoresimpresion();
                } catch (Exception ex) {
                    logger.info("CONFIGFORMATIMP_REQUEST_CODE. " + ex.toString());
                }
            }
        } else if (requestCode == ENTREGASGRUPALS_REQUEST_CODE) {//Entrega grupal

            if (resultCode == RESULT_OK && data != null) {

                try {
                    variables.medidoranterior = data.getStringExtra("ultimomed");
                    variables.ultimotiempo = utils.parseStringToDouble(data.getStringExtra("ultimotiempo_eg"));
                } catch (Exception ex) {
                    logger.info(" CONFIGFORMATIMP_REQUEST_CODE. " + ex.toString());
                }
                //envioFacturacionHilos();
            }
        } else if (requestCode == CHATINSETAR_REQUEST_CODE) {//Chat mesajeria

            imagenChat.setImageResource(R.drawable.chat_msg_no); //Ax:no hay por leer o traidos desde el server
            banderaChat = true; //Ax: true = leyo msg
        } else if (requestCode == CHAT_REQUEST_CODE) {//Chat mesajeria2
            if (resultCode == RESULT_OK && data != null) {
                VariablesGlobales.tipodebusqueda = 3;
                abrirArchivosDeFacturacion();
                buscaPorMedidor(data.getIntExtra("id", -1) + "", 3);
                cerrarArchivosFacturacion();
            }
        } else if (requestCode == DIGITOS_REQUEST_CODE) {//Chat mesajeria2
            txtElectura.setFilters(new InputFilter[]{new InputFilter.LengthFilter(VariablesGlobales.intcontroltexto)});
            //aqui guardar en dato que se nos indica
            abrirArchivosDeFacturacion();
            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
            VariablesGlobales.datodebusqueda = "" + VariablesGlobales.intcontroltexto;
            if (!VariablesGlobales.datodebusqueda.trim().equals("")) {
                infoRegistroSalida.settablaRegistroSalida_informe("CAMBIO DIGITOS " + VariablesGlobales.datodebusqueda.trim());

                if (VariablesGlobales.datodebusqueda.trim().length() == 1) {
                    infoRegistroSalida.settablaRegistroSalida_Nroenteros(VariablesGlobales.datodebusqueda.trim());
                    infoRegistroSalida.escribir_TablaRegistroSalida(VariablesGlobales.registroactual);
                }
            }
            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
            visualizarInformacionCliente(0);
            cerrarArchivosFacturacion();

        } else if (requestCode == VariablesGlobales.REQUEST_CONNECT_DEVICE) {

            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {

                VariablesGlobales.habilitadaimpresora = 1;
                this.imagenPrinter.setImageResource(R.drawable.impresora);

                addLog("resultCode==OK");
                // Get the device MAC address
                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                String mensajedes = data.getExtras().getString(DeviceListActivity.MENSAJE_DESASOCIAR);

                if (mensajedes.equals("SI")) {
                    Desasociar();
                } else {
                    BluetoothDevice device = VariablesGlobales.mBluetoothAdapter.getRemoteDevice(address);
                    VariablesGlobales.printerMacAddress = device.getAddress();

                    // Attempt to connect to the device
                    addLog("onActivityResult: connecting device...");
                    connectToDevice(device);
                }
            }
            VariablesGlobales.bDiscoveryStarted = false;
        } else if (requestCode == CUENTA_NUEVA) {//Modulo de ConsultaNoEnviados
            Log.e("errorCN", "sale ok " + RESULT_OK);
            if (resultCode == RESULT_OK && data != null) {

                try {
                    // abrirArchivosDeFacturacion();
                    Log.e("errorCN", "sale ok2 " + RESULT_OK);
                    //envioFacturacionHilos();
                    fotosCuentaNueva = true;

                } catch (Exception ex) {
                    logger.info("onActivityResult() CONSULTNONVIADS_REQUEST_CODE " + ex.toString());
                }
            }

        } else {
        }
    }

    private void abrirInforme(boolean ismenu, String InformeCausa) { //el parametro indica si viene del menu para no ser obligatorio
        Bundle bundle = new Bundle();
        String Resumen = "";
        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
            Resumen = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA() + ";" +
                    infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA() + ";" +
                    infoRegistroSalida.gettablaRegistroSalida_OBSERANT() + ";" +
                    infoRegistroSalida.gettablaRegistroSalida_CODESTADO().trim();
        }
        bundle.putString("DIRECTORIOACTUAL", "" + VariablesGlobales.getDirectorioactual());
        bundle.putInt("NUMEROREGISTROACTUAL", VariablesGlobales.getRegistroactual());
        bundle.putBoolean("ISMENU", ismenu); //Indica si se dio clic desde el menu, para poder salir en caso erroneo
        bundle.putString("RESUMEN", "" + Resumen);
        bundle.putString("INFORMECAUSA", "" + InformeCausa);

        bundle.putInt("registroActual", s_idac);//before VariablesGlobales.registroactual

        Intent intent = new Intent(MenuDeLiquidacion.this, Informe.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, INFORME_REQUEST_CODE);
    }

    /***
     * Ax: Procede a abrir la estructura 'D' y guardar el comentario (campo informe) por id
     */
    private void ingresarInforme(String info, String catg, int id, String Permitelect) {

        try {
            abrirArchivosDeFacturacion();
            info = info.replace("ó", "o");
            info = info.replace("é", "e");
            info = info.replace("í", "i");
            info = info.replace("á", "a");
            info = info.replace("ñ", "n");
            info = info.replace("Ñ", "N");
            info = info.replace("ú", "u");
            info = info.replace("ý", "");

            if (VariablesGlobales.registroactual != id) {
                infoRegistroSalida.lectura_TablaRegistroSalida(id);
            } else {
                infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
            }

            if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals(""))
                variables.numInformes++;

            if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99")) {
                String InformeReg = info.trim();
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
                    if (info.trim().length() > 250) {
                        infoRegistroSalida.settablaRegistroSalida_informe(info.substring(0, 250));
                    } else {
                        infoRegistroSalida.settablaRegistroSalida_informe(info);
                    }
                } else {
                    if (info.trim().length() > 250) {
                        infoRegistroSalida.settablaRegistroSalida_informe(info.substring(0, 250));
                    } else {
                        infoRegistroSalida.settablaRegistroSalida_informe(info);
                    }
                  /*  if (InformeReg.length() > 60) {
                        infoRegistroSalida.settablaRegistroSalida_CODESTADO(InformeReg.substring(0,30));
                        infoRegistroSalida.settablaRegistroSalida_DESCSERV(InformeReg.substring(30, 59));
                    } else {
                        if (InformeReg.length() <= 30)
                            infoRegistroSalida.settablaRegistroSalida_CODESTADO(InformeReg.trim());
                        else
                        {
                            infoRegistroSalida.settablaRegistroSalida_CODESTADO(InformeReg.substring(0,30));
                            infoRegistroSalida.settablaRegistroSalida_DESCSERV(InformeReg.substring(InformeReg.length()-30, InformeReg.length()));
                        }
                    }*/
                }
            } else {
                if (info.length() > 250) {
                    infoRegistroSalida.settablaRegistroSalida_informe(info.substring(0, 249));
                } else {
                    infoRegistroSalida.settablaRegistroSalida_informe(info);
                }
            }
            tomarFechaSistema("", "");
            infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
            infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);
            infoRegistroSalida.settablaRegistroSalida_CODCATEGORIA(catg);
            //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (16)"); //temporal?
            infoRegistroSalida.escribir_TablaRegistroSalida(id);

            if (VariablesGlobales.getTotalClientesReales() > 0) {
                if (id <= VariablesGlobales.getTotalClientesReales()) {
                    //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (17)"); //temporal?
                    infoRegistroSalida.escribir_TablaRegistroSalida(id);
                }
            } else {
                //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (18)"); //temporal?
                infoRegistroSalida.escribir_TablaRegistroSalida(id);
            }

            //seria nuevo ya que se esta perdiendo el informe del registro
            if (VariablesGlobales.registroactual != id) {
                lectModifica = true;
                guardarEnvioGPRSNuevo(1, id);
            }
            //realizar es la impresion
            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V"))
                EscribaArchivoImpresion_Visita("");

            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);
            visualizarInformacionCliente(0);
            cerrarArchivosFacturacion();

        } catch (Exception ex) {
            mensajeT("Problemas Procesando el archivo de salida", msgLargo);
        } finally {
            s_idac = 0;
        }
    }

    // Ax: lanza el modulo de Opciones de Supervisor (desactivar impresora obligatoria,
    // habilitar avanzar/retroceder, cierre forzado de ruta). Requiere autenticación
    // de supervisor dentro de esa actividad antes de habilitar sus controles.
    private void abrirOpcionesSupervisor() {
        Bundle bundle = new Bundle();
        bundle.putString("directorioactual", VariablesGlobales.directorioactual);
        bundle.putBoolean("navegacionHabilitada", navegacionRegistrosHabilitada);

        Intent intent = new Intent(this, com.gstolima.moduloopcionesupervisor.OpcionesDeSupervisor.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPCIONESSUPERVI_REQUEST_CODE);
    }

    private int cerrarRutaConNoLectura(String causaAsignar) {

        int temporal = VariablesGlobales.registroactual;

        VariablesGlobales.registroactual = 0;
        tiempoYDistanciaPromedio();

        abrirArchivosDeFacturacion();
        while (++VariablesGlobales.registroactual <= infoRegistroSalida.getTotal_TablaRegistroSalida()) {


            if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {
                leerInformacionUsuario(1);
                tomarFechaSistema("", "");

                infoRegistroSalida.settablaRegistroSalida_causadenolectura(causaAsignar);//before A
                infoRegistroSalida.settablaRegistroSalida_lecturatomada("        0");
                infoRegistroSalida.settablaRegistroSalida_leido("5");
                infoRegistroSalida.settablaRegistroSalida_LECTOR(String.format("%4s", lector.trim()));
                infoRegistroSalida.settablaRegistroSalida_TERMINAL(terminalImei.trim());

                if (infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim().equals("")) {
                    infoRegistroSalida.settablaRegistroSalida_IMPRESORA(String.format("%1$3s", impresora));
                }
                infoRegistroSalida.settablaRegistroSalida_fechalectura(variables.amd);
                infoRegistroSalida.settablaRegistroSalida_horalectura(variables.hm);

                if (escribirTablasSalida(11) != 0) {
                    mensajeT("FALLO en Proceso de ''cierre de no Lectura''\n registro:" + VariablesGlobales.registroactual, msgLargo);
                    return 0;
                }
                VariablesGlobales.totalcausasnolectura++;
                variables.totalregistrosleidos++;
                guardarEnvioGPRSNuevo(1, 0);
            }
        }
        VariablesGlobales.registroactual = temporal;
        leerInformacionUsuario(1);
        cerrarArchivosFacturacion();
        //nuevo para que envie despues de acabar el proceso
        //envioFacturacionHilos();
        mensajeT("Proceso de ''cierre de no Lectura'' Terminado correctamente", msgMedio);
        return 1;
    }

    private void setScannedInfo(String barcode) {//*-*-*

        try {
            scanEntregas = false;
            if (barcode.length() > 0) {
                String buscador = barcode;
                if (buscador.length() >= 6) {
                    if (buscador.length() >= 45 && buscador.length() <= 56) {
                        if (buscador.length() > 52)//|| Buscador.Length == 56
                        {
                            buscador = buscador.substring(20, 28);//.PadRight(9, ' ');
                            variables.impresora = "LE3";
                        } else {
                            if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01"))
                                buscador = buscador.substring(0, 8);//.PadRight(9, ' ');
                            else {
                                buscador = buscador.trim();//.PadRight(9, ' ');
                                if (buscador.length() > 10)
                                    buscador = buscador.substring(0, 10);

                            }
                            variables.impresora = "LE1";
                        }
                    } else {
                        if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("01"))
                            buscador = buscador.substring(0, 6);//.PadRight(9, ' ');
                        else
                            buscador = buscador.trim();//.PadRight(9, ' ');
                        variables.impresora = "LE2";
                    }
                } else {

                    if (buscador.length() == 0) {
                        mensajeT("Codigo de Barras NO Valido", msgMedio);
                        return;
                    }
                    variables.impresora = " 01";

                }

                VariablesGlobales.tipodebusqueda = 1;
                if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDOCONLECTURA().trim().equals("2")) {
                    activaLectorBarras = false;
                    abrirArchivosDeFacturacion();
                    scanEntregas = true;
                    buscaPorMedidor(buscador, 1);//buscarCuentaMedidor(buscador, 2);
                    activaLectorBarras = true;
                    cerrarArchivosFacturacion();
                } else {
                    if (!VariablesGlobales.getTipoDeRuta().trim().equals("E")) {
                        activaLectorBarras = false;
                        abriryEscribirBarras(buscador);
                        activaLectorBarras = true;
                        // if (SonBarrasGrupales == true)
                        UltimaBarraLeida = buscador;
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("setScannedInfo(), error Leyendo codigo de barras. " + ex.toString());
            activaLectorBarras = false;
            mensajeT("Codigo de Barras no Valido por excepcion", msgMedio);
            activaLectorBarras = true;
        }
    }

    private int buscaPorMedidor(String medidor, int tipo) {

        int registro_actual = 0;
        int ultimo_registro;
        ultimo_registro = VariablesGlobales.registroactual;
        Log.e("INFO", "buscaPorMedidor| medidor: " + medidor + " | tipo: " + tipo + " | tipobus: " + VariablesGlobales.tipodebusqueda);

        if (VariablesGlobales.tipodebusqueda == 0)
            registro_actual = infoRegistroSalida.buscarSecuencial_TablaRegistroSalida(medidor, 0);
        else if (VariablesGlobales.tipodebusqueda == 1) {
            //se debe probramar para que la busqueda por codigo de cuenta se haga en forma de indice que es el otro modelo de archivo

            if (medidor.trim().length() < 6)
                medidor = String.format("%1$6s", medidor.trim()).replace(" ", "0");// medidor.trim().PadLeft(6, '0');

            if (medidor.trim().length() < 9)
                medidor = String.format("%1$9s", medidor.trim());//medidor.trim().PadRight(9, ' ');

            if (!BuscarApuntadorCliente(medidor)) {

                File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "M" + NombreArchivos);

                if (!file.exists()) {
                    registro_actual = infoRegistroSalida.buscarSecuencial_TablaRegistroSalida(medidor, 1);//1
                }
            } else {
                registro_actual = parseStringToInteger(miApuntador.getapuntadorCliente_APUNTADOR());
                infoRegistroSalida.setEncontro_TablaRegistroSalida(parseStringToInteger(miApuntador.getapuntadorCliente_APUNTADOR()));
            }

        } else if (VariablesGlobales.tipodebusqueda == 3) { //Ax. creado para celsia, el tipo 1 anterior no se entiende
            registro_actual = infoRegistroSalida.buscarSecuencial_TablaRegistroSalida_3(medidor);
        } else
            registro_actual = infoRegistroSalida.buscarSecuencial_TablaRegistroSalida(medidor, 2);//2

        if (infoRegistroSalida.getEncontro_TablaRegistroSalida() > 0) {

            VariablesGlobales.registroactual = registro_actual;
            infoRegistroSalida.lectura_TablaRegistroSalida(VariablesGlobales.registroactual);

            if (tipo == 1) {

                variables.impresora = " 01";
                infoRegistroSalida.settablaRegistroSalida_IMPRESORA(" 01");


                if (VariablesGlobales.getTotalClientesReales() > 0) {
                    if (registro_actual <= VariablesGlobales.getTotalClientesReales()) {
                        //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (19)"); //temporal?
                        infoRegistroSalida.escribir_TablaRegistroSalida(registro_actual);
                    }
                } else {
                    //logger.info("RegActual:" + VariablesGlobales.registroactual + " Cuenta:" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " Total:" + infoRegistroSalida.getTotal_TablaRegistroSalida() + " (20)"); //temporal?
                    infoRegistroSalida.escribir_TablaRegistroSalida(registro_actual);
                }

            } else {

                variables.impresora = " 01";
            }

            visualizarInformacionCliente(1);
            if (tipo == 1) {
                if (validarEstadoRegistro(VariablesGlobales.registroactual, "L") == 0) {

                    procesarLectura(0, 0, "", "99");
                } else {
                    mensajeT("Error De ENTREGA\n" + "Cliente ya ENTREGADO\n" + "PROSIGA CON LA PROXIMA CUENTA", msgMedio);
                }
            }
        } else {
            //before scanEntregas = false;
            mensajeT("# NO EXISTE " + medidor, msgLargo);
            //Ax: lo que habia aca esta en la actividad ModuloEntregasGrupal
            VariablesGlobales.registroactual = ultimo_registro;
            visualizarInformacionCliente(1);
        }
        return (0);
    }

    private boolean BuscarApuntadorCliente(String apuntador) {

        if (apuntador.length() >= 9) {

            if (apuntador.length() > 9)
                apuntador = apuntador.substring(0, 9);

            miApuntador.archivo_ApuntadorCliente = (VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "M" + NombreArchivos);

            File file = new File(miApuntador.archivo_ApuntadorCliente);

            if (file.exists()) {
                if (miApuntador.abrir_ApuntadorCliente(miApuntador.archivo_ApuntadorCliente)) {

                    miApuntador.buscarbinarioChar_ApuntadorCliente_II(apuntador);

                    if (miApuntador.encontro_ApuntadorCliente > 0) {
                        infoRegistroSalida.setEncontro_TablaRegistroSalida(parseStringToInteger(miApuntador.getapuntadorCliente_APUNTADOR()));

                        miApuntador.Cerrar_ApuntadorCliente();
                        return true;
                    }
                    miApuntador.Cerrar_ApuntadorCliente();
                }
            }
        }
        return false;
    }

    public void muestraPosicionActual(Location loc) {

        if (loc == null) {
            txtLongitud.setText("0.0");
            txtLatitud.setText("0.0");
            velocidadGPS = "0";
        } else {// Si se encuentra, se mostrará la latitud y longitud
            txtLongitud.setText(String.valueOf(loc.getLatitude()));
            txtLatitud.setText(String.valueOf(loc.getLongitude()));
            velocidadGPS = String.valueOf(loc.getSpeed());
            numeroDeSatelitesGPS = "" + loc.getExtras().getInt("satellites");//Ax: sin probar!
            altitudReportadaGPS = String.valueOf(loc.getAltitude());

            Date date = new Date(loc.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            //Ax. ojo los meses se cuentan desde 0
            fechayHoraReportadaGPS = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(loc.getTime());
            /*antes calendar.get(Calendar.DAY_OF_MONTH) + "/" + (1 + (int) calendar.get(Calendar.MONTH)) + "/"
                    + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);*/

            VariablesGlobales.fechahoraGPS = date;

            leyoCoordenadas = 1;
        }
    }

//    public void addLog(String s) {
//        if (TAG.length() > 22)
//            Log.d(TAG.substring(0, 22), s);
//    }

    void updateConnectButton(boolean bConnected) {
        if (bConnected) {
            VariablesGlobales.habilitadaimpresora = 1;
            imagenPrinter.setImageResource(R.drawable.impresora);
        } else {
            VariablesGlobales.habilitadaimpresora = 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onStart();
        if (VariablesGlobales.mBluetoothAdapter != null) {
            // If BT is not on, request that it be enabled. setupChat() will then be called during onActivityResult
            if (!VariablesGlobales.mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, VariablesGlobales.REQUEST_ENABLE_BT);
                // Otherwise, setup the comm session
            } else {
                if (VariablesGlobales.btPrintService == null)
                    setupComm();
                // setupChat();
            }
        }
    }

    private void setupComm() {

        // Initialize the array adapter for the conversation thread
        VariablesGlobales.mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_txt_remote_device);//R.id.txtRemoteDevice Ax: todo: revisar esto , antes apuntaba a txt...se le creo este louttx...indefinida su funcion
        Log.d(TAG, "setupComm()");
        VariablesGlobales.btPrintService = new btPrintFile(this, mHandler);
        if (VariablesGlobales.btPrintService == null)
            Log.e(TAG, "VariablesGlobales.btPrintService init() failed");
    }

    // The Handler that gets information back from the VariablesGlobales.btPrintService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgTypes.MESSAGE_STATE_CHANGE:
                    Bundle bundle = msg.getData();
                    int status = bundle.getInt("state");

                    switch (msg.arg1) {
                        case btPrintFile.STATE_CONNECTED:
                            //addLog("connected to: " + VariablesGlobales.mConnectedDeviceName);
                            //if( VariablesGlobales.mConversationArrayAdapter==null)break;//Ax: aqui se revienta TODO: revisar
                            VariablesGlobales.mConversationArrayAdapter.clear();
                            Log.i(TAG, "handleMessage: STATE_CONNECTED: " + VariablesGlobales.mConnectedDeviceName);
                            break;
                        case btPrintFile.STATE_CONNECTING:
                            addLog("connecting...");
                            Log.i(TAG, "handleMessage: STATE_CONNECTING: " + VariablesGlobales.mConnectedDeviceName);
                            break;
                        case btPrintFile.STATE_LISTEN:
                            addLog("connection ready");
                            Log.i(TAG, "handleMessage: STATE_LISTEN");
                            break;
                        case btPrintFile.STATE_IDLE:
                            addLog("STATE_NONE");
                            Log.i(TAG, "handleMessage: STATE_NONE: not connected");
                            break;
                        case btPrintFile.STATE_DISCONNECTED:
                            addLog("disconnected");
                            Log.i(TAG, "handleMessage: STATE_DISCONNECTED");
                            break;
                    }
                    break;
                case msgTypes.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string
                    // from the buffer
                    String writeMessage = new String(writeBuf);
                    VariablesGlobales.mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case msgTypes.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string
                    // from the valid bytes
                    // in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    VariablesGlobales.mConversationArrayAdapter.add(VariablesGlobales.mConnectedDeviceName + ":  "
                            + readMessage);
                    addLog("recv>>>" + readMessage);
                    break;
                case msgTypes.MESSAGE_DEVICE_NAME:
                    // save the connected
                    // device's name
                    VariablesGlobales.mConnectedDeviceName = msg.getData().getString(msgTypes.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + VariablesGlobales.mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case msgTypes.MESSAGE_TOAST:

                    Toast.makeText(getApplicationContext(), msg.getData().getString(msgTypes.TOAST), Toast.LENGTH_LONG)
                            .show();
                    // myToast(msg.getData().getString(msgTypes.TOAST));
                    Log.i(TAG, "handleMessage: TOAST: " + msg.getData().getString(msgTypes.TOAST));
                    addLog(msg.getData().getString(msgTypes.TOAST));
                    break;
                case msgTypes.MESSAGE_INFO:
                    addLog(msg.getData().getString(msgTypes.INFO));
                    // mLog.append(msg.getData().getString(msgTypes.INFO));
                    // mLog.refreshDrawableState();
                    String s = msg.getData().getString(msgTypes.INFO);
                    if (s.length() == 0)
                        s = String.format("int: %i" + msg.getData().getInt(msgTypes.INFO));
                    Log.i(TAG, "handleMessage: INFO: " + s);
                    break;
            }
        }
    };

    public void addLog(String s) {
        if (TAG.length() > 22)
            Log.d(TAG.substring(0, 22), s);
    }

    private void Desasociar() {

        try { //Ax: esta parte trata de cerrar cualquier conexion, pero puede que no exista tal conexion
            VariablesGlobales.btPrintService.stop();
        } catch (Exception ex) {
        }
        utils.WriteLine(logPrint, "");
    }

    void connectToDevice(BluetoothDevice _device) {
        if (_device != null) {
            addLog("connecting to " + _device.getAddress());
            VariablesGlobales.btPrintService.connect(_device);

            utils.WriteLine(logPrint, _device.getAddress()); //escribe en log la mac
        } else {
            addLog("unknown remote device!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //Ax: recibe el resultado ejecutado de la clase async task del metodo onPostExecute(result).
    public void processFinish(String output) {
    }

    public void ProbarWS() {
        try {
            if (!estaEnvioAlWsOcupado) {
                estaEnvioAlWsOcupadoTrue();
                TaskHelper.execute(new AsyncCallWS(), "VALIDAR_CONEXION");
            } else {
                mensajeT("Existe un proceso de envio de datos al servidor,\n\n ...Inténtelo en uno segundos", msgLargo);
            }
        } catch (Exception ex) {
            mensajeT(ex.getMessage(), msgLargo);
            estaEnvioAlWsOcupado = false;
        }
    }

    public void tryAll() {

        if (tryAlL) return;

        try {
            if (estaEnvioAlWsOcupado) { //Ax set true : nah
                return;
            }
            TaskHelper.execute(new AsyncCallWS(), "tryAll");

        } catch (Exception ex) {
            estaEnvioAlWsOcupado = false;
            logger.info("tryAll" + ex.getMessage());
        }
    }


    private class AsyncCallWS extends AsyncTask<String, Integer, Void> {

        public String asyncResponse = "";
        public String asynCallFrom = "";

        @Override
        protected Void doInBackground(String... params) {

            asynCallFrom = params[0];

            try {
                switch (asynCallFrom) {

                    case "comprobarBorradoBackup":
                        comprobarBorradoBackup();
                        break;

                    // ELIMINADO: Ya no se usa SOAP para envío de lecturas
                    // case "ENVIARFACTURACIONCOLECCION":
                    //     EnviarAlServidorFacturacion();
                    //     break;

                    case "VALIDAR_CONEXION":
                        WSSoap wsoapp = new WSSoap(URL, paginaWs);
                        asyncResponse = wsoapp.verificarWs(asynCallFrom);
                        break;

                    // ELIMINADO: Ya no se usa SOAP para envío de fotos
                    // case "EnviarFotosServer":
                    //     asyncResponse = enviarFotografiasAsinc(true);
                    //     break;

                    case "ProcesaFotosAsync":
                        asyncResponse = ejecutarProcesoDeFoto3();
                        break;

                    // ELIMINADO: Ya no se usa envío en bucle
                    // case "EnviarFotografiasBucle":
                    //     asyncResponse = EnviarFotografiasBucle();
                    //     break;

                    case "enviarArchivos4_asinc":
                        // MANTENER: Este es para cierre de rutas (ZIP con archivos D, S, etc.)
                        asyncResponse = enviarArchivos4_asinc();
                        break;

                    case "ValidarNoEnviados":
                        asyncResponse = ValidarNoEnviados();
                        break;
                }
            } catch (Exception e) {
                asyncResponse = "Error, valide conexion con Ws";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            String msg = "";

            switch (asynCallFrom) {

                /*case "ValidarNoEnviados":
                    if (dialogloading.isShowing()) {
                        dialogloading.dismiss();
                    }
                    ValidandoNoEnv = 0;
                    Log.e("INFO", "ValidarNoEnviados| respuesta_:" + asyncResponse);

                    if (asyncResponse.contains("VALIDACION") || asyncResponse.contains("REALM")) {
                        leerInformacionUsuario(1);
                        visualizarInformacionCliente(0);
                        cerrarArchivosFacturacion();
                        mensajeOk(asyncResponse, "");
                    } else {
                        mensajeOk(asyncResponse, "");
                    }
                    break;*/
                case "ValidarNoEnviados":
                    if (dialogloading.isShowing()) {
                        dialogloading.dismiss();
                    }
                    ValidandoNoEnv = 0;
                    Log.e("INFO", "ValidarNoEnviados| respuesta_:" + asyncResponse);

                    // asyncResponse siempre tiene contenido útil, mostrar directamente
                    mensajeOk(asyncResponse, "");
                    leerInformacionUsuario(1);
                    visualizarInformacionCliente(0);
                    cerrarArchivosFacturacion();

                    break;

                case "VALIDAR_CONEXION":
                    Log.e("INFO", "[AsyncCallWS] VALIDA CONEXION -> " + asyncResponse);
                    estaEnvioAlWsOcupado = false;
                    try {
                        Integer.parseInt(asyncResponse);
                        imagenRed.setImageResource(R.drawable.redactiva);
                        MostrarAlertDialog("Alerta Seleccion Terminacion", "Conexion OK\n ¿DESEA ENVIAR AL SERVIDOR?", "validarconexion");

                    } catch (NumberFormatException e) {
                        imagenRed.setImageResource(R.drawable.redinativa);
                        mensajeT("NO HAY CONEXION\n CON EL SERVIDOR...", msgMedio);
                        cerrarArchivosFacturacion();
                    }
                    break;

                case "ProcesaFotosAsync":
                    // MODIFICADO: Ya no llama a EnviarFotografias()
                    if (!asyncResponse.contains("✓") && asyncResponse.contains("|")) {
                        String[] mss = asyncResponse.split("\\|");
                        VariablesGlobales.tipodebusqueda = 6;
                        VariablesGlobales.datodebusqueda = capRegistroActual + "";

                        if (!mss[1].trim().equals("")) {
                            mensajeOk("DEBE TOMAR FOTO DE NUEVO\n\n" + "Verifique Datos de la Cuenta: " + mss[1] + "\n Registro: " + capRegistroActual, "retomaFoto");
                            infoRegistroSalida.buscarSecuencial_TablaRegistroSalida_cuenta(mss[1]);
                            buscarCuentaMedidor("", 0);
                        } else {
                            if (capRegistroActual > 0) buscarCuentaMedidor("", 0);
                        }
                    }
                    // YA NO SE LLAMA A EnviarFotografias() - El servicio envía automáticamente
                    break;

                case "enviarArchivos4_asinc":
                    // MANTENER: Este es para cierre de rutas
                    if (ejecutaMenuEnvio == 1) {
                        ejecutaMenuEnvio = 0;
                        if (dialogloading.isShowing()) {
                            dialogloading.dismiss();
                        }
                    }
                    estaEnvioAlWsOcupado = false;

                    if (asyncResponse != null && !asyncResponse.isEmpty()) {
                        if (asyncResponse.contains("✓")) {
                            File desc = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/O" + NombreArchivos + ".SDA");
                            utils.EscribirLinea(desc, "O" + NombreArchivos);
                            utils.MensajeTime(asyncResponse, "ENVÍO DE ARCHIVOS OK", MenuDeLiquidacion.this, 8);
                        } else {
                            utils.MensajeTime(asyncResponse, "ENVÍO DE ARCHIVOS ERROR!", MenuDeLiquidacion.this, 8);
                        }
                    }
                    break;

                // ELIMINADOS los siguientes casos que ya no se usan:
                // case "EnviarFotosServer":
                // case "EnviarFotografiasBucle":
                // case "ENVIARFACTURACIONCOLECCION":
            }

            if (!msg.trim().equals("")) {
                mensajeT(msg, msgMedio);
            }
            asyncResponse = "";
        }

        @Override
        protected void onPreExecute() {
            // Vacío - ya no se muestra diálogo de carga para envíos
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    private class UsarGPS implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            muestraPosicionActual(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public boolean getParamsWs() {
        try {
            BDComunicaciones bdc = crudComuni.getParams();
            if (bdc != null) {
                if (bdc.gethttpSeguro() == 0) {
                    URL = "http://" + bdc.getURL();
                } else {
                    URL = "https://" + bdc.getURL();
                }
                paginaWs = bdc.getPaginaWs();
                rutaAdministrador = bdc.getRutaAdministrador() + "/";
                cadenaURLapi = bdc.getUrlApi();
                esComprimido = "1";
                //cadenaURLapi = URL.substring(0, URL.lastIndexOf(":") + 1) + puertoAPI + "/api/";

                Log.e("INFO", "RutaAdministrador" + rutaAdministrador);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error("[MenuDeLiquidacion]getParamsWs()|" + ex.getMessage());
            return false;
        }
    }


    private boolean MensajeriaCliente() {//Ax: MENSAJERIA ESPECIAL
        String Mensaje = infoRegistroSalida.gettablaRegistroSalida_CODESTADO().trim() + " " +
                infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(1, 31).trim() + " "
                + infoRegistroSalida.gettablaRegistroSalida_DESCSERV().trim();

        if (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0A")) {
         /* if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S"))
            {
                Mensaje+=" CUENTA SUSPENDIDA ,,,!!ALERTA DE CONTROL";
            }
            else {*/
            if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P")) {
                Mensaje += " MEDIDOR PROVISIONAL ,,,!!ALERTA DE CONTROL";
            } else {
                if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")) {
                    Mensaje += " CLIENTE EN JURIDICA ,,,!!ALERTA DE CONTROL";

                } else {
                    if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0X")) {
                        Mensaje += " CLIENTE CON CONTROL DE SELLOS ,,,!!ALERTA DE VERIFICACION ESPECIAL";

                    } else {
                        if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0D")) {
                            Mensaje += " CLIENTE CON CONTROL DE verifica en que Estado esta el Predio y reportarlo en el informe,,,/n!!ALERTA DE VERIFICACION ESPECIAL";

                        } else {
                            Mensaje += " CLIENTE CON ANOMALIA ESPECIAL LA TABLA NO SE HA INCORPORADO ,,,!!ALERTA DE VERIFICACION ESPECIAL";
                        }
                    }

                }
            }

            //}

        }
        if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {
            Mensaje = "Cuenta enviada con Lectura a Verificar \nLectura: [" +
                    infoRegistroSalida.gettablaRegistroSalida_CODESTADO().trim() + "]\n" +
                    " O anomalia de lectura aplicada \n" +
                    "Anomalia [" + infoRegistroSalida.gettablaRegistroSalida_OBSERANT() + "]";
        }

        new AlertDialog.Builder(this)
                .setTitle("Mensaje programado")
                .setMessage(Mensaje.toUpperCase())
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();

        return true;
    }

    private boolean cargarArchivoNombre() {//Ax: Lee y carga del archivo "Nombre" si existe

        String linea = "";
        String archivo_nombre = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/NOMBRE";
        File file = new File(archivo_nombre);

        if (!file.exists()) return false;

        try {
            FileReader r = new FileReader(archivo_nombre);
            BufferedReader reader = new BufferedReader(r);

            while ((linea = reader.readLine()) != null) {
                if (linea.trim().equals("")) {
                    continue;
                } else break;
            }

            NombreArchivos = linea.substring(10, 20).trim();//cambia de 9 a 10 y desde la 5 a la 10

            try {
                CicloReal = linea.substring(0, 9).trim();
                Ciclo = linea.substring(10, 13).trim();
                int x = parseStringToInteger(Ciclo);//Ax: Si tiene caracteres raros fallara
                //202512071L071132.006   .
                Municipio = linea.substring(10, 15);
                Seccion = linea.substring(15, 16);
                Division = linea.substring(17, 20);

                return true;

            } catch (Exception e) {
                return false;
            }
        } catch (Exception ex) {
            return false;// "Problema con la carga de:\n Archivo: Ciclo, Municipio...etc."
        }
    }

    private boolean CargarArchivoCarga() {

        String nombreArchivo = VariablesGlobales.directorioactual + "/ARCHIVOSLCARGA.CFI";
        int contador = 0;
        try {
            FileReader r = new FileReader(nombreArchivo);
            BufferedReader reader = new BufferedReader(r);
            String linea;

            while ((linea = reader.readLine()) != null) {

                if (contador < 1) { // tomar la ruta del sistema administrativo
                    rutaAdministrador = linea.trim();

                    if (!rutaAdministrador.contains(":")) {
                        mensajeT("No hay Archivo de Soporte...\n //ARCHIVOSLCARGA.CFI", msgLargo);
                        return false;
                    }
                }

                if (contador == 1) { // Ax: sin control de error
                    esComprimido = linea.trim();

                    if (esComprimido.trim().equals("ZIP SI")) {

                        esComprimido = "1";
                    } else {

                        esComprimido = "0";
                    }
                }
                contador++;
            }
            r.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

//
//    public String getSerialNumber() { // Captura numero IMEI del telefono
//
//        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceIMEI = tManager.getDeviceId();
//        return deviceIMEI;
//    }

    private void mensajeT(String msg, int dur) { //Ax: 1 segundo: 1000

        toast = Toast.makeText(MenuDeLiquidacion.this, msg, dur);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.setDuration(dur);
        toast.show();
    }

    public void chatAsyncall() {

        try {
            metodo = "CHATRECIBE";
            taskChat = new AsyncCallChat();
            taskChat.execute("");
        } catch (Exception ex) {
            mensajeT("Ha ocurrido un error al Recibir mensajes", msgMedio);
            logger.info("chatAsyncall()" + ex.getMessage());
        }
    }

    public String chatRecibirMensajes(String operario) {

        String respuestaWeb = "";
        WSSoap wsoaps = new WSSoap(URL, paginaWs);

        try {
            respuestaWeb = wsoaps.chatRecibirMensajes("RETORNAR_MENSAJES", operario);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.zxing_beep);

            if (ProcesarMensajes(respuestaWeb)) {
                mp.start();
                respuestaWeb = "CHATOK";
            } else { //Ax: no hay mensajes se procede a leer archivo
                File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/MENSAJES.SDA");

                if (file.exists()) {
                    FileReader stream3 = new FileReader(file);
                    BufferedReader br = new BufferedReader(stream3);
                    String linea;

                    while ((linea = br.readLine()) != null) {

                        if (linea.length() != 0 && linea.contains("|")) {
                            String[] x = linea.split("\\|");
                            if (x[3].trim().equals("0")) {
                                respuestaWeb = "CHATOK";
                                mp.start();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("chatRecibirMensajes()" + ex.getMessage());
        }
        return respuestaWeb;
    }

    public int contarCuentasNuevas = 0;

    public boolean ProcesarMensajes(String msg) {
//modificado por victor para recibir un mensaje y crear cuentas nuevas en el proceso de lecturas
        String temp;
        String MensajeRecibido = "";
        contarCuentasNuevas = 0;
        if (msg.contains("88888888888") || msg.contains("99999999999"))
            return false;

        msg = msg.toUpperCase();

        if (msg.contains("MENSAJERIA=")) {

            boolean seguir = true;

            while (seguir) {
                if (msg.contains("ASUNTO=")) {
                    if (msg.contains("CUENTA NUEVA")) {
                        //vamos a incluir algo nuevo por si en la cadena llega
// Extraer ASUNTO
                        msg = msg.substring(msg.indexOf("ASUNTO=") + 7);
                        String asunto = msg.substring(0, msg.indexOf(";")).trim();
// Extraer MENSAJE (usando separador ;FECHAMENSAJE)
                        msg = msg.substring(msg.indexOf("MENSAJE=") + 8);
                        String mensaje = msg.substring(0, msg.indexOf("FECHAMENSAJE=")).trim();
                        if (CrearRegistroCuentaNueva(mensaje)) ;
                        {
                            contarCuentasNuevas++;


                        }


                    } else {
                        msg = msg.substring(msg.indexOf("ASUNTO=") + 7);
                        temp = (msg.substring(0, msg.indexOf(";"))).trim();
                        msg = msg.substring(msg.indexOf("MENSAJE=") + 8);
                        MensajeRecibido = (msg.substring(0, msg.indexOf(";"))).trim();
                        temp += "|" + (msg.substring(0, msg.indexOf(";"))).trim();
                        msg = msg.substring(msg.indexOf("FECHAMENSAJE=") + 13);
                        temp += "|" + (msg.substring(0, msg.indexOf(";"))).trim() + "|0\r\n"; //Esta última posición 0 indica que no ha sido leido
                        utils.EscribirLinea(new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/MENSAJES.SDA"), temp);
                    }
                } else {


                    seguir = false;
                }
            }
/*
            if( contarCuentasNuevas>0)
            {
             //   mensajeT("APRECIADO OPERADOR SE CREA UNA CANDIDAD\n DE REGISTROS NUEVOS A SU RUTA..."+contarCuentasNuevas, msgLargo);
                //visualizarInformacionCliente(0);
                imagenChat.setImageResource(R.drawable.chat_msg_si);
                imagenChat.refreshDrawableState();//.setImageResource(R.drawable.chat_msg_si);
                //mostrarDialogoAlerta("ALERTA", "SEAPRECIADO OPERADOR DE CREARON UNA CANDIDAD\n DE REGISTROS NUEVOS A SU RUTA..."+contarCuentasNuevas);
            }*/
            return true;
        }
        return false;
    }

    public boolean CrearRegistroCuentaNueva(String msg) {
//modificado por victor para recibir un mensaje y crear cuentas nuevas en el proceso de lecturas
        //String temp;
        //String MensajeRecibido="";

        String[] campos = msg.split(";", -1); // -1 para que incluya vacíos
        if (campos.length < 55) {
            Log.e("Parser", "Error: el registro no tiene suficientes campos." + msg);
            return false;
        } else {
            try {


                TablaRegistroSalida infoRegistroSalida2 = new TablaRegistroSalida();
                infoRegistroSalida2.setArchivo_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());
                //nuevo para crear el registro nuevo
                if (infoRegistroSalida2.abrir_TablaRegistroSalida(infoRegistroSalida2.getArchivo_TablaRegistroSalida())) {


                    infoRegistroSalida2.settablaRegistroSalida_SUSPENDIDO(ajustarCampo(campos[0], 2));
                    infoRegistroSalida2.settablaRegistroSalida_SINLECTURA(ajustarCampo(campos[1], 4));
                    infoRegistroSalida2.settablaRegistroSalida_CICLO(ajustarCampo(campos[2], 3));
                    infoRegistroSalida2.settablaRegistroSalida_DESCDEPTO(ajustarCampo(campos[3], 32));
                    //***
                    infoRegistroSalida2.settablaRegistroSalida_CODMUNICIPIO(ajustarCampo(campos[4], 3));
                    infoRegistroSalida2.settablaRegistroSalida_CODESTADO(ajustarCampo(campos[5], 32));
                    //**
                    infoRegistroSalida2.settablaRegistroSalida_CODSECTOR(ajustarCampo(campos[6], 3));
                    infoRegistroSalida2.settablaRegistroSalida_DESCSECTOR(ajustarCampo(campos[7], 32));
                    infoRegistroSalida2.settablaRegistroSalida_ruta(ajustarCampo(campos[8], 13));

                    infoRegistroSalida2.settablaRegistroSalida_CUENTA(ajustarCampo(campos[9], 10));
                    infoRegistroSalida2.settablaRegistroSalida_Clasedeservicio(ajustarCampo(campos[10], 2));
                    infoRegistroSalida2.settablaRegistroSalida_DESCSERV(ajustarCampo(campos[11], 32));
                    infoRegistroSalida2.settablaRegistroSalida_UBICACION(ajustarCampo(campos[12], 1));

                    infoRegistroSalida2.settablaRegistroSalida_Nombre(ajustarCampo(campos[13], 48));
                    infoRegistroSalida2.settablaRegistroSalida_Direccion(ajustarCampo(campos[14], 64));

                    infoRegistroSalida2.settablaRegistroSalida_NROMEDIDORES(ajustarCampo(campos[15], 3));
                    infoRegistroSalida2.settablaRegistroSalida_nrocontador(ajustarCampo(campos[16], 20));
                    infoRegistroSalida2.settablaRegistroSalida_MARCA(ajustarCampo(campos[17], 3));
                    infoRegistroSalida2.settablaRegistroSalida_Tipomedida(ajustarCampo(campos[18], 2));
                    infoRegistroSalida2.settablaRegistroSalida_Factormultipicacion(ajustarCampo(campos[19], 18));
                    infoRegistroSalida2.settablaRegistroSalida_Nroenteros(ajustarCampo(campos[20], 1));
                    infoRegistroSalida2.settablaRegistroSalida_Consumopromediocliente(ajustarCampo(campos[21], 16));
                    infoRegistroSalida2.settablaRegistroSalida_Lecturaanterior(ajustarCampo(campos[22], 16));
                    infoRegistroSalida2.settablaRegistroSalida_lecturatomada(ajustarCampo(campos[23], 11));
                    infoRegistroSalida2.settablaRegistroSalida_lecturamodificada1(ajustarCampo(campos[24], 11));
                    infoRegistroSalida2.settablaRegistroSalida_lecturamodificada2(ajustarCampo(campos[25], 11));
                    infoRegistroSalida2.settablaRegistroSalida_causadenolectura(ajustarCampo(campos[26], 2));
                    infoRegistroSalida2.settablaRegistroSalida_comentario1(ajustarCampo(campos[27], 2));
                    infoRegistroSalida2.settablaRegistroSalida_fechalectura(ajustarCampo(campos[28], 8));
                    infoRegistroSalida2.settablaRegistroSalida_horalectura(ajustarCampo(campos[29], 6));
                    infoRegistroSalida2.settablaRegistroSalida_intentos(ajustarCampo(campos[30], 1));
                    infoRegistroSalida2.settablaRegistroSalida_modificaciones(ajustarCampo(campos[31], 1));

                    infoRegistroSalida2.settablaRegistroSalida_ULTIMOMEDIDORLEIDO(ajustarCampo(campos[32], 16));

                    infoRegistroSalida2.settablaRegistroSalida_TIEMPO(ajustarCampo(campos[33], 5));
                    infoRegistroSalida2.settablaRegistroSalida_NROIMPRESIONES(ajustarCampo(campos[34], 1));
                    infoRegistroSalida2.settablaRegistroSalida_IMPRESORA(ajustarCampo(campos[35], 3));
                    infoRegistroSalida2.settablaRegistroSalida_TERMINAL(ajustarCampo(campos[36], 15));
                    infoRegistroSalida2.settablaRegistroSalida_LECTOR(ajustarCampo(campos[37], 4));

                    infoRegistroSalida2.settablaRegistroSalida_informe(ajustarCampo(campos[38], 250));
                    infoRegistroSalida2.settablaRegistroSalida_leido(ajustarCampo(campos[39], 1));
                    infoRegistroSalida2.settablaRegistroSalida_OBSERANT(ajustarCampo(campos[40], 2));
                    infoRegistroSalida2.settablaRegistroSalida_CONSERVICIODIRECTO(ajustarCampo(campos[41], 1));
                    infoRegistroSalida2.settablaRegistroSalida_INDOBLIGAFOTO(ajustarCampo(campos[42], 1));
                    infoRegistroSalida2.settablaRegistroSalida_cordenadax(ajustarCampo(campos[43], 16));
                    infoRegistroSalida2.settablaRegistroSalida_cordenaday(ajustarCampo(campos[44], 16));
                    infoRegistroSalida2.settablaRegistroSalida_distanciacalculada(ajustarCampo(campos[45], 10));
                    infoRegistroSalida2.settablaRegistroSalida_CODCATEGORIA(ajustarCampo(campos[46], 2));
                    infoRegistroSalida2.settablaRegistroSalida_SUSPENDIDOCONLECTURA(ajustarCampo(campos[47], 1));
                    infoRegistroSalida2.settablaRegistroSalida_CODNOTIFICACION(ajustarCampo(campos[48], 2));
                    infoRegistroSalida2.settablaRegistroSalida_CODIGOGRUPOENTREGA(ajustarCampo(campos[49], 9));
                    infoRegistroSalida2.settablaRegistroSalida_INDICADORCRITICASAC(ajustarCampo(campos[50], 1));
                    infoRegistroSalida2.settablaRegistroSalida_CONSUMO_LIM_INFERIOR(ajustarCampo(campos[51], 12));
                    infoRegistroSalida2.settablaRegistroSalida_CONSUMO_LIM_SUPERIOR(ajustarCampo(campos[52], 12));
                    infoRegistroSalida2.settablaRegistroSalida_CODIGO_SAC(ajustarCampo(campos[53], 3));
                    infoRegistroSalida2.setId(ajustarCampo(campos[54], 11));
                    infoRegistroSalida2.settablaRegistroSalida_fin("\r\n");
                    //nuevo proceso para iniciar variables

                    int totalregistros = infoRegistroSalida.getTotal_TablaRegistroSalida() + 1;
                    infoRegistroSalida2.escribir_TablaRegistroSalida2(totalregistros);
                    //totalregistros = infoRegistroSalida2.getTotal_TablaRegistroSalida() + 1;

                    infoRegistroSalida2.setTotal_TablaRegistroSalida(totalregistros);
                    infoRegistroSalida.setTotal_TablaRegistroSalida(totalregistros);

                    infoRegistroSalida2.Cerrar_TablaRegistroSalida();
                } else {

                    mensajeT("No se pudo abrir registros Salida para crar la cuenta nueva", 7000);
                    return false;
                }
            } catch (Exception ex) {
                mensajeT("Ha ocurrido un error del mensaje recibido de crar cuenta", 7000);
                return false;
            }
        }


        return true;

    }

    private void mostrarDialogoAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);

        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Dialogo Acerca De", "Boton Ok pulsado");
            }
        });
        builder.show();
    }

    private String ajustarCampo(String valor, int longitud) {
        if (valor == null) valor = "";
        if (valor.length() > longitud) {
            return valor.substring(0, longitud); // recorta
        }
        return String.format("%-" + longitud + "s", valor);
    }

    private class AsyncCallChat extends AsyncTask<String, Integer, Void> {

        public String respuesta_ = "";

        @Override
        protected Void doInBackground(String... params) {

            try {
                switch (metodo) {

                    case "CHATRECIBE":
                        if (isCancelled()) break;
                        UtilsNet utilnet = new UtilsNet();
                        boolean hayI = utilnet.hayInternet(getApplicationContext());
                        if (!hayI) {
                            respuesta_ = "SINCONEXION";

                        } else {
                            respuesta_ = chatRecibirMensajes(lector);
                        }
                        break;
                }

            } catch (Exception e) {
                respuesta_ = "Error, valide conexion con Ws";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String msg = "";
            boolean bul = true;

            if (respuesta_.contains("CHATOK")) {
                imagenChat.setImageResource(R.drawable.chat_msg_si);
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
                    imagenChat.setImageResource(R.drawable.verificar);
                }
            }
            if (contarCuentasNuevas > 0) {
                //   mensajeT("APRECIADO OPERADOR SE CREA UNA CANDIDAD\n DE REGISTROS NUEVOS A SU RUTA..."+contarCuentasNuevas, msgLargo);

                imagenChat.setImageResource(R.drawable.chat_msg_si);

                //mensajeT("APRECIADO OPERADOR DE CREARON UNA CANDIDAD\n DE REGISTROS NUEVOS A SU RUTA..."+contarCuentasNuevas, 7000);
                //imagenChat.refreshDrawableState();//.setImageResource(R.drawable.chat_msg_si);
                mostrarDialogoAlerta("ALERTA", "APRECIADO OPERADOR SE CREA UNA CANDIDAD\n DE REGISTROS NUEVOS A SU RUTA...\nVER FINAL DE RUTA!!! ...." + contarCuentasNuevas);
                visualizarInformacionCliente(0);
            }
            switch (metodo) {
                case "CHATRECIBE":
                    procesandoenvioenHilos_chat = false;
                    if (respuesta_.equals("SINCONEXION")) {
                        imagenRed.setImageResource(R.drawable.redinativa);
                        break;
                    } else {
                        imagenRed.setImageResource(R.drawable.redactiva);
                    }
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

  /*  public void guardarBackupArchivoFotos() {
        try {
            String nuevoEnvioFotos = archivoEFotos + serialPDA + ".SDA";
            String backupEnvioFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/BKEnvioFotos.SDA";

            utils.CrearCopiaSin(nuevoEnvioFotos, backupEnvioFotos); //Crea una copis sin sobreescribir

            File file_backupEnvioFotos_2 = new File(VariablesGlobales.directorioBackUp + "BKEnvioFotos.SDA");

            File file_backupEnvioGPRS = new File(backupEnvioFotos);

            if (file_backupEnvioFotos_2.exists()) {
                file_backupEnvioFotos_2.delete();
            }
            utils.CrearCopia(file_backupEnvioGPRS.getAbsolutePath(), file_backupEnvioFotos_2.getAbsolutePath());
        } catch (Exception ex) {
            logger.info("guardarBackupArchivoFotos() " + ex.getMessage());
        }
    }*/

    public void tiempoYDistanciaPromedio() {

        TablaRegistroSalida infoRegistroSalidaTemp = new TablaRegistroSalida();
        infoRegistroSalidaTemp.setArchivo_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());
        infoRegistroSalidaTemp.inicializaBloque();
        int tiempoPromedio = 0;
        int distanciaPromedios = 0;

        if (!infoRegistroSalidaTemp.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida())) {
            return;
        }

        int registros = 1;
        while (registros <= infoRegistroSalidaTemp.getTotal_TablaRegistroSalida()) {
            infoRegistroSalidaTemp.lectura_TablaRegistroSalidaII(registros);

            int Tiempo1 = 0;

            if (!infoRegistroSalidaTemp.gettablaRegistroSalida_TIEMPO().trim().equals("")) {
                try {
                    Tiempo1 = Integer.parseInt(infoRegistroSalidaTemp.gettablaRegistroSalida_TIEMPO().trim());
                } catch (Exception ex1) {
                    logger.info("PromedioTiempo() " + ex1.getMessage());
                    Tiempo1 = 0;
                }
                tiempoPromedio = tiempoPromedio + Tiempo1;
            }

            if (!infoRegistroSalidaTemp.gettablaRegistroSalida_DISTANCIACALCULADA().trim().equals("")) {
                int discalculada = 0;
                try {
                    discalculada = Integer.parseInt(infoRegistroSalidaTemp.gettablaRegistroSalida_DISTANCIACALCULADA().trim());
                } catch (Exception ex) {
                    logger.info("PromedioDistancia() " + ex.getMessage());
                    discalculada = 50;
                }
                distanciaPromedios = distanciaPromedios + discalculada;
            }
            registros++;
        }
        if (variables.totalregistrosleidos != 0) {
            variables.totalTiempoPromedio = (tiempoPromedio / variables.totalregistrosleidos);
            variables.totalDistanciaPromedio = (distanciaPromedios / variables.totalregistrosleidos);
        }
    }


    public void actualizarVariablesConteo() {

        if (variables.estado.trim().equals("2")) {
            VariablesGlobales.totalLecturasIguales++;
        } else if (variables.estado.trim().equals("3")) {
            VariablesGlobales.totalConsumoNormal++;
        } else if (variables.estado.trim().equals("1")) {
            VariablesGlobales.totalConsumoAlto++;
        } else if (variables.estado.trim().equals("9")) {
            VariablesGlobales.totalConsumoBajo++;
        }
        if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("") && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0") && !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000"))
            VariablesGlobales.totalpredioscomentarios++;
    }

    public void restarVariablesConteo(String critica) {

        if (critica.trim().equals("2")) {
            VariablesGlobales.totalLecturasIguales--;
        } else if (critica.trim().equals("3")) {
            VariablesGlobales.totalConsumoNormal--;
        } else if (critica.trim().equals("1")) {
            VariablesGlobales.totalConsumoAlto--;
        } else if (critica.trim().equals("9")) {
            VariablesGlobales.totalConsumoBajo--;
        }
    }

    public void activityFirma() {

        Intent intents = new Intent();
        intents.putExtra("rutaFullFoto", VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "_FM_01.jpg");
        intents.putExtra("rutaFullFfile", VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "F" + NombreArchivos);
        intents.putExtra("id", infoRegistroSalida.getId());
        intents.putExtra("cuenta", infoRegistroSalida.gettablaRegistroSalida_CUENTA());
        intents.putExtra("serialPDA", serialPDA);
        intents.setClass(getApplicationContext(), Firma.class);
        //startActivity(intents);
        startActivityForResult(intents, CAPT_FIRMA_REQUEST_CODE);
        ventanaFirma = true;
    }

    /*private int ConsultaNoEnviados() {

        try {
            int totEnvioGPS = 0;
            int totEnvioGPSser = 0;
            String rutaEnvioGPS = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA";

            if (misenvios.abrir_EnvioGPS(rutaEnvioGPS)) {
                totEnvioGPS = misenvios.total_EnvioGPS;
            }
            misenvios.Cerrar_EnvioGPS();

            String rutaEnvioGPSser = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA";

            if (misenvios.abrir_EnvioGPS(rutaEnvioGPSser)) {
                totEnvioGPSser = misenvios.total_EnvioGPS;
            }
            misenvios.Cerrar_EnvioGPS();

            return totEnvioGPS + totEnvioGPSser;

        } catch (Exception ex) {
            logger.info("ConsultaNoEnviados()" + ex);
        }
        return -1;
    }*/

    private int ConsultaNoEnviados() {
        // ===== CONSULTAR REALM EN LUGAR DE ARCHIVOS PLANOS =====
        try {
            return CrudEnvioLectura.contarPendientes();
        } catch (Exception ex) {
            logger.info("ConsultaNoEnviados()" + ex);
            return -1;
        }
    }


    /*public String actualizaFotos_ANTERIOR() { //Recorre el archivo de fotos para actualizar los que si se fueron "Y"
        try {
            int desde = 31;//26
            int hasta = 32;//27

            String path = VariablesGlobales.directorioactual + "VariablesGlobales.getCarpetaLecturas()+"/" + "F" + NombreArchivos;
            FileReader r = new FileReader(path); //Ax: se recorre el archivo para actualizar las eNviadas
            File fileName = new File(path + "_temp");
            if (fileName.exists()) {
                fileName.delete();
            }
            BufferedReader reader = new BufferedReader(r);
            String linea;
            int conteo = 0;
            logger.info("actualizara las fotos enviadas() ");
            while ((linea = reader.readLine()) != null) { //Existe una remota posibilidad de que esto choque con Insertar nombre de foto nueva
                conteo++;

                for (int i = 0; i < posicfotocont.length; i++) {
                    if (conteo == posicfotocont[i]) {
                        linea = linea.substring(0, desde) + "Y" + linea.substring(hasta);
                        break;
                    }
                }
                utils.EscribirLinea(fileName, linea + "\r\n");
            }
            r.close();

            File fpath = new File(path);
            try {
                if (reader != null) reader.close();

                fpath.delete();
            } catch (Exception ex) {
                logger.info("actualizaFotos() " + ex.getMessage());
                return "Error en la actualizacion de fotos.. \n " + ex.getMessage();
            }
            //fileName.renameTo(fpath);
            boolean renamed = fileName.renameTo(fpath);

            if (!renamed) {
                copiarArchivo(fileName, fpath);
                fileName.delete();
            }


        } catch (Exception ex) {
            logger.info("actualizaFotos(). " + ex.getMessage());
            return "Error en la actualizacion de fotos \n " + ex.getMessage();
        } finally {
            posicfotocont = null;//rx
        }
        return "";
    }*/

    /*public String actualizaFotos() {
        int desde = 31;
        int hasta = 32;

        String path = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/F" + NombreArchivos;
        File original = new File(path);
        File temp = new File(path + "_temp");
        if (temp.exists()) {//&& temp.length() > original.length()
            temp.delete();
        }
        try (
                BufferedReader reader = new BufferedReader(new FileReader(original));
                BufferedWriter writer = new BufferedWriter(new FileWriter(temp, false))
        ) {
            String linea;
            int conteo = 0;

            while ((linea = reader.readLine()) != null) {
                conteo++;

                for (int pos : posicfotocont) {
                    if (conteo == pos) {
                        linea = linea.substring(0, desde) + "Y" + linea.substring(hasta);
                        break;
                    }
                }
                writer.write(linea);
                writer.newLine();
            }

            writer.flush();

        } catch (Exception ex) {
            logger.info("actualizaFotos()", ex.getMessage());
            return "Error actualizando fotos: " + ex.getMessage();
        }

        // 🔁 Reemplazo seguro
        if (!original.delete()) {
            return "No se pudo eliminar archivo original";
        }

        if (!temp.renameTo(original)) {
            return "No se pudo renombrar archivo temporal";
        }

        posicfotocont = null;
        return "";
    }*/

    /*public int fotosFaltantes() { //Recorre el archivo de fotos para actualizar los que si se fueron "Y"
        FileReader r = null;
        int cont = 0;
        int desde = 31;//26
        int hasta = 32;//27

        try {
            String path = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "F" + NombreArchivos;
            r = new FileReader(path);

            BufferedReader reader = new BufferedReader(r);
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.substring(desde, hasta).equals("X") || linea.substring(desde, hasta).equals("_")) {
                    cont++;
                }
            }
            reader.close();
        } catch (Exception ex) {
            logger.info("fotosFaltantes(): " + ex);
            if (cont > 0) return cont;
            else return -1;
        } finally {
            try {
                if (r != null) r.close();
            } catch (Exception ex) {
                logger.info("fotosFaltantes()| Error cerrando archivo de fotos: " + ex);
            }
        }
        return cont;
    }*/

    private void crearSyncBroadcastReceiver() {
        syncBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) return;

                switch (action) {
                    case LecturaSyncService.ACTION_SYNC_STARTED:
                        // Mostrar progressBar_cyclic
                        runOnUiThread(() -> {
                            ProgressBar progressBar = findViewById(R.id.progressBar_cyclic);
                            if (progressBar != null) {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        });
                        Log.d("MenuDeLiquidacion", "Sync iniciado - progressBar visible");
                        break;

                    case LecturaSyncService.ACTION_SYNC_FINISHED:
                        // Ocultar progressBar_cyclic
                        runOnUiThread(() -> {
                            ProgressBar progressBar = findViewById(R.id.progressBar_cyclic);
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        Log.d("MenuDeLiquidacion", "Sync terminado - progressBar oculto");
                        break;

                    case LecturaSyncService.ACTION_SYNC_PROGRESS:
                        String message = intent.getStringExtra(LecturaSyncService.EXTRA_PROGRESS_MESSAGE);
                        String type = intent.getStringExtra(LecturaSyncService.EXTRA_PROGRESS_TYPE);
                        int count = intent.getIntExtra(LecturaSyncService.EXTRA_PROGRESS_COUNT, 0);
                        Log.d("MenuDeLiquidacion", "Sync progreso: " + message);
                        // Opcional: mostrar mensaje en UI
                        break;

                    case LecturaSyncService.ACTION_CHAT_STATUS:
                        boolean chatActive = intent.getBooleanExtra(LecturaSyncService.EXTRA_CHAT_ACTIVE, false);
                        boolean isBusy = intent.getBooleanExtra(LecturaSyncService.EXTRA_IS_BUSY, false);

                        // Actualizar icono del chat
                        runOnUiThread(() -> {
                            if (!isBusy) {
                                // Replicar lógica del Reloj3

                                if (infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0S") ||
                                        infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0P") ||
                                        infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("0J")) {
                                    imagenChat.setImageResource(R.drawable.chat_msg_si);
                                } else {
                                    imagenChat.setImageResource(R.drawable.chat_msg_no);
                                }
                                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("V")) {//nuevo para verificar cuentas
                                    imagenChat.setImageResource(R.drawable.verificar);
                                }

                            }
                        });
                        break;

                    case LecturaSyncService.ACTION_REQUEST_CHAT_CALL:
                        // El servicio solicita que ejecutemos chatAsyncall()
                        // Esto reemplaza la llamada que hacía Reloj3
                        runOnUiThread(() -> {
                            if (!estaEnvioAlWsOcupado) {
                                procesandoenvioenHilos_chat = true;
                                chatAsyncall();
                            }
                        });
                        Log.d("MenuDeLiquidacion", "Ejecutando chatAsyncall() por solicitud del servicio");
                        break;

                    case LecturaSyncService.ACTION_LECTURAS_ENVIADAS:
                        int lecturasEnviadas = intent.getIntExtra(LecturaSyncService.EXTRA_CANTIDAD_ENVIADA, 0);
                        Log.i("MenuDeLiquidacion", "Lecturas enviadas por servicio: " + lecturasEnviadas);
                        break;

                    case LecturaSyncService.ACTION_FOTOS_ENVIADAS:
                        int fotosEnviadas = intent.getIntExtra(LecturaSyncService.EXTRA_CANTIDAD_ENVIADA, 0);
                        Log.i("MenuDeLiquidacion", "Fotos enviadas por servicio: " + fotosEnviadas);
                        break;

                    case LecturaSyncService.ACTION_CUENTAS_ENVIADAS:
                        int cuentasEnviadas = intent.getIntExtra(LecturaSyncService.EXTRA_CANTIDAD_ENVIADA, 0);
                        Log.i("MenuDeLiquidacion", "Cuentas nuevas enviadas por servicio: " + cuentasEnviadas);
                        break;
                }
            }
        };
    }

    private void registrarSyncBroadcastReceiver() {
        if (syncBroadcastReceiver == null) {
            crearSyncBroadcastReceiver();
        }

        if (!receiverRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(LecturaSyncService.ACTION_SYNC_STARTED);
            filter.addAction(LecturaSyncService.ACTION_SYNC_FINISHED);
            filter.addAction(LecturaSyncService.ACTION_SYNC_PROGRESS);
            filter.addAction(LecturaSyncService.ACTION_CHAT_STATUS);
            filter.addAction(LecturaSyncService.ACTION_REQUEST_CHAT_CALL);
            filter.addAction(LecturaSyncService.ACTION_LECTURAS_ENVIADAS);
            filter.addAction(LecturaSyncService.ACTION_FOTOS_ENVIADAS);
            filter.addAction(LecturaSyncService.ACTION_CUENTAS_ENVIADAS);

            registerReceiver(syncBroadcastReceiver, filter);
            receiverRegistered = true;
            Log.d("MenuDeLiquidacion", "BroadcastReceiver registrado");
        }
    }

    private void desregistrarSyncBroadcastReceiver() {
        if (receiverRegistered && syncBroadcastReceiver != null) {
            try {
                unregisterReceiver(syncBroadcastReceiver);
                receiverRegistered = false;
                Log.d("MenuDeLiquidacion", "BroadcastReceiver desregistrado");
            } catch (Exception e) {
                Log.w("MenuDeLiquidacion", "Error desregistrando receiver: " + e.getMessage());
            }
        }
    }

    public int fotosFaltantes() {
        // ===== CONSULTAR REALM EN LUGAR DE ARCHIVO PLANO =====
        try {
            return CrudEnvioFoto.contarPendientes();
        } catch (Exception ex) {
            logger.info("fotosFaltantes(): " + ex);
            return -1;
        }
    }


    //Ax: muestra mensaje en pantalla con boton 'ok' pero no hace nada
    public void mensajeOk(String msg, final String metodos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (metodos) {
                            case "retomaFoto":
                                capturaImagenFotografica();
                                break;
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    static class TaskHelper {

        public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
            execute(task, (P[]) null);
        }

        @SuppressLint("NewApi")
        public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else {
                task.execute(params);
            }
        }
    }

    public void estaEnvioAlWsOcupadoTrue() {
        estaEnvioAlWsOcupado = true;
        fechaEnvioAlWsOcupado = new Date();//Ax. esta fecha es para enviar si despues de x tiempo no pasa nada
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //handler.removeCallbacks(handlerTask);
            desregistrarSyncBroadcastReceiver();
        } catch (Exception ex) {
        }
        nuevoImpresor.close(); // Ax: cierra el executor del modelo de impresión nuevo
    }


    //create a file where the photo will be saved
    private File createImageFile() throws IOException {
        // Create an image file name

        File storageDir = new File(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL");//getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e("error", namePhoto + " directorio " + storageDir + "/" + namePhoto + ".jpg");
        /*File image = File.createTempFile(
                namePhoto,  /* prefix
                ".jpg",         /* suffix
                storageDir      /* directory
        );*/
        File image = new File(storageDir, "/" + namePhoto + ".jpg" /* directory */);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //take picture
    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.gstolima.accesoyseguridad.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    }*/
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NUEVA FORMA PARA TOMAR LA FOTO
    private void nwdispatchTakePictureIntent() {
        imageUri = createImageUri(nameForIntent);

        if (imageUri != null) {
            // Antes: new Intent(MediaStore.ACTION_IMAGE_CAPTURE) -> abria la camara del sistema,
            // sin control de flash/temporizador desde la app. Ahora usamos nuestra propia pantalla.
            Intent takePictureIntent = new Intent(this, CamaraFotoActivity.class);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //startActivityForResult(takePictureIntent, TAKE_PICTURE);
            CodigoIntent = TAKE_PICTURE;
            activityResultLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No se pudo crear el archivo para la imagen", Toast.LENGTH_SHORT).show();
        }
    }
    private Uri createImageUri(String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/FOTOGRAFIASL");

        ContentResolver resolver = getContentResolver();
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultado = result.getResultCode();
                    try {
                        //Intent data = result.getData();
                        Log.e("INFO", "lecturaFragment|onActivityResult | CodigoIntent: " + CodigoIntent + " | TAKE_PICTURE: " + TAKE_PICTURE);
                        if (CodigoIntent == TAKE_PICTURE) {//Respuesta Tomar fotos en liq.
                            Log.e("INFO", "lecturaFragment|onActivityResult | RESULT_OK: " + RESULT_OK + " | resultCode: " + resultado);
                            if (resultado == RESULT_OK) {
                                vecesImagen = 0;
                                exitoImagen = 1;
                               /* if (NumeroDeFotos>1)
                                {
                                    NumeroDeFotos = NumeroDeFotos - 1;
                                    vecesImagen = NumeroDeFotos;
                                }*/
                                ejecutarProcesoDeFotoII(vecesImagen);//Ax: idealmente se tomo la foto

                            } else {//Ax: No se tomo foto
                                if (FotoObligatoriaXLectura != 0) {// nueva forma de sacar a la persona que por error entro a ntomar foto
                                    if (vecesImagen > 0) {
                                        mensajeT("No se tomo foto, pruebe de nuevo", msgMedio);
                                    }
                                   /* if (NumeroDeFotos>1)
                                    {
                                        vecesImagen = NumeroDeFotos;
                                    }*/
                                    vecesImagen = 1;
                                    ejecutarProcesoDeFotoII(vecesImagen);
                                } else {
                                    YaTomoFotoCliente = 0;
                                    vecesImagen = 0;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("ERROR", "[MenuDeLiquidacion]onActivityResultLauncher|ERROR|" + ex.getMessage());
                        //util.log(, "[MenuDeLiquidacion]onActivityResultLauncher()| Error -> " + ex.getMessage());
                        logger.error("[MenuDeLiquidacion]onActivityResultLauncher()| Error -> " + ex.getMessage());
                    }
                }
            });


    //nuevo validar el codigo del lector para poderlo escribir siempre en su respectivo campo

    private void validarCodigoUsuario() {

        try {
            variables.setNombregeneral(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/" + "E" + NombreArchivos);
            // variables.setNombregeneral(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "E" + archivocargado);
            File archivoGeneral = new File(variables.getNombregeneral());

            Log.e("DEBUG", "Ruta: " + archivoGeneral.getAbsolutePath());

            if (archivoGeneral.exists()) {
                Log.e("DEBUG", "Existe Archivo: " + archivoGeneral.getAbsolutePath());
                tablaEncabezado.setarchivo_General(variables.getNombregeneral());
                if (tablaEncabezado.abrir_General(tablaEncabezado.getarchivo_General())) {
                    Log.e("DEBUG", "se abrio  Archivo: " + archivoGeneral.getAbsolutePath());
                    tablaEncabezado.lectura_General(1);
                    ControlEspecialZonaRoja = tablaEncabezado.getGeneral_SECCION(); // zona roja
                    ControlEspecialImpresora = tablaEncabezado.getGeneral_OBLIGACBARRAS(); //foto obliga
                    String lectorTmp = tablaEncabezado.getGeneral_LECTOR();
                    String codigoTmp = tablaEncabezado.getGeneral_SUPERVISOR().trim();

                    Log.e("DEBUG", "LECTOR: " + lectorTmp);
                    Log.e("DEBUG", "SUPERVISOR: " + codigoTmp);

                    if (lectorTmp != null) {
                        lector = lectorTmp.trim();
                        variables.setGlobaloperario(lector);
                    }

                    if (codigoTmp != null)
                        variables.setCodigoEmpresa(codigoTmp.trim());

                    tablaEncabezado.Cerrar_General();
                }

            } else {
                mensajeT("No hay Archivo de Encabezado...E" + NombreArchivos, msgLargo);
            }

        } catch (Exception ex) {
            Log.e("error", "datos error " + ex.getMessage());
        }
    }

    // ================================================================================
    // MÉTODOS DE RECONSTRUCCIÓN TOTAL DESDE ARCHIVOS PLANOS
    // ================================================================================

    /**
     * RECONSTRUYE TODAS las lecturas y fotos en Realm desde los archivos planos.
     * <p>
     * Usar cuando:
     * - Se eliminó y reinstalo la app (Realm vacío)
     * - Se copiaron archivos de otro teléfono
     * - Se quiere forzar reenvío de todo
     * <p>
     * CORREGIDO: Valida duplicados y solo recupera fotos de cuentas leídas
     */
    private String reconstruirTodoDesdeArchivos() {
        try {
            Log.i("INFO", "=== INICIO RECONSTRUCCIÓN TOTAL ===");

            int lecturasRecuperadas = 0;
            int lecturasYaExistentes = 0;
            int fotosRecuperadas = 0;
            int registrosProcesados = 0;
            String cuentasProblemaLocal = "";

            // Lista de cuentas leídas para validar fotos después
            java.util.Set<String> cuentasLeidas = new java.util.HashSet<>();

            // ===== FASE 1: RECONSTRUIR LECTURAS DESDE ARCHIVOS D, M, E =====

            try {
                abrirArchivosDeFacturacion();
                int numRegistroOriginal = VariablesGlobales.registroactual;
                VariablesGlobales.registroactual = 1;

                int totalRegistros = infoRegistroSalida.getTotal_TablaRegistroSalida();
                Log.i("INFO", "Reconstruyendo desde " + totalRegistros + " registros...");

                // Obtener anno y mes actual para validaciones
                String annoActual = "";
                String mesActual = "";
                try {
                    annoActual = variables.amd.substring(0, 4);
                    mesActual = variables.amd.substring(4, 6);
                } catch (Exception e) {
                    annoActual = String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
                    mesActual = String.format("%02d", java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1);
                }

                while (VariablesGlobales.registroactual <= totalRegistros) {
                    leerInformacionUsuario(1);
                    registrosProcesados++;

                    String fechaLectura = infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim();
                    String leido = infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim();
                    String cuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();
                    String tipoMedidor = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA().trim();

                    // Solo procesar registros que fueron leídos (tienen fecha)
                    if (fechaLectura.isEmpty()) {
                        VariablesGlobales.registroactual++;
                        continue;
                    }

                    if (leido.isEmpty()) {
                        cuentasProblemaLocal += cuenta + "/";
                        VariablesGlobales.registroactual++;
                        continue;
                    }

                    // Agregar a lista de cuentas leídas (para validar fotos después)
                    cuentasLeidas.add(cuenta + "_" + tipoMedidor);

                    // ===== VERIFICAR SI YA EXISTE EN REALM =====
                    if (CrudEnvioLectura.existe(cuenta, annoActual, mesActual, tipoMedidor)) {
                        Log.d("INFO", "Lectura ya existe en Realm: " + cuenta + " / " + tipoMedidor);
                        lecturasYaExistentes++;
                        VariablesGlobales.registroactual++;
                        continue;
                    }

                    // Sincronizar lectura modificada
                    sincronizarLecturaModificada();

                    // ===== GUARDAR LECTURA EN REALM =====
                    String nroContador = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();
                    String idContador = infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES();
                    String lecturaTomada = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();

                    String causaNolectura = formatearCampo(
                            infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim(), 2, "0");

                    String intentos = formatearCampo(
                            infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim(), 1, "0");

                    String lecturaModificada1 = formatearCampo(
                            infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim(), 10, "0");

                    String lecturaModificada2 = formatearCampo(
                            infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim(), 10, "0");

                    String digitos = formatearCampo(
                            infoRegistroSalida.gettablaRegistroSalida_NROENTEROS().trim(), 1, "0");

                    String criticaPDA = infoRegistroSalida.gettablaRegistroSalida_LEIDO();

                    String lecturaAnterior = String.valueOf(
                            parseStringToInteger_1(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR()));

                    String codBarras = "";
                    if (VariablesGlobales.getObligabarras().equals("1")) {
                        codBarras = obtenerCodigoBarras();
                    }

                    // Guardar en Realm
                    guardarDatosAEnviarNuevo(
                            nroContador,
                            idContador,
                            lecturaTomada,
                            causaNolectura,
                            intentos,
                            lecturaModificada1,
                            lecturaModificada2,
                            digitos,
                            criticaPDA,
                            lecturaAnterior,
                            99999,
                            codBarras
                    );

                    lecturasRecuperadas++;
                    Log.i("INFO", "Lectura reconstruida: Cuenta " + cuenta);

                    VariablesGlobales.registroactual++;
                }

                VariablesGlobales.registroactual = numRegistroOriginal;

            } catch (Exception e) {
                Log.e("INFO", "Error reconstruyendo lecturas: " + e.getMessage());
                logger.error("Error reconstruyendo lecturas: " + e.getMessage(), e);
            }

            // ===== FASE 2: RECONSTRUIR FOTOS (solo de cuentas leídas) =====

            fotosRecuperadas = escanearYRecuperarFotosDeCuentas(cuentasLeidas);

            // ===== FASE 3: RECONSTRUIR CUENTAS NUEVAS DESDE CUENTASNUEVA{serial}.SDA =====

            int cuentasNuevasRecuperadas = 0;
            int cuentasNuevasYaExistentes = 0;

            try {
                File archivoCuentasNuevas = new File(
                        VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/CUENTASNUEVA" + serialPDA + ".SDA"
                );

                if (archivoCuentasNuevas.exists()) {
                    Log.i("INFO", "Reconstruyendo cuentas nuevas desde: " + archivoCuentasNuevas.getName());

                    java.io.BufferedReader br = new java.io.BufferedReader(
                            new java.io.FileReader(archivoCuentasNuevas)
                    );
                    String lineaCN;

                    while ((lineaCN = br.readLine()) != null) {
                        if (lineaCN.trim().isEmpty()) continue;
                        try {
                            // Formato de línea (mismo que construye grabarSalir() en ModuloCuentaNueva):
                            // [0]  direccion     64 chars
                            // [1]  contador      20 chars
                            // [2]  marca         15 chars
                            // [3]  tipoMedidor    2 chars
                            // [4]  digitos        1 char
                            // [5]  lectura        7 chars
                            // [6]  observacion    2 chars
                            // [7]  ciclo          3 chars
                            // [8]  descDepto     32 chars
                            // [9]  codMunicipio   3 chars
                            // [10] codSector      3 chars
                            // [11] codRuta       13 chars
                            // [12] informe      250 chars
                            // [13] codReferencia  6 chars
                            // [14] latitud       16 chars
                            // [15] longitud      16 chars
                            // [16] fechaHora     20 chars
                            // [17] altitud       10 chars
                            // [18] numSatelites   3 chars
                            // [19] lector         4 chars
                            // [20] foto1         30 chars
                            // [21] foto2         30 chars
                            // [22] foto3         30 chars
                            // [23] cicloReal     10 chars
                            String[] partes = lineaCN.split(";", -1);
                            if (partes.length < 20) continue;

                            String contador = partes[1].trim();
                            if (contador.isEmpty()) continue;

                            // Evitar duplicados
                            if (CrudEnvioCuentaNueva.existePorContador(contador)) {
                                cuentasNuevasYaExistentes++;
                                continue;
                            }

                            EnvioCuentaNueva cn = new EnvioCuentaNueva();
                            cn.setDireccion(partes[0].trim());
                            cn.setContador(contador);
                            cn.setMarca(partes[2].trim());
                            cn.setTipoMedidor(partes[3].trim());
                            cn.setDigitos(partes[4].trim());
                            cn.setLectura(partes[5].trim());
                            cn.setObservacion(partes[6].trim());
                            cn.setCiclo(partes[7].trim());
                            cn.setDescDepto(partes[8].trim());
                            cn.setCodMunicipio(partes[9].trim());
                            cn.setCodSector(partes[10].trim());
                            cn.setCodRuta(partes[11].trim());
                            cn.setInforme(partes[12].trim());
                            cn.setCodReferencia(partes[13].trim());
                            cn.setLatitud(partes[14].trim());
                            cn.setLongitud(partes[15].trim());
                            cn.setFechaHora(partes[16].trim());
                            cn.setAltitud(partes[17].trim());
                            cn.setNumSatelites(partes[18].trim());
                            cn.setLector(partes[19].trim());
                            if (partes.length > 20) cn.setFoto1(partes[20].trim());
                            if (partes.length > 21) cn.setFoto2(partes[21].trim());
                            if (partes.length > 22) cn.setFoto3(partes[22].trim());
                            if (partes.length > 23) cn.setCicloReal(partes[23].trim());
                            cn.setSerial(serialPDA);
                            cn.setEstadoEnvio("P");

                            boolean ok = CrudEnvioCuentaNueva.insertar(cn);
                            if (ok) {
                                cuentasNuevasRecuperadas++;
                                Log.i("INFO", "Cuenta nueva reconstruida: " + contador);
                            }

                        } catch (Exception eCN) {
                            Log.e("INFO", "Error procesando linea cuenta nueva: " + eCN.getMessage());
                        }
                    }
                    br.close();

                } else {
                    Log.i("INFO", "No existe CUENTASNUEVA" + serialPDA + ".SDA - omitiendo");
                }

            } catch (Exception eCuentas) {
                Log.e("INFO", "Error reconstruyendo cuentas nuevas: " + eCuentas.getMessage());
                logger.error("Error reconstruyendo cuentas nuevas: " + eCuentas.getMessage(), eCuentas);
            }

            // ===== FASE 4: ESTADÍSTICAS FINALES =====

            int lecturasPendientes = CrudEnvioLectura.contarPendientes();
            int fotosPendientes = CrudEnvioFoto.contarPendientes();

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✓ RECONSTRUCCIÓN COMPLETADA\n\n");

            mensaje.append("📊 PROCESADOS:\n");
            mensaje.append("   Registros revisados: ").append(registrosProcesados).append("\n");
            mensaje.append("   Lecturas nuevas: ").append(lecturasRecuperadas).append("\n");
            if (lecturasYaExistentes > 0) {
                mensaje.append("   Ya existían: ").append(lecturasYaExistentes).append("\n");
            }
            mensaje.append("   Fotos recuperadas: ").append(fotosRecuperadas).append("\n");
            if (cuentasNuevasRecuperadas > 0) {
                mensaje.append("   Cuentas nuevas recuperadas: ").append(cuentasNuevasRecuperadas).append("\n");
            }
            if (cuentasNuevasYaExistentes > 0) {
                mensaje.append("   Cuentas nuevas ya existían: ").append(cuentasNuevasYaExistentes).append("\n");
            }
            mensaje.append("\n");

            mensaje.append("📄 EN REALM:\n");
            mensaje.append("   Lecturas pendientes: ").append(lecturasPendientes).append("\n");
            mensaje.append("   Fotos pendientes: ").append(fotosPendientes).append("\n\n");

            if (!cuentasProblemaLocal.isEmpty()) {
                mensaje.append("⚠️ Cuentas con problemas: ").append(cuentasProblemaLocal).append("\n\n");
                CuentasProblema = cuentasProblemaLocal;
            }

            mensaje.append("📡 El servicio enviará automáticamente");

            String resultado = mensaje.toString();
            Log.i("MenuDeLiquidacion", resultado);
            logger.info(resultado);

            return resultado;

        } catch (Exception ex) {
            logger.error("[MenuDeLiquidacion]reconstruirTodoDesdeArchivos|ERROR| " + ex.getMessage(), ex);
            return "ERROR: " + ex.getMessage();
        }
    }


    /**
     * Escanea la carpeta de fotos y recupera SOLO las que corresponden a cuentas leídas.
     *
     * @param cuentasLeidas Set con "cuenta_tipoMedidor" de cuentas que tienen lecturas
     * @return Número de fotos recuperadas
     */
    private int escanearYRecuperarFotosDeCuentas(java.util.Set<String> cuentasLeidas) {
        int fotosRecuperadas = 0;

        try {
            // Carpeta de fotos
            String directorioFotos = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/";
            File carpetaFotos = new File(directorioFotos);

            if (!carpetaFotos.exists() || !carpetaFotos.isDirectory()) {
                Log.w("MenuDeLiquidacion", "Carpeta de fotos no existe: " + directorioFotos);
                return 0;
            }

            File[] archivos = carpetaFotos.listFiles();
            if (archivos == null || archivos.length == 0) {
                Log.i("MenuDeLiquidacion", "No hay fotos en: " + directorioFotos);
                return 0;
            }

            Log.i("MenuDeLiquidacion", "Escaneando " + archivos.length + " archivos, validando contra " + cuentasLeidas.size() + " cuentas leídas");

            for (File archivo : archivos) {
                String nombreArchivo = archivo.getName().toLowerCase();

                // Solo procesar .jpg
                if (!nombreArchivo.endsWith(".jpg") && !nombreArchivo.endsWith(".jpeg")) {
                    continue;
                }

                // Parsear nombre: CUENTA_TIPO_CONTEO.jpg
                DatosFoto datos = parsearNombreFoto(archivo.getName());

                if (datos == null) {
                    Log.w("MenuDeLiquidacion", "No se pudo parsear: " + archivo.getName());
                    continue;
                }

                // ===== VALIDAR QUE LA CUENTA TIENE LECTURA =====
                String claveCuenta = datos.cuenta + "_" + datos.tipoMedidor;
                if (!cuentasLeidas.contains(claveCuenta)) {
                    Log.d("MenuDeLiquidacion", "Foto ignorada (cuenta sin lectura): " + archivo.getName());
                    continue;
                }

                // Verificar si ya existe en Realm
                String rutaCompleta = archivo.getAbsolutePath();
                if (CrudEnvioFoto.existeConRuta(rutaCompleta)) {
                    Log.d("MenuDeLiquidacion", "Foto ya en Realm: " + archivo.getName());
                    continue;
                }

                // Obtener anno y mes
                String anno = "";
                String mes = "";
                String ciclo = "";
                try {
                    anno = variables.amd.substring(0, 4);
                    mes = variables.amd.substring(4, 6);
                    ciclo = CicloReal;
                } catch (Exception e) {
                    anno = String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
                    mes = String.format("%02d", java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1);
                    ciclo = "";
                }

                // Guardar en Realm
                boolean guardado = SyncHelper.guardarFotoParaEnvio(
                        rutaCompleta,
                        datos.cuenta,
                        datos.tipoMedidor,
                        "",  // idRegistro - se buscará en el servidor
                        anno,
                        mes,
                        ciclo,
                        datos.campoDestino
                );

                if (guardado) {
                    fotosRecuperadas++;
                    Log.i("MenuDeLiquidacion", "Foto recuperada: " + archivo.getName());
                }
            }

        } catch (Exception e) {
            Log.e("MenuDeLiquidacion", "Error escaneando fotos: " + e.getMessage());
            logger.error("Error en escanearYRecuperarFotosDeCuentas: " + e.getMessage(), e);
        }

        return fotosRecuperadas;
    }

    /**
     * Versión que obtiene las cuentas leídas de los archivos D, M, E
     * y luego escanea fotos solo de esas cuentas
     *
     * @return Número de fotos recuperadas
     */
    private int escanearYRecuperarTodasLasFotos() {
        // Obtener lista de cuentas leídas desde archivos
        java.util.Set<String> cuentasLeidas = obtenerCuentasLeidasDeArchivos();
        return escanearYRecuperarFotosDeCuentas(cuentasLeidas);
    }

    /**
     * Obtiene las cuentas que tienen fecha de lectura desde los archivos D, M, E
     */
    private java.util.Set<String> obtenerCuentasLeidasDeArchivos() {
        java.util.Set<String> cuentasLeidas = new java.util.HashSet<>();

        try {
            abrirArchivosDeFacturacion();
            int numRegistroOriginal = VariablesGlobales.registroactual;
            VariablesGlobales.registroactual = 1;

            int totalRegistros = infoRegistroSalida.getTotal_TablaRegistroSalida();

            while (VariablesGlobales.registroactual <= totalRegistros) {
                leerInformacionUsuario(1);

                String fechaLectura = infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim();
                String cuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();
                String tipoMedidor = infoRegistroSalida.gettablaRegistroSalida_TIPOMEDIDA().trim();

                // Solo agregar si tiene fecha de lectura
                if (!fechaLectura.isEmpty()) {
                    cuentasLeidas.add(cuenta + "_" + tipoMedidor);
                }

                VariablesGlobales.registroactual++;
            }

            VariablesGlobales.registroactual = numRegistroOriginal;

        } catch (Exception e) {
            Log.e("MenuDeLiquidacion", "Error obteniendo cuentas leídas: " + e.getMessage());
        }

        return cuentasLeidas;
    }


    /**
     * Clase auxiliar para datos extraídos del nombre de la foto
     */
    private static class DatosFoto {
        String cuenta;
        String tipoMedidor;
        String conteo;
        String campoDestino;
    }

    /**
     * Parsea nombre de foto: CUENTA_TIPO_CONTEO.jpg
     * Ejemplo: 608043_A3_01.jpg → cuenta=608043, tipo=A3, conteo=01, campo=foto_1
     */
    private DatosFoto parsearNombreFoto(String nombreFoto) {
        try {
            String sinExtension = nombreFoto.toLowerCase()
                    .replace(".jpg", "")
                    .replace(".jpeg", "")
                    .replace(".png", "");

            String[] partes = sinExtension.split("_");

            if (partes.length < 3) {
                return null;
            }

            DatosFoto datos = new DatosFoto();
            datos.cuenta = partes[0].toUpperCase();
            datos.tipoMedidor = partes[1].toUpperCase();
            datos.conteo = partes[2];

            // Determinar campo destino
            int numConteo = 1;
            try {
                numConteo = Integer.parseInt(datos.conteo);
            } catch (NumberFormatException e) {
                numConteo = 1;
            }

            if (numConteo <= 1) {
                datos.campoDestino = "foto_1";
            } else if (numConteo == 2) {
                datos.campoDestino = "foto_2";
            } else {
                datos.campoDestino = "foto_3";
            }

            // Casos especiales
            if (nombreFoto.toUpperCase().contains("FM")) {
                datos.campoDestino = "firma";
            } else if (nombreFoto.toUpperCase().contains("CN")) {
                datos.campoDestino = "";
            }

            return datos;

        } catch (Exception e) {
            Log.e("MenuDeLiquidacion", "Error parseando nombre foto: " + e.getMessage());
            return null;
        }
    }

    //****CREACION EN ESTE PROYECTO EL MODULO DE IMPRESION
    void EscribaArchivoImpresion(String Texto) {

        int TipoPaginaImpresion = 2;
        //proceso si fue facturado deberia impresar el proceso de creacion de archivos donde creo que cuando manda a procesar la impresion
        double consumo_ = parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        if ((parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) < parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"))) && parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) > 0)
            consumo_ = 100000 + parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        int consumo = (int) consumo_;
        String texto = "";
        //NECESITAMOS INCLUIR EL ENVIO A LAS LOS DIFERENTES FORMATOS
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("7") ||
                infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("6") ||
                infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("9"))    //|| infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("1"))
            TipoPaginaImpresion = 1;

        String LecturaTomada = "";
        if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000") || infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            LecturaTomada = DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
        } else {
            if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()>0)
            {
                LecturaTomada=infoRegistroSalida.gettablaRegistroSalida_INFORME().trim();
            } else  {
                if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length()>0)
                {
                    LecturaTomada=DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
                } else  {

                    LecturaTomada = "0 : Toma Exitosa";
                }
            }
        }


        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            //tipo 1
                /*"Contrato : C4511951-25"
                "Apreciado usuario el dia de hoy "+tomarFechaSistemaII(1)+
                " visitamos su predio ubicado en la direccion:"+
                "Estimado usuario el dia de hoy "+
                infoRegistroSalida.gettablaRegistroSalida_DIRECCION()+
                " con numero de cuenta: "+
                infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim())
                "DONDE NO FUE POSIBLE LA TOMA DE LECTURA DEL SERVICIO DE ENERGIA, SENOR(A)   : " + infoRegistroSalida.gettablaRegistroSalida_NOMBRE()
                "EVITE QUE SE ACUMULE SU CONSUMO LE RECORDAMOS COMUNICARSE ANTES DE 24 HORAS DE LA FECHA DE TOMA DE LECTURA CON LA LINEA 3102305947 O LA LINEA WHATSAPP: 3102305947"
                 "O AL CORREO ELECTRONICO reportesulectura@gmail.com PARA COORDINAR NUEVA VISITA A SU PREDIO "
                "Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR()
                "Proceso de Lectura elaborado por INELMA SAS NIT. 800095465-0"*/

            texto += "! 0 200 200 580 1" + "\r\n";
            texto += "LABEL" + "\r\n";
            texto += ";//CONTRAST 0" + "\r\n";
            texto += ";//TONE 0" + "\r\n";
            texto += ";//SPEED 3" + "\r\n";
            texto += "PAGE-WIDTH 580" + "\r\n";
            texto += ";//BAR-SENSE" + "\r\n";
            texto += ";// PAGE 0000000004190520" + "\r\n";
            texto += "PCX 5 0 !<EMSA.PCX" + "\r\n";

            texto += "T 7 0 85  80 " + "Contrato : C4511951" + "\r\n";
            texto += "T 7 0 85 100 " + "Apreciado usuario el dia de hoy " + "\r\n";
            texto += "T 7 0 85 120 " + tomarFechaSistemaII(1) + "visitamos su" + "\r\n";
            texto += "T 7 0 85 140 " + "predio ubicado en la direccion:" + "\r\n";

            texto += "T 7 0 85 160 " + infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(0, 30) + "\r\n";
            texto += "T 7 0 85 180 " + infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(30) + "\r\n";

            texto += "T 7 0 85 200 " + "con numero de cuenta: " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "\r\n";
            texto += "T 7 0 85 220 " + "\r\n";
            texto += "T 7 0 85 240 " + "DONDE NO FUE POSIBLE LA TOMA DE " + "\r\n";
            texto += "T 7 0 85 260 " + "LECTURA DEL SERVICIO DE ENERGIA" + "\r\n";
            texto += "T 7 0 85 280 " + "SENOR(A)   : " + infoRegistroSalida.gettablaRegistroSalida_NOMBRE() + "\r\n";
            texto += "T 7 0 85 300 " + "EVITE QUE SE ACUMULE SU CONSUMO LE " + "\r\n";
            texto += "T 7 0 85 320 " + "RECORDAMOS COMUNICARSE ANTES DE 24 " + "\r\n";
            texto += "T 7 0 85 340 " + "HORAS DE LA FECHA DE TOMA DE LECTURA " + "\r\n";
            texto += "T 7 0 85 360 " + "A LA LINEA WHATSAPP: 3102305947 " + "\r\n";
            //texto += "T 7 0 65 380 " + "LINEA WHATSAPP: 3102305947" + "\r\n";
            texto += "T 7 0 85 380 " + "O AL CORREO ELECTRONICO " + "\r\n";
            texto += "T 7 0 85 400 " + "reportelectura@inelma.com.co PARA " + "\r\n";
            texto += "T 7 0 85 420 " + "COORDINAR NUEVA VISITA A SU PREDIO " + "\r\n";
            texto += "T 7 0 85 440 " + "Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR() + "\r\n";
            texto += "T 7 0 85 460 " + "Proceso de Lectura elaborado por " + "\r\n";
            texto += "T 7 0 85 480 " + "INELMA SAS" + "\r\n";
            texto += "T 7 0 85 500 " + "version " + VariablesGlobales.versionApp + "\r\n";
            texto += "T 7 0 85 520 " + ".." + "\r\n";
            texto += "PRINT" + "\r\n";

        } else {
            //tipo 2
            String Municipio = DescripcionMunicipio(variables.getCodigoEmpresa());
            texto += "! 0 200 200 560 1\r\n";
            texto += "LABEL\r\n";
            texto += ";//CONTRAST 0\r\n";
            texto += ";//TONE 0\r\n";
            texto += ";//SPEED \r\n";
            texto += "PAGE-WIDTH 560\r\n";
            texto += ";//BAR-SENSE\r\n";
            texto += ";// PAGE 0000000006200520\r\n";
            texto += "PCX 5 0 !<EMSA1.PCX\r\n";
            /*

             */

            texto += "T 7 0 85  70 " + "Contrato : C4511951" + "\r\n";
            texto += "T 7 0 85 100 " + "Estimado usuario el dia de hoy" + "\r\n";
            texto += "T 7 0 85 120 " + tomarFechaSistemaII(1) + " Estuvimos" + "\r\n";
            texto += "T 7 0 85 140 " + "realizando la toma de lectura con" + "\r\n";
            texto += "T 7 0 85 160 " + "reporte" + "\r\n";
            texto += "T 7 1 175 170 " + "Lectura  : " + parseStringToInteger(String.format("%1$9s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) + "\r\n";

            texto += "T 7 0 85 220 " + "OBS: " + LecturaTomada + "\r\n";
            texto += "T 7 0 85 240 " + "Cuenta   : " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "\r\n";
            texto += "T 7 0 85 260 " + "Municipio: " + Municipio.trim() + "\r\n";
            texto += "T 7 0 85 280 " + "Nombre   : " + infoRegistroSalida.gettablaRegistroSalida_NOMBRE().substring(0, 20) + "\r\n";

            texto += "T 7 0 85 300 " + "Direccion: " + infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(0, 20) + "\r\n";
            texto += "T 7 0 85 320 " + infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(20, 40) + "\r\n";
            texto += "T 7 0 85 340 " + "Medidor  : " + infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()+" - Marca: " +infoRegistroSalida.gettablaRegistroSalida_MARCA().trim() + "\r\n";
            texto += "T 7 0 85 360 " + "Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR() + "\r\n";
            texto += "T 7 0 85 380 " + "PARA MAYOR INFORMACION COMUNIQUESE A " + "\r\n";
            texto += "T 7 0 85 400 " + "LA LINEA WHATSAPP 3102305947" + "\r\n";
            texto += "T 7 0 85 420 " + "Proceso de Lectura elaborado " + "\r\n";
            texto += "T 7 0 85 440 " + "por INELMA SAS" + "\r\n";
            texto += "T 7 0 85 460 " + "version " + VariablesGlobales.versionApp + "\r\n";
            texto += "T 7 0 85 480 " + ".." + "\r\n";
            texto += "PRINT" + "\r\n";

        }

        File fileName = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.TXT");

        if (fileName.exists())
            fileName.delete();

        utils.EscribirLinea(fileName, texto);

        if (!VariablesGlobales.tipoDeRuta.equals("E")) {//EsEntregas != "SI")
            imprimirTextoNuevoModelo(texto); // Ax: reemplaza imprimirF2() - ver MODELO NUEVO DE IMPRESIÓN al final
        }
    }        //FIN IMPRESION DE ARCHIVOS

    private String descripcionEstado(String estado) {
        switch (estado == null ? "" : estado.trim()) {
            case "1":
                return "CONSUMO ALTO";
            case "9":
                return "CONSUMO BAJO";
            case "3":
                return "CONSUMO NORMAL";
            case "2":
                return "LECTURAS IGUALES";
            case "7":
                return "LECTURA MENOR A ANTERIOR";
            case "8":
                return "CRITICADA";
            case "5":
                return "ANOMALIA";
            default:
                return "LECTURA EXITOSA";   // fallback para códigos no listados (p.ej. "6")
        }
    }

    //nuevo modelo para imprimir
  /*  private void EscribaArchivoImpresionESC2(Bitmap logo) {//Bitmap logo

        String Direccion1 = infoRegistroSalida.gettablaRegistroSalida_DIRECCION().trim();
        if (Direccion1.length()>30)
        {
            Direccion1 = Direccion1.substring(0,30);
        }
        String Nombre1 = infoRegistroSalida.gettablaRegistroSalida_NOMBRE().trim();
        if (Direccion1.length()>30)
        {
            Nombre1 = Nombre1.substring(0,30);
        }


        double consumo_ = parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        if ((parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) < parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"))) && parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) > 0)
            consumo_ = 100000 + parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        int consumo = (int) consumo_;

        if (consumo < 0)
            consumo = 0;

        String LecturaTomada = "";

        if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000") || infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            LecturaTomada = DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
        } else {
            if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()>0)
            {
                LecturaTomada=infoRegistroSalida.gettablaRegistroSalida_INFORME().trim();
            } else  {
                if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length()>0)
                {
                    LecturaTomada=DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
                } else  {
                    LecturaTomada = "0 : Toma Exitosa";
                }
            }
        }


        String Municipio = DescripcionMunicipio(variables.getCodigoEmpresa());
        EscPosBuilder esc = new EscPosBuilder();
        esc.Fondo_B();
        esc.Linea_SMALL();
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            esc.center()
                    .logo(logo)

                    .left()
                    .Fondo_B()
                    .Linea_SMALL()
                    .densidad()
                    .line("Contrato : C4511951")
                    .line("Apreciado usuario el dia de hoy " + tomarFechaSistemaII(1) + " visitamos su predio ubicado en la direccion:" + Direccion1 + " con numero de cuenta: " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim())
                    .line("DONDE NO FUE POSIBLE LA TOMA DE LECTURA DEL SERVICIO DE ENERGIA, SENOR(A)   : " + Nombre1)
                    .line("EVITE QUE SE ACUMULE SU CONSUMO LE RECORDAMOS COMUNICARSE ANTES DE 24 HORAS DE LA FECHA DE TOMA DE LECTURA CON LA LINEA DE WHATSAPP: 3102305947")
                    .line("O AL CORREO ELECTRONICO reportelectura@inelma.com.co PARA COORDINAR NUEVA VISITA A SU PREDIO ")
                    .line("Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR())//consumo""
                    .line("Proceso de Lectura elaborado por INELMA SAS NIT. 800095465-0")
                    .line("version " + VariablesGlobales.versionApp)
                    .feed(6)
                    .cut();
        } else {
            esc.center()
                    .logo(logo)

                    .left()
                    .line("Contrato : C4511951")//tomarFechaSistemaII(1)
                    .line("Estimado usuario el dia de hoy")//tomarFechaSistemaII(1)
                    .line("Fecha    : " + tomarFechaSistemaII(1))//tomarFechaSistemaII(1)
                    .line("Estuvimos realizando la toma de lectura con reporte:")//tomarFechaSistemaII(1)
                    .line("Lectura  : " + parseStringToInteger(String.format("%1$9s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")))
                    .line("Cuenta   : " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim())//infoRegistroSalida.gettablaRegistroSalida_CUENTA()
                    .line("Municipio: " + Municipio.trim())
                    .line("Nombre   : " + Nombre1)
                    .line("Direccion: " + Direccion1)
                    .line("Medidor  : " + infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()+" - Marca: " +infoRegistroSalida.gettablaRegistroSalida_MARCA().trim())
                    .line("Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR())
                    .line("PARA MAYOR INFORMACION COMUNIQUESE A LA ")
                    .line("LINEA WHATSAPP 3102305947")//consumo""
                    .line("Proceso de Lectura elaborado por INELMA ")
                    .line("SAS NIT. 800095465-0")
                    .line("version " + VariablesGlobales.versionApp)
                    .feed(6)
                    .cut();
        }
        File file = new File(
                VariablesGlobales.directorioactual +
                        VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.BIN"
        );

        if (file.exists()) file.delete();

        byte[] datosImpresion = esc.build();
        escribirArchivoBinario(file, datosImpresion);

        if (!VariablesGlobales.tipoDeRuta.equals("E")) {
            imprimirBinarioNuevoModelo(datosImpresion); // Ax: reemplaza imprimirESC() - ver MODELO NUEVO DE IMPRESIÓN al final
        }
    }*/

    //nuevo modelo para imprimir
    private void EscribaArchivoImpresionESC(Bitmap logo) {//Bitmap logo

        String Direccion1 = infoRegistroSalida.gettablaRegistroSalida_DIRECCION().trim();
        if (Direccion1.length()>30)
        {
            Direccion1 = Direccion1.substring(0,30);
        }
        String Nombre1 = infoRegistroSalida.gettablaRegistroSalida_NOMBRE().trim();
        if (Direccion1.length()>30)
        {
            Nombre1 = Nombre1.substring(0,30);
        }


        double consumo_ = parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        if ((parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) < parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"))) && parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) > 0)
            consumo_ = 100000 + parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        int consumo = (int) consumo_;

        if (consumo < 0)
            consumo = 0;

        String LecturaTomada = "";

        if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000") || infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            LecturaTomada = DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
        } else {
            if (infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().length()>0)
            {
                LecturaTomada=infoRegistroSalida.gettablaRegistroSalida_INFORME().trim();
            } else  {
                if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().length()>0)
                {
                    LecturaTomada=DescripAnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
                } else  {
                    LecturaTomada = "0 : Toma Exitosa";
                }
            }
        }


        String Municipio = DescripcionMunicipio(variables.getCodigoEmpresa());
        EscPosBuilder esc = new EscPosBuilder();
        esc.Fondo_B();
        esc.Linea_SMALL();
        if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().equals("5")) {
            esc.center()
                    .logo(logo)

                    .left()
                    .Fondo_B()
                    .Linea_SMALL()
                    .densidad()
                    .line("Contrato : C4511951")
                    .line("Fecha    : " + tomarFechaSistemaII(1))
                    .line("Cuenta   : " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim())
                    .line("Direccion: " + Direccion1)
                    .line("Nombre   : " + Nombre1)
                    .line("OBS      : NO FUE POSIBLE LA TOMA DE LECTURA DEL SERVICIO DE ENERGIA")
                    // Ax: párrafos de atención al cliente sin tocar la redacción, solo
                    // envueltos a 42 caracteres (papel 2"/Fondo_B) en vez del wrap
                    // automático de la impresora, para controlar dónde corta cada línea.
                    .lineaEnvuelta("Evite que se acumule su consumo, le recordamos comunicarse antes de 24 horas de la fecha de toma de lectura con la linea de WhatsApp: 3102305947", 42)
                    .lineaEnvuelta("O al correo electronico reportelectura@inelma.com.co para coordinar nueva visita a su predio.", 42)
                    .line("Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR())//consumo""
                    .line("Proceso de Lectura elaborado por INELMA SAS")
                    .line("version " + VariablesGlobales.versionApp)
                    .feed(4) // Ax: era feed(6); si el cortador toca texto, subir de nuevo
                    .cut();
        } else {
            esc.center()
                    .logo(logo)

                    .left()
                    .line("Contrato : C4511951")//tomarFechaSistemaII(1)
                    .line("Estimado usuario el dia de hoy")//tomarFechaSistemaII(1)
                    .line("Fecha    : " + tomarFechaSistemaII(1))//tomarFechaSistemaII(1)
                    .line("Estuvimos realizando la toma de lectura con reporte:")//tomarFechaSistemaII(1)
                    .line("Lectura  : " + parseStringToInteger(String.format("%1$9s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")))//infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA())
                    .lineaEnvuelta("OBS: " + LecturaTomada, 42) // Ax: LecturaTomada puede venir hasta de 250 chars (multi-subcausa)
                    .line("Cuenta   : " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim())//infoRegistroSalida.gettablaRegistroSalida_CUENTA()
                    .line("Municipio: " + Municipio.trim())                 //infoRegistroSalida.gettablaRegistroSalida_CUENTA()
                    .line("Nombre   : " + Nombre1)//
                    .line("Direccion: " + Direccion1)//
                    .line("Medidor  : " + infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()+" - Marca: " +infoRegistroSalida.gettablaRegistroSalida_MARCA().trim())///infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR())
                    .line("Inspector: " + infoRegistroSalida.gettablaRegistroSalida_LECTOR())//consumo""
                    .line("PARA MAYOR INFORMACION COMUNIQUESE A LA ")
                    .line("LINEA WHATSAPP 3102305947")//consumo""
                    .line("Proceso de Lectura elaborado por INELMA ")
                    .line("SAS")
                    .line("version " + VariablesGlobales.versionApp)
                    .feed(4) // Ax: era feed(6); si el cortador toca texto, subir de nuevo
                    .cut();
        }
        File file = new File(
                VariablesGlobales.directorioactual +
                        VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.BIN"
        );

        if (file.exists()) file.delete();

        byte[] datosImpresion = esc.build();
        escribirArchivoBinario(file, datosImpresion);

        if (!VariablesGlobales.tipoDeRuta.equals("E")) {
            imprimirBinarioNuevoModelo(datosImpresion); // Ax: reemplaza imprimirESC() - ver MODELO NUEVO DE IMPRESIÓN al final
        }
    }


    void escribirArchivoBinario(File file, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            mensajeT("Error guardando archivo impresión", 5000);
        }
    }

    int imprimirESC() {

        File file = new File(
                VariablesGlobales.directorioactual +
                        VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.BIN"
        );

        if (!file.exists()) {
            mensajeT("No existe archivo de impresión", 3000);
            return 0;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            Enviar_al_puertoESC(buffer);
            //infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("1");
            return 1;

        } catch (Exception e) {
            //infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("0");
            mensajeT("Error enviando impresión", 5000);
            return 0;
        }
    }


    private void Enviar_al_puertoESC(byte[] data) {
        try {
            VariablesGlobales.btPrintService.write(data);
        } catch (Exception e) {
            mensajeT("Problemas enviando a la impresora", 5000);
        }
    }


    void EscribaArchivoImpresion_Visita(String Texto) {
        String claseServicio = infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO().trim().toUpperCase();

        switch (claseServicio) {

            case "FM":

                // lógica FM
                claseServicio += " Residencial";
                break;

            case "CR":
                claseServicio += " Comercial";
                // lógica CO
                break;

            case "ID":
                claseServicio += " Industrial";
                // lógica IN
                break;

            case "OF":
                claseServicio += " Oficial";
                // lógica OF
                break;

            case "MM":
            case "MO":
                claseServicio += " Macro Med";
                // lógica OF
                break;
            default:
                claseServicio += ", Otro";
                // otros
                break;
        }

        int TipoPaginaImpresion = 2;
        //proceso si fue facturado deberia impresar el proceso de creacion de archivos donde creo que cuando manda a procesar la impresion
        double consumo_ = parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        if ((parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) < parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"))) && parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) > 0)
            consumo_ = 100000 + parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim()).replace(" ", "0")) - parseStringToDouble(String.format("%1$8s", infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim()).replace(" ", "0"));

        int consumo = (int) consumo_;
        String texto = "";

        texto += "! 0 200 200 670 1" + "\r\n";
        texto += "LABEL" + "\r\n";
        texto += "CONTRAST 200" + "\r\n";
        texto += "TONE 20" + "\r\n";
        texto += "SPEED 20" + "\r\n";
        texto += "POSTFEED 0" + "\r\n";
        texto += "PAGE - WIDTH 816" + "\r\n";
        texto += "BAR - SENSE" + "\r\n";
        texto += "PCX 7 1 !<VISITA.PCX" + "\r\n";
        texto += "T 7 0 570 155 " + claseServicio + "\r\n";
        texto += "T 5 1 590  20 " + infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim() + "\r\n";
        texto += "T 7 0 70  120 " + infoRegistroSalida.gettablaRegistroSalida_NOMBRE().substring(0, 22) + "                            "
                + infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().substring(4, 6) + "-" +
                infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().substring(0, 4) + "\r\n";
        texto += "T 7 0 70  155 " + infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(0, 20) + "\r\n";
        texto += "T 7 0 20  240 " + infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA() + "\r\n";//lectura tomada
        texto += "T 7 0 220 240 " + infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA() + "\r\n";
        texto += "T 7 0 420 240 " + infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim() + "\r\n";
        texto += "T 7 0 550 240 " + infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO().trim() + "              " + infoRegistroSalida.gettablaRegistroSalida_IMPRESORA() + "\r\n";//faltaria descripcion de municipo
        texto += "T 7 0 720 240 " + infoRegistroSalida.gettablaRegistroSalida_CODCATEGORIA() + "\r\n";//estrato
        texto += "T 7 0 20  300 " + infoRegistroSalida.gettablaRegistroSalida_CODESTADO() + "\r\n";//.lectura verifuicada"
        texto += "T 7 0 320 300 " + infoRegistroSalida.gettablaRegistroSalida_OBSERANT() + "\r\n";//anomalia verifuicada"
        texto += "T 7 0 20  355 " + infoRegistroSalida.gettablaRegistroSalida_INFORME() + "\r\n";//deberia ser varias lineas
        texto += "T 7 0 20  480 " + infoRegistroSalida.gettablaRegistroSalida_LECTOR() + "\r\n";//deberia ser el nombre del lector
        texto += "T 0 0 30  580 Version "+ VariablesGlobales.versionApp +" Lect .11006508448  - ZQ - 521" + "\r\n";
        texto += "T 7 0 180  545 " + tomarFechaSistemaII(1) + "\r\n";
        texto += "PRINT" + "\r\n";

        File fileName = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.TXT");

        if (fileName.exists())
            fileName.delete();

        utils.EscribirLinea(fileName, texto);

        if (!VariablesGlobales.tipoDeRuta.equals("E")) {//EsEntregas != "SI")
            imprimirTextoNuevoModelo(texto); // Ax: reemplaza imprimirF2() - ver MODELO NUEVO DE IMPRESIÓN al final
        }

    }

    // ==========================================================================
    // LEGADO - ya sin llamadas desde ningún lado (los 3 constructores de
    // impresión ahora usan imprimirTextoNuevoModelo/imprimirBinarioNuevoModelo,
    // ver MODELO NUEVO DE IMPRESIÓN al final de la clase). Se deja intacto por
    // trazabilidad; verificar con grep en todo el proyecto antes de borrar
    // (imprimirF2/Enviar_al_puerto/imprimirESC/Enviar_al_puertoESC son
    // package-private, en teoría solo alcanzables desde esta misma clase).
    // ==========================================================================

    int imprimirF2() {
        String imprimira = "";
        //String fileName = "";

        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/IMPRIMIR.TXT");

        if (!file.exists()) {
            mensajeT("No existe el archivo de Impresion" + file.getName(), msgLargo);
            return (0);
        }
        try {
            FileReader leerImp = new FileReader(file);
            BufferedReader reader = new BufferedReader(leerImp);
            String linea;
            imprimira = "";

            //int PrimerEnvio = 0;
            while ((linea = reader.readLine()) != null) {

                imprimira += linea + "\r\n";
            }
            reader.close();
            Enviar_al_puerto(imprimira);
            infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("1");
        } catch (Exception ex) {
            tiempoInactivolaImpresora = 0;
            infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES("0");
            mensajeT("Error en envio de Impresion de la Factura", msgLargo);
        }
        return (1);
    }

    private void Enviar_al_puerto(String imprime) {

        int i = 0;
        try {
            String outputData = "";

            byte[] outputData2 = new byte[1];
            outputData2[0] = (byte) 134;
            String serialPort1_PortName = ""; //Ax: inventado

            //nuevo proceso si se imprime por el serial
            if (serialPort1_PortName != "COM1") { //serialPort1.PortName
                if (imprime.length() > 0) {
                    outputData = imprime;
                    VariablesGlobales.btPrintService.write(outputData.getBytes());// serialPort1.Write(outputData);
                }
            } else {
                int tamaño = imprime.length();
                String trozo = "";
                //MessageBox.Show("Trama de:"+tamaño.ToString()+".."+imprime);
                //envio del encavesado del sistema de facturacion
                for (i = 0; i <= imprime.length(); ) {
                    if (tamaño >= 35)
                        trozo = imprime.substring(i, 35);
                    else
                        trozo = imprime.substring(i, tamaño);
                    outputData = trozo;

                    tamaño -= 35;
                    i = i + 35;
                    VariablesGlobales.btPrintService.write(outputData.getBytes());//Ax: antes serialPort1.Write
                }
                if (tamaño > 0) {
                    i = i - 35;
                    outputData = imprime.substring(i, tamaño);
                    VariablesGlobales.btPrintService.write(outputData.getBytes());//Ax: antes serialPort1.Write
                }
            }

        } catch (Exception ex) {
            tiempoInactivolaImpresora = 0;
            mensajeT("Problemas enviando a la impresora", msgLargo);
        }
    }

    private String DescripAnomaliaDeNoLectura(String cod) {

        String desc = "";

        try {
            if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
                anomaliaDeLectura.lectura_AnomaliaDeNoLectura(1);
                anomaliaDeLectura.buscarAnomaliaDeNoLectura(cod);

                if (anomaliaDeLectura.getEncontro_AnomaliaDeNoLectura() > 0) {
                    desc = anomaliaDeLectura.getanomaliaDeNoLectura_DESCRIPCION().trim();
                }
                anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();
            }
        } catch (Exception ex) {
            //utils.Log(logfile, "[MenuDeLiquidacion]DescripAnomaliaDeNoLectura(); " + ex.getMessage());
            Log.e("Error Pantalla", "[MenuDeLiquidacion]DescripAnomaliaDeNoLectura(); " + String.valueOf(ex));
        }
        return desc;
    }

    private String DescripcionMunicipio(String cod) {

        String desc = cod + "...";

        try {
            TablaMunicipio.setArchivo_TablaMunicipios(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/MUNICIPIOS.TXT");
            File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas() + "/MUNICIPIOS.TXT");

            if (file.exists()) {

                if (TablaMunicipio.abrir_TablaMunicipios(TablaMunicipio.getArchivo_TablaMunicipios())) {
                    TablaMunicipio.lectura_TablaMunicipios(1);
                    TablaMunicipio.buscarSecuencial_TablaMunicipios(cod);

                    if (TablaMunicipio.getencontro_TablaMunicipios() > 0) {
                        desc = TablaMunicipio.gettablaMunicipios_DESCRIPCION().trim();
                    } else {
                        desc = cod + "...";
                    }
                    TablaMunicipio.Cerrar_TablaMunicipios();

                }
            }
        } catch (Exception ex) {
            //utils.Log(log, "[MenuDeLiquidacion]DescripAnomaliaDeNoLectura(); " + ex.getMessage());
            Log.e("Error Pantalla", "[MenuDeLiquidacion]DescripcionMunicipio(); " + String.valueOf(ex));
        }
        return desc;
    }

    private String tomarFechaSistemaII(int formato) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = null;

        switch (formato) {
            case 1:
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case 2:
                df = new SimpleDateFormat("ddMMyyyy");
                break;
            case 3:
                df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                break;
            case 4:
                df = new SimpleDateFormat("yyyyMMdd");
                break;
            default:
                df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                break;
        }
        return df.format(c.getTime());
    }

    //**FIN DEL PROCESO DE IMPRESION
  /*  public boolean fechaAutomaticaActiva(Context context) {

        try {

            return Settings.Global.getInt(
                    context.getContentResolver(),
                    Settings.Global.AUTO_TIME
            ) == 1;

        } catch (Exception e) {

            return false;
        }
    }*/
    private boolean fechaAutomaticaActiva(Context context) {

        boolean autoFechaRed = false;
        boolean autoFechaGps = false;
        boolean autoZonaHoraria = false;

        try {

            autoFechaRed =
                    android.provider.Settings.Global.getInt(
                            getContentResolver(),
                            android.provider.Settings.Global.AUTO_TIME,
                            0) == 1;

        } catch (Exception e) {
        }

        try {

            autoFechaGps =
                    android.provider.Settings.Global.getInt(
                            getContentResolver(),
                            "auto_time_gps",
                            0) == 1;

        } catch (Exception e) {
        }

        try {

            autoZonaHoraria =
                    android.provider.Settings.Global.getInt(
                            getContentResolver(),
                            android.provider.Settings.Global.AUTO_TIME_ZONE,
                            0) == 1;

        } catch (Exception e) {
        }

        // Si cualquiera está activa → válido

        return autoFechaRed
                || autoFechaGps
                || autoZonaHoraria;
    }

    private void VerMensajeria() {
        String mensaje2 = "";
        try {
            //final String mensaje1;
            // ✅ Mensaje definido correctamente
            String mensaje1 = "📡 ALERTA X MAL USO\n\n" +
                    "• SU MOVIL TIENE APAGADO LOS AUTOMATICOS\n" +
                    "• PARA EVITAR RIESGOS DE DAÑO EN DATOS\n\n" +
                    "• REPORTESE CON EL SUPERVISOR\n" +
                    "ℹ️ INDICANDO ESTO:\n\n\n" +
                    "- Su información movil fue alterada.\n" +
                    "- o por mal  manejo de la herramienta apago referensias del sistema.\n" +
                    "\n📌 SOPORTE:\n" +
                    "En caso de inconsistencias, comuníquese con su supervisor o el área técnica.\n" +
                    "Sistema OKINNOM READINGS - Gestión de Lecturas Móviles.** ";


            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje1)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();

            // ✅ cierre correcto del lambda
        } catch (Exception ex) {
            mensaje2 =
                    "📡 ESTADO DE MANIPULACION RELOJ ACTIVADO..\n\n" +
                            "✔ Información NO SE PROCESA.\n" +
                            "📅 sin CONTROL DEL RELOJ: " + "Hay un error" + "\n\n" +
                            "ℹ️ El sistema no esta listo para Ejecucion.";//obtenerFechaActual()
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje2)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();
        }
    }

    private boolean ubicacionHabilitada() {

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android 9 en adelante
            return locationManager.isLocationEnabled();
        } else {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
    }
    private boolean validarGPS() {

        if (!ubicacionHabilitada()) {

            new AlertDialog.Builder(this)
                    .setTitle("Ubicación desactivada")
                    .setMessage("Para utilizar esta aplicación debe activar la ubicación del dispositivo.")
                    .setCancelable(false)
                    .setPositiveButton("Activar ubicación", (dialog, which) -> {

                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        );

                        startActivity(intent);
                    })
                    .setNegativeButton("Salir", (dialog, which) -> {
                        finish();
                    })
                    .show();

            return false;
        }

        return true;
    }

    private void botonesAzul() {

        int azul = ContextCompat.getColor(this, R.color.bluePopHard);
        int blanco = ContextCompat.getColor(this, android.R.color.white);

        int[] ids = {
                R.id.Boton1,
                R.id.Boton2,
                R.id.Boton3,
                R.id.Boton4,
                R.id.Boton5,
                R.id.Boton6,
                R.id.Boton7,
                R.id.Boton8,
                R.id.Boton9,
                R.id.Boton0,
                R.id.BotonDel,
                R.id.BotonOk
        };

        for (int id : ids) {

            MaterialCardView boton = findViewById(id);

            // Fondo azul
            boton.setCardBackgroundColor(azul);

            // Cambiar los elementos internos
            for (int i = 0; i < boton.getChildCount(); i++) {

                View vista = boton.getChildAt(i);

                if (vista instanceof TextView) {
                    ((TextView) vista).setTextColor(blanco);
                }

                if (vista instanceof ImageView) {
                    ((ImageView) vista).setColorFilter(blanco);
                }
            }
        }
    }
    private void botonesNegro() {

        int negro = ContextCompat.getColor(this, android.R.color.black);
        int blanco = ContextCompat.getColor(this, android.R.color.white);

        int[] ids = {
                R.id.Boton1,
                R.id.Boton2,
                R.id.Boton3,
                R.id.Boton4,
                R.id.Boton5,
                R.id.Boton6,
                R.id.Boton7,
                R.id.Boton8,
                R.id.Boton9,
                R.id.Boton0,
                R.id.BotonDel,
                R.id.BotonOk
        };

        for (int id : ids) {

            MaterialCardView boton = findViewById(id);

            // Fondo blanco
            boton.setCardBackgroundColor(blanco);

            // Cambiar los elementos internos
            for (int i = 0; i < boton.getChildCount(); i++) {

                View vista = boton.getChildAt(i);

                if (vista instanceof TextView) {
                    ((TextView) vista).setTextColor(negro);
                }

                if (vista instanceof ImageView) {
                    ((ImageView) vista).setColorFilter(negro);
                }
            }
        }
    }

    // ==========================================================================
    // MODELO NUEVO DE IMPRESIÓN (stateless, sin archivo intermedio ni servicio
    // Bluetooth persistente). Reemplaza el envío de imprimirF2()/imprimirESC()
    // (ver sección LEGADO más arriba). Usa Bluetooth_Printer + la MAC guardada
    // por GuiAcceso en Bluetooth_SharedPreferences.
    //
    // Los 3 constructores de ticket (EscribaArchivoImpresion, ESC y _Visita)
    // siguen generando el contenido igual que antes (formato EPL para ZEBRA,
    // ESC/POS para JAL); solo cambió el transporte final: antes se escribía a
    // disco, se releía completo y se enviaba por VariablesGlobales.btPrintService
    // (socket persistente compartido); ahora se envía directo desde memoria por
    // un socket que se abre, escribe y cierra en cada impresión.
    //
    // La escritura a IMPRIMIR.TXT/IMPRIMIR.BIN se conserva únicamente como
    // registro de diagnóstico (para soporte en campo); ya no se vuelve a leer.
    // ==========================================================================

    /**
     * Envía texto plano (formato EPL, impresoras marca ZEBRA) directo por BT.
     * Requiere que el operario ya haya seleccionado impresora desde GuiAcceso.
     */
    private void imprimirTextoNuevoModelo(String texto) {
        enviarPorNuevoModelo(cb -> nuevoImpresor.print(obtenerMacImpresoraGuardada(), texto, cb));
    }

    /**
     * Envía bytes binarios crudos (formato ESC/POS con logo, impresoras marca
     * JAL) directo por BT. NUNCA convertir a String antes de esto: corrompe
     * los bytes >127 del bitmap embebido.
     */
    private void imprimirBinarioNuevoModelo(byte[] datos) {
        enviarPorNuevoModelo(cb -> nuevoImpresor.print(obtenerMacImpresoraGuardada(), datos, cb));
    }

    /**
     * Ax: punto único de validación + logging para ambas variantes (texto y
     * binario). Recibe la llamada a Bluetooth_Printer ya armada (functional
     * interface) para no duplicar el chequeo de permiso/MAC ni el manejo de
     * callback en cada variante.
     */
    private void enviarPorNuevoModelo(java.util.function.Consumer<Bluetooth_Printer.Callback> accionEnvio) {

        if (!Bluetooth_Permission.check(this)) {
            mensajeT("Falta permiso de Bluetooth. Actívelo en Ajustes de la app.", msgLargo);
            logger.warn("[IMPRESION] Intento de impresión sin permiso de Bluetooth concedido.");
            return;
        }

        String mac = obtenerMacImpresoraGuardada();
        if (mac == null || mac.isEmpty()) {
            mensajeT("No hay impresora seleccionada. Vaya a Pantalla de Acceso y elija una.", msgLargo);
            logger.warn("[IMPRESION] Intento de impresión sin MAC de impresora guardada.");
            return;
        }

        accionEnvio.accept(new Bluetooth_Printer.Callback() {
            @Override
            public void success() {
                runOnUiThread(() -> logger.info("[IMPRESION] Ticket enviado correctamente a " + mac));
            }

            @Override
            public void error(Exception e) {
                // Ax: si la impresora guardada ya no está vinculada, se limpia
                // la MAC (igual que en GuiAcceso) para forzar nueva selección.
                if (e.getMessage() != null && e.getMessage().contains("vinculada")) {
                    Bluetooth_SharedPreferences.clear(MenuDeLiquidacion.this);
                }
                runOnUiThread(() -> {
                    mensajeT("Error de impresión: " + e.getMessage(), msgLargo);
                    logger.error("[IMPRESION] Error enviando a " + mac + " | " + e.getMessage(), e);
                });
            }
        });
    }

    private String obtenerMacImpresoraGuardada() {
        return Bluetooth_SharedPreferences.get(this);
    }

    /**
     * Ax: reemplaza el click de imagenPrinter que abría DeviceListActivity
     * (escaneo/emparejamiento en vivo, flujo legado). Lista solo impresoras ya
     * vinculadas por Android y guarda la MAC elegida - igual que en GuiAcceso.
     *
     * IMPORTANTE: VariablesGlobales.habilitadaimpresora antes solo lo escribía
     * el servicio BT persistente (conectado/desconectado). Con el modelo nuevo
     * no hay conexión persistente que "esté conectada", así que aquí se
     * redefine su semántica a "hay una impresora configurada" - se pone en 1
     * al guardar una MAC y en 0 al olvidarla. Esto también controla el ícono
     * (ver visualizarInformacionCliente) y los bloqueos de "IMPRESORA NO
     * VINCULADA" en capturarAnomaliaTerreno()/procesarLectura(), que ahora
     * reflejan "impresora configurada" en vez de "conectada en este instante".
     */
    @SuppressLint("MissingPermission")
    private void seleccionarImpresoraNuevoModelo() {
        if (!Bluetooth_Permission.check(this)) {
            mensajeT("Falta permiso de Bluetooth. Actívelo en Ajustes de la app.", msgLargo);
            return;
        }

        List<BluetoothDevice> dispositivos = Bluetooth_Printer.getBondedDevices();

        if (dispositivos.isEmpty()) {
            mensajeT("No hay impresoras vinculadas. Vincúlela en Ajustes > Bluetooth.", msgLargo);
            return;
        }

        if (dispositivos.size() == 1) {
            guardarMacImpresoraNuevoModelo(dispositivos.get(0).getAddress());
            mensajeT("Impresora vinculada: " + dispositivos.get(0).getAddress(), msgMedio);
            return;
        }

        mostrarDialogoSeleccionImpresoraNuevoModelo(dispositivos);
    }

    @SuppressLint("MissingPermission")
    private void mostrarDialogoSeleccionImpresoraNuevoModelo(List<BluetoothDevice> dispositivos) {

        String[] items = new String[dispositivos.size()];
        for (int i = 0; i < dispositivos.size(); i++) {
            BluetoothDevice d = dispositivos.get(i);
            String nombre = d.getName();
            items[i] = (nombre != null ? nombre : "Dispositivo desconocido") + "\n" + d.getAddress();
        }

        final int[] seleccionado = {-1};

        new AlertDialog.Builder(this)
                .setTitle("Seleccione la impresora")
                .setCancelable(false)
                .setSingleChoiceItems(items, -1, (dialog, which) -> seleccionado[0] = which)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    if (seleccionado[0] < 0) {
                        mensajeT("Seleccione una impresora", msgMedio);
                        return;
                    }
                    String mac = dispositivos.get(seleccionado[0]).getAddress();
                    guardarMacImpresoraNuevoModelo(mac);
                    mensajeT("Impresora guardada:\n" + items[seleccionado[0]], msgMedio);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Ax: BRIDGE temporal - misma MAC en Bluetooth_SharedPreferences (nuevo,
     * fuente de verdad para imprimir) y en logPrint/PRINTER.LOG (legado, aún
     * leído por código no migrado). Quitar la escritura a logPrint solo
     * cuando se confirme que nada más en el proyecto lo lee.
     */
    private void guardarMacImpresoraNuevoModelo(String mac) {
        Bluetooth_SharedPreferences.save(this, mac);
        try {
            utils.WriteLine(logPrint, mac);
        } catch (Exception ex) {
            logger.error("[IMPRESION] guardarMacImpresoraNuevoModelo() " + ex.getMessage(), ex);
        }
        VariablesGlobales.habilitadaimpresora = 1;
        imagenPrinter.setImageResource(R.drawable.impresora);
    }

    private void olvidarImpresoraNuevoModelo() {
        String macActual = obtenerMacImpresoraGuardada();
        if (macActual == null) {
            mensajeT("No hay impresora asociada actualmente", msgMedio);
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Olvidar impresora")
                .setMessage("¿Desea olvidar la impresora asociada?\n" + macActual
                        + "\n\nDeberá seleccionar una nueva antes de imprimir.")
                .setCancelable(true)
                .setPositiveButton("Olvidar", (dialog, which) -> {
                    Bluetooth_SharedPreferences.clear(this);
                    try {
                        utils.WriteLine(logPrint, "");
                    } catch (Exception ex) {
                        logger.error("[IMPRESION] olvidarImpresoraNuevoModelo() " + ex.getMessage(), ex);
                    }
                    VariablesGlobales.habilitadaimpresora = 0;
                    imagenPrinter.setImageResource(R.drawable.imagen_prin_apagada);
                    mensajeT("MAC eliminada. Seleccione impresora antes de imprimir.", msgMedio);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}