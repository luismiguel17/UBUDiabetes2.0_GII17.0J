package com.ubu.lmi.gii170j.interfaz;

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
import com.ubu.lmi.gii170j.calculos.CalculaBolo;
import com.ubu.lmi.gii170j.persistencia.DataBaseManager;
import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

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

    private final int RESULT_EXIT = 0;
    //Consultar Preferencias de Usuario
    private  SharedPreferences misPreferencias;
    private boolean rapida;
    private double insulinaBasal;
    private double insulinaRapida;
    private double glucemiaMinima;
    private double glucemiaMaxima;
    private double glucemia;
    private Boolean bc_cero;
    private Boolean bc_uno;
    private Boolean bc_dos;
    private ValoresPOJO valoresPOJO;
    private CalculaBolo cb;

    private String comida;
    private String gramosPorRacion ="";
    private String indiceGlucemico="";
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
    private ArrayList<String> ingestaAlimentosList;

    //Prueba mostrar sumatorioCH
    private TextView editTextActualSumatoriHC;
    private TextView editTextActualBoloC;

    //Nuevo-Para la listview con varias columnas

    private Alimento alimento_ingesta;
    private ArrayList<Alimento> userlist_ingesta;
    private IngestaAlimento_ListAdapter adp_ListaIngesta;

    //para eliminar items de la lisview
    private int itemSelec=0;
    //private int contItemsIngesta =0;

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
        //editTextGramos.setText("0");

        //Obtenemos la referencia a la listView del Xml
        listViewIngesta = (ListView) findViewById(R.id.lv_id_listaingesta_fc);
        //listViewIngesta.setScrollContainer(true);
        //listViewIngesta.setFastScrollAlwaysVisible(true);
        //Obtenemos la referencia al button add del Xml
        buttonAddAlimento = (Button) findViewById(R.id.bt_id_addAlimento);
        //Obtenemos la referencia al button remove del Xml
        buttonRemoveAlimento = (Button)findViewById(R.id.bt_id_removeAlimento);
        buttonRemoveAlimento.setVisibility(View.INVISIBLE);
        //Obtenemos la referencia al buscador de alimentos
        autoCompleteTextViewBuscador = (AutoCompleteTextView) findViewById(R.id.actv_id_buscador);
        //Obtenemos la referencia al spinner de todos los alimentos
        listaComida = (Spinner) findViewById(R.id.spiner_alimentos);

        //Creamos un Arraylist para la lista de ingesta de alimentos del usuario
        ingestaAlimentosList = new ArrayList<String>();

        //Array Para la lista de alimentos
        totalAlimentosOrdenados  = getResources().getStringArray(R.array.arrayAlimentos);
        //ordenamos en orden alfabetico los alimentos
        Arrays.sort(totalAlimentosOrdenados);

        //Creamos un adaptador para el buscador y el spinner de alimentos
        //ArrayAdapter adpTodos = ArrayAdapter.createFromResource(this, R.array.arrayAlimentos, android.R.layout.simple_spinner_item);
        ArrayAdapter adpTodos = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,totalAlimentosOrdenados);
        autoCompleteTextViewBuscador.setAdapter(adpTodos);
        listaComida.setAdapter(adpTodos);

        //mostrar sumatorioCH
        editTextActualSumatoriHC =(TextView)findViewById(R.id.tv_id_actualSumatorioCH);
       //mostrar BoloC actual
        editTextActualBoloC = (TextView)findViewById(R.id.tv_id_actualBoloC);


        //** Nuevo- Para la listView con varias columnas
        userlist_ingesta = new ArrayList<>();
        adp_ListaIngesta = new IngestaAlimento_ListAdapter(this,R.layout.list_ingesta_alimentos, userlist_ingesta);
        listViewIngesta.setAdapter(adp_ListaIngesta);
       // adp_ListaIngesta = new IngestaAlimento_ListAdapter(this,R.layout.list_ingesta_alimentos, userlist_ingesta);
        //listViewIngesta.setAdapter(adp_ListaIngesta)

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
        bc_uno = misPreferencias.getBoolean(getString(R.string.decimal_bc_dos),false);
        bc_dos = misPreferencias.getBoolean(getString(R.string.decimal_bc_tres),false);
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
                listViewIngesta.setSelection(-1);
                listViewIngesta.requestLayout();
                listViewIngesta.setSelector(new ColorDrawable(Color.TRANSPARENT));
                buttonRemoveAlimento.setVisibility(View.INVISIBLE);


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
        String gr_HCperRation = "";
        String ig_alimento ="";
        String gramos = editTextGramos.getText().toString();
        String nom_alimento = listaComida.getSelectedItem().toString();
        int numeroGramos = 0;
        //Si ha insertados los gramos del alimento
        if (!gramos.equals("")) {
            numeroGramos = Integer.parseInt(gramos);
            informationQuery = consulta_grHC_IG(nom_alimento);
            gr_HCperRation = informationQuery[0];
            ig_alimento = informationQuery[1];
            sumatorioRaciones += calcularGramosDeHidratosDeCarbono(numeroGramos, gr_HCperRation);
            //Bolo corrector Temporal
            //cb = new CalculaBolo(valoresPOJO, sumatorioRaciones);
            actualSumatorioBoloC = calculaBoloCorrector(sumatorioRaciones);

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "After Query: ");
                Log.d(TAG, "Grams HC per ration: " + gr_HCperRation );
                Log.d(TAG, "Current count of carbohidrates (HC): " + sumatorioRaciones);
            }


            alimento_ingesta = new Alimento(nom_alimento, gramos ,ig_alimento);
            userlist_ingesta.add(0,alimento_ingesta);
            editTextActualSumatoriHC.setText("Actual Sum HC: " + String.format("%.2f",  sumatorioRaciones));
            editTextActualBoloC.setText("Actual Bolo C.:" + String.format("%.2f", actualSumatorioBoloC));
            editTextGramos.setText("");
            autoCompleteTextViewBuscador.setText("");
            listaComida.setSelection(0);
            adp_ListaIngesta.notifyDataSetChanged();



        }else {
            Toast.makeText(Carbohidratos.this,  "Inserta una cantidad (Gr)", Toast.LENGTH_LONG).show();
        }



    }


    public void removeOnClick(View view) {
        String[] informationQuery;
        String gr_HCperRation = "";

        Alimento removable_food = userlist_ingesta.get(itemSelec);
        String num_gramos = removable_food.getGramos();
        String nom_alimento = removable_food.getNombre();


        informationQuery = consulta_grHC_IG(nom_alimento);
        gr_HCperRation = informationQuery[0];


        userlist_ingesta.remove(itemSelec);
        listViewIngesta.clearChoices();
        listViewIngesta.requestLayout();
        adp_ListaIngesta.notifyDataSetChanged();
        listViewIngesta.setSelector(new ColorDrawable(Color.TRANSPARENT));


        Toast.makeText(Carbohidratos.this,  nom_alimento + " Eliminado", Toast.LENGTH_LONG).show();
        buttonRemoveAlimento.setVisibility(View.INVISIBLE);
        buttonAddAlimento.setVisibility(View.VISIBLE);

        sumatorioRaciones -= calcularGramosDeHidratosDeCarbono(Integer.parseInt(num_gramos), gr_HCperRation);
        if(sumatorioRaciones == 0.0){

            actualSumatorioBoloC =0.0;
        }else{
            actualSumatorioBoloC = calculaBoloCorrector(sumatorioRaciones);
        }

        editTextActualSumatoriHC.setText("Actual Sum HC: " +String.format("%.2f",  sumatorioRaciones));
        editTextActualBoloC.setText("Actual Bolo C.:" + String.format("%.2f", actualSumatorioBoloC));

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Eliminacion alimento: " + nom_alimento);

        }

    }

    /**
     * consulta_grHC_IG. Metodo que consulta de la BD los gr de HC (por racion) y el I.G de un alimento.
     * @param alimento
     * @return
     */
    public String[] consulta_grHC_IG(String alimento){
        String[] retorno = new String[2];
        String gr_perRation = "";
        String ig_alimento="";
        //Accedemos a la Bd
        DataBaseManager dbmanager = new DataBaseManager(this);
        final Cursor cursorAlimentos = dbmanager.selectAlimento(alimento);
        try {
            if (cursorAlimentos.moveToFirst()) {
                gr_perRation = cursorAlimentos.getString(COLUMNA_RACION);
                ig_alimento = cursorAlimentos.getString(COLUMNA_IG);
                retorno[0] = gr_perRation;
                retorno[1] = ig_alimento;
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Result Query: " + alimento);
                    Log.d(TAG, "Grams HC per ration: " + gr_perRation );
                    Log.d(TAG, "Indice Glucemico: " + ig_alimento);
                }
            }
        }finally {
            if (cursorAlimentos != null && !cursorAlimentos.isClosed())
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

        int num_decimal = -1;

        boloResult = calculaBoloCorrector(sumatorioRaciones);
        if (bc_cero){
            num_decimal = 0;
        }else if(bc_uno){
            num_decimal = 1;
        }else if(bc_dos){
            num_decimal = 2;
        }

        /**
         * LLamada a la asynktask
         */
        new RegistrosListaIngesta().execute("registrar_datos");

        String comentarioFinal = generaComentarioBolo(boloResult,num_decimal);
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
        double retorno=0.0;
        cb = new CalculaBolo(valoresPOJO, sumatorioHc);

        retorno = cb.calculoBoloCorrector();

        return retorno;
    }

    /**
     * Función usada para generar el comentario final que se mostrara por pantalla
     *
     * @param bolo resultado del calculo del bolo corrector
     */
    private String generaComentarioBolo(double bolo, int n_decimal) {

        String comentario = "";
        String formato ="";
        if(n_decimal == 0){
            int bolo_int = (int) Math.rint(bolo);
            formato =  String.format(" %d", bolo_int);
        }
        if(n_decimal == 1){
            formato = String.format(" %.1f", bolo);
        }
        if(n_decimal == 2) {
            formato = String.format(" %.2f", bolo);

        }
        comentario = getString(R.string.resultado_bolo) + formato ;

        if (bolo < 0) {
            comentario += "\n" + getString(R.string.ingerir_carbohidratos);
        }

        return comentario;
    }

    class RegistrosListaIngesta extends AsyncTask<String,Void,String> {
        private long id_lista=0;
        private String fecha_registro;
        private double sumatorio_HC;
        private double bolo_c;
        //private id_lista;


        //SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        //SharedPreferences.Editor editorPreferencias = misPreferencias.edit(); // Nuevo cambio para ir a la Activity MenuPrincipal despues de registrarse


        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());
        ContentValues values;// = new ContentValues();


        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Toast.makeText(Carbohidratos.this, "Registrando datos...:", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected String doInBackground(String... strings) {

            //Toast.makeText(Registro.this, "Cargando datos...:" , Toast.LENGTH_LONG).show();
            rellenarTablaListaIngesta();
            rellenarTablaDetallesListaIngesta();
            //editorPreferencias.putBoolean("tablaAlimentos", true);
            //editorPreferencias.apply();
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            // super.onPostExecute(s);

            Toast.makeText(Carbohidratos.this, "Finalizado Registro de datos", Toast.LENGTH_LONG).show();

            dbmanager.closeBD();
        }

        private void rellenarTablaListaIngesta() {
            long insertSQLExcp=0;
            try{
                long insertarlista = dbmanager.insertar("listaIngesta", generarContentValuesLista(sumatorioRaciones, boloResult));
                insertSQLExcp = insertarlista;
                if (insertarlista != -1) {
                    //Toast.makeText(Carbohidratos.this, R.string.registro_lista_ingesta_correcta, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Insertada lista_ingesta con id: " + insertarlista);
                    id_lista = insertarlista;
                }

            }catch (SQLException e) {
                Log.e(TAG, "Error inserting lista_ingesta " + insertSQLExcp, e);
            }



        }
        public ContentValues generarContentValuesLista(double arg_sumatorioHc, double arg_boloC ){
            values = new ContentValues();
            fecha_registro = getDateTime();
            values.put("fecha", fecha_registro);
            sumatorio_HC = (double)Math.round(arg_sumatorioHc * 100d) / 100d;
            values.put("sum_HC", sumatorio_HC);
            bolo_c= arg_boloC;
            values.put("bolo_corrector", bolo_c);
            return values;
        }

        /**
         * Función que genera la fecha actual con un formato determinado
         */
        private String getDateTime() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy", Locale.getDefault());
            Date date = new Date();
            return dateFormat.format(date);
        }

        private void rellenarTablaDetallesListaIngesta() {
            long insertSQLExcp=0;
            String nombre_al="";
            double cantidad = 0;
            try{
                for(int i= 0; i < userlist_ingesta.size();i++){
                    nombre_al = userlist_ingesta.get(i).getNombre();
                    cantidad =  Double.parseDouble(userlist_ingesta.get(i).getGramos());
                    long insertarDetalles = dbmanager.insertar("detalleslistaingesta", generarContentValuesDetalles(id_lista,nombre_al,cantidad));
                    insertSQLExcp = insertarDetalles;
                    if (insertarDetalles != -1) {
                        //Toast.makeText(Carbohidratos.this, R.string.registro_lista_ingesta_correcta, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Insertada Detalle_lista_ingesta con id: " + insertarDetalles);
                        Log.d(TAG, "id_lista: " + id_lista);
                        Log.d(TAG, "Alimento: " + nombre_al);
                        Log.d(TAG, "cantidad: " + cantidad);

                    }
                }
            }catch (SQLException e) {
                Log.e(TAG, "Error inserting Detalle_lista_ingesta " + insertSQLExcp , e);
            }


        }
        public ContentValues generarContentValuesDetalles(long arg_idlista,String arg_alimento, double arg_cantidad ){
            values = new ContentValues();
            values.put("id_lista", arg_idlista);
            values.put("alimento", arg_alimento);
            values.put("cantidad", arg_cantidad);
            return values;
        }


    }

}
