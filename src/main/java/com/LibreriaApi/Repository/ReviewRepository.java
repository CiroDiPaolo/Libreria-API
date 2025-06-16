package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

        List<Review> findByMultimedia_Id(Long id);
        @Modifying
        @Query("update Review r set r.status = false where r.id = :id")
        void logicallyDeleteById(@Param("id") Long id);

        boolean existsByUser_IdAndStatusTrueAndMultimedia_Id(Long idUser, Long multimediaId);

        List<Review> findByMultimediaIdAndStatusTrue(Long idMultimedia);
        Optional<Review> findByMultimediaIdAndUserIdAndStatusTrue(Long idMultimedia, Long idUser);

        Optional<Review> findByMultimediaIdAndUserId(Long idMultimedia, Long idUser);
}
