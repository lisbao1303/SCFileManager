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
import android.widget.Switch;
import android.widget.Toast;


public class EditScreen extends Fragment implements View.OnClickListener,Switch.OnCheckedChangeListener{
    private LayoutInflater inflater;
    private ViewGroup container;
    private View vi;
    private Switch mySwitch;
    private boolean isElevated;


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
        if(savedInstanceState==null || savedInstanceState.getBoolean("is_elevated")==false){
            vi = inflater.inflate(R.layout.autenticate_layout, container, false);
            vi.findViewById(R.id.button).setOnClickListener(this);
        }else{
            vi = inflater.inflate(R.layout.fragment_edit_screen, container, false);
            mySwitch=((Switch)vi.findViewById(R.id.switch1));
            mySwitch.setOnCheckedChangeListener(this);
            if(ConstantesDoProjeto.getInstance().isProtect()){
                mySwitch.setChecked(true);
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
                if(ConstantesDoProjeto.getInstance().isProtect()){
                    mySwitch.setChecked(true);
                }
            }else{
                Toast.makeText(getContext(),R.string.string_usuario_ou_senha_incorretos,Toast.LENGTH_SHORT).show();
            }
        }

        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("is_elevated",isElevated);
        super.onSaveInstanceState(outState);
    }

    private boolean autenticateElevatedUser(String matricula, String senha){
        FuncDB fdb=new FuncDB(getContext());
        Funcionario func = fdb.findByMatricula(matricula);
        if(func!=null){
            if(func.getSenha().equals(senha)){return isElevated=true;}
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mySwitch.isChecked()){
            ConstantesDoProjeto.getInstance().setProtect(true);
            Log.wtf("carai",String.valueOf(ConstantesDoProjeto.getInstance().isProtect()));
        }else{
            ConstantesDoProjeto.getInstance().setProtect(false);
        }
    }
}
