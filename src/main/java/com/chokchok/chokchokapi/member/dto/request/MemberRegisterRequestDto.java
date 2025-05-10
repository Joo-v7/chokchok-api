package com.chokchok.chokchokapi.member.dto.request;
import com.chokchok.chokchokapi.member.domain.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * 회원 가입을 위한 request DTO
 * @param username
 * @param email
 * @param password
 * @param dateOfBirth
 * @param gender
 */
public record MemberRegisterRequestDto(

        @NotBlank(message = "사용자 이름은 필수 입력 사항입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$", message = "사용자 이름은 한글 또는 영어 2자 이상 20자 이하로 입력해야 합니다.")
        String username,

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Size(max = 100, message = "이메일은 100자까지 입력 가능합니다.")
        @Email(message = "올바른 이메일 양식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password,

        @NotNull(message = "생년월일은 필수 입력 사항입니다.")
        LocalDate dateOfBirth,

        @NotNull(message = "성별은 필수 입력 사항입니다.")
        Gender gender
) {
}
