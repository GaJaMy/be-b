package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeePolicyHistoryUseCase {
    private final FeePolicyHistoryService feePolicyHistoryService;

    @Transactional
    public FeePolicyHistoryRegisterResponse registerFeePolicyHistory(FeePolicyHistoryRegisterRequest request) {
        FeePolicyHistory feePolicyHistory = feePolicyHistoryService.registerFeePolicyHistory(
                request.getTargetYearMonth(),
                request.getFeeRatePercent()
        );

        return FeePolicyHistoryRegisterResponse.of(
                feePolicyHistory.getId(),
                YearMonth.parse(feePolicyHistory.getTargetYearMonth()),
                feePolicyHistory.getFeeRatePercent()
        );
    }

    @Transactional(readOnly = true)
    public List<FeePolicyHistoryQueryResponse> queryFeePolicyHistories() {
        return feePolicyHistoryService.findAllFeePolicyHistories().stream()
                .map(feePolicyHistory -> FeePolicyHistoryQueryResponse.builder()
                        .feePolicyHistoryId(feePolicyHistory.getId())
                        .targetYearMonth(YearMonth.parse(feePolicyHistory.getTargetYearMonth()))
                        .feeRatePercent(feePolicyHistory.getFeeRatePercent())
                        .build()
                )
                .toList();
    }
}
