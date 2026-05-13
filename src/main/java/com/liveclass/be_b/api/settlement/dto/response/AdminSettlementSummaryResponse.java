package com.liveclass.be_b.api.settlement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSettlementSummaryResponse {
    private List<AdminSettlementSummaryItemResponse> items;
    private long totalExpectedPayoutAmount;

    public static AdminSettlementSummaryResponse of(List<AdminSettlementSummaryItemResponse> items, long totalExpectedPayoutAmount) {
        return AdminSettlementSummaryResponse.builder()
                .items(items)
                .totalExpectedPayoutAmount(totalExpectedPayoutAmount)
                .build();
    }
}
