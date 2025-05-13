package com.LibreriaApi.Control;

import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Service.BookStageService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookstage")
@AllArgsConstructor

public class BookStageController {
    private final BookStageService bookStageService;

    @GetMapping("/{id}")
    public BookStage getById(@PathVariable Long id) {
        return bookStageService.getBookStageService(id);
    }

    @GetMapping("/user/{userId}")
    public List<BookStage> getByUser(@PathVariable Long userId) {
        return bookStageService.getBookStagesByUserId(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<BookStage> getByBook(@PathVariable Long bookId) {
        return bookStageService.getBookStagesByBookId(bookId);
    }

    @PostMapping
    public void create(@RequestBody BookStage bookStage) {
        bookStageService.addBookStageService(bookStage);
    }

    @PutMapping
    public void update(@RequestBody BookStage bookStage) {
        bookStageService.updateBookStageService(bookStage);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookStageService.deleteBookStageService(id);
    }

    @GetMapping("/byUserAndBook")
    public Optional<BookStage> getByUserAndBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return bookStageService.getByUserIdAndBookId(userId, bookId);
    }
}