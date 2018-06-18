package com.ubu.lmi.gii170j.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.Alimento;

import java.util.ArrayList;

public class IngestaAlimento_ListAdapter extends ArrayAdapter<Alimento>{
    //private LayoutInflater ly_inflater;
   //private ArrayList<Alimento> alimentos;
    //private int viewResource_id;
    private Context mContext;
    int mResource;

    static final int ELEVADO = 70;
    static final int MODERADO_MIN = 56;
    static final int MODERADO_MAX = 69;
    static final int BAJO = 55;

    public IngestaAlimento_ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Alimento> objects) {
        super(context, resource, objects);
        //this.alimentos = objects;
         //ly_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mResource =  resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nombre = getItem(position).getNombre();
        String gramos = getItem(position).getGramos();
        String iglu = getItem(position).getIg();
        int indiceGlucemico= Integer.parseInt(iglu);

        Alimento alimento = new Alimento(nombre,gramos,iglu);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);

        TextView tv_nombre = (TextView)convertView.findViewById(R.id.tv_id_item_list_ingesta);
        TextView tv_grHC = (TextView)convertView.findViewById(R.id.tv_id_gralimento);
        TextView tv_ig = (TextView)convertView.findViewById(R.id.tv_id_ig);

        tv_nombre.setText(nombre);
        tv_grHC.setText(gramos);
        tv_ig.setText(iglu);
        if(indiceGlucemico == ELEVADO || indiceGlucemico > ELEVADO){
            tv_ig.setBackgroundColor(Color.RED);

        }else if (indiceGlucemico < MODERADO_MAX  && indiceGlucemico > MODERADO_MIN){
            tv_ig.setBackgroundColor(Color.YELLOW);
        }else if(indiceGlucemico == BAJO || indiceGlucemico < BAJO){
            tv_ig.setBackgroundColor(Color.GREEN);
        }


        return convertView;

    }
}
