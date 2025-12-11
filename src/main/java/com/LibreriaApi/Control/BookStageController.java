package com.LibreriaApi.Control;

import com.LibreriaApi.Enums.Stage;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Service.BookStageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/bookstage")
@Tag(name = "Book Stage (Favourite's List)", description = "Operaciones sobre Book Stage (Lista de favoritos)")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;

    @Operation(
            summary = "Agregar libro a favoritos",
            description = "Agrega un libro a la lista de favoritos del usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Libro agregado correctamente",
                            content = @Content(schema = @Schema(implementation = BookStage.class))),
                    @ApiResponse(responseCode = "404", description = "Libro no encontrado o usuario no existe",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "El libro ya está en la lista de favoritos",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/{idBook}")
    public ResponseEntity<BookStage> createBookStage(
            @Parameter(description = "ID del libro a agregar a favoritos", required = true)
            @PathVariable Long idBook) {
        return ResponseEntity.ok(bookStageService.createBookStage(idBook));
    }

    @Operation(
            summary = "Obtener estado de un libro",
            description = "Devuelve el BookStage (estado de lectura) de un libro por su ID. Solo accesible por administradores.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado de libro obtenido",
                            content = @Content(schema = @Schema(implementation = BookStage.class))),
                    @ApiResponse(responseCode = "404", description = "BookStage no encontrado",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookStage> getBookStageById(
            @Parameter(description = "ID del BookStage a obtener", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookStageService.getBookStageById(id));
    }

    @Operation(
            summary = "Listar favoritos del usuario",
            description = "Obtiene la lista completa de libros favoritos del usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookStage.class))))
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<List<BookStage>> getAllBookStageOfUser() {
        return ResponseEntity.ok(bookStageService.getAllBookStageOfUser());
    }

    @Operation(
            summary = "Listar favoritos de un usuario (ADMIN)",
            description = "Obtiene la lista completa de favoritos de un usuario específico. Solo accesible por administradores.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookStage.class))))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/{id}")
    public ResponseEntity<List<BookStage>> getAllBookStageOfAUser(
            @Parameter(description = "ID del usuario del cual se desea obtener la lista", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookStageService.getAllBookStageOfAUser(id));
    }

    @Operation(
            summary = "Eliminar libro de favoritos (usuario)",
            description = "Elimina un libro de la lista de favoritos del usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "BookStage no encontrado",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookStageOfUserById(
            @Parameter(description = "ID del BookStage a eliminar", required = true)
            @PathVariable Long id) {
        bookStageService.deleteBookStageOfUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Eliminar libro de favoritos (ADMIN)",
            description = "Elimina un libro de la lista de favoritos de un usuario específico. Solo accesible por administradores.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario o BookStage no encontrado",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUser}/{idBook}")
    public ResponseEntity<Void> deleteBookStageOfAUserById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long idUser,
            @Parameter(description = "ID del libro a eliminar de favoritos", required = true)
            @PathVariable Long idBook) {
        bookStageService.deleteBookStageOfAUserByDTO(idUser, idBook);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar estado de lectura",
            description = "Permite al usuario modificar el estado de lectura de un libro en su lista de favoritos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                            content = @Content(schema = @Schema(implementation = BookStage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario o BookStage no encontrado",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{idBook}")
    public ResponseEntity<BookStage> updateBookStage(
            @Parameter(description = "DTO con el nuevo estado de lectura del libro", required = true)
            @PathVariable Long idBook
            @RequestBody Stage newStage) {
        return ResponseEntity.ok(bookStageService.updateBookStage(idBook, newStage));
    }

}