package com.ssafy.trabuddy.domain.attraction.repository;

import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.model.enums.AttractionSource;
import com.ssafy.trabuddy.domain.attraction.repository.query.AttractionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttractionRepository extends JpaRepository<AttractionEntity, Long>, AttractionRepositoryCustom {
    Optional<AttractionEntity> findByContentIdAndSource(String contentId, AttractionSource source);
}
