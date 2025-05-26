package com.ssafy.trabuddy.domain.point.model.dto;

import com.ssafy.trabuddy.domain.attraction.model.dto.GetAttractionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPointResponse {
    private long pointId;
    private LocalDateTime from;
    private LocalDateTime to;
    private GetAttractionResponse attraction;
}
