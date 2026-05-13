package com.liveclass.be_b.repository.cancellation;

import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CancellationRepository extends JpaRepository<CancellationRecord, String> {

    List<CancellationRecord> findByCreatorAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
            Creator creator,
            LocalDateTime from,
            LocalDateTime to
    );

    List<CancellationRecord> findByCreatorInAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
            List<Creator> creatoList,
            LocalDateTime from,
            LocalDateTime to
    );
}
