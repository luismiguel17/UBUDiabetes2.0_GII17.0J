package com.ubu.lmi.gii170j.interfaz;

//imports Android
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import java.util.ArrayList;

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

    //private Spinner listaComida, listaTipo; --ANTIGUO
    private final int RESULT_EXIT = 0;
    /*
    private static final String INTENSIDAD_ALTA = "Larga (más de 2 horas)";
    private static final String INTENSIDAD_MEDIA = "Media (de 60 a 90 minutos)";
    private static final String INTENSIDAD_IRREGULAR = "Irregular e intermitente";
    */
    private String comida;
    private String gramosPorRacion ="";
    private String indiceGlucemico="";
    private double sumatorioRaciones;
    /**
    private String[] tipoAlimento;
    private String[] numeroTipoAlimento;
    private String[] alimento;
    private String[] indicesGlucemico;
    private String[] raciones;
    **/
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
    private ArrayList<String> totalAlimentos;
    private ArrayList<String> ingestaAlimentosList;
    //private ArrayAdapter<String> adpListaIngesta;

    //Prueba mostrar sumatorioCH
    private TextView editTextSumatorioCH;

    //Nuevo-Para la listview con varias columnas

    private Alimento alimento_ingesta;
    private ArrayList<Alimento> userlist_ingesta;
    private IngestaUsuario_ListAdapter adp_ListaIngesta;

    //para eliminar items de la lisview
    private int itemSelec=0;
    //private int contItemsIngesta =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbohidratos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTextGramos = (EditText) findViewById(R.id.et_gramos);
        editTextGramos.setText("0");

        /**
         * Nuevos cambios
         */
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

        //Creamos un Arraylist para la lista de ingesta de alimentos del usuario
        ingestaAlimentosList = new ArrayList<String>();

        // Creamos una adaptador para la lista Ingesta de alimentos del usuario
        //adpListaIngesta = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice ,ingestaAlimentosList);
        //listViewIngesta.setAdapter(adpListaIngesta);

        //Creamos un adaptador para el buscador y el spinner de alimentos
        ArrayAdapter adpTodos = ArrayAdapter.createFromResource(this, R.array.arrayAlimentos, android.R.layout.simple_spinner_item);

        autoCompleteTextViewBuscador.setAdapter(adpTodos);
        listaComida.setAdapter(adpTodos);

        //Prueba mostrar sumatorioCH
       editTextSumatorioCH =(TextView)findViewById(R.id.tv_id_sumatorioCH);
       //editTextSumatorioCH.setText("Bolo calculado: ");


        //** Nuevo- Para la listView con varias columnas
        userlist_ingesta = new ArrayList<>();
        adp_ListaIngesta = new IngestaUsuario_ListAdapter(this,R.layout.list_ingesta_alimentos, userlist_ingesta);
        listViewIngesta.setAdapter(adp_ListaIngesta);
       // adp_ListaIngesta = new IngestaUsuario_ListAdapter(this,R.layout.list_ingesta_alimentos, userlist_ingesta);
        //listViewIngesta.setAdapter(adp_ListaIngesta);

        /**
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();

         **/

        /**
        // Si la tabla de alimentos no estaba creada... se crea
        if (!misPreferencias.getBoolean("tablaAlimentos", false)) {
            rellenarTablaAlimentos();
            editor.putBoolean("tablaAlimentos", true);
            editor.apply();
        }

         **/

        // Comportamiento de la listview cuando se selecciona una item
        listViewIngesta.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             buttonRemoveAlimento.setVisibility(View.VISIBLE);
             itemSelec = position;

            }
        });

    }


    /**
     * Función rellena la tabla de alimentos a partir del archivo arrays.xml
     */
    /**
    public void rellenarTablaAlimentos() {
        //Categoria de loas Alimentos.
        tipoAlimento = getResources().getStringArray(R.array.arrayTipoAlimento);
        //Numero total de alimentos en cada categoria
        numeroTipoAlimento = getResources().getStringArray(R.array.numeroTipoAlimento);
        //Todos los alimentos (Todas las categorias)
        alimento = getResources().getStringArray(R.array.arrayAlimentos);
        //Indices Glucemicos de todos los alimentos
        indicesGlucemico = getResources().getStringArray(R.array.arrayIndicesGlucemicos);
        //RACIÓN DE HC(EN GRAMOS) de todos loas alimentos
        raciones = getResources().getStringArray(R.array.arrayRacionesHC);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Total alimentos: " + alimento.length);
            Log.d(TAG, "Total I.G.: " + indicesGlucemico.length);
            Log.d(TAG, "Total Racios HC: " + raciones.length);

        }

        DataBaseManager dbmanager = new DataBaseManager(this);
        ContentValues values = new ContentValues();

        int contadorNumeroTipoAlimento = 0;
        int acumulado = Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);

        for (int i = 0; i < alimento.length; ++i) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Procesando tipo de alimento: " + tipoAlimento[contadorNumeroTipoAlimento] +
                        " Alimento: " + alimento[i] + " 1 Ración en gramos: " + raciones[i] + "indice glucemico: " + indicesGlucemico[i]);
            }
            values.put("tipoAlimento", tipoAlimento[contadorNumeroTipoAlimento]);
            values.put("alimento", alimento[i]);
            values.put("racion", raciones[i]);
            values.put("indiceGlucemico", indicesGlucemico[i]);
            dbmanager.insertar("alimentos", values);
            values.clear();
            // si hemos procesado todos los alimentos de un cierto y no hay más tipos de alimentos a procesar
            if (i == acumulado - 1 && contadorNumeroTipoAlimento < tipoAlimento.length - 1) {
                contadorNumeroTipoAlimento++; // avanzamos al siguiente tipo de alimento
                acumulado += Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);
            }
        }
    }

     **/
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
        if (!gramos.equals("")) {
            numeroGramos = Integer.parseInt(gramos);
        }

        informationQuery = consulta_grHC_IG(nom_alimento);
        gr_HCperRation = informationQuery[0];
        ig_alimento = informationQuery[1];
        sumatorioRaciones += calcularGramosDeHidratosDeCarbono(numeroGramos, gr_HCperRation);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "After Query: ");
            Log.d(TAG, "Grams HC per ration: " + gr_HCperRation );
            Log.d(TAG, "Current count of carbohidrates (HC): " + sumatorioRaciones);
        }


        alimento_ingesta = new Alimento(nom_alimento, gramos ,ig_alimento);
        userlist_ingesta.add(0,alimento_ingesta);
        editTextSumatorioCH.setText("Conteo actual de carbohidratos (HC): " + sumatorioRaciones);
        editTextGramos.setText("0");
        autoCompleteTextViewBuscador.setText("");
        listaComida.setSelection(0);
        adp_ListaIngesta.notifyDataSetChanged();


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
        adp_ListaIngesta.notifyDataSetChanged();

        Toast.makeText(Carbohidratos.this, "Eliminado:"+ nom_alimento , Toast.LENGTH_LONG).show();
        buttonRemoveAlimento.setVisibility(View.INVISIBLE);

        sumatorioRaciones -= calcularGramosDeHidratosDeCarbono(Integer.parseInt(num_gramos), gr_HCperRation);
        listViewIngesta.clearAnimation();
        editTextSumatorioCH.setText("Conteo actual de carbohidratos (HC): " + sumatorioRaciones);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Eliminacion alimento: " + nom_alimento);
            //Log.d(TAG, "Current count of carbohidrates (HC): " + sumatorioRaciones);
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

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        // Cargar valores de preferencias
        boolean rapida = misPreferencias.getBoolean(getString(R.string.rapida), false);
        double insulinaBasal = Double.parseDouble(misPreferencias.getString(getString(R.string.udsBasal), "0"));
        double insulinaRapida = Double.parseDouble(misPreferencias.getString(getString(R.string.udsRapida), "0"));
        double glucemiaMinima = Double.parseDouble(misPreferencias.getString(getString(R.string.min), "0"));
        double glucemiaMaxima = Double.parseDouble(misPreferencias.getString(getString(R.string.max), "0"));
        double glucemia = misPreferencias.getInt(getString(R.string.glucemia), 0);

        Boolean bc_cero = misPreferencias.getBoolean(getString(R.string.decimal_bc_cero),false);
        Boolean bc_uno = misPreferencias.getBoolean(getString(R.string.decimal_bc_dos),false);
        Boolean bc_dos = misPreferencias.getBoolean(getString(R.string.decimal_bc_tres),false);

        ValoresPOJO valoresPOJO = new ValoresPOJO(rapida, insulinaBasal, insulinaRapida, glucemiaMinima, glucemiaMaxima, glucemia);


        CalculaBolo cb = new CalculaBolo(valoresPOJO, sumatorioRaciones);

        //Nuevo: Cambio calculo del bolo segun numero de decimales

        int num_decimal = -1;

        double boloResult = cb.calculoBoloCorrector();

        if (bc_cero){
            num_decimal = 0;
        }else if(bc_uno){
            num_decimal = 1;
        }else if(bc_dos){
            num_decimal = 2;
        }

        //String comentarioFinal = generaComentarioBolo(tipoEjer, boloResult);
        String comentarioFinal = generaComentarioBolo(boloResult,num_decimal);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    /**
     * Función usada para generar el comentario final que se mostrara por pantalla
     *
     * @param bolo resultado del calculo del bolo corrector
     */
    private String generaComentarioBolo(double bolo, int n_decimal) {

        String comentario = "";

        if(n_decimal == 0){
            int bolo_int = (int) Math.rint(bolo);
            comentario = getString(R.string.resultado_bolo) + String.format(" %d", bolo_int);
        }
        if(n_decimal == 1){
            comentario = getString(R.string.resultado_bolo) + String.format(" %.1f", bolo);
        }
        if(n_decimal == 2)
            comentario = getString(R.string.resultado_bolo) + String.format(" %.2f", bolo);

        if (bolo < 0) {
            comentario += "\n" + getString(R.string.ingerir_carbohidratos);
        }
        return comentario;
    }

}
