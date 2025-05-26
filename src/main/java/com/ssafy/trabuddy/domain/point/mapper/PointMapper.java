package com.ssafy.trabuddy.domain.point.mapper;

import com.ssafy.trabuddy.domain.point.model.dto.AddPointRequest;
import com.ssafy.trabuddy.domain.point.model.dto.AddPointResponse;
import com.ssafy.trabuddy.domain.point.model.dto.UpdatePointRequest;
import com.ssafy.trabuddy.domain.point.model.dto.UpdatePointResponse;
import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PointMapper {
    public static PointMapper INSTANCE = Mappers.getMapper(PointMapper.class);

    @Mapping(source = "from", target = "startDate")
    @Mapping(source = "to", target = "endDate")
    @Mapping(target = "attraction", ignore = true)
    PointEntity toPointEntity(AddPointRequest addPointRequest);

    @Mapping(source = "from", target = "startDate")
    @Mapping(source = "to", target = "endDate")
    PointEntity toPointEntity(UpdatePointRequest updatePointRequest);

    @Mapping(source = "startDate", target = "from")
    @Mapping(source = "endDate", target = "to")
    UpdatePointResponse toUpdatePointResponse(PointEntity pointEntity);

    @Mapping(source = "startDate", target = "from")
    @Mapping(source = "endDate", target = "to")
    @Mapping(source = "attraction.attractionId", target = "attractionId")
    AddPointResponse toAddPointResponse(PointEntity pointEntity);
}
