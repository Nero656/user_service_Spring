package com.example.user_service.mapper;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toEntity_ShouldMapUserRequestToUser() {
        // Arrange
        UserRequest userRequest = new UserRequest(
                "test@example.com",
                "Test User",
                "testuser",
                "password123"
        );

        // Act
        User user = UserMapper.toEntity(userRequest);

        // Assert
        assertNotNull(user);
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals(userRequest.getUsername(), user.getUsername());
        assertEquals(userRequest.getLogin(), user.getLogin());
        assertNotNull(user.getPassword());
        assertNotNull(user.getCreateAt());
    }

    @Test
    void toResponse_ShouldMapUserToUserResponse() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("Test User");
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setPassword("hashedPassword");
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        // Act
        UserResponse userResponse = UserMapper.toResponse(user);

        // Assert
        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUsername(), userResponse.getUsername());
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        User user = new User();
        user.setEmail("old@example.com");
        user.setUsername("Old User");
        user.setLogin("oldlogin");
        user.setPassword("oldPassword");

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("new@example.com");
        userRequest.setUsername("New User");

        // Act
        UserMapper.updateEntityFromRequest(userRequest, user);

        // Assert
        assertEquals("new@example.com", user.getEmail());
        assertEquals("New User", user.getUsername());
        assertEquals("oldlogin", user.getLogin()); // Не изменился
        assertEquals("oldPassword", user.getPassword()); // Не изменился
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setLogin("testuser");
        user.setPassword("password");

        UserRequest userRequest = new UserRequest(); // Все поля null

        // Act
        UserMapper.updateEntityFromRequest(userRequest, user);

        // Assert
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getUsername());
        assertEquals("testuser", user.getLogin());
        assertEquals("password", user.getPassword());
    }
}