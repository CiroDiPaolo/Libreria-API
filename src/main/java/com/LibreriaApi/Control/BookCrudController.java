package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookDTO;
import com.LibreriaApi.Service.BookCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/libros")
public class BookCrudController {

    @Autowired
    private BookCrudService bookCrudService;


    //METODOS GET

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookCrudService.getBookService(id);
    }

    @GetMapping("/all")
    public Iterable<Book> getAllBooks() {
        return bookCrudService.getAllBooksService();
    }

    @GetMapping("/search/{titulo}")
    public Optional<Book> searchBook(@PathVariable String titulo) { return bookCrudService.getBooksByTitleService(titulo); }

    @GetMapping("/search/isbn/{isbn}")
    public Optional<Book> searchBookByISBN(@PathVariable String isbn) { return bookCrudService.getBooksByISBNService(isbn); }

    @GetMapping("/search/autor/{autor}")
    public Optional<Book> searchBookByAutor(@PathVariable String autor) { return bookCrudService.getBooksByAutorService(autor); }

    @GetMapping("/search/editorial/{editorial}")
    public Optional<Book> searchBookByEditorial(@PathVariable String editorial) { return bookCrudService.getBooksByTitleService(editorial); }

    //METODOS DELETE

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {

        bookCrudService.deleteBookService(id);

    }


    @PostMapping()
    public ResponseEntity<Book> createBook(@RequestBody BookDTO book) {

        Book newBook = bookCrudService.addBookService(book);

        return ResponseEntity.ok(newBook);

    }

    @PutMapping()
    public void updateBook(@RequestBody Book book) {

        bookCrudService.updateBookService(book);

    }

}
