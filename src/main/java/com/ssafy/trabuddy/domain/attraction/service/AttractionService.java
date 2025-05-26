package com.ssafy.trabuddy.domain.attraction.service;

import com.ssafy.trabuddy.common.pageDto.PageResultDto;
import com.ssafy.trabuddy.domain.area.mapper.AreaMapper;
import com.ssafy.trabuddy.domain.area.model.dto.AreaResponse;
import com.ssafy.trabuddy.domain.area.repository.AreaRepository;
import com.ssafy.trabuddy.domain.attraction.mapper.AttractionMapper;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchRequest;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.repository.AttractionRepository;
import com.ssafy.trabuddy.domain.sigungu.mapper.SigunguMapper;
import com.ssafy.trabuddy.domain.sigungu.model.dto.AreaSigunguResponse;
import com.ssafy.trabuddy.domain.sigungu.repository.SigunguRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionService {
    private final AttractionRepository attractionRepository;
    private final SigunguRepository sigunguRepository;
    private final AreaRepository areaRepository;

    public PageResultDto<List<AttractionSearchResponse>> searchAttractions(AttractionSearchRequest request, Pageable pageable) {
        Page<AttractionEntity> pageResult = attractionRepository.searchAttractions(request, pageable);

        log.info(pageResult.toString());

        List<AttractionSearchResponse> content = pageResult.getContent()
                .stream().map(AttractionMapper.INSTANCE::toAttractionSearchResponse).toList();

        return PageResultDto.<List<AttractionSearchResponse>>builder()
                .page(pageResult.getPageable().getPageNumber())
                .content(content)
                .size(pageResult.getSize())
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .build();
    }

    public List<AreaSigunguResponse> getAreas(String areaCode){
        if(areaCode == null) {
            return areaRepository.findAll()
                    .stream().map(AreaMapper.INSTANCE::getAreas)
                    .toList();
        }

        return sigunguRepository.findAllByArea_AreaCode(Integer.parseInt(areaCode))
                .stream()
                .map(sigunguEntity -> SigunguMapper
                        .INSTANCE
                        .toSigunguResponse(sigunguEntity, Integer.parseInt(areaCode)))
                .toList();
    }

    public AttractionEntity findById(long attractionId) {
        return attractionRepository.findById(attractionId)
                .orElseThrow(() -> new IllegalArgumentException("attraction not found"));
    }
}
