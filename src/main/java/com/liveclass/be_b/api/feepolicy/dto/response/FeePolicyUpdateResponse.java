package com.liveclass.be_b.api.feepolicy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "현재 수수료율 변경 응답")
public class FeePolicyUpdateResponse {
    @Schema(description = "현재 적용 중인 수수료율", example = "25")
    private int feeRatePercent;

    public static FeePolicyUpdateResponse of(int feeRatePercent) {
        return FeePolicyUpdateResponse.builder()
                .feeRatePercent(feeRatePercent)
                .build();
    }
}
