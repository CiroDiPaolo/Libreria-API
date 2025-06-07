package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Multimedia;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ReviewDTO result = reviewService.getReviewByIdService(testReviewId);

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
                () -> reviewService.getReviewByIdService(testReviewId)
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
        List<ReviewDTO> result = reviewService.getAllReviewsOfABookService(testBookId);

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
                () -> reviewService.getAllReviewsOfABookService(testBookId)
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
        List<ReviewDTO> result = reviewService.getAllActiveReviewsOfABookService(testBookId);

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

}
