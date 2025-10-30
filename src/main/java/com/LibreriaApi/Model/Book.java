package com.LibreriaApi.Model;

import com.LibreriaApi.Enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;


@Entity
@Table(name = "Book")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends Multimedia{

    @Column(name = "urlImage")
    private String urlImage;

    @NotNull(message = "El ISBN no puede ser nulo")
    @Pattern(regexp = "^(\\d{9}[0-9X])|(978\\d{9}[0-9])$", message = "El ISBN debe tener 10 o 13 caracteres")
    @Column(name = "ISBN",length = 13)
    @JsonProperty("ISBN")
    private String ISBN;

    @NotNull(message = "El titulo no puede ser nulo")
    @Size(min = 1, max = 100, message = "El titulo no debe exdecer los 100 caracteres")
    @Column(name = "title",length = 100)
    private String title;

    @NotNull(message = "El autor no puede ser nulo")
    @Size(min = 1, max = 30, message = "El autor no debe exdecer los 30 caracteres")
    @Column(name = "author",length = 30)
    private String author;

    @NotNull(message = "La editorial no puede ser nula")
    @Size(min = 1, max = 60, message = "La editorial no debe exdecer los 60 caracteres")
    @Column(name = "publishingHouse",length = 60)
    private String publishingHouse;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookStage> bookStages;


}
