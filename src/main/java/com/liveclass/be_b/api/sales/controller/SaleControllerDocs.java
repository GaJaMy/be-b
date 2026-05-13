package com.liveclass.be_b.api.sales.controller;

import com.liveclass.be_b.api.sales.dto.request.CancellationRegisterRequest;
import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.CancellationRegisterResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
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
import java.util.List;

public interface SaleControllerDocs {

    ResponseEntity<ApiResponse<SaleRegisterResponse>> registerSale(
            @Valid @RequestBody SaleRegisterRequest request
    );

    ResponseEntity<ApiResponse<List<SaleQueryResponse>>> querySale(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam(name = "from")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(name = "to")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    );

    ResponseEntity<ApiResponse<CancellationRegisterResponse>> cancelSale(
            @PathVariable("saleId") String saleId,
            @Valid @RequestBody CancellationRegisterRequest request
    );
}
