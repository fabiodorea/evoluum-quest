package com.evoluum.desafio.controller;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.service.MunicipioProxyService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MunicipioControllerTest {
    @Mock
    private MunicipioProxyService municipioProxyService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MunicipioController municipioController = new MunicipioController(municipioProxyService);
        mockMvc = MockMvcBuilders.standaloneSetup(municipioController).build();
    }

    @Test
    public void shouldReturnCountys() throws Exception {

        List<MunicipioResponse> municipios = MockUtils.findCountysByUf_28_After();

        when(municipioProxyService.findByUfAsResponse("28"))
                .thenReturn(municipios);

        mockMvc.perform(
                get("/api/localidades/estados/28/municipios")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void shouldReturnCountyByName() throws Exception {

        Municipio municipio = new Municipio(2800308);

        when(municipioProxyService.findByName("Aracaju"))
                .thenReturn(municipio);

        mockMvc.perform(
                get("/api/localidades/municipios/{name}", "Aracaju")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2800308)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testDownloadFile() throws Exception {
        Mockito.when(municipioProxyService.findByUfIds("28")).thenReturn(new ArrayList<>());
        municipioProxyService.generateCsv("fileName", new ArrayList<>(), null);

        MvcResult result = mockMvc.perform(
                get("/api/localidades/estados/{ids}/municipios/download", "28")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertEquals("text/csv", result.getResponse().getContentType());
    }
}
