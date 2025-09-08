package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Status;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest (
        @NotNull(message = "유효하지 않은 상태값입니다.")
        Status status
) {
}
