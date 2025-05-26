package com.ssafy.trabuddy.domain.planShare.service;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.plan.mapper.PlanMapper;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.planShare.model.dto.ParticipantResponse;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateNicknameResponse;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateRoleResponse;
import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import com.ssafy.trabuddy.domain.planShare.repository.PlanShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanShareService {
    private final PlanShareRepository planShareRepository;

    public PlanShareEntity save(PlanShareEntity planShareEntity) {
        return planShareRepository.save(planShareEntity);
    }

    /**
     * 플랜의 모든 참여자 조회
     */
    public List<ParticipantResponse> getParticipants(PlanEntity plan) {
        return plan.getParticipants().stream()
                .filter(participant -> participant.getLeftAt() == null) // 탈퇴하지 않은 참여자만
                .map(participant -> ParticipantResponse.builder()
                        .userId(participant.getMember().getMemberId())
                        .nickname(participant.getNickname())
                        .role(participant.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 참여자 권한 업데이트 (주최자만 가능)
     */
    @Transactional
    public UpdateRoleResponse updateParticipantRole(PlanEntity plan, Long userId, MemberRole newRole, LoggedInMember loggedInMember) {
        // 현재 로그인한 사용자가 주최자인지 확인
        if (plan.getOwnerId() != loggedInMember.getMemberId()) {
            throw new IllegalArgumentException("플랜 주최자만 참여자 권한을 변경할 수 있습니다.");
        }

        PlanShareEntity participant = plan.getParticipants().stream()
                .filter(p -> p.getMember().getMemberId() == userId && p.getLeftAt() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 플랜에 참여하고 있지 않습니다."));

        // 주최자는 자신의 권한을 변경할 수 없음
        if (plan.getOwnerId() == userId) {
            throw new IllegalArgumentException("주최자는 자신의 권한을 변경할 수 없습니다.");
        }

        participant.setRole(newRole);
        planShareRepository.save(participant);

        log.info("참여자 권한 업데이트 - planId: {}, userId: {}, newRole: {}, 변경자: {}", 
                plan.getPlanId(), userId, newRole, loggedInMember.getMemberId());

        return UpdateRoleResponse.builder()
                .userId(userId)
                .role(newRole)
                .build();
    }

    /**
     * 참여자 추방 (주최자만 가능)
     */
    @Transactional
    public void removeParticipant(PlanEntity plan, Long userId, LoggedInMember loggedInMember) {
        // 현재 로그인한 사용자가 주최자인지 확인
        if (plan.getOwnerId() != loggedInMember.getMemberId()) {
            throw new IllegalArgumentException("플랜 주최자만 참여자를 추방할 수 있습니다.");
        }

        PlanShareEntity participant = plan.getParticipants().stream()
                .filter(p -> p.getMember().getMemberId() == userId && p.getLeftAt() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 플랜에 참여하고 있지 않습니다."));

        // 주최자는 추방할 수 없음
        if (plan.getOwnerId() == userId) {
            throw new IllegalArgumentException("주최자는 추방할 수 없습니다.");
        }

        participant.setLeftAt(LocalDateTime.now());
        planShareRepository.save(participant);

        log.info("참여자 추방 완료 - planId: {}, userId: {}, 추방자: {}", plan.getPlanId(), userId, loggedInMember.getMemberId());
    }

    /**
     * 플랜 내 닉네임 변경 (참여자 본인만 가능)
     */
    @Transactional
    public UpdateNicknameResponse updateNickname(PlanEntity plan, Long userId, String newNickname, LoggedInMember loggedInMember) {
        // 본인만 자신의 닉네임을 변경할 수 있음
        if (loggedInMember.getMemberId() != userId) {
            throw new IllegalArgumentException("본인의 닉네임만 변경할 수 있습니다.");
        }

        PlanShareEntity participant = plan.getParticipants().stream()
                .filter(p -> p.getMember().getMemberId() == userId && p.getLeftAt() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 플랜에 참여하고 있지 않습니다."));

        participant.setNickname(newNickname);
        planShareRepository.save(participant);

        log.info("플랜 내 닉네임 변경 - planId: {}, userId: {}, newNickname: {}", 
                plan.getPlanId(), userId, newNickname);

        return UpdateNicknameResponse.builder()
                .userId(userId)
                .nickname(newNickname)
                .build();
    }

    /**
     * 플랜 탈퇴 (참여자 본인만 가능, 주최자는 탈퇴 불가)
     */
    @Transactional
    public void leavePlan(PlanEntity plan, LoggedInMember loggedInMember) {
        // 주최자는 탈퇴할 수 없음
        if (plan.getOwnerId() == loggedInMember.getMemberId()) {
            throw new IllegalArgumentException("플랜 주최자는 탈퇴할 수 없습니다.");
        }

        PlanShareEntity participant = plan.getParticipants().stream()
                .filter(p -> p.getMember().getMemberId() == loggedInMember.getMemberId() && p.getLeftAt() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 플랜에 참여하고 있지 않습니다."));

        participant.setLeftAt(LocalDateTime.now());
        planShareRepository.save(participant);

        log.info("플랜 탈퇴 완료 - planId: {}, userId: {}", plan.getPlanId(), loggedInMember.getMemberId());
    }

    /**
     * 내가 참여하는 플랜 목록 조회
     */
    public List<GetPlanResponse> getMyJoinedPlans(LoggedInMember loggedInMember) {
        List<PlanShareEntity> joinedPlans = planShareRepository.findByMemberMemberIdAndLeftAtIsNull(loggedInMember.getMemberId());
        
        return joinedPlans.stream()
                .map(planShare -> PlanMapper.INSTANCE.toGetPlanResponse(planShare.getPlan()))
                .collect(Collectors.toList());
    }

    /**
     * 내가 참여하는 플랜 목록 조회 (SecurityContext에서 사용자 정보 가져오기)
     */
    public List<GetPlanResponse> getMyJoinedPlans() {
        // SecurityContextHolder에서 현재 로그인한 사용자 정보 가져오기
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("인증 정보가 없습니다.");
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        
        LoggedInMember loggedInMember = (LoggedInMember) authentication.getPrincipal();
        
        if (loggedInMember == null) {
            log.error("인증된 사용자 정보를 찾을 수 없습니다.");
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        
        return getMyJoinedPlans(loggedInMember);
    }
}
