package com.metaconsultoria.root.scfilemanager;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;

public class PdfReaderActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
        private String arqpath;
        public MyArquive arquivo;
        public RecentFilesDB db;
        private BottomNavigationView navigationView;
        private NavigationView drawer;
        private FrameLayout fundo;
        private int shortAnimationDuration;
        private boolean isDrawerOpen =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.nav_bottom_replace_location).getLayoutParams().height=(findViewById(R.id.pdf_reader_main_layout).getLayoutParams().height-findViewById(R.id.header_bottom_drawer_pdf_id).getLayoutParams().height)/2;

        drawer =  findViewById(R.id.bottom_view);
        fundo =(FrameLayout)findViewById(R.id.pdf_reader_main_layout);

        navigationView =(BottomNavigationView) findViewById(R.id.pdf_bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(true);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


        db=new RecentFilesDB(this);
        arqpath = getIntent().getExtras().getString("path");
        arquivo=db.findByPath(arqpath);
        Log.wtf("arquivoID",String.valueOf(arquivo.id));
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
        //findViewById(R.id.nav_bottom_replace_location).getLayoutParams().height=(findViewById(R.id.pdf_reader_main_layout).getLayoutParams().height-findViewById(R.id.header_bottom_drawer_pdf_id).getLayoutParams().height)/2;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if(i==R.id.action_open_with){
            openWith();
        }
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen) {
            closedrawer(drawer,fundo);
            isDrawerOpen=false;
        } else{
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        if(id==R.id.bottom_nav_coment){
            ((ImageView)findViewById(R.id.imageView)).setImageDrawable(getDrawable(R.drawable.ic_comment_black_24dp));
            ShowComentsFragment comFrag=new ShowComentsFragment();
            comFrag.setArq(arquivo);
            comFrag.setDb(db);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_bottom_replace_location,comFrag).commit();
            showdrawer(drawer,fundo);
            isDrawerOpen=true;
        }
        if(id==R.id.bottom_nav_new_coment){
            ((ImageView)findViewById(R.id.imageView)).setImageDrawable(getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
            NewComentFragment comFrag=new NewComentFragment();
            comFrag.setDb(db);
            comFrag.setArq(arquivo);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_bottom_replace_location,comFrag).commit();
            showdrawer(drawer,fundo);
            isDrawerOpen=true;

            //Log.wtf("mano:", String.valueOf(findViewById(R.id.coment_new_screen).getMeasuredHeight()));
        }
        return false;
    }

    private void setStared(boolean state){
        if(state) {
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_black_24dp);
        }else{
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_border_black_24dp);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showdrawer(final View drawer, final View fundo/*, Fragment frag*/){
        final ColorDrawable fadeColor=(ColorDrawable) getDrawable(R.color.black);
        fadeColor.setAlpha(0);
        drawer.setAlpha(0f);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,100);
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha(animatedValue);
                fadeColor.setAlpha((int)(animatedValue/100)*76);  //30% de 255 =76,5 cores sao definidas em hexa
                fundo.setForeground(fadeColor);
                drawer.setTranslationY(-(animatedValue/100)*drawer.getMeasuredHeight());
            }
        });
        animator.start();
    }

    private void closedrawer(final View drawer,final View fundo){
        final ColorDrawable fadeColor=(ColorDrawable) getDrawable(R.color.black);
        fadeColor.setAlpha(0);
        drawer.setAlpha(1f);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,100f);
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha(100-animatedValue);
                fadeColor.setAlpha(76-(int)(animatedValue/100)*76);  //30 % 255=76,5 cores sao definidas em hexa
                fundo.setForeground(fadeColor);
                drawer.setTranslationY(-drawer.getMeasuredHeight()+(animatedValue/100)*drawer.getMeasuredHeight());
            }
        });
        animator.start();
    }

}

//TODO:sistema de Delecao de arquivos da pasta storage, deixar um arquivo por vez

//TODO:escurecer tela em segundo plano e fazer cliques na parte escura esconder o drawer