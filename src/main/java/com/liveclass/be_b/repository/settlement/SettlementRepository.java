package com.liveclass.be_b.repository.settlement;

import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, String> {
    boolean existsByCreatorAndYearMonth(Creator creator, String yearMonth);

    List<Settlement> findByCreatorAndYearMonthBetweenOrderByYearMonthAsc(
            Creator creator,
            String fromYearMonth,
            String toYearMonth
    );

    @Query("""
            select s
            from Settlement s
            join fetch s.creator
            where s.yearMonth between :fromYearMonth and :toYearMonth
            order by s.yearMonth asc, s.creator.id asc
            """)
    List<Settlement> findAllByYearMonthBetweenWithCreator(
            @Param("fromYearMonth") String fromYearMonth,
            @Param("toYearMonth") String toYearMonth
    );

    @Query("""
            select s
            from Settlement s
            join fetch s.creator
            where s.id = :settlementId
            """)
    Optional<Settlement> findByIdWithCreator(@Param("settlementId") String settlementId);
}
