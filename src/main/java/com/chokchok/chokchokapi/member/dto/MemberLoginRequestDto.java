package com.chokchok.chokchokapi.member.dto;

public record MemberLoginRequestDto(
        String email,
        String password
) {
}
