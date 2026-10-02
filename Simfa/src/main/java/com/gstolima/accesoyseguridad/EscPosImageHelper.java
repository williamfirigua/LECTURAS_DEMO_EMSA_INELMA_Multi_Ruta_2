package com.gstolima.accesoyseguridad;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;

public class EscPosImageHelper {

    /**
     * Ancho util del papel de 58 mm a 203 dpi: 384 puntos = 48 bytes por fila.
     * Si el raster declara mas que esto, la impresora descarta el comando GS v 0 y se pone a
     * leer los bytes de la imagen como si fueran comandos: de ahi la basura y el pitido.
     * Para papel de 80 mm son 576 puntos (72 bytes).
     */
    public static final int MAX_DOTS_58MM = 384;
    public static final int MAX_DOTS_80MM = 576;

    /**
     * Deja el logo al ancho con el que se va a imprimir, en PUNTOS de la impresora.
     *
     * Dos reglas:
     *  - Nunca pasa del ancho del papel. Un raster mas ancho no se imprime, se descarta.
     *  - Nunca amplia. El logo es de 1 bit; ampliarlo solo lo deja dentado en termica. Si se
     *    necesita mas grande, hay que reemplazar el PNG por uno de mas pixeles, no estirar este.
     *
     * El ancho final se baja al multiplo de 8 mas cercano porque el raster viaja por bytes:
     * con un ancho que no sea multiplo de 8, los bits sobrantes de cada fila salen como relleno.
     *
     * @param anchoDeseadoDots ancho al que se quiere imprimir, en puntos
     * @param maxDots          ancho util del papel (MAX_DOTS_58MM / MAX_DOTS_80MM)
     */
    public static Bitmap prepararLogo(Bitmap origen, int anchoDeseadoDots, int maxDots) {
        if (origen == null) {
            return null;
        }
        int ancho = Math.min(anchoDeseadoDots, maxDots);
        ancho = Math.min(ancho, origen.getWidth());   // no ampliar
        ancho = (ancho / 8) * 8;
        if (ancho <= 0) {
            ancho = 8;
        }
        if (ancho == origen.getWidth()) {
            return origen;
        }
        int alto = Math.max(1, Math.round(origen.getHeight() * (ancho / (float) origen.getWidth())));
        return Bitmap.createScaledBitmap(origen, ancho, alto, true);
    }

    /**
     * Ax: comando original - manda el bitmap COMPLETO en cada impresión
     * (GS v 0). Se mantiene para compatibilidad y como fallback si la
     * impresora no soporta imagen guardada en NV (ver más abajo).
     */
    public static byte[] bitmapToEscPos(Bitmap bitmap) {
        return bitmapToEscPos(bitmap, MAX_DOTS_58MM);
    }

    /**
     * Igual que el anterior pero recortando al ancho del papel.
     *
     * Devuelve un arreglo VACIO (no imprime logo) antes que mandar una cabecera que no cuadre
     * con los datos: si GS v 0 anuncia mas bytes de los que van detras, la impresora se come el
     * resto del ticket creyendo que es imagen; si anuncia menos, los bytes sobrantes se leen
     * como comandos. Preferible un ticket sin logo a un ticket ilegible.
     */
    public static byte[] bitmapToEscPos(Bitmap bitmap, int maxDots) {
        if (bitmap == null) {
            return new byte[0];
        }
        int width = Math.min(bitmap.getWidth(), maxDots);
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return new byte[0];
        }
        int widthBytes = (width + 7) / 8;

        byte[] datos;
        try {
            datos = empaquetarBitsMonocromo(bitmap, width, height, widthBytes);
        } catch (Exception e) {
            return new byte[0];
        }
        if (datos.length != widthBytes * height) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(0x1D);
        baos.write(0x76);
        baos.write(0x30);
        baos.write(0x00);
        baos.write(widthBytes & 0xFF);
        baos.write((widthBytes >> 8) & 0xFF);
        baos.write(height & 0xFF);
        baos.write((height >> 8) & 0xFF);

        baos.write(datos, 0, datos.length);

        return baos.toByteArray();
    }

    /**
     * Ax: MEJORA - guarda el bitmap en la memoria NV (no volátil) de la
     * impresora, comando estándar Epson "FS q" (define NV bit image).
     * Se manda UNA SOLA VEZ; el dato sobrevive apagados de la impresora
     * porque queda en su propio hardware, no en la app.
     *
     * *** PROBADO EN STARPOS A200U: NO SOPORTADO ***
     * El comando se envía sin error (Bluetooth entrega los bytes bien) pero
     * la impresora lo descarta en silencio - no imprime nada al usar
     * logoGuardado() después. NO usar en el flujo real con este modelo.
     * Se deja el código por si en el futuro cambian de impresora a una que
     * sí lo soporte (Epson TM-* originales sí lo implementan normalmente).
     *
     * @param numeroImagen 1-255, identifica la imagen guardada (usar 1 si solo hay un logo)
     */
    public static byte[] comandoDefinirImagenNV(Bitmap bitmap, int numeroImagen) {
        if (bitmap == null) {
            return new byte[0];
        }
        int width = Math.min(bitmap.getWidth(), MAX_DOTS_58MM);
        int height = bitmap.getHeight();
        int widthBytes = (width + 7) / 8;

        byte[] datos;
        try {
            datos = empaquetarBitsMonocromo(bitmap, width, height, widthBytes);
        } catch (Exception e) {
            return new byte[0];
        }
        if (datos.length != widthBytes * height) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(0x1C);
        baos.write(0x71);
        baos.write(1); // n = cantidad de imágenes definidas en este comando (1 = solo el logo)
        baos.write(widthBytes & 0xFF);
        baos.write((widthBytes >> 8) & 0xFF);
        baos.write(height & 0xFF);
        baos.write((height >> 8) & 0xFF);

        baos.write(datos, 0, datos.length);

        return baos.toByteArray();
    }

    /**
     * Ax: comando corto que IMPRIME la imagen ya guardada con
     * comandoDefinirImagenNV() - "FS p" (print NV bit image). Reemplaza el
     * envío del bitmap completo por 4 bytes fijos, sin importar qué tan
     * pesado sea el logo.
     *
     * @param numeroImagen mismo número usado al definirla
     * @param modo 0=normal, 1=doble ancho, 2=doble alto, 3=doble ancho y alto
     */
    public static byte[] comandoImprimirImagenNV(int numeroImagen, int modo) {
        return new byte[]{0x1C, 0x70, (byte) numeroImagen, (byte) modo};
    }

    /**
     * Empaqueta a 1 bit por punto, 8 puntos por byte, el bit mas significativo a la izquierda.
     *
     * Se lee fila por fila con getPixels() en vez de getPixel() punto por punto: con un logo de
     * 400x160 eran 64.000 llamadas a JNI por cada ticket.
     */
    private static byte[] empaquetarBitsMonocromo(Bitmap bitmap, int width, int height, int widthBytes) {
        int anchoReal = bitmap.getWidth();
        int[] fila = new int[anchoReal];
        byte[] salida = new byte[widthBytes * height];
        int pos = 0;

        for (int y = 0; y < height; y++) {
            bitmap.getPixels(fila, 0, anchoReal, 0, y, anchoReal, 1);
            for (int x = 0; x < widthBytes * 8; x += 8) {
                int b = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int xx = x + bit;
                    if (xx < width) {
                        int pixel = fila[xx];
                        int gray = (Color.red(pixel) +
                                Color.green(pixel) +
                                Color.blue(pixel)) / 3;
                        // Un punto transparente es papel, no tinta. Sin mirar el alpha, un PNG
                        // con fondo transparente y canal RGB en negro sale como un cuadro negro.
                        if (Color.alpha(pixel) >= 128 && gray < 128) {
                            b |= (1 << (7 - bit));
                        }
                    }
                }
                salida[pos++] = (byte) b;
            }
        }
        return salida;
    }

}