package com.LibreriaApi.Config;

import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;
    
    @Override
    public void run(String... args) throws Exception {
        String email = adminEmail;
        if (!userRepository.existsByEmail(adminEmail)) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPass(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setStatus(true);
            userRepository.save(admin);
            System.out.println("Usuario administrador creado por defecto.");
        } else {
            System.out.println("El usuario admin ya existe. No se crea uno nuevo.");
        }
    }
}
