package com.liveclass.be_b.repository.sale;

import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, String> {
    @Query("""
                select sr
                from SaleRecord sr
                join fetch sr.course
                where sr.creator = :creator
                    and sr.paidAt between :from and :to
            """)
    List<SaleRecord> findByCreatorAndPaidAtBetweenWithCreator(
            @Param("creator") Creator creator,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
                select sr
                from SaleRecord sr
                join fetch sr.creator
                where sr.id = :saleId
            """)
    Optional<SaleRecord> findByIdWithCreator(
            @Param("saleId") String saleId
    );
}
