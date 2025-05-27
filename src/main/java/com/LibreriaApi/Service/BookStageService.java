package com.LibreriaApi.Service;

import com.LibreriaApi.Exceptions.BookStageNotFoundException;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Repository.BookStageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor

public class BookStageService {
    @Autowired
    private BookStageRepository bookStageRepository;

    public BookStage getBookStageService(Long id) {
        return bookStageRepository.findById(id)
                .orElseThrow(() -> new BookStageNotFoundException("No se encontró el stage para el libro " + id));
    }

    public List<BookStage> getAllBookStagesService() {
        return bookStageRepository.findAll();
    }

    public List<BookStage> getBookStagesByUserId(Long userId) {
        return bookStageRepository.findByUserId(userId);
    }

    public List<BookStage> getBookStagesByBookId(Long bookId) {
        return bookStageRepository.findByBookId(bookId);
    }

    public List<BookStage> getBookStagesByStage(String stage) {
        return bookStageRepository.findByStage(stage);
    }

    public List<BookStage> getBookStagesByStageAndUserId(String stage, Long userId) {
        return bookStageRepository.findByStageAndUserId(stage, userId);
    }

    public void addBookStageService(BookStage bookStage) {
        bookStageRepository.save(bookStage);
    }

    public Optional<BookStage> getByUserIdAndBookId(Long userId, Long bookId) {
        return bookStageRepository.findByUserIdAndBookId(userId, bookId);
    }

    public void updateBookStageService(BookStage bookStage) {
        Optional<BookStage> existing = bookStageRepository.findById(bookStage.getId());
        if (existing.isPresent()) {
            bookStageRepository.save(bookStage);
        } else {
            throw new BookStageNotFoundException("No se encontró un estado para el id: " + bookStage.getId());
        }
    }

    public void deleteBookStageService(Long id) {
        Optional<BookStage> bookStage = bookStageRepository.findById(id);
        if (bookStage.isPresent()) {
            bookStageRepository.deleteById(id);
        } else {
            throw new BookStageNotFoundException("no se encontró un estado para eliminar para el id: " + id);
        }
    }
}