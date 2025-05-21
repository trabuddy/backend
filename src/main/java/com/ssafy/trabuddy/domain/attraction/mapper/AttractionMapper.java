package com.ssafy.trabuddy.domain.attraction.mapper;

import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttractionMapper {
    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);

    @Mapping(source = "attractionId", target = "id")
    AttractionSearchResponse toAttractionSearchResponse(AttractionEntity attractionEntity);

    List<AttractionSearchResponse> toAttractionSearchResponseList(List<AttractionEntity> attractionEntities);

    default Page<AttractionSearchResponse> toAttractionSearchResponsePage(Page<AttractionEntity> attractionPage) {
        List<AttractionSearchResponse> responses = toAttractionSearchResponseList(attractionPage.getContent());
        return new PageImpl<>(responses, attractionPage.getPageable(), attractionPage.getTotalElements());
    }
}
