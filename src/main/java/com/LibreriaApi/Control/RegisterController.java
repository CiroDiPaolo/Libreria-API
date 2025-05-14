package com.LibreriaApi.Control;

import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping()
    public void register(@RequestBody @Valid UserEntity user) {

        registerService.registerService(user);

    }

}
