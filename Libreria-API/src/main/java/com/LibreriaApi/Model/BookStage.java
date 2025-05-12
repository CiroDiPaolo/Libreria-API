package com.LibreriaApi.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Libro")
@Data
public class BookStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBookStage")
    private long idBookStage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private Stage stage;
}
