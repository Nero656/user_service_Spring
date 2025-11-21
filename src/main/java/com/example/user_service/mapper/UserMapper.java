package com.example.user_service.mapper;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entities.User;

import java.time.LocalDateTime;

public class UserMapper {
    public static User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setLogin(userRequest.getLogin());
        user.setPassword(user.hashPassword(userRequest.getPassword()));
        user.setCreateAt(LocalDateTime.now());
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }

    public static void updateEntityFromRequest(UserRequest userRequest, User user) {
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getUsername() != null) {
            user.setUsername(userRequest.getUsername());
        }
        if (userRequest.getLogin() != null) {
            user.setLogin(userRequest.getLogin());
        }
        if (userRequest.getPassword() != null) {
            User.hashPassword(userRequest.getPassword());
        }
    }
}
