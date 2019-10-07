package com.evoluum.desafio.service;

import com.evoluum.desafio.config.interceptor.RequestResponseLoggingInterceptor;
import com.evoluum.desafio.domain.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class IbgeProxyService {

    private static final String URL_ESTADOS = "/estados";

    private final RestTemplate restTemplate;
    private final UriTemplateHandler uriTemplate;
    private final Path workDir;

    @Autowired
    public IbgeProxyService(RestTemplateBuilder restBuilder,
                            @Value("${ibge.ws.base-url}") String baseUrl) throws IOException {
        this.restTemplate = restBuilder
                .rootUri(baseUrl)
                .build();
        this.uriTemplate = restTemplate.getUriTemplateHandler();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
        restTemplate.getInterceptors().add(new RequestResponseLoggingInterceptor());
        restTemplate.setMessageConverters(configureMessageConverter());
        this.workDir = Paths.get("csv/");
        Files.createDirectories(this.workDir);
    }

    private List<HttpMessageConverter<?>> configureMessageConverter(){
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        return messageConverters;
    }

    public List<Estado> findAllStates(){
        final URI uri = uriTemplate.expand(URL_ESTADOS);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return doRequest(requestEntity, new ParameterizedTypeReference<List<Estado>>() {});
    }

    public <T> T doRequest(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
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

    public Path getWorkDir() {
        return workDir;
    }
}
