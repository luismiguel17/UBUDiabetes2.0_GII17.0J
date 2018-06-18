package com.ubu.lmi.gii170j.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.RegistroIngestas;

import java.util.ArrayList;

public class IngestaRegistros_ListAdapter extends ArrayAdapter<RegistroIngestas> {

    private Context mContext;
    int mResource;

    public IngestaRegistros_ListAdapter(@NonNull Context context, int resource, ArrayList<RegistroIngestas> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String id_lista = getItem(position).getId_lista();
        String fecha = getItem(position).getFecha();
        String sum_HC = getItem(position).getSumatorio_HC();
        String bolo_c = getItem(position).getBolo_c();

        RegistroIngestas list_ingesta = new RegistroIngestas(id_lista,fecha,sum_HC,bolo_c);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tv_id_lista = (TextView)convertView.findViewById(R.id.tv_id_itemid_list_registros);
        TextView tv_fechaa = (TextView)convertView.findViewById(R.id.tv_id_itemfecha_list_registros);
        TextView tv_sumatorioHC = (TextView)convertView.findViewById(R.id.tv_id_itemsumHC_list_registros);
        TextView tv_boloC = (TextView)convertView.findViewById(R.id.tv_id_itemboloC_list_registros);

        tv_id_lista.setText(id_lista);
        tv_fechaa.setText(fecha);
        tv_sumatorioHC.setText(sum_HC);
        tv_boloC.setText(bolo_c);

        return convertView;
    }
}
