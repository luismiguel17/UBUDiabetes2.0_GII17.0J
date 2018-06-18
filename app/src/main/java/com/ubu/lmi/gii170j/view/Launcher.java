package com.ubu.lmi.gii170j.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario",MODE_PRIVATE);
        Boolean arranque = misPreferencias.getBoolean("primeraEjecucion", false);
        Intent perfil = new Intent(this, Registro.class);
        //nuevo-Para la imagen de perfil
        //String uri = (misPreferencias.getString(getString(R.string.image_perfil), ""));
        Intent menuPrincipal = new Intent(this, MenuPrincipal.class);
        //menuPrincipal.putExtra("imagenPerfil", uri);

        if(!arranque){

            startActivity(menuPrincipal);
            startActivity(perfil);

        }else{
            startActivity(menuPrincipal);
        }
        finish();
    }
}
