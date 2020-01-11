package com.metaconsultoria.root.scfilemanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.io.File;

public class PdfReaderActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
        private String arqpath;
        private MyArquive arquivo;
        private RecentFilesDB db;
        private BottomNavigationView navigationView;
        private NavigationView drawer;
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


        drawer =  findViewById(R.id.bottom_view);
        //drawer.setVisibility(BottomNavigationView.INVISIBLE);

        navigationView =(BottomNavigationView) findViewById(R.id.pdf_bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(true);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


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
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen) {
            closedrawer(drawer);
            isDrawerOpen=false;
        } else{
            super.onBackPressed();
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
        if(id==R.id.bottom_nav_coment){
            showdrawer(drawer);
            isDrawerOpen=true;
        }
        if(id==R.id.bottom_nav_new_coment){
            showdrawer(drawer);
            isDrawerOpen=true;
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


    private void showdrawer(final View drawer){
        drawer.setAlpha(0f);
        drawer.setVisibility(View.VISIBLE);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,drawer.getMeasuredHeight());
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha((animatedValue/drawer.getMeasuredHeight())*100);
                Log.wtf("animando", "...");
                drawer.setTranslationY(-animatedValue);
            }
        });
        animator.start();
    }

    private void closedrawer(final View drawer){
        drawer.setAlpha(1f);
        drawer.setVisibility(View.VISIBLE);
        ValueAnimator animator= ValueAnimator.ofFloat(0f,drawer.getMeasuredHeight());
        animator.setDuration(this.shortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                drawer.setAlpha(100-(animatedValue/drawer.getMeasuredHeight())*100);
                Log.wtf("animando", "...");
                drawer.setTranslationY(-drawer.getMeasuredHeight()+animatedValue);
            }
        });
        animator.start();
    }

}

//TODO:sistema de Delecao de arquivos da pasta storage deixar um arquivo por vez
