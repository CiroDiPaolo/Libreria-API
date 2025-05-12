package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Review;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Review")
public class ReviewCrudController {


    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {

        return null;

    }

    @GetMapping("/all/{}")
    public Iterable<Review> getAllReviewsOfABook(@PathVariable int id) {

        return null;

    }

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable int id) {


    }

    @PostMapping()
    public void addReview(@RequestBody Review review) {


    }

    @PutMapping
    public void updateReview(@RequestBody Review review) {


    }



}
