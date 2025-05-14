package com.LibreriaApi.Repository;
import java.util.List;
import com.LibreriaApi.Model.BookStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookStageRepository extends JpaRepository<BookStage, Long> {
    Optional<BookStage> findByUserIdAndBookId(Long userId, Long bookId);
    List<BookStage> findByUserId(Long userId);
    List<BookStage> findByBookId(Long bookId);
    List<BookStage> findByStage(String stage);
    List<BookStage> findByStageContaining(String stage);
    List<BookStage> findByStageAndUserId(String stage, Long userId);
}