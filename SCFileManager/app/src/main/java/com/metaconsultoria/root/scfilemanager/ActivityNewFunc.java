package com.metaconsultoria.root.scfilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityNewFunc extends AppCompatActivity implements View.OnClickListener {
    private boolean isFirstAcess;
    private Funcionario generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_func);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.string_cadastrar_usuario);

        Bundle bundle= getIntent().getExtras();
        isFirstAcess =bundle.getBoolean("isFirst");

        ((Button)findViewById(R.id.button_add)).setOnClickListener(this);
        View cancel = ((Button)findViewById(R.id.button_cancel));
        cancel.setOnClickListener(this);

        if(isFirstAcess){
            cancel.setClickable(false);
            cancel.setAlpha(0.3f);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            FuncDB fdb = new FuncDB(this);
            generator=fdb.findByMatricula(bundle.getString("generator"));
        }
    }

    @Override
    public void onClick(View v) {

        if(v==findViewById(R.id.button_add)){
            String nome = ((EditText)findViewById(R.id.editText_nome)).getText().toString();
            String matricula = ((EditText)findViewById(R.id.editText_matricula)).getText().toString();
            String restauracao = ((EditText)findViewById(R.id.editText_restauracao)).getText().toString();
            String senha = ((EditText)findViewById(R.id.editText_senha)).getText().toString();
            String confirmSenha = ((EditText)findViewById(R.id.editText_confirm_senha)).getText().toString();

            boolean check =isThereAnEmptyText  (nome,
                                                matricula,
                                                restauracao,
                                                senha,
                                                confirmSenha);

            if(check) {
                if (nome.substring(nome.length() - 1).equals(" ")) {
                    nome = nome.substring(0, nome.length() - 1);
                }
                if (matricula.substring(matricula.length() - 1).equals(" ")) {
                    matricula = matricula.substring(0, matricula.length() - 1);
                }
                if (restauracao.substring(restauracao.length() - 1).equals(" ")) {
                    restauracao = restauracao.substring(0, restauracao.length() - 1);
                }


                check = isSpaceCorect(matricula,
                                        restauracao,
                                        senha,
                                        confirmSenha);
            }

            if(check) {
                if (senha.equals(confirmSenha)) {
                    FuncDB fdb = new FuncDB(this);
                    if(fdb.findByMatricula(matricula)==null){
                        Funcionario fx = new Funcionario();
                        fx.setNome(nome);
                        fx.setMatricula(matricula);
                        fx.setSenha(senha);
                        fx.setRestauracao(restauracao);
                        if (isFirstAcess) {
                            fdb.save(fx);
                        } else {
                            fdb.save(fx, generator);
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("matricula", matricula);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }else{
                        new AlertDialog.Builder(this)
                                .setTitle("Usuario Ja Cadastrado")
                                .setMessage("A matricula "+matricula+" ja esta cadastrada no banco "+
                                        "de daddos, remova o usuario cadastrado antes de salvar o "+
                                        "novo funcionario.")
                                .setNeutralButton(android.R.string.ok, null)
                                .show();
                    }
                } else {
                    Toast.makeText(this, "Senhas conflitantes", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(v==findViewById(R.id.button_cancel)){
            if(!isFirstAcess){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }

    }

    private boolean isThereAnEmptyText(String nome,String matricula,String restauracao, String senha, String comfirmSenha){
        if(nome.equals("")||nome.equals(" ")||nome.equals(getString(R.string.string_nome))){
            String buffer= this.addToBufferEmpty(getString(R.string.string_nome));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(matricula.equals("")||matricula.equals(" ")||matricula.equals(getString(R.string.string_matricula))){
            String buffer= this.addToBufferEmpty(getString(R.string.string_matricula));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(restauracao.equals("")||restauracao.equals(" ")||restauracao.equals(getString(R.string.string_email))){
            String buffer= this.addToBufferEmpty(getString(R.string.string_email));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.equals("")||senha.equals(" ")||senha.equals(getString(R.string.string_senha))){
            String buffer= this.addToBufferEmpty(getString(R.string.string_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(comfirmSenha.equals("")||comfirmSenha.equals(" ")||comfirmSenha.equals(getString(R.string.string_confirm_senha))){
            String buffer= this.addToBufferEmpty(getString(R.string.string_confirm_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isSpaceCorect(String matricula,String restauracao, String senha, String comfirmSenha){
        if(matricula.indexOf(' ')>=0){
            String buffer= this.addToBufferSpaceError(getString(R.string.string_matricula));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(restauracao.indexOf(' ')>=0){
            String buffer= this.addToBufferSpaceError(getString(R.string.string_email));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.indexOf(' ')>=0){
            String buffer= this.addToBufferSpaceError(getString(R.string.string_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(comfirmSenha.indexOf(' ')>=0){
            String buffer= this.addToBufferSpaceError(getString(R.string.string_confirm_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String addToBufferEmpty(String add){
        return getString(R.string.string_cadastrar_usuario_erro_1)+add+getString(R.string.string_cadastrar_usuario_erro_2);
    }

    private String addToBufferSpaceError(String add){
        return getString(R.string.string_cadastrar_usuario_erro_1)+add+getString(R.string.string_cadastrar_usuario_erro_3);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
