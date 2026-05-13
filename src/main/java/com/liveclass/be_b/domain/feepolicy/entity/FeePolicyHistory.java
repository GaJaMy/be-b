package com.liveclass.be_b.domain.feepolicy.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "fee_policy_history",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_fee_policy_history_target_year_month", columnNames = "target_year_month")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeePolicyHistory extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @Column(name = "fee_rate_percent", nullable = false)
    private Integer feeRatePercent;

    @Column(name = "target_year_month", length = 7, nullable = false)
    private String targetYearMonth;

    public static FeePolicyHistory create(String id, Integer feeRatePercent, String targetYearMonth) {
        FeePolicyHistory feePolicyHistory = new FeePolicyHistory();

        feePolicyHistory.id = id;
        feePolicyHistory.feeRatePercent = feeRatePercent;
        feePolicyHistory.targetYearMonth = targetYearMonth;

        return feePolicyHistory;
    }
}
