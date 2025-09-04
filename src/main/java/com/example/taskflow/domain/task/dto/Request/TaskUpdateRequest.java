package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskUpdateRequest(

        @NotBlank
        @Size(min = 2, max = 100)
        String title,

        @NotBlank
        String description,

        @NotBlank
        LocalDateTime dueDate,

        @NotBlank
        Priority priority,

        @NotBlank
        Status status,

        Long assigneeId
) {

}

