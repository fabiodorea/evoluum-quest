package com.evoluum.desafio.domain.views;

import com.evoluum.desafio.domain.Municipio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MunicipioResponse implements Serializable {
    private String nomeCidade;
    private String nomeMesorregiao;
    private String nomeFormatado;

    public MunicipioResponse() {
    }

    public static MunicipioResponse fromEntity(Municipio m){
        return new MunicipioResponse(m.getNome(), m.getMicrorregiao().getMesorregiao().getNome(), m.getNomeFormatado());
    }

    public MunicipioResponse(String nomeCidade, String nomeMesorregiao, String nomeFormatado) {
        this.nomeCidade = nomeCidade;
        this.nomeMesorregiao = nomeMesorregiao;
        this.nomeFormatado = nomeFormatado;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public String getNomeMesorregiao() {
        return nomeMesorregiao;
    }

    public void setNomeMesorregiao(String nomeMesorregiao) {
        this.nomeMesorregiao = nomeMesorregiao;
    }

    public String getNomeFormatado() {
        return nomeFormatado;
    }

    public void setNomeFormatado(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }
}
