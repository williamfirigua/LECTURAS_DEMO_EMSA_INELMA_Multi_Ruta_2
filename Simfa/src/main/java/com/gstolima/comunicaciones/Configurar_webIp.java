package com.gstolima.comunicaciones;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.Util.AsyncResponse;
import com.Util.Utils;
import com.Util.WSSoap;
import com.gstolima.accesoyseguridad.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Configurar_webIp extends AppCompatActivity implements AsyncResponse {

    public String URL;
    public String URL1;
    public String URL2;
    public String paginaWs;
    public String rutaAdm;
    public String paginaWs2;
    public int puertoAPI;
    CheckBox chkIp1;
    CheckBox chkIp2;
    CheckBox chkHttp1;

    EditText txtIpWs1;
    EditText txtIpWs2;
    EditText txtIpWs3;
    EditText txtIpWs4;
    EditText txtDescServWeb;
    EditText txtrutaadmin;
    EditText edtxPuertoAPI;
    EditText edtxMaxRegEnvio;
    EditText txtTramas;
    Button btnGrabarIp;
    Button btnPruebaWs;
    ImageView iconWebSoff;
    ImageView iconWebSon;
    TextView lblTransmite;
    String trama = "10"; //Ax: Por defecto
    String puerto = "85";
    private String nivelOperador;
    String usuario = "";
    String terminal = "";
    private Boolean pruebawsok;
    private Boolean chk1;
    private Boolean chk2;
    private Boolean chkhttp;
    Toast toast = null;
    Utils utils = new Utils();//Ax log y utilidades
    File logfile; //Ax: Es para los log de error

    Context ctx;
    String externalpath = "";
    CrudComunicaciones crudComuni;
    EditText txtdirWs;
    public String urlApi;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_web_ip);
        ctx = this;

        externalpath = ctx.getExternalFilesDir(null).getParent();
        String hardcoding = "/Android/data/";//Ax: todo: cambiar este hardcoding

        if (externalpath.contains(hardcoding)) {
            externalpath = externalpath.substring(0, externalpath.indexOf(hardcoding));
        }

        logfile = new File(externalpath + "/SIM_DCP/LOGEVENTOS.LOG");
        Intent intent = getIntent();
        nivelOperador = intent.getStringExtra("nivelOperador"); //Ax: capturar variable de la actividad anterior
        usuario = intent.getStringExtra("nombreE"); //Ax: capturar variable de la actividad anterior
        terminal = intent.getStringExtra("terminal"); //Ax: capturar variable de la actividad anterior
        pruebawsok = false;//intent.getBooleanExtra("pruebawsok", false);
        trama = "";//intent.getStringExtra("trama");

        chkIp1 = (CheckBox) findViewById(R.id.chkIp1);
        chkIp2 = (CheckBox) findViewById(R.id.chkIp2);
        //nuevo
        chkHttp1 = (CheckBox) findViewById(R.id.chkHttp1);

        txtdirWs = (EditText) findViewById(R.id.txtdirWs);
        btnGrabarIp = (Button) findViewById(R.id.btnGrabarIp);
        btnPruebaWs = (Button) findViewById(R.id.btnPruebaWs);
        lblTransmite = (TextView) findViewById(R.id.lblTransmite);
        iconWebSon = (ImageView) findViewById(R.id.iconwebserviceon);
        txtDescServWeb = (EditText) findViewById(R.id.txtDescServWeb);
        txtrutaadmin = (EditText) findViewById(R.id.txtrutaadmin);
        edtxPuertoAPI = (EditText) findViewById(R.id.edtxPuertoAPI);
        edtxMaxRegEnvio = (EditText) findViewById(R.id.edtxMaxRegEnvio);
        edtxMaxRegEnvio.setText(String.valueOf(CrudParametros.getMaxRegEnvio()));
        iconWebSoff = (ImageView) findViewById(R.id.iconwebserviceoff);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!nivelOperador.equals("A")) {// != "A"
            txtDescServWeb.setEnabled(false);
            txtrutaadmin.setEnabled(false);
            btnGrabarIp.setEnabled(false);
            txtdirWs.setEnabled(false);
            edtxPuertoAPI.setEnabled(false);
        /*txtIpWs1.setEnabled(false);
        txtIpWs2.setEnabled(false);
        txtIpWs3.setEnabled(false);
        txtIpWs4.setEnabled(false);
        txtTramas.setEnabled(false);*/
        }

        getParamsWs(0);

        //mostrarDatos();

        if (!pruebawsok) { //Ax: para no repetir prueba de conn
            iconWebSon.setVisibility(View.INVISIBLE);
            iconWebSoff.setVisibility(View.VISIBLE);
        } else {
            iconWebSoff.setVisibility(View.INVISIBLE);
            iconWebSon.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btnRegresar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data = new Intent();//Regresarle datos a counicaciones
                //data.putExtra("trama", trama);
                //data.putExtra("pruebawsok", pruebawsok);
                //data.putExtra("puerto", puerto);
                data.putExtra("url", URL);
                data.putExtra("paginaWs", paginaWs);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        findViewById(R.id.btnGrabarIp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateparams();
            }
        });

        findViewById(R.id.btnPruebaWs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkIp1.isChecked() || chkIp2.isChecked()) {
                    btnPruebaWs.setEnabled(false);
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute("");
                } else {
                    mensajeT("Chequee una casilla", 1000);
                }
            }
        });

        chkIp1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                  if (chkIp1.isChecked()) {
                                                      if (chkIp2.isChecked()) {
                                                          chkIp2.setChecked(false);
                                                      }
                                                      getParamsWs(1);
                                                  }
                                              }
                                          }
        );

        chkIp2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                  if (chkIp2.isChecked()) {
                                                      if (chkIp1.isChecked()) {
                                                          chkIp1.setChecked(false);
                                                      }
                                                      getParamsWs(2);
                                                  }
                                              }
                                          }
        );
    }

    public boolean updateparams() {
        try {
            BDComunicaciones bdc = new BDComunicaciones();
            int id;
            String dirws = txtdirWs.getText().toString().replace("http://", "").trim();
            dirws = dirws.replace("https://", "").trim();
            String pgws = txtDescServWeb.getText().toString().trim();
            String dir = txtrutaadmin.getText().toString().trim();
            //int papi = Integer.parseInt(edtxPuertoAPI.getText().toString().trim());
            if (!chkIp1.isChecked() && !chkIp2.isChecked())
            {
                chkIp1.setChecked(true);
            }
            String urlapi = edtxPuertoAPI.getText().toString().trim();
            if (!urlapi.isEmpty() && !urlapi.startsWith("http://") && !urlapi.startsWith("https://")) {
                mensajeT("La URL del API debe iniciar con http:// o https://", 1500);
                return false;
            }

            // Registros por ciclo de envio. Se valida aqui y no solo en el Crud
            // para que el operador vea el error, en vez de que el valor se
            // recorte en silencio.
            String maxRegTxt = edtxMaxRegEnvio.getText().toString().trim();
            if (!maxRegTxt.isEmpty()) {
                int maxReg;
                try {
                    maxReg = Integer.parseInt(maxRegTxt);
                } catch (NumberFormatException nfe) {
                    mensajeT("Registros por envio debe ser un numero", 1500);
                    return false;
                }
                if (maxReg < CrudParametros.MAX_REG_ENVIO_MIN || maxReg > CrudParametros.MAX_REG_ENVIO_MAX) {
                    mensajeT("Registros por envio debe estar entre "
                            + CrudParametros.MAX_REG_ENVIO_MIN + " y "
                            + CrudParametros.MAX_REG_ENVIO_MAX, 1500);
                    return false;
                }
                CrudParametros.updateMaxRegEnvio(maxReg);
            }


            if (chkIp1.isChecked() && !dirws.equals("") && !pgws.equals("")) {
                id = 1;
                bdc.setURL(dirws);
                bdc.setPaginaWs(pgws);
                bdc.setEstado("A");
                bdc.setUrlApi(urlapi);
                bdc.setRutaAdministrador(dir);
                bdc.sethttpSeguro(0);
                if (chkHttp1.isChecked()) {
                    bdc.sethttpSeguro(1);
                }
                crudComuni.updateParams(bdc, id);
                crudComuni.updateStatus(2, "I");
                if (chkHttp1.isChecked()) {
                    URL = "https://" + dirws;
                }
                else
                {
                    URL = "http://" + dirws;
                }
                paginaWs = pgws;
                Toast.makeText(this, "PARAMETROS ESTABLECIDOS CORRECTAMENTE", Toast.LENGTH_LONG).show();
                getParamsWs(id);
                return true;
            } else if (chkIp2.isChecked() && !dirws.equals("") && !pgws.equals("")) {
                id = 2;
                bdc.setURL(dirws);
                bdc.setPaginaWs(pgws);
                bdc.setEstado("A");
                bdc.setUrlApi(urlapi);
                bdc.setRutaAdministrador(dir);
                bdc.sethttpSeguro(0);
                if (chkHttp1.isChecked()) {
                    bdc.sethttpSeguro(1);
                }
                crudComuni.updateParams(bdc, id);
                crudComuni.updateStatus(1, "I");
                if (chkHttp1.isChecked()) {
                    URL = "https://" + dirws;
                }
                else
                {
                    URL = "http://" + dirws;
                }
                paginaWs = pgws;
                Toast.makeText(this, "PARAMETROS ESTABLECIDOS CORRECTAMENTE", Toast.LENGTH_LONG).show();
                getParamsWs(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            String[] time = getTimeError().split("\\|");
            utils.Log(logfile, "[ConfigWIP]getParamsWs()|" + ex.getMessage() + "|" + time[0] + "|" + time[1] + "|");
            return false;
        }
    }

    public boolean getParamsWs(int id) {
        try {
            if (id == 0) {
                BDComunicaciones bdc = crudComuni.getParams();
                if (bdc != null) {
                    if (bdc.getId() == 1) {
                        chk2 = false;
                        chk1 = true;
                    } else {
                        chk2 = true;
                        chk1 = false;
                    }
                    if (bdc.gethttpSeguro() == 1) {
                        URL = "https://" + bdc.getURL();
                        chkHttp1.setChecked(true);
                    }
                    else
                    {
                        chkHttp1.setChecked(false);
                        URL = "http://" + bdc.getURL();
                    }

                    paginaWs = bdc.getPaginaWs();
                    //puertoAPI = bdc.getPuertoApi();
                    urlApi = bdc.getUrlApi();
                    rutaAdm = bdc.getRutaAdministrador();
                    chkIp1.setChecked(chk1);
                    chkIp2.setChecked(chk2);
                    txtdirWs.setText(URL);
                    txtDescServWeb.setText(String.valueOf(paginaWs));
                    txtrutaadmin.setText(String.valueOf(rutaAdm));
                    edtxPuertoAPI.setText(urlApi);
                    return true;
                } else {
                    return false;
                }
            } else {
                BDComunicaciones bdc = crudComuni.getParamsbyId(id);
                if (bdc != null) {
                    if (id == 1) {
                        chk2 = false;
                        chk1 = true;
                    } else {
                        chk2 = true;
                        chk1 = false;
                    }
                    if (bdc.gethttpSeguro() == 1) {
                        URL = "https://" + bdc.getURL();
                        chkHttp1.setChecked(true);
                    }
                    else
                    {
                        chkHttp1.setChecked(false);
                        URL = "http://" + bdc.getURL();
                    }
                    paginaWs = bdc.getPaginaWs();
                    urlApi = bdc.getUrlApi();
                    rutaAdm = bdc.getRutaAdministrador();
                    chkIp1.setChecked(chk1);
                    chkIp2.setChecked(chk2);
                    //chkHttp1.setChecked(false);
                    txtdirWs.setText(URL);
                    txtrutaadmin.setText(String.valueOf(rutaAdm));
                    txtDescServWeb.setText(String.valueOf(paginaWs));
                    edtxPuertoAPI.setText(urlApi);

                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            String[] time = getTimeError().split("\\|");
            utils.Log(logfile, "[ConfigWIP]getParamsWs()|" + ex.getMessage() + "|" + time[0] + "|" + time[1] + "|");
            return false;
        }
        return true;
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
            utils.Log(logfile, "[ConfigWIP]getTimeError()|" + e + "|" + "2023-01-05" + "|" + "09:27:15" + "|");
            return "2023-01-05" + "|" + "09:27:15";
        }
    }

    /*private void mostrarDatos() {

        chkIp1.setChecked(chk1);
        chkIp2.setChecked(chk2);

        try {
            String temp = URL.replace("http://", "").replace(":", ".");
            temp = URL.replace("https://", "").replace(":", ".");
            String[] segmentos = temp.split("\\.");

            txtIpWs1.setText(segmentos[0]);
            txtIpWs2.setText(segmentos[1]);
            txtIpWs3.setText(segmentos[2]);
            txtIpWs4.setText(segmentos[3]);
            txtPuerto.setText(segmentos[4]);

            txtTramas.setText(trama + "");
            txtDescServWeb.setText(paginaWs);

        } catch (Exception ex) {
            Log.e("INFO","mostrarDatos | Error -> " + ex.getMessage());
            mensajeT("Error al mostrar datos\n" + ex.getMessage(), 1000);
        }
    }*/

    @Override
    public void processFinish(String output) {

    }


    private class AsyncCallWS extends AsyncTask<String, Integer, Void> {
        public String respuesta;
        ProgressDialog pDialog;
        int x = 0;

        @Override
        protected Void doInBackground(String... params) {
            // Log.i(TAG, "doInBackground");

            WSSoap wsoap = new WSSoap(URL, paginaWs);
            respuesta = wsoap.verificarWs("VALIDAR_CONEXION");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            try {
                Integer.parseInt(respuesta);
                iconWebSoff.setVisibility(View.INVISIBLE);
                iconWebSon.setVisibility(View.VISIBLE);
                lblTransmite.setText("Conexion con Webservice OK");

            } catch (NumberFormatException e) {
                iconWebSon.setVisibility(View.INVISIBLE);
                iconWebSoff.setVisibility(View.VISIBLE);
                lblTransmite.setText("Error de conexion con Webservice\n" + respuesta);
            }

            btnPruebaWs.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Configurar_webIp.this);
            pDialog.setMessage("Probando conexion...");
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    private void mensajeT(String msg, int dur) { //Ax: 1 segundo: 1000

        toast = Toast.makeText(this, msg, dur);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.setDuration(dur);
        toast.show();

    }
}