package com.gstolima.modulobusquedacuenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.gstolima.tablas.IndiceBusquedaRuta;
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

    // --- Busqueda en vivo sobre indice en memoria (el archivo se lee una sola vez) ---
    private static final int MAX_RESULTADOS = 25;
    private static final int MIN_CARACTERES = 3;
    private static final long ESPERA_TECLEO_MS = 150; // filtra cuando el operario deja de escribir
    private IndiceBusquedaRuta indice;
    private final Handler handlerBusqueda = new Handler(Looper.getMainLooper());
    private final Runnable tareaFiltrar = this::filtrarEnVivo;
    private String etiquetaTipoBusqueda = "";

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
                        // Mismo resultado que buscarIdCuenta(), pero sobre el indice en memoria.
                        filtrarEnVivo();
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

                programarFiltrado();
            }
        });

        rgBotones.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == rbContador.getId()) {
                    cambiarTipoEntrada(InputType.TYPE_CLASS_NUMBER, 16, 3, "Buscando por: Medidor", 1);
                } else if (checkedId == rbCuenta.getId()) {
                    cambiarTipoEntrada(InputType.TYPE_CLASS_NUMBER, 10, 3, "Buscando por: Cuenta", 2);
                } else if (checkedId == rbRuta.getId()) {
                    cambiarTipoEntrada(InputType.TYPE_CLASS_NUMBER, 13, 3, "Buscando por: Ruta", 3);
                } else if (checkedId == rbDireccion.getId()) {
                    cambiarTipoEntrada(InputType.TYPE_CLASS_TEXT, 63, 3, "Buscando por: Direccion", 4);
                } else if (checkedId == rbNombre.getId()) {
                    cambiarTipoEntrada(InputType.TYPE_CLASS_TEXT, 48, 3, "Buscando por: Nombre", 5);
                }
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
        rbContador.setChecked(true); //Ax: la busqueda arranca por Medidor
        cargarIndiceEnSegundoPlano();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Cambia el tipo de teclado del EditText y fuerza al IME a refrescarlo.
     * Desde Android 13+ (y de forma mas estricta en Android 15), setRawInputType()
     * actualiza el inputType internamente pero el teclado ya visible NO se refresca
     * solo: hay que ocultar, cambiar el tipo, y llamar a restartInput() para que el
     * InputMethodManager vuelva a leer el EditorInfo antes de reabrir el teclado.
     */
    private void cambiarTipoEntrada(int inputType, int maxLength, int minLength, String etiqueta, int nuevaOpcion) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);
        }

        txtBuscar.setText("");
        txtBuscar.setRawInputType(inputType);
        txtMaxLength = maxLength;
        txtMinLength = minLength;
        etiquetaTipoBusqueda = etiqueta;
        txtTipoBusqueda.setText(etiqueta);
        opcion = nuevaOpcion;
        handlerBusqueda.removeCallbacks(tareaFiltrar);
        borrarListaInformacionCliente();

        txtBuscar.requestFocus();
        if (imm != null) {
            imm.restartInput(txtBuscar); // clave: obliga al IME a releer el inputType actualizado
            imm.showSoftInput(txtBuscar, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Oculta el teclado usando la vista realmente enfocada. Se apoya en getCurrentFocus()
     * en vez de asumir siempre txtBuscar, por si el foco cambio (p.ej. hacia la tabla de resultados).
     */
    private void ocultarTeclado() {
        View vistaEnfocada = getCurrentFocus();
        if (vistaEnfocada == null) {
            vistaEnfocada = txtBuscar;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(vistaEnfocada.getWindowToken(), 0);
        }
    }

    /**
     * Punto unico de salida de la Activity. Oculta el teclado ANTES de finalizar,
     * sin importar cual de los 3 flujos de salida lo dispara (onBackPressed, ENTER con
     * campo vacio, o volveraLiquidacion). Asi la actividad de lecturas que recibe el
     * foco despues arranca sin el teclado numerico/alfa heredado.
     */
    @Override
    public void finish() {
        handlerBusqueda.removeCallbacks(tareaFiltrar);
        ocultarTeclado();
        super.finish();
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

    /**
     * El indice se arma leyendo el archivo de la ruta una sola vez; con 2000 registros toma
     * entre 30 y 60 ms. Se hace fuera del hilo principal y, si el operario ya escribio algo,
     * se filtra apenas queda listo.
     */
    private void cargarIndiceEnSegundoPlano() {
        final String archivoRuta = VariablesGlobales.directorioactual
                + VariablesGlobales.getCarpetaLecturas() + "/" + "D" + NombreArchivos_3;

        new Thread(() -> {
            try {
                final IndiceBusquedaRuta cargado = IndiceBusquedaRuta.obtener(archivoRuta);
                runOnUiThread(() -> {
                    indice = cargado;
                    if (!isFinishing()) {
                        filtrarEnVivo();
                    }
                });
            } catch (Exception e) {
                utils.Log(logfile, "[ModuloBusquedaCuenta] No se pudo construir el indice: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "No se pudo preparar la busqueda", Toast.LENGTH_LONG).show());
            }
        }, "indice-busqueda").start();
    }

    /** Agrupa las teclas seguidas en un solo filtrado. */
    private void programarFiltrado() {
        handlerBusqueda.removeCallbacks(tareaFiltrar);
        handlerBusqueda.postDelayed(tareaFiltrar, ESPERA_TECLEO_MS);
    }

    /**
     * Filtra en memoria y repinta la tabla. Con 2000 registros toma cerca de 1 ms, por eso
     * corre en el hilo principal sin afectar la escritura.
     */
    private void filtrarEnVivo() {
        String consulta = txtBuscar.getText().toString().trim();

        borrarListaInformacionCliente();
        limitador = 0;
        txtTipoBusqueda.setText(etiquetaTipoBusqueda);

        if (indice == null || consulta.length() < MIN_CARACTERES) {
            return; // indice aun cargando, o faltan caracteres
        }

        int[] encontrados = indice.filtrar(consulta, opcion, MAX_RESULTADOS);
        for (int registro : encontrados) {
            iGlobal = registro;
            limitador++;
            insertarFilaListaResumen(campoDeResultado(registro), valorDeResultado(registro));
        }

        int totalCoincidencias = indice.totalCoincidencias();
        if (totalCoincidencias > 0) {
            txtTipoBusqueda.setText(etiquetaTipoBusqueda + "  (" + encontrados.length + " de " + totalCoincidencias + ")");
        } else {
            txtTipoBusqueda.setText(etiquetaTipoBusqueda + "  (sin coincidencias)");
        }
    }

    /** Primera columna: el campo por el que se busco, igual que en llenarListaResumen(). */
    private String campoDeResultado(int registro) {
        if (rbContador.isChecked()) return indice.getMedidor(registro);
        if (rbCuenta.isChecked()) return indice.getCuenta(registro);
        if (rbRuta.isChecked()) return indice.getRuta(registro);
        if (rbDireccion.isChecked()) return indice.getDireccion(registro);
        return indice.getNombre(registro);
    }

    /** Segunda columna: estado si se busca por direccion, si no la direccion. */
    private String valorDeResultado(int registro) {
        if (rbDireccion.isChecked()) {
            try {
                return Integer.parseInt(indice.getSuspendido(registro).trim()) == 0 ? "Activo" : "Suspendido";
            } catch (Exception ex) {
                return indice.getSuspendido(registro);
            }
        }
        return indice.getDireccion(registro);
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