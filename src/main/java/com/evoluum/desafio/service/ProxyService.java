package com.evoluum.desafio.service;

import com.evoluum.desafio.config.interceptor.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

@Component
public class ProxyService {

    private final RestTemplate restTemplate;
    private final UriTemplateHandler uriTemplate;

    @Autowired
    public ProxyService(RestTemplateBuilder restBuilder,
                        @Value("${ibge.ws.base-url}") String baseUrl) {
        this.restTemplate = restBuilder
                .rootUri(baseUrl)
                .build();
        this.uriTemplate = restTemplate.getUriTemplateHandler();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
        restTemplate.getInterceptors().add(new RequestResponseLoggingInterceptor());
    }

    public <T> T doRequest(RequestEntity<?> requestEntity, Class<T> responseType) {
        // Busca dados do serviço solicitado
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

    public UriTemplateHandler getUriTemplate() {
        return uriTemplate;
    }
}
