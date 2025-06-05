package com.LibreriaApi.Exceptions;

public class BookStageAlredyInFavorite extends RuntimeException {
    public BookStageAlredyInFavorite(String message) {
        super(message);
    }
}
