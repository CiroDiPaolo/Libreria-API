package com.LibreriaApi.Service;

import com.LibreriaApi.Control.BookCrudController;
import com.LibreriaApi.Enums.Stage;
import com.LibreriaApi.Exceptions.BookStageNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookStageRepository;
import com.LibreriaApi.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStageService {

    @Autowired
    private BookStageRepository bookStageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookCrudController bookCrudController;

    public BookStage createService(BookStageDTO bookStageDTO) {
        UserEntity user = userRepository.getById(userService.getIdUserByToken());
        Book book = bookCrudController.getBook(bookStageDTO.getIdBook());

        // Verifica si ya existe un BookStage para este usuario y libro
        Optional<BookStage> existing = bookStageRepository.findByUserAndBook(user, book);
        if (existing.isPresent()) {
            throw new BookStageNotFoundException("Book already in user's favorite list");
        }

        BookStage bookStage = new BookStage();
        bookStage.setBook(book);
        bookStage.setUser(user);
        bookStage.setStage(Stage.PENDIENTE);

        return bookStageRepository.save(bookStage);
    }

}