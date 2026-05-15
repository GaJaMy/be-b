package com.liveclass.be_b.service.settlement;

import com.liveclass.be_b.api.settlement.dto.internal.SettlementMetrics;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import com.liveclass.be_b.repository.settlement.SettlementRepository;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private SettlementRepository settlementRepository;

    @InjectMocks
    private SettlementService settlementService;

    @Test
    @DisplayName("동일 연월 정산이 이미 존재하면 생성 시 예외가 발생한다")
    void createSettlementDuplicate() {
        Creator creator = TestFixtureFactory.creator();
        SettlementMetrics metrics = TestFixtureFactory.settlementMetrics();
        LocalDateTime requestedAt = LocalDateTime.of(2025, 4, 1, 9, 0);
        when(settlementRepository.existsByCreatorAndYearMonth(creator, "2025-03")).thenReturn(true);

        assertThatThrownBy(() -> settlementService.createSettlement(
                creator,
                YearMonth.of(2025, 3),
                metrics,
                requestedAt
        ))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_EXISTS_SETTLEMENT);
    }

    @Test
    @DisplayName("정산을 생성할 수 있다")
    void createSettlement() {
        Creator creator = TestFixtureFactory.creator();
        SettlementMetrics metrics = TestFixtureFactory.settlementMetrics();
        LocalDateTime requestedAt = LocalDateTime.of(2025, 4, 1, 9, 0);
        when(settlementRepository.existsByCreatorAndYearMonth(creator, "2025-03")).thenReturn(false);
        when(settlementRepository.save(any(Settlement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Settlement result = settlementService.createSettlement(
                creator,
                YearMonth.of(2025, 3),
                metrics,
                requestedAt
        );

        assertThat(result.getId()).startsWith("settlement-");
        assertThat(result.getCreator()).isSameAs(creator);
        assertThat(result.getYearMonth()).isEqualTo("2025-03");
        assertThat(result.getStatus()).isEqualTo(SettlementStatus.PENDING);
        assertThat(result.getRequestedAt()).isEqualTo(requestedAt);
    }

    @Test
    @DisplayName("정산 저장 시 DB 유니크 충돌이 나면 중복 정산 예외로 변환한다")
    void createSettlementDuplicateByDataIntegrityViolation() {
        Creator creator = TestFixtureFactory.creator();
        SettlementMetrics metrics = TestFixtureFactory.settlementMetrics();
        LocalDateTime requestedAt = LocalDateTime.of(2025, 4, 1, 9, 0);
        when(settlementRepository.existsByCreatorAndYearMonth(creator, "2025-03")).thenReturn(false);
        when(settlementRepository.save(any(Settlement.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> settlementService.createSettlement(
                creator,
                YearMonth.of(2025, 3),
                metrics,
                requestedAt
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_EXISTS_SETTLEMENT);
    }

    @Test
    @DisplayName("크리에이터 정산 목록을 조회할 수 있다")
    void queryCreatorSettlements() {
        Creator creator = TestFixtureFactory.creator();
        List<Settlement> settlements = List.of(TestFixtureFactory.settlement(creator));
        when(settlementRepository.findByCreatorAndYearMonthBetweenOrderByYearMonthAsc(creator, "2025-03", "2025-05"))
                .thenReturn(settlements);

        List<Settlement> result = settlementService.queryCreatorSettlements(creator, YearMonth.of(2025, 3), YearMonth.of(2025, 5));

        assertThat(result).containsExactlyElementsOf(settlements);
    }

    @Test
    @DisplayName("운영자 정산 목록을 조회할 수 있다")
    void queryAdminSettlements() {
        List<Settlement> settlements = List.of(TestFixtureFactory.settlement(TestFixtureFactory.creator()));
        when(settlementRepository.findAllByYearMonthBetweenWithCreator("2025-03", "2025-05"))
                .thenReturn(settlements);

        List<Settlement> result = settlementService.queryAdminSettlements(YearMonth.of(2025, 3), YearMonth.of(2025, 5));

        assertThat(result).containsExactlyElementsOf(settlements);
    }

    @Test
    @DisplayName("정산을 확정할 수 있다")
    void confirmSettlement() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.of(settlement));

        Settlement result = settlementService.confirmSettlement("settlement-1");

        assertThat(result.getStatus()).isEqualTo(SettlementStatus.CONFIRMED);
        assertThat(result.getConfirmedAt()).isNotNull();
    }

    @Test
    @DisplayName("정산을 지급 처리할 수 있다")
    void paySettlement() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        settlement.confirm(LocalDateTime.of(2025, 4, 2, 10, 0));
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.of(settlement));

        Settlement result = settlementService.paySettlement("settlement-1");

        assertThat(result.getStatus()).isEqualTo(SettlementStatus.PAID);
        assertThat(result.getPaidAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 확정된 정산은 다시 확정할 수 없다")
    void confirmSettlementInvalidStatus() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        settlement.confirm(LocalDateTime.of(2025, 4, 2, 10, 0));
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.of(settlement));

        assertThatThrownBy(() -> settlementService.confirmSettlement("settlement-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SETTLEMENT_CONFIRM);
    }

    @Test
    @DisplayName("확정되지 않은 정산은 지급 처리할 수 없다")
    void paySettlementInvalidStatus() {
        Settlement settlement = TestFixtureFactory.settlement(TestFixtureFactory.creator());
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.of(settlement));

        assertThatThrownBy(() -> settlementService.paySettlement("settlement-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SETTLEMENT_PAY);
    }

    @Test
    @DisplayName("정산이 없으면 예외가 발생한다")
    void findSettlementNotFound() {
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settlementService.confirmSettlement("settlement-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_SETTLEMENT);
    }

    @Test
    @DisplayName("지급 처리 대상 정산이 없으면 예외가 발생한다")
    void paySettlementNotFound() {
        when(settlementRepository.findByIdWithCreator("settlement-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settlementService.paySettlement("settlement-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_SETTLEMENT);
    }

}
