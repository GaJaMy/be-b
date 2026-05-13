package com.liveclass.be_b.repository.feepolicy;

import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeePolicyHistoryRepository extends JpaRepository<FeePolicyHistory, String> {
    Optional<FeePolicyHistory> findByTargetYearMonth(String targetYearMonth);

    List<FeePolicyHistory> findByTargetYearMonthBetween(String fromYearMonth, String toYearMonth);
}
