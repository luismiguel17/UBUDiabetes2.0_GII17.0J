package com.ubu.lmi.gii170j.model;

public class Alimento {
    //Nombre del alimento
    private String nombre;
    //1 RACIÓN DE HC SON (EN GRAMOS)
    private String gramos;
    //Indice Glucemico del alimento
    private String ig;

    public Alimento(String name, String grAlimento, String iG) {
        this.nombre = name;
        this.gramos = grAlimento;
        this.ig = iG;
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
