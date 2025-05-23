package com.ssafy.trabuddy.domain.plan.error;

import com.ssafy.trabuddy.common.error.ErrorCode;
import com.ssafy.trabuddy.common.error.TrabuddyException;

public class PlanNotFoundException extends TrabuddyException {
    public PlanNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PlanNotFoundException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
