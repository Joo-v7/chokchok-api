package com.chokchok.chokchokapi.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * API 응답을 나타내는 DTO 클래스
 * @param <T> - 응답 데이터 타입 (제네릭 타입)
 */
@Getter
@NoArgsConstructor
public class ResponseDto<T> {

    private boolean success;
    private HttpStatus status;
    private T data;
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Builder
    public ResponseDto(HttpStatus status, boolean success, T data) {
        this.success = success;
        this.status = status;
        this.data = data;
    }
}
