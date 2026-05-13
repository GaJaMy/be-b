package com.liveclass.be_b.domain.feepolicy.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fee_policy")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeePolicy extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @Column(name = "fee_rate_percent", nullable = false)
    private Integer feeRatePercent;

    public static FeePolicy create(String id, Integer feeRatePercent) {
        FeePolicy feePolicy = new FeePolicy();

        feePolicy.id = id;
        feePolicy.feeRatePercent = feeRatePercent;

        return feePolicy;
    }

    public void updateFeeRatePercent(int feeRatePercent) {
        this.feeRatePercent = feeRatePercent;
    }
}
