package com.Util;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

import com.gstolima.accesoyseguridad.MenuDeLiquidacion;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class WSSoap implements Runnable { //Ax: usa las librerias KSOAP2

    public static final String WS_NAMESPACE = "http://tempuri.org/";
    public static String WS_URL;
    public static String WS_METHOD_NAME;
    public static String WS_PAGE = "";
    public static String WS_URL_PAGE = "";
    public FileOutputStream fileOutpStrm;
    public FileInputStream fileInpStrm;
    public String result;
    private int timeouts = 40000;
    public final Logger logger = LoggerFactory.getLogger(MenuDeLiquidacion.class);

    public WSSoap(String url, String paginaws) {
        WS_PAGE = paginaws;
        WS_URL = url;
        WS_URL_PAGE = url + "/" + paginaws;
    }

    //Ax: Se conecta al webservice y el metodo VALIDAR_CONEXION devuelve "1" si esta arriba, si no string con error
    public String verificarWs(String metodo) {

        WS_METHOD_NAME = metodo;

        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);
        Log.e("error", "url " + WS_URL_PAGE);
        try {
            httpTransport.debug = true;
            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service
            SoapObject obj1 = (SoapObject) envelope.getResponse();
            result = obj1.getProperty(0).toString();

        } catch (IOException | XmlPullParserException e) {
            Log.d("logAx", e.getMessage());
            result = e.getMessage();
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        return result;
    }
    //NUEVA FORMA PARA VALIDAR EL SERVICIO Y SU CONEXION AL SERVIDOR
    private static final int PRECHECK_TIMEOUT_MS = 3000; // HEAD connect/read timeout
    private static final int SOAP_TRANSPORT_TIMEOUT_MS = 10000; // intento de timeout en HttpTransportSE (si disponible)
    private static final int TOTAL_TIMEOUT_SEC = 6; // timeout global para la tarea SOAP

    public String verificarWs2(final String metodo) {
        String WS_METHOD_NAME = metodo;
        String result = "";

        // Validar rápido la URL
        //if (!isHostReachable(WS_URL_PAGE, 3000)) {
        //    return "HOST_NO_REACHABLE";
        //}
        if (!isServiceReachable(WS_URL_PAGE, 3000)) {
            return "HOST_NO_REACHABLE";
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            try {
                SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 10000);
                httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);


                Object resp = envelope.getResponse();
                return (resp != null) ? resp.toString() : "NO_RESPONSE";

            } catch (IOException | org.xmlpull.v1.XmlPullParserException e) {
                return "ERROR: " + e.getMessage();
            }
        });

        try {
            result = future.get(15, TimeUnit.SECONDS); // timeout global
            if (result != null && result.contains("ValorRetorno=")) {
                result = result.replace("anyType{ValorRetorno=", "")
                        .replace(";", "")
                        .replace("}", "")
                        .trim();
            }
        } catch (TimeoutException te) {
            future.cancel(true);
            result = "TIMEOUT";
        } catch (Exception e) {
            result = "ERROR: " + e.getMessage();
        } finally {
            executor.shutdownNow();
        }

        return result;
    }
    // Pre-check simple: HEAD con timeouts cortos para reaccionar rápido si host/URL no responde.
    private boolean isServiceReachable(String url, int timeout) {
        try {
            java.net.URL urlObj = new java.net.URL(url);

            // 1. Validar host y puerto
            String host = urlObj.getHost();
            int port = urlObj.getPort();
            if (port == -1) {
                port = urlObj.getProtocol().equals("https") ? 443 : 80;
            }
            try (Socket socket = new Socket()) {
                socket.connect(new java.net.InetSocketAddress(host, port), timeout);
            }

            // 2. Validar con GET (mejor para SOAP .asmx)
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            int responseCode = connection.getResponseCode();

            // Aceptar si responde 200–399 o incluso 405
            return (responseCode >= 200 && responseCode < 400) || responseCode == 405;

        } catch (IOException e) {
            return false;
        }
    }
    //FIN DE LA NUEVA FORMA DE VALIDAR EL SERVICIO

    //Ax: Se conecta al webservice y el metodo RUTAS_PROGRAMADAS devuelve...
    public String rutasProgramadas(String metodo, String serial, String ruta) {

        WS_METHOD_NAME = metodo;

        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        //Ax: Añadir parametros
        PropertyInfo p = new PropertyInfo();
        p.setName("Dirciclo");
        p.setValue(ruta);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("serial");
        p2.setValue(serial);
        p2.setType(String.class);
        request.addProperty(p2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;
            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //Ax: Hace peticion al WS de un archivo localizado en WS y devuelve su tamaño
    public String ObtenerLongitudArchivo(String metodo, String archivoPorRecibir, int escomprimido, String ciclo, String ruta) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("rutaArchivo");
        p.setValue(archivoPorRecibir);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("Comprimido");
        p2.setValue(escomprimido);
        p2.setType(Long.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("Ciclo");
        p3.setValue(ciclo);
        p3.setType(Integer.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("Ruta");
        p4.setValue(ruta);
        p4.setType(Integer.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //Ax: Hace peticion al WS de una ruta y un archivo, si existe archivo lo borra y crea ruta en el server, devuelve booleano
    public String VerificarSiexisteDirectorioArchivo(String metodo, String directorio, String archivo) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("directorio");
        p.setValue(directorio);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("archivo");
        p2.setValue(archivo);
        p2.setType(Long.class);
        request.addProperty(p2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);
        Log.e("error", WS_METHOD_NAME + "-" + WS_NAMESPACE + "data conexion " + WS_URL_PAGE);
        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            Log.e("error", "envio eee " + e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            Log.e("error", "envio e220 " + e.getMessage());
            result = e.getMessage();
        }
        return result;
    }

    //Ax: Hace peticion al WS del archivo que se va a bajar o se bajo. REGISTRAR_PROGRAMACION_ENRUTADOR
    public String RegistrarProgramEnrutadosrOLD(String metodo, String dirciclo, String condicion, String tamano, String serial, String rutarchivo, String nombrearchivo) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("Dirciclo");
        p.setValue(dirciclo);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("condicion");
        p2.setValue(condicion);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("tamano");
        p3.setValue(tamano);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("serial");
        p4.setValue(serial);
        p4.setType(String.class);
        request.addProperty(p4);

        PropertyInfo p5 = new PropertyInfo();
        p5.setName("ruta_archivo");
        p5.setValue(rutarchivo);
        p5.setType(String.class);
        request.addProperty(p5);

        PropertyInfo p6 = new PropertyInfo();
        p6.setName("nombre_archivo");
        p6.setValue(nombrearchivo);
        p6.setType(String.class);
        request.addProperty(p6);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

            try {
                int x = Integer.parseInt(result);
                if (x != 1) result = MensajeRespuestaWeb(x, condicion);

            } catch (Exception e) {
            }

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //Ax: Hace peticion al WS del archivo que se va a bajar o se bajo. RegistroProgramaEnrutador
    public String RegistrarProgramEnrutadosr(String metodo, String dirciclo, String condicion, String tamano, String serial, String nombrearchivo, int RegistrosLeidos, int CausasLeidas) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("dirciclo");
        p.setValue(dirciclo);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("condicion");
        p2.setValue(condicion);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("tamano");
        p3.setValue(tamano);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("serial");
        p4.setValue(serial);
        p4.setType(String.class);
        request.addProperty(p4);

        PropertyInfo p6 = new PropertyInfo();
        p6.setName("nombreArchivo");
        p6.setValue(nombrearchivo);
        p6.setType(String.class);
        request.addProperty(p6);

        PropertyInfo p7 = new PropertyInfo();
        p7.setName("registrosLeidos");
        p7.setValue(RegistrosLeidos);
        p7.setType(Integer.class);
        request.addProperty(p7);

        PropertyInfo p8 = new PropertyInfo();
        p8.setName("causasLeidas");
        p8.setValue(CausasLeidas);
        p8.setType(Integer.class);
        request.addProperty(p8);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

            try {
                int x = Integer.parseInt(result);
                if (x != 1) result = MensajeRespuestaWeb(x, condicion);

            } catch (Exception e) {
            }

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //Ax: Hace peticion al WS y Trae el archivo por partes y numero de parte.
    public String ObtenerArchivo3(String metodo, String archivoPorRecibir, String rutaAGrabar, int tramaKB, int tamano, boolean borrar) {
        WS_METHOD_NAME = metodo;
        long veces;
        File file;
        byte[] buffer;
        FileOutputStream fileOutpStrm = null;
        int tamPaquetes = tramaKB * 1024;
        String result = "";

        try {
            file = new File(rutaAGrabar);

            // Si ya existe y se pide borrar
            if (file.exists() && borrar) {
                file.delete();
            }

            fileOutpStrm = new FileOutputStream(rutaAGrabar, true); // append

            // Calcular cuántas “veces”
            veces = (tamano + tamPaquetes - 1) / tamPaquetes;

            Log.d("SOAP", "Total de bloques a descargar: " + veces);

            for (int i = 0; i < veces; i++) {
                long trozo = (long) i * tamPaquetes;

                // calcular tamaño del último paquete
                int paqueteActual = tamPaquetes;
                if (i == (veces - 1)) {
                    paqueteActual = tamano - (int) trozo;
                }

                SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                request.addProperty("rutaArchivo", archivoPorRecibir);
                request.addProperty("trozo", trozo);
                request.addProperty("paquete", paqueteActual);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 30_000); // 30s timeout
                httpTransport.debug = true;

                Log.d("SOAP", "Solicitando bloque " + (i + 1) + "/" + veces + " (trozo: " + trozo + ", tamaño: " + paqueteActual + ")");

                try {
                    httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    buffer = Base64.decode(response.toString().getBytes("ISO-8859-1"), Base64.DEFAULT);

                    fileOutpStrm.write(buffer, 0, buffer.length);
                    Thread.sleep(100);
                } catch (Exception e) {
                    Log.e("SOAP", "Error en bloque " + (i + 1) + ": " + e.getMessage());
                    fileOutpStrm.close();
                    if (file.exists()) file.delete(); // limpiar archivo incompleto
                    return "Error en bloque " + (i + 1) + ": " + e.getMessage();
                }
            }

            if (fileOutpStrm != null) {
                fileOutpStrm.flush();
                fileOutpStrm.close();
            }

            result = "Descarga completada exitosamente";

        } catch (FileNotFoundException e) {
            Log.e("SOAP", "Archivo no encontrado: " + e.getMessage());
            result = "Error: Archivo no encontrado";
        } catch (IOException e) {
            Log.e("SOAP", "Error de I/O: " + e.getMessage());
            result = "Error: I/O " + e.getMessage();
        } catch (Exception e) {
            Log.e("SOAP", "Error general: " + e.getMessage());
            result = "Error general: " + e.getMessage();
        } finally {
            try {
                if (fileOutpStrm != null) fileOutpStrm.close();
            } catch (IOException e) {
                Log.e("SOAP", "Error cerrando archivo: " + e.getMessage());
            }
        }

        return result;
    }

    ///INICIO
    public String ObtenerArchivoNofunciono(String metodo, String archivoPorRecibir, String nombreArchivo, int tramaKB, int tamano, boolean borrar) {
        WS_METHOD_NAME = metodo;
        long veces;
        byte[] buffer;
        FileOutputStream fileOutpStrm = null;
        int tamPaquetes = tramaKB * 1024;
        String result = "";
        File file = null;

        try {
            // Crear ruta correcta en carpeta pública de descargas
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
            // downloadDir
            file = new File(nombreArchivo,"" );

            if (file.exists() && borrar) {
                file.delete();
            }

            fileOutpStrm = new FileOutputStream(file, true); // append

            // calcular cuántas veces
            veces = (tamano + tamPaquetes - 1) / tamPaquetes;

            Log.d("SOAP", "Total de bloques a descargar: " + veces);

            for (int i = 0; i < veces; i++) {
                long trozo = (long) i * tamPaquetes;

                // último paquete
                int paqueteActual = tamPaquetes;
                if (i == (veces - 1)) {
                    paqueteActual = tamano - (int) trozo;
                }

                boolean exito = false;
                int intentos = 0;

                while (!exito && intentos < 3) {
                    try {
                        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                        request.addProperty("rutaArchivo", archivoPorRecibir);
                        request.addProperty("trozo", trozo);
                        request.addProperty("paquete", paqueteActual);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 30_000); // timeout
                        httpTransport.debug = true;

                        Log.d("SOAP", "Bloque " + (i + 1) + "/" + veces + " intento " + (intentos + 1));

                        httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);
                        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                        // Decodificación correcta
                        buffer = Base64.decode(response.toString(), Base64.DEFAULT);

                        fileOutpStrm.write(buffer, 0, buffer.length);

                        exito = true;
                        Log.d("SOAP", "Bloque " + (i + 1) + " descargado");

                    } catch (Exception e) {
                        intentos++;
                        Log.e("SOAP", "Error bloque " + (i + 1) + " intento " + intentos + ": " + e.getMessage());

                        if (intentos == 3) {
                            fileOutpStrm.close();
                            if (file.exists()) file.delete();
                            return "Error en bloque " + (i + 1) + ": " + e.getMessage();
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }

            if (fileOutpStrm != null) {
                fileOutpStrm.flush();
                fileOutpStrm.close();
            }

            // Marcar archivo legible
            file.setReadable(true, false);

            // Calcular hash SHA-256
            String hash = calcularHashSHA256(file);
            Log.d("SOAP", "SHA-256 del archivo: " + hash);

            result =  "1";//"Descarga completada exitosamente. SHA-256: " + hash;

        } catch (Exception e) {
            Log.e("SOAP", "Error general: " + e.getMessage());
            result = "Error general: " + e.getMessage();
            try {
                if (fileOutpStrm != null) fileOutpStrm.close();
                if (file != null && file.exists()) file.delete();
            } catch (IOException ignored) {
            }
        }

        return result;
    }


    // Método para calcular el hash SHA-256
    private String calcularHashSHA256(File archivo) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(archivo);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
            fis.close();
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            Log.e("SOAP", "Error calculando SHA-256: " + e.getMessage());
            return "Error calculando SHA-256";
        }
    }

    public String ObtenerArchivofallo2(String metodo, String archivoPorRecibir, String rutaAGrabar, int tramaKB, int tamano, boolean borrar) {
        WS_METHOD_NAME = metodo;
        long veces;
        File file;
        byte[] buffer;
        FileOutputStream fileOutpStrm = null;
        int tamPaquetes = tramaKB * 1024;
        String result = "";

        try {
            file = new File(rutaAGrabar);

            // Si ya existe y se pide borrar
            if (file.exists() && borrar) {
                file.delete();
            }

            fileOutpStrm = new FileOutputStream(rutaAGrabar, true); // append

            // Calcular cuántas veces
            veces = (tamano + tamPaquetes - 1) / tamPaquetes;

            Log.d("SOAP", "Total de bloques a descargar: " + veces);

            for (int i = 0; i < veces; i++) {
                long trozo = (long) i * tamPaquetes;

                // calcular tamaño del último paquete
                int paqueteActual = tamPaquetes;
                if (i == (veces - 1)) {
                    paqueteActual = tamano - (int) trozo;
                }

                Log.d("SOAP", "Preparando bloque " + (i + 1) + "/" + veces + " (trozo: " + trozo + ", tamaño: " + paqueteActual + ")");

                boolean exito = false;
                int intentos = 0;

                while (!exito && intentos < 3) {
                    try {
                        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                        request.addProperty("rutaArchivo", archivoPorRecibir);
                        request.addProperty("trozo", trozo);
                        request.addProperty("paquete", paqueteActual);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 30_000); // 30s timeout
                        httpTransport.debug = true;

                        Log.d("SOAP", "Intento " + (intentos + 1) + " para bloque " + (i + 1));

                        httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);
                        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                        buffer = Base64.decode(response.toString().getBytes("ISO-8859-1"), Base64.DEFAULT);

                        fileOutpStrm.write(buffer, 0, buffer.length);

                        exito = true; // bloque descargado con éxito
                        Log.d("SOAP", "Bloque " + (i + 1) + " descargado correctamente");

                    } catch (Exception e) {
                        intentos++;
                        Log.e("SOAP", "Error en bloque " + (i + 1) + " intento " + intentos + ": " + e.getMessage());

                        if (intentos == 3) {
                            fileOutpStrm.close();
                            if (file.exists()) file.delete(); // limpiar archivo incompleto
                            return "Error en bloque " + (i + 1) + ": " + e.getMessage();
                        }

                        try {
                            Thread.sleep(500); // espera antes de reintentar
                        } catch (InterruptedException ignored) {}
                    }
                }

                try {
                    Thread.sleep(100); // delay entre bloques para no saturar servidor
                } catch (InterruptedException ignored) {}
            }

            if (fileOutpStrm != null) {
                fileOutpStrm.flush();
                fileOutpStrm.close();
            }

            result = "1";//""Descarga completada exitosamente";

        } catch (FileNotFoundException e) {
            Log.e("SOAP", "Archivo no encontrado: " + e.getMessage());
            result = "Error: Archivo no encontrado";
        } catch (IOException e) {
            Log.e("SOAP", "Error de I/O: " + e.getMessage());
            result = "Error: I/O " + e.getMessage();
        } catch (Exception e) {
            Log.e("SOAP", "Error general: " + e.getMessage());
            result = "Error general: " + e.getMessage();
        } finally {
            try {
                if (fileOutpStrm != null) fileOutpStrm.close();
            } catch (IOException e) {
                Log.e("SOAP", "Error cerrando archivo: " + e.getMessage());
            }
        }

        return result;
    }
    //FIN

    //original con espera de 30 segundos
    public String ObtenerArchivo(String metodo, String archivoPorRecibir, String rutaAGrabar, int trama, int tamano, boolean borrar) throws Exception {

        WS_METHOD_NAME = metodo;
        int i;
        long veces = 1, trozos = 0;
        File file;
        byte[] x = null;
        fileOutpStrm = null;
        int tamPaquetes = (trama * 1024);

        try {

            file = new File(rutaAGrabar);
            if (file.exists()) file.delete();

            fileOutpStrm = new FileOutputStream(rutaAGrabar, true);//True: se añade datos al final y no sobre escribe

            if (tamano > tamPaquetes) {

                if ((tamano % tamPaquetes) != 0) {
                    veces = (tamano / tamPaquetes) + 1;
                } else {
                    veces = (tamano / tamPaquetes);
                }
            }

            for (i = 0; i < veces; i++) {

                if (i == 0) {
                    trozos = 0;
                } else {
                    trozos = (tamPaquetes * i) + 1;
                }

                if (i == (veces - 1)) { //Ax: calcula el ultimo paquete
                    int y = (int) trozos;
                    tamPaquetes = tamano - y + 1;
                }

                boolean exito = false;
                int intentos = 0;
                while (!exito && intentos < 3) {
                    try {
                        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
                        p.setName("rutaArchivo");
                        p.setValue(archivoPorRecibir);
                        p.setType(String.class);
                        request.addProperty(p);

                        PropertyInfo p2 = new PropertyInfo();
                        p2.setName("trozo");
                        p2.setValue(trozos);
                        p2.setType(Long.class);
                        request.addProperty(p2);

                        PropertyInfo p3 = new PropertyInfo();
                        p3.setName("paquete");
                        p3.setValue(tamPaquetes);
                        p3.setType(Integer.class);
                        request.addProperty(p3);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                        envelope.dotNet = true;

                        envelope.setOutputSoapObject(request);

                        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 30_000);

                        //   try {
                        httpTransport.debug = true;
                        httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

                        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                        x = null;
                        x = Base64.decode(response.toString().getBytes("ISO-8859-1"), Base64.DEFAULT);//Ax: convierte la cadena en Base64 codificacion ISO

                        fileOutpStrm.write(x, 0, x.length);//ok

                        exito = true; // bloque descargado con éxito
                        Log.d("SOAP", "Bloque " + (i + 1) + " descargado correctamente");

                      /*  } catch (Exception e) {
                            result = "Error: " + e.getMessage();
                            Log.e("error", "1" + e.getMessage());
                            fileOutpStrm.close();
                        }*/
                    } catch (Exception e) {
                        Log.e("SOAP", "Error en bloque " + (i + 1) + " intento " + intentos + ": " + e.getMessage());

                        if (intentos == 3) {
                            fileOutpStrm.close();
                            if (file.exists()) file.delete(); // limpiar archivo incompleto
                            return "Error en bloque " + (i + 1) + ": " + e.getMessage();
                        }

                        try {
                            Thread.sleep(500); // espera antes de reintentar
                        } catch (InterruptedException ignored) {}

                    }

                }//fin reintentos
            }
            if (fileOutpStrm != null) {
                fileOutpStrm.flush();
                fileOutpStrm.close();//getFD().sync();

                result = "1";
            }

        }  catch (FileNotFoundException e) {
            Log.e("SOAP", "Archivo no encontrado: " + e.getMessage());
            result = "Error: Archivo no encontrado";
        } catch (IOException e) {
            Log.e("SOAP", "Error de I/O: " + e.getMessage());
            result = "Error: I/O " + e.getMessage();
        } catch (Exception e) {
            Log.e("SOAP", "Error general: " + e.getMessage());
            result = "Error general: " + e.getMessage();
        } finally {
            try {
                if (fileOutpStrm != null) fileOutpStrm.close();
            } catch (IOException e) {
                Log.e("SOAP", "Error cerrando archivo: " + e.getMessage());
            }
        }

        /*catch (FileNotFoundException e) {
            Log.e("error", "2");
            result = "Error: " + e.getMessage();
        } catch (IOException e) {
            Log.e("error", "3");
            result = "Error: " + e.getMessage();
        }*/
        return result;
    }

    public String ObtenerArchivoOriginal(String metodo, String archivoPorRecibir, String rutaAGrabar, int trama, int tamano, boolean borrar) throws Exception {

        WS_METHOD_NAME = metodo;
        int i;
        long veces = 1, trozos = 0;
        File file;
        byte[] x = null;
        fileOutpStrm = null;
        int tamPaquetes = (trama * 1024);

        try {

            file = new File(rutaAGrabar);
            if (file.exists()) file.delete();

            fileOutpStrm = new FileOutputStream(rutaAGrabar, true);//True: se añade datos al final y no sobre escribe

            if (tamano > tamPaquetes) {

                if ((tamano % tamPaquetes) != 0) {
                    veces = (tamano / tamPaquetes) + 1;
                } else {
                    veces = (tamano / tamPaquetes);
                }
            }

            for (i = 0; i < veces; i++) {

                if (i == 0) {
                    trozos = 0;
                } else {
                    trozos = (tamPaquetes * i) + 1;
                }

                if (i == (veces - 1)) { //Ax: calcula el ultimo paquete
                    int y = (int) trozos;
                    tamPaquetes = tamano - y + 1;
                }

                SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
                p.setName("rutaArchivo");
                p.setValue(archivoPorRecibir);
                p.setType(String.class);
                request.addProperty(p);

                PropertyInfo p2 = new PropertyInfo();
                p2.setName("trozo");
                p2.setValue(trozos);
                p2.setType(Long.class);
                request.addProperty(p2);

                PropertyInfo p3 = new PropertyInfo();
                p3.setName("paquete");
                p3.setValue(tamPaquetes);
                p3.setType(Integer.class);
                request.addProperty(p3);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE, 30_000);

                try {
                    httpTransport.debug = true;
                    httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    x = null;
                    x = Base64.decode(response.toString().getBytes("ISO-8859-1"), Base64.DEFAULT);//Ax: convierte la cadena en Base64 codificacion ISO

                    fileOutpStrm.write(x, 0, x.length);//ok

                } catch (Exception e) {
                    result = "Error: " + e.getMessage();
                    Log.e("error", "1" + e.getMessage());
                    fileOutpStrm.close();
                }
            }
            if (fileOutpStrm != null) {
                fileOutpStrm.flush();
                fileOutpStrm.close();//getFD().sync();

                result = "1";
//                if (borrar) {
//                    if (EliminarArchivoWs(archivoPorRecibir, "BorrarArchivoDelServidor")) {
//                        Log.e("error", "entra a borrar ");
//                        result = "2";
//                    }
//                } else {
//                    result = "2";
//                }
            }

        } catch (FileNotFoundException e) {
            Log.e("error", "2");
            result = "Error: " + e.getMessage();
        } catch (IOException e) {
            Log.e("error", "3");
            result = "Error: " + e.getMessage();
        }
        return result;
    }




    //Ax: sube archivo por partes y numero de parte.
    //rutaAGrabar : ruta en el servidor sin el archivo
    //rutaArchivofull: ruta del archivo por subir  .../sdcard0/archivosOut/algo.zip
    public String TerminalToServerReceiveold(String metodo, String rutaAGrabar, String rutaArchivofull, int trama, int comprimido) throws Exception {

        WS_METHOD_NAME = metodo;

        int veces = 1, conteo = 1;
        File file;
        fileInpStrm = null;
        int tamPaquetes = (trama * 1024);
        ByteArrayOutputStream byteArrOutStrem = null;

        try {
            file = new File(rutaArchivofull);

            if (file.length() > tamPaquetes) { //Ax: si el archivo es muy grande se divide en paquetes (varios envios)

                if ((file.length() % tamPaquetes) != 0) {
                    veces = ((int) file.length() / tamPaquetes) + 1; //Ax: la division no es exacta se le coloca una ultima vez (el ultimo paquete)
                } else {
                    veces = ((int) file.length() / tamPaquetes);
                }
            } else {
                if (file.length() < 1024) {
                    tamPaquetes = (int) file.length();
                } else {

                    tamPaquetes = 1024;//Ax: el archivo es muy pequeño (1 envio), se escoge el buffer estandard
                }
            }

            String nombre = rutaAGrabar + "\\" + file.getName(); //Ax: ruta en el servidor donde dejar el zip
            nombre = nombre.replace("\\\\", "\\");

            fileInpStrm = new FileInputStream(file);
            byteArrOutStrem = new ByteArrayOutputStream(tamPaquetes);
            byte[] buffer = new byte[tamPaquetes];
            int posFin;

            while ((posFin = fileInpStrm.read(buffer)) >= 0) {

                byte[] y;

                if (veces > 1 && veces == conteo) { //es el ultimo paquete el buffer debe cambiar
                    int pos = tamPaquetes * (veces - 1); //Ax. la posicion del paquete anterior  y es la posicion inicial del ultimo paquete
                    tamPaquetes = (int) file.length() - pos;//Ax: saco el tamaño del ultimo paquete   (Ojo  Antes:  tamPaquetes = (int) file.length() - pos -1)

                    FileInputStream fileInStr2 = new FileInputStream(file);//Ax: hay que volver a leer el archivo desde una posicion especifica
                    ByteArrayOutputStream byteArrOutStrem2 = new ByteArrayOutputStream(tamPaquetes);

                    fileInStr2.getChannel().position(pos);

                    byte[] buffer2 = new byte[tamPaquetes];
                    fileInStr2.read(buffer2);

                    byteArrOutStrem2.write(buffer2, 0, tamPaquetes);
                    y = new byte[tamPaquetes];
                    y = buffer2;

                    fileInStr2.close();
                    byteArrOutStrem2.close();

                } else { //Ax: aqui escribe los primeros bites para enviar

                    y = new byte[tamPaquetes];
                    byteArrOutStrem.write(buffer, 0, posFin);
                    y = buffer;
                }

                SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

                PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
                p.setName("rutaArchivo");
                p.setValue(nombre);
                p.setType(String.class);
                request.addProperty(p);

                PropertyInfo p2 = new PropertyInfo();
                p2.setName("tamanoArchivo");
                p2.setValue(file.length());
                p2.setType(Double.class);
                request.addProperty(p2);

                PropertyInfo p3 = new PropertyInfo();
                p3.setName("cadenaBytes");
                p3.setValue(y);
                p3.setType(MarshalBase64.class);//.BYTE_ARRAY_CLASS);// Byte.class
                request.addProperty(p3);

                PropertyInfo p4 = new PropertyInfo();
                p4.setName("Comprimido");
                p4.setValue(comprimido);
                p4.setType(Integer.class);
                request.addProperty(p4);

                PropertyInfo p5 = new PropertyInfo();
                p5.setName("DirectorioDondeDescomprimir");
                p5.setValue("");
                p5.setType(String.class);
                request.addProperty(p5);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                new MarshalBase64().register(envelope);   //serialization
                envelope.encodingStyle = SoapEnvelope.ENC;

                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

                try {
                    httpTransport.debug = true;
                    httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    result = response.toString(); //Ax: 1 = parte ok, 4=fin ok

                    if (!result.equals("1") && !result.equals("4")) {
                        break;
                    }
                    conteo++;

                } catch (Exception e) {
                    result = "Error: " + e.getMessage();
                }
            }
        } catch (FileNotFoundException e) {
            result = "Error: " + e.getMessage();
        } catch (IOException e) {
            result = "Error: " + e.getMessage();
        } finally {
            fileInpStrm.close();
            byteArrOutStrem.close();
        }
        return result;//4 total ok, 1 = parte ok(algo estuvo mal si llega aqui) el resto de numeros = error
    }

    //Ax: sube archivo por partes y numero de parte.
    //rutaAGrabar : ruta en el servidor sin el archivo
    //rutaArchivofull: ruta del archivo por subir  .../sdcard0/archivosOut/algo.zip
    public String TerminalToServerReceive(String metodo, String rutaAGrabar, int comprimido, String rutaArchivofull, int trama) throws Exception {

        WS_METHOD_NAME = metodo;

        int posFin;
        File file;
        fileInpStrm = null;
        int Paquetes = (trama * 1024);
        int PaquetesMin = (8 * 1024);
        file = new File(rutaArchivofull);
        long filezise = file.length();
        byte[] buffer;
        boolean esMultiplo = false;

        if (filezise < 1) return "Archivo en 0";
        //----------------------------------------------se busca un multiplo entre 8k y 50k, para usarlo de tamaño en buffer
        // Ejemplo:  6/2= 3 => 2...2...2 = (byte's de 2, se usa 3 veces)  diferente de 7/2 =3.5 => 3...3...1 (toca calcular un nuevo byte para 1 )

        if (filezise < Paquetes) {
            esMultiplo = true;
            Paquetes = (int) filezise;
            buffer = new byte[Paquetes];
        } else {
            int p = Paquetes;

            while (true) {

                if (filezise % p == 0) break;
                p--;

                if (p < PaquetesMin) {
                    p = 0; // Se disminuyó hasta que llego a lo minimo que se desea usar
                    break;
                }
            }
            if (p > 1) { // p puede ser un multiplo que dé un numero exacto de paquetes sin sobrar bytes

                Paquetes = p;
                buffer = new byte[Paquetes];
                esMultiplo = true;
            } else { //Se usa un numero de buffer opcional y se debe controlar la ultima, leida de bytes
                buffer = new byte[Paquetes];
            }
        }
        //----------------------------------------------
        ByteArrayOutputStream byteArrOutStrem = null;
        String res = "-1";

        try {
            fileInpStrm = new FileInputStream(file);
            byteArrOutStrem = new ByteArrayOutputStream(Paquetes);

            if (esMultiplo) { //Aca se envia uno o varios pedazos, pero todos del mismo tamaño
                while ((posFin = fileInpStrm.read(buffer)) >= 0) {

                    byteArrOutStrem.write(buffer, 0, posFin);
                    res = TerminalToServerReceiveII(metodo, rutaAGrabar, comprimido, rutaArchivofull, buffer);
                    comprimido=0;
                    if (!res.equals("1")) break;
                }
            } else { //Ax: aca es donde se usa el "Paquetes" original y el último pedazo por enviar se recalcula
                long size = filezise;

                while ((posFin = fileInpStrm.read(buffer)) >= 0) {

                    if (size < buffer.length) {

                        long pos = Math.abs(filezise - size);

                        FileInputStream fis2 = new FileInputStream(file);
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream((int) size);
                        byte[] bf2 = new byte[(int) size];
                        fis2.skip(pos);//fileInpStrm2.getChannel().position(pos); //Ax: skip salta a una posicion de bytes

                        posFin = fis2.read(bf2);
                        baos2.write(bf2, 0, posFin);
                        res = TerminalToServerReceiveII(metodo, rutaAGrabar, comprimido, rutaArchivofull, bf2); //Ax: como es el último pedazo se sale del loop
                        fis2.close();
                        baos2.close();
                        break;
                    } else {
                        byteArrOutStrem.write(buffer, 0, posFin);
                        res = TerminalToServerReceiveII(metodo, rutaAGrabar, comprimido, rutaArchivofull, buffer);
                    }
                    size = Math.abs(size - Paquetes);
                    comprimido=0;
                    if (!res.equals("1")) break;
                }
            }

        } catch (FileNotFoundException e) {
            logger.info("[WSSoap]TerminalToServerReceive(1) ERROR: " + e.getMessage());
            result = "Error: " + e.getMessage();
        } catch (IOException e) {
            logger.info("[WSSoap]TerminalToServerReceive(2) ERROR: " + e.getMessage());
            result = "Error: " + e.getMessage();
        } finally {
            try{
                if(fileInpStrm.available() > 0){
                    fileInpStrm.close();
                }
                if(byteArrOutStrem != null){
                    byteArrOutStrem.close();
                }
            }catch (Exception ex){
                logger.info("[WSSoap]TerminalToServerReceive(3) ERROR: " + ex.getMessage());
                result = "Error: " + ex.getMessage();
            }
        }
        return result;//4 total ok, 1 = parte ok(algo estuvo mal si llega aqui) el resto de numeros = error
    }

    //Ax: sube archivo por partes (nuevo).
    //rutaRemota : ruta en el servidor sin el archivo
    //rutaArchivofull: ruta del archivo por subir  .../sdcard0/archivosOut/algo.zip
    public String TerminalToServerReceiveII(String metodo, String rutaRemota, int comprimido, String rutaLocalArchivo, byte[] bite) throws Exception {

        WS_METHOD_NAME = metodo;
        File file = new File(rutaLocalArchivo);
        try {
            String nombre = rutaRemota + "\\" + file.getName(); //Ax: ruta en el servidor donde dejar el zip
            nombre = nombre.replace("\\\\", "\\");

            SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

            PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
            p.setName("rutaArchivo");
            p.setValue(nombre);
            p.setType(String.class);
            request.addProperty(p);

            PropertyInfo p2 = new PropertyInfo();
            p2.setName("tamanoArchivo");
            p2.setValue(file.length());
            p2.setType(Double.class);
            request.addProperty(p2);

            PropertyInfo p3 = new PropertyInfo();
            p3.setName("cadenaBytes");
            p3.setValue(bite);
            p3.setType(MarshalBase64.class);//.BYTE_ARRAY_CLASS);// Byte.class
            request.addProperty(p3);

            PropertyInfo p4 = new PropertyInfo();
            p4.setName("Comprimido");
            p4.setValue(comprimido);
            p4.setType(Integer.class);
            request.addProperty(p4);

            PropertyInfo p5 = new PropertyInfo();
            p5.setName("DirectorioDondeDescomprimir");
            p5.setValue(nombre);
            p5.setType(String.class);
            request.addProperty(p5);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            new MarshalBase64().register(envelope);   //serialization
            envelope.encodingStyle = SoapEnvelope.ENC;

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

            try {
                httpTransport.debug = true;
                httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = response.toString(); //Ax: 1 = parte ok, 4=fin ok

            } catch (Exception e) {
                result = "Error: " + e.getMessage();
                logger.info("[WSSoap]TerminalToServerReceiveII(1) ERROR: " + e.getMessage());
            }
        } catch (Exception ex) {
            logger.info("[WSSoap]TerminalToServerReceiveII(2) ERROR: " + ex.getMessage());
        }
        return result;//4 total ok, 1 = parte ok(algo estuvo mal si llega aqui) el resto de numeros = error
    }

    //Ax: se el envia la ruta y descomprime en server.
    public String Descomprimir(String metodo, String pathDescomprimir, String zipFile, String hash, String indRuta) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("pathDescomprimir");
        p.setValue(pathDescomprimir);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("zipFile");
        p2.setValue(zipFile);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("hash");
        p3.setValue(hash);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("indRuta");
        p4.setValue(indRuta);
        p4.setType(String.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            result = RespuestasWs(response.toString());

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    private String RespuestasWs(String RespuestaWeb) {

        switch (RespuestaWeb) {
            case "0":
                RespuestaWeb = "Archivo no existe "; //No implementado
                break;
            case "1":
                RespuestaWeb = "true"; //ok
                break;
            case "2":
                RespuestaWeb = "Falla de integridad ";
                break;
            case "3":
                RespuestaWeb = "Error al descomprimir ";
                break;
            case "4":
                RespuestaWeb = "Otros... " + RespuestaWeb;
                break;
            default:
                RespuestaWeb = "Cod. Desconocido... "; //rx
                break;
        }
        return RespuestaWeb;
    }

    //Ax: se el envia la ruta y descomprime en server.
    //rutaDescomServer : ruta en el servidor sin el archivo donde se descomprimirá
    //rutaArchivofull: ruta en el servidor con el archivo.zip
    public String DirectorioDescomprimir(String metodo, String rutaDescomServer, String rutaArchivofull, String tipoequipo, String indruta, int trama) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("DirectorioDondeDescomprimir");
        p.setValue(rutaDescomServer);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("ArchivoADescomprimir");
        p2.setValue(rutaArchivofull);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("TipoEquipo");
        p3.setValue(tipoequipo);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("IndRuta");
        p4.setValue(indruta);
        p4.setType(String.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    /**
     * Sube los 2 archivos descomprimidos en el servidor
     *
     * @param metodo                INSERTAR_DATOS_TABLAS_TEMPORALES
     * @param rutaServer            C:\EnRutadorSIMFA
     * @param rutaCicloDescomServer CIC0241\DescargasEnvioGPRS
     * @param Serial                355236030406210
     * @param num                   "" vacío sin implementar
     * @return
     * @throws Exception
     */
    /*public String InsertarDatosTablasTemporales(String metodo, String rutaServer, String rutaCicloDescomServer, String Serial, String num) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("directorioActual");
        p.setValue(rutaServer);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("ciclo");
        p2.setValue(rutaCicloDescomServer);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("terminal");
        p3.setValue(Serial);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("estado1");
        p4.setValue(num);
        p4.setType(String.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;
            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }*/

    public String InsertarDatosTablasTemporales(String metodo, String rutaServer, String rutaCicloDescomServer, String Serial, String num) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("directorioActual");
        p.setValue(rutaServer);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("ciclo");
        p2.setValue(rutaCicloDescomServer);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("terminal");
        p3.setValue(Serial);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("estado1");
        p4.setValue(num);
        p4.setType(String.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    //Ax: Hace peticion al WS de borrar un archivo localizado en WS
    public Boolean EliminarArchivoWs(String archivo, String metodo) throws Exception {//BorrarArchivoDelServidor

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("archivo");
        p.setValue(archivo);
        p.setType(String.class);
        request.addProperty(p);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();

            if (result.equals("true")) {
                return true;
            }

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return false;
    }

    public String chatRecibirMensajes(String metodo, String receptor) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("receptor1");
        p.setValue(receptor);
        p.setType(String.class);
        request.addProperty(p);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            //   SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            SoapObject response = (SoapObject) envelope.getResponse();
            result = response.toString();

            //result = response.getProperty(0).toString();


        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();

        } catch (Exception e) {
            result = e.getMessage();

        }
        return result;
    }

    /**
     * kim metodo que inserta los mensajes
     *
     * @param metodo    nombre del metodo que consuluta en el webservice
     * @param remitente codigo de quien lo envia
     * @param receptor  codigo de quien recibe
     * @param mensaje   cadena con el mensaje y asunto
     * @param estado    estado del mensaje
     * @return
     * @throws Exception
     */
    public String chatInsertarMensajes(String metodo, String remitente, String receptor, String mensaje, String estado) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("remitente1");
        p.setValue(remitente);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("receptor1");
        p2.setValue(receptor);
        p2.setType(String.class);
        request.addProperty(p2);

        PropertyInfo p3 = new PropertyInfo();
        p3.setName("mensaje1");
        p3.setValue(mensaje);
        p3.setType(String.class);
        request.addProperty(p3);

        PropertyInfo p4 = new PropertyInfo();
        p4.setName("estado1");
        p4.setValue(estado);
        p4.setType(String.class);
        request.addProperty(p4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            Object response = (Object) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //funcion para enviar la respuesta de la coneccion con el pc
    private String MensajeRespuestaWeb(int RespuestaWeb, String condicion) {
        String Respuesta = "";
        switch (RespuestaWeb) {
            case -20:
                Respuesta = "Problemas en el Metodo Programacion EnRutamiento en el SW. " + RespuestaWeb;
                break;
            case -2:
                Respuesta = "No Puede abrir el Archivo de Programacion Enrutamiento. " + RespuestaWeb;
                break;
            case 3:
                Respuesta = "No Existe Archivo de ruta para el lector. " + RespuestaWeb;
                break;
            case 0:
                Respuesta = "No Existe Archivo Programado. " + RespuestaWeb;
                break;
            case -1:
                Respuesta = "No hay Programacion en el Sistema. " + RespuestaWeb;
                break;
            case 1:
                if (condicion == "cargaf" || condicion == "descargaf")
                    Respuesta = "registro de programacion ✓ " + RespuestaWeb;
                break;
            case 4:
                Respuesta = "Esta Ruta ya tiene Descarga no se puede procesar carga alguna... " + RespuestaWeb;
                break;
            case 5:
                Respuesta = "Serial dispositivo no esta Programada... " + RespuestaWeb;
                break;
            default:
                break;
        }
        return Respuesta;
    }

    public String validarVersion(String metodo, String directorio, String serial) throws Exception {

        WS_METHOD_NAME = metodo;
        result = "";
        Log.e("datos", metodo + " " + directorio + " " + serial);
        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("directorioDondeExiste");
        p.setValue(directorio);
        p.setType(String.class);
        request.addProperty(p);

        PropertyInfo p2 = new PropertyInfo();
        p2.setName("serie");
        p2.setValue(serial);
        p2.setType(String.class);
        request.addProperty(p2);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            //   SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            Object response = (Object) envelope.getResponse();
            result = response.toString();

            //result = response.getProperty(0).toString();
            Log.e("error", "trata de consultar");

        } catch (IOException | XmlPullParserException e) {
            Log.e("error", "error 1 " + e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            Log.e("error", "error 2 " + e.getMessage());
            result = e.getMessage();
        }
        return result;
    }
    public int obtenerLecturasPendientes(String metodo, int ciclo, int anio, int mes, String nombreArchivo) {

        WS_METHOD_NAME = metodo;
        int result = -1;

        try {
            SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

            request.addProperty("ciclo", ciclo);
            request.addProperty("anio", anio);
            request.addProperty("mes", mes);
            request.addProperty("nombreArchivo", nombreArchivo);

            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);
            httpTransport.debug = true;

            String soapAction = WS_NAMESPACE + WS_METHOD_NAME;
            httpTransport.call(soapAction, envelope);

            Object response = envelope.getResponse();

            if (response instanceof SoapPrimitive) {
                result = Integer.parseInt(response.toString());
            } else if (response instanceof SoapObject) {
                SoapObject obj = (SoapObject) response;
                result = Integer.parseInt(obj.getProperty(0).toString());
            }

            Log.e("SOAP_REQUEST", httpTransport.requestDump);
            Log.e("SOAP_RESPONSE", httpTransport.responseDump);

        } catch (Exception e) {
            Log.e("SOAP_ERROR", e.toString());
        }

        return result;
    }
    public int obtenerLecturasPendientes_2(String metodo, int anio, int mes, String nombreArchivo) throws Exception {
        //original public int ObtenerLecturasPendientes(int anio, int mes, string nombreArchivo)
        WS_METHOD_NAME = metodo;
        int result = 0;
        Log.e("datos", metodo + " - " +anio + " - " + mes+" - "+nombreArchivo);
        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        // Parámetro anio
        PropertyInfo p1 = new PropertyInfo();
        p1.setName("anio");
        p1.setValue(anio);
        p1.setType(Integer.class);
        request.addProperty(p1);

        // Parámetro mes
        PropertyInfo p2 = new PropertyInfo();
        p2.setName("mes");
        p2.setValue(mes);
        p2.setType(Integer.class);
        request.addProperty(p2);

        // Parámetro nombreArchivo
        PropertyInfo p3 = new PropertyInfo();
        p3.setName("nombreArchivo");
        p3.setValue(nombreArchivo);
        p3.setType(String.class);
        request.addProperty(p3);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
            httpTransport.debug = true;

            httpTransport.call(WS_NAMESPACE + WS_METHOD_NAME, envelope);

            Object response = envelope.getResponse();
            result = Integer.parseInt(response.toString());

        } catch (IOException | XmlPullParserException e) {
            Log.e("SOAP", "Error SOAP: " + e.getMessage());
            result = -1;
        } catch (Exception e) {
            Log.e("SOAP", "Error general: " + e.getMessage());
            result = -1;
        }

        return result;
    }
    //Ax: ...
    public String tryAll(String metodo, String ser) {

        WS_METHOD_NAME = metodo;
        result = "";

        SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_NAME);

        PropertyInfo p = new PropertyInfo(); //Ax: Añadir parametros
        p.setName("serial");
        p.setValue(ser);
        p.setType(String.class);
        request.addProperty(p);

        result = "";

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(WS_URL_PAGE);

        try {
//            httpTransport.debug = true;
//            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service
//            SoapObject obj1 = (SoapObject) envelope.getResponse();
//            result = obj1.getProperty(0).toString();

            httpTransport.debug = true;

            httpTransport.call((WS_NAMESPACE + WS_METHOD_NAME), envelope);// Ax: Enviando la petición al web service

            //   SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            Object response = (Object) envelope.getResponse();
            result = response.toString();

        } catch (IOException | XmlPullParserException e) {
            Log.d("logAx", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    public void run() {
        this.run();
    }
}
