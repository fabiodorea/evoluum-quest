package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class UfProxyService {

    private static final String URL_ESTADOS = "/estados";

    private final ProxyService proxyService;

    public UfProxyService(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public List<Estado> getUfs() {
        final URI uri = proxyService.getUriTemplate().expand(URL_ESTADOS);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return proxyService.doRequest(requestEntity, List.class);
    }

}
