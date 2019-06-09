package com.metaconsultoria.root.scfilemanager;

public class Funcionario {
      public long id;
      private  String nome;
      private  String matricula;
      private  String senha;
      private  boolean is_elevated=false;

      Funcionario(){
            id=0;
      }

      Funcionario(String Nome,String Matricula){
          this.nome=Nome;
          this.matricula=Matricula;
          id=0;
      }

      Funcionario(String Nome,String Matricula, String senha){
          this.nome=Nome;
          this.matricula=Matricula;
          this.senha=senha;
          id=0;
      }


      Funcionario(String Nome,String Matricula, String senha, boolean is_elevated){
        this.nome=Nome;
        this.matricula=Matricula;
        this.senha=senha;
        this.is_elevated=is_elevated;
        id=0;
      }

      public void setNome(String Nome){
          this.nome=Nome;
      }

      public void setMatricula(String Matricula){
        this.matricula=Matricula;
      }

      public void setSenha(String senha){
        this.senha=senha;
    }

      public void setIsElevated(boolean is_elevated){
        this.is_elevated=is_elevated;
    }

      public String getNome(){
          return this.nome;
      }

      public String getMatricula(){
          return this.matricula;
      }

      public String getSenha(){
        return this.senha;
    }

      public boolean getIsElevated(){
        return this.is_elevated;
    }

}

