package com.example.taskflow.domain.team.exception;

import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.exception.GlobalException;

public class InvalidTeamException extends GlobalException {
    public InvalidTeamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
