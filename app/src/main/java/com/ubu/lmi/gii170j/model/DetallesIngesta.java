package com.ubu.lmi.gii170j.model;

public class DetallesIngesta {
    private String tipoAlimento;
    private String indiceGlucemico;
    private String nomAlimento;
    private String cantidad;

    public DetallesIngesta(String argTipo, String argNomAlimento, String argCantidad , String argIg) {
        this.tipoAlimento = argTipo;
        this.nomAlimento = argNomAlimento;
        this.cantidad = argCantidad;
        this.indiceGlucemico = argIg;
    }

    public String getTipoAlimento() {
        return tipoAlimento;
    }

    public String getIndiceGlucemico() {
        return indiceGlucemico;
    }

    public String getNomAlimento() {
        return nomAlimento;
    }

    public String getCantidad() {
        return cantidad;
    }
}
