package com.evoluum.desafio;

import com.evoluum.desafio.controller.UfController;
import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.service.UfProxyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class EstadosTest {

    @Mock
    private UfProxyService ufProxyService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        UfController ufController = new UfController(ufProxyService);
        mockMvc = MockMvcBuilders.standaloneSetup(ufController).build();
    }

    @Test
    public void shouldReturnEstates() throws Exception {
        List<Estado> estados = mockEstados();

        when(ufProxyService.getUfs())
                .thenReturn(estados);

        mockMvc.perform(
                get("/api/localidades/estados")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    private List mockEstados() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("estates.json"), List.class);
    }

}
