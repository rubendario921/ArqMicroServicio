package com.rdcl.Usersapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String reference,String message) {
        super(String.format("Error: Reference: '%s' Details: '%s'", reference, message));
    }
}
