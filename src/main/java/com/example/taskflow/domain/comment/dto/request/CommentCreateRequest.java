package com.example.taskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank
        String content) {}

