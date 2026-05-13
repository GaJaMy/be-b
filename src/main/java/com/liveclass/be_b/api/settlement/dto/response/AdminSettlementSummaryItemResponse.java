package com.liveclass.be_b.api.settlement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSettlementSummaryItemResponse {
    private String creatorId;
    private String creatorName;
    private long expectedPayoutAmount;

    public static AdminSettlementSummaryItemResponse of(String creatorId, String creatorName, long expectedPayoutAmount) {
        return AdminSettlementSummaryItemResponse.builder()
                .creatorId(creatorId)
                .creatorName(creatorName)
                .expectedPayoutAmount(expectedPayoutAmount)
                .build();
    }
}
