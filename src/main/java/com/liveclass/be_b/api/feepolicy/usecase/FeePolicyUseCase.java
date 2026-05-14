package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import com.liveclass.be_b.service.feepolicy.FeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class FeePolicyUseCase {
    private final FeePolicyService feePolicyService;
    private final FeePolicyHistoryService feePolicyHistoryService;

    @Transactional
    public FeePolicyUpdateResponse updateCurrentFeePolicy(FeePolicyUpdateRequest request) {
        int changedFeeRatePercent =
                feePolicyService.updateCurrentFeePolicyRatePercent(request.getFeeRatePercent());

        feePolicyHistoryService.registerFeePolicyHistory(
                LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                changedFeeRatePercent
        );

        return FeePolicyUpdateResponse.of(changedFeeRatePercent);
    }
}
