package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.AdminLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.AdminLoginResponse;
import com.liveclass.be_b.api.auth.usecase.AdminAuthUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController implements AdminAuthControllerDocs{
    private final AdminAuthUseCase adminAuthUseCase;

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(AdminLoginRequest request) {
        AdminLoginResponse adminLoginResponse = adminAuthUseCase.adminLogin(request);
        return ApiResponse.ok(adminLoginResponse);
    }
}
