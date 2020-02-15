package com.metaconsultoria.root.scfilemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class MainNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, FragmentFileEx.FragmentListener, ActivityCompat.OnRequestPermissionsResultCallback {

    //private String matricula;
    private FragmentFileEx mainFragmentFileEx;
    public MenuItem searchItem ;
    public RecentFilesDB db;
    public boolean inline = false;
    private MenuItem configItem;
    private MenuItem listCardItem;
    private FragmentMainTabs fragMain;
    private int tabselected=0;
    private int navDrawerSelected;
    private Bundle instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = savedInstanceState;

        //testando permissoes
        if(!checkPermissoes()){
            try {
                requestPermissoes();
                while(checkPermissoes());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //encontrando cartao fisico
        File[] teste = this.getExternalFilesDirs(null);
        String buffer="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for(int i=0;i<teste.length;i++){
                if(Environment.isExternalStorageRemovable((teste[i]))){
                    buffer=teste[i].getPath();
                    break;
                }
                if(i==(teste.length-1)){
                    buffer=teste[0].getPath();
                }
            }
        }else{
            if(teste.length>1){
                buffer=teste[1].getPath();
            }else{
                buffer=teste[0].getPath();
            }
        }
        buffer=buffer.substring(0,buffer.indexOf("/Android"));
        ConstantesDoProjeto.getInstance().setMainPath(buffer);
        ConstantesDoProjeto.getInstance().setMainPathProtected(buffer+"/ArquivosSouza");

        //inicio da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        db = new RecentFilesDB(this);
        if(instance!=null) {
            tabselected = instance.getInt("tab_selected");
        }
        Log.wtf("resolvendo rotacao:",this.getClass().getName());
        Thread.dumpStack();
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(checkPermissoes()&& instance==null){
                if(!setMainFrag(navDrawerSelected)){
                    Log.wtf("teste","maroto");
                fragMain = new FragmentMainTabs();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMain).commit();
                navDrawerSelected=R.id.nav_arq_window;}
        super.onResume();
        FuncDB fdb = new FuncDB(this);
        if(fdb.findAll()==null){
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst",true);
            Intent intent = new Intent(this, ActivityNewFunc.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,ConstantesDoProjeto.NEW_USER_REQUEST);
        }
       }
        else{
                if(instance!=null && navDrawerSelected==R.id.nav_arq_window){
                    fragMain=(FragmentMainTabs) this.getSupportFragmentManager().findFragmentById(R.id.screen_area);
                }
                super.onResume();
        }
    }



    @Override
    protected void onPause() {
        if(fragMain!=null) {
            tabselected=fragMain.getCurentTab();
        }else{
            tabselected=0;
        }
        navDrawerSelected=((NavigationView)findViewById(R.id.nav_view)).getCheckedItem().getItemId();

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==ConstantesDoProjeto.NEW_USER_REQUEST){
            if(resultCode== Activity.RESULT_OK){

            }else{
                Toast.makeText(this,"Usuario nao salvo",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(navDrawerSelected==R.id.nav_arq_window){
              fragMain=(FragmentMainTabs) this.getSupportFragmentManager().findFragmentById(R.id.screen_area);
              if(fragMain.getCurentTab()==1) {
                  FragmentFileEx fragmentFileEx=(FragmentFileEx) this.getSupportFragmentManager().findFragmentById(R.id.file_ex_area);
                  if (fragmentFileEx.upDir()) {
                      super.onBackPressed();
                  }
              }
        } else if(navDrawerSelected==R.id.nav_add_qr_code){
            FragmentFileEx fragmentFileEx=(FragmentFileEx) this.getSupportFragmentManager().findFragmentById(R.id.replace_add_qr_fragment);
            if (fragmentFileEx.upDir()) {
                super.onBackPressed();
            }

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ATENCAO: Os elementos do Navigation Drawer sao instaciados somente aqui n incluir eles no onCreate
        getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        configItem = menu.findItem(R.id.action_settings);
        listCardItem = menu.findItem(R.id.action_list_mode);
        searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Procurar...");

        if(instance!=null){
            refreshToolbar(navDrawerSelected);
        }

        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(fragMain!=null) {
            outState.putInt("tab_selected", fragMain.getCurentTab());
        }else{
            outState.putInt("tab_selected",0);
        }

        outState.putInt("nav_drawer_selected",((NavigationView)findViewById(R.id.nav_view)).getCheckedItem().getItemId());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        tabselected = savedInstanceState.getInt("tab_selected");
        navDrawerSelected = savedInstanceState.getInt("nav_drawer_selected");
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onQueryTextSubmit (String query){
        if(query!=null && query.length()!=0) {
            Toast.makeText(this.getApplicationContext(),"Procurando...",Toast.LENGTH_LONG).show();
        }else{
            FragmentManager fm = getSupportFragmentManager();
            FragmentFileEx fragex = (FragmentFileEx) fm.findFragmentById(R.id.file_ex_area);
            //fragex.Canceltask();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange (String newText){
        FragmentManager fm = getSupportFragmentManager();
        FragmentFileEx fragex = (FragmentFileEx) fm.findFragmentById(R.id.file_ex_area);
        fragex.NewSearch(newText);
        return false;
    }

    @Override
    public void setPdfActivity(MyArquive arq) {
        db.myCommit(arq);
        Bundle bundle = new Bundle();
        bundle.putString("path", arq.getPath());
        Intent intent = new Intent(this, PdfReaderActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void Scanner(String qR) {
        abrirexplorador(qR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ((NavigationView)findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_edit_window);
            abrirEditPage();
            return true;
        }
        if (id== R.id.action_list_mode){
            inline=!inline;
            if(inline){
                item.setIcon(R.drawable.ic_view_module_black_24dp);
            }
            else {
                item.setIcon(R.drawable.ic_view_list_black_24dp);
            }
            abrirFavoritos();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        item.setChecked(true);
        setMainFrag(item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean setMainFrag(int id){
        boolean returnstatement;
        if (id == R.id.nav_arq_window) {
            this.abrirArqPage();
            this.attToolbarArqPage();
            returnstatement=true;
        }  else if (id == R.id.nav_edit_window) {
            this.attToolbarEditPage();
            this.abrirEditPage();
            returnstatement=true;
        } else if (id == R.id.nav_fav_explorer) {
            this.attToolbarFavoritos();
            this.abrirFavoritos();
            returnstatement=true;
        } else if (id == R.id.nav_add_qr_code) {
            this.abrirAddQr();
            this.attToolbarAddQr();
            returnstatement=true;
        } else if (id == R.id.nav_user_manager) {
            this.abrirAcountMan();
            this.attToolbarAcountMan();
            returnstatement=true;
        }else {
            Log.wtf("nao achor","nd");
            returnstatement=false;
        }
        navDrawerSelected=id;
        return returnstatement;
    }

    private boolean refreshToolbar(int id){
        if (id == R.id.nav_arq_window) {
            this.attToolbarArqPage();
            return true;
        }  else if (id == R.id.nav_edit_window) {
            this.attToolbarEditPage();
            return true;
        } else if (id == R.id.nav_fav_explorer) {
            this.attToolbarFavoritos();
            return true;
        } else if (id == R.id.nav_add_qr_code) {
            this.attToolbarAddQr();
            return true;
        } else if (id == R.id.nav_user_manager) {
            this.attToolbarAcountMan();
            return true;
        }else {
            Log.wtf("nao achor","nd");
            return false;
        }
    }

    public void botaoLeitorDeQR(View view) {
          fragMain.performClick(0);
          onScannerClick(findViewById(R.id.screen_area));
    }

    // metodo de selecao do drawer
    private void attToolbarArqPage(){
        this.setTitle(R.string.title_activity_main_navigation_drawer);
        if(tabselected==0) {
            findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        }
        if(tabselected==1){
            findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
        }
        if(tabselected==0){searchItem.setVisible(false);}
        else{searchItem.setVisible(true);}
        configItem.setVisible(true);
        listCardItem.setVisible(false);
    }

    private void abrirArqPage(){
        if(fragMain==null){fragMain=new FragmentMainTabs();}
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMain).commit();
    }

    private void attToolbarEditPage(){
        this.setTitle(R.string.configuracoes);
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        searchItem.setVisible(false);
        configItem.setVisible(false);
        listCardItem.setVisible(false);
    }

    // metodo de selecao do drawer
    private void abrirEditPage(){
        EditScreen config= new EditScreen();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, config).commit();
    }

    private void attToolbarFavoritos(){
        this.setTitle("Favoritos");
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        searchItem.setVisible(false);
        configItem.setVisible(true);
        listCardItem.setVisible(true);
    }

    // metodo de selecao do drawer
    private void abrirFavoritos(){
        StaredFragment fav= new StaredFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fav).commit();
    }

    private void attToolbarAddQr(){
        this.setTitle("Gerador de QR");
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        searchItem.setVisible(false);
        configItem.setVisible(true);
        listCardItem.setVisible(false);
    }

    // metodo de selecao do drawer
    private void abrirAddQr(){
        FragmentAddQR mfragment= new FragmentAddQR();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, mfragment).commit();
    }

    private void attToolbarAcountMan(){
        this.setTitle("Gerenciar Usuarios");
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        searchItem.setVisible(false);
        configItem.setVisible(true);
        listCardItem.setVisible(false);
    }

    private void abrirAcountMan(){
        if(navDrawerSelected!=R.id.nav_user_manager){
        ManageAcounts config= new ManageAcounts();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, config).commit();}
    }

    private void abrirexplorador(String Cpass){

        Bundle arguments = new Bundle();
        arguments.putString("arqpath", Cpass);
        arguments.putString("text",null);
        mainFragmentFileEx.refresh(arguments);
        fragMain.performClick(1);

    }







    //permissoes

    public boolean checkPermissoes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result_1 = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            int result_2 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
            int result_3 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return (result_1 == PackageManager.PERMISSION_GRANTED &&
                    result_2 == PackageManager.PERMISSION_GRANTED &&
                    result_3 == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }
    public void requestPermissoes() throws Exception {
        try {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
            }, 0x3);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0x3: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();
                } else {
                    this.finish();
                }
            }

        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void onScannerClick(View view) {
       Toast.makeText(this,R.string.help_scanner,Toast.LENGTH_LONG).show();
    }

    public void setMainFragmentFileEx(FragmentFileEx frag){
        this.mainFragmentFileEx=frag;
    }

    public void setTabselected(int tabselected) {
        this.tabselected = tabselected;
    }

    public int getTabselected() {
        return tabselected;
    }
}
