package com.example.taskflow.domain.tasks.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class InvalidTaskException extends GlobalException {
    public InvalidTaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
