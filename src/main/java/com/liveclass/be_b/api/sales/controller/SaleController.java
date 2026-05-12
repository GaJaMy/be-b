package com.liveclass.be_b.api.sales.controller;

import com.liveclass.be_b.api.sales.dto.request.SaleCancelRegisterRequest;
import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.api.sales.usecase.SaleUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController implements SaleControllerDocs{
    private final SaleUseCase saleUseCase;

    @Override
    public ResponseEntity<ApiResponse<SaleRegisterResponse>> registerSale(SaleRegisterRequest request) {
        SaleRegisterResponse response = saleUseCase.registerSale(request);
        return ApiResponse.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse<SaleQueryResponse>> querySale(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<SaleCancelRegisterRequest>> cancelSale(SaleCancelRegisterRequest request) {
        return null;
    }
}
