package com.liveclass.be_b.service.settlement;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.api.settlement.dto.internal.SettlementMetrics;
import com.liveclass.be_b.repository.settlement.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private static final String SETTLEMENT_ID_PREFIX = "settlement";
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final SettlementRepository settlementRepository;

    @Transactional(readOnly = true)
    public void validateNotExists(Creator creator, YearMonth yearMonth) {
        if (settlementRepository.existsByCreatorAndYearMonth(creator, yearMonth.toString())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_SETTLEMENT);
        }
    }

    @Transactional
    public Settlement createSettlement(
            Creator creator,
            YearMonth yearMonth,
            SettlementMetrics settlementMetrics,
            LocalDateTime requestedAt
    ) {
        String uuid = UUID.randomUUID().toString();
        String settlementId = String.format("%s-%s", SETTLEMENT_ID_PREFIX, uuid);

        Settlement settlement = Settlement.create(
                settlementId,
                creator,
                yearMonth.toString(),
                settlementMetrics.grossSalesAmount(),
                settlementMetrics.refundAmount(),
                settlementMetrics.netSalesAmount(),
                settlementMetrics.platformFeeAmount(),
                settlementMetrics.expectedPayoutAmount(),
                settlementMetrics.saleCount(),
                settlementMetrics.cancelCount(),
                requestedAt
        );

        return settlementRepository.save(settlement);
    }

    @Transactional(readOnly = true)
    public List<Settlement> queryCreatorSettlements(Creator creator, YearMonth from, YearMonth to) {
        return settlementRepository.findByCreatorAndYearMonthBetweenOrderByYearMonthAsc(
                creator,
                from.toString(),
                to.toString()
        );
    }

    @Transactional(readOnly = true)
    public List<Settlement> queryAdminSettlements(YearMonth from, YearMonth to) {
        return settlementRepository.findAllByYearMonthBetweenWithCreator(
                from.toString(),
                to.toString()
        );
    }

    @Transactional
    public Settlement confirmSettlement(String settlementId) {
        Settlement settlement = findSettlement(settlementId);
        settlement.confirm(LocalDateTime.now(KST_ZONE_ID));
        return settlement;
    }

    @Transactional
    public Settlement paySettlement(String settlementId) {
        Settlement settlement = findSettlement(settlementId);
        settlement.pay(LocalDateTime.now(KST_ZONE_ID));
        return settlement;
    }

    private Settlement findSettlement(String settlementId) {
        return settlementRepository.findByIdWithCreator(settlementId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SETTLEMENT));
    }
}
