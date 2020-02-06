package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;

public class MyFileDirectory {
    private  String nome;
    private  String path;

    MyFileDirectory(){}

    MyFileDirectory(String path){
        this.path=path;
        this.nome=path.substring(path.lastIndexOf('/')+1);
    }

    MyFileDirectory(String Nome, String path){
        this.nome=Nome;
        this.path=path;
    }


    public void setNome(String nome){
        this.nome=nome;
    }

    public void setPath(String path){
        this.path=path;
    }

    public String getNome(){
        return this.nome;
    }

    public String getPath(){
        return this.path;
    }

    public String getData_Hr(){
        String m_path = Uri.parse(this.getPath()).getPath();
        File m_file = new File(m_path);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(m_file.lastModified());
    }

    public String getData(){
        String m_path = Uri.parse(this.getPath()).getPath();
        File  m_file = new File(m_path);
        return new SimpleDateFormat("dd/MM/yyyy").format(m_file.lastModified());
    }

    public String getHora(){
        String m_path = Uri.parse(this.getPath()).getPath();
        File  m_file = new File(m_path);
        return new SimpleDateFormat("HH:mm:ss").format(m_file.lastModified());
    }

}
