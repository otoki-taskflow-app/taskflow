package com.example.taskflow.domain.team.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements ErrorCode {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_NOT_IN_TEAM(HttpStatus.BAD_REQUEST, "사용자가 팀 멤버가 아닙니다"),
    TEAM_NAME_DUPLICATE(HttpStatus.BAD_REQUEST, "팀 이름이 이미 존재합니다"),
    TEAM_USER_DUPLICATE(HttpStatus.BAD_REQUEST, "사용자가 이미 팀 멤버입니다");

    private final HttpStatus httpStatus;
    private final String message;
}