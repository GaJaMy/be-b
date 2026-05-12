package com.liveclass.be_b.repository.cancellation;

import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<CancellationRecord, String> {
}
