package com.gstolima.modulocaptnovedlect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.Aforos;
import com.gstolima.tablas.ItemAforos;

import java.io.File;

public class ModuloActualizaAforos extends AppCompatActivity {

    TextView lbltotal;

    EditText txtCodigoAforo;
    EditText txtCantidaEquipos;
    EditText txtVatiosAforo;
    EditText txtTipoAforo;
    EditText txtCmbAforos;
    EditText txtCmbTipoAforo;

    Button btnGrabarAforo;
    Button btnReturnaforos;

    ImageButton imgbtnaforos;
    ImageButton imgbtnaforostipo;

    String[] Op_Aforos;
    String[] Op_Aforostipo;

    String directorioActual;
    String descripcionaforo;
    String Cuenta = "";
    String documento = "";
    String ciclo = "";
    String mes = "";
    String anno = "";
    String lector = "";
    String archivo = "";

    File logfile;
    Utils utils = new Utils();//Ax log y utilidades
    Aforos mi_Aforo = new Aforos();
    int sino_grabaaforo = 0; //Registra si o no cuando sale el alertdialog 0 = neutral
    ItemAforos miAforos = new ItemAforos();

    AlertDialog levelDialog;

    TableLayout tabla;
    TableLayout tablacliente;
    TableLayout tablacabecera;
    TableRow.LayoutParams layoutFila;
    TableRow fila;

    Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualiza_aforos);

        txtCodigoAforo = (EditText) findViewById(R.id.txtCodigoAforo);
        txtCantidaEquipos = (EditText) findViewById(R.id.txtCantidaEquipos);
        txtVatiosAforo = (EditText) findViewById(R.id.txtVatiosAforo);
        txtTipoAforo = (EditText) findViewById(R.id.txtTipoAforo);
        txtCmbAforos = (EditText) findViewById(R.id.txtCmbAforos);
        txtCmbTipoAforo = (EditText) findViewById(R.id.txtCmbTipoAforo);

        lbltotal = (TextView) findViewById(R.id.lbltotal);

        btnGrabarAforo = (Button) findViewById(R.id.btnGrabarAforo);
        btnReturnaforos = (Button) findViewById(R.id.btnReturnaforos);

        imgbtnaforos = (ImageButton) findViewById(R.id.imgbtnaforos);
        imgbtnaforostipo = (ImageButton) findViewById(R.id.imgbtnaforostipo);

        tabla = (TableLayout) findViewById(R.id.tblaforos);
        tablacliente = (TableLayout) findViewById(R.id.tblcliente);
        tablacabecera = (TableLayout) findViewById(R.id.tblcabeceraforo);

        tabla.setPadding(0, 0, 0, 0);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);//(int w, int h, float initWeight)

        resource = this.getResources();
        Bundle bundle = getIntent().getExtras();

        documento = (bundle.getString("documento"));
        ciclo = (bundle.getString("ciclo"));
        mes = (bundle.getString("mes"));
        anno = (bundle.getString("anno"));
        lector = (bundle.getString("lector"));
        Cuenta = (bundle.getString("cuenta"));
        directorioActual = (bundle.getString("directorio"));
        archivo = (bundle.getString("archivo"));

        Aforos_Load();

        LlenarListaCliente("Direccion", directorioActual);
        LlenarListaCliente("Documento", documento);
        LlenarListaCliente("Ciclo", ciclo);
        LlenarListaCliente("Anno", anno);
        LlenarListaCliente("Periodo", mes);
        LlenarListaCliente("Aforador", lector);
        LlenarListaCliente("Cuenta", Cuenta);

        logfile = new File(directorioActual + VariablesGlobales.CarpetaLecturas+"/LOGEVENTOS.LOG"); //Ax: para escribir log de eventos

        imgbtnaforos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialogEscogerLista(Op_Aforos, "Aforo", "aforos");
            }
        });

        imgbtnaforostipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialogEscogerLista(Op_Aforostipo, "Aforo Tipo", "aforostipo");
            }
        });

        btnGrabarAforo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GrabarAforo();
            }
        });

        btnReturnaforos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }//End Oncreate

    @Override
    public void onBackPressed() {
        finish();
    }

    private void Aforos_Load() {

        Limpia_textos();
        LlenarCombos();
        Llenar_AforoCabecera("Codigo", "Cantidad", "Vatios", "Tipo conexion");
        BuscarAforo();
    }

    private void BuscarAforo() {
        //combos primera pagina
        try {
            File aforos = new File(directorioActual + VariablesGlobales.CarpetaLecturas+"/AFOROS.SDA");

            if (!aforos.exists()) {
                aforos.createNewFile();
            }

            tabla.removeAllViews();
            tabla.refreshDrawableState();

            if (mi_Aforo.abrir_Aforos(aforos.getAbsolutePath())) {
                lbltotal.setText("0");
                for (int i = 1; i <= mi_Aforo.total_Aforos; i++) {
                    mi_Aforo.lectura_Aforos(i);

                    if (Cuenta.equals(mi_Aforo.getaforos_CODIGOCUENTA()) && mi_Aforo.getaforos_NUMEROACTA().trim().equals(documento.trim())) {
                        Llenar_Aforo(true, mi_Aforo.getaforos_CODIGOAFORO(), mi_Aforo.getaforos_CANTIDAD(), mi_Aforo.getaforos_W(), mi_Aforo.getaforos_TIPOCONEXION());
                    }
                }
                mi_Aforo.Cerrar_Aforos();
            } else {
                mensajes("No Hay Archivo " + "AFOROS.SDA" + " Cargue Datos a Trabajar!!");
                return;
            }

        } catch (Exception ex) {
            mensajes("Problemas archivo AFOROS DE SALIDA \n" + ex.getMessage());
            utils.Log(logfile, "[ModuloActualizaAforos] Aforos() ; " + ex.getMessage());
            finish();
        }

    }

    private void LlenarCombos() {

        String temp = "";
        String who = "";

        try {
            who = "Aforos";
            String p1;
            String p2;

            if (miAforos.abrir_ItemAforos(directorioActual + VariablesGlobales.CarpetaLecturas+"/AFOROS.TXT")) {

                for (int i = 1; i <= miAforos.total_ItemAforos; i++) {
                    miAforos.lectura_ItemAforos(i);

                    p1 = miAforos.getItemAforos_DESCREIPCION().substring(0, 34).trim();
                    p2 = miAforos.getItemAforos_DESCREIPCION().substring(34, 40).trim();

                    if (!temp.equals("")) {
                        temp = temp + "]" + miAforos.getItemAforos_CODIGOAFORO() + " | " + p1 + " | " + p2;
                    } else {
                        temp = temp + miAforos.getItemAforos_CODIGOAFORO() + " | " + p1 + " | " + p2;
                    }
                }
                miAforos.Cerrar_ItemAforos();
            } else {
                mensajes("No Hay Archivo Aforo.txt Cargue Datos");
                return;
            }
            Op_Aforos = temp.split("]");
            temp = "";
            who = "Aforostipo";
            //llenar combo de aforos de la tabla determinada  //llenar Tipo Coneccion

            temp = "R - Registrada" + ";D - Directa";
            Op_Aforostipo = temp.split(";");

        } catch (Exception ex) {
            mensajes("Error llenar Combos " + who + "\n");
            utils.Log(logfile, "[ModuloActualizaAforos] Aforos() ; " + ex.getMessage());
            finish();
        }
    }

    public void AlertDialogEscogerLista(final String[] msgs, String titulo, final String llamado) {

        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AlertDialogTheme); //Ax: el alert dialog toma el 'estilo' (tamaño de letra) desde Styles xml
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle(titulo);

        builder.setSingleChoiceItems(msgs, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                try {
                    if (msgs.length < 1) return;

                    switch (llamado) {
                        case "aforos":

                            descripcionaforo = "";
                            String[] spliteo = Op_Aforos[item].split("\\|");
                            txtCodigoAforo.setText(spliteo[0].trim());
                            txtVatiosAforo.setText(spliteo[2].trim());
                            descripcionaforo = spliteo[1].trim();
                            txtCmbAforos.setText(descripcionaforo);
                            btnGrabarAforo.setEnabled(true);

                            break;
                        case "aforostipo":
                            txtTipoAforo.setText(Op_Aforostipo[item].substring(0, 1));
                            txtCmbTipoAforo.setText(Op_Aforostipo[item]);
                            break;
                    }
                } catch (Exception ex) {
                    mensajes("PROBLEMAS EN SELECCION DE" + llamado);
                }
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.setCancelable(false);
        levelDialog.setCanceledOnTouchOutside(false);
        levelDialog.show();
    }

    public void LlenarListaCliente(String titulo, String dato) {

        TextView txttitulo = new TextView(this);
        TextView txtdato = new TextView(this);
        txttitulo.setTextSize(16);
        txtdato.setTextSize(16);
        txttitulo.setBackgroundResource(R.drawable.bg_bordecomun);
        txtdato.setBackgroundResource(R.drawable.bg_bordecomun);
        txttitulo.setPadding(2, 2, 2, 2);
        txtdato.setPadding(2, 2, 2, 2);
        txtdato.setGravity(Gravity.LEFT);
        txttitulo.setGravity(Gravity.LEFT);

        txttitulo.setText(titulo);
        txtdato.setText(dato);

        fila = new TableRow(this);
        fila.setBackgroundColor(resource.getColor(R.color.grayBlueSoft));

        fila.addView(txttitulo);
        fila.addView(txtdato);
        tablacliente.addView(fila);
    }

    public void Llenar_Aforo(boolean w, String codigo, String cantidad, String vatios, String tipoconx) {

        TextView txtcodigo = new TextView(this);
        TextView txtcantidad = new TextView(this);
        TextView txtvatios = new TextView(this);
        TextView txtipoconx = new TextView(this);
        txtcodigo.setTextSize(16);
        txtcantidad.setTextSize(16);
        txtvatios.setTextSize(16);
        txtipoconx.setTextSize(16);
        txtcodigo.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcantidad.setBackgroundResource(R.drawable.bg_bordecomun);
        txtvatios.setBackgroundResource(R.drawable.bg_bordecomun);
        txtipoconx.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcodigo.setPadding(2, 2, 2, 2);
        txtcantidad.setPadding(2, 2, 2, 2);
        txtvatios.setPadding(2, 2, 2, 2);
        txtipoconx.setPadding(2, 2, 2, 2);
        txtcantidad.setGravity(Gravity.LEFT);
        txtcodigo.setGravity(Gravity.LEFT);
        txtvatios.setGravity(Gravity.LEFT);
        txtipoconx.setGravity(Gravity.LEFT);

        txtcodigo.setText(codigo);
        txtcantidad.setText(cantidad);
        txtvatios.setText(vatios);
        txtipoconx.setText(tipoconx);

        txtcodigo.setWidth(90);
        txtcantidad.setWidth(110);
        txtvatios.setWidth(100);
        txtipoconx.setWidth(170);

        fila = new TableRow(this);
        fila.setBackgroundColor(resource.getColor(R.color.grayBlueSoft));

        fila.addView(txtcodigo);
        fila.addView(txtcantidad);
        fila.addView(txtvatios);
        fila.addView(txtipoconx);
        tabla.addView(fila);

        if (w) {
            int totalW = utils.parseStringToInteger(lbltotal.getText().toString()) + (utils.parseStringToInteger(mi_Aforo.getaforos_W()) * utils.parseStringToInteger(mi_Aforo.getaforos_CANTIDAD()));
            lbltotal.setText(totalW + "");
        }
    }

    public void Llenar_AforoCabecera(String codigo, String cantidad, String vatios, String tipoconx) {

        TextView txtcodigo = new TextView(this);
        TextView txtcantidad = new TextView(this);
        TextView txtvatios = new TextView(this);
        TextView txtipoconx = new TextView(this);
        txtcodigo.setTextSize(16);
        txtcantidad.setTextSize(16);
        txtvatios.setTextSize(16);
        txtipoconx.setTextSize(16);
        txtcodigo.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcantidad.setBackgroundResource(R.drawable.bg_bordecomun);
        txtvatios.setBackgroundResource(R.drawable.bg_bordecomun);
        txtipoconx.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcodigo.setPadding(2, 2, 2, 2);
        txtcantidad.setPadding(2, 2, 2, 2);
        txtvatios.setPadding(2, 2, 2, 2);
        txtipoconx.setPadding(2, 2, 2, 2);
        txtcantidad.setGravity(Gravity.CENTER);
        txtcodigo.setGravity(Gravity.CENTER);
        txtvatios.setGravity(Gravity.CENTER);
        txtipoconx.setGravity(Gravity.CENTER);

        txtcodigo.setText(codigo);
        txtcantidad.setText(cantidad);
        txtvatios.setText(vatios);
        txtipoconx.setText(tipoconx);

        txtcodigo.setWidth(90);
        txtcantidad.setWidth(110);
        txtvatios.setWidth(100);
        txtipoconx.setWidth(170);

        fila = new TableRow(this);
        fila.setBackgroundColor(resource.getColor(R.color.grayBlue));

        fila.addView(txtcodigo);
        fila.addView(txtcantidad);
        fila.addView(txtvatios);
        fila.addView(txtipoconx);
        tablacabecera.addView(fila);
    }

    private void Reemplazar_textos() {
        String descripcionaforo_Envia= descripcionaforo;
        descripcionaforo_Envia=   String.format("%1$-39s",descripcionaforo_Envia );
        descripcionaforo_Envia = descripcionaforo_Envia + "X";//Ax: Annade  'X' a lo ultimo de la direccion para despues usarlo y saber si fue enviado o no

        mi_Aforo.setaforos_numeroacta(documento);
        mi_Aforo.setaforos_codigoaforo(txtCodigoAforo.getText().toString());
        mi_Aforo.setaforos_cantidad(txtCantidaEquipos.getText().toString());
        mi_Aforo.setaforos_w(txtVatiosAforo.getText().toString());
        mi_Aforo.setaforos_tipoconexion(txtTipoAforo.getText().toString());
        mi_Aforo.setaforos_lector(lector);
        mi_Aforo.setaforos_codigocuenta(Cuenta);
        mi_Aforo.setaforos_ciclo(ciclo);
        mi_Aforo.setaforos_mes(mes);
        mi_Aforo.setaforos_anno(anno);
        mi_Aforo.setaforos_descripcionaforo(descripcionaforo_Envia);
        mi_Aforo.setaforos_CRNL("\r\n");
    }

    private void GrabarAforo() {

        try {
            if (sino_grabaaforo == 0) {

                if (!Validacion()) {
                    mensajes("Faldan datos por llenar... Verifiquelo");
                    return;
                }

                int encontro = 0;

                if (mi_Aforo.abrir_Aforos(directorioActual + VariablesGlobales.CarpetaLecturas+"/AFOROS.SDA")) {
                    for (int i = 1; i <= mi_Aforo.total_Aforos; i++) {
                        mi_Aforo.lectura_Aforos(i);

                        if (Cuenta.trim().equals(mi_Aforo.getaforos_CODIGOCUENTA().trim()) && txtCodigoAforo.getText().toString().trim().equals(mi_Aforo.getaforos_CODIGOAFORO().trim()) && utils.parseStringToInteger(txtVatiosAforo.getText().toString()) == utils.parseStringToInteger(mi_Aforo.getaforos_W()) && txtTipoAforo.getText().toString().trim().equals(mi_Aforo.getaforos_TIPOCONEXION().trim()) && mi_Aforo.getaforos_NUMEROACTA().trim().equals(documento.trim())) {
                            mi_Aforo.ultimo_Aforos = i;
                            encontro = 1;
                            i = mi_Aforo.total_Aforos + 1;
                        }
                    }

                    Reemplazar_textos();

                    if (encontro == 0) {
                        mi_Aforo.ultimo_Aforos = mi_Aforo.total_Aforos + 1;
                    } else {
                        AlertDialogEscogerSiNo("Opciones de Captura", "YA esta Registrado el Aforo\n" + "Desea Borrarlo: [Yes]\n [No]Modifica Registro \no Cancela proceso?", "grabaraforo");
                        return;
                    }
                } else {
                    mensajes("No Hay Archivo " + "AFOROS.SDA" + " Cargue Datos a Trabajar!!");
                    return;
                }

            } else {
                if (sino_grabaaforo == 1) {
                    mi_Aforo.lectura_Aforos(mi_Aforo.ultimo_Aforos);
                    mi_Aforo.posicion_Aforos(mi_Aforo.ultimo_Aforos, mi_Aforo.LONGITUD_REGISTRO);
                    mi_Aforo.setaforos_numeroacta("XXXXXXXXXXXXXXX");

                } else {
                    if (sino_grabaaforo == -1) {
                        mi_Aforo.Cerrar_Aforos();
                        return;
                    }
                }
            }
            mi_Aforo.posicion_Aforos(mi_Aforo.ultimo_Aforos, mi_Aforo.LONGITUD_REGISTRO);
            mi_Aforo.escribir_Aforos(mi_Aforo.ultimo_Aforos);

            mi_Aforo.Cerrar_Aforos();
            BuscarAforo();

        } catch (Exception ex) {
            mensajes("Error grabando AFOROS \n" + ex.getMessage());
            utils.Log(logfile, "[ModuloActualizaAforos] GrabarAforo() ; " + ex.getMessage());
            finish();
        }

        Limpia_textos();
        descripcionaforo = "";
        btnGrabarAforo.setEnabled(false);
    }

    private boolean Validacion() {
        if (txtCodigoAforo.getText().toString().trim().equals("") || txtCantidaEquipos.getText().toString().trim().equals(""))
            return false;

        if (txtVatiosAforo.getText().toString().trim().equals("") || txtTipoAforo.getText().toString().trim().equals(""))
            return false;

        if (utils.parseStringToInteger(txtCantidaEquipos.getText().toString().trim()) < 1)
            return false;

        return (true);
    }

    private void Limpia_textos() {
        txtCodigoAforo.setText("");
        txtCantidaEquipos.setText("1");
        txtVatiosAforo.setText("");
        txtTipoAforo.setText("R");
        txtCmbAforos.setText("");
        txtCmbTipoAforo.setText("");
    }

    public void AlertDialogEscogerSiNo(String titulo, String mensaje, final String NombreMetodo) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ModuloActualizaAforos.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) {
                    case "grabaraforo":
                        sino_grabaaforo = 1;//si
                        GrabarAforo();
                        sino_grabaaforo = 0;//neu
                        break;
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) {
                    case "grabaraforo":
                        sino_grabaaforo = -1;//no
                        GrabarAforo();
                        sino_grabaaforo = 0;//neu
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(ModuloActualizaAforos.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 50);
        toast.show();
    }
}