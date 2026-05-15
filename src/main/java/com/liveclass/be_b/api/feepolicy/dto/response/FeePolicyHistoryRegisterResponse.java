package com.liveclass.be_b.api.feepolicy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "수수료율 이력 등록 응답")
public class FeePolicyHistoryRegisterResponse {
    @Schema(description = "등록된 수수료율 이력 ID", example = "fee-policy-history-1")
    private String feePolicyHistoryId;
    @Schema(description = "적용 시작 시각", example = "2025-05-10T00:00:00+09:00", type = "string", format = "date-time")
    private OffsetDateTime effectiveStartedAt;
    @Schema(description = "적용 수수료율", example = "30")
    private int feeRatePercent;

    public static FeePolicyHistoryRegisterResponse of(
            String feePolicyHistoryId,
            OffsetDateTime effectiveStartedAt,
            int feeRatePercent
    ) {
        return FeePolicyHistoryRegisterResponse.builder()
                .feePolicyHistoryId(feePolicyHistoryId)
                .effectiveStartedAt(effectiveStartedAt)
                .feeRatePercent(feeRatePercent)
                .build();
    }
}
