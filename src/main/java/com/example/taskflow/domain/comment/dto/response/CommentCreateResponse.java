package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        CommentUserResponse user,
        Long parentId, // 추가
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static CommentCreateResponse from(Comment comment, CommentUserResponse userResponse) {
        Long pId = (comment.getParentId() != null) ? comment.getParentId().getId() : null; // 부모 댓글이 있으면 그 부모의 id를 꺼내고, 없으면 null을 넣는다
        return new CommentCreateResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                userResponse,
                pId, // 대댓글이면 부모 댓글 id, 일반 댓글이면 null
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
