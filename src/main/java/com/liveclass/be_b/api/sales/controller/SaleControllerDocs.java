package com.liveclass.be_b.api.sales.controller;

import com.liveclass.be_b.api.sales.dto.request.SaleCancelRegisterRequest;
import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public interface SaleControllerDocs {

    ResponseEntity<ApiResponse<SaleRegisterResponse>> registerSale(
            @Valid @RequestBody SaleRegisterRequest request
    );

    ResponseEntity<ApiResponse<SaleQueryResponse>> querySale(
            @RequestParam(name = "from")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(name = "to")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    );

    ResponseEntity<ApiResponse<SaleCancelRegisterRequest>> cancelSale(
            @Valid @RequestBody SaleCancelRegisterRequest request
    );
}
