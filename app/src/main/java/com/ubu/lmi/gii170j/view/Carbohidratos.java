package com.ubu.lmi.gii170j.view;

//imports Android
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.BuildConfig;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.calculations.CalculaBolo;
import com.ubu.lmi.gii170j.model.Alimento;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.persistence.ValoresPOJO;
import com.ubu.lmi.gii170j.util.IngestaAlimentoListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Carbohidratos extends AppCompatActivity {

    /**
     * Gramos de hidratos de carbono (HC).
     * 10 Gramos de HC = 1 RACIÓN DE HC.
     * Tomado de la tabla de raciones de hidratos de carbono
     * Ver: http://www.fundaciondiabetes.org/upload/publicaciones_ficheros/71/TABLAHC.pdf.
     */
    private static final double GRAMOS_DE_HC = 10.0;
    /**
     * Tag for log.
     */
    private static String TAG = Carbohidratos.class.getName();

    /**
     * Posición en la tabla de alimentos donde se almacenan los gramos por ración del alimento.
     */
    public static final int COLUMNA_RACION = 2;

    /**
     * Posición en la tabla de alimentos donde se almacenan los indices Glucemicos del alimento.
     */

    public static final int COLUMNA_IG = 3;

    private static final int RESULT_EXIT = 0;
    //Consultar Preferencias de Usuario
    private  SharedPreferences misPreferencias;
    private boolean rapida;
    private double insulinaBasal;
    private double insulinaRapida;
    private double glucemiaMinima;
    private double glucemiaMaxima;
    private double glucemia;
    private Boolean bc_cero;
    private Boolean bcUno;
    private Boolean bcDos;
    private ValoresPOJO valoresPOJO;
    private CalculaBolo cb;

    private double sumatorioRaciones=0;
    private double actualSumatorioBoloC=0;
    private double boloResult=0;

    ArrayList<String> arrayAlimentos = new ArrayList<>();
    ArrayList<Integer> arrayRaciones = new ArrayList<>();

    //Creamos nuestro objeto listView para mostrar la lista de ingesta del Usuario.
    private ListView listViewIngesta;

    //Creamos nuestro objeto button para agregar alimentos a nuestra listView
    private Button buttonAddAlimento;
    //Creamos nuestro objeto button para eliminar alimentos de nuestra listView
    private Button buttonRemoveAlimento;
    //Creamos nuestro objeto EditText para introducir la cantidad del alimento (Gr)
    private EditText editTextGramos;
    //Creamos nuestro objeto AutoCompleteTextView para el buscador de alimentos
    private AutoCompleteTextView autoCompleteTextViewBuscador;
    //Creamos nuestro objeto spinner para nuestra lista total de alimentos
    private Spinner listaComida;
    private String[] totalAlimentosOrdenados;

    //Prueba mostrar sumatorioCH
    private TextView editTextActualSumatoriHC;
    private TextView editTextActualBoloC;

    //Nuevo-Para la listview con varias columnas

    private Alimento alimentoIngesta;
    private ArrayList<Alimento> userlistIngesta;
    private IngestaAlimentoListAdapter adpListaIngesta;

    //para eliminar items de la lisview
    private int itemSelec=0;

    //Nuevo-Para el doble click de la listview  ingesta de aliemntos
    private int previousPosition;
    private int count;
    private long previousMil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbohidratos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTextGramos = (EditText) findViewById(R.id.et_gramos);

        //Obtenemos la referencia a la listView del Xml
        listViewIngesta = (ListView) findViewById(R.id.lv_id_listaingesta_fc);

        //Obtenemos la referencia al button add del Xml
        buttonAddAlimento = (Button) findViewById(R.id.bt_id_addAlimento);
        //Obtenemos la referencia al button remove del Xml
        buttonRemoveAlimento = (Button)findViewById(R.id.bt_id_removeAlimento);
        buttonRemoveAlimento.setVisibility(View.INVISIBLE);
        //Obtenemos la referencia al buscador de alimentos
        autoCompleteTextViewBuscador = (AutoCompleteTextView) findViewById(R.id.actv_id_buscador);
        //Obtenemos la referencia al spinner de todos los alimentos
        listaComida = (Spinner) findViewById(R.id.spiner_alimentos);


        //Array Para la lista de alimentos
        totalAlimentosOrdenados  = getResources().getStringArray(R.array.arrayAlimentos);
        //ordenamos en orden alfabetico los alimentos
        Arrays.sort(totalAlimentosOrdenados);

        //Creamos un adaptador para el buscador y el spinner de alimentos
        ArrayAdapter adpTodos = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,totalAlimentosOrdenados);
        autoCompleteTextViewBuscador.setAdapter(adpTodos);
        listaComida.setAdapter(adpTodos);

        //mostrar sumatorioCH
        editTextActualSumatoriHC =(TextView)findViewById(R.id.tv_id_actualSumatorioCH);
        editTextActualSumatoriHC.setText("Actual Sum HC: 0.0");
       //mostrar BoloC actual
        editTextActualBoloC = (TextView)findViewById(R.id.tv_id_actualBoloC);
        editTextActualBoloC.setText("Actual Bolo C.: 0.0");

        //** Nuevo- Para la listView con varias columnas
        userlistIngesta = new ArrayList<>();
        adpListaIngesta = new IngestaAlimentoListAdapter(this,R.layout.list_ingesta_alimentos, userlistIngesta);
        listViewIngesta.setAdapter(adpListaIngesta);


        //Nuevo ValoresPojo
        misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        // Cargar valores de preferencias
        rapida = misPreferencias.getBoolean(getString(R.string.rapida), false);
        insulinaBasal = Double.parseDouble(misPreferencias.getString(getString(R.string.udsBasal), "0"));
        insulinaRapida = Double.parseDouble(misPreferencias.getString(getString(R.string.udsRapida), "0"));
        glucemiaMinima = Double.parseDouble(misPreferencias.getString(getString(R.string.min), "0"));
        glucemiaMaxima = Double.parseDouble(misPreferencias.getString(getString(R.string.max), "0"));
        glucemia = misPreferencias.getInt(getString(R.string.glucemia), 0);
        bc_cero = misPreferencias.getBoolean(getString(R.string.decimal_bc_cero),false);
        bcUno = misPreferencias.getBoolean(getString(R.string.decimal_bc_dos),false);
        bcDos = misPreferencias.getBoolean(getString(R.string.decimal_bc_tres),false);
        valoresPOJO = new ValoresPOJO(rapida, insulinaBasal, insulinaRapida, glucemiaMinima, glucemiaMaxima, glucemia);





        previousPosition=-1;
        count=0;
        previousMil=0;
        // Comportamiento de la listview cuando se selecciona un item: Deseleccionar
        listViewIngesta.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(previousPosition==position)
                {
                    count++;
                    if(count==2 && System.currentTimeMillis()-previousMil<=2000)
                    {
                        Toast.makeText(Carbohidratos.this,  "Mantenga pulsado para seleccionar", Toast.LENGTH_LONG).show();
                        count=1;
                    }
                }
                else
                {
                    previousPosition=position;
                    count=1;
                    previousMil=System.currentTimeMillis();
                }

                listViewIngesta.setSelector(new ColorDrawable(Color.TRANSPARENT));
                buttonRemoveAlimento.setVisibility(View.INVISIBLE);
                buttonAddAlimento.setVisibility(View.VISIBLE);


            }

        });
        //Comportamiento de la listview cuando se selecciona un item: Seleccionar
        listViewIngesta.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewIngesta.setSelector(new ColorDrawable(Color.CYAN));
                buttonRemoveAlimento.setVisibility(View.VISIBLE);
                buttonAddAlimento.setVisibility(View.INVISIBLE);
                itemSelec = position;
                return true;
            }
        });
    }

    /**
     * Función que define el comportamiento de la aplicacion al pulsar el boton añadir
     * Acumula el resultado obtenido y deja el editext por defecto para permitir añadir mas alimentos.
     *
     * @param view
     */
    public void añadirOtroOnClick(View view) {
        String[] informationQuery;
        String grHCperRation = "";
        String igAlimento ="";
        String gramos = editTextGramos.getText().toString();
        String nomAlimento = listaComida.getSelectedItem().toString();
        int numeroGramos = 0;
        //Si ha insertados los gramos del alimento
        if (!gramos.equals("")) {
            numeroGramos = Integer.parseInt(gramos);
            informationQuery = consulta_grHC_IG(nomAlimento);
            grHCperRation = informationQuery[0];
            igAlimento = informationQuery[1];
            sumatorioRaciones += calcularGramosDeHidratosDeCarbono(numeroGramos, grHCperRation);
            //Bolo corrector Temporal
            actualSumatorioBoloC = calculaBoloCorrector(sumatorioRaciones);


            alimentoIngesta = new Alimento(nomAlimento, gramos ,igAlimento);
            userlistIngesta.add(0, alimentoIngesta);
            editTextActualSumatoriHC.setText("Actual Sum HC: " + String.format("%.2f",  sumatorioRaciones));
            editTextActualBoloC.setText("Actual Bolo C.:" + String.format("%.2f", actualSumatorioBoloC));
            editTextGramos.setText("");
            autoCompleteTextViewBuscador.setText("");
            listaComida.setSelection(0);
            adpListaIngesta.notifyDataSetChanged();



        }else {
            Toast.makeText(Carbohidratos.this,  "Inserta una cantidad (Gr)", Toast.LENGTH_LONG).show();
        }



    }


    public void removeOnClick(View view) {
        String[] informationQuery;
        String grHCperRation = "";

        Alimento removableFood = userlistIngesta.get(itemSelec);
        String numGramos = removableFood.getGramos();
        String nomAlimento = removableFood.getNombre();


        informationQuery = consulta_grHC_IG(nomAlimento);
        grHCperRation = informationQuery[0];


        userlistIngesta.remove(itemSelec);
        listViewIngesta.clearChoices();
        listViewIngesta.requestLayout();
        adpListaIngesta.notifyDataSetChanged();
        listViewIngesta.setSelector(new ColorDrawable(Color.TRANSPARENT));


        Toast.makeText(Carbohidratos.this,  nomAlimento + " Eliminado", Toast.LENGTH_LONG).show();
        buttonRemoveAlimento.setVisibility(View.INVISIBLE);
        buttonAddAlimento.setVisibility(View.VISIBLE);

        sumatorioRaciones -= calcularGramosDeHidratosDeCarbono(Integer.parseInt(numGramos), grHCperRation);
        if(sumatorioRaciones == 0.0){

            actualSumatorioBoloC =0.0;
        }else{
            actualSumatorioBoloC = calculaBoloCorrector(sumatorioRaciones);
        }

        editTextActualSumatoriHC.setText("Actual Sum HC: " +String.format("%.2f",  sumatorioRaciones));
        editTextActualBoloC.setText("Actual Bolo C.:" + String.format("%.2f", actualSumatorioBoloC));

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Eliminacion alimento: " + nomAlimento);

        }

    }

    /**
     * consulta_grHC_IG. Metodo que consulta de la BD los gr de HC (por racion) y el I.G de un alimento.
     * @param alimento
     * @return
     */
    public String[] consulta_grHC_IG(String alimento){
        String[] retorno = new String[2];
        String grPerRation = "";
        String igAlimento="";
        //Accedemos a la Bd
        DataBaseManager dbmanager = new DataBaseManager(this);
        final Cursor cursorAlimentos = dbmanager.selectAlimento(alimento);
        try {
            if (cursorAlimentos.moveToFirst()) {
                grPerRation = cursorAlimentos.getString(COLUMNA_RACION);
                igAlimento = cursorAlimentos.getString(COLUMNA_IG);
                retorno[0] = grPerRation;
                retorno[1] = igAlimento;

            }
        }finally {
            cursorAlimentos.close();
            dbmanager.closeBD();

        }

        return retorno;
    }


    /**
     * Calcula los gramos de HC para el alimento según los gramos ingeridos
     *
     * @param numeroGramos gramos ingeridos
     * @param gramosPorRacion gramos por ración en la tabla de la federación española
     * @return formula aplicada para el cálculo o cero si no es valorable
     */
    public double calcularGramosDeHidratosDeCarbono(int numeroGramos, String gramosPorRacion) {
        if (Double.parseDouble(gramosPorRacion) == 0) {
            return 0.0; // evita el bug de división por cero con alimentos con valor "no es valorable"
        }
        return (numeroGramos * GRAMOS_DE_HC) / Double.parseDouble(gramosPorRacion);
    }

    /**
     * Función que define el comportamiento de la aplicación al pulsar el boton Finalizar
     * Genera una instancia de CalculaBolo, obtiene el resultado, y lo muestra por pantalla
     * lanzando un ventana emergente.
     *
     * @param view
     */
    public void finalizarOnClick(View view) {

        int numDecimal = -1;

        boloResult = calculaBoloCorrector(sumatorioRaciones);
        if (bc_cero){
            numDecimal = 0;
        }else if(bcUno){
            numDecimal = 1;
        }else if(bcDos){
            numDecimal = 2;
        }

        /**
         * LLamada a la asynktask
         */
        new RegistrosListaIngesta().execute("registrar_datos");

        String comentarioFinal = generaComentarioBolo(boloResult,numDecimal);
        AlertDialog.Builder builder = new AlertDialog.Builder(Carbohidratos.this);
        builder.setMessage(comentarioFinal)
                .setTitle(getString(R.string.bolo))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                setResult(RESULT_EXIT);
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public double calculaBoloCorrector(Double sumatorioHc){
        cb = new CalculaBolo(valoresPOJO, sumatorioHc);
        return cb.calculoBoloCorrector();
    }

    /**
     * Función usada para generar el comentario final que se mostrara por pantalla
     *
     * @param bolo resultado del calculo del bolo corrector
     */
    private String generaComentarioBolo(double bolo, int nDecimal) {

        String comentario = "";
        String formato ="";
        if(nDecimal == 0){
            int boloInt = (int) Math.rint(bolo);
            formato =  String.format(" %d", boloInt);
        }
        if(nDecimal == 1){
            formato = String.format(" %.1f", bolo);
        }
        if(nDecimal == 2) {
            formato = String.format(" %.2f", bolo);

        }
        comentario = getString(R.string.resultado_bolo) + formato ;

        if (bolo < 0) {
            comentario += "\n" + getString(R.string.ingerir_carbohidratos);
        }

        return comentario;
    }

    class RegistrosListaIngesta extends AsyncTask<String,Void,String> {
        private long idLista =0;
        private String fechaRegistro;
        private double sumatorioHC;
        private double boloC;


        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());
        ContentValues values;


        @Override
        protected void onPreExecute() {

            Toast.makeText(Carbohidratos.this, R.string.textregistrando, Toast.LENGTH_SHORT).show();

        }


        @Override
        protected String doInBackground(String... strings) {

            rellenarTablaListaIngesta();
            rellenarTablaDetallesListaIngesta();

            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(Carbohidratos.this, R.string.textregistrofinalizado, Toast.LENGTH_SHORT).show();

            dbmanager.closeBD();
        }

        private void rellenarTablaListaIngesta() {

            try{
                long insertarlista = dbmanager.insertar("listaingesta", generarContentValuesLista(sumatorioRaciones, boloResult));
                if (insertarlista != -1) {
                    idLista = insertarlista;
                }

            }catch (SQLException e) {
                Log.e(TAG, "Error inserting lista_ingesta " +  e);
            }



        }
        public ContentValues generarContentValuesLista(double argSumatorioHc, double argBoloC ){
            values = new ContentValues();
            fechaRegistro = getDateTime();
            values.put("fecha", fechaRegistro);
            sumatorioHC = (double)Math.round(argSumatorioHc * 100d) / 100d;
            values.put("sum_HC", sumatorioHC);
            boloC = argBoloC;
            values.put("bolo_corrector", boloC);
            return values;
        }

        /**
         * Función que genera la fecha actual con un formato determinado
         */
        private String getDateTime() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = new Date();
            return dateFormat.format(date);
        }

        private void rellenarTablaDetallesListaIngesta() {
            long insertSQLExcp=0;
            String nombreAl="";
            double cantidad = 0;
            try{
                for(int i = 0; i < userlistIngesta.size(); i++){
                    nombreAl = userlistIngesta.get(i).getNombre();
                    cantidad =  Double.parseDouble(userlistIngesta.get(i).getGramos());
                    long insertarDetalles = dbmanager.insertar("detalleslistaingesta", generarContentValuesDetalles(idLista,nombreAl,cantidad));
                    insertSQLExcp = insertarDetalles;
                    if (insertarDetalles != -1) {
                        Log.d(TAG, "Inserting Detalle_lista_ingesta OK");
                    }
                }
            }catch (SQLException e) {
                Log.e(TAG, "Error inserting Detalle_lista_ingesta " + insertSQLExcp , e);
            }


        }
        public ContentValues generarContentValuesDetalles(long argIdlista,String argAlimento, double argCantidad ){
            values = new ContentValues();
            values.put("id_lista", argIdlista);
            values.put("alimento", argAlimento);
            values.put("cantidad", argCantidad);
            return values;
        }


    }

}
