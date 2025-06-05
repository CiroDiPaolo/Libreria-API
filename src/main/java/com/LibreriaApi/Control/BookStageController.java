package com.LibreriaApi.Control;

import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookIdDTO;
import com.LibreriaApi.Service.BookStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstage")
public class BookStageController {

    @Autowired
    private BookStageService bookStageService;


    @PostMapping
    public ResponseEntity<BookStage> createBookStage(@RequestBody BookIdDTO bookStageDTO) {

        return ResponseEntity.ok(bookStageService.createService(bookStageDTO));
    }


}