package com.ubu.lmi.gii170j.persistence;

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
            +"racion int not null,"
            +"indiceGlucemico int not null);";

    //Nuevo- Para el-registro de Alimentos
    public static final String CREATE_LISTA_INGESTA = "create table listaingesta("
            +"id integer primary key autoincrement,"
            +"fecha text not null,"
            +"sum_HC real not null,"
            +"bolo_corrector real not null);";

    public static final String CREATE_DETALLES_LISTA_INGESTA = "create table detalleslistaingesta("
            +"id integer primary key autoincrement,"
            +"id_lista integer not null,"
            +"alimento text not null,"
            +"cantidad real not null,"
            +"CONSTRAINT fk_listaIngesta FOREIGN KEY(id_lista) REFERENCES listaingesta(id_lista),"
            +"CONSTRAINT fk_alimento FOREIGN KEY(alimento) REFERENCES alimentos(alimento));";
    /**
     * Función que inserta una entrada en una determinada tabla
     */
    public long insertar(String tabla, ContentValues valores){
        long result;
        result= db.insert(tabla,null,valores);
        return result;
    }
    public int eliminar(String tabla ){
        int result;
        result =db.delete(tabla,null,null);
        return result;
    }

    /**
     * Función que devuelve el resultado de una consulta de todos los alimentos.
     */
    public Cursor consultarAlimentos(){

        return db.rawQuery("select * from Alimentos",null);
    }

    public Cursor consultarRegistroIngesta(){
        return db.rawQuery("select * from listaingesta",null);
    }
    public Cursor consultarDetallesIngesta(int argIdLista){
        return db.rawQuery("select * from detalleslistaingesta where id_lista ='"+argIdLista+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de un alimento a partir de su id
     */
    public Cursor selectAlimento(String id){
        return db.rawQuery("select * from Alimentos where Alimento='"+id+"'",null);
    }

    String[] columnas=new String[]{"id","fecha","periodo","valor"};
    public Cursor consultarGlucemias(){
        return db.query("Glucemias",columnas,null,null,null,null,null);
    }

    /**
     * Función que devuelve el resultado de una consulta de una glucemia por mes
     */
    public Cursor selectGlucemia(String fecha){
        return db.rawQuery("select * from Glucemias where Fecha LIKE'%"+fecha+"%'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una glucemia por fecha y periodo
     */
    public Cursor selectGlucemia(String fecha, String periodo){
        return db.rawQuery("select * from Glucemias where Fecha='"+fecha+"' and Periodo='"+periodo+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una glucemia por Hora y periodo
     */
    public Cursor selectGlucemiaHora(String fecha, String periodo){
        return db.rawQuery("select * from Glucemias where Fecha='"+fecha+"' and Periodo='"+periodo+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una glucemia por fecha y valor
     */
    public Cursor selectGlucemiaValor(String fecha, int periodo){
        return db.rawQuery("select * from Glucemias where Fecha LIKE'%"+fecha+"%' and Periodo='"+periodo+"'",null);
    }
    /**
     * Función que devuelve el resultado de una consulta de una incidencia por el id de la glucemia
     * a la que esta asociada
     */
    public Cursor selectIncidencia(int idGlucemia){
        return db.rawQuery("select * from Incidencias where Glucemia='"+idGlucemia+"'",null);
    }
    public void closeBD(){
        db.close();
    }
}
