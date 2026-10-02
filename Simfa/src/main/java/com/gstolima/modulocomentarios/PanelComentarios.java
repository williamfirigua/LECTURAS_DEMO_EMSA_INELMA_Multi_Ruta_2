package com.gstolima.modulocomentarios;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.TablaRegistroSalida;

public class PanelComentarios extends AppCompatActivity {

    TextView txtComentario;
    EditText lblComentario;
    ListView listadoComentarios;
    Comenta tablaComentarios = new Comenta();
    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
    public int registroActual = 1;

    boolean obliga = false;//Indica si este formulario se debe hacer obligado
    boolean obligaFoto = true;//reinicia variables al regresar
    public String directorioActual = "";
    String respuesta = "";
    String filtro = "";
    String foto;
    String info;
    boolean iguales = false;

    ArrayAdapter<String> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_comentarios);

        Bundle bundle = getIntent().getExtras();

        directorioActual = bundle.getString("DIRECTORIOACTUAL");
        registroActual = bundle.getInt("NUMEROREGISTROACTUAL");
        String NombreArchivos_2 = bundle.getString("NombreArchivos");
        obliga = bundle.getBoolean("obliga", false);
        filtro = bundle.getString("filtro");
        obligaFoto = bundle.getBoolean("obligafoto", true);
        iguales = bundle.getBoolean("iguales");

        listadoComentarios = (ListView) findViewById(R.id.listadoComentarios);
        txtComentario = (TextView) findViewById(R.id.txtComentario);
        lblComentario = (EditText) findViewById(R.id.lblComentario);

        item = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listadoComentarios.setOnItemClickListener(itemClickListener);
        tablaComentarios.setarchivo_Comenta(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/OBSERVA.TXT");
        infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + NombreArchivos_2);
        llenarListadoComentarios();
        txtComentario.requestFocus();
        txtComentario.setText("");

        txtComentario.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                foto = "0";
                info = "0";

                String cc = txtComentario.getText().toString();

                if (cc.length() < 3) return;

                String fi = cc.substring(cc.length() - 3, cc.length()).trim();
                Log.e("error","data---"+fi);

                if (fi.contains(";")) {
                    info = "1";
                }

                if (fi.contains(".")) {
                    foto = "1";
                }

                ingresarComentarioLectura(cc);
            }
        });

        lblComentario.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 6) {//Ax: se comprueba cadena llena y se va con datos a la otra actividad
                    busqueda();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (obliga) {
            mensajes("Comentario obligatorio");
            return;
        }
        finish();
    }

    //Ax trata de buscar la primera letra ingresada
    private void busqueda() {

        String coment = lblComentario.getText().toString().trim();

        if(!filtro.equals("")){
            if(!coment.toUpperCase().contains(filtro.toUpperCase())){
                mensajes("Codigo No valido");
                txtComentario.setText("");
                lblComentario.setText("");
                return;
            }
        }

        if (coment.length() != 1) return;
        int conteo = 0;
        try {
            if (tablaComentarios.abrir_Comenta(tablaComentarios.getarchivo_Comenta())) {

                for (int i = 1; i <= tablaComentarios.gettotal_Comenta(); i++) {
                    tablaComentarios.lectura_Comenta(i);

                    if (tablaComentarios.getComenta_CODIGO().trim().toLowerCase().equals(coment.toLowerCase())) {
                        txtComentario.setText(tablaComentarios.getComenta_CODIGO() + "-" + tablaComentarios.getComenta_DESCRIPCION().trim() + ".");
                        conteo++;
                    }
                }
            }

            if (conteo < 1) {
                mensajes("¡No existe codigo!");
                txtComentario.setText("");
                lblComentario.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tablaComentarios.Cerrar_Comenta();
        }
    }

    private void llenarListadoComentarios() {

        try {
            if (tablaComentarios.abrir_Comenta(tablaComentarios.getarchivo_Comenta())) {
                listadoComentarios.setAdapter(null);

                for (int i = 1; i <= tablaComentarios.gettotal_Comenta(); i++) {
                    tablaComentarios.lectura_Comenta(i);

                    if(!filtro.equals("")){
                        if(!filtro.toUpperCase().contains(tablaComentarios.getComenta_CODIGO().trim().toUpperCase()))continue;//Si no contiene caracteres del filtro, no se muestran
                    }

                    foto = " ";
                    info = " ";

                    if (tablaComentarios.getComenta_REQUIEREINFORME().trim().equals("1")) { //Ax: este machetezo es para saber si se requiere informe o foto, estos simbolos se recuperan despues
                        info = ";";//machete
                    }
                    if (tablaComentarios.getComenta_OBLIGAFOTO().trim().equals("1")) {
                        foto = ".";
                    }

                    item.add(tablaComentarios.getComenta_CODIGO() + "- " + tablaComentarios.getComenta_DESCRIPCION().trim() + " " + info + foto);
                }
                tablaComentarios.Cerrar_Comenta();
            }
            listadoComentarios.setAdapter(item);
        } catch (Exception e) {
            {
                mensajes("Hay problemas AL LLENAR LAS COMENTARIOS EN TERRENO");
            }
        }
    }

    private void ingresarComentarioLectura(String observacionAct) {

        try {
            respuesta = observacionAct;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Comentario Lectura");
            builder.setMessage("Aceptar el Comentario\n?" + observacionAct);//+ " " + tablaComentarios.getComenta_DESCRIPCION());
            builder.setIcon(R.drawable.ic_launcher);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida())) {

                        infoRegistroSalida.lectura_TablaRegistroSalida(registroActual);
                        if(iguales){
                            infoRegistroSalida.settablaRegistroSalida_causadenolectura(respuesta.substring(0, 2));//tablaComentarios.getComenta_CODIGO().trim());
                        }
                        else {
                            infoRegistroSalida.settablaRegistroSalida_comentario1(respuesta.substring(0, 2));//tablaComentarios.getComenta_CODIGO().trim());
                        }
                        VariablesGlobales.numObservaciones++;
                        infoRegistroSalida.escribir_TablaRegistroSalida(registroActual);
                        infoRegistroSalida.Cerrar_TablaRegistroSalida();

                        regresoaMenuLiquidacion();
                    } else {
                        mensajes("Problemas Procesando el archivo de salida");
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    respuesta = "";
                    foto = " ";
                    info = " ";
                    txtComentario.setText("");
                    lblComentario.setText("");
                    // regresoaMenuLiquidacion();
                }
            });
            builder.show();

        } catch (Exception e) {
            mensajes("Problemas Procesando el archivo de salida");
        }
    }

    public void regresoaMenuLiquidacion() {
        Intent regreso = new Intent(this, MenuDeLiquidacion.class);

        if(!obligaFoto){
            foto = "0";
            info = "0";
        }
        regreso.putExtra("info", info);
        regreso.putExtra("foto", foto);
        setResult(RESULT_OK, regreso);
        finish();
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            txtComentario.setText(((TextView) v).getText().toString().toUpperCase());
            foto = "0";
            info = "0";

            String cc = txtComentario.getText().toString();

            if (cc.length() < 3) return;

            String fi = cc.substring(cc.length() - 3, cc.length()).trim();
            Log.e("error","data---"+fi);

            if (fi.contains(";")) {
                info = "1";
            }

            if (fi.contains(".")) {
                foto = "1";
            }

            ingresarComentarioLectura(cc);
        }
    };

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(PanelComentarios.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.show();
    }
}
