package com.ubu.lmi.gii170j.interfaz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.ubu.lmi.gii170j.R;

public class MenuPrincipal extends AppCompatActivity {


    SharedPreferences misPreferencias;
    SharedPreferences.Editor editorPreferencias;
    //String[] opciones = {getString(R.string.main_registro),getString(R.string.main_historial),getString(R.string.main_registroIngestas)};
    CircleMenu circleMenuPrincipal;
    TextView welcome;
    TextView welcomeUser;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Salir");
        alertDialogBuilder
                .setMessage("¿Quiere salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        welcome = (TextView)findViewById(R.id.tx_id_welcome);
        welcomeUser = (TextView)findViewById(R.id.tv_id_welcomeuser);
        misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        editorPreferencias = misPreferencias.edit();

        welcomeUser.setText(misPreferencias.getString(getString(R.string.nombre), ""));

        circleMenuPrincipal = (CircleMenu)findViewById(R.id.cm_id_fragmentMenuPrincipal);

        circleMenuPrincipal.setMainMenu(Color.parseColor("#fef359"),R.drawable.ic_lock_open_black_24dp,R.drawable.ic_lock_outline_black_24dp)
                .addSubMenu(Color.parseColor("#00aacc"),R.mipmap.ic_calcular_bolo_menu)
                .addSubMenu(Color.parseColor("#FFE0B2"),R.drawable.ic_create_black_24dp)
                .addSubMenu(Color.parseColor("#00E676"),R.drawable.ic_timeline_black_24dp)
                .addSubMenu(Color.parseColor("#FFB74D"),R.drawable.ic_search_black_24dp)


                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int item) {

                        if(item == 0){
                            //SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
                            //SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
                            editorPreferencias.putBoolean("boloCorrector",true);
                            editorPreferencias.apply();

                            Intent paso1 = new Intent(getApplicationContext(), RegistroGlucemias.class);
                            startActivity(paso1);

                        } else if (item == 1) {
                            //SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
                            //SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
                            editorPreferencias.putBoolean("boloCorrector",false);
                            editorPreferencias.apply();
                            Intent i = new Intent(getApplicationContext(), RegistroGlucemias.class);
                            startActivity(i);
                        }else if(item == 2){
                            Intent i = new Intent(getApplicationContext(), Historial.class);
                            startActivity(i);
                        }else if (item == 3){
                            //Pruebas para consultar los registros de ingesta
                            Intent i = new Intent(getApplicationContext(), RegistroListaIngesta.class);
                            startActivity(i);
                        }

                    }
                });





        /**
        ListView listaMenu = (ListView) findViewById(R.id.lv_opciones);
        ArrayAdapter<String> adaptlv = new ArrayAdapter<>(getApplicationContext(),R.layout.list_black_text,opciones);
        listaMenu.setAdapter(adaptlv);
        ListViewUtility.setListViewHeightBasedOnChildren(listaMenu);

        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
                    //SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
                    editorPreferencias.putBoolean("boloCorrector",false);
                    editorPreferencias.apply();
                    Intent i = new Intent(getApplicationContext(), RegistroGlucemias.class);
                    startActivity(i);
                }else if(position == 1){
                    Intent i = new Intent(getApplicationContext(), Historial.class);
                    startActivity(i);
                }else if (position == 2){
                    //Pruebas para consultar los registros de ingesta
                    Intent i = new Intent(getApplicationContext(), RegistroListaIngesta.class);
                    startActivity(i);
                }
            }
        });
         */


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
    /**
    public void calcularBoloOnClick(View view){
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit();
        editorPreferencias.putBoolean("boloCorrector",true);
        editorPreferencias.apply();

        Intent paso1 = new Intent(this, RegistroGlucemias.class);
        startActivity(paso1);

    }
     */

}
