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
public class AddPointResponse {
    private long attractionId;
    private long pointId;
    private LocalDateTime from;
    private LocalDateTime to;
}
