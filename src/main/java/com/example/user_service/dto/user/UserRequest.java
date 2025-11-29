package com.example.user_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email обязателен")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotBlank(message = "Username обязателен")
    @Size(min = 3, max = 100, message = "Длина имени пользователя должно быть между 3 и 100 символами")
    private String username;

    @NotBlank(message = "Login is mandatory")
    @Size(min = 3, max = 100, message = "Длина логина должна быть между 3 и 100 символами")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен состоять не менее чем из 6 символов")
    private String password;
}
