package com.LibreriaApi.Model;

import com.LibreriaApi.Enums.Stage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "BookStage")
@Data
public class BookStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStage")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idUser")
    private UserEntity user;

    @NotNull(message = "El libro no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "idBook", referencedColumnName = "idMultimedia")
    @Valid
    private Book book;

    @NotNull(message = "El estado del libro no puede ser nulo")
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookStage bookStage = (BookStage) o;
        return Objects.equals(id, bookStage.id) && Objects.equals(user, bookStage.user) && Objects.equals(book, bookStage.book) && stage == bookStage.stage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, book, stage);
    }
}