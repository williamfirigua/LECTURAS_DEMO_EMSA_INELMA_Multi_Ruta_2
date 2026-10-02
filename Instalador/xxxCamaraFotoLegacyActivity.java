package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
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
    private static final String KEY_FLASH_ACTIVO = "flash_activo";

    // Techo de resolucion para la foto: 1920x1080 (~2MP) es de sobra para leer digitos de un
    // medidor, y reduce notablemente el tiempo de codificacion JPEG + escritura, que es la
    // parte del pipeline que si podemos controlar por software.
    private static final int MAX_PIXELES_FOTO = 1920 * 1080;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton btnFlash;
    private ImageButton btnCancelar;
    private ImageButton btnCapturar;
    private Button btnZoom1x;
    private Button btnZoom2x;
    private RadioGroup rgTemporizador;
    private TextView txtCountdown;
    private View panelRevision;
    private ImageView imgRevisionFoto;
    private ImageButton btnAceptarFoto;
    private ImageButton btnRepetirFoto;

    private Camera camera;
    private int cameraId = -1;
    private Uri imageUriDestino;
    private int temporizadorSegundos = 0;
    private boolean flashActivo = false;
    private boolean superficieLista = false;
    private CountDownTimer countDownTimer;
    private OrientationEventListener orientationEventListener;
    private int orientacionDispositivoActual = 0; // 0/90/180/270, segun el sensor, independiente del lock de pantalla

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
        cargarFlashGuardado();
        configurarListeners();
        inicializarListenerDeOrientacion();

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
        panelRevision = findViewById(R.id.panelRevision);
        imgRevisionFoto = findViewById(R.id.imgRevisionFoto);
        btnAceptarFoto = findViewById(R.id.btnAceptarFoto);
        btnRepetirFoto = findViewById(R.id.btnRepetirFoto);
    }

    private void configurarListeners() {
        btnCancelar.setOnClickListener(v -> cancelarYSalir());
        btnFlash.setOnClickListener(v -> alternarFlash());
        btnCapturar.setOnClickListener(v -> iniciarSecuenciaDeCaptura());
        btnZoom1x.setOnClickListener(v -> aplicarZoom(1.0f));
        btnZoom2x.setOnClickListener(v -> aplicarZoom(2.0f));
        btnAceptarFoto.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
        btnRepetirFoto.setOnClickListener(v -> volverACapturar());

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

    /** Recupera el ultimo estado del flash (encendido/apagado) para no tener que reactivarlo cada vez. */
    private void cargarFlashGuardado() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        flashActivo = prefs.getBoolean(KEY_FLASH_ACTIVO, false);
        btnFlash.setImageResource(flashActivo ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
    }

    private void guardarFlashActivo(boolean activo) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_FLASH_ACTIVO, activo)
                .apply();
    }

    private boolean tienePermisoCamara() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
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

    /**
     * Escucha la orientacion fisica real del dispositivo (via sensor), independiente de que
     * la Activity este fija en portrait. Sin esto, la foto capturada con Camera1 siempre sale
     * rotada segun la orientacion "de fabrica" de la app, sin importar como se sostiene el
     * equipo al momento de disparar.
     */
    private void inicializarListenerDeOrientacion() {
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == ORIENTATION_UNKNOWN) return;

                if (orientation >= 315 || orientation < 45) {
                    orientacionDispositivoActual = 0;
                } else if (orientation < 135) {
                    orientacionDispositivoActual = 90;
                } else if (orientation < 225) {
                    orientacionDispositivoActual = 180;
                } else {
                    orientacionDispositivoActual = 270;
                }
            }
        };
    }

    /** Rotacion que hay que aplicarle al JPEG para que salga como el usuario sostuvo el equipo. */
    private int calcularRotacionFoto() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotacion;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotacion = (info.orientation + orientacionDispositivoActual) % 360;
            rotacion = (360 - rotacion) % 360;
        } else {
            rotacion = (info.orientation - orientacionDispositivoActual + 360) % 360;
        }
        return rotacion;
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
            params.setJpegQuality(90); // 100 no aporta nada visible para lectura de medidores y es mas lento de codificar

            List<String> flashesSoportados = params.getSupportedFlashModes();
            if (flashesSoportados != null && flashesSoportados.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                params.setFlashMode(flashActivo ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
            }

            // Sin esto la camara nunca enfoca: se queda en el modo que traiga por defecto
            // (a veces fijo), produciendo fotos borrosas sin importar que tan buena sea la luz.
            // CONTINUOUS_PICTURE reenfoca solo mientras se encuadra; si no esta soportado, se
            // usa AUTO y se dispara un autoFocus() explicito justo antes de cada captura.
            List<String> focosSoportados = params.getSupportedFocusModes();
            if (focosSoportados != null && focosSoportados.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focosSoportados != null && focosSoportados.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            camera.setParameters(params);
            camera.setDisplayOrientation(calcularRotacionPreview());
            camera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "configurarPreview() -> error configurando la camara", e);
            Toast.makeText(this, "Error iniciando la vista de camara", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Elige la mayor resolucion disponible que no supere MAX_PIXELES_FOTO. Usar siempre la
     * resolucion maxima del sensor (varios equipos superan 12-16MP) es la principal causa de
     * lentitud en la captura: mas pixeles = mas tiempo de codificacion JPEG y de escritura.
     * Si por algun motivo ninguna resolucion cabe en el limite, se usa la mas grande disponible.
     */
    private Camera.Size elegirMejorTamanoFoto(List<Camera.Size> tamanos) {
        if (tamanos == null || tamanos.isEmpty()) return null;

        Camera.Size mayorGeneral = tamanos.get(0);
        Camera.Size mejorDentroDelLimite = null;

        for (Camera.Size tamano : tamanos) {
            long pixeles = (long) tamano.width * tamano.height;

            if (pixeles > (long) mayorGeneral.width * mayorGeneral.height) {
                mayorGeneral = tamano;
            }

            if (pixeles <= MAX_PIXELES_FOTO
                    && (mejorDentroDelLimite == null
                        || pixeles > (long) mejorDentroDelLimite.width * mejorDentroDelLimite.height)) {
                mejorDentroDelLimite = tamano;
            }
        }

        return mejorDentroDelLimite != null ? mejorDentroDelLimite : mayorGeneral;
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
        guardarFlashActivo(flashActivo);
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
            Camera.Parameters params = camera.getParameters();
            params.setRotation(calcularRotacionFoto());
            camera.setParameters(params);

            // autoFocus() explicito antes de disparar: incluso en CONTINUOUS_PICTURE, esto
            // fuerza la convergencia final justo antes de la captura y evita disparar a mitad
            // de un reajuste de foco. Se captura igual aunque success sea false (mejor una foto
            // con foco no perfecto que quedar colgado esperando).
            camera.autoFocus((success, camaraEnfocada) -> capturarFotoReal());
        } catch (Exception e) {
            Log.e(TAG, "tomarFoto() -> error al invocar autoFocus()/takePicture()", e);
            Toast.makeText(this, "Error al tomar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
        }
    }

    private void capturarFotoReal() {
        try {
            camera.takePicture(null, null, jpegCallback);
        } catch (Exception e) {
            Log.e(TAG, "capturarFotoReal() -> error al invocar takePicture()", e);
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
            mostrarRevision();
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

    /** Oculta la vista previa/controles y muestra la foto recien capturada con opciones aceptar/repetir. */
    private void mostrarRevision() {
        surfaceView.setVisibility(View.GONE);
        btnFlash.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        ((View) btnCapturar.getParent()).setVisibility(View.GONE);

        try (InputStream entrada = getContentResolver().openInputStream(imageUriDestino)) {
            Bitmap bitmap = BitmapFactory.decodeStream(entrada);
            imgRevisionFoto.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e(TAG, "mostrarRevision() -> error cargando la foto capturada", e);
        }

        panelRevision.setVisibility(View.VISIBLE);
    }

    /**
     * Vuelve al modo de captura. A diferencia de CameraX, Camera1 detiene el preview al
     * disparar takePicture(); hay que reanudarlo explicitamente con startPreview().
     */
    private void volverACapturar() {
        panelRevision.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
        btnFlash.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);
        ((View) btnCapturar.getParent()).setVisibility(View.VISIBLE);
        btnCapturar.setEnabled(true);

        if (camera != null) {
            try {
                camera.startPreview();
            } catch (Exception e) {
                Log.e(TAG, "volverACapturar() -> error reanudando el preview", e);
            }
        }
    }

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

    /**
     * Los botones fisicos de volumen disparan la foto, igual que en la camara estandar del
     * sistema. Se ignoran mientras se esta en la pantalla de revision o si el boton de captura
     * ya esta deshabilitado (una captura en curso), para no disparar dos veces.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btnCapturar.isEnabled() && panelRevision.getVisibility() != View.VISIBLE) {
                iniciarSecuenciaDeCaptura();
            }
            return true; // consumir el evento: no debe cambiar el volumen del equipo
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (panelRevision.getVisibility() == View.VISIBLE) {
            volverACapturar();
        } else {
            cancelarYSalir();
        }
    }

    @Override
    protected void onPause() {
        liberarCamara();
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orientationEventListener != null && orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
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
