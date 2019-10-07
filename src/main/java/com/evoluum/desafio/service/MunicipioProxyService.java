package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.domain.interfaces.MunicipioService;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.exception.ProxyException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MunicipioProxyService implements MunicipioService {

    private static final String URL_MUNICIPIOS_BY_UF = "/estados/{id}/municipios";
    private static final String URL_MUNICIPIOS = "/municipios";

    private final IbgeProxyService ibgeProxyService;
    private RetryTemplate retryTemplate;

    public MunicipioProxyService(IbgeProxyService ibgeProxyService, RetryTemplate retryTemplate) {
        this.ibgeProxyService = ibgeProxyService;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public List<Municipio> findAll() {
        return retryTemplate.execute(arg0 -> {
            final URI uri = ibgeProxyService.getUriTemplate().expand(URL_MUNICIPIOS);
            RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
            return ibgeProxyService
                    .doRequest(requestEntity, new ParameterizedTypeReference<List<Municipio>>(){});
        });
    }

    @Override
    public Municipio findByName(String name) throws Exception {
        try {
            Objects.requireNonNull(name, "O nome não pode ser nulo");
            return retryTemplate.execute(arg0 -> findAll()
                    .stream()
                    .filter(municipio -> municipio.getNome().equalsIgnoreCase(name))
                    .findFirst()
                    .get());
        } catch (Exception e){
            throw new ProxyException(e.getMessage(), e);
        }
    }

    @Override
    public List<Municipio> findByUfIds(String ids) {
        return retryTemplate.execute(arg0 ->{
                Objects.requireNonNull(ids, "O parametro ids não pode ser nulo");
                final URI uri = ibgeProxyService.getUriTemplate().expand(URL_MUNICIPIOS_BY_UF, ids);
                RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
                return ibgeProxyService
                        .doRequest(requestEntity, new ParameterizedTypeReference<List<Municipio>>(){});
        });
    }

    public List<MunicipioResponse> findByUfAsResponse(String ids) {
        return findByUfIds(ids)
                .stream()
                .map(municipio -> MunicipioResponse.fromEntity(municipio))
                .collect(Collectors.toList());
    }

    public Path generateCsv(String fileName, List localidades) throws IOException {
        Objects.requireNonNull(fileName, "O nome do arquivo não pode ser nulo.");
        Objects.requireNonNull(localidades, "A lista de municípios não pode ser nula.");
        Path path = Files.createFile(ibgeProxyService.getWorkDir().resolve(fileName));
        PrintWriter pw = new PrintWriter(new File(path.toUri()));

        StringBuffer csvHeader = new StringBuffer("");
        StringBuffer csvData = new StringBuffer("");
        csvHeader.append("nomeCidade,nomeMesorregiao,nomeFormatado\n");

        // write header
        pw.write(csvHeader.toString());
        ((List<Municipio>) localidades).stream().forEach(e -> {
            csvData.append(e.getNome())
                    .append(",")
                    .append(e.getMicrorregiao().getMesorregiao().getNome())
                    .append(",")
                    .append(e.getNomeFormatado())
                    .append("\n");
            pw.write(csvData.toString());
        });
        pw.close();
        return path;
    }

    @Override
    public List<Municipio> recoverFindAll(ProxyException ex) {
        return new ArrayList<>();
    }

    @Override
    public Municipio recoverFindByName(ProxyException ex, String name){
        return new Municipio();
    }

    @Override
    public List<Municipio> recoverFindByUf(ProxyException ex, String ids) {
        return new ArrayList<>();
    }

}
