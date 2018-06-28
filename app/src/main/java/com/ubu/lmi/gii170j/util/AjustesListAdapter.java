package com.ubu.lmi.gii170j.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;

public class AjustesListAdapter extends ArrayAdapter<String> {

    private final Activity mContext;
    private final String[] iconName;
    private final Integer[] integers;

    public AjustesListAdapter(Activity  context, String[] iconName2, Integer[] integers2) {
        super(context, R.layout.list_ajustes, iconName2);

        this.mContext=context;
        this.iconName = iconName2;
        this.integers=integers2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_ajustes,null,true);


        ImageView imageViewIcono = (ImageView)rowView.findViewById(R.id.iv_id_icon_ajustes);
        TextView textViewNombreIcono = (TextView)rowView.findViewById(R.id.tv_id_nombre_ajustes);

        imageViewIcono.setImageResource(integers[position]);
        textViewNombreIcono.setText(iconName[position]);
        return rowView;
    }
}


