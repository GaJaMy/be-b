package com.liveclass.be_b.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "크리에이터 로그인 요청")
public class CreatorLoginRequest {
    @Schema(description = "크리에이터 로그인 ID", example = "creator-1")
    @NotNull
    private String loginId;

    @Schema(description = "크리에이터 비밀번호", example = "qwe123123!")
    @NotNull
    private String password;
}
