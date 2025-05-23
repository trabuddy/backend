package com.ssafy.trabuddy.domain.plan.mapper;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.dto.AddPlanRequest;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlanMapper {
    public static PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    GetPlanResponse toGetPlanResponse(PlanEntity planEntity);

    @Mapping(source  = "loggedInMember.nickname", target = "ownerNickname")
    PlanEntity toPlanEntity(AddPlanRequest addPlanRequest, LoggedInMember loggedInMember);
}
