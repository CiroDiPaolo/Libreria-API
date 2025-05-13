package com.LibreriaApi.Model;

import com.LibreriaApi.Enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Multimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMultimedia")
    private Long id;

    @NotNull(message = "La categoría no puede ser nula")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20)
    private Category category;

    @NotNull(message = "La descripción no puede ser nula")
    @Column(name = "descripcion", length = 150)
    private String description;

    @NotNull(message = "La fecha de lanzamiento no puede ser nula")
    @Column(name = "releaseDate")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @NotNull(message = "El estado no puede ser nulo")
    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "multimedia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
