package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Municipio;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class MunicipioProxyService {

    private static final String URL_MUNICIPIOS = "/estados/{id}/municipios";

    private final ProxyService proxyService;

    public MunicipioProxyService(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public List<Municipio> getMunicipiosByUf(String ids) {
        final URI uri = proxyService.getUriTemplate().expand(URL_MUNICIPIOS, ids);
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return proxyService.doRequest(requestEntity, List.class);
    }
}
