package com.liveclass.be_b.service.course;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.repository.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public Course findCourse(String courseId) {
        return courseRepository.findByIdWithCreator(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COURSE));
    }
}
