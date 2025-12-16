package com.example.user_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeletedEvent {
    private String eventType;
    private String email;
    private String username;
}