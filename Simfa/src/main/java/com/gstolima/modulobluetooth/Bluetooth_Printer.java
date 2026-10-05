package com.gstolima.modulobluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bluetooth_Printer {

    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Ax: parámetros de texto EPL/ZPL (impresoras de buffer chico, necesitan pacing).
    private static final int BUFFER_SIZE_TEXTO = 1024;
    private static final long PAUSA_MICRO_TEXTO_MS = 50;
    private static final long PAUSA_FINAL_TEXTO_MS = 800;

    // Ax: parámetros de binario ESC/POS (impresoras POS modernas, buffer/BT
    // más rápido). Antes reutilizaban los mismos valores que EPL sin ninguna
    // razón real - eso es lo que estaba frenando la impresión ESC/POS.
    // El propio socket Bluetooth ya bloquea el write() si el receptor se
    // satura (flow control del protocolo RFCOMM), así que no hace falta
    // pacing manual agresivo como sí lo necesita el EPL por línea.
    private static final int BUFFER_SIZE_BINARIO = 4096;
    private static final long PAUSA_MICRO_BINARIO_MS = 0;
    private static final long PAUSA_FINAL_BINARIO_MS = 200;

    /**
     * Ax: EPL/ZPL usan codificación de 1 byte por carácter (CP850/Latin-1) -
     * la impresora no reensambla UTF-8. Enviar tildes/ñ en UTF-8 corta o
     * corrompe la trama según firmware. Cambiar solo si la impresora está
     * configurada con otra tabla de códigos (poco común en EPL/Eltron/Zebra).
     */
    private static final Charset CHARSET_IMPRESORA = Charset.forName("ISO-8859-1");

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    /**
     * Ax: EL PITIDO DE LA STARPOS A200U.
     *
     * Antes cada ticket abria su propio socket RFCOMM, imprimia y lo cerraba en el finally.
     * Esa impresora -como casi todas las termicas con Bluetooth- hace sonar el zumbador cada
     * vez que se ESTABLECE el enlace: es un tono del firmware del modulo Bluetooth, no un
     * comando ESC/POS, asi que no se podia quitar tocando el contenido del ticket.
     *
     * Se comprobo en campo el 02/10/2026: el MISMO IMPRIMIR.BIN mandado por cable USB desde el
     * PC (herramienta RAW de Windows) NO pita, y mandado por Bluetooth desde la movil SI pita,
     * aunque el ticket salga perfecto. O sea el pitido no depende de los bytes sino del enlace.
     *
     * Con la conexion reutilizada se conecta UNA vez y se queda abierta: un solo pitido al
     * primer ticket de la jornada en vez de uno por cada ticket. Si la impresora se apaga, se
     * aleja o corta el enlace, la escritura falla y se reconecta sola (ver escribirConReintento).
     *
     * OJO - SOLO APLICA AL BINARIO ESC/POS (StarPOS). Las Zebra imprimen por el camino de
     * TEXTO (CPCL/EPL) y ese va SIEMPRE con conexion nueva, como antes de todo esto. El
     * 02/10/2026 se comprobo en campo que la ZQ521 recibia la trama -se le veia el LED- pero no
     * sacaba el papel, mientras el MISMO archivo por cable si imprimia. Esa impresora necesita
     * el cierre del socket para dar la etiqueta por terminada, asi que para texto no se
     * reutiliza nada. Quien decide es el parametro permiteReuso de cada print().
     *
     * Poner en false para volver al comportamiento viejo tambien en el binario.
     */
    public static boolean REUSAR_CONEXION = true;

    // Conexion viva. Solo se tocan desde dentro del executor, que es de un solo hilo.
    private BluetoothSocket socketActivo;
    private BufferedOutputStream salidaActiva;
    private String macActiva;

    public interface Callback {
        void success();

        void error(Exception e);
    }

    @SuppressLint("MissingPermission")
    public static List<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return new ArrayList<>();
        return new ArrayList<>(adapter.getBondedDevices());
    }

    /**
     * Texto de comandos EPL/ZPL. Se envía como UN SOLO bloque, fragmentado
     * únicamente por tamaño (igual que el binario), con pausas cortas entre
     * fragmentos (máx. PAUSA_MICRO_TEXTO_MS = 50ms). Sin pausa larga
     * artificial entre grupos de líneas.
     *
     * Ax: originalmente esto agrupaba de a 10 líneas con 150ms de pausa
     * entre grupos, pensando que la impresora necesitaba ese "descanso"
     * para no perder datos. Se comprobó que NO era así: lo que fallaba era
     * un CRLF faltante al final del comando PRINT (terminador de trama), no
     * falta de pacing. Con eso corregido en el generador del ticket, la
     * pausa larga solo agregaba tiempo muerto sin ningún beneficio real.
     */
    public void print(final String mac, final String textoComandos, final Callback callback) {
        if (textoComandos == null) {
            if (callback != null) callback.error(new Exception("Error: contenido de impresión nulo"));
            return;
        }

        List<byte[]> lotes = new ArrayList<>();
        lotes.add(textoComandos.getBytes(CHARSET_IMPRESORA));
        // true: CPCL tambien reutiliza. Con false, cada tirilla pagaba una conexion
        // Bluetooth nueva (~1,5 a 4 s) mas la pausa final de 800 ms.
        enviarLotes(mac, lotes, 0,
                BUFFER_SIZE_TEXTO, PAUSA_MICRO_TEXTO_MS, PAUSA_FINAL_TEXTO_MS, true, callback);
    }

    /**
     * Overload AVANZADO: pacing manual por grupos de líneas, con pausa larga
     * configurable entre grupos. Dejar disponible solo por si en el futuro
     * aparece una impresora EPL que sí demuestre necesitar pausas largas
     * (buffer real muy chico) - no usar por defecto sin evidencia concreta
     * de que print(mac, texto, callback) falla por esa razón específica.
     */
    public void print(final String mac, final String textoComandos,
                      final int lineasPorLote, final long pausaEntreLotesMs,
                      final Callback callback) {

        if (textoComandos == null) {
            if (callback != null) callback.error(new Exception("Error: contenido de impresión nulo"));
            return;
        }

        List<byte[]> lotes = partirEnLotesDeLineas(textoComandos, lineasPorLote);
        enviarLotes(mac, lotes, pausaEntreLotesMs,
                BUFFER_SIZE_TEXTO, PAUSA_MICRO_TEXTO_MS, PAUSA_FINAL_TEXTO_MS, true, callback);
    }

    /**
     * Contenido binario crudo (ESC/POS con logo/bitmap embebido). NO pasar
     * por String: reencodear binario como texto corrompe los bytes >127 del
     * bitmap. Se envía como un único lote (fragmentado solo por tamaño vía
     * BUFFER_SIZE - un binario no tiene "líneas" que pausar).
     */
    public void print(final String mac, final byte[] datos, final Callback callback) {
        if (datos == null) {
            if (callback != null) callback.error(new Exception("Error: contenido de impresión nulo"));
            return;
        }
        List<byte[]> lotes = new ArrayList<>();
        lotes.add(datos);
        // true: solo el ESC/POS binario (StarPOS) reutiliza la conexion, que es donde se
        // valido el arreglo del pitido.
        enviarLotes(mac, lotes, 0,
                BUFFER_SIZE_BINARIO, PAUSA_MICRO_BINARIO_MS, PAUSA_FINAL_BINARIO_MS, true, callback);
    }

    private List<byte[]> partirEnLotesDeLineas(String texto, int lineasPorLote) {
        String[] lineas = texto.split("\r\n|\n", -1);
        List<byte[]> lotes = new ArrayList<>();
        StringBuilder loteActual = new StringBuilder();
        int contador = 0;

        for (String linea : lineas) {
            loteActual.append(linea).append("\r\n");
            contador++;
            if (contador >= lineasPorLote) {
                lotes.add(loteActual.toString().getBytes(CHARSET_IMPRESORA));
                loteActual.setLength(0);
                contador = 0;
            }
        }
        if (loteActual.length() > 0) {
            lotes.add(loteActual.toString().getBytes(CHARSET_IMPRESORA));
        }
        return lotes;
    }

    @SuppressLint("MissingPermission")
    private void enviarLotes(final String mac, final List<byte[]> lotes,
                             final long pausaEntreLotesMs,
                             final int bufferSize, final long pausaMicroMs, final long pausaFinalMs,
                             final boolean permiteReuso, final Callback callback) {

        if (mac == null || mac.isEmpty()) {
            if (callback != null) callback.error(new Exception("Error: MAC de impresora no válida"));
            return;
        }

        executor.execute(() -> {
            long tInicio = System.currentTimeMillis();
            try {
                escribirConReintento(mac, lotes, pausaEntreLotesMs, bufferSize, pausaMicroMs, pausaFinalMs, permiteReuso);
                Log.i("Bluetooth_Printer", "TOTAL impresión: " + (System.currentTimeMillis() - tInicio) + "ms");
                if (callback != null) callback.success();
            } catch (Exception ex) {
                cerrarConexion();
                if (callback != null) callback.error(ex);
            } finally {
                if (seVaACerrar(permiteReuso)) {
                    cerrarConexion();
                }
            }
        });
    }

    /**
     * Escribe el ticket. Si la conexion que se venia reutilizando ya murio (la impresora se
     * apago, se alejo o corto el enlace por inactividad), el primer write falla: se bota esa
     * conexion, se abre una nueva y se reintenta UNA vez desde el principio del ticket.
     */
    private void escribirConReintento(String mac, List<byte[]> lotes, long pausaEntreLotesMs,
                                      int bufferSize, long pausaMicroMs, long pausaFinalMs,
                                      boolean permiteReuso) throws Exception {
        boolean veniaReutilizada = permiteReuso && socketActivo != null;

        // La pausa final existe para que la impresora alcance a recibir ANTES de que se le
        // cierre el socket encima. Si la conexion queda abierta no hay nada que esperar.
        long pausaEfectiva = seVaACerrar(permiteReuso) ? pausaFinalMs : 0;

        try {
            asegurarConexion(mac, permiteReuso);
            escribirLotes(lotes, pausaEntreLotesMs, bufferSize, pausaMicroMs, pausaEfectiva);
        } catch (IOException e) {
            cerrarConexion();
            if (!veniaReutilizada) {
                throw e; // la conexion era nueva: si fallo, fallo de verdad
            }
            Log.w("Bluetooth_Printer", "La conexión reutilizada ya no servía ("
                    + e.getMessage() + "). Reconectando y reintentando el ticket.");
            asegurarConexion(mac, permiteReuso);
            escribirLotes(lotes, pausaEntreLotesMs, bufferSize, pausaMicroMs, pausaEfectiva);
        }
    }

    /** true cuando esta impresion va a cerrar el socket al terminar. */
    private static boolean seVaACerrar(boolean permiteReuso) {
        return !permiteReuso || !REUSAR_CONEXION;
    }

    private void escribirLotes(List<byte[]> lotes, long pausaEntreLotesMs,
                               int bufferSize, long pausaMicroMs, long pausaFinalMs)
            throws IOException, InterruptedException {

        long tConectado = System.currentTimeMillis();

        for (int loteIdx = 0; loteIdx < lotes.size(); loteIdx++) {
            byte[] lote = lotes.get(loteIdx);

            int offset = 0;
            while (offset < lote.length) {
                int length = Math.min(bufferSize, lote.length - offset);
                salidaActiva.write(lote, offset, length);
                salidaActiva.flush();

                offset += length;

                // sleep para no saturar el buffer de la impresora dentro del lote (Throttling)
                if (offset < lote.length && pausaMicroMs > 0) {
                    Thread.sleep(pausaMicroMs);
                }
            }

            // pausa larga ENTRE lotes (ej. cada 10 líneas de EPL) - esta es la
            // que replica el "descanso" que ya funcionaba en el modelo antiguo
            if (loteIdx < lotes.size() - 1 && pausaEntreLotesMs > 0) {
                Thread.sleep(pausaEntreLotesMs);
            }
        }

        Log.i("Bluetooth_Printer", "Escritura de " + lotes.size() + " lote(s) terminada en "
                + (System.currentTimeMillis() - tConectado) + "ms");

        // sleep para que la impresora termine de recibir antes de soltar el control
        if (pausaFinalMs > 0) {
            Thread.sleep(pausaFinalMs);
        }
    }

    /** Deja lista una conexion utilizable con esa impresora, reutilizando la que haya. */
    @SuppressLint("MissingPermission")
    private void asegurarConexion(String mac, boolean permiteReuso) throws Exception {
        if (permiteReuso
                && REUSAR_CONEXION
                && socketActivo != null
                && salidaActiva != null
                && socketActivo.isConnected()
                && mac.equalsIgnoreCase(macActiva)) {
            return; // ya hay enlace vivo con esta impresora: ni un pitido mas
        }
        cerrarConexion();
        conectar(mac);
    }

    @SuppressLint("MissingPermission")
    private void conectar(String mac) throws Exception {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null)
            throw new Exception("Bluetooth no soportado en este dispositivo.");

        if (!adapter.isEnabled())
            throw new Exception("El Bluetooth está apagado. Por favor, actívelo.");

        // Verificar si el dispositivo está vinculado
        boolean isBonded = false;
        for (BluetoothDevice d : adapter.getBondedDevices()) {
            if (mac.equalsIgnoreCase(d.getAddress())) {
                isBonded = true;
                break;
            }
        }

        if (!isBonded)
            throw new Exception("La impresora no está vinculada. Por favor, vincúlela en ajustes de Android.");

        adapter.cancelDiscovery();
        BluetoothDevice device = adapter.getRemoteDevice(mac);

        final AtomicBoolean isTimeout = new AtomicBoolean(false);
        // Marca que connect() ya volvio. El vigilante se apaga con esto y no con
        // socket.isConnected(): esa API miente, y con la conexion reutilizada el vigilante
        // llegaba a los 4 segundos con el socket YA en uso y lo cerraba en plena faena.
        final AtomicBoolean conexionResuelta = new AtomicBoolean(false);
        long tAntesConectar = System.currentTimeMillis();
        BluetoothSocket socket = null;
        boolean usoFallback = false;

        // Intento de conexión normal con Timeout manual
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);

            // Hilo de timeout para no esperar los 9 segundos por defecto de Android
            final BluetoothSocket tempSocket = socket;
            Thread timeoutWatcher = new Thread(() -> {
                try {
                    long limite = System.currentTimeMillis() + 4000; // espera maxima
                    while (System.currentTimeMillis() < limite) {
                        if (conexionResuelta.get()) {
                            return;              // connect() ya volvio: nada que vigilar
                        }
                        Thread.sleep(100);
                    }
                    if (!conexionResuelta.get() && tempSocket != null) {
                        isTimeout.set(true);
                        tempSocket.close();      // desbloquea el connect() colgado
                    }
                } catch (Exception ignored) {}
            });
            timeoutWatcher.setDaemon(true);
            timeoutWatcher.start();

            try {
                socket.connect();
            } finally {
                conexionResuelta.set(true);
            }
        } catch (Exception e) {
            try { if (socket != null) socket.close(); } catch (IOException ignored) {}

            // Si fue por timeout de 4s, abortamos inmediatamente sin intentar el fallback
            if (isTimeout.get()) {
                throw new Exception("La impresora no responde. Verifique que esté encendida y cerca.");
            }

            // Segundo intento (Fallback por reflexión)
            usoFallback = true;
            Log.w("Bluetooth_Printer", "Conexión primaria falló tras "
                    + (System.currentTimeMillis() - tAntesConectar) + "ms, cayendo a fallback. Causa: " + e.getMessage());
            try {
                Thread.sleep(500);
                socket = (BluetoothSocket) device.getClass()
                        .getMethod("createRfcommSocket", int.class)
                        .invoke(device, 1);

                if (socket != null) {
                    socket.connect();
                } else {
                    throw new Exception();
                }
            } catch (Exception e2) {
                throw new Exception("No se pudo conectar con la impresora. Verifique que esté encendida, cerca y vinculada.");
            }
        }

        socketActivo = socket;
        salidaActiva = new BufferedOutputStream(socket.getOutputStream());
        macActiva = mac;

        Log.i("Bluetooth_Printer", "Conexión establecida en "
                + (System.currentTimeMillis() - tAntesConectar) + "ms (fallback=" + usoFallback + ")");
    }

    private void cerrarConexion() {
        try {
            if (salidaActiva != null) salidaActiva.close();
        } catch (IOException ignored) {}
        try {
            if (socketActivo != null) socketActivo.close();
        } catch (IOException ignored) {}
        salidaActiva = null;
        socketActivo = null;
        macActiva = null;
    }

    /**
     * Suelta el enlace con la impresora. Llamar al salir de la pantalla de liquidacion o al
     * cerrar sesion; no hace falta entre ticket y ticket, que es justo lo que se quiere evitar.
     */
    public void desconectar() {
        try {
            executor.execute(this::cerrarConexion);
        } catch (RuntimeException ignored) {
            // el executor ya estaba cerrado: no hay nada que soltar
        }
    }

    public void close() {
        // execute() lanza RejectedExecutionException si ya se hizo shutdown (por ejemplo si
        // onDestroy corre dos veces). Antes close() era solo shutdown() y eso nunca pasaba.
        try {
            executor.execute(this::cerrarConexion);
        } catch (RuntimeException ignored) {
        }
        executor.shutdown();
    }

}