package com.example.user_service.services.user;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entities.User;
import com.example.user_service.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest(
                "test@example.com",
                "Test User",
                "testuser",
                "password123"
        );

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setLogin("testuser");
        user.setPassword("hashedPassword");
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Test User");
    }

    @Test
    void createUser_WhenUserDoesNotExist_ShouldCreateUser() {
        // Arrange
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse result = userService.createUser(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getUsername(), result.getUsername());

        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenUserWithEmailExists_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(userRequest));

        assertEquals("Пользователь с email: " + userRequest.getEmail() + " уже существует!",
                exception.getMessage());

        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserResponse result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getUsername(), result.getUsername());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(999L));

        assertEquals("Пользователь с ID: 999 не найден", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void partialUpdateUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        UserRequest updateRequest = new UserRequest(
                "new@example.com",
                "New Username",
                "newlogin",
                "newpassword"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByLogin("newlogin")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse result = userService.partialUpdateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).existsByLogin("newlogin");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void partialUpdateUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.partialUpdateUser(999L, userRequest));

        assertEquals("Пользователь с ID: 999 не найден", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void partialUpdateUser_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.partialUpdateUser(1L, updateRequest));

        assertEquals("Этот email уже используется. Воспользуйтесь восстановлением пароля.",
                exception.getMessage());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void partialUpdateUser_WhenLoginAlreadyExists_ShouldThrowException() {
        // Arrange
        UserRequest updateRequest = new UserRequest();
        updateRequest.setLogin("existinglogin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByLogin("existinglogin")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.partialUpdateUser(1L, updateRequest));

        assertEquals("Login: existinglogin уже используется", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByLogin("existinglogin");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void partialUpdateUser_WhenOnlyPasswordChanged_ShouldUpdatePassword() {
        // Arrange
        UserRequest updateRequest = new UserRequest();
        updateRequest.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse result = userService.partialUpdateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void partialUpdateUser_WhenSameEmail_ShouldNotCheckEmailExistence() {
        // Arrange
        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail("test@example.com"); // Тот же email

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse result = userService.partialUpdateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, never()).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(999L));

        assertEquals("Пользователь с ID: 999 не найден", exception.getMessage());
        verify(userRepository).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}