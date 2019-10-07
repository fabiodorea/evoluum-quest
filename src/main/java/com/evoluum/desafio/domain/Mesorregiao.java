package com.evoluum.desafio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Mesorregiao implements Serializable {
    private int id;
    private String nome;
    @JsonProperty(value = "UF")
    private Estado UF;

    public Mesorregiao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getUF() {
        return UF;
    }

    public void setUF(Estado UF) {
        this.UF = UF;
    }

    @Override
    public String toString() {
        return "Mesorregiao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", UF=" + UF +
                '}';
    }
}
