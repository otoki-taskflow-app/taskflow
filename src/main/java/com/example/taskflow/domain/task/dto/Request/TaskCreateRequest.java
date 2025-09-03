package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Priority;

import java.time.LocalDateTime;

public record TaskCreateRequest(String title, String description, LocalDateTime dueDate, Priority priority, Long userId) {

}
