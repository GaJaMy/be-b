package com.liveclass.be_b.api.settlement.dto.internal;

public record SettlementMetrics(
        long grossSalesAmount,
        long refundAmount,
        long netSalesAmount,
        long platformFeeAmount,
        long expectedPayoutAmount,
        int saleCount,
        int cancelCount
) {
}
