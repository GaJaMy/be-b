package com.liveclass.be_b.api.settlement.dto.response;

import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSettlementManagementItemResponse {
    private String settlementId;
    private String creatorId;
    private String creatorName;
    private String yearMonth;
    private long grossSalesAmount;
    private long refundAmount;
    private long netSalesAmount;
    private long platformFeeAmount;
    private long expectedPayoutAmount;
    private int saleCount;
    private int cancelCount;
    private SettlementStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime paidAt;

    public static AdminSettlementManagementItemResponse from(Settlement settlement) {
        return AdminSettlementManagementItemResponse.builder()
                .settlementId(settlement.getId())
                .creatorId(settlement.getCreator().getId())
                .creatorName(settlement.getCreator().getName())
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
