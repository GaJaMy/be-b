package com.liveclass.be_b.service.auth;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.security.AdminAuthenticationProvider;
import com.liveclass.be_b.security.AuthenticatedPrincipal;
import com.liveclass.be_b.security.jwt.JwtProvider;
import com.liveclass.be_b.service.auth.result.AdminLoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
    private final AdminAuthenticationProvider adminAuthenticationProvider;
    private final JwtProvider jwtProvider;

    public AdminLoginResult adminLogin(String loginId, String password) {
        Authentication authentication;
        try {
            authentication = adminAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginId, password)
            );
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.INVALID_ADMIN_ID_OR_PASSWORD);
        }

        AuthenticatedPrincipal authenticationPrincipal =
                (AuthenticatedPrincipal) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(
                authenticationPrincipal.getPrincipalId(),
                authenticationPrincipal.getUsername(),
                authenticationPrincipal.getRole(),
                AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE
        );

        return AdminLoginResult.of(accessToken);
    }

}
