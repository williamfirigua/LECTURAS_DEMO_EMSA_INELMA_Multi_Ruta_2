package com.gstolima.accesoyseguridad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * PT582 - impresión rápida.
 *
 * OBJETIVO:
 * - Una sola operación de envío para casi todo el ticket.
 * - Sin flush() por línea.
 * - Sin pausas entre líneas.
 * - Texto ESC/POS, no imágenes para el contenido.
 * - Logo pequeño monocromático.
 *
 * IMPORTANTE:
 * Reemplazar el package por el de tu aplicación.
 */
public class PT582PrinterFast {

    private static final byte ESC = 0x1B;
    private static final byte GS  = 0x1D;

    private static final Charset CHARSET = Charset.forName("CP437");

    // 58 mm típico: 384 puntos a 203 dpi.
    // El logo es intencionalmente pequeño.
    private static final int MAX_WIDTH_DOTS = 384;

    private final OutputStream out;
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(8192);

    public PT582PrinterFast(OutputStream out) {
        this.out = out;
    }

    // Todo se acumula en memoria y se manda al Bluetooth en bloques.
    private void b(byte value) {
        buffer.write(value);
    }

    private void bytes(byte[] data) {
        buffer.write(data, 0, data.length);
    }

    private void text(String s) {
        if (s != null) {
            byte[] data = s.getBytes(CHARSET);
            buffer.write(data, 0, data.length);
        }
    }

    private void line(String s) {
        text(s);
        b((byte) 0x0A);
    }

    private void init() {
        bytes(new byte[]{ESC, '@'});       // Inicializar
        bytes(new byte[]{ESC, '2'});       // Interlineado estándar
        bytes(new byte[]{ESC, 'a', 0});    // Izquierda
        bytes(new byte[]{ESC, 'M', 1});    // Fuente B
        bytes(new byte[]{ESC, 'E', 0});    // Negrita OFF
        bytes(new byte[]{GS, '!', 0});     // Tamaño normal
    }

    private void left() {
        bytes(new byte[]{ESC, 'a', 0});
    }

    private void center() {
        bytes(new byte[]{ESC, 'a', 1});
    }

    private void normal() {
        bytes(new byte[]{GS, '!', 0});
        bytes(new byte[]{ESC, 'E', 0});
        left();
    }

    private void big() {
        // Doble ancho/alto.
        bytes(new byte[]{GS, '!', 0x11});
        bytes(new byte[]{ESC, 'E', 1});
        center();
    }

    /**
     * Logo rápido:
     * GS v 0 + imagen 1 bit.
     *
     * Se construye directamente dentro del buffer.
     * No se hace out.write() por cada fila.
     */
    private void logo(Bitmap source) {
        if (source == null) return;

        Bitmap bmp = source;

        if (bmp.getWidth() > MAX_WIDTH_DOTS) {
            int newH = Math.max(1,
                    bmp.getHeight() * MAX_WIDTH_DOTS / bmp.getWidth());
            bmp = Bitmap.createScaledBitmap(
                    bmp, MAX_WIDTH_DOTS, newH, true);
        }

        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        final int widthBytes = (width + 7) / 8;

        // GS v 0 m xL xH yL yH
        bytes(new byte[]{
                GS, 'v', '0', 0,
                (byte)(widthBytes & 0xFF),
                (byte)((widthBytes >> 8) & 0xFF),
                (byte)(height & 0xFF),
                (byte)((height >> 8) & 0xFF)
        });

        for (int y = 0; y < height; y++) {
            for (int xb = 0; xb < widthBytes; xb++) {

                int value = 0;

                for (int bit = 0; bit < 8; bit++) {
                    int x = xb * 8 + bit;
                    if (x >= width) continue;

                    int pixel = bmp.getPixel(x, y);

                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int bl = pixel & 0xFF;

                    int gray = (r * 299 + g * 587 + bl * 114) / 1000;

                    if (gray < 160) {
                        value |= (1 << (7 - bit));
                    }
                }

                b((byte)value);
            }
        }

        b((byte)0x0A);
    }

    /**
     * Imprime el ticket.
     *
     * NO poner Thread.sleep() entre estas líneas.
     */
    public void imprimirTicket(
            Context context,
            String contrato,
            String fechaHora,
            String lectura,
            String obs,
            String cuenta,
            String municipio,
            String nombre,
            String direccion,
            String medidor,
            String inspector
    ) throws IOException {

        buffer.reset();

        init();

        // ---------- LOGO ----------
        center();

        int logoId = context.getResources().getIdentifier(
                "emsa_logo_pt582",
                "drawable",
                context.getPackageName());

        Bitmap logoBmp = BitmapFactory.decodeResource(
                context.getResources(), logoId);

        logo(logoBmp);

        // ---------- TEXTO ----------
        normal();

        line("Contrato : " + contrato);
        line("Estimado usuario el dia de hoy");
        line(fechaHora + " Estuvimos");
        line("realizando la toma de lectura con");
        line("reporte");

        // ---------- LECTURA ----------
        big();
        line("Lectura : " + lectura);
        normal();

        line("OBS: 0 : " + obs);
        line("Cuenta    : " + cuenta);
        line("Municipio : " + municipio);

        wrapped("Nombre    : " + nombre, 42);
        wrapped("Direccion : " + direccion, 42);

        line("Medidor   : " + medidor);
        line("Inspector : " + inspector);

        wrapped("PARA MAYOR INFORMACION COMUNIQUESE", 42);
        wrapped("LA LINEA WHATSAPP 3102305947", 42);

        line("Proceso de Lectura elaborado");
        line("por INELMA SAS NIT. 800095465-0");
        line("Version Ver1.4.2-260809");

        // Solo 1-2 saltos.
        b((byte)0x0A);
        b((byte)0x0A);

        // Corte. Si tu PT582 no tiene cutter, eliminar esta instrucción.
        bytes(new byte[]{GS, 'V', 66, 0});

        // UNA SOLA escritura al Bluetooth.
        sendFast();
    }

    private void wrapped(String value, int max) {
        if (value == null) return;

        String rest = value.trim();

        while (rest.length() > max) {
            int cut = rest.lastIndexOf(' ', max);

            if (cut <= 0) cut = max;

            line(rest.substring(0, cut));
            rest = rest.substring(cut).trim();
        }

        if (!rest.isEmpty()) {
            line(rest);
        }
    }

    /**
     * Envío rápido.
     *
     * No hacemos flush() después de cada línea.
     * El flush ocurre únicamente al terminar el ticket.
     */
    private void sendFast() throws IOException {
        byte[] data = buffer.toByteArray();

        // Enviar en bloques grandes para evitar demasiadas escrituras
        // al Bluetooth RFCOMM.
        final int CHUNK = 4096;

        int pos = 0;

        while (pos < data.length) {
            int count = Math.min(CHUNK, data.length - pos);
            out.write(data, pos, count);
            pos += count;
        }

        out.flush();
    }
}
