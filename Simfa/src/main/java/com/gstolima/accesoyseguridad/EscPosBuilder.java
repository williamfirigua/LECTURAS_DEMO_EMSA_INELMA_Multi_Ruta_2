package com.gstolima.accesoyseguridad;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
public class EscPosBuilder {
    private final ByteArrayOutputStream buffer;
    private final Charset charset = Charset.forName("ISO-8859-1");

    // ===== COMANDOS BASE =====
    private static final byte[] ESC_INIT = new byte[]{0x1B, 0x40};
    private static final byte[] ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    private static final byte[] ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};
    private static final byte[] ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};

    private static final byte[] BOLD_ON = new byte[]{0x1B, 0x45, 0x01};
    private static final byte[] BOLD_OFF = new byte[]{0x1B, 0x45, 0x00};

    private static final byte[] CUT_PAPER = new byte[]{0x1D, 0x56, 0x41, 0x00};
    // Comandos ESC/POS
    private static final byte[] FONT_B = {0x1B, 0x4D, 0x01}; // Fuente B
    private static final byte[] LINE_SMALL = {0x1B, 0x33, 0x10}; // Interlineado
    private static final byte[] NORMAL = {0x1D, 0x21, 0x00}; // Tamaño normal

    private static final byte[] DENSIDAD = new byte[] { 0x1D, 0x28, 0x4B, 0x02, 0x00, 0x31, (byte) 0xFC};


    public EscPosBuilder() {
        buffer = new ByteArrayOutputStream();
        init();
    }

    // ===== INICIALIZAR =====
    /*private void init() {
        write(ESC_INIT);
    }*/
    public EscPosBuilder init() {
        //buffer.write(0x1B);
        //buffer.write(0x40);
        write(ESC_INIT);
        return this;
    }

    // ===== ALINEACIÓN =====
    public EscPosBuilder left() {
        write(ALIGN_LEFT);
        return this;
    }

    public EscPosBuilder center() {
        write(ALIGN_CENTER);
        return this;
    }

    public EscPosBuilder densidad() {
        write(DENSIDAD);
        return this;
    }

    // ===== TONO (que tan negro quema el papel) =====

    /**
     * Ax: 1 = lo mas claro, 5 = lo mas oscuro. 3 es lo que traen de fabrica.
     *
     * Bajarlo tiene tres efectos a la vez: la impresion sale mas clara, el cabezal calienta
     * menos tiempo cada punto (MENOS BATERIA) y el ticket sale un poco mas rapido. Si con 2
     * el papel sale muy palido, subir a 3; si se alcanza a leer bien, probar 1.
     *
     * Ojo: el gasto no depende solo de esto sino de CUANTOS puntos se queman. El logo actual
     * tiene 33% de tinta en su recuadro; es lo que mas consume de todo el ticket.
     */
    public static int TONO_IMPRESION = 1;

    /**
     * Ax: APAGADO por defecto, y es a proposito.
     *
     * *** NO USAR ESC 7 EN ESTA IMPRESORA ***
     * Se probo en campo el 02/10/2026 mandando ESC 7 6 55 3 al principio del ticket. La
     * StarPOS A200U NO implementa ese comando: no lo descarta limpiamente sino que se
     * desincroniza, y a partir de ahi leyo el GS v 0 del logo y sus 1160 bytes de imagen como
     * si fueran caracteres. Resultado: el logo salio convertido en basura. Se comprobo
     * comparando los dos IMPRIMIR.BIN byte a byte: el raster que manda la app era IDENTICO en
     * los dos, lo que cambio fue como lo leyo la impresora.
     *
     * Lo que queda disponible es GS ( K, que es el unico comando de densidad que esta impresora
     * ya venia recibiendo sin romperse (densidad() lo usa en la rama de LEIDO == "5").
     * Encender esto SOLO para probar, y revisando el ticket siguiente.
     */
    public static boolean TONO_ACTIVO = false;

    // Nivel -> parametro m de GS ( K <fn=49>. 250..255 aclaran (-6..-1), 0 es el de fabrica,
    // 1..6 oscurecen. Mas claro = menos quemado = menos bateria.
    private static final int[] DENSIDAD_POR_NIVEL = {0, 250, 252, 0, 3, 6};

    public EscPosBuilder tono() {
        return tono(TONO_IMPRESION);
    }

    /**
     * Ajusta que tan negro quema el papel. 1 = lo mas claro (menos bateria), 5 = lo mas oscuro,
     * 3 = lo de fabrica. No manda nada si TONO_ACTIVO esta en false.
     */
    public EscPosBuilder tono(int nivel) {
        if (!TONO_ACTIVO) {
            return this;
        }
        if (nivel < 1) nivel = 1;
        if (nivel > 5) nivel = 5;
        // GS ( K pL pH fn m   (pL=2, pH=0, fn=49 = densidad de impresion)
        write(new byte[]{0x1D, 0x28, 0x4B, 0x02, 0x00, 0x31, (byte) DENSIDAD_POR_NIVEL[nivel]});
        return this;
    }
    public EscPosBuilder Fondo_B() {
        write(FONT_B);
        return this;
    }
    public EscPosBuilder Linea_SMALL() {
        write(LINE_SMALL);
        return this;
    }


    public EscPosBuilder right() {
        write(ALIGN_RIGHT);
        return this;
    }

    // ===== TEXTO =====

    /**
     * Ax: quita los bytes de control antes de mandarlos a la impresora.
     *
     * El texto del ticket sale de los archivos planos (DIRECCION, NOMBRE, INFORME, LECTOR...),
     * que son de ancho fijo y pueden traer basura en campos que nunca se escribieron. Un byte
     * de control no se imprime: la impresora lo EJECUTA. El mas delicado es 0x07 (BEL), que es
     * justamente la orden de hacer sonar el zumbador; 0x1B y 0x1D arrancarian un comando falso
     * y se comerian los caracteres siguientes.
     *
     * Se conservan el salto de linea y el tabulador, que si son legitimos.
     */
    private static String limpiarControl(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder limpio = new StringBuilder(texto.length());
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\n' || c == '\t') {
                limpio.append(c);
            } else if (c >= 0x20 && c != 0x7F) {
                limpio.append(c);
            }
            // el resto (0x00-0x1F y 0x7F) se descarta
        }
        return limpio.toString();
    }

    public EscPosBuilder text(String text) {
        write(limpiarControl(text).getBytes(charset));
        return this;
    }

    public EscPosBuilder line(String text) {
        write(limpiarControl(text).getBytes(charset));
        newLine();
        return this;
    }

    public EscPosBuilder newLine() {
        write("\n".getBytes());
        return this;
    }

    /**
     * Ax: parte 'texto' en varias líneas de máximo 'maxAncho' caracteres,
     * sin cortar palabras a la mitad (busca el último espacio que quepa).
     * A diferencia de dejar que la impresora haga su propio wrap automático
     * al recibir una línea larga (mismo número de líneas físicas resultante,
     * pero corte de palabra donde caiga, sin control), esto controla
     * exactamente dónde se parte cada línea.
     *
     * Ancho recomendado para esta StarPOS (papel 2" / 58mm, fuente B): 42.
     * Confirmar visualmente con una impresión de prueba antes de asumirlo
     * como definitivo - el margen real puede variar según firmware.
     */
    public EscPosBuilder lineaEnvuelta(String texto, int maxAncho) {
        if (texto == null || texto.trim().isEmpty()) {
            return this;
        }

        String[] palabras = texto.trim().split("\\s+");
        StringBuilder lineaActual = new StringBuilder();

        for (String palabra : palabras) {
            if (lineaActual.length() == 0) {
                lineaActual.append(palabra);
            } else if (lineaActual.length() + 1 + palabra.length() <= maxAncho) {
                lineaActual.append(" ").append(palabra);
            } else {
                line(lineaActual.toString());
                lineaActual.setLength(0);
                lineaActual.append(palabra);
            }
        }
        if (lineaActual.length() > 0) {
            line(lineaActual.toString());
        }
        return this;
    }

    public EscPosBuilder feed(int lines) {
        for (int i = 0; i < lines; i++) {
            newLine();
        }
        return this;
    }

    // ===== ESTILOS =====
    public EscPosBuilder boldOn() {
        write(BOLD_ON);
        return this;
    }

    public EscPosBuilder boldOff() {
        write(BOLD_OFF);
        return this;
    }

    // ===== LOGO =====
    public EscPosBuilder logo(Bitmap bitmap) {
        if (bitmap != null) {
            byte[] imageCmd = EscPosImageHelper.bitmapToEscPos(bitmap);
            write(imageCmd);
            newLine();
        }
        return this;
    }

    /**
     * Ax: MEJORA - guarda el logo en la memoria NV de la impresora. Llamar
     * UNA SOLA VEZ (no en cada ticket); después de esto usar logoGuardado()
     * en vez de logo() para no reenviar el bitmap completo cada impresión.
     * Ver advertencia de compatibilidad en EscPosImageHelper.
     */
    public EscPosBuilder definirLogoGuardado(Bitmap bitmap, int numeroImagen) {
        if (bitmap != null) {
            write(EscPosImageHelper.comandoDefinirImagenNV(bitmap, numeroImagen));
        }
        return this;
    }

    /**
     * Ax: imprime el logo ya guardado con definirLogoGuardado() - comando
     * corto de 4 bytes en vez de reenviar el bitmap completo. Usar esto en
     * el flujo normal de impresión una vez confirmado que la impresora
     * soporta imagen NV.
     */
    public EscPosBuilder logoGuardado(int numeroImagen) {
        write(EscPosImageHelper.comandoImprimirImagenNV(numeroImagen, 0));
        newLine();
        return this;
    }

    // ===== CORTE =====

    /**
     * Ax: false = la impresora NO tiene cortador (los portatiles de 58 mm casi nunca lo traen).
     * En false, cut() saca papel en vez de mandar GS V.
     *
     * Queda en FALSE por defecto. Varias impresoras responden con el zumbador de error a un
     * GS V que no pueden ejecutar, y en esta StarPOS A200U es lo unico que quedaba sin probar
     * de verdad en el ticket. Si resulta que la impresora si corta y lo quieren de vuelta,
     * poner true.
     */
    public static boolean IMPRESORA_CON_CORTADOR = false;

    public EscPosBuilder cut() {
        if (IMPRESORA_CON_CORTADOR) {
            write(CUT_PAPER);
        } else {
            // Sin cortador, lo unico que hace falta es sacar el papel para poder romperlo.
            feed(2);
        }
        return this;
    }

    // ===== OBTENER RESULTADO =====
    public byte[] build() {
        return buffer.toByteArray();
    }

    // ===== WRITE SEGURO =====
    private void write(byte[] data) {
        try {
            buffer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public EscPosBuilder textSize(int ancho, int alto) {

        byte[] comando = new byte[] {
                0x1D,
                0x21,
                (byte) (((ancho - 1) << 4) | (alto - 1))
        };

        write(comando);

        return this;
    }
}