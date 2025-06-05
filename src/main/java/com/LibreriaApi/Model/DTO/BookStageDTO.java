package com.LibreriaApi.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookStageDTO {

    @NotBlank(message = "El id del libro no puede ser nulo")
    @Positive(message = "El ID de libro debe ser positivo")
    private Long idBook;

    @NotBlank(message = "El id del usuario no puede ser nulo")
    @Positive(message = "El ID de libro debe ser positivo")
    private Long idUser;


}
