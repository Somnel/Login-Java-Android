package com.example.loginjava.objs;

import java.util.HashMap;
import java.util.Map;

public class Produto {
    private String codigo;
    private String preco;

    public Produto(final String preco) {
        this.preco = preco;
    }

    public Produto(final String codigo, final String preco) {
        this.preco = preco;
        this.codigo = codigo;
    }


    public Map<String, String> toMapPreco() {
        Map<String, String> product = new HashMap<>();
        product.put("preco", preco);

        return product;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) { this.preco = preco; }
}
