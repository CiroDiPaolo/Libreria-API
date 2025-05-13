package com.LibreriaApi.Model;

import jakarta.persistence.*;
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

    @Column(name = "rating")
    private int rating;

    @Column(name = "content", length = 250)
    private String content;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMultimedia")
    private Multimedia multimedia;

}
