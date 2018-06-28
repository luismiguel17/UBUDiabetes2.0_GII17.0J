package com.ubu.lmi.gii170j.view;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.ChartListAdapter;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Historial extends AppCompatActivity  {

    private static String TAG = Historial.class.getName();
    public static final int COLUMNA_FECHA = 1;
    public static final int COLUMNA_GLUCEMIA = 3;
    DataBaseManager dbmanager;

    private String anioCalendario;//anio actual
    private String[] fechaGraficas;

    Spinner spinnerMeses;
    TextView textViewFechaPrincipal;
    ListView listView;
    Button buttonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Spinner Meses
        spinnerMeses = (Spinner) findViewById(R.id.sp_id_meses);

        //Fecha Principal-Encabezado pantalla
        Date date = new Date();
        anioCalendario = (String) DateFormat.format("yyyy", date); // 2018
        textViewFechaPrincipal = (TextView) findViewById(R.id.tv_id_fecha);
        textViewFechaPrincipal.setText(anioCalendario);
        //List con las gráficas
        listView = (ListView) findViewById(R.id.lv_id_historial);
        buttonBuscar =  (Button)findViewById(R.id.btn_id_buscar);

    }

    public void consultarGrafica(View view) {
        inicializeChart();
    }

    /**
     * inicializeChart. Método que inicializa las gráficas.
     */
    public void inicializeChart(){

        ArrayList<LineData> list = new ArrayList<>();

        String mesSpinner = spinnerMeses.getSelectedItem().toString();
        //fecha de registro
        String fechaRegistro = ObtenerFechaRegistro(mesSpinner);
        //Registros segun fecha: fecha-valor
        HashMap<String, String> hmapTotalRegistros= obtenerRegistrosFecha(fechaRegistro);
        //Numero de semana segun fecha de registro.
        HashMap<String, Integer> hmapSemanaRegistros= obtenerSemanaFecha(hmapTotalRegistros);
        //Numero de dia segun fecha
        HashMap<String, Integer> hmapDiaRegistros= obtenerDiaFecha(hmapTotalRegistros);
        //Numero total de semanas
        List<Integer> listSemanas = obtenerNumerodeSemanas(hmapSemanaRegistros.values());
        Log.d(TAG,"numero de semanas: " + listSemanas);
        //obtenemos las fechas para cada grafica
        fechaGraficas =  obtenerFechasGraficas(listSemanas);//semana 1, semana 2
        if(listSemanas.isEmpty()){
            Toast.makeText(Historial.this, "Mes de " + mesSpinner + " sin registros", Toast.LENGTH_LONG).show();
        }else{
            //Numero de graficas a mostrar segun fecha
            for(int i= 0;i<listSemanas.size();i++){
                list.add(generateData(listSemanas.get(i),hmapTotalRegistros,hmapSemanaRegistros,hmapDiaRegistros));
            }
        }


        ChartListAdapter chartListAdapter = new ChartListAdapter(getApplicationContext(),R.layout.list_mpandroidchart,list,fechaGraficas);
        listView.setAdapter(chartListAdapter);
    }

    /**
     * obtenerNumerodeSemanas. Método que obtiene el numero de semanas en las que hay registros en el mes x.
     * @param values semana por cada registro.
     * @return num total semanas (unucas).
     */
    private List<Integer> obtenerNumerodeSemanas(Collection<Integer> values) {

        List<Integer> semanasUnicas = new ArrayList<>();
        //set para obtener los semanas únicas del mes x.
        Set<Integer> setSemanas = new HashSet<>();
        Collection<Integer> collectionOfSets = values;
        for (Integer s : collectionOfSets) {
            setSemanas.add(s);
        }
        for(Iterator<Integer> it = setSemanas.iterator(); it.hasNext();){
            semanasUnicas.add(it.next());
        }
        return semanasUnicas;
    }

    /**
     * obtenerFechasGraficas. Método que obtiene el numero de semana (texto-encabezado) para cada una de las gráficas.
     * @param listSemanas numero de semanas total.
     * @return fechasG numero de semana para cada gráfica.
     */
    private String[] obtenerFechasGraficas(List<Integer> listSemanas) {
        String[] fechasG =  new String[listSemanas.size()];
        for (int i=0;i<listSemanas.size();i++){
            String date = "Semana " + listSemanas.get(i);
            fechasG[i] = date;
        }
        return  fechasG;
    }

    /**
     * ObtenerFechaRegistro. Método que obtiene la fecha según el item seleccionado del spinner.
     * @return fecha fecha.
     */
    public String ObtenerFechaRegistro(String mesSpinner){
        String mes = "";
        String fecha="";
        switch (mesSpinner){
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
            default:
                mes = "01";
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
        HashMap<String, String> hmapRegistros = new HashMap<String, String>();

        dbmanager = new DataBaseManager(this);
        final Cursor cursorRegistrosGlucemias = dbmanager.selectGlucemia(fecha);
        try{
            while(cursorRegistrosGlucemias.moveToNext()){
                String fechaCursor = cursorRegistrosGlucemias.getString(COLUMNA_FECHA);
                String glucemiaCursor = cursorRegistrosGlucemias.getString(COLUMNA_GLUCEMIA);
                hmapRegistros.put(fechaCursor,glucemiaCursor);
            }

        }finally{
            cursorRegistrosGlucemias.close();

        }

        return hmapRegistros;
    }

    /**
     * obtenerSemanaFecha. Método que obtiene el numero de semana segun la fecha de registros pasados por parametro.
     * @param fechas de registros.
     * @return hmap_semanas registros.
     */
    private HashMap<String, Integer> obtenerSemanaFecha( HashMap<String, String> fechas){
        HashMap<String, Integer> hmapSemanas = new HashMap<String, Integer>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt = null;

        try {
            for(Map.Entry<String, String> entry : fechas.entrySet()){
                String fechaRegistro = entry.getKey();
                dt = dateFormat.parse(fechaRegistro);
                c.setMinimalDaysInFirstWeek(1);
                c.setTime(dt);
                int numSemana = c.get(Calendar.WEEK_OF_MONTH);
                hmapSemanas.put(fechaRegistro,numSemana);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hmapSemanas;
    }

    /**
     * obtenerDiaFecha. Método que obtiene el número de dia de la semana según una fecha pasada por argumento.
     * @param fechas fechas.
     * @return hmap_dias fechas y numero de dia.
     */
    private HashMap<String, Integer> obtenerDiaFecha( HashMap<String, String> fechas){
        HashMap<String, Integer> hmapDias = new HashMap<>();
        Calendar c = Calendar.getInstance();
        /**
         SimpleDateFormat dateFormat = new SimpleDateFormat(
         "dd/MM/yyyy HH:mm", Locale.getDefault());
         */

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            for(Map.Entry<String, String> entry : fechas.entrySet()){
                String fechaRegistro = entry.getKey();
                Date dt = dateFormat.parse(fechaRegistro);
                c.setMinimalDaysInFirstWeek(1);
                c.setTime(dt);
                int numDia = c.get(Calendar.DAY_OF_WEEK);
                hmapDias.put(fechaRegistro,numDia);

            }
        } catch (ParseException e) {
            Log.e(TAG, "Error " +  e);
        }

        return hmapDias;
    }

    /**
     * generateData. Método que genera los datos (Entrys) para las gráficas.
     * @param cont numero de semana.
     * @param hmapTotalRegistros fecha-glucemia.
     * @param hmapSemanaRegistros fecha-semana.
     * @param hmapDiaRegistros fecha-dia.
     * @return data  datos.
     */
    public LineData generateData(int cont, HashMap<String, String> hmapTotalRegistros, HashMap<String, Integer> hmapSemanaRegistros, HashMap<String, Integer> hmapDiaRegistros){

        HashMap<int[], float[]>  resultAvgs = obtenerMediasGlucemias(cont,hmapTotalRegistros,hmapSemanaRegistros,hmapDiaRegistros);
        HashMap<int[], float[]>  resultMaxs = obtenerMaxGlucemias(cont,hmapTotalRegistros,hmapSemanaRegistros,hmapDiaRegistros);
        HashMap<int[], float[]>  resultMins = obtenerMinGlucemias(cont,hmapTotalRegistros,hmapSemanaRegistros,hmapDiaRegistros);

        int[] numDays = new int[0];
        float[] averageGlucemias = new float[0];
        float[] maxGlucemias = new float[0];
        float[] minGlucemias = new float[0];
        //Entrys para las medias
        for(Map.Entry<int[], float[]> entry : resultAvgs.entrySet()){
            numDays = entry.getKey();
            averageGlucemias = entry.getValue();
        }
        //Entrys para las maximas
        for(Map.Entry<int[], float[]> entry : resultMaxs.entrySet()){
            maxGlucemias = entry.getValue();
        }
        //Entrys para las minimas
        for(Map.Entry<int[], float[]> entry : resultMins.entrySet()){
            minGlucemias = entry.getValue();
        }

        List<Entry> averagesEntries = new ArrayList<>();
        List<Entry> maximumEntries = new ArrayList<>();
        List<Entry> minimumEntries = new ArrayList<>();

        for(int i=0;i<numDays.length ;i++){
            averagesEntries.add(new Entry(numDays[i], averageGlucemias[i]));
            maximumEntries.add(new Entry(numDays[i], maxGlucemias[i]));
            minimumEntries.add(new Entry(numDays[i], minGlucemias[i]));
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet dataSetGlucemiasAvg = new LineDataSet(averagesEntries, "G. Medias");
        LineDataSet dataSetGlucemiasMax = new LineDataSet(maximumEntries, "G. Máximass");
        LineDataSet dataSetGlucemiasMin = new LineDataSet(minimumEntries, "G. Mínimas");
        dataSets.add(dataSetGlucemiasAvg);
        dataSets.add(dataSetGlucemiasMax);
        dataSets.add(dataSetGlucemiasMin);

        LineData data = new LineData(dataSets);


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
     * @param hmapTotalRegistros fecha-glucemia.
     * @param hmapSemanaRegistros fecha-semana.
     * @param hmapDiaRegistros fecha-dia.
     * @return min_days_map minimas glucemias.
     */
    private HashMap<int[],float[]> obtenerMinGlucemias(int cont, HashMap<String, String> hmapTotalRegistros, HashMap<String, Integer> hmapSemanaRegistros, HashMap<String, Integer> hmapDiaRegistros) {

        HashMap<int[], float[]>  minDaysMap = new HashMap<>();
        List<Float> minGluList = new ArrayList<>();
        List<String> fechasSemana = new ArrayList<>();
        List<Integer> diasUnicos= new ArrayList<>();
        HashMap<String, Integer>  mapDias = new HashMap<>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmapSemanaRegistros.entrySet()){
            String fechaRegistro = entry.getKey();
            int semanaRegistro = entry.getValue();

            if(semanaRegistro == cont) {
                fechasSemana.add(fechaRegistro);

            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechasSemana.size();i++){
            int dia = hmapDiaRegistros.get(fechasSemana.get(i));
            mapDias.put(fechasSemana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> setDias = new HashSet<>();
        Collection<Integer> collectionOfSets = mapDias.values();
        for (Integer s : collectionOfSets) {
            setDias.add(s);
        }
        for(Iterator<Integer> it = setDias.iterator(); it.hasNext();){
            diasUnicos.add(it.next());
        }

        //obtenemos los valores minimos para cada dia de la semana x.
        float min = 250;
        for(int i=0;i<diasUnicos.size();i++){
            int diaUnico = diasUnicos.get(i);
            for(Map.Entry<String, Integer> entry : mapDias.entrySet()){
                String fechaMap = entry.getKey();
                int diaMap = entry.getValue();
                if (diaMap == diaUnico){
                    int glucemia = Integer.parseInt(hmapTotalRegistros.get(fechaMap));
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
            daysArr[i]= diasUnicos.get(i);
        }
        minDaysMap.put(daysArr,minGluArr);
        return minDaysMap;
    }

    /**
     * obtenerMaxGlucemias. Método que obtiene los valores maximos de glucemias para cada dia de la semana x.
     * @param cont numero de semana.
     * @param hmapTotalRegistros fecha-glucemia.
     * @param hmapSemanaRegistros fecha-semana.
     * @param hmapDiaRegistros fecha-dia.
     * @return max_days_map glucemias maximas.
     */
    private HashMap<int[],float[]> obtenerMaxGlucemias(int cont, HashMap<String, String> hmapTotalRegistros, HashMap<String, Integer> hmapSemanaRegistros, HashMap<String, Integer> hmapDiaRegistros) {

        HashMap<int[], float[]>  maxDaysMap = new HashMap<>();
        List<Float> maxGluList = new ArrayList<>();
        List<String> fechasSemana = new ArrayList<>();
        List<Integer> diasUnicos= new ArrayList<>();
        HashMap<String, Integer>  mapDias = new HashMap<>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmapSemanaRegistros.entrySet()){
            String fechaRegistro = entry.getKey();
            int semanaRegistro = entry.getValue();

            if(semanaRegistro == cont) {
                fechasSemana.add(fechaRegistro);

            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechasSemana.size();i++){
            int dia = hmapDiaRegistros.get(fechasSemana.get(i));
            mapDias.put(fechasSemana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> setDias = new HashSet<>();
        Collection<Integer> collectionOfSets = mapDias.values();
        for (Integer s : collectionOfSets) {
            setDias.add(s);
        }
        for(Iterator<Integer> it = setDias.iterator(); it.hasNext();){
            diasUnicos.add(it.next());
        }

        //obtenemos los valores maximos para cada dia de la semana x.
        float max = 0;
        for(int i=0;i<diasUnicos.size();i++){
            int diaUnico = diasUnicos.get(i);
            for(Map.Entry<String, Integer> entry : mapDias.entrySet()){
                String fechaMap = entry.getKey();
                int diaMap = entry.getValue();
                if (diaMap == diaUnico){
                    int glucemia = Integer.parseInt(hmapTotalRegistros.get(fechaMap));
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
            daysArr[i]= diasUnicos.get(i);
        }
        maxDaysMap.put(daysArr,maxGluArr);
        return maxDaysMap;
    }

    /**
     * obtenerMediasGlucemias. Método que obtiene las medias de las glucemias por semana.
     * @param cont numero de semana.
     * @param hmapTotalRegistros fecha-glucemia.
     * @param hmapSemanaRegistros fecha-semana.
     * @param hmapDiaRegistros fecha-dia.
     * @return avgGlu  media glucemias.
     */
    private HashMap<int[], float[]> obtenerMediasGlucemias(int cont, HashMap<String, String> hmapTotalRegistros, HashMap<String, Integer> hmapSemanaRegistros, HashMap<String, Integer> hmapDiaRegistros) {

        HashMap<int[], float[]>  avgDaysMap = new HashMap<>();

        List<Float> averageGlu = new ArrayList<>();
        List<String> fechasSemana = new ArrayList<>();
        List<Integer> diasUnicos= new ArrayList<>();
        HashMap<String, Integer>  mapDias = new HashMap<>();
        //obtenemos las fechas de la semana = cont
        for(Map.Entry<String, Integer> entry : hmapSemanaRegistros.entrySet()){
            String fechaRegistro = entry.getKey();
            int semanaRegistro = entry.getValue();
            if(semanaRegistro == cont) {
                fechasSemana.add(fechaRegistro);
            }
        }

        //obtenemos los dias para la semana x
        for(int i=0;i<fechasSemana.size();i++){
            int dia = hmapDiaRegistros.get(fechasSemana.get(i));
            mapDias.put(fechasSemana.get(i),dia);
        }
        //set para obtener los dias únicos de la semana x.
        Set<Integer> setDias = new HashSet<>();
        Collection<Integer> collectionOfSets = mapDias.values();
        for (Integer s : collectionOfSets) {
            setDias.add(s);
        }
        for(Iterator<Integer> it = setDias.iterator(); it.hasNext();){
            diasUnicos.add(it.next());
        }

        //calculamos las medias para cada dia de la semana x.
        float avgGlu = 0;
        for(int i=0;i<diasUnicos.size();i++){
            int diaUnico = diasUnicos.get(i);
            float sumatorio = 0;
            int contador = 0;
            for(Map.Entry<String, Integer> entry : mapDias.entrySet()){
                String fechaMap = entry.getKey();
                int diaMap = entry.getValue();
                if (diaMap == diaUnico){
                    int glucemia = Integer.parseInt(hmapTotalRegistros.get(fechaMap));
                    sumatorio += glucemia;
                    contador+=1;
                }
            }
            //If the denominator to a division or modulo operation is zero it would result in a fatal error.
            if(contador == 0)
                contador = 1;
            avgGlu = sumatorio/contador;
            averageGlu.add(avgGlu);
        }

        //Obtenemos las medias correspondiente a su dia x.
        float[] avgGluArr = new float[averageGlu.size()];
        int[] daysArr = new int[averageGlu.size()];
        for(int i=0;i<averageGlu.size();i++){
            avgGluArr[i]= averageGlu.get(i);
            daysArr[i]= diasUnicos.get(i);
        }
        avgDaysMap.put(daysArr,avgGluArr);
        return avgDaysMap;
    }

}
