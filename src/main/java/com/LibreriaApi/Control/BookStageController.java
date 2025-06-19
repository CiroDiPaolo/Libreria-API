package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Service.BookStageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstage")
@Tag(name = "Book Stage (Favourite's List)", description = "Operaciones sobre Book Stage")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;

    //Metodo post
    @Operation(
            summary = "Agrega un libro a la lista de favoritos",
            description = "Permite a un usuario agregar un libro a su lista de favoritos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Libro agregado exitosamente"
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Usuario o BookStage no encontrado",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)))
                    ,
                    @ApiResponse(responseCode = "409",
                            description = "El usuario ya tiene el libro agregado a su lista",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/{idBook}")
    public ResponseEntity<BookStage> createBookStage(@PathVariable Long idBook) {

        return ResponseEntity.ok(bookStageService.createService(idBook));
    }

    //Metodo get
    @Operation(
            summary = "Obtener Book Stage por id ",
            description = "Permite a un admin obtener el estado de un libro por el id del BookStage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Libro agregado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class))

                    ),
                    @ApiResponse(responseCode = "404",
                            description = "BookStage no encontrado",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)))

            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookStage> getBookStageById(@PathVariable Long id){

        return ResponseEntity.ok(bookStageService.getBookStageById(id));

    }

    @Operation(
            summary = "Obtener lista propia de favoritos",
            description = "Permite a un usuario obtener su lista de favoritos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista encontrada",
                            content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookStage.class))
                    )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<List<BookStage>> getAllBookStageOfUser(){

        return ResponseEntity.ok(bookStageService.getAllBookStageOfUserService());

    }

    @Operation(
            summary = "Obtener lista de favoritos de un usuario",
            description = "Permite a un admin obtener lista de favoritos de un usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista encontrada",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookStage.class))
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/{id}")
    public ResponseEntity<List<BookStage>> getAllBookStageOfAUser(@PathVariable Long id){

        return ResponseEntity.ok(bookStageService.getAllBookStageOfAUserService(id));

    }

    //metodos DELETE
    //RECIBE EL ID DEL BookStage Y LO ELIMINA DE LA LISTA DEL USUARIO LOGUEADO
    @Operation(
            summary = "Eliminar libro de la lista de favoritos de un usuario",
            description = "Permite a un usuario eliminar un libro de su lista de favoritos.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Libro eliminado",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookStage.class))
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookStageOfUserById(@PathVariable Long id){

        bookStageService.deleteBookStageOfUserById(id);

        return ResponseEntity.noContent().build();

    }

    //recibe el id de un usuario al cual se le quiera eliminar un bookStage y el id del bookStage a travez del dto
    @Operation(
            summary = "Eliminar libro de lista de favoritos (ADMIN)",
            description = "Permite a un admin eliminar un libro de la lista de favoritos de un usuario.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Libro eliminado de la lista."
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Usuario o BookStage no encontrado, o el usuario no contiene el libro en la lista",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)))

            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUser}/{idBook}")
    public ResponseEntity<Void> deleteBookStageOfAUserById(@PathVariable Long idUser, @PathVariable Long idBook){

        bookStageService.deleteBookStageOfAUserByDTO(idUser,idBook);

        return ResponseEntity.noContent().build();

    }



    //Metodos put
    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    @Operation(
            summary = "Actualiza el estado de lectura de un libro",
            description = "Permite a un usuario actualizar el estado de lectura de un libro de su lista.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estado de lectura actualizado correctamente."
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Usuario o BookStage no encontrado",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public ResponseEntity<BookStage> updateBookStage(@RequestBody BookStageDTO bookStageDTO){

        return ResponseEntity.ok(bookStageService.updateBookStage(bookStageDTO));

    }



}