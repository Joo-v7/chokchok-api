package com.chokchok.chokchokapi.common.exception.code;

/**
 * 에러 응답 코드를 정의하는 enum 클래스
 * 각 에러 코드에는 HTTP 상태 코드 및 고유한 코드 값이 할당
 */
public enum ErrorCode {

    // HTTP_CODE 200
    SUCCESS(2000),

    // HTTP_CODE 204
    ACCEPTED(2041),

    // HTTP_CODE 400
    // InvalidException.class
    MISSING_REQUEST_PARAMETER(4001),
    INVALID_REQUEST_PARAMETER(4002),
    INVALID_MEMBER_GENDER_VALUE(4003),
    INVALID_MEMBER_STATUS_VALUE(4004),

    // HTTP_CODE 401 - 인증되지 않았거나 유효한 인증 정보가 부족
    UNAUTHORIZED(4011),


    // HTTP_CODE 403 - 접근 권한이 없음
    // AuthorizationException.class
    INSUFFICIENT_PERMISSION(4031),

    // HTTP_CODE 404
    // NotFoundException.class
    MEMBER_NOT_FOUND(4041),
    MEMBER_GRADE_NOT_FOUND(4042),
    MEMBER_ROLE_NOT_FOUND(4043),

    // HTTP_CODE 409 - 서버가 요청을 처리할 수 없음
    // ConflictException.class
    MEMBER_ALREADY_EXISTS(4091),
    MEMBER_NAME_ALREADY_EXISTS(4092),
    MEMBER_EMAIL_ALREADY_EXISTS(4093);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
