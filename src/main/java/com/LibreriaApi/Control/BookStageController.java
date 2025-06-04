package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import com.LibreriaApi.Service.BookStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstage")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;

    @Autowired
    private BookCrudController bookCrudController;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookStage> createBookStage(@RequestBody BookStageDTO bookStageDTO) {

        System.out.println("prueba");

        Book book = bookCrudController.getBook(bookStageDTO.getIdBook());

        UserEntity user = userRepository.getById(bookStageDTO.getIdUser());

        return ResponseEntity.ok(bookStageService.createService(user,book));
    }


}