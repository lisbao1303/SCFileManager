package com.metaconsultoria.root.scfilemanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


public class EditScreen extends Fragment implements View.OnClickListener,Switch.OnCheckedChangeListener,AlertDialog.OnCancelListener{
    private LayoutInflater inflater;
    private ViewGroup container;
    private View vi;
    private Switch mySwitch;
    private boolean isElevated;
    private boolean isCleaned=false;
    private Funcionario func;
    private TextView textViewSave;
    private AsyncTask task;
    private AlertDialog dialog;


    public EditScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        container.removeAllViews();
        super.onDestroy();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;
        if(savedInstanceState==null || !(isElevated=savedInstanceState.getBoolean("is_elevated"))){
            vi = inflater.inflate(R.layout.autenticate_layout, container, false);
            vi.findViewById(R.id.button).setOnClickListener(this);
        }else{
            vi = inflater.inflate(R.layout.fragment_edit_screen, container, false);
            mySwitch=((Switch)vi.findViewById(R.id.switch1));
            mySwitch.setOnCheckedChangeListener(this);
            if((new FuncDB(getContext())).getValor("is_protected").equals("true")){
                mySwitch.setChecked(true);
            }
            ImageButton button= vi.findViewById(R.id.buttonLimparCache);
            button.setOnClickListener(this);
            if(savedInstanceState.getBoolean("is_cleaned")){
                button.setAlpha(0.3f);
                button.setClickable(false);
                isCleaned=true;
            }
            vi.findViewById(R.id.buttonSalvar).setOnClickListener(this);
            vi.findViewById(R.id.buttonRestaurar).setOnClickListener(this);
            textViewSave=vi.findViewById(R.id.textViewSave);
            FuncDB fdb = new FuncDB(getContext());
            if(fdb.getValor("last_save")!=null){
                textViewSave.setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
            }
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        if(v==vi.findViewById(R.id.button)){
            String matricula= ((EditText)vi.findViewById(R.id.editText_matricula)).getText().toString();
            String senha = ((EditText)vi.findViewById(R.id.editText_senha)).getText().toString();
            if(autenticateElevatedUser(matricula,senha)){
                container.removeAllViews();
                getLayoutInflater().inflate(R.layout.fragment_edit_screen,container,true);
                mySwitch=((Switch)getActivity().findViewById(R.id.switch1));
                mySwitch.setOnCheckedChangeListener(this);
                if((new FuncDB(getContext())).getValor("is_protected").equals("true")){
                    mySwitch.setChecked(true);
                }
                getActivity().findViewById(R.id.buttonLimparCache).setOnClickListener(this);
                getActivity().findViewById(R.id.buttonSalvar).setOnClickListener(this);
                getActivity().findViewById(R.id.buttonRestaurar).setOnClickListener(this);
                textViewSave=getActivity().findViewById(R.id.textViewSave);
                FuncDB fdb = new FuncDB(getContext());
                if(fdb.getValor("last_save")!=null){
                    textViewSave.setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
                }
            }else{
                Toast.makeText(getContext(),R.string.string_usuario_ou_senha_incorretos,Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.buttonLimparCache){
            RecentFilesDB db= new RecentFilesDB(getContext());
            db.clearDataBase();
            v.setAlpha(0.3f);
            v.setClickable(false);
            isCleaned=true;
        }
        if(v.getId()==R.id.buttonSalvar){
            this.showProgresBarr("Salvando BackUp");
            task=new FileHandlerSaveThread();
            ((FileHandlerSaveThread)task).execute(func);
        }
        if(v.getId()==R.id.buttonRestaurar){
            this.showProgresBarr("Restaurando BackUp");
            task=new FileHandlerRestoreThread();
            ((FileHandlerRestoreThread)task).execute(func);
        }
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("is_elevated",isElevated);
        outState.putBoolean("is_cleaned",isCleaned);
        super.onSaveInstanceState(outState);
    }

    private boolean autenticateElevatedUser(String matricula, String senha){
        FuncDB fdb=new FuncDB(getContext());
        func = fdb.findByMatricula(matricula);
        if(func!=null){
            if(func.getSenha().equals(senha)){return isElevated=true;}
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mySwitch.isChecked()){
            (new FuncDB(getContext())).updateValue("is_protected","true");
        }else{
            (new FuncDB(getContext())).updateValue("is_protected","false");
        }
    }

    private void showProgresBarr(String title){
        final AlertDialog.Builder certo = new AlertDialog.Builder(getContext());
        View v =getLayoutInflater().inflate(R.layout.savin_layout,null);
        certo.setView(v);
        certo.setNegativeButton(android.R.string.cancel,(new DialogInterface.OnClickListener() {
            private Fragment frag;
            public DialogInterface.OnClickListener getInstance(Fragment frag){
                this.frag=frag;
                return this;
            }
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((EditScreen)frag).onCancel(null);
            }
        }).getInstance(this));
        certo.setOnCancelListener(this);
        certo.setTitle(title);
        dialog=certo.show();
    }

    @Override
    public void onCancel(@Nullable DialogInterface dialog) {
        task.cancel(true);
    }


    private class FileHandlerSaveThread extends AsyncTask<Funcionario,Void,Funcionario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Funcionario doInBackground(Funcionario... funcs) {
            for(int i=0;i<1000;i++)Log.wtf("saida","lol");
            return funcs[0];
        }

        @Override
        protected void onCancelled() {
            //task = null;
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(Funcionario func) {
            FuncDB fdb= new FuncDB(getContext());
            String lastSave= " Ultima atualizaçao feita por " +
                    func.getNome()+" ("+
                    func.getMatricula()+") em "+
                    NewComentFragment.utilGetDate();
            if(fdb.getValor("last_save")==null){
                fdb.saveChave("last_save",lastSave);
            }else{
                fdb.updateValue("last_save",lastSave);
            }
            onTaskSaveResult();
        }
    }

    private void onTaskSaveResult(){
        dialog.dismiss();
        FuncDB fdb = new FuncDB(getContext());
        if(fdb.getValor("last_save")!=null){
            textViewSave.setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
            Snackbar.make(getActivity().findViewById(R.id.screen_area),"BackUp salvo",Snackbar.LENGTH_LONG).show();
        }
    }

    private class FileHandlerRestoreThread extends AsyncTask<Funcionario,Void,Funcionario> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Funcionario doInBackground(Funcionario... funcs) {
            for(int i=0;i<1000;i++)Log.wtf("saida","lol");
            return funcs[0];
        }

        @Override
        protected void onCancelled() {
            //task = null;
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(Funcionario func) {
            onTaskRestoreResult();
        }
    }


    private void onTaskRestoreResult(){
        dialog.dismiss();
        Snackbar.make(getActivity().findViewById(R.id.screen_area),"Dados Restaurados",Snackbar.LENGTH_LONG).show();
    }

}
