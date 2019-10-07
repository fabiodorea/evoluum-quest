package com.evoluum.desafio.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Municipio implements Serializable {
    private int id;
    private String nome;
    private Microrregiao microrregiao;

    public Municipio() {
    }

    public Municipio(int id) {
        this.id = id;
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

    public Microrregiao getMicrorregiao() {
        return microrregiao;
    }

    public void setMicrorregiao(Microrregiao microrregiao) {
        this.microrregiao = microrregiao;
    }

    public String getNomeFormatado() {
        return nome + "/" + microrregiao.getMesorregiao().getUF().getSigla();
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", microrregiao=" + microrregiao.toString() +
                '}';
    }
}
