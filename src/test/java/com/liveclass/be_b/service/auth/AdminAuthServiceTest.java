package com.liveclass.be_b.service.auth;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.security.AdminAuthenticationProvider;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import com.liveclass.be_b.security.jwt.JwtProvider;
import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @Mock
    private AdminAuthenticationProvider adminAuthenticationProvider;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AdminAuthService adminAuthService;

    @Test
    @DisplayName("운영자 로그인에 성공하면 액세스 토큰을 반환한다")
    void adminLoginSuccess() {
        AuthenticatedPrincipal principal = TestFixtureFactory.adminPrincipal();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(adminAuthenticationProvider.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtProvider.generateAccessToken(
                principal.getPrincipalId(),
                principal.getUsername(),
                principal.getRole(),
                AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE
        )).thenReturn("admin-access-token");

        AdminLoginResult result = adminAuthService.adminLogin("admin-1", "qwe123123!");

        assertThat(result.getAccessToken()).isEqualTo("admin-access-token");
    }

    @Test
    @DisplayName("운영자 로그인에 실패하면 비즈니스 예외가 발생한다")
    void adminLoginFailure() {
        when(adminAuthenticationProvider.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> adminAuthService.adminLogin("admin-1", "wrong-password"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_ADMIN_ID_OR_PASSWORD);
    }
}
