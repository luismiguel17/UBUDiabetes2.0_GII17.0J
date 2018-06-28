package com.ubu.lmi.gii170j.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.DetallesIngesta;

import java.util.List;

public class IngestaDetallesListAdapter extends ArrayAdapter<DetallesIngesta> {
    static final String LACTEOS = "Lacteos";
    static final String CEREALES = "Cereales y derivados. Harinas, legumbres y tubérculos";
    static final String FRUTAS = "Frutas";
    static final String HORTALIZAS = "Hortalizas";
    static final String FRUTAGRASA = "Fruta grasa y seca";
    static final String BEBIDAS = "Bebidas";
    static final String OTROS = "Otros";


    private Context mContext;
    int mResource;


    public IngestaDetallesListAdapter(@NonNull Context context, int resource, @NonNull List<DetallesIngesta> objects) {
        super(context, resource , objects);

        mContext = context;
        mResource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String tipo = getItem(position).getTipoAlimento();
        String nombre = getItem(position).getNomAlimento();
        String cantidad = getItem(position).getCantidad();
        String indiceGlucemico = getItem(position).getIndiceGlucemico();

        DetallesIngesta detalleListIngesta = new DetallesIngesta(tipo,nombre,cantidad,indiceGlucemico);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tvTipo = (TextView)convertView.findViewById(R.id.tv_id_itemtipo_list_detalles);
        TextView tvNombreAlimento = (TextView)convertView.findViewById(R.id.tv_id_itemvaluenombre_list_detalles);
        TextView tvCantidad = (TextView)convertView.findViewById(R.id.tv_id_itemvaluecantidad_list_detalles);
        TextView tvIndiceGlu = (TextView)convertView.findViewById(R.id.tv_id_itemvalueindiceglu_list_detalles);

        tvTipo.setText(detalleListIngesta.getTipoAlimento());
        if(tipo.equals(LACTEOS)){
            tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorLacteos));
        }else if (tipo.equals(CEREALES)){
            tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCereales));
        }else if (tipo.equals(FRUTAS)){
            tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorFrutas));
        }else if (tipo.equals(HORTALIZAS)){
            tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorHortalizas));
        }else if (tipo.equals(FRUTAGRASA)){
            tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorFrutaGrasa));
        }else if (tipo.equals(BEBIDAS)){
        tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBebidas));
        }else if (tipo.equals(OTROS)){
        tvTipo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorOtros));
        }
        tvNombreAlimento.setText(detalleListIngesta.getNomAlimento());
        tvCantidad.setText(detalleListIngesta.getCantidad());
        tvIndiceGlu.setText(detalleListIngesta.getIndiceGlucemico());

        return convertView;
    }
}
