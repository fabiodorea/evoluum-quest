package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.domain.views.EstadoResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EstadoServiceTest {

    @Mock
    private IbgeProxyService ibgeProxyService;

    @InjectMocks
    private EstadoProxyService estadoProxyService;

    @Test
    public void shouldReturnAllStates() throws IOException, URISyntaxException {
        List<Estado> estados = MockUtils.findAllStatesFromIbge();

        when(ibgeProxyService.findAllStates())
                .thenReturn(estados);

        List<Estado> estadosAfter = estadoProxyService.findAll();

        assertThat(estados.size()).isSameAs(estadosAfter.size());
    }

    @Test
    public void shouldReturnAllStatesAsResponse() throws IOException, URISyntaxException {
        List<Estado> estadosFromIbge = MockUtils.findAllStatesFromIbge();
        List<EstadoResponse> estados = MockUtils.findAllStates();

        when(ibgeProxyService.findAllStates())
                .thenReturn(estadosFromIbge);

        List<EstadoResponse> estadosAfter = estadoProxyService.findStatesAsResponse();

        assertThat(estados.size()).isSameAs(estadosAfter.size());
    }

}
