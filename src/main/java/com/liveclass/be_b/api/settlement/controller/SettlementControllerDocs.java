package com.liveclass.be_b.api.settlement.controller;

import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorMonthlySettlementResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;

public interface SettlementControllerDocs {

    ResponseEntity<ApiResponse<CreatorMonthlySettlementResponse>> queryCreatorSettlement(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam("yearMonth")
            @NotNull(message = "조회 연월은 필수입니다.")
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth
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
