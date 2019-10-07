package com.evoluum.desafio.domain.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface ExportFile {

    Path generateCsv(String fileName, List localidades) throws IOException;

    default Path generatePdf(String fileName) throws IOException {
        return Paths.get("path/to/pdf/file");
    }

    default Path generateXml(String fileName) throws IOException {
        return Paths.get("path/to/xml/file");
    }
}
