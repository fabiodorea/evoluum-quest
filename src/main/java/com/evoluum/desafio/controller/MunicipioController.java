package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.service.MunicipioProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class MunicipioController {
    private static final Logger LOGGER = Logger.getLogger(UfController.class.getName());

    private final MunicipioProxyService municipioProxyService;

    @Autowired
    public MunicipioController(MunicipioProxyService municipioProxyService) {
        this.municipioProxyService = municipioProxyService;
    }

    @GetMapping("api/localidades/estados/{ids}/municipios")
    public ResponseEntity<List<Municipio>> getMunicipiosByUf(@PathVariable("ids") String ids) {
        try {
            return new ResponseEntity<>(municipioProxyService.getMunicipiosByUf(ids), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
}
