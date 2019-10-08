package com.evoluum.desafio.domain.interfaces;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.exception.ProxyException;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;

import java.util.List;

public interface EstadoService {

    @Recover
    List<Estado> recoverFindAll(RuntimeException ex, String ids);

    /**
     * Retorna uma lista de estados da República
     *
     * @return List<Estado>
     * @throws Exception
     */
    @CircuitBreaker(value = { RuntimeException.class}, maxAttempts = 3, resetTimeout = 8000L)
    List<Estado> findAll() throws Exception;
}
