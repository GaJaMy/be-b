package com.liveclass.be_b.api.auth.usecase;

import com.liveclass.be_b.api.auth.dto.request.CreatorLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.CreatorLoginResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.service.auth.CreatorAuthService;
import com.liveclass.be_b.service.auth.result.CreatorLoginResult;
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
class CreatorAuthUseCaseTest {

    @Mock
    private CreatorAuthService creatorAuthService;

    @InjectMocks
    private CreatorAuthUseCase creatorAuthUseCase;

    @Test
    @DisplayName("크리에이터 로그인 결과를 응답 DTO로 변환한다")
    void creatorLogin() {
        CreatorLoginRequest request = CreatorLoginRequest.builder()
                .loginId("creator-1")
                .password("qwe123123!")
                .build();
        when(creatorAuthService.creatorLogin("creator-1", "qwe123123!"))
                .thenReturn(CreatorLoginResult.of("creator-access-token"));

        CreatorLoginResponse response = creatorAuthUseCase.creatorLogin(request);

        assertThat(response.getAccessToken()).isEqualTo("creator-access-token");
    }

    @Test
    @DisplayName("크리에이터 로그인 실패 예외를 그대로 전파한다")
    void creatorLoginFailure() {
        CreatorLoginRequest request = CreatorLoginRequest.builder()
                .loginId("creator-1")
                .password("wrong-password")
                .build();
        when(creatorAuthService.creatorLogin("creator-1", "wrong-password"))
                .thenThrow(new BusinessException(ErrorCode.INVALID_CREATOR_ID_OR_PASSWORD));

        assertThatThrownBy(() -> creatorAuthUseCase.creatorLogin(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CREATOR_ID_OR_PASSWORD);
    }
}
