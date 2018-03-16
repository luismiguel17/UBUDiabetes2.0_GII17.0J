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
import android.widget.EditText;
import android.widget.Spinner;

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

    private Spinner listaComida, listaTipo;
    private final int RESULT_EXIT = 0;
    /*
    private static final String INTENSIDAD_ALTA = "Larga (más de 2 horas)";
    private static final String INTENSIDAD_MEDIA = "Media (de 60 a 90 minutos)";
    private static final String INTENSIDAD_IRREGULAR = "Irregular e intermitente";
    */
    private String comida;
    private double sumatorioRaciones;
    private String[] tipoAlimento;
    private String[] numeroTipoAlimento;
    private String[] alimento;

    /**
     * Datos de las raciones en gramos para cada tipo de alimento.
     * Ver: http://www.fundaciondiabetes.org/upload/publicaciones_ficheros/71/TABLAHC.pdf.
     */
    public static final int[] raciones =
            {200, 50, 50, 50, 100, // Lacteos - 22
                    200, 200, 200, 200, 20,
                    25, 300, 50, 70, 250,
                    0, 200, 125, 70, 70,
                    100, 200, // Fin de lacteos
                    //
                    13, 38, 13, 40, 12, // Cereales - 74 - Fila 1
                    13, 34, 17, 34, 15, // 2
                    50, 14, 42, 15, 38, // 3
                    15, 20, 15, 65, 50, // 4
                    40, 16, 15, 14, 18, // 5
                    20, 50, 100, 15, 17, // 6
                    70, 30, 24, 20, 50, // 7
                    20, 50, 50, 90, 20, // 8
                    15, 53, 15, 20, 20, // 9
                    20, 18, 23, 15, 15, // 10
                    15, 15, 50, 16, 50, // 11
                    35, 30, 20, 15, 80, // 12
                    19, 48, 14, 90, 30, // 13
                    100, 45, 12, 33, 14, // 14
                    42, 16, 39, 33,      // 15

                    0, 150, 100, 30, 25, // Frutas -  42 - linea 1
                    100, 50, 100, 200, 150, // 2
                    15, 150, 200, 70, 200,  // 3
                    140, 100, 100, 0, 70,   // 4
                    100, 100, 100, 50, 100, // 5
                    50, 100, 150, 20, 150,  // 6
                    100, 100, 100, 100, 125,    //7
                    100, 100, 85, 60, 50,   // 8
                    200, 50,    //9

                    300, 40, 300, 300, 500, // Hortalizas - 42 - linea 1
                    300, 0, 0, 0, 300,          // 2
                    300, 200, 300, 150, 100,    // 3
                    0, 0, 300, 0, 300,           // 4
                    0, 0, 0, 0, 250,            // 5
                    300, 0, 300, 200, 300,      // 6
                    300, 300, 300, 150, 300,    // 7
                    0, 300, 300, 300, 150,      // 8
                    200, 225,                   // 9

                    250, 15, 150, 140, 150, // Fruta grasa y seca - 15  - línea 1
                    100, 15, 15, 15, 300,       // 2
                    300, 80, 80, 100, 15,       // 3

                    130, 100, 0, 100, 250, // Bebidas - 26 - línea 1
                    80, 100, 0, 250, 250,       // 2
                    300, 250, 0, 0, 75,         // 3
                    300, 30, 70, 100, 200,      // 4
                    100, 75, 0, 75, 100,        // 5
                    250,                        // 6

                    10, 10, 20, 20, 20,         // Otros - 57 - línea 1
                    20, 12, 22, 120, 100,       // 2
                    12, 17, 25, 25, 25,         // 3
                    100, 40, 50, 23, 50,        // 4
                    23, 10, 150, 62, 10,        // 5
                    18, 50, 100, 130, 25,       // 6
                    25, 11, 20, 0, 13,          // 7
                    0, 20, 25, 35, 0,           // 8
                    40, 15, 100, 100, 150,      // 9
                    0, 100, 0, 0, 100,          // 10
                    25, 0, 120, 25, 25,         // 11
                    0, 15                       // 12
            };



    ArrayList<String> arrayAlimentos = new ArrayList<>();
    ArrayList<Integer> arrayRaciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbohidratos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText carboEt = (EditText) findViewById(R.id.et_gramos);
        carboEt.setText("0");
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();

        // Si la tabla de alimentos no estaba creada... se crea
        if (!misPreferencias.getBoolean("tablaAlimentos", false)) {
            rellenarTablaAlimentos();
            editor.putBoolean("tablaAlimentos", true);
            editor.apply();
        }

        //Generamos los adaptadores para todos los posibles spinners
        final ArrayAdapter adpTipoAlimento = ArrayAdapter.createFromResource(this, R.array.spinerTipoAlimento, android.R.layout.simple_spinner_item);

        final ArrayAdapter adpLacteo = ArrayAdapter.createFromResource(this, R.array.spinerLacteos, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpArroz = ArrayAdapter.createFromResource(this, R.array.spinerCereales, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpFruta = ArrayAdapter.createFromResource(this, R.array.spinnerFrutas, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpHortaliza = ArrayAdapter.createFromResource(this, R.array.spinnerHortalizas, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpFrutaGrasaSeca = ArrayAdapter.createFromResource(this, R.array.spinnerFrutaGrasaSeca, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpBebida = ArrayAdapter.createFromResource(this, R.array.spinerBebidas, android.R.layout.simple_spinner_item);
        final ArrayAdapter adpOtros = ArrayAdapter.createFromResource(this, R.array.spinerOtros, android.R.layout.simple_spinner_item);

        listaComida = (Spinner) findViewById(R.id.sp_comidas);
        listaComida.setAdapter(adpTipoAlimento);
        listaTipo = (Spinner) findViewById(R.id.spiner_tipo);
        listaComida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Establecemos los datos del segundo spinner en función del primero
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    listaTipo.setAdapter(adpLacteo);
                } else if (position == 1) {
                    listaTipo.setAdapter(adpArroz);
                } else if (position == 2) {
                    listaTipo.setAdapter(adpFruta);
                } else if (position == 3) {
                    listaTipo.setAdapter(adpHortaliza);
                } else if (position == 4) {
                    listaTipo.setAdapter(adpFrutaGrasaSeca);
                } else if (position == 5) {
                    listaTipo.setAdapter(adpBebida);
                } else if (position == 6) {
                    listaTipo.setAdapter((adpOtros));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listaTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comida = listaTipo.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Función rellena la tabla de alimentos a partir del archivo arrays.xml
     */
    public void rellenarTablaAlimentos() {
        tipoAlimento = getResources().getStringArray(R.array.arrayTipoAlimento);
        numeroTipoAlimento = getResources().getStringArray(R.array.numeroTipoAlimento);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Tipos de alimentos: " + tipoAlimento.length);
            Log.d(TAG, "Numero de elementos de tipos de alimentos: " + numeroTipoAlimento.length);
        }


        alimento = getResources().getStringArray(R.array.arrayAlimentos);

        DataBaseManager dbmanager = new DataBaseManager(this);
        ContentValues values = new ContentValues();
        int contadorNumeroTipoAlimento = 0;
        int acumulado = Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);

        for (int i = 0; i < alimento.length; ++i) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Procesando tipo de alimento: " + tipoAlimento[contadorNumeroTipoAlimento] +
                        " Alimento: " + alimento[i] + " 1 Ración en gramos: " + raciones[i]);
            }
            values.put("tipoAlimento", tipoAlimento[contadorNumeroTipoAlimento]);
            values.put("alimento", alimento[i]);
            values.put("racion", raciones[i]);
            dbmanager.insertar("alimentos", values);
            values.clear();
            // si hemos procesado todos los alimentos de un cierto y no hay más tipos de alimentos a procesar
            if (i == acumulado - 1 && contadorNumeroTipoAlimento < tipoAlimento.length - 1) {
                contadorNumeroTipoAlimento++; // avanzamos al siguiente tipo de alimento
                acumulado += Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);
            }
        }
    }

    /**
     * Función que define el comportamiento de la aplicacion al pulsar el boton añadir
     * Acumula el resultado obtenido y deja el editext por defecto para permitir añadir mas alimentos.
     *
     * @param view
     */
    public void añadirOtroOnClick(View view) {
        EditText gramosEt = (EditText) findViewById(R.id.et_gramos);
        String gramos = gramosEt.getText().toString();
        int numeroGramos = 0;
        if (!gramos.equals("")) {
            numeroGramos = Integer.parseInt(gramos);
        }
        gramosEt.setText("0");

        DataBaseManager dbmanager = new DataBaseManager(this);
        final Cursor cursorAlimentos = dbmanager.selectAlimento(comida);
        if (cursorAlimentos.moveToFirst()) {
            String gramosPorRacion = cursorAlimentos.getString(COLUMNA_RACION);
            // RMS: Cambiamos el cálculo de la formula en la versión 1.1
            //sumatorioRaciones += Integer.parseInt(n) * nracion; // Versión 1.0
            sumatorioRaciones += calcularGramosDeHidratosDeCarbono(numeroGramos, gramosPorRacion);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Grams per ration: " + gramosPorRacion + " Grams: " + numeroGramos);
                Log.d(TAG, "Current count of carbohidrates (HC): " + sumatorioRaciones);
            }
        }
    }

    /**
     * Calcula los gramos de HC para el alimento según los gramos ingeridos
     *
     * @param numeroGramos gramos ingeridos
     * @param gramosPorRacion gramos por ración en la tabla de la federación española
     * @return formula aplicada para el cálculo o cero si no es valorable
     */
    private double calcularGramosDeHidratosDeCarbono(int numeroGramos, String gramosPorRacion) {
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
        EditText gramosEt = (EditText) findViewById(R.id.et_gramos);
        String gramos = gramosEt.getText().toString();
        int numeroGramos = 0;
        if (!gramos.equals("")) {
            numeroGramos = Integer.parseInt(gramos);
        }

        DataBaseManager dbmanager = new DataBaseManager(this);
        final Cursor cursorAlimentos = dbmanager.selectAlimento(comida);
        if (cursorAlimentos.moveToFirst()) {
            String gramosPorRacion = cursorAlimentos.getString(COLUMNA_RACION);
            // RMS: Cambiamos el cálculo de la formula en la versión 1.1
            //sumatorioRaciones += Integer.parseInt(n) * nracion; // Versión 1.0
            sumatorioRaciones += calcularGramosDeHidratosDeCarbono(numeroGramos, gramosPorRacion);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Grams per ration: " + gramosPorRacion + " Grams: " + numeroGramos);
                Log.d(TAG, "Current count of carbohidrates (HC): " + sumatorioRaciones);
            }
        }

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        // Cargar valores de preferencias
        boolean rapida = misPreferencias.getBoolean(getString(R.string.rapida), false);
        double insulinaBasal = Double.parseDouble(misPreferencias.getString(getString(R.string.udsBasal), "0"));
        double insulinaRapida = Double.parseDouble(misPreferencias.getString(getString(R.string.udsRapida), "0"));
        double glucemiaMinima = Double.parseDouble(misPreferencias.getString(getString(R.string.min), "0"));
        double glucemiaMaxima = Double.parseDouble(misPreferencias.getString(getString(R.string.max), "0"));
        double glucemia = misPreferencias.getInt(getString(R.string.glucemia), 0);
        ValoresPOJO valoresPOJO = new ValoresPOJO(rapida, insulinaBasal, insulinaRapida, glucemiaMinima, glucemiaMaxima, glucemia);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Current glucemia: " + glucemia + " Current count of carbohidrates (HC): " + sumatorioRaciones);
        }

        CalculaBolo cb = new CalculaBolo(valoresPOJO, sumatorioRaciones);

        // Version 1.1.1, se calcula el bolo SIN DECIMALES, redondeando al entero más cercano.
        int boloResult = (int) Math.rint(cb.calculoBoloCorrector());

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Bolo calculado: " + boloResult);
        }
        //String comentarioFinal = generaComentarioBolo(tipoEjer, boloResult);
        String comentarioFinal = generaComentarioBolo(boloResult);


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
    private String generaComentarioBolo(int bolo) {
        String comentario = getString(R.string.resultado_bolo) + String.format(" %d", bolo);
        if (bolo < 0) {
            comentario += "\n" + getString(R.string.ingerir_carbohidratos);
        }
        return comentario;
    }

}
