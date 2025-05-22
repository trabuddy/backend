package com.ssafy.trabuddy.common.auditing;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomCreatedBy implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current Authentication: {}", authentication);
        
        if (authentication == null) {
            log.error("Authentication is null");
            return Optional.empty();
        }
        
        if (!authentication.isAuthenticated()) {
            log.error("User is not authenticated");
            return Optional.empty();
        }
        
        Object principal = authentication.getPrincipal();
        log.info("Principal type: {}", principal.getClass().getName());
        
        if (!(principal instanceof LoggedInMember)) {
            log.error("Principal is not LoggedInMember");
            return Optional.empty();
        }

        LoggedInMember loggedInMember = (LoggedInMember) principal;
        log.info("LoggedInMember ID: {}", loggedInMember.getMemberId());
        return Optional.of(loggedInMember.getMemberId());
    }
}
