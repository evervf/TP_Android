package com.example.ever.myapplication.modelo;

/**
 * Created by Ever on 13/10/2016.
 * Entidad "Producto"
 */

public class Producto {

    public Integer id_producto;
    //public Integer id_tipo;
    public String nom_producto;
    public Integer precio_unitario;
    public Integer stock_actual;

    public Producto(Integer id_producto, String nom_producto, Integer precio_unitario, Integer stock_actual) {
        this.id_producto = id_producto;
        //this.id_tipo= id_tipo;
        this.nom_producto = nom_producto;
        this.precio_unitario = precio_unitario;
        this.stock_actual = stock_actual;
    }

}
