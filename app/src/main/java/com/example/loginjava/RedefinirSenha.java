package com.example.loginjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginjava.objs.UsuarioAuth;
import com.example.loginjava.objs.UsuarioAuthCallback;

public class RedefinirSenha extends AppCompatActivity {

    private Button redefsenhaButton;
    private EditText redefsenhaEmail_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);
        init();
    }

    private void redefinirSenhar() {
        String email = redefsenhaEmail_edittext.getText().toString();
        final Context context = this;

        if(email.isEmpty()) {
            Toast.makeText(context, "Preencha o campo de E-mail", Toast.LENGTH_SHORT).show();
        } else {
            // Desativa os inputs
            redefsenhaEmail_edittext.setEnabled(false);
            redefsenhaButton.setEnabled(false);

            UsuarioAuth.redefinirSenha(email, new UsuarioAuthCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "E-mail enviado", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception ex) {
                    Toast.makeText(context, "Falha ao enviar E-mail", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailureMessage(String ex) {

                }
            });

            // Reativa os inputs
            redefsenhaEmail_edittext.setEnabled(true);
            redefsenhaButton.setEnabled(true);
        }
    }

    // Inicializa os componentes
    private void init() {
        String email = getIntent().getStringExtra("loginEmail");

        redefsenhaEmail_edittext = findViewById(R.id.redefsenhaEmail);
        if(email != null && !email.isEmpty()) redefsenhaEmail_edittext.setText(email);

        redefsenhaButton = findViewById(R.id.redefsenhaButton);
        redefsenhaButton.setOnClickListener(view -> redefinirSenhar());

        findViewById(R.id.redefsenhaToLogin)
                .setOnClickListener(view -> toLogin());
    }

    private void toLogin() {
        finish();
    }
}