package com.example.taskflow.domain.comment.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_TASK_MISMATCH(HttpStatus.BAD_REQUEST, "Task가 일치하지 않습니다."),
    COMMENT_USER_MISMATCH(HttpStatus.BAD_REQUEST, "User가 일치하지 않습니다."),
    REPLY_COMMENT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "대댓글에 댓글은 허용되지 않습니다."),;


    private final HttpStatus httpStatus;
    private final String message;
}