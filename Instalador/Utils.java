package com.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.nio.file.*;
import android.os.Environment;

import com.gstolima.accesoyseguridad.MenuDeLiquidacion;
import com.gstolima.accesoyseguridad.VariablesGlobales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
/**
 * Created by Ax on 09/12/2015.
 */
public class Utils {

    private static boolean respuesta;
    private static boolean ciclo;
    private static final int BUFFER = 80000;
    VariablesGlobales variables = new VariablesGlobales();

    public final Logger logger = LoggerFactory.getLogger(MenuDeLiquidacion.class);
    //Ax: descomprime un zip y extrae los archivos a un path mas(folderUnzip), compara tamano y si existe el archivo
    //debe recibir la ruta completa con el zip,una carpeta y el tamaño del zip
    public String DescomprimeZip(String fullRuta, String folderUnzip, int fulltamano, boolean borrar) {

        File file = new File(fullRuta);

        if (!file.exists()) return "Descomprime(Error: no existe arc.)";
        else if (file.length() != fulltamano) return "Descomprime(Error: tamano arc.)";

        String fileName = fullRuta.substring(fullRuta.lastIndexOf('/') + 1, fullRuta.length());
        String ruta = fullRuta.replace(fileName, folderUnzip);

        File newpath = new File(ruta);
        if (!newpath.exists()) {
            if (!newpath.mkdir()) return "Descomprime(Error: creando path.)"; //No se dejo crear
        }

        try {
            String outputPath = ruta;
            ZipFile zipFile = new ZipFile(new File(file.getAbsolutePath()));

            @SuppressWarnings("unchecked")
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();//Ax. obtiene la lista de los archivos del zip

            for (FileHeader fileHeader : fileHeaders) {

                String filename = fileHeader.getFileName();

                if (filename.contains("/")) {
                    filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length()); //Android
                } else {
                    filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length()); //Windows
                }
                zipFile.extractFile(fileHeader, outputPath, null, filename);//Ax: Para evitar errores:  fileheader,salida,zipparametros,nuevonombre
            }
        } catch (Exception ex) {
            return "Descomprime(Error:" + ex.getMessage() + ")";
        }

        if (borrar) {
            try {    //Ax: borrar zip si está habilitado
                file.delete();
            } catch (Exception ex) {
                return "Descomprime ✓; borrar Zip (error)";
            }
        }
        return "Descomprime ✓; borrar Zip ✓";
    }

    public String CreaZip(String zipPorCrear, ArrayList filesToAdd) {//String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipPorCrear);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < filesToAdd.size(); i++) {

                FileInputStream fi = new FileInputStream(filesToAdd.get(i).toString());
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(filesToAdd.get(i).toString().substring(filesToAdd.get(i).toString().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            //Log.e("error", "no comprime " + e.getMessage());
            return e.getMessage();
        }
        return "ok ✓";
    }

    //Ax copia un archivo a otro sobreescribiendolo si ya existe

    public boolean CrearCopia(String rutaOrigen, String rutaDestino) {

        File origen = new File(rutaOrigen);
        File destino = new File(rutaDestino);

        // Validaciones básicas
        if (!origen.exists() || !origen.isFile()) return false;

        // Crear directorio destino si no existe
        File parent = destino.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) return false;
        }



        // 2️⃣ Si renombrar falla → copiar por partes (forma segura)
        try (InputStream in = new FileInputStream(origen);
             OutputStream out = new FileOutputStream(destino)) {

            byte[] buffer = new byte[11264];//8192
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

   //NUEVO PARA VALIDAR SI SE MEJORA ESTO DE ESTA COPIA
public boolean CrearCopiaSin(String inputFullFile, String outputFullFile) {

    File inputFile = new File(inputFullFile);
    File outputFile = new File(outputFullFile);

    // 0) Evitar copiar sobre sí mismo
    if (inputFile.getAbsolutePath().equals(outputFile.getAbsolutePath())) {
        Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                "[Utils]CrearCopiaSin() | Error -> input y output son la misma ruta");
        return false;
    }

    // 1) Verifica existencia y tamaño
    if (!inputFile.exists() || inputFile.length() < 4) {
        Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                "[Utils]CrearCopiaSin() | false -> inputFile no existe o tamaño < 4 (" + inputFullFile + ")");
        return false;
    }

    // 2) Verificar contenido con la función; confirmar semántica: true = error
    boolean tieneNulos;
    try {
        tieneNulos = verificarYActuar(inputFullFile);
    } catch (Exception e) {
        Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                "[Utils]CrearCopiaSin() | Error al ejecutar verificarYActuar -> " + Log.getStackTraceString(e));
        return false;
    }
    if (tieneNulos) {
        Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                "[Utils]CrearCopiaSin() | Error -> archivo con Nulos");
        return false;
    }

    // 3) Asegurar que exista el directorio destino
    File parent = outputFile.getParentFile();
    if (parent != null && !parent.exists()) {
        if (!parent.mkdirs()) {
            Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                    "[Utils]CrearCopiaSin() | Error -> no se pudo crear directorio destino: " + parent.getAbsolutePath());
            return false;
        }
    }

    // 4) Copiar con try-with-resources (append = true según tu intención)
    try (InputStream in = new FileInputStream(inputFile);
         OutputStream out = new FileOutputStream(outputFile, true)) {

        byte[] buffer = new byte[11264];//16384]
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            if (bytesRead > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }
        out.flush();
        return true;

    } catch (Exception ex) {
        Log(new File(VariablesGlobales.directorioactual + "LOGEVENTOS.LOG"),
                "[Utils]CrearCopiaSin() | Error -> " + Log.getStackTraceString(ex));
        return false;
    }
}
    //FIN VALIDACION
    public static boolean verificarYActuar(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        boolean tieneNUL = false;

        try (InputStream in = new FileInputStream(archivo)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if (buffer[i] == 0x00) {
                        tieneNUL = true;
                        break;
                    }
                }
                if (tieneNUL) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false; // opcional: si no puedo leerlo, fallo
        }

        if (tieneNUL) {
            escribirLogEnRaiz("El archivo contiene NUL. Se renombrará para verificación.");

            String nuevoNombre = rutaArchivo + ".VERIFICAR";
            try {
                Files.move(archivo.toPath(), Paths.get(nuevoNombre), StandardCopyOption.REPLACE_EXISTING);
                escribirLogEnRaiz("Error en archivo de respaldo enviogprs" + nuevoNombre);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return tieneNUL;
    }
    public static void escribirLogEnRaiz(String mensaje) {
        try {
            File carpeta = new File(Environment.getExternalStorageDirectory(), "LECTURAMEDIDORES");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            File archivoLog = new File(carpeta, "LOGEVENTOS.log");

            FileWriter writer = new FileWriter(archivoLog, true); // true = append
            BufferedWriter bw = new BufferedWriter(writer);

            bw.write(mensaje);
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean CrearCopiaSinAnterior(String inputFullFile, String outputFullFile) {

        File inn = new File(inputFullFile);
        if (!inn.exists() || inn.length() < 4) {
            return false;
        }

        InputStream in = null;
        OutputStream out = null;
        try {

            File dir = new File(outputFullFile);
            if (!dir.exists()) dir.createNewFile();

            in = new FileInputStream(inputFullFile);
            out = new FileOutputStream(outputFullFile, true);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();// write the output file
            out.close();
            out = null;

        } catch (FileNotFoundException fe) {
            //Log.e("error", "no crea coipa1 " + fe.getMessage());
            return false;
        } catch (Exception ex) {
            //Log.e("error", "no crea coipa " + ex.getMessage());
            return false;
        }
        return true;
    }
    /**
     * Trata de crear un archivo, sobrescribiendo siempre, si falla no hace nada (ojo crea nueva linea tambien)
     *
     * @param logFile ruta completa en SDcard + archivo.log
     * @param msg     mensage
     */
    public void WriteLine(File logFile, String msg) {

        try {
            if (!logFile.exists()) logFile.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, false));//append
            bufferedWriter.write(msg + "\r\n");
            bufferedWriter.close();

        } catch (Exception e) {
            //Log.e("error", "no escribe " + e.getMessage());
            return;
        }
    }

    /**
     * Trata de crear un loG de eventos en datos de entrada, si falla no hace nada
     *
     * @param logFile ruta completa en SDcard +archivo.log
     * @param msg     mensage
     */
    public void Log(File logFile, String msg) {
        try {
            if (!logFile.exists()) logFile.createNewFile();
            Date d = new Date();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));//append true
            bufferedWriter.write(d.toString() + " | " + msg + "\r\n");
            bufferedWriter.close();

            if (logFile.length() > 1000000) logFile.delete(); //Ax: si tiene mas de 1 mega lo borra

        } catch (Exception e) {
            return;
        }
    }

    /**
     * Trata de crear un archivo, añadiendo texto siempre, si falla no hace nada, no añade nueva linea y no sobreescribe, solo añade
     *
     * @param logFile ruta completa en SDcard + archivo.log
     * @param msg     mensage
     */
    public Boolean EscribirLinea(File logFile, String msg) {

        try {
            if (!logFile.exists()) logFile.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));//append true, añade a lo que ya existe
            bufferedWriter.write(msg);
            bufferedWriter.close();
            return true;

        } catch (Exception e) {
            logger.info("[Utils]EscribirLinea() ERROR : " + e.getMessage());
            //Log.e("error","pasa por aqui 3 "+e.getMessage());
            return false;
        }
    }

    /***
     * Trata de leer la linea de texto del archivo de parametro. Si no existe el archivo lo crea
     *
     * @param txtFile
     * @return
     */
    public String ReadLine(File txtFile) {

        FileReader r = null;

        try {
            if (!txtFile.exists()) txtFile.createNewFile();

            r = new FileReader(txtFile);
            BufferedReader reader = new BufferedReader(r);
            String linea = "";

            while ((linea = reader.readLine()) != null) {

                if (!linea.trim().equals("")) return linea.trim();
            }
        } catch (IOException e) {
        } finally {
            try {
                if (r != null) r.close();
            } catch (IOException e) {
            }

        }
        return "";
    }

    public double parseStringToDouble(String x) {
        try {
            double y = Double.parseDouble(x.trim());
            return y;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error.Double parseo " + x);
        } catch (Exception e) {
            throw new RuntimeException("Error.Double parseo " + x);
        }
    }

    public int parseStringToInteger(String x) {
        try {
            if (x.contains(".")) {
                int y = (int) parseStringToDouble(x);
                return y;
            }
            int y = Integer.parseInt(x.trim());
            return y;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error.Integer parseo " + x);
        } catch (Exception e) {
            throw new RuntimeException("Error.Integer parseo " + x);
        }
    }

    public String Md5Hash(String path) { //genera un HASH MD5  de un archivo y devuelve la cadena en mayusculas
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(path);

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (Exception ex) {
            return "";
        }
    }

    public String ReduceImagen2old(String filePath, String mensajeFoto) {//rx

        int qualitys = 60;
        InputStream in = null;
        OutputStream out = null;
        File fotoTomada = new File(filePath);
        File fotoTemp = new File(filePath.replace(".jpg", "temp.jpg"));
        Canvas canvas = null;
        Bitmap bitmap = null;
        try {
            //Random random = new Random();  ||  random.nextBoolean()

            if (fotoTomada.length() < 400000) {
                return "Tamaño < 400kb, de: " + fotoTomada.length(); //foto negra 500k //rx
            }

            if (fotoTomada.length() < 2000000) {
                qualitys = 68;
            }

            in = new FileInputStream(fotoTomada);
            bitmap = BitmapFactory.decodeStream(in);
//
//            Matrix rotateMatrix = new Matrix();  //Nuevo, Las fotos salen horizontales hay que voltearlas (ver camara nueva)setRutayNombreFoto
//            rotateMatrix.postRotate(90);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);  //fin Nuevo,

            bitmap = getResizedBitmap2(bitmap, 640); //Ax: Todo: cambiar cuando la camara se coloca horizontal
            out = new FileOutputStream(fotoTemp);

            Paint paint = new Paint();
            paint.setColor(Color.RED); // Text Color alpha de 98%, rojo
            paint.setStrokeWidth(2); // Text Size
            paint.setFakeBoldText(true);
            paint.setTextSize(16); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // text Overlapping Pattern

            for (int i = 0; i < 15; i++) {

                canvas = new Canvas(bitmap); //Se inicia el proceso de escribir en la foto
                canvas.drawBitmap(bitmap, 0, 0, paint);
                canvas.drawText(mensajeFoto, 15, 17, paint);

                fotoTemp.createNewFile();
                out = new FileOutputStream(fotoTemp);

                if (bitmap.compress(Bitmap.CompressFormat.JPEG, qualitys, out)) {//60 ---ojo esta calidad depende de los mpx de la camara

                    long tam1 = fotoTemp.length();

                    if (tam1 < 13000) {
                        qualitys = qualitys + 7;
                        fotoTemp.delete();
                        continue;
                    }

                    if (tam1 > 100000) {
                        qualitys = qualitys - 30;
                        fotoTemp.delete();
                        continue;
                    }

                    if (tam1 > 70000) {
                        qualitys = qualitys - 15;
                        fotoTemp.delete();
                        continue;
                    }

                    if (tam1 > 50000) {
                        qualitys = qualitys - 9;
                        fotoTemp.delete();
                        continue;
                    }

                    if (tam1 > 40000) {
                        qualitys = qualitys - 6;
                        fotoTemp.delete();
                        continue;
                    }

                    fotoTemp.renameTo(fotoTomada); //Se borra el nuevo imagen y se le coloca el nombre de la original

                    if (fotoTomada.length() < 10000) {
                        return "Tamaño < 10kb, de: " + fotoTomada.length();//rx
                    }

                    return "";

                } else {
                    return "Fallo al salvar como JPEG";
                }
            }

            fotoTemp.renameTo(fotoTomada);

            if (fotoTomada.length() > 13000 && fotoTomada.length() < 40000)
                return "";
            else {
                return "error Foto final:" + fotoTomada.length();
            }
        } catch (Exception ex) {
            return "Foto. " + ex.getMessage();
        } finally {
            try {
                if (out != null) { //rx
                    out.close();
                }
                if (in != null) {//rx
                    in.close();
                }

                File fotoTemp_pslm = new File(filePath.replace(".jpg", "temp.jpg"));
                if (fotoTemp_pslm.exists()) fotoTemp_pslm.delete();

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (canvas != null) {
                    canvas = null;
                }

            } catch (Exception ex) {
            }
        }
    }

    /**
     * Reduce una foto ajustando resolución y calidad JPEG mediante búsqueda binaria
     * hasta converger en un rango de tamaño objetivo, con marca de agua.
     *
     * Cambios respecto a la versión original:
     *  - Ancho destino subido de 640 a TARGET_WIDTH (config) para eliminar pixelado
     *    al apuntar a un rango de tamaño mayor.
     *  - Watermark se dibuja UNA sola vez (antes se re-dibujaba en cada iteración,
     *    degradando nitidez y desperdiciando CPU).
     *  - Búsqueda binaria de calidad (máx MAX_ITERATIONS, converge en log2(rango)),
     *    en vez de incrementos/decrementos lineales sin bounds que podían oscilar
     *    sin converger.
     *  - Compresión de prueba en memoria (ByteArrayOutputStream); solo se escribe
     *    a disco UNA vez al final -> elimina I/O y fugas de FileOutputStream por
     *    iteración (el original abría un stream nuevo en cada 'continue' sin
     *    cerrar el anterior).
     *  - Decode con inSampleSize calculado (BitmapFactory.Options) para evitar
     *    cargar el bitmap completo en memoria antes de reducirlo -> menor riesgo
     *    de OutOfMemoryError con fotos de cámara de alta resolución.
     *  - try-with-resources para streams; recycle() garantizado en finally.
     */

    private static final String TAG = "ReduceImagen";

    public String ReduceImagen2(String filePath, String mensajeFoto) {

        final long MIN_SOURCE_BYTES = 400_000;
        final long TARGET_MIN_BYTES = 40_000;
        final long TARGET_MAX_BYTES = 60_000;
        final int MAX_QUALITY_ITERATIONS = 7;
        final int MIN_QUALITY = 30;
        final int MAX_QUALITY = 95;
        // Pasadas de ancho descendente: si a 1080 no cabe en el rango con ninguna calidad,
        // se prueba con menos resolución antes de rendirse.
        final int[] WIDTH_PASSES = {1080, 900, 720, 540, 420};

        File fotoTomada = new File(filePath);
        File fotoTemp = new File(fotoTomada.getParentFile(), fotoTomada.getName() + ".tmp");

        if (fotoTomada.length() < MIN_SOURCE_BYTES) {
            return "Tamaño < 400kb, de: " + fotoTomada.length();
        }

        Bitmap sampled = null;
        Bitmap working = null;
        byte[] overallBest = null;
        long overallBestDiff = Long.MAX_VALUE;
        int overallBestWidth = 0;
        int overallBestQuality = 0;
        boolean hitTarget = false;

        try {
            BitmapFactory.Options boundsOpts = new BitmapFactory.Options();
            boundsOpts.inJustDecodeBounds = true;
            try (InputStream in = new FileInputStream(fotoTomada)) {
                BitmapFactory.decodeStream(in, null, boundsOpts);
            }
            if (boundsOpts.outWidth <= 0 || boundsOpts.outHeight <= 0) {
                return "Error: decodeBounds inválido (" + boundsOpts.outWidth + "x" + boundsOpts.outHeight + ")";
            }

            int inSampleSize = calculateInSampleSize(boundsOpts, WIDTH_PASSES[0]);
            BitmapFactory.Options decodeOpts = new BitmapFactory.Options();
            decodeOpts.inSampleSize = inSampleSize;
            try (InputStream in = new FileInputStream(fotoTomada)) {
                sampled = BitmapFactory.decodeStream(in, null, decodeOpts);
            }
            if (sampled == null) {
                return "Error: no se pudo decodificar la imagen";
            }

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            paint.setFakeBoldText(true);
            paint.setTextSize(16);

            // --- Pasada externa: resolución descendente ---
            for (int width : WIDTH_PASSES) {
                working = getResizedBitmap2(sampled, width);
                boolean isNewBitmap = (working != sampled);

                // Watermark sobre el bitmap de esta pasada (dimensiones cambian entre pasadas)
                new Canvas(working).drawText(mensajeFoto, 15, 17, paint);

                // --- Búsqueda binaria de calidad para esta resolución ---
                int low = MIN_QUALITY, high = MAX_QUALITY;
                for (int i = 0; i < MAX_QUALITY_ITERATIONS && low <= high; i++) {
                    int mid = (low + high) / 2;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    working.compress(Bitmap.CompressFormat.JPEG, mid, baos);
                    byte[] buffer = baos.toByteArray();
                    long size = buffer.length;

                    long diff = size < TARGET_MIN_BYTES ? TARGET_MIN_BYTES - size
                            : size > TARGET_MAX_BYTES ? size - TARGET_MAX_BYTES
                            : 0;

                    if (diff < overallBestDiff) {
                        overallBestDiff = diff;
                        overallBest = buffer;
                        overallBestWidth = width;
                        overallBestQuality = mid;
                    }

                    if (size >= TARGET_MIN_BYTES && size <= TARGET_MAX_BYTES) {
                        hitTarget = true;
                        break;
                    } else if (size < TARGET_MIN_BYTES) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }

                if (isNewBitmap) {
                    working.recycle();
                }
                working = null;

                if (hitTarget) break; // ya encontramos algo dentro del rango, no seguir bajando resolución
            }

            if (overallBest == null) {
                return "Error: no se generó buffer de imagen";
            }

            try (OutputStream out = new FileOutputStream(fotoTemp)) {
                out.write(overallBest);
            }

            if (!fotoTemp.renameTo(fotoTomada)) {
                return "Error: no se pudo reemplazar el archivo original";
            }

            long finalSize = fotoTomada.length();
            Log.e(TAG, "Resultado: ancho=" + overallBestWidth + " calidad=" + overallBestQuality
                    + " tamaño=" + finalSize + " dentroDeRango=" + hitTarget);

            if (finalSize < 10_000) {
                return "Tamaño < 10kb, de: " + finalSize;
            }

            // Éxito real solo si cayó dentro del rango pedido; si no, se informa explícitamente
            if (!hitTarget) {
                return "Advertencia: no se alcanzó el rango " + TARGET_MIN_BYTES + "-" + TARGET_MAX_BYTES
                        + " ni reduciendo resolución hasta " + WIDTH_PASSES[WIDTH_PASSES.length - 1]
                        + "px. Mejor resultado: " + finalSize + " bytes (ancho=" + overallBestWidth
                        + ", calidad=" + overallBestQuality + ")";
            }

            return "";

        } catch (Exception ex) {
            Log.e(TAG, "Excepción reduciendo imagen: " + filePath, ex);
            return "Foto. " + ex.getClass().getSimpleName() + ": " + ex.getMessage();
        } finally {
            if (working != null && !working.isRecycled()) working.recycle();
            if (sampled != null && !sampled.isRecycled()) sampled.recycle();
            if (fotoTemp.exists()) fotoTemp.delete();
        }
    }

    public String ReduceImagen3(String filePath, String mensajeFoto) {

        final long MIN_SOURCE_BYTES = 400_000;
        final long TARGET_MIN_BYTES = 100_000;
        final long TARGET_MAX_BYTES = 150_000;
        final int MAX_ITERATIONS = 8;      // log2(95-35) ~= 6, con margen
        final int MIN_QUALITY = 35;
        final int MAX_QUALITY = 95;
        final int TARGET_WIDTH = 1080;     // antes 640px -> causa principal del pixelado

        File fotoTomada = new File(filePath);
        File fotoTemp = new File(filePath.replace(".jpg", "_temp.jpg"));

        if (fotoTomada.length() < MIN_SOURCE_BYTES) {
            return "Tamaño < 400kb, de: " + fotoTomada.length();
        }

        Bitmap resized = null;

        try {
            // --- Decode eficiente en memoria (evita cargar el bitmap full-size) ---
            BitmapFactory.Options boundsOpts = new BitmapFactory.Options();
            boundsOpts.inJustDecodeBounds = true;
            try (InputStream in = new FileInputStream(fotoTomada)) {
                BitmapFactory.decodeStream(in, null, boundsOpts);
            }
            int inSampleSize = calculateInSampleSize(boundsOpts, TARGET_WIDTH);

            BitmapFactory.Options decodeOpts = new BitmapFactory.Options();
            decodeOpts.inSampleSize = inSampleSize;
            Bitmap sampled;
            try (InputStream in = new FileInputStream(fotoTomada)) {
                sampled = BitmapFactory.decodeStream(in, null, decodeOpts);
            }
            if (sampled == null) {
                return "Error: no se pudo decodificar la imagen";
            }

            resized = getResizedBitmap2(sampled, TARGET_WIDTH);
            if (resized != sampled) {
                sampled.recycle();
            }

            // --- Watermark: se dibuja una única vez sobre el bitmap final ---
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            paint.setFakeBoldText(true);
            paint.setTextSize(16);
            new Canvas(resized).drawText(mensajeFoto, 15, 17, paint);

            // --- Búsqueda binaria de calidad JPEG sobre el rango [MIN_QUALITY, MAX_QUALITY] ---
            int low = MIN_QUALITY;
            int high = MAX_QUALITY;
            byte[] bestBuffer = null;
            long bestDiff = Long.MAX_VALUE;

            for (int i = 0; i < MAX_ITERATIONS && low <= high; i++) {
                int mid = (low + high) / 2;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, mid, baos);
                byte[] buffer = baos.toByteArray();
                long size = buffer.length;

                long diff = size < TARGET_MIN_BYTES ? TARGET_MIN_BYTES - size
                        : size > TARGET_MAX_BYTES ? size - TARGET_MAX_BYTES
                        : 0;

                if (diff < bestDiff) {
                    bestDiff = diff;
                    bestBuffer = buffer;
                }

                if (size >= TARGET_MIN_BYTES && size <= TARGET_MAX_BYTES) {
                    break; // dentro del rango objetivo
                } else if (size < TARGET_MIN_BYTES) {
                    low = mid + 1;  // subir calidad
                } else {
                    high = mid - 1; // bajar calidad
                }
            }

            if (bestBuffer == null) {
                return "Error: no se generó buffer de imagen";
            }

            try (OutputStream out = new FileOutputStream(fotoTemp)) {
                out.write(bestBuffer);
            }

            if (!fotoTemp.renameTo(fotoTomada)) {
                return "Error: no se pudo reemplazar el archivo original";
            }

            long finalSize = fotoTomada.length();
            if (finalSize < 10_000) {
                return "Tamaño < 10kb, de: " + finalSize;
            }

            return ""; // éxito

        } catch (Exception ex) {
            return "Foto. " + ex.getMessage();
        } finally {
            if (resized != null && !resized.isRecycled()) {
                resized.recycle();
            }
            if (fotoTemp.exists()) {
                fotoTemp.delete();
            }
        }
    }

    /**
     * Calcula el inSampleSize potencia de 2 más grande que mantiene
     * width/height >= reqWidth, evitando decodificar más resolución
     * de la que realmente se va a usar.
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        final int width = options.outWidth;
        int inSampleSize = 1;
        while ((width / (inSampleSize * 2)) >= reqWidth) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }
    //***fin proceso nuevo para reducir foto

    public Bitmap getResizedBitmap2(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {//ojo antes: if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public boolean leerArchivoFotos(File name, String foto) {

        if (!name.exists()) return false;

        try {
            FileReader r = new FileReader(name);
            BufferedReader reader = new BufferedReader(r);

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.length() > 30) {
                    if (linea.substring(0, 30).trim().equals(foto)) {
                        return true;
                    }
                }
            }
            return false;

        } catch (Exception ex) {
            //Log.e("error", "xxxx2 " + ex.getMessage());
            return false;// "Problema con la carga de:\n Archivo: Ciclo, Municipio...etc."
        }
    }

    public void MensajeTime(String msg, String titulo, Context context, int tiempo) { //Ax: crea un mensaje que se puede cerrar o dura 5 segundos

        try {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle(titulo);
            builder.setMessage(msg);
            builder.setCancelable(true);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            final androidx.appcompat.app.AlertDialog dlg = builder.create();

            dlg.show();

            if (tiempo < 1) tiempo = 5000;
            else tiempo = tiempo * 1000;

            final Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss();
                    timer.cancel();
                }
            }, tiempo);

        } catch (Exception ex) {
            //Todo: Implementar algo
        }
    }
//BODEBA DE FUNCIONES RECOUNSTRUIDAS PARA EL PROYECTO NUEVO
/*
    private void EnviarAlServidorFacturacion_OLD() throws IOException {//CERRADO POR VICTOR

    int abrioarchivo = 0;
    ErroresDeEnvio = 0;
    //nuevo procedimiento para guardar el archivo de resultados sin que se espere la guardada de foto y demas
    int EnviarXMaximo = 0;

    String ArchivoEnvio = VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA";
    String ArchivoCuentasNuevas = VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/CUENTASNUEVA" + serialPDA + ".SDA";


    MisEnviosHilos.archivo_EnvioGPS = ArchivoEnvio;


//Este es el proceso que onstruyeron para enviar elarchivo de enviosgprsserial por paquetes,creeria se puede quetar y ver  los resultados
    if (EnviarxBloque == 1) {
        if (MisEnviosHilos.abrir_EnvioGPS(MisEnviosHilos.archivo_EnvioGPS)) {
            MisEnviosHilos.Cerrar_EnvioGPS();
        }
        if (MisEnviosHilos.total_EnvioGPS >= parseStringToInteger(variables.maximoregaenviar.trim()) && parseStringToInteger(variables.maximoregaenviar.trim()) > 0) {
            File CopiaGPRS = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");
            if (!CopiaGPRS.exists()) {
                CrearArchivoEnvio(ArchivoEnvio);
                MisEnviosHilos.archivo_EnvioGPS = VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA";
            } else {
                if (NroEnvios == 0) {
                    NroEnvios = Math.round(MisEnviosHilos.total_EnvioGPS / parseStringToInteger(variables.maximoregaenviar.trim())) + 1;
                }
            }
            if (NroEnvios == 0) {
                NroEnvios = Math.round(MisEnviosHilos.total_EnvioGPS / parseStringToInteger(variables.maximoregaenviar.trim()));
            }
        }
    }

    if (MisEnviosHilos.abrir_EnvioGPS(MisEnviosHilos.archivo_EnvioGPS)) {
        abrioarchivo = 1;
        if (MisEnviosHilos.total_EnvioGPS > 0) {
            try {
                abrioarchivo = 2;
                try {
                    File nombreZipPorCrear;
                    nombreZipPorCrear = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/" + "Tpl" + (Municipio + Seccion + Division) + ".zip");


                    ArrayList filestoZip = new ArrayList();

                    filestoZip.add(new File(MisEnviosHilos.archivo_EnvioGPS));

                    //en esta parte deberiamos de implementar que envie al plano de nuevos y seria??
                    File cuentasNUEVAS = new File(ArchivoCuentasNuevas);

                    if (cuentasNUEVAS.exists()) {
                        filestoZip.add(new File(ArchivoCuentasNuevas));
                    }

                    String msgTemp = "";

                    Log.e("INFO", "Antes de enviar rutaAGrabarTraer ->" + (rutaAdministrador + VariablesGlobales.moduloTrabajo + CicloReal));
                    String msg = EnviarArchivo((rutaAdministrador + VariablesGlobales.moduloTrabajo + CicloReal), nombreZipPorCrear, filestoZip, trama);//to do: traer la trama desde inicio

                    if (msg.equals("ok")) {
                        enviosGPRScantidad = 1;
                        GuardarBackupEnvio(MisEnviosHilos.archivo_EnvioGPS);
                        File f = new File(MisEnviosHilos.archivo_EnvioGPS);
                        if (f.exists())
                            f.delete();

                        File RenameOri = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");
                        if (RenameOri.exists() && !f.exists()) {
                            File Original = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA");
                            // RenameOri.renameTo(Original);
                            boolean renamed = RenameOri.renameTo(Original);
                            if (!renamed) {
                                copiarArchivo(RenameOri, Original);
                                RenameOri.delete();
                            }
                        }
                        if (cuentasNUEVAS.exists()) {
                            cuentasNUEVAS.delete();
                        }

                    } else {

                        //nueva forma ojo para validar
                        File copiaFile = new File(VariablesGlobales.directorioactual +
                                "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");

                        if (copiaFile.exists()) {

                            File original = new File(VariablesGlobales.directorioactual +
                                    "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA");

                            // modo append = true
                            try (FileInputStream in = new FileInputStream(copiaFile);
                                 FileOutputStream out = new FileOutputStream(original, true)) {

                                byte[] buffer = new byte[8192];
                                int bytes;

                                while ((bytes = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytes);
                                }

                                out.flush();

                                // borrar el archivo temporal solo cuando ya se copió
                                copiaFile.delete();

                            } catch (Exception e) {
                                Log.e("COPIAGPRS", "Error anexando archivo: " + e);
                            }
                        }


                        NroEnvios = 0;
                        enviosGPRScantidad = 5;
                        logger.info("EnviarArchivo():" + msg);
                    }
                    // }

                    if (nombreZipPorCrear.exists()) {
                        nombreZipPorCrear.delete();
                    }

                    vecesRea++;
                    if (vecesRea <= NroEnvios) {
                        EnviarAlServidorFacturacion();
                    }

                } catch (Exception ex) {
                    //nueva forma ojo para validar
                    File copiaFile = new File(VariablesGlobales.directorioactual +
                            "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");

                    if (copiaFile.exists()) {

                        File original = new File(VariablesGlobales.directorioactual +
                                "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA");

                        // modo append = true
                        try (FileInputStream in = new FileInputStream(copiaFile);
                             FileOutputStream out = new FileOutputStream(original, true)) {

                            byte[] buffer = new byte[8192];
                            int bytes;

                            while ((bytes = in.read(buffer)) != -1) {
                                out.write(buffer, 0, bytes);
                            }

                            out.flush();

                            // borrar el archivo temporal solo cuando ya se copió
                            copiaFile.delete();

                        } catch (Exception e) {
                            Log.e("COPIAGPRS", "Error anexando archivo: " + e);
                        }
                    }
                    vecesRea = 0;
                    NroEnvios = 0;
                    Log.e("ERROR", "[MenuDeLiquidacion]EnviarAlServidorFacturacion();" + "_" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + "/" + ex.getMessage());
                    logger.info("[MenuDeLiquidacion]EnviarAlServidorFacturacion();" + "_" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + "/" + ex.getMessage());
                }

            } catch (Exception ex) {
                //nueva forma ojo para validar
                File copiaFile = new File(VariablesGlobales.directorioactual +
                        "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");

                if (copiaFile.exists()) {

                    File original = new File(VariablesGlobales.directorioactual +
                            "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA");

                    // modo append = true
                    try (FileInputStream in = new FileInputStream(copiaFile);
                         FileOutputStream out = new FileOutputStream(original, true)) {

                        byte[] buffer = new byte[8192];
                        int bytes;

                        while ((bytes = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytes);
                        }

                        out.flush();

                        // borrar el archivo temporal solo cuando ya se copió
                        copiaFile.delete();

                    } catch (Exception e) {
                        Log.e("COPIAGPRS", "Error anexando archivo: " + e);
                    }
                }


                vecesRea = 0;
                NroEnvios = 0;
                Log.e("ERROR", "[MenuDeLiquidacion]EnviarAlServidorFacturacion();" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " / " + ex.getMessage());//Ax: antes infoRegistroSalida.gettablaRegistroSalida_CUENTA()
                logger.info("[MenuDeLiquidacion]EnviarAlServidorFacturacion();" + infoRegistroSalida.gettablaRegistroSalida_CUENTA() + " / " + ex.getMessage());//Ax: antes infoRegistroSalida.gettablaRegistroSalida_CUENTA()
            }

        }
    }

    if (abrioarchivo == 2) {
        MisEnviosHilos.Cerrar_EnvioGPS();
    } else if (abrioarchivo == 1) {
        MisEnviosHilos.Cerrar_EnvioGPS();
    }
    vecesRea = 0;
    NroEnvios = 0;

    File CopiaFile = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");
    if (CopiaFile.exists()) {
        File Original = new File(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/ENVIOGPRS" + serialPDA + ".SDA");
        FileReader stream3 = new FileReader(VariablesGlobales.directorioactual + "/LECTURAMEDIDORES/COPIAGPRS" + serialPDA + ".SDA");
        BufferedReader reader = new BufferedReader(stream3);
        String linea = "";
        String dato;
        int cont = 0;
        RandomAccessFile writer = new RandomAccessFile(Original, "rw");//1117
        while ((linea = reader.readLine()) != null) {
            dato = linea + "\r\n";
            writer.seek(writer.length());
            writer.writeBytes(dato);
        }
        writer.close();
        reader.close();
        CopiaFile.delete();
    }
    return;
    //*****



}
*/
    //FIN DE LA BODEGA


}