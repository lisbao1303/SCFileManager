package com.metaconsultoria.root.scfilemanager;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void buttonEntrarHandler(View view){
        String nomeDeUsuario=text_login_object.getText().toString();
        String senha=text_password_object.getText().toString();
        if(autenticacaoDeUsuario(nomeDeUsuario,senha)){
            Bundle bundle = new Bundle();
            bundle.putString("nomeDeUsuario", nomeDeUsuario);
            Intent intent = new Intent(this, MainNavigationDrawerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Usuario Inexistente",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean autenticacaoDeUsuario(String usuario, String senha){
        return true;
    }

}
