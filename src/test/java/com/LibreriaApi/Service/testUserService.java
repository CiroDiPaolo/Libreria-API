package com.LibreriaApi.Service;

import com.LibreriaApi.Enums.Role;
import com.LibreriaApi.Exceptions.EntityNotFoundException;
import com.LibreriaApi.Model.DTO.UserEntityDTO;
import com.LibreriaApi.Model.UserEntity;
import com.LibreriaApi.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class testUserService {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private UserService userService;

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

    // TESTS PARA getUserByToken() ////////////////////////

    // SI EL USUARIO ESTA AUTENTICADO, DEVUELVE SU ID
    @Test
    void getIdUserByToken_WhenUserAuthenticated_ReturnsUserId() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

            // Act
            Long result = userService.getIdUserByToken();

            // Assert
            assertEquals(1L, result);
            verify(userRepository).findByEmail("test@example.com");
        }
    }

    // EL USUARIO NUNCA SE LOGUEO, (authenticacion == null)
    @Test
    void getIdUserByToken_WhenNoAuthentication_ThrowsRuntimeException() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.getIdUserByToken());
            assertEquals("Usuario no autenticado", exception.getMessage());
        }
    }

    // EL USUARIO FALLO EL INTENTO DE AUTENTICACION, (authentication.isAuthenticated() == false)
    @Test
    void getIdUserByToken_WhenNotAuthenticated_ThrowsRuntimeException() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.getIdUserByToken());
            assertEquals("Usuario no autenticado", exception.getMessage());
        }
    }

    // EL USUARIO NO EXISTE
    // Es raro que suceda xq tenemos su token, pero puede suceder en aplicaciones reales.
    @Test
    void getIdUserByToken_WhenUserNotFound_ThrowsEntityNotFoundException() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("nonexistent@example.com");
            when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.getIdUserByToken());
            assertEquals("El usuario no existe", exception.getMessage());
        }
    }
}
