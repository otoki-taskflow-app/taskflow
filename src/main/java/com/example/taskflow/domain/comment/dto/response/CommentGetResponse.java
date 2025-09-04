package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentGetResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        CommentUserResponse user,
        Long parentId, // 추가
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static CommentGetResponse from(Comment comment, CommentUserResponse userResponse) {
        Long pId = (comment.getParentId() != null) ? comment.getParentId().getId() : null;
        return new CommentGetResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                userResponse,
                pId,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}