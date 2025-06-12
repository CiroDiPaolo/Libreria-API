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

    /*
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
*/
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
        // DESPUES DIVIDIR LA LOGICA EN UNA CLASE SignUpService
        try {
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
            newUser.setStatus(true); // SETEO ESTADO DEL USUARIO EN TRUE
            // GUARDO AL USUARIO EN LA BDD
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al ingresar los datos. " + e.getMessage());
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
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> authLogIn(@RequestBody @Valid LogInRequest request) {
        try {
            // VALIDAR ESTADO DEL USUARIO ANTES DE AUTENTICAR
            Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isPresent() && !userOpt.get().getStatus()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Su cuenta esta inhabilitada.");
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // DEVUELVE EL USUARIO AUNTENTICADO
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            // USA EL USUARIO AUTENTICADO PARA DEVOLVER EL TOKEN
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok().body(Collections.singletonMap("token", jwt));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


}
