package com.ubu.lmi.gii170j.interfaz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ubu.lmi.gii170j.R;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] opciones = {getString(R.string.main_registro),getString(R.string.main_historial)};

        ListView listaMenu = (ListView) findViewById(R.id.lv_opciones);
        ArrayAdapter<String> adaptlv = new ArrayAdapter<>(getApplicationContext(),R.layout.list_black_text,opciones);
        listaMenu.setAdapter(adaptlv);
        ListViewUtility.setListViewHeightBasedOnChildren(listaMenu);

        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
                    SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
                    editorPreferencias.putBoolean("boloCorrector",false);
                    editorPreferencias.apply();
                    Intent i = new Intent(getApplicationContext(), RegistroGlucemias.class);
                    startActivity(i);
                }else if(position == 1){
                    Intent i = new Intent(getApplicationContext(), Historial.class);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.action_acerca){
            Toast.makeText(this, getString(R.string.acerca_de), Toast.LENGTH_LONG).show();
        }else if(id==R.id.action_settings){
            Intent i = new Intent(this,Registro.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Función que define el comportamiento de la aplicacion al pulsar el boton Calcular Bolo
     */
    public void calcularBoloOnClick(View view){
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
        editorPreferencias.putBoolean("boloCorrector",true);
        editorPreferencias.apply();

        Intent paso1 = new Intent(this, RegistroGlucemias.class);
        startActivity(paso1);

    }

}
