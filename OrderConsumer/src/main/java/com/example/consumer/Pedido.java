package com.example.consumer;

public class Pedido {
    public String id;
    public String cliente;
    public double total;

    public Pedido(String id, String cliente, double total) {
        this.id = id;
        this.cliente = cliente;
        this.total = total;
    }

    public Pedido() {
    }
}