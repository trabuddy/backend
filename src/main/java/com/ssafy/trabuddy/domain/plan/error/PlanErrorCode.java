package com.ssafy.trabuddy.domain.plan.error;

import com.ssafy.trabuddy.common.error.ErrorCode;

public enum PlanErrorCode implements ErrorCode {
    NOT_FOUND_PLAN(404, "존재하지 않는 계획입니다."),
    NOT_VALID_ID(400, "유효하지 않은 계획 ID입니다"),
    NOT_AUTHORIZED_PLAN(403, "사용자는 계획에 대한 권한이 없습니다");

    private final int statusCode;
    private final String message;

    PlanErrorCode(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
