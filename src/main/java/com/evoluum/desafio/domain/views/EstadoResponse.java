package com.evoluum.desafio.domain.views;

import com.evoluum.desafio.domain.Estado;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class EstadoResponse implements Serializable {
    private int idEstado;
    private String siglaEstado;
    private String regiaoNome;

    public EstadoResponse() {
    }

    public static EstadoResponse fromEntity(Estado e) {
        EstadoResponse response = new EstadoResponse();
        response.setIdEstado(e.getId());
        response.setSiglaEstado(e.getSigla());
        response.setRegiaoNome(e.getRegiao().getNome());
        return response;
    }

    public EstadoResponse(int idEstado, String siglaEstado, String regiaoNome) {
        this.idEstado = idEstado;
        this.siglaEstado = siglaEstado;
        this.regiaoNome = regiaoNome;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public String getRegiaoNome() {
        return regiaoNome;
    }

    public void setRegiaoNome(String regiaoNome) {
        this.regiaoNome = regiaoNome;
    }
}
