package com.LibreriaApi.Exceptions;

public class BookStageNotFoundException extends RuntimeException {
    public BookStageNotFoundException(String message) {
        super(message);
    }
}
