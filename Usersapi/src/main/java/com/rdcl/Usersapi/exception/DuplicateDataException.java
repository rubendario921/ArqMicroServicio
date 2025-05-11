package com.rdcl.Usersapi.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String nameClass, String nameMethod, String message) {
        super(String.format("Error: Reference in '%s'.'%s'(): Details: '%s'", nameClass, nameMethod, message));
    }
}
