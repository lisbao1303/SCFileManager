package com.metaconsultoria.root.scfilemanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodePreview extends AppCompatActivity {
        private RecentFilesDB db;
        private MyArquive arq;

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
        db = new RecentFilesDB(this);
        arq = db.findByPath(path);


        ImageView localQR= findViewById(R.id.imageView_Previw_QR_Code);
        Bitmap bitmap =QRCodeGenerator.generateQRCodeImage(arq.getPath(),500,500);
        Bitmap bit= BitmapFactory.decodeResource(getResources(),R.mipmap.cropped_logo_meta);
        bit = Bitmap.createScaledBitmap(bit, 130, 130, true);
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
            Toast.makeText(this,"Em Breve",Toast.LENGTH_SHORT).show();
        }
        if(i==R.id.action_print){
            Toast.makeText(this,"Em Breve",Toast.LENGTH_SHORT).show();
        }
        if(i==R.id.action_send){
            Toast.makeText(this,"Em Breve",Toast.LENGTH_SHORT).show();
        }
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}