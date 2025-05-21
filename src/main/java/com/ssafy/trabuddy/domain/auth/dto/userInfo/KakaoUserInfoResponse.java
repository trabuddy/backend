package com.ssafy.trabuddy.domain.auth.dto.userInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class KakaoUserInfoResponse {
    private long id;
    private boolean has_signed_up;
    private LocalDateTime connected_at;
    private LocalDateTime synched_at;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Properties properties;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private KakaoAccount kakao_account;
}
