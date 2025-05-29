package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookDTO;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Service.GoogleBooksApi.GoogleBooksRequeast;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookCrudService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GoogleBooksRequeast googleApi;

    //METODOS GET

    public Book getBookService(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));
    }

    public Iterable<Book> getAllBooksService() { return bookRepository.findAll(); }

    public Optional<Book> getBooksByTitleService(String titulo) { return bookRepository.findByTitle(titulo); }

    public Optional<Book> getBooksByISBNService(String isbn) { return bookRepository.findByISBN(isbn); }

    public Optional<Book> getBooksByAutorService(String autor) { return bookRepository.findByAuthor(autor); }

    public Optional<Book> getBooksByEditorialService(String editorial) { return bookRepository.findBypublishingHouse(editorial); }

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

        System.out.println(dto.toString());

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

        System.out.println(book);

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
}
