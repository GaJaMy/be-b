package com.liveclass.be_b.api.course.controller;

import com.liveclass.be_b.api.course.dto.request.CourseRegisterRequest;
import com.liveclass.be_b.api.course.dto.response.CourseRegisterResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Courses", description = "강의 등록 API")
public interface CourseControllerDocs {

    @Operation(
            summary = "강의 등록",
            description = "과제 검증용 원천 데이터를 입력하기 위해 강의를 등록한다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "강의 등록 성공",
                    content = @Content(schema = @Schema(implementation = CourseRegisterResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 강의 ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    ResponseEntity<ApiResponse<CourseRegisterResponse>> registerCourse(
            @Valid @RequestBody CourseRegisterRequest request
    );
}
