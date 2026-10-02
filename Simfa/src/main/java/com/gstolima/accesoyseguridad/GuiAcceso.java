package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.Util.Printzpl;
import com.Util.Utils;
import com.gstolima.captureException.myExceptionHandler;
import com.gstolima.comunicaciones.Comunicaciones;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.comunicaciones.CrudGeneral;

import io.realm.Realm;

import java.io.InputStreamReader;

import com.gstolima.moduloLicencia.ActLicencia;
import com.gstolima.modulobluetooth.DeviceListActivity;
import com.gstolima.modulobluetooth.btPrintFile;
import com.gstolima.modulobluetooth.msgTypes;
import com.gstolima.tablas.TablaAforadores_Lector;
import com.gstolima.tablas.TablaEncabezado_General;
import com.gstolima.tablas.ClaveRuta;
import com.gstolima.tablas.TablaRegistroSalida;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.res.ColorStateList;
import android.graphics.Color;

import com.gstolima.modulobluetooth.Bluetooth_Permission;
import com.gstolima.modulobluetooth.Bluetooth_Printer;
import com.gstolima.modulobluetooth.Bluetooth_SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class GuiAcceso extends AppCompatActivity {

    //final Logger logger = LoggerFactory.getLogger(GuiAcceso.class);
    private static final int REQUEST_SELECT_FILE = 4;
    private static final String RUTA_ACTIVA1 = "CARPETA_ACTIVA1.txt";
    private static final String RUTA_ACTIVA2   = "CARPETA_ACTIVA2.txt";
    TextView txtRemoteDevice;
    TextView txtVersion;
    TextView txtConexion;
    TextView txtCondicion;
    Button btnIngresar;
    ImageButton btnSalir;

    ImageButton btnAcercaDe;
    ImageButton btnImprime;
    ImageButton btnBluetooth;
    ImageButton btnBateria;
    ImageButton btnComunicar2;
    ImageButton btnInfo;
    String Empresa = " INELMA ";
    Boolean conn = true; //Ax: guarda estado si la impresora para permitir uso de botones
    public static String macAdress = ""; //Ax: guarda la mac actual
    public static boolean esMayor9 = true; //Indica si la version es 10 u otra
    Boolean sdDisponible;
    Boolean sdAccesoEscritura;
    TextView txtCodInterno;
    String codigoSalida = "4321";
    String claveSalida = "56789";
    String nivelOperador = "";
    String datosEncriptados;
    String archivocargado = "";
    TextView txtClave;
    TextView txtCodigo;
    TextView txtNombreUsuario;
    int aforadorClaveCorrecto = 0;
    int currentVersionCode;
    int contadorImprimir=0;
    String currentVersionName = "";
    String telephoneSerialNumber = "";

    String mensajeDeAlerta = "";
    String NombreArchivos_0;
    String RutaActiva = "";
    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
    TablaAforadores_Lector tablaAforadores = new TablaAforadores_Lector();
    TablaEncabezado_General tablaEncabezado = new TablaEncabezado_General();
    final File sdCard = Environment.getExternalStorageDirectory();

    VariablesGlobales variables = new VariablesGlobales();
    public Utils utils = new Utils();
    ImageView fotoUsuario;

//    private int mDstWidth;// Wanted width of decoded image
//
//    private int mDstHeight;//Wanted height of decoded image

    private static final String TAG = "GuiAcceso - btprint";
    File logfile; //Ax: Es para los log de error
    File logPrint; //Ax: Es para guardar la de mac de impresora bluetooth
    TextView txtFecha;

    Spinner spnPath;
    TextView txtPath; // etiqueta sobre el selector: dice la ruta activa
    private boolean actualizandoSelector = false; // true mientras el codigo llena el selector
    private static final int ACTIVITY_MENU_PRINCIPAL = 1000;
    private static final int ACTIVITY_COMUNICACIONES = 1500;
    Context ctx;
    private boolean primeraVez = true;

    // Ax: modelo de impresión nuevo (stateless, sin servicio persistente).
    // Convive con VariablesGlobales.btPrintService (legado) hasta migrar
    // también MenuDeLiquidacion; ver bridge en guardarMacImpresora().
    private final Bluetooth_Printer nuevoImpresor = new Bluetooth_Printer();

    // TODO: reemplazar por el ZPL real de prueba que usa tu compañero
    // (Bluetooth_zpl_samples, no llegó en los archivos subidos). Este es
    // un ZPL mínimo válido, sirve como prueba de conexión mientras tanto.
    private static final String ZPL_PRUEBA =
            "! 0 200 200 560 1\r\n"
                    + "JOURNAL\r\n"// LABEL
                    + "CONTRAST 0\r\n" + "TONE 0\r\n" + "SPEED 5\r\n" + "PAGE-WIDTH 780\r\n" + "NONE-SENSE\r\n" + "POSTFEED 20\r\n"
                    + ";// PAGE 0000000007800560\r\n" + "LINE 449 154 449 220 341\r\n" + "LINE 0 22 0 88 271\r\n" + "LINE 0 0 0 24 793\r\n"
                    + "LINE 189 88 189 154 341\r\n" + "LINE 0 221 0 287 793\r\n" + "\r\n"// FORM
                    + "PRINT" + "\r\n";;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_acceso);
        ctx = this;
        datosEncriptados = "DCRFVTGBYH";

        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnSalir = (ImageButton) findViewById(R.id.btnSalir);

        btnAcercaDe = (ImageButton) findViewById(R.id.btnAcercaDe);
        btnImprime = (ImageButton) findViewById(R.id.btnImprime);
        btnBluetooth = (ImageButton) findViewById(R.id.btnBluetooth);
        txtRemoteDevice = (TextView) findViewById(R.id.txtRemoteDevice);
        txtConexion = (TextView) findViewById(R.id.txtConexion);
        txtCondicion = (TextView) findViewById(R.id.txtCondicion);
        btnBateria = (ImageButton) findViewById(R.id.btnBateria);
        btnComunicar2 = (ImageButton) findViewById(R.id.btnComunicar2);
        btnInfo = (ImageButton) findViewById(R.id.btnInfo);

        spnPath = (Spinner) findViewById(R.id.spnTipoMedida);
        txtPath = (TextView) findViewById(R.id.txtPath);

        btnSalir.setEnabled(false);
        btnIngresar.setEnabled(false);
        btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        txtVersion= (TextView) findViewById(R.id.txtVerion);
        txtCodInterno = (TextView) findViewById(R.id.txtCodInterno);
        txtCodigo = (TextView) findViewById(R.id.txtUsername);
        txtClave = (TextView) findViewById(R.id.txtPassword);
        txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuario);

        txtCodigo.setText("");//4056 //0026
        txtClave.setText("");//12345//
        txtClave.setEnabled(false);

        //telephoneSerialNumber = getSerialNumber();

        fotoUsuario = (ImageView) findViewById(R.id.fotoUsuario);

        try {
            PackageInfo pi = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            currentVersionCode = pi.versionCode;
            currentVersionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {
        }

        Bundle bundle = getIntent().getExtras();

        if (getIntent().getStringExtra("valida") != null) { // kim validacion de licencia

            telephoneSerialNumber = bundle.getString("PhoneImei");

        } else {
            esMayor9 = true;
            Bundle bundlex = new Bundle();
            bundlex.putString("telephoneSerialNumber", telephoneSerialNumber);
            bundlex.putBoolean("esMayor9", esMayor9);
            Intent miIntent = new Intent();
            miIntent.setClass(this, ActLicencia.class);
            miIntent.putExtras(bundlex);
            startActivity(miIntent);
            finish();
        }
        txtCodInterno.setText(telephoneSerialNumber);

        cargarImagenUsuario("aforador");//Si en la carpeta aforador existe esta plantilla, la carga

        txtFecha = (TextView) findViewById(R.id.txtFecha);

        testSD();
        File dir;

        configurarSelectorRutas();
        SacarClaveEncabezado();

        if (sdDisponible && sdAccesoEscritura && Environment.isExternalStorageManager()) {


            final Logger logger = LoggerFactory.getLogger(MenuDeLiquidacion.class);

            if (getIntent().getBooleanExtra("L", false)) {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                finish();
            }

            Thread myThread = null;

            Runnable myRunnableThread = new Reloj();
            myThread = new Thread(myRunnableThread);
            myThread.start();

            String externalpath = ctx.getExternalFilesDir(null).getParent();
            String hardcoding = "/Android/data/";//Ax: todo: cambiar este hardcoding
            if (externalpath.contains(hardcoding)) {
                externalpath = externalpath.substring(0, externalpath.indexOf(hardcoding));
            }
            String consecutivo = "2.3.6";
            txtVersion.setText("Versión " + consecutivo);
            VariablesGlobales.directorioactual = externalpath;
            VariablesGlobales.vrsDerechos = "Septi. 24 2026. L-" + Empresa + "_GSS"; //Vrs para mostrar desde el login
            VariablesGlobales.versionApp = "Ver" + consecutivo + "-260924"; //Vrs para envio al modulo de liquidacion
            VariablesGlobales.archivolog = externalpath + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG"; //path hacia el Log
            Thread.setDefaultUncaughtExceptionHandler(new myExceptionHandler(getApplicationContext()));

            // ===== PASO 1: aplicar permiso guardado (offline) =====
            // ===== PASO 2: consultar BD remota y actualizar permiso =====
            verificarVersionApp();

            // ===== VERIFICACIÓN DE RUTA: evitar mezcla de datos =====
            verificarRutaCargada();

            //-----------------------------------------------------------------------------------------
            File directorio = new File(VariablesGlobales.directorioactual);
            String[] lista = directorio.list();

            String[] filex = {"LECTURAMEDIDORES","LECTURAMEDIDORES2", "FOTOGRAFIASL","FOTOGRAFIASL2", "AFORADORESL", "DIRECCIONLIP.TXT", "IMPRESION.LOG", "VALORESFORMATO.LOG", "IMAGENACAPTURAR.TXT", "ARCHIVOSLCARGA.CFI"};//Son los archivos ubicados en el path

            todoMayuscula(lista, filex, directorio);

            File directorio2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas());
            String[] lista2 = directorio2.list();

            String[] files = {"LOGEVENTOS.LOG", "PRINTER.LOG", "NOMBRE", "CATEGORIASINF.TXT", "CAUSAS.TXT", "CENSO.TXT", "ESTADOS.TXT", "LECTOR.TXT", "NOTIFICACIONES.TXT", "OBSERVA.TXT", "RANGOS.TXT", "FECHADOR", "ADMINIST.TXT", "ADMINISTADORDEPDA.TXT", "ENVIOSGPRS.SDA", "FECHADOR.TXT", "LECTURAADMINISTRADA.TXT"};//Son los archivos ubicados en el path

            todoMayuscula(lista2, files, directorio2);
            //-----------------------------------------------------------------------------------------

            logfile = new File(VariablesGlobales.archivolog);
            logPrint = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/PRINTER.LOG");

            dir = new File(VariablesGlobales.getDirectorioactual() + "/FOTOGRAFIASL");
            dir.mkdirs();

            dir = new File(VariablesGlobales.getDirectorioactual() + "/AFORADORESL");
            dir.mkdirs();

            dir = new File(VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES");
            dir.mkdirs();
            //preparando el modelo para una segunda ruta
            dir = new File(VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES2");
            dir.mkdirs();
            dir = new File(VariablesGlobales.getDirectorioactual() + "/FOTOGRAFIASL2");
            dir.mkdirs();


            File file = new File(VariablesGlobales.getDirectorioactual() + "/ARCHIVOSLCARGA.CFI");

            try {
                if (file.exists())
                    file.delete();

                if (!file.exists()) {
                    file.createNewFile();

                    String ruta = "D:\\ENRUTADOR_" + "EMSA" + "\\ \r\n" +//Empresa.trim()
                            "ZIP SI                                 \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\CLIENTE.TXT \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\GENERAL.TXT \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\MEDIDOR.TXT \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\REGISTRO.TXT \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\IDCLIENTES.TXT \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\CLIENTE.SDA  \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\CO_COBRO.SDA   \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\MEDIDOR.SDA \r\n" +
                            "   Cic@@@@@@\\C!!!!!!!\\REGISTRO.SDA \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\ACTIVID.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\CAUSAS_NL.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\CLASE_SE.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\COMENTAR.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\CONVENIO.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\DES_CONC.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\DES_TARI.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\EST_CLIE.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\LECTOR.TXT  \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\MENSAJES.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\MUNICIP.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\RANGOS.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\TARIFAS.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\MARCAS.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\AFOROS.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\SUBCAUSAS.TXT \r\n" +
                            "\\Cic@@@@@@\\DatosSoporte\\IDNOVEDADES.TXT";

                    utils.WriteLine(file, ruta);
                }
            } catch (Exception ex) {
                utils.Log(logfile, "[GuiAcceso]Oncreate|ERROR -> " + ex.getMessage());
            }
        }

        btnIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (null != txtCodigo.getText() && !txtCodigo.getText().toString().trim().equals("") && (Long.parseLong(codigoSalida.trim()) == Long.parseLong(txtCodigo.getText().toString().trim())) && (null != txtClave.getText() && !txtClave.getText().toString().trim().equals("") && Long.parseLong(claveSalida.trim()) == Long.parseLong(txtClave.getText().toString().trim())) || nivelOperador.equals("S") || nivelOperador.equals("A") || aforadorClaveCorrecto == 1) {
                    SacarClaveEncabezado();
                    ingresoPrincipal();
                    txtCodigo.setText("");
                    txtClave.setText("");
                    btnSalir.setEnabled(false);
                    btnIngresar.setEnabled(false);

                    btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                } else {
                    mensajes("Acceso Denegado.", 1000);
                    txtNombreUsuario.setText("No existe el Usuario");
                }
            }
        });

        txtCodigo.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {//7900

                Log.e("error", "actionId " + actionId);
                if (actionId == 5 || actionId == 6) { //555
                    aforadorClaveCorrecto = 0;
                    SacarClaveEncabezado();
                    String codigo = txtCodigo.getText().toString().trim();

                    if (codigo.length() > 0) {

                        if (Long.parseLong(codigo) == Long.parseLong(codigoSalida)) {//se sale
                            // HardCoded by test!!!!

                            // txtClave.setText("");//12282702
                            txtClave.setEnabled(true);
                            txtClave.requestFocus();
                        } else {

                            validarCodigoUsuario();
                        }
                    }
                }
                return false;
            }
        });

        txtClave.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {

                String codigo = txtCodigo.getText().toString().trim().length() > 0 ? txtCodigo.getText().toString().trim() : "0";
                String clave = txtClave.getText().toString().trim().length() > 0 ? txtClave.getText().toString().trim() : "0";

                if (codigo.length() > 0 && clave.length() > 0) {

                    if (Long.parseLong(clave) > 0) {

//                        String claveFormateada = String.format("%12s", txtClave.getText().toString().trim());
//                        txtClave.setText(claveFormateada);

                        if ((Long.parseLong(codigoSalida.trim()) == Long.parseLong(codigo)) && (Long.parseLong(claveSalida.trim()) == Long.parseLong(clave))) {

                            // se deberia crear el archivo de administracion
                            crearArchivoAdministrado();
                            nivelOperador = "A";
                            btnIngresar.setEnabled(true);
                            btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6A00")));

                            btnSalir.setEnabled(true);

                        } else {

                            Printzpl xmlrest = new Printzpl(telephoneSerialNumber.trim());
                            try {

                                if (!xmlrest.isSerialPDA2(telephoneSerialNumber.trim())) { //Fallo la conexion con el server

                                    //AQUI SE PODRIA IMPLEMENTAR LO DEL ARCHIVO DE TERMINALES APROBADAS
                                    File file2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/TERMINALES.TXT");
                                    if (file2.exists()) {
                                        //si existe leerlo linea a linea y ver si la se puede trabajar
                                        //********************************************************
                                        String rutaf = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/TERMINALES.TXT";


                                        FileReader r = new FileReader(rutaf); //Ax: se recorre el archivo para capturar las no enviadas o en envio
                                        BufferedReader reader = new BufferedReader(r);
                                        String linea;
                                        int ExisteSerial = 0;
                                        while ((linea = reader.readLine()) != null) {

                                            String[] campoNombre = linea.split(";");
                                            //before 25
                                            if (campoNombre.length > 0) {
                                                if (telephoneSerialNumber.trim().equals(campoNombre[0])) {
                                                    ExisteSerial = 1;
                                                    break;

                                                }
                                            }

                                        }
                                        r.close();
                                        if (ExisteSerial == 0) {
                                            mostrarDialogoAlerta("Advertencia", "Dispositivo y su serial\n No autorizador..." + telephoneSerialNumber.trim());
                                            return false;
                                        }


                                        //*********************************************************
                                        //Finalizariamos si encuenta el registro de terminales

                                    } else {
                                        mostrarDialogoAlerta("Advertencia", "Dispositivo y su serial\n No autorizador..." + telephoneSerialNumber.trim());
                                        return false;
                                    }
                                }
                            } catch (Exception ex) {
                                utils.Log(logfile, "[GuiAcceso]txtClave(onEditorAction)|ERROR -> " + ex.getMessage());
                                mostrarDialogoAlerta("Advertencia", "Dispositivo comprobando Licencias\n Comunicarse con el proveedor");
                                return false;
                            }
                            //fin verificacion


                            File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LECTOR.TXT");
                            if (file.exists())
                                procesoVerificacionClave();
                            else
                                mostrarDialogoAlerta("Advertencia", "Dispositivo no tiene Archivos\n Cargados... Favor Cargar Datos");
                        }
                    } else {

                        /*btnIngresar.setEnabled(true);
                        btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6A00")));

                        btnSalir.setEnabled(true);*/
                        mostrarDialogoAlerta("Advertencia", "Clave con Valor cero\n No se permite acceso..." + telephoneSerialNumber.trim());
                        return false;
                    }

                } else {

                    btnIngresar.setEnabled(false);

                    btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnSalir.setEnabled(false);
                }
                return true;
            }
        });

        btnSalir.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String mensaje = "Sistema Movil de Lectu-Entregas \n " + "de Servicios Publicos de Energia\n Versiones de Android Homologadas: 13 a la 16 \nVersion Identtificada: " + currentVersionName + " \n" + "@-Derechos Reservados GS&S \n"
                        + "Compilada en: " + VariablesGlobales.vrsDerechos;

                mostrarDialogoAlerta("Informacion", mensaje);
            }

        });

        btnImprime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                imprimirPruebaNueva();



            }
        });

        // Get local Bluetooth adapter
        VariablesGlobales.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (VariablesGlobales.mBluetoothAdapter == null) {
            mensajes("Bluetooth no esta disponible", 1000);
            btnBluetooth.setEnabled(false);
        }

        btnBluetooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                seleccionarImpresora();
            }
        });

        // Ax: mantiene visible "olvidar impresora" sin requerir un botón nuevo en el XML.
        // Si más adelante agregan un botón dedicado, reemplazar este long-click por su click.
        btnBluetooth.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                olvidarImpresoraActual();
                return true;
            }
        });

        btnBateria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                verificarBattery();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                nombreArchivo(2);
                txtCodigo.setText("");
                txtClave.setText("");
                txtCodigo.requestFocus();
            }
        });


        btnComunicar2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                iniciarPanelComunicaciones();
            }
        });

        spnPath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (primeraVez) {
                    primeraVez = false;
                    return; // Ignora la selección automática
                }

                // Los cambios hechos por codigo (al rellenar el selector) no deben ejecutar
                // la logica de cambio de ruta: solo cuenta lo que toca el usuario.
                if (actualizandoSelector) {
                    return;
                }

                String rutaSelect = spnPath.getSelectedItem().toString().trim();

                // La opcion escogida define DOS cosas: la carpeta (1 o 2) y el modulo.
                // Las opciones "2" (LECTURAS2 / ENTREGAS2) apuntan a la carpeta 2.
                boolean carpeta2 = rutaSelect.endsWith("2");
                String moduloEscogido = rutaSelect.startsWith("ENTREGAS")
                        ? ClaveRuta.MODULO_ENTREGAS
                        : ClaveRuta.MODULO_LECTURAS;

                VariablesGlobales.moduloTrabajo = moduloEscogido;
                guardarRutaTrabajo(carpeta2 ? "RUTA2" : "RUTA1");

                mostrarModuloYCarpeta(carpeta2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (Environment.isExternalStorageManager()) {
            preNombreArchivo(0);
            validarTipoRuta();
        }
    }//end onCreate

    private void todoMayuscula(String[] lista, String[] filex, File directorio) { //Ax: esto es para telefonos tipo bv6000 u otros donde fallan los nombres y extensiones, se pasa tod0 a mayusculas
        try {

            for (int i = 0; i < lista.length; i++) {
                for (int k = 0; k < filex.length; k++) {
                    if (lista[i].trim().toUpperCase().equals(filex[k])) { //'CARPETA' == 'CARPETA'

                        if (!lista[i].trim().equals(filex[k])) { //Aqui se quita el uppercase para comparar  'CarpeTa' == 'CARPETA'

                            File file = new File(directorio.getAbsolutePath(), lista[i].trim());
                            File file2 = new File(directorio.getAbsolutePath(), "_" + filex[k]);
                            File file3 = new File(directorio.getAbsolutePath(), filex[k]);
                            file.renameTo(file3);

                            if (file.renameTo(file2)) {

                                if (file.exists()) {
                                    file.delete();
                                }

                                if (file2.exists()) {

                                    file2.renameTo(file3);

                                    if (file2.exists()) {
                                        file2.delete();
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            File directorio2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas());
//            String[] lista2 = directorio2.list();
//
//            String[] files  = {"LOGEVENTOS.LOG", "PRINTER.LOG", "NOMBRE", "CATEGORIASINF.TXT", "CAUSAS.TXT", "CENSO.TXT", "ESTADOS.TXT", "LECTOR.TXT", "NOTIFICACIONES.TXT", "OBSERVA.TXT", "RANGOS.TXT", "FECHADOR", "ADMINIST.TXT", "ADMINISTADORDEPDA.TXT", "ENVIOSGPRS.SDA", "FECHADOR.TXT", "LECTURAADMINISTRADA.TXT"};//Son los archivos ubicados en el path
//
//            for (int i = 0; i < lista2.length; i++) {
//                for (int k = 0; k < files.length; k++) {
//                    if (lista2[i].trim().toUpperCase().equals(files[k])) { //'CARPETA' == 'CARPETA'
//
//                        if (!lista2[i].trim().equals(files[k])) { //Aqui se quita el uppercase para comparar  'CarpeTa' == 'CARPETA'
//
//                            File file = new File(VariablesGlobales.directorioactual, lista2[i].trim());
//                            File file2 = new File(VariablesGlobales.directorioactual, "_" + files[k]);
//                            File file3 = new File(VariablesGlobales.directorioactual, files[k]);
//                            file.renameTo(file3);
//
//                            if (file.renameTo(file2)) {
//
//                                if (file.exists()) {
//                                    file.delete();
//                                }
//
//                                if (file2.exists()) {
//
//                                    file2.renameTo(file3);
//
//                                    if (file2.exists()) {
//                                        file2.delete();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]todoMayuscula()|ERROR -> " + ex.getMessage());
            mensajes("Hay archivos cargados con nombre en minusculas", 2000);
        }
    }

    private void cargarImagenUsuario(String codigo) {

        try {
            File imgFile = new File(VariablesGlobales.directorioactual + "/AFORADORESL/" + codigo + ".JPG");

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.fotoUsuario);

                myImage.setImageBitmap(myBitmap);

            }
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]cargarImagenUsuario()|ERROR -> " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //Ax: verifica el % de estado de la bateria
    private void verificarBattery() {

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            mensajes("BATERIA \n\n    " + "\uD83D\uDD0B" + "\n\n" + Float.toString(50.0f) + "%", 1000);
        }

        mensajes("BATERIA \n\n    " + "\uD83D\uDD0B" + "\n\n" + Float.toString(((float) level / (float) scale) * 100.0f) + "%", 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gui_acceso, menu);
        return true;
    }

    // maneja los dispositivos escaneados
    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case VariablesGlobales.REQUEST_CONNECT_DEVICE:
                addLog("onActivityResult: requestCode==REQUEST_CONNECT_DEVICE");
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    addLog("resultCode==OK");
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    String mensajedes = data.getExtras().getString(DeviceListActivity.MENSAJE_DESASOCIAR);

                    if (mensajedes.equals("SI")) {
                        Desasociar();
                    } else {
                        addLog("onActivityResult: got device=" + address);
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = VariablesGlobales.mBluetoothAdapter.getRemoteDevice(address);
                        txtRemoteDevice.setText(device.getAddress());
                        // txtConectadoCon.setVisibility(TextView.VISIBLE);
                        VariablesGlobales.printerMacAddress = device.getAddress();

                        // Attempt to connect to the device
                        addLog("onActivityResult: connecting device...");
                        // VariablesGlobales.btPrintService.connect(device);
                        connectToDevice(device);
                    }
                }
                VariablesGlobales.bDiscoveryStarted = false;
                break;
            case VariablesGlobales.REQUEST_ENABLE_BT:
                addLog("requestCode==REQUEST_ENABLE_BT");
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "onActivityResult: resultCode==OK");
                    // Bluetooth is now enabled, so set up a chat session
                    Log.i(TAG, "onActivityResult: starting setupComm()...");
                    setupComm();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "onActivityResult: BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case ACTIVITY_MENU_PRINCIPAL:
                Log.e("error", "entra al result1 ");
                if (VariablesGlobales.moduloTrabajo.equals("")) {
                    configurarSelectorRutas();
                    spnPath.setEnabled(true);
                } else {
                    renameArchivo();
                    validarTipoRuta();
                }
                break;
            case ACTIVITY_COMUNICACIONES:
                Log.e("error", "entra al result ");
                if (VariablesGlobales.moduloTrabajo.equals("")) {
                    configurarSelectorRutas();
                    spnPath.setEnabled(true);
                } else {
                    renameArchivo();
                    validarTipoRuta();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ax: si falta el permiso de Bluetooth aquí, significa que el operario lo
     * negó (o lo revocó luego) durante el flujo de ActLicencia. No se vuelve
     * a pedir desde aquí (ver Bluetooth_Permission) - se explica y se ofrece
     * ir directo a Ajustes de la app para concederlo.
     */
    private void avisarPermisoBluetoothFaltante() {
        new AlertDialog.Builder(this)
                .setTitle("Permiso de Bluetooth requerido")
                .setMessage("Esta acción requiere permiso de Bluetooth, concedido normalmente\n" +
                        "al iniciar sesión. Actívelo en Ajustes de la aplicación.")
                .setCancelable(true)
                .setPositiveButton("Ir a Ajustes", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ==========================================================================
    // MODELO NUEVO (stateless) - selección, impresión de prueba y "olvidar MAC"
    // usando Bluetooth_Printer / Bluetooth_SharedPreferences.
    // ==========================================================================

    /**
     * Lista impresoras ya vinculadas (bonded) por Android y permite escoger una.
     * A diferencia del flujo legado, NO hace discovery: la impresora debe estar
     * previamente vinculada desde Ajustes > Bluetooth de Android.
     */
    @SuppressLint("MissingPermission")
    private void seleccionarImpresora() {
        if (!Bluetooth_Permission.check(this)) {
            avisarPermisoBluetoothFaltante();
            return;
        }

        List<BluetoothDevice> dispositivos = Bluetooth_Printer.getBondedDevices();

        if (dispositivos.isEmpty()) {
            mensajes("No hay impresoras vinculadas. Vincúlela en Ajustes > Bluetooth.", 1500);
            return;
        }

        if (dispositivos.size() == 1) {
            guardarMacImpresora(dispositivos.get(0).getAddress());
            mensajes("Impresora vinculada automáticamente: " + dispositivos.get(0).getAddress(), 1200);
            return;
        }

        mostrarDialogoSeleccionImpresora(dispositivos);
    }

    @SuppressLint("MissingPermission")
    private void mostrarDialogoSeleccionImpresora(List<BluetoothDevice> dispositivos) {

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
                        mensajes("Seleccione una impresora", 1000);
                        return;
                    }
                    String mac = dispositivos.get(seleccionado[0]).getAddress();
                    guardarMacImpresora(mac);
                    mensajes("Impresora guardada:\n" + items[seleccionado[0]], 1500);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Ax: BRIDGE temporal - guarda la MAC en el nuevo mecanismo (SharedPreferences)
     * Y en el archivo legado (logPrint / PRINTER.LOG) para que MenuDeLiquidacion,
     * que todavía no está migrado, siga viendo la misma impresora seleccionada.
     * Quitar la escritura a logPrint únicamente cuando MenuDeLiquidacion también
     * use Bluetooth_SharedPreferences como única fuente de verdad.
     */
    private void guardarMacImpresora(String mac) {
        Bluetooth_SharedPreferences.save(this, mac);
        try {
            utils.WriteLine(logPrint, mac);
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]guardarMacImpresora()|ERROR -> " + ex.getMessage());
        }
    }

    private void olvidarImpresoraActual() {
        String macActual = Bluetooth_SharedPreferences.get(this);
        if (macActual == null) {
            mensajes("No hay impresora asociada actualmente", 1200);
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
                        utils.Log(logfile, "[GuiAcceso]olvidarImpresoraActual()|ERROR -> " + ex.getMessage());
                    }
                    mensajes("MAC eliminada. Se pedirá seleccionar impresora en la próxima impresión.", 1500);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @SuppressLint("MissingPermission")
    private void imprimirPruebaNueva() {
        if (!Bluetooth_Permission.check(this)) {
            avisarPermisoBluetoothFaltante();
            return;
        }

        String mac = Bluetooth_SharedPreferences.get(this);

        if (mac == null) {
            mensajes("No hay impresora seleccionada. Elija una primero.", 1200);
            seleccionarImpresora();
            return;
        }

        mensajes("Enviando prueba de impresión...", 800);

        nuevoImpresor.print(mac, ZPL_PRUEBA, new Bluetooth_Printer.Callback() {
            @Override
            public void success() {
                runOnUiThread(() -> mensajes("Impresión de prueba enviada", 1200));
            }

            @Override
            public void error(Exception e) {
                // Ax: si la impresora ya no está vinculada, se limpia la MAC guardada
                // (igual que hace MainActivity) para forzar una nueva selección.
                if (e.getMessage() != null && e.getMessage().contains("vinculada")) {
                    Bluetooth_SharedPreferences.clear(GuiAcceso.this);
                    try {
                        utils.WriteLine(logPrint, "");
                    } catch (Exception ex) {
                        utils.Log(logfile, "[GuiAcceso]imprimirPruebaNueva()|ERROR -> " + ex.getMessage());
                    }
                }
                runOnUiThread(() -> mensajes("Error de impresión: " + e.getMessage(), 2000));
            }
        });
    }

    // PASO 1 - llamar UNA SOLA VEZ para guardar el logo en la memoria NV de la
    // impresora. Después de confirmar que funcionó (ver PASO 2), esto no se
    // vuelve a necesitar salvo que cambien de impresora física.
    @SuppressLint("MissingPermission")
    private void imprimirPruebaDefinirLogoNV() {
        if (!Bluetooth_Permission.check(this)) {
            avisarPermisoBluetoothFaltante();
            return;
        }

        String mac = Bluetooth_SharedPreferences.get(this);

        if (mac == null) {
            mensajes("No hay impresora seleccionada. Elija una primero.", 1200);
            seleccionarImpresora();
            return;
        }

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_lectura);
        if (logo == null) {
            mensajes("No se pudo cargar el logo (R.drawable.ic_lectura)", 1500);
            return;
        }

        mensajes("Guardando logo en la impresora...", 800);

        byte[] comandoDefinir = new EscPosBuilder()
                .definirLogoGuardado(logo, 1)
                .build();

        nuevoImpresor.print(mac, comandoDefinir, new Bluetooth_Printer.Callback() {
            @Override
            public void success() {
                runOnUiThread(() -> mensajes("Logo guardado. Ahora pruebe con el botón de impresión NV.", 1800));
            }

            @Override
            public void error(Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("vinculada")) {
                    Bluetooth_SharedPreferences.clear(GuiAcceso.this);
                    try {
                        utils.WriteLine(logPrint, "");
                    } catch (Exception ex) {
                        utils.Log(logfile, "[GuiAcceso]imprimirPruebaDefinirLogoNV()|ERROR -> " + ex.getMessage());
                    }
                }
                runOnUiThread(() -> mensajes("Error guardando logo: " + e.getMessage(), 2000));
            }
        });
    }

    // PASO 2 - imprime usando el logo YA GUARDADO (comando corto de 4 bytes,
    // no reenvía el bitmap). Solo funciona si PASO 1 se ejecutó antes y la
    // impresora realmente soporta imagen NV - si el logo sale corrupto o no
    // sale nada aquí, esta StarPOS no soporta el comando y hay que seguir
    // usando logo(bitmap) normal en EscPosBuilder.
    @SuppressLint("MissingPermission")

    private void imprimirPruebaLogoGuardado() {
        if (!Bluetooth_Permission.check(this)) {
            avisarPermisoBluetoothFaltante();
            return;
        }

        String mac = Bluetooth_SharedPreferences.get(this);

        if (mac == null) {
            mensajes("No hay impresora seleccionada. Elija una primero.", 1200);
            seleccionarImpresora();
            return;
        }

        mensajes("Enviando prueba con logo guardado...", 800);

        byte[] datosImpresion = new EscPosBuilder()
                .center()
                .logoGuardado(1)
                .left()
                .line("Prueba de logo NV")
                .feed(2)
                .cut()
                .build();

        nuevoImpresor.print(mac, datosImpresion, new Bluetooth_Printer.Callback() {
            @Override
            public void success() {
                runOnUiThread(() -> mensajes("Impresión de prueba enviada", 1200));
            }

            @Override
            public void error(Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("vinculada")) {
                    Bluetooth_SharedPreferences.clear(GuiAcceso.this);
                    try {
                        utils.WriteLine(logPrint, "");
                    } catch (Exception ex) {
                        utils.Log(logfile, "[GuiAcceso]imprimirPruebaLogoGuardado()|ERROR -> " + ex.getMessage());
                    }
                }
                runOnUiThread(() -> mensajes("Error de impresión: " + e.getMessage(), 2000));
            }
        });
    }

    // ==========================================================================
    // LEGADO (servicio BT persistente) - ya no se invoca desde los botones tras
    // migrar btnImprime/btnBluetooth al modelo stateless (ver sección siguiente).
    // Se deja intacto porque VariablesGlobales.btPrintService todavía es usado
    // por MenuDeLiquidacion (pendiente de migrar). No borrar hasta migrar allá.
    // ==========================================================================

    void startDiscovery() {

        if (VariablesGlobales.bDiscoveryStarted)
            return;

        VariablesGlobales.bDiscoveryStarted = true;

        String conectadoa = "";

        if (VariablesGlobales.btPrintService.getState() != btPrintFile.STATE_DISCONNECTED && VariablesGlobales.btPrintService.getState() != btPrintFile.STATE_IDLE) {

            conectadoa = utils.ReadLine(logPrint);
        }

        Bundle bundle = new Bundle();
        bundle.putString("conectadoa", conectadoa);

        // Lanza DeviceListActivity para ver dispositivos y escanear
        Intent serverIntent = new Intent(this, DeviceListActivity.class);

        serverIntent.putExtras(bundle);

        startActivityForResult(serverIntent, VariablesGlobales.REQUEST_CONNECT_DEVICE);
    }

    public void addLog(String s) {
        Log.d(TAG, s);
    }

    private void Desasociar() {

        try { //Ax: esta parte trata de cerrar cualquier conexion, pero puede que no exista tal conexion
            VariablesGlobales.btPrintService.stop();
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]Desasociar()|ERROR -> " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /***
     * Trata de verificar el estatus de la impresora para ver si se conecta, anula o no hace nada
     */
    private void VerificarPreconexionImpresora() { //Aqui trata de conectarse, desde Onstart(primera vez)

        if (VariablesGlobales.bDiscoveryStarted) {
            return;
        }

        if (VariablesGlobales.btPrintService.getState() == btPrintFile.STATE_DISCONNECTED || VariablesGlobales.btPrintService.getState() == btPrintFile.STATE_IDLE || VariablesGlobales.habilitadaimpresora == 0) {
            conexionForzadaImpresora();
            return;
        } else if (VariablesGlobales.btPrintService.getState() == btPrintFile.STATE_CONNECTING) {
            return;
        }

        if (!VariablesGlobales.prueba_impresora()) { //Prueba de impresora envia un caracter al la impresora y devuelve falso si falla

            try { //Ax: esta parte trata de cerrar cualquier conexion, pero puede que no exista tal conexion
                VariablesGlobales.btPrintService.stop();
                //setConnectState(btPrintFile.STATE_DISCONNECTED);
            } catch (Exception ex) {
                utils.Log(logfile, "[GuiAcceso]VerificarPreconexionImpresora()|ERROR -> " + ex.getMessage());
                ex.printStackTrace();
            }
            conexionForzadaImpresora();
        }
    }

    /***
     * Ax: trata de conectar a la impresora, obteniendo la mac guardada en archivo log
     */
    private void conexionForzadaImpresora() {

        String printer = utils.ReadLine(logPrint);

        if (!printer.equals("")) { //Ax: hay algun texto en el log

            try {
                conn = false;
                BluetoothDevice device = VariablesGlobales.mBluetoothAdapter.getRemoteDevice(printer);
                VariablesGlobales.printerMacAddress = device.getAddress();
                connectToDevice(device);

            } catch (Exception ex) {
                conn = true;
                utils.Log(logfile, "[GuiAcceso] conexionForzadaImpresora()" + ex.getMessage());
                VariablesGlobales.bDiscoveryStarted = false;
                txtRemoteDevice.setText("Error de conexion impresora");
            }
        } else {
            if (VariablesGlobales.btPrintService.getState() == btPrintFile.STATE_DISCONNECTED || VariablesGlobales.btPrintService.getState() == btPrintFile.STATE_IDLE || VariablesGlobales.habilitadaimpresora == 0) {
                conn = true;
            }
            txtRemoteDevice.setText("No hay Impresora asociada");
            mensajes("No hay Impresora asociada", 1000);
        }
    }

    // Validar el usuario programado para la lectura
    private void validarCodigoUsuario() {

        String fecha = getPhoneDate();

        try {
            if (Integer.parseInt(fecha.trim()) < 20170901) {
                mensajes("Fecha del sistema desactualizada: ", 1000);
                File fechador = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/FECHADOR.TXT");
                if (fechador.exists()) {

                    fechador.delete();
                }
            }
            File archivoGeneral = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/LECTOR.TXT");
            if (archivoGeneral.exists()) {

                nombreArchivo(1);

                tablaEncabezado.setarchivo_General(variables.getNombregeneral());
                tablaAforadores.setarchivo_Lector(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/LECTOR.TXT");

                String temp;

                infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + NombreArchivos_0);

                if (tablaEncabezado.abrir_General(tablaEncabezado.getarchivo_General()) && infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida())) {

                    tablaEncabezado.lectura_General(1);
                    VariablesGlobales.setAdministrador("912");
                    ;//912 tablaEncabezado.gettablaEncabezado_ADMINISTRADOR
                    // VariablesGlobales.setTotalimpresiones(tablaEncabezado.);
                    VariablesGlobales.setConsumoauditoria("100");//100 gettablaEncabezado_TIEMPOAUDITORIA()
                    VariablesGlobales.setDistanciagps(tablaEncabezado.getGeneral_VARIOS());//100 gettablaEncabezado_DISTANCIAGPS()
                    VariablesGlobales.setNrodias("5");//5 gettablaEncabezado_NRODIAS(
                    VariablesGlobales.setFechainicial("01/01/2017");//01/01/2017 gettablaEncabezado_FECHAINICIAL
                    VariablesGlobales.setFechafinal("01/01/2017");//01/01/2017 gettablaEncabezado_FECHAFINAL
                    VariablesGlobales.setObligafotos("0");//0 gettablaEncabezado_OBLIGAFOTOS
                    VariablesGlobales.setObligabarras("0");//tablaEncabezado.getGeneral_OBLIGACBARRAS());
                    VariablesGlobales.setMaximoregaenviar("20");//15 gettablaEncabezado_MAXIMOREGAENVIAR(
                    VariablesGlobales.setObligaFirma(tablaEncabezado.getGeneral_obligaFirma());
                    //variable nueva
                    if (tablaEncabezado.getGeneral_SECCION().trim().equals("DUP")) {
                        VariablesGlobales.setRutaDuplicada(true);
                    } else {
                        VariablesGlobales.setRutaDuplicada(false);
                    }

                    infoRegistroSalida.lectura_TablaRegistroSalida(1);
                    temp = infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().trim();

                    if (temp.length() > 0) {
                        temp = temp.substring(0, 1);
                    }

                    VariablesGlobales.setTipoDeRuta(temp);//L gettablaEncabezado_TIPORUTA(
                    VariablesGlobales.setMaximovalorentrega("100");//100 gettablaEncabezado_VALORENTREGA(
                    VariablesGlobales.setMinimovalorentrega("100");//100 gettablaEncabezado_VALORMINENTREGA()
                    VariablesGlobales.setActivar_validar_horario("0");//0 getTablaEncabezado_general_obliga_hora_inicial(

                    infoRegistroSalida.Cerrar_TablaRegistroSalida();

                    if (VariablesGlobales.getMinimovalorentrega() == null) {
                        VariablesGlobales.setMinimovalorentrega("0");
                    }

                    File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");

                    if (!archivoNombre.exists()) {

                        try {
                            RandomAccessFile rFile = new RandomAccessFile(archivoNombre, "rw");

                            String generalDpto = "073";//073 gettablaEncabezado_DPTO(
                            String.format("%+3s", generalDpto);
                            String generalMunicipio = "001";//001 gettablaEncabezado_MUNICIPIO
                            String.format("%+3s", generalMunicipio);

                            rFile.writeBytes("!!!!C" + generalDpto + generalMunicipio + "??     .");
                            rFile.close();
                            nombreArchivo(1);

                        } catch (Exception ex) {
                            utils.Log(logfile, "[GuiAcceso]validarCodigoUsuario(archivoNombre.exists())|ERROR -> " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }

                    if (!tablaAforadores.abrir_Lector(tablaAforadores.getarchivo_Lector())) {
                        mensajes("No existe Archivo LECTOR.TXT...\nCargue Datos...", 1000);
                        tablaEncabezado.Cerrar_General();
                        return;
                    }

                    tablaAforadores.buscarSecuencial_Lector(txtCodigo.getText().toString().trim());

                    if (tablaAforadores.getencontro_Lector() > 0) {

                        if (!VariablesGlobales.tipoDeRuta.trim().equals("E")) {

                            if (tablaAforadores.getLector_ESTADO().equals("L")) {

                                String CodigoDigitado =txtCodigo.getText().toString();
                                String CodigoProgramado =tablaEncabezado.getGeneral_LECTOR().trim();
                                if (Long.parseLong(CodigoDigitado) != Long.parseLong(CodigoProgramado)) {
                                    mensajes("Lector No Programado Intente con Otros", 1000);
                                    tablaEncabezado.Cerrar_General();
                                    tablaAforadores.Cerrar_Lector();
                                    txtCodigo.setText("");
                                    return;
                                }
                            }
                        }
                        // aqui nos damos cuenta que si existe el operador en la base de datos controlar el que esta realmente esta programado para la ruta
                        nivelOperador = tablaAforadores.getLector_ESTADO().trim();
                        txtNombreUsuario.setText(tablaAforadores.getLector_DESCRIPCION().trim());
                        VariablesGlobales.setNombreLectorPDA(tablaAforadores.getLector_DESCRIPCION());
                        txtClave.setEnabled(true);
                        txtClave.requestFocus();

                    } else {

                        nivelOperador = "";
                        txtClave.setEnabled(false);
                        txtNombreUsuario.setText("Aforador/Super.. No Existe");
                        Toast.makeText(getApplicationContext(), "LECTOR PROGRAMADO \nNO EXISTE..." + txtCodigo.getText(), Toast.LENGTH_LONG).show();
                        txtCodigo.setText("");
                        txtClave.setText("");
                        txtCodigo.requestFocus();
                    }
                    tablaEncabezado.Cerrar_General();
                    tablaAforadores.Cerrar_Lector();
                } else {

                    Toast.makeText(getApplicationContext(), "No pudo leer archivo " + tablaEncabezado.getarchivo_General(), Toast.LENGTH_LONG).show();
                    txtCodigo.setText("");
                    txtClave.setText("");
                    tablaEncabezado.Cerrar_General();
                }
            } else {
                mostrarDialogoAlerta("Advertencia", "Dispositivo no tiene Archivos\n Cargados... Favor Cargar Datos");
                txtClave.setText("12282702");
                txtClave.setEnabled(true);
            }
        } catch (Exception ex) {
            Log.e("error", "datos error " + ex.getMessage());

            utils.Log(logfile, "[GuiAcceso]validarCodigoUsuario " + ex.getMessage());
        }
    }

    //Ax: Archivo necesario para entrar a comunicaciones
    private void preNombreArchivo(Integer Alerta) {

        File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        txtCondicion.setText(" | Carga:" + "CICAAAAMMCCC" + " " + "LXXXXXXX.YYY");
        if (!archivoNombre.exists() && archivoNombre.length() < 1) {
            if (Alerta>0)
                mostrarDialogoAlerta("Advertencia", "Dispositivo no tiene Archivos\n Cargados... Favor Cargar Datos: ");

            btnInfo.setImageResource(R.drawable.protecciondedatos1);
            return;
        }

        try {
            RandomAccessFile rFile = new RandomAccessFile(archivoNombre, "rw");
            int fileSize = (int) rFile.length();
            byte[] byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            String archivo_cargado = new String(byteArray);
//| Archivo: AAAAMMRRR LXXXXXXX.000 txtcondicion
            NombreArchivos_0 = archivo_cargado.substring(10, 20);//cambia 5, 14
            txtCondicion.setText(" | Carga:" + archivo_cargado.substring(0, 9).trim() + " " + NombreArchivos_0);
            rFile.close();

            btnInfo.setImageResource(R.drawable.protecciondedatos);
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]preNombreArchivo()|ERROR -> " + ex.getMessage());
            mensajes("Error Procesando Archivo Nombre! \n" + ex.getMessage(), 1000);
        }
    }

    // Metodo sacar la clave de administracion
    private void SacarClaveEncabezado() {

        try {

            File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
            String archivo_cargado = "";

            if (!archivoNombre.exists() && archivoNombre.length() < 1) {
                archivo_cargado = "Nombre";
                // claveSalida = generarCodigo(1432,5);
                return;
            }


            RandomAccessFile rFile = new RandomAccessFile(archivoNombre, "rw");
            int fileSize = (int) rFile.length();
            byte[] byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            archivo_cargado = new String(byteArray);
            archivocargado = archivo_cargado.substring(10, 20).trim();
            rFile.close();

            variables.setNombregeneral(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "E" + archivocargado);
            File fileGeneral = new File(variables.getNombregeneral());

            if (fileGeneral.exists()) {

                tablaEncabezado.setarchivo_General(variables.getNombregeneral());
                if (tablaEncabezado.abrir_General(tablaEncabezado.getarchivo_General())) {
                    tablaEncabezado.lectura_General(1);
                    //mensajes("Procesado Archivo Encabezado! \n" + tablaEncabezado.getGeneral_CLAVE_SUPERVISOR().trim(), 5000);
                    //si aqui tomamos el codigo de la clave que podemos usar para identificar la empresa
                    claveSalida = tablaEncabezado.getGeneral_CLAVE_SUPERVISOR().trim();
                    variables.setCodigoEmpresa(tablaEncabezado.getGeneral_SUPERVISOR().trim());
                    if (claveSalida.length() == 0) {
                        //cambiar clave por regla de valoresde 4 digitos
                        claveSalida = generarCodigo(1432, 5);
                    }
                }
                tablaEncabezado.Cerrar_General();
                aplicarFondoPorNit();


            } else {
                // claveSalida = generarCodigo(1432,5);
                Toast.makeText(getApplicationContext(), "No Hay Ruta Cargada --" + fileGeneral.getName().trim(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]SacarClaveEncabezado()|ERROR -> " + ex.getMessage());
            // claveSalida = generarCodigo(1432,5);
            mensajes("Error Procesando Archivo Encabezado! \n" + ex.getMessage(), 5000);
        }
    }

    public void aplicarFondoPorNit() {

        LinearLayout layout = findViewById(R.id.layoutPrincipal);

        String nit = variables.getCodigoEmpresa();

        int fondo;

        switch (nit) {

            case "9999"://veritas
                fondo = R.drawable.electrico;
                Empresa = " VERITASV ";
                break;

            case "9988"://hesego
                fondo = R.drawable.electrico;
                Empresa = " HESEGO ";
                break;

            case "9977": //oca global colombia 901288398
                fondo = R.drawable.electrico;
                Empresa = " GLOBAL ";
                break;
            case "1257"://global
                fondo = R.drawable.ic_launcher1;
                Empresa = " GLOBALGSS ";
                break;
            default:
                fondo = R.drawable.electrico;
                Empresa = " DEMO ";
                break;
        }

        layout.setBackgroundResource(fondo);
    }

    public static String generarCodigo(int claveSecreta, int digitos) {

        Calendar cal = Calendar.getInstance();

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int anio = cal.get(Calendar.YEAR) % 100; // últimos 2 dígitos

        int base = (dia * mes * anio) + claveSecreta;

        int modulo = (int) Math.pow(10, digitos);

        int codigo = base % modulo;

        // rellenar con ceros a la izquierda
        return String.format("%0" + digitos + "d", codigo);
    }


    // Metodo para validar el archivo cargado en la terminal
    private void nombreArchivo(int tipo) {

        File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        File archivoobserva = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/SUBCAUSAS.TXT");
        File archivonotifca = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOTIFICACIONES.TXT");
        String archivo_cargado = "";
        btnInfo.setImageResource(R.drawable.protecciondedatos1);
        if (!archivoNombre.exists() && archivoNombre.length() < 1) {
            archivo_cargado = "Nombre";
            Log.e("error", "entra aqui1 " + archivoNombre.length());
            btnInfo.setImageResource(R.drawable.protecciondedatos);
        } else if (!archivoobserva.exists() && archivoobserva.length() < 1) {
            Log.e("error", "entra aqui2 " + archivoobserva.length());
            archivo_cargado = "SUBCAUSAS";
        } /*else if (!archivonotifca.exists() && archivonotifca.length() < 1) {
            Log.e("error", "entra aqui3 " + archivonotifca.length());
            archivo_cargado = "Notificaciones";
        }*/

        if (!archivo_cargado.equals("")) {
            btnInfo.setImageResource(R.drawable.protecciondedatos1);
            mostrarDialogoAlerta("Advertencia", "Dispositivo no tiene Archivos\n Cargados... Favor Cargar Datos como: " + archivo_cargado);
            mensajeDeAlerta += "No Hay Ruta Cargada";
            return;
        }

        try {
            RandomAccessFile rFile = new RandomAccessFile(archivoNombre, "rw");
            int fileSize = (int) rFile.length();
            byte[] byteArray = new byte[fileSize];
            rFile.readFully(byteArray, 0, fileSize);
            archivo_cargado = new String(byteArray);

            archivocargado = archivo_cargado.substring(10, 20).trim();
            mensajeDeAlerta = "Ruta Cargada: " + archivocargado;
            rFile.close();
            Log.e("error", "entra 1");
            NombreArchivos_0 = archivocargado;
            File fileL = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "L" + archivocargado);
            File fileD = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + archivocargado);
            variables.setNombregeneral(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "E" + archivocargado);

            if (fileL.exists()) {
                if (!fileL.renameTo(fileD)) {
                    mensajes("El archivo L No pudo procesarse", 2000);
                    btnInfo.setImageResource(R.drawable.protecciondedatos1);
                    return;
                }
            }
            //Log.e("error", "entra 2 " + variables.getNombregeneral());

            if (tipo == 1) return;

            File fileGeneral = new File(variables.getNombregeneral());

            if (fileGeneral.exists()) {

                tablaEncabezado.setarchivo_General(variables.getNombregeneral());
                tablaAforadores.setarchivo_Lector(variables.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/LECTOR.TXT");

                if (tablaEncabezado.abrir_General(tablaEncabezado.getarchivo_General())) {
                    //Log.e("error", "entra 3");

                    tablaEncabezado.lectura_General(1);
                    //Log.e("error", "entra 4");

                    if (!tablaAforadores.abrir_Lector(tablaAforadores.getarchivo_Lector())) {

                        mensajeDeAlerta = "\nNo existe tabla Lector...";
                        tablaEncabezado.Cerrar_General();
                        return;
                    }
                    // Log.e("error", "entra 5 " + tablaEncabezado.getGeneral_LECTOR());
                    tablaAforadores.buscarSecuencial_Lector(tablaEncabezado.getGeneral_LECTOR());

                    if (tablaAforadores.getencontro_Lector() > 0) {
                        // Log.e("error", "entra 7");
                        btnInfo.setImageResource(R.drawable.protecciondedatos);
                        mensajeDeAlerta += "\n" + tablaAforadores.getLector_CODIGO() + " - " + tablaAforadores.getLector_DESCRIPCION();
                        VerMensajeria("CIC" + archivo_cargado.substring(0, 9).trim(), archivocargado, tablaAforadores.getLector_CODIGO(), tablaAforadores.getLector_DESCRIPCION(), tablaEncabezado.getGeneral_CLAVE_SUPERVISOR());

                    } else {
                        // Log.e("error", "entra 8");
                        btnInfo.setImageResource(R.drawable.protecciondedatos1);
                        mensajeDeAlerta += "Aforador en la lista No Existe...";
                        VerMensajeria("CIC" + archivo_cargado.substring(0, 9).trim(), archivocargado, tablaEncabezado.getGeneral_LECTOR(), mensajeDeAlerta, "*****");
                    }
                    tablaEncabezado.Cerrar_General();
                    tablaAforadores.Cerrar_Lector();
                    //  mensajes(mensajeDeAlerta, 1000);
                }
            } else {

                Toast.makeText(getApplicationContext(), "No Hay Ruta Cargada en E--" + fileGeneral.getName().trim(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]nombreArchivo()|ERROR -> " + ex.getMessage());
            mensajes("Error Procesando Archivo Nombre! \n" + ex.getMessage(), 1000);
        }
    }

    private void VerMensajeria(String Dato1, String Dato2, String Dato3, String Dato4, String Dato5) {
        String mensaje2 = "";
        try {
            //final String mensaje1;
            // ✅ Mensaje definido correctamente
            String mensaje1 = "📡 ESTADO DE CARGA\n\n" +
                    "• CICLO: " + Dato1 + "\n" +
                    "• Ruta: " + Dato2 + "\n" + "✔ Información cargada correctamente\n\n" +
                    "• Codigo Lector: " + Dato3 + "\n" +
                    "ℹ️ Nombre Lector:" + Dato4 + "\n\n\n" +
                    "- Su información ya fue cargada a la movil.\n" +
                    "- Puede continuar con la toma de nuevas lecturas sin inconvenientes.\n" +
                    "- Verifique su conexión a internet.\n" +
                    "- Diríjase al menú de 'Envío de datos'.\n" +
                    "- Asegúrese de enviar las fotografías pendientes.\n" +
                    "\n📌 SOPORTE:\n" +
                    "En caso de inconsistencias, comuníquese con su supervisor o el área técnica.\n" +
                    "Sistema SIM-LE - Gestión de Lecturas Móviles.** " + Dato5 + " **";


            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje1)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();

            // ✅ cierre correcto del lambda
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]VerMensajeria()|ERROR -> " + ex.getMessage());
            mensaje2 =
                    "📡 ESTADO DE CARGA..\n\n" +
                            "✔ Información TIENE ERRORE.\n" +
                            "📅 sin fecha de Carga: " + "Hay un error" + "\n\n" +
                            "ℹ️ El sistema no esta listo para Ejecucion.";//obtenerFechaActual()
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje2)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();
        }
    }

    /**
     * Retorna la fecha actual en formato yyyymmdd
     */
    private String getPhoneDate() {

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    /**
     * Retorna la fecha Actual del celular en formato 'HH:mm:ss'
     */
    public static String getPhoneHour() {

        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteHour = df.format(dt.getTime());
        return formatteHour;
    }

    /**
     * Verifica estado de la SD
     */

    public void testSD() {

        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }

    // Captura numero IMEI del telefono
    public String getSerialNumber() {

//        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceIMEI = tManager.getDeviceId();
//        return deviceIMEI;

        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                            imei = telephonyManager.getMeid();
                        } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                            imei = telephonyManager.getImei();
                            // imei = telephonyManager.getDeviceId();
                        }

                        esMayor9 = true;
                    } catch (Exception e) {
                        utils.Log(logfile, "[GuiAcceso]getSerialNumber()|ERROR -> " + e.getMessage());
                        esMayor9 = true;
                        imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                }
            } else {
                ActivityCompat.requestPermissions(GuiAcceso.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(GuiAcceso.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                }
            } else {
                ActivityCompat.requestPermissions(GuiAcceso.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        }
        return imei;
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

    private boolean crearArchivoAdministrado() {

        String fecha = getPhoneDate();
        String hora = getPhoneHour();

        if (Long.parseLong(hora.substring(0, 2) + hora.substring(3, 5)) < 900) {

            return false;
        }

        File nombreArchivo = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LECTURAADMINISTRADA.TXT");
        if (nombreArchivo.exists()) {

            nombreArchivo.delete();
        }

        try {
            String x = nombreArchivo.getAbsoluteFile().toString();

            RandomAccessFile archivoAdministrado = new RandomAccessFile(nombreArchivo, "rw");
            archivoAdministrado.writeBytes(fecha);
            archivoAdministrado.close();
            // txtCodigo.setText("");
            // txtClave.setText("");
            txtClave.setEnabled(false);
            Toast.makeText(getApplicationContext(), "EQUIPO AUTORIZADO PARA LECTURAS POSTERIORES A LAS 9 AM", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            utils.Log(logfile, "[GuiAcceso]crearArchivoAdministrado()|ERROR -> " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    private void procesoVerificacionClave() {

        try {
            String clv = txtClave.getText().toString().trim();
            String clv2 = tablaAforadores.getLector_ESTADO();
            Log.e("error", clv + "-data-" + tablaAforadores.getLector_CLAVE());
            if (tablaAforadores.getLector_CLAVE().trim().equals(clv) && tablaAforadores.getLector_ESTADO().equals("A")) { //claveDigitada == Long.parseLong(cadenaClave.trim()) &&
                crearArchivoAdministrado();
            } else {

                if (tablaAforadores.getLector_CLAVE().trim().equals(clv) && nivelOperador.equals("S")) { //claveDigitada == Long.parseLong(cadenaClave.trim()) || claveDigitada == Long.parseLong(claveSalida) ||

                    txtClave.setEnabled(false);
                    aforadorClaveCorrecto = 1;
                    btnSalir.setEnabled(true);
                    btnIngresar.setEnabled(true);
                    btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6A00")));

                    btnSalir.requestFocus();
                } else if (tablaAforadores.getLector_CLAVE().trim().equals(clv) && nivelOperador.equals("L")) { //claveDigitada == Long.parseLong(cadenaClave.trim()) || claveDigitada == Long.parseLong(claveSalida) ||

                    txtClave.setEnabled(false);
                    aforadorClaveCorrecto = 1;
                    btnIngresar.setEnabled(true);
                    btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6A00")));

                    btnIngresar.requestFocus();
                } else {

                    txtClave.setText("");
                    txtClave.setEnabled(false);
                    txtCodigo.setText("");
                    txtNombreUsuario.setText("Clave incorrecta para el: ");

                    Toast.makeText(getApplicationContext(), "CLAVE INCORRECTA DEL USUARIO...", Toast.LENGTH_LONG).show();
                    btnSalir.setEnabled(false);

                    btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnIngresar.setEnabled(false);
                    return;
                }
                cargarImagenUsuario("" + txtCodigo.getText().toString().trim());
            }
        } catch (Exception ex) {
            utils.Log(logfile, "[GuiAcceso]procesoVerificacionClave()|ERROR -> " + ex.getMessage());
            mensajes("Error con la Clave del lector... consulte con el Administrador... Remita Fecha si no hay carga", 4000);
        }
    }

    private void ingresoPrincipal() {

        variables.setGlobaloperario(txtCodigo.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putString("codigoUsuario", txtCodigo.getText().toString());
        bundle.putString("nivelOperador", nivelOperador);
        bundle.putString("IMEI", txtCodInterno.getText().toString());
        bundle.putString("fecha", txtFecha.getText().toString());
        bundle.putString("NombreArchivos_Env", NombreArchivos_0);

        if (!(archivocargado.equals("")) && !(archivocargado == null)) {

            bundle.putString("archivocargado", archivocargado);

        }
        Intent i = new Intent(this, MenuPrincipal.class);
        i.putExtras(bundle);

        //startActivity(i);
        startActivityForResult(i, ACTIVITY_MENU_PRINCIPAL);

    }

    void connectToDevice(BluetoothDevice _device) {
        if (_device != null) {
            String dirmac = _device.getAddress();
            VariablesGlobales.btPrintService.connect(_device);

            macAdress = dirmac;
            utils.WriteLine(logPrint, dirmac); //escribe en log la mac
            // VariablesGlobales.btPrintService.getState()
        } else {
            addLog("unknown remote device!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (VariablesGlobales.btPrintService != null)
            VariablesGlobales.btPrintService.stop(); // Stop the Bluetooth chat services
        nuevoImpresor.close(); // Ax: cierra el executor del modelo stateless nuevo
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

    @Override
    public void onStart() {
        super.onStart();

        if (VariablesGlobales.mBluetoothAdapter != null) {
            // If BT is not on, request that it be enabled. setupChat() will then be called during onActivityResult
            if (!VariablesGlobales.mBluetoothAdapter.isEnabled()) {
                //se comenta Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //se comenta  startActivityForResult(enableIntent, VariablesGlobales.REQUEST_ENABLE_BT);
                // Otherwise, setup the comm session
            } else {
                if (VariablesGlobales.btPrintService == null)
                    setupComm();
                // setupChat();
            }
        }
    }

    public void ImpresoraStatus() {

        switch (VariablesGlobales.btPrintService.getState()) {
            case btPrintFile.STATE_DISCONNECTED:
                txtRemoteDevice.setText("DESCONECTADA");//
                conn = true;
                break;
            case btPrintFile.STATE_CONNECTED:
                txtRemoteDevice.setText("CONECTADO " + macAdress);
                conn = true;
                break;
            case btPrintFile.STATE_CONNECTING:
                conn = false;
                txtRemoteDevice.setText("CONECTANDO..... ");
                break;
            case btPrintFile.STATE_IDLE:
                txtRemoteDevice.setText("DESCONECTADA");
                conn = true;
                break;
            case btPrintFile.STATE_LISTEN:
                txtRemoteDevice.setText("CONECTADO a " + macAdress);
                break;
            default:
                txtRemoteDevice.setText("OBTENIENDO STATUS...");
                conn = true;
                break;
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    Calendar calendar = Calendar.getInstance();

                    String fecha = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    int seconds = calendar.get(Calendar.SECOND);

                    String mins = "" + minutes;
                    String secs = "" + seconds;

                    if (minutes < 10) {

                        mins = "0" + mins;
                    }
                    if (seconds < 10) {

                        secs = "0" + secs;
                    }

                    String curTime = fecha + " - " + hours + ":" + mins + ":" + secs;
                    txtFecha.setText(curTime);

                } catch (Exception e) {
                    utils.Log(logfile, "[GuiAcceso]doWork()|ERROR -> " + e.getMessage());
                }
            }
        });
    }

    class Reloj implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    utils.Log(logfile, "[GuiAcceso]Reloj(InterruptedException)|ERROR -> " + e.getMessage());
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    utils.Log(logfile, "[GuiAcceso]Reloj(Exception)|ERROR -> " + e.getMessage());
                }
            }
        }
    }

    private void iniciarPanelComunicaciones() {

        Bundle bundle = new Bundle();

        bundle.putString("NIVEL_OPERADOR", nivelOperador);
        bundle.putString("CODIGO_INTERNO_PDA", txtCodInterno.getText().toString().trim());
        bundle.putString("NombreArchivos_Env", NombreArchivos_0);

        Intent comunicaciones = new Intent(this, Comunicaciones.class);
        comunicaciones.putExtras(bundle);
        startActivityForResult(comunicaciones, ACTIVITY_COMUNICACIONES);

        String dirEjecutable = VariablesGlobales.directorioactual;
        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ADMINIST.TXT"); //CLAVES PROHIBICIONES

        if (file.exists()) {
            File file2 = new File(dirEjecutable + "/ADMINISTADORDEPDA.TXT");

            if (file2.exists()) {

                file2.delete();
            }

            VariablesGlobales.copyFile(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ADMINIST.TXT", dirEjecutable + "/ADMINISTADORDEPDA.TXT", true);
        }
    }

    private void mensajes(String msg, int dur) {

        Toast toast = Toast.makeText(GuiAcceso.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 150);
        toast.show();
    }

    /**
     * Deja el selector parado sobre la ruta que realmente esta cargada: modulo (lecturas o
     * entregas) y carpeta (1 o 2). Las dos carpetas pueden tener rutas de tipos distintos.
     *
     * Antes se abria el archivo D de la carpeta activa y se miraba su DESCSECTOR, pero:
     *  - El nombre del archivo (NombreArchivos_0) venia de una lectura anterior de NOMBRE, hecha
     *    antes de que CargarRutaTrabajo() fijara la carpeta activa. Con una ruta en cada carpeta
     *    el nombre podia ser el de la otra, la ruta del archivo D no existia y se caia al
     *    caso por defecto (lecturas, carpeta 1), sin importar lo que hubiera cargado.
     *  - El archivo D quedaba abierto: nunca se llamaba Cerrar_TablaRegistroSalida().
     *
     * Ahora el tipo se toma del archivo NOMBRE (posicion 9: L o E), que es mas barato, no deja
     * archivos abiertos y no depende de que el archivo D exista.
     */
    /**
     * Deja el selector parado sobre la ruta que realmente esta cargada: modulo (lecturas o
     * entregas) y carpeta (1 o 2). Las dos carpetas pueden tener rutas de tipos distintos.
     *
     * Antes se abria el archivo D de la carpeta activa y se miraba su DESCSECTOR, pero:
     *  - El nombre del archivo (NombreArchivos_0) venia de una lectura anterior de NOMBRE, hecha
     *    antes de fijar la carpeta activa. Con una ruta en cada carpeta el nombre podia ser el de
     *    la otra, la ruta del archivo D no existia y se caia al caso por defecto (lecturas,
     *    carpeta 1) sin importar lo que hubiera cargado.
     *  - El archivo D quedaba abierto: nunca se llamaba Cerrar_TablaRegistroSalida().
     *
     * Ahora el tipo se toma del archivo NOMBRE (posicion 9: L o E), que es mas barato, no deja
     * archivos abiertos y no depende de que el archivo D exista.
     */
    /**
     * Deja el selector mostrando SOLO lo que de verdad esta cargado, y parado sobre la ruta
     * activa. El tipo (lecturas o entregas) sale del archivo NOMBRE, posicion 9: L o E.
     */
    public void validarTipoRuta() {
        try {
            ClaveRuta ruta1 = ClaveRuta.leerDeCarpeta(VariablesGlobales.directorioactual, ClaveRuta.CARPETA_1);
            ClaveRuta ruta2 = ClaveRuta.leerDeCarpeta(VariablesGlobales.directorioactual, ClaveRuta.CARPETA_2);

            // Que carpeta esta marcada como activa. Misma precedencia que CargarRutaTrabajo():
            // si por alguna razon existieran las dos marcas, manda la 1.
            boolean esCarpeta2 = new File(getFilesDir(), RUTA_ACTIVA2).exists()
                    && !new File(getFilesDir(), RUTA_ACTIVA1).exists();

            // Si la carpeta marcada quedo sin ruta (recien borrada, o nunca se cargo ahi) pero la
            // otra si tiene, el selector se para sobre la que existe en vez de sobre una vacia.
            if ((esCarpeta2 ? ruta2 : ruta1) == null && (esCarpeta2 ? ruta1 : ruta2) != null) {
                esCarpeta2 = !esCarpeta2;
            }

            // Se fija la carpeta UNA sola vez y ya sobre la definitiva: CargarRutaTrabajo() avisa
            // cuando la carpeta no tiene datos, y no debe avisar por una que igual vamos a dejar.
            guardarRutaTrabajo(esCarpeta2 ? "RUTA2" : "RUTA1");

            ClaveRuta ruta = esCarpeta2 ? ruta2 : ruta1;
            configurarSelectorRutas(ruta1, ruta2, esCarpeta2);

            if (ruta == null) {
                // Ninguna carpeta tiene ruta: el selector ofrece las cuatro y arranca en lecturas.
                VariablesGlobales.moduloTrabajo = ClaveRuta.MODULO_LECTURAS;
                mostrarSinRuta(esCarpeta2);
                return;
            }

            // Al arrancar, el modulo se restaura de lo que hay cargado (moduloTrabajo es estatico
            // y se pierde al cerrar la app). De ahi en adelante lo fija el selector.
            VariablesGlobales.moduloTrabajo = ruta.getModuloTrabajo();

            // Releer el nombre del archivo YA con la carpeta definitiva, para que
            // NombreArchivos_0 y la tabla de registros apunten a la ruta seleccionada.
            preNombreArchivo(0);
            infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual
                    + VariablesGlobales.getCarpetaLecturas() + "/" + "D" + NombreArchivos_0);

            mostrarModuloYCarpeta(esCarpeta2);

        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]validarTipoRuta()|ERROR -> " + e.getMessage());
            Log.e("error", "error al metodo " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if(spnPath.isEnabled()){
            Log.e("error","entra a resumen");
            finish();
            startActivity(getIntent());
        }*/
        preNombreArchivo(0);
    }

    public void renameArchivo() {
        File fileL = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "L" + archivocargado);
        File fileD = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + archivocargado);

        if (fileL.exists()) {
            if (!fileL.renameTo(fileD)) {
                //mensajes("El archivo L No pudo procesarse", 2000);
                //return;
            }
        }
    }

    // =========================================================================
    // VERIFICACIÓN DE VERSIÓN
    // =========================================================================

    /**
     * Flujo en dos pasos:
     * <p>
     * PASO 1 — Inmediato, sin internet:
     * Lee permisoTrabajo desde el modelo General en Realm.
     * Si es "N" → bloquea btnIngresar de inmediato.
     * Garantiza que el bloqueo persiste aunque no haya internet.
     * <p>
     * PASO 2 — Background, con internet:
     * Llama GET /api/parametros/ultimaVersion.
     * Compara con VariablesGlobales.versionApp (versión del APK instalado):
     * - Iguales             → guarda permisoTrabajo="S", habilita btnIngresar.
     * - Diferentes, ≤3 días → guarda "N", aviso obligatorio, puede continuar esta vez.
     * - Diferentes, >3 días → guarda "N", bloqueo total.
     * - Sin internet        → no modifica Realm (conserva estado anterior).
     */
    private void verificarVersionApp() {
        Realm.init(this);

        // ===== PASO 1: aplicar permiso guardado en Realm (offline) =====
        String permiso = CrudGeneral.getPermisoTrabajo();
        String VersionAnterior = CrudGeneral.getVersionBloqueada();//.substring(3,6)

        if (VersionAnterior.trim().equals("") && VersionAnterior.trim().length() < 6)
            VersionAnterior = "1.0";
        else
            VersionAnterior = CrudGeneral.getVersionBloqueada().substring(3, 6);
        String ParteVersionActual = VariablesGlobales.versionApp.trim().substring(3, 6);//.substring(3,6)

        double numero1 = 0;
        double numero2 = 0;
        //numero1 = Double.parseDouble(VersionAnterior);
        //numero2 = Double.parseDouble(ParteVersionActual);
        try {

            numero1 = Double.parseDouble(VersionAnterior);
        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]verificarVersionApp(numero1)|ERROR -> " + e.getMessage());
            //  numero1 = 0.0;
        }

        try {
            numero2 = Double.parseDouble(ParteVersionActual);
        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]verificarVersionApp(numero2)|ERROR -> " + e.getMessage());
            // numero2 = 0.0;
        }
        try {
            if (numero2 >= numero1)
                permiso = "S";
        } catch (NumberFormatException e) {
            utils.Log(logfile, "[GuiAcceso]verificarVersionApp(permiso)|ERROR -> " + e.getMessage());
            //error
        }
        if ("N".equals(permiso)) {
            String versionBloqueada = CrudGeneral.getVersionBloqueada();
            bloquearBtnIngresar();
            mostrarDialogoBloqueo(VariablesGlobales.versionApp.trim(), versionBloqueada);
        }
        txtConexion.setText("  | Estado: Conectado");
        // ===== PASO 2: consultar API y actualizar permiso =====
        try {
            BDComunicaciones bdc = CrudComunicaciones.getParams();
            if (bdc == null) {
                Log.w("GuiAcceso", "verificarVersionApp: sin config de red");

                return;
            }

            /*String urlBase = (bdc.gethttpSeguro() == 0 ? "http://" : "https://") + bdc.getURL();
            String cadenaURL = urlBase.substring(0, urlBase.lastIndexOf(":") + 1)
                    + bdc.getPuertoApi() + "/api/";
            String endpoint = cadenaURL + "parametros/ultimaVersion";*/
            String endpoint = bdc.getUrlApi() + "parametros/ultimaVersion";
            final String versionLocal = VariablesGlobales.versionApp.trim();
            final String permisoFinal = permiso;
            new Thread(() -> {
                try {
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection)
                            new java.net.URL(endpoint).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    if (conn.getResponseCode() != 200) {
                        conn.disconnect();
                        txtConexion.setText("  | Estado: Sin Conexion");
                        return; // Respuesta inválida → conservar Realm sin cambios
                    }

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String linea;
                    while ((linea = br.readLine()) != null) sb.append(linea);
                    br.close();
                    conn.disconnect();

                    String versionBD = extraerValor1(sb.toString());
                    if (versionBD == null || versionBD.isEmpty()) return;

                    Log.i("GuiAcceso", "version local=[" + versionLocal
                            + "] version BD=[" + versionBD + "]");

                    if (versionLocal.equalsIgnoreCase(versionBD.trim()) || "S".equals(permisoFinal)) {
                        // Versiones iguales → desbloquear
                        CrudGeneral.updatePermisoTrabajo("S", versionLocal, versionLocal);//porque esta en"" quitando la version anterior que pasa despues analizar
                        runOnUiThread(() -> {
                            // btnIngresar.setEnabled(true);
                            btnIngresar.setAlpha(1.0f);
                            txtCodigo.setEnabled(true);
                            txtCodigo.requestFocus();
                        });
                    } else {
                        // Versiones diferentes → guardar bloqueo y mostrar alerta
                        long dias = diasDesdeVersionBD(versionBD.trim());
                        CrudGeneral.updatePermisoTrabajo("N", versionBD.trim(), versionLocal);
                        runOnUiThread(() -> mostrarAlertaVersion(versionLocal, versionBD.trim(), dias));
                    }

                } catch (Exception e) {
                    utils.Log(logfile, "[GuiAcceso]verificarVersionApp(Exception)|ERROR -> " + e.getMessage());
                    Log.e("GuiAcceso", "verificarVersionApp HTTP: " + e.getMessage());
                    txtConexion.setText("  | Estado: Sin Conexion");
                    // Sin internet → Realm mantiene su estado anterior
                }
            }).start();

        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]verificarVersionApp(Exception 2)|ERROR -> " + e.getMessage());
            Log.e("GuiAcceso", "verificarVersionApp: " + e.getMessage());
        }
    }

    /**
     * Deshabilita visualmente el botón de ingreso
     */
    private void bloquearBtnIngresar() {
        txtCodigo.setEnabled(false);
        txtClave.setEnabled(false);
        btnIngresar.setEnabled(false);
        btnIngresar.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        btnIngresar.setAlpha(0.4f);
    }

    /**
     * Diálogo de bloqueo persistente (viene del estado guardado en Realm).
     * El usuario ya no puede ingresar aunque cierre y abra la app.
     */
    private void mostrarDialogoBloqueo(String versionLocal, String versionBD) {
        new AlertDialog.Builder(this)
                .setTitle("VERSIÓN DESACTUALIZADA - ACCESO BLOQUEADO")
                .setMessage(
                        "Su versión fue bloqueada previamente.\n\n" +
                                "Versión instalada: " + versionLocal + "\n" +
                                "Versión requerida: " + (versionBD.isEmpty() ? "consulte a su supervisor" : versionBD) + "\n\n" +
                                "Comuníquese con su supervisor para actualizar."
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton("CERRAR", (d, w) -> d.dismiss())
                .show();
    }

    /**
     * Muestra alerta según días transcurridos desde la versión vigente en BD.
     * ≤ 3 días → aviso, puede continuar (pero permisoTrabajo ya está en "N" en Realm).
     * > 3 días → bloqueo total.
     */
    private void mostrarAlertaVersion2(String versionLocal, String versionBD, long diasDesde) {
        if (diasDesde >= 0 && diasDesde <= 3) {
            long diasRestantes = 3 - diasDesde;
            new AlertDialog.Builder(this)
                    .setTitle("ACTUALIZACIÓN PENDIENTE")
                    .setMessage(
                            "Su versión está desactualizada.\n\n" +
                                    "Versión instalada: " + versionLocal + "\n" +
                                    "Versión actual:    " + versionBD + "\n\n" +
                                    "Le quedan " + diasRestantes + " día(s) para actualizar.\n" +
                                    "Comuníquese con su supervisor."
                    )
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .setPositiveButton("ENTENDIDO, CONTINUAR", (d, w) -> d.dismiss())
                    .show();
        } else {
            // Más de 3 días → bloqueo
            bloquearBtnIngresar();
            long vencidoHace = diasDesde > 3 ? diasDesde - 3 : 0;
            new AlertDialog.Builder(this)
                    .setTitle("VERSIÓN DESACTUALIZADA - ACCESO BLOQUEADO")
                    .setMessage(
                            "Su versión venció hace " + vencidoHace + " día(s).\n\n" +
                                    "Versión instalada: " + versionLocal + "\n" +
                                    "Versión requerida: " + versionBD + "\n\n" +
                                    "Comuníquese con su supervisor para actualizar."
                    )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("CERRAR", (d, w) -> d.dismiss())
                    .show();
        }
    }

    private void mostrarAlertaVersion(String versionLocal, String versionBD, long diasDesde) {

        // 🔒 VALIDACIÓN CRÍTICA
        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (diasDesde >= 0 && diasDesde <= 3) {

            long diasRestantes = 3 - diasDesde;

            new AlertDialog.Builder(this)
                    .setTitle("ACTUALIZACIÓN PENDIENTE")
                    .setMessage(
                            "Su versión está desactualizada.\n\n" +
                                    "Versión instalada: " + versionLocal + "\n" +
                                    "Versión actual:    " + versionBD + "\n\n" +
                                    "Le quedan " + diasRestantes + " día(s) para actualizar.\n" +
                                    "Comuníquese con su supervisor."
                    )
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .setPositiveButton("ENTENDIDO, CONTINUAR", (d, w) -> d.dismiss())
                    .show();

        } else {

            bloquearBtnIngresar();

            long vencidoHace = diasDesde > 3 ? diasDesde - 3 : 0;

            // 🔒 VALIDAR DE NUEVO (por seguridad extra)
            if (isFinishing() || isDestroyed()) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("VERSIÓN DESACTUALIZADA - ACCESO BLOQUEADO")
                    .setMessage(
                            "Su versión venció hace " + vencidoHace + " día(s).\n\n" +
                                    "Versión instalada: " + versionLocal + "\n" +
                                    "Versión requerida: " + versionBD + "\n\n" +
                                    "Comuníquese con su supervisor para actualizar."
                    )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("CERRAR", (d, w) -> d.dismiss())
                    .show();
        }
    }

    /**
     * Extrae "valor1" del JSON: {"data":{"nomParametro":"ultimaVersion","valor1":"Ver5.4 26.01.21"}}
     */
    private String extraerValor1(String json) {
        try {
            String key = "\"valor1\"";
            int idx = json.indexOf(key);
            if (idx < 0) return null;
            int abre = json.indexOf('"', idx + key.length() + 1);
            int cierra = json.indexOf('"', abre + 1);
            if (abre < 0 || cierra < 0) return null;
            return json.substring(abre + 1, cierra).trim();
        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]extraerValor1()|ERROR -> " + e.getMessage());
            return null;
        }
    }

    /**
     * Calcula días transcurridos desde la fecha embebida en el string de versión.
     * Formato: "VerX.X YY.MM.DD"  →  año=20YY, mes=MM, dia=DD
     * Retorna -1 si no puede parsear.
     */
    private long diasDesdeVersionBD(String version) {
        try {
            int espacio = version.indexOf(' ');
            if (espacio < 0) return -1;
            String[] p = version.substring(espacio + 1).trim().split("\\.");
            if (p.length < 3) return -1;
            int anio = 2000 + Integer.parseInt(p[0].trim());
            int mes = Integer.parseInt(p[1].trim()) - 1;
            int dia = Integer.parseInt(p[2].trim());
            Calendar fechaBD = Calendar.getInstance();
            fechaBD.set(anio, mes, dia, 0, 0, 0);
            fechaBD.set(Calendar.MILLISECOND, 0);
            return (Calendar.getInstance().getTimeInMillis()
                    - fechaBD.getTimeInMillis()) / (1000L * 60 * 60 * 24);
        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]diasDesdeVersionBD()|ERROR -> " + e.getMessage());
            Log.e("GuiAcceso", "diasDesdeVersionBD: " + e.getMessage());
            return -1;
        }
    }

    // =========================================================================
    // VERIFICACIÓN DE RUTA CARGADA
    // =========================================================================

    /**
     * Verifica que se pueda ingresar sin mezclar rutas.
     * <p>
     * Reglas:
     * 1. Realm vacío → OK siempre.
     * 2. Realm con datos + más de un archivo D en LECTURAMEDIDORES → BLOQUEAR.
     * No se puede determinar a cuál ruta pertenecen los datos.
     * 3. Realm con datos + exactamente un archivo D → verificar que ese D
     * coincida con lo que dice el archivo NOMBRE (substring 10-20).
     * Si no coincide → BLOQUEAR.
     * 4. Realm con datos + ningún archivo D → dejar pasar
     * (el sistema manejará el caso al cargar la ruta).
     *
     * @return true si puede continuar, false si se bloqueó.
     */
    private boolean verificarRutaCargada() {
        try {
            File dirLect = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/");
            File[] archivosD = dirLect.listFiles(f -> f.isFile() && f.getName().startsWith("D"));
            int cantidadD = (archivosD != null) ? archivosD.length : 0;

            Log.e("RutaCheck", "Archivos D: " + cantidadD);
            if (archivosD != null) {
                for (File f : archivosD) Log.e("RutaCheck", "  -> " + f.getName());
            }

            // Más de un D → bloquear siempre, sin importar Realm
            if (cantidadD > 1) {
                bloquearBtnIngresar();
                StringBuilder listaD = new StringBuilder();
                for (File f : archivosD) listaD.append("  ").append(f.getName()).append("\n");

                new AlertDialog.Builder(this)
                        .setTitle("MÚLTIPLES RUTAS DETECTADAS")
                        .setMessage(
                                "Hay " + cantidadD + " rutas cargadas en el dispositivo:\n\n" +
                                        listaD.toString() +
                                        "\nNo puede trabajar con múltiples rutas.\n" +
                                        "Comuníquese con su supervisor."
                        )
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                        .show();
                return false;
            }

            // Exactamente un D → verificar que coincida con NOMBRE
            btnInfo.setImageResource(R.drawable.protecciondedatos1);
            if (cantidadD == 1) {
                String nombreD = archivosD[0].getName().substring(1); // quitar "D" → "090132.000"

                File archivoNombre = new File(
                        VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE"
                );
                if (!archivoNombre.exists() || archivoNombre.length() < 20) return true;
                btnInfo.setImageResource(R.drawable.protecciondedatos);
                RandomAccessFile rNombre = new RandomAccessFile(archivoNombre, "r");
                byte[] bytes = new byte[(int) rNombre.length()];
                rNombre.readFully(bytes);
                rNombre.close();

                String contenido = new String(bytes);
                if (contenido.length() < 20) return true;

                // substring(10, 20) igual que nombreArchivo() del sistema
                String nombreNombre = contenido.substring(10, 20).trim();
                txtCondicion.setText(" | Carga:" + contenido.substring(0, 9).trim() + " " + contenido.substring(9, 20));

                Log.e("RutaCheck", "D en disco=[" + nombreD + "] NOMBRE dice=[" + nombreNombre + "]");

                if (nombreD.equalsIgnoreCase(nombreNombre)) return true;

                // El D no coincide con NOMBRE → ruta diferente
                String tipoRuta = VariablesGlobales.moduloTrabajo.equals("ENT") ? "ENTREGAS" : "LECTURAS";
                new AlertDialog.Builder(this)
                        .setTitle("RUTA DIFERENTE DETECTADA")
                        .setMessage(
                                "La ruta cargada no coincide con los datos del dispositivo.\n\n" +
                                        "Ruta en disco:  D" + nombreD + "\n" +
                                        "Ruta en NOMBRE: D" + nombreNombre + " (" + tipoRuta + ")\n\n" +
                                        "Comuníquese con su supervisor."
                        )
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                        .show();
                return false;
            }

            // cantidadD == 0 → sin archivos D, dejar pasar
            return true;

        } catch (Exception e) {
            utils.Log(logfile, "[GuiAcceso]verificarRutaCargada()|ERROR -> " + e.getMessage());
            Log.e("GuiAcceso", "verificarRutaCargada: " + e.getMessage());
            return true;
        }
    }

    private void guardarRutaTrabajo(String RutaEscogida) {
        File archivoRuta1 = new File(getFilesDir(), RUTA_ACTIVA1);
        File archivoRuta2 = new File(getFilesDir(), RUTA_ACTIVA2);
        try {
            if (RutaEscogida.equals("RUTA1")) {
                if (!archivoRuta1.exists())
                    archivoRuta1.createNewFile();
                if (archivoRuta2.exists())
                    archivoRuta2.delete();
            } else {
                if (!archivoRuta2.exists())
                    archivoRuta2.createNewFile();
                if (archivoRuta1.exists())
                    archivoRuta1.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CargarRutaTrabajo();
    }

    /**
     * Revisa cual de los dos archivos existe y deja MarcaDeImpresora
     * establecida con esa marca. Llamala en el onCreate() para restaurar
     * la impresora usada en la ultima sesion. Si no existe ninguno (primer
     * uso de la app), deja el valor por defecto que ya traiga la variable.
     */
    /**
     * Escribe sobre el selector que modulo se esta trabajando y en cual ruta (1 o 2).
     *
     * El modulo es el que fija el selector (VariablesGlobales.moduloTrabajo). La etiqueta
     * txtPath existia en el layout pero nunca se enlazaba: siempre decia "Modulo a trabajar",
     * asi que al abrir la app no habia forma de saber en que se estaba.
     */
    private void mostrarModuloYCarpeta(boolean esCarpeta2) {
        if (txtPath == null) {
            return;
        }
        String modulo = ClaveRuta.MODULO_ENTREGAS.equals(VariablesGlobales.moduloTrabajo)
                ? "ENTREGAS" : "LECTURAS";
        txtPath.setText("Trabajando en\n" + modulo + " - RUTA " + (esCarpeta2 ? "2" : "1"));
        txtPath.setTextColor(Color.parseColor("#1B5E20"));
    }

    /** Cuando no hay ninguna ruta cargada en la carpeta activa. */
    private void mostrarSinRuta(boolean esCarpeta2) {
        if (txtPath == null) {
            return;
        }
        txtPath.setText("Trabajando en\nRUTA " + (esCarpeta2 ? "2" : "1") + " - SIN DATOS");
        txtPath.setTextColor(Color.parseColor("#B00020"));
    }

    /** Nombre de la opcion del selector para un modulo y una carpeta. */
    private String etiquetaOpcion(boolean esEntrega, boolean esCarpeta2) {
        return (esEntrega ? "ENTREGAS" : "LECTURAS") + (esCarpeta2 ? "2" : "");
    }

    /**
     * Llena el selector con las opciones que de verdad aplican, y lo deja parado sobre la
     * carpeta activa.
     *
     * Antes el selector mostraba siempre las cuatro combinaciones del arreglo rutasT, asi que
     * se podia escoger, por ejemplo, ENTREGAS teniendo lecturas cargadas en la ruta 1. Ahora:
     *  - Carpeta CON ruta cargada  -> solo su opcion real, para no prestarse a confusion.
     *  - Carpeta SIN ruta cargada  -> las dos, porque ahi todavia se puede cargar cualquiera.
     */
    private void configurarSelectorRutas(ClaveRuta ruta1, ClaveRuta ruta2, boolean esCarpeta2) {
        List<String> opciones = new ArrayList<>();
        agregarOpcionesDeCarpeta(opciones, ruta1, false);
        agregarOpcionesDeCarpeta(opciones, ruta2, true);

        ClaveRuta activa = esCarpeta2 ? ruta2 : ruta1;
        int posicion = opciones.indexOf(etiquetaOpcion(activa != null && activa.esEntrega(), esCarpeta2));

        // Rellenar el selector dispara onItemSelected: se marca para que el listener lo ignore
        // y no vuelva a ejecutar el cambio de ruta por un movimiento que no hizo el usuario.
        actualizandoSelector = true;
        ArrayAdapter<String> pathA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        pathA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPath.setAdapter(pathA);
        if (posicion >= 0) {
            spnPath.setSelection(posicion);
        }
        spnPath.post(() -> actualizandoSelector = false);
    }

    /** Version que lee el estado actual de las dos carpetas. */
    private void configurarSelectorRutas() {
        configurarSelectorRutas(
                ClaveRuta.leerDeCarpeta(VariablesGlobales.directorioactual, ClaveRuta.CARPETA_1),
                ClaveRuta.leerDeCarpeta(VariablesGlobales.directorioactual, ClaveRuta.CARPETA_2),
                VariablesGlobales.getCarpetaLecturas().contains(ClaveRuta.CARPETA_2));
    }

    private void agregarOpcionesDeCarpeta(List<String> opciones, ClaveRuta ruta, boolean esCarpeta2) {
        if (ruta != null) {
            opciones.add(etiquetaOpcion(ruta.esEntrega(), esCarpeta2));
        } else {
            opciones.add(etiquetaOpcion(false, esCarpeta2));
            opciones.add(etiquetaOpcion(true, esCarpeta2));
        }
    }

    private void CargarRutaTrabajo() {
        File archivoRuta1 = new File(getFilesDir(), RUTA_ACTIVA1);
        File archivoRuta2 = new File(getFilesDir(), RUTA_ACTIVA2);

        if (archivoRuta1.exists()) {
            VariablesGlobales.CarpetaLecturas = "/LECTURAMEDIDORES";
        } else if (archivoRuta2.exists()) {
            VariablesGlobales.CarpetaLecturas = "/LECTURAMEDIDORES2";
        }

        File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        String archivo_cargado = "";

        if (archivoNombre.exists()) {
            btnInfo.setImageResource(R.drawable.protecciondedatos);
        }
        else {
            btnInfo.setImageResource(R.drawable.protecciondedatos1);
            new AlertDialog.Builder(this)
                    .setTitle("VERIFICACION DE LA RUTA")
                    .setMessage(
                            "La ruta seleccionada no tiene datos.\n\n" +
                                    VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas() + "\n" +
                                    "*****************************"  + ")\n\n" +
                                    "ingrese a cargar en Comunícaciones."
                    )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();
        }


        // si no existe ninguno, se deja el valor con el que ya venia MarcaDeImpresora
    }

}