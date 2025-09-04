package com.example.taskflow.domain.task.dto.Response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;

import java.time.LocalDateTime;

public record TaskGetResponse(Long id, String title, String description, LocalDateTime dueDate, Priority priority, Status status, Long assigneeId, TaskUserResponse assignee, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static TaskGetResponse from(Task task) {
        return new TaskGetResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getAssignee().getId(),
                TaskUserResponse.from(task),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
