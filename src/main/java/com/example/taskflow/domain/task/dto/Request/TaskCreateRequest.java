package com.example.taskflow.domain.task.dto.Request;

import com.example.taskflow.domain.task.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskCreateRequest(

        @NotBlank(message = "유효하지 않은 상태값입니다.")
        @Size(min = 2, max = 100, message = "유효하지 않은 상태값입니다.")
        String title,

        @NotBlank(message = "유효하지 않은 상태값입니다.")
        String description,

        @NotNull(message = "유효하지 않은 상태값입니다.")
        LocalDateTime dueDate,

        @NotNull(message = "유효하지 않은 상태값입니다.")
        Priority priority,

        Long assigneeId
) {

}
