package com.liveclass.be_b.api.settlement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorMonthlySettlementResponse {
    private String creatorId;
    private YearMonth yearMonth;
    private long grossSalesAmount;
    private long refundAmount;
    private long netSalesAmount;
    private long platformFeeAmount;
    private long expectedPayoutAmount;
    private int saleCount;
    private int cancelCount;

    public static CreatorMonthlySettlementResponse of(
            String creatorId,
            YearMonth yearMonth,
            long grossSalesAmount,
            long refundAmount,
            long netSalesAmount,
            long platformFeeAmount,
            long expectedPayoutAmount,
            int saleCount,
            int cancelCount
    ) {
        return CreatorMonthlySettlementResponse.builder()
                .creatorId(creatorId)
                .yearMonth(yearMonth)
                .grossSalesAmount(grossSalesAmount)
                .refundAmount(refundAmount)
                .netSalesAmount(netSalesAmount)
                .platformFeeAmount(platformFeeAmount)
                .expectedPayoutAmount(expectedPayoutAmount)
                .saleCount(saleCount)
                .cancelCount(cancelCount)
                .build();
    }
}
