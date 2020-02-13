package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.support.v4.content.FileProvider;
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
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

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

        String path=getIntent().getExtras().getString("path");

        arq= new MyFileDirectory(path);


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
            openWith(str,"QR_"+arq.getNome(),"application/pdf");
        }
        if(i==R.id.action_print){
                // Get a PrintManager instance
                PrintManager printManager = (PrintManager) this
                        .getSystemService(Context.PRINT_SERVICE);

                // Set job name, which will be displayed in the print queue
                String jobName = this.getString(R.string.app_name) + " Document";

                // Start a print job, passing in a PrintDocumentAdapter implementation
                // to handle the generation of a print document
                printManager.print(jobName, new MyPrintDocumentAdapter(this),
                        null); //
        }
        if(i==R.id.action_send){
                String str=FileHandler.saveImageToInternalStorage(bitmap,this,arq.getNome());
                openWith(str,arq.getNome(),"image/jpeg");
        }
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openWith(String arqpath, String name,String tipo){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri ur = Uri.parse(arqpath);
            File mfile = new File(getFilesDir().getPath());
            mfile.mkdir();
            File provider = new File(mfile.getPath(),"/storage");
            FileHandler.copyToProvider(ur.getPath(),provider.getPath());
            File shareFile=new File(provider.getPath()+"/"+name);

            Log.wtf("open with",shareFile.getPath());
            Uri uri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", shareFile);

            intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri,tipo)
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent =intent.createChooser(intent,getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(arqpath), "image/jpeg");
            intent = Intent.createChooser(intent, getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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

            File dir = new File(path,"QR_"+arq.getNome());
            if (!dir.exists()) {
                dir.getParentFile().mkdirs();
            }
            //path=dir.getPath();
            FileOutputStream fOut = new FileOutputStream(dir);
            fOut.flush();

            PdfWriter.getInstance(document, fOut);
            document.open();

            //////////////////
            Font f1=new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
            Font f2=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
            Paragraph p1 =new Paragraph(nome,f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            //p1.setSpacingAfter(10);
            document.add(p1);
            Image img = Image.getInstance((new File(Uri.parse(str).getPath())).getAbsolutePath());
            img.setAlignment(Element.ALIGN_CENTER);
            img.scaleToFit(new Rectangle(200,200));
            img.setSpacingAfter(30);
            document.add(img);
            Paragraph p2 =new Paragraph(str,f2);
            //p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);
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