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

        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,50}$", message = "이름은 한글 또는 영어 2자에서 20자까지 입력 가능합니다.")
        String username,

        @Email
        @NotBlank
        @Size(max = 100)
        String email,

        @NotBlank
        String password,

        @NotNull
        LocalDate dateOfBirth,

        @NotNull
        Gender gender
) {
}
