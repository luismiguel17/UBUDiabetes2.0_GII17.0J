package com.ubu.lmi.gii170j.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistence.DataBaseManager;
import com.ubu.lmi.gii170j.util.AjustesListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Ajustes extends AppCompatActivity {

    private static String TAG = Ajustes.class.getName();
    private static final int RESULT_EXIT = 0;

    private String [] itemsAjustes;

    private Integer[] imagesId ={
        R.mipmap.ic_perfil_ajustes,
                R.mipmap.ic_copiaseguridad_ajustes,
            R.mipmap.ic_liberarespacio_ajustes
                };
    private ListView listaAjustes;
    SharedPreferences misPreferencias;
    SharedPreferences.Editor editorPreferencias;
    DataBaseManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemsAjustes = getResources().getStringArray(R.array.itemsAjustes);
        AjustesListAdapter ajustesListAdapter=new AjustesListAdapter(this, itemsAjustes, imagesId);
        listaAjustes =(ListView)findViewById(R.id.lv_id_listaajustes);
        listaAjustes.setAdapter(ajustesListAdapter);
        listaAjustes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Ajustes-Perfil
                if(position == 0){
                    Intent i = new Intent(getApplicationContext(), Registro.class);
                    startActivity(i);
                    finish();
                    //Ajuste-Copia Seguridad
                }else if (position == 1){
                    alertDialogBackUp();
                    //Ajuestes-Liberar espacio
                }else if(position == 2){
                    alertDialogDeleteRecords();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void backupdDatabaseUBUDiabetes() {
        File sd = null;
        File data = null;
        String packageName = null;
        String sourceDBName = null;
        String targetDBName = null;

        File currentDB = null;
        File backupDB = null;
        File newdirectory= null;
        misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        editorPreferencias = misPreferencias.edit();
        try {


            sd = Environment.getExternalStorageDirectory();
            //sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            data = Environment.getDataDirectory();
            packageName = getPackageName();
            sourceDBName = "dataBase.sqlite";
            targetDBName = "UBUDiabetes_BackUp";
            // a este path añadimos una nueva ruta de directorio.
            newdirectory = new File(sd.getAbsolutePath() + "/UBUDiabetes/");
            // creamos el directorio si no existia
            newdirectory.mkdir();
            if (sd.canWrite()) {
                //Path donde se encuentra nuestra base de datos
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                //Fecha para el backup
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                Date today = Calendar.getInstance().getTime();
                String reportDate = dateFormat.format(today);
                //Nombre del backup
                String backupDBName = targetDBName + reportDate + ".db";

                currentDB = new File(data, currentDBPath);
                backupDB = new File(newdirectory, backupDBName);

                Log.d(TAG, "backupDB=" + backupDB.getAbsolutePath());
                Log.d(TAG, "sourceDB=" + currentDB.getAbsolutePath());
                Log.d(TAG, "dateString=" +  reportDate);

                try (FileChannel src = new FileInputStream(currentDB).getChannel()) {
                    try (FileChannel dst = new FileOutputStream(backupDB).getChannel()) {
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
                editorPreferencias.putString(getString(R.string.lastBackUp), reportDate);
                editorPreferencias.apply();
            }

        } catch (Exception e) {
            Log.i("Backup", e.toString());

        }
    }
    private void alertDialogBackUp() {
        misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        String lastBackUp = misPreferencias.getString(getString(R.string.lastBackUp),"");
        String comentario = getString(R.string.comentario_copiaSeguridad) + "\n" + "Última copia de seguridad: " + lastBackUp;

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Ajustes.this);
        alertdialog.setTitle("Copia de Seguridad");
        alertdialog.setMessage(comentario);
        alertdialog.setCancelable(false);
        alertdialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                backupdDatabaseUBUDiabetes();
            }
        });
        alertdialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                setResult(RESULT_EXIT);
                finish();

            }
        });
        AlertDialog alertBackUp = alertdialog.create();
        alertBackUp.show();

    }
    public void eliminarRegistros() {

        int deleteglucemias = 0;
        int deleteincidencias = 0;
        int deletelistaingesta = 0;
        int deletedetalleslistaingesta = 0 ;
        try{
            dbmanager = new DataBaseManager(getBaseContext());
            deleteglucemias = dbmanager.eliminar("glucemias");
            deleteincidencias = dbmanager.eliminar("incidencias");
            deletelistaingesta = dbmanager.eliminar("listaingesta");
            deletedetalleslistaingesta = dbmanager.eliminar("detalleslistaingesta");
            if (deleteglucemias != 0 && deleteincidencias !=0 && deletelistaingesta != 0 && deletedetalleslistaingesta != 0) {
                Log.d(TAG, "Registros Eliminados correctamente");

            }

        }catch(Exception e){
            Log.e(TAG, "Error deleting records from tables " +  e.getMessage());
            e.printStackTrace();
        }finally{
            dbmanager.closeBD();

        }

    }
    private void alertDialogDeleteRecords(){

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Ajustes.this);
        alertdialog.setTitle("Eliminar Registros");
        String comentario = getString(R.string.comentario_eliminarRegistros);
        alertdialog.setMessage(comentario);
        alertdialog.setCancelable(false);
        alertdialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                eliminarRegistros();
            }
        });
        alertdialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                setResult(RESULT_EXIT);
                finish();

            }
        });
        AlertDialog alertDeletingRecords = alertdialog.create();
        alertDeletingRecords.show();
    }

}
