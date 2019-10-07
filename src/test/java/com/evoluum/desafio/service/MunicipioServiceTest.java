package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.util.MockUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MunicipioServiceTest {

    @Mock
    private IbgeProxyService ibgeProxyService;

    @InjectMocks
    private MunicipioProxyService municipioProxyService;

    @Test
    public void shoulReturnAllCountys() throws IOException, URISyntaxException {
        List<Municipio> cidades = MockUtils.findAllCountys();

        when(ibgeProxyService.findAllCountys()).thenReturn(cidades);

        List<Municipio> municipios = municipioProxyService.findAll();

        assertThat(cidades.size()).isEqualTo(municipios.size());
    }

    @Test
    public void shoulReturnCountyByStateIds() throws IOException, URISyntaxException {
        List<Municipio> cidades = MockUtils.findCountysByUf_28_Before();

        when(ibgeProxyService.findCountyByStateId("28")).thenReturn(cidades);

        List<Municipio> municipios = municipioProxyService.findByUfIds("28");

        assertThat(cidades.size()).isEqualTo(municipios.size());
    }

    @Test
    public void shoulReturnCountyByName() throws Exception {
        List<Municipio> cidades = MockUtils.findAllCountys();


        when(ibgeProxyService.findAllCountys()).thenReturn(cidades);

        Municipio municipio = municipioProxyService.findByName("Aracaju");

        assertThat(municipio.getId()).isEqualTo(2800308);
    }

}
