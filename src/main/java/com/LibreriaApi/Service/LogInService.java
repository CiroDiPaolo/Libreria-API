package com.LibreriaApi.Service;

import com.LibreriaApi.Model.User;
import com.LibreriaApi.Repository.LogInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogInService {

    @Autowired
    private LogInRepository repository;

    public Optional<User> logInService(String email, String password){

        return repository.findByEmailAndPass(email, password)
                .or(() -> {
                    System.out.println("No existe el usuario");
                    return Optional.empty();
                });


    }

}
