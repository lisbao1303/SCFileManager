package com.metaconsultoria.root.scfilemanager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
    private Funcionario funcionario;


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
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            isElevated=savedInstanceState.getBoolean("is_elevated");
            FuncDB fdb= new FuncDB(getContext());
            String matricula =savedInstanceState.getString("manager");
            funcionario=fdb.findByMatricula(matricula);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if(isElevated){
            AccountList manFuncList= new AccountList();
            Bundle b= new Bundle();
            b.putString("manager",this.funcionario.getMatricula());
            manFuncList.setArguments(b);
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.func_frag_area,manFuncList).commit();
        }
        super.onResume();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater=inflater;
        this.container=container;
        Log.wtf("que","loucura");
        if(!isElevated){
            vi = inflater.inflate(R.layout.autenticate_layout, container, false);
            vi.findViewById(R.id.button).setOnClickListener(this);
        }else{
            vi=inflater.inflate(R.layout.fragment_manage_acounts,container,false);
            FloatingActionButton buttonX=vi.findViewById(R.id.floatingActionButtonRemoveaAct);
            if(funcionario.getGeneratedBy()==null) {
                buttonX.setClickable(false);
                buttonX.setVisibility(FloatingActionButton.INVISIBLE);
            }else{
                buttonX.setOnClickListener(this);
            }
            vi.findViewById(R.id.floatingActionButtonAdd).setOnClickListener(this);
            ((TextView)vi.findViewById(R.id.textViewPrimaryName)).setText(this.funcionario.getFirstName());
            ((TextView)vi.findViewById(R.id.textViewName)).setText(this.getText(R.string.nome_const)+this.funcionario.getNome());
            ((TextView)vi.findViewById(R.id.textViewMatricula)).setText(this.getText(R.string.matricula_const)+this.funcionario.getMatricula());
            AccountList manFuncList= new AccountList();
            Bundle b= new Bundle();
            b.putString("manager",this.funcionario.getMatricula());
            manFuncList.setArguments(b);
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.func_frag_area,manFuncList).commit();
        }
        return vi;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {

        if(v==vi.findViewById(R.id.button)){
            String matricula= ((EditText)vi.findViewById(R.id.editText_matricula)).getText().toString();
            String senha = ((EditText)vi.findViewById(R.id.editText_senha)).getText().toString();
            if(autenticateElevatedUser(matricula,senha)){
                container.removeAllViews();
                getLayoutInflater().inflate(R.layout.fragment_manage_acounts,container,true);
                FloatingActionButton buttonX=getActivity().findViewById(R.id.floatingActionButtonRemoveaAct);
                if(funcionario.getGeneratedBy()==null) {
                    buttonX.setClickable(false);
                    buttonX.setVisibility(FloatingActionButton.INVISIBLE);
                }else{
                    buttonX.setOnClickListener(this);
                }
                getActivity().findViewById(R.id.floatingActionButtonAdd).setOnClickListener(this);
                ((TextView)getActivity().findViewById(R.id.textViewPrimaryName)).setText(this.funcionario.getFirstName());
                ((TextView)getActivity().findViewById(R.id.textViewName)).setText(this.getText(R.string.nome_const)+this.funcionario.getNome());
                ((TextView)getActivity().findViewById(R.id.textViewMatricula)).setText(this.getText(R.string.matricula_const)+this.funcionario.getNome());
                AccountList manFuncList= new AccountList();
                Bundle b= new Bundle();
                b.putString("manager",this.funcionario.getMatricula());
                manFuncList.setArguments(b);
                this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.func_frag_area,manFuncList).commit();

            }else{
                Toast.makeText(getContext(),R.string.string_usuario_ou_senha_incorretos,Toast.LENGTH_SHORT).show();
            }
        }

        if(v==container.getRootView().findViewById(R.id.floatingActionButtonAdd)){
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst",false);
            bundle.putString("generator",funcionario.getMatricula());
            Intent intent = new Intent(this.getActivity(), ActivityNewFunc.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,ConstantesDoProjeto.NEW_USER_REQUEST);
        }

        if(v==container.getRootView().findViewById(R.id.floatingActionButtonRemoveaAct)){
            new AlertDialog.Builder(getContext())
                    .setTitle("Deletar Conta Atual")
                    .setMessage("Deseja deletar "+funcionario.getNome())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            FuncDB fdb=new FuncDB(getContext());
                            fdb.deleteByMatricula(funcionario.getMatricula());
                            ((MainNavigationDrawerActivity)getActivity()).setMainFrag(R.id.nav_arq_window);
                            ((NavigationView)((MainNavigationDrawerActivity)getActivity()).findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_arq_window);
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
        if(funcionario!=null) {
            outState.putString("manager", funcionario.getMatricula());
        }
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
        if (matricula.substring(matricula.length() - 1).equals(" ")) {
            matricula = matricula.substring(0, matricula.length() - 1);
        }
        FuncDB fdb=new FuncDB(getContext());
        Funcionario func = fdb.findByMatricula(matricula);
        if(func!=null){
            if(func.getSenha().equals(senha)){
                this.funcionario=func;
                return isElevated=true;
            }
        }
        return false;
    }


}
