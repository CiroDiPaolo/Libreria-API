package com.LibreriaApi.Control;

import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //metodos get

    @GetMapping("/{idUser}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long idUser) {

        return ResponseEntity.ok(userService.getUserById(idUser));

    }

    //metodo delete
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long idUser){

        userService.deleteUserById(idUser);

        return ResponseEntity.noContent().build();

    }

}
