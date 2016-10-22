package com.example.ever.myapplication.modelo;

/**
 * Created by Ever on 13/10/2016.
 * Entidad "Vendedor"
 */

public class Vendedor {

    public Integer ci_vendedor;
    public String nom_vendedor;
    public String ape_vendedor;

    public Vendedor(Integer ci_vendedor, String nom_vendedor, String ape_vendedor) {
        this.ci_vendedor = ci_vendedor;
        this.nom_vendedor = nom_vendedor;
        this.ape_vendedor = ape_vendedor;
    }

}
