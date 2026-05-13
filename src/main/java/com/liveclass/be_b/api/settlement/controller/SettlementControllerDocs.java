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

public interface SettlementControllerDocs {

    ResponseEntity<ApiResponse<CreatorSettlementCreateResponse>> createCreatorSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody CreatorSettlementCreateRequest request
    );

    ResponseEntity<ApiResponse<List<CreatorSettlementItemResponse>>> queryCreatorSettlements(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam("from")
            @NotNull(message = "조회 시작 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth from,

            @RequestParam("to")
            @NotNull(message = "조회 종료 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth to
    );

    ResponseEntity<ApiResponse<CreatorMonthlySettlementResponse>> queryCreatorSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam("yearMonth")
            @NotNull(message = "조회 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth
    );

    ResponseEntity<ApiResponse<List<AdminSettlementManagementItemResponse>>> queryAdminSettlementManagement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam("from")
            @NotNull(message = "조회 시작 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth from,

            @RequestParam("to")
            @NotNull(message = "조회 종료 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth to
    );

    ResponseEntity<ApiResponse<SettlementStatusResponse>> confirmSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @PathVariable("settlementId") String settlementId
    );

    ResponseEntity<ApiResponse<SettlementStatusResponse>> paySettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @PathVariable("settlementId") String settlementId
    );

    ResponseEntity<ApiResponse<AdminSettlementSummaryResponse>> queryAdminSettlementSummary(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam(name = "from")
            @NotNull(message = "조회 시작일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(name = "to")
            @NotNull(message = "조회 종료일은 필수입니다.")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    );
}
