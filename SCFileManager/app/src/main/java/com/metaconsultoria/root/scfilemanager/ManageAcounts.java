package com.metaconsultoria.root.scfilemanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageAcounts extends Fragment implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback{
    private LayoutInflater inflater;
    private ViewGroup container;
    private View vi;
    private boolean isElevated=false;


    public ManageAcounts() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        container.removeAllViews();
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        if(isElevated){
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.func_frag_area,new AccountList()).commit();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater=inflater;
        this.container=container;
        if(savedInstanceState!=null){
            isElevated=savedInstanceState.getBoolean("is_elevated");
        }
        if(!isElevated){
            vi = inflater.inflate(R.layout.autenticate_layout, container, false);
            vi.findViewById(R.id.button).setOnClickListener(this);
        }else{
            vi=inflater.inflate(R.layout.fragment_manage_acounts,container,false);
            vi.findViewById(R.id.floatingActionButtonAdd).setOnClickListener(this);
            vi.findViewById(R.id.floatingActionButtonRemoveaAct).setOnClickListener(this);
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
                getLayoutInflater().inflate(R.layout.fragment_manage_acounts,container,true);
                getActivity().findViewById(R.id.floatingActionButtonAdd).setOnClickListener(this);
                getActivity().findViewById(R.id.floatingActionButtonRemoveaAct).setOnClickListener(this);
                this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.func_frag_area,new AccountList()).commit();

            }else{
                Toast.makeText(getContext(),R.string.string_usuario_ou_senha_incorretos,Toast.LENGTH_SHORT).show();
            }
        }

        if(v==container.getRootView().findViewById(R.id.floatingActionButtonAdd)){
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst",false);
            Intent intent = new Intent(this.getActivity(), ActivityNewFunc.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,ConstantesDoProjeto.NEW_USER_REQUEST);
        }

        if(v==container.getRootView().findViewById(R.id.floatingActionButtonRemoveaAct)){
            new AlertDialog.Builder(getContext())
                    .setTitle("Deletar Conta Atual")
                    .setMessage("Deseja deletar Thiago de Souza Alves")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"hj o pai ta chato",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.ic_close_black_24dp)
                    .show();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("is_elevated",isElevated);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==ConstantesDoProjeto.NEW_USER_REQUEST){
            if(resultCode== Activity.RESULT_OK){

            }else{
                Toast.makeText(getContext(),"Usuario nao salvo",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean autenticateElevatedUser(String matricula, String senha){
        FuncDB fdb=new FuncDB(getContext());
        Funcionario func = fdb.findByMatricula(matricula);
        if(func!=null){
            if(func.getSenha().equals(senha)){return isElevated=true;}
        }
        return false;
    }


}
