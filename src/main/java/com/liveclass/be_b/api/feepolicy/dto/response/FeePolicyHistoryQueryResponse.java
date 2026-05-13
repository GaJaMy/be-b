package com.liveclass.be_b.api.feepolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyHistoryQueryResponse {
    private String feePolicyHistoryId;
    private YearMonth targetYearMonth;
    private int feeRatePercent;
}
