package com.metaconsultoria.root.scfilemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.metaconsultoria.root.scfilemanager.utils.ExternalDirsUtils;
import com.metaconsultoria.root.scfilemanager.utils.PermissionUtils;
import java.util.Objects;


public class MainNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   SearchView.OnQueryTextListener,
                   FragmentFileEx.FragmentListener,
                   ActivityCompat.OnRequestPermissionsResultCallback {

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
        getPermissoes();

        //encontrando cartao fisico
        ExternalDirsUtils.prepareSdDirs(this);

        //?is protect
        FuncDB fdb= new FuncDB(this);
        if(fdb.getValor("is_protected")==null){
            fdb.saveChave("is_protected","true");
        }

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
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(PermissionUtils.checkPermissoes(this)&& instance==null){
                    if(!setMainFrag(navDrawerSelected)){
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
        }else{
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
        SearchView searchView = (SearchView) searchItem.getActionView();
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

        outState.putInt("nav_drawer_selected",
                        Objects.requireNonNull(((NavigationView) findViewById(R.id.nav_view)).getCheckedItem()).getItemId());

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
            attToolbarEditPage();
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
        } else if (id == R.id.nav_about) {
            this.abrirAbout();
            returnstatement=false;
            id=navDrawerSelected;
        } else if (id == R.id.nav_novo_arq) {
            this.abrirHelp();
            returnstatement=false;
            id=navDrawerSelected;
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
        if(searchItem!=null) {
            if (searchItem.isActionViewExpanded()) {
                searchItem.collapseActionView();
            }
        }
        searchItem.setVisible(false);
        configItem.setVisible(false);
        listCardItem.setVisible(false);
    }

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

    private void abrirAbout(){

        final Dialog certo = new Dialog(this);
        certo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getResources().getConfiguration().screenWidthDp > 600 && getResources().getConfiguration().screenHeightDp > 800){
        certo.setContentView(R.layout.sobre_l);
        }else {
            certo.setContentView(R.layout.sobre_s);
        }
        certo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        certo.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certo.dismiss();
            }
        });
        certo.show();
    }

    private void abrirHelp(){

        final Dialog certo = new Dialog(this);
        certo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getResources().getConfiguration().screenWidthDp > 600 && getResources().getConfiguration().screenHeightDp > 800){
            certo.setContentView(R.layout.novo_arq_l);
        }else {
            certo.setContentView(R.layout.novo_arq_s);
        }
        certo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        certo.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certo.dismiss();
            }
        });
        certo.show();
    }



    public void botaoLeitorDeQR(View view) {
        fragMain.performClick(0);
        onScannerClick(findViewById(R.id.screen_area));
    }





    private void getPermissoes(){
        if(!PermissionUtils.checkPermissoes(this)){
            try {
                PermissionUtils.requestPermissoes(this);
                while(PermissionUtils.checkPermissoes(this));
            } catch (Exception e){
                e.printStackTrace();
            }
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
