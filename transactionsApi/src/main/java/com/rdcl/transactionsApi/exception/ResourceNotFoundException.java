package com.rdcl.transactionsApi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String reference,String message) {

        super(String.format("Error: Reference: '%s' Details: '%s'", reference, message));
    }
}