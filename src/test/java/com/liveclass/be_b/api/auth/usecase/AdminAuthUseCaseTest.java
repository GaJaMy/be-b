package com.liveclass.be_b.api.auth.usecase;

import com.liveclass.be_b.api.auth.dto.request.AdminLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.AdminLoginResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.service.auth.AdminAuthService;
import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuthUseCaseTest {

    @Mock
    private AdminAuthService adminAuthService;

    @InjectMocks
    private AdminAuthUseCase adminAuthUseCase;

    @Test
    @DisplayName("운영자 로그인 결과를 응답 DTO로 변환한다")
    void adminLogin() {
        AdminLoginRequest request = AdminLoginRequest.builder()
                .loginId("admin-1")
                .password("qwe123123!")
                .build();
        when(adminAuthService.adminLogin("admin-1", "qwe123123!"))
                .thenReturn(AdminLoginResult.of("admin-access-token"));

        AdminLoginResponse response = adminAuthUseCase.adminLogin(request);

        assertThat(response.getAccessToken()).isEqualTo("admin-access-token");
    }

    @Test
    @DisplayName("운영자 로그인 실패 예외를 그대로 전파한다")
    void adminLoginFailure() {
        AdminLoginRequest request = AdminLoginRequest.builder()
                .loginId("admin-1")
                .password("wrong-password")
                .build();
        when(adminAuthService.adminLogin("admin-1", "wrong-password"))
                .thenThrow(new BusinessException(ErrorCode.INVALID_ADMIN_ID_OR_PASSWORD));

        assertThatThrownBy(() -> adminAuthUseCase.adminLogin(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_ADMIN_ID_OR_PASSWORD);
    }
}
