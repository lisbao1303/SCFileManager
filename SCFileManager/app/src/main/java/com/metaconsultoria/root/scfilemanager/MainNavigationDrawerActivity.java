package com.metaconsultoria.root.scfilemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, FragmentFileEx.FragmentListener{

    //private String matricula;
    private FragmentFileEx fragMenu = new FragmentFileEx();
    private String mainpath = Environment.getExternalStorageDirectory().getPath();
    private MenuItem searchItem;
    private NavigationView navigationView;
    private UsersDB db;
    private Funcionario fx;
    private SearchView searchView;
    private Toolbar toolbar;
    private FragmentMainTabs fragMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermissoes()){
        }else{
            try {
                requestPermissoes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main_navigation_drawer);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragMain=new FragmentMainTabs();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMain).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ATENCAO: Os elementos do Navigation Drawer sao instaciados somente aqui n icluir eles no onCreate

        getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Procurar...");
        return true;
    }

    @Override
    public boolean onQueryTextSubmit (String query){
        return false;
    }

    @Override
    public boolean onQueryTextChange (String newText){
        fragMenu.NewSearch(newText);
        return false;
    }

    @Override
    public void setpdffrag() {

        Bundle arguments = new Bundle();
        arguments.putString("caminho", fragMenu.file.toString());
        FragmentPDF fragment = new FragmentPDF();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragment).commit();
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        toolbar.collapseActionView();
        this.setTitle("Visualizador de PDF");
        searchItem.setVisible(false);
    }

    @Override
    public void Scanner(String qR) {
        abrirexplorador(qR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"configuraç�es",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_arq_window) {
            this.abrirArqPage();
        }

        /* else if (id == R.id.nav_open_explorer) {
            this.abrirexplorador(mainpath);
        } else if (id == R.id.nav_edit_window) {
            this.abrirEditorDeArquivos();

        }*/
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //public void botaoLeitorDeQR(View view) {
    //    navigationView.setCheckedItem(R.id.nav_openQR);
    //    this.abrirLeitorDeQR();
    //}

    private void abrirArqPage(){
      this.setTitle(R.string.arq_title);
      //  findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
      //  searchItem.setVisible(false);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMain).commit();
    }

    private void abrirexplorador(String Cpass){
        this.setTitle(R.string.ex_title);
        fragMenu = new FragmentFileEx();
        Bundle arguments = new Bundle();
        arguments.putString("arqpath",Cpass);
        arguments.putString("text",null);
        fragMenu.setArguments(arguments);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMenu).commit();
        if(findViewById(R.id.floatingActionButton).getVisibility()==View.INVISIBLE){
            findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
        }
        searchItem.setVisible(true);
    }

    private void abrirEditorDeArquivos(){
        this.setTitle(R.string.arq_ed_title);
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
        return false;
    }
    public void requestPermissoes() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0x3);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
