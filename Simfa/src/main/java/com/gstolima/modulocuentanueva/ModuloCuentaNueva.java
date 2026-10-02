package com.gstolima.modulocuentanueva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import com.gstolima.comunicaciones.SyncHelper;
import com.gstolima.comunicaciones.EnvioCuentaNueva;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;

public class ModuloCuentaNueva extends AppCompatActivity {

    EditText txtcnDireccion;
    Spinner spnTipoMedida;
    EditText txtcnContador;
    EditText txtcuMarca;
    EditText txtcnDigitos;
    EditText txtcnObserv;
    EditText txtcnLectura;
    EditText txtcuInforme;
    EditText edtCodRefere;

    Button btncngrabar;
    Button btncnreotornar;
    Button btncnfoto;

    File logfile;

    String cnCiclo = "";
    String cnDescDepto = "";
    String cnDireccion = "";
    String cnCodMunicipio = "";
    String cnCodSector = "";
    String cnCodRuta = "";
    String nombrearchivo6 = "";
    String AppPath = "";
    String latitud;
    String longitud;
    String numSatelites;
    String altitud;
    String fechaHora;
    String serial;
    String lector = "";
    String CicloReal = "";

    Utils utils = new Utils();//Ax log y utilidades

    int vecesImagen = 0;// Captura la cantidad de reintentos de tomar una foto
    private String namePhoto;
    public String nombredelaImagen = "";//Ax: Captura el nombre del archivo de imagen
    String cuentaFoto = "";
    Toast toast = null;
    int msgCorto = 200;
    int msgMedio = 500;
    int msgLargo = 1000;
    private static int TAKE_PICTURE = 1;
    private String currentPhotoPath;
    public String nombrefoto = ""; // lleva el nombre de la foto que se creara para despues mandarla a optimizar
    int exitoImagen = 0;// = 1 si la imagen se tomó
    private String name = "";
    String foto1 = "";
    String foto2 = "";
    String foto3 = "";


    // Fotografia
    private Uri imageUri;
    String nameForIntent = "";
    int CodigoIntent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_nueva);

        txtcnDireccion = (EditText) findViewById(R.id.txtcnDireccion);
        txtcnContador = (EditText) findViewById(R.id.txtcnContador);
        txtcuMarca = (EditText) findViewById(R.id.txtcuMarca);

        txtcnDigitos = (EditText) findViewById(R.id.txtcnDigitos);
        txtcnObserv = (EditText) findViewById(R.id.txtcnObserv);
        txtcnLectura = (EditText) findViewById(R.id.txtcnLectura);
        txtcuInforme = (EditText) findViewById(R.id.txtcuInforme);
        edtCodRefere = (EditText) findViewById(R.id.txtcnrRefer);
        spnTipoMedida = (Spinner) findViewById(R.id.spnTipoMedida);
        spnTipoMedida.setSelection(0);
        btncngrabar = (Button) findViewById(R.id.btncngrabar);
        btncnreotornar = (Button) findViewById(R.id.btncnreotornar);
        btncnfoto = (Button) findViewById(R.id.btncnfoto);

        Bundle bundle = getIntent().getExtras();

        nombrearchivo6 = bundle.getString("nombrearchivo").trim();
        AppPath = bundle.getString("directorioactual").trim();
        cnCiclo = bundle.getString("cnCiclo");
        cnDescDepto = bundle.getString("cnDesc_Depto");
        cnDireccion = bundle.getString("cnDireccion");
        cnCodMunicipio = bundle.getString("cnCod_Municipio");
        cnCodSector = bundle.getString("cnCod_Sector");
        cnCodRuta = bundle.getString("cnCod_Ruta");
        latitud = bundle.getString("latitud");
        longitud = bundle.getString("longitud");
        numSatelites = bundle.getString("numSatelites");
        altitud = bundle.getString("altitud");
        fechaHora = bundle.getString("fechaHora");
        serial = bundle.getString("serial");
        lector = bundle.getString("lector");
        CicloReal = bundle.getString("CicloReal");
        txtcnDireccion.setText(cnDireccion);
        txtcnObserv.setText("12");
        txtcnDigitos.setText("5");
        txtcuInforme.setText("CLIENTE CON CAMBIO DE MEDIDOR");


        Log.e("error", "data intent " + nombrearchivo6 + "-" + AppPath + "-" + cnCiclo + "-" + cnDescDepto
                + "-" + cnCodMunicipio + "-" + cnCodSector + "-" + cnCodRuta);
        txtcnDireccion.requestFocus();

        logfile = new File(AppPath + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG");

        btncnfoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validarDatos()) {
                    mensajes("Faltan datos por llenar!");
                } else {
                    capturaImagenFotografica();
                }
            }
        });

        btncngrabar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!validarDatos() ) {//|| !validarFoto()
                    mensajes("Faltan datos por llenar!");
                } else {
                    dialogMsg();
                }
            }
        });

        btncnreotornar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                View view1 = findViewById(R.id.btncngrabar);
                InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                finish();
            }
        });

        txtcnDireccion.requestFocus();
        verifyChart();

    }//End Oncreate

    @Override
    public void onBackPressed() {
        mensajes("Debe completar los campos y guardar");
    }

    /*private void grabarSalir() {

        String linea = "";
        try {
            File f = new File(AppPath + VariablesGlobales.getCarpetaLecturas()+"/" + "N" + nombrearchivo6);
            File cuentasEnvio = new File(AppPath + VariablesGlobales.getCarpetaLecturas()+"/" + "CUENTASNUEVA" + serial + ".SDA");

            if (cnDescDepto.length() > 31) {
                cnDescDepto = cnDescDepto.substring(30, 32);
            } else {
                if (cnDescDepto.length() > 1) {
                    cnDescDepto = cnDescDepto.substring(0, 2);
                }
            }

            String InformeFinal = txtcuInforme.getText().toString().trim();
            InformeFinal = InformeFinal.replace("í", "i");
            InformeFinal = InformeFinal.replace("á", "a");
            InformeFinal = InformeFinal.replace("ó", "o");
            InformeFinal = InformeFinal.replace("ú", "u");
            InformeFinal = InformeFinal.replace("é", "e");
            InformeFinal = InformeFinal.replace("ñ", "n");
            InformeFinal = InformeFinal.replace("Ñ", "N");

            linea = String.format("%1$-64s", txtcnDireccion.getText().toString().trim()) + ";" +
                    String.format("%1$-20s", txtcnContador.getText().toString().trim()) + ";" +//before 16
                    String.format("%1$-15s", txtcuMarca.getText().toString().trim()) + ";" +//before 3
                    String.format("%1$-2S", spnTipoMedida.getText().su.toString().trim()) + ";" +
                    String.format("%1$1s", txtcnDigitos.getText().toString().trim()) + ";" +
                    String.format("%1$7s", txtcnLectura.getText().toString().trim()) + ";" +
                    String.format("%1$-2s", txtcnObserv.getText().toString().trim()) + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCiclo)) + "")) + ";" +
                    String.format("%1$-32s", cnDescDepto.trim() + "") + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCodMunicipio)) + "")) + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCodSector)) + "")) + ";" +
                    String.format("%1$-13s", ((utils.parseStringToInteger(cnCodRuta)) + "")) + ";" +
                    String.format("%1$-60s", InformeFinal.trim()) + ";" +
                    String.format("%1$-6s", edtCodRefere.getText().toString().trim()) + ";" +
                    String.format("%1$-16s", latitud) + ";" +
                    String.format("%1$-16s", longitud) + ";" +
                    String.format("%1$-20s", fechaHora) + ";" +
                    String.format("%1$-10s", altitud) + ";" +
                    String.format("%1$-3s", numSatelites) + ";" +
                    String.format("%1$-4s", lector) + ";" +
                    String.format("%1$-30s", foto1) + ";" +
                    String.format("%1$-30s", foto2) + ";" +
                    String.format("%1$-30s", foto3) + ";" +
                    String.format("%1$-10s", CicloReal) + ";"
                    + "\r\n";

            mensajes(" Nuevo Registro CREADO");
            //nuevo para retornar y cerrar teclado
            try {
                View view1 = findViewById(R.id.btncngrabar);
                InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            } catch (Exception x3) {
                mensajes("Problema al cambiar teclado");
                utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); No se creo la linea: " + linea + " en la ruta: ");

            }

            if (utils.EscribirLinea(f, linea)) {
                utils.EscribirLinea(cuentasEnvio, linea);
            } else {
                mensajes("Problema Grabando Nuevo Registro");
                utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); No se creo la linea: " + linea + " en la ruta: " + f.getAbsolutePath());
                return;
            }
        } catch (Exception ex) {
            mensajes("Problema Grabando Nuevo Registro \n");
            utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); No se creo la linea: " + linea);
        }
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);

        setResult(RESULT_OK, iBackActivity);
        finish();
    }*/

    private void grabarSalir() {

        String linea = "";
        try {
            File f = new File(AppPath + VariablesGlobales.getCarpetaLecturas()+"/" + "N" + nombrearchivo6);
            // Archivo de respaldo para recuperación en otro dispositivo
            File cuentasEnvio = new File(AppPath + VariablesGlobales.getCarpetaLecturas()+"/" + "CUENTASNUEVA" + serial + ".SDA");

            if (cnDescDepto.length() > 31) {
                cnDescDepto = cnDescDepto.substring(30, 32);
            } else {
                if (cnDescDepto.length() > 1) {
                    cnDescDepto = cnDescDepto.substring(0, 2);
                }
            }

            String InformeFinal = txtcuInforme.getText().toString().trim();
            InformeFinal = InformeFinal.replace("í", "i");
            InformeFinal = InformeFinal.replace("á", "a");
            InformeFinal = InformeFinal.replace("ó", "o");
            InformeFinal = InformeFinal.replace("ú", "u");
            InformeFinal = InformeFinal.replace("é", "e");
            InformeFinal = InformeFinal.replace("ñ", "n");
            InformeFinal = InformeFinal.replace("Ñ", "N");

            // Crear línea para archivo local (se mantiene para respaldo)
            String Tipomedidor = spnTipoMedida.getSelectedItem().toString().trim().substring(0,2);
            linea = String.format("%1$-64s", txtcnDireccion.getText().toString().trim()) + ";" +
                    String.format("%1$-20s", txtcnContador.getText().toString().trim()) + ";" +
                    String.format("%1$-15s", txtcuMarca.getText().toString().trim()) + ";" +
                    String.format("%1$-2S", Tipomedidor) + ";" +
                    String.format("%1$1s", txtcnDigitos.getText().toString().trim()) + ";" +
                    String.format("%1$7s", txtcnLectura.getText().toString().trim()) + ";" +
                    String.format("%1$-2s", txtcnObserv.getText().toString().trim()) + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCiclo)) + "")) + ";" +
                    String.format("%1$-32s", cnDescDepto.trim() + "") + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCodMunicipio)) + "")) + ";" +
                    String.format("%1$-3s", ((utils.parseStringToInteger(cnCodSector)) + "")) + ";" +
                    String.format("%1$-13s", ((utils.parseStringToInteger(cnCodRuta)) + "")) + ";" +
                    String.format("%1$-60s", InformeFinal.trim()) + ";" +
                    String.format("%1$-6s", edtCodRefere.getText().toString().trim()) + ";" +
                    String.format("%1$-16s", latitud) + ";" +
                    String.format("%1$-16s", longitud) + ";" +
                    String.format("%1$-20s", fechaHora) + ";" +
                    String.format("%1$-10s", altitud) + ";" +
                    String.format("%1$-3s", numSatelites) + ";" +
                    String.format("%1$-4s", lector) + ";" +
                    String.format("%1$-30s", foto1) + ";" +
                    String.format("%1$-30s", foto2) + ";" +
                    String.format("%1$-30s", foto3) + ";" +
                    String.format("%1$-10s", CicloReal) + ";"
                    + "\r\n";

            mensajes(" Nuevo Registro CREADO");

            // Cerrar teclado
            try {
                View view1 = findViewById(R.id.btncngrabar);
                InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            } catch (Exception x3) {
                mensajes("Problema al cambiar teclado");
                utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); Error teclado");
            }

            // Guardar en archivo local (respaldo)
            if (utils.EscribirLinea(f, linea)) {

                // ===== GUARDAR RESPALDO CUENTAS NUEVAS (para recuperación en otro dispositivo) =====
                utils.EscribirLinea(cuentasEnvio, linea);
                // ===== FIN RESPALDO =====

                // ===== GUARDAR EN REALM (REEMPLAZA ARCHIVO CUENTASNUEVA*.SDA) =====
                try {
                    EnvioCuentaNueva cuenta = new EnvioCuentaNueva();

                    // Datos de ubicación
                    cuenta.setCiclo(cnCiclo);
                    cuenta.setDescDepto(cnDescDepto);
                    cuenta.setCodMunicipio(cnCodMunicipio);
                    cuenta.setCodSector(cnCodSector);
                    cuenta.setCodRuta(cnCodRuta);
                    cuenta.setCicloReal(CicloReal);

                    // Datos del cliente/medidor
                    cuenta.setDireccion(txtcnDireccion.getText().toString().trim());
                    cuenta.setContador(txtcnContador.getText().toString().trim());
                    cuenta.setMarca(txtcuMarca.getText().toString().trim());
                    cuenta.setTipoMedidor(Tipomedidor);
                    cuenta.setDigitos(txtcnDigitos.getText().toString().trim());
                    cuenta.setLectura(txtcnLectura.getText().toString().trim());
                    cuenta.setObservacion(txtcnObserv.getText().toString().trim());
                    cuenta.setInforme(InformeFinal);
                    cuenta.setCodReferencia(edtCodRefere.getText().toString().trim());

                    // Coordenadas GPS
                    cuenta.setLatitud(latitud);
                    cuenta.setLongitud(longitud);
                    cuenta.setAltitud(altitud);
                    cuenta.setNumSatelites(numSatelites);
                    cuenta.setFechaHora(fechaHora);

                    // Datos del lector/terminal
                    cuenta.setLector(lector);
                    cuenta.setSerial(serial);

                    // Fotos asociadas
                    cuenta.setFoto1(foto1);
                    cuenta.setFoto2(foto2);
                    cuenta.setFoto3(foto3);

                    // Nombre del archivo
                    cuenta.setNombreArchivo(nombrearchivo6);

                    // Guardar en Realm
                    boolean guardado = CrudEnvioCuentaNueva.insertar(cuenta);

                    if (guardado) {
                        Log.i("ModuloCuentaNueva", "Cuenta nueva guardada en Realm: " + cuenta.getContador());
                    } else {
                        Log.e("ModuloCuentaNueva", "Error guardando cuenta nueva en Realm");
                        utils.Log(logfile, "[ModuloCuentaNueva] Error guardando en Realm");
                    }

                } catch (Exception e) {
                    Log.e("ModuloCuentaNueva", "Error guardando en Realm: " + e.getMessage());
                    utils.Log(logfile, "[ModuloCuentaNueva] Error Realm: " + e.getMessage());
                }
                // ===== FIN GUARDAR EN REALM =====

            } else {
                mensajes("Problema Grabando Nuevo Medidor");
                utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); No se creo la linea: " + linea + " en la ruta: " + f.getAbsolutePath());
                return;
            }
        } catch (Exception ex) {
            mensajes("Problema Grabando Nuevo Medidor \n");
            utils.Log(logfile, "[ModuloCuentaNueva]GrabarDatos(); No se creo la linea: " + linea);
        }

        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        setResult(RESULT_OK, iBackActivity);
        finish();
    }


    private boolean validarDatos() {

        if (txtcnDireccion.getText().toString().trim().equals("")) return false;

        if (spnTipoMedida.getSelectedItem().toString().trim().equals("")) return false;

        if (txtcnLectura.getText().toString().trim().equals("")) return false;

        if (txtcnContador.getText().toString().trim().equals("")) return false;

        if (txtcuMarca.getText().toString().trim().equals("")) return false;

        return true;
    }

    private void dialogMsg() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ModuloCuentaNueva.this);

        builder.setTitle("REPORTE CAMBIO MEDIDOR");
        builder.setMessage("¿Desea Grabar al Nuevo MEDIDOR?");
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                grabarSalir();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(ModuloCuentaNueva.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 50);
        toast.show();
    }

    public void verifyChart() {

        if (latitud.trim().length() > 16) {
            latitud.trim().substring(0, 15);
        }

        if (longitud.trim().length() > 16) {
            longitud.trim().substring(0, 15);
        }

        if (altitud.trim().length() > 10) {
            altitud = altitud.trim().substring(0, 9);
        }
        altitud = altitud.contains(",") ? altitud.replace(",", ".") : altitud;

        if (fechaHora.trim().equals("")) {//2018-03-01 14:13:11.000
            fechaHora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        }

        if (fechaHora.trim().length() > 20)
            fechaHora = fechaHora.substring(0, 19);
    }


    private void ejecutarProcesoDeFoto(String nombre, String tipomed, int D2Digital, int directorio, String tipoProceso, int veces) {

        vecesImagen = veces;
        boolean existeF = false;

        String directorioF = "/DCIM/FOTOGRAFIASL/";

        String existeFoto;

        File foto;

        try {
            existeFoto = VariablesGlobales.directorioactual + directorioF + nombre + "_CN";// + "_" + tipomed calendar.get(Calendar.YEAR)
            foto = new File(existeFoto.trim() + "01" + ".jpg");
            namePhoto = nombre + "_CN" + "01";// + "_" + tipomed
            if (foto1.equals("") && namePhoto.endsWith("_CN01")) {
                foto1 = namePhoto + ".jpg";
            }

            if (foto.exists()) {

                // boolean salir = true;
                int conteo = 2;

                while (conteo < 4) {

                    Log.e("error", "entra a existeFoto.trim() " + existeFoto.trim());
                    foto = new File(existeFoto.trim() + (String.format("%1$2s", conteo).replace(" ", "0")) + ".jpg"); //Ax:  303123_A1_02.jpg
                    namePhoto = nombre + "_CN" + (String.format("%1$2s", conteo).replace(" ", "0"));//+ "_" + tipomed
                    if (conteo == 2) {
                        foto2 = namePhoto + ".jpg";
                    } else {
                        foto3 = namePhoto + ".jpg";
                    }

                    if (!foto.exists()) { //Ax: Si la foto no existe, puedo tomar ese nombre
                        break;
                    }
                    if (conteo == 3 && foto.exists()) {
                        existeF = true;
                    }
                    conteo++;
                }
            }
            nameForIntent = namePhoto + ".jpg";

            VariablesGlobales.activarcamarafotografica = 1;

            if (D2Digital == 0) {

                if (existeF) {
                    mensajeT("Ya tomo todas las 3 fotos", msgLargo);

                } else {
                    nombredelaImagen = foto.getAbsolutePath(); //Ax: variable global para guardar nombre de imagen para el metodo II
                    cuentaFoto = nombre;

                    ejecutarProcesoDeFotoII(veces);//va a tomar foto mientras el codigo continua

                }

            } else {
                mensajeT("Proceso a color esta en desarrollo", msgCorto);
            }


        } catch (Exception ex) {
            mensajeT("PROBLEMA EN ESCRIBIR LA FOTOGRAFIA...\n " + ex.getMessage(), msgLargo);
        }

        VariablesGlobales.activarcamarafotografica = 0;

    }


    private void ejecutarProcesoDeFotoII(int veces) {//Ax este metodo se ejecuta la primera en 'ejecutarProcesoDeFoto' y de nuevo cuando la camara se cierra (LLamado desde onActivityResult ) veces es la cantidad de reintentos de tomar foto
        try {
            Log.e("error", "entra a fotos 2 veces: " + veces + " vecesImagen: " + vecesImagen + " exitoImagen: " + exitoImagen);
            if (veces > 0) {
                //----------------------------------------------------
                /*if (ActivityCompat.checkSelfPermission(ModuloCuentaNueva.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ModuloCuentaNueva.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ModuloCuentaNueva.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, TAKE_PICTURE);
                    utils.MensajeTime("La aplicación no tiene permisos de cámara", "TOMAR FOTO !!", this, 8);
                    return;
                }
                Uri output = Uri.fromFile(new File(nombredelaImagen));
                File x = new File(nombredelaImagen);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.e("error","entra a foto ");
                    dispatchTakePictureIntent();

                    nombrefoto = VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL/" + x.getName();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (!nombredelaImagen.trim().isEmpty()) {
                        // Uri output = Uri.fromFile(new File(nombredelaImagen));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                        vecesImagen--;
                        startActivityForResult(intent, TAKE_PICTURE);

                        nombrefoto = output.getPath();
                    } else {
                        utils.MensajeTime("Ha ocurrido un error, debe Tomar Foto de Nuevo", "TOMAR FOTO DE NUEVO", this, 8);
                       // logger.info("sin nombre de foto. otra var fotos:" + nombrefoto);
                    }
                }*/
                Uri output = Uri.fromFile(new File(nombredelaImagen));
                File x = new File(nombredelaImagen);

                vecesImagen--;
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
                        //logger.info("sin nombre de foto. otra var fotos:" + nombrefoto);
                    }
                }

            } else {
                vecesImagen = 0;

                if (exitoImagen > 0) { //la foto si existe (aparentemente)

                    try {
                        Log.e("error", "entra a AsyncCallWS 1");
                        TaskHelper.execute(new AsyncCallWS(), "ProcesaFotosAsync");
                    } catch (Exception ex) {
                        Log.e("error", "error a AsyncCallWS 1 " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            mensajeT("ERROR: Proceso Modulo de Fotografia II", msgCorto);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - NUEVA FORMA PARA TOMAR LA FOTO
    private void nwdispatchTakePictureIntent() {
        imageUri = createImageUri(nameForIntent);

        if (imageUri != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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


    private void mensajeT(String msg, int dur) { //Ax: 1 segundo: 1000

        toast = Toast.makeText(ModuloCuentaNueva.this, msg, dur);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.setDuration(dur);
        toast.show();
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
                                ejecutarProcesoDeFotoII(vecesImagen);//Ax: idealmente se tomo la foto

                            } else {//Ax: No se tomo foto
                                if (vecesImagen > 0) {
                                    mensajeT("No se tomo foto, pruebe de nuevo", msgMedio);
                                }
                                vecesImagen = 1;
                                ejecutarProcesoDeFotoII(vecesImagen);
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("ERROR", "[MenuDeLiquidacion]onActivityResultLauncher|ERROR|" + ex.getMessage());
                        //util.log(, "[MenuDeLiquidacion]onActivityResultLauncher()| Error -> " + ex.getMessage());
                        //logger.error("[MenuDeLiquidacion]onActivityResultLauncher()| Error -> " + ex.getMessage());
                    }
                }
            });


    private class AsyncCallWS extends AsyncTask<String, Integer, Void> {

        private ProgressDialog dialog = new ProgressDialog(ModuloCuentaNueva.this);
        public String asyncResponse = "";
        public String asynCallFrom = "";

        @Override
        protected Void doInBackground(String... params) {

            asynCallFrom = params[0];

            try {
                switch (asynCallFrom) {

                    case "ProcesaFotosAsync":
                        asyncResponse = ejecutarProcesoDeFoto3();
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = "";

            switch (asynCallFrom) {

                case "ProcesaFotosAsync":
                    Log.e("INF", "asyncResponse: " + asyncResponse);
                    if (!asyncResponse.contains("✓") && asyncResponse.contains("|")) {
                        String[] mss = asyncResponse.split("\\|");

                        if (!mss[1].trim().equals("")) {
                            mensajeOk("DEBE TOMAR FOTO DE NUEVO\n\n" + "Verifique Datos del contador: " + mss[1] + "\n  " + txtcnContador.getText().toString().trim(), "retomaFoto");
                        }
                    }
                    break;

            }

            if (!msg.trim().equals("")) {
                mensajeT(msg, msgMedio);
            }
            asyncResponse = "";
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
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
            String mensajefoto = utils2.ReduceImagen2(nombrefoto, getPhoneDate() + "-" + getPhoneHour() + " " + latitud + " " + longitud);

            File nombrefotoFile = new File(nombrefoto);

            if (!mensajefoto.trim().equals("")) {
                nombrefotoFile.delete();
                //  logger.info(mensajefoto + ", cuenta " + nombretempfoto + ", Registro:" + capRegistroActual + ", Foto:" + nombrefoto);
                return_msg = " |" + nombretempfoto;
            } else {

                //Ax: en este punto se procede a hacer envio de la foto, se crea el archivo F y se procede a enviar
                File foto = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "F" + nombrearchivo6);

                if (!nombrefoto.trim().isEmpty()) {
                    //nuevo para crear la linea que argumenta el nombre de la foto
                    String idFoto="999999999";
                    String cuentaFoto2="9999999";
                    String cadenaFoto = String.format("%11s", idFoto) + ";" + String.format("%9s", cuentaFoto2) + ";" + String.format("%1$-50s", nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length())) + ";\r\n";

                    if (!utils2.leerArchivoFotos(foto, nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length()))) {
                        //before 25
                        utils2.EscribirLinea(new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "BKEnvioFotos.SDA"), cadenaFoto);

                        if (!utils2.EscribirLinea(foto, String.format("%1$-30s", nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1, nombrefoto.length())) + ";X\r\n")) { //Aqui llena el archivo de fotos con el nombre de la foto tomada//'X'es no enviada
                            //   logger.info("Problema Grabando Nombre de Foto" + mensajefoto);
                            return_msg = " |" + nombretempfoto;
                        }
                    }

                    if (nombrefotoFile.length() > 400000) {
                        utils2.ReduceImagen2(nombrefoto, "");//Ax: intento de bajarle a algunas fotos que se escapan al proceso de reduccion
                    }

                    utils.CrearCopia(nombrefotoFile.getAbsolutePath(), VariablesGlobales.directorioBackUp + nombrefotoFile.getName());
                    borradoDcim();


                } else {
                    return_msg = " |" + nombretempfoto;
                }
            }
        } catch (Exception ex) {
            //logger.info("ejecutarProcesoDeFoto3 " + ex.getMessage());
            return_msg = " |" + nombretempfoto;
        }
        return return_msg;
    }*/

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
            String mensajefoto = utils2.ReduceImagen2(nombrefoto, getPhoneDate() + "-" + getPhoneHour() + " " + latitud + " " + longitud);

            File nombrefotoFile = new File(nombrefoto);

            if (!mensajefoto.trim().equals("")) {
                nombrefotoFile.delete();
                return_msg = " |" + nombretempfoto;
            } else {
                if (!nombrefoto.trim().isEmpty()) {

                    // ===== GUARDAR FOTO EN REALM (REEMPLAZA ARCHIVO F) =====
                    try {
                        String nombreSinExt = nombrefoto.substring(nombrefoto.lastIndexOf("/") + 1);

                        // Para cuentas nuevas usamos valores por defecto
                        boolean guardado = SyncHelper.registrarFoto(
                                "NUEVA",              // cuenta (marcador de cuenta nueva)
                                "",  // tipo medidor
                                "999999999",          // id registro (placeholder)
                                nombrefoto,           // ruta completa
                                "",                   // anno (se completará después)
                                "",                   // mes
                                cnCiclo,              // ciclo
                                lector,               // lector
                                serial                // terminal
                        );

                        if (guardado) {
                            Log.i("ModuloCuentaNueva", "Foto registrada en Realm: " + nombreSinExt);
                        } else {
                            Log.e("ModuloCuentaNueva", "Error registrando foto en Realm");
                        }
                    } catch (Exception e) {
                        Log.e("ModuloCuentaNueva", "Error registrando foto: " + e.getMessage());
                    }
                    // ===== FIN GUARDAR FOTO EN REALM =====

                    // Reducir si es muy grande
                    if (nombrefotoFile.length() > 400000) {
                        utils2.ReduceImagen2(nombrefoto, "");
                    }

                    // Crear copia de respaldo
                    utils.CrearCopia(nombrefotoFile.getAbsolutePath(), VariablesGlobales.directorioBackUp + nombrefotoFile.getName());
                    borradoDcim();

                } else {
                    return_msg = " |" + nombretempfoto;
                }
            }
        } catch (Exception ex) {
            return_msg = " |" + nombretempfoto;
        }
        return return_msg;
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


    public void capturaImagenFotografica() {
        // esto si vamos a registrar si la foto se tomo o no se tomo
        /*String contador = "";
        String marca = "";
        if (txtcnContador.getText().toString().trim().length() > 10) {
            contador = txtcnContador.getText().toString().trim().substring(0, 10);
        } else {
            contador = txtcnContador.getText().toString().trim();
        }

        if (txtcuMarca.getText().toString().trim().length() > 6) {
            marca = txtcuMarca.getText().toString().trim().substring(0, 6);
        } else {
            marca = txtcuMarca.getText().toString().trim();
        }*/
        ejecutarProcesoDeFoto(txtcnContador.getText().toString().trim(), "", 0, 0, "", 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE) {//Respuesta Tomar fotos en liq.

            if (resultCode == RESULT_OK) {
                Log.e("error", "data " + data);
                if (data != null) {

                    if (data.hasExtra("output")) {
                        name = data.getParcelableExtra("output");
                    }
                }
                new MediaScannerConnection.MediaScannerConnectionClient() {   // Para guardar la imagen en la galería, utilizamos una conexión a un MediaScanner
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
                ejecutarProcesoDeFotoII(vecesImagen);//Ax: idealmente se tomo la foto
            } else {//Ax: No se tomo foto

                if (vecesImagen > 0) {
                    mensajeT("No se tomo foto, pruebe de nuevo", msgMedio);
                }

                vecesImagen = 1;
                ejecutarProcesoDeFotoII(vecesImagen);
            }

//        else if (requestCode == CAMARA_REQUEST_CODE) { //camara nueva se quita 17 Jul
//
//            if (resultCode == RESULT_OK) {
//                vecesImagen = 0;
//                exitoImagen = 1;
//                ejecutarProcesoDeFotoII(vecesImagen);//Ax: idealmente se tomo la foto
//            } else {//Ax: No se tomo foto
//
//                if (vecesImagen > 0) {
//                    mensajeT("No se tomo foto, pruebe de nuevo", msgMedio);
//                }
//
//                vecesImagen = 1;
//                ejecutarProcesoDeFotoII(vecesImagen);
//            }
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
                            // logger.info("borradoDcim() " + f.getAbsolutePath());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            //logger.info("Error al borrar fotografia de /DCIM/Camera/");
        }
    }

    private boolean validarFoto() {

        if (foto1.toString().trim().equals("")) {
            mensajeT("ERROR: Por lo menos tome una foto", msgLargo);
            return false;
        }

        return true;
    }
}