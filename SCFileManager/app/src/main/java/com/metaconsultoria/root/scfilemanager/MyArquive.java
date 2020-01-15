package com.metaconsultoria.root.scfilemanager;

import java.sql.Array;

public class MyArquive implements Comparable<MyArquive> {
      public long id;
      private  String nome;
      private  String path;
      private  String lastuse;
      private  boolean isStared;

      MyArquive(){
            this.id=0;
            this.lastuse="0";
      }

      MyArquive(int Id){
          this.id=id;
          this.lastuse="0";
      }

      MyArquive(String Nome, String path){
          this.nome=Nome;
          this.path=path;
          this.lastuse="0";
          id=0;
      }


      MyArquive(String nome, String path, String lastuse){
        this.nome=nome;
        this.path=path;
        this.lastuse=lastuse;
        id=0;
      }

    MyArquive(String nome, String path, String lastuse, boolean isStared){
        this.nome=nome;
        this.path=path;
        this.lastuse=lastuse;
        this.isStared=isStared;
        id=0;
    }




      public void setNome(String nome){
          this.nome=nome;
      }

      public void setPath(String path){
        this.path=path;
    }

      public void setLastuse(String lastuse){
        this.lastuse=lastuse;
    }

      public void setStared(boolean stared) {
        this.isStared = stared;
    }

      public String getNome(){
          return this.nome;
      }

      public String getPath(){
        return this.path;
    }

      public String getLastUse(){
        return this.lastuse;
    }

      public boolean getStared(){return isStared;}

    public static int getComparableLastUseLRU(String lastuse) throws IllegalArgumentException{
          char[] array = lastuse.toCharArray();
          for(int i=0;i<array.length-1;i++){
              if(array[i]=='1') { return i;}
              if(array[i]!='0' && array[i]!='1'){throw new IllegalArgumentException("String com valores nao binarios");}
          }
          return 0;
      }

    public static int getComparableLastUseMRU(String lastuse){
        int soma=0;
        char[] array = lastuse.toCharArray();
        for(int i=0;i<array.length-1;i++){
            if(array[i]=='1') {soma++;}
            if(array[i]!='0' && array[i]!='1'){throw new IllegalArgumentException("String com valores nao binarios");}
        }
        return soma;
    }

    @Override
    public int compareTo(MyArquive o) {
          return MyArquive.getComparableLastUseLRU(this.getLastUse())-MyArquive.getComparableLastUseLRU(o.getLastUse());
    }
}

