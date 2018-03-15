package com.ubu.lmi.gii170j.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LuisMiguel on 15/03/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dataBase.sqlite";
    private static final int DB_SCHEMA_VERSION = 1;



    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DB_SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_ALIMENTOS);
        db.execSQL(DataBaseManager.CREATE_GLUCEMIAS);
        db.execSQL(DataBaseManager.CREATE_INCIDENCIAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
