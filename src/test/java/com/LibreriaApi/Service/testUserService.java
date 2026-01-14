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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    // EN SPRING ES MAS COMUN TRABAJAR CON AAA (Arrange, Act & Assert)
    // Arrange = prepara los mocks, Act = llama a los metodos del service, Assert = Verifica los resultados

    // TESTS PARA getIdUserByToken() ////////////////////////

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

    // TEST PARA METODOS GET ///////////////////////////

    // OBTIENE UN USUARIO QUE EXISTE
    @Test
    void getUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        UserEntity result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getUsername(), result.getUsername());
        verify(userRepository).findById(1L);
    }

    // BUSCA OBTENER UN USUARIO QUE NO EXISTE
    @Test
    void getUserById_WhenUserNotExists_ThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(1L));
        assertEquals("No se encontro usuario con el id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    // OBTIENE A TODOS LOS USUARIOS
    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("user2");
        List<UserEntity> expectedUsers = Arrays.asList(mockUser, user2);
        //when(userRepository.getAllUsers()).thenReturn(expectedUsers);
        Pageable pageable = PageRequest.of(0, 2);
        // Act
        List<UserEntity> result = (userService.findAll(pageable)).getContent();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        //verify(userRepository).getAllUsers();
    }

    // TEST PARA METODOS DELETE ///////////////////////////

    // ELIMINA UN USUARIO QUE EXISTE
    @Test
    void deleteUserById_WhenUserExists_SetsStatusToFalse() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

            // Act
            userService.deleteUserById(1L);

            // Assert
            // assertFalse(mockUser.getStatus());
            verify(userRepository).findById(1L);
        }
    }

    // INTENTA ELIMINAR UN USUAIRO QUE NO EXISTE
    @Test
    void deleteUserById_WhenUserNotExists_DoesNothing() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            userService.deleteUserById(1L);

            // Assert
            //assertTrue(mockUser.getStatus()); // Status should remain unchanged
            verify(userRepository).findById(1L);
        }
    }

    // TEST PARA UPDATE //////////////////////////////

    // ACTUALIZA UN USUARIO QUE EXISTE
    @Test
    void updateUser_WhenUserExists_UpdatesAndReturnsUser() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

            // Act
            UserEntity result = userService.updateUser(mockUserDTO);

            // Assert
            assertNotNull(result);
            assertEquals(mockUserDTO.getUsername(), mockUser.getUsername());
            assertEquals(mockUserDTO.getEmail(), mockUser.getEmail());
            assertEquals(mockUserDTO.getPassword(), mockUser.getPass());
            verify(userRepository).findById(1L);
            verify(userRepository).save(mockUser);
        }
    }

    // ACTUALIZA UN USUARIO QUE NO EXISTE
    @Test
    void updateUser_WhenUserNotExists_ThrowsEntityNotFoundException() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.updateUser(mockUserDTO));
            assertEquals("Usuario no encontrado para actualizar", exception.getMessage());
            verify(userRepository).findById(1L);
            verify(userRepository, never()).save(any(UserEntity.class));
        }
    }

    // ACTUALIZA UN USUARIO CUYA AUTENTICACION FALLA
    @Test
    void updateUser_WhenAuthenticationFails_ThrowsRuntimeException() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Arrange
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.updateUser(mockUserDTO));
            assertEquals("Usuario no autenticado", exception.getMessage());
            verify(userRepository, never()).save(any(UserEntity.class));
        }
    }

}
