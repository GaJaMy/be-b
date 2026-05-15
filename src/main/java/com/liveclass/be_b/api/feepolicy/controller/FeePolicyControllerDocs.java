package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Fee Policy", description = "현재 적용 수수료율 정책 API")
public interface FeePolicyControllerDocs {
    @Operation(
            summary = "현재 수수료율 변경",
            description = "현재 적용 수수료율을 즉시 변경한다. 이후 발생하는 판매부터 새 수수료율이 적용된다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "현재 수수료율 변경 성공",
                    content = @Content(schema = @Schema(implementation = FeePolicyUpdateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<FeePolicyUpdateResponse>> updateCurrentFeePolicy(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody FeePolicyUpdateRequest request
    );
}
