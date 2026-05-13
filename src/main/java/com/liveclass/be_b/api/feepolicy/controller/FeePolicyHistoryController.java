package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.api.feepolicy.usecase.FeePolicyHistoryUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/fee-policy-histories")
@RequiredArgsConstructor
public class FeePolicyHistoryController implements FeePolicyHistoryControllerDocs{
    private final FeePolicyHistoryUseCase feePolicyHistoryUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<FeePolicyHistoryRegisterResponse>> registerFeePolicyHistory(AuthenticatedPrincipal principal, FeePolicyHistoryRegisterRequest request) {
        FeePolicyHistoryRegisterResponse response = feePolicyHistoryUseCase.registerFeePolicyHistory(request);
        return ApiResponse.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeePolicyHistoryQueryResponse>>> queryFeePolicyHistory(AuthenticatedPrincipal principal) {
        List<FeePolicyHistoryQueryResponse> response = feePolicyHistoryUseCase.queryFeePolicyHistories();
        return ApiResponse.ok(response);
    }
}
