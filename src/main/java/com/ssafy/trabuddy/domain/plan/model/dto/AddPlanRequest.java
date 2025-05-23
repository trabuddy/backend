package com.ssafy.trabuddy.domain.plan.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPlanRequest {
    @Size(min = 1, max = 255, message = "제목은 1자 이상 255자 이하로 작성해 주세요")
    private String title;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "끝나는 날짜는 필수입니다.")
    private LocalDateTime endDate;

    @Positive(message = "테마 아이디는 숫자만 가능합니다.")
    @NotNull(message = "테마 아이디는 필수입니다.")
    private Integer themeId;
}
