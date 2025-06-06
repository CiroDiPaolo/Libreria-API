package com.LibreriaApi.Control;

import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //metodos get
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{idUser}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long idUser) {

        return ResponseEntity.ok(userService.getUserById(idUser));

    }

    //metodo delete
    @PreAuthorize("hasrole('ADMIN')")
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
