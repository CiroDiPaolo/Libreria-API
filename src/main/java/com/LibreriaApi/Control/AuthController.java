package com.LibreriaApi.Control;

import com.LibreriaApi.Config.JwtUtil;
import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Model.LogInRequest;
import com.LibreriaApi.Model.SignUpRequest;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import com.LibreriaApi.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
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
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;


    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema con rol USER por defecto",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Usuario registrado exitosamente")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en los datos proporcionados o email ya existe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Error al ingresar los datos. El email ya está registrado")
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> authSignUp(@RequestBody @Valid SignUpRequest request) {
        try {

            String result = authService.registerUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al ingresar los datos. " + e.getMessage());
        }
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve un token JWT para acceder a endpoints protegidos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login exitoso, devuelve token JWT",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Object.class),
                                    examples = @ExampleObject(
                                            name = "Token JWT",
                                            value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Credenciales inválidas")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuario inactivo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Su cuenta esta inhabilitada.")
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> authLogIn(@RequestBody @Valid LogInRequest request) {
        try {

            String token = authService.authenticateUser(request);

            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


}
