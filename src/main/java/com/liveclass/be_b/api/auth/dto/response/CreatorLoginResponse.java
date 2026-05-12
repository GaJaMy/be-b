package com.liveclass.be_b.api.auth.dto.response;

import com.liveclass.be_b.service.auth.result.CreatorLoginResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorLoginResponse {
    private String accessToken;

    public static CreatorLoginResponse from(CreatorLoginResult creatorLoginResult) {
        return CreatorLoginResponse.builder()
                .accessToken(creatorLoginResult.getAccessToken())
                .build();
    }
}
