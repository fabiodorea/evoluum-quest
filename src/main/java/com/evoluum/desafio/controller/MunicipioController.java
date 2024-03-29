package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.views.LocalidadeResponse;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.service.MunicipioProxyService;
import com.evoluum.desafio.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class MunicipioController {
    private static final Logger LOGGER = Logger.getLogger(EstadoController.class.getName());

    private final MunicipioProxyService municipioProxyService;

    @Autowired
    public MunicipioController(MunicipioProxyService municipioProxyService) {
        this.municipioProxyService = municipioProxyService;
    }

    @GetMapping("api/localidades/estados/{ids}/municipios")
    public ResponseEntity<List<MunicipioResponse>> findByUf(@PathVariable("ids") String ids) {
        return new ResponseEntity<>(municipioProxyService.findByUfAsResponse(ids), HttpStatus.OK);
    }

    @GetMapping("api/localidades/municipios/{name}")
    public ResponseEntity<LocalidadeResponse> findByName(@PathVariable("name") String name) throws Exception {
        LocalidadeResponse response = new LocalidadeResponse(municipioProxyService.findByName(name).getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("api/localidades/estados/{ids}/municipios/download")
    public void downloadCsv(HttpServletResponse response, @PathVariable("ids") String ids) throws IOException {
        try {
            String csvFileName = Utils.formatResponse(response, "cidades");
            municipioProxyService.generateCsv(csvFileName, municipioProxyService.findByUfIds(ids), response);
        } catch (Exception e){
            throw e;
        }
    }
}
