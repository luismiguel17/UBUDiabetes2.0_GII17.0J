package com.ubu.lmi.gii170j.interfaz;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.DataBaseManager;

import java.util.ArrayList;

public class ConsultaDetallesListaIngesta extends AppCompatActivity {

    private static String TAG = ConsultaDetallesListaIngesta.class.getName();
    public static final int COLUMNA_TIPO = 0;
    public static final int COLUMNA_IG = 3;
    public static final int COLUMNA_ALIMENTO = 2;
    public static final int COLUMNA_CANTIDAD = 3;

    public int id_lista_select=0;

    DetallesListaIngesta detallesListaIngesta;
    private ArrayList<DetallesListaIngesta> detalle_listIngesta;
    private IngestaDetalles_ListAdapter adp_detalle_listIngesta;
    ListView lv_detalle_ingesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles__lista_ingesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        id_lista_select = getIntent().getExtras().getInt("id_lista");
        lv_detalle_ingesta = (ListView)findViewById(R.id.lv_id_detalleslistaingesta_dli);


        detalle_listIngesta = new ArrayList<>();
        adp_detalle_listIngesta = new IngestaDetalles_ListAdapter(this,R.layout.list_detalles_lista_ingesta,detalle_listIngesta);
        lv_detalle_ingesta.setAdapter(adp_detalle_listIngesta);

        /**
         * LLamada a la asynktask
         */
        new AsyncTaskConsultaDetallesLista().execute("get_info");
    }

    public void volver(View view) {

        Intent intent = new Intent(getBaseContext(), RegistroListaIngesta.class);
        startActivity(intent);
        finish();

    }

    class AsyncTaskConsultaDetallesLista extends AsyncTask<String,Void,String> {
        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());

        ArrayList<String> tipo = new ArrayList<String>();
        ArrayList<String> nom_alimento = new ArrayList<String>();
        ArrayList<String> cantidad = new ArrayList<String>();
        ArrayList<String> ig = new ArrayList<String>();
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //Toast.makeText(ConsultaDetallesListaIngesta.this, "Cargando detalles:", Toast.LENGTH_LONG).show();
        }


        @Override
        protected String doInBackground(String... strings) {

            //Toast.makeText(Registro.this, "Cargando datos...:" , Toast.LENGTH_LONG).show();

            consultaDetallesListaIngesta();
            consultarAlimento();
            createListDetalles();
            return strings[0];
        }
        public void consultaDetallesListaIngesta(){
            final Cursor cursorDetalleRegistrosIngesta = dbmanager.consultarDetallesIngesta(id_lista_select);
            try{
                while(cursorDetalleRegistrosIngesta.moveToNext()){

                    nom_alimento.add(cursorDetalleRegistrosIngesta.getString(COLUMNA_ALIMENTO));
                    cantidad.add(cursorDetalleRegistrosIngesta.getString(COLUMNA_CANTIDAD));
                }
            }finally{

                if (cursorDetalleRegistrosIngesta != null && !cursorDetalleRegistrosIngesta.isClosed())
                    cursorDetalleRegistrosIngesta.close();
                //dbmanager.closeBD();
            }
            //dbmanager.closeBD();

        }

        public void consultarAlimento(){


            for(int i=0;i<nom_alimento.size();i++){
                final Cursor cursorAlimento = dbmanager.selectAlimento(nom_alimento.get(i));
                try{
                    while(cursorAlimento.moveToNext()){

                        tipo.add(cursorAlimento.getString(COLUMNA_TIPO));
                        ig.add(cursorAlimento.getString(COLUMNA_IG));

                    }
                }finally{

                    if (cursorAlimento != null && !cursorAlimento.isClosed())
                        cursorAlimento.close();
                    //dbmanager.closeBD();
                }
            }


        }

        public void createListDetalles(){

            for(int j = 0;j < nom_alimento.size();j++){
                detallesListaIngesta = new DetallesListaIngesta(tipo.get(j),nom_alimento.get(j),cantidad.get(j),ig.get(j));
                detalle_listIngesta.add(0,detallesListaIngesta);
                adp_detalle_listIngesta.notifyDataSetChanged();
            }
            //dbmanager.closeBD();
        }
        @Override
        protected void onPostExecute(String s) {
            dbmanager.closeBD();
        }

    }

}
