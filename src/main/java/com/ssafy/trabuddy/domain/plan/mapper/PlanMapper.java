package com.ssafy.trabuddy.domain.plan.mapper;

import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlanMapper {
    public static PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    GetPlanResponse toGetPlanResponse(PlanEntity planEntity);
}
