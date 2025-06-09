package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByISBN(String ISBN);
    Optional<Book> findByTitle(String title);
    Optional<Book> findByISBN(String ISBN);
    Optional<Book> findByAuthor(String author);
    Optional<Book> findBypublishingHouse(String publishingHouse);
    @Modifying
    @Query("update Multimedia b set b.status = false where b.id = :id")
    void logicallyDeleteById(@Param("id") Long id);
    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitleLikeIgnoreCase(@Param("title") String title);
    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> searchByAuthorLikeIgnoreCase(@Param("author") String author);
    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.publishingHouse) LIKE LOWER(CONCAT('%', :publishingHouse, '%'))")
    List<Book> searchByPublishinHouseLikeIgnoreCase(@Param("publishingHouse") String publishingHouse);

}
