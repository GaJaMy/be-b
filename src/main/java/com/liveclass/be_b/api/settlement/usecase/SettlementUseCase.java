package com.liveclass.be_b.api.settlement.usecase;

import com.liveclass.be_b.api.settlement.dto.internal.SettlementMetrics;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryItemResponse;
import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorMonthlySettlementResponse;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SettlementUseCase {
    private static final double PLATFORM_FEE_RATE = 0.2;

    private final SaleRecordService saleRecordService;
    private final CancellationService cancellationService;
    private final CreatorService creatorService;

    @Transactional(readOnly = true)
    public CreatorMonthlySettlementResponse queryCreatorMonthlySettlement(String creatorId ,YearMonth yearMonth) {
        Creator creator = creatorService.findCreator(creatorId);

        LocalDateTime from = DateTimeUtil.startOfMonth(yearMonth);
        LocalDateTime to = DateTimeUtil.endExclusiveOfMonth(yearMonth);

        List<SaleRecord> saleRecordList = saleRecordService.querySaleBetweenFromTo(creator, from, to);
        List<CancellationRecord> cancellationRecordList = cancellationService.queryCancellationBetweenFromTo(creator, from, to);

        SettlementMetrics settlementMetrics = calculateSettlementMetrics(saleRecordList, cancellationRecordList);

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
        List<Creator> allCreators = creatorService.findAllCreators();
        LocalDateTime fromDateTime = DateTimeUtil.startOfDay(from);
        LocalDateTime toDateTime = DateTimeUtil.endExclusiveOfDay(to);

        List<SaleRecord> saleRecordList =
                saleRecordService.querySaleBetweenFromTo(allCreators, fromDateTime, toDateTime);
        List<CancellationRecord> cancellationRecordList =
                cancellationService.queryCancellationBetweenFromTo(allCreators, fromDateTime, toDateTime);

        Map<String, List<SaleRecord>> groupedSaleRecord = saleRecordList.stream()
                .collect(
                        Collectors.groupingBy(
                                saleRecord -> saleRecord.getCreator().getId()
                        )
                );

        Map<String, List<CancellationRecord>> groupedCancellationRecord = cancellationRecordList.stream()
                .collect(
                        Collectors.groupingBy(
                                cancellationRecord -> cancellationRecord.getCreator().getId()
                        )
                );

        long totalExpectedPayoutAmount = 0;
        List<AdminSettlementSummaryItemResponse> itemList = new ArrayList<>();
        for (Creator creator : allCreators) {
            List<SaleRecord> saleRecords = groupedSaleRecord.getOrDefault(creator.getId(), List.of());
            List<CancellationRecord> cancellationRecords = groupedCancellationRecord.getOrDefault(creator.getId(), List.of());

            if (saleRecords.isEmpty() && cancellationRecords.isEmpty()) {
                continue;
            }

            SettlementMetrics settlementMetrics = calculateSettlementMetrics(saleRecords, cancellationRecords);

            itemList.add(
                    AdminSettlementSummaryItemResponse.of(
                            creator.getId(),
                            creator.getName(),
                            settlementMetrics.expectedPayoutAmount()
                    )
            );

            totalExpectedPayoutAmount += settlementMetrics.expectedPayoutAmount();
        }

        return AdminSettlementSummaryResponse.of(
                itemList,
                totalExpectedPayoutAmount
        );
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
        long platformFeeAmount = (long) Math.ceil(netSalesAmount * PLATFORM_FEE_RATE);
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
}
