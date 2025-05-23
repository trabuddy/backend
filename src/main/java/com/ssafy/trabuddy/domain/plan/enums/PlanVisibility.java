package com.ssafy.trabuddy.domain.plan.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ssafy.trabuddy.domain.plan.error.InvalidVisibilityException;
import com.ssafy.trabuddy.domain.plan.error.PlanErrorCode;
import jakarta.validation.constraints.NotNull;

public enum PlanVisibility {
    @NotNull
    open("open"),
    @NotNull
    hidden("hidden");

    private final String value;

    PlanVisibility(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PlanVisibility fromValue(String value) {
        for (PlanVisibility visibility : PlanVisibility.values()) {
            if (visibility.value.equalsIgnoreCase(value)) {
                return visibility;
            }
        }

        throw new InvalidVisibilityException(PlanErrorCode.NOT_VALID_VISIBITY);
    }
}
