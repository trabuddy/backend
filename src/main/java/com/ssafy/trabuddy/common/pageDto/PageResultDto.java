package com.ssafy.trabuddy.common.pageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResultDto<T> {
    private T content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
