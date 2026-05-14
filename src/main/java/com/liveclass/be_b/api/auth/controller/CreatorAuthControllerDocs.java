package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.CreatorLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.CreatorLoginResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Creator Auth", description = "크리에이터 인증 API")
public interface CreatorAuthControllerDocs {

    @Operation(
            summary = "크리에이터 로그인",
            description = "크리에이터 로그인 ID와 비밀번호로 액세스 토큰을 발급한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = CreatorLoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치")
    })
    ResponseEntity<ApiResponse<CreatorLoginResponse>> login(
            @Valid @RequestBody CreatorLoginRequest request
    );
}
