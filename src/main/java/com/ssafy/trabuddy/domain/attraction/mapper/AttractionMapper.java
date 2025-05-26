package com.ssafy.trabuddy.domain.attraction.mapper;

import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.attraction.model.dto.GetAttractionResponse;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.model.enums.AttractionSource;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, AttractionSource.class})
public interface AttractionMapper {
    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);

    GetAttractionResponse toGetAttractionResponse(AttractionEntity attractionEntity);

    @Mapping(source = "attractionId", target = "id")
    AttractionSearchResponse toAttractionSearchResponse(AttractionEntity attractionEntity);

    List<AttractionSearchResponse> toAttractionSearchResponseList(List<AttractionEntity> attractionEntities);

    // 카카오 API에서 제공하지 않는 값은 빈 string이나 null 등 더미 값으로 설정
    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "placeName", target = "title")
    @Mapping(source = "phone", target = "telephone", qualifiedByName = "nullSafeString")
    @Mapping(source = "addressName", target = "address1", qualifiedByName = "nullSafeString")
    @Mapping(source = "roadAddressName", target = "address2", qualifiedByName = "nullSafeString")
    @Mapping(source = "y", target = "latitude", qualifiedByName = "stringToDouble")
    @Mapping(source = "x", target = "longitude", qualifiedByName = "stringToDouble")
    @Mapping(target = "attractionId", ignore = true) // 자동 생성
    @Mapping(target = "contentTypeId", constant = "")
    @Mapping(target = "createdTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "zipCode", constant = "")
    @Mapping(target = "category1", constant = "")
    @Mapping(target = "category2", constant = "")
    @Mapping(target = "category3", constant = "")
    @Mapping(target = "mapLevel", constant = "-1")
    @Mapping(target = "firstImageUrl", constant = "")
    @Mapping(target = "firstImageThumbnailUrl", constant = "")
    @Mapping(target = "copyrightDivisionCode", constant = "")
    @Mapping(target = "booktourInfo", constant = "")
    @Mapping(target = "source", expression = "java(AttractionSource.kakao)")
    @Mapping(target = "sigungu", ignore = true)
    @Mapping(target = "area", ignore = true)
    AttractionEntity toAttractionEntity(KakaoPlaceSearchResponse.Document document);

    default Page<AttractionSearchResponse> toAttractionSearchResponsePage(Page<AttractionEntity> attractionPage) {
        List<AttractionSearchResponse> responses = toAttractionSearchResponseList(attractionPage.getContent());
        return new PageImpl<>(responses, attractionPage.getPageable(), attractionPage.getTotalElements());
    }

    // latitude와 longitude 변환을 위한 커스텀 메서드
    @Named("stringToDouble")
    default double stringToDouble(String value) {
        return value != null ? Double.parseDouble(value) : 0.0;
    }

    // telephone 필드 null 안전 처리
    @Named("nullSafeString")
    default String nullSafeString(String value) {
        return value != null ? value : "";
    }
}
