package com.example.taskflow.domain.task.dto.Response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;

import java.time.LocalDateTime;

public record TaskStatusUpdateResponse(Long id, String title, String description, Status status, Priority priority, Long assigneeId, TaskUserResponse assignee, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime dueDate) {

    public static TaskStatusUpdateResponse from(Task task) {
        return new TaskStatusUpdateResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignee().getId(),
                TaskUserResponse.from(task),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate()
        );
    }
}
