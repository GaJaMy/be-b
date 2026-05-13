package com.liveclass.be_b.domain.cancellation.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cancellation_record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancellationRecord extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_record_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SaleRecord saleRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Creator creator;

    @Column(name = "refund_amount", nullable = false)
    private Long refundAmount;

    @Column(name = "canceled_at", nullable = false)
    private LocalDateTime canceledAt;

    public static CancellationRecord create(String id, SaleRecord saleRecord, Long refundAmount, LocalDateTime canceledAt) {
        CancellationRecord cancellationRecord = new CancellationRecord();

        cancellationRecord.id = id;
        cancellationRecord.saleRecord = saleRecord;
        cancellationRecord.creator = saleRecord.getCreator();
        cancellationRecord.refundAmount = refundAmount;
        cancellationRecord.canceledAt = canceledAt;

        return cancellationRecord;
    }
}
