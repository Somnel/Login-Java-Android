package com.example.loginjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginjava.objs.ProdService;
import com.example.loginjava.objs.Produto;
import com.example.loginjava.objs.ProdutoCallback;
import com.example.loginjava.objs.UsuarioAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Handler;

public class Inicio extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private boolean eAtivado = true;
    private EditText inicio_codigo;
    public EditText inicio_preco;
    public TextView inicio_helper;
    private final ProdService _service = new ProdService();


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            UsuarioAuth.deslogar();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false; // Reseta o contador após o tempo limite
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        init();
    }

    private void init() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
            finish();
        } else {
            inicio_codigo = findViewById(R.id.inicio_codigo);
            inicio_preco = findViewById(R.id.inicio_preco);
            inicio_helper = findViewById(R.id.inicio_helper);
        }
    }


    private void alternarEstadoInputs() {
        eAtivado = !eAtivado;

        inicio_preco.setEnabled(eAtivado);
        inicio_codigo.setEnabled(eAtivado);
        inicio_helper.setVisibility((eAtivado) ? View.INVISIBLE : View.VISIBLE);
    }

    public void Inserir(View view) {
        if(eAtivado) {
            final String codigo = inicio_codigo.getText().toString();
            final String preco = inicio_preco.getText().toString();
            final Context context = this;

            if(codigo.isEmpty() || preco.isEmpty()) {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                alternarEstadoInputs();

                Produto prod = new Produto(codigo, preco);
                _service.visualizar(codigo)
                        .addOnSuccessListener(produto -> {
                            if(produto != null) {
                                Toast.makeText(context, "Produto já existe", Toast.LENGTH_SHORT).show();
                            } else {
                                _service.inserir(prod)
                                            .addOnSuccessListener(task -> {
                                                if(task) Toast.makeText(context, "Produto adicionado", Toast.LENGTH_LONG).show();
                                                else Toast.makeText(context, "Falha ao adicionar o produto", Toast.LENGTH_LONG).show();
                                            });
                            }
                        }).addOnFailureListener(e -> Toast.makeText(context, "Falha ao acessar banco", Toast.LENGTH_LONG).show());

                alternarEstadoInputs();
            }
        }
    }

    public void Visualizar(View view) {
        if(eAtivado) {
            final String codigo = inicio_codigo.getText().toString();
            final Context context = this;

            if(codigo.isEmpty()) {
                Toast.makeText(context, "Preencha o campo de Código", Toast.LENGTH_SHORT).show();
            } else {
                alternarEstadoInputs(); // Para false

                _service.visualizar(codigo)
                        .addOnSuccessListener(produto -> {
                            if(produto != null) {
                                inicio_preco.setText( produto.getPreco() );
                            } else {
                                Toast.makeText(context, "Produto não existe", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(context, "Falha ao procurar produto", Toast.LENGTH_LONG).show());

                alternarEstadoInputs(); // Para true
            }
        }
    }

    public void Atualizar(View view) {
        if(eAtivado) {
            final String codigo = inicio_codigo.getText().toString();
            final String preco = inicio_preco.getText().toString();
            final Context context = this;

            if(codigo.isEmpty() || preco.isEmpty()) {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                alternarEstadoInputs();

                _service.visualizar(codigo)
                        .addOnSuccessListener(produto -> {
                            if(produto != null) {
                                produto.setPreco(preco);
                                _service.update(produto)
                                        .addOnCompleteListener(task -> {
                                            if(task.getResult()) Toast.makeText(context, "Produto atualizado", Toast.LENGTH_LONG).show();
                                            else Toast.makeText(context, "Falha ao atualizar o produto", Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                Toast.makeText(context, "Produto não existe", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(context, "Falha ao acessar banco", Toast.LENGTH_LONG).show());

                alternarEstadoInputs();
            }
        }
    }
    public void Deletar(View view) {
        if(eAtivado) {
            final String codigo = inicio_codigo.getText().toString();
            final Context context = this;

            if(codigo.isEmpty()) {
                Toast.makeText(context, "Preencha o campo de Código", Toast.LENGTH_SHORT).show();
            } else {
                alternarEstadoInputs(); // Para false

                _service.visualizar(codigo)
                        .addOnSuccessListener(produto -> {
                            if(produto != null) {
                                _service.delete(produto)
                                        .addOnCompleteListener(task -> {
                                            if(task.getResult()) Toast.makeText(context, "Produto deletado", Toast.LENGTH_LONG).show();
                                            else Toast.makeText(context, "Falha ao deletar produto", Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                Toast.makeText(context, "Produto não existe", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(context, "Falha ao procurar produto", Toast.LENGTH_LONG).show());

                alternarEstadoInputs(); // Para true
            }
        }
    }
}