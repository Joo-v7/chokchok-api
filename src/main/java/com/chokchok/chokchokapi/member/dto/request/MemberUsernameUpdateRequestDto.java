package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.Pattern;

/**
 * 회원의 사용자 이름 수정을 위한 request DTO
 * @param username
 */
public record MemberUsernameUpdateRequestDto(
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,50}$", message = "이름은 한글 또는 영어 2자에서 20자까지 입력 가능합니다.")
        String username
) {
}
