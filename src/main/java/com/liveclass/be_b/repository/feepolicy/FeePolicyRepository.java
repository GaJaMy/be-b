package com.liveclass.be_b.repository.feepolicy;

import com.liveclass.be_b.domain.feepolicy.entity.FeePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeePolicyRepository extends JpaRepository<FeePolicy, String> {
    Optional<FeePolicy> findFirstByOrderByCreatedAtAsc();
}
