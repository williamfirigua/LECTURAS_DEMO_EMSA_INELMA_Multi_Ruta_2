package com.gstolima.accesoyseguridad;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.Util.Utils;
import com.gstolima.captureException.myExceptionHandler;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.tablas.EnvioGPS;
import com.gstolima.tablas.TablaRegistroSalida;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.Util.WSSoap;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Typeface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gstolima.comunicaciones.CrudEnvioLectura;
import com.gstolima.comunicaciones.CrudEnvioFoto;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;

public class ResumenEstadistico extends Activity {
    String URL = "";
    String paginaWs = "";
    CrudComunicaciones crudComuni;
    int ciclo = 0;
    int Anio = 0;
    int Mes = 0;
    int CheckRadio = 0;
    ListView list;
    String directorio;
    String aforador;
    String terminal;
    String archivoCargado;
    String nombrePredio;
    String ultimaRutaSeleccionada = "";
    String ResumenConteo = "";
    ScrollView scroll;
    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();

    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutRuta;
    TableRow.LayoutParams layoutNroMedidor;
    TableRow.LayoutParams layoutDireccion;
    TableRow.LayoutParams layoutNombre;
    TableRow.LayoutParams layoutLectura;
    TableRow.LayoutParams layoutAnomalia;
    TableRow.LayoutParams layoutComentario;
    TableRow.LayoutParams layoutInforme;
    TableRow.LayoutParams layoutId;

    TableRow fila;
    TextView idLeidos;
    TextView txtRuta;
    TextView txtNroMedidor;
    TextView txtDireccion;
    TextView txtNombre;
    TextView txtLectura;
    TextView txtAnomalia;
    TextView txtComentario;
    TextView txtInforme;
    TextView txtId;
    //asignacion de las nuevas variables para tener valores del resumen estadistico
    RadioGroup GrupoOpciones;
    int UltimoRegistro = 1; //Ax: para llenar la tabla de arriba antes de los conteos y no influir en otras cosas.
    int Ultimo = 0; //Ax: para llenar la tabla de arriba antes de los conteos y no influir en otras cosas.
    TextView txtNombreRuta;
    TextView txtFecha;
    TextView txtEstado;
    TextView txtTotal;
    TextView txtLeidos;
    TextView txtPendientes;
    ImageButton btnAtras1;
    ImageButton btnAdelante1;
    ImageButton btnPrimero1;
    ImageButton btnUltimo1;
    ImageButton btnCerrar;
    String rutaRegistro;

    Resources rs;
    ArrayList<String> item;
    ArrayAdapter<String> adapter;
    Map<Integer, String> map = new HashMap<Integer, String>();//Ax: para guardar el id y el color de los row llenados
    public View.OnClickListener TablaListener; //Ax: escucha para capturar clic en rows de la tabla generada
    public int ClickedRow;//Ax: guarda el ID (cliente) al dar clic a una fila.
    boolean nofromclick = false; //Ax: indica si el regreso proviene de doble clic en tabla o boton
    boolean isError = false; //Ax: indica si el regreso proviene de doble clic en tabla o boton

    public EnvioGPS misenvios = new EnvioGPS();
    File logfile; //Ax: Es para los log de error
    Utils utils = new Utils();//Ax log y utilidades
    int VarGlobregistroactual = 1; //Ax: para llenar la tabla de arriba antes de los conteos y no influir en otras cosas.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_estadistico);
        Thread.setDefaultUncaughtExceptionHandler(new myExceptionHandler(getApplicationContext()));
        rs = this.getResources();
        tabla = (TableLayout) findViewById(R.id.tablaResumen);
        // tabla.setShrinkAllColumns(true);
        cabecera = (TableLayout) findViewById(R.id.cabecera);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutRuta = new TableRow.LayoutParams(300, TableRow.LayoutParams.WRAP_CONTENT);
        layoutNroMedidor = new TableRow.LayoutParams(350, TableRow.LayoutParams.WRAP_CONTENT);
        layoutDireccion = new TableRow.LayoutParams(800, TableRow.LayoutParams.WRAP_CONTENT);
        layoutNombre = new TableRow.LayoutParams(600, TableRow.LayoutParams.WRAP_CONTENT);
        layoutLectura = new TableRow.LayoutParams(300, TableRow.LayoutParams.WRAP_CONTENT);
        layoutAnomalia = new TableRow.LayoutParams(300, TableRow.LayoutParams.WRAP_CONTENT);
        layoutComentario = new TableRow.LayoutParams(400, TableRow.LayoutParams.WRAP_CONTENT);
        layoutInforme = new TableRow.LayoutParams(800, TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT);

        btnAdelante1 = (ImageButton) findViewById(R.id.btnAdelante1);
        btnAtras1 = (ImageButton) findViewById(R.id.btnAtras1);
        btnPrimero1 = (ImageButton) findViewById(R.id.btnPrimero1);
        btnUltimo1 = (ImageButton) findViewById(R.id.btnUltimo1);
        btnCerrar = (ImageButton) findViewById(R.id.btnCerrar);
        scroll = (ScrollView) findViewById(R.id.scroll);
        GrupoOpciones = findViewById(R.id.GrupoOpciones);
        list = (ListView) findViewById(R.id.list);
        txtNombreRuta = (TextView) findViewById(R.id.txtNombreRuta);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtLeidos = (TextView) findViewById(R.id.txtLeidos);
        idLeidos= (TextView) findViewById(R.id.idLeidos);
        txtPendientes = (TextView) findViewById(R.id.txtPendientes);
        //item = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // list.setAdapter(item);
        item = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                item
        );

        list.setAdapter(adapter);





        Bundle bundle = getIntent().getExtras();
        aforador = bundle.getString("aforador");
        terminal = bundle.getString("terminal");
        directorio = bundle.getString("directorioActual");
        nombrePredio = bundle.getString("nombrePredio");
        archivoCargado = nombrePredio;

        logfile = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG");
        getParamsWs();
        if (!nombreArchivo()) return;
        CheckRadio = 1;
        realizarConteo();
        GrupoOpciones.check(R.id.OptTodas);

        GrupoOpciones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.OptTodas:
                        CheckRadio = 1;
                        UltimoRegistro = 1;
                        VarGlobregistroactual = 1;
                        llenarTabla2(VarGlobregistroactual);
                        Toast.makeText(ResumenEstadistico.this,"Selecciono Todas",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.OptProcesadas:
                        CheckRadio = 2;
                        UltimoRegistro = 1;
                        VarGlobregistroactual = 1;
                        llenarTabla2(VarGlobregistroactual);
                        Toast.makeText(ResumenEstadistico.this,"Selecciono Procesadas",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.OptNoprocesada:
                        CheckRadio = 3;
                        UltimoRegistro = 1;
                        VarGlobregistroactual = 1;
                        llenarTabla2(VarGlobregistroactual);
                        Toast.makeText(ResumenEstadistico.this,"Selecciono No Procesadas",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        btnCerrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nofromclick = true;
                ingresoMenuPrincipal();
            }
        });

        btnAtras1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                atrasadelante("tras");
            }
        });

        btnAdelante1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                atrasadelante("delante");
            }
        });

        btnPrimero1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                atrasadelante("primero");
            }
        });

        btnUltimo1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                atrasadelante("ultimo");
            }
        });

        TablaListener = new View.OnClickListener() {

            public void onClick(View v) {

                try {
                    int ClickedRow_old = ClickedRow; //Ax: guardo el id anterior
                    ClickedRow = v.getId(); //Ax: Guardo el id actual

                    v.setBackgroundColor(Color.rgb(255, 77, 77)); //Ax: Subrayo la seleccionada en rojo

                    if (ClickedRow == ClickedRow_old) { //Ax: hubo dos clic de la misma fila
                        ingresoMenuPrincipal();
                    } else {
                        // Ax: Devuelvo el color a la fila anterior, para ello busco su id en un map
                        TableRow tableRow = (TableRow) findViewById(ClickedRow_old);

                        int ent = 0;
                        for (Map.Entry<Integer, String> e : map.entrySet()) {

                            if (e.getKey() == ClickedRow_old) {
                                ent++;
                                switch (e.getValue()) {
                                    case "green":
                                        tableRow.setBackgroundColor(rs.getColor(R.color.greenPopsoft));
                                        break;
                                    case "orange":
                                        tableRow.setBackgroundColor(rs.getColor(R.color.orangePopsoft));
                                        break;
                                    case "white":
                                        tableRow.setBackgroundColor(Color.WHITE);
                                        break;
                                    default:
                                        tableRow.setBackgroundColor(Color.WHITE);
                                        break;
                                }
                            }
                        }
                        if (ent < 1) {
                            tableRow.setBackgroundColor(Color.WHITE);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        ejecutarConsultaSoap();
        ajustarPantalla();
        atrasadelante("primero");//Ax: ojo! El llamado inutil aquí, a esta función, es Necesario para desbloquear el scrollview y que permita dar clic a las filas!!!
    }//end Oncreate

    //se creoo esto nuevo para realizar el conteo
// =====================================
    // MÉTODO CORRECTO PARA EJECUTAR SOAP
    // =====================================
    // Executor reutilizable a nivel de Activity, no uno nuevo por llamada.
    private final ExecutorService soapExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private void ejecutarConsultaSoap() {
        soapExecutor.execute(() -> {
            String resultado;
            try {
                WSSoap wsoap = new WSSoap(URL, paginaWs);
                String estado = wsoap.verificarWs("VALIDAR_CONEXION");
                Log.i("SOAP", "Estado WS: " + estado);   // i, no e: es un estado normal

                if ("1".equals(estado)) {
                    int faltantes = wsoap.obtenerLecturasPendientes(
                            "ObtenerLecturasPendientes",
                            ciclo,
                            Anio,
                            Mes,
                            "L" + nombrePredio
                    );
                    resultado = (faltantes >= 0)
                            ? faltantes + " POR ACTUALIZAR"
                            : "ERROR AL CONSULTAR";
                } else {
                    resultado = "SIN CONEXION";
                }
            } catch (Exception ex) {
                // Antes no habia try/catch: si el WS lanzaba, el hilo moria en
                // silencio y txtEstado se quedaba con el valor anterior para siempre.
                Log.e("SOAP", "Error consultando WS", ex);
                utils.Log(logfile, "Error en [ejecutarConsultaSoap]: " + ex.getMessage());
                resultado = "ERROR DE CONSULTA";
            }

            final String resultadoFinal = resultado;

            // TODA mutacion de UI va aqui, en el main thread. Antes txtEstado.setText()
            // se ejecutaba en el hilo de fondo, fuera del handler.post().
            mainHandler.post(() -> {
                // Evita tocar vistas de una Activity ya destruida (rotacion / salida).
                if (isFinishing() || isDestroyed()) return;

                txtEstado.setText(resultadoFinal);
                ResumenConteo = "Info Envios : " + resultadoFinal;
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Sin esto, cada Activity deja un thread pool vivo (leak).
        soapExecutor.shutdownNow();
        mainHandler.removeCallbacksAndMessages(null);
    }


    //fin de lo que se creo para el conteo

    public boolean getParamsWs() {
        try {
            BDComunicaciones bdc = crudComuni.getParams();
            if (bdc != null) {
                if (bdc.gethttpSeguro() == 0) {
                    URL = "http://" + bdc.getURL();
                } else {
                    URL = "https://" + bdc.getURL();
                }
                paginaWs = bdc.getPaginaWs();

                Log.e("INFO", "Ruta Pagina" + paginaWs);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            mensajes("Error Procesando de Seleccion de url! \n" + ex.getMessage(), 1000);
            return false;
        }
    }

 /*   public String TraerRegistrosFaltantes(int annio, int mes1, String NombreRuta) {
        try {
            WSSoap wsoap = new WSSoap(URL, paginaWs);

           String asyncResponse = wsoap.verificarWs("VALIDAR_CONEXION");
         if (asyncResponse.equals("1")) {
             int respuesta = wsoap.obtenerLecturasPendientes("ObtenerLecturasPendientes", annio, mes1, NombreRuta);//Ax: Se comprueba si existe la ruta destino remota

             if (respuesta != 99999999) {
                 //retornar el valor leido de faltantes
                 return " " + respuesta + " POR ACTUALIZAR";
             }
         }
         else {
             return " "  + " SIN CONEXION";
         }

        } catch (Exception ex) {
            utils.Log(logfile, "Error en [ObtenerLecturasPendientes]: " + ex.getMessage());
            return ("Error: al OBTENER CONTADOR \n\n, ....Intente de nuevo");
        } finally {

        }
        return "0 DOTOS ERRADOS";
    }*/


    private boolean nombreArchivo() {

        File archivoNombre = new File(VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        String archivo_cargado;
        txtNombreRuta.setText("Ruta: No esta Cargada");
        String Hora12 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        txtFecha.setText("Fecha: "+Hora12 );
        if (archivoNombre.exists() && archivoNombre.length() > 20) {

            try {

                RandomAccessFile rFile = new RandomAccessFile(archivoNombre, "rw");
                int fileSize = (int) rFile.length();
                byte[] byteArray = new byte[fileSize];
                rFile.readFully(byteArray, 0, fileSize);
                archivo_cargado = new String(byteArray);
                rFile.close();
                ciclo = Integer.parseInt(archivo_cargado.substring(6, 9));
                txtNombreRuta = (TextView) findViewById(R.id.txtNombreRuta);
                txtFecha = (TextView) findViewById(R.id.txtFecha);
                txtNombreRuta.setText("Ruta: "+archivo_cargado.substring(0, 9)+" - L"+archivo_cargado.substring(10, 20));
                archivo_cargado = archivo_cargado.substring(10, 20).trim();
                String archivoL = archivo_cargado;
                nombrePredio =archivo_cargado;
                rutaRegistro = VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + archivoL;

                File fileL = new File(rutaRegistro);

                if (!fileL.exists()) {
                    return false;
                }
                return true;

            } catch (Exception ex) {

                mensajes("Error Procesando Archivo Nombre! \n" + ex.getMessage(), 1000);
            }
        } else {
            mensajes("Dispositivo No tiene Archivos\n Cargados... Favor Cargar Datos", 1000);
        }
        return false;
    }

    /**
     * Obtiene tamaño de la pantalla y ajusta el scroll y la lista para que se ajusten
     */
    private void ajustarPantalla() { //Ax: ajustar el tamaño de los dos scrolls
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;//854
        if(height >= 1100){
            //ViewGroup.LayoutParams params = scroll.getLayoutParams();
            //params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
            //scroll.setLayoutParams(params);
            scroll.getLayoutParams().height = 700;//Telefonos grandes 230 en xml 400
            scroll.requestLayout();
            list.getLayoutParams().height = 3620;//Telefonos grandes 200 en xml 320
            list.requestLayout();

            return;
        }

        if (height >= 854) {//1184
            scroll.getLayoutParams().height = 280;//Telefonos grandes 230 en xml -- 280
            scroll.requestLayout();
            list.getLayoutParams().height = 3240;//Telefonos grandes 200 en xml   -- 240
            list.requestLayout();
        }
    }

    private void realizarConteo() {

        // try {
        infoRegistroSalida.setArchivo_TablaRegistroSalida(rutaRegistro);//directorio + VariablesGlobales.getCarpetaLecturas()+"/REGISTRO.SDA"


        infoRegistroSalida.inicializaBloque();

        File registroSalida = new File(infoRegistroSalida.getArchivo_TablaRegistroSalida());

        if (registroSalida.exists()) {//&& entradaCliente.exists() && clienteSalida.exists()

            if (!infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida())) {
                return;
            }
            resumenGlobal();
            agregarCabecera();
            llenarTabla2(VarGlobregistroactual);//Ax nuevo llenado
        } else {

            Toast.makeText(getApplicationContext(), "Problemas al Abrir Ruta Cargada", Toast.LENGTH_LONG).show();
        }
        //   } catch (Exception ex) {
        //      utils.Log(logfile, "Error en [ResumenEstadistico]realizarConteo: " + ex.getMessage());
        //     mensajes("Error Procesando Registro! \n" + ex.getMessage(), 1000);
        //  }
    }

    private int validarEstadoRegistro(int registroactual) {

        try {
            int respuesta = infoRegistroSalida.lectura_TablaRegistroSalidaX2(registroactual);
            if (respuesta==-1) {
                return (3);
            }
            if (respuesta==-2) {
                return (4);
            }
            if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") && infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("5") ) {
                //  if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L"))
                return (2);
                //   else
                //       return (1);
            }
            if (infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().length() != 0
            ) {

                VariablesGlobales.totalSoloLecturas++;
                return (1);
            }


        } catch (Exception ex) {
            utils.Log(logfile, "Error en [ResumenEstadistico]validarEstadoRegistro: " + ex.getMessage());
        }
        return 0;
    }
    //***
    int C_3 = 0;
    int C_5 = 0;
    int C_6 = 0;
    int C_12 = 0;
    int C_15 = 0;
    int C_17 = 0;
    int C_20  = 0;
    int C_23  = 0;
    int C_25  = 0;
    int C_26  = 0;
    int C_30  = 0;
    int C_32  = 0;
    int C_33  = 0;
    int C_35  = 0;
    int C_36  = 0;
    int C_otras = 0;
    //-----------------------------------

    private void resumenGlobal() {
        String linea = "";
        try {

            int tiempoPromedio = 0;
            int distanciaPromedios = 0;

            String horaini = "000000";
            String horafin = "000000";
            String HoraCompara = "";
            int resultado;
            int ultimaCuentaNoLeida = 0;

            // Inicializar contadores
            VariablesGlobales.totallecturas = 0;
            VariablesGlobales.totalcausasnolectura = 0;
            VariablesGlobales.totalnuevos = 0;
            VariablesGlobales.totalinformes = 0;
            VariablesGlobales.totalregistrosleidos = 0;
            VariablesGlobales.totalprediosleidos = 0;
            VariablesGlobales.totalpredioscomentarios = 0;
            VariablesGlobales.totalprediosliquidados = 0;
            VariablesGlobales.totalprediosimpresos = 0;
            VariablesGlobales.totalprediosnofacturados = 0;
            VariablesGlobales.registroactual = 1;

            // Nuevas variables
            VariablesGlobales.totalSoloLecturas = 0;
            VariablesGlobales.totalConsumoBajo = 0;
            VariablesGlobales.totalConsumoAlto = 0;
            VariablesGlobales.totalLecturasIguales = 0;
            VariablesGlobales.totalConsumoNormal = 0;
            VariablesGlobales.numObservaciones = 0;
            VariablesGlobales.numInformes = 0;
            VariablesGlobales.totalTiempoPromedio = 0;
            VariablesGlobales.totalDistanciaPromedio = 0;
            VariablesGlobales.setTotalClientesReales(infoRegistroSalida.getTotal_TablaRegistroSalida());

            // Reiniciar contadores de causas
            C_3 = 0; C_5 = 0; C_6 = 0; C_12= 0; C_15= 0; C_17= 0; C_20= 0;
            C_23= 0; C_25= 0; C_26= 0; C_30= 0; C_32= 0; C_33= 0; C_35= 0;
            C_36= 0;

            int clientesConLectura = 0;
            ultimaRutaSeleccionada = "";
            String miultimomedidor = "";
            infoRegistroSalida.lectura_TablaRegistroSalida(1);
            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0,1).equals("L")) {
                idLeidos.setText("✅ LEÍDOS");

            }
            else
            {
                if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0,1).equals("V")) {
                    idLeidos.setText("✅ VERIFICA");
                }
                else {
                    idLeidos.setText("✅ ENTREGAS");
                }
            }

            // ========== RECORRER REGISTROS DE LA RUTA ==========
            while (VariablesGlobales.registroactual <= infoRegistroSalida.getTotal_TablaRegistroSalida()) {

                resultado = validarEstadoRegistro(VariablesGlobales.registroactual);
                Anio = Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim().substring(0, 4));
                Mes = Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim().substring(4, 6));

                if (resultado > 2) {
                    Toast.makeText(getApplicationContext(),
                            "ARCHIVO PARA EJECUTAR TIENE ERRORES .. REPORTAR CON ADMINISTRACION Y SOPORTE DE GLOBAL, NO LEER ESTA RUTA en linea " +
                                    VariablesGlobales.registroactual, Toast.LENGTH_LONG).show();
                    return;
                }

                if (resultado == 2) {
                    ContarNovedades(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim());
                }

                linea = "linea -> " + VariablesGlobales.registroactual;

                HoraCompara = String.format("%1$6s", infoRegistroSalida.gettablaRegistroSalida_HORALECTURA()).replace(" ", "0");//.trim()

                if (Integer.parseInt(horaini) == 0)
                    horaini = HoraCompara;
                else if (Integer.parseInt(HoraCompara) != 0 && Integer.parseInt(HoraCompara) < Integer.parseInt(horaini)) {
                    horaini = HoraCompara;
                } else {
                    if (Integer.parseInt(HoraCompara) > Integer.parseInt(horafin))
                        horafin = HoraCompara;
                }

                if (resultado == 1) {
                    VariablesGlobales.totalregistrosleidos++;
                    VariablesGlobales.totallecturas++;
                    ContarNovedades(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim());
                } else if (resultado == 2) {
                    VariablesGlobales.totalregistrosleidos++;
                    if (VariablesGlobales.moduloTrabajo.equals("ENT") &&(
                            infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")
                                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("91")
                                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("92")
                                    || infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("93")
                    )) {
                        VariablesGlobales.totalSoloLecturas++;
                    } else {
                        VariablesGlobales.totalcausasnolectura++;
                    }
                    VariablesGlobales.totallecturas++;
                } else {
                    if (ultimaCuentaNoLeida == 0) {
                        ultimaCuentaNoLeida = VariablesGlobales.registroactual;
                    }
                    VariablesGlobales.totalprediosnofacturados++;
                }

                VariablesGlobales.registroactual++;

                if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("") &&
                        !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("") &&
                        !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("0") &&
                        !infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim().equals("000000000"))
                    VariablesGlobales.totalpredioscomentarios++;



                if (!infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals("") )//|| (!infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO().equals("99") &&   !infoRegistroSalida.gettablaRegistroSalida_CODESTADO().trim().equals(""))
                {
                    VariablesGlobales.totalinformes++;
                }
                if (miultimomedidor.equals(infoRegistroSalida.gettablaRegistroSalida_CUENTA())) {
                    continue;
                }




                miultimomedidor = infoRegistroSalida.gettablaRegistroSalida_CUENTA();

                switch (Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("") ? "0" :
                        infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim())) {
                    case 1:
                        VariablesGlobales.totalConsumoAlto++;
                        VariablesGlobales.totalprediosleidos++;
                        break;
                    case 2:
                        VariablesGlobales.totalLecturasIguales++;
                        VariablesGlobales.totalprediosleidos++;
                        break;
                    case 3:
                        VariablesGlobales.totalConsumoNormal++;
                        VariablesGlobales.totalprediosleidos++;
                        clientesConLectura++;
                        break;
                    case 9:
                        VariablesGlobales.totalConsumoBajo++;
                        VariablesGlobales.totalprediosleidos++;
                        break;
                    case 7:
                        VariablesGlobales.totalLecturasNegativas++;
                        VariablesGlobales.totalprediosleidos++;
                        break;
                    case 5:
                        VariablesGlobales.totalprediosleidos++;
                        break;
                }

                if (!infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals(""))
                {
                    VariablesGlobales.numInformes++;
                }

                if (!infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim().equals("")) {
                    VariablesGlobales.numObservaciones++;
                }

                if (!infoRegistroSalida.gettablaRegistroSalida_TIEMPO().trim().equals("")) {
                    tiempoPromedio = tiempoPromedio + Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_TIEMPO().trim());
                }

                if (!infoRegistroSalida.gettablaRegistroSalida_DISTANCIACALCULADA().trim().equals("")) {
                    distanciaPromedios = distanciaPromedios + Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_DISTANCIACALCULADA().trim());
                }
            }

            if (VariablesGlobales.totalregistrosleidos != 0) {
                VariablesGlobales.totalTiempoPromedio = (tiempoPromedio / VariablesGlobales.totalregistrosleidos);
                VariablesGlobales.totalDistanciaPromedio = (distanciaPromedios / VariablesGlobales.totalregistrosleidos);
            }

            // ========== LLENAR LISTA CON RESUMEN ==========
            // item.add("Info Envios : " + "");
            //item.add("Total Registradores a Leer: " + infoRegistroSalida.getTotal_TablaRegistroSalida());
            //item.add("Registradores Leidos  : " + VariablesGlobales.getTotalregistrosleidos());
            //txtEstado = (TextView) findViewById(R.id.txtEstado);
            txtTotal.setText("" +infoRegistroSalida.getTotal_TablaRegistroSalida());
            txtLeidos.setText("" + VariablesGlobales.getTotalregistrosleidos());
            if (VariablesGlobales.totalprediosnofacturados > 0) {
                //item.add("Registradores por leer    : " + VariablesGlobales.totalprediosnofacturados);
                txtPendientes.setText("" + VariablesGlobales.totalprediosnofacturados);
            } else {
                //item.add("Registradores por leer    : " + "0");
                txtPendientes.setText("" +  "0");
            }

            // ========== ESTADÍSTICAS DESDE REALM (REEMPLAZA BKENVIOSGPRS.SDA) ==========
            try {
                // Obtener estadísticas de lecturas
                int[] lecStats = CrudEnvioLectura.getEstadisticas();
                // lecStats[0] = pendientes, lecStats[1] = enviados, lecStats[2] = errores

                // Obtener estadísticas de fotos
                int[] fotoStats = CrudEnvioFoto.getEstadisticas();
                // fotoStats[0] = pendientes, fotoStats[1] = enviados, fotoStats[2] = errores

                // Obtener estadísticas de cuentas nuevas
                int[] cuentaStats = CrudEnvioCuentaNueva.getEstadisticas();
                // cuentaStats[0] = pendientes, cuentaStats[1] = enviados, cuentaStats[2] = errores

                int totalEnviados = lecStats[1];
                int totalPendientes = lecStats[0];
                int totalErrores = lecStats[2];

                // Limitar enviados al total de registros de la ruta
                if (totalEnviados > infoRegistroSalida.getTotal_TablaRegistroSalida()) {
                    totalEnviados = infoRegistroSalida.getTotal_TablaRegistroSalida();
                }

                item.add("== EVALUACION SINCRONIZACION ==");
                item.add("📄 Registros Enviadas: " + totalEnviados);
                item.add("📄 Registros Pendientes: " + totalPendientes);
                if (totalErrores > 0) {
                    item.add("⚠️ Con reintento de Envio: " + totalErrores);
                }

                // Mostrar fotos si hay alguna
                int totalFotos = fotoStats[0] + fotoStats[1] + fotoStats[2];
                if (totalFotos > 0) {
                    item.add("== SINCRONIZACION DE FOTOS ==");
                    item.add("📷 Fotos Enviadas: " + fotoStats[1]);
                    if (fotoStats[0] > 0) {
                        item.add("📷 Fotos por enviar: " + fotoStats[0]);
                    }
                    if (fotoStats[2] > 0) {
                        item.add("⚠️ Con Reintentos: " + fotoStats[2]);
                    }
                }

                // Mostrar cuentas nuevas si hay alguna

                int totalCuentas = cuentaStats[0] + cuentaStats[1] + cuentaStats[2];
                if (totalCuentas>0) {
                    item.add("== SINCRONIZACION DE NUEVAS ==");
                    if (totalCuentas > 0) {
                        item.add("🆕 Nuevas Sincronizadas: " + cuentaStats[1]);
                        if (cuentaStats[0] > 0) {
                            item.add("🆕 Nuevas por Sincronizar: " + cuentaStats[0]);
                        }
                    }
                }
                // Indicador de sincronización automática
                if (totalPendientes > 0 || fotoStats[0] > 0 || cuentaStats[0] > 0) {
                    item.add("📡 Sincronización automática activa");
                }

            } catch (Exception e) {
                Log.e("ResumenEstadistico", "Error obteniendo stats Realm: " + e.getMessage());
                item.add("Registros Enviados: (error consultando)");
            }
            // ========== FIN ESTADÍSTICAS REALM ==========
            if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L")) {
                item.add("== Desglose Lecturas ==");
                item.add("Solo tomo Lectura    : " + VariablesGlobales.totalSoloLecturas);
                item.add("Con novedad No Lectu.: " + VariablesGlobales.totalcausasnolectura);
                item.add("____________________________________");
                item.add("Registros sin Lectura: " + VariablesGlobales.totalprediosnofacturados);
            }

            item.add("== Desglose Informacion Especial ==");
            if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L")) {
                item.add("Con Comentario y Lect:  " + VariablesGlobales.totalpredioscomentarios);
                item.add("Informes Reportados  :  " + VariablesGlobales.totalinformes);
            }
            item.add("Hora Inicial         :  " + horaini);
            item.add("Hora Final           :  " + horafin);


            if (VariablesGlobales.totalprediosnofacturados == 0) {
                Toast.makeText(getApplicationContext(),
                        "Proceso de Lecturas concluido debe descargar y cargar datos nuevos", Toast.LENGTH_LONG).show();
                VariablesGlobales.registroactual = 0;
            } else {
                VariablesGlobales.registroactual = ultimaCuentaNoLeida;
            }
            if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L")) {
                item.add("== Desglose Anomalias ==");

                // ========== DESGLOSE DE CAUSAS ==========
                String CADENA = "Conciliacion Causas....";
                item.add(CADENA);

                if (C_3 > 0) item.add("Causas 3: " + C_3);
                if (C_5 > 0) item.add("Causas 5: " + C_5);
                if (C_6 > 0) item.add("Causas 6: " + C_6);
                if (C_12 > 0) item.add("Causas 12: " + C_12);
                if (C_15 > 0) item.add("Causas 15: " + C_15);
                if (C_17 > 0) item.add("Causas 17: " + C_17);
                if (C_20 > 0) item.add("Causas 20: " + C_20);
                if (C_23 > 0) item.add("Causas 23: " + C_23);
                if (C_25 > 0) item.add("Causas 25: " + C_25);
                if (C_26 > 0) item.add("Causas 26 " + C_26);
                if (C_30 > 0) item.add("Causas 30: " + C_30);
                if (C_32 > 0) item.add("Causas 32: " + C_32);
                if (C_33 > 0) item.add("Causas 33: " + C_33);
                if (C_35 > 0) item.add("Causas 35: " + C_35);
                if (C_36 > 0) item.add("Causas 36: " + C_36);
                if (C_otras > 0) item.add("Causas OTRAS: " + C_otras);
            }
            item.add("-------------FIN CONTEO-------------------");

            if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L")) {
                item.add("📊 RESUMEN VISUAL:");
                item.add("🔺 Altas               = " + VariablesGlobales.totalConsumoAlto);
                item.add("🔻 Bajas               = " + VariablesGlobales.totalConsumoBajo);
                item.add("✔  Normal              = " + VariablesGlobales.totalConsumoNormal);
                item.add("📉 Negativas           = " + VariablesGlobales.totalLecturasNegativas);
                item.add("🔄 Iguales             = "+ VariablesGlobales.totalLecturasIguales);
                item.add("🚨 Con Anomalia        = " + VariablesGlobales.totalcausasnolectura);
            }
            else
            {
                item.add("✔ Facturas Entregadas: " + VariablesGlobales.getTotalregistrosleidos());
                item.add("❌ Sin Entregar " + (infoRegistroSalida.getTotal_TablaRegistroSalida()-VariablesGlobales.getTotalregistrosleidos()));
            }
            ///incluir el porcentaje
            double porcentaje =
                    ((double) VariablesGlobales.getTotalregistrosleidos() / (double) infoRegistroSalida.getTotal_TablaRegistroSalida()) * 100;

            item.add("％ Porcentaje realizado " + String.format("%.2f %%", porcentaje));


            infoRegistroSalida.terminaBloque();
            String Hora12 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            txtFecha.setText("Fecha: "+Hora12 + " -- ％ " + String.format("%.2f %%", porcentaje));

        } catch (Exception ex) {
            isError = true;
            mensajes("Error en la " + linea + ", comuniquese con su supervisor", 5000);
            utils.Log(logfile, "Error en [ResumenEstadistico] resumenGlobal: " + ex.getMessage() + " | " + linea);
        }
    }


    public void ContarNovedades(String Causal)
    {
        switch  (Causal.trim()) {
            case "3":
                C_3++;
                break;
            case "5":
                C_5++;
                break;
            case "6":
                C_6++;
                break;
            case "12":
                C_12++;
                break;
            case "15":
                C_15++;
                break;
            case "17":
                C_17 ++;
                break;
            case "20":
                C_20 ++;
                break;
            case "23":
                C_23 ++;
                break;
            case "25":
                C_25 ++;
                break;
            case "26":
                C_26 ++;
                break;
            case "30":
                C_30 ++;
                break;
            case "32":
                C_32 ++;
                break;
            case "33":
                C_33 ++;
                break;
            case "35":
                C_35 ++;
                break;
            case "36":
                C_36 ++;
                break;
            default:
                if (Causal.trim().length()>0)
                    C_otras++;

                break;
        }

    }
    private void atrasadelante(String va) {

        switch (va) {
            case "primero":
                Ultimo = 0;
                UltimoRegistro = 1;
                VarGlobregistroactual = 1;
                llenarTabla2(VarGlobregistroactual);
                break;
            case "delante": //Adelante
                Ultimo = 0;
                if ((VarGlobregistroactual + 20) > infoRegistroSalida.getTotal_TablaRegistroSalida()) {//este if lo puso victor
                    Log.e("INFO","Ya esta en el final");
                    VarGlobregistroactual=infoRegistroSalida.getTotal_TablaRegistroSalida()-19;

                }
                else
                {
                    VarGlobregistroactual=VarGlobregistroactual+20;
                }

                UltimoRegistro = VarGlobregistroactual;
                llenarTabla2(VarGlobregistroactual);
                break;
            case "tras"://Atras
                Ultimo = 0;

                if ((VarGlobregistroactual - 20) < 1)
                {
                    UltimoRegistro = 1;
                    VarGlobregistroactual = 1;
                }
                else {
                    VarGlobregistroactual=VarGlobregistroactual-20;
                    UltimoRegistro=VarGlobregistroactual;
                }


                llenarTabla2(VarGlobregistroactual);
                break;
            case "ultimo":
                Ultimo = 1;
                VarGlobregistroactual = infoRegistroSalida.getTotal_TablaRegistroSalida() - 19;
                UltimoRegistro = VarGlobregistroactual;
                llenarTabla2(VarGlobregistroactual);
                break;
        }
    }


    private void llenarTabla2(int pos) {

        tabla.removeAllViews();
        tabla.refreshDrawableState();
        int contlineas = 1;
        VarGlobregistroactual = pos;
        if (Ultimo == 0) {
            for (int cont = pos; cont <= infoRegistroSalida.getTotal_TablaRegistroSalida(); cont++) {

                //if (contlineas > 20 && contlineas < 22 && Ultimo == 0) {
                if (contlineas > 20 ) {
                    break;
                }

                infoRegistroSalida.lectura_TablaRegistroSalida(cont);
                if (CheckRadio == 1) {// Todas
                    UltimoRegistro++;
                    contlineas++;
//                    if (Ultimo == 0) {
                    llenarListaResumen(cont);
                    //                   }
                } else if (CheckRadio == 2 && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {// Procesadas
                    UltimoRegistro++;
                    contlineas++;
                    //                  if (Ultimo == 0) {
                    llenarListaResumen(cont);
                    //                }
                } else if (CheckRadio == 3 && infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")) {// No Procesadas
                    UltimoRegistro++;
                    contlineas++;
                    //              if (Ultimo == 0) {
                    llenarListaResumen(cont);
                    //              }
                }
                pos = cont;
                int quiensoy = infoRegistroSalida.getTotal_TablaRegistroSalida();
                if (cont >= infoRegistroSalida.getTotal_TablaRegistroSalida()) {

                    break;
                }
            }

        }else {
            //if (Ultimo == 1){
            //for (int x = UltimoRegistro - 20;x <= UltimoRegistro; x++){
            for (int x = pos;x <= infoRegistroSalida.getTotal_TablaRegistroSalida(); x++){
                infoRegistroSalida.lectura_TablaRegistroSalida(x);

                // if (CheckRadio == 1){// Todas
                llenarListaResumen(x);
                //}else if (CheckRadio == 2 && !infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")){// Procesadas
                //    llenarListaResumen(pos);
                //}else if (CheckRadio == 3 && infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals("")){// No Procesadas
                //    llenarListaResumen(pos);
                //}
            }
        }

    }
    private String obtenerTipoLectura() {

        String leido = infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim();
        String causa = infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim();

        if (leido.equals("5")) {
            return "NO_LEIDO";
        }

        if (leido.equals("")) return "PENDIENTE";

        int estado = Integer.parseInt(leido);

        switch (estado) {
            case 1: return "ALTA";
            case 2: return "IGUAL";
            case 3: return "NORMAL";
            case 7: return "NEGATIVA";
            case 9: return "BAJA";
            case 5: return "NO_LEIDO";
            default: return "PENDIENTE";
        }
    }
    private int getColor(String tipo) {
        switch (tipo) {
            case "ALTA": return Color.parseColor("#E53935"); // rojo
            case "BAJA": return Color.parseColor("#1E88E5"); // azul
            case "NORMAL": return Color.parseColor("#43A047"); // verde
            case "NEGATIVA": return Color.parseColor("#8E24AA"); // morado
            case "NO_LEIDO": return Color.parseColor("#FB8C00"); // naranja fuerte
            default: return Color.LTGRAY;
        }
    }

    private String getIcono(String tipo) {
        switch (tipo) {
            case "ALTA":
                return "🔺";
            case "BAJA":
                return "🔻";
            case "NORMAL":
                return "✔";
            case "NEGATIVA":
                return "⚠";
            case "NO_LEIDO":
                return "⛔";
            default:
                return "•";
        }
    }
    public void llenarListaResumen(int pos) {

        String color = "white";
        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        fila.setClickable(true);
        fila.setOnClickListener(TablaListener);
        // =========================
        // 🔥 OBTENER VALORES
        // =========================
        String lecturaStr = infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim();
        String leido = infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim();
        String causa = infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim();

        int lectom = 0;
        try {
            lectom = Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA().trim());
        } catch (Exception ex) {
        }


        // =========================
        // 🎯 CLASIFICACIÓN VISUAL
        // =========================
        int lectura = 0;
        try {
            lectura = Integer.parseInt(lecturaStr);
        } catch (Exception ignored) {
        }
        String icono = "⚪"; // default
        int bgColor = Color.GRAY;
        if (leido.equals("5")) {
            if (VariablesGlobales.getTipoDeRuta().substring(0,1).equals("L")) {
                icono = "🚫"; // no lectura
                bgColor = rs.getColor(R.color.orangePopsoft);
                color = "orange";
            }
            else {
                icono = "✅"; // normal
                bgColor = rs.getColor(R.color.greenPop);
                color = "green";
            }
        }
        else
        {
            if ((!lecturaStr.equals("") && !leido.equals(""))) {

                if (leido.equals("7")) {
                    icono = "🔻"; // negativa
                    bgColor = Color.parseColor("#F1574B"); // azul claro
                    color = "blue";
                } else if (leido.equals("9")) {
                    icono = "⚠️"; // sospechosa
                    bgColor = Color.parseColor("#FFFF00"); // naranja claro
                    color = "yellow";
                } else if (leido.equals("1")) {
                    icono = "🔺"; // alta
                    bgColor = Color.parseColor("#FFEBEE"); // rojo suave
                    color = "red";
                } else {
                    icono = "✅"; // normal
                    bgColor = rs.getColor(R.color.greenPopsoft);
                    color = "green";
                }

            } else
            {
                if (!leido.trim().equals("")) {
                    icono = "📘"; // procesado sin lectura
                    bgColor = rs.getColor(R.color.blueCelsia);
                    color = "blue";
                }
            }
        }
        txtRuta = new TextView(this);
        txtNroMedidor = new TextView(this);
        txtDireccion = new TextView(this);
        txtNombre = new TextView(this);
        txtLectura = new TextView(this);
        txtAnomalia = new TextView(this);
        txtComentario = new TextView(this);
        txtInforme = new TextView(this);
        txtId = new TextView(this);

        txtDireccion.setText(icono+" |"+infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(0, 25));
        // txtDireccion.setText(infoRegistroSalida.gettablaRegistroSalida_DIRECCION().substring(0, 30));
        txtDireccion.setGravity(Gravity.LEFT);
        txtDireccion.setTextAppearance(this, R.style.etiqueta);
        txtDireccion.setBackgroundResource(R.drawable.tabla_celda);
        txtDireccion.setLayoutParams(layoutDireccion);

        txtNroMedidor.setText(infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim().replaceFirst("^0*", ""));//rx
        txtNroMedidor.setGravity(Gravity.LEFT);
        txtNroMedidor.setTextAppearance(this, R.style.etiqueta);
        txtNroMedidor.setBackgroundResource(R.drawable.tabla_celda);
        txtNroMedidor.setLayoutParams(layoutNroMedidor);

        txtRuta.setText(infoRegistroSalida.gettablaRegistroSalida_RUTA().trim());
        txtRuta.setGravity(Gravity.LEFT);
        txtRuta.setTextAppearance(this, R.style.etiqueta);
        txtRuta.setBackgroundResource(R.drawable.tabla_celda);
        txtRuta.setLayoutParams(layoutRuta);
        String NombreCliente =infoRegistroSalida.gettablaRegistroSalida_NOMBRE().trim();

        if (NombreCliente.length()>20) {
            NombreCliente = NombreCliente.substring(0, 20);
        }

        txtNombre.setText(NombreCliente);
        txtNombre.setGravity(Gravity.LEFT);
        txtNombre.setTextAppearance(this, R.style.etiqueta);
        txtNombre.setBackgroundResource(R.drawable.tabla_celda);
        txtNombre.setLayoutParams(layoutNombre);

        txtLectura.setText(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA());
        txtLectura.setGravity(Gravity.CENTER_HORIZONTAL);
        txtLectura.setTextAppearance(this, R.style.etiqueta);
        txtLectura.setBackgroundResource(R.drawable.tabla_celda);
        txtLectura.setBackgroundColor(bgColor);
        txtLectura.setLayoutParams(layoutLectura);

        txtAnomalia.setText(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());
        txtAnomalia.setGravity(Gravity.CENTER_HORIZONTAL);
        txtAnomalia.setTextAppearance(this, R.style.etiqueta);
        txtAnomalia.setBackgroundResource(R.drawable.tabla_celda);
        txtAnomalia.setBackgroundColor(bgColor);
        txtAnomalia.setLayoutParams(layoutAnomalia);

        txtComentario.setText(infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1());
        txtComentario.setGravity(Gravity.CENTER_HORIZONTAL);
        txtComentario.setTextAppearance(this, R.style.etiqueta);
        txtComentario.setBackgroundResource(R.drawable.tabla_celda);
        txtComentario.setLayoutParams(layoutComentario);

        if (!infoRegistroSalida.gettablaRegistroSalida_INFORME().trim().equals("")) {
            txtInforme.setText(infoRegistroSalida.gettablaRegistroSalida_INFORME().substring(0, 30));
        }else {
            txtInforme.setText(infoRegistroSalida.gettablaRegistroSalida_CODESTADO().substring(0, 30));
        }


        txtInforme.setGravity(Gravity.LEFT);
        txtInforme.setTextAppearance(this, R.style.etiqueta);
        txtInforme.setBackgroundResource(R.drawable.tabla_celda);
        txtInforme.setLayoutParams(layoutInforme);

        int id = pos;//Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR().trim()); se desborda

        txtId.setText(id + "");//Ax: antes txtId.setText(tablaEntradaCliente.gettablaEntradaClientes_CONSECUTIVO());
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(this, R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda);
        txtId.setLayoutParams(layoutId);

        fila.setId(id); //Ax: id para capturar en un Listener de la tabla el numero para si dan clic ir a liquidacion
        map.put(id, color);//Ax: Guardo el id y color para jugar con estos colores en el listener de rows ya que en ejecucion no puedo capturar getbackgrondcolor

        fila.addView(txtDireccion);
        fila.addView(txtNroMedidor);
        fila.addView(txtRuta);
        fila.addView(txtNombre);
        fila.addView(txtLectura);
        fila.addView(txtAnomalia);
        fila.addView(txtComentario);
        fila.addView(txtInforme);
        fila.addView(txtId);
        tabla.addView(fila);
    }
    public void agregarCabecera() {

        TableRow fila;

        /*TextView txtEstado = new TextView(this);
        txtEstado.setText("Estado");
        txtEstado.setBackgroundColor(Color.parseColor("#2196F3"));
        txtEstado.setLayoutParams(new TableRow.LayoutParams(120, TableRow.LayoutParams.WRAP_CONTENT));*/


        TextView txtRuta;
        TextView txtNroMedidor;
        TextView txtDireccion;
        TextView txtNombre;
        TextView txtLectura;
        TextView txtAnomalia;
        TextView txtComentario;
        TextView txtInforme;
        TextView txtId;

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        fila.setBackgroundColor(Color.parseColor("#2196F3"));
        txtRuta = new TextView(this);
        txtNroMedidor = new TextView(this);
        txtDireccion = new TextView(this);
        txtNombre = new TextView(this);
        txtLectura = new TextView(this);
        txtAnomalia = new TextView(this);
        txtComentario = new TextView(this);
        txtInforme = new TextView(this);
        txtId = new TextView(this);

        txtRuta.setText(rs.getString(R.string.ruta));
        txtRuta.setGravity(Gravity.CENTER_HORIZONTAL);
        txtRuta.setTextAppearance(this, R.style.etiqueta);
        txtRuta.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtRuta.setLayoutParams(layoutRuta);

        txtNroMedidor.setText(rs.getString(R.string.nroMedidor));
        txtNroMedidor.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNroMedidor.setTextAppearance(this, R.style.etiqueta);
        txtNroMedidor.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtNroMedidor.setLayoutParams(layoutNroMedidor);

        txtDireccion.setText(rs.getString(R.string.direccion));
        txtDireccion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDireccion.setTextAppearance(this, R.style.etiqueta);
        txtDireccion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDireccion.setLayoutParams(layoutDireccion);

        txtNombre.setText(rs.getString(R.string.nombre));
        txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNombre.setTextAppearance(this, R.style.etiqueta);
        txtNombre.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtNombre.setLayoutParams(layoutNombre);

        txtLectura.setText(rs.getString(R.string.lectura));
        txtLectura.setGravity(Gravity.CENTER_HORIZONTAL);
        txtLectura.setTextAppearance(this, R.style.etiqueta);
        txtLectura.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtLectura.setLayoutParams(layoutLectura);

        txtAnomalia.setText(rs.getString(R.string.anomalia));
        txtAnomalia.setGravity(Gravity.CENTER_HORIZONTAL);
        txtAnomalia.setTextAppearance(this, R.style.etiqueta);
        txtAnomalia.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtAnomalia.setLayoutParams(layoutAnomalia);

        txtComentario.setText(rs.getString(R.string.comentario));
        txtComentario.setGravity(Gravity.CENTER_HORIZONTAL);
        txtComentario.setTextAppearance(this, R.style.etiqueta);
        txtComentario.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtComentario.setLayoutParams(layoutComentario);

        txtInforme.setText(rs.getString(R.string.informe));
        txtInforme.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInforme.setTextAppearance(this, R.style.etiqueta);
        txtInforme.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtInforme.setLayoutParams(layoutInforme);

        txtId.setText(rs.getString(R.string.id));
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(this, R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtId.setLayoutParams(layoutId);
        fila.setWeightSum(9);

        //fila.addView(txtEstado);

        fila.addView(txtDireccion);////crearHeader("Direccion")
        fila.addView(txtNroMedidor);
        fila.addView(txtRuta);
        fila.addView(txtNombre);
        fila.addView(txtLectura);
        fila.addView(txtAnomalia);
        fila.addView(txtComentario);
        fila.addView(txtInforme);
        fila.addView(txtId);
        cabecera.addView(fila);
    }

    private TextView crearHeader(String texto) {
        TextView tv = new TextView(this);

        tv.setText(texto);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setPadding(12, 12, 12, 12);
        tv.setTextColor(Color.BLACK);

        // 🔥 CLAVE: ESTO ARREGLA TU PROBLEMA
        tv.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
        ));

        tv.setGravity(Gravity.CENTER);

        return tv;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.resumen_estadistico, menu);
        return true;
    }

    private void ingresoMenuPrincipal() {//Ax. envia datos a la actividad que la llamo, cierra esta y trata de abrir en la otra liquid.

        if (nofromclick) {

            ClickedRow = 1;

            if (VariablesGlobales.registroactual > 1) {
                ClickedRow = VariablesGlobales.registroactual;
            }
        }

        Intent iBackActivity = new Intent(this, MenuPrincipal.class);
        iBackActivity.putExtra("fila", ClickedRow + "");
        iBackActivity.putExtra("error", isError);
        setResult(RESULT_OK, iBackActivity);
        finish();
    }

    private void mensajes(String msg, int dur) {

        Toast toast = Toast.makeText(ResumenEstadistico.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 150);
        toast.show();
    }
}