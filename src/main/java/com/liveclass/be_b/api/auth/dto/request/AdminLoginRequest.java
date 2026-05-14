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
@Schema(description = "운영자 로그인 요청")
public class AdminLoginRequest {
    @Schema(description = "운영자 로그인 ID", example = "admin-1")
    @NotNull
    private String loginId;

    @Schema(description = "운영자 비밀번호", example = "qwe123123!")
    @NotNull
    private String password;
}
