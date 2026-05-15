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

import java.time.LocalDateTime;

@Entity
@Table(
        name = "fee_policy_history",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_fee_policy_history_effective_started_at", columnNames = "effective_started_at")
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

    @Column(name = "effective_started_at", nullable = false)
    private LocalDateTime effectiveStartedAt;

    public static FeePolicyHistory create(String id, Integer feeRatePercent, LocalDateTime effectiveStartedAt) {
        FeePolicyHistory feePolicyHistory = new FeePolicyHistory();

        feePolicyHistory.id = id;
        feePolicyHistory.feeRatePercent = feeRatePercent;
        feePolicyHistory.effectiveStartedAt = effectiveStartedAt;

        return feePolicyHistory;
    }
}
