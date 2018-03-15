package com.ubu.lmi.gii170j.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by LuisMiguel on 15/03/2018.
 */

public class DataBaseManager {
    private DataBaseHelper helper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context){
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();

    }

    public static final String CREATE_GLUCEMIAS = "create table glucemias("
            +"id integer primary key autoincrement,"
            +"fecha text not null,"
            +"periodo text not null,"
            +"valor int not null);";
    public static final String CREATE_INCIDENCIAS = "create table incidencias("
            +"id integer primary key autoincrement,"
            +"tipo text not null,"
            +"observaciones text not null,"
            +"fecha text not null,"
            +"glucemia integer not null)";

    public static final String CREATE_ALIMENTOS = "create table alimentos("
            +"tipoAlimento text not null,"
            +"alimento text primary key,"
            +"racion int not null);";

    /**
     * Función que inserta una entrada en una determinada tabla
     */
    public long insertar(String tabla, ContentValues valores){
        long result;
        result= db.insert(tabla,null,valores);

        return result;
    }

    /**
     * Función que devuelve el resultado de una consulta de todos los alimentos
     */
    public Cursor consultarAlimentos(){
        return db.rawQuery("select * from Alimentos",null);
    }

    /**
     * Función que devuelve el resultado de una consulta de un alimento a partir de su id
     */
    public Cursor selectAlimento(String id){
        return db.rawQuery("select * from Alimentos where Alimento='"+id+"'",null);
    }

    String columnas[]=new String[]{"id","fecha","periodo","valor"};
    public Cursor consultarGlucemias(){
        return db.query("Glucemias",columnas,null,null,null,null,null);
    }

    /**
     * Función que devuelve el resultado de una consulta de una glucemia por fecha y periodo
     */
    public Cursor selectGlucemia(String fecha, String periodo){
        return db.rawQuery("select * from Glucemias where Fecha='"+fecha+"' and Periodo='"+periodo+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una glucemia por fecha y valor
     */
    public Cursor selectGlucemiaValor(String fecha, int valor){
        return db.rawQuery("select * from Glucemias where Fecha='"+fecha+"' and Valor='"+valor+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una incidencia por el id de la glucemia
     * a la que esta asociada
     */
    public Cursor selectIncidencia(int idGlucemia){
        return db.rawQuery("select * from Incidencias where Glucemia='"+idGlucemia+"'",null);
    }
}
