package com.example.taskflow.domain.search.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class SearchException extends GlobalException {
    public SearchException(ErrorCode errorCode) {
        super(errorCode);
    }
}