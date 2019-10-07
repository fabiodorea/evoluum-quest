package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.util.MockUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryState;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(MockitoJUnitRunner.class)
public class EstadoServiceTest {

    @Mock
    private IbgeProxyService ibgeProxyService;

    @Mock
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private RetryTemplate retryTemplate;

    @InjectMocks
    private EstadoProxyService estadoProxyService;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnAllStates() throws IOException, URISyntaxException {
        List<Estado> estados = MockUtils.findAllStatesFromIbge();

        when(restTemplate.exchange(any(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<String>(HttpStatus.OK));

        when(retryTemplate.execute(
                any(RetryCallback.class),
                any(RecoveryCallback.class),
                any(RetryState.class)))
                .thenAnswer(invocation -> {
                    RetryCallback retry = invocation.getArgument(0);
                    return retry.doWithRetry(/*here goes RetryContext but it's ignored in ServiceRequest*/null);
                });

        when(ibgeProxyService.findAllStates())
                .thenReturn(estados);

        List<Estado> estadosAfter = estadoProxyService.findAll();
        System.out.println(estados);
        System.out.println(estadosAfter);
        assertThat(estados.size()).isSameAs(estadosAfter.size());
    }

}
