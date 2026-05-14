package com.liveclass.be_b.api.auth.dto.response;

import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "운영자 로그인 응답")
public class AdminLoginResponse {
    @Schema(description = "발급된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    public static AdminLoginResponse from(AdminLoginResult adminLoginResult) {
        return AdminLoginResponse.builder()
                .accessToken(adminLoginResult.getAccessToken())
                .build();
    }
}
