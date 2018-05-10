package com.ubu.lmi.gii170j.interfaz;

public class DetallesListaIngesta {
    private String tipo_alimento;
    private String indice_glucemico;
    //private String hidratos_carbono;
    //private String id_lineaLista;
    //private String id_lista;
    private String nomAlimento;
    private String cantidad;

    public DetallesListaIngesta(String arg_tipo, String arg_nomAlimento, String arg_cantidad , String arg_ig) {
        this.tipo_alimento = arg_tipo;
        this.nomAlimento = arg_nomAlimento;
        this.cantidad = arg_cantidad;
        this.indice_glucemico = arg_ig;
        //this.hidratos_carbono = arg_hc;
    }

    public String getTipo_alimento() {
        return tipo_alimento;
    }

    public String getIndice_glucemico() {
        return indice_glucemico;
    }

    /**
    public String getHidratos_carbono() {
        return hidratos_carbono;
    }
    */
    public String getNomAlimento() {
        return nomAlimento;
    }

    public String getCantidad() {
        return cantidad;
    }
}
