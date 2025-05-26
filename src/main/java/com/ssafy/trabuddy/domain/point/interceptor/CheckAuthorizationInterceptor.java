package com.ssafy.trabuddy.domain.point.interceptor;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import com.ssafy.trabuddy.domain.point.service.PointService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckAuthorizationInterceptor implements HandlerInterceptor {
    private final PointService pointService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (!("PATCH".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method))) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        long pointId;
        try {
            pointId = Long.parseLong(pathVars.get("pointId"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 pointId 경로 변수");
            return false;
        }

        LoggedInMember user = (LoggedInMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PointEntity point = pointService.findByPointIdUsingFetchJoin(pointId);
        if (point == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 포인트를 찾을 수 없습니다");
            return false;
        }

        boolean isOwner = point.getPlan().getParticipants().stream()
                .anyMatch(planShare ->
                        planShare.getMember().getMemberId() == user.getMemberId()
                                && planShare.getRole().equals(MemberRole.owner)
                );

        if (!isOwner) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "수정/삭제 권한이 없습니다");
            return false;
        }

        return true;
    }
}
