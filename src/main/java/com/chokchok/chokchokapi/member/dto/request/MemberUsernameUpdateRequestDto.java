package com.chokchok.chokchokapi.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 회원의 사용자 이름 수정을 위한 request DTO
 * @param username
 */
public record MemberUsernameUpdateRequestDto(

        @NotBlank(message = "사용자 이름은 필수 입력 사항입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$", message = "사용자 이름은 한글 또는 영어 2자 이상 20자 이하로 입력해야 합니다.")
        String username
) {
}
