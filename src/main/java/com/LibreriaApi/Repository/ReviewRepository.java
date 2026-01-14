package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.DTO.ReviewDTO;
import com.LibreriaApi.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

        Page<Review> findByMultimedia_Id(Long id, Pageable pageable);
        @Modifying
        @Query("update Review r set r.status = false where r.id = :id")
        void logicallyDeleteById(@Param("id") Long id);

        boolean existsByUser_IdAndStatusTrueAndMultimedia_Id(Long idUser, Long multimediaId);

        Page<Review> findByMultimedia_IdAndStatusTrue(Long idMultimedia, Pageable pageable);
        Optional<Review> findByMultimediaIdAndUserIdAndStatusTrue(Long idMultimedia, Long idUser);

        Optional<Review> findByMultimediaIdAndUserId(Long idMultimedia, Long idUser);
        @Query("SELECT r FROM Review r WHERE r.user.id = :idUser")
        List<Review> findReviewActiveOfUserById(Long idUser);
}
