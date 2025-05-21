package com.ssafy.trabuddy.domain.attraction.repository.query;

import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchRequest;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttractionRepositoryCustom {
    Page<AttractionEntity> searchAttractions(AttractionSearchRequest request, Pageable pageable);
}
