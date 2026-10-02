package com.gstolima.moduloentregasgrupal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.ApuntadorCliente;
import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.Entregas;
import com.gstolima.tablas.EnvioGPS;
import com.gstolima.tablas.TablaRegistroSalida;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ModuloEntregasGrupal extends AppCompatActivity {

    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
    ApuntadorCliente apuntadorcliente = new ApuntadorCliente();
    public EnvioGPS misenvios = new EnvioGPS();
    Entregas entrega = new Entregas();

    String ruta = "";
    String rutabck = "";
    String fechayHoraGPS = "";
    String NombreArchivos_5 = "";
    ImageView imagennoexiste;
    String rutaCoordenadas;
    String amd_eg;
    String hm_eg;
    String ultimomed;
    String longitud = "";
    String latitud = "";
    String lector_eg;
    String terminal_eg;
    String printereg = "";
    String satelites = "";
    double ultimotiempo_eg = 1;
    String codigo_actual = "";
    String altitud;
    String municipio_fg;

    EditText txtNombreEntregrupal;
    EditText txtCuentaEntregrupal;
    EditText txtTelefEntregrupal;
    EditText txtTotalEntregrupal;
    EditText txtCuentaLeida;
    EditText txtCuentaPpal;

    ImageButton btnActivaScannEg;
    Button btnRetornarEntregag;

    Utils utils = new Utils();//Ax log y utilidades
    public File logfile; //Ax: Es para los log de error

    TableLayout tabla;
    TableLayout tablacabecera;
    TableRow fila;

    Resources resource;

    int conteo = 0;
    int registro_actual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas_grupal);

        txtCuentaPpal = (EditText) findViewById(R.id.txtCuentaPpal);
        txtCuentaLeida = (EditText) findViewById(R.id.txtCuentaLeida);
        txtTelefEntregrupal = (EditText) findViewById(R.id.txtTelefEntregrupal);
        txtTotalEntregrupal = (EditText) findViewById(R.id.txtTotalEntregrupal);
        txtNombreEntregrupal = (EditText) findViewById(R.id.txtNombreEntregrupal);
        txtCuentaEntregrupal = (EditText) findViewById(R.id.txtCuentaEntregrupal);

        btnActivaScannEg = (ImageButton) findViewById(R.id.btnActivaScannEg);
        btnRetornarEntregag = (Button) findViewById(R.id.btnRetornarEntregag);

        imagennoexiste = (ImageView) findViewById(R.id.imagennoexiste);

        resource = this.getResources();
        Bundle bundle = getIntent().getExtras();
        latitud = bundle.getString("latitud");
        altitud = bundle.getString("altitud");
        ruta = (bundle.getString("rutapath"));
        lector_eg = bundle.getString("lector");
        longitud = bundle.getString("longitud");
        terminal_eg = bundle.getString("terminal");
        satelites = (bundle.getString("satelites"));
        rutabck = (bundle.getString("rutapathbck"));
        municipio_fg = (bundle.getString("Municipio"));
        ultimomed = (bundle.getString("medidoranterior"));
        fechayHoraGPS = (bundle.getString("fechayHoraGPS"));
        ultimotiempo_eg = (bundle.getDouble("ultimotiempo"));
        NombreArchivos_5 = (bundle.getString("NombreArchivos5"));

        if (ultimomed.trim().equals("")) {
            ultimomed = "0";
        }

        logfile = new File(ruta + VariablesGlobales.getCarpetaLecturas()+"/LOGEVENTOS.LOG");
        tabla = (TableLayout) findViewById(R.id.tblaforos);
        tablacabecera = (TableLayout) findViewById(R.id.tblcabeceraforo);//
        tabla.setPadding(0, 0, 0, 0);

        leerArchivos(ruta, NombreArchivos_5);

        Llenar_Cabecera("Cuenta", "Direccion");

        btnRetornarEntregag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                retornar();
            }
        });

        btnActivaScannEg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!verficaNombreTel()) {
                    mensajes("Nombre y Telefonos obligatorios");
                    return;
                }
                startBarCodeReaderEg();
            }
        });

        txtCuentaEntregrupal.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (!verficaNombreTel()) {
                    mensajes("Nombre y Telefonos obligatorios");
                    return false;
                }

                if (actionId == 6) { //Ax "tecla ENTER" telefono chino ax = 5 otro = 6
                    String texto = txtCuentaEntregrupal.getText().toString().trim();

                    if (texto.equals("")) return false;

                    String codigo = String.format("%1$9s", texto).replace(" ", "0"); //Ax: siempre de 9
                    txtCuentaLeida.setText(codigo);

                    busca_por_medidor(texto);//CODIGO

                    // txtCuentaEntregrupal.setText("");
                    txtCuentaEntregrupal.requestFocus();
                }
                return false;
            }
        });

    }//End Oncreate

    @Override
    public void onBackPressed() {
        retornar();
    }

    //Ax: verfica nombre y telefono obligatorio
    private boolean verficaNombreTel() {

        if (txtNombreEntregrupal.getText().toString().trim().length() > 2) {

            if (txtTelefEntregrupal.getText().toString().trim().length() > 5) {
                return true;
            }
        }
        return false;
    }

    //Comprueba si si crearon regstros para devolver estados o variables a menudeliquidacion
    public void retornar() {

        if (conteo > 0) {//aqui se deben devolver parametros a liquidacion y otras cosas hacer

            Intent iBackActivityeg = new Intent(this, MenuDeLiquidacion.class);
            iBackActivityeg.putExtra("ultimomed", ultimomed);
            iBackActivityeg.putExtra("ultimotiempo_eg", ultimotiempo_eg + "");
            setResult(RESULT_OK, iBackActivityeg);
            finish();
        } else {
            finish();
        }
    }

    /***
     * Comprueba si existen o se crean los archivos siguientes archivos:
     * D Lecturas,
     * G Coordenadas,
     * M apuntador cliente (solo lectura),
     * P Entregas,
     * Archivo de EnvioGPRS (abajo un archivo backup)
     *
     * @param path            Ruta general
     * @param nombrearchivo_5 Ciclo + muncipio...etc   ej. "25160.051"
     */
    private void leerArchivos(String path, String nombrearchivo_5) {

        try {
            File f = new File(path + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + nombrearchivo_5);

            if (!f.exists()) {
                mensajes("No hay archivo de trabajo 'D'");
                retornar();
            }
            rutaCoordenadas = path + VariablesGlobales.getCarpetaLecturas()+"/" + "G" + nombrearchivo_5;//Ax: se usa mas abajo
            File g = new File(path + VariablesGlobales.getCarpetaLecturas()+"/" + "M" + nombrearchivo_5);

            if (!g.exists()) {
                mensajes("No hay archivo de trabajo 'M'");
                retornar();
            }
            entrega.setarchivo_Entregas(path + VariablesGlobales.getCarpetaLecturas()+"/" + "P" + nombrearchivo_5);
            infoRegistroSalida.setArchivo_TablaRegistroSalida(f.getAbsolutePath());
            apuntadorcliente.archivo_ApuntadorCliente = g.getAbsolutePath();

            misenvios.archivo_EnvioGPS = ruta + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA";

            File fileeg = new File(misenvios.archivo_EnvioGPS);

            if (!fileeg.exists()) {
                try {
                    fileeg.createNewFile();
                } catch (Exception e) {
                    mensajes("No hay archivo de trabajo 'EnvioGPRS'");
                    utils.Log(logfile, "[ModuloEntregasGrupal]leerArchivos(), No se pudo crear:" + fileeg.getAbsolutePath());
                }
            }

        } catch (Exception ex) {
            mensajes("Problema al leer archivos de trabajo" + "\n" + ex.getMessage());
            retornar();
        }
    }

    /***
     * Inicia la actividad del Lector de codigo de barras, el resultado va a un "onActivityResult"
     */
    public void startBarCodeReaderEg() {

        try {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);//ONE_D_CODE_TYPES
            integrator.setPrompt("CAPTURAR CODIGO DE BARRAS");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(false);
            integrator.initiateScan();
        } catch (Exception ex) {
            utils.Log(logfile, "[ModuloEntregasGrupal]startBarCodeReader(); " + ex.getMessage());
        }
    }

    /**
     * Funcion que se ejecuta cuando concluye el intent en el que se solicita
     * una imagen ya sea de la camara o del BarCode Scanner,etc
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentIntegrator.REQUEST_CODE) {//Respuesta Codigo de barras

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null) {
                String barcode = null;
                barcode = scanResult.getContents();

                if (barcode != null) {
                    mensajes("Codigo leido: " + barcode);

                    barcode = sacarCodigoBarras(barcode.trim()).trim();

                    if (barcode.equals("")) return;

                    txtCuentaEntregrupal.setText(barcode);
                    busca_por_medidor(barcode);
                } else {
                    mensajes("No se obtuvo ningun codigo...");
                }
            } else {
                mensajes("No se obtuvo ningun codigo...");
            }
        }
    }

    /***
     * Busca en el archivo 'M' la cuenta
     *
     * @param apuntador
     * @return
     */
    private boolean BuscarApuntadorCliente(String apuntador) {

        if (apuntadorcliente.abrir_ApuntadorCliente(apuntadorcliente.archivo_ApuntadorCliente)) {
            apuntadorcliente.buscarbinarioChar_ApuntadorCliente_II(apuntador);//Ax: mas rapido

            if (apuntadorcliente.encontro_ApuntadorCliente > 0) {

                infoRegistroSalida.setEncontro_TablaRegistroSalida(utils.parseStringToInteger(apuntadorcliente.getapuntadorCliente_APUNTADOR())); //Ax: guarda si encontro algo en esta tabla o la otra
                apuntadorcliente.Cerrar_ApuntadorCliente();
                return true;
            }
            apuntadorcliente.Cerrar_ApuntadorCliente();
        }
        return false;
    }

    public int busca_por_medidor(String codigoMed) {

        int registro_actual= 0;

        infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida()); //Abrir "D"

        if (codigoMed.trim().length() < 6)
            codigoMed = String.format("%1$6s", codigoMed.trim()).replace(" ", "0");

        if (codigoMed.trim().length() < 9)
            codigoMed = String.format("%1$9s", codigoMed.trim());

        if (!BuscarApuntadorCliente(codigoMed)) {

            File file = new File(ruta + VariablesGlobales.getCarpetaLecturas()+"/" + "M" + NombreArchivos_5);

            if (!file.exists()) {
                registro_actual = infoRegistroSalida.buscarSecuencial_TablaRegistroSalida(codigoMed, 1);//1
            }
        } else {
            registro_actual = utils.parseStringToInteger(apuntadorcliente.getapuntadorCliente_APUNTADOR());
            infoRegistroSalida.setEncontro_TablaRegistroSalida(registro_actual);
        }

        if (infoRegistroSalida.getEncontro_TablaRegistroSalida() > 0) { //Buscar registro en

            infoRegistroSalida.lectura_TablaRegistroSalida(registro_actual);//lea_usuario(1); //Ax mirar aqui si esta parado sobre le posicion correcta

            if (codigoMed.length() == 12) {
                printereg = "LE1";
                infoRegistroSalida.settablaRegistroSalida_IMPRESORA("LE1");
            } else {
                if (codigoMed.length() == 9) {
                    printereg = "LE2";
                    infoRegistroSalida.settablaRegistroSalida_IMPRESORA("LE2");
                } else {
                    if (infoRegistroSalida.gettablaRegistroSalida_IMPRESORA().trim().equals("")) {
                        printereg = "001";
                        infoRegistroSalida.settablaRegistroSalida_IMPRESORA("001");
                    }
                }
            }

            codigo_actual = codigoMed;

            AgregarRegistroEntrega(registro_actual);

            infoRegistroSalida.escribir_TablaRegistroSalida(registro_actual);//verificar si realiza el grabado del codigo

            imagennoexiste.setImageResource(android.R.color.transparent);

            infoRegistroSalida.Cerrar_TablaRegistroSalida();
            apuntadorcliente.Cerrar_ApuntadorCliente();
        } else {

            mensajes("# NO EXISTE " + codigoMed);
            imagennoexiste.setImageResource(R.drawable.noexiste);

            infoRegistroSalida.lectura_TablaRegistroSalida(1);//Ax: Se toma un registro de ejemplo

            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("E")) {

                if (conteo == 0) {

                    codigo_actual = codigoMed;

                    MostrarAlertDialog_eg("REGISTRO NO EXISTE", "DESEA AGREGAR EL REGISTRO AL SISTEMA?\n" + infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim(), "RegistroNuevo");
                }
            }
        }
        return (0);
    }

    private void accionRegistroNuevo() {
        CrearRegistroNuevo(codigo_actual);
        AgregarRegistroEntrega(registro_actual);
        infoRegistroSalida.Cerrar_TablaRegistroSalida();
    }

    private void AgregarRegistroEntrega(int registro) {

        try {
            String cppal = String.valueOf(Long.parseLong(txtCuentaEntregrupal.getText().toString().trim())); //quitar ceros izq.

            if (infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR().substring(0, 1).equals("E") && cppal.length() > 4) {
                //realizar el proceso de toda de codigos en la ventana de varias facturas

                if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")) {

                    if (txtCuentaPpal.getText().toString().trim().equals(""))
                        txtCuentaPpal.setText(cppal);

                    String dir = infoRegistroSalida.gettablaRegistroSalida_DIRECCION().trim().replace("   ", " ").replace("  ", " ").replace("  ", " ");
                    Llenar_Tabla_EntregaGrupal(cppal, dir);
                    conteo++;

                    txtTotalEntregrupal.setText(conteo + "");
                    infoRegistroSalida.settablaRegistroSalida_CODIGOGRUPOENTREGA(txtCuentaPpal.getText().toString().trim());
                    infoRegistroSalida.escribir_TablaRegistroSalida(registro);

                    if (!infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals("99")) {

                        procesarLectura_eg(); //Ax: escribe archivo de             "D"
                        infoRegistroSalida.escribir_TablaRegistroSalida(registro);
                        GuardarCoordenadas(); //Ax: escribe archivo de             "G"
                        guardarDatosAEnviarNuevo_fg(); //Ax: escribe archivo de    EnvioGPRS y Backup
                        CrearEntregas();//Ax: escribe archivo de                   "P"

                        mensajes("SE PROCESO ENTREGA");
                        txtNombreEntregrupal.setEnabled(false);
                        txtTelefEntregrupal.setEnabled(false);
                    } else {
                        mensajes("FACTURA YA REGISTRADA");
                    }
                } else {
                    if (txtCuentaPpal.getText().toString().trim().equals("")) //se puede dar segun victor
                        txtCuentaPpal.setText(txtCuentaEntregrupal.getText());

                    mensajes("FACTURA YA REGISTRADA!!!\n" + txtCuentaEntregrupal.getText());
                }
                txtCuentaEntregrupal.setText("");
                txtCuentaEntregrupal.requestFocus();
            } else {
                mensajes("No es un archivo de entregas o cuenta erronea");
                if (conteo == 0) {
                    txtNombreEntregrupal.setText("");
                    txtTelefEntregrupal.setText("");
                }
            }
        } catch (Exception ex) {
            mensajes("Problemas al crear registros\n: " + ex.getMessage());
            utils.Log(logfile, "[ModuloEntregasGrupal]AgregarRegistroEntrega(); " + ex.getMessage());
        }
    }

    public void Llenar_Tabla_EntregaGrupal(String codigo, String direccion) {

        TextView txtcodigo = new TextView(this);
        TextView txtdireccion = new TextView(this);

        txtcodigo.setTextSize(12);
        txtdireccion.setTextSize(12);
        txtcodigo.setBackgroundResource(R.drawable.bg_bordecomun);
        txtdireccion.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcodigo.setPadding(2, 2, 2, 2);
        txtdireccion.setPadding(2, 2, 2, 2);
        txtdireccion.setGravity(Gravity.LEFT);
        txtcodigo.setGravity(Gravity.LEFT);

        txtcodigo.setText(codigo);
        txtdireccion.setText(direccion);

        txtcodigo.setWidth(160);
        txtdireccion.setWidth(600);//190

        fila = new TableRow(this);
        fila.setBackgroundColor(resource.getColor(R.color.grayBlueSoft));

        fila.addView(txtcodigo);
        fila.addView(txtdireccion);
        tabla.addView(fila);
    }

    public void Llenar_Cabecera(String cuenta, String direccion) {

        TextView txtcuenta = new TextView(this);
        TextView txtdireccion = new TextView(this);
        txtcuenta.setTextSize(14);
        txtdireccion.setTextSize(14);

        txtcuenta.setBackgroundResource(R.drawable.bg_bordecomun);
        txtdireccion.setBackgroundResource(R.drawable.bg_bordecomun);
        txtcuenta.setPadding(2, 2, 2, 2);
        txtdireccion.setPadding(2, 2, 2, 2);
        txtdireccion.setGravity(Gravity.CENTER);
        txtcuenta.setGravity(Gravity.CENTER);

        txtcuenta.setText(cuenta);
        txtdireccion.setText(direccion);

        txtcuenta.setWidth(170);
        txtdireccion.setWidth(600);

        fila = new TableRow(this);
        fila.setBackgroundColor(resource.getColor(R.color.grayBlue));

        fila.addView(txtcuenta);
        fila.addView(txtdireccion);
        tablacabecera.addView(fila);
    }

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(ModuloEntregasGrupal.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 50);
        toast.show();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Parte copia de liquidacion
    private void tomarFechaSistema_eg() {

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        amd_eg = df.format(date);

        Date dt = new Date();
        SimpleDateFormat hf = new SimpleDateFormat("HHmmss");
        String formatteHour = hf.format(dt.getTime());
        hm_eg = formatteHour;
    }

    private void procesarLectura_eg() { //Actualizar D

        try {
            tomarFechaSistema_eg();

            double t2_eg = (Double.parseDouble(hm_eg.substring(0, 2)) * 3600);

            if (ultimotiempo_eg > t2_eg) {
                infoRegistroSalida.settablaRegistroSalida_TIEMPO(utils.parseStringToInteger(((ultimotiempo_eg - t2_eg) + "").trim()) + "");

            } else {
                infoRegistroSalida.settablaRegistroSalida_TIEMPO(utils.parseStringToInteger(((t2_eg - ultimotiempo_eg) + "").trim()) + "");
            }

            reportarDistancia(); //infoRegistroSalida.settablaRegistroSalida_distanciacalculada
            infoRegistroSalida.settablaRegistroSalida_fechalectura(amd_eg);
            infoRegistroSalida.settablaRegistroSalida_horalectura(hm_eg);
            infoRegistroSalida.settablaRegistroSalida_causadenolectura("99");
            infoRegistroSalida.settablaRegistroSalida_leido("5");
            infoRegistroSalida.settablaRegistroSalida_intentos(("1"));
            infoRegistroSalida.settablaRegistroSalida_ULTIMOMEDIDORLEIDO(ultimomed); //Ax: Pendiente,  al cerrar devolver esto a liquidacion
            ultimomed = infoRegistroSalida.gettablaRegistroSalida_CUENTA();
            infoRegistroSalida.settablaRegistroSalida_LECTOR(String.format("%1$4s", lector_eg));//infoClienteSalida.settablaClienteSalida_LECTOR
            infoRegistroSalida.settablaRegistroSalida_TERMINAL(String.format("%1$3s", terminal_eg.substring(terminal_eg.length() - 3, terminal_eg.length())));
            infoRegistroSalida.settablaRegistroSalida_IMPRESORA(String.format("%1$3s", printereg));
            infoRegistroSalida.settablaRegistroSalida_lecturatomada(String.format("%1$10s", "0"));
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada1(String.format("%1$10s", "0"));
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada2(infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim());
            infoRegistroSalida.settablaRegistroSalida_CODMUNICIPIO(municipio_fg);

        } catch (Exception ex) {
            mensajes("Problema al procesarLectura" + "\n" + ex.getMessage());
            utils.Log(logfile, "[ModuloEntregasGrupal]procesarLectura_eg(); " + ex.getMessage());
            retornar();
        }
    }

    private void GuardarCoordenadas() {  //aX Esto se debe hacer para entregas tambien

        try {
            String texto;
            String cuenta = infoRegistroSalida.gettablaRegistroSalida_CUENTA().trim();

            if (longitud.trim().length() > 12)
                longitud = longitud.trim().substring(0, 12);
            if (latitud.trim().length() > 12)
                latitud = latitud.trim().substring(0, 12);

            texto = String.format("%1$-9s", cuenta) + ";" + String.format("%1$12s", longitud.trim()) + ";" + String.format("%1$12s", latitud.trim()) + ";" + String.format("%1$2s", satelites) + ";" + "\r\n";

            File file = new File(rutaCoordenadas);
            utils.EscribirLinea(file, texto);
        } catch (Exception ex) {
            utils.Log(logfile, "[ModuloEntregasGrupal]GuardarCoordenadas(); " + ex.getMessage());
            mensajes("Problemas al guardar Coordenadas");
        }
    }

    //leer el codigo de barras
    public String sacarCodigoBarras(String Buscador) {

        try {
            if (Buscador.length() >= 6) {

                if (Buscador.length() >= 45 && Buscador.length() <= 56) {

                    if (Buscador.length() > 52) {
                        Buscador = String.format("%1$9s", Buscador.substring(22, 28)); //.PadRight(9, ' ');
                        printereg = "LE3";
                    } else {
                        Buscador = String.format("%1$9s", Buscador.substring(0, 6)); //Buscador = Buscador.substring(0, 6).PadRight(9, ' ');
                        printereg = "LE1";
                    }
                } else {
                    Buscador = String.format("%1$9s", Buscador.substring(0, 6));
                    printereg = "LE2";
                }
            } else {
                if (Buscador.length() == 0) {

                    mensajes("LECTURA DE SCANER NO VALIDO");
                    return "";
                }
                printereg = "001";
            }
        } catch (Exception ex) {
            utils.Log(logfile, "[ModuloEntregasGrupal]sacarCodigoBarras(); " + ex.getMessage());
            mensajes("error en sacar del codigo de barras");
            return "";
        }
        return Buscador;
    }

    //Ax: esta funcion viene de variables globales
    public double evaluarDistanciaAlPredio_2(double Latitud1, double Longitud1, double Latitud2, double Longitud2) {

        double degtorad = 0.01745329;
        double radtodeg = 57.29577951;
        double dlong;
        double dvalue;
        double dd;
        double km;
        dlong = Longitud1 - Longitud2;
        dvalue = (Math.sin(Latitud1 * degtorad) * Math.sin(Latitud2 * degtorad)) + (Math.cos(Latitud1 * degtorad) * Math.cos(Latitud2 * degtorad) * Math.cos(dlong * degtorad));
        dd = Math.acos(dvalue) * radtodeg;
        km = dd * 111.302;
        return km * 1000;
    }

    private int reportarDistancia() {

        try {
            if (infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim().equals(""))
                infoRegistroSalida.settablaRegistroSalida_cordenadax("0");
            if (infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim().equals(""))
                infoRegistroSalida.settablaRegistroSalida_cordenaday("0");

            int distancia = (int) evaluarDistanciaAlPredio_2(utils.parseStringToDouble(longitud), utils.parseStringToDouble(latitud), utils.parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim()), utils.parseStringToDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAY().trim()));

            infoRegistroSalida.settablaRegistroSalida_distanciacalculada(Integer.toString(distancia).trim());

            if (distancia > 250) {
                if ((Double.parseDouble(infoRegistroSalida.gettablaRegistroSalida_CORDENADAX().trim()) != 0) && (utils.parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_INTENTOS().trim()) <= 2) && (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals(""))) {

                    mensajes("ESTA CUENTA SE REGISTRO SIN ESTAR UBICADO EN EL PREDIO\nSE RECODIFICARA LA UBICACION \n DEL  PREDIO");
                    return 0;
                }
            }

        } catch (Exception e) {
            mensajes("Problema en las coordenadas iniciales");
            return 0;
        }
        return 1;
    }

    private void CrearRegistroNuevo(String Cuenta) {

        try {
            //VariablesG.MiGlobal.RegistroActual = 1;
            infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida());//lea_usuario(1);
            infoRegistroSalida.lectura_TablaRegistroSalida(1);

            String susPENDIDO = infoRegistroSalida.gettablaRegistroSalida_SUSPENDIDO();
            String sinLECTURA = infoRegistroSalida.gettablaRegistroSalida_SINLECTURA();
            String ciCLO = infoRegistroSalida.gettablaRegistroSalida_CICLO();
            String DESC_DEPTO = infoRegistroSalida.gettablaRegistroSalida_DESCDEPTO();
            String COD_MUNICIPIO = infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO();
            String COD_ESTADO = infoRegistroSalida.gettablaRegistroSalida_CODESTADO();
            String COD_SECTOR = infoRegistroSalida.gettablaRegistroSalida_CODSECTOR();
            String DESC_SECTOR = infoRegistroSalida.gettablaRegistroSalida_DESCSECTOR();
            String COD_RUTA = infoRegistroSalida.gettablaRegistroSalida_RUTA();
            String cuenta = String.format("%1$9s", Cuenta);
            String CLASE_SERV = infoRegistroSalida.gettablaRegistroSalida_CLASEDESERVICIO();
            String DESC_SERV = infoRegistroSalida.gettablaRegistroSalida_DESCSERV();
            String UBICACION = infoRegistroSalida.gettablaRegistroSalida_UBICACION();
            String NOMBRE = String.format("%1$-48s", txtNombreEntregrupal.getText().toString().trim());
            String DIRECCION = "Desconocido                                                     ";
            String NRO_MEDIDORES = "  0";
            String nro_contador = "0               ";
            String marca = "AAA";
            String TIPO_MED = " 1";
            String FACTOR_MUL = "   1.00           ";
            String DIGITOS = "5";
            String PROMEDIO = "0               ";
            String LECTURA_ANTERIOR = "0               ";
            String OBSERANT = "  ";
            String CONSERVICIODIRECTO = "0";
            String IND_OBLIGAFOTO = "0";

            //estos son los datos de la entrada

            String lectura_tomada = "0";
            String LECTURA_MODIFICADA_1 = "0";
            String LECTURA_MODIFICADA_2 = "0";
            String causa_de_no_lectura = "  ";
            String comentario_1 = "  ";
            String fecha_lectura = "0";
            String hora_lectura = "0";
            String intentos = "0";
            String MODIFICACIONES = "0";
            String ULTIMO_MEDIDOR_LEIDO = "0";
            String TIEMPO_ULT_LECTURA = "0";
            String NRO_IMPRESIONES = "0";
            String IMPRESORA = "0";
            String TERMINAL = terminal_eg.substring(terminal_eg.length() - 3, terminal_eg.length());
            String LECTOR = lector_eg;
            String informe = "Nuevo";
            String leido = " ";
            String LOGNITUDENTRADA = "0";
            String LATITUDENTRADA = "0";
            String DISTANCIA = "         0";
            String CODCATEGORIA = "0";
            String SUSPENDIDOCONLECTURA = "0";
            String CODNOTIFICACION = " 0";
            String CODIGOGRUPOENTREGA = "0";

            int registroActual = infoRegistroSalida.getTotal_TablaRegistroSalida() + 1;//VariablesG.MiGlobal.RegistroActual

            infoRegistroSalida.settablaRegistroSalida_SUSPENDIDO(susPENDIDO);
            infoRegistroSalida.settablaRegistroSalida_SINLECTURA(sinLECTURA);
            infoRegistroSalida.settablaRegistroSalida_CICLO(ciCLO);
            infoRegistroSalida.settablaRegistroSalida_DESCDEPTO(DESC_DEPTO);
            infoRegistroSalida.settablaRegistroSalida_CODMUNICIPIO(COD_MUNICIPIO);
            infoRegistroSalida.settablaRegistroSalida_CODESTADO(COD_ESTADO);
            infoRegistroSalida.settablaRegistroSalida_CODSECTOR(COD_SECTOR);
            infoRegistroSalida.settablaRegistroSalida_DESCSECTOR(DESC_SECTOR);
            infoRegistroSalida.settablaRegistroSalida_ruta(COD_RUTA);
            infoRegistroSalida.settablaRegistroSalida_CUENTA(cuenta);
            infoRegistroSalida.settablaRegistroSalida_Clasedeservicio(CLASE_SERV);
            infoRegistroSalida.settablaRegistroSalida_DESCSERV(DESC_SERV);
            infoRegistroSalida.settablaRegistroSalida_UBICACION(UBICACION);
            infoRegistroSalida.settablaRegistroSalida_Nombre(NOMBRE);
            infoRegistroSalida.settablaRegistroSalida_Direccion(DIRECCION);
            infoRegistroSalida.settablaRegistroSalida_NROMEDIDORES(NRO_MEDIDORES);
            infoRegistroSalida.settablaRegistroSalida_nrocontador(nro_contador);
            infoRegistroSalida.settablaRegistroSalida_MARCA(marca);
            infoRegistroSalida.settablaRegistroSalida_Tipomedida(TIPO_MED);
            infoRegistroSalida.settablaRegistroSalida_Factormultipicacion(FACTOR_MUL);
            infoRegistroSalida.settablaRegistroSalida_Nroenteros(DIGITOS);
            infoRegistroSalida.settablaRegistroSalida_Consumopromediocliente(PROMEDIO);
            infoRegistroSalida.settablaRegistroSalida_Lecturaanterior(LECTURA_ANTERIOR);
            infoRegistroSalida.settablaRegistroSalida_lecturatomada(lectura_tomada);
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada1(LECTURA_MODIFICADA_1);
            infoRegistroSalida.settablaRegistroSalida_lecturamodificada2(LECTURA_MODIFICADA_2);
            infoRegistroSalida.settablaRegistroSalida_causadenolectura(causa_de_no_lectura);
            infoRegistroSalida.settablaRegistroSalida_comentario1(comentario_1);
            infoRegistroSalida.settablaRegistroSalida_fechalectura(fecha_lectura);
            infoRegistroSalida.settablaRegistroSalida_horalectura(hora_lectura);
            infoRegistroSalida.settablaRegistroSalida_intentos(intentos);
            infoRegistroSalida.settablaRegistroSalida_modificaciones(MODIFICACIONES);
            infoRegistroSalida.settablaRegistroSalida_ULTIMOMEDIDORLEIDO(ULTIMO_MEDIDOR_LEIDO);
            infoRegistroSalida.settablaRegistroSalida_TIEMPO(TIEMPO_ULT_LECTURA);
            infoRegistroSalida.settablaRegistroSalida_NROIMPRESIONES(NRO_IMPRESIONES);
            infoRegistroSalida.settablaRegistroSalida_IMPRESORA(IMPRESORA);
            infoRegistroSalida.settablaRegistroSalida_TERMINAL(TERMINAL);
            infoRegistroSalida.settablaRegistroSalida_LECTOR(LECTOR);
            infoRegistroSalida.settablaRegistroSalida_informe(informe);
            infoRegistroSalida.settablaRegistroSalida_leido(leido);
            infoRegistroSalida.settablaRegistroSalida_OBSERANT(OBSERANT);
            infoRegistroSalida.settablaRegistroSalida_CONSERVICIODIRECTO(CONSERVICIODIRECTO);
            infoRegistroSalida.settablaRegistroSalida_INDOBLIGAFOTO(IND_OBLIGAFOTO);
            infoRegistroSalida.settablaRegistroSalida_cordenadax(LOGNITUDENTRADA);
            infoRegistroSalida.settablaRegistroSalida_cordenaday(LATITUDENTRADA);
            infoRegistroSalida.settablaRegistroSalida_distanciacalculada(DISTANCIA);
            infoRegistroSalida.settablaRegistroSalida_CODCATEGORIA(CODCATEGORIA);
            infoRegistroSalida.settablaRegistroSalida_SUSPENDIDOCONLECTURA(SUSPENDIDOCONLECTURA);
            infoRegistroSalida.settablaRegistroSalida_CODNOTIFICACION(CODNOTIFICACION);
            infoRegistroSalida.settablaRegistroSalida_CODIGOGRUPOENTREGA(CODIGOGRUPOENTREGA);

            infoRegistroSalida.escribir_TablaRegistroSalida(registroActual);
            infoRegistroSalida.setTotal_TablaRegistroSalida(infoRegistroSalida.getTotal_TablaRegistroSalida() + 1);
            registro_actual = infoRegistroSalida.getTotal_TablaRegistroSalida();
        } catch (Exception ex) {
            utils.Log(logfile, "[ModuloEntregasGrupal]CrearRegistroNuevo(); " + ex.getMessage());
            mensajes("error al Crear Registro Nuevo");
        }
    }

    private void CrearEntregas() {

        try {
            //entrega.Archivo_RegistroEntregas = AppPath VariablesGlobales.getCarpetaLecturas()+"\P" + S_registro.Archivo_s_regist.Substring(S_registro.Archivo_s_regist.Length - 9, 9);
            File file = new File(entrega.getarchivo_Entregas());
            if (!file.exists()) {
                file.createNewFile();
            }

            if (entrega.abrir_Entregas(entrega.getarchivo_Entregas())) {
                entrega.setEntregas_CodigoGrupoEntrega(txtCuentaPpal.getText().toString().trim());
                entrega.setEntregas_Ciclo(infoRegistroSalida.gettablaRegistroSalida_CICLO());
                entrega.setEntregas_cantidadCodigos(txtTotalEntregrupal.getText().toString().trim());
                entrega.setEntregas_Distancia(infoRegistroSalida.gettablaRegistroSalida_DISTANCIACALCULADA());
                entrega.setEntregas_CodCliente(codigo_actual);
                entrega.setEntregas_FechaEntrega(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA());
                entrega.setEntregas_HoraEntrega(infoRegistroSalida.gettablaRegistroSalida_HORALECTURA());
                entrega.setEntregas_Latitud(latitud);
                entrega.setEntregas_Longitud(longitud);
                entrega.setEntregas_Lector(lector_eg);
                entrega.setEntregas_Municipio(infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO());
                entrega.setEntregas_Nombre(txtNombreEntregrupal.getText().toString().trim());
                entrega.setEntregas_Ruta(infoRegistroSalida.gettablaRegistroSalida_RUTA());
                entrega.setEntregas_Satelites(satelites);
                entrega.setEntregas_Seccion(infoRegistroSalida.gettablaRegistroSalida_CODSECTOR());
                entrega.setEntregas_Telefono(txtTelefEntregrupal.getText().toString().trim());
                entrega.setEntregas_zona(infoRegistroSalida.gettablaRegistroSalida_DESCDEPTO().substring(30, 32));
                entrega.escribir_Entregas(entrega.gettotal_Entregas() + 1);
                entrega.settotal_Entregas(entrega.gettotal_Entregas() + 1);

                entrega.Cerrar_Entregas();
            } else {
                mensajes("error al Procesar Archivo Entregas");
            }
        } catch (Exception ex) {
            mensajes("error al Crear Entregas");
            utils.Log(logfile, "[ModuloEntregasGrupal]CrearEntregas(); " + ex.getMessage());
            retornar();
        }
    }

    private void guardarDatosAEnviarNuevo_fg() {//String NroContador, String IdContador, String LecturaTomada, String causadenolectura, String Digitos, String criticaPDA, String lecturaanterior) {

        if (!infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().equals("")) {

            try {
                misenvios.setEnvioGPS_ciclo(infoRegistroSalida.gettablaRegistroSalida_CICLO());//infoClienteEntrada.gettablaEntradaClientes_CICLO());
                misenvios.setEnvioGPS_mununicipio(infoRegistroSalida.gettablaRegistroSalida_CODMUNICIPIO());//infoClienteEntrada.gettablaEntradaClientes_MUNICIPIO());
                misenvios.setEnvioGPS_seccion(infoRegistroSalida.gettablaRegistroSalida_CODSECTOR());//infoClienteEntrada.gettablaEntradaClientes_SECTOR()
                misenvios.setEnvioGPS_departamento("73");//infoClienteEntrada.gettablaEntradaClientes_DEPARTAMENTO()
                misenvios.setEnvioGPS_anno(amd_eg.substring(0, 4));
                misenvios.setEnvioGPS_mes(amd_eg.substring(4, 6));
                misenvios.setEnvioGPS_cuenta(infoRegistroSalida.gettablaRegistroSalida_CUENTA().substring(0, 6));
                misenvios.setEnvioGPS_NroContador(infoRegistroSalida.gettablaRegistroSalida_NROCONTADOR());

                if (infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES().trim().equals(""))
                    misenvios.setEnvioGPS_idcontador("0");
                else
                    misenvios.setEnvioGPS_idcontador(infoRegistroSalida.gettablaRegistroSalida_NROMEDIDORES());// InfoRegistroEntrada.EREGISTCONCECUTIVO;

                misenvios.setEnvioGPS_lecturatomada(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA()); //InfoRegistroSalida.SREGISTLECTURATOMADA;
                misenvios.setEnvioGPS_causadenolectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());//InfoRegistroSalida.SREGISTCAUSADENOLECTURA.Trim().PadLeft(2, '0');
                // if (infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim().equals(""))
                misenvios.setEnvioGPS_descanomalia("LECTURA EXITOSA");
                // else {
//                    anomaliaDeLectura.setEncontro_AnomaliaDeNoLectura(0);
//                    if (anomaliaDeLectura.abrir_AnomaliaDeNoLectura(anomaliaDeLectura.getArchivo_AnomaliaDeNoLectura())) {
//                        anomaliaDeLectura.buscarbinario_AnomaliaDeNoLectura(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA().trim());
//                        anomaliaDeLectura.Cerrar_AnomaliaDeNoLectura();
//                    }

//                    if (anomaliaDeLectura.getEncontro_AnomaliaDeNoLectura() > 0) {
//                        String temp = anomaliaDeLectura.getanomaliaDeNoLectura_DESCRIPCION().trim();
//                        temp = String.format("%1$-50s", temp).substring(0, 30);
//                        misenvios.setEnvioGPS_descanomalia(temp);
//                    } else
//                        misenvios.setEnvioGPS_descanomalia("Causa Sin Descripcion");
                //   }

                misenvios.setEnvioGPS_descanomalia(infoRegistroSalida.gettablaRegistroSalida_CAUSADENOLECTURA());//Ax:: esto lo puse porque no e usa anomaliaDeLectura

                String temp1 = infoRegistroSalida.gettablaRegistroSalida_COMENTARIO1().trim();//.PadLeft(2, '0'); //Axx: revisar esto!
                temp1 = String.format("%1$2s", temp1).replace(" ", "0");
                misenvios.setEnvioGPS_comentario(temp1);

                misenvios.setEnvioGPS_desccomentario("");//"Con Comentario"; //Ax: antes = variables.datodebusqueda

                if (!infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().trim().equals("")) {

                    misenvios.setEnvioGPS_fechayhoralectura(infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().substring(6, 8) + "/" + infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().substring(4, 6) + "/" + infoRegistroSalida.gettablaRegistroSalida_FECHALECTURA().substring(0, 4) + " " + infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(0, 2) + ":" + infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(2, 4) + ":" + infoRegistroSalida.gettablaRegistroSalida_HORALECTURA().substring(4, 6));
                } else {
                    Calendar calendar = Calendar.getInstance();

                    String dia = calendar.get(Calendar.DAY_OF_MONTH) + "";
                    String anno = calendar.get(Calendar.YEAR) + "";
                    String mes = (calendar.get(Calendar.MONTH) + 1) + "";
                    String hora = calendar.get(Calendar.HOUR) + "";
                    String min = calendar.get(Calendar.MINUTE) + "";
                    String sec = calendar.get(Calendar.SECOND) + "";

                    dia = String.format("%1$2s", dia).replace(" ", "0");
                    anno = String.format("%1$4s", anno).replace(" ", "0");
                    mes = String.format("%1$2s", mes).replace(" ", "0");
                    hora = String.format("%1$2s", hora).replace(" ", "0");
                    min = String.format("%1$2s", min).replace(" ", "0");
                    sec = String.format("%1$2s", sec).replace(" ", "0");

                    misenvios.setEnvioGPS_fechayhoralectura(dia + "/" + mes + "/" + anno + " " + hora + ":" + min + ":" + sec);
                }

                if (infoRegistroSalida.gettablaRegistroSalida_LECTOR().trim().equals("")) {//infoClienteSalida.gettablaClienteSalida_LECTOR()
                    infoRegistroSalida.settablaRegistroSalida_LECTOR(lector_eg);
                    misenvios.setEnvioGPS_CodLector(lector_eg);
                } else {
                    misenvios.setEnvioGPS_CodLector(infoRegistroSalida.gettablaRegistroSalida_LECTOR());//infoClienteSalida.gettablaClienteSalida_LECTOR()
                }

                misenvios.setEnvioGPS_primermedidor("1");//nfoClienteSalida.gettablaClienteSalida_PRIMERMEDIDOR()
                misenvios.setEnvioGPS_HoraImpresion("80");// infoClienteSalida.SCLIENTHORAIMPRESION;
                misenvios.setEnvioGPS_NombreArchivo(NombreArchivos_5);
                misenvios.setEnvioGPS_Longitud(longitud);
                misenvios.setEnvioGPS_Latitud(latitud);
                misenvios.setEnvioGPS_NroSatelites(satelites);
                misenvios.setEnvioGPS_Distancia(infoRegistroSalida.gettablaRegistroSalida_DISTANCIACALCULADA());//infoClienteSalida.gettablaClienteSalida_DISTANCIACALCULADA()
                misenvios.setEnvioGPS_FechaHoraSatelite(fechayHoraGPS);
                misenvios.setEnvioGPS_AltitudSatelite(altitud);
                misenvios.setEnvioGPS_Terminal(terminal_eg);

                //if (VariablesGlobales.ultimaNovedad.equals("0000"))
                misenvios.setEnvioGPS_IndicadorNovedad("N");// "Ind Nov";
//                else
//                    misenvios.setEnvioGPS_IndicadorNovedad("S");// "Ind Nov";

                if (printereg.length() > 1) {
                    if (!printereg.substring(0, 2).equals("LE")) {
                        misenvios.setEnvioGPS_Indcodbarras("N"); //"Ind_CodBa";
                    } else {
                        misenvios.setEnvioGPS_Indcodbarras("S"); //"Ind_CodBa";
                    }
                } else {
                    misenvios.setEnvioGPS_Indcodbarras("S"); //"Ind_CodBa";
                }

                misenvios.setEnvioGPS_Intentos(infoRegistroSalida.gettablaRegistroSalida_INTENTOS());
                misenvios.setEnvioGPS_criticapda(infoRegistroSalida.gettablaRegistroSalida_LEIDO());// InfoRegistroSalida.SREGISTLEIDO; //criticaPDA
                //misenvios.setEnvioGPS_ValorFacturado("0");//infoClienteSalida.gettablaClienteSalida_VALORFACTURADO()

                int x = Math.abs(utils.parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURATOMADA()) - utils.parseStringToInteger(infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR()));//Ax: antes Double.parseDouble
                String temp2 = x + "";//infoClienteSalida.gettablaClienteSalida_CONSUMOFACTURADO().trim()
                temp2 = String.format("%1$10s", temp2).replace(" ", "0");
                misenvios.setEnvioGPS_ConsumoFacturado(temp2);

                misenvios.setEnvioGPS_SubContribucion("0");//infoClienteSalida.gettablaClienteSalida_CONTRIBUCIONENERGIA()
                misenvios.setEnvioGPS_Consumo1("0");
                misenvios.setEnvioGPS_Consumo2("0");
                misenvios.setEnvioGPS_Consumo3("0");

                //faltaria un consumo tres que es el de CT contarores testigos
                misenvios.setEnvioGPS_NroConceptos("0");//infoClienteEntrada.gettablaEntradaClientes_NRODECONCEPTOS()

                if (infoRegistroSalida.gettablaRegistroSalida_LEIDO().trim().equals(""))// infoClienteSalida.gettablaClienteSalida_INDFACTURACION().trim().equals(""))
                    misenvios.setEnvioGPS_IndFacturacion("N");
                else
                    misenvios.setEnvioGPS_IndFacturacion(infoRegistroSalida.gettablaRegistroSalida_LEIDO());//infoClienteSalida.gettablaClienteSalida_INDFACTURACION());

                String temp4 = "0";//infoClienteEntrada.gettablaEntradaClientes_NROFACTURA().trim();
                temp4 = String.format("%1$15s", temp4).replace(" ", "0");
                misenvios.setEnvioGPS_NroFactura(temp4);

                temp4 = "01/01/2017";//infoClienteSalida.gettablaClienteSalida_FECHAVENCE().trim().trim();
                temp4 = String.format("%1$8s", temp4).replace(" ", "0");

                if (temp4.equals("00000000")) {

                    Calendar calenda = Calendar.getInstance();

                    String dia = calenda.get(Calendar.DAY_OF_MONTH) + "";
                    String anno = calenda.get(Calendar.YEAR) + "";
                    String mes = (calenda.get(Calendar.MONTH) + 1) + "";

                    dia = String.format("%1$2s", dia).replace(" ", "0");
                    anno = String.format("%1$4s", anno).replace(" ", "0");
                    mes = String.format("%1$2s", mes).replace(" ", "0");

                    misenvios.setEnvioGPS_FechaVence(dia + "/" + mes + "/" + anno);

                    misenvios.setEnvioGPS_FechaCorte(dia + "/" + mes + "/" + anno);
                } else {

                    String temp5 = "01/01/2017";// infoClienteSalida.gettablaClienteSalida_FECHAVENCE().trim();
                    temp5 = String.format("%1$8s", temp5).replace(" ", "0");
                    misenvios.setEnvioGPS_FechaVence(temp5);

                    temp5 = "01/01/2017";//infoClienteSalida.gettablaClienteSalida_FECHACORTE().trim();
                    temp5 = String.format("%1$8s", temp5).replace(" ", "0");
                    misenvios.setEnvioGPS_FechaCorte(temp5);
                }

                String temp_eg = String.format("%1$10s", infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA1().trim()).replace(" ", "0");

                misenvios.setEnvioGPS_LecturaModificada1(temp_eg);// InfoRegistroSalida.SREGISTLECTURAMODIFICADA1.Trim().PadLeft(10, '0');

                temp_eg = String.format("%1$10s", infoRegistroSalida.gettablaRegistroSalida_LECTURAMODIFICADA2().trim()).replace(" ", "0");

                misenvios.setEnvioGPS_LecturaModificada2(temp_eg);// InfoRegistroSalida.SREGISTLECTURAMODIFICADA2.Trim().PadLeft(10, '0');

                misenvios.setEnvioGPS_Digitos(infoRegistroSalida.gettablaRegistroSalida_NROENTEROS());// InfoRegistroEntrada.EREGISTNROENTEROS.Trim().PadLeft(1, '0');//Digitos

                String dc = infoRegistroSalida.gettablaRegistroSalida_CUENTA().substring(6, 9);
                dc = String.format("%1$3s", dc).replace(" ", "0");

                misenvios.setEnvioGPS_digitochequeo(dc);
                misenvios.setEnvioGPS_Procesado("N");

                misenvios.setEnvioGPS_NumeroConceptos("0");//infoClienteEntrada.gettablaEntradaClientes_NRODECONCEPTOS()
                misenvios.setEnvioGPS_PrimerConcepto("0");//infoClienteEntrada.gettablaEntradaClientes_PRIMERCONCEPTO()

                misenvios.setEnvioGPS_CritiaSIEC("0");//ultimaCriticaLectura
                misenvios.setEnvioGPS_CodNovedad("0000");//variables.ultimaNovedad

                String temp6 = infoRegistroSalida.gettablaRegistroSalida_LECTURAANTERIOR().trim();//lecturaanterior
                temp6 = String.format("%1$10s", temp6).replace(" ", "0");
                misenvios.setEnvioGPS_LecturaAnterior(temp6);

                misenvios.setEnvioGPS_EstadoEnvio("N");
                misenvios.setEnvioGPS_CRNL("\r\n");

                misenvios.archivo_EnvioGPS = ruta + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA";
                //crear el protocolo para este otro recurso
                if (misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS)) {

                    misenvios.escribir_EnvioGPS(misenvios.total_EnvioGPS + 1);
                    misenvios.total_EnvioGPS++;

                    misenvios.Cerrar_EnvioGPS();

                    //crea la copia de seguridad del archivo de lecturas------------
                    misenvios.archivo_EnvioGPS = rutabck + "COPIARESPALDOLECTURAS.SDA";

                    File filegps = new File(misenvios.archivo_EnvioGPS);

                    if (!filegps.exists()) {
                        try {
                            filegps.createNewFile();
                        } catch (Exception ep) {
                            mensajes("No se Pudo crear CopiaRespaldoLecturas\n " + ep.getMessage());
                        }
                    }
                    if (misenvios.abrir_EnvioGPS(misenvios.archivo_EnvioGPS)) {
                        misenvios.escribir_EnvioGPS(misenvios.total_EnvioGPS + 1);
                        misenvios.Cerrar_EnvioGPS();
                    }
                    //crear el protocolo para este otro recurso
                } else {
                    utils.Log(logfile, "[ModuloEntregasGrupal]guardarDatosAEnviarNuevo_fg();No se pudo Leer:" + misenvios.archivo_EnvioGPS);
                    mensajes(("No se pudo registrar en el archivo de datos " + misenvios.archivo_EnvioGPS));
                }

            } catch (Exception ex) {
                mensajes("[ModuloEntregasGrupal]guardarDatosAEnviarNuevo_fg();" + ex.getMessage());
                utils.Log(logfile, "[ModuloEntregasGrupal]guardarDatosAEnviarNuevo_fg();error:" + misenvios.archivo_EnvioGPS + "; " + ex.getMessage());
                retornar();
            }
        }
    }

    public void MostrarAlertDialog_eg(String titulo, String mensaje, final String NombreMetodo) { //Crea un alertDialog con si-no y espera hasta un clic SI o No

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ModuloEntregasGrupal.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) { //Esto es para reusar este metodo 'MostrarAlertDialog' con mas llamados en 'NombreMetodo'
                    case "RegistroNuevo":
                        accionRegistroNuevo();
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
                    case "RegistroNuevo":
                        break;

                    default:
                        break;
                }
            }
        });

        builder.create().show();
    }
}


