package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SaleUseCaseDateRangeTest {

    @Mock
    private SaleRecordService saleRecordService;

    @Mock
    private CourseService courseService;

    @Mock
    private CreatorService creatorService;

    @Mock
    private FeePolicyHistoryService feePolicyHistoryService;

    @InjectMocks
    private SaleUseCase saleUseCase;

    @Test
    @DisplayName("판매 목록 조회 시 기간이 잘못되면 예외가 발생한다")
    void querySaleInvalidRange() {
        assertThatThrownBy(() -> saleUseCase.querySale(
                "creator-1",
                LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 1)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_DATE_RANGE);
    }
}
