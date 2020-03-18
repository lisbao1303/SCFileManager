package com.metaconsultoria.root.scfilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodePreview extends AppCompatActivity {
        private MyFileDirectory arq;
        private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_preview);
        setTitle("QR Code Visualizador");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getIntent().getExtras()!=null) {
            String path = getIntent().getExtras().getString("path");
            arq = new MyFileDirectory(path);
        }

        ImageView localQR= findViewById(R.id.imageView_Previw_QR_Code);
        bitmap =QRCodeGenerator.generateQRCodeImage(arq.getPath(),500,500);
        Bitmap bit= BitmapFactory.decodeResource(getResources(),R.mipmap.souza_qr_code_neg_round);
        bit = Bitmap.createScaledBitmap(bit, 130, 90, true);
        bitmap= QRCodeGenerator.addCenterOverlay(bit,bitmap);


        localQR.setImageBitmap(bitmap);

        ((TextView)findViewById(R.id.textView_nome_arq)).setText(arq.getNome());
        ((TextView)findViewById(R.id.textView_path)).setText(arq.getPath());
        ((TextView)findViewById(R.id.textView_data_hr)).setText(arq.getData_Hr());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitivity_qr_preview,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if(i==R.id.action_pdf){
            String str=FileHandler.saveImageToInternalStorage(bitmap,this,arq.getNome());
            str= makePdf(this,arq.getNome(),str);
            FileHandler.openWith(str,"QR_"+arq.getNome()+".pdf","application/pdf",this);
        }
        if(i==R.id.action_print){
                PrintHelper photoPrinter = new PrintHelper(this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                Bitmap bit= BitmapFactory.decodeResource(getResources(),R.mipmap.meta_2);
                bit = Bitmap.createScaledBitmap(bit, 80, 80, true);
                photoPrinter.printBitmap("droids.jpg - test print", QRCodeGenerator.genratePrintPdf(bitmap,bit,arq.getNome(),arq.getPath()));
        }
        if(i==R.id.action_send){
                String str=FileHandler.saveImageToInternalStorage(bitmap,this,arq.getNome());
                FileHandler.openWith(str,"QR_"+arq.getNome()+".jpeg","image/jpeg",this);
        }
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public String makePdf(Context contx, String nome,String str ) {
        Document document = null;
        String path = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + contx.getApplicationContext().getPackageName()
                + "/Files/QR_PDFs";

        try {
            document = new Document(PageSize.A6,10, 10, 10, 10);
            document.addAuthor("Thiago de Souza");
            document.addSubject("This is the result of a Test.");

            File dir = new File(path,"QR_"+arq.getNome()+".pdf");
            if (!dir.exists()) {
                dir.getParentFile().mkdirs();
            }
            path=dir.getPath();
            FileOutputStream fOut = new FileOutputStream(dir);
            fOut.flush();

            PdfWriter.getInstance(document, fOut);
            document.open();
            //////////////////
            Font f1=new Font(Font.FontFamily.HELVETICA,20,Font.BOLD);
            Font f2=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL);
            Paragraph p1 =new Paragraph(nome,f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            //p1.setSpacingAfter(10);
            document.add(p1);
            document.addTitle(nome);
            Image img = Image.getInstance((new File(Uri.parse(str).getPath())).getAbsolutePath());
            img.setAlignment(Element.ALIGN_CENTER);
            img.scaleToFit(new Rectangle(200,200));
            document.add(img);
            PdfPTable table= new PdfPTable(new float[] { 0.8f, 0.2f });
            PdfPCell p2 =(new PdfPCell(new Paragraph(arq.getPath(),f2)));
            p2.setBorder(PdfPCell.NO_BORDER);
            Bitmap bit= BitmapFactory.decodeResource(getResources(),R.mipmap.meta_2);
            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image maimg = Image.getInstance(stream3.toByteArray());
            maimg.scaleToFit(new Rectangle(30,30));
            PdfPCell p3 =(new PdfPCell(maimg));
            p3.setBorder(PdfPCell.NO_BORDER);
            p3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            p3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            table.addCell(p2);
            table.addCell(p3);
            table.setWidthPercentage(90.0f);
            table.setSpacingBefore(30);
            //p1.setAlignment(Element.ALIGN_CENTER);
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(document!=null) {
                document.close();
            }
        }
        File file =new File(path);
        return Uri.fromFile(file).toString();
    }

}