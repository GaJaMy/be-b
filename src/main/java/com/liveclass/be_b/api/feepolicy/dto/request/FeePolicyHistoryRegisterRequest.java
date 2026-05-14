package com.liveclass.be_b.api.feepolicy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "수수료율 이력 등록 요청")
public class FeePolicyHistoryRegisterRequest {
    @Schema(description = "수수료율 적용 시작 시각", example = "2025-05-10T00:00:00+09:00", type = "string", format = "date-time")
    @NotNull(message = "수수료율 적용 시작 시각은 필수입니다.")
    private OffsetDateTime effectiveStartedAt;

    @Schema(description = "적용 수수료율", example = "30", minimum = "0", maximum = "100")
    @NotNull(message = "수수로율은 필수 입니다.")
    @Min(0)
    @Max(100)
    private Integer feeRatePercent;
}
