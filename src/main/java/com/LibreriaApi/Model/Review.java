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
    private Long idRevies;

    private int rating;

    private String content;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id_usuario")
    private User user;

    //private Multimedia multimedia;

}
