package com.liveclass.be_b.api.settlement.usecase;

import com.liveclass.be_b.api.settlement.dto.response.AdminSettlementSummaryResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorMonthlySettlementResponse;
import com.liveclass.be_b.api.settlement.dto.response.CreatorSettlementCreateResponse;
import com.liveclass.be_b.api.settlement.dto.response.SettlementStatusResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import com.liveclass.be_b.service.settlement.SettlementService;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementUseCaseTest {

    @Mock
    private SaleRecordService saleRecordService;

    @Mock
    private CancellationService cancellationService;

    @Mock
    private CreatorService creatorService;

    @Mock
    private SettlementService settlementService;

    @InjectMocks
    private SettlementUseCase settlementUseCase;

    @Test
    @DisplayName("크리에이터 월별 예상 정산을 계산한다")
    void queryCreatorMonthlySettlement() {
        Creator creator = TestFixtureFactory.creator();
        var course1 = TestFixtureFactory.course("course-1", creator, "Spring Boot 입문");
        var course2 = TestFixtureFactory.course("course-2", creator, "JPA 실전");

        List<SaleRecord> saleRecords = List.of(
                TestFixtureFactory.saleRecord("sale-1", course1, creator, "student-1", 50000L, 20, LocalDateTime.of(2025, 3, 5, 10, 0)),
                TestFixtureFactory.saleRecord("sale-2", course1, creator, "student-2", 50000L, 20, LocalDateTime.of(2025, 3, 15, 14, 30)),
                TestFixtureFactory.saleRecord("sale-3", course2, creator, "student-3", 80000L, 20, LocalDateTime.of(2025, 3, 20, 9, 0)),
                TestFixtureFactory.saleRecord("sale-4", course2, creator, "student-4", 80000L, 20, LocalDateTime.of(2025, 3, 22, 11, 0))
        );
        List<CancellationRecord> cancellationRecords = List.of(
                TestFixtureFactory.cancellationRecord("cancel-1", saleRecords.get(2), 80000L, LocalDateTime.of(2025, 3, 25, 18, 0)),
                TestFixtureFactory.cancellationRecord("cancel-2", saleRecords.get(3), 30000L, LocalDateTime.of(2025, 3, 28, 15, 0))
        );

        when(creatorService.findCreator("creator-1")).thenReturn(creator);
        when(saleRecordService.querySaleBetweenFromTo(
                creator,
                LocalDateTime.of(2025, 3, 1, 0, 0),
                LocalDateTime.of(2025, 4, 1, 0, 0)
        )).thenReturn(saleRecords);
        when(cancellationService.queryCancellationBetweenFromTo(
                creator,
                LocalDateTime.of(2025, 3, 1, 0, 0),
                LocalDateTime.of(2025, 4, 1, 0, 0)
        )).thenReturn(cancellationRecords);

        CreatorMonthlySettlementResponse response =
                settlementUseCase.queryCreatorMonthlySettlement("creator-1", YearMonth.of(2025, 3));

        assertThat(response.getGrossSalesAmount()).isEqualTo(260000L);
        assertThat(response.getRefundAmount()).isEqualTo(110000L);
        assertThat(response.getNetSalesAmount()).isEqualTo(150000L);
        assertThat(response.getPlatformFeeAmount()).isEqualTo(30000L);
        assertThat(response.getExpectedPayoutAmount()).isEqualTo(120000L);
        assertThat(response.getSaleCount()).isEqualTo(4);
        assertThat(response.getCancelCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("운영자 정산 집계 조회 시 기간이 잘못되면 예외가 발생한다")
    void queryAdminSettlementSummaryInvalidRange() {
        assertThatThrownBy(() -> settlementUseCase.queryAdminSettlementSummary(
                LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 1)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_DATE_RANGE);
    }

    @Test
    @DisplayName("운영자 정산 집계 조회 시 정산 예정 금액이 0원이어도 목록에 포함한다")
    void queryAdminSettlementSummaryIncludesZeroExpectedPayout() {
        Creator creator = TestFixtureFactory.creator();
        var course = TestFixtureFactory.course("course-1", creator, "Spring Boot 입문");
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(
                "sale-1",
                course,
                creator,
                "student-1",
                10000L,
                20,
                LocalDateTime.of(2025, 3, 5, 10, 0)
        );
        CancellationRecord cancellationRecord = TestFixtureFactory.cancellationRecord(
                "cancel-1",
                saleRecord,
                10000L,
                LocalDateTime.of(2025, 3, 6, 10, 0)
        );

        when(creatorService.findAllCreators()).thenReturn(List.of(creator));
        when(saleRecordService.querySaleBetweenFromTo(
                List.of(creator),
                LocalDateTime.of(2025, 3, 1, 0, 0),
                LocalDateTime.of(2025, 4, 1, 0, 0)
        )).thenReturn(List.of(saleRecord));
        when(cancellationService.queryCancellationBetweenFromTo(
                List.of(creator),
                LocalDateTime.of(2025, 3, 1, 0, 0),
                LocalDateTime.of(2025, 4, 1, 0, 0)
        )).thenReturn(List.of(cancellationRecord));

        AdminSettlementSummaryResponse response = settlementUseCase.queryAdminSettlementSummary(
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 31)
        );

        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().getFirst().getCreatorId()).isEqualTo("creator-1");
        assertThat(response.getItems().getFirst().getExpectedPayoutAmount()).isZero();
        assertThat(response.getTotalExpectedPayoutAmount()).isZero();
    }

    @Test
    @DisplayName("크리에이터 정산 목록 조회 시 연월 범위가 잘못되면 예외가 발생한다")
    void queryCreatorSettlementsInvalidRange() {
        assertThatThrownBy(() -> settlementUseCase.queryCreatorSettlements(
                "creator-1",
                YearMonth.of(2025, 5),
                YearMonth.of(2025, 4)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_DATE_RANGE);
    }

    @Test
    @DisplayName("운영자 정산 관리 목록 조회 시 연월 범위가 잘못되면 예외가 발생한다")
    void queryAdminSettlementManagementInvalidRange() {
        assertThatThrownBy(() -> settlementUseCase.queryAdminSettlementManagement(
                YearMonth.of(2025, 5),
                YearMonth.of(2025, 4)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_DATE_RANGE);
    }

    @Test
    @DisplayName("월이 종료되지 않았으면 정산을 생성할 수 없다")
    void createCreatorSettlementMonthNotClosed() {
        YearMonth currentYearMonth = YearMonth.now(java.time.ZoneId.of("Asia/Seoul"));

        assertThatThrownBy(() -> settlementUseCase.createCreatorSettlement("creator-1", currentYearMonth))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SETTLEMENT_MONTH_NOT_CLOSED);
    }

    @Test
    @DisplayName("실제 정산을 생성할 수 있다")
    void createCreatorSettlement() {
        Creator creator = TestFixtureFactory.creator();
        Settlement settlement = TestFixtureFactory.settlement(creator);
        when(creatorService.findCreator("creator-1")).thenReturn(creator);
        when(saleRecordService.querySaleBetweenFromTo(eq(creator), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(cancellationService.queryCancellationBetweenFromTo(eq(creator), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(settlementService.createSettlement(eq(creator), eq(YearMonth.of(2025, 3)), any(), any(LocalDateTime.class)))
                .thenReturn(settlement);

        CreatorSettlementCreateResponse response =
                settlementUseCase.createCreatorSettlement("creator-1", YearMonth.of(2025, 3));

        assertThat(response.getSettlementId()).isEqualTo("settlement-1");
        assertThat(response.getStatus()).isEqualTo(SettlementStatus.PENDING);
    }

    @Test
    @DisplayName("정산 확정 결과를 응답으로 변환한다")
    void confirmSettlement() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        settlement.confirm(LocalDateTime.of(2025, 4, 2, 10, 0));
        when(settlementService.confirmSettlement("settlement-1")).thenReturn(settlement);

        SettlementStatusResponse response = settlementUseCase.confirmSettlement("settlement-1");

        assertThat(response.getSettlementId()).isEqualTo("settlement-1");
        assertThat(response.getStatus()).isEqualTo(SettlementStatus.CONFIRMED);
    }

    @Test
    @DisplayName("정산 지급 결과를 응답으로 변환한다")
    void paySettlement() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        settlement.confirm(LocalDateTime.of(2025, 4, 2, 10, 0));
        settlement.pay(LocalDateTime.of(2025, 4, 3, 11, 0));
        when(settlementService.paySettlement("settlement-1")).thenReturn(settlement);

        SettlementStatusResponse response = settlementUseCase.paySettlement("settlement-1");

        assertThat(response.getSettlementId()).isEqualTo("settlement-1");
        assertThat(response.getStatus()).isEqualTo(SettlementStatus.PAID);
    }
}
