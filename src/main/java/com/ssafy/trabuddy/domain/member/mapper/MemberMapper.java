package com.ssafy.trabuddy.domain.member.mapper;

import com.ssafy.trabuddy.domain.member.model.dto.GetMemberInfoResponse;
import com.ssafy.trabuddy.domain.member.model.dto.GetProfileResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    LoggedInMember entityToDto(MemberEntity entity);

    @Mapping(source = "memberId", target = "userId")
    GetMemberInfoResponse getMemberInfoResponse(LoggedInMember loggedInMember);
    GetProfileResponse profileToDto(LoggedInMember loggedInMember);
}
