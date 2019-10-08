package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.views.EstadoResponse;
import com.evoluum.desafio.service.EstadoProxyService;
import com.evoluum.desafio.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class EstadoController {
    private static final Logger LOGGER = Logger.getLogger(EstadoController.class.getName());

    private final EstadoProxyService estadoProxyService;

    @Autowired
    public EstadoController(EstadoProxyService estadoProxyService) {
        this.estadoProxyService = estadoProxyService;
    }

    @GetMapping("api/localidades/estados")
    public ResponseEntity<List<EstadoResponse>> findAll() {
        return new ResponseEntity<>(estadoProxyService.findStatesAsResponse(), HttpStatus.OK);
    }

    @GetMapping("api/localidades/estados/download")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        try {
            String csvFileName = Utils.formatResponse(response, "estados");
            estadoProxyService.generateCsv(csvFileName, estadoProxyService.findAll(), response);
        } catch (Exception e){
            throw e;
        }
    }

}
