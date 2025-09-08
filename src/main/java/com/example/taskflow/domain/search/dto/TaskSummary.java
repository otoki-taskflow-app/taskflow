package com.example.taskflow.domain.search.dto;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Status;

public record TaskSummary (
        Long id,
        String title,
        String description,
        Status status,
        Assignee assignee
) {
    public static TaskSummary from(Task task) {
        return new TaskSummary(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                Assignee.from(task.getAssignee())
        );
    }
}