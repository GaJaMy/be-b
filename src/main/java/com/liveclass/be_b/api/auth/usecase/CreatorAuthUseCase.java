package com.liveclass.be_b.api.auth.usecase;

import com.liveclass.be_b.api.auth.dto.request.CreatorLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.CreatorLoginResponse;
import com.liveclass.be_b.service.auth.CreatorAuthService;
import com.liveclass.be_b.service.auth.result.CreatorLoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorAuthUseCase {
    private final CreatorAuthService creatorAuthService;

    public CreatorLoginResponse creatorLogin(CreatorLoginRequest request) {
        CreatorLoginResult creatorLoginResult = creatorAuthService.creatorLogin(request.getLoginId(), request.getPassword());
        return CreatorLoginResponse.from(creatorLoginResult);
    }
}
