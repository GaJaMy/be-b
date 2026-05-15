package com.liveclass.be_b.api.settlement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "크리에이터 월별 예상 정산 조회 응답")
public class CreatorMonthlySettlementResponse {
    @Schema(description = "크리에이터 ID", example = "creator-1")
    private String creatorId;
    @Schema(description = "조회 연월", example = "2025-03", type = "string", pattern = "yyyy-MM")
    private YearMonth yearMonth;
    @Schema(description = "총 판매 금액", example = "260000")
    private long grossSalesAmount;
    @Schema(description = "총 환불 금액", example = "110000")
    private long refundAmount;
    @Schema(description = "순 판매 금액", example = "150000")
    private long netSalesAmount;
    @Schema(description = "플랫폼 수수료 금액", example = "30000")
    private long platformFeeAmount;
    @Schema(description = "정산 예정 금액", example = "120000")
    private long expectedPayoutAmount;
    @Schema(description = "판매 건수", example = "4")
    private int saleCount;
    @Schema(description = "취소 건수", example = "2")
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
