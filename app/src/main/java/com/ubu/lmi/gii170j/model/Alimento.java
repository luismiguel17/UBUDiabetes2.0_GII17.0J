package com.ubu.lmi.gii170j.model;

public class Alimento {
    //Nombre del alimento
    private String nombre;
    //1 RACIÓN DE HC SON (EN GRAMOS)
    private String gramos;
    //Indice Glucemico del alimento
    private String ig;

    public Alimento(String name, String gr_alimento, String i_g) {
        this.nombre = name;
        this.gramos = gr_alimento;
        this.ig = i_g;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGramos() {
        return gramos;
    }

    public String getIg() {
        return ig;
    }
}
