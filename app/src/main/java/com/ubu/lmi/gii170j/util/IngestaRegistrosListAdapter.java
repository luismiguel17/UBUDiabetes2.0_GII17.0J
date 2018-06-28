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

import java.util.List;

public class IngestaRegistrosListAdapter extends ArrayAdapter<RegistroIngestas> {

    private Context mContext;
    int mResource;

    public IngestaRegistrosListAdapter(@NonNull Context context, int resource, List<RegistroIngestas> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String idLista = getItem(position).getIdLista();
        String fecha = getItem(position).getFecha();
        String sumHC = getItem(position).getSumatorioHC();
        String boloC = getItem(position).getBoloC();

        RegistroIngestas registroIngestas = new RegistroIngestas(idLista,fecha,sumHC,boloC);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tvIdLista = (TextView)convertView.findViewById(R.id.tv_id_itemid_list_registros);
        TextView tvFecha = (TextView)convertView.findViewById(R.id.tv_id_itemfecha_list_registros);
        TextView tvSumatorioHC = (TextView)convertView.findViewById(R.id.tv_id_itemsumHC_list_registros);
        TextView tvBoloC = (TextView)convertView.findViewById(R.id.tv_id_itemboloC_list_registros);

        tvIdLista.setText(registroIngestas.getIdLista());
        tvFecha.setText(registroIngestas.getFecha());
        tvSumatorioHC.setText(registroIngestas.getSumatorioHC());
        tvBoloC.setText(registroIngestas.getBoloC());

        return convertView;
    }
}
