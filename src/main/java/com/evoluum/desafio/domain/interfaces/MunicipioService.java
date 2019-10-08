package com.evoluum.desafio.domain.interfaces;

import com.evoluum.desafio.domain.Municipio;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.evoluum.desafio.exception.ProxyException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.sql.SQLException;
import java.util.List;

public interface MunicipioService {

    @Recover
    List<Municipio> recoverFindAll(RuntimeException ex);

    @Recover
    Municipio recoverFindByName(RuntimeException ex, String name);

    @Recover
    List<Municipio> recoverFindByUf(RuntimeException ex, String ids);


    /**
     * Retorna a lista completa de municípios da República.
     *
     * @return List<Municipio>
     * @throws Exception
     */
    @CircuitBreaker(value = { RuntimeException.class }, maxAttempts = 2, resetTimeout = 8000)
    List<Municipio> findAll() throws Exception;

    /**
     * Retorna um ou mais municípios que possui o nome igual a <code>name</code>
     *
     * @param name
     * @return Municipio
     * @throws RuntimeException
     */
    @CircuitBreaker(value = { RuntimeException.class }, maxAttempts = 2, resetTimeout = 8000)
    @Cacheable(value = "municipio", key="#name")
    Municipio findByName(String name) throws RuntimeException;

    /**
     * Retorna uma lista de Municipios dos Estados que possuem os <code>ids</code> informados.
     *
     * @param ids
     * @return
     * @throws RuntimeException
     */
    @CircuitBreaker(value = { RuntimeException.class }, maxAttempts = 2, resetTimeout = 8000)
    List<Municipio> findByUfIds(String ids) throws RuntimeException;

}
