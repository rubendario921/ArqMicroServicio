package com.rdcl.transactionsApi.exception;

public class IsNullOrEmptyException extends RuntimeException {
    public IsNullOrEmptyException(String nameClass, String nameMethod, String message) {

        super(String.format("Error: Reference:'%s' - '%s': Details:'%s'", nameClass, nameMethod, message));
    }
}