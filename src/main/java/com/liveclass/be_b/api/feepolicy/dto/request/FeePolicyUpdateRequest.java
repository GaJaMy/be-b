package com.liveclass.be_b.api.feepolicy.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePolicyUpdateRequest {

    @NotNull(message = "수수로율은 필수 입니다.")
    @Min(0)
    @Max(100)
    private Integer feeRatePercent;
}
