package com.LibreriaApi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    private int rating;

    private String content;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMultimedia")
    private Multimedia multimedia;

}
