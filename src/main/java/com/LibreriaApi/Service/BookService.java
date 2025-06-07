package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import com.LibreriaApi.Model.DTO.BookWithReviewsDTO;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Service.GoogleBooksApi.GoogleBooksRequeast;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GoogleBooksRequeast googleApi;
    @Autowired
    private ReviewService reviewService;

    //METODOS GET

    public Book getBookByIdService(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));
    }

    public BookWithReviewsDTO getBookWithReviewsService(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));

        return this.toBookWithReviewsDTO(book);
    }

    public List<Book> getAllBooksService() { return bookRepository.findAll(); }

    public List<Book> getBooksByTitleService(String title) { return bookRepository.searchByTitleLikeIgnoreCase(title); }

    public Book getBooksByISBNService(String isbn) { return bookRepository.findByISBN(isbn)
            .orElseThrow(()-> new EntityNotFoundException("El libro con ISBN " + isbn + " no existe.")); }

    public List<Book> getBooksByAutorService(String author) { return bookRepository.searchByAuthorLikeIgnoreCase(author); }

    public List<Book> getBooksByPublishingHouseService(String publishingHouse) { return bookRepository.searchByPublishinHouseLikeIgnoreCase(publishingHouse); }

    //METODOS DELETE
    public void deleteBookService(Long id) {

        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {

            bookRepository.deleteById(id);

        } else {

            throw new EntityNotFoundException("El libro no existe");

        }
    }

    //METODO ADD
    public Book addBookService(BookDTO dto) {

        String url = googleApi.getThumbnailByISBN(dto.getISBN());

        Book book = new Book();

        book.setISBN(dto.getISBN());
        book.setAuthor(dto.getAuthor());
        book.setPublishingHouse(dto.getPublishingHouse());
        book.setTitle(dto.getTitle());
        book.setCategory(dto.getCategory());
        book.setDescription(dto.getDescription());
        book.setReleaseDate(dto.getReleaseDate());
        book.setStatus(dto.getStatus());
        book.setUrlImage(url);

        return bookRepository.save(book);

    }

    //METODO UPDATE
    public void updateBookService(Book book) {

        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (bookOptional.isPresent()) {

            bookRepository.save(book);

        } else {

            throw new EntityNotFoundException("El libro no existe");

        }

    }

    public BookWithReviewsDTO toBookWithReviewsDTO(Book book) {
        List<ReviewDTO> reviewDTOs = book.getReviews().stream()
                .filter(Review::getStatus)
                .map(review -> reviewService.toDTO(review))
                .toList();

        BookWithReviewsDTO dto = new BookWithReviewsDTO();
        dto.setId(book.getId());
        dto.setISBN(book.getISBN());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishingHouse(book.getPublishingHouse());
        dto.setCategory(book.getCategory());
        dto.setDescription(book.getDescription());
        dto.setReleaseDate(book.getReleaseDate());
        dto.setStatus(book.getStatus());
        dto.setUrlImage(book.getUrlImage());
        dto.setReviewsDTO(reviewDTOs);
        dto.setReviews(null);
        return dto;
    }
}
