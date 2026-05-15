package com.liveclass.be_b.api.settlement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "운영자 예상 정산 집계 항목")
public class AdminSettlementSummaryItemResponse {
    @Schema(description = "크리에이터 ID", example = "creator-1")
    private String creatorId;
    @Schema(description = "크리에이터 이름", example = "김강사")
    private String creatorName;
    @Schema(description = "정산 예정 금액", example = "120000")
    private long expectedPayoutAmount;

    public static AdminSettlementSummaryItemResponse of(String creatorId, String creatorName, long expectedPayoutAmount) {
        return AdminSettlementSummaryItemResponse.builder()
                .creatorId(creatorId)
                .creatorName(creatorName)
                .expectedPayoutAmount(expectedPayoutAmount)
                .build();
    }
}
