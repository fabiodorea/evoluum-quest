package com.evoluum.desafio.service;

import com.evoluum.desafio.config.interceptor.RequestResponseLoggingInterceptor;
import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.domain.Municipio;
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
import org.springframework.stereotype.Service;
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
import java.util.Objects;

@Service
public class IbgeProxyService {

    private static final String URL_ESTADOS = "/estados";
    private static final String URL_MUNICIPIOS_BY_UF = "/estados/{id}/municipios";
    private static final String URL_MUNICIPIOS = "/municipios";

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

    public List<Estado> findAllStates(){
        final URI uri = uriTemplate.expand(URL_ESTADOS);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return doRequest(requestEntity, new ParameterizedTypeReference<List<Estado>>() {});
    }

    public List<Municipio> findAllCountys() {
        final URI uri = uriTemplate.expand(URL_MUNICIPIOS);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return doRequest(requestEntity, new ParameterizedTypeReference<List<Municipio>>(){});
    }

    public List<Municipio> findCountyByStateId(String ids) {
        Objects.requireNonNull(ids, "O parametro ids não pode ser nulo");
        final URI uri = uriTemplate.expand(URL_MUNICIPIOS_BY_UF, ids);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return doRequest(requestEntity, new ParameterizedTypeReference<List<Municipio>>(){});
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

    public Path getWorkDir() {
        return workDir;
    }
}
