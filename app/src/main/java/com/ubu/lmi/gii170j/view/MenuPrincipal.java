package com.ubu.lmi.gii170j.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.ubu.lmi.gii170j.R;

public class MenuPrincipal extends AppCompatActivity {


    SharedPreferences misPreferencias;
    SharedPreferences.Editor editorPreferencias;
    CircleMenu circleMenuPrincipal;
    TextView welcomeUser;
    ImageView imageViewPerfil;

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

        welcomeUser = (TextView)findViewById(R.id.tv_id_welcomeuser);
        imageViewPerfil = (ImageView)findViewById(R.id.iv_id_imageprofile_MP);

        misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        editorPreferencias = misPreferencias.edit();

        welcomeUser.setText(misPreferencias.getString(getString(R.string.nombre), ""));
        Uri uri = Uri.parse(misPreferencias.getString(getString(R.string.image_perfil), ""));
        imageViewPerfil.setImageURI(uri);


        circleMenuPrincipal = (CircleMenu)findViewById(R.id.cm_id_fragmentMenuPrincipal);

        circleMenuPrincipal.setMainMenu(Color.parseColor("#fef359"),R.drawable.ic_zoom_out_map_black_24dp,R.drawable.ic_vertical_align_center_black_24dp)
                .addSubMenu(Color.parseColor("#00aacc"),R.mipmap.ic_calcular_bolo_menu)
                .addSubMenu(Color.parseColor("#FFE0B2"),R.drawable.ic_create_black_24dp)
                .addSubMenu(Color.parseColor("#00E676"),R.drawable.ic_timeline_black_24dp)
                .addSubMenu(Color.parseColor("#FFB74D"),R.drawable.ic_search_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int item) {

                        if(item == 0){

                            editorPreferencias.putBoolean("boloCorrector",true);
                            editorPreferencias.apply();

                            Intent paso1 = new Intent(getApplicationContext(), RegistroGlucemias.class);
                            startActivity(paso1);

                        } else if (item == 1) {

                            editorPreferencias.putBoolean("boloCorrector",false);
                            editorPreferencias.apply();
                            Intent i = new Intent(getApplicationContext(), RegistroGlucemias.class);
                            startActivity(i);
                        }else if(item == 2){
                            Intent i = new Intent(getApplicationContext(), Historial.class);
                            startActivity(i);
                        }else if (item == 3){

                            Intent i = new Intent(getApplicationContext(), RegistroListaIngesta.class);
                            startActivity(i);
                        }

                    }
                });
        circleMenuPrincipal.openMenu();

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
            Intent i = new Intent(this,Ajustes.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
