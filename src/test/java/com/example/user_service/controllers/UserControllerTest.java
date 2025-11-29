package com.example.user_service.controllers;

import com.example.user_service.controllers.user.UserControllers;
import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllers.class)
@Import(UserMapper.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest(
                "test@example.com",
                "Test User",
                "testuser",
                "password123"
        );

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Test User");

        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Test User"));

        verify(userService).createUser(any(UserRequest.class));
    }

    @Test
    void getUser_ShouldReturnUser() throws Exception {
        // Arrange
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Test User");

        when(userService.getUserById(1L)).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Test User"));

        verify(userService).getUserById(1L);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Updated User");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Updated User");

        when(userService.partialUpdateUser(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Updated User"));

        verify(userService).partialUpdateUser(anyLong(), any(UserRequest.class));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(userService).deleteUser(1L);
    }
}