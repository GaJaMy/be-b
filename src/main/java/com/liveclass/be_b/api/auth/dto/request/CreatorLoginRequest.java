package com.liveclass.be_b.api.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorLoginRequest {
    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
