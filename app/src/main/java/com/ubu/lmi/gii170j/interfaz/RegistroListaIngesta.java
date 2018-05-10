package com.ubu.lmi.gii170j.interfaz;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.BuildConfig;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.DataBaseManager;

import java.util.ArrayList;

public class RegistroListaIngesta extends Activity {

    /**
     * Tag for log.
     */
    private static String TAG = RegistroListaIngesta.class.getName();
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_FECHA = 1;
    public static final int COLUMNA_SUMATORIOHC = 2;
    public static final int COLUMNA_BOLOC = 3;

    //para eliminar items de la lisview
    private int itemSelec=0;

    ListaIngesta listaIngesta;
    private ArrayList<ListaIngesta> registro_listIngesta;
    private IngestaRegistros_ListAdapter adp_registro_listIngesta;
    ListView lv_registro_ingestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_lista_ingesta);

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
                itemSelec = position;

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Se va a consultar el item: " + itemSelec);
                }
            }
        });
    }

    public void consultarItemLista(View view) {
        Intent intent = new Intent(RegistroListaIngesta.this, ConsultaDetallesListaIngesta.class);
        intent.putExtra("id_lista", (itemSelec+1));
        startActivity(intent);

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

                    listaIngesta = new ListaIngesta(id_listaIngesta,fecha,sumatorioHC,bolo_C);
                    registro_listIngesta.add(listaIngesta);
                    adp_registro_listIngesta.notifyDataSetChanged();
                }
            }finally{

                if (cursorRegistrosIngesta != null && !cursorRegistrosIngesta.isClosed())
                    cursorRegistrosIngesta.close();
            }
            dbmanager.closeBD();
        }
        @Override
        protected void onPostExecute(String s) {
            // super.onPostExecute(s);

            Toast.makeText(RegistroListaIngesta.this, "Finalizado Carga de datos...", Toast.LENGTH_LONG).show();
            //Intent menuPrincipal = new Intent(Registro.this, MenuPrincipal.class);
            //menuPrincipal.putExtra("usuario", nombreEt.getText().toString());
            //startActivity(menuPrincipal);
            //finish();
        }



    }

}
