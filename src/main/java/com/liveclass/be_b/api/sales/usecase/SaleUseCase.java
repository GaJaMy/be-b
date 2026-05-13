package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.SaleQueryResponse;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleUseCase {
    private final SaleRecordService saleRecordService;
    private final CourseService courseService;
    private final CreatorService creatorService;

    @Transactional
    public SaleRegisterResponse registerSale(SaleRegisterRequest request) {

        Course course = courseService.findCourse(request.getCourseId());

        String saleId = saleRecordService.registerSale(
                course,
                request.getStudentId(),
                request.getAmount(),
                request.getPaidAt().toLocalDateTime()
        );

        return SaleRegisterResponse.of(saleId);
    }

    @Transactional
    public List<SaleQueryResponse> querySale(String creatorId, LocalDate from, LocalDate to) {
        Creator creator = creatorService.findCreator(creatorId);

        LocalDateTime fromDateTime = DateTimeUtil.startOfDay(from);
        LocalDateTime toDateTime = DateTimeUtil.endExclusiveOfDay(to);

        List<SaleRecord> saleRecords = saleRecordService.querySaleBetweenFromTo(creator, fromDateTime, toDateTime);

        return saleRecords.stream()
                .map(saleRecord -> SaleQueryResponse.of(
                        saleRecord.getId(),
                        saleRecord.getCourse().getId(),
                        saleRecord.getStudentId(),
                        saleRecord.getAmount(),
                        saleRecord.getPaidAt()
                ))
                .toList();
    }
}
