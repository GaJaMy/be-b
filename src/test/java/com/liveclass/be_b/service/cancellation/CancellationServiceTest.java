package com.liveclass.be_b.service.cancellation;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.cancellation.CancellationRepository;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancellationServiceTest {

    @Mock
    private CancellationRepository cancellationRepository;

    @InjectMocks
    private CancellationService cancellationService;

    @Test
    @DisplayName("취소를 등록할 수 있다")
    void registerCancellation() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        LocalDateTime canceledAt = saleRecord.getPaidAt().plusDays(1);

        when(cancellationRepository.existsById("cancel-1")).thenReturn(false);
        when(cancellationRepository.sumRefundAmountBySaleRecord(saleRecord)).thenReturn(0L);
        when(cancellationRepository.save(any(CancellationRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = cancellationService.registerCancellation("cancel-1", saleRecord, 30000L, canceledAt);

        assertThat(result).isEqualTo("cancel-1");
    }

    @Test
    @DisplayName("중복된 취소 ID면 예외가 발생한다")
    void registerCancellationDuplicate() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        when(cancellationRepository.existsById("cancel-1")).thenReturn(true);

        assertThatThrownBy(() -> cancellationService.registerCancellation("cancel-1", saleRecord, 10000L, saleRecord.getPaidAt().plusDays(1)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_CANCELLATION);
    }

    @Test
    @DisplayName("취소 저장 시 DB 유니크 충돌이 나면 중복 취소 예외로 변환한다")
    void registerCancellationDuplicateByDataIntegrityViolation() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        LocalDateTime canceledAt = saleRecord.getPaidAt().plusDays(1);
        when(cancellationRepository.existsById("cancel-1")).thenReturn(false);
        when(cancellationRepository.sumRefundAmountBySaleRecord(saleRecord)).thenReturn(0L);
        when(cancellationRepository.save(any(CancellationRecord.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> cancellationService.registerCancellation("cancel-1", saleRecord, 30000L, canceledAt))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_CANCELLATION);
    }

    @Test
    @DisplayName("취소 일시가 결제 일시보다 빠르면 예외가 발생한다")
    void registerCancellationInvalidDate() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        when(cancellationRepository.existsById("cancel-1")).thenReturn(false);

        assertThatThrownBy(() -> cancellationService.registerCancellation("cancel-1", saleRecord, 10000L, saleRecord.getPaidAt().minusMinutes(1)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CANCELLATION_DATE);
    }

    @Test
    @DisplayName("누적 환불액이 원결제 금액을 초과하면 예외가 발생한다")
    void registerCancellationOverSaleAmount() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        when(cancellationRepository.existsById("cancel-1")).thenReturn(false);
        when(cancellationRepository.sumRefundAmountBySaleRecord(saleRecord)).thenReturn(40000L);

        assertThatThrownBy(() -> cancellationService.registerCancellation("cancel-1", saleRecord, 20000L, saleRecord.getPaidAt().plusDays(1)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.OVER_SALE_AMOUNT);
    }

    @Test
    @DisplayName("크리에이터 기간별 취소 내역을 조회할 수 있다")
    void queryCancellationBetweenFromTo() {
        Creator creator = TestFixtureFactory.creator();
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(creator), creator);
        List<CancellationRecord> cancellations = List.of(
                TestFixtureFactory.cancellationRecord(saleRecord, 30000L, saleRecord.getPaidAt().plusDays(1))
        );
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 1, 0, 0);
        when(cancellationRepository.findByCreatorAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(creator, from, to))
                .thenReturn(cancellations);

        List<CancellationRecord> result = cancellationService.queryCancellationBetweenFromTo(creator, from, to);

        assertThat(result).containsExactlyElementsOf(cancellations);
    }
}
