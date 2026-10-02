package com.Util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reduccion de fotos con EXACTAMENTE el mismo criterio de compresion de Utils.ReduceImagen2
 * original:
 *  - Rango objetivo 40-60 KB.
 *  - Pasadas de lado mayor 1080 -> 900 -> 720 -> 540 -> 420 (misma regla de getResizedBitmap2).
 *  - Busqueda binaria de calidad JPEG en [30, 95], maximo 7 iteraciones, se detiene en la
 *    primera calidad que cae dentro del rango.
 *  - Marca de agua roja, texto 16px, negrita, en (15, 17).
 *  - Origen < 50 KB o resultado < 10 KB => rechazo.
 *
 * Solo se corrigen defectos, sin tocar la compresion:
 *  - Se aplica la rotacion EXIF antes de reducir (Camera1 en muchos equipos solo marca EXIF).
 *  - El bitmap sobre el que se dibuja siempre es mutable (antes podia lanzar
 *    IllegalStateException en new Canvas() y la foto se descartaba).
 *  - Si ninguna combinacion cae en el rango, se guarda la mas cercana (antes se devolvia
 *    "Advertencia" y el llamador BORRABA la foto).
 *  - Escritura atomica.
 *  - Debe ejecutarse fuera del hilo principal.
 */
public final class ImageReducer {

    private static final String TAG = "ImageReducer";

    public static final class Config {
        public int[] longSidePasses = {1080, 900, 720, 540, 420};
        public long targetMinBytes = 40_000;
        public long targetMaxBytes = 60_000;
        public long minSourceBytes = 50_000;
        public long minResultBytes = 10_000;
        public int minQuality = 30;
        public int maxQuality = 95;
        public int maxQualityIterations = 7;
        public float watermarkTextSize = 16f;
        public float watermarkX = 15f;
        public float watermarkY = 17f;

        public static Config defaults() {
            return new Config();
        }
    }

    public static final class Result {
        public final boolean ok;
        public final boolean inTargetRange;
        public final String error;
        public final int width;
        public final int height;
        public final int quality;
        public final long bytes;

        private Result(boolean ok, boolean inTargetRange, String error, int width, int height, int quality, long bytes) {
            this.ok = ok;
            this.inTargetRange = inTargetRange;
            this.error = error;
            this.width = width;
            this.height = height;
            this.quality = quality;
            this.bytes = bytes;
        }

        static Result success(boolean inRange, int w, int h, int q, long bytes) {
            return new Result(true, inRange, "", w, h, q, bytes);
        }

        static Result failure(String error) {
            return new Result(false, false, error, 0, 0, 0, 0);
        }

        @Override
        public String toString() {
            return ok
                    ? ("OK " + width + "x" + height + " q=" + quality + " " + bytes + "B" + (inTargetRange ? "" : " (fuera de rango)"))
                    : ("ERROR " + error);
        }
    }

    /** Abstraccion minima de origen/destino para soportar File y Uri con el mismo algoritmo. */
    public interface ImageStore {
        InputStream openInput() throws IOException;

        long length();

        void write(byte[] jpeg) throws IOException;
    }

    private ImageReducer() {
    }

    public static Result reduce(File file, String watermark, Config cfg) {
        return reduce(new FileStore(file), watermark, cfg);
    }

    public static Result reduce(ContentResolver resolver, Uri uri, String watermark, Config cfg) {
        return reduce(new UriStore(resolver, uri), watermark, cfg);
    }

    public static Result reduce(ImageStore store, String watermark, Config cfg) {
        Bitmap base = null;
        Bitmap working = null;
        try {
            long sourceBytes = store.length();
            if (sourceBytes >= 0 && sourceBytes < cfg.minSourceBytes) {
                return Result.failure("Tamano < 50kb, de: " + sourceBytes);
            }

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            try (InputStream in = store.openInput()) {
                BitmapFactory.decodeStream(in, null, bounds);
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
                return Result.failure("decodeBounds invalido (" + bounds.outWidth + "x" + bounds.outHeight + ")");
            }

            BitmapFactory.Options decode = new BitmapFactory.Options();
            decode.inSampleSize = sampleSizeFor(bounds.outWidth, cfg.longSidePasses[0]);
            try (InputStream in = store.openInput()) {
                base = BitmapFactory.decodeStream(in, null, decode);
            }
            if (base == null) {
                return Result.failure("no se pudo decodificar la imagen");
            }
            base = rotate(base, readExifRotation(store));

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            paint.setFakeBoldText(true);
            paint.setTextSize(cfg.watermarkTextSize);
            String texto = watermark == null ? "" : watermark;

            ByteArrayOutputStream baos = new ByteArrayOutputStream(96 * 1024);
            byte[] best = null;
            long bestDiff = Long.MAX_VALUE;
            int bestW = 0, bestH = 0, bestQ = 0;
            boolean hit = false;

            for (int longSide : cfg.longSidePasses) {
                working = mutableResized(base, longSide);
                new Canvas(working).drawText(texto, cfg.watermarkX, cfg.watermarkY, paint);

                int low = cfg.minQuality;
                int high = cfg.maxQuality;
                for (int i = 0; i < cfg.maxQualityIterations && low <= high; i++) {
                    int mid = (low + high) / 2;
                    baos.reset();
                    if (!working.compress(Bitmap.CompressFormat.JPEG, mid, baos)) {
                        throw new IllegalStateException("Bitmap.compress fallo con calidad " + mid);
                    }
                    long size = baos.size();
                    long diff = size < cfg.targetMinBytes ? cfg.targetMinBytes - size
                            : size > cfg.targetMaxBytes ? size - cfg.targetMaxBytes
                            : 0;

                    if (diff < bestDiff) {
                        bestDiff = diff;
                        best = baos.toByteArray();
                        bestW = working.getWidth();
                        bestH = working.getHeight();
                        bestQ = mid;
                    }
                    if (diff == 0) {
                        hit = true;
                        break;
                    } else if (size < cfg.targetMinBytes) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }

                working.recycle();
                working = null;
                if (hit) {
                    break;
                }
            }

            if (best == null) {
                return Result.failure("no se genero buffer de imagen");
            }
            if (best.length < cfg.minResultBytes) {
                return Result.failure("Tamano < 10kb, de: " + best.length);
            }

            store.write(best);
            Result r = Result.success(hit, bestW, bestH, bestQ, best.length);
            if (hit) {
                Log.i(TAG, r.toString());
            } else {
                Log.w(TAG, r + " -> no se alcanzo " + cfg.targetMinBytes + "-" + cfg.targetMaxBytes + " B; se conserva la mas cercana");
            }
            return r;

        } catch (OutOfMemoryError oom) {
            Log.e(TAG, "OOM reduciendo imagen", oom);
            return Result.failure("Memoria insuficiente al procesar la foto");
        } catch (Exception ex) {
            Log.e(TAG, "Error reduciendo imagen", ex);
            return Result.failure(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } finally {
            if (working != null && !working.isRecycled()) working.recycle();
            if (base != null && !base.isRecycled()) base.recycle();
        }
    }

    /** Igual que Utils.calculateInSampleSize: basado en el ancho decodificado. */
    static int sampleSizeFor(int sourceWidth, int reqWidth) {
        int sample = 1;
        while ((sourceWidth / (sample * 2)) >= reqWidth) {
            sample *= 2;
        }
        return sample;
    }

    /**
     * Mismas dimensiones que Utils.getResizedBitmap2 (el lado mayor queda en maxSize),
     * pero garantizando un bitmap nuevo y mutable para poder dibujar la marca de agua.
     */
    private static Bitmap mutableResized(Bitmap src, int maxSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        float ratio = (float) width / (float) height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        Bitmap scaled = Bitmap.createScaledBitmap(src, width, height, true);
        if (scaled != src && scaled.isMutable()) {
            return scaled;
        }
        Bitmap copy = scaled.copy(Bitmap.Config.ARGB_8888, true);
        if (scaled != src) {
            scaled.recycle();
        }
        if (copy == null) {
            throw new IllegalStateException("No se pudo crear copia mutable del bitmap");
        }
        return copy;
    }

    private static Bitmap rotate(Bitmap src, int degrees) {
        if (degrees == 0) {
            return src;
        }
        Matrix m = new Matrix();
        m.postRotate(degrees);
        Bitmap rotated = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
        if (rotated != src) {
            src.recycle();
        }
        return rotated;
    }

    private static int readExifRotation(ImageStore store) {
        try (InputStream in = store.openInput()) {
            int orientation = new ExifInterface(in)
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            Log.w(TAG, "No se pudo leer EXIF, se asume sin rotacion: " + e.getMessage());
            return 0;
        }
    }

    // ------------------------------------------------------------------ Stores

    public static final class FileStore implements ImageStore {
        private final File file;

        public FileStore(File file) {
            this.file = file;
        }

        @Override
        public InputStream openInput() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public long length() {
            return file.length();
        }

        /** Escritura atomica: temporal oculto con extension de imagen (valida en DCIM bajo scoped storage) + rename. */
        @Override
        public void write(byte[] jpeg) throws IOException {
            File tmp = new File(file.getParentFile(), "." + file.getName() + ".part.jpg");
            try (FileOutputStream out = new FileOutputStream(tmp)) {
                out.write(jpeg);
                out.getFD().sync();
            } catch (IOException e) {
                //noinspection ResultOfMethodCallIgnored
                tmp.delete();
                throw e;
            }
            if (!tmp.renameTo(file)) {
                //noinspection ResultOfMethodCallIgnored
                tmp.delete();
                throw new IOException("No se pudo reemplazar " + file.getAbsolutePath());
            }
        }
    }

    public static final class UriStore implements ImageStore {
        private final ContentResolver resolver;
        private final Uri uri;

        public UriStore(ContentResolver resolver, Uri uri) {
            this.resolver = resolver;
            this.uri = uri;
        }

        @Override
        public InputStream openInput() throws IOException {
            InputStream in = resolver.openInputStream(uri);
            if (in == null) {
                throw new IOException("openInputStream null para " + uri);
            }
            return in;
        }

        @Override
        public long length() {
            try (android.content.res.AssetFileDescriptor afd = resolver.openAssetFileDescriptor(uri, "r")) {
                return afd != null ? afd.getLength() : -1;
            } catch (Exception e) {
                return -1;
            }
        }

        /**
         * "wt" es obligatorio: "w" no trunca en Android 10+ y deja bytes basura del JPEG anterior.
         * Escribir por la Uri hace que MediaStore actualice tamano/dimensiones al cerrar.
         * No es atomico, por eso se sincroniza a disco, se verifica el tamano y se reintenta una vez.
         */
        @Override
        public void write(byte[] jpeg) throws IOException {
            IOException ultimo = null;
            for (int intento = 1; intento <= 2; intento++) {
                try {
                    writeOnce(jpeg);
                    return;
                } catch (IOException e) {
                    ultimo = e;
                    Log.w(TAG, "Escritura por Uri fallo (intento " + intento + "): " + e.getMessage());
                }
            }
            throw ultimo;
        }

        private void writeOnce(byte[] jpeg) throws IOException {
            try (android.os.ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "wt")) {
                if (pfd == null) {
                    throw new IOException("openFileDescriptor null para " + uri);
                }
                // El descriptor pertenece a pfd: no se cierra el stream aqui (lo cierra pfd al final),
                // asi el tamano se puede verificar antes del cierre, que es cuando MediaStore reescanea.
                FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
                out.write(jpeg);
                out.flush();
                pfd.getFileDescriptor().sync();
                long escrito = pfd.getStatSize();
                if (escrito >= 0 && escrito != jpeg.length) {
                    throw new IOException("Tamano escrito " + escrito + " != esperado " + jpeg.length);
                }
            }
        }
    }
}