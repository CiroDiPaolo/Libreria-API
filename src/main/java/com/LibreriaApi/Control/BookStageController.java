package com.LibreriaApi.Control;

import com.LibreriaApi.Model.Book;
import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import com.LibreriaApi.Service.BookStageService;
import com.LibreriaApi.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstage")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;


    @PostMapping
    public ResponseEntity<BookStage> createBookStage(@RequestBody BookStageDTO bookStageDTO) {

        return ResponseEntity.ok(bookStageService.createService(bookStageDTO));
    }


}