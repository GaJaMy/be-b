package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleUseCaseTest {

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
    @DisplayName("판매 등록 요청을 서비스 호출로 변환한다")
    void registerSale() {
        Creator creator = TestFixtureFactory.creator();
        Course course = TestFixtureFactory.course(creator);
        SaleRegisterRequest request = SaleRegisterRequest.builder()
                .saleId("sale-1")
                .courseId("course-1")
                .studentId("student-1")
                .amount(50000L)
                .paidAt(OffsetDateTime.of(2025, 3, 5, 10, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();
        LocalDateTime paidAt = LocalDateTime.of(2025, 3, 5, 10, 0);

        when(courseService.findCourseForSale("course-1", paidAt)).thenReturn(course);
        when(feePolicyHistoryService.findFeeRatePercentByAppliedAt(paidAt)).thenReturn(20);
        when(saleRecordService.registerSale(
                eq(course),
                eq("sale-1"),
                eq("student-1"),
                eq(50000L),
                eq(20),
                eq(paidAt)
        )).thenReturn("sale-1");

        SaleRegisterResponse response = saleUseCase.registerSale(request);

        assertThat(response.getSaleId()).isEqualTo("sale-1");
    }

    @Test
    @DisplayName("판매 목록 조회 결과를 응답 DTO로 변환한다")
    void querySale() {
        Creator creator = TestFixtureFactory.creator();
        Course course = TestFixtureFactory.course(creator);
        SaleRecord saleRecord = TestFixtureFactory.saleRecord("sale-1", course, creator, "student-1", 50000L, 20, LocalDateTime.of(2025, 3, 5, 10, 0));
        when(creatorService.findCreator("creator-1")).thenReturn(creator);
        when(saleRecordService.querySaleBetweenFromTo(
                creator,
                LocalDateTime.of(2025, 3, 1, 0, 0),
                LocalDateTime.of(2025, 4, 1, 0, 0)
        )).thenReturn(List.of(saleRecord));

        List<SaleQueryResponse> responses = saleUseCase.querySale(
                "creator-1",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 31)
        );

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().getSaleId()).isEqualTo("sale-1");
        assertThat(responses.getFirst().getFeeRatePercent()).isEqualTo(20);
    }

    @Test
    @DisplayName("판매 등록 시 적용 가능한 수수료율 이력이 없으면 예외가 발생한다")
    void registerSaleFeePolicyHistoryNotFound() {
        Creator creator = TestFixtureFactory.creator();
        Course course = TestFixtureFactory.course(creator);
        SaleRegisterRequest request = SaleRegisterRequest.builder()
                .saleId("sale-1")
                .courseId("course-1")
                .studentId("student-1")
                .amount(50000L)
                .paidAt(OffsetDateTime.of(2025, 3, 5, 10, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();
        LocalDateTime paidAt = LocalDateTime.of(2025, 3, 5, 10, 0);

        when(courseService.findCourseForSale("course-1", paidAt)).thenReturn(course);
        when(feePolicyHistoryService.findFeeRatePercentByAppliedAt(paidAt))
                .thenThrow(new BusinessException(ErrorCode.NOT_FOUND_FEE_POLICY));

        assertThatThrownBy(() -> saleUseCase.registerSale(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_FEE_POLICY);
    }
}
