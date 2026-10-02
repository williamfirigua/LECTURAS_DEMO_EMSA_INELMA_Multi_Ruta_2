package com.gstolima.moduloopcionesupervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.TablaAforadores_Lector;

public class OpcionesDeSupervisor extends AppCompatActivity {

    Button btnDesactivarImpresora; // Ax: antes 'Cambiar Fecha/Hora' - ahora desactiva la obligatoriedad de impresora
    Button btnAnlAnl;
    Button btnRetornarA;
    Button btnHabilitarNavegacion; // Ax: antes 'Activar Camara' - ahora habilita/deshabilita avanzar-retroceder
    Button btnAnlingreso;

    EditText txtOsClave;
    EditText txtOsCod;

    String path = "";

    boolean asignanolectura=false;
    boolean navegacionHabilitada=false; // Ax: estado de avanzar/retroceder, recibido y devuelto a MenuDeLiquidacion
    boolean desactivarImpresoraObligatoria=false; // Ax: autorización de trabajar sin impresora vinculada

    TextView txtSupervisor;

    ImageView iconAsigNoLect;
    ImageView iconFechaHora;
    ImageView iconActivCam;
    ImageView iconRetornar;

    TablaAforadores_Lector tablaAforadores = new TablaAforadores_Lector();

    Spinner spnCaus;
    String valorCausa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_supervisor);


        btnAnlAnl = (Button) findViewById(R.id.btnAnlAnl);
        btnRetornarA = (Button) findViewById(R.id.btnRetornarA);
        btnHabilitarNavegacion = (Button) findViewById(R.id.btnHabilitarNavegacion);
        btnDesactivarImpresora = (Button) findViewById(R.id.btnDesactivarImpresora);
        btnAnlingreso = (Button) findViewById(R.id.btnAnlingreso);

        txtOsClave = (EditText) findViewById(R.id.txtOsClave);
        txtOsCod = (EditText) findViewById(R.id.txtOsCod);

        txtSupervisor = (TextView) findViewById(R.id.txtSupervisor);

        iconAsigNoLect = (ImageView) findViewById(R.id.iconAsigNoLect);
        iconFechaHora = (ImageView) findViewById(R.id.iconFechaHora);
        iconActivCam = (ImageView) findViewById(R.id.iconActivCam);
        iconRetornar = (ImageView) findViewById(R.id.iconRetornar);
        spnCaus = (Spinner) findViewById(R.id.spnCaus);

        Bundle bundle = getIntent().getExtras();

        path = bundle.getString("directorioactual");
        navegacionHabilitada = bundle.getBoolean("navegacionHabilitada");

        ArrayAdapter causA = ArrayAdapter.createFromResource(this, R.array.anomalias, android.R.layout.simple_spinner_item);
        causA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCaus.setAdapter(causA);

        if(navegacionHabilitada){
            iconActivCam.setImageResource(R.drawable.icon_fecha);
        }else{ iconActivCam.setImageResource(R.drawable.primero);}

        btnDesactivarImpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desactivarImpresoraObligatoria) {
                    alert("La obligatoriedad de impresora ya fue desactivada en esta sesión.", "Información");
                    return;
                }
                MostrarAlertDialog("Impresora Obligatoria",
                        "¿Autoriza DESACTIVAR la obligatoriedad de impresora?\n" +
                                "El operario podrá continuar trabajando en campo\n" +
                                "sin impresora vinculada.",
                        "desactivarimpresora");
            }
        });

        btnAnlAnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(valorCausa.equals("") || valorCausa.equals("0")){
                        alert("Debe seleccionar la causal ","Alerta");
                    }
                    else {
                        MostrarAlertDialog("Alerta de Cierre de Ruta", "Este Proceso dara por\n terminada la Ruta\n de Facturacion. \nDesea Cerrarla la ruta?", "asignarnolectura");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnHabilitarNavegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNavegacion();
            }
        });

        btnAnlingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compruebaSuperv();
            }
        });

        btnRetornarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volveraLiquidacion();
            }
        });

        spnCaus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] select = spnCaus.getSelectedItem().toString().split("-");
                valorCausa = select[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtOsClave.requestFocus();
    }

    @Override
    public void onBackPressed() {
        volveraLiquidacion();
    }

    private void volveraLiquidacion() {

        Log.e("error","data causal "+valorCausa);
        Intent iBackActivity = new Intent(this, com.gstolima.accesoyseguridad.MenuDeLiquidacion.class);
        iBackActivity.putExtra("navegacionHabilitada", navegacionHabilitada);
        iBackActivity.putExtra("desactivarImpresoraObligatoria", desactivarImpresoraObligatoria);
        iBackActivity.putExtra("asignanolectura",asignanolectura);
        iBackActivity.putExtra("asignanocausa",valorCausa);
        iBackActivity.putExtra("supervisorAutoriza", txtSupervisor.getText().toString().trim());

        setResult(Activity.RESULT_OK, iBackActivity);
        finish();
    }

    private void compruebaSuperv() {
        try {
            String user = txtOsCod.getText().toString().trim();
            String pass = txtOsClave.getText().toString().trim();

            if (user.equals("") || pass.equals("")) return;

            tablaAforadores.setarchivo_Lector(path + VariablesGlobales.getCarpetaLecturas()+"/LECTOR.TXT");

            if (tablaAforadores.abrir_Lector(tablaAforadores.getarchivo_Lector())) {

                tablaAforadores.buscarSecuencial_Lector(user);

                if (tablaAforadores.getencontro_Lector() > 0) {

                    if (tablaAforadores.getLector_CLAVE().trim().equals(pass) && tablaAforadores.getLector_ESTADO().trim().toUpperCase().equals("S")) {

                        txtSupervisor.setText(tablaAforadores.getLector_DESCRIPCION().trim());
                        btnHabilitarNavegacion.setEnabled(true);
                        btnAnlAnl.setEnabled(true);
                        btnDesactivarImpresora.setEnabled(true);

                        return;
                    } else {
                        mensajesT("Supervisor no existe o Clave incorrecta", 400);
                    }
                } else {
                    mensajesT("Usuario no existe", 500);
                }
            } else {
                mensajesT("No se ha podido abrir archivo Lectores", 500);
            }
            btnHabilitarNavegacion.setEnabled(false);
            btnAnlAnl.setEnabled(false);
            btnDesactivarImpresora.setEnabled(false);

        } catch (Exception ex) {
            mensajesT("Problemas al identificar usuario", 500);
            ex.printStackTrace();
        }
    }

    private void toggleNavegacion() {
        try {
            if(navegacionHabilitada){
                MostrarAlertDialog("Avanzar / Retroceder","Desea DESHABILITAR (apagar) los\nbotones de avanzar y retroceder?","navegacion");
            }else{
                MostrarAlertDialog("Avanzar / Retroceder","Desea HABILITAR (encender) los\nbotones de avanzar y retroceder?","navegacion");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Ax: metodo para lanzar AlertDialog (que es asincrono) y post-ejecutar algun otro metodo con el parametro 'metodo'
    public void MostrarAlertDialog(String titulo, String mensaje, final String NombreMetodo) { //Crea un alertDialog con si-no y espera hasta un clic SI o No

        final AlertDialog.Builder builder = new AlertDialog.Builder(OpcionesDeSupervisor.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) { //Esto es para reusar este metodo 'MostrarAlertDialog' con mas llamados en 'NombreMetodo'
                    case "navegacion":
                        navegacionHabilitada = !navegacionHabilitada;
                        iconActivCam.setImageResource(navegacionHabilitada
                                ? R.drawable.flecha
                                : R.drawable.primero);
                        break;

                    case "desactivarimpresora":
                        desactivarImpresoraObligatoria = true;
                        break;

                    case "asignarnolectura":
                        MostrarAlertDialog("Alerta de Cierre de Ruta","Confirma que Desea \nCerrar la Ruta en Proceso?","asignarnolectura2");
                        break;

                    case "asignarnolectura2":
                        asignanolectura=true;
                        volveraLiquidacion();
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
                    case   "navegacion":
                        break;
                    case "desactivarimpresora":
                        break;
                    case "asignarnolectura":
                        break;
                    case "asignarnolectura2":
                        break;
                    default:
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void mensajesT(String msg, int dur) { //Ax: 1 segundo: 1000
        Toast toast = Toast.makeText(OpcionesDeSupervisor.this, msg, dur);
        toast.setGravity(Gravity.TOP, 10, 370);
        toast.setDuration(dur);
        toast.show();
    }

    public void alert(String mensaje,String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage(mensaje)
         .setTitle(titulo);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
         builder.create().show();
    }
}
