package com.liveclass.be_b.service.course;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.repository.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional
    public String registerCourse(String courseId, Creator creator, String title, LocalDateTime registeredAt) {
        if (courseRepository.existsById(courseId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_COURSE);
        }

        try {
            Course course = Course.create(courseId, creator, title, registeredAt);
            Course savedCourse = courseRepository.save(course);
            return savedCourse.getId();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_COURSE);
        }
    }

    @Transactional(readOnly = true)
    public Course findCourseForSale(String courseId, LocalDateTime paidAt) {
        Course course = courseRepository.findByIdWithCreatorForSale(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COURSE));

        if (paidAt.isBefore(course.getRegisteredAt())) {
            throw new BusinessException(ErrorCode.INVALID_SALE_DATE);
        }

        return course;
    }
}
