package com.metaconsultoria.root.scfilemanager;

public class Funcionario {
      public long id;
      private  String nome;
      private  String matricula;
    Funcionario(){
            id=0;
    }
      Funcionario(String Nome,String Matricula){
          this.nome=Nome;
          this.matricula=Matricula;
          id=0;
    }

      public void setNome(String Nome ){
          this.nome=Nome;
      }

      public void setMatricula(String Matricula){
        this.matricula=Matricula;
      }

      public String getNome(){
          return this.nome;
      }

      public String getMatricula(){
          return this.matricula;
      }
}

