package com.evoluum.desafio.service;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.exception.ProxyException;
import com.evoluum.desafio.util.MockUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {EstadoProxyService.class, RetryTemplate.class})
public class RetryMethodTest {

    @Autowired
    EstadoProxyService estadoProxyService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Test
    public void testMethodWithFailure_ShouldRetry1TimesThenThrowException(){



    }


}
