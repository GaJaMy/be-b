package com.liveclass.be_b.api.course.controller;

import com.liveclass.be_b.api.course.dto.request.CourseRegisterRequest;
import com.liveclass.be_b.api.course.dto.response.CourseRegisterResponse;
import com.liveclass.be_b.api.course.usecase.CourseUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController implements CourseControllerDocs {
    private final CourseUseCase courseUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<CourseRegisterResponse>> registerCourse(
            @Valid @RequestBody CourseRegisterRequest request
    ) {
        CourseRegisterResponse response = courseUseCase.registerCourse(request);
        return ApiResponse.ok(response);
    }
}
