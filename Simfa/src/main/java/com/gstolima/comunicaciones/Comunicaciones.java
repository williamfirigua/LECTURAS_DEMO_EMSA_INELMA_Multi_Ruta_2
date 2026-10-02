package com.gstolima.comunicaciones;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.AsyncResponse;
import com.Util.DatosWS;
import com.Util.Utils;
import com.Util.UtilsNet;
import com.Util.WSSoap;
import com.gstolima.accesoyseguridad.GuiAcceso;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.Generica;
import com.gstolima.tablas.TablaRegistroSalida;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.gstolima.tablas.TablaClienteSalida;

public class Comunicaciones extends AppCompatActivity implements AsyncResponse {


    Button btnCargar;
    int OpcionActualizar = 0;
    EditText txtCiclo;
    EditText txtMunicipio;
    EditText txtSeccion;
    EditText txtDivision;
    ImageView iconWsOn;
    ImageView iconWsOff;

    String serialPDA = "000";
    private String TAG = "Comunicaciones";
    public String URL;
    String datoIP = "";
    String nivelOperador;
    String rutaAdministrador = "";

    String CicloReal = "000000000";//Ax se guardan en vez de capturar de txt...
    String Ciclo = "000";//Ax se guardan en vez de capturar de txt...
    String Municipio = "00";
    String Seccion = "0000";
    String Division = "0";
    String paginaWs = "";

    int trama = 50; //Ax: Por defecto
    int puerto = 85; //Ax: Por defecto
    int tamanoArchivo = 0; //Ax: tamano obtenido de lo que hay que bajar o tambien de lo que se va a enviar(descarga)
    int tamFilasArchivoD = 0;

    AlertDialog levelDialog;

    String metodo = ""; //Ax: lleva el metodo a ejecutar en Asyntask
    String banderaMet2llamo = ""; //Ax: metodo que llamo a metodo tamanoarchivo
    boolean banderaWsOcupado = false; //Ax: es para inhabilitar acciones mientras se eejcta asyntask
    boolean pruebawsok = false; //Ax: se envia a la nueva ventana para no probar 2 veces ws
    public final int REQUEST_CODE = 6878;//Ax para enviar a actividad

    DatosWS wsoap = new DatosWS("", "", "", 0);//Axx

    Utils util = new Utils();
    File logfile; //Ax: Es para los log de error

    TextView txtInfo;
    TextView lblTxtArchivo;

    String NombreArchivos_Env;
    String NombreZip = ""; //Ax: archivo zip por recibir
    String ArchivoAEnviarRecibir = ""; //Ax: Ruta servidor del archivo zip por recibir
    String RutaAGrabar = ""; //Ax: Ruta dispositivo del archivo zip por recibir
    String esComprimido = "";
    String tipodecarga = ""; //Ax: "cargai"  ,"cargaf",  descargai
    List<String> zips; //Ax: para guardar las fotos por enviar

    long tamLongitRegD = 0;

    //kim
    private Context context;
    private Autoupdater updater;
    private ProgressDialog dialog = null;
    private int currentVersionCode;
    String serial = "";
    String rutaAPK = "";
    boolean validaEnvio = false;
    String hash = "";
    int versionActualizar = 0;
    boolean traerRuta = false;
    boolean recibirDatos = false;
    int puertoAPI = 0;
    String cadenaURLapi = "";
    CrudComunicaciones crudComuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicaciones); //Ax: antes era: (R.layout.activity_main);

        crudComuni = new CrudComunicaciones(this);
        Bundle bundle = getIntent().getExtras();

        serialPDA = bundle.getString("CODIGO_INTERNO_PDA");
        nivelOperador = bundle.getString("NIVEL_OPERADOR");
        NombreArchivos_Env = bundle.getString("NombreArchivos_Env");

        txtCiclo = (EditText) findViewById(R.id.txtCiclo);
        txtMunicipio = (EditText) findViewById(R.id.txtMunicipio);
        txtSeccion = (EditText) findViewById(R.id.txtSeccion);
        txtDivision = (EditText) findViewById(R.id.txtDivision);
        iconWsOff = (ImageView) findViewById(R.id.iconwebserviceoff);
        iconWsOn = (ImageView) findViewById(R.id.iconwebserviceon);

        Button btnTraerRuta = (Button) findViewById(R.id.btnTraerRuta);
        Button btnRecibirDatos = (Button) findViewById(R.id.btnRecibirDatos);
        Button btnEnviarDatos = (Button) findViewById(R.id.btnEnviarDatos);
        Button btnValidaEnvio = (Button) findViewById(R.id.btnValidaEnvio);
        Button btnEnviarFotos = (Button) findViewById(R.id.btnEnviarFotos);
        Button btnBackupFotos = (Button) findViewById(R.id.btnBackupFotos);

        Button btnConfigurarWeb = (Button) findViewById(R.id.btnConfigurarWeb);
        Button btnBorrarBackupFotos = (Button) findViewById(R.id.btnBorrarBackupFotos);

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        lblTxtArchivo = (TextView) findViewById(R.id.lblTxtArchivo);

        wsoap.delegate = this;//Axx Ax: tarea asincrona para enviar datos aqui a processFinish

        //verificarArchivos();
        Log.e("INFO","verificaCarpetaLlena ANTES " + VariablesGlobales.CarpetaLecturas);
        verificaCarpetaLlena();
        cargarArchivoNombre();
        getParamsWs();
        ProbarConn();  //Ax: comunicaciones es el unico que comprueba primero la conexion, las otras actividades no.
        SetIconButtons();

        try {//kim version de codigo
            PackageInfo pckginfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            currentVersionCode = pckginfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        serial = serialPDA;//getSerialNumber();
        // currentVersionCode=39;
        rutaAPK = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/SIMLE" + (currentVersionCode) + ".APK";

        //   rutaApk = Environment.getExternalStorageDirectory().getAbsolutePath() + "//download//SIMLE39"  + ".APK";

        logfile = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG");

        btnTraerRuta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                traerRuta = true;
                // OpcionActualizar=3;
                // validarV();
                //comenzarActualizar(rutaAPK);

                TraerRuta();
            }
        });

        btnEnviarFotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnviarFotosMensaje();
            }
        });

        btnBorrarBackupFotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                borraBackupFotos();
            }
        });

        btnBackupFotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                crearBackupFotos();
            }
        });

        btnValidaEnvio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validaEnvio = true;
                validarEnvio();
            }
        });

        btnRecibirDatos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recibirDatos = true;
                //validarV();
                RecibirDatos();
            }
        });

        btnEnviarDatos.setOnClickListener(new View.OnClickListener() {//78
            public void onClick(View v) {
                EnviarDatosMensaje1();
            }
        });

        btnConfigurarWeb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Comunicaciones.this, Configurar_webIp.class);
                Log.e("INFO","toConfigWebIp -> nivelOperador: "+ nivelOperador);
                Log.e("INFO","toConfigWebIp -> pruebawsok: "+ pruebawsok);
                Log.e("INFO","toConfigWebIp -> trama: "+ trama);
                myIntent.putExtra("nivelOperador", nivelOperador);
                myIntent.putExtra("pruebawsok", pruebawsok);
                myIntent.putExtra("trama", trama + "");
                Comunicaciones.this.startActivityForResult(myIntent, REQUEST_CODE);
            }
        });

        //Ax: para regresar a main
        findViewById(R.id.btnRegresar).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });

        //kim: validar version de apk
        findViewById(R.id.btnValidarVersion).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //   comenzarActualizar();
                OpcionActualizar=0;
                validarV();
            }
        });

        Log.e("error", "variable modulo " + VariablesGlobales.moduloTrabajo);
        if (VariablesGlobales.moduloTrabajo.equals("")) {
            seleccionModuloTrabajo("Selecione [SI] para lecturas o [NO] para entregas ", "Seleccione modulo de trabajo");
        }

    }

    private boolean verificaCarpetaLlena(){
        try {
            boolean existeNombre1 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES/NOMBRE").exists();
            boolean existeNombre2 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES2/NOMBRE").exists();
            // EXISTEN DOS RUTAS
            if(VariablesGlobales.CarpetaLecturas.contains("LECTURAMEDIDORES2") && !existeNombre1 && !existeNombre2){
                VariablesGlobales.CarpetaLecturas = "/LECTURAMEDIDORES";
            }

            Log.e("INFO","verificaCarpetaLlena CarpetaLecturas " + VariablesGlobales.CarpetaLecturas);
            return true;
        }catch (Exception ex){
            Log.e("INFO","verificaCarpetaLlena error -> " + ex.getMessage());
            return false;
        }
    }
    private void salir() {
        //Ax: comentado genera problemas conexion printer
        //startActivity(new Intent(Comunicaciones.this, com.gstolima.accesoyseguridad.GuiAcceso.class));
        Intent iBackActivity = new Intent(this, GuiAcceso.class);
        setResult(RESULT_OK, iBackActivity);
        this.finish();
    }

    //Ax. para mensajes generales
    private void mensajes(String msg) {
        // Toast.makeText(Comunicaciones.this, msg, Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(Comunicaciones.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 50);
        toast.show();
    }

    //Ax: recibe el resultado ejecutado de la clase async task del metodo onPostExecute(result).
    public void processFinish(String output) {

    }

    //Ax: coloca iconos pequeños a los botones
    private void SetIconButtons() {

        try {

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.btnindocs42, null);
            Button btn = (Button) findViewById(R.id.btnRecibirDatos);
            drawable.setBounds(0, 0, 40, 40);
            btn.setCompoundDrawables(drawable, null, null, null);

            Drawable drawable2 = ResourcesCompat.getDrawable(getResources(), R.drawable.traerruta, null);
            Button btn2 = (Button) findViewById(R.id.btnTraerRuta);
            drawable2.setBounds(0, 0, 40, 40);
            btn2.setCompoundDrawables(drawable2, null, null, null);

//            Drawable drawable3 = ResourcesCompat.getDrawable(getResources(), R.drawable.borrarbckpfotos, null);
//            Button btn3 = (Button) findViewById(R.id.btnBorrarBackupFotos);
//            drawable3.setBounds(0, 0, 40, 40);
//            btn3.setCompoundDrawables(drawable3, null, null, null);

            Drawable drawable4 = ResourcesCompat.getDrawable(getResources(), R.drawable.enviarfotos, null);
            Button btn4 = (Button) findViewById(R.id.btnEnviarFotos);
            drawable4.setBounds(0, 0, 40, 40);
            btn4.setCompoundDrawables(drawable4, null, null, null);

            Drawable drawable5 = ResourcesCompat.getDrawable(getResources(), R.drawable.enviardatos, null);
            Button btn5 = (Button) findViewById(R.id.btnEnviarDatos);
            drawable5.setBounds(0, 0, 40, 40);
            btn5.setCompoundDrawables(drawable5, null, null, null);

            Drawable drawable6 = ResourcesCompat.getDrawable(getResources(), R.drawable.validarenvio, null);
            Button btn6 = (Button) findViewById(R.id.btnValidaEnvio);
            drawable6.setBounds(0, 0, 40, 40);
            btn6.setCompoundDrawables(drawable6, null, null, null);

        } catch (Exception ex) {
            mensajes("Error al cargar iconos");
        }
    }

    private void verificarArchivos() {

        String nombreArchivo = VariablesGlobales.directorioactual + "/ARCHIVOSLCARGA.CFI";
        File file = new File(nombreArchivo);

        //Ax: no existe "ArchivosLCarga.cfi" advertir y no hacer nada
        if (!file.exists()) {

            banderaWsOcupado = true;//tratar de bloquear toda funcion
            txtInfo.setText("No hay Archivo de Soporte...\n ARCHIVOSLCARGA.CFI");
            mensajes("No hay Archivo de Soporte...\n //ARCHIVOSLCARGA.CFI");
            return;
        }

        cargarArchivoNombre();

        int contador = 0;
        try {
            FileReader r = new FileReader(nombreArchivo);
            BufferedReader reader = new BufferedReader(r);
            String linea = "";

            while ((linea = reader.readLine()) != null) {

                if (contador < 1) { // tomar la ruta del sistema administrativo
                    rutaAdministrador = linea.trim();

                    if (!rutaAdministrador.contains(":")) {
                        mensajes("No hay Archivo de Soporte...\n //ARCHIVOSLCARGA.CFI");
                        btnCargar.setEnabled(true);
                        return;
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
        //seleccionUrl();
    }

    //Ax: Lee y carga del archivo "Nombre" si existe
    private void cargarArchivoNombre() {

        String linea;
        String archivo_nombre = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE";
        File file = new File(archivo_nombre);

        if (file.exists()) {//Ax: se comprueba y carga si existe

            try {
                FileReader r = new FileReader(archivo_nombre);
                BufferedReader reader = new BufferedReader(r);

                while ((linea = reader.readLine()) != null) {
                    if (linea.trim() == "") {
                        continue;
                    } else break;
                }

                try { // Estructura :  "123456789L012456.789"
                    // Estructura anterio:  "1234L12456.789"
                    CicloReal = linea.substring(0, 9).trim();
                    Ciclo = linea.substring(6, 9).trim();
                    int x = Integer.parseInt(Ciclo);//Ax: Si tiene caracteres raros fallara

                    Seccion = linea.substring(10, 14);
                    Municipio = linea.substring(14, 16);
                    Division = linea.substring(17, 20);

                    txtCiclo.setText(CicloReal);
                    txtMunicipio.setText(Municipio);
                    txtSeccion.setText(Seccion);
                    txtDivision.setText(Division);
                    lblTxtArchivo.setText(linea.substring(9, 20).trim());
                    return;

                } catch (Exception e) {
                    Log.e("ERROR","[Comunicaciones]CargarArchivoNombre erro-> " + e.getMessage());
                    CicloReal = "000000000";
                    Ciclo = "000";
                    Seccion = "0000";
                    Municipio = "00";
                    Division = "000";
                }
            } catch (Exception ex) {
                Log.e("ERROR","[Comunicaciones]CargarArchivoNombre erro-> " + ex.getMessage());
                mensajes("Problema con la carga de:\n Archivo: Ciclo, Municipio...etc.");
            }
        }

        txtCiclo.setText(CicloReal);
        txtMunicipio.setText(Municipio);
        txtSeccion.setText(Seccion);
        txtDivision.setText(Division);
    }

    //Ax: crea el archivo "Nombre" si la carga fue exitosa
    private void crearArchivoNombre() {

        String archivo_nombre = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE";
        File file = new File(archivo_nombre);

        if (file.exists()) file.delete();

        try {
            FileWriter writer = new FileWriter(file);

            String tmp = txtCiclo.getText().toString().trim();

            if (tmp.length() == 1) tmp = "0" + tmp;

            CicloReal = String.format("%1$9s", tmp); // "1234"  ... " 321" ... "  21"
            Ciclo = String.format("%1$3s", Ciclo.trim()).replace(" ", "0");
            Seccion = String.format("%1$4s", txtSeccion.getText().toString().trim()).replace(" ", "0");
            Municipio = String.format("%1$2s", txtMunicipio.getText().toString().trim()).replace(" ", "0");
            Division = String.format("%1$3s", txtDivision.getText().toString().trim()).replace(" ", "0");

            writer.append(CicloReal + "L" + Seccion + Municipio + "." + Division  + "   .");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            mensajes("Error al crear \nNombre");
            file.delete();
        }
    }

    // nuevo proceso para capturar la ippublica
   /* private void seleccionUrl() {
        try {
            String nombreArchivo = VariablesGlobales.directorioactual + "/DIRECCIONLIP.TXT";
            File file = new File(nombreArchivo);

            if (!file.exists()) {
                RandomAccessFile writer2 = new RandomAccessFile(file, "rw");

                String ip_puerto = "";//"162.214. 76. 70;  89;000                 ;GPRS                ;/hesego.asmx                                                                                                   " + ";A\n";

                writer2.writeBytes(ip_puerto);
                ip_puerto = "162.214. 76. 70;  89;000                 ;GPRS                ;/hesego.asmx                                                                                                   ;A\n" +
                        "000.000.000.000;8081;000                 ;GPRS                ;/hesego.asmx                                                                                                   ;I";

                writer2.writeBytes(ip_puerto);
                writer2.close();
            }

            if (file.exists()) {

                FileReader stream3 = new FileReader(nombreArchivo);
                BufferedReader reader = new BufferedReader(stream3);
                String linea;
                int cont = 0;

                while ((linea = reader.readLine()) != null) {

                    datoIP = linea;

                    if (datoIP.substring(datoIP.length() - 1, datoIP.length()).trim().equals("A")) {
                        URL = "http://" + datoIP.substring(0, 15).trim() + ":" + datoIP.substring(16, 20).trim();
                        paginaWs = datoIP.substring(64, 157).trim();
                        cont++;
                        break;
                    }
                }

                if (cont < 1) {
                    file.delete();
                    seleccionUrl();
                    return;
                }
                reader.close();
            }
            URL = URL.replace(" ", "");

        } catch (IOException e) {
            e.printStackTrace();
            mensajes("Error, verificar Archivo de Configuracion IP");
        }
    }*/

    public boolean getParamsWs() {
        try {
            BDComunicaciones bdc = crudComuni.getParams();
            if (bdc != null) {
                if (bdc.gethttpSeguro()==0)
                    URL = "http://" + bdc.getURL();
                else
                    URL = "https://" + bdc.getURL();

                paginaWs = bdc.getPaginaWs();
                rutaAdministrador = bdc.getRutaAdministrador() + "/";
                cadenaURLapi = bdc.getUrlApi();
                esComprimido = "1";
                //TENER EN CUENTA ESTE OTRO PARAMETRO OJO CONSULTAR CON MANUEL CUANDO SE USARIA TENIENDO MARCADO EL 1
                //cadenaURLapi = URL.substring(0,URL.lastIndexOf(":") + 1) + puertoAPI + "/api/";

                Log.e("INFO", "RutaAdministrador" + rutaAdministrador);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("INFO", "[Comunicaciones]getParamsWs()|" + ex.getMessage());
            return false;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    //trama = Integer.parseInt(data.getStringExtra("trama"));
                    //puerto = Integer.parseInt(data.getStringExtra("puerto"));
                    //pruebawsok = data.getBooleanExtra("pruebawsok", false);
                    URL = data.getStringExtra("url");
                    paginaWs = data.getStringExtra("paginaWs");

                } catch (Exception ex) {
                    mensajes("Error obteniendo info\n de Configurar IP");
                }
            }
        }
    }

    public void ProbarWS() {

        if (banderaWsOcupado) return; //Ax: si hay una conexion abierta no hace nada

        metodo = "VALIDAR_CONEXION";

        try {
            AsyncCallWS task = new AsyncCallWS();
            task.execute("");
            banderaWsOcupado = true;

        } catch (Exception ex) {
            mensajes(ex.getMessage());
        }
    }

    public void ProbarConn() {

        if (banderaWsOcupado) return; //Ax: si hay una conexion abierta no hace nada

        metodo = "HAY_CONEXION";

        try {
            AsyncCallWS task = new AsyncCallWS();
            task.contexto = this; //adjuntar el contexto
            task.execute("");
            banderaWsOcupado = true;

        } catch (Exception ex) {
            mensajes(ex.getMessage());
        }
    }

    public boolean ExisteNombre() {

        String archivo_nombre = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE";
        File file = new File(archivo_nombre);

        if (file.exists()) {
            mensajes("La terminal ya esta cargada \ndebe Borrar o Mover datos \ny cargar la nueva Ruta si\n es lo que requiere");
            return true;
        }
        return false;
    }

    public void TraerRuta() {

        if (banderaWsOcupado) return;

        if (!pruebawsok) {
            mensajes("No hay conexion con el Servicio web");
            return;
        }

        if (ExisteNombre()) return;

        try {
            metodo = "RUTAS_PROGRAMADAS";
            CicloReal = txtCiclo.getText().toString().trim();
            txtCiclo.setText(CicloReal);
            Log.e("INFO","TraerRuta_CicloReal: " + CicloReal);

            AsyncCallWS task = new AsyncCallWS();
            task.execute("");//Ax: continua aqui, en AsyncCallWS.doinback...
            banderaWsOcupado = true;//Bandera

        } catch (Exception ex) {
            mensajes("Error al traer ruta");
            banderaWsOcupado = false;//Bandera
        }
    }

    public void RecibirDatos() { //Ax: Se ejecuta totalmente despues de la comprobacion alertdialog en RecibirDatosRun

        if (banderaWsOcupado) return;

        if (!pruebawsok) {
            mensajes("No hay conexion con el Servicio web");
            return;
        }

        if (ExisteNombre()) return;

        AlertDialogEscoger("Alerta de transmision de datos!", "Desea cargar Archivos?", "RecibirDatos2");
    }

    public void RecibirDatos2() {

        try {

            Seccion = String.format("%1$4s", txtSeccion.getText().toString().trim()).replace(" ", "0");
            Municipio = String.format("%1$2s", txtMunicipio.getText().toString().trim()).replace(" ", "0");
            Division = String.format("%1$3s", txtDivision.getText().toString().trim()).replace(" ", "0");

        } catch (Exception ex) {
            mensajes("error al capturar Ciclo, Municip,Secc. y Div.");
            util.Log(logfile, "[Comunicaciones]RecibirDatos2(): " + ex.getMessage());
            return;
        }

        //NombreZip = Ciclo + Municipio + Division + "." + Seccion + ".ZIP";
        NombreZip = Seccion + Municipio   + "." + Division + ".ZIP";
        ArchivoAEnviarRecibir = rutaAdministrador.trim() + VariablesGlobales.moduloTrabajo + CicloReal + "\\L" + NombreZip;//antes C AX El Ws aun usa C
        RutaAGrabar = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "L" + NombreZip;

        lblTxtArchivo.setText("L" + NombreZip);
        txtCiclo.setText(CicloReal);
        txtMunicipio.setText(Municipio);
        txtSeccion.setText(Seccion);
        txtDivision.setText(Division);

        WsGetTamanoArchivo();
        banderaMet2llamo = "RecibirDatos3";
    }

    public void RecibirDatos3(String resp) {

        metodo = "ENVIAR_DEL_SERVIDOR_AL_PDA";
        AsyncCallWS task = new AsyncCallWS();
        task.execute("");//Ax: continua aqui, en AsyncCallWS.doinback...
        banderaWsOcupado = true;//Bandera conexion ocupada
    }

    public void RegistProgramacEnrut() {

        try {
            metodo = "RegistroProgramaEnrutador";
            AsyncCallWS task = new AsyncCallWS();
            task.execute("");//Ax: continua aqui, en AsyncCallWS.doinback...
            banderaWsOcupado = true;//Bandera conexion ocupada

        } catch (Exception ex) {
            mensajes(ex.getMessage());
            banderaWsOcupado = false;//Bandera conexion ocupada
            banderaMet2llamo = "";
        }
    }

    public void EnviarDatosMensaje1() {

        if (banderaWsOcupado) return;

        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        File file2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LEIDO.TXT");

        if (!file.exists()) {
            mensajes("Terminal NO tiene\n ruta cargada");
            return;
        }

        if (!file2.exists()) {
            AlertDialogEscoger("Alerta de transmision de datos!", "LAS RUTAS NO HAN SIDO LEIDAS\n" + "EN SU TOTALIDAD, DESEA CONTINUAR?", "EnviarDatosMensaje2");
            return;
        } else {
            EnviarDatosMensaje2();
        }
    }

    public void EnviarDatosMensaje2() {

        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA");

        //validar
        //nuevo para crear archivo EnviosGPRSHilos desde ENVIOGPRS" + serialPDA
        String EnviosGprsda = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOGPRS" + serialPDA + ".SDA";
        String archivoI = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/I" + NombreArchivos_Env;
        File Archivo3 = new File(archivoI);

        if (Archivo3.exists())
            Archivo3.delete();
        //copiar a EnviosGPRSHilos para el envio
        VariablesGlobales.copyFile(EnviosGprsda, archivoI, false);
        //-----------------------------------

        String archivoJ = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/J" + NombreArchivos_Env;
        File Archivo1 = new File(archivoJ);

        if (Archivo1.exists())
            Archivo1.delete();
        //copiar a EnviosGPRSHilos para el envio
        VariablesGlobales.copyFile(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA", archivoJ, false);
        //-----------------------------------------------------------

        String backupEnvioGPRS = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/BKENVIOSGPRS.SDA";
        String archivoH = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/J" + NombreArchivos_Env;
        File Archivo2 = new File(archivoH);

        if (Archivo2.exists())
            Archivo2.delete();
        //copiar a EnviosGPRSHilos para el envio
        VariablesGlobales.copyFile(backupEnvioGPRS, archivoH, false);

        //------------------------------------------------------------------------

        String archivoFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "ENVIOFOTOS" + serialPDA + ".SDA";
        String archivoW = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/W" + NombreArchivos_Env;
        File Archivo1F = new File(archivoW);

        if (Archivo1F.exists())
            Archivo1F.delete();
        //copiar a EnviosGPRSHilos para el envio
        VariablesGlobales.copyFile(archivoFotos, archivoW, false);

        //------------------------------------------------------------------------

        String backupFotos = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/BKEnvioFotos.SDA";
        String archivoX = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/X" + NombreArchivos_Env;
        File Archivo1X = new File(archivoX);

        if (Archivo1X.exists())
            Archivo1X.delete();
        //copiar a EnviosGPRSHilos para el envio
        VariablesGlobales.copyFile(backupFotos, archivoX, false);

        //------------------------------------------------------------------------

        if (!file.exists()) {
            AlertDialogEscoger("Alerta de transmision de datos!", "ALERTA! HAY RUTAS SIN\n" + "ENVIAR AL SERVIDOR, DESEA CONTINUAR?", "EnviarDatosMensaje3");
            return;
        } else {
            EnviarDatosMensaje3();
        }
    }

    public void EnviarDatosMensaje3() {
        AlertDialogEscoger("Alerta de transmision de datos!", "Desea enviar los datos ?\n" + "", "EnviarDatosServer");
        return;
    }

    public void EnviarDatosServer() {

        txtInfo.setText("");
        NombreZip = "D" + NombreArchivos_Env + ".ZIP"; // ciclo +Municipio + Division+ Seccion
        ArchivoAEnviarRecibir = rutaAdministrador.trim() + VariablesGlobales.moduloTrabajo + CicloReal + "\\";
        RutaAGrabar = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/";
        banderaMet2llamo = "EnviarDatosServer";//Bandera puesta, ya que otros metodos invocan este RegistProgramacEnrut
        tipodecarga = "descargai";

        try {
            metodo = "EnviarDatosServer";
            AsyncCallWS task = new AsyncCallWS();
            task.execute("");
            banderaWsOcupado = true;

        } catch (Exception ex) {
            mensajes(ex.getMessage());
            banderaWsOcupado = false;//Bandera conexion ocupada
            banderaMet2llamo = "";
            util.Log(logfile, "[Comunicaciones]EnviarDatosServer(): " + ex.getMessage());
        }
    }

    public String EnviarDatosServerAsync() {

        if (NombreArchivos_Env.equals("") || NombreArchivos_Env == null) {
            mensajes("Error en el archivo Nombre");
            return "Error en el archivo Nombre";
        }

        try {

            WSSoap wsoap = new WSSoap(URL, paginaWs);

            metodo = "VerificarSiexiste_Directorio_Archivo";
            String respuesta = wsoap.VerificarSiexisteDirectorioArchivo(metodo, ArchivoAEnviarRecibir, NombreZip);//Ax: Se comprueba si existe la ruta destino remota

            if (!respuesta.equals("true")) {
                return "Error No se ha encontrado destino en el servidor!";
            }
            //Ax: Se registra un Programacion Enrutados Descargai
            metodo = "RegistroProgramaEnrutador";
            //antes respuesta = wsoap.RegistrarProgramEnrutadosr(metodo, ArchivoAEnviarRecibir, tipodecarga, (tamanoArchivo + ""), (serialPDA.substring(serialPDA.length() - 3, serialPDA.length()) + ""), ArchivoAEnviarRecibir + NombreZip, NombreArchivos_Env);//(Municipio + Seccion + Division));
            // respuesta = wsoap.RegistrarProgramEnrutadosr(metodo, VariablesGlobales.moduloTrabajo + CicloReal, tipodecarga, (tamanoArchivo + ""), serialPDA, (Ciclo + Municipio + Division + "." + Seccion), 0, 0);

          /*  if (!respuesta.equals("1")) {
                return "Error No se ha podido registrar Descargai";
            }*/

            String ext = NombreArchivos_Env.substring(NombreArchivos_Env.indexOf("."), NombreArchivos_Env.length());

            if (Integer.parseInt(esComprimido) == 1) { //Ax: Se procede a crear el ZIP

                ArrayList filestoZip = new ArrayList();
                File f = new File(RutaAGrabar);//Ax: ruta donde buscar los archivos
                File[] files = f.listFiles();//array de Files de la carpeta

                for (int i = 0; i < files.length; i++) {

                    File file = files[i];
                    String noPermidtido1 = "E" + NombreArchivos_Env; //Ax: archivos que no se envian : M25160.034  E....
                    String noPermidtido2 = "M" + NombreArchivos_Env;

                    if (!file.isDirectory() && file.getName().toUpperCase().endsWith(ext) && !file.getName().toUpperCase().equals(noPermidtido1) && !file.getName().toUpperCase().equals(noPermidtido2)) {
                        Log.e("error", "que anda 1 " + file);
                        filestoZip.add(file);
                    }
                }

                //antes    String comprimir = util.CreaZip4j(RutaAGrabar + NombreZip, filestoZip);
                String comprimir = util.CreaZip(RutaAGrabar + NombreZip, filestoZip);

                if (!comprimir.contains("✓")) {
                    Log.e("error", "entor por ese camino 1 " + RutaAGrabar + "-" + NombreZip);
                    return "Error Comprimiendo \n Archivos por enviar";
                }
            }
            hash = util.Md5Hash(RutaAGrabar + NombreZip);
            Log.e("error", "hash archivo " + RutaAGrabar + "-" + NombreZip);

            tamanoArchivo = (int) (new File(RutaAGrabar + NombreZip).length());

            metodo = "Terminal_ToServerReceive";
            respuesta = wsoap.TerminalToServerReceive(metodo, ArchivoAEnviarRecibir, 1, RutaAGrabar + NombreZip, trama);//Enviar Datos al servidor
//por ahora     respuesta = wsoap.TerminalToServerReceive(metodo, ArchivoAEnviarRecibir, RutaAGrabar + NombreZip, trama, Integer.parseInt(esComprimido));//Enviar Datos al servidor
            Log.e("error", "respuesta " + respuesta);
            if (!(respuesta.equals("4") || respuesta.equals("1"))) {
                return "Error Enviar Archivo al servidor";
            }
            tipodecarga = "descargaf";

            //Ax: Se registra un Programacion Enrutados Descargaf
            //se comeenta metodo = "RegistroProgramaEnrutador";
            //antes respuesta = wsoap.RegistrarProgramEnrutadosr(metodo, ArchivoAEnviarRecibir, tipodecarga, (tamanoArchivo + ""), (serialPDA.substring(serialPDA.length() - 3, serialPDA.length()) + ""), ArchivoAEnviarRecibir + NombreZip, NombreArchivos_Env);//(Municipio + Seccion + Division));
            Log.e("error", "data " + (ArchivoAEnviarRecibir) + "-" + tamanoArchivo + "-" + serialPDA + "-" + (RutaAGrabar + NombreZip));
            // se comeenta  respuesta = wsoap.RegistrarProgramEnrutadosr(metodo, ArchivoAEnviarRecibir, tipodecarga, (tamanoArchivo + ""), serialPDA, (Ciclo + Municipio + Division + "." + Seccion), 0, 0);

          /*se comenta  if (!respuesta.equals("1")) {
                return "Error No se ha podido registrar Descargaf";
            } */

            //antes metodo = "ProcesarDescomprimidoPC";
            metodo = "Descomprime";
            //antes respuesta = wsoap.DirectorioDescomprimir(metodo, ArchivoAEnviarRecibir, ArchivoAEnviarRecibir + NombreZip, "", "xx", trama);//Ax: todo: reintentos
            respuesta = wsoap.Descomprimir(metodo, ArchivoAEnviarRecibir, ArchivoAEnviarRecibir + NombreZip, hash, NombreArchivos_Env);

            if (!respuesta.equals("true")) {
                return " Error descomprimir remoto, intentelo de nuevo";
            }

            File file = new File(RutaAGrabar + NombreZip);

            if (file.exists()) file.delete();

        } catch (Exception ex) {
            mensajes("Error al enviar archivos al servidor\n, intente de nuevo");
            txtInfo.setText("Error al enviar archivos al servidor, intente de nuevo");
            util.Log(logfile, "[Comunicaciones]EnviarDatosServerAsync(): " + ex.getMessage());
        }
        return "Proceso subir archivos Exitoso! ✓";
    }

    public void EnviarFotosMensaje() {

//        RutaAGrabar = VariablesGlobales.directorioactual + "/FOTOGRAFIASL/";
//
//        File f = new File(RutaAGrabar);
//
//        if (!f.exists()) {
//
//            if (!f.mkdirs()) {
//                mensajes("No se pudo crear carpeta de fotografia(s)");
//                txtInfo.setText("No se pudo crear carpeta de fotografia(s)");
//            }
//            mensajes("No existe carpeta de fotografia(s)");
//            txtInfo.setText("No existe carpeta de fotografia(s)");
//            return;
//        }

        util.MensajeTime("Debe realizar este proceso en el MENÚ DE LIQUIDACIÓN", "DESHABILITADO", this, 4);

//        if (banderaWsOcupado) return;
//
//        AlertDialogEscoger("Alerta de transmision de datos!", "Desea enviar las fotos?", "EnviarFotos");
//        return;
    }

    /***
     * Busca la ruta de backup y hace una copia de la carpeta de fotos al backup
     */
    private void crearBackupFotos() {

        List<String> ListaFotos = new ArrayList<String>(); //Ax: Contendra lista de solo  archivos JPG,PNG

        File pathFotos = new File(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL");//Ax: ruta donde buscar los archivos

        try {
            File[] files = pathFotos.listFiles();//array de Archivos y carpetas de la ruta

            for (int i = 0; i < files.length; i++) {

                File file = files[i];

                if (!file.isDirectory() && file.getName().toUpperCase().endsWith(".JPG") || !file.isDirectory() && file.getName().toUpperCase().endsWith(".PNG")) {
                    ListaFotos.add(file.getAbsolutePath());
                }
            }

            if (files.length < 1) {
                mensajes(" Nada para backup ");
                return;
            }

            int cont = 0;

            if (ListaFotos.size() > 0) {

                File pathFotosBck = new File(VariablesGlobales.directorioBackUp + "/DCIM/FOTOGRAFIASL");

                if (!pathFotosBck.exists()) {
                    pathFotosBck.mkdirs();
                }

                for (String fullPath : ListaFotos) {
                    String fb = fullPath.replace(VariablesGlobales.directorioactual, VariablesGlobales.directorioBackUp).replace("//", "/");
                    if (!util.CrearCopiaSin(fullPath, fb)) {
                        cont++;
                    }
                }
            }

            if (cont > 0) {
                mensajes(cont + " archivos problematicos");
            } else {
                mensajes(" Backup Finalizado ");
            }

        } catch (Exception ex) {
            mensajes("Error al crear Backup Fotos");
        }
    }

    /***
     * Busca la ruta de backup fotos y la borra
     */
    private void borraBackupFotos() {

        try {
            int cont = 0;
            File pathFotosBck = new File(VariablesGlobales.directorioBackUp + "/FOTOGRAFIASL");

            if (pathFotosBck.isDirectory()) {
                String[] children = pathFotosBck.list();
                for (int i = 0; i < children.length; i++) {
                    if (!new File(pathFotosBck, children[i]).delete()) {
                        cont++;
                    }
                }
                if (children.length < 1) {
                    mensajes(" Nada por borrar ");
                    return;
                }
            }

            if (cont > 0) {
                mensajes(cont + " archivos problematicos");
            } else {
                mensajes(" Borrado Finalizado ");
            }

        } catch (Exception ex) {
            mensajes("Error al crear Backup Fotos");
        }
    }

    public void EnviarFotos() {

        try {
            String respuesta;
            int cantFotos = 20;
            int contadorZipFotos = 1;
            NombreZip = "FOTOS"  + Seccion+ Municipio  + Division;//No tiene el .ZIP

            zips = new ArrayList<String>(); //Ax: Contendra solo lista de archivos ZIP
            List<String> fotos = new ArrayList<String>(); //Ax: Contendra lista de solo  archivos JPG

            File f = new File(RutaAGrabar);//Ax: ruta donde buscar los archivos
            File[] files = f.listFiles();//array de Archivos y carpetas de la ruta

            int desde = 31;//26
            int hasta = 32;//27

            FileReader r = new FileReader(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/F" + NombreArchivos_Env); //Ax: se recorre el archivo para capturar las no eviadas o en envio
            BufferedReader reader = new BufferedReader(r);
            String linea;
            String UltimaFoto = "";
            while ((linea = reader.readLine()) != null) {

                //before 25
                if (linea.substring(desde, hasta).equals("X") || linea.substring(desde, hasta).equals("_")) {
                    if (!UltimaFoto.trim().equals(linea.substring(0, 30).trim())) {
                        UltimaFoto = linea.substring(0, 30).trim();
                        File fotop = new File(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + linea.substring(0, 30).trim());

                        if (fotop.exists()) {
                            //como buscar en una lista en android
                            if (!fotos.contains(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + linea.substring(0, 30).trim())) {
                                //Log.e("error", "No contiene a " + VariablesGlobales.directorioactual + "/FOTOGRAFIASL/" + linea.substring(0, 20).trim());
                                fotos.add(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + linea.substring(0, 30).trim());
                            }
                        }
                    }
                }
            }
            r.close();

            if (fotos.size() > 0) {

                int i = 1;
                ArrayList filestoZip = new ArrayList(); //Contiene los archivos por comprimir

                for (String x : fotos) { //Recorrer cada jpg encontrado para añadirlo

                    filestoZip.add(new File(x));

                    if (filestoZip.size() == cantFotos || i == fotos.size()) {//Si el arreglo llega a el limite de fotos por comprimir en una tanda

                        String nombrearchivozip;

                        if (contadorZipFotos <= 9) {
                            nombrearchivozip = RutaAGrabar + NombreZip + contadorZipFotos + ".ZIP"; //Ej: /sdcard01/Fotogra.../FOTOS....

                        } else { //Ax: si el munero es mas grande que 10 se cambia el ultimo numero del nombre por una letra
                            nombrearchivozip = sufijo(contadorZipFotos);
                            nombrearchivozip = RutaAGrabar + NombreZip + nombrearchivozip + ".ZIP";
                        }
                        contadorZipFotos++;

                        //  zips.add(nombrearchivozip);//Guarda nombre de los zips, esto sera usado en el envio asincrono

                        //util.LogFoto(LogFotos, nombrearchivozip + ";NO ");//Escribir en el log de fotos cuales sera los zip creado, con la palabra NO//Ya no se usa (zips)

                        respuesta = util.CreaZip(nombrearchivozip, filestoZip);

                        if (!respuesta.contains("✓")) {
                            Log.e("error", "Entro por este camino 2 " + RutaAGrabar + "-" + NombreZip);
                            mensajes("Error Comprimiendo \n Archivos por enviar");
                            txtInfo.setText("Error Comprimiendo \n Archivos por enviar");
                            return;
                        } else
                            zips.add(nombrearchivozip);//Guarda nombre de los zips, esto sera usado en el envio asincrono

                        filestoZip = new ArrayList();
                    }
                    i++;
                }//end for

                txtInfo.setText("");
                ArchivoAEnviarRecibir = rutaAdministrador.trim() + VariablesGlobales.moduloTrabajo + CicloReal + "\\";

                try {
                    metodo = "EnviarFotosServer";
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute("");
                    banderaWsOcupado = true;

                } catch (Exception ex) {
                    mensajes(ex.getMessage());
                    banderaWsOcupado = false;//Bandera conexion ocupada
                    banderaMet2llamo = "";
                    util.Log(logfile, "[Comunicaciones] EnviarFotosServer(): " + ex.getMessage());
                }

            } else {
                mensajes("No hay fotografia(s) para enviar");
                txtInfo.setText("No hay Fotografia(s) para enviar");
            }
        } catch (Exception ex) {
            mensajes("Error en el envio de fotos \n " + ex.getMessage());
            util.Log(logfile, "[Comunicaciones] EnviarFotosServer().: " + ex.getMessage());
        }
    }

    public String EnviarFotosServerAsync() {

        try {

            WSSoap wsoap = new WSSoap(URL, paginaWs);

            for (String zip : zips) { //Recorrer cada .zip creado para tratar de enviarlo


                File file = new File(zip);
                hash = util.Md5Hash(zip);
                Log.e("error", hash + " zip " + zip);
                tamanoArchivo = (int) file.length();


                metodo = "VerificarSiexiste_Directorio_Archivo";
                String respuesta = wsoap.VerificarSiexisteDirectorioArchivo(metodo, ArchivoAEnviarRecibir, file.getName());//Ax: Se comprueba si existe la ruta destino remota
                Log.e("error", "respuesta 1f " + respuesta);
                if (!respuesta.equals("true")) {
                    return "Error No se ha encontrado destino en el servidor!";
                }

                metodo = "Terminal_ToServerReceive";
                respuesta = wsoap.TerminalToServerReceive(metodo, ArchivoAEnviarRecibir, 1, zip, trama);//Enviar Datos al servidor
                //  respuesta = wsoap.TerminalToServerReceive(metodo, ArchivoAEnviarRecibir, zip, trama, Integer.parseInt(esComprimido));//Enviar Datos al servidor
                Log.e("error", "respuesta 2f " + respuesta + "-" + ArchivoAEnviarRecibir + "-" + zip);

                if (!(respuesta.equals("4") || respuesta.equals("1"))) {
                    return "Error Enviar Archivo al servidor";
                }

                // ArchivoAEnviarRecibir=ArchivoAEnviarRecibir+"Fotogra..."+ "\\";
                metodo = "Descomprime";
                respuesta = wsoap.Descomprimir(metodo, ArchivoAEnviarRecibir + "FOTOGRAFIAS", ArchivoAEnviarRecibir + file.getName(), hash, serialPDA);
                // respuesta = wsoap.DirectorioDescomprimir(metodo, ArchivoAEnviarRecibir, ArchivoAEnviarRecibir + file.getName(), "", "xx", trama);
                Log.e("error", "respuesta 3f " + respuesta + "-" + ArchivoAEnviarRecibir + "-" + file.getName() + "-*" + NombreArchivos_Env + "-" + hash);

                if (!respuesta.equals("true")) {
                    return " Error descomprimir remoto,\n Intentelo de nuevo";
                }

                file.delete();
            }//end foreach

        } catch (Exception ex) {
            mensajes("Error al enviar fotos al servidor\n, intente de nuevo");
            txtInfo.setText("Error al enviar fotos al servidor, intente de nuevo");
            util.Log(logfile, "[Comunicaciones]EnviarFotosServerAsync(): " + ex.getMessage());
        }
        return "Proceso subir Fotos Exitoso! ✓";
    }

    public String sufijo(int num) {
        String st = "";

        switch (num) {
            case 10:
                st = "A";
                break;
            case 11:
                st = "B";
                break;
            case 12:
                st = "C";
                break;
            case 13:
                st = "D";
                break;
            case 14:
                st = "E";
                break;
            case 15:
                st = "F";
                break;
            case 16:
                st = "G";
                break;
            case 17:
                st = "H";
                break;
            case 18:
                st = "I";
                break;
            case 19:
                st = "J";
                break;
            case 20:
                st = "K";
                break;
            case 21:
                st = "L";
                break;
            case 22:
                st = "M";
                break;
            case 23:
                st = "N";
                break;
            case 24:
                st = "0";
                break;
            case 25:
                st = "P";
                break;
            case 26:
                st = "Q";
                break;
            case 27:
                st = "R";
                break;
            case 28:
                st = "S";
                break;
            case 29:
                st = "T";
                break;
            case 30:
                st = "U";
                break;
            case 31:
                st = "V";
                break;
            case 32:
                st = "X";
                break;
            case 33:
                st = "Y";
                break;
            default:
                if (num > 33)
                    st = "Z";
                break;
        }
        return st;
    }

    private void validarEnvio() {

        if (banderaWsOcupado) return;

        txtInfo.setText("");
        esComprimido = "0";
        NombreZip = "D" + NombreArchivos_Env; //Ojo solo el D, no zip
        ArchivoAEnviarRecibir = rutaAdministrador.trim() + VariablesGlobales.moduloTrabajo + CicloReal + "\\" + NombreZip;
        banderaMet2llamo = "validarEnvio";//Bandera puesta, ya que otros metodos invocan este RegistProgramacEnrut

        try {
            TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
            infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/D" + NombreArchivos_Env);
            infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());
            tamLongitRegD = infoRegistroSalida.getLongitudRegistro();
            tamFilasArchivoD = infoRegistroSalida.getTotal_TablaRegistroSalida();
            infoRegistroSalida.Cerrar_TablaRegistroSalida();

            metodo = "OBTENER_LONGITUD_ARCHIVO_2";

            AsyncCallWS task = new AsyncCallWS();
            task.execute("");//Ax: continua aqui, en AsyncCallWS.doinback...
            banderaWsOcupado = true;//Bandera conexion ocupada

        } catch (Exception ex) {
            mensajes("OBTENER_LONGITUD_ARCHIVO_2 " + "Error al Obtener Tamano de Archivo");
            banderaWsOcupado = false;//Bandera conexion ocupada
            banderaMet2llamo = "";
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialogEscoger("Alerta de cierre!", "¡¡ SE INTERRUMPIRAN PROCESOS \nDE CARGA o DESCARGA !!", "salir");
    }

    public void WsGetTamanoArchivo() {

        try {
            metodo = "OBTENER_LONGITUD_ARCHIVO";

            AsyncCallWS task = new AsyncCallWS();
            task.execute("");//Ax: continua aqui, en AsyncCallWS.doinback...
            banderaWsOcupado = true;//Bandera conexion ocupada

        } catch (Exception ex) {
            mensajes("Error al Obtener Tamano de Archivo");
            banderaWsOcupado = false;//Bandera conexion ocupada
            banderaMet2llamo = "";
        }
    }

    //Ax: permite llamado asincrono para ejecutar conexion al Ws
    private class AsyncCallWS extends AsyncTask<String, Integer, Void> {

        ProgressDialog pDialog;
        public String respuesta;
        int x = 0;
        public Context contexto;

        @Override
        protected Void doInBackground(String... params) {

            Log.e("INFO","AsyncCallWS - URL, paginaWs" + URL + paginaWs);
            WSSoap wsoap = new WSSoap(URL, paginaWs);

            try {

                switch (metodo) {
                    case "EnviarDatosServer":
                        respuesta = EnviarDatosServerAsync();
                        break;

                    case "EnviarFotosServer":
                        respuesta = EnviarFotosServerAsync();
                        break;

                    case "HAY_CONEXION":

                        UtilsNet utilnet = new UtilsNet();
                        boolean hayI = utilnet.hayInternet(contexto);

                        if (!hayI) {
                            respuesta = "Hay problemas con la CONEXION";
                        } else {
                            respuesta = "Conexion Ok";
                        }
                        break;

                    case "VALIDAR_CONEXION":
                        //respuesta = wsoap.verificarWs(metodo);
                        respuesta = wsoap.verificarWs2(metodo);
                        break;

                    case "RUTAS_PROGRAMADAS":
                        String nuevoserial = serialPDA;//.substring(serialPDA.length() - 3, serialPDA.length());
                        String ruta = rutaAdministrador + VariablesGlobales.moduloTrabajo + CicloReal;
                        Log.e("INFO", "[AsyncCallWs] RUTAS_PROGRAMADAS - rutaAdministrador" + rutaAdministrador + " | ruta -> " + ruta + "-" + nuevoserial);
                        respuesta = wsoap.rutasProgramadas(metodo, nuevoserial, ruta); //Puede devolver 2 ó 10 rutas
                            Log.e("error", "datos " + ruta + nuevoserial);
                        break;

                    case "OBTENER_LONGITUD_ARCHIVO":
                        Log.e("error", "data " + ArchivoAEnviarRecibir + "-" + esComprimido + "-" + CicloReal + "-" + ( Seccion + Municipio + "." + Division ));
                        respuesta = wsoap.ObtenerLongitudArchivo(metodo, ArchivoAEnviarRecibir, Integer.parseInt(esComprimido), CicloReal,  Seccion + Municipio   + "." + Division ); //25160.051
                        break;

                    case "OBTENER_LONGITUD_ARCHIVO_2":
                        respuesta = wsoap.ObtenerLongitudArchivo("OBTENER_LONGITUD_ARCHIVO", ArchivoAEnviarRecibir, Integer.parseInt(esComprimido), CicloReal, NombreArchivos_Env); //"...",D://carpeta.../D25160.034,0,25,25160.034
                        break;

                    case "RegistroProgramaEnrutador"://Dirciclo,condicion,tamano,serial,rutarchivo,nombrearchivo

                        int xx = ArchivoAEnviarRecibir.lastIndexOf("\\");
                        String remotepath = ArchivoAEnviarRecibir.substring(0, xx);
                        String archivrec = ArchivoAEnviarRecibir;
                        respuesta = wsoap.RegistrarProgramEnrutadosr(metodo, VariablesGlobales.moduloTrabajo + CicloReal, tipodecarga, (tamanoArchivo + ""), serialPDA, (Seccion + Municipio  + "." + Division), 0, 0);
                        break;

                    case "ENVIAR_DEL_SERVIDOR_AL_PDA":
                        Log.e("error", "datos " + ArchivoAEnviarRecibir + "-" + RutaAGrabar + "-" + trama + "-" + tamanoArchivo);
                        respuesta = wsoap.ObtenerArchivo(metodo, ArchivoAEnviarRecibir, RutaAGrabar, trama, tamanoArchivo, false); //Segun victor el ultimo parametro no borra el archivo zip geenrdo en server
                        break;
                    case "ValidarVersionSIMFACT":
                        //  Log.e("error", "entra al validar");
                        respuesta = versionSimfa();//before traerArchivo  versionSIMLE
                        break;
                    case "OBTENER_LONGITUD":
                        respuesta = wsoap.ObtenerLongitudArchivo("OBTENER_LONGITUD_ARCHIVO", rutaAdministrador, Integer.parseInt(esComprimido), Ciclo.trim().toUpperCase(), Seccion.trim() + Municipio.trim()  + Division.trim());
                        break;
                    case "TraerVersionSIMFACT":
                        Log.e("error", "entra al validar");
                        respuesta = traerArchivo();//before traerArchivo  versionSIMLE
                        break;
                }

            } catch (Exception e) {
                Log.e("error", "envio f " + e.getMessage());
                respuesta = "Error, valide conexion con Ws";
                banderaMet2llamo = "";
                pruebawsok = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            banderaWsOcupado = false;
            String msg = "";
            boolean bul = true;

            switch (metodo) {
                case "VALIDAR_CONEXION":
                    metodo = "";
                    try {
                        Integer.parseInt(respuesta);//genera excepcion si diferente de 1
                        iconWsOff.setVisibility(View.INVISIBLE);
                        iconWsOn.setVisibility(View.VISIBLE);
                        msg = "Conexion con Webservice ✓";

                    } catch (NumberFormatException e) {
                        iconWsOff.setVisibility(View.VISIBLE);
                        iconWsOn.setVisibility(View.INVISIBLE);
                        bul = false; //Ws no respopndio
                        msg = "Error de conexion con Webservice";
                    }
                    break;

                case "RUTAS_PROGRAMADAS":
                    metodo = "";
                    if (respuesta.contains(";")) {

                        respuesta = respuesta.replace(" ", "").trim();
                        String[] resp = respuesta.split(";");
                        traerRuta = false;

                        if (resp.length < 2){
                            setNombreArgs(respuesta);
                        } else {
                            AlertDialogEscoger(resp); //Ax:mostrar opcion
                        }

                        txtCiclo.setEnabled(false);
                        txtMunicipio.setEnabled(false);
                        txtSeccion.setEnabled(false);
                        txtDivision.setEnabled(false);

                    } else {
                        msg = "Server: " + respuesta;
                    }
                    break;

                case "OBTENER_LONGITUD_ARCHIVO":
                    metodo = "";
                    try {
                        int x = Integer.parseInt(respuesta);
                        Log.e("error", "longitud " + x);
                        tamanoArchivo = x;
                        if (x > 0) { //Ax: Puede responder -1,-2-0
                            switch (banderaMet2llamo) {
                                case "RecibirDatos3":
                                    tipodecarga = "cargai";
                                    //Ax: aqui corre otra tarea paralela
                                    RegistProgramacEnrut(); //Ax: primero va a REGISTRAR_PROGRAMACION_ENRUTADOR  //Aqui colocar lo de fotos?  !chEnviaFotos.Checked

                                    break;
                            }
                        } else if (x == -99) {
                            msg = "Ciclo cerrado.";
                            tamanoArchivo = 0;
                        } else {
                            msg = "Error al obtener Longitud de archivo.";
                            tamanoArchivo = 0;
                        }

                    } catch (NumberFormatException ex) {
                        msg = respuesta;
                        tamanoArchivo = 0;
                    }
                    break;

                case "RegistroProgramaEnrutador": //Este metodo crea un registro de inicio y fin de transacciones como auditoria

                    if (respuesta.equals("1")) {

                        switch (banderaMet2llamo) {
                            case "RecibirDatos3":
                                msg += txtInfo.getText() + "Reg. Prog. Enrutador ✓ " + tipodecarga + "; ";
                                RecibirDatos3(respuesta); //Ax: luego va a ENVIAR_DEL_SERVIDOR_AL_PDA //Otra tarea paralela
                                break;//
                            default:
                                break;
                        }
                    } else {
                        msg += respuesta + "; ";
                        banderaMet2llamo = "";
                    }
                    break;

                case "ENVIAR_DEL_SERVIDOR_AL_PDA":

                    if (respuesta.length() == 1) {//"1" bajados sin borrado "2" bajado y borrado
                        msg = "Datos cargados ✓; " + "; ";

                        if (respuesta.equals("2"))
                            msg = "Datos cargados ✓;" + "; ";

                        String zipMsg = util.DescomprimeZip(RutaAGrabar, "", tamanoArchivo, true); //Ax: descomprimir archivos

                        msg += zipMsg + "; ";

                        if (zipMsg.contains("Descomprime ✓")) {

                            //Axx: aqui iba el cambio de hora y fecha, se salta este paso (pendiente todo)
                            //Ax movarch no se usa, ahora todo_ esta en una sola carpeta
                            // String movarch = util.MoverArchivosPorTipo(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/", VariablesGlobales.directorioactual + "VariablesGlobales.getCarpetaLecturas()+"/", ".SDA");//Ax: Mover Archivos Por extension

                            //  msg += movarch + "; ";

                            //  if (movarch.equals("Mover ✓")) {

                            crearArchivoNombre();
                            tamanoArchivo = 0;
                            tipodecarga = "cargaf";
                            banderaMet2llamo = "";//Ax evito que vuelva a bajar datos

                            RegistProgramacEnrut(); //Ax: aqui corre otra tarea paralela
                            //  }
                        }

                    } else {
                        msg = "Error: " + respuesta;
                    }
                    //metodo = "";
                    break;

                case "EnviarDatosServer": //Ax: no funciona?
                    msg = respuesta;
                    if (respuesta.contains("✓")) {
                        iconWsOff.setVisibility(View.INVISIBLE);
                        iconWsOn.setVisibility(View.VISIBLE);
                    }
                    break;

                case "EnviarFotosServer": //Ax: no funciona?
                    msg = respuesta;
                    if (respuesta.contains("✓")) {
                        Generica gen = new Generica();
                        gen.WriteFileByPos("_", "Y", false); //Busca '_' y sobreescribe 'Y' y No crea archivo por enviar

                        iconWsOff.setVisibility(View.INVISIBLE);
                        iconWsOn.setVisibility(View.VISIBLE);
                    }
                    break;

                case "HAY_CONEXION":
                    msg = respuesta;

                    if (msg.toLowerCase().contains("ok")) {
                        ProbarWS();
                    }
                    break;

                case "ValidarVersionSIMFACT":
                    // msg = respuesta;
                    Log.e("error", "respuesta version " + respuesta);
                    try {
                        if (Integer.parseInt(respuesta.trim()) == 0) {

                            if (!traerRuta && !recibirDatos) {
                                mensajes("No hay actualizaciones ");
                            } else {
                                validateMonth();
                            }

                        } else if (Integer.parseInt(respuesta.trim()) > 0) {

                            versionActualizar = Integer.parseInt(respuesta.trim());
                            String versionActual2=respuesta.trim();
                            if (respuesta.trim().length()==1) {
                                versionActual2="0"+respuesta.trim();
                                //versionActualizar = "0"+ respuesta.trim();
                            }

                            ///mirar si me puedo trar la version actual del instalador
                            File downloadDir = Environment.getExternalStorageDirectory();
                            File apkFile = null;
                            int versionLocal = -1;

                            // Buscar archivos en la carpeta download/
                            File[] archivos = new File(downloadDir, "download").listFiles();

                            if (archivos != null) {
                                for (File archivo : archivos) {
                                    String nombre = archivo.getName();
                                    if (nombre.startsWith("SIMLE") && nombre.endsWith(".APK")) {
                                        // Extraer los dos dígitos de la versión
                                        String versionStr = nombre.substring(5, 7);  // SIMLEXX.APK
                                        try {
                                            int v = Integer.parseInt(versionStr);
                                            if (v > versionLocal) { // por si hay varias
                                                versionLocal = v;
                                                apkFile = archivo;
                                            }
                                        } catch (NumberFormatException e) {
                                            mensajes("Error tratando de conseguir si hay un SIMLEnn.APK ");
                                        }
                                    }
                                }
                            }

                            Log.d("APK", "Versión local encontrada: " + versionLocal);
                            Log.d("APK", "Versión servidor: " + versionActualizar);
                            rutaAPK = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/SIMLE" + versionActual2 + ".APK";
                            if (versionLocal == -1) {
                                // No hay APK
                                Log.d("APK", "No hay ninguna versión local. Descargar nueva.");
                                //  util.MensajeTime("Hay una nueva version, la descarga se iniciara SIMLE se intentara traer -->"+rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                                // mensajes("Hay una nueva version, la descarga se iniciara SIMLE"+respuesta.trim()+".APK");

                                // TraerVersionSimfa();

                                AlertDialog.Builder builder = new AlertDialog.Builder(Comunicaciones.this);
                                builder.setTitle("Alerta de actualización");
                                builder.setMessage("Hay una nueva versión disponible.\n\nSe intentará traer:\n" + rutaAPK + "\n\n¿Deseas actualizar?");
                                builder.setIcon(R.drawable.ic_launcher);

                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (OpcionActualizar==0)
                                            TraerVersionSimfa();
                                        else
                                            util.MensajeTime("Hay una nueva version, debes intalar en el boton actualizar te traera la -->"+rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);

                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();


                            } else if (versionActualizar > versionLocal) {
                                // Servidor tiene versión más nueva
                                Log.d("APK", "Versión servidor es mayor. Borrar local y descargar nueva.");
                                if (apkFile != null && apkFile.exists()) {
                                    apkFile.delete();
                                }

                                //util.MensajeTime("Hay una nueva version, la descarga se iniciara SIMLE intentara traer -->"+respuesta.trim()+" --- "+rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                                //mensajes("Hay una nueva version, la descarga se iniciara SIMLE"+respuesta.trim()+".APK");
                                //TraerVersionSimfa();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Comunicaciones.this);
                                builder.setTitle("Alerta de actualización");
                                builder.setMessage("Hay una nueva versión disponible.\n\nSe intentará traer:\n" + rutaAPK + "\n\n¿Deseas actualizar?");
                                builder.setIcon(R.drawable.ic_launcher);

                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (OpcionActualizar==0)
                                            TraerVersionSimfa();
                                        else
                                            util.MensajeTime("Hay una nueva version, debes instalar en el boton actualizar esta te traera la APK-->"+rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();



                            } else if (versionActualizar == versionLocal) {
                                // Ya está actualizada
                                if (OpcionActualizar==0) {
                                    Log.d("APK", "Ya tienes la última versión instalada.");
                                    util.MensajeTime("Ya tienes la última versión Descargada, de ser el caso Instale o borre archivo -->" + rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                                    mensajes("Ya tienes la última versión Descargada, de ser el caso Instale o borre archivo" + rutaAPK);
                                }

                            } else {
                                // Local tiene una versión mayor (raro)
                                if (OpcionActualizar==0) {
                                    mensajes("Tienes una versión mayor que la del servidor");
                                    util.MensajeTime("Tienes una versión superior para instalar o instalada en su celular verifique -->" + rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                                    Log.d("APK", "Tienes una versión mayor que la del servidor.");
                                }
                            }

                        }
                    } catch (NumberFormatException e) {

                        if (!traerRuta && !recibirDatos) {
                            mensajes("Error al consultar version ");
                        } else {
                            validateMonth();
                        }

                        /*if (traerRuta) {
                            traerRuta = false;
                            TraerRuta();
                        }
                        if (recibirDatos) {
                            recibirDatos = false;
                            RecibirDatos();
                        }*/


                        // mensajes("Error al consultar version ");
                    }
                   /* if (respuesta.contains("✓")) {
                        if (validaEnvio) {
                            validaEnvio = false;
                            mensajes("Primero Actualize la version ");

                        } else {
                            comenzarActualizar();
                        }
                    } else {
                        if (validaEnvio) {
                            validaEnvio = false;
                            Longitud();
                        } else {
                            mensajes("No hay actualizaciones ");
                        }
                    }*/
                    break;

                case "OBTENER_LONGITUD":
                    int res = Integer.parseInt(respuesta);
                    if (res > 0) {
                        mensajes("Archivos Registros.sda Encontrado con " + String.valueOf(res) + " bites");
                    } else {
                        mensajes("El archivo no existe");
                    }

                    break;

                case "OBTENER_LONGITUD_ARCHIVO_2": //Ax nuevo, anterior perdido

                    try {
                        long resp = Integer.parseInt(respuesta);

                        if (resp > 0) {

                            long tam = (int) (resp / tamLongitRegD);

                            if (tamFilasArchivoD == tam) {
                                mensajes("El envio Correcto! \n Archivo de " + tamFilasArchivoD + " Filas");
                            } else {
                                mensajes("El envio  No coincide, bites erroneos\n" + String.valueOf(resp));
                            }

                        } else if (resp == -99) {
                            mensajes ("Ciclo cerrado..");
                        } else {
                            mensajes("Envio no coincide, vuelva a enviar");
                        }
                    } catch (Exception ex) {
                        mensajes("Error no se pudo comprobar Envio");
                    }
                    break;

                case "TraerVersionSIMFACT":
                    // msg = respuesta;
                    Log.e("Ok Traer version", "respuesta version " + respuesta);
                    if (respuesta.contains("✓")) {
                        util.MensajeTime("Proceso de traer el .APK concluido favor si se bloquea ir a FILE\\Download y darle intalar a SIMLEnn.APK -->"+rutaAPK, "OPCION ACTUALIZA VERSION", Comunicaciones.this, 10);
                        comenzarActualizar(rutaAPK);
                    }
                    break;

                default:
                    msg = respuesta;

                    if (respuesta.contains("✓")) {
                        iconWsOff.setVisibility(View.INVISIBLE);
                        iconWsOn.setVisibility(View.VISIBLE);
                    }
                    break;
            }

            if (!msg.isEmpty()) {
                txtInfo.setText(msg);
                mensajes(msg);
            }
            //banderaMet2llamo = "";
            OpcionActualizar=0;
            respuesta = "";
            pruebawsok = bul; //Ws respopndio
            txtCiclo.requestFocus();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Comunicaciones.this);
            pDialog.setMessage("cargando...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    public void setNombreArgs(String args) {

        try {
            lblTxtArchivo.setText(args); //Aqui llega una respuesta del servidor  de tamaño 11 = L090040.002  aun asi el ciclo sea 9009
            //Aqui llega una respuesta del servidor  de tamaño 10 = L090040.002  aun asi el ciclo sea 9009
           // Ciclo = args.substring(1, 4).trim(); //Puede haber cambiado
            //cambiar el ciclo sale de del txtciclo que digitaron el valor
            Ciclo="000";
            if (CicloReal.trim().length()==9)
            Ciclo = args.substring(7, 10).trim(); //Puede haber cambiado

            Seccion = args.substring(1, 5); //Puede haber cambiado la Seccion
            Municipio = args.substring(5, 7); //Puede haber cambiado el Municipio
            Division = args.substring(8, 11); //Puede haber cambiado la Division

            txtCiclo.setText(CicloReal);
            txtMunicipio.setText(Municipio);
            txtSeccion.setText(Seccion);
            txtDivision.setText(Division);

            txtInfo.setText("Ruta seleccionada, reciba datos ✓");
            mensajes("Ruta seleccionada, reciba datos");
            OpcionActualizar = 3;
            validarV();

        } catch (Exception ex) {
            mensajes(ex.getMessage());
        }
        return;
    }

    public void AlertDialogEscoger(final String[] msgs) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Comunicaciones.this);
        builder.setTitle("Escoger ruta");

        builder.setSingleChoiceItems(msgs, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        setNombreArgs(msgs[0]);
                        break;
                    case 1:
                        setNombreArgs(msgs[1]);
                        break;
                    case 2:
                        setNombreArgs(msgs[2]);
                        break;
                    case 3:
                        setNombreArgs(msgs[3]);
                        break;
                    default:
                        setNombreArgs(msgs[item]);
                        //mensajes("Error o Excede items por esocger \n contacte admin.");
                }
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.setCancelable(false);
        levelDialog.setCanceledOnTouchOutside(false);
        levelDialog.show();
    }

    public void AlertDialogEscoger(String titulo, final String mensaje, final String method) {
        new AlertDialog.Builder(Comunicaciones.this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (method) {
                            case "RecibirDatos2":
                                RecibirDatos2();
                                break;
                            case "EnviarDatosMensaje2":
                                EnviarDatosMensaje2();
                                break;
                            case "EnviarDatosMensaje3":
                                EnviarDatosMensaje3();
                                break;
                            case "EnviarDatosServer":
                                EnviarDatosServer();
                                break;
                            case "EnviarFotos":
                                EnviarFotos();
                                break;
                            case "salir":
                                salir();
                                break;
                        }
                    }
                })
                .create()
                .show();
    }

//    // proceso de cambiar fecha del con el sistema central
//    private void modificacionFechaHoraSistema() {
//
//        // DIRECTORIOACTUAL + VariablesGlobales.getCarpetaLecturas()+"
//        String fecha_tomada = "";
//        String nombreArchivo = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/FECHAACONFIGURAR.TXT";
//        String textoLeido = "";
//
//        File file;
//        try {
//            file = new File(nombreArchivo);
//
//            if (file.exists()) {
//                file = new File(nombreArchivo);
//                FileReader stream1 = new FileReader(nombreArchivo);
//                BufferedReader reader1 = new BufferedReader(stream1);
//
//                // FileStream stream1 = new FileStream(NombreArchivo,
//                // FileMode.Open, FileAccess.Read);
//                // StreamReader reader1 = new StreamReader(stream1);
//
//                fecha_tomada = reader1.readLine();
//                reader1.close();
//                textoLeido = fecha_tomada.substring(6, 10) + fecha_tomada.substring(3, 8) + fecha_tomada.substring(0, 2) + fecha_tomada.substring(11, 13)
//                        + fecha_tomada.substring(14, 16) + fecha_tomada.substring(17, 19);
//                if (Long.parseLong(textoLeido) < 20120624000000L) {
//                    file.delete();
//                    mensajes("Fecha a Registrar CON ERROR");
//                    //Toast.makeText(getApplicationContext(), "Fecha a Registrar CON ERROR", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                // no deberia de borrar solo copiarla tambien como fechador
//                file.delete();
//            } else {
//
//                return;
//            }
//
//            nombreArchivo = VariablesGlobales.directorioactual + "/FECHADOR.TXT";
//            file = new File(nombreArchivo);
//            file.delete();
//
//            RandomAccessFile writer2 = new RandomAccessFile(file, "rw");
//            writer2.writeBytes(textoLeido);
//            writer2.close();
//        } catch (NumberFormatException e) {
//
//            e.printStackTrace();
//
//        } catch (FileNotFoundException e) {
//
//            e.printStackTrace();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
//
//        GregorianCalendar t = new GregorianCalendar(Integer.valueOf(fecha_tomada.substring(6, 10)), Integer.valueOf(fecha_tomada.substring(3, 5)),
//                Integer.valueOf(fecha_tomada.substring(0, 2)), Integer.valueOf(fecha_tomada.substring(11, 13)),
//                Integer.valueOf(fecha_tomada.substring(14, 16)), Integer.valueOf(fecha_tomada.substring(17, 19)));
//
//        // TODO: ClaseEspecial.TIEMPODELSISTEMA st = new
//        // ClaseEspecial.TIEMPODELSISTEMA();
//        // st.FromDateTime(t);
//        // ClaseEspecial.SetLocalTime(ref st);
//        // MessageBox.Show("El Sistema Actualizo la Fecha y La Hora");
//        file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/FECHAACONFIGURAR.TXT");
//        file.delete();
//
//    }

    /**
     * kim
     * Codigo que se va a ejecutar una vez terminado de bajar los datos.
     */
    private void comenzarActualizaranterior(String ruta) {
        //kim
        //Para tener el contexto mas a mano.
        String[] ms = null;
        ms[1]=ruta;
        context = this;
        //Creamos el Autoupdater.
        updater = new Autoupdater(this,ruta);
        //Ponemos a correr el ProgressBar.

        //Crea mensaje con datos de versión.
        String msj = "Nueva Version: ";
        msj += "\nDesea Actualizar?";
        //Crea ventana de alerta.
        androidx.appcompat.app.AlertDialog.Builder dialog1 = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog1.setMessage(msj);
        //Establece el boton de Aceptar y que hacer si se selecciona.
        dialog1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                updater.InstallNewVersion(null);
            }
        });

        //Muestra la ventana esperando respuesta.
        dialog1.show();

    }
    private void comenzarActualizar(String ruta) {
        //Para tener el contexto mas a mano.
        context = this;
        //Creamos el Autoupdater.
        updater = new Autoupdater(this,ruta.trim());
        //Ponemos a correr el ProgressBar.
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("ACTUALIZACION DISPONIBLE");
        builder.setMessage("¿Desea actualizar la version del dispositivo?");

        builder.setIcon(R.drawable.fondos_global);

        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                updater.setCurrentVersionCode(currentVersionCode);
                updater.InstallNewVersion(null);
            }
        });
        builder.show();
    }

    public void validarV() {
        try {

            metodo = "ValidarVersionSIMFACT";
            AsyncCallWS task = new AsyncCallWS();
            task.execute("");
        } catch (Exception exc) {

        }
    }

//    public String getSerialNumber() {
//
//        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceIMEI = tManager.getDeviceId();
//        return deviceIMEI;
//    }

    public String traerArchivo() {
        String respuestaWeb = "";
        int versionAct = 0;

        if (versionActualizar==0)
        {
            util.MensajeTime("Debe VALIDAR VERSION ANTES DE INDICAR TRAER", "OPCION ACTUALIZA VERSION", this, 4);
        }
        try {
            WSSoap wsoaps = new WSSoap(URL, paginaWs);



            respuestaWeb = wsoaps.ObtenerLongitudArchivo("OBTENER_LONGITUD_ARCHIVO", rutaAdministrador + "Android\\SIMLE" + String.format("%1$2s", versionActualizar).replace(" ", "0") + ".APK", 0, Ciclo, Seccion+Municipio  + Division);//before (currentVersionCode + 1)
            Log.e("error", String.format("%1$2s", versionActualizar).replace(" ", "0") + " 2222222222 " + respuestaWeb);
            //     }

            int res = Integer.parseInt(respuestaWeb);
            //Log.e("error", res + " 999999999" + " LA RUTA YA SE ESTA TRABAJANDO EN OTRO CELULAR");

            if (res == -99) {
                Log.e("error", "999999999" + " LA RUTA NO SE TRABAJA PORQUE EL CICLO YA ESTA CERRADO");
            }

            if (res > 0) {
                // String Versionapk=String.format("%1$2s", versionActualizar).replace(" ", "0");
                respuestaWeb = wsoaps.ObtenerArchivo("ENVIAR_DEL_SERVIDOR_AL_PDA", rutaAdministrador + "Android\\SIMLE" + String.format("%1$2s", versionActualizar).replace(" ", "0") + ".APK", rutaAPK, trama, res, false);//before  (currentVersionCode + 1)
                Log.e("error", "3333333333" + respuestaWeb);
            }

            if (new File(rutaAPK).exists() && new File(rutaAPK).length() > 0) {
                //ESTO QUIERE DECIR QUE ME TRAJO EL ARCHIVO CORRECTAMENTE
                respuestaWeb = "traerArchivo()|"+"✓";
            } else  {//if (respuestaWeb.equals("2"))
                respuestaWeb = "traerArchivo()|ERROR|No se pudo COMPLETAR TRAIDA DEL ARCHIVO APK";//util.DescomprimeZip(rutaAPK, "", res, true);
                Log.e("error", "444444444" + respuestaWeb);
            }
            versionActualizar = 0;
        } catch (Exception ex) {
            versionActualizar = 0;
            Log.e("error", ex.getMessage());
            return "Problemas EnviarArchivo()" + ex.getMessage();
        }
        if (new File(rutaAPK).isDirectory()) {
            File f = new File(rutaAPK);
            f.delete();
        }
        return respuestaWeb;
    }


    public void seleccionModuloTrabajo(String mensaje, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle(titulo);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                VariablesGlobales.moduloTrabajo = "CIC";
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                VariablesGlobales.moduloTrabajo = "ENT";
                VariablesGlobales.moduloTrabajo = "ENT";
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public String versionSimfa() {

        String respuestaWeb = "";

        try {
            WSSoap wsoaps = new WSSoap(URL, paginaWs);
            respuestaWeb = wsoaps.validarVersion("ValidarVersionSIMFACT_2", rutaAdministrador, serial);//+ "Android\\SIMLE" + (currentVersionCode + 1) + ".ZIP"
            // Log.e("error", "ruta version " + rutaAdministrador + "-" + serial);
            return respuestaWeb;

        } catch (Exception ex) {
            return respuestaWeb;
        }
    }

    public void TraerVersionSimfa() {
        try {

            metodo = "TraerVersionSIMFACT";
            AsyncCallWS task = new AsyncCallWS();
            task.execute("");
        } catch (Exception exc) {

        }
    }

    public void validateMonth() {

        String ciclo = txtCiclo.getText().toString();
        String mes = "";
        Calendar fecha = Calendar.getInstance();
        int mesActual = fecha.get(Calendar.MONTH) + 1;
        if (ciclo.length() >= 9) {
            mes = ciclo.substring(4, 6);
        } else {
            mensajes("El ciclo es incorrecto");
            return;
        }
        if (Integer.parseInt(mes) < mesActual) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Seguro desea cargar el periodo " + mes + " y no el actual " + mesActual)
                    .setTitle("Alerta cargue de trabajo");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (traerRuta) {
                        traerRuta = false;
                        TraerRuta();
                    }
                    if (recibirDatos) {
                        recibirDatos = false;
                        RecibirDatos();
                    }
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        } else {
            if (traerRuta) {
                traerRuta = false;
                TraerRuta();
            }
            if (recibirDatos) {
                recibirDatos = false;
                RecibirDatos();
            }
        }
    }
}