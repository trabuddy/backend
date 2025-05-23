package com.ssafy.trabuddy.domain.plan.model.dto;

import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRequest {
    @Positive(message = "테마 아이디는 숫자만 입력 가능합니다.")
    private int themeId;
    @Size(min = 1, max = 255, message = "제목은 1자 이상 255자 이하로 작성해 주세요")
    private String title;

    @Size(max = 65535, message = "설명은 65535자 이하로 작성해 주세요")
    private String description;
    @Size(max = 65535, message = "설명은 65535자 이하로 작성해 주세요")
    private String memo;
    @Positive(message = "인원수는 숫자만 입력 가능합니다.")
    private int people;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @NotNull(message = "공개 여부는 필수입니다")
    private PlanVisibility visibility;
}
