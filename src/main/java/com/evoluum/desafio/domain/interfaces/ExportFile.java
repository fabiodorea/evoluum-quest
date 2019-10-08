package com.evoluum.desafio.domain.interfaces;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface ExportFile {

    void generateCsv(String fileName, List localidades, HttpServletResponse response) throws IOException;

    default void generatePdf(String fileName) throws IOException {

    }

    default void generateXml(String fileName) throws IOException {

    }
}
