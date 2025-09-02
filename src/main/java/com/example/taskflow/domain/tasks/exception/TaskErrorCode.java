package com.example.taskflow.domain.tasks.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TaskErrorCode implements ErrorCode {

    FORBIDDEN_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "해당 댓글에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
