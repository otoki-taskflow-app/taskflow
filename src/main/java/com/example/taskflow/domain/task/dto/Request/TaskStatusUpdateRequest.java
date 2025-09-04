package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Status;

public record TaskStatusUpdateRequest (Status status) {
}
