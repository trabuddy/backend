package com.ssafy.trabuddy.domain.sigungu.repository;

import com.ssafy.trabuddy.domain.sigungu.model.entity.SigunguEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<SigunguEntity, Integer> {
    List<SigunguEntity> findAllByArea_AreaCode(int areaCode);
}
