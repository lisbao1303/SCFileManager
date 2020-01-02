package com.metaconsultoria.root.scfilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;


public class RacentFilesDB extends SQLiteOpenHelper {
    public static final String NOME_DO_BANCO = "bancoDeUsuarios.sqlite";
    private static final int VERSAO_DO_BANCO = 1;
    private Context cont;

    RacentFilesDB(Context contx){
            super(contx,NOME_DO_BANCO,null, VERSAO_DO_BANCO);
            this.cont=contx;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists funcionarios (id integer primary key autoincrement,nome text,matricula text,senha text);"
        );
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

    }


    public long init(Funcionario funcionario){
        long id = funcionario.id;
        SQLiteDatabase db = getWritableDatabase();
        try{


            ContentValues values = new ContentValues();
            values.put("nome",funcionario.getNome());
            values.put("matricula",funcionario.getMatricula());
            values.put("senha",funcionario.getSenha());
            if(id==0) {
                return db.insert("funcionarios", "", values);
            }else {
                String _id = String.valueOf(funcionario.id);
                String[] whereArgs = new String[]{_id};
                return db.update("funcionarios",values,"_id",whereArgs);
            }
        }finally{
            db.close();
        }
    }

    public long save(Funcionario funcionario){
        long id = funcionario.id;
        SQLiteDatabase db = getWritableDatabase();
        try{

        Funcionario outra_coisa = findByMatricula(funcionario.getMatricula(),db);
        if(funcionario.getMatricula().equals(outra_coisa.getMatricula())){
            return 0;
        }

        ContentValues values = new ContentValues();
        values.put("nome",funcionario.getNome());
        values.put("matricula",funcionario.getMatricula());
        values.put("senha",funcionario.getSenha());
        if(id==0) {
            return db.insert("funcionarios", "", values);
        }else {
            String _id = String.valueOf(funcionario.id);
            String[] whereArgs = new String[]{_id};
            return db.update("funcionarios",values,"_id",whereArgs);
        }
        }finally{
            db.close();
        }
    }


    public List<Funcionario>  findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor c = db.query("funcionarios",null,null,null,null,null,null);
            if(c.getCount()>0){
                 List<Funcionario> fx_vetor= new ArrayList<Funcionario>();
                 if(c.moveToFirst()){
                    do {
                        Funcionario fx=new Funcionario();
                        fx.setNome(c.getString(c.getColumnIndex("nome")));
                        fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                        fx.setSenha(c.getString(c.getColumnIndex("senha")));
                        fx_vetor.add(fx);
                    } while(c.moveToNext());
                 }
                 return fx_vetor;
            }else{
                 return null;
            }
        }finally{
            db.close();
        }
    }


    public Funcionario findByMatricula(String matricula){
        SQLiteDatabase db= getWritableDatabase();
        try{
            Cursor c= db.query("funcionarios",null,"matricula='"+matricula+"'",null,null,null,null);
            if(c.getCount()>0){
                Funcionario fx= new Funcionario();
                c.moveToFirst();
                fx.setNome(c.getString(c.getColumnIndex("nome")));
                fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                fx.setSenha(c.getString(c.getColumnIndex("senha")));
                return fx;
            }else{
                return null;
            }
        }finally{
            db.close();
        }
    }

    public Funcionario findByMatricula(String matricula,SQLiteDatabase db){

        try{
            Cursor c= db.query("funcionarios",null,"matricula='"+matricula+"'",null,null,null,null);
            if(c.getCount()>0){
                Funcionario fx= new Funcionario();
                c.moveToFirst();
                fx.setNome(c.getString(c.getColumnIndex("nome")));
                fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                fx.setSenha(c.getString(c.getColumnIndex("senha")));
                return fx;
            }else{
                return null;
            }
        }finally{
            db.close();
        }
    }


    public int deleteByMatricula(String matricula){
        SQLiteDatabase db= getWritableDatabase();
        try{
            int count = db.delete("funcionarios","matricula=?",new String[]{matricula});
            return count;
        } finally{
            db.close();
        }
    }

    public int deleteAll(){
        SQLiteDatabase db= getWritableDatabase();
        try{
            int count = db.delete("funcionarios","",null);
            return count;
        } finally{
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
