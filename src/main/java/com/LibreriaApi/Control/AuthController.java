package com.LibreriaApi.Control;

import com.LibreriaApi.Config.JwtUtil;
import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Model.LogInRequest;
import com.LibreriaApi.Model.SignUpRequest;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // DESPUES DIVIDIR LA LOGICA EN UNA CLASE SignUpService
    @PostMapping("/register")
    public ResponseEntity<String> authSignUp(@RequestBody @Valid SignUpRequest request) {
        try {
            System.out.println("Entrando a /auth/register...");
            if (userRepository.existsByEmail(request.getEmail())) {
                // ESTO LO ARREGLA THE BREAKBALLS (meli)
                throw new RuntimeException("El email ya está registrado");
            }

            // CREO UN USUARIO Y SETEO SUS ATRIBUTOS CON LOS DATOS QUE VIENEN DEL SIGNUPREQUEST
            UserEntity newUser = new UserEntity();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPass(passwordEncoder.encode(request.getPassword()));
            newUser.setRole(Role.USER); // ROLE IGUAL A USER POR DEFECTO
            // GUARDO AL USUARIO EN LA BDD
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al ingresar los datos. " + e.getMessage());
        }
    }


    //LOGIN CON JWT
    @PostMapping("/login")
    public ResponseEntity<?> authLogIn(@RequestBody @Valid LogInRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // DEVUELVE EL USUARIO AUNTENTICADO
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            // USA EL USUARIO AUTENTICADO PARA DEVOLVER EL TOKEN
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok().body(Collections.singletonMap("token", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


}
