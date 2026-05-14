package com.liveclass.be_b.api.sales.controller;

import com.liveclass.be_b.api.sales.dto.request.CancellationRegisterRequest;
import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.CancellationRegisterResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
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
import java.util.List;

@Tag(name = "Sales", description = "판매 내역 및 취소 내역 API")
public interface SaleControllerDocs {

    @Operation(
            summary = "판매 내역 등록",
            description = "과제 검증용 원천 데이터를 입력하기 위해 판매 내역을 등록한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "판매 등록 성공",
                    content = @Content(schema = @Schema(implementation = SaleRegisterResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강의를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 판매 ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<SaleRegisterResponse>> registerSale(
            @Valid @RequestBody SaleRegisterRequest request
    );

    @Operation(
            summary = "판매 내역 목록 조회",
            description = "크리에이터가 본인의 판매 내역을 기간 조건으로 조회한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "판매 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SaleQueryResponse.class)))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<List<SaleQueryResponse>>> querySale(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Parameter(description = "조회 시작일", example = "2025-03-01")
            @RequestParam(name = "from")
            @NotNull(message = "조회 시작일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @Parameter(description = "조회 종료일", example = "2025-03-31")
            @RequestParam(name = "to")
            @NotNull(message = "조회 종료일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    );

    @Operation(
            summary = "취소 내역 등록",
            description = "원본 판매를 참조하는 취소 내역을 등록한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "취소 등록 성공",
                    content = @Content(schema = @Schema(implementation = CancellationRegisterResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "판매 내역을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 취소 ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "취소 일시 역전 또는 누적 환불 초과"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<CancellationRegisterResponse>> cancelSale(
            @Parameter(description = "취소 대상 판매 ID", example = "sale-1")
            @PathVariable("saleId") String saleId,
            @Valid @RequestBody CancellationRegisterRequest request
    );
}
