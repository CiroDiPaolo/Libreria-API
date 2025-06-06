package com.LibreriaApi.Control;

import com.LibreriaApi.Model.BookStage;
import com.LibreriaApi.Model.DTO.BookStageDTO;
import com.LibreriaApi.Service.BookStageService;
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
    @PostMapping("/{idBook}")
    public ResponseEntity<BookStage> createBookStage(@PathVariable Long idBook) {

        return ResponseEntity.ok(bookStageService.createService(idBook));
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

    @GetMapping("/all/{id}")
    public ResponseEntity<List<BookStage>> getAllBookStageOfAUser(@PathVariable Long id){

        return ResponseEntity.ok(bookStageService.getAllBookStageOfAUserService(id));

    }

    //metodos DELETE
    //recibe el id del stage y elimina el del usuario logueado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookStageOfUserById(@PathVariable Long id){

        bookStageService.deleteBookStageOfUserById(id);

        return ResponseEntity.noContent().build();

    }

    //recibe el id de un usuario al cual se le quiera eliminar un bookStage y el id del bookStage a travez del dto
    @DeleteMapping("/{idUser}/{idBook}")
    public ResponseEntity<Void> deleteBookStageOfAUserById(@PathVariable Long idUser, @PathVariable Long idBook){

        bookStageService.deleteBookStageOfAUserByDTO(idUser,idBook);

        return ResponseEntity.noContent().build();

    }

    //Metodos put

    @PutMapping()
    public ResponseEntity<BookStage> updateBookStage(@RequestBody BookStageDTO bookStageDTO){

        return ResponseEntity.ok(bookStageService.updateBookStage(bookStageDTO));

    }


}