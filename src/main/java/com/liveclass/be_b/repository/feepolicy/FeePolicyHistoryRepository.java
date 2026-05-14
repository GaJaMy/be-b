package com.liveclass.be_b.repository.feepolicy;

import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FeePolicyHistoryRepository extends JpaRepository<FeePolicyHistory, String> {
    boolean existsByEffectiveStartedAt(LocalDateTime effectiveStartedAt);

    List<FeePolicyHistory> findAllByOrderByEffectiveStartedAtAsc();
}
