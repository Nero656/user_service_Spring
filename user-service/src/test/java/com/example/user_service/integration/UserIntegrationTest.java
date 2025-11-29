package com.example.user_service.integration;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; // НЕ должно быть null

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndRetrieveUser_IntegrationTest() {
        UserRequest userRequest = new UserRequest(
                "integration@test.com",
                "Integration User",
                "integrationuser",
                "password123"
        );

        UserResponse createdUser = userService.createUser(userRequest);
        UserResponse retrievedUser = userService.getUserById(createdUser.getId());

        assertNotNull(createdUser);
        assertNotNull(retrievedUser);
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals("Integration User", retrievedUser.getUsername());
    }

    @Test
    void createUserWithDuplicateEmail_ShouldThrowException() {
        UserRequest userRequest1 = new UserRequest(
                "duplicate@test.com",
                "User 1",
                "user1",
                "password123"
        );

        UserRequest userRequest2 = new UserRequest(
                "duplicate@test.com",
                "User 2",
                "user2",
                "password123"
        );

        userService.createUser(userRequest1);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(userRequest2));

        assertTrue(exception.getMessage().contains("уже существует"));
    }
}