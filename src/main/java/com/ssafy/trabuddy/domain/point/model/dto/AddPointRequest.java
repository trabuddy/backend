package com.ssafy.trabuddy.domain.point.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddPointRequest {
    private long attractionId;
    private LocalDateTime from;
    private LocalDateTime to;
}
