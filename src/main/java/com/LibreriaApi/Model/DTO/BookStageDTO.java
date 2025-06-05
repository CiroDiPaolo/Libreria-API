package com.LibreriaApi.Model.DTO;

import com.LibreriaApi.Enums.Stage;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El estado del libro no puede ser nulo")
    @Enumerated(EnumType.STRING)
    private Stage stage;

}
