package com.LibreriaApi.Exceptions;

import com.LibreriaApi.Model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception){
        ErrorResponse entityNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(entityNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookStageNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(BookStageNotFoundException exception){
        ErrorResponse entityNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(entityNotFound, HttpStatus.NOT_FOUND);
    }

    //Se lanza cuando un DTO con @Valid falla la validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(messages);
    }

    //Se lanza cuando validás con @Validated en parámetros simples del controller
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    //Se lanza cuando el cuerpo JSON está mal formado o tiene un tipo incorrecto
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("JSON mal formado o con tipos incorrectos");
    }

    // Se lanza cuando falta un @RequestParam obligatorio
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Falta el parámetro obligatorio: " + ex.getParameterName();
        return ResponseEntity.badRequest().body(msg);
    }

    //Se lanza cuando se hace un request con metodo HTTP incorrecto
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleWrongMethod(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Metodo no permitido");
    }

    //Se lanza cuando el usuario no puede acceder al metodo
    @ExceptionHandler(AccessDeniedUserException.class)
    public ResponseEntity<?> AccessDeniedUserException(AccessDeniedUserException exception){
        ErrorResponse entityNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(entityNotFound, HttpStatus.FORBIDDEN);
    }

}
