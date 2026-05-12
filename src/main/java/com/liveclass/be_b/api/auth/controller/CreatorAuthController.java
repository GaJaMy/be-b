package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.CreatorLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.CreatorLoginResponse;
import com.liveclass.be_b.api.auth.usecase.CreatorAuthUseCase;
import com.liveclass.be_b.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creator/auth")
@RequiredArgsConstructor
public class CreatorAuthController implements CreatorAuthControllerDocs{
    private final CreatorAuthUseCase creatorAuthUseCase;

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CreatorLoginResponse>> login(CreatorLoginRequest request) {
        CreatorLoginResponse creatorLoginResponse = creatorAuthUseCase.creatorLogin(request);
        return ApiResponse.ok(creatorLoginResponse);
    }
}
