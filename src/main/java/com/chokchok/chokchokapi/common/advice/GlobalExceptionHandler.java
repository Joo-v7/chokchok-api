package com.chokchok.chokchokapi.common.advice;

import com.chokchok.chokchokapi.common.exception.base.AuthorizationException;
import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.InvalidException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외를 처리하는 핸들러 클래스
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 인증 또는 권한 관련 예외 처리
     * @param e - AuthorizationException
     * @return ErrorResponseDto
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.of(HttpStatus.FORBIDDEN.value(), e.getErrorCode().getCode(), e.getMessage()));
    }

    /**
     * 리소스가 중복 예외 처리
     * @param e - ConflictException
     * @return ErrorResponseDto
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponseDto.of(HttpStatus.CONFLICT.value(), e.getErrorCode().getCode(), e.getMessage()));
    }

    /**
     * 유효하지 않은 요청에 대한 예외 처리 클래스
     * @param e - InvalidException
     * @return ErrorResponseDto
     */
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<?> handleInvalidException(InvalidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(), e.getErrorCode().getCode(), e.getMessage()));
    }

    /**
     * 요청한 리소스를 찾을 수 없는 경우 발생하는 예외
     * @param e - NotFoundException
     * @return ErrorResponseDto
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.of(HttpStatus.NOT_FOUND.value(), e.getErrorCode().getCode(), e.getMessage()));
    }

    /**
     * 유효성 검사 실패 예외 처리
     * @param e - MethodArgumentNotValidException
     * @return ErrorResponseDto
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_REQUEST_PARAMETER.getCode(),e.getMessage()));
    }

}
