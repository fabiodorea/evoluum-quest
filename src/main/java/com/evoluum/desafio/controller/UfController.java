package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.service.ProxyService;
import com.evoluum.desafio.service.UfProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class UfController {
    private static final Logger LOGGER = Logger.getLogger(UfController.class.getName());

    private final UfProxyService ufProxyService;

    @Autowired
    public UfController(UfProxyService ufProxyService) {
        this.ufProxyService = ufProxyService;
    }

    @GetMapping("api/localidades/estados")
    public ResponseEntity<List<Estado>> getUfsFromIbge() {
        try {
            return new ResponseEntity<>(ufProxyService.getUfs(), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

}
