package com.chokchok.chokchokapi.member.dto;
import com.chokchok.chokchokapi.member.domain.Gender;
import java.time.LocalDate;

public record MemberRegisterRequestDto(
        String username,
        String email,
        String password,
        LocalDate dateOfBirth,
        Gender gender
) {
}
