package com.example.taskflow.domain.comment.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 작성자를 찾을 수 없습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 달릴 Task를 찾을 수 없습니다."),
    COMMENT_TASK_MISMATCH(HttpStatus.NOT_FOUND, "Task가 일치하지 않습니다."),
    COMMENT_USER_MISMATCH(HttpStatus.NOT_FOUND, "User가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
