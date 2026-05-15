package com.liveclass.be_b.api.course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의 등록 요청")
public class CourseRegisterRequest {

    @Schema(description = "강의 ID", example = "course-1")
    @NotBlank(message = "강의 ID는 필수입니다.")
    private String courseId;

    @Schema(description = "소유 크리에이터 ID", example = "creator-1")
    @NotBlank(message = "크리에이터 ID는 필수입니다.")
    private String creatorId;

    @Schema(description = "강의명", example = "Spring Boot 입문")
    @NotBlank(message = "강의명은 필수입니다.")
    private String title;

    @Schema(description = "강의 등록 시각", example = "2025-03-01T00:00:00+09:00", type = "string", format = "date-time")
    @NotNull(message = "강의 등록 시각은 필수입니다.")
    private OffsetDateTime registeredAt;
}
