package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Pantalla de captura propia. Reemplaza a la camara del sistema (antes lanzada via
 * MediaStore.ACTION_IMAGE_CAPTURE) unicamente para poder ofrecer flash controlado por la app
 * y un temporizador real (necesario para fotos tomadas con palo/monopie, donde nadie puede
 * pulsar el disparador).
 *
 * CONTRATO con el llamador (MenuDeLiquidacion.nwdispatchTakePictureIntent):
 *  - Recibe el Uri destino en el extra MediaStore.EXTRA_OUTPUT (el mismo que ya se usaba con
 *    la camara del sistema, no cambia).
 *  - Devuelve RESULT_OK cuando la foto quedo escrita en ese Uri, o RESULT_CANCELED en cualquier
 *    otro caso (cancelacion, error, permiso denegado).
 *  - No conoce ni depende de la logica de negocio de quien la invoca: onActivityResult,
 *    ejecutarProcesoDeFotoII, escribirTablasSalida, etc. siguen funcionando sin cambios.
 */
public class CamaraFotoActivity extends AppCompatActivity {

    private static final String TAG = "CamaraFotoActivity";
    private static final int REQUEST_CODE_PERMISOS = 4321;
    private static final int REQUEST_CODE_FALLBACK_SISTEMA = 4322;
    private static final int REQUEST_CODE_CAMARA_LEGACY = 4325;

    private static final String PREFS_NAME = "CamaraFotoPrefs";
    private static final String KEY_TIMER_SEGUNDOS = "timer_segundos";

    private PreviewView previewView;
    private ImageButton btnFlash;
    private ImageButton btnCancelar;
    private ImageButton btnCapturar;
    private RadioGroup rgTemporizador;
    private TextView txtCountdown;
    private Button btnZoom1x;
    private Button btnZoom2x;
    private ScaleGestureDetector scaleGestureDetector;

    private ImageCapture imageCapture;
    private Camera camera;
    private ProcessCameraProvider cameraProvider;
    private Uri imageUriDestino;
    private int temporizadorSegundos = 0;
    private boolean flashActivo = false;
    private CountDownTimer countDownTimer;

    private static final long TIMEOUT_CAPTURA_MS = 8000L;
    private final Handler handlerTimeout = new Handler(Looper.getMainLooper());
    private Runnable runnableTimeoutCaptura;
    private boolean callbackDeCapturaRecibido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_foto);

        imageUriDestino = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (imageUriDestino == null) {
            Toast.makeText(this, "No se recibio el destino de la imagen", Toast.LENGTH_SHORT).show();
            cancelarYSalir();
            return;
        }

        enlazarVistas();
        cargarTemporizadorGuardado();
        configurarListeners();

        if (esCamaraTraseraLegacy()) {
            // Hardware Camera2 nivel LEGACY confirmado (ej. Armor 13): CameraX se cuelga en
            // este equipo. Se delega a la version Camera1 directa, que es compatible con ese
            // nivel de hardware y mantiene temporizador, flash y zoom.
            Log.e(TAG, "onCreate() -> camara LEGACY detectada, delegando a CamaraFotoLegacyActivity");
            usarCamaraLegacy();
            return;
        }

        if (tienePermisoCamara()) {
            iniciarCamara();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISOS);
        }
    }

    /**
     * Determina si la camara trasera del equipo opera en nivel de hardware LEGACY
     * (el mas bajo de Camera2, usado como capa de compatibilidad sobre el Camera1 API viejo).
     * Confirmado por pruebas: en ese nivel, CameraX.ImageCapture.takePicture() se cuelga sin
     * lanzar excepcion en ciertos equipos PDA/industriales. Si no se puede determinar el nivel
     * por cualquier razon, se asume que NO es LEGACY y se deja que CameraX lo intente igual.
     */
    private boolean esCamaraTraseraLegacy() {
        try {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            if (cameraManager == null) return false;

            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics caracteristicas = cameraManager.getCameraCharacteristics(cameraId);
                Integer orientacion = caracteristicas.get(CameraCharacteristics.LENS_FACING);
                if (orientacion == null || orientacion != CameraCharacteristics.LENS_FACING_BACK) {
                    continue;
                }
                Integer nivelHardware = caracteristicas.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                return nivelHardware != null
                        && nivelHardware == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
            }
        } catch (Exception e) {
            Log.e(TAG, "esCamaraTraseraLegacy() -> no se pudo determinar el nivel de hardware", e);
        }
        return false;
    }

    private void enlazarVistas() {
        previewView = findViewById(R.id.previewView);
        btnFlash = findViewById(R.id.btnFlash);
        btnCancelar = findViewById(R.id.btnCancelarCaptura);
        btnCapturar = findViewById(R.id.btnCapturarFoto);
        rgTemporizador = findViewById(R.id.rgTemporizador);
        txtCountdown = findViewById(R.id.txtCountdown);
        btnZoom1x = findViewById(R.id.btnZoom1x);
        btnZoom2x = findViewById(R.id.btnZoom2x);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {
                if (camera == null) return true;
                ZoomState zoomState = camera.getCameraInfo().getZoomState().getValue();
                if (zoomState == null) return true;
                float zoomDeseado = zoomState.getZoomRatio() * detector.getScaleFactor();
                aplicarZoom(zoomDeseado);
                return true;
            }
        });
        previewView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });
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

    /**
     * Aplica un ratio de zoom, ajustandolo automaticamente al rango soportado por el
     * hardware (ZoomState.getMinZoomRatio()/getMaxZoomRatio()) para nunca lanzar excepcion
     * si el equipo no llega al zoom pedido (p.ej. un 2x en un lente sin zoom optico real).
     */
    private void aplicarZoom(float ratioDeseado) {
        if (camera == null) return;
        ZoomState zoomState = camera.getCameraInfo().getZoomState().getValue();
        if (zoomState == null) return;

        float ratioFinal = Math.max(zoomState.getMinZoomRatio(),
                Math.min(ratioDeseado, zoomState.getMaxZoomRatio()));
        camera.getCameraControl().setZoomRatio(ratioFinal);
    }

    private int segundosPorOpcion(int checkedId) {
        if (checkedId == R.id.rbTemporizador3s) return 3;
        if (checkedId == R.id.rbTemporizador5s) return 5;
        if (checkedId == R.id.rbTemporizador10s) return 10;
        return 0; // R.id.rbTemporizadorOff
    }

    /** Recupera la ultima seleccion de temporizador guardada (persistente entre sesiones). */
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarCamara();
            } else {
                Toast.makeText(this, "Se requiere permiso de camara para tomar la foto", Toast.LENGTH_LONG).show();
                cancelarYSalir();
            }
        }
    }

    private void iniciarCamara() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                vincularCasosDeUso(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error inicializando CameraX", e);
                Toast.makeText(this, "No se pudo iniciar la camara", Toast.LENGTH_SHORT).show();
                cancelarYSalir();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void vincularCasosDeUso(ProcessCameraProvider cameraProvider) {
        this.cameraProvider = cameraProvider;

        // Forzar el mismo aspect ratio en Preview e ImageCapture: en camaras Camera2 nivel
        // LEGACY el numero de combinaciones de streams concurrentes validas es muy limitado,
        // y dejar que cada caso de uso elija su propia resolucion por defecto puede generar
        // una combinacion no soportada por el HAL (se cuelga sin lanzar excepcion).
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(flashActivo ? ImageCapture.FLASH_MODE_ON : ImageCapture.FLASH_MODE_OFF)
                .build();

        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    /**
     * Alterna el flash. Controla dos cosas: el modo de disparo (flash en el instante de la foto)
     * y el torch continuo (ilumina mientras se encuadra), util para medidores en sitios oscuros.
     */
    private void alternarFlash() {
        flashActivo = !flashActivo;

        if (imageCapture != null) {
            imageCapture.setFlashMode(flashActivo ? ImageCapture.FLASH_MODE_ON : ImageCapture.FLASH_MODE_OFF);
        }
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            camera.getCameraControl().enableTorch(flashActivo);
        }
        btnFlash.setImageResource(flashActivo ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
    }

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
                long segundoActual = (millisUntilFinished / 1000L) + 1L;
                txtCountdown.setText(String.valueOf(segundoActual));
            }

            @Override
            public void onFinish() {
                txtCountdown.setVisibility(View.GONE);
                tomarFoto();
            }
        };
        countDownTimer.start();
    }

    /**
     * Respaldo para hardware incompatible con CameraX (camaras Camera2 nivel LEGACY que
     * cuelgan el pipeline de captura). Libera la camara de CameraX y delega a la camara
     * del sistema, igual que se hacia antes de este cambio. Se pierde el temporizador y el
     * control de flash propio en ese caso puntual, pero el operario nunca se queda sin poder
     * tomar la foto.
     */
    private void usarCamaraDelSistemaComoFallback() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        Intent intentSistema = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentSistema.putExtra(MediaStore.EXTRA_OUTPUT, imageUriDestino);
        startActivityForResult(intentSistema, REQUEST_CODE_FALLBACK_SISTEMA);
    }

    private void usarCamaraLegacy() {
        Intent intentLegacy = new Intent(this, CamaraFotoLegacyActivity.class);
        intentLegacy.putExtra(MediaStore.EXTRA_OUTPUT, imageUriDestino);
        startActivityForResult(intentLegacy, REQUEST_CODE_CAMARA_LEGACY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FALLBACK_SISTEMA || requestCode == REQUEST_CODE_CAMARA_LEGACY) {
            setResult(resultCode);
            finish();
        }
    }

    private void tomarFoto() {
        if (imageCapture == null) {
            Log.e(TAG, "tomarFoto() -> imageCapture es null, la camara no termino de enlazarse");
            Toast.makeText(this, "La camara no esta lista", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
            return;
        }

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                imageUriDestino,
                new ContentValues()
        ).build();

        callbackDeCapturaRecibido = false;
        Log.e(TAG, "tomarFoto() -> invocando takePicture()...");

        // Salvaguarda: si el HAL de la camara nunca contesta (visto en algunos PDA/terminales
        // industriales con Camera2 nivel LEGACY), esto evita que la pantalla quede colgada
        // esperando para siempre.
        runnableTimeoutCaptura = () -> {
            if (!callbackDeCapturaRecibido) {
                Log.e(TAG, "tomarFoto() -> TIMEOUT: takePicture() nunca invoco el callback en "
                        + TIMEOUT_CAPTURA_MS + "ms. Cayendo a camara del sistema como respaldo.");
                Toast.makeText(CamaraFotoActivity.this,
                        "Este dispositivo no soporta la camara mejorada, abriendo camara basica",
                        Toast.LENGTH_LONG).show();
                usarCamaraDelSistemaComoFallback();
            }
        };
        handlerTimeout.postDelayed(runnableTimeoutCaptura, TIMEOUT_CAPTURA_MS);

        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        callbackDeCapturaRecibido = true;
                        handlerTimeout.removeCallbacks(runnableTimeoutCaptura);
                        Log.e(TAG, "tomarFoto() -> onImageSaved() OK");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        callbackDeCapturaRecibido = true;
                        handlerTimeout.removeCallbacks(runnableTimeoutCaptura);
                        Log.e(TAG, "tomarFoto() -> onError() " + exception.getMessage(), exception);
                        Toast.makeText(CamaraFotoActivity.this,
                                "Error al tomar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
                        btnCapturar.setEnabled(true);
                    }
                }
        );
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
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (runnableTimeoutCaptura != null) {
            handlerTimeout.removeCallbacks(runnableTimeoutCaptura);
        }
        super.onDestroy();
    }
}
