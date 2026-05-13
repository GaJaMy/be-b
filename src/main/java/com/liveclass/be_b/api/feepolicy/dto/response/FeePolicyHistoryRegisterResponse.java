package com.liveclass.be_b.api.feepolicy.dto.response;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyHistoryRegisterResponse {
    private String feePolicyHistoryId;
    private YearMonth targetYearMonth;
    private int feeRatePercent;

    public static FeePolicyHistoryRegisterResponse of(
            String feePolicyHistoryId,
            YearMonth targetYearMonth,
            int feeRatePercent
    ) {
        return FeePolicyHistoryRegisterResponse.builder()
                .feePolicyHistoryId(feePolicyHistoryId)
                .targetYearMonth(targetYearMonth)
                .feeRatePercent(feeRatePercent)
                .build();
    }
}
