package com.example.taskflow.domain.task.dto.Response;

import com.example.taskflow.domain.task.entity.Task;

public record TaskUserResponse(Long id, String username, String name, String email) {

    public static TaskUserResponse from(Task task) {
        return new TaskUserResponse(
                task.getUser().getId(),
                task.getUser().getUsername(),
                task.getUser().getName(),
                task.getUser().getEmail()
        );
    }
}
