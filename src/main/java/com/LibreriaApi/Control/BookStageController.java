package com.LibreriaApi.Control;

import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookIdDTO;
import com.LibreriaApi.Service.BookStageService;
import com.LibreriaApi.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstage")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;

    //Metodo post
    @PostMapping
    public ResponseEntity<BookStage> createBookStage(@RequestBody BookIdDTO bookStageDTO) {

        return ResponseEntity.ok(bookStageService.createService(bookStageDTO));
    }

    //Metodo get
    @GetMapping("/{id}")
    public ResponseEntity<BookStage> getBookStageById(@PathVariable Long id){

        return ResponseEntity.ok(bookStageService.getBookStageById(id));

    }

    @GetMapping("/all")
    public ResponseEntity<List<BookStage>> getAllBookStageOfUser(){

        return ResponseEntity.ok(bookStageService.getAllBookStageOfUserService());

    }

}