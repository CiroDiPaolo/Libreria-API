package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

        Iterable<Review> findByMultimedia(Long id);

        Optional<Review> findById(Long id);
}
