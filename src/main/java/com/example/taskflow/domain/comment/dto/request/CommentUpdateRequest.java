package com.example.taskflow.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    @NotBlank
    @Size(max = 100)
    private String content;
}
