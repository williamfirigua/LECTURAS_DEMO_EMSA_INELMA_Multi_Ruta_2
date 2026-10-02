package com.gstolima.modulocomentarios;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Util.Utils;
import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.R;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Firma extends AppCompatActivity {

    Bitmap DrawBitmap;
    Canvas mCanvas;
    Paint mPaint;
    Path mPath;
    Paint DrawBitmapPaint;
    RelativeLayout reloutmf;
    CustomView View;
    ImageView imvFirma;

    String archivoJpgFirma;
    String archivoFfotos;
    Bitmap mBitmapFromSdcard = BitmapFactory.decodeFile("");
    ImageButton btnmfErase, btnmfDelete, btnmfDraw, btnmfSave, volver;

    boolean dibujo = false;
    boolean firmo = false;
    boolean firmaEst = false;
    public int DrawBitmapWidth;
    public int DrawBitmapHeight;
    long idrevision;
    String id = "";
    String cuenta = "";
    String serialPDA = "";
    String archivoEFotos = VariablesGlobales.directorioactual + VariablesGlobales.CarpetaLecturas+"/" + "ENVIOFOTOS";
    EditText edttelefono;
    String selector = "TEL";
    RadioGroup rgBotones;
    RadioButton radio_tel;
    RadioButton radio_ced;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firma);

        btnmfErase = (ImageButton) findViewById(R.id.btnmfErase);
        btnmfDelete = (ImageButton) findViewById(R.id.btnmfDelete);
        btnmfDraw = (ImageButton) findViewById(R.id.btnmfDraw);
        btnmfSave = (ImageButton) findViewById(R.id.btngrabar);
        volver = (ImageButton) findViewById(R.id.btnVolver);
        reloutmf = (RelativeLayout) findViewById(R.id.reloutmf);
        imvFirma = (ImageView) findViewById(R.id.imvFirma);
        edttelefono = (EditText)findViewById(R.id.edttelefono);

        rgBotones = (RadioGroup) findViewById(R.id.rgBotones);
        radio_tel = (RadioButton) findViewById(R.id.radio_tel);
        radio_ced = (RadioButton) findViewById(R.id.radio_ced);

        Bundle bundle = getIntent().getExtras();
        archivoJpgFirma = bundle.getString("rutaFullFoto");
        archivoFfotos = bundle.getString("rutaFullFfile");
        id = bundle.getString("id");
        cuenta = bundle.getString("cuenta");
        serialPDA = bundle.getString("serialPDA");

        DrawBitmapWidth = 680;//no se puede capturar el tamaño de reloutmf
        DrawBitmapHeight = 300;
        Display display = getWindowManager().getDefaultDisplay();

        int screenHeight = display.getHeight();
        int screenWidth = display.getWidth();

        if(screenHeight >= 1400){
            DrawBitmapWidth = 1050;//no se puede capturar el tamaño de reloutmf
            DrawBitmapHeight = 500;
        }else{
            DrawBitmapWidth = 680;//no se puede capturar el tamaño de reloutmf
            DrawBitmapHeight = 300;
        }
        Log.e("error", "Alto de pantalla  = " + screenHeight);
        Log.e("error", "Ancho de pantalla = " + screenWidth);


        View = new CustomView(this);
        reloutmf.addView(View);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        btnmfErase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DrawBitmapWidth = reloutmf.getWidth();
                DrawBitmapHeight = reloutmf.getHeight();


                confirma("¿Usar Borrador Manual? ", 3);
            }
        });

        btnmfDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirma("¿Desea Borrar la Firma?\nDebe reescribir", 1);
            }
        });

        btnmfSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                opociones(4);
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (firmo) getSendData();
                else
                    Toast.makeText(getApplicationContext(), "DEBE FIRMAR Y GUARDAR!", Toast.LENGTH_LONG).show();
            }
        });

        trataCargarFirma();

        rgBotones.setOnCheckedChangeListener(new OnCheckedChangeListener() {


            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == radio_tel.getId()) {
                    selector = "TEL";
                    //"Tel";
                }
                if (checkedId == radio_ced.getId()) {
                    selector = "CED";
                    // "CED";
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
    }

    public void getSendData() {

        if(edttelefono.getText().toString().trim().equals("")){
            selector = "";
        }
        Log.e("error","selector "+selector);
        Intent iBackActivity = new Intent(this, MenuDeLiquidacion.class);
        iBackActivity.putExtra("telefono"," "+selector+" "+edttelefono.getText().toString());
        iBackActivity.putExtra("firmo", firmo);
        setResult(RESULT_OK, iBackActivity);
        finish();
    }

    private void trataCargarFirma() {

        if (new File(archivoJpgFirma).exists()) {
            firmo = true;
            Bitmap myBitmap = BitmapFactory.decodeFile(archivoJpgFirma);
            imvFirma.setImageBitmap(myBitmap);
        } else {
            imvFirma.setImageResource(android.R.color.transparent);
        }

    }

    public void opociones(int i) {

        mPaint.setXfermode(null);
        switch (i) {
            case 1://Borrador
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                break;

            case 2://delete
                View = new CustomView(this);
                dibujo = false;
                firmaEst = false;
                break;

            case 3://draw
                mPaint.setXfermode(null);
                break;

            case 4://save
                if (!dibujo) {
                    Toast.makeText(getApplicationContext(), "DEBE FIRMAR!", Toast.LENGTH_LONG).show();
                    break;
                }
                File file = new File(archivoJpgFirma);

                if (file.exists()) {
                    file.delete();
                }
                try {
                    DrawBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    ReduceImagen(archivoJpgFirma);
                    firmo = true;
                    Utils utils = new Utils();//celsia
                    if (!utils.EscribirLinea(new File(archivoFfotos), String.format("%1$-20s", archivoJpgFirma.substring(archivoJpgFirma.lastIndexOf("/") + 1 )) + ";X\r\n")) { //Aqui llena el archivo de fotos con el nombre de la foto tomada//'X'es no enviada
                        Toast.makeText(getApplicationContext(), "Problema Grabando Nombre de Foto", Toast.LENGTH_LONG).show();
                    }
                    String cadenaFoto =  String.format("%11s", id) + ";" + String.format("%9s",cuenta) + ";" + String.format("%1$-50s", archivoJpgFirma.substring(archivoJpgFirma.lastIndexOf("/") + 1 ))+ ";\r\n";
                    utils.EscribirLinea(new File(archivoEFotos+ serialPDA + ".SDA"),cadenaFoto);

                    Toast.makeText(getApplicationContext(), "Firma guardada:", Toast.LENGTH_LONG).show();
                    trataCargarFirma();
                    //antes no estaba aqui se anexo
                    getSendData();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "ERROR \n" + e.toString(), Toast.LENGTH_LONG).show();
                }
                break;

            case 5://delete
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                break;
        }
    }

    public void confirma(String mensaje, final int opc) {

        final AlertDialog.Builder alertConfCreado = new AlertDialog.Builder(this);
        alertConfCreado.setMessage(mensaje).setTitle("Alerta ")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (opc == 1) {
                            opociones(2);
                        }
                        if (opc == 2) {
                            opociones(3);
                        }
                        if (opc == 3) {
                            opociones(1);
                        }

                    }
                });
        AlertDialog alert = alertConfCreado.create();
        alert.show();
    }

    public String ReduceImagen(String filePath) {//, int calidad, int oWidth, int oHeight
        String err = "";

        try {
            File _filePath = new File(filePath);
            if (_filePath.isDirectory()) return "No es foto";

            if (!_filePath.getName().toUpperCase().contains(".PNG")) return "No es jpg";

            File file2 = new File(_filePath.getAbsolutePath().replace(".png", "temp.png"));

            try {
                InputStream in = new FileInputStream(_filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                bitmap = getResizedBitmap(bitmap, 300); // Todo: cambiar cuando la camara se coloca horizontal
                OutputStream out = new FileOutputStream(file2);

                try {
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 60, out)) {
                        {
                            file2.renameTo(_filePath); //Se borra el nuevo imagen y se le coloca el nombre de la original

                        }
                    } else {
                        return "Failed to save the image as a JPEG"; //throw new Exception("Failed to save the image as a JPEG");
                    }

                } catch (Exception ex) {
                    err = "" + ex.getMessage();
                } finally {
                    out.close();
                    in.close();
                    return err;
                }
            } catch (Exception ex) {
                err = "" + ex.getMessage();
                return err;
            }
        } catch (Exception ex) {
            err = "" + ex.getMessage();
            return err;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) { //TODO: esto esta en utils
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, 300, 300, true);
    }

    //------------------------------------------------------------------------------
    public class CustomView extends android.view.View {

        @SuppressWarnings("deprecation")
        public CustomView(Context c) {

            super(c);

            Log.e("error",reloutmf.getHeight()+"-firm3 "+reloutmf.getWidth());
            Log.e("error",DrawBitmapHeight+"-firm2 "+DrawBitmapWidth);
            //Display Disp = getWindowManager().getDefaultDisplay();
            DrawBitmap = Bitmap.createBitmap(DrawBitmapWidth, DrawBitmapHeight, Bitmap.Config.ARGB_8888);//Disp.getWidth(), Disp.getHeight()

            mCanvas = new Canvas(DrawBitmap);
            mPath = new Path();
            DrawBitmapPaint = new Paint(Paint.DITHER_FLAG);
            DrawBitmapPaint.setColor(Color.WHITE);
            mCanvas.drawColor(Color.WHITE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            setDrawingCacheEnabled(true);

            if (firmaEst) {
                canvas.drawBitmap(mBitmapFromSdcard, 0, 0, DrawBitmapPaint);
            } else {
                canvas.drawBitmap(DrawBitmap, 0, 0, DrawBitmapPaint);
            }
            canvas.drawPath(mPath, mPaint);
            canvas.drawRect(mY, 0, mY, 0, DrawBitmapPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {

            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {

            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            dibujo = true;
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_tel:
                if (checked)
                    selector = "TEL";
                    break;
            case R.id.radio_ced:
                if (checked)
                    selector = "CED";
                    break;
        }
    }



}

