package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 회원의 패스워드 수정을 위한 request DTO
 */
public record MemberPasswordUpdateRequestDto(
        @NotBlank
        String password
) {
}
