package com.liveclass.be_b.api.sales.controller;

import com.liveclass.be_b.api.sales.dto.request.CancellationRegisterRequest;
import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.CancellationRegisterResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.api.sales.usecase.CancellationUseCase;
import com.liveclass.be_b.api.sales.usecase.SaleUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Validated
public class SaleController implements SaleControllerDocs{
    private final SaleUseCase saleUseCase;
    private final CancellationUseCase cancellationUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<SaleRegisterResponse>> registerSale(
            @Valid @RequestBody SaleRegisterRequest request
    ) {
        SaleRegisterResponse response = saleUseCase.registerSale(request);
        return ApiResponse.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleQueryResponse>>> querySale(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @RequestParam(name = "from")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(name = "to")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        List<SaleQueryResponse> responses = saleUseCase.querySale(principal.getPrincipalId(), from, to);
        return ApiResponse.ok(responses);
    }

    @Override
    @PostMapping("/{saleId}/cancellations")
    public ResponseEntity<ApiResponse<CancellationRegisterResponse>> cancelSale(
            @PathVariable("saleId") String saleId,
            @Valid @RequestBody CancellationRegisterRequest request
    ) {
        CancellationRegisterResponse response = cancellationUseCase.registerCancellation(saleId, request);
        return ApiResponse.ok(response);
    }
}
