package com.example.taskflow.domain.comment.dto.response;

import com.example.taskflow.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentUserResponse {
    private Long id;
    private String userName;
    private String name;
    private String email;
    // private ?? role

    public static CommentUserResponse from(Comment comment) { // comment 엔티티의 작성자 user 정보를 꺼냄
        return new CommentUserResponse(
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getName(),
                comment.getUser().getEmail()
        );
    }
}
