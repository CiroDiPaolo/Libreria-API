package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.BookDTO;
import com.LibreriaApi.Model.DTO.BookWithReviewsDTO;
import com.LibreriaApi.Model.DTO.LoadBookDTO;
import com.LibreriaApi.Service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Operaciones sobre book (libros)")
public class BookCrudController {

    @Autowired
    private BookService bookService;

    @Operation(
            summary = "Obtener libro por id",
            description = "Obtiene libro por id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro existente obtenido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron reseñas con ese ID")
    })
    @GetMapping("/{id}")
    //DEBERIA TENER PREAUTHORIZE ROLE ADMIN
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID del libro a obtener", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(
            summary = "Obtener ficha de libro por id (Con reviews activas)",
            description = "Obtiene libro por id con sus reviews activas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha de libro obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookWithReviewsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Libro inexistente con ese ID")
    })
    @GetMapping("bookSheet/{id}")
    public ResponseEntity<BookWithReviewsDTO> getBookSheet(
            @Parameter(description = "ID del libro para obtener ficha con reviews", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookWithReviews(id));
    }

    @Operation(
            summary = "Obtener todos los libros ",
            description = "Obtiene todos los libros "
    )
    @ApiResponse(responseCode = "200", description = "Lista de todos los libros (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }


    @Operation(
            summary = "Obtener todos los libros activos ",
            description = "Obtiene todos los libros activos "
    )
    @ApiResponse(responseCode = "200", description = "Lista de todos los libros activos (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping("/all/active")
    public List<BookDTO> getAllActiveBooks() {
            return bookService.getAllActiveBooks();
    }

    @Operation(
            summary = "Obtener todos los libros por titulo",
            description = "Obtiene todos los libros por titulo"
    )
    @ApiResponse(responseCode = "200", description = "Lista de libros con los titulos encontrados (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<Book>> searchBookByTitle(
            @Parameter(description = "Título para buscar", required = true)
            @PathVariable String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @Operation(
            summary = "Obtener todos los libros por titulo",
            description = "Obtiene todos los libros por titulo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de libros con los titulos que coinciden con el ingresado (puede estar vacia)",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/isbn/{isbn}")
    public ResponseEntity<Book> searchBookByISBN(
            @Parameter(description = "ISBN para buscar", required = true)
            @PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBooksByISBN(isbn));
    }

    @Operation(
            summary = "Obtener todos los libros por autor",
            description = "Obtiene todos los libros por autor"
    )
    @ApiResponse(responseCode = "200", description = "Lista de libros con los autores que coinciden con el ingresado  (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping("/search/author/{author}")
    public ResponseEntity<List<Book>> searchBookByAuthor(
            @Parameter(description = "Autor para buscar", required = true)
            @PathVariable String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @Operation(
            summary = "Obtener todos los libros por editorial",
            description = "Obtiene todos los libros por editorial"
    )
    @ApiResponse(responseCode = "200", description = "Lista de libros con las editoriales que coinciden con el ingresado  (puede estar vacia)",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping("/search/publishingHouse/{publishingHouse}")
    public ResponseEntity<List<Book>> searchBookByPublishingHouse(
            @Parameter(description = "Editorial para buscar", required = true)
            @PathVariable String publishingHouse) {
        return ResponseEntity.ok(bookService.getBooksByPublishingHouse(publishingHouse));
    }

    @Operation(
            summary = "Dar de baja un libro por su ID",
            description = "Busca el libro por su ID y hace un soft delete."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro dado de baja exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró un libro con el ID proporcionado."),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta operación.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID del libro a eliminar", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Crear y guardar un libro en la base de datos",
            description = "Se introducen los datos del libro para poder crearlo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta operación."),
            @ApiResponse(responseCode = "409", description = "Ya existe un libro con el mismo ISBN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Book> createBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class)
                    )
            )
            @Valid @RequestBody BookDTO book) {
        Book newBook = bookService.addBook(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return ResponseEntity.created(location).body(newBook);
    }

    @Operation(
            summary = "Crear y guardar un libro en la base de datos con ayuda de Google Books API",
            description = "Se introducen datos por formulario y otros datos se obtienen por la API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta operación."),
            @ApiResponse(responseCode = "409", description = "Ya existe un libro con el mismo ISBN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Book> createBookWithAPI(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro para crear vía API",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoadBookDTO.class)
                    )
            )
            @Valid @RequestBody LoadBookDTO book) {
        Book newBook = bookService.addBookWithAPI(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return ResponseEntity.created(location).body(newBook);
    }

    @Operation(
            summary = "Actualizar un libro",
            description = "Actualiza los datos de un libro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente."),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID del libro a actualizar", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del libro",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class)
                    )
            )
            @Valid @RequestBody BookDTO book) {

        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }


}
