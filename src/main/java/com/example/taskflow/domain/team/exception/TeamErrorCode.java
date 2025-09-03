package com.example.taskflow.domain.team.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements ErrorCode {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 팀을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
