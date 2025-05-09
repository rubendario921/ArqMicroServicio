package com.rdcl.transactionsApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnavailableValue extends RuntimeException {
    public UnavailableValue(String accountNumber, String message) {

        super(String.format("Error: Account Number: '%s' Detail: '%s'", accountNumber, message));
    }
}