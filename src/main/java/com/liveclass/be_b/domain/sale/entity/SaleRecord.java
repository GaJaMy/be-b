package com.liveclass.be_b.domain.sale.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale_record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleRecord extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Creator creator;

    @Column(name = "student_id", length = 50, nullable = false)
    private String studentId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "fee_rate_percent", nullable = false)
    private Integer feeRatePercent;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    public static SaleRecord create(
            String id,
            Course course,
            Creator creator,
            String studentId,
            Long amount,
            Integer feeRatePercent,
            LocalDateTime paidAt
    ) {
        SaleRecord saleRecord = new SaleRecord();

        saleRecord.id = id;
        saleRecord.course = course;
        saleRecord.creator = creator;
        saleRecord.studentId = studentId;
        saleRecord.amount = amount;
        saleRecord.feeRatePercent = feeRatePercent;
        saleRecord.paidAt = paidAt;

        return saleRecord;
    }
}
