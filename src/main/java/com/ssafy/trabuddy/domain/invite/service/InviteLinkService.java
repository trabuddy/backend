package com.ssafy.trabuddy.domain.invite.service;

import com.ssafy.trabuddy.domain.invite.model.dto.InviteLinkResponse;
import com.ssafy.trabuddy.domain.invite.model.dto.JoinPlanResponse;
import com.ssafy.trabuddy.domain.invite.model.dto.PlanInfoResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import com.ssafy.trabuddy.domain.planShare.service.PlanShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteLinkService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PlanService planService;
    private final MemberService memberService;
    private final PlanShareService planShareService;
    private static final SecureRandom secureRandom = new SecureRandom();

    private static final String INVITE_KEY_PREFIX = "invite:";
    private static final long INVITE_EXPIRATION_DAYS = 7; // 일주일
    private static final int INVITE_CODE_BYTES = 8; // 16자리 hex 코드

    /**
     * 랜덤 hex 코드 생성
     */
    private String generateRandomHex(int numBytes) {
        byte[] bytes = new byte[numBytes];
        secureRandom.nextBytes(bytes);
        StringBuilder hex = new StringBuilder(2 * numBytes);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public InviteLinkResponse createInviteLink(PlanEntity plan) {
        // 랜덤 초대 코드 생성 (매번 다름)
        String inviteCode;
        String redisKey;
        
        // 중복 방지를 위해 Redis에 존재하지 않는 코드가 생성될 때까지 반복
        do {
            inviteCode = generateRandomHex(INVITE_CODE_BYTES);
            redisKey = INVITE_KEY_PREFIX + inviteCode;
        } while (redisTemplate.hasKey(redisKey));
        
        // Redis에 저장 (일주일 TTL)
        redisTemplate.opsForValue().set(redisKey, String.valueOf(plan.getPlanId()));
        redisTemplate.expire(redisKey, INVITE_EXPIRATION_DAYS, TimeUnit.DAYS);

        // TTL 초 단위로 계산
        Long ttlSeconds = INVITE_EXPIRATION_DAYS * 24 * 60 * 60;
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(INVITE_EXPIRATION_DAYS);

        log.info("랜덤 초대 링크 생성 완료 - planId: {}, inviteCode: {}, TTL: {}일", plan.getPlanId(), inviteCode, INVITE_EXPIRATION_DAYS);
        
        return InviteLinkResponse.builder()
                .inviteCode(inviteCode)
                .expiresAt(expiresAt)
                .build();
    }

    @Transactional
    public JoinPlanResponse joinPlan(String inviteCode, LoggedInMember loggedInMember) {
        // 초대 코드 검증
        Long planId = validateInviteLink(inviteCode);
        
        // 플랜 정보 조회
        PlanEntity plan = planService.findById(planId);
        
        // 주최자인지 체크 - 주최자는 자신의 플랜에 다시 참여할 수 없음
        if (plan.getOwnerId() == loggedInMember.getMemberId()) {
            throw new IllegalArgumentException("플랜 주최자는 초대 링크로 참여할 수 없습니다.");
        }
        
        // 회원 정보 조회
        MemberEntity member = memberService.getById(loggedInMember.getMemberId());
        
        // 이미 참여 중인지 확인
        boolean alreadyJoined = plan.getParticipants().stream()
                .anyMatch(participant -> participant.getMember().getMemberId() == loggedInMember.getMemberId()
                        && participant.getLeftAt() == null);
        
        if (alreadyJoined) {
            throw new IllegalArgumentException("이미 참여 중인 플랜입니다.");
        }
        
        // 플랜 참가자로 추가
        PlanShareEntity planShare = PlanShareEntity.builder()
                .plan(plan)
                .member(member)
                .nickname(loggedInMember.getNickname())
                .role(MemberRole.member)
                .build();
        
        planShareService.save(planShare);
        plan.addShare(planShare);
        
        log.info("플랜 참여 완료 - planId: {}, memberId: {}, inviteCode: {}", 
                planId, loggedInMember.getMemberId(), inviteCode);
        
        return JoinPlanResponse.builder()
                .planId(planId)
                .title(plan.getTitle())
                .message("플랜에 성공적으로 참여했습니다!")
                .build();
    }

    public PlanInfoResponse getPlanInfo(String inviteCode) {
        // 초대 코드 검증
        Long planId = validateInviteLink(inviteCode);
        
        // 플랜 정보 조회
        PlanEntity plan = planService.findById(planId);
        
        log.info("플랜 정보 조회 - planId: {}, inviteCode: {}", planId, inviteCode);
        
        return PlanInfoResponse.builder()
                .nickname(plan.getOwnerNickname()) // 주최자 닉네임
                .title(plan.getTitle())           // 플랜 제목
                .build();
    }

    public Long validateInviteLink(String inviteCode) {
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        String planId = redisTemplate.opsForValue().get(redisKey);
        
        if (planId == null) {
            log.warn("유효하지 않은 초대 링크 - inviteCode: {}", inviteCode);
            throw new IllegalArgumentException("유효하지 않거나 만료된 초대 링크입니다.");
        }

        log.info("초대 링크 검증 성공 - inviteCode: {}, planId: {}", inviteCode, planId);
        return Long.parseLong(planId);
    }

    public void deleteInviteLink(String inviteCode) {
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        redisTemplate.delete(redisKey);
        log.info("초대 링크 삭제 완료 - inviteCode: {}", inviteCode);
    }

    public Long getRemainingTTL(String inviteCode) {
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        return redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
    }
} 