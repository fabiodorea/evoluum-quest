package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.views.EstadoResponse;
import com.evoluum.desafio.service.EstadoProxyService;
import com.evoluum.desafio.util.MockUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
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

        when(estadoProxyService.findStatesAsResponse())
                .thenReturn(estados);

        mockMvc.perform(
                get("/api/localidades/estados")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testDownloadFile() throws Exception {
        Mockito.when(estadoProxyService.findStatesAsResponse()).thenReturn(new ArrayList<>());
        estadoProxyService.generateCsv("fileName", new ArrayList<>(), null);

        MvcResult result = mockMvc.perform(
                get("/api/localidades/estados/download")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertEquals("text/csv", result.getResponse().getContentType());
    }

}
