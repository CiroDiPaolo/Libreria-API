package com.LibreriaApi.Repository;

import com.LibreriaApi.Enums.Category;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Book> findAll(Pageable pageable);
    @Modifying
    @Query("update Multimedia b set b.status = false where b.id = :id")
    void logicallyDeleteById(@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitleLikeIgnoreCase(@Param("title") String title);

   @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
   Page<Book> searchByAuthorLikeIgnoreCase(@Param("author") String author, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> searchByAuthorLikeIgnoreCase(@Param("author") String author);


    @Query("SELECT b FROM Book b WHERE b.status = true AND LOWER(b.publishingHouse) LIKE LOWER(CONCAT('%', :publishingHouse, '%'))")
    List<Book> searchByPublishinHouseLikeIgnoreCase(@Param("publishingHouse") String publishingHouse);

    @Query("""
    SELECT new com.LibreriaApi.Model.DTO.BookDTO(
        b.id, b.category, b.description, b.releaseDate, b.status,
        b.ISBN, b.title, b.author, b.publishingHouse, b.urlImage
    )
    FROM Book b
    WHERE b.status = true
    """)
    Page<BookDTO> findAllActiveBookDTOs(Pageable pageable);

    @Query("""
    SELECT new com.LibreriaApi.Model.DTO.BookDTO(
        b.id,
        b.category,
        b.description,
        b.releaseDate,
        b.status,
        b.ISBN,
        b.title,
        b.author,
        b.publishingHouse,
        b.urlImage
    )
    FROM Book b
    WHERE b.id = :id
""")
    Optional<BookDTO> findBookSheetById(@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE b.category = :c AND b.status = true")
    Page<Book> findByCategoryActive(@Param("c") Category c, Pageable pageable);


    @Query(value = """
        SELECT b FROM Book b
        WHERE (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
          AND (:category IS NULL OR b.category = :category)
          AND (:publishingHouse IS NULL OR LOWER(b.publishingHouse) LIKE LOWER(CONCAT('%', :publishingHouse, '%')))
          AND (:fromYear IS NULL OR function('year', b.releaseDate) >= :fromYear)
          AND (:toYear IS NULL OR function('year', b.releaseDate) <= :toYear)
        """,
            countQuery = """
        SELECT COUNT(b) FROM Book b
        WHERE (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
          AND (:category IS NULL OR b.category = :category)
          AND (:publishingHouse IS NULL OR LOWER(b.publishingHouse) LIKE LOWER(CONCAT('%', :publishingHouse, '%')))
          AND (:fromYear IS NULL OR function('year', b.releaseDate) >= :fromYear)
          AND (:toYear IS NULL OR function('year', b.releaseDate) <= :toYear)
        """
    )
    Page<Book> search(
            @Param("author") String author,
            @Param("category") Category category,
            @Param("publishingHouse") String publishingHouse,
            @Param("fromYear") Integer fromYear,
            @Param("toYear") Integer toYear,
            Pageable pageable
    );

}
