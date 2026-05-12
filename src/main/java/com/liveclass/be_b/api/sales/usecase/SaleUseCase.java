package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.api.sales.dto.request.SaleRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.SaleRegisterResponse;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleUseCase {
    private final SaleRecordService saleRecordService;
    private final CourseService courseService;

    public SaleRegisterResponse registerSale(SaleRegisterRequest request) {

        Course course = courseService.findCourse(request.getCourseId());

        String saleId = saleRecordService.registerSale(
                course,
                request.getStudentId(),
                request.getAmount(),
                request.getPaidAt()
        );

        return SaleRegisterResponse.of(saleId);
    }
}
