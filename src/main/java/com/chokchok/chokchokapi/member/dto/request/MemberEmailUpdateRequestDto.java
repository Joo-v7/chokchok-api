package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원의 이메일 수정을 위한 request DTO
 */
public record MemberEmailUpdateRequestDto(
        @Email(message = "이메일 양식을 지켜주세요.")
        @NotBlank(message = "email을 입력해주세요.")
        @Size(max = 100)
        String email
) {
}
