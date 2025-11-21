package com.example.user_service.Entity;

import com.example.user_service.entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void hashPassword_ShouldReturnHashedPassword() {
        // Arrange
        String password = "password123";

        // Act
        String hashedPassword = User.hashPassword(password);

        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    @Test
    void checkPassword_WithCorrectPassword_ShouldReturnTrue() {
        // Arrange
        String password = "password123";
        String hashedPassword = User.hashPassword(password);

        // Act
        boolean result = User.checkPassword(password, hashedPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkPassword_WithIncorrectPassword_ShouldReturnFalse() {
        // Arrange
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        String hashedPassword = User.hashPassword(correctPassword);

        // Act
        boolean result = User.checkPassword(wrongPassword, hashedPassword);

        // Assert
        assertFalse(result);
    }

    @Test
    void userEntity_ShouldHaveCorrectGettersAndSetters() {
        // Arrange & Act
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setLogin("testuser");
        user.setPassword("hashedPassword");

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getUsername());
        assertEquals("testuser", user.getLogin());
        assertEquals("hashedPassword", user.getPassword());
    }
}
