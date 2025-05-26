package com.ssafy.trabuddy.domain.point.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePointRequest {
    private LocalDateTime from;
    private LocalDateTime to;
}
