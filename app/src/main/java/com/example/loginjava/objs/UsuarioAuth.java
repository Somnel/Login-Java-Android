package com.example.loginjava.objs;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;



public class UsuarioAuth {
    private static final String TAG = "UsuarioAuth";


    public static void cadastrar(String nome, String email, String senha, UsuarioAuthCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // Coleta usuário
                        FirebaseUser user = task.getResult().getUser();

                        // Constroi a requisição de alteração
                        UserProfileChangeRequest updateUser = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nome)
                                        .build();

                        user.updateProfile(updateUser).addOnCompleteListener(taskUpdate -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Cadastro feito com sucesso");
                                callback.onSuccess();
                            } else {
                                Log.d(TAG, "Falha ao cadastrar");
                                callback.onFailure(task.getException());
                            }
                        });


                    } else {
                        Log.d("LoginAuth", "Falha ao cadastrar");
                        callback.onFailure(task.getException());
                    }
                });
    }

    public static void logar(String email, String senha, UsuarioAuthCallback callback)  {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(task.getException() == null) {
                            Log.d(TAG, "logado com sucesso");
                            callback.onSuccess();
                        } else {
                            Log.d(TAG, "Falha ao logar usuário", task.getException());
                            callback.onFailure(task.getException());
                        }
                    } else {
                        Log.d(TAG, "Falha ao logar usuário", task.getException());
                        callback.onFailure(task.getException());
                    }
                })
                .addOnFailureListener(task -> {
                   callback.onFailureMessage(task.getMessage());
                });


    }

    public static void deslogar() {
        FirebaseAuth.getInstance().signOut();
    }


    public static void redefinirSenha(@NonNull String email, UsuarioAuthCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("pt_br");
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email de enviado com sucesso");
                        callback.onSuccess();
                    } else {
                        Log.d(TAG, "Falha ao redefinir senha");
                        callback.onFailure(task.getException());
                    }
                });

    }
}
