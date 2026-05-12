package com.liveclass.be_b.service.course;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.repository.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private CourseRepository courseRepository;

    public Course findCourse(String courseId) {
        return courseRepository.findByIdWithCreator(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COURSE));
    }
}
