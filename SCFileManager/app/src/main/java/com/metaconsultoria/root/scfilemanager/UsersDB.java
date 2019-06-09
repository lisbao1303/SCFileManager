package com.metaconsultoria.root.scfilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class UsersDB extends SQLiteOpenHelper {
    public static final String NOME_DO_BANCO = "bancoDeUsuarios.sqlite";
    private static final int VERSAO_DO_BANCO = 1;
    private Context cont;

    UsersDB(Context contx){
            super(contx,NOME_DO_BANCO,null, VERSAO_DO_BANCO);
            this.cont=contx;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("sql","criando tabela");
        db.execSQL(
                "create table if not exists funcionarios (id integer primary key autoincrement,nome text,matricula text);"
        );
        Log.d("sql","criou tabela");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

    }
    public long save(Funcionario funcionario){
        SQLiteDatabase db = getWritableDatabase();
        try{
        ContentValues values = new ContentValues();
        values.put("nome",funcionario.getNome());
        values.put("matricula",funcionario.getMatricula());
        return db.insert("funcionarios","",values);
        }finally{
            db.close();
        }
    }
    public Funcionario findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor c = db.query("funcionarios",null,null,null,null,null,null);
            if(c.getCount()>0){
                 Funcionario fx= new Funcionario();
                 c.moveToFirst();
                 fx.setNome(c.getString(c.getColumnIndex("nome")));
                 fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                 return fx;
            }else{
                 return null;
            }
        }
        finally{
            db.close();
        }
    }
    public void execSQL(String sql){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

}
