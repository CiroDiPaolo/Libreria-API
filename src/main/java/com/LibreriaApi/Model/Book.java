package com.LibreriaApi.Model;

import com.LibreriaApi.Enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.SimpleTimeZone;

@Entity
@Table(name = "Book")
@Data
public class Book extends Multimedia{

    @NotNull(message = "El ISBN no puede ser nulo")
    @Column(name = "ISBN",length = 20)
    private String ISBN;

    @NotNull(message = "El titulo no puede ser nulo")
    @Size(max = 100, message = "El titulo no debe exdecer los 100 caracteres")
    @Column(name = "title",length = 100)
    private String title;

    @NotNull(message = "El autor no puede ser nulo")
    @Size(max = 30, message = "El autor no debe exdecer los 30 caracteres")
    @Column(name = "author",length = 30)
    private String author;

    @NotNull(message = "La descripcion no puede ser nula")
    @Size(max = 150, message = "La descripcion no debe exdecer los 100 caracteres")
    @Column(name = "description",length = 150)
    private String description;

    @NotNull(message = "La editorial no puede ser nula")
    @Size(max = 30, message = "La editorial no debe exdecer los 30 caracteres")
    @Column(name = "publishingHouse",length = 30)
    private String publishingHouse;

    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "publicationDate")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @NotNull(message = "El genero no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Category gender;


}
