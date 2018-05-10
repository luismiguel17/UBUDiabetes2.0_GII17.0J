package com.ubu.lmi.gii170j.interfaz;

public class ListaIngesta {

    private String id_lista;
    private String fecha;
    private String sumatorio_HC;
    private String bolo_c;

    public ListaIngesta(String arg_idlista, String arg_fecha, String arg_sumatorioHC, String arg_bolo_c) {
        this.id_lista = arg_idlista;
        this.fecha = arg_fecha;
        this.sumatorio_HC = arg_sumatorioHC;
        this.bolo_c = arg_bolo_c;
    }

    public String getId_lista() {
        return id_lista;
    }

    public String getFecha() {
        return fecha;
    }

    public String getSumatorio_HC() {
        return sumatorio_HC;
    }

    public String getBolo_c() {
        return bolo_c;
    }
}


