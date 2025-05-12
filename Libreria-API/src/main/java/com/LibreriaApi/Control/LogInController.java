package com.LibreriaApi.Control;

import com.LibreriaApi.Model.LogInRequest;
import com.LibreriaApi.Service.LogInService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LogInController {

    @Autowired
    private LogInService logInService;

    @PutMapping("/")
    public void logIn(@RequestBody @Valid LogInRequest logInRequest) {

        if(logInService.logInService(logInRequest.getEmail(), logInRequest.getPassword()).isPresent()) {
            System.out.println("Usuario logueado");
        }

    }

}
