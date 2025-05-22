package com.ssafy.trabuddy.domain.member.service;

import com.ssafy.trabuddy.domain.auth.dto.userInfo.KakaoUserInfoResponse;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSocialType;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
