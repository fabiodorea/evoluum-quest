package com.evoluum.desafio.domain.interfaces;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.exception.ProxyException;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;

import java.util.List;

public interface EstadoService {

    @Recover
    List<Estado> recoverFindAll(ProxyException ex, String ids);

    @CircuitBreaker(value = { ProxyException.class})
    List<Estado> findAll() throws Exception;
}
