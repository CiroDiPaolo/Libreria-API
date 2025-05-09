package com.LibreriaApi.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.SimpleTimeZone;

@Entity
@Table(name = "Libro")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @NotNull(message = "El ISBN no puede ser nulo")
    @Column(name = "ISBN",length = 20)
    private String ISBN;

    @NotNull(message = "El titulo no puede ser nulo")
    @Size(max = 100, message = "El titulo no debe exdecer los 100 caracteres")
    @Column(name = "titulo",length = 100)
    private String titulo;

    @NotNull(message = "El autor no puede ser nulo")
    @Size(max = 30, message = "El autor no debe exdecer los 30 caracteres")
    @Column(name = "autor",length = 30)
    private String autor;

    @NotNull(message = "La descripcion no puede ser nula")
    @Size(max = 150, message = "La descripcion no debe exdecer los 100 caracteres")
    @Column(name = "descripcion",length = 150)
    private String descripcion;

    @NotNull(message = "La editorial no puede ser nula")
    @Size(max = 30, message = "La editorial no debe exdecer los 30 caracteres")
    @Column(name = "editorial",length = 30)
    private String editorial;

    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "fechaPublicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;

    @NotNull(message = "El genero no puede ser nulo")
    @Column( name = "genero", length = 20)
    private String genero;


}
