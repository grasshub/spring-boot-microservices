package com.ecomm.ordersservice.exception;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public void serviceUnavailable(CallNotPermittedException callNotPermittedException) {
        log.error(callNotPermittedException.getMessage());
    }

    @ExceptionHandler(BulkheadFullException.class)
    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public void bandwidthExceeded(BulkheadFullException bulkheadFullException) {
        log.error(bulkheadFullException.getMessage());
    }
}
