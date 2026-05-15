package com.liveclass.be_b.service.sale;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.sale.SaleRecordRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleRecordServiceTest {

    @Mock
    private SaleRecordRepository saleRecordRepository;

    @InjectMocks
    private SaleRecordService saleRecordService;

    @Test
    @DisplayName("판매를 등록할 수 있다")
    void registerSale() {
        Creator creator = TestFixtureFactory.creator();
        Course course = TestFixtureFactory.course(creator);
        when(saleRecordRepository.existsById("sale-1")).thenReturn(false);
        when(saleRecordRepository.save(any(SaleRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = saleRecordService.registerSale(
                course,
                "sale-1",
                "student-1",
                50000L,
                20,
                LocalDateTime.of(2025, 3, 5, 10, 0)
        );

        assertThat(result).isEqualTo("sale-1");
    }

    @Test
    @DisplayName("중복된 판매 ID면 예외가 발생한다")
    void registerSaleDuplicate() {
        when(saleRecordRepository.existsById("sale-1")).thenReturn(true);

        assertThatThrownBy(() -> saleRecordService.registerSale(
                TestFixtureFactory.course(TestFixtureFactory.creator()),
                "sale-1",
                "student-1",
                50000L,
                20,
                LocalDateTime.of(2025, 3, 5, 10, 0)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_SALE);
    }

    @Test
    @DisplayName("판매 저장 시 DB 유니크 충돌이 나면 중복 판매 예외로 변환한다")
    void registerSaleDuplicateByDataIntegrityViolation() {
        Creator creator = TestFixtureFactory.creator();
        Course course = TestFixtureFactory.course(creator);
        when(saleRecordRepository.existsById("sale-1")).thenReturn(false);
        when(saleRecordRepository.save(any(SaleRecord.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> saleRecordService.registerSale(
                course,
                "sale-1",
                "student-1",
                50000L,
                20,
                LocalDateTime.of(2025, 3, 5, 10, 0)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_SALE);
    }

    @Test
    @DisplayName("판매 내역을 조회할 수 있다")
    void findSaleRecord() {
        SaleRecord saleRecord = TestFixtureFactory.saleRecord(
                TestFixtureFactory.course(TestFixtureFactory.creator()),
                TestFixtureFactory.creator()
        );
        when(saleRecordRepository.findByIdWithCreator("sale-1")).thenReturn(Optional.of(saleRecord));

        SaleRecord result = saleRecordService.findSaleRecord("sale-1");

        assertThat(result).isSameAs(saleRecord);
    }

    @Test
    @DisplayName("판매 내역이 없으면 예외가 발생한다")
    void findSaleRecordNotFound() {
        when(saleRecordRepository.findByIdWithCreator("sale-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleRecordService.findSaleRecord("sale-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_SALE);
    }

    @Test
    @DisplayName("크리에이터 기간별 판매 내역을 조회할 수 있다")
    void querySaleBetweenFromTo() {
        Creator creator = TestFixtureFactory.creator();
        List<SaleRecord> saleRecords = List.of(TestFixtureFactory.saleRecord(TestFixtureFactory.course(creator), creator));
        LocalDateTime from = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 1, 0, 0);
        when(saleRecordRepository.findByCreatorAndPaidAtBetweenWithCreator(creator, from, to)).thenReturn(saleRecords);

        List<SaleRecord> result = saleRecordService.querySaleBetweenFromTo(creator, from, to);

        assertThat(result).containsExactlyElementsOf(saleRecords);
    }
}
