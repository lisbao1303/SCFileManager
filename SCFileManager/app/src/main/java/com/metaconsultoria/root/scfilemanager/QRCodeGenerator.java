package com.metaconsultoria.root.scfilemanager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;


public class QRCodeGenerator {


    public static Bitmap generateQRCodeImage(String text, int width, int height){
        MultiFormatWriter writer= new MultiFormatWriter();
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,width,height,hints);
            BarcodeEncoder barcodeEncoder= new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        }catch(WriterException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap addCenterOverlay(Bitmap overlayBitmap, Bitmap overlayedBitmap){
        double marginLeft = (overlayedBitmap.getWidth() * 0.5 - overlayBitmap.getWidth() * 0.5);
        double marginTop = (overlayedBitmap.getHeight() * 0.5 - overlayBitmap.getHeight() * 0.5);
        Canvas canvas= new Canvas(overlayedBitmap);
        canvas.drawBitmap(overlayedBitmap,new Matrix(),null);
        canvas.drawBitmap(overlayBitmap, (float)marginLeft, (float)marginTop,null);
        return overlayedBitmap;
    }

}
