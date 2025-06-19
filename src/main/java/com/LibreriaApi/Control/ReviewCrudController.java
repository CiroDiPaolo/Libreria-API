package com.LibreriaApi.Control;

import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Service.ReviewService;
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
@RequestMapping("/review")
@Tag(name = "Review", description = "Operaciones sobre reviews")
public class ReviewCrudController {

    @Autowired
    private ReviewService reviewService;

    // GET
    @Operation(
            summary = "Obtener una reseña por ID",
            description = "Devuelve la reseña correspondiente al ID proporcionado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reseña con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @Parameter(description = "ID de la reseña a obtener", required = true)
            @PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewByIdService(id);
        return ResponseEntity.ok(review);
    }

    @Operation(
            summary = "Obtener todas las reseñas de un libro",
            description = "Devuelve todas las reseñas asociadas al libro con el ID especificado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas encontrada",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Review.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron reseñas para el libro con ese ID")
    })
    @GetMapping("/all/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsOfABook(
            @Parameter(description = "ID del libro para obtener sus reseñas", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getAllReviewsOfABookService(id));
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllActiveReviewsOfABook(
            @Parameter(description = "ID del libro para obtener reseñas activas", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getAllActiveReviewsOfABookService(id));
    }

    @GetMapping("/userReview/{id}")
    public ResponseEntity<ReviewDTO> getUserReviewByBookAndStatusActive(
            @Parameter(description = "ID del libro para obtener reseña activa del usuario", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewByUserAndBookAndStatusTrue(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<ReviewDTO> getUserReviewByBook(
            @Parameter(description = "ID del libro para obtener reseña del usuario", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewByUserAndBook(id));
    }

    // DELETE
    @Operation(
            summary = "Eliminar una review por ID",
            description = "Realiza la baja lógica de una reseña por su ID proporcionado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña eliminada(desactivada)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Review.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron reseñas con ese ID")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewUser(
            @Parameter(description = "ID de la reseña a eliminar (usuario)", required = true)
            @PathVariable Long id) {

        reviewService.deleteByIdServiceUser(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID de la reseña a eliminar (admin)", required = true)
            @PathVariable Long id) {

        reviewService.deleteByIdService(id);

        return ResponseEntity.noContent().build();
    }

    // POST
    @Operation(
            summary = "Agregar una reseña",
            description = "Agrega una reseña con los datos proporcionados"
    )
    @ApiResponse(responseCode = "200", description = "Reseña agregada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReviewDTO.class)))
    @PostMapping()
    public ResponseEntity<ReviewDTO> addReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la reseña a agregar",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class))
            )
            @Valid @RequestBody ReviewDTO review) {
        ReviewDTO createdReview = reviewService.addReviewService(review);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReview.getIdReview())
                .toUri();
        return ResponseEntity.created(location).body(createdReview);
    }

    // PUT
    @Operation(
            summary = "Actualizar una reseña",
            description = "Actualiza una reseña con los datos proporcionados"
    )
    @ApiResponse(responseCode = "200", description = "Reseña modificada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Review.class)))
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "ID de la reseña a actualizar", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la reseña",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class))
            )
            @Valid @RequestBody ReviewDTO reviewDTO) {

        ReviewDTO updatedReview = reviewService.updateReviewService(id, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }
}
