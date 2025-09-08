package com.example.taskflow.domain.user.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class UserException extends GlobalException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
