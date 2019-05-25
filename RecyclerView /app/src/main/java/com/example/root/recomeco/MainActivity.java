package com.example.root.recomeco;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    boolean desenvolvedor=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragtran = fm.beginTransaction();
            FragmentMain frag = new FragmentMain();
            fragtran.add(R.id.fragment_frame,frag,"FragmentMain");
            fragtran.commit();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) throws java.lang.NullPointerException {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ActionBar act_bar;
        act_bar=getSupportActionBar();
        act_bar.setIcon(R.drawable.ic_home_invc);
        act_bar.setDisplayShowHomeEnabled(true);
        act_bar.setTitle(R.string.titulo_menu_principal);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.action_atualizar){
            Toast.makeText(this,R.string.alerta_atualizacao,Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

