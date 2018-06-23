package com.ubu.lmi.gii170j.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.DetallesIngesta;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Chart_ListAdapter extends ArrayAdapter<LineData>{


    private Context mContext;
    int mResource;
    private final String[] semanas;
    private int valMax, valMin;

    public Chart_ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LineData> objects, String[] semana2){

        super(context, resource , objects);
        mContext = context;
        mResource =  resource;
        this.semanas = semana2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //BarData data = getItem(position);
        SharedPreferences misPreferencias = mContext.getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        valMax=Integer.parseInt(misPreferencias.getString("max",""));
        valMin=Integer.parseInt(misPreferencias.getString("min",""));

        LineData data = getItem(position);

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView= inflater.inflate(mResource,parent,false);

            holder.chart = (LineChart) convertView.findViewById(R.id.lc_id_chart);
            convertView.setTag(holder);
            TextView fecha_tv = (TextView)convertView.findViewById(R.id.tv_id_fecha_chart);
            fecha_tv.setText(semanas[position]);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Style grafica
        data.setValueTextColor(Color.BLUE);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(true);
        //Establecemos color de fondo
        holder.chart.setBackgroundColor(Color.WHITE);
        // tiempo de actualizacion de la grafica.
        holder.chart.animateY(500);

        //Personalizamos la leyenda
        Legend l = holder.chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.DKGRAY);

        //Personalizamos el eje x
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setAvoidFirstLastClipping(true);


        final HashMap<Integer, String> numMap = new HashMap<>();
        numMap.put(1, "Dom");
        numMap.put(2, "Lun");
        numMap.put(3, "Mar");
        numMap.put(4, "Mie");
        numMap.put(5, "Jue");
        numMap.put(6, "Vie");
        numMap.put(7, "Sab");
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return numMap.get((int)value);
            }

        });

        //Personalizamos el eje y
        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setLabelCount(4, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMaximum(250f);
        leftAxis.setAxisMinimum(10f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setLabelCount(4, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMaximum(250f);
        rightAxis.setAxisMinimum(10f);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawZeroLine(true);



        //Establecemos las lineas de limite
        leftAxis.setDrawLimitLinesBehindData(true);
        LimitLine min = new LimitLine(valMin,"Min");
        LimitLine max = new LimitLine(valMax,"Max");
        max.setTextSize(10);
        max.setLineColor(Color.MAGENTA);
        min.setTextSize(10);
        min.setLineColor(Color.MAGENTA);
        leftAxis.addLimitLine(min);
        leftAxis.addLimitLine(max);


        // set data
        holder.chart.setData(data);

        return convertView;
    }

    private class ViewHolder {
        LineChart chart;
    }
}
