package com.metaconsultoria.root.scfilemanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class PdfReaderActivity extends AppCompatActivity {
        private String arqpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arqpath = getIntent().getExtras().getString("path");
        Bundle arguments= new Bundle();
        arguments.putString("caminho",arqpath);;
        FragmentPDF fragment= new FragmentPDF();
        fragment.setArguments(arguments);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.pdf_screen_area,fragment).commit();
    }

}
