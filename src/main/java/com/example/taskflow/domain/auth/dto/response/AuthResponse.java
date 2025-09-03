package com.example.taskflow.domain.auth.dto.response;

import com.example.taskflow.domain.user.entity.User;

import java.time.LocalDateTime;

public record AuthResponse(
        Long id,
        String username,
        String email,
        String name,
        String role,
        LocalDateTime createdAt
) {
    public static AuthResponse from(User user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                String.valueOf(user.getRole()),
                user.getCreatedAt()
        );
    }
}
