package com.liveclass.be_b.service.auth.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResult {
    private String accessToken;

    public static AdminLoginResult of(String accessToken) {
        return AdminLoginResult.builder()
                .accessToken(accessToken)
                .build();
    }
}
