package com.liveclass.be_b.service.course;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.repository.course.CourseRepository;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("중복된 강의 ID면 예외가 발생한다")
    void registerCourseDuplicate() {
        when(courseRepository.existsById("course-1")).thenReturn(true);

        assertThatThrownBy(() -> courseService.registerCourse(
                "course-1",
                TestFixtureFactory.creator(),
                "Spring Boot 입문",
                LocalDateTime.of(2025, 3, 1, 0, 0)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_COURSE);
    }

    @Test
    @DisplayName("강의 저장 시 DB 유니크 충돌이 나면 중복 강의 예외로 변환한다")
    void registerCourseDuplicateByDataIntegrityViolation() {
        when(courseRepository.existsById("course-1")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> courseService.registerCourse(
                "course-1",
                TestFixtureFactory.creator(),
                "Spring Boot 입문",
                LocalDateTime.of(2025, 3, 1, 0, 0)
        )).isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_COURSE);
    }

    @Test
    @DisplayName("판매 가능한 강의를 조회할 수 있다")
    void findCourseForSale() {
        Course course = TestFixtureFactory.course(TestFixtureFactory.creator());
        when(courseRepository.findByIdWithCreatorForSale("course-1")).thenReturn(Optional.of(course));

        Course result = courseService.findCourseForSale("course-1", LocalDateTime.of(2025, 3, 5, 10, 0));

        assertThat(result).isSameAs(course);
    }

    @Test
    @DisplayName("강의가 없으면 예외가 발생한다")
    void findCourseForSaleNotFound() {
        when(courseRepository.findByIdWithCreatorForSale("course-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findCourseForSale("course-1", LocalDateTime.of(2025, 3, 5, 10, 0)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_COURSE);
    }

    @Test
    @DisplayName("결제 일시가 강의 등록 일시보다 빠르면 예외가 발생한다")
    void findCourseForSaleWithInvalidPaidAt() {
        Course course = TestFixtureFactory.course(TestFixtureFactory.creator());
        when(courseRepository.findByIdWithCreatorForSale("course-1")).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> courseService.findCourseForSale("course-1", LocalDateTime.of(2025, 2, 28, 23, 59)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SALE_DATE);
    }

}
