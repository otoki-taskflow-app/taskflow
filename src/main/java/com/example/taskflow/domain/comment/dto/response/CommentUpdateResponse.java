package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentUpdateResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        CommentUserResponse user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static CommentUpdateResponse from(Comment comment, CommentUserResponse userResponse) {
        return new CommentUpdateResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                userResponse,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
