package com.liveclass.be_b.api.auth.usecase;

import com.liveclass.be_b.api.auth.dto.request.AdminLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.AdminLoginResponse;
import com.liveclass.be_b.service.auth.AdminAuthService;
import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthUseCase {
    private final AdminAuthService adminAuthService;

    public AdminLoginResponse adminLogin(AdminLoginRequest request) {
        AdminLoginResult adminLoginResult = adminAuthService.adminLogin(request.getLoginId(), request.getPassword());
        return AdminLoginResponse.from(adminLoginResult);
    }
}
