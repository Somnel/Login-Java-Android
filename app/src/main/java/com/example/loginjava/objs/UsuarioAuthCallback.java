package com.example.loginjava.objs;

public interface UsuarioAuthCallback {
    void onSuccess();
    void onFailure(Exception ex);

    void onFailureMessage(String ex);
}
