package com.example.user_service.services.user;

import com.example.user_service.dto.user.UserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entities.User;
import com.example.user_service.repositories.user.UserRepository;
import com.example.user_service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Пользователь с email: " + userRequest.getEmail() + " уже существует!");
        }

        User user = UserMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));
        return UserMapper.toResponse(user);
    }

    public UserResponse partialUpdateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));

        //проверка email
        if (userRequest.getEmail() != null && !existingUser.getEmail().equals(userRequest.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new RuntimeException("Этот email уже используется. Воспользуйтесь восстановлением пароля.");
            }
            existingUser.setEmail(userRequest.getEmail());
        }

        //проверка login
        if (userRequest.getLogin() != null && !existingUser.getLogin().equals(userRequest.getLogin())) {
            if (userRepository.existsByLogin(userRequest.getLogin())) {
                throw new RuntimeException("Login: " + userRequest.getLogin() + " уже используется");
            }
            existingUser.setLogin(userRequest.getLogin());
        }

        if (userRequest.getUsername() != null && !existingUser.getUsername().equals(userRequest.getUsername())) {
            existingUser.setUsername(userRequest.getUsername());
        }

        if (userRequest.getPassword() != null) {
            existingUser.hashPassword(userRequest.getPassword());
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с ID: " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
