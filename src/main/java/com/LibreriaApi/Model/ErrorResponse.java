package com.LibreriaApi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
//Es para probar commit
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
}
