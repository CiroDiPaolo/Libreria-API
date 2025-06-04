package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Stage;
import com.LibreriaApi.Exceptions.BookStageNotFoundException;
import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.BookStageRepository;
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

    public BookStage createService(UserEntity user, Book book){

        BookStage bookStage = new BookStage();

        bookStage.setBook(book);

        bookStage.setUser(user);

        bookStage.setStage(Stage.PENDIENTE);

        return bookStageRepository.save(bookStage);

    }


}