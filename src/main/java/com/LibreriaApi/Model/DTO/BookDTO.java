package com.LibreriaApi.Model.DTO;

import com.LibreriaApi.Model.Multimedia;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public class BookDTO extends Multimedia {

        private String urlImage;

        @NotNull(message = "El ISBN no puede ser nulo")
        @Pattern(regexp = "^(\\d{9}[0-9X])|(978\\d{9}[0-9])$", message = "El ISBN debe tener 10 o 13 caracteres")
        @JsonProperty("ISBN")
        private String ISBN;
    
        @NotNull(message = "El titulo no puede ser nulo")
        @Size(min = 1, max = 100, message = "El titulo no debe exdecer los 100 caracteres")
        private String title;
    
        @NotNull(message = "El autor no puede ser nulo")
        @Size(min = 1, max = 30, message = "El autor no debe exdecer los 30 caracteres")
        private String author;
    
        @NotNull(message = "La editorial no puede ser nula")
        @Size(min = 1, max = 30, message = "La editorial no debe exdecer los 30 caracteres")
        private String publishingHouse;

        public BookDTO(
                Long id,
                com.LibreriaApi.Enums.Category category,
                String description,
                java.util.Date releaseDate,
                Boolean status,
                String ISBN,
                String title,
                String author,
                String publishingHouse,
                String urlImage
        ) {
            this.setId(id);
            this.setCategory(category);
            this.setDescription(description);
            this.setReleaseDate(releaseDate);
            this.setStatus(status);
            this.ISBN = ISBN;
            this.title = title;
            this.author = author;
            this.publishingHouse = publishingHouse;
            this.urlImage = urlImage;
        }

    }
