package com.ssafy.trabuddy.domain.invite.interceptor;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanOwnerInterceptor implements HandlerInterceptor {
    private final PlanService planService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        
        // POST, PUT, DELETE 요청만 체크
        if (!("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        long planId;
        try {
            planId = Long.parseLong(pathVars.get("planId"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 planId 경로 변수");
            return false;
        }

        // 현재 로그인한 사용자 정보 가져오기
        LoggedInMember user = (LoggedInMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 플랜 정보 조회
        PlanEntity plan;
        try {
            plan = planService.findById(planId);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 플랜을 찾을 수 없습니다");
            return false;
        }

        // 주최자 권한 체크
        if (plan.getOwnerId() != user.getMemberId()) {
            String action = "POST".equals(method) ? "초대 링크 생성" : 
                           "PUT".equals(method) ? "참여자 권한 변경" : "참여자 추방";
            log.warn("권한 없는 사용자가 {} 시도 - planId: {}, userId: {}, ownerId: {}", 
                    action, planId, user.getMemberId(), plan.getOwnerId());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "플랜 주최자만 이 작업을 수행할 수 있습니다");
            return false;
        }

        log.info("주최자 권한 확인 완료 - planId: {}, ownerId: {}, method: {}", planId, plan.getOwnerId(), method);
        return true;
    }
} 