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
    List<Municipio> recoverFindAll(ProxyException ex);

    @Recover
    Municipio recoverFindByName(ProxyException ex, String name);

    @Recover
    List<Municipio> recoverFindByUf(ProxyException ex, String ids);


    @CircuitBreaker(value = { ProxyException.class })
    List<Municipio> findAll() throws Exception;

    @CircuitBreaker(value = { ProxyException.class })
    @Cacheable(value = "municipio", key="#name")
    Municipio findByName(String name) throws Exception;

    @CircuitBreaker(value = { ProxyException.class })
    List<Municipio> findByUfIds(String ids) throws Exception;

}
