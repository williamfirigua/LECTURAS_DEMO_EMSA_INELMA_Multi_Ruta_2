package com.gstolima.modulobusquedacuenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.TablaRegistroSalida;

import java.io.File;

public class ModuloBusquedaCuenta extends AppCompatActivity {

    public View.OnClickListener TablaListener; //Ax: escucha para capturar clic en rows de la tabla generada
    public int ClickedRow;//Ax: guarda el ID (cliente) al dar clic a una fila.
    final static String ACTIVITY_INFO = "com.gstolima.accesoyseguridad.MenuDeLiquidacion";

    //    TablaEntradaClientes infoClienteEntrada;
//    TablaClienteSalida infoClienteSalida;
//    TablaMedidorEntrada infoMedidorEntrada;
    TablaRegistroSalida infoRegistroSalida;

    RadioButton rbContador;
    RadioButton rbCuenta;
    RadioButton rbRuta;
    RadioButton rbDireccion;
    RadioButton rbNombre;

    RadioGroup rgBotones;

    TextView txtBuscar;
    Utils utils = new Utils();
    File logfile;
    TextView txtTipoBusqueda;
    Resources rs;

    // Lista datos
    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutCampo;
    TableRow.LayoutParams layoutValor;
    TableRow.LayoutParams layoutFila;
    TableRow fila;

    String NombreArchivos_3;

    int txtMaxLength = 16; //Ax: manejaran el largo de la entrada de texto respecto a cada radiobutton
    int txtMinLength = 3;
    int limitador = 0; //Ax: limita el numero de rows creadas a 6
    int opcion = 0; //Ax: define que busqueda se realiza y se la envia a liquidacion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_busqueda_cuenta);

        rs = this.getResources();
        Bundle bundle = getIntent().getExtras();

//        infoClienteEntrada = new TablaEntradaClientes();
//        infoClienteSalida = new TablaClienteSalida();
//        infoMedidorEntrada = new TablaMedidorEntrada();
        infoRegistroSalida = new TablaRegistroSalida();

        NombreArchivos_3 = bundle.getString("NombreArchivos");

        rbContador = (RadioButton) findViewById(R.id.rbContador);
        rbCuenta = (RadioButton) findViewById(R.id.rbCuenta);
        rbRuta = (RadioButton) findViewById(R.id.rbRuta);
        rbDireccion = (RadioButton) findViewById(R.id.rbDireccion);
        rbNombre = (RadioButton) findViewById(R.id.rbNombre);
        rgBotones = (RadioGroup) findViewById(R.id.rgBotones);

        txtBuscar = (TextView) findViewById(R.id.txtBuscar);
        txtTipoBusqueda = (TextView) findViewById(R.id.txtTipoBusqueda);
        tabla = (TableLayout) findViewById(R.id.tablaResumenDatos);
        cabecera = (TableLayout) findViewById(R.id.cabeceraDatos);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutCampo = new TableRow.LayoutParams(300, TableRow.LayoutParams.WRAP_CONTENT);
        layoutValor = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT);

        txtBuscar.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String a = "";

                if (txtBuscar.getText().length() < txtMinLength) {
                    Toast.makeText(getApplicationContext(), "Rango de Caracteres de " + txtMinLength + " a " + txtMaxLength, Toast.LENGTH_LONG).show();
                    return false;
                }

                if (actionId == 6)//Ax "ENTER" telefono chino ax = 5 otro = 6
                {
                    if (txtBuscar.getText().length() == txtMaxLength && opcion == 2) {
                        volveraLiquidacion(); //Cuando el numeo es de 9 digitos se busca por binario en liquidacion
                    }
                    limitador = 0;
                    borrarListaInformacionCliente();

                    if (txtBuscar.getText().toString().trim().length() > 0) {
                        //llenar las variables para buscar la cuenta
                        //Variables.DatoDeBusqueda = txtBuscar.Text.Trim();
                        VariablesGlobales.datodebusqueda = "0";
                        txtBuscar.setText(txtBuscar.getText().toString().trim().toUpperCase());
                        buscarIdCuenta(txtBuscar.getText().toString().trim());
                        if (tabla.getChildCount() > 0) {
                            tabla.requestFocus();
                            //ListaResumen.Items[0].Selected = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Parametro de busqueda no Existe: " + txtBuscar.getText(), Toast.LENGTH_LONG).show();
                            //txtBuscar.setText("");
                            txtBuscar.requestFocus();
                        }
                    } else {
                        VariablesGlobales.datodebusqueda = "0";
                        finish();
                    }
                }
                return false;
            }
        });

        txtBuscar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtBuscar.getText().length() > txtMaxLength) {
                    txtBuscar.setText(txtBuscar.getText().toString().substring(0, txtMaxLength));
                    EditText etext = (EditText) findViewById(R.id.txtBuscar);//Ax: Coloca el cursor al final
                    etext.setSelection(etext.getText().length());
                }
            }
        });

        rgBotones.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if () {
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                if (checkedId == rbContador.getId()) {
                    txtBuscar.setRawInputType(InputType.TYPE_CLASS_NUMBER);//Ax. convertir teclado numerico
                    txtMaxLength = 16;
                    txtMinLength = 3;
                    txtTipoBusqueda.setText("Buscando por: Medidor");
                    opcion = 1;
                    //"Contador";
                }
                if (checkedId == rbCuenta.getId()) {
                    txtBuscar.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    txtMaxLength = 10;
                    txtMinLength = 3;
                    txtTipoBusqueda.setText("Buscando por: Cuenta");
                    opcion = 2;
                    // "Cuenta";
                }
                if (checkedId == rbRuta.getId()) {
                    txtBuscar.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    txtMaxLength = 13;
                    txtMinLength = 4;
                    txtTipoBusqueda.setText("Buscando por: Ruta");
                    opcion = 3;
                    //"Ruta";
                }
                if (checkedId == rbDireccion.getId()) {
                    txtBuscar.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    txtMaxLength = 63;
                    txtMinLength = 8;
                    txtTipoBusqueda.setText("Buscando por: Direccion");
                    opcion = 4;
                    //"Direccion";
                }
                if (checkedId == rbNombre.getId()) {
                    txtBuscar.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    txtMaxLength = 48;
                    txtMinLength = 3;
                    txtTipoBusqueda.setText("Buscando por: Nombre");
                    opcion = 5;
                    //"Nombre";
                }

                txtBuscar.setText("");
                txtBuscar.requestFocus();
                //Toast.makeText(getApplicationContext(), "El RadioButton " + nombre + " fue seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

        TablaListener = new View.OnClickListener() {

            public void onClick(View v) {
                v.setBackgroundColor(Color.rgb(255, 255, 153));
                int ClickedRow_old = ClickedRow;
                ClickedRow = v.getId();
                if (ClickedRow == ClickedRow_old) {//Comprobar doble clic no seguido
                    if (opcion == 2)
                        opcion = 6; //Ax: ya lo encontro aqui por busqueda normal, se cambia opcion para no hacer de nuevo la busqueda alla (binaria)
                    volveraLiquidacion();
                }
            }
        };

        logfile = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG"); //Ax: para logs de error
        moduloBusquedaCuenta_Load();
        rbCuenta.setChecked(true);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void abrirArchivosDeFacturacion() {
        try {
            infoRegistroSalida.setArchivo_TablaRegistroSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + NombreArchivos_3);
//            infoMedidorEntrada.setArchivo_TablaMedidorEntrada(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/MEDIDOR.TXT");
//            infoClienteEntrada.setArchivo_TablaEntradaClientes(VariablesGlobales.directorioactual + "VariablesGlobales.getCarpetaLecturas()+"/CLIENTE.TXT");
//            infoClienteSalida.setArchivo_TablaClienteSalida(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/CLIENTE.SDA");

            infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());
//            infoClienteEntrada.abrir_TablaEntradaClientes(infoClienteEntrada.getArchivo_TablaEntradaClientes());
//            infoMedidorEntrada.abrir_TablaMedidorEntrada(infoMedidorEntrada.getArchivo_TablaMedidorEntrada());
//            infoClienteSalida.abrir_TablaClienteSalida(infoClienteSalida.getArchivo_TablaClienteSalida());
        } catch (Exception e) {
            utils.Log(logfile, "[ModuloBusquedaCuenta] Error en abrirArchivosDeFacturacion()" + e.getMessage());
            Toast.makeText(getApplicationContext(), "Problema Abriendo\nArchivos de Facturacion", Toast.LENGTH_LONG).show();
        }
    }

    private void cerraArchivosDeFacturacion() {
        try {
            infoRegistroSalida.Cerrar_TablaRegistroSalida();
//            infoClienteEntrada.Cerrar_TablaEntradaClientes();
//            infoMedidorEntrada.Cerrar_TablaMedidorEntrada();
//            infoClienteSalida.Cerrar_TablaClienteSalida();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Problema Cerrando\nArchivos de Facturacion", Toast.LENGTH_LONG).show();
        }
    }

    private void buscarIdCuenta(String idBuscar) {

        abrirArchivosDeFacturacion();

        if (rbContador.isChecked() || (rbCuenta.isChecked())) {

            buscarIdMedidor(idBuscar);//buscar en medidor
        } else {
            buscarIdCliente(idBuscar);//buscar en cliente
        }
        cerraArchivosDeFacturacion();
    }

    public void buscarIdMedidor(String codigo) {

        int tam = infoRegistroSalida.getTotal_TablaRegistroSalida();
        infoRegistroSalida.inicializaBloque();
        codigo = codigo.trim();

        for (int i = 1; i <= (tam); i++) {
            iGlobal = i; //para llevarse este i y llenar el id para dar clic
            infoRegistroSalida.lectura_TablaRegistroSalida_VI(i);//   lectura_TablaRegistroSalida
            if (rbContador.isChecked()) {

                if (infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().indexOf(codigo) >= 0) {//infoMedidorEntrada.gettablaMedidorEntrada_NUMERO()
                    if (limitador == 6) return;
                    llenarListaResumen();
                }
            } else {
                if (infoRegistroSalida.gettablaRegistroSalida_CUENTA().indexOf(codigo) >= 0) {//infoMedidorEntrada.gettablaMedidorEntrada_CUENTA()
                    if (limitador == 6) return;
                    llenarListaResumen();
                }
            }
        }
        infoRegistroSalida.terminaBloque();
        return;
    }

    public int iGlobal = 0;

    public void buscarIdCliente(String codigo) {

        int tam = infoRegistroSalida.getTotal_TablaRegistroSalida();
        infoRegistroSalida.inicializaBloque();
        codigo = codigo.trim().toUpperCase();

        for (int i = 1; i <= tam; i++) {//infoClienteEntrada.getTotal_TablaEntradaClientes()
            iGlobal = i; //para llevarse este i y llenar el id para dar clic
            infoRegistroSalida.lectura_TablaRegistroSalida_V(i);
            if (rbRuta.isChecked()) {

                String x= infoRegistroSalida.gettablaRegistroSalida_RUTA().toUpperCase();
                if (x.indexOf(codigo) >= 0) {//infoClienteEntrada.gettablaEntradaClientes_RUTA()
                    //leer cliente
                    // infoMedidorEntrada.lectura_TablaMedidorEntrada(Integer.parseInt(infoClienteEntrada.gettablaEntradaClientes_PRIMERMEDIDOR()));
                    //llenar la lista;
                    if (limitador == 6) return;
                    llenarListaResumen();
                }
            } else {
                if (rbDireccion.isChecked()) {
                    if (infoRegistroSalida.gettablaRegistroSalida_DIRECCION().toUpperCase().indexOf(codigo) >= 0) {//infoClienteEntrada.gettablaEntradaClientes_DIRECCION()
                        //leer cliente
                        //  infoMedidorEntrada.lectura_TablaMedidorEntrada(Integer.parseInt(infoClienteEntrada.gettablaEntradaClientes_PRIMERMEDIDOR()));
                        //llenar la lista;
                        if (limitador == 6) return;
                        llenarListaResumen();
                    }
                } else {//Por Nombre
                    if (infoRegistroSalida.gettablaRegistroSalida_NOMBRE().toUpperCase().indexOf(codigo) >= 0) {//infoClienteEntrada.gettablaEntradaClientes_NOMBRE()
                        //leer cliente
                        //  infoMedidorEntrada.lectura_TablaMedidorEntrada(Integer.parseInt(infoClienteEntrada.gettablaEntradaClientes_PRIMERMEDIDOR()));
                        //llenar la lista;
                        if (limitador == 6) return;
                        llenarListaResumen();
                    }
                }
            }
        }
        infoRegistroSalida.terminaBloque();
        return;
    }

    private void llenarListaResumen() {

        limitador++;

        String campo;
        String valor;
        if (rbContador.isChecked()) {

            campo = infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR();//  infoMedidorEntrada.gettablaMedidorEntrada_NUMERO();
        } else {

            if (rbCuenta.isChecked()) {

                campo = infoRegistroSalida.gettablaRegistroSalida_CUENTA();// infoMedidorEntrada.gettablaMedidorEntrada_CUENTA();
            } else {

                if (rbRuta.isChecked()) {

                    campo = infoRegistroSalida.gettablaRegistroSalida_RUTA();//infoClienteEntrada.gettablaEntradaClientes_RUTA();
                } else {

                    if (rbDireccion.isChecked()) {

                        campo = infoRegistroSalida.gettablaRegistroSalida_DIRECCION();//infoClienteEntrada.gettablaEntradaClientes_DIRECCION();
                    } else {

                        campo = infoRegistroSalida.gettablaRegistroSalida_NOMBRE();// infoClienteEntrada.gettablaEntradaClientes_NOMBRE();
                    }
                }
            }
        }

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        if (rbDireccion.isChecked())
            try {
                if (Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().trim()) == 0) {
                    valor = "Activo";
                } else {
                    valor = "Suspendido";
                }
            } catch (Exception ex) {
                valor = infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO();// infoMedidorEntrada.gettablaMedidorEntrada_IDCORTADO();
            }
        else
            valor = infoRegistroSalida.gettablaRegistroSalida_DIRECCION();//infoClienteEntrada.gettablaEntradaClientes_DIRECCION();

        //valor += " - " + infoMedidorEntrada.gettablaMedidorEntrada_CLIENTE().trim();

        insertarFilaListaResumen(campo, valor);
    }

    private void insertarFilaListaResumen(String campo, String valor) {

        TextView txtCampo;
        TextView txtValor;

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        fila.setClickable(true);
        fila.setOnClickListener(TablaListener);

        //   if (opcion != 1) {
        fila.setId(iGlobal); //Ax: colocarle la posicion del cliente //Nuevo //Integer.parseInt(infoMedidorEntrada.gettablaMedidorEntrada_CLIENTE().trim())
        //  }
//        else {
//            fila.setId(Integer.parseInt(infoMedidorEntrada.gettablaMedidorEntrada_PRIMERREGISTRO().trim()));
//        }

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
        tabla.addView(fila);
    }

    public void volveraLiquidacion() {

        Intent iBackActivity = new Intent(this, com.gstolima.accesoyseguridad.MenuDeLiquidacion.class);
        if (txtBuscar.getText().length() == txtMaxLength && opcion == 2) {
            iBackActivity.putExtra("idcliente", txtBuscar.getText().toString());
        } else {
            iBackActivity.putExtra("idcliente", (ClickedRow + ""));
        }
        iBackActivity.putExtra("opcion", (opcion + ""));
        setResult(Activity.RESULT_OK, iBackActivity);
        finish();
    }

    private void borrarListaInformacionCliente() {
        tabla.removeAllViews();
    }

    private void moduloBusquedaCuenta_Load() {
        borrarListaInformacionCliente();
        txtBuscar.setText("");
        txtBuscar.requestFocus();
    }
}
