package com.liveclass.be_b.api.auth.dto.response;

import com.liveclass.be_b.service.auth.result.CreatorLoginResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "크리에이터 로그인 응답")
public class CreatorLoginResponse {
    @Schema(description = "발급된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    public static CreatorLoginResponse from(CreatorLoginResult creatorLoginResult) {
        return CreatorLoginResponse.builder()
                .accessToken(creatorLoginResult.getAccessToken())
                .build();
    }
}
