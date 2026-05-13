package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface FeePolicyControllerDocs {
    ResponseEntity<ApiResponse<FeePolicyUpdateResponse>> updateCurrentFeePolicy(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody FeePolicyUpdateRequest request
    );
}
