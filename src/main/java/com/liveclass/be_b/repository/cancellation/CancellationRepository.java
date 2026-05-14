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

    @Query("""
            select coalesce(sum(cr.refundAmount), 0)
            from CancellationRecord cr
            where cr.saleRecord = :saleRecord
            """)
    Long sumRefundAmountBySaleRecord(@Param("saleRecord") SaleRecord saleRecord);

    @Query("""
            select cr
            from CancellationRecord cr
            join fetch cr.saleRecord sr
            where cr.creator = :creator
                and cr.canceledAt >= :from and cr.canceledAt < :to
            """)
    List<CancellationRecord> findByCreatorAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
            @Param("creator") Creator creator,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
            select cr
            from CancellationRecord cr
            join fetch cr.saleRecord sr
            where cr.creator in :creatorList
                and cr.canceledAt >= :from and cr.canceledAt < :to
            """)
    List<CancellationRecord> findByCreatorInAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
            @Param("creatorList") List<Creator> creatorList,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
