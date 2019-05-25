package com.example.root.recomeco;

public class Oportunidade {
    private String titulo;
    private String ofertante;
    Oportunidade(String titulo, String ofertante){
        this.titulo=titulo;
        this.ofertante=ofertante;
    }

    public String getOfertante() {
        return ofertante;
    }

    public void setOfertante(String ofertante) {
        this.ofertante = ofertante;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
