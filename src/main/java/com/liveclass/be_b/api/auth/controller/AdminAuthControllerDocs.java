package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.AdminLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.AdminLoginResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminAuthControllerDocs {

    ResponseEntity<ApiResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request
    );
}
