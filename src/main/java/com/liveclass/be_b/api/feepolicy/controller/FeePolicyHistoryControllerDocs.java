package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Fee Policy History", description = "수수료율 변경 이력 API")
public interface FeePolicyHistoryControllerDocs {

    @Operation(
            summary = "수수료율 이력 등록",
            description = "특정 시각부터 적용되는 수수료율 변경 이력을 등록한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수수료율 이력 등록 성공",
                    content = @Content(schema = @Schema(implementation = FeePolicyHistoryRegisterResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "동일한 변경 기준 시각의 수수료율 이력이 이미 존재함"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<FeePolicyHistoryRegisterResponse>> registerFeePolicyHistory(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody FeePolicyHistoryRegisterRequest request
    );

    @Operation(
            summary = "수수료율 이력 목록 조회",
            description = "등록된 수수료율 변경 이력을 적용 시작 시각 오름차순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수수료율 이력 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FeePolicyHistoryQueryResponse.class)))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<ApiResponse<List<FeePolicyHistoryQueryResponse>>> queryFeePolicyHistory(
            @AuthenticationPrincipal AuthenticatedPrincipal principal
    );
}
