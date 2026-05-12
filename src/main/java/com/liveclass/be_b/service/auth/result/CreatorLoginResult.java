package com.liveclass.be_b.service.auth.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorLoginResult {
    private String accessToken;

    public static CreatorLoginResult of(String accessToken) {
        return CreatorLoginResult.builder()
                .accessToken(accessToken)
                .build();
    }
}
