package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
            FuncDB fdb= new FuncDB(getContext());
            String lastSave= " Ultima atualizaçao feita por " +
                    func.getNome()+" ("+
                    func.getMatricula()+") em "+
                    NewComentFragment.utilGetDate();
            if(fdb.getValor("last_save")==null){
                fdb.saveChave("last_save",lastSave);
                textViewSave.setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
            }else{
                fdb.updateValue("last_save",lastSave);
                textViewSave.setText(getString(R.string.edit_screen)+fdb.getValor("last_save"));
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
}
