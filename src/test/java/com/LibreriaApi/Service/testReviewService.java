package com.LibreriaApi.Service;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
