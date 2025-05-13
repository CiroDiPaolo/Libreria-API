package com.LibreriaApi.Service;

import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class ReviewCrudService {

    @Autowired
    private ReviewRepository reviewRepository;

    //Metodos GET

    public Optional<Review> getReviewByIdService(Long id) {

        return reviewRepository.findById(id);

    }

    public Iterable<Review> getAllReviewsOfABookService(Long id) {

        return reviewRepository.findByMultimedia_Id(id);

    }

    //Metodos DELETE
    public void deleteByIdService(@PathVariable Long id) {

        Optional<Review> op = reviewRepository.findById(id);

        if(op.isPresent()){

            reviewRepository.deleteById(id);

        }

    }

    //Metodo PUT

    public void addReviewService(Review review) {

        reviewRepository.save(review);

    }

    //Meteodo POST

    public void updateReviewService(Review review) {

        Optional<Review> op = reviewRepository.findById(review.getIdReview());

        if(op.isPresent()){

            Review updatedReview = op.get();

            if(updatedReview.getIdReview() == review.getIdReview() ||
                    updatedReview.getMultimedia() == review.getMultimedia() ||
                    updatedReview.getUser() == review.getUser()){

                reviewRepository.save(review);


            }

        }

    }


}