package com.ubu.lmi.gii170j.interfaz;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.DataBaseManager;

//imports Android
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
//imports github
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

//imports Java
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Historial extends AppCompatActivity implements OnChartValueSelectedListener {

    private ArrayList<Entry> entriesDesayuno, entriesComida, entriesCena;
    private ArrayList<String> labels;
    private int valMax, valMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        valMax=Integer.parseInt(misPreferencias.getString("max",""));
        valMin=Integer.parseInt(misPreferencias.getString("min",""));

        inicializeChart();

    }

    public void inicializeChart(){
        //Generamos los entries con los valores de la última semana para cada periodo
        entriesDesayuno = new ArrayList<>(generarCursores(generaFechas(),getString(R.string.leyenda_desayuno)));
        entriesComida = new ArrayList<>(generarCursores(generaFechas(),getString(R.string.leyenda_comida)));
        entriesCena = new ArrayList<>(generarCursores(generaFechas(), getString(R.string.leyenda_cena)));

        //Array que contiene las etiquetas para el eje X
        labels = new ArrayList<>(generaFechas());

        //Inicializamos los set de datos para cada periodo, incluyendo su leyenda
        LineData datasetline = new LineData(labels);
        LineDataSet setDesayuno = new LineDataSet(entriesDesayuno,getString(R.string.leyenda_desayuno));
        LineDataSet setComida = new LineDataSet(entriesComida,getString(R.string.leyenda_comida));
        LineDataSet setCena = new LineDataSet(entriesCena,getString(R.string.leyenda_cena));
        setComida.setCircleColor(Color.GREEN);
        setComida.setColor(Color.GREEN);
        setCena.setCircleColor(Color.RED);
        setCena.setColor(Color.RED);
        datasetline.addDataSet(setDesayuno);
        datasetline.addDataSet(setComida);
        datasetline.addDataSet(setCena);



        RelativeLayout rl = new RelativeLayout(getApplicationContext());
        LineChart mChart = new LineChart(getApplicationContext());
        rl.addView(
                mChart,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(rl);

        mChart.setDescription("");

        //Habilitamos las opciones para interactuar con el gráfico
        mChart.setTouchEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        //Habilitamos las opciones para arrastrar y escalar el gráfico
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        //Habilitamos el pinch zoom para que ambos ejes escalen por igual
        mChart.setPinchZoom(true);

        //Establecemos color de fondo
        mChart.setBackgroundColor(Color.WHITE);

        //Añadimos el dataset al gráfico
        mChart.setData(datasetline);

        //Personalizamos la leyenda
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        //Personalizamos el eje x
        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        //Personalizamos el eje y
        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setAxisMaxValue(250f);
        y1.setDrawGridLines(true);

        //Establecemos las lineas de limite
        y1.setDrawLimitLinesBehindData(true);
        LimitLine min = new LimitLine(valMin,"min");
        LimitLine max = new LimitLine(valMax,"max");
        y1.addLimitLine(min);
        y1.addLimitLine(max);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);


        mChart.setDescription(getString(R.string.leyenda_chart));
    }


    /**
     * Función que genera el array de entries para las fechas y periodo del dia deseados
     * @param fechas
     * @param periodo
     */
    public ArrayList<Entry> generarCursores(ArrayList<String> fechas, String periodo){
        ArrayList<Entry> entries = new ArrayList<>();
        DataBaseManager dbmanager = new DataBaseManager(this);

        for(int i=0;i<7;i++){
            Cursor cursorDesayuno = dbmanager.selectGlucemia(fechas.get(i),periodo);
            try {
                if (cursorDesayuno.moveToLast()) {
                    entries.add(new BarEntry(Integer.parseInt(cursorDesayuno.getString(3)), i));
                }

            }finally {
                if (cursorDesayuno != null && !cursorDesayuno.isClosed())
                    cursorDesayuno.close();
                //dbmanager.closeBD();
            }
        }
        dbmanager.closeBD();
        return entries;
    }

    /**
     * Función que genera un array con las fechas de los ultimos 7 dias
     */
    public ArrayList<String> generaFechas(){
        ArrayList<String> fechas = new ArrayList<>();
        for(int i=0;i<7;i++){
            if(i==6){
                fechas.add(i,getFechaActual());
            }else{
                Date d = stringToDate(getFechaActual());
                fechas.add(i,restarFechasDias(d,6-i).toString());
            }
        }
        return fechas;
    }


    /**
     * Función usada para obtener la fecha actual
     */
    public String getFechaActual(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Función usada para obtener una fecha a partir de otra y un numero de dias que restarle
     */
    public String restarFechasDias(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        Date ahora = new Date(cal.getTimeInMillis());
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }

    /**
     * Función usada para pasar de una fecha en formato string a una fecha tipo Date
     */
    public Date stringToDate(String fecha) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaEnviar;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);
            return fechaEnviar;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Redefinicion de onValueSelected para hacer que se muestre la incidencia asociada a un
     * determinado valor de la gráfica, o informe de que no la hay en caso contrario.
     */
    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        int posicion = entry.getXIndex();
        int val = (int)entry.getVal();
        String fecha = labels.get(posicion);
        DataBaseManager dbmanager = new DataBaseManager(this);
        if(val<valMin||val>valMax){
            Cursor cursor = dbmanager.selectGlucemiaValor(fecha,val);
            try {
                if(cursor.moveToLast()){
                    String id = cursor.getString(0);
                    Cursor cursorIncidencia = dbmanager.selectIncidencia(Integer.parseInt(id));
                    try{
                        if(cursorIncidencia.moveToFirst()) {
                            String incidencia = cursorIncidencia.getString(1);
                            String observaciones = cursorIncidencia.getString(2);
                            if (!observaciones.equals("")) {
                                Toast.makeText(getApplicationContext(),getString(R.string.hist_incidencia)+" " + incidencia + ". "
                                        + getString(R.string.hist_observacion)+ " " + observaciones, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.hist_incidencia)+" " + incidencia + ". " +getString(R.string.hist_sinobservaciones)
                                        , Toast.LENGTH_LONG).show();
                            }

                        }
                    }finally {
                        if (cursorIncidencia != null && !cursorIncidencia.isClosed())
                            cursorIncidencia.close();
                        //dbmanager.closeBD();
                    }
                }
            }finally {
                if (cursor != null && !cursor.isClosed())
                    cursor.close();
                dbmanager.closeBD();
            }

        }else{
            Toast.makeText(getApplicationContext(),R.string.sinIncidencia,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected() {

    }

}
