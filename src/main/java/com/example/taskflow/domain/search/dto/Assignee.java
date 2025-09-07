package com.example.taskflow.domain.search.dto;

import com.example.taskflow.domain.user.entity.User;

public record Assignee (
        Long id,
        String name
) {
    public static Assignee from(User user) {
        if (user == null) {
            return null;
        }
        return new Assignee(user.getId(), user.getName());
    }
}
