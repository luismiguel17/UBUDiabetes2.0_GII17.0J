package com.ubu.lmi.gii170j.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.BuildConfig;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.model.RegistroIngestas;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.IngestaRegistros_ListAdapter;

import java.util.ArrayList;

public class RegistroListaIngesta extends AppCompatActivity {

    /**
     * Tag for log.
     */
    private static String TAG = RegistroListaIngesta.class.getName();
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_FECHA = 1;
    public static final int COLUMNA_SUMATORIOHC = 2;
    public static final int COLUMNA_BOLOC = 3;

    //para eliminar items de la lisview
    private int idListSelected =0;

    RegistroIngestas registroIngestas;
    private ArrayList<RegistroIngestas> registro_listIngesta;
    private IngestaRegistros_ListAdapter adp_registro_listIngesta;
    ListView lv_registro_ingestas;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit();

        lv_registro_ingestas = (ListView)findViewById(R.id.lv_id_listaingesta_rli);


        registro_listIngesta = new ArrayList<>();
        adp_registro_listIngesta = new IngestaRegistros_ListAdapter(this,R.layout.list_ingesta_registros,registro_listIngesta);
        lv_registro_ingestas.setAdapter(adp_registro_listIngesta);

        new ConsultaListaIngesta().execute("get_info");

        // Comportamiento de la listview cuando se selecciona un item
        lv_registro_ingestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_registro_ingestas.setSelector(new ColorDrawable(Color.CYAN));

                RegistroIngestas obj_selected = (RegistroIngestas) parent.getItemAtPosition(position);
                idListSelected = Integer.parseInt(obj_selected.getId_lista());
                //idListSelected = position;
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Se va a consultar la lista con id: " + idListSelected);
                }
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
        //ContentValues values;// = new ContentValues();

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //Toast.makeText(RegistroListaIngesta.this, "Cargando datos...:", Toast.LENGTH_LONG).show();

        }


        @Override
        protected String doInBackground(String... strings) {

            //Toast.makeText(Registro.this, "Cargando datos...:" , Toast.LENGTH_LONG).show();

            consultaRegistrosIngesta();
            return strings[0];
        }
        public void consultaRegistrosIngesta(){

            String id_listaIngesta = "";
            String fecha="";
            String sumatorioHC="";
            String bolo_C="";

            //final Cursor cursorAlimentos = dbmanager.selectAlimento(alimento);
            final Cursor cursorRegistrosIngesta = dbmanager.consultarRegistroIngesta();
            try{
                while(cursorRegistrosIngesta.moveToNext()){
                    id_listaIngesta = cursorRegistrosIngesta.getString(COLUMNA_ID);
                    fecha = cursorRegistrosIngesta.getString(COLUMNA_FECHA);
                    sumatorioHC = cursorRegistrosIngesta.getString(COLUMNA_SUMATORIOHC);
                    bolo_C = cursorRegistrosIngesta.getString(COLUMNA_BOLOC);

                    registroIngestas = new RegistroIngestas(id_listaIngesta,fecha,sumatorioHC,bolo_C);
                    registro_listIngesta.add(0, registroIngestas);
                    adp_registro_listIngesta.notifyDataSetChanged();
                }
            }finally{

                if (cursorRegistrosIngesta != null && !cursorRegistrosIngesta.isClosed())
                    cursorRegistrosIngesta.close();
            }
            //dbmanager.closeBD();
        }
        @Override
        protected void onPostExecute(String s) {
            // super.onPostExecute(s);

            //Toast.makeText(RegistroListaIngesta.this, "Finalizado Carga de datos...", Toast.LENGTH_LONG).show();
            //Intent menuPrincipal = new Intent(Registro.this, MenuPrincipal.class);
            //menuPrincipal.putExtra("usuario", nombreEt.getText().toString());
            //startActivity(menuPrincipal);
            //finish();
            dbmanager.closeBD();
        }



    }

}
