package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.domain.interfaces.EstadoService;
import com.evoluum.desafio.domain.interfaces.ExportFile;
import com.evoluum.desafio.domain.views.EstadoResponse;
import com.evoluum.desafio.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
public class EstadoProxyService implements EstadoService, ExportFile {

    @Autowired
    private final IbgeProxyService ibgeProxyService;

    public EstadoProxyService(IbgeProxyService ibgeProxyService) {
        this.ibgeProxyService = ibgeProxyService;
    }

    @Override
    public List<Estado> findAll() throws RuntimeException {
       return ibgeProxyService.findAllStates();
    }

    public List<EstadoResponse> findStatesAsResponse() {
        return findAll()
                .stream()
                .map(estado -> EstadoResponse.fromEntity(estado))
                .collect(Collectors.toList());
    }

    @Override
    public void generateCsv(String fileName, List localidades, HttpServletResponse response) throws IOException {
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
            csvData.setLength(0);
        });
        pw.close();

        FileUtils.writeFileToOutput(path, response.getOutputStream());
    }

    @Override
    public List<Estado> recoverFindAll(RuntimeException ex, String ids) {
        return new ArrayList<>();
    }

}
