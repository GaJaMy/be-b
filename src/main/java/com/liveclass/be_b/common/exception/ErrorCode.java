package com.liveclass.be_b.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("COMMON_001", "잘못된 입력값입니다.", 400),
    INTERNAL_SERVER_ERROR("COMMON_002", "서버 내부 오류입니다.", 500),
    NOT_FOUND_ENDPOINT("COMMON_003", "존재하지 않는 엔드포인트입니다.", 404),
    METHOD_NOT_ALLOWED("COMMON_004", "허용되지 않은 HTTP 메서드입니다.", 405),

    INVALID_ACCESS_TOKEN("AUTH_001", "유효하지 않은 액세스 토큰입니다.", 401),
    EXPIRED_ACCESS_TOKEN("AUTH_002", "만료된 액세스 토큰입니다.", 401),
    AUTHENTICATION_REQUIRED("AUTH_003", "인증이 필요합니다.", 401),
    ACCESS_DENIED("AUTH_004", "권한이 없습니다.", 403),

    INVALID_CREATOR_ID_OR_PASSWORD("AUTH_005", "아이디 또는 비밀번호가 올바르지 않습니다.", 401),
    INVALID_ADMIN_ID_OR_PASSWORD("AUTH_006", "아이디 또는 비밀번호가 올바르지 않습니다.", 401),
    NOT_FOUND_CREATOR("AUTH_007", "크리에이터를 찾을 수 없습니다.", 404),

    NOT_FOUND_COURSE("COURSE_001", "강의를 찾을 수 없습니다.", 404),

    NOT_FOUND_SALE("SALE_001", "판매 내역을 찾을 수 없습니다..", 404)
    ;
    private final String code;
    private final String message;
    private final int status;
}
