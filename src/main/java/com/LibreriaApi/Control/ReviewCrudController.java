package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Review;
import com.LibreriaApi.Repository.ReviewRepository;
import com.LibreriaApi.Service.ReviewCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewCrudController {

    @Autowired
    private ReviewCrudService reviewCrudService;

    //GET

    @GetMapping("/{id}")
    public Optional<Review> getReviewById(@PathVariable Long id) {

        return reviewCrudService.getReviewByIdService(id);

    }

    @GetMapping("/all/{}")
    public Iterable<Review> getAllReviewsOfABook(@PathVariable Long id) {

        return reviewCrudService.getAllReviewsOfABookService(id);

    }

    //DELETE

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable Long id) {

        reviewCrudService.deleteByIdService(id);

    }

    //POST

    @PostMapping()
    public void addReview(@RequestBody Review review) {

        reviewCrudService.addReviewService(review);

    }

    //PUT

    @PutMapping()
    public void updateReview(@RequestBody Review review) {

        reviewCrudService.updateReviewService(review);

    }

}
