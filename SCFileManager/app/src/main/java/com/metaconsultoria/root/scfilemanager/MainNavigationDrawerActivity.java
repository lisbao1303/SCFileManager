package com.metaconsultoria.root.scfilemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

public class MainNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, FragmentFileEx.FragmentListener {
    private String matricula;
    private final Activity activity = this;
    private FragmentFileEx fragMenu = new FragmentFileEx();
    private String codigoqr=null;
    private String mainpath = Environment.getExternalStorageDirectory().getPath();
    private MenuItem searchItem;
    public boolean explorer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermissionForReadExtertalStorage()){
        }else{
            try {
                requestPermissionForReadExternalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main_navigation_drawer);
        Intent intentActivityMain = getIntent();
        Bundle bundle = intentActivityMain.getExtras();
        matricula = bundle.getString("nomeDeUsuario");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ATENCAO: Os elementos do Navigation Drawer sao instaciados somente aqui n icluir eles no onCreate
        //
        getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
        TextView nome_field = (TextView) findViewById(R.id.nav_text_nome_usuario);
        nome_field.setText(getString(R.string.nome_const)+"Thiago de Souza Alves");
        TextView matricula_field = (TextView) findViewById(R.id.nav_text_numero_de_matricula);
        matricula_field.setText(getString(R.string.matricula_const)+matricula);

        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
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
    public void metodo() {
        Bundle arguments = new Bundle();
        arguments.putString("caminho", fragMenu.file.toString());
        FragmentPDF fragment = new FragmentPDF();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragment).commit();
        searchItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_openQR) {
            this.abrirLeitorDeQR();
        } else if (id == R.id.nav_open_explorer) {
            this.abrirexplorador();
        } else if (id == R.id.nav_edit_window) {
            this.abrirEditorDeArquivos();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void botaoLeitorDeQR(View view) {
         this.abrirLeitorDeQR();
    }

    private void abrirLeitorDeQR(){
        IntentIntegrator integrator;
        integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    public void abrirexplorador(){
        fragMenu = new FragmentFileEx();
        Bundle arguments = new Bundle();
        if(codigoqr!=null){
            arguments.putString("arqpath",mainpath+codigoqr);
            arguments.putString("text",null);
            fragMenu.setArguments(arguments);
            codigoqr=null;
        }else{
            arguments.putString("arqpath",mainpath);
            arguments.putString("text",null);
            fragMenu.setArguments(arguments);
        }
        this.getSupportFragmentManager().beginTransaction().replace(R.id.screen_area, fragMenu).commit();

        searchItem.setVisible(true);
        explorer = true;

    }

    private void abrirEditorDeArquivos(){

    }

    //ciclo de vida da activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if (result.getContents() != null){
                codigoqr = result.getContents();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        if(codigoqr!=null) {
            boolean exists = (new File(mainpath + codigoqr)).exists();
            if(exists){
                abrirexplorador();
            } else{
                codigoqr=null;
                Toast.makeText(getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
            }
        }
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(explorer) {
            searchItem.setVisible(true);
        }
    }

    //permissoes

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExternalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x3);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
