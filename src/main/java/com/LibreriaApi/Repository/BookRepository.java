package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
    Optional<Book> findByISBN(String ISBN);
    Optional<Book> findByAuthor(String author);
    Optional<Book> findBypublishingHouse(String publishingHouse);
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitleLikeIgnoreCase(@Param("title") String title);
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> searchByAuthorLikeIgnoreCase(@Param("author") String author);
    @Query("SELECT b FROM Book b WHERE LOWER(b.publishingHouse) LIKE LOWER(CONCAT('%', :publishingHouse, '%'))")
    List<Book> searchByPublishinHouseLikeIgnoreCase(@Param("publishingHouse") String publishingHouse);

}
