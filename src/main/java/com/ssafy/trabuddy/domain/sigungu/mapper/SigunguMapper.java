package com.ssafy.trabuddy.domain.sigungu.mapper;

import com.ssafy.trabuddy.domain.sigungu.model.dto.AreaSigunguResponse;
import com.ssafy.trabuddy.domain.sigungu.model.dto.SigunguResponse;
import com.ssafy.trabuddy.domain.sigungu.model.entity.SigunguEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SigunguMapper {
    public static SigunguMapper INSTANCE = Mappers.getMapper(SigunguMapper.class);

    // 1) MapStruct가 구현체를 생성할 수 있는 구체 반환 메서드
    SigunguResponse toSigunguResponseEntity(SigunguEntity entity, int areaCode);

    // 2) 실제 서비스에서는 이 메서드를 호출해 인터페이스를 받도록!
    default AreaSigunguResponse toSigunguResponse(SigunguEntity entity, int areaCode) {
        return toSigunguResponseEntity(entity, areaCode);
    }
}
