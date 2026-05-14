package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.AdminLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.AdminLoginResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Admin Auth", description = "운영자 인증 API")
public interface AdminAuthControllerDocs {

    @Operation(
            summary = "운영자 로그인",
            description = "운영자 로그인 ID와 비밀번호로 액세스 토큰을 발급한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AdminLoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치")
    })
    ResponseEntity<ApiResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request
    );
}
