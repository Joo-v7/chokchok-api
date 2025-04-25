package com.chokchok.chokchokapi.common.exception.base;

import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import lombok.Getter;

/**
 * 요청한 리소스를 찾을 수 없는 경우 발생하는 예외
 */
@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
