package com.evoluum.desafio.service;

import com.evoluum.desafio.config.interceptor.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.net.URI;
import java.util.function.Supplier;

@Service
public class IbgeProxyService {

    private static final String URL_MUNICIPIOS = "/estados/{id}/municipios";
    private static final String URL_ESTADOS = "/estados";

    private final RestTemplate restTemplate;
    private final UriTemplateHandler uriTemplate;

    @Autowired
    public IbgeProxyService(RestTemplateBuilder restBuilder,
                            @Value("${ibge.ws.base-url}") String baseUrl) {
        this.restTemplate = restBuilder
                .requestFactory((Supplier<ClientHttpRequestFactory>) new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .rootUri(baseUrl)
                .build();
        this.uriTemplate = restTemplate.getUriTemplateHandler();

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.getInterceptors().add(new RequestResponseLoggingInterceptor());
    }

    private <T> T doRequest(RequestEntity<?> requestEntity, Class<T> responseType) {
        // Busca dado do serviço solicitado
        HttpStatus statusCode;
        try {
            ResponseEntity<T> response = restTemplate.exchange(requestEntity, responseType);
            statusCode = response.getStatusCode();

            if (statusCode.is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            statusCode = e.getStatusCode();
        }

        throw new RuntimeException("Erro no serviço: URI=" + requestEntity.getUrl() + " status=" + statusCode);
    }

/*    public ProductBilletDTO billetInfo(String onix) {

        final URI uri = uriTemplate.expand(URL_PRODDUCT_BILLET_INFO, onix);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();

        return doRequest(requestEntity, ProductBilletDTO.class);
    }*/

}
