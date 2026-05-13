package com.liveclass.be_b.domain.settlement.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "settlement",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_settlement_creator_year_month", columnNames = {"creator_id", "year_month"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Creator creator;

    @Column(name = "year_month", length = 7, nullable = false)
    private String yearMonth;

    @Column(name = "gross_sales_amount", nullable = false)
    private Long grossSalesAmount;

    @Column(name = "refund_amount", nullable = false)
    private Long refundAmount;

    @Column(name = "net_sales_amount", nullable = false)
    private Long netSalesAmount;

    @Column(name = "platform_fee_amount", nullable = false)
    private Long platformFeeAmount;

    @Column(name = "expected_payout_amount", nullable = false)
    private Long expectedPayoutAmount;

    @Column(name = "sale_count", nullable = false)
    private Integer saleCount;

    @Column(name = "cancel_count", nullable = false)
    private Integer cancelCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private SettlementStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public void confirm(LocalDateTime confirmedAt) {
        if (status != SettlementStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_SETTLEMENT_CONFIRM);
        }

        this.status = SettlementStatus.CONFIRMED;
        this.confirmedAt = confirmedAt;
    }

    public void pay(LocalDateTime paidAt) {
        if (status != SettlementStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.INVALID_SETTLEMENT_PAY);
        }

        this.status = SettlementStatus.PAID;
        this.paidAt = paidAt;
    }

    public static Settlement create(
            String id,
            Creator creator,
            String yearMonth,
            Long grossSalesAmount,
            Long refundAmount,
            Long netSalesAmount,
            Long platformFeeAmount,
            Long expectedPayoutAmount,
            Integer saleCount,
            Integer cancelCount,
            LocalDateTime requestedAt
    ) {
        Settlement settlement = new Settlement();

        settlement.id = id;
        settlement.creator = creator;
        settlement.yearMonth = yearMonth;
        settlement.grossSalesAmount = grossSalesAmount;
        settlement.refundAmount = refundAmount;
        settlement.netSalesAmount = netSalesAmount;
        settlement.platformFeeAmount = platformFeeAmount;
        settlement.expectedPayoutAmount = expectedPayoutAmount;
        settlement.saleCount = saleCount;
        settlement.cancelCount = cancelCount;
        settlement.status = SettlementStatus.PENDING;
        settlement.requestedAt = requestedAt;

        return settlement;
    }
}
