package com.evoluum.desafio.config.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Log RestTemplate Request and Response without destroying the body
 */
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = Logger.getLogger(RequestResponseLoggingInterceptor.class.getName());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        LOGGER.log(Level.INFO,"===========================request begin================================================");
        LOGGER.log(Level.INFO, "URI         : {0}", request.getURI().toString());
        LOGGER.log(Level.INFO, "Method      : {0}", request.getMethod());
        LOGGER.log(Level.INFO,"Headers     : {0}", request.getHeaders());
        LOGGER.log(Level.INFO,"Request body: {0}", new String(body, "UTF-8"));
        LOGGER.log(Level.INFO,"==========================request end================================================");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        LOGGER.log(Level.INFO,"============================response begin==========================================");
        LOGGER.log(Level.INFO,"Status code  : {}", response.getStatusCode());
        LOGGER.log(Level.INFO,"Status text  : {}", response.getStatusText());
        LOGGER.log(Level.INFO,"Headers      : {}", response.getHeaders());
        LOGGER.log(Level.INFO,"Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        LOGGER.log(Level.INFO,"=======================response end=================================================");
    }

}
