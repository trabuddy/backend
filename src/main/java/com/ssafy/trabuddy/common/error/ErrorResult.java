package com.ssafy.trabuddy.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResult {
    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }
}