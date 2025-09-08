package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Status;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest (
        @NotNull
        Status status
) {
}
