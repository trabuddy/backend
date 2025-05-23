package com.ssafy.trabuddy.common.error;

import lombok.Getter;

@Getter
public class TrabuddyException extends RuntimeException {
    private final ErrorCode errorCode;

    public TrabuddyException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public TrabuddyException(ErrorCode errorCode, Throwable throwable){
        super(errorCode.getMessage(), throwable);
        this.errorCode = errorCode;
    }
}
