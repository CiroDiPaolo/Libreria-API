package com.LibreriaApi.Service;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Repository.BookRepository;
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

    //METODOS GET

    public Book getBookService(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }

    public Iterable<Book> getAllBooksService() { return bookRepository.findAll(); }

    public Optional<Book> getBooksByTitleService(String titulo) { return bookRepository.findByTitulo(titulo); }

    public Optional<Book> getBooksByISBNService(String isbn) { return bookRepository.findByISBN(isbn); }

    public Optional<Book> getBooksByAutorService(String autor) { return bookRepository.findByAutor(autor); }

    public Optional<Book> getBooksByEditorialService(String editorial) { return bookRepository.findByEditorial(editorial); }

    //METODOS DELETE
    public void deleteBookService(Long id) {

        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {

            bookRepository.deleteById(id);

        } else {

            throw new RuntimeException("El libro no existe");

        }
    }

    //METODO ADD
    public void addBookService(Book book) { bookRepository.save(book); }

    //METODO UPDATE
    public void updateBookService(Book book) {

        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (bookOptional.isPresent()) {

            bookRepository.save(book);

        } else {

            throw new RuntimeException("El libro no existe");

        }

    }
}
