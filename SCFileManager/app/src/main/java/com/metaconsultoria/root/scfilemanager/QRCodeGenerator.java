package com.metaconsultoria.root.scfilemanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextPaint;

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

    public static Bitmap genratePrintPdf(Bitmap imageQr,Bitmap metaIc,String nome,String path){
        Bitmap bit=Bitmap.createBitmap(imageQr.getWidth(),(int)(imageQr.getWidth()*1.5) , Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bit);
        canvas.drawBitmap(bit,new Matrix(),null);
        double marginLeft = 0;
        double marginTop = (bit.getHeight()*0.5-imageQr.getHeight()*0.5);
        canvas.drawBitmap(imageQr,(float)marginLeft,(float)marginTop,null);
        marginLeft = (imageQr.getWidth()- metaIc.getWidth());
        marginTop = (bit.getHeight()-metaIc.getHeight());
        canvas.drawBitmap(metaIc,(float)marginLeft,(float)marginTop,null);
        Paint paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(25);
        int cont=0;
        int loc=50;
        int contador=0;
        while(contador<3) {
            if(cont+30>nome.length()){
                canvas.drawText(nome,cont,nome.length(), 40, loc, paint);
                break;
            }
            canvas.drawText(nome,cont,cont+30, 40, loc, paint);
            cont=cont+30;
            loc=loc+50;
            contador++;
            if(cont>nome.length()){break;}
        }
        paint.setTextSize(12);
        cont=path.length();
        loc=20;
        contador=0;
        int linesNumber = cont/50;
        if(linesNumber>=5){
            loc=20*5;
            path=path.substring(path.length()-5*50);
        }else{
            loc=loc+20*linesNumber;
        }
        cont=0;
        while(contador<5) {
            if(cont+50>path.length()){
                canvas.drawText(path,cont,path.length(), 40,bit.getHeight()-loc, paint);
                break;
            }
            canvas.drawText(path,cont,cont+50, 40, bit.getHeight()-loc, paint);
            cont=cont+50;
            loc=loc-20;
            contador++;
            if(cont>path.length()){break;}
        }
        return bit;
    }

}
