package com.example.taskflow.domain.search.dto;

import com.example.taskflow.domain.user.entity.User;

public record UserSummary(
        Long id,
        String name,
        String email
) {
    public static UserSummary from(User user) {
        return new UserSummary(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
