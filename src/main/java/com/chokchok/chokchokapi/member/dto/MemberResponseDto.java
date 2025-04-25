package com.chokchok.chokchokapi.member.dto;

import com.chokchok.chokchokapi.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponseDto(
        String username,
        String email,
        LocalDate dateOfBirth,
        String gender,
        String status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt,
        MemberRoleResponseDto memberRole,
        MemberGradeResponseDto memberGrade
) {

    // Member -> MemberResponseDto
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getUsername(),
                member.getEmail(),
                member.getDateOfBirth(),
                member.getGender().name(),
                member.getStatus().name(),
                member.getCreatedAt(),
                member.getLastLoginAt() != null ? member.getLastLoginAt() : null,
                MemberRoleResponseDto.from(member.getMemberRole()),
                MemberGradeResponseDto.from(member.getMemberGrade())
        );
    }
}
