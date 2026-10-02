package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Pantalla de captura para hardware Camera2 nivel LEGACY (confirmado en equipos Armor 13 y
 * similares): en ese hardware, CameraX.ImageCapture.takePicture() se cuelga sin lanzar
 * excepcion. Esta version usa la API vieja Camera1 directamente, que es la misma capa que
 * CameraX termina usando por debajo en hardware LEGACY, y por eso es compatible donde CameraX
 * no lo es.
 *
 * CONTRATO con el llamador (CamaraFotoActivity.usarCamaraLegacy / MenuDeLiquidacion): identico
 * al de CamaraFotoActivity -> recibe el Uri en MediaStore.EXTRA_OUTPUT, devuelve RESULT_OK con
 * la foto ya escrita en ese Uri, o RESULT_CANCELED en cualquier otro caso.
 */
public class CamaraFotoLegacyActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = "CamaraFotoLegacyActivity";
    private static final int REQUEST_CODE_PERMISOS = 4323;
    private static final int REQUEST_CODE_FALLBACK_SISTEMA = 4324;

    private static final String PREFS_NAME = "CamaraFotoPrefs";
    private static final String KEY_TIMER_SEGUNDOS = "timer_segundos";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton btnFlash;
    private ImageButton btnCancelar;
    private ImageButton btnCapturar;
    private Button btnZoom1x;
    private Button btnZoom2x;
    private RadioGroup rgTemporizador;
    private TextView txtCountdown;

    private Camera camera;
    private int cameraId = -1;
    private Uri imageUriDestino;
    private int temporizadorSegundos = 0;
    private boolean flashActivo = false;
    private boolean superficieLista = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_foto_legacy);

        imageUriDestino = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (imageUriDestino == null) {
            Toast.makeText(this, "No se recibio el destino de la imagen", Toast.LENGTH_SHORT).show();
            cancelarYSalir();
            return;
        }

        enlazarVistas();
        cargarTemporizadorGuardado();
        configurarListeners();

        surfaceHolder = surfaceView.getHolder();

        if (tienePermisoCamara()) {
            surfaceHolder.addCallback(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISOS);
        }
    }

    private void enlazarVistas() {
        surfaceView = findViewById(R.id.surfaceViewCamara);
        btnFlash = findViewById(R.id.btnFlash);
        btnCancelar = findViewById(R.id.btnCancelarCaptura);
        btnCapturar = findViewById(R.id.btnCapturarFoto);
        rgTemporizador = findViewById(R.id.rgTemporizador);
        txtCountdown = findViewById(R.id.txtCountdown);
        btnZoom1x = findViewById(R.id.btnZoom1x);
        btnZoom2x = findViewById(R.id.btnZoom2x);
    }

    private void configurarListeners() {
        btnCancelar.setOnClickListener(v -> cancelarYSalir());
        btnFlash.setOnClickListener(v -> alternarFlash());
        btnCapturar.setOnClickListener(v -> iniciarSecuenciaDeCaptura());
        btnZoom1x.setOnClickListener(v -> aplicarZoom(1.0f));
        btnZoom2x.setOnClickListener(v -> aplicarZoom(2.0f));

        rgTemporizador.setOnCheckedChangeListener((group, checkedId) -> {
            temporizadorSegundos = segundosPorOpcion(checkedId);
            guardarTemporizadorSeleccionado(temporizadorSegundos);
        });
    }

    private int segundosPorOpcion(int checkedId) {
        if (checkedId == R.id.rbTemporizador3s) return 3;
        if (checkedId == R.id.rbTemporizador5s) return 5;
        if (checkedId == R.id.rbTemporizador10s) return 10;
        return 0;
    }

    private void cargarTemporizadorGuardado() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        temporizadorSegundos = prefs.getInt(KEY_TIMER_SEGUNDOS, 0);

        int idAMarcar;
        if (temporizadorSegundos == 3) {
            idAMarcar = R.id.rbTemporizador3s;
        } else if (temporizadorSegundos == 5) {
            idAMarcar = R.id.rbTemporizador5s;
        } else if (temporizadorSegundos == 10) {
            idAMarcar = R.id.rbTemporizador10s;
        } else {
            idAMarcar = R.id.rbTemporizadorOff;
        }
        rgTemporizador.check(idAMarcar);
    }

    private void guardarTemporizadorSeleccionado(int segundos) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putInt(KEY_TIMER_SEGUNDOS, segundos)
                .apply();
    }

    private boolean tienePermisoCamara() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, androidx.annotation.NonNull String[] permissions, androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder.addCallback(this);
            } else {
                Toast.makeText(this, "Se requiere permiso de camara para tomar la foto", Toast.LENGTH_LONG).show();
                cancelarYSalir();
            }
        }
    }

    // ---------------------------------------------------------------- SurfaceHolder.Callback

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        superficieLista = true;
        abrirCamara();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            configurarPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        superficieLista = false;
        liberarCamara();
    }

    // ---------------------------------------------------------------- Ciclo de la camara

    private int encontrarCamaraTrasera() {
        int numCamaras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < numCamaras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return -1;
    }

    private void abrirCamara() {
        cameraId = encontrarCamaraTrasera();
        if (cameraId == -1) {
            Log.e(TAG, "abrirCamara() -> no se encontro camara trasera");
            usarCamaraDelSistemaComoUltimoRecurso();
            return;
        }
        try {
            camera = Camera.open(cameraId);
            configurarPreview();
        } catch (Exception e) {
            // Ultimo recurso: si ni siquiera Camera1 puede abrir el hardware, se delega
            // a la app de camara del sistema, igual que en CamaraFotoActivity.
            Log.e(TAG, "abrirCamara() -> no se pudo abrir la camara con Camera1", e);
            usarCamaraDelSistemaComoUltimoRecurso();
        }
    }

    private void configurarPreview() {
        try {
            camera.setPreviewDisplay(surfaceHolder);

            Camera.Parameters params = camera.getParameters();

            Camera.Size mejorTamano = elegirMejorTamanoFoto(params.getSupportedPictureSizes());
            if (mejorTamano != null) {
                params.setPictureSize(mejorTamano.width, mejorTamano.height);
            }

            List<String> flashesSoportados = params.getSupportedFlashModes();
            if (flashesSoportados != null && flashesSoportados.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                params.setFlashMode(flashActivo ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
            }

            camera.setParameters(params);
            camera.setDisplayOrientation(calcularRotacionPreview());
            camera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "configurarPreview() -> error configurando la camara", e);
            Toast.makeText(this, "Error iniciando la vista de camara", Toast.LENGTH_SHORT).show();
        }
    }

    private Camera.Size elegirMejorTamanoFoto(List<Camera.Size> tamanos) {
        if (tamanos == null || tamanos.isEmpty()) return null;
        Camera.Size mejor = tamanos.get(0);
        for (Camera.Size tamano : tamanos) {
            if ((long) tamano.width * tamano.height > (long) mejor.width * mejor.height) {
                mejor = tamano;
            }
        }
        return mejor;
    }

    /** Calcula la rotacion necesaria para que el preview no se vea de lado o invertido. */
    private int calcularRotacionPreview() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int gradosPantalla;
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90: gradosPantalla = 90; break;
            case Surface.ROTATION_180: gradosPantalla = 180; break;
            case Surface.ROTATION_270: gradosPantalla = 270; break;
            default: gradosPantalla = 0;
        }

        int resultado;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            resultado = (info.orientation + gradosPantalla) % 360;
            resultado = (360 - resultado) % 360;
        } else {
            resultado = (info.orientation - gradosPantalla + 360) % 360;
        }
        return resultado;
    }

    private void liberarCamara() {
        if (camera != null) {
            try {
                camera.stopPreview();
            } catch (Exception ignored) {
                // stopPreview() puede lanzar si nunca llego a arrancar; no es relevante al liberar.
            }
            camera.release();
            camera = null;
        }
    }

    // ---------------------------------------------------------------- Flash / Zoom

    private void alternarFlash() {
        flashActivo = !flashActivo;
        if (camera != null) {
            try {
                Camera.Parameters params = camera.getParameters();
                List<String> flashesSoportados = params.getSupportedFlashModes();
                if (flashesSoportados != null && flashesSoportados.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                    params.setFlashMode(flashActivo ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                }
            } catch (Exception e) {
                Log.e(TAG, "alternarFlash() -> error", e);
            }
        }
        btnFlash.setImageResource(flashActivo ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
    }

    /**
     * Camera1 no maneja ratios de zoom en float como CameraX; usa un indice dentro de una
     * lista de ratios soportados (getZoomRatios(), valores x100: 100 = 1x, 200 = 2x, etc).
     * Se busca el indice cuyo ratio quede mas cerca del solicitado.
     */
    private void aplicarZoom(float ratioDeseado) {
        if (camera == null) return;
        try {
            Camera.Parameters params = camera.getParameters();
            if (!params.isZoomSupported()) return;

            List<Integer> ratiosSoportados = params.getZoomRatios();
            int maxZoomIndex = params.getMaxZoom();
            int ratioObjetivoX100 = Math.round(ratioDeseado * 100);

            int indiceElegido = 0;
            int mejorDiferencia = Integer.MAX_VALUE;
            for (int i = 0; i <= maxZoomIndex && i < ratiosSoportados.size(); i++) {
                int diferencia = Math.abs(ratiosSoportados.get(i) - ratioObjetivoX100);
                if (diferencia < mejorDiferencia) {
                    mejorDiferencia = diferencia;
                    indiceElegido = i;
                }
            }

            params.setZoom(indiceElegido);
            camera.setParameters(params);
        } catch (Exception e) {
            Log.e(TAG, "aplicarZoom() -> error", e);
        }
    }

    // ---------------------------------------------------------------- Captura

    private void iniciarSecuenciaDeCaptura() {
        btnCapturar.setEnabled(false);

        if (temporizadorSegundos <= 0) {
            tomarFoto();
            return;
        }

        txtCountdown.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(temporizadorSegundos * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCountdown.setText(String.valueOf((millisUntilFinished / 1000L) + 1L));
            }

            @Override
            public void onFinish() {
                txtCountdown.setVisibility(View.GONE);
                tomarFoto();
            }
        };
        countDownTimer.start();
    }

    private void tomarFoto() {
        if (camera == null) {
            Toast.makeText(this, "La camara no esta lista", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
            return;
        }
        try {
            camera.takePicture(null, null, jpegCallback);
        } catch (Exception e) {
            Log.e(TAG, "tomarFoto() -> error al invocar takePicture()", e);
            Toast.makeText(this, "Error al tomar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
        }
    }

    private final Camera.PictureCallback jpegCallback = (data, camaraQueDisparo) -> {
        try (OutputStream salida = getContentResolver().openOutputStream(imageUriDestino)) {
            if (salida == null) {
                throw new IOException("No se pudo abrir el OutputStream del Uri destino");
            }
            salida.write(data);
            salida.flush();
            setResult(Activity.RESULT_OK);
            finish();
        } catch (IOException e) {
            Log.e(TAG, "jpegCallback -> error al escribir la foto en el Uri destino", e);
            Toast.makeText(this, "Error al guardar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
            try {
                camaraQueDisparo.startPreview(); // reanudar preview para permitir reintentar
            } catch (Exception ignored) {
            }
        }
    };

    // ---------------------------------------------------------------- Salida / respaldo

    private void usarCamaraDelSistemaComoUltimoRecurso() {
        Toast.makeText(this, "No se pudo iniciar la camara, abriendo camara basica", Toast.LENGTH_LONG).show();
        Intent intentSistema = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentSistema.putExtra(MediaStore.EXTRA_OUTPUT, imageUriDestino);
        startActivityForResult(intentSistema, REQUEST_CODE_FALLBACK_SISTEMA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FALLBACK_SISTEMA) {
            setResult(resultCode);
            finish();
        }
    }

    private void cancelarYSalir() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelarYSalir();
    }

    @Override
    protected void onPause() {
        liberarCamara();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null && superficieLista) {
            abrirCamara();
        }
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
