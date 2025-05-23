package com.ssafy.trabuddy.domain.plan.error;

import com.ssafy.trabuddy.common.error.ErrorCode;
import com.ssafy.trabuddy.common.error.TrabuddyException;

public class InvalidVisibilityException extends TrabuddyException {
    public InvalidVisibilityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidVisibilityException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
