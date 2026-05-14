package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeePolicyHistoryUseCase {
    private final FeePolicyHistoryService feePolicyHistoryService;

    @Transactional
    public FeePolicyHistoryRegisterResponse registerFeePolicyHistory(FeePolicyHistoryRegisterRequest request) {
        FeePolicyHistory feePolicyHistory = feePolicyHistoryService.registerFeePolicyHistory(
                DateTimeUtil.toKstLocalDateTime(request.getEffectiveStartedAt()),
                request.getFeeRatePercent()
        );

        return FeePolicyHistoryRegisterResponse.of(
                feePolicyHistory.getId(),
                DateTimeUtil.toKstOffsetDateTime(feePolicyHistory.getEffectiveStartedAt()),
                feePolicyHistory.getFeeRatePercent()
        );
    }

    @Transactional(readOnly = true)
    public List<FeePolicyHistoryQueryResponse> queryFeePolicyHistories() {
        return feePolicyHistoryService.findAllFeePolicyHistories().stream()
                .map(feePolicyHistory -> FeePolicyHistoryQueryResponse.builder()
                        .feePolicyHistoryId(feePolicyHistory.getId())
                        .effectiveStartedAt(DateTimeUtil.toKstOffsetDateTime(feePolicyHistory.getEffectiveStartedAt()))
                        .feeRatePercent(feePolicyHistory.getFeeRatePercent())
                        .build()
                )
                .toList();
    }
}
