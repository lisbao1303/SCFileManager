package com.metaconsultoria.root.scfilemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {
    private EditText text_login_object;
    private EditText text_password_object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_login_object = (EditText) findViewById(R.id.editTextLogin);
        text_login_object.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        text_login_object.setText("");
                        text_login_object.setTextColor(getResources().getColor(R.color.black));
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
}
