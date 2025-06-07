package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.AccessDeniedUserException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.MultimediaRepository;
import com.LibreriaApi.Repository.ReviewRepository;
import com.LibreriaApi.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //Metodos GET

    public ReviewDTO getReviewByIdService(Long id) {

        return this.toDTO(reviewRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + id)));


    }

    public List<ReviewDTO> getAllReviewsOfABookService(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        return reviewRepository.findByMultimedia_Id(bookId).stream().map(this::toDTO).toList();
    }

    public List<ReviewDTO> getAllActiveReviewsOfABookService(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        return reviewRepository.findByMultimediaIdAndStatusTrue(bookId).stream().map(this::toDTO).toList();
    }

    public ReviewDTO getReviewByUserAndBookAndStatusTrue(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        Long idUser = userService.getIdUserByToken();
        Optional<Review> review = reviewRepository.findByMultimediaIdAndUserIdAndStatusTrue(bookId, idUser);
        if (review.isPresent()){
            return this.toDTO(review.get());
        }else{
            throw new EntityNotFoundException("El usuario no tiene review del libro " + bookId);
        }
    }

    public ReviewDTO getReviewByUserAndBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        Long idUser = userService.getIdUserByToken();
        Optional<Review> review = reviewRepository.findByMultimediaIdAndUserId(bookId, idUser);
        if (review.isPresent()){
            return this.toDTO(review.get());
        }else{
            throw new EntityNotFoundException("El usuario no tiene review del libro " + bookId);
        }
    }

    //Metodos DELETE
    @Transactional
    public void deleteByIdServiceUser(Long idReview) {
        Long idUser = userService.getIdUserByToken();
       if(this.checkReviewBelongsToUser(idUser, idReview)){
           reviewRepository.logicallyDeleteById(idReview);
       }
    }

    @Transactional
    public void deleteByIdService(Long idReview) {
        if(reviewRepository.existsById(idReview)){
            reviewRepository.logicallyDeleteById(idReview);
        }else{
            throw new EntityNotFoundException("La review con id " + idReview + " no existe");
        }
    }

    //Metodo POST
    @Transactional
    public ReviewDTO addReviewService(ReviewDTO review) {
        Long idUser = userService.getIdUserByToken();
        review.setIdUser(idUser);
        review.setStatus(true);
        return this.toDTO(reviewRepository.save(this.toModel(review)));
    }

    //Meteodo PUT

    @Transactional
    public ReviewDTO updateReviewService(Long idReview,ReviewDTO reviewDTO) {
        Long idUser = userService.getIdUserByToken();
        this.checkReviewBelongsToUser(idUser, idReview);

        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + idReview));

        if(!review.getStatus()){
            throw new AccessDeniedUserException("La reseña esta eliminada");
        }
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());

        return this.toDTO(review);
    }

    public boolean checkReviewBelongsToUser(Long idUser, Long idReview){
        Optional<Review> review = reviewRepository.findById(idReview);
        if(review.isPresent()){
            if(Objects.equals(review.get().getUser().getId(), idUser)){
                return true;
            }else{
                throw new AccessDeniedUserException("La review no corresponde a su usuario");
            }
        }else{
            throw new EntityNotFoundException("Review no encontrada id = " + idReview);
        }
    }

    public ReviewDTO toDTO (Review review){
        return new ReviewDTO(
                review.getIdReview(),
                review.getRating(),
                review.getContent(),
                review.getStatus(),
                review.getUser().getId(),
                review.getMultimedia().getId()
        );
    }

    public Review toModel (ReviewDTO reviewDTO){
        Long idMultimedia = reviewDTO.getIdMultimedia();
        Multimedia multimedia = multimediaRepository.findById(idMultimedia)
                .orElseThrow(() -> new EntityNotFoundException("Multimedia no encontrado con id: " + idMultimedia));

        Long idUser = reviewDTO.getIdUser();
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUser));
        return new Review(
                reviewDTO.getIdReview(),
                reviewDTO.getRating(),
                reviewDTO.getContent(),
                reviewDTO.getStatus(),
                user,
                multimedia
        );
    }
}

