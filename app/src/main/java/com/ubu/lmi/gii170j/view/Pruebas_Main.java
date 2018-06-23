package com.ubu.lmi.gii170j.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.Chart_ListAdapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class Pruebas_Main extends AppCompatActivity {

    private static String TAG = Pruebas_Main.class.getName();
    public static final int COLUMNA_FECHA = 1;
    public static final int COLUMNA_GLUCEMIA = 3;
    DataBaseManager dbmanager;

    private ArrayList<Entry> entries, entriesComida, entriesCena;
    private ArrayList<String> labels;
    private int valMax, valMin;
    private String anioCalendario;//anio actual
    private String[] fechaGraficas;

    Spinner spinner_meses;
    TextView textViewFechaPrincipal;
    ListView listView;
    Button button_buscar;

    TextView textViewFechaSecundaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pruebas);
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        valMax=Integer.parseInt(misPreferencias.getString("max",""));
        valMin=Integer.parseInt(misPreferencias.getString("min",""));

        //Spinner Meses
        spinner_meses = (Spinner) findViewById(R.id.sp_id_meses);

        //Fecha Principal-Encabezado pantalla
        Date date = new Date();
        anioCalendario = (String) DateFormat.format("yyyy", date); // 2018
        textViewFechaPrincipal = (TextView) findViewById(R.id.tv_id_fecha);
        textViewFechaPrincipal.setText(anioCalendario);
        //List con las gráficas
        listView = (ListView) findViewById(R.id.lv_id_pruebas);
        button_buscar =  (Button)findViewById(R.id.btn_id_buscar);


    }

    public void consultarGrafica(View view) {
        inicializeChart();
    }

    /**
     * inicializeChart. Método que inicializa las gráficas.
     */
    public void inicializeChart(){

        ArrayList<LineData> list = new ArrayList<>();

        String mes_spinner = spinner_meses.getSelectedItem().toString();
        //fecha de registro
        String fecha_registro = ObtenerFechaRegistro(mes_spinner);
        //Registros segun fecha: fecha-valor
        HashMap<String, String> hmap_totalRegistros= obtenerRegistrosFecha(fecha_registro);
        //Numero de semana segun fecha de registro.
        HashMap<String, Integer> hmap_SemanaRegistros= obtenerSemanaFecha(hmap_totalRegistros);
        //Numero de dia segun fecha
        HashMap<String, Integer> hmap_DiaRegistros= obtenerDiaFecha(hmap_totalRegistros);
        //Numero total de semanas
        List<Integer> list_semanas = obtenerNumerodeSemanas(hmap_SemanaRegistros.values());
        Log.d(TAG,"numero de semanas: " + list_semanas);
        //obtenemos las fechas para cada grafica
        fechaGraficas =  obtenerFechasGraficas(list_semanas);//semana 1, semana 2
        if(list_semanas.size()<1){
            Toast.makeText(Pruebas_Main.this, "Mes de " + mes_spinner + " sin registros", Toast.LENGTH_LONG).show();
        }else{
            //Numero de graficas a mostrar segun fecha
            for(int i= 0;i<list_semanas.size();i++){
                list.add(generateData(list_semanas.get(i),hmap_totalRegistros,hmap_SemanaRegistros,hmap_DiaRegistros));
            }
        }


        Chart_ListAdapter chart_listAdapter = new Chart_ListAdapter(getApplicationContext(),R.layout.list_mpandroidchart,list,fechaGraficas);
        listView.setAdapter(chart_listAdapter);
    }

    /**
     * obtenerNumerodeSemanas. Método que obtiene el numero de semanas en las que hay registros en el mes x.
     * @param values semana por cada registro.
     * @return num total semanas (unucas).
     */
    private List<Integer> obtenerNumerodeSemanas(Collection<Integer> values) {

        List<Integer> semanas_unicas = new ArrayList<>();
        //set para obtener los semanas únicas del mes x.
        Set<Integer> set_semanas = new HashSet<Integer>();
        Collection<Integer> collectionOfSets = values;
        for (Integer s : collectionOfSets) {
            set_semanas.add(s);
        }
        for(Iterator<Integer> it = set_semanas.iterator(); it.hasNext();){
            semanas_unicas.add(it.next());
        }
        return semanas_unicas;
    }

    /**
     * obtenerFechasGraficas. Método que obtiene el numero de semana (texto-encabezado) para cada una de las gráficas.
     * @param list_semanas numero de semanas total.
     * @return fechasG numero de semana para cada gráfica.
     */
    private String[] obtenerFechasGraficas(List<Integer> list_semanas) {
        String[] fechasG =  new String[list_semanas.size()];
        for (int i=0;i<list_semanas.size();i++){
            String date = "Semana " + list_semanas.get(i);
            fechasG[i] = date;
        }
        return  fechasG;
    }

    /**
     * ObtenerFechaRegistro. Método que obtiene la fecha según el item seleccionado del spinner.
     * @return fecha fecha.
     */
    public String ObtenerFechaRegistro(String mes_spinner){
        String mes = "";
        String fecha="";
        switch (mes_spinner){
            case "Enero":
                mes = "01";
                break;
            case "Febrero":
                mes = "02";
                break;
            case "Marzo":
                mes = "03";
                break;
            case "Abril":
                mes = "04";
                break;
            case "Mayo":
                mes = "05";
                break;
            case "Junio":
                mes = "06";
                break;
            case "Julio":
                mes = "07";
                break;
            case "Agosto":
                mes = "08";
                break;
            case "Septiembre":
                mes = "09";
                break;
            case "Octubre":
                mes = "10";
                break;
            case "Noviembre":
                mes = "11";
                break;
            case "Diciembre":
                mes = "12";
                break;
        }
        fecha =  mes + "/" + anioCalendario;
        return  fecha;
    }
    /**
     * obtenerRegistrosFecha. Método que obtiene los registros de glucemia segun la fecha pasada por parametro.
     * @param fecha de registros.
     * @return hmap_registros registros.
     */
    private HashMap<String, String> obtenerRegistrosFecha(String fecha){
        HashMap<String, String> hmap_registros = new HashMap<String, String>();

        dbmanager = new DataBaseManager(this);
        final Cursor cursorRegistrosGlucemias = dbmanager.selectGlucemia(fecha);
        try{
            while(cursorRegistrosGlucemias.moveToNext()){
                String fecha_cursor = cursorRegistrosGlucemias.getString(COLUMNA_FECHA);
                String glucemia_cursor = cursorRegistrosGlucemias.getString(COLUMNA_GLUCEMIA);
                hmap_registros.put(fecha_cursor,glucemia_cursor);
            }

        }finally{
            if (cursorRegistrosGlucemias != null && !cursorRegistrosGlucemias.isClosed())
                cursorRegistrosGlucemias.close();
            //dbmanager.closeBD();
        }
        //dbmanager.closeBD();
        return hmap_registros;
    }

    /**
     * obtenerSemanaFecha. Método que obtiene el numero de semana segun la fecha de registros pasados por parametro.
     * @param fechas de registros.
     * @return hmap_semanas registros.
     */
    private HashMap<String, Integer> obtenerSemanaFecha( HashMap<String, String> fechas){
        HashMap<String, Integer> hmap_semanas = new HashMap<String, Integer>();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt = null;

        try {
            for(Map.Entry<String, String> entry : fechas.entrySet()){
                String fecha_registro = entry.getKey();
                dt = dateFormat.parse(fecha_registro);
                c.setMinimalDaysInFirstWeek(1);
                c.setTime(dt);
                int num_semana = c.get(Calendar.WEEK_OF_MONTH);
                hmap_semanas.put(fecha_registro,num_semana);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hmap_semanas;
    }

    /**
     * obtenerDiaFecha. Método que obtiene el número de dia de la semana según una fecha pasada por argumento.
     * @param fechas fechas.
     * @return hmap_dias fechas y numero de dia.
     */
    private HashMap<String, Integer> obtenerDiaFecha( HashMap<String, String> fechas){
        HashMap<String, Integer> hmap_dias = new HashMap<String, Integer>();
        Calendar c = Calendar.getInstance();
        /**
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
         */

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            for(Map.Entry<String, String> entry : fechas.entrySet()){
                String fecha_registro = entry.getKey();
                Date dt = dateFormat.parse(fecha_registro);
                c.setMinimalDaysInFirstWeek(1);
                c.setTime(dt);
                int num_dia = c.get(Calendar.DAY_OF_WEEK);
                hmap_dias.put(fecha_registro,num_dia);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hmap_dias;
    }

    /**
     * generateData. Método que genera los datos (Entrys) para las gráficas.
     * @param cont numero de semana.
     * @param hmap_totalRegistros fecha-glucemia.
     * @param hmap_SemanaRegistros fecha-semana.
     * @param hmap_DiaRegistros fecha-dia.
     * @return data  datos.
     */
    public LineData generateData(int cont, HashMap<String, String> hmap_totalRegistros, HashMap<String, Integer> hmap_SemanaRegistros, HashMap<String, Integer> hmap_DiaRegistros){

        HashMap<int[], float[]>  result_avgs = obtenerMediasGlucemias(cont,hmap_totalRegistros,hmap_SemanaRegistros,hmap_DiaRegistros);
        HashMap<int[], float[]>  result_maxs = obtenerMaxGlucemias(cont,hmap_totalRegistros,hmap_SemanaRegistros,hmap_DiaRegistros);
        HashMap<int[], float[]>  result_mins = obtenerMinGlucemias(cont,hmap_totalRegistros,hmap_SemanaRegistros,hmap_DiaRegistros);

        int[] numDays = new int[0];
        float[] averageGlucemias = new float[0];
        float[] maxGlucemias = new float[0];
        float[] minGlucemias = new float[0];
        //Entrys para las medias
        for(Map.Entry<int[], float[]> entry : result_avgs.entrySet()){
            numDays = entry.getKey();
            averageGlucemias = entry.getValue();
        }
        //Entrys para las maximas
        for(Map.Entry<int[], float[]> entry : result_maxs.entrySet()){
            maxGlucemias = entry.getValue();
        }
        //Entrys para las minimas
        for(Map.Entry<int[], float[]> entry : result_mins.entrySet()){
            minGlucemias = entry.getValue();
        }
        //Días Eje x
        //int[] numDays2 = {1,2,3,4,5,6,7};//{Dom,Lun,Mar,Mie,Jue,Vie,Sab};
        //float[] averageGlucemias =
        //int[] averageGlucemias2 = {120,140,110,110,125,115,120};
        //int[] maxGlucemias = obtenerMaximasGlucemias();
        //int[] maxGlucemias2 = {125,145,120,130,125,130,125};
        List<Entry> averagesEntries = new ArrayList<Entry>();
        List<Entry> maximumEntries = new ArrayList<Entry>();
        List<Entry> minimumEntries = new ArrayList<Entry>();
        //Pruebas
        int[] numDaysPrueba = {1,2,3,4,5,6,7};
        float[] averageGlucemiasPrueba = {110,115,100,110,115,100,100};
        float[] maxGlucemiasPrueba ={130,125,135,120,140,115,120};
        float[] minGlucemiasPrueba = {100,110,120,115,120,110,105};
        for(int i=0;i<numDaysPrueba.length ;i++){
            //averagesEntries.add(new Entry(numDays[i], averageGlucemias[i]));
            averagesEntries.add(new Entry(numDaysPrueba[i], averageGlucemiasPrueba[i]));
            //Log.d(TAG,"media glucemia: " + averageGlucemias[i]);
            //maximumEntries.add(new Entry(numDays[i], maxGlucemias[i]));
            maximumEntries.add(new Entry(numDaysPrueba[i], maxGlucemiasPrueba[i]));
            //Log.d(TAG,"max glucemia: " + maxGlucemias[i]);
            //minimumEntries.add(new Entry(numDays[i], minGlucemias[i]));
            minimumEntries.add(new Entry(numDaysPrueba[i], minGlucemiasPrueba[i]));
            //Log.d(TAG,"min glucemia: " + minGlucemias[i]);

        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        LineDataSet dataSetGlucemiasAvg = new LineDataSet(averagesEntries, "G. Medias");
        LineDataSet dataSetGlucemiasMax = new LineDataSet(maximumEntries, "G. Máximass");
        LineDataSet dataSetGlucemiasMin = new LineDataSet(minimumEntries, "G. Mínimas");
        dataSets.add(dataSetGlucemiasAvg);
        dataSets.add(dataSetGlucemiasMax);
        dataSets.add(dataSetGlucemiasMin);

        LineData data = new LineData(dataSets);
        //LineDataSet dataSet = new LineDataSet(averagesEntries, "Glucemias");

        dataSetGlucemiasAvg.setLineWidth(2.5f);
        dataSetGlucemiasAvg.setCircleRadius(2f);
        dataSetGlucemiasAvg.setCircleHoleRadius(2.5f);
        dataSetGlucemiasAvg.setColor(Color.GREEN);
        dataSetGlucemiasAvg.setCircleColor(Color.BLUE);
        dataSetGlucemiasAvg.setHighLightColor(Color.YELLOW);
        dataSetGlucemiasAvg.setDrawValues(true);

        dataSetGlucemiasMax.setLineWidth(2.5f);
        dataSetGlucemiasMax.setCircleRadius(2f);
        dataSetGlucemiasMax.setCircleHoleRadius(2.5f);
        dataSetGlucemiasMax.setColor(Color.RED);
        dataSetGlucemiasMax.setCircleColor(Color.BLACK);
        dataSetGlucemiasMax.setHighLightColor(Color.YELLOW);
        dataSetGlucemiasMax.setDrawValues(true);

        dataSetGlucemiasMin.setLineWidth(2.5f);
        dataSetGlucemiasMin.setCircleRadius(2.f);
        dataSetGlucemiasMin.setCircleHoleRadius(2.5f);
        dataSetGlucemiasMin.setColor(Color.DKGRAY);
        dataSetGlucemiasMin.setCircleColor(Color.GREEN);
        dataSetGlucemiasMin.setHighLightColor(Color.YELLOW);
        dataSetGlucemiasMin.setDrawValues(true);

        return data;
    }

    /**
     * obtenerMinGlucemias. Método que obtiene los valores minimos de glucemias para cada dia de la semana x.
     * @param cont numero de semana.
     * @param hmap_totalRegistros fecha-glucemia.
     * @param hmap_semanaRegistros fecha-semana.
     * @param hmap_diaRegistros fecha-dia.
     * @return min_days_map minimas glucemias.
     */
    private HashMap<int[],float[]> obtenerMinGlucemias(int cont, HashMap<String, String> hmap_totalRegistros, HashMap<String, Integer> hmap_semanaRegistros, HashMap<String, Integer> hmap_diaRegistros) {

        HashMap<int[], float[]>  min_days_map = new HashMap<int[], float[]>();
        List<Float> minGluList = new ArrayList<>();
        List<String> fechas_semana = new ArrayList<>();
        List<Integer> dias_unicos= new ArrayList<>();
        HashMap<String, Integer>  map_dias = new HashMap<String,Integer>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmap_semanaRegistros.entrySet()){
            String fecha_registro = entry.getKey();
            int semana_registro = entry.getValue();

            if(semana_registro == cont) {
                fechas_semana.add(fecha_registro);

            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechas_semana.size();i++){
            int dia = hmap_diaRegistros.get(fechas_semana.get(i));
            map_dias.put(fechas_semana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> set_dias = new HashSet<Integer>();
        Collection<Integer> collectionOfSets = map_dias.values();
        for (Integer s : collectionOfSets) {
            set_dias.add(s);
        }
        for(Iterator<Integer> it = set_dias.iterator(); it.hasNext();){
            dias_unicos.add(it.next());
        }

        //obtenemos los valores minimos para cada dia de la semana x.
        float min = 250;
        for(int i=0;i<dias_unicos.size();i++){
            int dia_unico = dias_unicos.get(i);
            int sumatorio = 0;
            int contador = 0;
            for(Map.Entry<String, Integer> entry : map_dias.entrySet()){
                String fecha_map = entry.getKey();
                int dia_map = entry.getValue();
                if (dia_map == dia_unico){
                    int glucemia = Integer.parseInt(hmap_totalRegistros.get(fecha_map));
                    if(min > glucemia){
                        min = glucemia;
                    }
                }
            }

            minGluList.add(min);
        }

        //Obtenemos las mmaximas con su correspondiente dia x.
        float[] minGluArr = new float[minGluList.size()];
        int[] daysArr = new int[minGluList.size()];
        for(int i=0;i<minGluList.size();i++){
            minGluArr[i]= minGluList.get(i);
            daysArr[i]= dias_unicos.get(i);
        }
        min_days_map.put(daysArr,minGluArr);
        return min_days_map;
    }

    /**
     * obtenerMaxGlucemias. Método que obtiene los valores maximos de glucemias para cada dia de la semana x.
     * @param cont numero de semana.
     * @param hmap_totalRegistros fecha-glucemia.
     * @param hmap_semanaRegistros fecha-semana.
     * @param hmap_diaRegistros fecha-dia.
     * @return max_days_map glucemias maximas.
     */
    private HashMap<int[],float[]> obtenerMaxGlucemias(int cont, HashMap<String, String> hmap_totalRegistros, HashMap<String, Integer> hmap_semanaRegistros, HashMap<String, Integer> hmap_diaRegistros) {

        HashMap<int[], float[]>  max_days_map = new HashMap<int[], float[]>();
        List<Float> maxGluList = new ArrayList<>();
        List<String> fechas_semana = new ArrayList<>();
        List<Integer> dias_unicos= new ArrayList<>();
        HashMap<String, Integer>  map_dias = new HashMap<String,Integer>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmap_semanaRegistros.entrySet()){
            String fecha_registro = entry.getKey();
            int semana_registro = entry.getValue();

            if(semana_registro == cont) {
                fechas_semana.add(fecha_registro);

            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechas_semana.size();i++){
            int dia = hmap_diaRegistros.get(fechas_semana.get(i));
            map_dias.put(fechas_semana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> set_dias = new HashSet<Integer>();
        Collection<Integer> collectionOfSets = map_dias.values();
        for (Integer s : collectionOfSets) {
            set_dias.add(s);
        }
        for(Iterator<Integer> it = set_dias.iterator(); it.hasNext();){
            dias_unicos.add(it.next());
        }

        //obtenemos los valores maximos para cada dia de la semana x.
        float max = 0;
        for(int i=0;i<dias_unicos.size();i++){
            int dia_unico = dias_unicos.get(i);
            int sumatorio = 0;
            int contador = 0;
            for(Map.Entry<String, Integer> entry : map_dias.entrySet()){
                String fecha_map = entry.getKey();
                int dia_map = entry.getValue();
                if (dia_map == dia_unico){
                    int glucemia = Integer.parseInt(hmap_totalRegistros.get(fecha_map));
                    if(max < glucemia){
                        max = glucemia;
                    }
                }
            }

            maxGluList.add(max);
        }

        //Obtenemos las mmaximas con su correspondiente dia x.
        float[] maxGluArr = new float[maxGluList.size()];
        int[] daysArr = new int[maxGluList.size()];
        for(int i=0;i<maxGluList.size();i++){
            maxGluArr[i]= maxGluList.get(i);
            daysArr[i]= dias_unicos.get(i);
        }
        max_days_map.put(daysArr,maxGluArr);
        return max_days_map;
    }

    /**
     * obtenerMediasGlucemias. Método que obtiene las medias de las glucemias por semana.
     * @param cont numero de semana.
     * @param hmap_totalRegistros fecha-glucemia.
     * @param hmap_SemanaRegistros fecha-semana.
     * @param hmap_DiaRegistros fecha-dia.
     * @return avgGlu  media glucemias.
     */
    private HashMap<int[], float[]> obtenerMediasGlucemias(int cont, HashMap<String, String> hmap_totalRegistros, HashMap<String, Integer> hmap_SemanaRegistros, HashMap<String, Integer> hmap_DiaRegistros) {

        HashMap<int[], float[]>  avg_days_map = new HashMap<int[], float[]>();

        List<Float> averageGlu = new ArrayList<>();
        List<String> fechas_semana = new ArrayList<>();
        List<Integer> dias_unicos= new ArrayList<>();
        HashMap<String, Integer>  map_dias = new HashMap<String,Integer>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmap_SemanaRegistros.entrySet()){
            String fecha_registro = entry.getKey();
            int semana_registro = entry.getValue();
            Log.d(TAG,"fecha_registro: " + fecha_registro);
            Log.d(TAG,"semana_registro: " + semana_registro);
            if(semana_registro == cont) {
                fechas_semana.add(fecha_registro);
                Log.d(TAG,"fechas de la semana " + cont + " :" + fecha_registro);
            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechas_semana.size();i++){
            int dia = hmap_DiaRegistros.get(fechas_semana.get(i));
            map_dias.put(fechas_semana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> set_dias = new HashSet<Integer>();
        Collection<Integer> collectionOfSets = map_dias.values();
        for (Integer s : collectionOfSets) {
            set_dias.add(s);
        }
        for(Iterator<Integer> it = set_dias.iterator(); it.hasNext();){
            dias_unicos.add(it.next());
        }

        //calculamos las medias para cada dia de la semana x.
        float avg_glu = 0;
        for(int i=0;i<dias_unicos.size();i++){
            int dia_unico = dias_unicos.get(i);
            int sumatorio = 0;
            int contador = 0;
            for(Map.Entry<String, Integer> entry : map_dias.entrySet()){
                String fecha_map = entry.getKey();
                int dia_map = entry.getValue();
                if (dia_map == dia_unico){
                    int glucemia = Integer.parseInt(hmap_totalRegistros.get(fecha_map));
                    sumatorio += glucemia;
                    contador+=1;
                }
            }
            avg_glu = sumatorio/contador;
            averageGlu.add(avg_glu);
        }

        //Obtenemos las medias correspondiente a su dia x.
        float[] avgGluArr = new float[averageGlu.size()];
        int[] daysArr = new int[averageGlu.size()];
        for(int i=0;i<averageGlu.size();i++){
            avgGluArr[i]= averageGlu.get(i);
            daysArr[i]= dias_unicos.get(i);
        }
        avg_days_map.put(daysArr,avgGluArr);
        return avg_days_map;
    }
}



