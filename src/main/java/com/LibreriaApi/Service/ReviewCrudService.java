package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.AccessDeniedUserException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Multimedia;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.MultimediaRepository;
import com.LibreriaApi.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewCrudService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MultimediaRepository multimediaRepository;

    //Metodos GET

    public Review getReviewByIdService(Long id) {

        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + id));

    }

    public List<Review> getAllReviewsOfABookService(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Libro no encontrado con id: " + bookId);
        }
        return reviewRepository.findByMultimedia_Id(bookId);
    }

    //Metodos DELETE
    @Transactional
    public void deleteByIdService(Long idReview, Long idUser) {
       if(this.checkReviewBelongsToUser(idUser, idReview)){
           reviewRepository.logicallyDeleteById(idReview);
       }
    }

    //Metodo POST
    @Transactional
    public Review addReviewService(Review review, Long idUser) {
        if (idUser == null) {
            throw new IllegalArgumentException("El id del usuario no puede ser null");
        }

        // Asignar el usuario con el id que recibo
        UserEntity user = new UserEntity();
        user.setId(idUser);
        review.setUser(user);

        Long idMultimedia = review.getMultimedia() != null ? review.getMultimedia().getId() : null;
        if (idMultimedia == null) {
            throw new IllegalArgumentException("El id de multimedia no puede ser null");
        }

        Multimedia multimedia = multimediaRepository.findById(idMultimedia)
                .orElseThrow(() -> new EntityNotFoundException("Multimedia no encontrado con id: " + idMultimedia));

        review.setMultimedia(multimedia);

        return reviewRepository.save(review);
    }

    //Meteodo PUT

    @Transactional
    public Review updateReviewService(Long idReview,Review newReview, Long idUser) {
        if (!this.checkReviewBelongsToUser(idReview, idUser)) {
            throw new AccessDeniedUserException("La review no pertenece al usuario.");
        }

        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + idReview));

        review.setContent(newReview.getContent());
        review.setRating(newReview.getRating());

        return review;
    }

    public boolean checkReviewBelongsToUser(Long idUser, Long idReview){
        Optional<Review> review = reviewRepository.findById(idReview);
        if(review.isPresent()){
            if(review.get().getUser().getId() == idUser){
                return true;
            }else{
                throw new AccessDeniedUserException("La review no corresponde a su usuario");
            }
        }else{
            throw new EntityNotFoundException("Review no encontrada id = " + idReview);
        }
    }
}

