package com.LibreriaApi.Exceptions;

public class AccessDeniedUserException extends RuntimeException {
    public AccessDeniedUserException(String message) {
        super(message);
    }
}
