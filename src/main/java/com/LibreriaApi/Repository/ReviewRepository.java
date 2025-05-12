package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Long, Review> {

        Iterable<Review> findByIdBook(int idBook);

}
