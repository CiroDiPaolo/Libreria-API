package com.LibreriaApi.Service;

import com.LibreriaApi.Config.JwtUtil;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Long getIdUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = authentication.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get().getId();
        } else {
            throw new EntityNotFoundException("El usuario no existe");
        }
    }

    //metodos get
    public UserEntity getUserById(Long id){

        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encontro usuario con el id: " + id));

    }

    //metodo delete
    public void deleteUserById(Long id){

        userRepository.deleteById(id);

    }


}