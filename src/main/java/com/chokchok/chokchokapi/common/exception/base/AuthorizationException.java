package com.chokchok.chokchokapi.common.exception.base;

import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 인증 또는 권한 관련 예외 처리 클래스
 */
@Getter
public class AuthorizationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthorizationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
