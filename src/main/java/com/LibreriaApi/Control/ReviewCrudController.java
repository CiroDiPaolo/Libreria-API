package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Security.UserEntityDetails;
import com.LibreriaApi.Service.MultimediaService;
import com.LibreriaApi.Service.ReviewCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/review")
@Tag(name = "Review", description = "Operaciones sobre reviews")
public class ReviewCrudController {

    @Autowired
    private ReviewCrudService reviewCrudService;

    //GET
    @Operation(
            summary = "Obtener una reseña por ID",
            description = "Devuelve la reseña correspondiente al ID proporcionado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reseña encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontró ninguna reseña con ese ID"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewCrudService.getReviewByIdService(id);
        return ResponseEntity.ok(review);
    }

    @Operation(
            summary = "Obtener todas las reseñas de un libro",
            description = "Devuelve todas las reseñas asociadas al libro con el ID especificado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de reseñas encontrada",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Review.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontraron reseñas para el libro con ese ID"
                    )
            }
    )
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Review>> getAllReviewsOfABook(@PathVariable Long id) {
        return ResponseEntity.ok(reviewCrudService.getAllReviewsOfABookService(id));
    }

    //DELETE
    @Operation(
            summary = "Eliminar una review por ID",
            description = "Realiza la baja lógica de una reseña por su ID proporcionado",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reseña eliminada(desactivada)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontraron reseñas con ese ID"
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewUser(@PathVariable Long id, @AuthenticationPrincipal UserEntityDetails userDetails) {

        Long idUser = userDetails.getId();
        reviewCrudService.deleteByIdService(id, idUser);

        return ResponseEntity.noContent().build();
    }

    //POST
    @Operation(
            summary = "Agregar una reseña",
            description = "Agrega una reseña con los datos proporcionados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reseña agregada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class))
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<Review> addReview(@RequestBody Review review, @AuthenticationPrincipal UserEntityDetails userDetails) {

        review.setStatus(true);
        Review createdReview = reviewCrudService.addReviewService(review, userDetails.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReview.getIdReview())
                .toUri();
        return ResponseEntity.created(location).body(createdReview);
    }

    //PUT
    @Operation(
            summary = "Actualizar una reseña",
            description = "Actualiza una reseña con los datos proporcionados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reseña modificada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review, @AuthenticationPrincipal UserEntityDetails userDetails) {
        Review updatedReview = reviewCrudService.updateReviewService(id, review, userDetails.getId());
        return ResponseEntity.ok(updatedReview);
    }

}
