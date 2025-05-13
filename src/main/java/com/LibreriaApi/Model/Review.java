package com.LibreriaApi.Model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReview")
    private Long idReview;

    @NotNull(message = "La calificacion no puede ser nula")
    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    @Column(name = "rating")
    private int rating;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Column(name = "content", length = 250)
    private String content;

    @NotNull(message = "El estado no puede ser nulo")
    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @Valid
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMultimedia")
    @Valid
    private Multimedia multimedia;

}
