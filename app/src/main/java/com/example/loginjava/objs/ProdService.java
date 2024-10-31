package com.example.loginjava.objs;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ProdService {
    private final FirebaseFirestore _db;
    private final String TAG = "Product Service";
    private final String PATH = "produtos";

    public ProdService() {
        _db = FirebaseFirestore.getInstance();
    }



    public Task<Boolean> inserir(Produto prod) {
        TaskCompletionSource<Boolean> result = new TaskCompletionSource<>();
        _db.collection(PATH)
                .document(prod.getCodigo())
                .set(prod.toMapPreco())
                .addOnSuccessListener(documentReference -> {
                    result.setResult(true);
                    Log.d(TAG, "Adicionado");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Erro ao adicionar produto", e));

        return result.getTask();
    }



    public Task<Produto> visualizar(final String codigo) {
        TaskCompletionSource<Produto> taskCompletionSource = new TaskCompletionSource<>();

        _db.collection(PATH).document(codigo)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            taskCompletionSource.setResult(new Produto(codigo, document.getString("preco")));
                        } else {
                            Log.d(TAG, "Documento não existe");
                            taskCompletionSource.setResult(null);
                        }


                    } else {
                        Log.d(TAG, "Erro ao visualizar ", task.getException());
                    }
                });

        return taskCompletionSource.getTask();
    }



    public Task<Boolean> update(Produto prod) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        final String prod_preco = prod.getPreco();

        _db.collection(PATH)
                .document(prod.getCodigo())
                .update("preco", prod_preco)
                .addOnSuccessListener(command -> {
                    Log.d(TAG, "Atualizado com sucesso");
                    taskCompletionSource.setResult(true);
                })
                .addOnFailureListener(command -> Log.w(TAG, "Falha ao atualizar ", command.getCause()));

        return  taskCompletionSource.getTask();
    }



    public Task<Boolean> delete(Produto prod) {
        TaskCompletionSource<Boolean> result = new TaskCompletionSource<>();

        _db.collection(PATH)
                .document(prod.getCodigo())
                .delete()
                .addOnSuccessListener(command -> {
                    Log.d(TAG, "Produto removido com sucesso");
                    result.setResult(true);
                })
                .addOnFailureListener(command -> Log.w(TAG, "Falha ao deletar produto", command.getCause()));

        return result.getTask();
    }
}
