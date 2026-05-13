package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FeePolicyHistoryControllerDocs {

    ResponseEntity<ApiResponse<FeePolicyHistoryRegisterResponse>> registerFeePolicyHistory(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody FeePolicyHistoryRegisterRequest request
    );

    ResponseEntity<ApiResponse<List<FeePolicyHistoryQueryResponse>>> queryFeePolicyHistory(
            @AuthenticationPrincipal AuthenticatedPrincipal principal
    );
}
