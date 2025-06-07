package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import com.LibreriaApi.Model.DTO.BookWithReviewsDTO;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/libros")
@Tag(name = "Books", description = "Operaciones sobre book")
public class BookCrudController {

    @Autowired
    private BookService bookService;


    //METODOS GET
    @Operation(
            summary = "Obtener libro por id",
            description = "Obtiene libro por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Libro existente obtenido",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontraron reseñas con ese ID"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookByIdService(id));
    }

    @Operation(
            summary = "Obtener ficha de libro por id (Con reviews activas)",
            description = "Obtiene libro por id con sus reviews activas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ficha de libro obtenida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookWithReviewsDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Libro inexistente con ese ID"
                    )
            }
    )
    @GetMapping("bookSheet/{id}")
    public ResponseEntity<BookWithReviewsDTO> getBookSheet(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookWithReviewsService(id));
    }

    @Operation(
            summary = "Obtener todos los libros ",
            description = "Obtiene todos los libros ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de todos los libros (puede estar vacia)",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                            )
                    )
            }
    )
    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooksService();
    }

    @Operation(
            summary = "Obtener todos los libros por titulo",
            description = "Obtiene todos los libros por titulo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de libros con los titulos encontrados (puede estar vacia)",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
                    )
            }
    )
    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<Book>> searchBookByTitle(@PathVariable String title) { return ResponseEntity.ok(bookService.getBooksByTitleService(title)); }

    @Operation(
            summary = "Obtener todos los libros por titulo",
            description = "Obtiene todos los libros por titulo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de libros con los titulos que coinciden con el ingresado (puede estar vacia)",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/isbn/{isbn}")
    public ResponseEntity<Book> searchBookByISBN(@PathVariable String isbn) { return ResponseEntity.ok(bookService.getBooksByISBNService(isbn)); }

    @Operation(
            summary = "Obtener todos los libros por autor",
            description = "Obtiene todos los libros por autor",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de libros con los autores que coinciden con el ingresado  (puede estar vacia)",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                            )
                    )
            }
    )
    @GetMapping("/search/author/{author}")
    public ResponseEntity<List<Book>> searchBookByAutor(@PathVariable String author) { return ResponseEntity.ok(bookService.getBooksByAutorService(author)); }

    @Operation(
            summary = "Obtener todos los libros por editorial",
            description = "Obtiene todos los libros por editorial",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de libros con las editoriales que coinciden con el ingresado  (puede estar vacia)",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                            )
                    )
            }
    )
    @GetMapping("/search/publishingHouse/{publishingHouse}")
    public ResponseEntity<List<Book>> searchBookByEditorial(@PathVariable String publishingHouse) { return ResponseEntity.ok(bookService.getBooksByPublishingHouseService(publishingHouse)); }

    //METODOS DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {

        bookService.deleteBookService(id);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Book> createBook(@RequestBody BookDTO book) {
        Book newBook = bookService.addBookService(book);

        return ResponseEntity.ok(newBook);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public void updateBook(@RequestBody Book book) {

        bookService.updateBookService(book);

    }

}
