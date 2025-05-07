package com.LibreriaApi.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Libro")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column(name = "ISBN",length = 20)
    private String ISBN;

    @Column(name = "titulo",length = 100)
    private String titulo;

    @Column(name = "autor",length = 30)
    private String autor;

    @Column(name = "descripcion",length = 100)
    private String descripcion;

    @Column(name = "editorial",length = 30)
    private String editorial;

    @Column(name = "fechaPublicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;

    @Column( name = "genero", length = 20)
    private String genero;


}
