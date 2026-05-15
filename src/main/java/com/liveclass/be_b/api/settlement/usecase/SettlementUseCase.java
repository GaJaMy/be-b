package com.liveclass.be_b.api.settlement.usecase;

import com.liveclass.be_b.api.settlement.dto.internal.SettlementMetrics;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementManagementItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorMonthlySettlementResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorSettlementItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorSettlementCreateResponse;
import com.liveclass.be_b.api.settlement.dto.response.SettlementStatusResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import com.liveclass.be_b.service.settlement.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SettlementUseCase {
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final SaleRecordService saleRecordService;
    private final CancellationService cancellationService;
    private final CreatorService creatorService;
    private final SettlementService settlementService;

    @Transactional(readOnly = true)
    public CreatorMonthlySettlementResponse queryCreatorMonthlySettlement(String creatorId ,YearMonth yearMonth) {
        Creator creator = creatorService.findCreator(creatorId);

        LocalDateTime from = DateTimeUtil.startOfMonth(yearMonth);
        LocalDateTime to = DateTimeUtil.endExclusiveOfMonth(yearMonth);

        List<SaleRecord> saleRecordList = saleRecordService.querySaleBetweenFromTo(creator, from, to);
        List<CancellationRecord> cancellationRecordList = cancellationService.queryCancellationBetweenFromTo(creator, from, to);

        SettlementMetrics settlementMetrics = calculateSettlementMetrics(
                saleRecordList,
                cancellationRecordList
        );

        return CreatorMonthlySettlementResponse.of(
                creator.getId(),
                yearMonth,
                settlementMetrics.grossSalesAmount(),
                settlementMetrics.refundAmount(),
                settlementMetrics.netSalesAmount(),
                settlementMetrics.platformFeeAmount(),
                settlementMetrics.expectedPayoutAmount(),
                settlementMetrics.saleCount(),
                settlementMetrics.cancelCount()
        );
    }

    @Transactional(readOnly = true)
    public AdminSettlementSummaryResponse queryAdminSettlementSummary(LocalDate from, LocalDate to) {
        DateTimeUtil.validateDateRange(from, to);

        List<Creator> allCreators = creatorService.findAllCreators();
        LocalDateTime fromDateTime = DateTimeUtil.startOfDay(from);
        LocalDateTime toDateTime = DateTimeUtil.endExclusiveOfDay(to);

        List<SaleRecord> saleRecordList =
                saleRecordService.querySaleBetweenFromTo(allCreators, fromDateTime, toDateTime);
        List<CancellationRecord> cancellationRecordList =
                cancellationService.queryCancellationBetweenFromTo(allCreators, fromDateTime, toDateTime);

        Map<String, List<SaleRecord>> groupedSaleRecord = saleRecordList.stream()
                .collect(Collectors.groupingBy(
                        saleRecord -> saleRecord.getCreator().getId()
                ));

        Map<String, List<CancellationRecord>> groupedCancellationRecord = cancellationRecordList.stream()
                .collect(Collectors.groupingBy(
                        cancellationRecord -> cancellationRecord.getCreator().getId()
                ));

        long totalExpectedPayoutAmount = 0;
        List<AdminSettlementSummaryItemResponse> itemList = new ArrayList<>();
        for (Creator creator : allCreators) {
            List<SaleRecord> saleRecords = groupedSaleRecord.getOrDefault(creator.getId(), List.of());
            List<CancellationRecord> cancellationRecords =
                    groupedCancellationRecord.getOrDefault(creator.getId(), List.of());

            if (saleRecords.isEmpty() && cancellationRecords.isEmpty()) {
                continue;
            }

            SettlementMetrics settlementMetrics = calculateSettlementMetrics(saleRecords, cancellationRecords);
            long expectedPayoutAmount = settlementMetrics.expectedPayoutAmount();

            itemList.add(
                    AdminSettlementSummaryItemResponse.of(
                            creator.getId(),
                            creator.getName(),
                            expectedPayoutAmount
                    )
            );

            totalExpectedPayoutAmount += expectedPayoutAmount;
        }

        return AdminSettlementSummaryResponse.of(
                itemList,
                totalExpectedPayoutAmount
        );
    }

    @Transactional
    public CreatorSettlementCreateResponse createCreatorSettlement(String creatorId, YearMonth yearMonth) {
        validateSettlementMonthClosed(yearMonth);

        Creator creator = creatorService.findCreator(creatorId);

        LocalDateTime from = DateTimeUtil.startOfMonth(yearMonth);
        LocalDateTime to = DateTimeUtil.endExclusiveOfMonth(yearMonth);

        List<SaleRecord> saleRecords = saleRecordService.querySaleBetweenFromTo(creator, from, to);
        List<CancellationRecord> cancellationRecords =
                cancellationService.queryCancellationBetweenFromTo(creator, from, to);

        SettlementMetrics settlementMetrics = calculateSettlementMetrics(
                saleRecords,
                cancellationRecords
        );

        Settlement settlement = settlementService.createSettlement(
                creator,
                yearMonth,
                settlementMetrics,
                LocalDateTime.now(KST_ZONE_ID)
        );

        return CreatorSettlementCreateResponse.of(settlement.getId(), settlement.getStatus());
    }

    @Transactional(readOnly = true)
    public List<CreatorSettlementItemResponse> queryCreatorSettlements(
            String creatorId,
            YearMonth from,
            YearMonth to
    ) {
        DateTimeUtil.validateYearMonthRange(from, to);

        Creator creator = creatorService.findCreator(creatorId);
        return settlementService.queryCreatorSettlements(creator, from, to).stream()
                .map(CreatorSettlementItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminSettlementManagementItemResponse> queryAdminSettlementManagement(
            YearMonth from,
            YearMonth to
    ) {
        DateTimeUtil.validateYearMonthRange(from, to);

        return settlementService.queryAdminSettlements(from, to).stream()
                .map(AdminSettlementManagementItemResponse::from)
                .toList();
    }

    @Transactional
    public SettlementStatusResponse confirmSettlement(String settlementId) {
        Settlement settlement = settlementService.confirmSettlement(settlementId);
        return SettlementStatusResponse.of(settlement.getId(), settlement.getStatus());
    }

    @Transactional
    public SettlementStatusResponse paySettlement(String settlementId) {
        Settlement settlement = settlementService.paySettlement(settlementId);
        return SettlementStatusResponse.of(settlement.getId(), settlement.getStatus());
    }

    private SettlementMetrics calculateSettlementMetrics(
            List<SaleRecord> saleRecords,
            List<CancellationRecord> cancellationRecords
    ) {
        long grossSalesAmount = saleRecords.stream()
                .mapToLong(SaleRecord::getAmount)
                .sum();

        long refundAmount = cancellationRecords.stream()
                .mapToLong(CancellationRecord::getRefundAmount)
                .sum();

        long netSalesAmount = grossSalesAmount - refundAmount;
        Map<Integer, Long> netSalesAmountByFeeRatePercent = new HashMap<>();

        for (SaleRecord saleRecord : saleRecords) {
            Integer feeRatePercent = saleRecord.getFeeRatePercent();
            Long currentNetSalesAmount = netSalesAmountByFeeRatePercent.get(feeRatePercent);

            if (currentNetSalesAmount == null) {
                netSalesAmountByFeeRatePercent.put(feeRatePercent, saleRecord.getAmount());
                continue;
            }

            long updatedNetSalesAmount = currentNetSalesAmount + saleRecord.getAmount();
            netSalesAmountByFeeRatePercent.put(feeRatePercent, updatedNetSalesAmount);
        }

        for (CancellationRecord cancellationRecord : cancellationRecords) {
            Integer feeRatePercent = cancellationRecord.getSaleRecord().getFeeRatePercent();
            Long currentNetSalesAmount = netSalesAmountByFeeRatePercent.get(feeRatePercent);
            long refundAmountForFeeRate = cancellationRecord.getRefundAmount();

            if (currentNetSalesAmount == null) {
                netSalesAmountByFeeRatePercent.put(feeRatePercent, -refundAmountForFeeRate);
                continue;
            }

            long updatedNetSalesAmount = currentNetSalesAmount - refundAmountForFeeRate;
            netSalesAmountByFeeRatePercent.put(feeRatePercent, updatedNetSalesAmount);
        }

        long platformFeeAmount = 0L;
        for (Map.Entry<Integer, Long> entry : netSalesAmountByFeeRatePercent.entrySet()) {
            Integer feeRatePercent = entry.getKey();
            Long amountByFeeRatePercent = entry.getValue();

            long feeAmount = calculateFeeAmount(amountByFeeRatePercent, feeRatePercent);
            platformFeeAmount += feeAmount;
        }

        long expectedPayoutAmount = netSalesAmount - platformFeeAmount;

        return new SettlementMetrics(
                grossSalesAmount,
                refundAmount,
                netSalesAmount,
                platformFeeAmount,
                expectedPayoutAmount,
                saleRecords.size(),
                cancellationRecords.size()
        );
    }

    private long calculateFeeAmount(Long amount, Integer feeRatePercent) {
        if (amount <= 0) {
            return 0L;
        }

        return BigDecimal.valueOf(amount)
                .multiply(BigDecimal.valueOf(feeRatePercent))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.CEILING)
                .longValue();
    }

    private void validateSettlementMonthClosed(YearMonth yearMonth) {
        YearMonth currentYearMonth = YearMonth.now(KST_ZONE_ID);
        if (!yearMonth.isBefore(currentYearMonth)) {
            throw new BusinessException(ErrorCode.SETTLEMENT_MONTH_NOT_CLOSED);
        }
    }

}
