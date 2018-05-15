package com.ubu.lmi.gii170j.interfaz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;

import java.util.ArrayList;
import java.util.List;

public class IngestaDetalles_ListAdapter extends ArrayAdapter<DetallesListaIngesta> {
    static final String LACTEOS = "Lacteos";
    static final String CEREALES = "Cereales y derivados. Harinas, legumbres y tubérculos";
    static final String FRUTAS = "Frutas";
    static final String HORTALIZAS = "Hortalizas";
    static final String FRUTAGRASA = "Fruta grasa y seca";
    static final String BEBIDAS = "Bebidas";
    static final String OTROS = "Otros";


    private Context mContext;
    int mResource;


    public IngestaDetalles_ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DetallesListaIngesta> objects) {
        super(context, resource , objects);

        mContext = context;
        mResource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String tipo = getItem(position).getTipo_alimento();
        String nombre = getItem(position).getNomAlimento();
        String cantidad = getItem(position).getCantidad();
        String indice_glucemico = getItem(position).getIndice_glucemico();
        //String hidratos_carbono = getItem(position).getHidratos_carbono();

        DetallesListaIngesta detalle_list_ingesta = new DetallesListaIngesta(tipo,nombre,cantidad,indice_glucemico);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tv_tipo = (TextView)convertView.findViewById(R.id.tv_id_itemtipo_list_detalles);
        TextView tv_nombreAlimento = (TextView)convertView.findViewById(R.id.tv_id_itemvaluenombre_list_detalles);
        TextView tv_cantidad = (TextView)convertView.findViewById(R.id.tv_id_itemvaluecantidad_list_detalles);
        TextView tv_indiceGlu = (TextView)convertView.findViewById(R.id.tv_id_itemvalueindiceglu_list_detalles);
        //TextView tv_hidratosC = (TextView)convertView.findViewById(R.id.tv_id_itemvaluehidratos_list_detalles);

        tv_tipo.setText(tipo);
        if(tipo.equals(LACTEOS)){
            tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorLacteos));
        }else if (tipo.equals(CEREALES)){
            tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCereales));
        }else if (tipo.equals(FRUTAS)){
            tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorFrutas));
        }else if (tipo.equals(HORTALIZAS)){
            tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorHortalizas));
        }else if (tipo.equals(FRUTAGRASA)){
            tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorFrutaGrasa));
        }else if (tipo.equals(BEBIDAS)){
        tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBebidas));
        }else if (tipo.equals(OTROS)){
        tv_tipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorOtros));
        }
        tv_nombreAlimento.setText(nombre);
        tv_cantidad.setText(cantidad);
        tv_indiceGlu.setText(indice_glucemico);
        //tv_hidratosC.setText(hidratos_carbono);
        return convertView;
    }
}
