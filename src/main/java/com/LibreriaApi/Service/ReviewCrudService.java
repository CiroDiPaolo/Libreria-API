package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.BookRepository;
import com.LibreriaApi.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
    public void deleteByIdService(Long id) {

        Optional<Review> op = reviewRepository.findById(id);

        if(op.isPresent()){

            reviewRepository.deleteById(id);

        }else{
            throw new EntityNotFoundException("Review no encontrada");
        }

    }

    //Metodo PUT

    public Review addReviewService(Review review) {
        return reviewRepository.save(review);
    }

    //Meteodo POST

    public Review updateReviewService(Long id, Review review) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Review no encontrada");
        }
        review.setIdReview(id); 
        return reviewRepository.save(review);
    }

}