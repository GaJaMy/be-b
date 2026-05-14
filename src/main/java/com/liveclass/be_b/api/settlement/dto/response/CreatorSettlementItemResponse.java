package com.liveclass.be_b.api.settlement.dto.response;

import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "크리에이터 실제 정산 목록 항목")
public class CreatorSettlementItemResponse {
    @Schema(description = "정산 ID", example = "settlement-1")
    private String settlementId;
    @Schema(description = "크리에이터 ID", example = "creator-1")
    private String creatorId;
    @Schema(description = "정산 대상 연월", example = "2025-03")
    private String yearMonth;
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
    @Schema(description = "정산 상태", example = "PENDING")
    private SettlementStatus status;
    @Schema(description = "정산 요청 시각", example = "2025-04-01T09:00:00")
    private LocalDateTime requestedAt;
    @Schema(description = "정산 확정 시각", example = "2025-04-02T10:00:00", nullable = true)
    private LocalDateTime confirmedAt;
    @Schema(description = "정산 지급 시각", example = "2025-04-03T11:00:00", nullable = true)
    private LocalDateTime paidAt;

    public static CreatorSettlementItemResponse from(Settlement settlement) {
        return CreatorSettlementItemResponse.builder()
                .settlementId(settlement.getId())
                .creatorId(settlement.getCreator().getId())
                .yearMonth(settlement.getYearMonth())
                .grossSalesAmount(settlement.getGrossSalesAmount())
                .refundAmount(settlement.getRefundAmount())
                .netSalesAmount(settlement.getNetSalesAmount())
                .platformFeeAmount(settlement.getPlatformFeeAmount())
                .expectedPayoutAmount(settlement.getExpectedPayoutAmount())
                .saleCount(settlement.getSaleCount())
                .cancelCount(settlement.getCancelCount())
                .status(settlement.getStatus())
                .requestedAt(settlement.getRequestedAt())
                .confirmedAt(settlement.getConfirmedAt())
                .paidAt(settlement.getPaidAt())
                .build();
    }
}
