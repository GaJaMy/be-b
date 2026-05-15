package com.liveclass.be_b.api.course.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의 등록 응답")
public class CourseRegisterResponse {

    @Schema(description = "등록된 강의 ID", example = "course-1")
    private String courseId;

    public static CourseRegisterResponse of(String courseId) {
        return CourseRegisterResponse.builder()
                .courseId(courseId)
                .build();
    }
}
