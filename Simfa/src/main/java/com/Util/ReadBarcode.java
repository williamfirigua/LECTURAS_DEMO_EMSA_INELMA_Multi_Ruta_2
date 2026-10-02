package com.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReadBarcode {
    public void leer() {
//        String filePath = "QRCode.png";
//        String charset = "UTF-8"; // or "ISO-8859-1"
//        Map hintMap = new HashMap();
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//
//        try {
//            System.out.println("Data read from QR Code: "
//                    + readQRCode(filePath, charset, hintMap));
//        }catch(Exception a){}
//    }
//
//    public static String readQRCode(String filePath, String charset, Map hintMap) throws FileNotFoundException, IOException, NotFoundException {
//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new  BufferedImageLuminanceSource( ImageIO.read(new FileInputStream(filePath)))));//BitmapFactory.decodeFile(inFilePath)
//        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
//        return qrCodeResult.getText();
//       // return null;
    }

    public Result test() { //Ax: por terminar, es para leer codigo de barras, abrir foto, tomarla, leer codigo desde imagen

        /* http://stackoverflow.com/questions/3422651/decoding-qr-code-from-image-stored-on-the-phone-with-zxing-on-android-phone/14178993#14178993
            http://stackoverflow.com/questions/3422651/decoding-qr-code-from-image-stored-on-the-phone-with-zxing-on-android-phone?lq=1
            http://stackoverflow.com/questions/10787560/how-to-use-preview-frame-to-decode-a-qr-using-zxing-in-android?rq=1   (abajo too)
            http://stackoverflow.com/questions/11010386/send-bitmap-using-intent-android
         */

        try {
            File file = new File("");
            InputStream inputStream = new FileInputStream(file);
            //Uri uri;
//            InputStream inputStream2 = mProviderContext.getContentResolver().openInputStream(uri);
//            InputStream inputStream = getContentResolver().openInputStream(uri);

            // InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                //Log.e(TAG, "uri is not a bitmap," + uri.toString());
                return null;
            }

            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bBitmap);
                return result;
            } catch (NotFoundException e) {
                // Log.e(TAG, "decode exception", e);
                return null;
            }
        } catch (FileNotFoundException e) {
            //Log.e(TAG, "can not open file" + uri.toString(), e);
            return null;
        }

    }
}

