package com.example.taskflow.domain.search.exception;

import com.example.taskflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SearchErrorCode implements ErrorCode {

    EMPTY_QUERY(HttpStatus.BAD_REQUEST, "검색어를 입력해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
