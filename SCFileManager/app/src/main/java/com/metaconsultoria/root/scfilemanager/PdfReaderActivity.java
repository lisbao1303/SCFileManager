package com.metaconsultoria.root.scfilemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class PdfReaderActivity extends AppCompatActivity {
        private String arqpath;
        private MyArquive arquivo;
        private RecentFilesDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);

        db=new RecentFilesDB(this);
        arqpath = getIntent().getExtras().getString("path");
        arquivo=db.findByPath(arqpath);

        Bundle arguments= new Bundle();
        arguments.putString("caminho",arquivo.getPath());
        FragmentPDF fragment= new FragmentPDF();
        fragment.setArguments(arguments);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.pdf_screen_area,fragment).commit();

        this.setTitle(arquivo.getNome()+".pdf");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pdf_toobar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if(i==R.id.action_open_with){
            openWith();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openWith(){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri ur = Uri.parse(arqpath);
            File mfile = new File(getFilesDir().getPath());
            mfile.mkdir();
            File provider = new File(mfile.getPath(),"/storage");
            FileHandler.copyToProvider(ur.getPath(),provider.getPath());
            File shareFile=new File(provider.getPath()+"/"+arquivo.getNome()+".pdf");


            Uri uri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", shareFile);

            intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri,"application/pdf")
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent =intent.createChooser(intent,getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(arqpath), "application/pdf");
            intent = Intent.createChooser(intent, getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}

//TODO:sistema de Delecao de arquivos da pasta storage deixar um arquivo por vez
