package com.ssafy.trabuddy.domain.member.service;

import com.ssafy.trabuddy.common.util.NullPropertyUtils;
import com.ssafy.trabuddy.domain.auth.dto.userInfo.KakaoUserInfoResponse;
import com.ssafy.trabuddy.domain.member.mapper.MemberMapper;
import com.ssafy.trabuddy.domain.member.model.dto.GetMemberInfoResponse;
import com.ssafy.trabuddy.domain.member.model.dto.GetProfileResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.dto.UpdateMypageRequest;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSocialType;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PlanService planService;

    public void join(KakaoUserInfoResponse userInfo) {
        Optional<MemberEntity> userEntity = memberRepository
                .findBySocialIdAndUnregisteredAtIsNull(userInfo.getId());

        if (userEntity.isPresent()) {
            return;
        }

        MemberEntity user = MemberEntity.builder()
                .nickname(userInfo.getProperties().getNickname())
                .socialType(MemberSocialType.kakao)
                .socialId(userInfo.getId())
                .build();

        memberRepository.save(user);
    }

    public GetMemberInfoResponse getNickname(LoggedInMember loggedInMember) {
        return MemberMapper.INSTANCE.getMemberInfoResponse(loggedInMember);
    }

    public GetProfileResponse getProfile(LoggedInMember loggedInMember) {
        return MemberMapper.INSTANCE.profileToDto(loggedInMember);
    }

    public List<GetPlanResponse> getPlans(LoggedInMember loggedInMember) {
        return planService.getPlans(loggedInMember);
    }

    @Transactional
    public void updateMypage(UpdateMypageRequest request, LoggedInMember loggedInMember) {
        String[] proStrings = NullPropertyUtils.getNullPropertyNames(request);

        MemberEntity member = memberRepository.findByMemberIdAndUnregisteredAtIsNull(loggedInMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        BeanUtils.copyProperties(request, member, proStrings);
    }

    public MemberEntity getById(long id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못됨"));
    }
}
