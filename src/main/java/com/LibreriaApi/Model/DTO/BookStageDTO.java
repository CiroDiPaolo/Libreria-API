package com.LibreriaApi.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookStageDTO {

    @NotBlank(message = "El id del libro no puede ser nulo")
    @Positive(message = "El ID de libro debe ser positivo")
    private Long idBook;

    @NotBlank(message = "El id del usuario no puede ser nulo")
    @Positive(message = "El ID de libro debe ser positivo")
    private Long idUser;


}
