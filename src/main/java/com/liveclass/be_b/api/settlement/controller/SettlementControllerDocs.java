package com.liveclass.be_b.api.settlement.controller;

import com.liveclass.be_b.api.settlement.dto.request.CreatorSettlementCreateRequest;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementManagementItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorMonthlySettlementResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorSettlementCreateResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorSettlementItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.SettlementStatusResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "Settlements", description = "예상 정산 조회 및 실제 정산 관리 API")
public interface SettlementControllerDocs {

    @Operation(
            summary = "정산 생성",
            description = "크리에이터가 대상 연월의 실제 정산 데이터를 생성한다. 생성된 정산 상태는 PENDING 이다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정산 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreatorSettlementCreateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "대상 월 미종료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "동일 연월 정산 중복"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<ApiResponse<CreatorSettlementCreateResponse>> createCreatorSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody CreatorSettlementCreateRequest request
    );

    @Operation(
            summary = "크리에이터 정산 목록 조회",
            description = "생성된 실제 정산 데이터를 연월 범위로 조회한다. from, to는 모두 포함한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정산 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CreatorSettlementItemResponse.class)))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<List<CreatorSettlementItemResponse>>> queryCreatorSettlements(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "조회 시작 연월", example = "2025-03")
            @RequestParam("from")
            @NotNull(message = "조회 시작 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth from,

            @Parameter(description = "조회 종료 연월", example = "2025-05")
            @RequestParam("to")
            @NotNull(message = "조회 종료 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth to
    );

    @Operation(
            summary = "크리에이터 월별 예상 정산 조회",
            description = "실제 settlement 테이블이 아니라 판매/취소 원천 데이터를 기준으로 해당 월의 예상 정산 금액을 계산해 조회한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "예상 정산 조회 성공",
                    content = @Content(schema = @Schema(implementation = CreatorMonthlySettlementResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<CreatorMonthlySettlementResponse>> queryCreatorSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "조회 연월", example = "2025-05")
            @RequestParam("yearMonth")
            @NotNull(message = "조회 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth
    );

    @Operation(
            summary = "운영자 정산 관리 목록 조회",
            description = "생성된 실제 정산 데이터를 연월 범위로 조회한다. from, to는 모두 포함한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정산 관리 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminSettlementManagementItemResponse.class)))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<List<AdminSettlementManagementItemResponse>>> queryAdminSettlementManagement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "조회 시작 연월", example = "2025-03")
            @RequestParam("from")
            @NotNull(message = "조회 시작 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth from,

            @Parameter(description = "조회 종료 연월", example = "2025-05")
            @RequestParam("to")
            @NotNull(message = "조회 종료 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth to
    );

    @Operation(
            summary = "정산 확정",
            description = "PENDING 상태의 실제 정산을 CONFIRMED 상태로 변경한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정산 확정 성공",
                    content = @Content(schema = @Schema(implementation = SettlementStatusResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "정산 정보를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "현재 상태에서는 정산 확정을 할 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<ApiResponse<SettlementStatusResponse>> confirmSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "정산 ID", example = "settlement-1")
            @PathVariable("settlementId") String settlementId
    );

    @Operation(
            summary = "정산 지급 처리",
            description = "CONFIRMED 상태의 실제 정산을 PAID 상태로 변경한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정산 지급 처리 성공",
                    content = @Content(schema = @Schema(implementation = SettlementStatusResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "정산 정보를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "현재 상태에서는 정산 지급 처리를 할 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<ApiResponse<SettlementStatusResponse>> paySettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "정산 ID", example = "settlement-1")
            @PathVariable("settlementId") String settlementId
    );

    @Operation(
            summary = "운영자 예상 정산 집계 조회",
            description = "실제 settlement 테이블이 아니라 판매/취소 원천 데이터를 기준으로 기간 내 전체 크리에이터 예상 정산 현황과 합계를 조회한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "예상 정산 집계 조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminSettlementSummaryResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<AdminSettlementSummaryResponse>> queryAdminSettlementSummary(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "조회 시작일", example = "2025-05-01")
            @RequestParam(name = "from")
            @NotNull(message = "조회 시작일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @Parameter(description = "조회 종료일", example = "2025-05-31")
            @RequestParam(name = "to")
            @NotNull(message = "조회 종료일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    );
}
