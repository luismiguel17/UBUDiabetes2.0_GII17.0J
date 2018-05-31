package com.ubu.lmi.gii170j.interfaz;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;

public class Ajustes_ListAdapter extends ArrayAdapter<String> {

    private final Activity mContext;
    //int mResource;
    private final String[] icon_name;
    private final Integer[] integers;

    public Ajustes_ListAdapter(Activity  context, String[] icon_name2, Integer[] integers2) {
        super(context, R.layout.list_ajustes, icon_name2);

        this.mContext=context;
        this.icon_name = icon_name2;
        this.integers=integers2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_ajustes,null,true);


        ImageView imageView_icono = (ImageView)rowView.findViewById(R.id.iv_id_icon_ajustes);
        TextView textView_nombreIcono = (TextView)rowView.findViewById(R.id.tv_id_nombre_ajustes);

        imageView_icono.setImageResource(integers[position]);
        textView_nombreIcono.setText(icon_name[position]);
        return rowView;
    }
}


