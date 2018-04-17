package com.ubu.lmi.gii170j.interfaz;

class Alimento {
    //Nombre del alimento
    private String nombre;
    //1 RACIÓN DE HC SON (EN GRAMOS)
    private String racionHC;
    //Indice Glucemico del alimento
    private String ig;

    public Alimento(String name, String r_HC, String i_g) {
        this.nombre = name;
        this.racionHC = r_HC;
        this.ig = i_g;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRacionHC() {
        return racionHC;
    }

    public String getIg() {
        return ig;
    }
}
