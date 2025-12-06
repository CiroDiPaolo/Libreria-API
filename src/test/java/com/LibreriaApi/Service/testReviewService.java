package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.AccessDeniedUserException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.MultimediaRepository;
import com.LibreriaApi.Repository.ReviewRepository;
import com.LibreriaApi.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class testReviewService {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private MultimediaRepository multimediaRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review testReview;
    private ReviewDTO testReviewDTO;
    private UserEntity testUser;
    private Book testBook;
    private Long testUserId = 1L;
    private Long testBookId = 1L;
    private Long testReviewId = 1L;

    @BeforeEach
    void setUp() {
        // Seteo solo el ID del usuario de prueba, por que no hace falta nada mas
        testUser = new UserEntity();
        testUser.setId(testUserId);
        // Con el libro lo mismo, solo seteo su ID
        testBook = new Book();
        testBook.setId(testBookId);

        // Y seteo la Review con su DTO
        testReview = new Review();
        testReview.setIdReview(testReviewId);
        testReview.setRating(5);
        testReview.setContent("Excelente libro");
        testReview.setStatus(true);
        testReview.setUser(testUser);
        testReview.setMultimedia(testBook);

        testReviewDTO = new ReviewDTO(
                testReviewId,
                5,
                "Excelente libro",
                true,
                testUserId,
                testBookId
        );
    }

    // METODOS GET ////////////////////////////////////

    // OBTIENE UNA REVIEW EXISTENTE POR SU ID
    @Test
    void getReviewByIdService_WhenReviewExists_ShouldReturnReviewDTO() {
        // Given
        when(reviewRepository.findById(testReviewId)).thenReturn(Optional.of(testReview));

        // When
        ReviewDTO result = reviewService.getReviewById(testReviewId);

        // Then
        // Aca voy a forzar que falle
        assertNull(result); // Tiene que ser assertNotNull
        assertEquals(testReviewId, result.getIdReview());
        assertEquals(10, result.getRating()); // Tendria que ser 5 == 5
        assertEquals("Libro malisimo", result.getContent()); // Tendira que ser "Excelente libro"
        verify(reviewRepository).findById(testReviewId);
    }

    // BUSCA UNA REVIEW QUE NO EXISTE POR SU ID, TIENE QUE ARROJAR UNA EXCEPCION
    @Test
    void getReviewByIdService_WhenReviewNotExists_ShouldThrowEntityNotFoundException() {
        // Given
        when(reviewRepository.findById(testReviewId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.getReviewById(testReviewId)
        );
        assertEquals("Review no encontrada con id: " + testReviewId, exception.getMessage());
    }

    // OBTIENE TODAS LAS RESEÑAS DE UN LIBRO QUE SI EXISTE, TIENE QUE RETORNAR UNA LISTA CON LAS REVIEW
    @Test
    void getAllReviewsOfABookService_WhenBookExists_ShouldReturnReviewList() {
        // Given
        List<Review> reviews = Arrays.asList(testReview);
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(reviewRepository.findByMultimedia_Id(testBookId)).thenReturn(reviews);

        // When
        List<ReviewDTO> result = reviewService.getAllReviewsOfABook(testBookId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testReviewId, result.get(0).getIdReview());
        verify(bookRepository).existsById(testBookId);
        verify(reviewRepository).findByMultimedia_Id(testBookId);
    }

    // OBTENER TODAS LAS RESEÑAS DE UN LIBRO QUE NO EXISTE, TENDRIA QUE ARROJAR UNA EXCEPCION
    @Test
    void getAllReviewsOfABookService_WhenBookNotExists_ShouldThrowEntityNotFoundException() {
        // Given
        when(bookRepository.existsById(testBookId)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.getAllReviewsOfABook(testBookId)
        );
        assertEquals("Libro no encontrado con id: " + testBookId, exception.getMessage());
    }

    // OBTIENE TODAS LAS RESEÑAS ACTIVAS DE UN LIBRO QUE SI EXISTE
    @Test
    void getAllActiveReviewsOfABookService_WhenBookExists_ShouldReturnActiveReviews() {
        // Given
        List<Review> activeReviews = Arrays.asList(testReview);
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(reviewRepository.findByMultimediaIdAndStatusTrue(testBookId)).thenReturn(activeReviews);

        // When
        List<ReviewDTO> result = reviewService.getAllActiveReviewsOfABook(testBookId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getStatus());
        verify(reviewRepository).findByMultimediaIdAndStatusTrue(testBookId);
    }

    // OBTIENE LAS RESEÑA ACTIVA DE UN USUARIO EN UN LIBRO, SUPONIENDO QUE TIENE UNA
    @Test
    void getReviewByUserAndBookAndStatusTrue_WhenReviewExists_ShouldReturnReviewDTO() {
        // Given
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(userService.getIdUserByToken()).thenReturn(testUserId);
        when(reviewRepository.findByMultimediaIdAndUserIdAndStatusTrue(testBookId, testUserId))
                .thenReturn(Optional.of(testReview));

        // When
        ReviewDTO result = reviewService.getReviewByUserAndBookAndStatusTrue(testBookId);

        // Then
        assertNotNull(result);
        assertEquals(testReviewId, result.getIdReview());
        verify(userService).getIdUserByToken();
        verify(reviewRepository).findByMultimediaIdAndUserIdAndStatusTrue(testBookId, testUserId);
    }

    // OBTIENE LAS RESEÑA ACTIVA DE UN USUARIO EN UN LIBRO
    // EN CASO DE QUE NO SE CUMPLA UNA CONDICION DEBE ARROJAR UNA EXCEPCION
    @Test
    void getReviewByUserAndBookAndStatusTrue_WhenReviewNotExists_ShouldThrowEntityNotFoundException() {
        // Given
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(userService.getIdUserByToken()).thenReturn(testUserId);
        when(reviewRepository.findByMultimediaIdAndUserIdAndStatusTrue(testBookId, testUserId))
                .thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reviewService.getReviewByUserAndBookAndStatusTrue(testBookId)
        );
        assertEquals("El usuario no tiene review del libro " + testBookId, exception.getMessage());
    }

    // OBTIENE LAS RESEÑAS DE UN USUARIO EN UN LIBRO, EN CASO DE QUE TENGA REVIEWS HECHAS
    @Test
    void getReviewByUserAndBook_WhenReviewExists_ShouldReturnReviewDTO() {
        // Given
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(userService.getIdUserByToken()).thenReturn(testUserId);
        when(reviewRepository.findByMultimediaIdAndUserId(testBookId, testUserId))
                .thenReturn(Optional.of(testReview));

        // When
        ReviewDTO result = reviewService.getReviewByUserAndBook(testBookId);

        // Then
        assertNotNull(result);
        assertEquals(testReviewId, result.getIdReview());
        verify(reviewRepository).findByMultimediaIdAndUserId(testBookId, testUserId);
    }

    // METODOS DELETE /////////////////////

    // ELIMINA UNA REVIEW POR SU ID
    @Test
    void deleteByIdService_Success() {
        // Arrange
        when(reviewRepository.existsById(1L)).thenReturn(true);

        // Act
        reviewService.deleteById(1L);

        // Assert
        verify(reviewRepository).logicallyDeleteById(1L);
    }

    // INTENTA ELIMINAR UNA REVIEW POR SU ID PERO NO EXISTE
    @Test
    void deleteByIdService_ReviewNotExists() {
        // Arrange
        when(reviewRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.deleteById(999L)
        );
        assertEquals("La review con id 999 no existe", exception.getMessage());
        verify(reviewRepository, never()).logicallyDeleteById(anyLong());
    }

    // METODOS CREATE ////////////////////////

    // AGREGA UNA RESEÑA
    @Test
    void addReviewService_Success() {
        // Arrange
        // Simulo que estoy ingresando una reseña:
        ReviewDTO inputDTO = new ReviewDTO(null, 4, "Buen libro", null, null, 1L);
        when(userService.getIdUserByToken()).thenReturn(1L);
        when(multimediaRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // Act
        ReviewDTO result = reviewService.addReview(testReviewDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertTrue(result.getStatus());
        assertEquals("Excelente libro", result.getContent());
        assertEquals(5, result.getRating());

        verify(userService).getIdUserByToken();
        verify(reviewRepository).save(any(Review.class));
    }

    // INTENTA AGREGAR UNA RESEÑA PERO EL LIBRO NO EXISTE
    @Test
    void addReviewService_BookNotFound() {
        // Arrange
        when(userService.getIdUserByToken()).thenReturn(1L);
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.addReview(testReviewDTO)
        );
        assertEquals("Multimedia no encontrado con id: 999", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    // INTENTA AGREGAR UNA RESEÑA PERO EL USUARIO NO EXISTE
    @Test
    void addReviewService_UserNotFound() {
        // Arrange
        when(userService.getIdUserByToken()).thenReturn(999L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.addReview(testReviewDTO)
        );
        assertEquals("Usuario no encontrado con id: 999", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    // METODOS UPDATE /////////////////////////

    // ACTUALIZA UNA RESEÑA
    @Test
    void updateReviewService_Success() {
        // Arrange
        ReviewDTO updateDTO = new ReviewDTO(1L, 3, "Contenido actualizado", true, 1L, 1L);
        when(userService.getIdUserByToken()).thenReturn(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // Act
        ReviewDTO result = reviewService.updateReview(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Contenido actualizado", result.getContent());
        assertEquals(3, result.getRating());
        assertEquals(testReview.getContent(), "Contenido actualizado");
        assertEquals(testReview.getRating(), 3);
    }

    // ACTUALIZA UNA RESEÑA PERO NO EXISTE
    @Test
    void updateReviewService_ReviewNotFound() {
        // Arrange
        ReviewDTO updateDTO = new ReviewDTO(1L, 3, "Contenido actualizado", true, 1L, 1L);
        when(userService.getIdUserByToken()).thenReturn(1L);
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.updateReview(999L, updateDTO)
        );
        assertEquals("Review no encontrada id = 999", exception.getMessage());
    }

    // ACTUALIZA UNA RESEÑA PERO NO PERTENECE AL USUARIO
    @Test
    void updateReviewService_ReviewNotBelongsToUser() {
        // Arrange
        ReviewDTO updateDTO = new ReviewDTO(1L, 3, "Contenido actualizado", true, 2L, 1L);
        Review reviewFromOtherUser = new Review();
        reviewFromOtherUser.setIdReview(1L);
        reviewFromOtherUser.setStatus(true);
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        reviewFromOtherUser.setUser(otherUser);

        when(userService.getIdUserByToken()).thenReturn(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewFromOtherUser));

        // Act & Assert
        AccessDeniedUserException exception = assertThrows(
                AccessDeniedUserException.class,
                () -> reviewService.updateReview(1L, updateDTO)
        );
        assertEquals("La review no corresponde a su usuario", exception.getMessage());
    }

    // ACTUALIZA UNA RESEÑA PERO ESTA DADA DE BAJA
    @Test
    void updateReviewService_ReviewDeleted() {
        // Arrange
        ReviewDTO updateDTO = new ReviewDTO(1L, 3, "Contenido actualizado", true, 1L, 1L);
        testReview.setStatus(false); // Review eliminada
        when(userService.getIdUserByToken()).thenReturn(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // Act & Assert
        AccessDeniedUserException exception = assertThrows(
                AccessDeniedUserException.class,
                () -> reviewService.updateReview(1L, updateDTO)
        );
        assertEquals("La reseña esta eliminada", exception.getMessage());
    }

}
