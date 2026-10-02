package com.gstolima.modulochat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Chat extends AppCompatActivity {

    ImageButton btnVolver;
    ImageButton btnBorrar;
    File fullpath;
    ListView listview;
    List<String> array_list;
    String click_Mensaje;
    int click_Posicion;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnVolver = (ImageButton) findViewById(R.id.btnVolver);
        btnBorrar = (ImageButton) findViewById(R.id.btnBorrar);
        listview = (ListView) findViewById(R.id.listview);

        Bundle bundle = getIntent().getExtras();
        fullpath = new File(bundle.getString("DIRECTORIOFULL"));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                click_Mensaje = (String) listview.getAdapter().getItem(position);
                click_Posicion = position;
                pregunta("¿DESEA BUSCAR REGISTRO? \nEl número desaparecerá de esta lista", 1);
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pregunta("¿DESEA BORRAR TODO?", 2);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        llenarLista();
    }

    @Override
    public void onBackPressed() {

    }

    private void llenarListaanterior() {

        if (fullpath.exists()) {

            try {
                FileReader stream3 = new FileReader(fullpath);
                BufferedReader br = new BufferedReader(stream3);

                String linea;
                array_list = new ArrayList<String>();

                while ((linea = br.readLine()) != null) {

                    if (linea.length() != 0 && linea.contains("|")) {
                        String[] x = linea.split("\\|");

                        if (x[3].trim().equals("0")) {
                            array_list.add(linea);
                        }
                    }
                }
                br.close();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_list);
                listview.setAdapter(arrayAdapter);

            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    private void llenarLista() {

        if (fullpath.exists()) {

            try {
                FileReader stream3 = new FileReader(fullpath);
                BufferedReader br = new BufferedReader(stream3);

                String linea;
                array_list = new ArrayList<String>();

                while ((linea = br.readLine()) != null) {

                    if (linea.length() != 0 && linea.contains("|")) {
                        String[] x = linea.split("\\|");

                        if (x[3].trim().equals("0")) {
                            //array_list.add(linea);
                            array_list.add("CUENTA: " + x[0].trim() + ".\nFECHA: " + x[2].trim().replace(".", "") + ".\nMENSAJE:" + x[1].trim());
                        }
                    }
                }
                br.close();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_list);
                listview.setAdapter(arrayAdapter);

            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }
        }
    }


    /*private void procesarClick() {

        array_list.remove(click_Posicion);

        click_Mensaje = click_Mensaje.substring(0, click_Mensaje.indexOf("|")).trim();

        int x = 0;
        try {
            x = Integer.parseInt(click_Mensaje);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "ERROR cuenta No válida", Toast.LENGTH_SHORT).show();
        }

        if (fullpath.exists()) fullpath.delete();

        if (array_list.size() > 0) {
            for (String s : array_list) {
                utils.EscribirLinea(fullpath, s);
            }
        }

        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("id", x);
        setResult(RESULT_OK, iBackActivity);
        finish();
    }*/

    private void procesarClick() {
        try {
            //array_list.remove(click_Posicion);
            String[] spl_click = click_Mensaje.split("\\.");

            //Log.e("INFO", "antes click_Mensaje: " + click_Mensaje);
            click_Mensaje = click_Mensaje.substring(click_Mensaje.indexOf(":") + 1, click_Mensaje.indexOf(".")).trim();
            //Log.e("INFO", "despu click_Mensaje: |" + click_Mensaje + "|");
            int x = 0;
            x = Integer.parseInt(click_Mensaje);

            if (fullpath.exists()) fullpath.delete();
            if (!fullpath.exists()) fullpath.createNewFile();

            if (array_list.size() > 0) {
                for (String s : array_list) {
                    //Log.e("INFO","[Chat] procesarClick() s: " + s);
                    String[] spl_info = s.split("\\.");
                    String fecha_select = spl_click[1].substring( 7).trim();
                    String msg_select = spl_click[2].substring(spl_click[2].lastIndexOf(":") + 1);
                    /*Log.e("INFO","[Chat] procesarClick() cuenta: " + x);
                    Log.e("INFO","[Chat] procesarClick() fecha_select: " + fecha_select);
                    Log.e("INFO","[Chat] procesarClick() msg_select: " + msg_select);*/

                    long cuenta_info = Long.parseLong(spl_info[0].substring(spl_info[0].lastIndexOf(":") + 1).trim());
                    String fecha_info = spl_info[1].substring(7).trim();
                    String msg_info = spl_info[2].substring(spl_info[2].lastIndexOf(":") + 1);
                    /*Log.e("INFO","[Chat] procesarClick() cuenta_info: " + cuenta_info);
                    Log.e("INFO","[Chat] procesarClick() fecha_info: " + fecha_info);
                    Log.e("INFO","[Chat] procesarClick() msg_info: " + msg_info);*/

                    if (cuenta_info == x && fecha_info.equals(fecha_select)) {
                        utils.EscribirLinea(fullpath, cuenta_info + "|" + msg_info + "|" + fecha_info + ".|1\r\n");
                    }else{
                        utils.EscribirLinea(fullpath, cuenta_info + "|" + msg_info + "|" + fecha_info + ".|0\r\n");
                    }
                }
            }
            Log.e("INFO", "[Chat] procesarClick() x: " + x);

            Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
            iBackActivity.putExtra("id", x);
            setResult(RESULT_OK, iBackActivity);
            finish();
        } catch (Exception ex) {
            Log.e("ERROR","[Chat]procesarClick(): " + ex.getMessage());
            Toast.makeText(getApplicationContext(), "ERROR cuenta No válida", Toast.LENGTH_SHORT).show();
        }
    }

    private void pregunta(String msg, final int modo) {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Alerta!");
        builder2.setMessage(msg);

        builder2.setIcon(R.drawable.ic_launcher);

        builder2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                switch (modo) {

                    case 1:
                        procesarClick();
                        break;
                    case 2:
                        fullpath.delete();
                        finish();
                        break;
                }

            }
        });

        builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder2.show();
    }
}
