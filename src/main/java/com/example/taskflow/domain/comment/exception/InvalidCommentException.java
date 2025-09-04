package com.example.taskflow.domain.comment.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class InvalidCommentException extends GlobalException {
    public InvalidCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}




