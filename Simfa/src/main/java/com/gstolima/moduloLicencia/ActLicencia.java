package com.gstolima.moduloLicencia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.GuiAcceso;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.api.ApiService;
import com.gstolima.api.models.ValidarImeiRequest;
import com.gstolima.api.models.ValidarImeiResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActLicencia extends AppCompatActivity implements View.OnClickListener {
    String telephoneSerialNumber = "";
    EditText serial;
    EditText Codigo;
    Button validar;
    final File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    File file;// = new File(sdCard.getAbsolutePath(), "/Android/data/Config.txt");
    String control_serial = "P";
    String DatosEncriptados = "DCRFVTGBYH";
    DecimalFormat numF = new DecimalFormat("#,###");
    boolean esMayor9;
    File logfile;
    Utils utils = new Utils();//Ax log y utilidades
    String URL = "";
    String paginaWs = "";

    /** true si existe Config.txt: el equipo ya fue licenciado en su momento. */
    private boolean yaLicenciado = false;

    /** IMEI leido de Config.txt; es el que se valida contra `moviles`. */
    private String imeiLicenciado = "";

    // Marca persistente del ultimo IMEI autorizado por el servidor. Se guarda el
    // IMEI y no un booleano: si el equipo cambia (o se restaura el Config.txt en
    // otro terminal), el valor deja de coincidir y se fuerza revalidacion.
    private static final String PREFS_LIC = "licencia_prefs";
    private static final String KEY_IMEI_VALIDADO = "imei_validado";

    private static final int TIMEOUT_VALIDACION_SEG = 30;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_PERMISOS_ALMACENAMIENTO = 2, CODIGO_PERMISOS_TELEFONO = 3, CODIGO_PERMISOS_UBICACION = 4, CODIGO_PERMISOS_BLUETOOHSCN = 5,
            CODIGO_PERMISOS_BLUETOOHCNT = 6, CODIGO_PERMISOS_IMAGENES = 7, CODIGO_COMUNICACION = 8, CODIGO_FULL_WRITE = 9;

    //ActivityResultLauncher<Intent> activityResultLauncher;
    Context ctx;
    public int CodigoIntent = 0;
    CrudComunicaciones crudComuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_licencia);
        serial = (EditText) findViewById(R.id.serial);
        Codigo = (EditText) findViewById(R.id.codigo);
        validar = (Button) findViewById(R.id.btnValidar);
        validar.setOnClickListener(this);
        ctx = this;
        crudComuni = new CrudComunicaciones(this);
        Bundle bundle = getIntent().getExtras();
        telephoneSerialNumber = bundle.getString("telephoneSerialNumber");
        esMayor9 = bundle.getBoolean("esMayor9", false);

        comprobarPermisos(1);

        serial.setEnabled(true);
        gestionarLicencia();
        setParams();
        getParamsWs();
    }// endOncreate

    /*ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (CodigoIntent == CODIGO_FULL_WRITE) {
                gestionarLicencia();
                //comprobarPermisos(3);
            }
        }
    });*/

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (CodigoIntent == CODIGO_FULL_WRITE) {
                //gestionarLicencia();
                comprobarPermisos(3);
            }
        }
    });

    public void gestionarLicencia() {
        try {
            String externalpath = ctx.getExternalFilesDir(null).getParent();
            String hardcoding = "/Android/data/";//Ax: todo: cambiar este hardcoding
            if (externalpath.contains(hardcoding)) {
                externalpath = externalpath.substring(0, externalpath.indexOf(hardcoding));
            }

            file = new File(externalpath, "/Documents");
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(externalpath, "/Documents/Config.txt");

            if (file.exists()) {
                // Config.txt existe -> equipo YA licenciado. Se marca ANTES de leer,
                // para que un fallo de lectura o desencriptacion no lo empuje al
                // flujo de serial+codigo. Aqui solo se confirma el IMEI.
                yaLicenciado = true;

                // El codigo no aplica para un equipo ya licenciado; se oculta para
                // que el tecnico no crea que debe ingresarlo.
                Codigo.setVisibility(View.GONE);

                try {
                    FileInputStream fIn = new FileInputStream(file);
                    InputStreamReader archivo = new InputStreamReader(fIn);
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String dt = "";
                    String Cdesenc = "";
                    control_serial = "I";
                    int contador = 0;
                    while (linea != null) {
                        dt = dt + linea;
                        linea = br.readLine();
                        Cdesenc = Desencriptar(dt);
                        contador++;
                        if (contador > 0)
                            break;
                    }

                    br.close();
                    archivo.close();

                    imeiLicenciado = (Cdesenc != null) ? Cdesenc.trim() : "";
                } catch (Exception e) {
                    utils.Log(logfile, "[ActLicencia]gestionarLicencia(lectura)|" + e.getMessage());
                }

                // Respaldo: si no se pudo desencriptar, usar el IMEI del dispositivo.
                if (imeiLicenciado.isEmpty()
                        && telephoneSerialNumber != null
                        && !telephoneSerialNumber.trim().isEmpty()) {
                    imeiLicenciado = telephoneSerialNumber.trim();
                }

                // Prefijar el campo serial: el tecnico solo pulsa Validar.
                if (!imeiLicenciado.isEmpty()) {
                    serial.setText(imeiLicenciado);
                }

                // Si este mismo IMEI ya fue autorizado por el servidor, entrar directo
                // sin pasar por el modulo de licencia en cada arranque.
                String imeiValidado = getSharedPreferences(PREFS_LIC, MODE_PRIVATE)
                        .getString(KEY_IMEI_VALIDADO, "");
                if (!imeiValidado.isEmpty() && imeiValidado.equals(imeiLicenciado)) {
                    entrarComoLicenciado();
                    return;
                }

            } else {
            }
        } catch (Exception ex) {
            Log.e("ERROR", "[ActLicencia]gestionarLicencia()|Error -> " + ex.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(2);
                }
                break;

            case CODIGO_PERMISOS_ALMACENAMIENTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(3);
                }
                break;

            case CODIGO_PERMISOS_TELEFONO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(4);
                }
                break;

            case CODIGO_PERMISOS_UBICACION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(5);
                }
                break;

            case CODIGO_PERMISOS_BLUETOOHSCN:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(6);
                }
                break;

            case CODIGO_PERMISOS_BLUETOOHCNT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(7);
                }
                break;

            case CODIGO_PERMISOS_IMAGENES:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos(2); // Luego de imágenes, va almacenamiento si aplica
                }
                break;
        }
    }

    private boolean comprobarPermisos(int permiso) {
        try {
            switch (permiso) {
                case 1:
                    int estadoPermisoCamara = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                    if (estadoPermisoCamara != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CODIGO_PERMISOS_CAMARA);
                    } else {
                        comprobarPermisos(2);
                    }
                    break;
                case 2:
                    if (Build.VERSION.SDK_INT >= 30) { // Android 11 o superior
                        if (!Environment.isExternalStorageManager()) {
                            CodigoIntent = CODIGO_FULL_WRITE;
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                activityResultLauncher.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncher.launch(intent);
                            }
                        } else {
                            comprobarPermisos(3);
                        }
                    } else { // Android 10 o menor
                        int estadoPermisoAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (estadoPermisoAlmacenamiento != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO);
                        } else {
                            comprobarPermisos(3);
                        }
                    }
                    break;
                case 3:
                    int estadoPermisoTelefono = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                    if (estadoPermisoTelefono != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, CODIGO_PERMISOS_TELEFONO);
                    } else {
                        comprobarPermisos(4);
                    }
                    break;
                case 4:
                    int estadoPermisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (estadoPermisoUbicacion != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODIGO_PERMISOS_UBICACION);
                    } else {
                        comprobarPermisos(5);
                    }
                    break;
                case 5:
                    if (Build.VERSION.SDK_INT >= 31) {
                        int estadoPermisobthscan = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
                        if (estadoPermisobthscan != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, CODIGO_PERMISOS_BLUETOOHSCN);
                        } else {
                            comprobarPermisos(6);
                        }
                    } else {
                        comprobarPermisos(7);
                    }
                    break;
                case 6:
                    int estadoPermisobthadm = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
                    if (estadoPermisobthadm != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, CODIGO_PERMISOS_BLUETOOHCNT);
                    } else {
                        comprobarPermisos(7);
                    }
                    break;
                case 7:
                    // Todos los permisos concedidos
                    Log.e("PERMISOS", "Todos los permisos concedidos. Puedes continuar.");
                    gestionarLicencia();
                    break;
            }
        } catch (Exception ex) {
            Log.e("Tag Permisos", "Error: " + ex);
            alertMensaje("Error al Comprobar Permisos: " + ex);
        }
        return false;
    }

//    public String getSerialNumber() {
//
//        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE);
//        String deviceIMEI = tManager.getDeviceId();
//        return deviceIMEI;
//    }

    public void setParams() {
        try {
            BDComunicaciones bdcini = crudComuni.getParamsbyId(1);
            if (bdcini == null) {
                //------------------------------------------ Parametro para conexion WIFI
                BDComunicaciones bdc = new BDComunicaciones();
                bdc.setId(1);
                bdc.setURL("lecturas.meta.inelma.info");
                bdc.setPaginaWs("lecturas.asmx");
                bdc.setEstado("A");
                bdc.setRutaAdministrador("D:/ENRUTADOR_EMSA");
                bdc.setUrlApi("https://meta.inelma.info/api/");
                bdc.sethttpSeguro(0);
                String res = crudComuni.setCofigUrl(bdc);
                if (!res.contains("INSERCCION")) {
                    alertMensaje("Error al establecer los parametros");
                    return;
                }
                //------------------------------------------ Parametro para conexion GPRS
                bdc.setId(2);
                bdc.setURL("162.215.135.191:3628");
                bdc.setPaginaWs("lecturas.asmx");
                bdc.setEstado("I");
                bdc.setRutaAdministrador("D:/ENRUTADOR_EMSA");
                bdc.setUrlApi("http://162.215.135.191:3627/api/");
                bdc.sethttpSeguro(0);
                res = crudComuni.setCofigUrl(bdc);
                if (!res.contains("INSERCCION")) {
                    alertMensaje("Error al establecer los parametros");
                }
            }
        } catch (Exception ex) {
            String[] time = getTimeError().split("\\|");
            utils.Log(logfile, "999999999" + "|" + "999999999" + "|" + time[0].substring(5, 7) + "|" + time[0].substring(0, 4) + "|" +
                    telephoneSerialNumber + "|" + "[ActLicencia]setParams()|" + ex.getMessage() + "|" + time[0] + "|" + time[1] + "|");

        }
    }

    public void getParamsWs() {
        try {
            BDComunicaciones bdc = crudComuni.getParams();
            if (bdc != null) {
                if (bdc.gethttpSeguro()==0) {
                    URL = "http://" + bdc.getURL();
                }
                else
                {
                    URL = "https://" + bdc.getURL();
                }

                paginaWs = bdc.getPaginaWs();
            } else {
                alertMensaje("No se encontraron parametros");
                return;
            }
        } catch (Exception ex) {
            String[] time = getTimeError().split("\\|");
            utils.Log(logfile, "999999999" + "|" + "999999999" + "|" + time[0].substring(5, 7) + "|" + time[0].substring(0, 4) + "|" +
                    telephoneSerialNumber + "|" + "[ActLicencia]getParamsWs()|" + ex.getMessage() + "|" + time[0] + "|" + time[1] + "|");

        }

    }
    private String getTimeError() {
        try {
            Calendar cal = new GregorianCalendar();
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formatteDate = df.format(date);

            Date dt = new Date();
            SimpleDateFormat hf = new SimpleDateFormat("HH:mm:ss");
            String formatteHour = hf.format(dt.getTime());

            return formatteDate + "|" + formatteHour;
        } catch (Exception e) {
            utils.Log(logfile, "[ActLicencia]setParams()|" + e + "|" + "2023-01-05" + "|" + "09:27:15" + "|");
            return "2023-01-05" + "|" + "09:27:15";
        }
    }
    public int Procesar_Serial(String Serial, Double valor_entrada, String controlador) {

        telephoneSerialNumber = Serial;
        double valor_calculado = 0;
        double valor_recibido = 0;
        String letra = "";
        int res = 0;
        Intent miIntent = new Intent();

        try {

            double Numero = 0;
            double Numero2 = 0;

            for (int i = 0; i < Serial.length(); i++) {

                letra = Serial.substring(i, i + 1);
                valor_recibido = Letras_Serial(letra);

                if (valor_recibido == 0) //se dara algo con el valor
                {

                    Numero = Double.parseDouble(letra);
                    Numero2 = Double.parseDouble(letra + letra);

                    if (i < 6) {
                        valor_recibido = Math.pow(Numero, Numero);//Convert.ToInt32(letra.PadLeft(i + 1, '3'));

                    } else {
                        valor_recibido = Math.pow(Numero2, 6);
                        // MessageBox.Show("Valor " + Numero2.ToString() + "=" + valor_recibido.ToString());
                    }

                }
                valor_calculado += valor_recibido;
            }

            int ConversionP = String.valueOf(numF.format(valor_calculado)).indexOf(".");
            int ConversionC = String.valueOf(numF.format(valor_calculado)).indexOf(",");
            String Cambio = "";

            if (ConversionP >= 0) {
                Cambio = ".";
            }
            if (ConversionC >= 0) {
                Cambio = ",";
            }

            if (controlador.trim().toUpperCase().equals("V")) {

                alertMensaje("Este es su codigo de acceso " + String.format("%-40s", numF.format(valor_calculado)).replace(Cambio, "").trim());

            } else if (String.format("%-40s", numF.format(valor_calculado)).replace(Cambio, "").trim().equals(String.format("%-40s", numF.format(valor_entrada)).replace(Cambio, "").trim()) && controlador.trim().toUpperCase().equals("I")) {

                if (esMayor9) {
                    Serial = Encriptar(telephoneSerialNumber);
                } else {
                    Serial = Encriptar(String.format("%-40s", numF.format(valor_calculado)).replace(Cambio, "").trim());
                }

                try {

                    if (!file.exists()) {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
                        osw.write(Serial);
                        osw.flush();
                        osw.close();
                        Log.e("bien", "Los datos fueron grabados correctamente");

                    } else {
                    }

                    Bundle bundleg = new Bundle();
                    bundleg.putString("valida", "SI");
                    bundleg.putString("PhoneImei", telephoneSerialNumber);
                    miIntent.setClass(this, GuiAcceso.class);
                    miIntent.putExtras(bundleg);
                    startActivity(miIntent);
                    finish();
                } catch (Exception e) {
                    //    Log.e("valor", "no se proceso " + e.getMessage());
                }
                res = 1;
                return (res);
            } else {
                finish();
            }
            return (res);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("valor", "no se proceso " + e.getMessage());

            res = 0;
            return (res);
        }

    }

    public int Letras_Serial(String letra) {
        int res = 0;
        try {
            if (letra.toUpperCase() == "Z" || letra.toUpperCase() == "X" || letra.toUpperCase() == "C" || letra.toUpperCase() == "V") {
                res = 563289;
                return (res);
            }
            if (letra.toUpperCase() == "B" || letra.toUpperCase() == "N" || letra.toUpperCase() == "M" || letra.toUpperCase() == "Ñ") {
                res = 654987;
                return (res);
            } else {
                if (letra.toUpperCase() == "L" || letra.toUpperCase() == "K" || letra.toUpperCase() == "J" || letra.toUpperCase() == "H") {
                    res = 354982;
                    return (res);
                } else if (letra.toUpperCase() == "G" || letra.toUpperCase() == "F" || letra.toUpperCase() == "D" || letra.toUpperCase() == "S") {
                    res = 852147;
                    return (res);
                } else if (letra.toUpperCase() == "A" || letra.toUpperCase() == "Q" || letra.toUpperCase() == "W" || letra.toUpperCase() == "E" || letra.toUpperCase() == "R") {
                    res = 963258;

                    return (res);
                } else if (letra.toUpperCase() == "T" || letra.toUpperCase() == "Y" || letra.toUpperCase() == "U" || letra.toUpperCase() == "I" || letra.toUpperCase() == "O" || letra.toUpperCase() == "P") {
                    res = 325698;
                    return (res);
                } else {
                    return (res);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return (res);
        }


        // return (res);

    }

    public String Desencriptar(String entrada) {
        String DatosEncriptados = "DCRFVTGBYH";
        int i = 0;
        int j = 0;
        String salida = "";
        String alfabeto = "*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "\n" + "\r";

        String sub1 = "QAZWSX" + DatosEncriptados + "NUJM IKOLP*>" + "+=,<-;.:/908?172634[|@" + "\n" + "\r";

        //DCRFVTGBYH
        for (i = 0; i <= entrada.length() - 1; i++) {
            for (j = 0; j <= sub1.length() - 1; j++) {
                if (entrada.substring(i, i + 1).equals(sub1.substring(j, j + 1))) {

                    salida += alfabeto.substring(j, j + 1);
                }
            }
        }
        return (salida);
    }

    public String Encriptar(String entrada) {
        String DatosEncriptados = "DCRFVTGBYH";
        int i = 0;
        int j = 0;
        String salida = "";
        String alfabeto = "*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "\n" + "\r";
        String sub1 = "QAZWSX" + DatosEncriptados + "NUJM IKOLP*>" + "+=,<-;.:/908?172634|[@" + "\n" + "\r";

        //DCRFVTGBYH
        for (i = 0; i <= entrada.length() - 1; i++) {
            for (j = 0; j <= alfabeto.length() - 1; j++) {

                if (entrada.substring(i, i + 1).equals(alfabeto.substring(j, j + 1))) {
                    salida += sub1.substring(j, j + 1);
                }
            }
        }
        return (salida);
    }

    public void alertMensaje(String mensaje) {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(500);
        AlertDialog.Builder alertConfCreado = new AlertDialog.Builder(this);
        alertConfCreado.setIcon(R.drawable.ic_launcher);
        alertConfCreado
                .setMessage(mensaje)
                .setTitle("Alerta")
                .setCancelable(false)
                .setPositiveButton("Terminar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
        AlertDialog alert = alertConfCreado.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnValidar) {
            validarImeiYContinuar();
        }
    }

    /**
     * Valida el equipo contra la tabla `moviles` del servidor: autoriza solo si
     * existe un registro con cod_movil = IMEI AND asignado = 1.
     *
     * El IMEI es lo que hay en el campo serial (precargado desde Config.txt en
     * equipos ya licenciados). Si el servidor autoriza:
     *   - equipo ya licenciado -> entra directo, sin serial+codigo
     *   - equipo nuevo         -> continua el flujo original de licencia
     *
     * Ante rechazo NO se cierra la actividad: el tecnico puede corregir el IMEI
     * y reintentar.
     */
    private void validarImeiYContinuar() {
        final String imeiStr = serial.getText().toString().trim();
        if (imeiStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el IMEI del equipo", Toast.LENGTH_LONG).show();
            return;
        }

        final long codMovil;
        try {
            codMovil = Long.parseLong(imeiStr);   // cod_movil es bigint en el servidor
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "IMEI invalido (debe ser numerico)", Toast.LENGTH_LONG).show();
            return;
        }

        final String baseUrl = obtenerUrlApi();
        if (baseUrl.isEmpty()) {
            Toast.makeText(this,
                    "No hay URL de API configurada. Revise Configuracion IP.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        validar.setEnabled(false);
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_VALIDACION_SEG, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_VALIDACION_SEG, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService api = retrofit.create(ApiService.class);
            api.validarImei(new ValidarImeiRequest(codMovil))
                    .enqueue(new Callback<ValidarImeiResponse>() {
                        @Override
                        public void onResponse(Call<ValidarImeiResponse> call,
                                               Response<ValidarImeiResponse> resp) {
                            validar.setEnabled(true);

                            if (resp.isSuccessful() && resp.body() != null && resp.body().autorizado) {
                                // Persistir el IMEI autorizado: en los proximos arranques
                                // se entra directo sin pasar por el modulo de licencia.
                                getSharedPreferences(PREFS_LIC, MODE_PRIVATE).edit()
                                        .putString(KEY_IMEI_VALIDADO, imeiStr)
                                        .apply();

                                if (yaLicenciado) {
                                    entrarComoLicenciado();
                                } else {
                                    continuarConSerial();
                                }
                            } else {
                                String msg = (resp.body() != null && resp.body().mensaje != null)
                                        ? resp.body().mensaje
                                        : "Equipo no registrado o no asignado";
                                Toast.makeText(ActLicencia.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ValidarImeiResponse> call, Throwable t) {
                            validar.setEnabled(true);
                            utils.Log(logfile, "[ActLicencia]validarImei onFailure|" + t.getMessage());
                            Toast.makeText(ActLicencia.this,
                                    "No se pudo validar el equipo (sin conexion con el servidor). Reintente.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            validar.setEnabled(true);
            utils.Log(logfile, "[ActLicencia]validarImeiYContinuar()|" + e.getMessage());
            Toast.makeText(this, "Error validando el equipo: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * URL base del API tomada de la parametrizacion vigente (perfil activo de
     * BDComunicaciones). Sin tabla de empresas: este cliente trabaja contra un
     * unico punto de acceso, el que el operador configura en Configuracion IP.
     */
    private String obtenerUrlApi() {
        try {
            BDComunicaciones bdc = CrudComunicaciones.getParams();
            if (bdc != null) {
                String url = bdc.getUrlApi();
                return url != null ? url.trim() : "";
            }
        } catch (Exception e) {
            utils.Log(logfile, "[ActLicencia]obtenerUrlApi()|" + e.getMessage());
        }
        return "";
    }

    /**
     * Entrada de un equipo ya licenciado tras pasar la validacion de IMEI.
     * Replica la navegacion que antes hacia gestionarLicencia() directamente,
     * sin repetir serial+codigo.
     */
    private void entrarComoLicenciado() {
        Intent miIntent = new Intent();
        Bundle bundleg = new Bundle();
        bundleg.putString("valida", "SI");
        bundleg.putString("PhoneImei", imeiLicenciado);
        miIntent.setClass(this, GuiAcceso.class);
        miIntent.putExtras(bundleg);
        startActivity(miIntent);
        finish();
    }

    /** Flujo original de validacion por serial + codigo (equipo nuevo). */
    private void continuarConSerial() {
        if (Codigo.getText().toString().trim().length() > 0) {
            if (Codigo.getText().toString().trim().equals("9004312571966")) {
                control_serial = "V";
                Procesar_Serial(serial.getText().toString().trim(),
                        Double.parseDouble(Codigo.getText().toString().trim()), control_serial);
            } else {
                control_serial = "I";
                Procesar_Serial(serial.getText().toString().trim(),
                        Double.parseDouble(Codigo.getText().toString().trim()), control_serial);
            }
        } else {
            Toast.makeText(this, "Ingrese el codigo de licencia", Toast.LENGTH_LONG).show();
        }
    }
}