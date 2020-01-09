package com.metaconsultoria.root.scfilemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class PdfReaderActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
        private String arqpath;
        private MyArquive arquivo;
        private RecentFilesDB db;
        private BottomNavigationView navigationView;
        private boolean isStared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);

        navigationView =(BottomNavigationView) findViewById(R.id.pdf_bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(true);

        db=new RecentFilesDB(this);
        arqpath = getIntent().getExtras().getString("path");
        arquivo=db.findByPath(arqpath);
        setStared(arquivo.getStared());

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
        //navigationView.setNavigationItemSelectedListener(this);

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.bottom_nav_star){
            if(arquivo.getStared()){
                    arquivo.setStared(false);
                    db.attStared(arquivo.getPath(),false);
                    setStared(false);
                }
                else{
                    arquivo.setStared(true);
                    db.attStared(arquivo.getPath(),true);
                    setStared(true);
                }
        }
        if(id==R.id.bottom_nav_coment){Toast.makeText(this,"coment",Toast.LENGTH_SHORT).show();}
        if(id==R.id.bottom_nav_new_coment){Toast.makeText(this,"+coment",Toast.LENGTH_SHORT).show();}
        return false;
    }

    private void setStared(boolean state){
        if(state) {
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_black_24dp);
        }else{
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }
}

//TODO:sistema de Delecao de arquivos da pasta storage deixar um arquivo por vez
