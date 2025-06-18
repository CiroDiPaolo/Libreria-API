package com.LibreriaApi.Model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoadBookDTO {

    @NotNull(message = "La categoría no puede ser nula")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20)
    private String category;

    @NotNull(message = "El ISBN no puede ser nulo")
    @Pattern(regexp = "^(\\d{9}[0-9X])|(978\\d{9}[0-9])$", message = "El ISBN debe tener 10 o 13 caracteres")
    @JsonProperty("ISBN")
    private String isbn;
    
    @NotNull(message = "El autor no puede ser nulo")
    @Size(min = 1, max = 30, message = "El autor no debe exdecer los 30 caracteres")
    private String author;

    @NotBlank(message = "La descripcion no puede estar en blanco")
    @Size(max = 150, message = "La descripcion no puede exceder los 150 caracteres")
    @Column(name = "descripcion", length = 150)
    private String description;

}
