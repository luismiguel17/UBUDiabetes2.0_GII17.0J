package com.ubu.lmi.gii170j.view;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Incidencias extends AppCompatActivity {

    private static final int RESULT_EXIT = 0;
    private String incidencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int valor = bundle.getInt("valor");
        int max = bundle.getInt("max");

        final ArrayAdapter adapter;
        Spinner listaIncidencias = (Spinner) findViewById(R.id.sp_incidencias);

        //Comprobamos si la incidencia se ha producido por un valor más alto o mas bajo de lo normal y rellenamos
        //el spinner en consecuencia
        if (valor > max) {
            adapter = ArrayAdapter.createFromResource(this, R.array.spinnerIncidenciasAlto, android.R.layout.simple_spinner_item);
        }else {
            adapter = ArrayAdapter.createFromResource(this, R.array.spinnerIncidenciasBajo, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaIncidencias.setAdapter(adapter);

        listaIncidencias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incidencia = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Required empty public method
            }
        });
    }

    /**
     * Función que define el comportamiento de la aplicación al pulsar el boton Guardar
     *
     * @param view
     */
    public void incidenciasOnClick(View view) {
        EditText observEt = (EditText) findViewById(R.id.et_observaciones);
        String observ = observEt.getText().toString();


        Bundle bundle = getIntent().getExtras();
        Long id = bundle.getLong("id");
        Integer idGlucemia = id.intValue();

        DataBaseManager dbmanager = new DataBaseManager(this);

        long insertar = dbmanager.insertar("incidencias", generarContentValues(incidencia, observ, idGlucemia));

        if (insertar != -1) {
            Toast.makeText(Incidencias.this, R.string.incidencia_correcta, Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_EXIT);
        super.onBackPressed();

    }

    /**
     * Función que genera el ContentValues necesario para insertar la incidencia en la base de datos
     *
     * @param incidencia    incidencia seleccionada
     * @param observaciones observaciones introducidas
     * @param id            id de la glucemia a que hace referencia
     */
    public ContentValues generarContentValues(String incidencia, String observaciones, Integer id) {
        ContentValues valores = new ContentValues();
        String fecha = getDateTime();
        valores.put("fecha", fecha);
        valores.put("glucemia", id);
        valores.put("tipo", incidencia);
        valores.put("observaciones", observaciones);
        return valores;
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


}
