package com.LibreriaApi.Exceptions;

import com.LibreriaApi.Model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerEntityNotFoundException(EntityNotFoundException exception){
        ErrorResponse entityNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(entityNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookStageNotFoundException.class)
    public ResponseEntity<?> handlerEntityNotFoundException(BookStageNotFoundException exception){
        ErrorResponse entityNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage());
        return new ResponseEntity<>(entityNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(messages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }


}
