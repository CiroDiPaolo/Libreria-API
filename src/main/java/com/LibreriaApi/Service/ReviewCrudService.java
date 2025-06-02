package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.AccessDeniedUserException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.BookRepository;
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

    //Metodo PUT

    public Review addReviewService(Review review) {
        return reviewRepository.save(review);
    }

    //Meteodo POST

    @Transactional
    public Review updateReviewService(Long id,Review newReview) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review no encontrada con id: " + id));

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
            throw new EntityNotFoundException("Review no encontrada");
        }
    }
}

