package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.service.feepolicy.FeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FeePolicyUseCase {
    private final FeePolicyService feePolicyService;

    @Transactional
    public FeePolicyUpdateResponse updateCurrentFeePolicy(FeePolicyUpdateRequest request) {
        int changedFeeRatePercent =
                feePolicyService.updateCurrentFeePolicyRatePercent(request.getFeeRatePercent());

        return FeePolicyUpdateResponse.of(changedFeeRatePercent);
    }
}
