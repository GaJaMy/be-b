package com.liveclass.be_b.api.feepolicy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "현재 수수료율 변경 요청")
public class FeePolicyUpdateRequest {

    @Schema(description = "즉시 적용할 수수료율", example = "25", minimum = "0", maximum = "100")
    @NotNull(message = "수수로율은 필수 입니다.")
    @Min(0)
    @Max(100)
    private Integer feeRatePercent;
}
