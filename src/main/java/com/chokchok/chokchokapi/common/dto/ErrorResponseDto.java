package com.chokchok.chokchokapi.common.dto;

import java.time.LocalDateTime;

/**
 * 에러 응답 정보를 담는 DTO 클래스
 *
 * @param error           에러 발생 여부 (true/false)
 * @param httpStatusCode  HTTP 상태 코드 (예: 400, 404 등)
 * @param errorCode       사용자 지정 에러 코드 (ErrorCode에서 정의된 값)
 * @param message         에러 메시지
 * @param timestamp       에러 발생 시간
 */

public record ErrorResponseDto(
        boolean error,
        int httpStatusCode,
        int errorCode,
        String message,
        LocalDateTime timestamp
) {

    public static ErrorResponseDto of(int httpStatusCode, int errorCode, String message) {
        return new ErrorResponseDto(true, httpStatusCode, errorCode, message, LocalDateTime.now());
    }

}
