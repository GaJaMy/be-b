package com.liveclass.be_b.api.auth.controller;

import com.liveclass.be_b.api.auth.dto.request.CreatorLoginRequest;
import com.liveclass.be_b.api.auth.dto.response.CreatorLoginResponse;
import com.liveclass.be_b.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CreatorAuthControllerDocs {

    ResponseEntity<ApiResponse<CreatorLoginResponse>> login(
            @Valid @RequestBody CreatorLoginRequest request
    );
}
