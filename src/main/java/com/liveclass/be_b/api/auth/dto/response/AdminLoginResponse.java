package com.liveclass.be_b.api.auth.dto.response;

import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse {
    private String accessToken;

    public static AdminLoginResponse from(AdminLoginResult adminLoginResult) {
        return AdminLoginResponse.builder()
                .accessToken(adminLoginResult.getAccessToken())
                .build();
    }
}
