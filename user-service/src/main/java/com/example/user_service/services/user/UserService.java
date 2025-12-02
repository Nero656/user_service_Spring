package com.example.user_service.services.user;

import com.example.user_service.dto.user.*;
import com.example.user_service.entities.User;
import com.example.user_service.repositories.user.UserRepository;
import com.example.user_service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebClient webClient;

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Пользователь с email: " + userRequest.getEmail() + " уже существует!");
        }

        User user = UserMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);

        kafkaTemplate.send("user.created",
                new UserCreatedEvent("USER_CREATED", savedUser.getEmail(), savedUser.getUsername()));

        return UserMapper.toResponse(savedUser);
    }

    public UserResponse createUserWithOutKafka(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Пользователь с email: " + userRequest.getEmail() + " уже существует!");
        }

        User user = UserMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);

        sendWelcomeNotification(savedUser.getEmail(), savedUser.getUsername());

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

        if (userRequest.getEmail() != null && !existingUser.getEmail().equals(userRequest.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new RuntimeException("Этот email уже используется. Воспользуйтесь восстановлением пароля.");
            }
            existingUser.setEmail(userRequest.getEmail());
        }

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));

        kafkaTemplate.send("user.deleted",
                new UserDeletedEvent("USER_DELETED", user.getEmail(), user.getUsername()));

        userRepository.deleteById(id);
    }

    public void deleteUserWithOutKafka(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));
        sendDeleteNotification(user.getEmail(), user.getUsername());

        userRepository.deleteById(id);
    }


    private void sendWelcomeNotification(String email, String username) {
        NotificationRequestUser request = new NotificationRequestUser(email, username);

        webClient.post()
                .uri("http://notification-service:18081/api/notify/create_user_no_kafka")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void sendDeleteNotification(String email, String username) {
        NotificationRequestUser request = new NotificationRequestUser(email, username);

        webClient.post()
                .uri("http://notification-service:18081/api/notify/delete_user_no_kafka")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}