package com.liveclass.be_b.api.feepolicy.controller;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.api.feepolicy.usecase.FeePolicyUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FeePolicyController implements FeePolicyControllerDocs{
    private final FeePolicyUseCase feePolicyUseCase;

    @Override
    @PatchMapping("/fee-policy")
    public ResponseEntity<ApiResponse<FeePolicyUpdateResponse>> updateCurrentFeePolicy(
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            @Valid @RequestBody FeePolicyUpdateRequest request
    ) {
        FeePolicyUpdateResponse response = feePolicyUseCase.updateCurrentFeePolicy(request);
        return ApiResponse.ok(response);
    }
}
