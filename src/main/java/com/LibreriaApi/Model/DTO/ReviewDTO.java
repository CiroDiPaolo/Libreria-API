package com.LibreriaApi.Model.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ReviewDTO {

    private Long idReview;

    @NotNull(message = "La calificacion no puede ser nula")
    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    private int rating;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Size(min = 1, max = 250, message = "El comentario debe tener entre 1 y 250 caracteres")
    private String content;

    private Boolean status;


    private Long idUser;

    @NotNull
    private Long idMultimedia;


}
