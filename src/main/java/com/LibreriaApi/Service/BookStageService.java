package com.LibreriaApi.Service;

import com.LibreriaApi.Control.BookCrudController;
import com.LibreriaApi.Enums.Stage;
import com.LibreriaApi.Exceptions.BookStageNotFoundException;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookIdDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookStageRepository;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //POST

    public BookStage createService(BookIdDTO bookStageDTO) {

        Long userId = userService.getIdUserByToken();

        Optional<UserEntity> user = userRepository.findById(userId);

        Book book = bookCrudController.getBook(bookStageDTO.getIdBook());

        bookStageRepository.findByUserAndBook(user.orElse(null), book)
                .ifPresent(bs -> {
                    throw new BookStageNotFoundException("Este usuario ya tiene este libro en favoritos");
                });

        BookStage bookStage = new BookStage();

        bookStage.setBook(book);

        bookStage.setUser(user.get());

        bookStage.setStage(Stage.PENDIENTE);

        return bookStageRepository.save(bookStage);
    }

    //metodo get
    public BookStage getBookStageById(Long id){

        return bookStageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BookStage no encontrado con el id: " + id));
    }

}