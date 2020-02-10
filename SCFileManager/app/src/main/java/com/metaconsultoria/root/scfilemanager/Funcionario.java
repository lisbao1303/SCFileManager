package com.metaconsultoria.root.scfilemanager;

public class Funcionario implements Comparable<Funcionario>{
    public final long id;
    private String Nome;
    private String Matricula;
    private String Restauracao;
    private String Senha;
    private String GeneratedBy;

    public Funcionario(){
        this.id=-1;
    }

    public Funcionario(long id){
        this.id=id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }

    public String getRestauracao() {
        return Restauracao;
    }

    public void setRestauracao(String restauracao) {
        Restauracao = restauracao;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public boolean isSaveble() {
        if (this.getMatricula() != null && this.getSenha() != null) {
            return true;
        }else{
            return false;
        }
    }

    public String getGeneratedBy() {
        return GeneratedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        GeneratedBy = generatedBy;
    }

    public String generate(){
        return this.getMatricula()+","+getGeneratedBy();
    }

    public String getFirstName(){
        int index=getNome().indexOf(' ');
        if(index>0){
            return getNome().substring(0,index);
        }
        else{
            return getNome();
        }
    }

    @Override
    public int compareTo(Funcionario o) {
        return this.getNome().compareTo(o.getNome());
    }
}
