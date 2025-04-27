package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 로그인을 위한 request DTO
 * @param email
 * @param password
 */
public record MemberLoginRequestDto(
        @Email
        @NotBlank
        @Size(max = 100)
        String email,

        @NotBlank
        String password
) {
}
