package com.example.loginjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginjava.objs.UsuarioAuth;
import com.example.loginjava.objs.UsuarioAuthCallback;


public class MainActivity extends AppCompatActivity {

    private EditText loginEmail_edittext;
    private EditText loginSenha_edittext;
    private Button loginLogar_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void logar() {
        final Context context = this;

        final String email = loginEmail_edittext.getText().toString();
        final String senha = loginSenha_edittext.getText().toString();


        if(email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {
            // Desativa os inputs
            loginEmail_edittext.setEnabled(false);
            loginSenha_edittext.setEnabled(false);
            loginLogar_button.setEnabled(false);

            // Tenta logar o usuário
            UsuarioAuth.logar(email, senha, new UsuarioAuthCallback() {
                @Override
                public void onSuccess() {
                    Intent in = new Intent(context, Inicio.class);
                    startActivity(in);

                    // Ativa os inputs
                    loginEmail_edittext.setEnabled(true);
                    loginSenha_edittext.setEnabled(true);
                    loginLogar_button.setEnabled(true);
                }

                @Override
                public void onFailure(Exception ex) {

                    if(ex.getMessage() != null && ex.getMessage().contains("The email address is badly formatted.")) {
                        Toast.makeText(context, "Preencha o e-mail corretamenta", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Senha ou E-mail incorretos", Toast.LENGTH_LONG).show();
                    }

                    // Ativa os inputs
                    loginEmail_edittext.setEnabled(true);
                    loginSenha_edittext.setEnabled(true);
                    loginLogar_button.setEnabled(true);
                }

                @Override
                public void onFailureMessage(String ex) {
                    Toast.makeText(context, ex, Toast.LENGTH_LONG).show();

                    // Ativa os inputs
                    loginEmail_edittext.setEnabled(true);
                    loginSenha_edittext.setEnabled(true);
                    loginLogar_button.setEnabled(true);
                }
            });
        }
    }

    public void login_toCadastro(View v) {
        Intent in = new Intent(this, Cadastro.class);
        startActivity(in);
    }

    public void login_toTrocaSenha(View v) {
        String email = loginEmail_edittext.getText().toString();

        Intent in = new Intent(this, RedefinirSenha.class);

        if(!email.isEmpty()) {
            in.putExtra("loginEmail", email);
        }

        startActivity(in);
    }



    private void init() {
        loginEmail_edittext = findViewById(R.id.loginEmail);
        loginSenha_edittext = findViewById(R.id.editTextTextPassword);

        loginLogar_button = findViewById(R.id.loginButton);
        loginLogar_button.setOnClickListener((View v) -> logar());
    }
}