package com.LibreriaApi.Service;

import com.LibreriaApi.Model.User;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    public void registerService(User user) {

        userRepository.save(user);

    }

}
