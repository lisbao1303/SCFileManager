package com.metaconsultoria.root.scfilemanager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class EditScreen extends Fragment implements View.OnClickListener,Switch.OnCheckedChangeListener{
    private LayoutInflater inflater;
    private ViewGroup container;
    private View vi;
    private Switch mySwitch;
    private boolean isElevated;
    private boolean isCleaned=false;
    private Funcionario func;
    private TextView textViewSave;


    public EditScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        container.removeAllViews();
        ConstantesDoProjeto.getInstance().setEditScreenShow(false);
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
            ConstantesDoProjeto.getInstance().setEditScreenShow(true);
            ConstantesDoProjeto.getInstance().setEditScreen(this);
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
                ConstantesDoProjeto.getInstance().setEditScreenShow(true);
                ConstantesDoProjeto.getInstance().setEditScreen(this);
                if(ConstantesDoProjeto.getInstance().isSaving()){
                    this.showProgresBarrSave();
                }
                if(ConstantesDoProjeto.getInstance().isRestoring()){
                    this.showProgresBarrRestore();
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
            if(!ConstantesDoProjeto.getInstance().isRestoring()) {
                this.showProgresBarrSave();
                File file=new File(ConstantesDoProjeto.getInstance().getMainPathProtected());
                if(file.exists()){
                    new FileHandlerSaveThread().execute(getFunc());
                } else{
                    Toast.makeText(getContext(),"Arquivo nao Encontrado",Toast.LENGTH_LONG).show();
                    this.closeProgresBarrSave();
                }
            }else{
                Toast.makeText(getActivity(),"Restauraçao em andamento",Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==R.id.buttonRestaurar){
            if(!ConstantesDoProjeto.getInstance().isSaving()) {
                this.showProgresBarrRestore();
                File file=new File(getActivity().getExternalFilesDir(null).getPath()+"/ArquivosSouza");
                if(file.exists()) {
                    new FileHandlerRestoreThread().execute(this);
                }else{
                    Toast.makeText(getContext(),"BackUp nao Encontrado",Toast.LENGTH_LONG).show();
                    this.closeProgresBarrSave();
                }
            }else{
                Toast.makeText(getActivity(),"BackUp em andamento",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("is_elevated",isElevated);
        outState.putBoolean("is_cleaned",isCleaned);
        super.onSaveInstanceState(outState);
    }

    private boolean autenticateElevatedUser(String matricula, String senha){
        if (matricula.substring(matricula.length() - 1).equals(" ")) {
            matricula = matricula.substring(0, matricula.length() - 1);
        }
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

    private void showProgresBarrSave(){
        ImageButton imgbut= getActivity().findViewById(R.id.buttonSalvar);
        imgbut.setImageAlpha(0);
        imgbut.setClickable(false);
        getActivity().findViewById(R.id.progressBarSave).setVisibility(View.VISIBLE);
    }

    private void closeProgresBarrSave(){
        ImageButton imgbut= getActivity().findViewById(R.id.buttonSalvar);
        imgbut.setImageAlpha(255);
        imgbut.setClickable(true);
        getActivity().findViewById(R.id.progressBarSave).setVisibility(View.INVISIBLE);
    }

    private void showProgresBarrRestore(){
        ImageButton imgbut= getActivity().findViewById(R.id.buttonRestaurar);
        imgbut.setImageAlpha(0);
        imgbut.setClickable(false);
        getActivity().findViewById(R.id.progressBarRestore).setVisibility(View.VISIBLE);
    }

    private void closeProgresBarrRestore(){
        ImageButton imgbut= getActivity().findViewById(R.id.buttonRestaurar);
        imgbut.setImageAlpha(255);
        imgbut.setClickable(true);
        getActivity().findViewById(R.id.progressBarRestore).setVisibility(View.INVISIBLE);
    }


    private class FileHandlerSaveThread extends AsyncTask<Funcionario,Void,Funcionario> {

        private final Context contx=getContext();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConstantesDoProjeto.getInstance().setSaving(true);
        }

        @Override
        protected Funcionario doInBackground(Funcionario... funcs) {
            FileHandler.copiDirectory(ConstantesDoProjeto.getInstance().getMainPathProtectedCopy(),
                        "/ArquivosSouza",
                                        ConstantesDoProjeto.getInstance().getBackUpPath());
            return funcs[0];
        }

        @Override
        protected void onCancelled() {
            //task = null;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Funcionario func) {
            FuncDB fdb= new FuncDB(contx);
            String lastSave= " Ultima atualizaçao feita por " +
                    func.getNome()+" ("+
                    func.getMatricula()+") em "+
                    NewComentFragment.utilGetDate();
            if(fdb.getValor("last_save")==null){
                fdb.saveChave("last_save",lastSave);
            }else{
                fdb.updateValue("last_save",lastSave);
            }
            if(contx.getApplicationContext()!=null) {
                Toast.makeText(contx.getApplicationContext(), "BackUp salvo", Snackbar.LENGTH_LONG).show();
            }
            if(ConstantesDoProjeto.getInstance().isEditScreenShow()){
                ConstantesDoProjeto.getInstance().getEditScreen().onTaskSaveResult();
            }
            ConstantesDoProjeto.getInstance().setSaving(false);
        }
    }

    private void onTaskSaveResult(){
        FuncDB fdb = new FuncDB(getContext());
        if(fdb.getValor("last_save")!=null){
            ((TextView)getActivity().findViewById(R.id.textViewSave))
                    .setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
        }
        this.closeProgresBarrSave();
    }

    private class FileHandlerRestoreThread extends AsyncTask<Fragment,Void,Fragment> {

        private final Context contx=getContext();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConstantesDoProjeto.getInstance().setRestoring(true);
        }

        @Override
        protected Fragment doInBackground(Fragment... contx) {
            FileHandler.copiDirectory(ConstantesDoProjeto.getInstance().getBackUpPath(),
                    "/ArquivosSouza",
                    ConstantesDoProjeto.getInstance().getMainPathProtectedCopy());
            return contx[0];
        }

        @Override
        protected void onCancelled() {
            //task = null;
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(Fragment fragment) {
            if(contx.getApplicationContext()!=null) {
                Toast.makeText(contx.getApplicationContext(), "Restauraçao Completa", Snackbar.LENGTH_LONG).show();
            }
            if(ConstantesDoProjeto.getInstance().isEditScreenShow()){
                ConstantesDoProjeto.getInstance().getEditScreen().onTaskRestoreResult();
            }
            ConstantesDoProjeto.getInstance().setRestoring(false);
        }
    }


    private void onTaskRestoreResult(){
        //dialog.dismiss();
        this.closeProgresBarrRestore();
    }

    public Funcionario getFunc(){
        return this.func;
    }

}
