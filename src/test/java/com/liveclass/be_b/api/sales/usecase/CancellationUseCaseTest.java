package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.api.sales.dto.request.CancellationRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.CancellationRegisterResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancellationUseCaseTest {

    @Mock
    private CancellationService cancellationService;

    @Mock
    private SaleRecordService saleRecordService;

    @InjectMocks
    private CancellationUseCase cancellationUseCase;

    @Test
    @DisplayName("취소 등록 요청을 서비스 호출로 변환한다")
    void registerCancellation() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(TestFixtureFactory.course(TestFixtureFactory.creator()), TestFixtureFactory.creator());
        CancellationRegisterRequest request = CancellationRegisterRequest.builder()
                .cancelId("cancel-1")
                .refundAmount(30000L)
                .canceledAt(OffsetDateTime.of(2025, 3, 28, 15, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();

        when(saleRecordService.findSaleRecordWithLock("sale-1")).thenReturn(saleRecord);
        when(cancellationService.registerCancellation(
                eq("cancel-1"),
                eq(saleRecord),
                eq(30000L),
                eq(LocalDateTime.of(2025, 3, 28, 15, 0))
        )).thenReturn("cancel-1");

        CancellationRegisterResponse response = cancellationUseCase.registerCancellation("sale-1", request);

        assertThat(response.getCancelId()).isEqualTo("cancel-1");
    }

    @Test
    @DisplayName("취소 등록 시 판매 내역이 없으면 예외가 발생한다")
    void registerCancellationSaleNotFound() {
        CancellationRegisterRequest request = CancellationRegisterRequest.builder()
                .cancelId("cancel-1")
                .refundAmount(30000L)
                .canceledAt(OffsetDateTime.of(2025, 3, 28, 15, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();
        when(saleRecordService.findSaleRecordWithLock("sale-1"))
                .thenThrow(new BusinessException(ErrorCode.NOT_FOUND_SALE));

        assertThatThrownBy(() -> cancellationUseCase.registerCancellation("sale-1", request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_SALE);
    }
}
