package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원의 모든 정보를 담은 response DTO
 * @param id
 * @param username
 * @param email
 * @param dateOfBirth
 * @param gender
 * @param status
 * @param createdAt
 * @param lastLoginAt
 * @param memberRole
 * @param memberGrade
 */
public record MemberResponseDto(
        Long id,
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
                member.getId(),
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
