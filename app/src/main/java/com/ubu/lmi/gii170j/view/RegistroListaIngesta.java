package com.ubu.lmi.gii170j.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.RegistroIngestas;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.IngestaRegistrosListAdapter;

import java.util.ArrayList;

public class RegistroListaIngesta extends AppCompatActivity {

    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_FECHA = 1;
    public static final int COLUMNA_SUMATORIOHC = 2;
    public static final int COLUMNA_BOLOC = 3;

    //para eliminar items de la lisview
    private int idListSelected =0;

    RegistroIngestas registroIngestas;
    private ArrayList<RegistroIngestas> registroListIngesta;
    private IngestaRegistrosListAdapter adpRegistroListIngesta;
    ListView lvRegistroIngestas;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_lista_ingesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvRegistroIngestas = (ListView)findViewById(R.id.lv_id_listaingesta_rli);


        registroListIngesta = new ArrayList<>();
        adpRegistroListIngesta = new IngestaRegistrosListAdapter(this,R.layout.list_ingesta_registros, registroListIngesta);
        lvRegistroIngestas.setAdapter(adpRegistroListIngesta);

        new ConsultaListaIngesta().execute("get_info");

        // Comportamiento de la listview cuando se selecciona un item
        lvRegistroIngestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvRegistroIngestas.setSelector(new ColorDrawable(Color.CYAN));

                RegistroIngestas objSelected = (RegistroIngestas) parent.getItemAtPosition(position);
                idListSelected = Integer.parseInt(objSelected.getIdLista());

            }
        });

    }

    public void consultarItemLista(View view) {
        if(idListSelected != 0){
            Intent intent = new Intent(RegistroListaIngesta.this, ConsultaDetallesListaIngesta.class);
            intent.putExtra("id_lista", (idListSelected));
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(), "Seleccione un item" , Toast.LENGTH_LONG).show();
        }


    }

    class ConsultaListaIngesta extends AsyncTask<String,Void,String> {
        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());

        @Override
        protected void onPreExecute() {
           // Required empty protected method.
        }


        @Override
        protected String doInBackground(String... strings) {

            consultaRegistrosIngesta();
            return strings[0];
        }
        public void consultaRegistrosIngesta() {

            String idListaIngesta = "";
            String fecha = "";
            String sumatorioHC = "";
            String boloC = "";

            final Cursor cursorRegistrosIngesta = dbmanager.consultarRegistroIngesta();
            try {
                while (cursorRegistrosIngesta.moveToNext()) {
                    idListaIngesta = cursorRegistrosIngesta.getString(COLUMNA_ID);
                    fecha = cursorRegistrosIngesta.getString(COLUMNA_FECHA);
                    sumatorioHC = cursorRegistrosIngesta.getString(COLUMNA_SUMATORIOHC);
                    boloC = cursorRegistrosIngesta.getString(COLUMNA_BOLOC);

                    registroIngestas = new RegistroIngestas(idListaIngesta, fecha, sumatorioHC, boloC);
                    registroListIngesta.add(0, registroIngestas);
                    adpRegistroListIngesta.notifyDataSetChanged();
                }
            } finally {
                cursorRegistrosIngesta.close();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            dbmanager.closeBD();
        }



    }

}
