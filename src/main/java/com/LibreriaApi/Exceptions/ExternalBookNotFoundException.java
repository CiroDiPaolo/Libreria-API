package com.LibreriaApi.Exceptions;

public class ExternalBookNotFoundException extends RuntimeException {
    public ExternalBookNotFoundException(String message) {
        super(message);
    }
}
