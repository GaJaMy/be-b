package com.liveclass.be_b.api.course.usecase;

import com.liveclass.be_b.api.course.dto.request.CourseRegisterRequest;
import com.liveclass.be_b.api.course.dto.response.CourseRegisterResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.service.course.CourseService;
import com.liveclass.be_b.service.creator.CreatorService;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseUseCaseTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CreatorService creatorService;

    @InjectMocks
    private CourseUseCase courseUseCase;

    @Test
    @DisplayName("강의 등록 요청을 서비스 호출로 변환한다")
    void registerCourse() {
        Creator creator = TestFixtureFactory.creator();
        CourseRegisterRequest request = CourseRegisterRequest.builder()
                .courseId("course-1")
                .creatorId("creator-1")
                .title("Spring Boot 입문")
                .registeredAt(OffsetDateTime.of(2025, 3, 1, 0, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();

        when(creatorService.findCreator("creator-1")).thenReturn(creator);
        when(courseService.registerCourse(
                eq("course-1"),
                eq(creator),
                eq("Spring Boot 입문"),
                eq(LocalDateTime.of(2025, 3, 1, 0, 0))
        )).thenReturn("course-1");

        CourseRegisterResponse response = courseUseCase.registerCourse(request);

        assertThat(response.getCourseId()).isEqualTo("course-1");
    }

    @Test
    @DisplayName("강의 등록 시 크리에이터가 없으면 예외가 발생한다")
    void registerCourseCreatorNotFound() {
        CourseRegisterRequest request = CourseRegisterRequest.builder()
                .courseId("course-1")
                .creatorId("creator-1")
                .title("Spring Boot 입문")
                .registeredAt(OffsetDateTime.of(2025, 3, 1, 0, 0, 0, 0, ZoneOffset.ofHours(9)))
                .build();
        when(creatorService.findCreator("creator-1"))
                .thenThrow(new BusinessException(ErrorCode.NOT_FOUND_CREATOR));

        assertThatThrownBy(() -> courseUseCase.registerCourse(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_CREATOR);
    }
}
