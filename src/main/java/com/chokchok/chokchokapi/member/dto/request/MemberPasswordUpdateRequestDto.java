package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 회원의 패스워드 수정을 위한 request DTO
 */
public record MemberPasswordUpdateRequestDto(
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password
) {
}
