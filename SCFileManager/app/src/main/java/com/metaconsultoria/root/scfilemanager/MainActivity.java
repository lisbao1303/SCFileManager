package com.metaconsultoria.root.scfilemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText text_login_object;
    private EditText text_password_object;
    private boolean isFirstChangeNome = true;
    private boolean isFirstChangePassword =true;
    private UsersDB db;
    private Funcionario func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new UsersDB(this);
        if(db.findByMatricula("11711EMT002")==null){
            func= new Funcionario("Thiago de Souza Alves","11711EMT002","teste");
            db.init(func);
        }
    }

    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        text_login_object = (EditText) findViewById(R.id.editTextLogin);
        text_login_object.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(isFirstChangeNome) {
                            text_login_object.setText("");
                            text_login_object.setTextColor(getResources().getColor(R.color.black));
                            text_login_object.setCursorVisible(true);
                            isFirstChangeNome=false;
                        }
                    }
                }
        );
        text_login_object.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
        text_password_object = (EditText) findViewById(R.id.editTextPassword);
        text_password_object.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(isFirstChangePassword) {
                            text_password_object.setText("");
                            text_password_object.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            text_password_object.setTextColor(getResources().getColor(R.color.black));
                            text_password_object.setCursorVisible(true);
                            isFirstChangePassword=false;
                        }
                    }
                }
        );
        text_password_object.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
    }

    public void buttonEntrarHandler(View view) {
        String nomeDeUsuario=text_login_object.getText().toString();
        String senha=text_password_object.getText().toString();
        //Toast.makeText(this,"" + db.findAll().size(),Toast.LENGTH_SHORT).show();
        if(/*autenticacaoDeUsuario(nomeDeUsuario,senha)*/true){
            Bundle bundle = new Bundle();
            bundle.putString("nomeDeUsuario", nomeDeUsuario);
            Intent intent = new Intent(this, MainNavigationDrawerActivity.class);
            intent.putExtras(bundle);
            resetParametros(true);
            //Toast.makeText(this,Integer.toString(db.findAll().size()),Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    private void resetParametros(boolean parametro){
        this.isFirstChangeNome=parametro;
        this.isFirstChangePassword=parametro;
    }

    private boolean autenticacaoDeUsuario(@NonNull String usuario, String senha) {
        if(usuario.equals("")){
            Toast.makeText(this,getString(R.string.erro_login_2),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(usuario.equals(getString(R.string.login_name_text))){
            Toast.makeText(this,getString(R.string.erro_login_2),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(senha.equals("")){
            Toast.makeText(this,getString(R.string.erro_login_3),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(senha.equals(getString(R.string.login_password_text))){
            Toast.makeText(this,getString(R.string.erro_login_3),Toast.LENGTH_SHORT).show();
            return false;
        }

        Funcionario teste = db.findByMatricula(usuario);
        if(teste!=null) {
            if (teste.getSenha().equals(senha)) {
                return true;
            }
        }

        Toast.makeText(this,getString(R.string.erro_login_1),Toast.LENGTH_SHORT).show();
        return false;
    }

}