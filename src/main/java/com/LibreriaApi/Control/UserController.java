package com.LibreriaApi.Control;

import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Operaciones sobre users")
public class UserController {

    @Autowired
    private UserService userService;

    //metodos get
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{idUser}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long idUser) {

        return ResponseEntity.ok(userService.getUserById(idUser));

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping()
    public ResponseEntity<UserEntity> getUserLogged(){

        System.out.println(userService.getIdUserByToken());

        return ResponseEntity.ok(userService.getUserById(userService.getIdUserByToken()));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UserEntity> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers()).getBody();

    }

    //metodo delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long idUser){

        userService.deleteUserById(idUser);

        return ResponseEntity.noContent().build();

    }

    //metodo post
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntityDTO user){

        return ResponseEntity.ok(userService.updateUser(user));

    }

}
