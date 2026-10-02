package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.Utils;
import com.Util.WSSoap;
import com.gstolima.comunicaciones.BDComunicaciones;
import com.gstolima.comunicaciones.Comunicaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;

import com.gstolima.comunicaciones.CrudComunicaciones;
import com.gstolima.comunicaciones.EnvioLectura;
import com.gstolima.comunicaciones.EnvioFoto;
import com.gstolima.comunicaciones.EnvioCuentaNueva;
import com.gstolima.comunicaciones.General;
import com.gstolima.tablas.ClaveRuta;
import com.gstolima.comunicaciones.CrudEnvioLectura;
import com.gstolima.comunicaciones.CrudEnvioFoto;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;
import java.util.HashSet;
import java.util.Set;
import com.gstolima.tablas.TablaRegistroSalida;

public class MenuPrincipal extends AppCompatActivity {

    String aforador = "";
    String nivelOperador = "";
    String terminalImei = "";
    int puertoAPI = 0;
    String cadenaURLapi = "";
    CrudComunicaciones crudComuni;
    String archivoCargado = "";
    TextView txtArchivoCargado;
    VariablesGlobales variables = new VariablesGlobales();
    int ciclo = 0;
    int Anio = 0;
    int Mes = 0;
    String nombrePredio;
    String ResumenConteo = "";
    TablaRegistroSalida infoRegistroSalida = new TablaRegistroSalida();
    ImageButton btnIniciarLecturas;
    ImageButton btnUtilidades;
    ImageButton btnRetornar;
    ImageButton btnEliminarInfo;
    ImageButton btnMenuComunicar;
    ImageButton btnAyuda;
    Utils utils = new Utils();
    public File logfile;
    String fecha = "";
    String lactitud = "";
    String longitud = "";
    String URL = "";
    String paginaWs = "";
    String esComprimido = "";
    String rutaAdministrador = "";
    String NombreArchivos_me;
    public String archivosExt;

    int registrosPendientes=0;
    String fechaUltimoEnvio="AAAA-MM-DD HH:MM:SS";
    boolean enviado;

    public final int RESUMEN_REQUEST_CODE = 3203; //Ax: numero para activity for requiest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Realm.init(this);

        Bundle bundle = getIntent().getExtras();//pasar datos entre actividades

        txtArchivoCargado = (TextView) findViewById(R.id.txtArchivoCargado);

        NombreArchivos_me = bundle.getString("NombreArchivos_Env");
        archivoCargado = bundle.getString("archivocargado");
        aforador = bundle.getString("codigoUsuario");
        nivelOperador = bundle.getString("nivelOperador");
        terminalImei = bundle.getString("IMEI");

        txtArchivoCargado.setText("Archivo: L"+archivoCargado);

        btnIniciarLecturas = (ImageButton) findViewById(R.id.btnIniciarLecturas);
        btnUtilidades = (ImageButton) findViewById(R.id.btnUtilidades);
        btnRetornar = (ImageButton) findViewById(R.id.btnRetornar);
        btnEliminarInfo = (ImageButton) findViewById(R.id.btnElimnarInfo);
        btnMenuComunicar = (ImageButton) findViewById(R.id.btnComunica);
        btnAyuda = (ImageButton) findViewById(R.id.btnAyuda);

        btnAyuda.setEnabled(true);
        btnAyuda.setClickable(true);
        fecha = bundle.getString("fecha");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MostrarAlertDialog_Main("Alerta!", "SU TERMINAL NO TIENE ACTIVADO EL GPS", "SINGPS");
        }

        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new UsarGPS();
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mlocListener);
        crudComuni = new CrudComunicaciones(this);
        getParamsWs();
        // seleccionUrl();
        CargarArchivoCarga();
        leerNombreArchivos(10);

        btnIniciarLecturas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                procesoLecturas();
            }
        });

        btnUtilidades.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                utilidades();
            }
        });

        btnRetornar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                retornar();
            }
        });

        btnEliminarInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ejecutarBorradoDeArchivos();
            }
        });

        btnMenuComunicar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iniciarPanelComunicaciones();
            }
        });
        btnAyuda.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//por incluir el bloque del mensaje
                Log.e("CLICK", "BOTON PRESIONADO");
                VerMensajeriaEnvio();
            }
        });
    }

    private void VerMensajeriaEnvio() {
        String mensaje2 = "";
        try {
            //final String mensaje1;
            ejecutarConsultaSoap((faltantes, conexionOk) -> {

                if (!conexionOk) {
                    enviado = false;
                } else
                    enviado = true;


                if (faltantes > 0) {
                    registrosPendientes = faltantes;
                } else {
                    registrosPendientes = faltantes;
                }

                // ✅ Mensaje definido correctamente
                String mensaje1 =      "📡 ESTADO DE ENVÍO DE INFORMACIÓN\n\n" +

                        "• Estado: Conexion " + (enviado ? "✔ Información enviada correctamente" : "❌ Pendiente de envío") + "\n" +
                        "• Registros pendientes: " + registrosPendientes + "\n" +
                        "• Última sincronización: " + fechaUltimoEnvio + "\n\n" +

                        "ℹ️ RECOMENDACIONES:\n" +

                        (enviado ?
                                "- Su información ya fue transmitida al servidor.\n" +
                                        "- Puede continuar con nuevas lecturas sin inconvenientes.\n"
                                :
                                "- Verifique su conexión a internet.\n" +
                                        "- Diríjase al menú de 'Envío de datos'.\n" +
                                        "- Asegúrese de enviar las fotografías pendientes.\n"
                        ) +

                        "\n📌 SOPORTE:\n" +
                        "En caso de inconsistencias, comuníquese con su supervisor o el área técnica.\n" +
                        "Sistema SIM-LE - Gestión de Lecturas Móviles.";


                new AlertDialog.Builder(this)
                        .setTitle("Información del Sistema")
                        .setMessage(mensaje1)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                        .show();

            }); // ✅ cierre correcto del lambda
        }catch (Exception ex)
        {
            mensaje2 =
                    "📡 ESTADO DE ENVÍO\n\n" +
                            "✔ Información sincronizada con el servidor.\n" +
                            "📅 Última verificación: " +  "Hay un error de reconexion"+ "\n\n" +
                            "ℹ️ El sistema está listo para continuar operaciones.";//obtenerFechaActual()
            new AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje2)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();
        }
    }
    private void iniciarPanelComunicaciones() {

        Bundle bundle = new Bundle();
        bundle.putString("NIVEL_OPERADOR", nivelOperador);
        bundle.putString("CODIGO_INTERNO_PDA", terminalImei);
        bundle.putString("NombreArchivos_Env", NombreArchivos_me);
        Intent comunicaciones = new Intent(this, Comunicaciones.class);
        comunicaciones.putExtras(bundle);
        startActivity(comunicaciones);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
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

    private void retornar() {
        LecturaSyncService.stop(this);
        Intent iBackActivity = new Intent(this, GuiAcceso.class);
        setResult(RESULT_OK, iBackActivity);
        this.finish();
    }

    public int fotosFaltantes() { //Recorre el archivo de fotos para actualizar los que si se fueron "Y"
        FileReader r = null;
        int cont = 0;

        try {
            String path = VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/" + "F" + archivosExt;

            if (!new File(path).exists()) return 0;

            r = new FileReader(path);

            BufferedReader reader = new BufferedReader(r);
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.contains(";X") || linea.contains(";_")) {
                    cont++;
                }
            }
        } catch (Exception ex) {

            if (cont > 0) return cont;
            else return -1;
        } finally {
            try {
                if (r != null) r.close();
            } catch (Exception ex) {
            }
        }
        return cont;
    }

    public interface SoapCallback {
        void onResult(int faltantes, boolean conexionOk);
    }
    private void ejecutarConsultaSoap(SoapCallback callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            WSSoap wsoap = new WSSoap(URL, paginaWs);

            String estado = wsoap.verificarWs("VALIDAR_CONEXION");

            int faltantes = -1;
            boolean conexionOk = false;

            if ("1".equals(estado)) {
                conexionOk = true;
                int numero=0;
                try {
                    numero = Integer.parseInt(nombrePredio.substring(7,10));
                } catch (NumberFormatException e) {
                    numero = 0; // valor por defecto
                }

                if (numero < 100) // no son duplicados
                    faltantes = wsoap.obtenerLecturasPendientes(
                            "ObtenerLecturasPendientes",
                            ciclo,
                            Anio,
                            Mes,
                            "L" + nombrePredio

                    );
            }

            int finalFaltantes = faltantes;
            boolean finalConexionOk = conexionOk;

            handler.post(() -> callback.onResult(finalFaltantes, finalConexionOk));
        });
    }
    /**
     * Elimina la informacion de la ruta ACTIVA (la carpeta seleccionada en el acceso).
     *
     * QUE CAMBIO Y POR QUE:
     * Las validaciones anteriores miraban archivos planos que el sistema ya no genera desde que
     * el envio pasa por REST (LecturaSyncService):
     *   - "O<ext>.SDA"  lo escribia el envio SOAP antiguo al responder OK. Como ya no se crea,
     *                   la condicion nunca se cumplia y salia siempre "AUN NO HA ENVIADO
     *                   ARCHIVOS O FOTOGRAFIAS" aunque estuviera todo enviado. Ese era el
     *                   bloqueo que impedia borrar desde la app.
     *   - "F<ext>"      lo recorria fotosFaltantes(); tampoco se genera.
     *   - "ENVIOSGPRS.SDA" era la cola del envio GPRS anterior; hoy los pendientes estan en Realm,
     *                   asi que esa validacion siempre pasaba, incluso con pendientes reales.
     * Ahora lo pendiente se consulta donde de verdad esta: en Realm, y solo de la ruta activa.
     *
     * Tambien se quito permiteBorrarDosRutas(), que exigia terminar AMBAS rutas: existia porque
     * no se sabia a cual ruta pertenecia cada foto. Con claveRuta guardada en cada fila ya se
     * puede borrar solo la activa y dejar la otra intacta.
     */
    void ejecutarBorradoDeArchivos() {

        leerNombreArchivos(10);

        if (archivosExt.equals("SINARCHIVO") || archivosExt.equals("SIN_CARGAS")) {
            MostrarAlertDialog_Main(
                    "Alerta",
                    "ESTA MOVIL NO TIENE ARCHIVOS\nCARGADOS PARA EJECUTAR EL BORRADO",
                    "Alerta1");
            return;
        }

        final ClaveRuta rutaActiva = ClaveRuta.activa();
        if (rutaActiva == null) {
            MostrarAlertDialog_Main(
                    "Alerta",
                    "NO SE PUDO IDENTIFICAR LA RUTA CARGADA\nEN LA CARPETA ACTIVA",
                    "Alerta1");
            return;
        }

        // El administrador puede forzar el borrado saltandose las validaciones.
        if (nivelOperador.equals("A")) {
            new AlertDialog.Builder(this)
                    .setTitle("USUARIO ADMINISTRADOR")
                    .setMessage("¿DESEA FORZAR EL BORRADO DE LA RUTA ACTIVA?\n\n"
                            + descripcionDeRutaActiva(rutaActiva))
                    .setPositiveButton("Sí", (d, w) -> borrarRutaActiva(rutaActiva))
                    .setNegativeButton("No", null)
                    .show();
            return;
        }

        // --- 1) La ruta debe estar leida en su totalidad ---
        File leido = new File(VariablesGlobales.directorioactual
                + VariablesGlobales.getCarpetaLecturas() + "/LEIDO.TXT");
        if (!leido.exists()) {
            utils.MensajeTime(
                    "LA RUTA NO SE HA LEIDO EN SU TOTALIDAD\nDEBE TERMINARLA ANTES DE BORRAR",
                    "RUTA SIN TERMINAR",
                    this, 8);
            return;
        }

        // --- 2) Nada sin enviar en Realm, para ESTA ruta ---
        String faltaEnRealm = pendientesEnRealmDeRutaActiva(rutaActiva);
        if (faltaEnRealm != null) {
            utils.MensajeTime(
                    "AUN HAY INFORMACION SIN ENVIAR DE ESTA RUTA:\n\n" + faltaEnRealm
                            + "\n\nEspere a que el envio termine o revise la conexion.",
                    "FALTAN ENVÍOS!",
                    this, 8);
            return;
        }

        // --- 3) El servidor tampoco debe reportar pendientes ---
        ejecutarConsultaSoap((faltantes, conexionOk) -> {

            if (!conexionOk) {
                utils.MensajeTime("SIN CONEXIÓN AL SERVIDOR", "ERROR", this, 6);
                return;
            }

            // faltantes < 0 significa que no se pudo consultar (error, o no se consulto por el
            // tipo de carga). Antes eso pasaba la validacion como si fuera cero.
            if (faltantes < 0) {
                utils.MensajeTime(
                        "NO SE PUDO VERIFICAR CON EL SERVIDOR\nSI QUEDAN REGISTROS PENDIENTES",
                        "NO SE PUEDE BORRAR",
                        this, 8);
                return;
            }

            if (faltantes > 0) {
                utils.MensajeTime(
                        "AÚN HAY " + faltantes + " REGISTROS SIN SINCRONIZAR",
                        "NO SE PUEDE BORRAR",
                        this, 8);
                return;
            }

            mostrarConfirmacionBorrado(rutaActiva);
        });
    }

    /** Texto corto para identificar la ruta en los dialogos. */
    private String descripcionDeRutaActiva(ClaveRuta ruta) {
        String carpeta = VariablesGlobales.getCarpetaLecturas().contains(ClaveRuta.CARPETA_2)
                ? "RUTA 2" : "RUTA 1";
        String modulo = ruta.esEntrega() ? "ENTREGAS" : "LECTURAS";
        return modulo + " - " + carpeta + "\nCiclo " + ruta.getCicloReal() + "  " + ruta.getNombrePredio();
    }

    /**
     * Las filas sin claveRuta (creadas antes de que el esquema la guardara) solo se pueden
     * atribuir a esta ruta cuando no hay otra cargada.
     */
    private boolean hayOtraRutaCargada() {
        String otra = VariablesGlobales.getCarpetaLecturas().contains(ClaveRuta.CARPETA_2)
                ? ClaveRuta.CARPETA_1 : ClaveRuta.CARPETA_2;
        return ClaveRuta.leerDeCarpeta(VariablesGlobales.directorioactual, otra) != null;
    }

    /**
     * @return null si no queda nada por enviar de esa ruta; si no, el detalle para mostrar.
     */
    private String pendientesEnRealmDeRutaActiva(ClaveRuta ruta) {
        boolean incluirSinRuta = !hayOtraRutaCargada();
        String clave = ruta.getClave();

        int lecturas = CrudEnvioLectura.contarNoEnviadasDeRuta(clave, incluirSinRuta);
        int fotos = CrudEnvioFoto.contarNoEnviadasDeRuta(clave, incluirSinRuta);
        int cuentas = CrudEnvioCuentaNueva.contarNoEnviadasDeRuta(clave, incluirSinRuta);

        // -1 = no se pudo consultar. Ante la duda no se deja borrar.
        if (lecturas < 0 || fotos < 0 || cuentas < 0) {
            return "No se pudo revisar la base de datos local";
        }
        if (lecturas == 0 && fotos == 0 && cuentas == 0) {
            return null;
        }
        return "  Lecturas      : " + lecturas + "\n"
                + "  Fotografias   : " + fotos + "\n"
                + "  Cuentas nuevas: " + cuentas;
    }

    private void mostrarConfirmacionBorrado(ClaveRuta rutaActiva) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿DESEA ELIMINAR LA INFORMACIÓN DE ESTA RUTA?\n\n"
                        + descripcionDeRutaActiva(rutaActiva)
                        + "\n\nLa otra ruta, si la hay, no se toca.")
                .setPositiveButton("Sí", (dialog, which) -> borrarRutaActiva(rutaActiva))
                .setNegativeButton("No", null)
                .show();
    }

   /* void ejecutarBorradoDeArchivos() {

        leerNombreArchivos(10);

        File desc = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/O" + archivosExt + ".SDA"); //Ax: EJ: O108010.009

        if (!nivelOperador.equals("A")) {

            File[] files = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/").listFiles();
            if (files.length > 2) {
                if ((!desc.exists() && desc.length() < 1) || (fotosFaltantes() > 0)) {//|| (fotosFaltantes() > 0)
                    utils.MensajeTime("AUN NO HA ENVIADO ARCHIVOS O FOTOGRAFÍAS\n IR A LIQUIDACION EN... MENÚ (4) ULTIMAS OPCIONES ", "FALTAN ENVÍOS!", this, 8);
                    return;
                }
            }
        }
        //ejecutarConsultaSoap();

        File file = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/ENVIOSGPRS.SDA");
        if (file.exists() && file.length() > 0) {

            MostrarAlertDialog_Main("Alerta de eliminacion", "SU TERMINAL TIENE REGISTROS\nSIN ENVIAR DEBE RETORNAR \nA LECTURAS Y ENVIARLOS\n" + "DESEA REGRESAR?", "Alerta1");

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerta de eliminacion");
            builder.setMessage("DESEA CONTINUAR CON LA ELIMINACION DE LOS ARCHIVOS?");

            builder.setIcon(R.drawable.ic_launcher);

            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ejecutarBorradoArchivos2();

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
    }*/

    public void MostrarAlertDialog_Main(String titulo, String mensaje, final String NombreMetodo) { //Crea un alertDialog con si-no y espera hasta un clic SI o No

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuPrincipal.this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_launcher);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (NombreMetodo) { //Esto es para reusar este metodo 'MostrarAlertDialog' con mas llamados en 'NombreMetodo'
                    case "Alerta1":
                        break;

                    case "Alerta2":

                        break;

                    case "SINGPS":
                        finish();
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
                    case "Alerta1":

                        MostrarAlertDialog_Main("Alerta de eliminacion", "LE CONFIRMO QUE SU TERMINAL TIENE REGISTROS \n SIN ENVIAR DEBE RETORNAR A LECTURAS Y ENVIARLOS  \n DESEA REGRESAR", "Alerta2");
                        break;

                    case "Alerta2":
                        // Este camino tambien borraba las DOS carpetas con rutas fijas, sin mirar
                        // cual estaba activa. Ahora borra solo la activa, igual que el resto.
                        ClaveRuta rutaParaBorrar = ClaveRuta.activa();
                        if (rutaParaBorrar != null) {
                            borrarRutaActiva(rutaParaBorrar);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "NO SE PUDO IDENTIFICAR LA RUTA CARGADA", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case "SINGPS":
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void utilidades() {
        try {

            Intent myIntent = new Intent(MenuPrincipal.this, FechayHora.class);
            MenuPrincipal.this.startActivity(myIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean permiteBorrarDosRutas(){
        try {
            boolean existeNombre1 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES/NOMBRE").exists();
            boolean existeNombre2 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES2/NOMBRE").exists();
            // EXISTEN DOS RUTAS
            if(existeNombre1 && existeNombre2){
                boolean existeLeidos1 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES/LEIDO.TXT").exists();
                boolean existeLeidos2 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES2/LEIDO.TXT").exists();
                //Validar si existe el archivo leidos en ambas
                if(existeLeidos1 && existeLeidos2){
                    return true;
                }else{
                    return false;
                }
            } else if(existeNombre1 && !existeNombre2){
                boolean existeLeidos1 = new File (VariablesGlobales.getDirectorioactual() + "/LECTURAMEDIDORES/LEIDO.TXT").exists();
                //Validar si existe el archivo leidos en una
                if(existeLeidos1){
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        }catch (Exception ex){
            Log.e("INFO","permiteBorrarDosRutas error -> " + ex.getMessage());
            return false;
        }
    }
    private String leerNombreArchivos(int desde) {
        int error = 0;
        try {
            ciclo=0;
            Anio=0;
            Mes=0;
            nombrePredio = "";
            File file2 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
            error = 1;
            if (file2.exists() && file2.length() > 0) {
                RandomAccessFile rFile = new RandomAccessFile(file2, "rw");//Aqui se lee el 'Nombre' y se saca la extension de archivos otros ej. ".034"
                int fileSize = (int) rFile.length();
                byte[] byteArray = new byte[fileSize];
                rFile.readFully(byteArray, 0, fileSize);
                error = 2;

                archivosExt = new String(byteArray);
                Log.e("INFO","archivosExt: " +archivosExt);
                txtArchivoCargado.setText("Ciclo: "+archivosExt.substring(0, 9)+" Archivo: "+archivosExt.substring(desde, 20));
                error = 3;
                ciclo = Integer.parseInt(archivosExt.substring(6, 9));
                error = 4;
                archivosExt = archivosExt.substring(desde, 20);
                error = 5;
                String rutaRegistro = VariablesGlobales.getDirectorioactual() + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + archivosExt.trim();
                File fileL = new File(rutaRegistro);
                error = 6;
                if (fileL.exists()) {
                    nombrePredio = archivosExt;
                    infoRegistroSalida.setArchivo_TablaRegistroSalida(rutaRegistro);
                    if (infoRegistroSalida.abrir_TablaRegistroSalida(infoRegistroSalida.getArchivo_TablaRegistroSalida()))
                    {
                        error = 7;
                        infoRegistroSalida.lectura_TablaRegistroSalida(1);
                        Anio = Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim().substring(0, 4));
                        error = 8;
                        Mes = Integer.parseInt(infoRegistroSalida.gettablaRegistroSalida_CODIGOGRUPOENTREGA().trim().substring(4, 6));
                        infoRegistroSalida.Cerrar_TablaRegistroSalida();
                        error = 9;
                    }

                }
                else{
                    archivosExt="SINARCHIVO";
                    txtArchivoCargado.setText(archivosExt);
                    btnAyuda.setImageResource(R.drawable.formato_de_sinarchivo);
                }
            }
            else
            {
                archivosExt="SIN_CARGAS";
                txtArchivoCargado.setText(archivosExt);
                btnAyuda.setImageResource(R.drawable.formato_de_sinarchivo);
            }
            return archivosExt;
        } catch (Exception e) {
            Log.e("INFO","[MenuPrincipal]leerNombreArchivos("+error+") " + e.getMessage());
            utils.Log(logfile, "[MenuPrincipal]leerNombreArchivos("+error+") " + e.getMessage());
            archivosExt = "";
            txtArchivoCargado.setText("Error validando archivo");
            btnAyuda.setImageResource(R.drawable.formato_de_sinarchivo);
            return "";
        }
    }

    /**
     * Borra la informacion de UNA ruta: su carpeta, sus fotos y sus filas en Realm.
     *
     * QUE CAMBIO Y POR QUE:
     *  - Antes se llamaba con "/LECTURAMEDIDORES" fijo y luego, si existia, con la carpeta 2:
     *    borraba LAS DOS rutas e ignoraba cual estaba activa. Ahora borra solo la activa.
     *  - Antes vaciaba toda la carpeta DCIM/FOTOGRAFIASL, que es COMPARTIDA por las dos rutas,
     *    llevandose fotos de la otra ruta incluso pendientes de envio. Ahora borra solo las de
     *    esta ruta (las registradas en Realm con su claveRuta, mas las huerfanas cuya cuenta
     *    pertenece a esta ruta).
     *  - Antes hacia realm.delete() de EnvioLectura, EnvioFoto, EnvioCuentaNueva y General sin
     *    filtrar: se perdian los pendientes de la otra ruta. Y General no es de ruta: guarda el
     *    permiso de trabajo y el control de version, asi que borrarlo DESBLOQUEABA una movil
     *    bloqueada (getPermisoTrabajo() devuelve "S" cuando no hay registro). Ya no se toca.
     *  - Antes mostraba siempre "RUTA NO SE HA LEIDO EN SU TOTALIDAD", incluso completa: el
     *    LEIDO.TXT se calculaba en una variable que nunca se usaba. Ese aviso ahora se valida
     *    antes, en ejecutarBorradoDeArchivos().
     *  - Se detiene el servicio de envio mientras se borra, para que no este subiendo archivos
     *    que estamos eliminando.
     */
    private void borrarRutaActiva(ClaveRuta rutaActiva) {
        final String carpeta = VariablesGlobales.getCarpetaLecturas();
        try {
            File fileCausa = new File(VariablesGlobales.directorioactual + carpeta + "/CAUSAS.TXT");
            if (!fileCausa.exists()) {
                Toast.makeText(getApplicationContext(), "NO EXISTEN ARCHIVOS DE CARGA PARA BORRAR", Toast.LENGTH_LONG).show();
                return;
            }

            // Respaldo antes de tocar nada.
            if (!"OK".equals(GeneraUnZIp(carpeta))) {
                Toast.makeText(getApplicationContext(),
                        "NO SE PUEDE BORRAR HASTA NO \nPROCESAR EL ZIP DE RESPALDO", Toast.LENGTH_LONG).show();
                return;
            }

            // Que el servicio no este enviando mientras borramos.
            try {
                LecturaSyncService.stop(this);
            } catch (Exception ex) {
                Log.w("MenuPrincipal", "No se pudo detener el servicio de envio: " + ex.getMessage());
            }

            Set<String> cuentasDeLaRuta = cuentasDeLaCarpeta(carpeta);
            int fotos = borrarFotosDeLaRuta(rutaActiva, cuentasDeLaRuta);
            int filas = borrarFilasDeRealm(rutaActiva);
            int archivos = borrarArchivosDeLaCarpeta(carpeta);

            ClaveRuta.limpiarCache();

            // El envio se detuvo solo para no estar subiendo archivos mientras se borraban.
            // Si la otra ruta sigue cargada, puede tener pendientes: se vuelve a levantar.
            if (hayOtraRutaCargada()) {
                try {
                    LecturaSyncService.start(this);
                } catch (Exception ex) {
                    Log.w("MenuPrincipal", "No se pudo reanudar el servicio de envio: " + ex.getMessage());
                }
            }

            VariablesGlobales.moduloTrabajo = "";
            txtArchivoCargado.setText("Movil sin Archivos Cargados...Ejecute comunicaciones");
            btnAyuda.setImageResource(R.drawable.formato_de_sinarchivo);

            Log.i("MenuPrincipal", "Borrado de " + rutaActiva.getClave()
                    + ": " + archivos + " archivos, " + fotos + " fotos, " + filas + " filas de Realm");

            utils.MensajeTime(
                    "Se elimino la informacion de:\n" + descripcionDeRutaActiva(rutaActiva)
                            + "\n\n  Archivos : " + archivos
                            + "\n  Fotos    : " + fotos
                            + "\n  Registros: " + filas,
                    "BORRADO COMPLETADO", this, 8);

        } catch (Exception e) {
            Log.e("MenuPrincipal", "Error borrando la ruta activa", e);
            Toast.makeText(getApplicationContext(),
                    "RECURSO DE BORRADO CON ERROR... RESETEE E INTENTE DE NUEVO", Toast.LENGTH_LONG).show();
        }
    }

    /** Archivos de trabajo de la carpeta. No toca DCIM ni otras carpetas. */
    private int borrarArchivosDeLaCarpeta(String carpeta) {
        File dir = new File(VariablesGlobales.directorioactual + carpeta);
        File[] archivos = dir.listFiles();
        if (archivos == null) {
            return 0;
        }
        int borrados = 0;
        for (File f : archivos) {
            if (!f.isFile()) {
                continue;
            }
            String nombre = f.getName().toUpperCase();
            boolean esDeTrabajo = nombre.endsWith(".TXT") || nombre.endsWith(".ZIP")
                    || nombre.endsWith(".SDA") || nombre.endsWith("NULL") || nombre.endsWith("_TEMP")
                    || nombre.equals("NOMBRE") || nombre.endsWith(archivosExt.trim().toUpperCase());
            // LOGEVENTOS.LOG se conserva: es el historial de errores del equipo, no datos de la ruta.
            if (esDeTrabajo && !nombre.equals("LOGEVENTOS.LOG")) {
                if (f.delete()) {
                    borrados++;
                } else {
                    Log.w("MenuPrincipal", "No se pudo borrar " + f.getAbsolutePath());
                }
            }
        }
        return borrados;
    }

    /**
     * Cuentas que pertenecen a la ruta cargada en esa carpeta, leidas del archivo de registros.
     * Sirve para reconocer las fotos huerfanas (las que quedaron sin fila en Realm, por ejemplo
     * tras una reinstalacion) que son de ESTA ruta y no de la otra.
     */
    private Set<String> cuentasDeLaCarpeta(String carpeta) {
        Set<String> cuentas = new HashSet<>();
        TablaRegistroSalida tabla = new TablaRegistroSalida();
        try {
            String ruta = VariablesGlobales.directorioactual + carpeta + "/D" + archivosExt.trim();
            tabla.setArchivo_TablaRegistroSalida(ruta);
            if (!new File(ruta).exists() || !tabla.abrir_TablaRegistroSalida(ruta)) {
                return cuentas;
            }
            int total = tabla.getTotal_TablaRegistroSalida();
            for (int i = 1; i <= total; i++) {
                tabla.lectura_TablaRegistroSalida_VI(i);
                String cuenta = tabla.gettablaRegistroSalida_CUENTA();
                if (cuenta != null && !cuenta.trim().isEmpty()) {
                    cuentas.add(cuenta.trim());
                }
            }
        } catch (Exception e) {
            Log.e("MenuPrincipal", "No se pudieron leer las cuentas de la ruta: " + e.getMessage());
        } finally {
            try {
                tabla.Cerrar_TablaRegistroSalida();
            } catch (Exception ignored) {
                // ya estaba cerrado
            }
        }
        return cuentas;
    }

    /**
     * Borra las fotos de esta ruta: las que tienen fila en Realm con su claveRuta, y las
     * huerfanas cuyo nombre empieza por una cuenta de esta ruta. Las de la otra ruta se quedan.
     */
    private int borrarFotosDeLaRuta(ClaveRuta rutaActiva, Set<String> cuentasDeLaRuta) {
        int borradas = 0;

        // 1) Las registradas: se borran por su ruta local exacta.
        for (String rutaLocal : CrudEnvioFoto.rutasLocalesDeRuta(
                rutaActiva.getClave(), !hayOtraRutaCargada())) {
            File f = new File(rutaLocal);
            if (f.exists() && f.delete()) {
                borradas++;
            }
        }

        // 2) Las huerfanas de esta ruta, en la carpeta compartida de fotos.
        File carpetaFotos = new File(VariablesGlobales.directorioactual + "/DCIM/FOTOGRAFIASL");
        File[] archivos = carpetaFotos.listFiles();
        if (archivos == null || cuentasDeLaRuta.isEmpty()) {
            return borradas;
        }
        for (File f : archivos) {
            if (!f.isFile()) {
                continue;
            }
            String nombre = f.getName().toLowerCase();
            if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
                continue;
            }
            if (cuentasDeLaRuta.contains(cuentaDelNombreDeFoto(f.getName())) && f.delete()) {
                borradas++;
            }
        }
        return borradas;
    }

    /**
     * Cuenta a la que pertenece una foto, segun su nombre. Los nombres son "CUENTA_N.jpg" y, en
     * rutas de sector "V", "CUENTAVN.jpg" (sin guion bajo).
     */
    private String cuentaDelNombreDeFoto(String nombreArchivo) {
        String sinExtension = nombreArchivo;
        int punto = sinExtension.lastIndexOf('.');
        if (punto > 0) {
            sinExtension = sinExtension.substring(0, punto);
        }
        int corte = sinExtension.length();
        for (int i = 0; i < sinExtension.length(); i++) {
            if (!Character.isDigit(sinExtension.charAt(i))) {
                corte = i;
                break;
            }
        }
        return sinExtension.substring(0, corte);
    }

    /** Filas de Realm de esta ruta. General NO se toca: no es de ruta. */
    private int borrarFilasDeRealm(ClaveRuta rutaActiva) {
        boolean incluirSinRuta = !hayOtraRutaCargada();
        String clave = rutaActiva.getClave();
        int total = 0;
        for (int borradas : new int[]{
                CrudEnvioLectura.borrarDeRuta(clave, incluirSinRuta),
                CrudEnvioFoto.borrarDeRuta(clave, incluirSinRuta),
                CrudEnvioCuentaNueva.borrarDeRuta(clave, incluirSinRuta)}) {
            if (borradas > 0) {
                total += borradas;
            }
        }
        return total;
    }

    private String GeneraUnZIp(String directorioCarpeta){
        //nuevo para hacer un backup de todo si se nos quiere borrar sin la precaucion de haber enviado al servidor

        Calendar calenda = Calendar.getInstance();
        String dia = calenda.get(Calendar.DAY_OF_MONTH) + "";
        dia = String.format("%1$2s", dia).replace(" ", "0");
        dia = dia.substring(1,2);

        String RutaAGrabarX = VariablesGlobales.directorioactual + directorioCarpeta +"/";
        String RutaAGrabar = VariablesGlobales.directorioactual + "/DCIM/SIMFABACKUP/";
        String NombreZip = directorioCarpeta + "_" + dia + ".ZIP";
        File Directorio = new File(RutaAGrabar);

      /*  if (Directorio != null && Directorio.isDirectory()) {
            File[] archivos = Directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile() && archivo.getName().endsWith(".ZIP")) {
                        archivo.delete(); // elimina el archivo ZIP
                    }
                }
            }
        }*/

        File nombreDeArchivo = new File(RutaAGrabar+NombreZip);

        if (nombreDeArchivo.exists())
            nombreDeArchivo.delete();

        ArrayList filestoZip = new ArrayList();
        File f = new File(RutaAGrabarX);//Ax: ruta donde buscar los archivos
        File[] files = f.listFiles();//array de Files de la carpeta
        //validar
        for (int i = 0; i < files.length; i++) {

            File file = files[i];
            //Log.e("error","nombre archivo "+file.getName());

            //if (!file.isDirectory() && file.getName().toUpperCase().endsWith(".SDA") && !file.getName().toUpperCase().equals("ENVIOGPRS" + serialPDA + ".SDA") || file.getName().equals("TOMOLECTURA.TXT") || file.getName().equals("LOGEVENTOS.LOG")) {
            filestoZip.add(file);
            // }
        }

        String comprimir = utils.CreaZip(RutaAGrabar + NombreZip, filestoZip);
        Log.e("error","respuesta comprime "+comprimir);
        if (!comprimir.contains("✓")) {
            // mensajesAlert("Alerta de eliminacion", " ERROR AL COMPRIMIR\n Y GUARDAR UNA COPIA DE RESPALDO", 5);
            return "Error Comprimiendo \n Archivos por enviar";
        }
        return "OK";
    }


    private void procesoLecturas() {

        if (nivelOperador.equals("A")) {
            Toast.makeText(getApplicationContext(), "Proceso no lo puede ejecutar el Administrador", Toast.LENGTH_LONG).show();
            String mensaje2 =
                    "📡 ESTADO DE PROCESO\n\n" +
                            "✔ Información de prevencion al operdor.\n" +
                            "📅 NO EJECUCION: " +  "Hay una alerta de no tener permitido ingresar al modulo.."+ "\n\n" +
                            "ℹ️ El sistema le informa al administador no puede operar este modulo.";//obtenerFechaActual()
            new AlertDialog.Builder(this)
                    .setTitle("Información del Sistema")
                    .setMessage(mensaje2)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ENTENDIDO", (d, w) -> d.dismiss())
                    .show();

            return;
        }
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String fecha = df.format(date);

        if (Integer.parseInt(fecha.trim()) < 20170111) {

            Toast.makeText(this, "Fecha Desactualizada ..ir a Utilidades: " + fecha.trim() + "\nFecha Inicial a Leer: " + VariablesGlobales.fechainicial,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        File fileNombre = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        if (!fileNombre.exists()) {

            Toast.makeText(getApplicationContext(), "Dispositivo no tiene Informacion de Trabajo... Cargar Informacion", Toast.LENGTH_LONG).show();
            return;
        } else {
            inicializaProcesoLecturas();
            return;
        }
    }

    private void inicializaProcesoLecturas() {

        String archivo2 = "";

        File fileLeido = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/LEIDO.TXT");
        if (fileLeido.exists()) {
            Toast.makeText(getApplicationContext(), "Proceso ya Esta Leido..\nDescargue... la informacion para su Backup", Toast.LENGTH_LONG).show();
            // return;
        }

        File fileDescarga = new File(VariablesGlobales.directorioactual + "VariablesGlobales.getCarpetaLecturas()+\"/DESCARGA");
        if (fileDescarga.exists()) {
            Toast.makeText(getApplicationContext(), "Ruta ya Esta Descargada Borre datos y Cargue una nueva ruta", Toast.LENGTH_LONG).show();
            // return;
        }

        File archivo_1 = new File(VariablesGlobales.directorioactual + VariablesGlobales.getCarpetaLecturas()+"/NOMBRE");
        String archivocargado;
        byte[] byteArray;

        if (archivo_1.exists()) {
            try {

                RandomAccessFile rFile = new RandomAccessFile(archivo_1, "rw");
                int fileSize = (int) rFile.length();
                byteArray = new byte[fileSize];
                rFile.readFully(byteArray, 0, fileSize);
                archivocargado = new String(byteArray);
                txtArchivoCargado.setText("Ciclo"+archivocargado.substring(0, 9).trim()+" Archivo: L" + archivocargado.substring(10, 20).trim());
                rFile.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Dispositivo sin Informacion", Toast.LENGTH_LONG).show();
            return;
        }

        variables.setNombrepredio(archivo2);

        if (VariablesGlobales.registroactual>1)
            VariablesGlobales.registroactual++;

        try {
            Bundle bundle2 = new Bundle();
            bundle2.putString("aforador", aforador);
            bundle2.putString("directorioActual", VariablesGlobales.directorioactual);
            bundle2.putString("nombrePredio", variables.getNombrepredio());
            bundle2.putString("terminal", terminalImei);
            bundle2.putString("nivelOperador", nivelOperador);
            //Ax: Abre el modulo y se identifica con el ID (ANOMALIA_REQUEST_CODE), que mas adelante se recupera valor en 'onActivityResult'
            Intent intent = new Intent(MenuPrincipal.this, ResumenEstadistico.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundle2);
            startActivityForResult(intent, RESUMEN_REQUEST_CODE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Ax: se ejecuta cuando concluye el intent ResumenEstadistico y va al codigo RESUMEN_REQUEST_CODE
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESUMEN_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                String filaid = data.getStringExtra("fila");
                boolean isError = data.getBooleanExtra("error",false);
                if(isError){
                    Toast.makeText(getApplicationContext(), "Problema con los archivos, comuniquese con su supervisor", Toast.LENGTH_LONG).show();
                    return;
                }
                BDifNull bdl = new BDifNull(Realm.getDefaultInstance());
                CrudifNull[] ced = bdl.obtnerdatos();
                for (int i = 0; i < ced.length; i++) {
                    Log.d("Resultados", String.valueOf(ced[i].getId()));
                }
                if (ced.length > 0) {
                    CrudifNull cedbyid = bdl.obtenerdatabyid(1);
                    bdl.actualizarData(cedbyid, filaid, aforador, "001", VariablesGlobales.directorioactual, nivelOperador);
                } else {
                    bdl.guardarDatos(1, terminalImei, filaid, aforador, "001", VariablesGlobales.directorioactual, nivelOperador);
                    //crudCedula.setCedula(Integer.parseInt(input.getText().toString()));
                    Log.d("Insert", "guaradado");
                }

                Bundle bundle = new Bundle();
                bundle.putString("fila", filaid);
                bundle.putString("operario", aforador);
                bundle.putString("terminal", terminalImei);
                bundle.putString("impresora", "001");
                bundle.putString("path", VariablesGlobales.directorioactual);
                bundle.putString("nivelOperador", nivelOperador);

                Intent i = new Intent(this, MenuDeLiquidacion.class);
                i.putExtras(bundle);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Ha salido de Resumen Estadistico", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void muestraPosicionActual(Location loc) {

        if (loc == null) {
            longitud = "0.0";
            lactitud = "0.0";
        } else {
            lactitud = String.valueOf(loc.getLatitude());
            longitud = String.valueOf(loc.getLongitude());

        }
    }

    private class UsarGPS implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            muestraPosicionActual(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
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
                rutaAdministrador = bdc.getRutaAdministrador() + "/";
                cadenaURLapi = bdc.getUrlApi();
                esComprimido = "1";
                //cadenaURLapi = URL.substring(0, URL.lastIndexOf(":") + 1) + puertoAPI + "/api/";

                Log.e("INFO", "RutaAdministrador" + rutaAdministrador);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            //logger.error("[MenuDeLiquidacion]getParamsWs()|" + ex.getMessage());
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, verificar Configuracion IP", Toast.LENGTH_LONG).show();
            return false;
        }
    }
   /* private boolean seleccionUrl() {
        try {
            String nombreArchivo = VariablesGlobales.directorioactual + "/DIRECCIONLIP.TXT";
            File file = new File(nombreArchivo);

            if (!file.exists()) return false;

            FileReader stream3 = new FileReader(nombreArchivo);
            BufferedReader reader = new BufferedReader(stream3);
            String linea = "";
            String datoIP;
            int cont = 0;

            while ((linea = reader.readLine()) != null) {

                datoIP = linea;

                if (datoIP.substring(datoIP.length() - 1, datoIP.length()).trim().equals("A")) {
                    URL = "http://" + datoIP.substring(0, 15).trim() + ":" + datoIP.substring(16, 20).trim();
                    paginaWs = datoIP.substring(64, 157).trim();
                    cont++;
                    break;
                }
            }

            reader.close();

            if (cont < 1) return false;

            URL = URL.replace(" ", "");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, verificar Configuracion IP", Toast.LENGTH_LONG).show();
        }
        return false;
    }*/

    private boolean CargarArchivoCarga() {

        String nombreArchivo = VariablesGlobales.directorioactual + "/ARCHIVOSLCARGA.CFI";
        int contador = 0;
        try {
            FileReader r = new FileReader(nombreArchivo);
            BufferedReader reader = new BufferedReader(r);
            String linea;

            while ((linea = reader.readLine()) != null) {

                if (contador < 1) {
                    rutaAdministrador = linea.trim();

                    if (!rutaAdministrador.contains(":")) {
                        Toast.makeText(getApplicationContext(), "No hay Archivo de Soporte...\n //ARCHIVOSLCARGA.CFI", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                if (contador == 1) {
                    esComprimido = linea.trim();

                    if (esComprimido.trim().equals("ZIP SI")) {

                        esComprimido = "1";
                    } else {

                        esComprimido = "0";
                    }
                }
                contador++;
            }
            r.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }


    //se creoo esto nuevo para realizar el conteo
// =====================================
    // MÉTODO CORRECTO PARA EJECUTAR SOAP
    // =====================================
 /*   private void ejecutarConsultaSoap() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            WSSoap wsoap = new WSSoap(URL, paginaWs);

            String estado = wsoap.verificarWs("VALIDAR_CONEXION");
            Log.e("SOAP", "Estado WS: " + estado);

            final String resultado;

            if ("1".equals(estado)) {

                int faltantes = wsoap.obtenerLecturasPendientes(
                        "ObtenerLecturasPendientes",
                        Anio,
                        Mes,
                        "L" + nombrePredio
                );

                if (faltantes >= 0) {
                    resultado = faltantes + " POR ACTUALIZAR";
                } else {
                    resultado = "ERROR AL CONSULTAR";
                }


            } else {
                resultado = "SIN CONEXION";
            }
            ResumenConteo = "Info Envios : " + resultado;


        });
    }*/



}