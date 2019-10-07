package com.evoluum.desafio.util;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class Utils {

    public static String formatResponse(HttpServletResponse response, String prefix){
        String csvFileName = prefix + "-" + LocalDateTime.now().toString().replaceAll("[:.]", "-") + ".csv";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        response.setContentType("text/csv");
        return csvFileName;
    }
}
