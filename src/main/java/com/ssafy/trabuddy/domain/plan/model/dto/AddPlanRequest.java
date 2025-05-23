package com.ssafy.trabuddy.domain.plan.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPlanRequest {
    @Size(min = 1, max = 255, message = "제목은 1자 이상 255자 이하로 작성해 주세요")
    private String title;
}
