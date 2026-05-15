package com.liveclass.be_b.api.course.usecase;

import com.liveclass.be_b.api.course.dto.request.CourseRegisterRequest;
import com.liveclass.be_b.api.course.dto.response.CourseRegisterResponse;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.creator.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CourseUseCase {
    private final CourseService courseService;
    private final CreatorService creatorService;

    @Transactional
    public CourseRegisterResponse registerCourse(CourseRegisterRequest request) {
        Creator creator = creatorService.findCreator(request.getCreatorId());
        LocalDateTime registeredAt = DateTimeUtil.toKstLocalDateTime(request.getRegisteredAt());

        String courseId = courseService.registerCourse(
                request.getCourseId(),
                creator,
                request.getTitle(),
                registeredAt
        );

        return CourseRegisterResponse.of(courseId);
    }
}
