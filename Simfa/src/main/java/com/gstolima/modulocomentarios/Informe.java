package com.gstolima.modulocomentarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
public class Informe extends AppCompatActivity {

    public String directorioActual = "";
    public int registroActual = 0;
    public String Resumen = "";
    public String INFORMECAUSA = "";
    Button bntguardarInf;
    EditText txtInforme;
    ListView listadoinformes;
    TextView lblcategoria;
    ArrayAdapter<String> item;
    boolean ismenu;
    //microfono
    ImageButton imgbtnhablar;
    static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    Button bntSinInf;
    int registroA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_informe);

        listadoinformes = (ListView) findViewById(R.id.listadoinformes);
        bntguardarInf = (Button) findViewById(R.id.bntguardarInf);
        txtInforme = (EditText) findViewById(R.id.txtInforme);
        lblcategoria = (TextView) findViewById(R.id.lblcategoria);
        //microfono
        imgbtnhablar = (ImageButton) findViewById(R.id.imgbtnhablar);
        bntSinInf  = (Button) findViewById(R.id.bntSinInf);

        item = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        Bundle bundle = getIntent().getExtras();
        directorioActual = bundle.getString("DIRECTORIOACTUAL");
        registroActual = bundle.getInt("NUMEROREGISTROACTUAL");
        Resumen = bundle.getString("RESUMEN");
        ismenu  = bundle.getBoolean("ISMENU");
        registroA = bundle.getInt("registroActual");
        INFORMECAUSA = bundle.getString("INFORMECAUSA");
        listadoinformes.setOnItemClickListener(itemClickListener);
        cargarCategorias();
        txtInforme.setText(INFORMECAUSA);
       /* if (Resumen.trim().length() > 4) {

            try {

                String[] datos = Resumen.split(";");

                String lecturaActualStr = datos[0].trim();
                String causalActual = datos[1].trim();
                String causalAnterior = datos[2].trim();
                String lecturaAnteriorStr = datos[3].trim();

                int lecturaActual = parseStringToInteger(lecturaActualStr);
                int lecturaAnterior = parseStringToInteger(lecturaAnteriorStr);

                StringBuilder informe = new StringBuilder();

                // =========================
                // VALIDAR DIFERENCIA LECTURAS
                // =========================

                if (lecturaActual > 0 && lecturaAnterior > 0) {

                    int diferencia =
                            Math.abs(lecturaActual - lecturaAnterior);

                    if (diferencia > 20) {

                        informe.append(
                                "Lectura actual "+ lecturaActualStr + " muy diferente a la anterior. "+lecturaAnteriorStr);
                    }
                }

                // =========================
                // VALIDAR CAUSALES DIFERENTES
                // =========================

                if (causalActual.length() > 0 &&
                        causalAnterior.length() > 0 &&
                        !causalActual.equals(causalAnterior)) {

                    informe.append(
                            "Causales diferentes. "+causalActual);
                }

                // =========================
                // ANTES HABIA CAUSAL Y AHORA LECTURA
                // =========================

                if (causalAnterior.length() > 0 &&
                        lecturaActual > 0 &&
                        causalActual.length() == 0) {

                    informe.append(
                            "Antes existía causal y ahora hay lectura. ");
                }

                // =========================
                // AHORA HAY CAUSAL Y ANTES LECTURA
                // =========================

                if (lecturaAnterior > 0 &&
                        causalActual.length() > 0) {

                    informe.append(
                            "Antes existía lectura y ahora causal. ");
                }

                // =========================
                // SIN NOVEDADES
                // =========================

                if (informe.length() == 0) {

                    informe.append("Sin novedades detectadas.");
                }

                //txtInforme.setText(informe.toString());

                txtInforme.setText(INFORMECAUSA);

            } catch (Exception e) {

                txtInforme.setText(
                        "Error procesando resumen.");
            }
        }*/
        bntguardarInf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                regresoaMenuLiquidacion();
            }
        });

        txtInforme.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 6 || actionId == 5) { //Ax "tecla ENTER" telefono chino ax = 5 otro = 6
                    regresoaMenuLiquidacion();
                }
                return false;
            }
        });

        txtInforme.requestFocus();

        //microfono
        imgbtnhablar.setOnClickListener(new View.OnClickListener() { ///8888888
            public void onClick(View v) {
                Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
                try {
                    startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
                } catch (ActivityNotFoundException a) {
                    mensajes("Error con Microfono");
                }
            }
        });

        txtInforme.setEnabled(true);

        bntSinInf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sinInforme();
            }
        });
    }

    @Override
    public void onBackPressed() {
        salir();
    }
    public int parseStringToInteger(String x) {

        try {

            if (x == null)
                return 0;

            x = x.trim();

            if (x.isEmpty())
                return 0;

            if (!x.matches("-?\\d+(\\.\\d+)?")) {
               // logger.info("Valor no numérico: " + x);
                return 0;
            }

            if (x.contains(".")) {
                return (int) parseStringToDouble(x);
            }

            return Integer.parseInt(x);

        } catch (Exception e) {

           // logger.info(e.getMessage());
            return 0;
        }
    }
    public double parseStringToDouble(String x) {
        try {
            double y = Double.parseDouble(x.trim());
            return y;
        } catch (NumberFormatException e) {
            //logger.info("parseStringToDouble() " + e.getMessage());
            throw new RuntimeException("Error.Double parseo " + x);
        } catch (Exception e) {
            //logger.info("parseStringToDouble(). " + e.getMessage());
            throw new RuntimeException("Error.Double parseo " + x);
        }
    }
    public void regresoaMenuLiquidacion() {

        String info = txtInforme.getText().toString().trim();

        if (info.length() < 11) {
            mensajes("El informe debe tener mas de 10 caracteres");
            return;
        }
        Log.e("error","activity informe");
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("info", info);
        iBackActivity.putExtra("catg", lblcategoria.getText().toString().trim());
        iBackActivity.putExtra("registroActual", registroA+"");
        setResult(RESULT_OK, iBackActivity);
        finish();
    }

    private void salir() {

        if (!ismenu) {
            mensajes("Debe terminar el informe");
            return;
        }
//nuevo para retornar y cerrar teclado
        View view1= findViewById(R.id.bntSinInf);
        InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Capturar Informe");
        builder.setMessage("¿Desea salir del Informe?");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void cargarCategorias() {
        try {
            String nombreArchivo = directorioActual + VariablesGlobales.getCarpetaLecturas()+"/CATEGORIASINF.TXT";
            File file = new File(nombreArchivo);

            if (!file.exists()) {
                mensajes("No hay archivo de categorias!");
                String[] datos1;
                item.add("00" + " " + "Categorias No Existen");
                listadoinformes.setAdapter(item);
            //    finish();
            }
            else {
                int cont = 0;

                FileReader fr = new FileReader(nombreArchivo);
                BufferedReader reader = new BufferedReader(fr);
                String linea;
                String[] datos;

                while ((linea = reader.readLine()) != null) {

                    if (linea.trim().equals("")) continue;

                    datos = linea.split(",");
                    item.add(datos[0] + " " + datos[1].trim());
                    cont++;
                }
                reader.close();

                if (cont < 1) {
                    mensajes("No hay datos en el archivo de categorias!");
                    item.add("00" + " " + "Categorias Sin datos");
                    // finish();
                }
                listadoinformes.setAdapter(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            txtInforme.setEnabled(true);
            String b = ((TextView) v).getText().toString();
            lblcategoria.setText(b.substring(0, 2));
        }
    };

    private void mensajes(String msg) {
        Toast toast = Toast.makeText(Informe.this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 10, 170);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:

                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = txtInforme.getText().toString();
                    //EditText donde vas a mostrar el texto capturado
                    txtInforme.setText(strSpeech2Text + " " + speech.get(0));
                }
                break;
            default:
                break;
        }
    }

    public void sinInforme() {
        View view1= findViewById(R.id.bntSinInf);
        InputMethodManager imm = (InputMethodManager) view1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        Log.e("error","activity informe");
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("info", "");
        iBackActivity.putExtra("catg", lblcategoria.getText().toString().trim());
        iBackActivity.putExtra("registroActual", registroA+"");
        setResult(RESULT_OK, iBackActivity);
        finish();
    }
}
