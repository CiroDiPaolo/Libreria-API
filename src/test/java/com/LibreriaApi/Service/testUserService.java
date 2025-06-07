package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
public class testUserService {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private UserService service;

    private UserEntity mockUser;
    private UserEntityDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        // Seteo el usuario de prueba
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPass("password123");
        mockUser.setStatus(true);
        mockUser.setRole(Role.USER);

        // Seteo el DTO
        mockUserDTO = new UserEntityDTO();
        mockUserDTO.setUsername("updateduser");
        mockUserDTO.setEmail("updated@example.com");
        mockUserDTO.setPassword("newpassword");
    }
}
