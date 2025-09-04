package com.example.taskflow.domain.auth.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class AuthException extends GlobalException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
