package com.ssafy.trabuddy.domain.plan.interceptor;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
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
public class CheckAuthorizationForPlanInterceptor implements HandlerInterceptor {
    private final PlanService planService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        if (method.equals("PATCH") || method.equals("DELETE")) {
            Map<String,String> pathVars = (Map<String, String>)
                    request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            long planId = Long.parseLong(pathVars.get("planId"));

            LoggedInMember loggedInMember = (LoggedInMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            PlanEntity plan = planService.findById(planId);

            return plan.getOwnerId() == loggedInMember.getMemberId();
        }

        return true;
    }
}
