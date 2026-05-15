package com.liveclass.be_b.service.auth;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import com.liveclass.be_b.security.CreatorAuthenticationProvider;
import com.liveclass.be_b.security.jwt.JwtProvider;
import com.liveclass.be_b.service.auth.result.CreatorLoginResult;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatorAuthServiceTest {

    @Mock
    private CreatorAuthenticationProvider creatorAuthenticationProvider;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private CreatorAuthService creatorAuthService;

    @Test
    @DisplayName("크리에이터 로그인에 성공하면 액세스 토큰을 반환한다")
    void creatorLoginSuccess() {
        AuthenticatedPrincipal principal = TestFixtureFactory.creatorPrincipal();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(creatorAuthenticationProvider.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtProvider.generateAccessToken(
                principal.getPrincipalId(),
                principal.getUsername(),
                principal.getRole(),
                AuthenticatedPrincipal.CREATOR_PRINCIPAL_TYPE
        )).thenReturn("creator-access-token");

        CreatorLoginResult result = creatorAuthService.creatorLogin("creator-1", "qwe123123!");

        assertThat(result.getAccessToken()).isEqualTo("creator-access-token");
    }

    @Test
    @DisplayName("크리에이터 로그인에 실패하면 비즈니스 예외가 발생한다")
    void creatorLoginFailure() {
        when(creatorAuthenticationProvider.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> creatorAuthService.creatorLogin("creator-1", "wrong-password"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CREATOR_ID_OR_PASSWORD);
    }
}
