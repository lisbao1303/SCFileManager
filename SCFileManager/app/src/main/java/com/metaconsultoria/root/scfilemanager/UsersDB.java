package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDB extends SQLiteOpenHelper {
    public static final String NOME_DO_BANCO = "bancoDeUsuarios.sqlite";
    private static final int VERSAO_DO_BANCO = 1;

     public UsersDB(Context cont){
            super(cont,NOME_DO_BANCO,null, VERSAO_DO_BANCO);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists carro (Id Integer primary key autoincrement,nome text,matricula text);"
        );
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

    }


}
