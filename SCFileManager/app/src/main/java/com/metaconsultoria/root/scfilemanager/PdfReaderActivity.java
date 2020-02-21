package com.metaconsultoria.root.scfilemanager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
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
        private int tabSelected=-1;
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
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


        db=new RecentFilesDB(this);
        arqpath = getIntent().getExtras().getString("path");
        arquivo=db.findByPath(arqpath);
        db.createNewComentDB(arquivo);
        Log.wtf("arquivoID",String.valueOf(arquivo.id));
        setStared(arquivo.getStared());

        if(savedInstanceState==null) {
            Bundle arguments = new Bundle();
            arguments.putString("caminho", arquivo.getPath());
            FragmentPDF fragment = new FragmentPDF();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.pdf_screen_area, fragment).commit();
        }
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
        if(i==R.id.action_inspect_qr){
            inspectQR();
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
        } else{
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.bottom_nav_star){
            stareClick();
        }
        if(id==R.id.bottom_nav_coment){
            comentsClick();
        }
        if(id==R.id.bottom_nav_new_coment){
            newComentClick();
        }
        return false;
    }


    public void stareClick(){
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

    public void comentsClick() {
        ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.ic_comment_black_24dp);
        ShowComentsFragment comFrag=new ShowComentsFragment();
        comFrag.setArq(arquivo);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_bottom_replace_location,comFrag).commit();
        showdrawer(drawer,fundo);
        tabSelected=0;
    }

    public void newComentClick() {
        ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
        NewComentFragment comFrag=new NewComentFragment();
        comFrag.setArq(arquivo);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_bottom_replace_location,comFrag).commit();
        showdrawer(drawer,fundo);
        tabSelected=1;
    }

    private void setStared(boolean state){
        if(state) {
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_black_24dp);
        }else{
            navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }


    @Override
    protected void onPause() {
        if(isDrawerOpen){
            closedrawer(drawer,fundo);
        }
        super.onPause();
    }

    public void openWith(){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri ur = Uri.parse(arqpath);
            File mfile = new File(getFilesDir().getPath());
            mfile.mkdir();
            File provider = new File(mfile.getPath(),"/storage");
            FileHandler.copyToProvider(ur.getPath(),provider.getPath());
            File shareFile=new File(provider.getPath()+"/"+arquivo.getNome());

            Log.wtf("open with",shareFile.getPath());
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

    public void inspectQR(){
        Bundle bundle = new Bundle();
        bundle.putString("path",arquivo.getPath());
        Intent intent = new Intent(this, QRCodePreview.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showdrawer(final View drawer, final View fundo/*, Fragment frag*/){
        final View shadow = (View) findViewById(R.id.shadow_backgroud);
        shadow.setVisibility(View.VISIBLE);
        shadow.setAlpha(0);
        drawer.setAlpha(0f);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,100f);
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha(animatedValue/100f);
                shadow.setAlpha((animatedValue/100f)*0.3f);  //30% de 255 =76,5 cores sao definidas em hexa
                drawer.setTranslationY(-(animatedValue/100)*drawer.getMeasuredHeight());
            }
        });
        animator.start();
        shadow.setClickable(true);
        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedrawer(drawer,fundo);
            }
        });
        isDrawerOpen=true;
    }


    private void closedrawer(final View drawer,final View fundo){

        final View shadow = (View) findViewById(R.id.shadow_backgroud);
        shadow.setAlpha(0.3f);
        drawer.setAlpha(1f);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,100f);
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha(1f-animatedValue/100f);
                shadow.setAlpha(0.3f-(animatedValue/100f)*0.3f);  //30 % 255=76,5 cores sao definidas em hexa
                drawer.setTranslationY(-drawer.getMeasuredHeight()+(animatedValue/100)*drawer.getMeasuredHeight());
            }
        });
        animator.start();
        shadow.setAlpha(0f);
        shadow.setVisibility(View.INVISIBLE);
        shadow.setClickable(false);
        isDrawerOpen=false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("is_drawer_open",isDrawerOpen);
        if(isDrawerOpen){
           outState.putInt("tab_selected",tabSelected);
        }
        super.onSaveInstanceState(outState);
    }

    public void editComent(MyArquive arq,long id){
        ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.ic_edit_black_24dp);
        NewComentFragment comFrag=new NewComentFragment();
        comFrag.setArq(arquivo);
        Bundle b = new Bundle();
        b.putLong("coment_id",id);
        comFrag.setArguments(b);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_bottom_replace_location,comFrag).commit();
        showdrawer(drawer,fundo);
        tabSelected=1;
    }
}

//TODO:sistema de Delecao de arquivos da pasta storage, deixar um arquivo por vez