package com.evoluum.desafio.domain;

import java.io.Serializable;

public class Regiao implements Serializable {

    private int id;
    private char sigla;
    private String nome;

    public Regiao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getSigla() {
        return sigla;
    }

    public void setSigla(char sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
