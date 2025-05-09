package com.rdcl.transactionsApi.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(UnavailableValue.class)
    public String handleUnavailableValue(UnavailableValue ex){
        return ex.getMessage();
    }
    @ExceptionHandler(IsNullOrEmptyException.class)
    public String handleIsNullOrEmptyException(IsNullOrEmptyException ex){
        return ex.getMessage();
    }
}