package com.example.loginjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginjava.objs.UsuarioAuth;
import com.example.loginjava.objs.UsuarioAuthCallback;

import java.util.Objects;

public class Cadastro extends AppCompatActivity {

    private EditText nome_edittext;
    private EditText email_edittext;
    private EditText senha_edittext;
    private EditText senharepita_edittext;
    private Button cadastro_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        init();
    }


    private void cadastrar() {
        // Adquiri o contexto
        final Context context = this;

        // Coleta as strings
        final String nome = nome_edittext.getText().toString();
        final String email = email_edittext.getText().toString();
        final String senha = senha_edittext.getText().toString();
        final String senharepita = senharepita_edittext.getText().toString();

        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senharepita.isEmpty()) {
            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {
            if(!senha.equals(senharepita)) {
                Toast.makeText(context, "Repita a senha corretamente", Toast.LENGTH_SHORT).show();
            } else {
                // Desativa as entradas
                nome_edittext.setEnabled(false);
                email_edittext.setEnabled(false);
                senha_edittext.setEnabled(false);
                senharepita_edittext.setEnabled(false);
                cadastro_btn.setEnabled(false);


                UsuarioAuth.cadastrar(nome, email, senha, new UsuarioAuthCallback() {
                    @Override
                    public void onSuccess() {
                        Intent in = new Intent(context, Inicio.class);
                        startActivity(in);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception ex) {
                        if(ex.getMessage().contains("Password should be at least 6 characters")) {
                            Toast.makeText(context, "A senha necessita de pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        Log.d("result", Objects.requireNonNull(ex.getMessage()));

                        // Ativa as entradas
                        nome_edittext.setEnabled(true);
                        email_edittext.setEnabled(true);
                        senha_edittext.setEnabled(true);
                        senharepita_edittext.setEnabled(true);
                        cadastro_btn.setEnabled(true);
                    }

                    @Override
                    public void onFailureMessage(String ex) {
                        Toast.makeText(context, ex, Toast.LENGTH_LONG).show();
                        Log.d("result", ex);

                        // Ativa as entradas
                        nome_edittext.setEnabled(true);
                        email_edittext.setEnabled(true);
                        senha_edittext.setEnabled(true);
                        senharepita_edittext.setEnabled(true);
                        cadastro_btn.setEnabled(true);
                    }
                });
            }
        }

    }

    private void init() {
        nome_edittext = findViewById(R.id.cadastroNome);
        email_edittext = findViewById(R.id.cadastroEmail);
        senha_edittext = findViewById(R.id.cadastroSenha);
        senharepita_edittext = findViewById(R.id.cadastroRepitaSenha);

        cadastro_btn = findViewById(R.id.cadastroButton);
        cadastro_btn.setOnClickListener(view -> cadastrar());
    }

    public void CadastrotoLogin(View v) {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        finish();
    }
}