package com.liveclass.be_b.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("COMMON_001", "잘못된 입력값입니다.", 400),
    INTERNAL_SERVER_ERROR("COMMON_002", "서버 내부 오류입니다.", 500);

    private final String code;
    private final String message;
    private final int status;
}
