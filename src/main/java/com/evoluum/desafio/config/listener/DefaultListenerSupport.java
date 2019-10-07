package com.evoluum.desafio.config.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultListenerSupport extends RetryListenerSupport {

    private static final Logger logger = Logger.getLogger(DefaultListenerSupport.class.getName());

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        logger.info("onClose");
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        logger.info("onError");
        logger.log(Level.SEVERE, throwable.getMessage(), throwable);
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        logger.info("onOpen");
        return super.open(context, callback);
    }

}