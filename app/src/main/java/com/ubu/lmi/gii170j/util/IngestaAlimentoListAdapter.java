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
import java.util.List;

public class IngestaAlimentoListAdapter extends ArrayAdapter<Alimento>{

    private Context mContext;
    int mResource;

    static final int ELEVADO = 70;
    static final int MODERADO_MIN = 56;
    static final int MODERADO_MAX = 69;
    static final int BAJO = 55;

    public IngestaAlimentoListAdapter(@NonNull Context context, int resource, @NonNull List<Alimento> objects) {
        super(context, resource, objects);
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
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvNombre = (TextView)convertView.findViewById(R.id.tv_id_item_list_ingesta);
        TextView tvGrHC = (TextView)convertView.findViewById(R.id.tv_id_gralimento);
        TextView tvIg = (TextView)convertView.findViewById(R.id.tv_id_ig);

        tvNombre.setText(alimento.getNombre());
        tvGrHC.setText(alimento.getGramos());
        tvIg.setText(alimento.getIg());
        if(indiceGlucemico == ELEVADO || indiceGlucemico > ELEVADO){
            tvIg.setBackgroundColor(Color.RED);

        }else if (indiceGlucemico < MODERADO_MAX  && indiceGlucemico > MODERADO_MIN){
            tvIg.setBackgroundColor(Color.YELLOW);
        }else if(indiceGlucemico == BAJO || indiceGlucemico < BAJO){
            tvIg.setBackgroundColor(Color.GREEN);
        }


        return convertView;

    }
}
