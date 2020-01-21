package com.metaconsultoria.root.scfilemanager;

public class Funcionario implements Comparable<Funcionario>{
    public final long id;
    private String Nome;
    private String Matricula;
    private String Restauracao;
    private String Senha;

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

    @Override
    public int compareTo(Funcionario o) {
        return this.getNome().compareTo(o.getNome());
    }
}
