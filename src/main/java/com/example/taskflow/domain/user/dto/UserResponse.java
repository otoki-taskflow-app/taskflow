package com.example.taskflow.domain.user.dto;

import com.example.taskflow.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String name,
        String email,
        String role,
        LocalDateTime createdAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

//    public static UserResponse from(User user) {
//        return new UserResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getName(),
//                user.getEmail(),
//                user.getRole().name(),
//                user.getCreatedAt()
//        );
//    }
}
