package com.ubu.lmi.gii170j.view;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.DetallesIngesta;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.IngestaDetallesListAdapter;

import java.util.ArrayList;

public class ConsultaDetallesListaIngesta extends AppCompatActivity {

    public static final int COLUMNA_TIPO = 0;
    public static final int COLUMNA_IG = 3;
    public static final int COLUMNA_ALIMENTO = 2;
    public static final int COLUMNA_CANTIDAD = 3;

    public int idListaSelect =0;

    DetallesIngesta detallesListaIngesta;
    private ArrayList<DetallesIngesta> detalleListIngesta;
    private IngestaDetallesListAdapter adpDetalleListIngesta;
    ListView lvDetalleIngesta;
    TextView tVDetallesLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles__lista_ingesta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        idListaSelect = getIntent().getExtras().getInt("id_lista");
        lvDetalleIngesta = (ListView)findViewById(R.id.lv_id_detalleslistaingesta_dli);

        tVDetallesLista = (TextView)findViewById(R.id.tv_id_dli);
        String txtEncabezadoLista = getString(R.string.detalles_lista) + ": " + idListaSelect;
        tVDetallesLista.setText(txtEncabezadoLista);

        detalleListIngesta = new ArrayList<>();
        adpDetalleListIngesta = new IngestaDetallesListAdapter(this,R.layout.list_detalles_lista_ingesta, detalleListIngesta);
        lvDetalleIngesta.setAdapter(adpDetalleListIngesta);

        /**
         * LLamada a la asynktask
         */
        new AsyncTaskConsultaDetallesLista().execute("get_info");
    }

    class AsyncTaskConsultaDetallesLista extends AsyncTask<String,Void,String> {
        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());

        ArrayList<String> tipo = new ArrayList<>();
        ArrayList<String> nomAlimento = new ArrayList<>();
        ArrayList<String> cantidad = new ArrayList<>();
        ArrayList<String> ig = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            // Required empty protected method
        }


        @Override
        protected String doInBackground(String... strings) {
            consultaDetallesListaIngesta();
            consultarAlimento();
            createListDetalles();
            return strings[0];
        }
        public void consultaDetallesListaIngesta(){
            final Cursor cursorDetalleRegistrosIngesta = dbmanager.consultarDetallesIngesta(idListaSelect);
            try{
                while(cursorDetalleRegistrosIngesta.moveToNext()){

                    nomAlimento.add(cursorDetalleRegistrosIngesta.getString(COLUMNA_ALIMENTO));
                    cantidad.add(cursorDetalleRegistrosIngesta.getString(COLUMNA_CANTIDAD));
                }
            }finally{
                cursorDetalleRegistrosIngesta.close();

            }

        }

        public void consultarAlimento(){


            for(int i = 0; i< nomAlimento.size(); i++){
                final Cursor cursorAlimento = dbmanager.selectAlimento(nomAlimento.get(i));
                try{
                    while(cursorAlimento.moveToNext()){

                        tipo.add(cursorAlimento.getString(COLUMNA_TIPO));
                        ig.add(cursorAlimento.getString(COLUMNA_IG));

                    }
                }finally{
                    cursorAlimento.close();

                }
            }


        }

        public void createListDetalles(){

            for(int j = 0; j < nomAlimento.size(); j++){
                detallesListaIngesta = new DetallesIngesta(tipo.get(j), nomAlimento.get(j),cantidad.get(j),ig.get(j));
                detalleListIngesta.add(0,detallesListaIngesta);
                adpDetalleListIngesta.notifyDataSetChanged();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            dbmanager.closeBD();
        }

    }

}
