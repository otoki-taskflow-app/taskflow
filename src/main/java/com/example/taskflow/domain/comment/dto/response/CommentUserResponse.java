package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.user.enums.Role;

public record CommentUserResponse(
        Long id,
        String userName,
        String name,
        String email,
        Role role) {

    public static CommentUserResponse from(Comment comment) {
        return new CommentUserResponse(
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getName(),
                comment.getUser().getEmail(),
                comment.getUser().getRole()
        );
    }
}
