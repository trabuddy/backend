package com.ssafy.trabuddy.domain.attraction.controller;

import com.ssafy.trabuddy.common.pageDto.PageResultDto;
import com.ssafy.trabuddy.domain.area.mapper.AreaMapper;
import com.ssafy.trabuddy.domain.area.model.dto.AreaResponse;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchRequest;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.service.AttractionService;
import com.ssafy.trabuddy.domain.sigungu.model.dto.AreaSigunguResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;

    @GetMapping("/v1/attractions")
    public ResponseEntity<PageResultDto<List<AttractionSearchResponse>>> searchAttractions(@RequestParam(required = false) String keyword,
                                                                                           @RequestParam(required = false) String sigunguCode,
                                                                                           @RequestParam(required = false) String areaCode,
                                                                                           Pageable pageable) {
        AttractionSearchRequest request = new AttractionSearchRequest(areaCode, sigunguCode, keyword);
        System.out.println("searchAttractions: " + request);
        PageResultDto<List<AttractionSearchResponse>> response = attractionService.searchAttractions(request, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/attractions/areas")
    public ResponseEntity<List<AreaSigunguResponse>> getAreas(@RequestParam(required = false) String areaCode) {
        List<AreaSigunguResponse> response = attractionService.getAreas(areaCode);

        return ResponseEntity.ok(response);
    }
}
