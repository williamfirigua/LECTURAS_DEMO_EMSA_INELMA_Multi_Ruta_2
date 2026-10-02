package com.gstolima.moduloanomalias;

import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.Util.Utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ModuloDeAnomalias extends AppCompatActivity {

    TextView txtAnomalia;
    EditText txtNumAnomalia;
    TextView txtTitleAnomalia;
    ListView listadoAnomaliasNoLectura;
    AnomaliaDeNoLectura anomaliaDeNoLectura = new AnomaliaDeNoLectura();
    SubAnomaliaDeNoLectura subAnomaliaDeNoLectura = new SubAnomaliaDeNoLectura();
    CodigosSac ControladorCodigosSac = new CodigosSac();
    ArrayAdapter<String> item;
    boolean busq = false; //Ax: indica si viene de busqueda

    String foto="";
    String info="";
    String filtro="";
    String indicadorProceso = "";
    String lectura = "";
    String UltimoInforme="";
    String code = "";
    boolean lecturasIguales = false;
    int registroActual = 0;
    Button btncnreotornar;
    int TOMARNUEVACRITICA = 0;
    boolean ismenu;
    boolean medidorCero;

    boolean Menu2_Activo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_de_anomalias);

        Bundle bundle = getIntent().getExtras();
        filtro = bundle.getString("filtro");
        lecturasIguales = bundle.getBoolean("lecturasIguales");
        registroActual = bundle.getInt("registroActual");
        ismenu  = bundle.getBoolean("ISMENU");
        medidorCero = bundle.getBoolean("medidorCero");
        UltimoInforme=bundle.getString("UltimoInforme");
        TOMARNUEVACRITICA = bundle.getInt("TOMARNUEVACRITICA");

        if (VariablesGlobales.moduloTrabajo.equals("ENT")) {
            indicadorProceso = "E";
        } else {
            indicadorProceso = "L";
        }
        txtTitleAnomalia =(TextView) findViewById(R.id.txtTitleAnomalia);
        txtAnomalia = (TextView) findViewById(R.id.txtAnomalia);
        txtNumAnomalia = (EditText) findViewById(R.id.txtNumAnomalia);//Ax: Creado para capturar manualmente numero de anomalia
        btncnreotornar = (Button) findViewById(R.id.btncnreotornar);
        listadoAnomaliasNoLectura = (ListView) findViewById(R.id.listadoAnomaliasNoLectura);
        if (TOMARNUEVACRITICA>1) {
            txtTitleAnomalia.setText("SELECCIONE CODIGO SAC");
        }
      //  item = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        item = new ArrayAdapter<String>(
                this,
                R.layout.item_simple_pequeno);

        listadoAnomaliasNoLectura.setOnItemClickListener(itemClickListener);
        anomaliaDeNoLectura.setArchivo_AnomaliaDeNoLectura(VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/CAUSAS.TXT");
        subAnomaliaDeNoLectura.setArchivo_SubAnomaliaDeNoLectura(VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/SUBCAUSAS.TXT");
        ControladorCodigosSac.setArchivo_CodigosSac(VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/CODIGOSSAC.TXT");

        if (filtro.equals("0")) {
            code="0";
            llenarListadoSubAnomalia(code);
        }
        else {
            llenarListadoAnomalias();
        }
        VariablesGlobales.datodebusqueda = "";
        txtAnomalia.requestFocus();
        txtAnomalia.setText("");

        txtNumAnomalia.setOnFocusChangeListener(new View.OnFocusChangeListener() {//Ax: borrar el texto en focus para que ingresen numero
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtNumAnomalia.setText("");
            }
        });

        txtNumAnomalia.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 6) {//Ax: se comprueba cadena llena y se va con datos a la otra actividad
                    busq = true;
                    comprobar(txtNumAnomalia.getText().toString().trim());
                }
                return false;
            }
        });

        btncnreotornar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                View view1= findViewById(R.id.btncnreotornar);
                InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                finish();
            }
        });
        if (lecturasIguales || filtro.equals("XXX")) {
            btncnreotornar.setVisibility(View.INVISIBLE);
        }


    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,
                "Debe registrar la anomalía antes de continuar",
                Toast.LENGTH_SHORT).show();
    }
    private void comprobar( String stemp) {

        if (stemp.equals("") && busq) {
            Toast.makeText(getApplicationContext(), "Debe ingresar un codigo", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e("error","busca1 "+stemp);
        stemp = stemp.substring(0, 2);

        if (anomaliaDeNoLectura.abrir_AnomaliaDeNoLectura(anomaliaDeNoLectura.getArchivo_AnomaliaDeNoLectura())) {
            Log.e("error","busca "+stemp);
            anomaliaDeNoLectura.buscarbinarioChar_AnomaliaDeNoLectura_II(stemp);//.buscarbinario_AnomaliaDeNoLectura(causal);
            anomaliaDeNoLectura.Cerrar_AnomaliaDeNoLectura();
        }
        if (anomaliaDeNoLectura.getEncontro_AnomaliaDeNoLectura() == 0) {
            Toast.makeText(getApplicationContext(), "Anomalia no existe en la Tabla\n" + stemp, Toast.LENGTH_LONG).show();
        } else {
            foto = anomaliaDeNoLectura.getAnomaliaDeNoLectura_OBLIGAFOTO().trim();
            info = anomaliaDeNoLectura.getAnomaliaDeNoLectura_REQUIEREINFORME().trim();
            txtAnomalia.setText(stemp + " " + anomaliaDeNoLectura.getanomaliaDeNoLectura_DESCRIPCION());

            if(code.trim().equals("C") && !lecturasIguales && !medidorCero){
                // Ax: se ejecuta el alertdialog asincrono para que el codigo continue y salga de onActivityResult y se pueda ver la vista this
                AnomMostrarAlertDialog("Causas no lectura", "Acepta Anomalia seleccionada\n" + stemp + " "
                        + anomaliaDeNoLectura.getanomaliaDeNoLectura_DESCRIPCION()+ "\n [SI] acepta lectura [NO] cierra con anomalia", stemp);
            }
            else {
                // Ax: se ejecuta el alertdialog asincrono para que el codigo continue y salga de onActivityResult y se pueda ver la vista this
                if (!code.trim().equals("0")) {
                    AnomMostrarAlertDialog("Causas no lectura", "Acepta Anomalia seleccionada?\n" + stemp + " " + anomaliaDeNoLectura.getanomaliaDeNoLectura_DESCRIPCION(), stemp);
                }
                else if (!Menu2_Activo)
                {
                    int resultado = (llenarListadoSubAnomalia(code));
                    if (resultado > 0) {
                        return;
                    }


                }
            }
        }
        anomaliaDeNoLectura.Cerrar_AnomaliaDeNoLectura();
    }
    private void comprobarSubAnomalia( String stemp) {

        if (stemp.equals("") && busq) {
            Toast.makeText(getApplicationContext(), "Debe Seleccionar Desglose", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e("error","busca1 "+stemp);
       String  stemp2= stemp.trim();
        stemp = stemp.substring(0, 2);
        txtAnomalia.setText(stemp2);
        UltimoInforme  =stemp2.trim().substring(4,stemp2.trim().length());//SE QUITA EL -3
          //  txtAnomalia.setText(stemp + " " + subAnomaliaDeNoLectura.getSubAnomaliaDeNoLectura_DESCRIPCION());

            if(code.trim().equals("C") && !lecturasIguales && !medidorCero){
                // Ax: se ejecuta el alertdialog asincrono para que el codigo continue y salga de onActivityResult y se pueda ver la vista this
                AnomMostrarAlertDialog("Observacion de Causa", "Acepta Observacion de lo seleccionada= " + stemp + "\n"
                        + stemp2 + "\n [SI] Acepta Observ con Lectura [NO] Cierra sin Lectura", stemp);

            }
            else
            {
                // Ax: se ejecuta el alertdialog asincrono para que el codigo continue y salga de onActivityResult y se pueda ver la vista this
                AnomMostrarAlertDialog("Observacion de la causa", "Acepta Observacion de la Anomalia seleccionada\n" + stemp + " "
                        + stemp2 + "\n [SI] Acepta Causa-Observacion [NO] Cambiar Seleccion", stemp);


            }

    }

    private void comprobarCodigoSAC( String stemp) {

        if (stemp.equals("") && busq) {
            Toast.makeText(getApplicationContext(), "Debe Seleccionar un Codigo SAC", Toast.LENGTH_LONG).show();
            return;
        }
        Log.e("error","busca1 "+stemp);
        String  stemp2= stemp.trim();
        stemp = stemp.substring(0, 3);
        txtAnomalia.setText(stemp2);
        UltimoInforme  =stemp2.trim().substring(5,stemp2.trim().length()-3);
        //  txtAnomalia.setText(stemp + " " + subAnomaliaDeNoLectura.getSubAnomaliaDeNoLectura_DESCRIPCION());


            AnomMostrarAlertDialog("CODIGOS SUB", "Acepta el codigo SUB seleccionado= " + stemp + "\n"
                    + stemp2 + "\n [SI] Acepta Codigo SUB [NO] Cambiara Seleccion", stemp);



    }

    //Ax: metodo para lanzar AlertDialog (que es asincrono) y post-ejecutar algun otro metodo con el parametro 'metodo'
    public void AnomMostrarAlertDialog(String titulo, String mensaje, final String cod) { //Crea un alertDialog con si-no y espera hasta un clic SI o No

        final AlertDialog.Builder builder = new AlertDialog.Builder(ModuloDeAnomalias.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);


        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                int resultado=0;
                if (TOMARNUEVACRITICA<2) {
                    if (!Menu2_Activo) {
                        resultado = (llenarListadoSubAnomalia(code));
                        if (resultado > 0)
                            return;

                    }
                    if (code.trim().equals("C") && !lecturasIguales) {
                        lectura = "1";
                        getSendData(" " + cod);//La de texto viene con un espacio adelante

                    } else {
                        getSendData(" " + cod);//La de texto viene con un espacio adelante
                    }
                }
                else
                {
                    //entregar el resultado de la novedad de critica
                    getSendData(" " + cod);//La de texto viene con un espacio adelante

                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (TOMARNUEVACRITICA<2) {
                    if (code.trim().equals("C")) {

                        int resultado = 0;
                        if (!Menu2_Activo) {
                            resultado = (llenarListadoSubAnomalia(code));
                            if (resultado > 0)
                                return;

                        }
                        lectura = "0";
                        getSendData(" " + cod);//La de texto viene con un espacio adelante
                    } else {
                        txtNumAnomalia.setText("");
                        txtAnomalia.setText("");
                        foto = "0";
                        info = "0";
                        lectura = "0";
                        UltimoInforme = "";
                    }
                }
                else {
                    //para proseguir cuando es codigo sac
                    Toast.makeText(getApplicationContext(), "Debe Seleccionar un codigo SAC para cerrar la cuenta", Toast.LENGTH_LONG).show();
                    return;

                }
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /*public void getSendData(String snumero) { //Ax. envia datos a la actividad que la llamo y cierra esta
        if (!UltimoInforme.trim().equals("") && !UltimoInforme.trim().toUpperCase().equals("COMENTARIO LIBRE"))
        {
            info="0";
        }
        if (UltimoInforme.trim().length()>60)
        {
            UltimoInforme =UltimoInforme.trim().substring(0,59);
        }
        Log.e("error","envia "+snumero);
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("snumero", snumero.trim());
        iBackActivity.putExtra("info", info);
        iBackActivity.putExtra("foto", foto);
        iBackActivity.putExtra("permiteLectura", lectura);
        iBackActivity.putExtra("registroActual",registroActual+"");
        iBackActivity.putExtra("UltimoInforme",UltimoInforme+"");
        setResult(RESULT_OK, iBackActivity);
        finish();
    }*/
    public void getSendData(String snumero) { //Ax. envia datos a la actividad que la llamo y cierra esta
        if (!UltimoInforme.trim().equals("") && !UltimoInforme.trim().toUpperCase().equals("COMENTARIO LIBRE"))
        {
            info="0";

        }
        String informe = UltimoInforme.trim();
        if (informe.endsWith("*")) {
            // Si no quieres mostrar el *
            informe = informe.substring(0, informe.length() - 1).trim();
            UltimoInforme = informe;
            lectura = "1";
        }
        if (snumero.trim().equals("0"))
        {
            lectura = "1";
        }
        if (UltimoInforme.trim().length()>60)
        {
            UltimoInforme =UltimoInforme.trim().substring(0,59);
        }
        Log.e("error","envia "+snumero);
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("snumero", snumero.trim());
        iBackActivity.putExtra("info", info);
        iBackActivity.putExtra("foto", foto);
        iBackActivity.putExtra("permiteLectura", lectura);
        iBackActivity.putExtra("registroActual",registroActual+"");
        iBackActivity.putExtra("UltimoInforme",UltimoInforme+"");
        setResult(RESULT_OK, iBackActivity);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.modulo_de_anomalias, menu);
        return true;
    }

    private void llenarListadoAnomalias() {
        try {
            String LecturaSi = " NO";
            String FotoSi = " NO - ";
            String InformeSi = " NO -";
            if (TOMARNUEVACRITICA<2) {
                if (anomaliaDeNoLectura.abrir_AnomaliaDeNoLectura(anomaliaDeNoLectura.getArchivo_AnomaliaDeNoLectura())) {
                    listadoAnomaliasNoLectura.setAdapter(null);
                    for (int i = 1; i <= anomaliaDeNoLectura.getTotal_AnomaliaDeNoLectura(); i++) {
                        anomaliaDeNoLectura.lectura_AnomaliaDeNoLectura(i);
                        foto = " ";
                        info = " ";
                        lectura = "0";
                        FotoSi = " NO - ";
                        InformeSi = " NO -";
                        LecturaSi = " NO";

                        if (!filtro.equals("")) {

                            if (filtro.equals("XXX")) {

                                if (anomaliaDeNoLectura
                                        .getAnomaliaDeNoLectura_LECTURA()
                                        .trim()
                                        .equals("0")) {

                                    continue;
                                }

                            } else {

                                String codigo =
                                        anomaliaDeNoLectura
                                                .getanomaliaDeNoLectura_CODIGO()
                                                .trim()
                                                .toUpperCase();

                                boolean encontrado = false;

                                String[] filtros =
                                        filtro.toUpperCase().split(",");

                                for (String itemFiltro : filtros) {

                                    if (codigo.equals(itemFiltro.trim())) {

                                        encontrado = true;
                                        break;
                                    }
                                }

                                if (!encontrado)
                                    continue;
                            }
                        }

                        if (anomaliaDeNoLectura.getAnomaliaDeNoLectura_OBLIGAFOTO().trim().equals("1")) {
                            foto = ".";
                            FotoSi=" SI - ";
                        }

                        if (anomaliaDeNoLectura.getAnomaliaDeNoLectura_REQUIEREINFORME().trim().equals("1")) { //Ax: este machetezo es para saber si se requiere informe o foto, estos simbolos se recuperan despues
                            info = ";";
                            InformeSi=" SI - ";

                        }
                        if (anomaliaDeNoLectura.getAnomaliaDeNoLectura_LECTURA().trim().equals("1")) {
                            lectura = "*";
                            LecturaSi = " SI";
                        }
//!anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("0")
                        if (!anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("")) {
                            if (indicadorProceso.equals(anomaliaDeNoLectura.getAnomaliaDeNoLectura_INDICADOR().trim())) {
                                if (filtro.equals("")) {
                                    if (!anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("0") && !anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("7") &&!anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("40")) {//&& !anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO().trim().equals("4")
                                        item.add(anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO() + "- " + String.format("%-20s", anomaliaDeNoLectura.getanomaliaDeNoLectura_DESCRIPCION().trim()) + " |" + info +InformeSi+ foto +FotoSi + lectura+LecturaSi+ "|");
                                    }
                                } else {
                                    item.add(anomaliaDeNoLectura.getanomaliaDeNoLectura_CODIGO() + "- " + String.format("%-20s", anomaliaDeNoLectura.getanomaliaDeNoLectura_DESCRIPCION().trim())+ " |" + info +InformeSi+ foto +FotoSi + lectura+LecturaSi+ "|");
                                }
                            }
                        }
                    }
                    anomaliaDeNoLectura.Cerrar_AnomaliaDeNoLectura();
                }

            }
            else {
                //nuevo modulo llenando en la misma forma los codigos sac
                if (ControladorCodigosSac.abrir_CodigosSac(ControladorCodigosSac.getArchivo_CodigosSac())) {
                    listadoAnomaliasNoLectura.setAdapter(null);
                    for (int i = 1; i <= ControladorCodigosSac.getTotal_CodigosSac(); i++) {
                        ControladorCodigosSac.lectura_CodigosSac(i);
                        foto = " ";
                        info = " ";
                        lectura = "0";

                            if (ControladorCodigosSac.getCodigosSac_INDICADOR().equals("I") && TOMARNUEVACRITICA==1) {
                                    item.add(ControladorCodigosSac.getCodigosSac_CODIGO() + "- " + ControladorCodigosSac.getCodigosSac_DESCRIPCION().trim() + " " + info + foto + lectura);

                            } else {
                                if (ControladorCodigosSac.getCodigosSac_INDICADOR().equals("S") && TOMARNUEVACRITICA == 2) {
                                    item.add(ControladorCodigosSac.getCodigosSac_CODIGO() + "- " + ControladorCodigosSac.getCodigosSac_DESCRIPCION().trim() + " " + info + foto + lectura);
                                }
                            }

                    }
                    ControladorCodigosSac.Cerrar_CodigosSac();
                }

            }
            listadoAnomaliasNoLectura.setAdapter(item);
        } catch (Exception e) {
            {
                String Mierror = e.toString();

                if ( TOMARNUEVACRITICA<2) {
                    Toast.makeText(getApplicationContext(), "Modulo de listar \nAnomalias con Problemas", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Modulo de listar \nCodigos SAC de critica con Problemas", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private int llenarListadoSubAnomalia(String Codigo) {
       int NumReg=0;
                try {
                    if (subAnomaliaDeNoLectura.abrir_SubAnomaliaDeNoLectura(subAnomaliaDeNoLectura.getArchivo_SubAnomaliaDeNoLectura())) {
                        listadoAnomaliasNoLectura.clearAnimation();
                        listadoAnomaliasNoLectura.setAdapter(null);
                        item.clear();
                        String Cadena="";

                        for (int i = 1; i <= subAnomaliaDeNoLectura.getTotal_SubAnomaliaDeNoLectura(); i++) {
                            subAnomaliaDeNoLectura.lectura_SubAnomaliaDeNoLectura(i);
                            Cadena=subAnomaliaDeNoLectura.getSubAnomaliaDeNoLectura_CODIGO().trim().toUpperCase();
                            if (Cadena.equals(Codigo.trim().toUpperCase()))
                            {
                                Menu2_Activo = true;
                                NumReg++;
                                item.add(subAnomaliaDeNoLectura.getSubAnomaliaDeNoLectura_CODIGO()+ "- " + subAnomaliaDeNoLectura.getSubAnomaliaDeNoLectura_DESCRIPCION().trim());
                            }

                        }
                        subAnomaliaDeNoLectura.Cerrar_SubAnomaliaDeNoLectura();
                    }
                    listadoAnomaliasNoLectura.setAdapter(item);
                } catch (Exception e) {
                    {
                        Toast.makeText(getApplicationContext(), "Modulo de listar \nSubCausas con Problemas", Toast.LENGTH_LONG).show();
                        return NumReg;
                    }
                }
                return NumReg;
    }
    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            if ( TOMARNUEVACRITICA<2) {
                if (!Menu2_Activo) {
                    foto = "0";
                    info = "0";
                    lectura = "0";

                    String cc = ((TextView) v).getText().toString().toUpperCase();

                    String fi = cc.substring(cc.length() - 19, cc.length()).trim();

                    code = cc.substring(0, 2).trim();

                    Log.e("errora", "fffff " + code);
                    txtAnomalia.setText(cc);

                    if (fi.contains(";")) {
                        info = "1";
                    }

                    if (fi.contains(".")) {
                        foto = "1";
                    }

                    if (fi.contains("*") && !lecturasIguales && !medidorCero) {
                        lectura = "1";
                    }
                    busq = false;
                    comprobar(cc);// getSendData(cc.substring(0, 2));
                } else {
                    String cc = ((TextView) v).getText().toString().toUpperCase();
                    //String fi = cc.substring(cc.length() - 19, cc.length()).trim();
                    code = cc.substring(0, 2).trim();
                    Log.e("errora", "fffff " + code);
                    txtAnomalia.setText(cc);

                    busq = false;
                    comprobarSubAnomalia(cc);// getSendData(cc.substring(0, 2));
                }
            }
            else {
                foto = "0";
                info = "0";
                lectura = "0";

                String cc = ((TextView) v).getText().toString().toUpperCase();

               // String fi = cc.substring(cc.length() - 19, cc.length()).trim();

                code = cc.substring(0, 3).trim();

                Log.e("errora", "fffff " + code);
                txtAnomalia.setText(cc);
                busq = false;
                comprobarCodigoSAC(cc);// getSendData(cc.substring(0, 2));
            }
        }
    };

    //@Override
 //   public void onBackPressed() {
 //       salir();
        // super.onBackPressed();
 //   }

    private void salir() {

        if (!ismenu) {
            mensajes("Debe seleccionar la anomalia");
            return;
        }
//nuevo para retornar y cerrar teclado

        View view1= findViewById(R.id.btncnreotornar);
        InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Capturar Anomalia");
        builder.setMessage("¿Desea salir de Anomalias?");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(ModuloDeAnomalias.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.show();
    }
}
