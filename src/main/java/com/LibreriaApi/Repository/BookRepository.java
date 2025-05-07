package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitulo(String titulo);
    Optional<Book> findByISBN(String ISBN);
    Optional<Book> findByAutor(String autor);
    Optional<Book> findByEditorial(String editorial);


}
