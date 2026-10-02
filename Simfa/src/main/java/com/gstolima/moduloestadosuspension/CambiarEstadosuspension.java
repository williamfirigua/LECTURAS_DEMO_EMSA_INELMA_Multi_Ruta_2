package com.gstolima.moduloestadosuspension;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;
import com.gstolima.tablas.TablaRegistroSalida;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CambiarEstadosuspension extends AppCompatActivity {

    TextView lblcategoria;

    ListView listadocambioestadosuspension;
    TablaRegistroSalida infoRegistroSalidaCeS = new TablaRegistroSalida();
    ArrayAdapter<String> item;

    String path;
    String nombreArchivoCes;

    int id;

    Button btncessave;
    Button btncesreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_estado_suspension);

        btncessave = (Button) findViewById(R.id.btncessave);
        btncesreturn = (Button) findViewById(R.id.btncesreturn);
        lblcategoria = (TextView) findViewById(R.id.lblcategoria);

        listadocambioestadosuspension = (ListView) findViewById(R.id.listadocambioestadosuspension);

        item = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listadocambioestadosuspension.setOnItemClickListener(itemClickListener);

        Bundle bundle = getIntent().getExtras();

        id = bundle.getInt("NumeroRegistroActual");
        path = bundle.getString("DirectorioActual");
        nombreArchivoCes = bundle.getString("NombreArchivos");
        infoRegistroSalidaCeS.setArchivo_TablaRegistroSalida(path + VariablesGlobales.getCarpetaLecturas()+"/" + "D" + nombreArchivoCes);

        llenarListadoAnomalias();
        VariablesGlobales.datodebusqueda = "";

        btncesreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btncessave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarEstadoSusp();
            }
        });
    }

    private void cambiarEstadoSusp() {

        try {
            String estado = lblcategoria.getText().toString().trim();

            if (estado.equals("")) {
                Toast.makeText(getApplicationContext(), "Debe escoger un Estado", Toast.LENGTH_LONG).show();
                return;
            }

            if (infoRegistroSalidaCeS.abrir_TablaRegistroSalida(infoRegistroSalidaCeS.getArchivo_TablaRegistroSalida())) {
                infoRegistroSalidaCeS.lectura_TablaRegistroSalida(id);
                infoRegistroSalidaCeS.settablaRegistroSalida_CODESTADO(estado);
                infoRegistroSalidaCeS.escribir_TablaRegistroSalida(id);
            }
            infoRegistroSalidaCeS.Cerrar_TablaRegistroSalida();
            Toast.makeText(getApplicationContext(), "Estado cambiado!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error al cambiar estado\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.modulo_de_anomalias, menu);
        return true;
    }

    private void llenarListadoAnomalias() {
        try {

            String ruta = path + VariablesGlobales.getCarpetaLecturas()+"/" + "ESTADOS.TXT";

            if (!new File(ruta).exists()) {
                Toast.makeText(getApplicationContext(), "NO existe archivo de Estados", Toast.LENGTH_LONG).show();
                finish();
            }

            listadocambioestadosuspension.setAdapter(null);

            try {
                FileReader r = new FileReader(ruta);
                BufferedReader reader = new BufferedReader(r);
                String linea;

                while ((linea = reader.readLine()) != null) {
                    item.add(linea.trim().replace(",", " "));
                }

                r.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            listadocambioestadosuspension.setAdapter(item);
        } catch (Exception e) {
            {
                Toast.makeText(getApplicationContext(), "Modulo de cambio con Problemas\n", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String codigo = ((TextView) v).getText().toString().trim().toUpperCase().substring(0, 1);

            lblcategoria.setText(codigo);
        }
    };
}
