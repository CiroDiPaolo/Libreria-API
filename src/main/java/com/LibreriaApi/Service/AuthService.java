package com.LibreriaApi.Service;

import com.LibreriaApi.Config.JwtUtil;
import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Model.LogInRequest;
import com.LibreriaApi.Model.SignUpRequest;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(SignUpRequest request) {
        // VALIDO QUE EL EMAIL NO ESTE EN USO
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // SETEO LOS DATOS DEL USUARIO
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPass(passwordEncoder.encode(request.getPassword())); // ENCRIPTO LA CONTRASEÑA
        newUser.setRole(Role.USER); // ROL POR DEFECTO ES "USER"
        newUser.setStatus(true); // ESTADO EN TRUE

        userRepository.save(newUser); // GUARDO EL USUARIO
        return "Usuario registrado exitosamente";
    }

    public String authenticateUser(LogInRequest request) {
        // VALIDAR ESTADO DEL USUARIO ANTES DE AUTENTICAR
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent() && !userOpt.get().getStatus()) {
            throw new DisabledException("Su cuenta esta inhabilitada.");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return jwtUtil.generateToken(userDetails); // GENERO EL TOKEN Y SE LO PASO A LA CONTROLADORA
    }

}
