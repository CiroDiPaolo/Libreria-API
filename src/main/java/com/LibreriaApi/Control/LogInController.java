package com.LibreriaApi.Control;

import com.LibreriaApi.Model.LogInRequest;
import com.LibreriaApi.Service.LogInService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LogInController {

    @Autowired
    private LogInService logInService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PutMapping("/")
    public void logIn(@RequestBody @Valid LogInRequest logInRequest) {

        if(logInService.logInService(logInRequest.getEmail(), logInRequest.getPassword()).isPresent()) {
            System.out.println("Usuario logueado");
        }

    }

    @PutMapping("/auth")
    public ResponseEntity<String> authLogIn(@RequestBody @Valid LogInRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            return ResponseEntity.ok("Autenticación exitosa");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

}
