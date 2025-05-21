package com.ssafy.trabuddy.domain.attraction.repository;

import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.repository.query.AttractionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<AttractionEntity, Long>, AttractionRepositoryCustom {

}
