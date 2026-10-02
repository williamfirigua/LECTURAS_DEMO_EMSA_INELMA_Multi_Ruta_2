package com.gstolima.accesoyseguridad;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
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

import com.gstolima.captureException.LogEventos;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Collections;
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

    // Tope para el preview. Sin fijarlo, varios equipos arrancan el preview a la resolucion
    // maxima del sensor: el HAL reserva buffers enormes y el primer frame tarda mucho mas,
    // sin ninguna ganancia visible en una pantalla de telefono.
    // 1280x960 deja pasar 1280x720 en equipos 16:9 y 1280x960 en los 4:3, y deja fuera
    // 1920x1080 y todo lo de arriba, que es lo que encarece el arranque sin verse mejor.
    private static final int MAX_PIXELES_PREVIEW = 1280 * 960;
    private static final float TOLERANCIA_ASPECTO = 0.05f;

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

    // Arranque de la camara. Camera.open() tarda entre 300 y 800 ms (mas en equipos cuyo
    // Camera1 es capa de compatibilidad), asi que se hace en un hilo aparte y en paralelo con
    // el inflado de la pantalla, en vez de esperar a que la Surface exista. El preview arranca
    // cuando coinciden las dos cosas: camara abierta y Surface lista.
    private boolean abriendoCamara = false;
    private boolean previewActivo = false;
    private boolean pausado = false;
    private int anchoSuperficie = 0;
    private int altoSuperficie = 0;

    private CountDownTimer countDownTimer;
    private OrientationEventListener orientationEventListener;
    private int orientacionDispositivoActual = 0; // 0/90/180/270, segun el sensor, independiente del lock de pantalla

    // Salvaguarda: en hardware donde Camera1 es solo una capa de compatibilidad sobre un HAL
    // moderno (ej. equipos para los que Camera1 no fue pensado), el callback de autoFocus()
    // puede no dispararse nunca. Sin este timeout la captura queda colgada para siempre.
    private static final long TIMEOUT_AUTOFOCUS_MS = 3000L;
    private final Handler handlerAutoFocus = new Handler(Looper.getMainLooper());
    private Runnable runnableTimeoutAutoFocus;
    private boolean callbackDeEnfoqueRecibido = false;
    private Bitmap bitmapRevision;

    // Zoom libre (pellizco). Camera1 solo acepta indices discretos de getZoomRatios() (x100),
    // por eso se acumula el ratio deseado en float y solo se llama setParameters() cuando el
    // indice cambia: evita trabarse en el mismo indice con gestos lentos y no satura el HAL.
    private ScaleGestureDetector detectorZoom;
    private List<Integer> ratiosZoom;      // null si la camara no soporta zoom
    private int indiceZoomActual = 0;
    private float ratioZoomDeseado = 1.0f;

    // Enfoque por toque. Las areas de Camera1 usan coordenadas del sensor en [-1000, 1000],
    // independientes de la orientacion de pantalla; se convierten desde el toque con una matriz.
    private static final int LADO_AREA_ENFOQUE = 200;          // 10% del campo visual
    private static final long OCULTAR_INDICADOR_MS = 1500L;
    private static final long TIMEOUT_ENFOQUE_TOQUE_MS = 3000L;
    private GestureDetector detectorToque;
    private View indicadorEnfoque;
    private View capaToqueEnfoque;
    // static: las capacidades del equipo no cambian entre fotos. Como instancia se volvia a
    // consultar y a escribir en LOGEVENTOS en CADA apertura de camara, dentro del camino
    // critico del preview.
    private static boolean capacidadesRegistradas = false;
    private boolean avisoFocoFijoMostrado = false;
    private int rotacionPreview = 0;
    private List<Camera.Area> areasEnfoqueManual;              // null = enfoque automatico continuo
    private final Runnable ocultarIndicador = () -> {
        if (indicadorEnfoque != null) indicadorEnfoque.setVisibility(View.GONE);
    };
    private static final int LADO_MAX_REVISION = 1280;

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
            // Se arranca la apertura YA, sin esperar a surfaceCreated(): asi Camera.open()
            // corre en paralelo con el inflado de la pantalla en vez de sumarse despues.
            abrirCamaraEnSegundoPlano();
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
        capaToqueEnfoque = findViewById(R.id.capaToqueEnfoque);
        indicadorEnfoque = findViewById(R.id.indicadorEnfoque);
    }

    private void configurarListeners() {
        btnCancelar.setOnClickListener(v -> cancelarYSalir());
        btnFlash.setOnClickListener(v -> alternarFlash());
        btnCapturar.setOnClickListener(v -> iniciarSecuenciaDeCaptura());
        btnZoom1x.setOnClickListener(v -> aplicarZoom(1.0f));
        btnZoom2x.setOnClickListener(v -> aplicarZoom(2.0f));

        detectorZoom = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                aplicarZoom(ratioZoomDeseado * detector.getScaleFactor());
                return true;
            }
        });
        detectorToque = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (!detectorZoom.isInProgress()) {
                    enfocarEnPunto(e.getX(), e.getY());
                }
                return true;
            }
        });
        capaToqueEnfoque.setOnTouchListener((v, event) -> {
            detectorZoom.onTouchEvent(event);
            detectorToque.onTouchEvent(event);
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        });
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
                abrirCamaraEnSegundoPlano();
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

    /**
     * Ojo: aqui NO se configura el preview. surfaceCreated() todavia no conoce el tamano real
     * de la superficie, y surfaceChanged() se dispara siempre inmediatamente despues con las
     * medidas ya resueltas. Configurar en las dos terminaba haciendo el ciclo completo dos
     * veces por cada foto.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        superficieLista = true;
        if (camera == null) {
            abrirCamaraEnSegundoPlano();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        boolean cambioElTamano = (width != anchoSuperficie || height != altoSuperficie);
        anchoSuperficie = width;
        altoSuperficie = height;

        if (camera == null) {
            // La camara viene en camino: arranca sola al llegar, ya con el tamano registrado.
            return;
        }

        // Antes esto repetia el ciclo completo de configuracion sobre un preview que ya estaba
        // corriendo: getParameters + setParameters + startPreview de mas, y ademas
        // setPreviewDisplay() y setDisplayOrientation() lanzan excepcion si el preview ya
        // arranco, que era lo que terminaba en el Toast "Error iniciando la vista de camara".
        if (previewActivo && !cambioElTamano) return;

        if (previewActivo) {
            try {
                camera.stopPreview();
            } catch (Exception ignored) {
                // si no estaba corriendo, no hay nada que detener
            }
            previewActivo = false;
        }
        configurarPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        superficieLista = false;
        // Se olvidan las medidas: si vuelve a crearse la superficie, el primer surfaceChanged
        // tiene que contar como tamano nuevo.
        anchoSuperficie = 0;
        altoSuperficie = 0;
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

    /**
     * Abre la camara fuera del hilo principal.
     *
     * Antes se abria dentro de surfaceCreated(), o sea DESPUES de que la ventana ya estaba
     * medida, dispuesta y con buffer asignado: los 300-800 ms de Camera.open() no se solapaban
     * con nada, se sumaban al final y con la pantalla ya visible y congelada. Ahora arranca en
     * onCreate() y compite con el inflado, que es tiempo que de todas formas hay que pagar.
     */
    private void abrirCamaraEnSegundoPlano() {
        if (abriendoCamara || camera != null) return;
        abriendoCamara = true;

        new Thread(() -> {
            int id = encontrarCamaraTrasera();
            Camera abierta = null;
            Exception fallo = null;
            if (id != -1) {
                try {
                    abierta = Camera.open(id);
                } catch (Exception e) {
                    fallo = e;
                }
            }
            final int idFinal = id;
            final Camera camaraFinal = abierta;
            final Exception falloFinal = fallo;
            runOnUiThread(() -> onCamaraAbierta(idFinal, camaraFinal, falloFinal));
        }, "abrir-camara").start();
    }

    /** Hilo principal. Recibe el resultado de la apertura. */
    private void onCamaraAbierta(int id, Camera abierta, Exception fallo) {
        abriendoCamara = false;

        // Puede llegar despues de que el operario salio o de que la pantalla paso a segundo
        // plano: en ese caso hay que soltar el hardware, no quedarselo.
        if (pausado || isFinishing() || isDestroyed()) {
            if (abierta != null) {
                abierta.release();
            }
            return;
        }

        if (abierta == null) {
            // Ultimo recurso: si ni siquiera Camera1 puede abrir el hardware, se delega
            // a la app de camara del sistema, igual que en CamaraFotoActivity.
            if (id == -1) {
                Log.e(TAG, "abrirCamara() -> no se encontro camara trasera");
            } else {
                Log.e(TAG, "abrirCamara() -> no se pudo abrir la camara con Camera1", fallo);
            }
            usarCamaraDelSistemaComoUltimoRecurso();
            return;
        }

        cameraId = id;
        camera = abierta;
        arrancarPreviewSiTodoListo();
    }

    /**
     * El preview necesita las dos cosas: camara abierta y superficie con medidas ya resueltas.
     * Llega la que llegue primero; la segunda es la que dispara la configuracion, una sola vez.
     */
    private void arrancarPreviewSiTodoListo() {
        if (camera == null || previewActivo) return;
        if (!superficieLista || anchoSuperficie <= 0 || altoSuperficie <= 0) return;
        configurarPreview();
    }

    private void configurarPreview() {
        try {
            camera.setPreviewDisplay(surfaceHolder);

            Camera.Parameters params = camera.getParameters();

            Camera.Size mejorTamano = elegirMejorTamanoFoto(params.getSupportedPictureSizes());
            if (mejorTamano != null) {
                params.setPictureSize(mejorTamano.width, mejorTamano.height);
            }

            // Si no se fija, el preview lo elige el equipo y varios arrancan a la resolucion
            // maxima del sensor: buffers enormes y primer frame mucho mas lento, para nada.
            Camera.Size tamanoPreview = elegirTamanoPreview(params.getSupportedPreviewSizes(), mejorTamano);
            if (tamanoPreview != null) {
                params.setPreviewSize(tamanoPreview.width, tamanoPreview.height);
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
            if (areasEnfoqueManual != null && soportaEnfoquePorArea(params)) {
                // Volviendo de segundo plano con un punto ya tocado: se conserva ese punto.
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                params.setFocusAreas(areasEnfoqueManual);
                if (params.getMaxNumMeteringAreas() > 0) {
                    params.setMeteringAreas(areasEnfoqueManual);
                }
            } else if (focosSoportados != null && focosSoportados.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focosSoportados != null && focosSoportados.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            prepararZoom(params);

            camera.setParameters(params);
            rotacionPreview = calcularRotacionPreview();
            camera.setDisplayOrientation(rotacionPreview);
            camera.startPreview();
            previewActivo = true;

            // Despues de que el preview ya arranco: es informativo y no tiene por que
            // retrasar lo que el operario esta esperando ver.
            registrarCapacidadesCamara(params);
        } catch (Exception e) {
            previewActivo = false;
            Log.e(TAG, "configurarPreview() -> error configurando la camara", e);
            Toast.makeText(this, "Error iniciando la vista de camara", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mayor preview que no pase de MAX_PIXELES_PREVIEW y que tenga la MISMA relacion de aspecto
     * que la foto, para que lo encuadrado sea lo que sale capturado. Si ninguno coincide en
     * aspecto devuelve null y se respeta el que traiga el equipo, antes que deformar la imagen.
     */
    private Camera.Size elegirTamanoPreview(List<Camera.Size> tamanos, Camera.Size tamanoFoto) {
        if (tamanos == null || tamanos.isEmpty() || tamanoFoto == null || tamanoFoto.height == 0) {
            return null;
        }
        float aspectoFoto = (float) tamanoFoto.width / tamanoFoto.height;

        Camera.Size mejor = null;
        for (Camera.Size tamano : tamanos) {
            if (tamano.height == 0) continue;
            if ((long) tamano.width * tamano.height > MAX_PIXELES_PREVIEW) continue;
            if (Math.abs((float) tamano.width / tamano.height - aspectoFoto) > TOLERANCIA_ASPECTO) continue;

            if (mejor == null || (long) tamano.width * tamano.height > (long) mejor.width * mejor.height) {
                mejor = tamano;
            }
        }
        return mejor;
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
        previewActivo = false;
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
     * Lee una sola vez las capacidades de zoom del equipo y deja en 'params' el indice que
     * corresponde al zoom actual (se conserva al volver de segundo plano).
     */
    private void prepararZoom(Camera.Parameters params) {
        if (!params.isZoomSupported()) {
            ratiosZoom = null;
            indiceZoomActual = 0;
            return;
        }
        List<Integer> ratios = params.getZoomRatios();
        int maxIndice = Math.min(params.getMaxZoom(), ratios == null ? -1 : ratios.size() - 1);
        if (maxIndice <= 0) {
            ratiosZoom = null;
            indiceZoomActual = 0;
            return;
        }
        ratiosZoom = ratios.subList(0, maxIndice + 1);
        ratioZoomDeseado = limitarRatio(ratioZoomDeseado);
        indiceZoomActual = indiceMasCercano(ratioZoomDeseado);
        params.setZoom(indiceZoomActual);
    }

    /**
     * Aplica un zoom libre dentro del rango real del equipo (acercar > 1, alejar hacia el minimo).
     * Camera1 usa indices sobre getZoomRatios() (valores x100: 100 = 1x, 250 = 2.5x).
     */
    private void aplicarZoom(float ratioSolicitado) {
        if (camera == null || ratiosZoom == null) return;

        ratioZoomDeseado = limitarRatio(ratioSolicitado);
        int indice = indiceMasCercano(ratioZoomDeseado);
        if (indice == indiceZoomActual) return;

        try {
            Camera.Parameters params = camera.getParameters();
            params.setZoom(indice);
            camera.setParameters(params);
            indiceZoomActual = indice;
        } catch (Exception e) {
            Log.e(TAG, "aplicarZoom(" + ratioSolicitado + ") -> error", e);
        }
    }

    private float limitarRatio(float ratio) {
        float minimo = ratiosZoom.get(0) / 100f;
        float maximo = ratiosZoom.get(ratiosZoom.size() - 1) / 100f;
        return Math.max(minimo, Math.min(ratio, maximo));
    }

    /** Busqueda binaria: getZoomRatios() viene ordenada de forma ascendente. */
    private int indiceMasCercano(float ratio) {
        int objetivo = Math.round(ratio * 100);
        int bajo = 0;
        int alto = ratiosZoom.size() - 1;
        while (bajo < alto) {
            int medio = (bajo + alto) >>> 1;
            if (ratiosZoom.get(medio) < objetivo) {
                bajo = medio + 1;
            } else {
                alto = medio;
            }
        }
        if (bajo > 0 && Math.abs(ratiosZoom.get(bajo - 1) - objetivo) <= Math.abs(ratiosZoom.get(bajo) - objetivo)) {
            return bajo - 1;
        }
        return bajo;
    }

    // ---------------------------------------------------------------- Enfoque por toque

    private static boolean soportaEnfoquePorArea(Camera.Parameters params) {
        List<String> modos = params.getSupportedFocusModes();
        return params.getMaxNumFocusAreas() > 0
                && modos != null && modos.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }

    /**
     * Enfoca en el punto tocado. El recuadro se muestra SIEMPRE primero, para que el operario
     * vea que el toque se recibio aunque la camara no soporte enfocar por zona.
     *
     *  - Con zonas de enfoque (getMaxNumFocusAreas() > 0): enfoca en el punto tocado.
     *  - Sin zonas pero con FOCUS_MODE_AUTO: fuerza un re-enfoque automatico (centro).
     *  - Foco fijo: solo muestra el recuadro y avisa una vez.
     *
     * El punto queda fijo: el autoFocus() previo a la captura en tomarFoto() usa esta misma zona.
     */
    private void enfocarEnPunto(float x, float y) {
        if (!btnCapturar.isEnabled() || panelRevision.getVisibility() == View.VISIBLE) {
            return; // captura o cuenta regresiva en curso, o pantalla de revision
        }
        mostrarIndicador(x, y, Color.WHITE);
        ocultarIndicadorEn(TIMEOUT_ENFOQUE_TOQUE_MS);
        if (camera == null) {
            ocultarIndicadorEn(OCULTAR_INDICADOR_MS);
            return;
        }

        try {
            Camera.Parameters params = camera.getParameters();
            List<String> modos = params.getSupportedFocusModes();
            boolean modoAuto = modos != null && modos.contains(Camera.Parameters.FOCUS_MODE_AUTO);
            boolean porZona = modoAuto && params.getMaxNumFocusAreas() > 0;
            boolean medicion = params.getMaxNumMeteringAreas() > 0;

            if (!modoAuto) {
                ocultarIndicadorEn(OCULTAR_INDICADOR_MS);
                if (!avisoFocoFijoMostrado) {
                    avisoFocoFijoMostrado = true;
                    Toast.makeText(this, "Este equipo no permite ajustar el enfoque", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            List<Camera.Area> areas = Collections.singletonList(
                    new Camera.Area(areaSensorDesdeToque(x, y), 1000));

            camera.cancelAutoFocus();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            if (porZona) {
                params.setFocusAreas(areas);
            }
            if (medicion) {
                params.setMeteringAreas(areas);
            }
            aplicarParametrosEnfoque(params, medicion);
            areasEnfoqueManual = porZona ? areas : null;

            camera.autoFocus((exito, cam) -> {
                mostrarIndicador(x, y, exito ? Color.GREEN : Color.RED);
                ocultarIndicadorEn(OCULTAR_INDICADOR_MS);
            });
        } catch (Exception e) {
            LogEventos.registrarError(this, "[CAMARA] enfocarEnPunto(" + x + ", " + y + ")", e);
            mostrarIndicador(x, y, Color.RED);
            ocultarIndicadorEn(OCULTAR_INDICADOR_MS);
        }
    }

    /**
     * Algunos HAL rechazan la zona de medicion aunque digan soportarla ("setParameters failed").
     * En ese caso se reintenta sin medicion para no perder el enfoque.
     */
    private void aplicarParametrosEnfoque(Camera.Parameters params, boolean conMedicion) {
        try {
            camera.setParameters(params);
        } catch (RuntimeException e) {
            if (!conMedicion) throw e;
            params.setMeteringAreas(null);
            camera.setParameters(params);
            LogEventos.escribir(this, "[CAMARA] El equipo rechazo la zona de medicion; se enfoca sin ella.");
        }
    }

    /** Deja en LOGEVENTOS, una vez por pantalla, lo que la camara de este equipo soporta realmente. */
    private void registrarCapacidadesCamara(Camera.Parameters params) {
        if (capacidadesRegistradas) return;
        capacidadesRegistradas = true;

        // Se arma aqui el texto (solo lee campos ya cargados en params) y se deja para un hilo
        // aparte lo caro: la consulta a camera2 (binder al servicio de camara) y la escritura
        // en LOGEVENTOS. Antes las dos cosas corrian en el hilo principal y ademas ANTES del
        // setParameters/startPreview, o sea justo en el camino a ver el preview.
        final String capacidades = "modosEnfoque=" + params.getSupportedFocusModes()
                + " | zonasEnfoque=" + params.getMaxNumFocusAreas()
                + " | zonasMedicion=" + params.getMaxNumMeteringAreas()
                + " | zoom=" + params.isZoomSupported() + " (max " + params.getMaxZoom() + ")";
        final int idCamara = cameraId;
        final android.content.Context contexto = getApplicationContext();

        new Thread(() -> {
            String nivelCamera2 = "desconocido";
            try {
                android.hardware.camera2.CameraManager cm =
                        (android.hardware.camera2.CameraManager) contexto.getSystemService(CAMERA_SERVICE);
                if (cm != null) {
                    Integer nivel = cm.getCameraCharacteristics(String.valueOf(idCamara))
                            .get(android.hardware.camera2.CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    nivelCamera2 = String.valueOf(nivel);
                }
            } catch (Exception ignored) {
                // informativo
            }
            LogEventos.escribir(contexto, "[CAMARA] Capacidades camara " + idCamara
                    + " | " + capacidades
                    + " | nivelCamera2=" + nivelCamera2 + " (0=LIMITED,1=FULL,2=LEGACY,3=LEVEL_3,4=EXTERNAL)");
        }, "log-capacidades-camara").start();
    }

    /**
     * Convierte el toque (pixeles de la vista) al sistema de Camera1 [-1000, 1000], el mismo
     * metodo que usa la app de camara de AOSP: la matriz sensor->vista se invierte.
     */
    private Rect areaSensorDesdeToque(float x, float y) {
        int ancho = capaToqueEnfoque.getWidth();
        int alto = capaToqueEnfoque.getHeight();

        Matrix sensorAVista = new Matrix();
        sensorAVista.postRotate(rotacionPreview);        // camara trasera: sin espejo
        sensorAVista.postScale(ancho / 2000f, alto / 2000f);
        sensorAVista.postTranslate(ancho / 2f, alto / 2f);
        Matrix vistaASensor = new Matrix();
        sensorAVista.invert(vistaASensor);

        float mitad = LADO_AREA_ENFOQUE / 2f;
        float[] centro = {x, y};
        vistaASensor.mapPoints(centro);
        float cx = Math.max(-1000 + mitad, Math.min(centro[0], 1000 - mitad));
        float cy = Math.max(-1000 + mitad, Math.min(centro[1], 1000 - mitad));

        RectF area = new RectF(cx - mitad, cy - mitad, cx + mitad, cy + mitad);
        return new Rect(Math.round(area.left), Math.round(area.top), Math.round(area.right), Math.round(area.bottom));
    }

    private void mostrarIndicador(float x, float y, int color) {
        if (indicadorEnfoque == null) return;
        int lado = indicadorEnfoque.getLayoutParams().width;
        float maxX = Math.max(0, capaToqueEnfoque.getWidth() - lado);
        float maxY = Math.max(0, capaToqueEnfoque.getHeight() - lado);
        indicadorEnfoque.setX(Math.max(0, Math.min(x - lado / 2f, maxX)));
        indicadorEnfoque.setY(Math.max(0, Math.min(y - lado / 2f, maxY)));

        GradientDrawable borde = (GradientDrawable) indicadorEnfoque.getBackground().mutate();
        borde.setStroke(Math.round(2 * getResources().getDisplayMetrics().density), color);
        indicadorEnfoque.setVisibility(View.VISIBLE);
    }

    private void ocultarIndicadorEn(long ms) {
        handlerAutoFocus.removeCallbacks(ocultarIndicador);
        handlerAutoFocus.postDelayed(ocultarIndicador, ms);
    }

    // ---------------------------------------------------------------- Captura

    private void iniciarSecuenciaDeCaptura() {
        btnCapturar.setEnabled(false);
        ocultarIndicadorEn(0);

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
            callbackDeEnfoqueRecibido = false;
            runnableTimeoutAutoFocus = () -> {
                if (!callbackDeEnfoqueRecibido) {
                    Log.e(TAG, "tomarFoto() -> TIMEOUT esperando autoFocus(), se dispara sin confirmar enfoque");
                    callbackDeEnfoqueRecibido = true;
                    capturarFotoReal();
                }
            };
            handlerAutoFocus.postDelayed(runnableTimeoutAutoFocus, TIMEOUT_AUTOFOCUS_MS);

            camera.autoFocus((success, camaraEnfocada) -> {
                if (!callbackDeEnfoqueRecibido) {
                    callbackDeEnfoqueRecibido = true;
                    handlerAutoFocus.removeCallbacks(runnableTimeoutAutoFocus);
                    capturarFotoReal();
                }
            });
        } catch (Exception e) {
            handlerAutoFocus.removeCallbacks(runnableTimeoutAutoFocus);
            callbackDeEnfoqueRecibido = true;
            Log.e(TAG, "tomarFoto() -> error al invocar autoFocus()/takePicture()", e);
            Toast.makeText(this, "Error al tomar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
        }
    }

    private void capturarFotoReal() {
        if (camera == null || isFinishing()) {
            btnCapturar.setEnabled(true);
            return;
        }
        try {
            camera.takePicture(null, null, jpegCallback);
        } catch (Exception e) {
            Log.e(TAG, "capturarFotoReal() -> error al invocar takePicture()", e);
            Toast.makeText(this, "Error al tomar la foto, intente de nuevo", Toast.LENGTH_SHORT).show();
            btnCapturar.setEnabled(true);
        }
    }

    private final Camera.PictureCallback jpegCallback = (data, camaraQueDisparo) -> {
        try (OutputStream salida = getContentResolver().openOutputStream(imageUriDestino, "wt")) {
            if (salida == null) {
                throw new IOException("No se pudo abrir el OutputStream del Uri destino");
            }
            salida.write(data);
            salida.flush();
            mostrarRevision(data);
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
    private void mostrarRevision(byte[] data) {
        surfaceView.setVisibility(View.GONE);
        btnFlash.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        ((View) btnCapturar.getParent()).setVisibility(View.GONE);

        liberarBitmapRevision();
        bitmapRevision = decodificarParaRevision(data);
        imgRevisionFoto.setImageBitmap(bitmapRevision);

        panelRevision.setVisibility(View.VISIBLE);
    }

    /**
     * Decodifica el JPEG en memoria a una resolucion acotada. Decodificarlo completo fallaba con
     * OOM / "Canvas: trying to draw too large bitmap" en equipos que no ofrecen tamanos <= 2MP.
     * Se aplica la rotacion EXIF para que la revision coincida con la foto final.
     */
    private Bitmap decodificarParaRevision(byte[] data) {
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, bounds);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            int ladoMayor = Math.max(bounds.outWidth, bounds.outHeight);
            int sample = 1;
            while (ladoMayor / (sample * 2) >= LADO_MAX_REVISION) {
                sample *= 2;
            }
            opts.inSampleSize = sample;
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            if (bmp == null) {
                return null;
            }

            int grados = 0;
            try (java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(data)) {
                int o = new android.media.ExifInterface(in).getAttributeInt(
                        android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL);
                if (o == android.media.ExifInterface.ORIENTATION_ROTATE_90) grados = 90;
                else if (o == android.media.ExifInterface.ORIENTATION_ROTATE_180) grados = 180;
                else if (o == android.media.ExifInterface.ORIENTATION_ROTATE_270) grados = 270;
            }
            if (grados == 0) {
                return bmp;
            }
            android.graphics.Matrix m = new android.graphics.Matrix();
            m.postRotate(grados);
            Bitmap rotado = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
            if (rotado != bmp) {
                bmp.recycle();
            }
            return rotado;
        } catch (Exception | OutOfMemoryError e) {
            Log.e(TAG, "decodificarParaRevision() -> no se pudo generar la vista previa", e);
            return null;
        }
    }

    private void liberarBitmapRevision() {
        if (imgRevisionFoto != null) {
            imgRevisionFoto.setImageDrawable(null);
        }
        if (bitmapRevision != null && !bitmapRevision.isRecycled()) {
            bitmapRevision.recycle();
        }
        bitmapRevision = null;
    }

    /**
     * Vuelve al modo de captura. A diferencia de CameraX, Camera1 detiene el preview al
     * disparar takePicture(); hay que reanudarlo explicitamente con startPreview().
     */
    private void volverACapturar() {
        panelRevision.setVisibility(View.GONE);
        liberarBitmapRevision();
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
        // La Uri pertenece a esta app: sin estos flags la app de camara no puede escribirla.
        intentSistema.setClipData(ClipData.newRawUri("output", imageUriDestino));
        intentSistema.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivityForResult(intentSistema, REQUEST_CODE_FALLBACK_SISTEMA);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "usarCamaraDelSistemaComoUltimoRecurso() -> no hay app de camara", e);
            Toast.makeText(this, "No hay aplicacion de camara disponible", Toast.LENGTH_LONG).show();
            cancelarYSalir();
        }
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            txtCountdown.setVisibility(View.GONE);
        }
        if (runnableTimeoutAutoFocus != null) {
            handlerAutoFocus.removeCallbacks(runnableTimeoutAutoFocus);
        }
        handlerAutoFocus.removeCallbacks(ocultarIndicador);
        if (indicadorEnfoque != null) {
            indicadorEnfoque.setVisibility(View.GONE);
        }
        if (panelRevision.getVisibility() != View.VISIBLE) {
            btnCapturar.setEnabled(true);
        }
        pausado = true;
        liberarCamara();
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pausado = false;
        if (orientationEventListener != null && orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
        if (camera == null && superficieLista) {
            abrirCamaraEnSegundoPlano();
        }
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (runnableTimeoutAutoFocus != null) {
            handlerAutoFocus.removeCallbacks(runnableTimeoutAutoFocus);
        }
        handlerAutoFocus.removeCallbacks(ocultarIndicador);
        liberarBitmapRevision();
        super.onDestroy();
    }
}