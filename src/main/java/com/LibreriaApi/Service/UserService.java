package com.LibreriaApi.Service;

import com.LibreriaApi.Config.JwtUtil;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
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

    public List<UserEntity> getAllUsers(){

        return userRepository.getAllUsers();

    }

    //metodo delete
    public void deleteUserById(Long id){

        Optional<UserEntity> user = userRepository.findById(getIdUserByToken());

        if(user.isPresent()){

            user.get().setStatus(false);

        }

    }

    //metodo post
   public UserEntity updateUser(UserEntityDTO userDTO){

        Optional<UserEntity> userO = userRepository.findById(getIdUserByToken());

        if (userO.isPresent()) {

            UserEntity userEntity = userO.get();
            userEntity.setUsername(userDTO.getUsername());
            userEntity.setEmail(userDTO.getEmail());
            userEntity.setPass(userDTO.getPassword());
            PasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            userEntity.setPass(passwordEncoder.encode(userDTO.getPassword()));

            return userRepository.save(userEntity);
        } else {
            throw new EntityNotFoundException("Usuario no encontrado para actualizar");
        }

    }


}