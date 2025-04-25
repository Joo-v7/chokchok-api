package com.chokchok.chokchokapi.common.exception.base;

import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 리소스가 중복되어 충돌이 발생하는 경우 발생하는 예외
 */
@Getter
public class ConflictException extends RuntimeException {
    private final ErrorCode errorCode;

    public ConflictException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
