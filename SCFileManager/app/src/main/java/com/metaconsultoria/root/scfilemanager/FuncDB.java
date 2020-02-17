package com.metaconsultoria.root.scfilemanager;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncDB extends SQLiteOpenHelper {
    public static final String NOME_DO_BANCO = "elevated.sqlite";
    private static final String TABLE_OF_FUNC_NAME ="elevated_func";
    private static final String TABLE_OF_KEY_VALUES ="chave_valor";
    private static final int VERSAO_DO_BANCO = 1;
    private Context cont;

    FuncDB(Context contx) {
        super(contx, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
        this.cont = contx;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists "+FuncDB.TABLE_OF_FUNC_NAME+" (id integer primary key autoincrement," +
                        "nome text," +
                        "matricula text not null," +
                        "restauracao text," +
                        "senha text not null," +
                        "generated_by text);"
        );
        db.execSQL(
                "create table if not exists "+FuncDB.TABLE_OF_KEY_VALUES+" (chave text, valor text);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long save(Funcionario func) {
        if(!func.isSaveble()){return -1;}
        Funcionario pesquisa = findByMatricula(func.getMatricula());
        if (pesquisa != null) {
            if (func.getMatricula().equals(pesquisa.getMatricula())) {
                return pesquisa.id;
            }
        }
        ContentValues values = new ContentValues();
        values.put("nome", func.getNome());
        values.put("matricula", func.getMatricula());
        values.put("restauracao", func.getRestauracao());
        values.put("senha", func.getSenha());

        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.insert(FuncDB.TABLE_OF_FUNC_NAME, "", values);
        } finally {
            db.close();
        }
    }

    public long save(Funcionario func, Funcionario generator) {
        if(!func.isSaveble()){return -1;}
        Funcionario pesquisa = findByMatricula(func.getMatricula());
        if (pesquisa != null) {
            if (func.getMatricula().equals(pesquisa.getMatricula())) {
                return pesquisa.id;
            }
        }
        ContentValues values = new ContentValues();
        values.put("nome", func.getNome());
        values.put("matricula", func.getMatricula());
        values.put("restauracao", func.getRestauracao());
        values.put("senha", func.getSenha());
        values.put("generated_by",generator.generate());

        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.insert(FuncDB.TABLE_OF_FUNC_NAME, "", values);
        } finally {
            db.close();
        }
    }

    public List<Funcionario> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query(FuncDB.TABLE_OF_FUNC_NAME, null, null, null, null, null, null);
            if (c.getCount() > 0) {
                List<Funcionario> fx_vetor = new ArrayList<Funcionario>();
                if (c.moveToFirst()) {
                    do {
                        Funcionario fx = new Funcionario(c.getLong(c.getColumnIndex("id")));
                        fx.setNome(c.getString(c.getColumnIndex("nome")));
                        fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                        fx.setRestauracao(c.getString(c.getColumnIndex("restauracao")));
                        fx.setSenha(c.getString(c.getColumnIndex("senha")));
                        fx.setGeneratedBy(c.getString(c.getColumnIndex("generated_by")));
                        fx_vetor.add(fx);
                    } while (c.moveToNext());
                }
                Collections.sort(fx_vetor);
                return fx_vetor;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public Funcionario findByMatricula(String matricula) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query(FuncDB.TABLE_OF_FUNC_NAME, null, "matricula='" + matricula + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                Funcionario fx = new Funcionario(c.getLong(c.getColumnIndex("id")));
                fx.setNome(c.getString(c.getColumnIndex("nome")));
                fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                fx.setRestauracao(c.getString(c.getColumnIndex("restauracao")));
                fx.setSenha(c.getString(c.getColumnIndex("senha")));
                fx.setGeneratedBy(c.getString(c.getColumnIndex("generated_by")));
                return fx;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public List<Funcionario> findGeneratedBy(String matricula) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query(FuncDB.TABLE_OF_FUNC_NAME, null, "generated_by like '%" +matricula+ "%'", null, null, null, null);
            if (c.getCount() > 0) {
                List<Funcionario> fx_vetor = new ArrayList<Funcionario>();
                if (c.moveToFirst()) {
                    do {
                        Funcionario fx = new Funcionario(c.getLong(c.getColumnIndex("id")));
                        fx.setNome(c.getString(c.getColumnIndex("nome")));
                        fx.setMatricula(c.getString(c.getColumnIndex("matricula")));
                        fx.setRestauracao(c.getString(c.getColumnIndex("restauracao")));
                        fx.setSenha(c.getString(c.getColumnIndex("senha")));
                        fx.setGeneratedBy(c.getString(c.getColumnIndex("generated_by")));
                        fx_vetor.add(fx);
                    } while (c.moveToNext());
                }
                Collections.sort(fx_vetor);
                return fx_vetor;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public int deleteByMatricula(String matricula) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(FuncDB.TABLE_OF_FUNC_NAME, "matricula=?", new String[]{matricula});
        } finally {
            db.close();
        }
    }

    public int deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(FuncDB.TABLE_OF_FUNC_NAME, "", null);
        } finally {
            db.close();
        }
    }

    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    public int updateElementRowByMatricula(String colunm,String  new_value, String matricula) {
        if(this.findByMatricula(matricula)==null){return -1;}
        ContentValues values = new ContentValues();
        values.put(colunm, new_value);
        String selection = "matricula" + " LIKE ?";
        String[] selectionArgs = {matricula};
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.update(
                    FuncDB.TABLE_OF_FUNC_NAME,
                    values,
                    selection,
                    selectionArgs);
        }finally {
            db.close();
        }
    }

    public long updateRowByMatricula(Funcionario func){
        if(!func.isSaveble()){return -1;}
        if(this.findByMatricula(func.getMatricula())==null){return save(func);}
        ContentValues values = new ContentValues();
        values.put("nome", func.getNome());
        values.put("restauracao", func.getRestauracao());
        values.put("senha", func.getSenha());
        values.put("generated_by",func.generate());

        String selection = "matricula" + " LIKE ?";
        String[] selectionArgs = {func.getMatricula()};
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.update(
                    FuncDB.TABLE_OF_FUNC_NAME,
                    values,
                    selection,
                    selectionArgs);
        }finally {
            db.close();
        }
    }

    public long saveChave(String chave,String valor){
        ContentValues values = new ContentValues();
        values.put("chave", chave);
        values.put("valor", valor);

        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.insert(FuncDB.TABLE_OF_KEY_VALUES, "", values);
        } finally {
            db.close();
        }
    }

    public String getValor(String chave) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query(FuncDB.TABLE_OF_KEY_VALUES, null, "chave='" + chave + "'", null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                return c.getString(c.getColumnIndex("valor"));
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public int updateValue(String  chave, String valor) {
        if(this.getValor(chave)==null){return -1;}
        ContentValues values = new ContentValues();
        values.put("valor", valor);
        String selection = "chave" + " LIKE ?";
        String[] selectionArgs = {chave};
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.update(
                    FuncDB.TABLE_OF_KEY_VALUES,
                    values,
                    selection,
                    selectionArgs);
        }finally {
            db.close();
        }
    }

    public int deleteChave(String chave) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(FuncDB.TABLE_OF_KEY_VALUES, "chave=?", new String[]{chave});
        } finally {
            db.close();
        }
    }

    public int deleteAllChaves() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(FuncDB.TABLE_OF_KEY_VALUES, "", null);
        } finally {
            db.close();
        }
    }


}

