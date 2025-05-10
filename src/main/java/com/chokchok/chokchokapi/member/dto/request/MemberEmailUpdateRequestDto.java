package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원의 이메일 수정을 위한 request DTO
 */
public record MemberEmailUpdateRequestDto(
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Size(max = 100, message = "이메일은 100자까지 입력 가능합니다.")
        @Email(message = "올바른 이메일 양식이 아닙니다.")
        String email
) {
}
