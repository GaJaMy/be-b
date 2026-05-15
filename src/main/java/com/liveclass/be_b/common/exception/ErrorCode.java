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
    INVALID_DATE_RANGE("COMMON_005", "조회 시작 값은 조회 종료 값보다 늦을 수 없습니다.", 400),

    INVALID_ACCESS_TOKEN("AUTH_001", "유효하지 않은 액세스 토큰입니다.", 401),
    EXPIRED_ACCESS_TOKEN("AUTH_002", "만료된 액세스 토큰입니다.", 401),
    AUTHENTICATION_REQUIRED("AUTH_003", "인증이 필요합니다.", 401),
    ACCESS_DENIED("AUTH_004", "권한이 없습니다.", 403),

    INVALID_CREATOR_ID_OR_PASSWORD("AUTH_005", "아이디 또는 비밀번호가 올바르지 않습니다.", 401),
    INVALID_ADMIN_ID_OR_PASSWORD("AUTH_006", "아이디 또는 비밀번호가 올바르지 않습니다.", 401),
    NOT_FOUND_CREATOR("AUTH_007", "크리에이터를 찾을 수 없습니다.", 404),

    NOT_FOUND_COURSE("COURSE_001", "강의를 찾을 수 없습니다.", 404),
    DUPLICATE_COURSE("COURSE_002", "이미 존재하는 강의 ID 입니다.", 409),

    NOT_FOUND_SALE("SALE_001", "판매 내역을 찾을 수 없습니다.", 404),
    DUPLICATE_SALE("SALE_002", "이미 존재하는 판매 ID 입니다.", 409),
    INVALID_SALE_DATE("SALE_003", "결제 일시는 강의 등록 일시보다 빠를 수 없습니다.", 422),

    DUPLICATE_CANCELLATION("CANCELLATION_001", "이미 존재하는 취소 ID 입니다.", 409),
    INVALID_CANCELLATION_DATE("CANCELLATION_002", "취소 일시는 결제 일시보다 빠를 수 없습니다.", 422),
    OVER_SALE_AMOUNT("CANCELLATION_003", "환불 금액이 원 결제 금액을 초과할 수 없습니다.", 422),

    NOT_FOUND_FEE_POLICY("FEE_POLICY_001", "수수료율 정책을 찾을 수 없습니다.", 404),
    DUPLICATE_FEE_POLICY_HISTORY("FEE_POLICY_002", "동일한 변경 기준 시각의 수수료율 이력이 이미 존재합니다.", 409),

    ALREADY_EXISTS_SETTLEMENT("SETTLEMENT_001", "동일 연월 정산이 이미 존재합니다.", 409),
    SETTLEMENT_MONTH_NOT_CLOSED("SETTLEMENT_002", "대상 월이 종료되기 전에는 정산을 생성할 수 없습니다.", 400),
    NOT_FOUND_SETTLEMENT("SETTLEMENT_003", "정산 정보를 찾을 수 없습니다.", 404),
    INVALID_SETTLEMENT_CONFIRM("SETTLEMENT_004", "현재 상태에서는 정산 확정을 할 수 없습니다.", 409),
    INVALID_SETTLEMENT_PAY("SETTLEMENT_005", "현재 상태에서는 정산 지급 처리를 할 수 없습니다.", 409)
    ;
    private final String code;
    private final String message;
    private final int status;
}
