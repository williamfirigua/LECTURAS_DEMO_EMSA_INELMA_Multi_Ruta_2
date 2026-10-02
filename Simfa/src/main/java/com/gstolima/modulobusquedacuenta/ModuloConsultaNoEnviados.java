package com.gstolima.modulobusquedacuenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gstolima.accesoyseguridad.R;
import com.gstolima.comunicaciones.CrudEnvioLectura;
import com.gstolima.comunicaciones.CrudEnvioFoto;
import com.gstolima.comunicaciones.CrudEnvioCuentaNueva;
import com.gstolima.comunicaciones.EnvioLectura;
import com.gstolima.comunicaciones.EnvioFoto;
import com.gstolima.comunicaciones.EnvioCuentaNueva;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuloConsultaNoEnviados - Consulta de registros pendientes de envío
 * 
 * MODIFICADO: Ahora lee desde Realm en lugar de archivos planos
 * - Lecturas pendientes: CrudEnvioLectura
 * - Fotos pendientes: CrudEnvioFoto
 * - Cuentas nuevas pendientes: CrudEnvioCuentaNueva
 * 
 * Estados: 0=Pendiente, 1=Enviado, 2=Error, 3=NoExiste (fotos)
 * 
 * @author Global Solutions & Service S.A.S.
 */
public class ModuloConsultaNoEnviados extends AppCompatActivity {

    // UI Components (del layout existente)
    ImageButton btnCerrarNe2;
    ImageButton btnAtras1;
    ImageButton btnAdelante1;
    ImageButton btnPrimero1;
    ImageButton btnUltimo1;

    Resources rs;
    TextView lblpos;
    TextView txtregnoenviados;

    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutCuenta;
    TableRow.LayoutParams layoutContador;

    String path;
    String serialPDA = "000000000000000";
    String serialNumberImei;

    // Datos desde Realm
    List<EnvioLectura> lecturasPendientes = new ArrayList<>();
    List<EnvioFoto> fotosPendientes = new ArrayList<>();
    List<EnvioCuentaNueva> cuentasPendientes = new ArrayList<>();
    
    // Lista combinada para mostrar
    List<RegistroPendiente> registrosCombinados = new ArrayList<>();

    int totalLecturas = 0;
    int totalFotos = 0;
    int totalCuentas = 0;
    int totalGeneral = 0;
    
    int registroactual = 0;
    int registrolimite = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_consultnoenviads);

        rs = this.getResources();

        Bundle bundle = getIntent().getExtras();
        path = bundle.getString("directorioactual");
        serialNumberImei = bundle.getString("serialNumberImei");
        serialPDA = serialNumberImei;

        inicializarUI();
        cargarDatosDesdeRealm();
    }

    private void inicializarUI() {
        tabla = (TableLayout) findViewById(R.id.tblcuentacontador);
        cabecera = (TableLayout) findViewById(R.id.tblcabecera);
        
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutCuenta = new TableRow.LayoutParams(350, TableRow.LayoutParams.WRAP_CONTENT);
        layoutContador = new TableRow.LayoutParams(900, TableRow.LayoutParams.WRAP_CONTENT);

        btnAdelante1 = (ImageButton) findViewById(R.id.btnAdelanteNe2);
        btnAtras1 = (ImageButton) findViewById(R.id.btnAtrasNe2);
        btnPrimero1 = (ImageButton) findViewById(R.id.btnPrimeroNe2);
        btnUltimo1 = (ImageButton) findViewById(R.id.btnUltimoNe2);

        txtregnoenviados = (TextView) findViewById(R.id.txtregnoenviados);
        lblpos = (TextView) findViewById(R.id.lblpos);

        btnCerrarNe2 = (ImageButton) findViewById(R.id.btnCerrarNe2);

        // Listeners
        btnCerrarNe2.setOnClickListener(v -> volveraLiquidacion());
        btnAtras1.setOnClickListener(v -> navegar("atras"));
        btnAdelante1.setOnClickListener(v -> navegar("adelante"));
        btnPrimero1.setOnClickListener(v -> navegar("primero"));
        btnUltimo1.setOnClickListener(v -> navegar("ultimo"));
    }

    /**
     * Carga los datos pendientes desde Realm
     */
    private void cargarDatosDesdeRealm() {
        try {
            // Cargar lecturas pendientes
            lecturasPendientes = CrudEnvioLectura.obtenerPendientes(500);
            totalLecturas = lecturasPendientes.size();
            
            // Cargar fotos pendientes
            fotosPendientes = CrudEnvioFoto.obtenerPendientes(500);
            totalFotos = fotosPendientes.size();
            
            // Cargar cuentas nuevas pendientes
            cuentasPendientes = CrudEnvioCuentaNueva.obtenerPendientes(500);
            totalCuentas = cuentasPendientes.size();
            
            // Combinar todos los registros
            combinarRegistros();
            
            totalGeneral = registrosCombinados.size();
            
            Log.i("ModuloConsultaNoEnviados", String.format(
                "Pendientes: %d lecturas, %d fotos, %d cuentas = %d total",
                totalLecturas, totalFotos, totalCuentas, totalGeneral
            ));
            
            if (totalGeneral == 0) {
                mensajes("No hay Registros para enviar al Servidor");
                finish();
                return;
            }
            
            txtregnoenviados.setText(String.valueOf(totalGeneral));
            agregarCabecera();
            mostrarRegistros();
            
        } catch (Exception ex) {
            Log.e("ModuloConsultaNoEnviados", "Error cargando datos: " + ex.getMessage());
            mensajes("Error cargando datos");
            finish();
        }
    }

    /**
     * Combina todos los registros pendientes en una lista única
     */
    private void combinarRegistros() {
        registrosCombinados.clear();
        
        // Agregar lecturas
        for (EnvioLectura lectura : lecturasPendientes) {
            RegistroPendiente reg = new RegistroPendiente();
            reg.tipo = "L"; // Lectura
            reg.cuenta = lectura.getCuenta();
            reg.detalle = lectura.getNroContador();
            reg.estado = lectura.getEstadoEnvio(); // int: 0,1,2
            reg.idRealm = lectura.getIdRealm(); // long
            registrosCombinados.add(reg);
        }
        
        // Agregar fotos
        for (EnvioFoto foto : fotosPendientes) {
            RegistroPendiente reg = new RegistroPendiente();
            reg.tipo = "F"; // Foto
            reg.cuenta = foto.getCodCuenta();
            reg.detalle = foto.getNombreFoto();
            reg.estado = foto.getEstadoEnvio(); // int: 0,1,2,3
            reg.idRealm = foto.getId(); // long
            registrosCombinados.add(reg);
        }
        
        // Agregar cuentas nuevas
        for (EnvioCuentaNueva cuenta : cuentasPendientes) {
            RegistroPendiente reg = new RegistroPendiente();
            reg.tipo = "C"; // Cuenta nueva
            reg.cuenta = cuenta.getContador();
            reg.detalle = cuenta.getDireccion();
            reg.estado = convertirEstadoString(cuenta.getEstadoEnvio()); // String a int
            reg.idRealmStr = cuenta.getIdRealm(); // String
            registrosCombinados.add(reg);
        }
    }
    
    /**
     * Convierte estado String (P, E, X) a int (0, 1, 2)
     */
    private int convertirEstadoString(String estado) {
        if (estado == null) return 0;
        switch (estado) {
            case "E": return 1; // Enviado
            case "X": return 2; // Error
            case "N": return 3; // No existe
            default: return 0;  // Pendiente
        }
    }

    /**
     * Muestra los registros en la tabla con paginación
     */
    private void mostrarRegistros() {
        tabla.removeAllViews();
        tabla.refreshDrawableState();

        try {
            int inicio = registroactual;
            int fin = Math.min(registroactual + registrolimite, totalGeneral);
            
            for (int i = inicio; i < fin; i++) {
                RegistroPendiente reg = registrosCombinados.get(i);
                llenarTabla(reg.cuenta, reg.detalle + " [" + reg.tipo + "]");
            }
            
            // Actualizar posición
            if (totalGeneral < 1) {
                lblpos.setText("0/0");
            } else {
                lblpos.setText((fin) + "/" + totalGeneral);
            }
            
        } catch (Exception ex) {
            Log.e("ModuloConsultaNoEnviados", "Error mostrando registros: " + ex.getMessage());
        }
    }

    /**
     * Navegación por la lista
     */
    private void navegar(String direccion) {
        switch (direccion) {
            case "primero":
                registroactual = 0;
                break;
            case "adelante":
                if (registroactual + registrolimite < totalGeneral) {
                    registroactual += registrolimite;
                }
                break;
            case "atras":
                registroactual -= registrolimite;
                if (registroactual < 0) {
                    registroactual = 0;
                }
                break;
            case "ultimo":
                registroactual = Math.max(0, totalGeneral - registrolimite);
                break;
        }
        mostrarRegistros();
    }

    /**
     * Llena una fila de la tabla (compatible con layout original)
     */
    public void llenarTabla(String cuenta, String nrocontador) {
        TableRow fila;
        TextView txtCuenta = new TextView(this);
        TextView txtNroContador = new TextView(this);

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        txtCuenta.setText(cuenta != null ? cuenta.trim() : "");
        txtCuenta.setGravity(Gravity.LEFT);
        txtCuenta.setTextAppearance(this, R.style.etiqueta);
        txtCuenta.setBackgroundResource(R.drawable.tabla_celda);
        txtCuenta.setLayoutParams(layoutCuenta);

        txtNroContador.setText(nrocontador != null ? nrocontador.trim() : "");
        txtNroContador.setGravity(Gravity.LEFT);
        txtNroContador.setTextAppearance(this, R.style.etiqueta);
        txtNroContador.setBackgroundResource(R.drawable.tabla_celda);
        txtNroContador.setLayoutParams(layoutContador);

        fila.addView(txtCuenta);
        fila.addView(txtNroContador);
        tabla.addView(fila);
    }

    /**
     * Agrega la cabecera de la tabla
     */
    public void agregarCabecera() {
        cabecera.removeAllViews();
        
        TableRow fila;
        TextView txtCuenta = new TextView(this);
        TextView txtNroContador = new TextView(this);

        fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);

        txtCuenta.setText("CUENTA");
        txtCuenta.setGravity(Gravity.CENTER_HORIZONTAL);
        txtCuenta.setTextAppearance(this, R.style.etiqueta);
        txtCuenta.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtCuenta.setLayoutParams(layoutCuenta);

        // Mostrar resumen en cabecera
        String resumen = String.format("L:%d F:%d C:%d", totalLecturas, totalFotos, totalCuentas);
        txtNroContador.setText(resumen);
        txtNroContador.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNroContador.setTextAppearance(this, R.style.etiqueta);
        txtNroContador.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtNroContador.setLayoutParams(layoutContador);

        fila.addView(txtCuenta);
        fila.addView(txtNroContador);
        cabecera.addView(fila);
    }

    /**
     * Vuelve a la actividad de liquidación
     */
    public void volveraLiquidacion() {
        Intent iBackActivity = new Intent(this, com.gstolima.accesoyseguridad.MenuDeLiquidacion.class);
        setResult(Activity.RESULT_OK, iBackActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        mensajes("inhabilitado");
    }

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(ModuloConsultaNoEnviados.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 50);
        toast.show();
    }

    /**
     * Clase auxiliar para representar un registro pendiente de cualquier tipo
     */
    private static class RegistroPendiente {
        String tipo;       // L=Lectura, F=Foto, C=Cuenta
        String cuenta;     // Número de cuenta o contador
        String detalle;    // Detalle adicional
        int estado;        // 0=Pendiente, 1=Enviado, 2=Error, 3=NoExiste
        long idRealm;      // ID en Realm (para Lectura/Foto)
        String idRealmStr; // ID en Realm (para Cuenta - es String)
    }
}
