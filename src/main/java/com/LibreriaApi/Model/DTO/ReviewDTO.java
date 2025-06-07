package com.LibreriaApi.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {

    private Long idReview;

    @NotNull(message = "La calificacion no puede ser nula")
    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    private int rating;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 250, message = "La calificación no puede ser mayor a 5")
    private String content;

    private Boolean status;


    private Long idUser;

    @NotNull
    private Long idMultimedia;


}
