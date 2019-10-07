package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.domain.interfaces.MunicipioService;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.exception.ProxyException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MunicipioProxyService implements MunicipioService {

    private final IbgeProxyService ibgeProxyService;

    public MunicipioProxyService(IbgeProxyService ibgeProxyService) {
        this.ibgeProxyService = ibgeProxyService;
    }

    @Override
    public List<Municipio> findAll() {
        return ibgeProxyService.findAllCountys();
    }

    @Override
    public Municipio findByName(String name) {
        try {
            Objects.requireNonNull(name, "O nome não pode ser nulo");
            return findAll()
                    .stream()
                    .filter(municipio -> municipio.getNome().equalsIgnoreCase(name))
                    .findFirst()
                    .get();
        } catch (Exception e){
            throw new ProxyException(e.getMessage(), e);
        }
    }

    @Override
    public List<Municipio> findByUfIds(String ids) {
        return ibgeProxyService.findCountyByStateId(ids);
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
