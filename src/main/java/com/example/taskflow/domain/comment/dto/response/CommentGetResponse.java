package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentGetResponse {
    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private CommentUserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentGetResponse from(Comment comment, CommentUserResponse userResponse) {
        return new CommentGetResponse(
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
