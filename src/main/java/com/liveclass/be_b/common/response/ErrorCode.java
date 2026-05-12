package com.liveclass.be_b.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("COMMON_001", "잘못된 입력값입니다.", 400),
    INTERNAL_SERVER_ERROR("COMMON_002", "서버 내부 오류입니다.", 500),

    INVALID_ACCESS_TOKEN("AUTH_001", "유효하지 않은 액세스 토큰입니다.", 401),
    EXPIRED_ACCESS_TOKEN("AUTH_002", "만료된 액세스 토큰입니다.", 401);

    private final String code;
    private final String message;
    private final int status;
}
