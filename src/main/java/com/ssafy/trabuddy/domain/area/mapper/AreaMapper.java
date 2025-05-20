package com.ssafy.trabuddy.domain.area.mapper;

import com.ssafy.trabuddy.domain.area.model.dto.AreaResponse;
import com.ssafy.trabuddy.domain.area.model.entity.AreaEntity;
import com.ssafy.trabuddy.domain.sigungu.model.dto.AreaSigunguResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AreaMapper {
    public static AreaMapper INSTANCE = Mappers.getMapper(AreaMapper.class);

    // 1) MapStruct가 구현체를 생성할 구체 클래스 반환 메서드
    AreaResponse toAreaResponseEntity(AreaEntity entity);

    // 2) 서비스 코드에서는 이 default 메서드를 호출해 인터페이스로 사용
    default AreaSigunguResponse getAreas(AreaEntity entity) {
        return toAreaResponseEntity(entity);
    }
}
