package com.liveclass.be_b.api.feepolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyUpdateResponse {
    private int feeRatePercent;

    public static FeePolicyUpdateResponse of(int feeRatePercent) {
        return FeePolicyUpdateResponse.builder()
                .feeRatePercent(feeRatePercent)
                .build();
    }
}
