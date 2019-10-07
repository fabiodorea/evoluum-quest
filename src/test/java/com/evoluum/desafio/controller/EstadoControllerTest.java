package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.views.EstadoResponse;
import com.evoluum.desafio.service.EstadoProxyService;
import com.evoluum.desafio.util.MockUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EstadoControllerTest {

    @Mock
    private EstadoProxyService estadoProxyService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        EstadoController estadoController = new EstadoController(estadoProxyService);
        mockMvc = MockMvcBuilders.standaloneSetup(estadoController).build();
    }

    @Test
    public void shouldReturnEstates() throws Exception {
        List<EstadoResponse> estados = MockUtils.findAllStates();

        when(estadoProxyService.getUfsAsResponse())
                .thenReturn(estados);

        mockMvc.perform(
                get("/api/localidades/estados")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

}
