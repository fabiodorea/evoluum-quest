package com.evoluum.desafio.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static void writeFileToOutput(Path path, OutputStream output) throws IOException {
        InputStream inputStream = new FileInputStream(new File(path.toUri())); //load the file
        IOUtils.copy(inputStream, output);
        output.flush();
        inputStream.close();
        //exclui o arquivo após o envio.
        Files.deleteIfExists(path);
    }
}
