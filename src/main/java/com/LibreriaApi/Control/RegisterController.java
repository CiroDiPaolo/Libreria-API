package com.LibreriaApi.Control;

import com.LibreriaApi.Model.User;
import com.LibreriaApi.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PutMapping("/")
    public void register(@RequestBody @Valid User user) {

        registerService.registerService(user);

    }

}
