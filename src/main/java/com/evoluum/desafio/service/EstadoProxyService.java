package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.domain.interfaces.EstadoService;
import com.evoluum.desafio.domain.views.EstadoResponse;
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
public class EstadoProxyService implements EstadoService {

    private static final String URL_ESTADOS = "/estados";

    private final IbgeProxyService ibgeProxyService;
    private RetryTemplate retryTemplate;

    public EstadoProxyService(IbgeProxyService ibgeProxyService, RetryTemplate retryTemplate) {
        this.ibgeProxyService = ibgeProxyService;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public List<Estado> findAll() {
        return retryTemplate.execute(arg0 -> ibgeProxyService.findAllStates());
    }

    public List<EstadoResponse> getUfsAsResponse() {
        return findAll()
                .stream()
                .map(estado -> EstadoResponse.fromEntity(estado))
                .collect(Collectors.toList());
    }

    public Path generateCsv(String fileName, List localidades) throws IOException {
        Objects.requireNonNull(fileName, "O nome do arquivo não ser nullo.");
        Objects.requireNonNull(localidades, "A lista de estados não pode ser nulla.");
        Path path = Files.createFile(ibgeProxyService.getWorkDir().resolve(fileName));
        PrintWriter pw = new PrintWriter(new File(path.toUri()));

        StringBuffer csvHeader = new StringBuffer("");
        StringBuffer csvData = new StringBuffer("");
        csvHeader.append("idEstado,siglaEstado,regiaoNome\n");

        // write header
        pw.write(csvHeader.toString());
        ((List<Estado>) localidades).stream().forEach(e -> {
            csvData.append(e.getId())
                    .append(",")
                    .append(e.getSigla())
                    .append(",")
                    .append(e.getRegiao().getNome())
                    .append("\n");
            pw.write(csvData.toString());
        });
        pw.close();
        return path;
    }

    @Override
    public List<Estado> recoverFindAll(ProxyException ex, String ids) {
        return new ArrayList<>();
    }

}
