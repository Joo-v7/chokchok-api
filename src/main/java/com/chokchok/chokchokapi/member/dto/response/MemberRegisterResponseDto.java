package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.Member;

import java.time.LocalDate;

/**
 * 회원 등록 이후 클라이언트에게 반환하는 멤버 정보를 담은 response DTO
 * @param id
 * @param username
 * @param email
 * @param dateOfBirth
 * @param gender
 * @param status
 * @param memberGrade
 * @param memberRole
 */
public record MemberRegisterResponseDto(
        Long id,
        String username,
        String email,
        LocalDate dateOfBirth,
        String gender,
        String status,
        String memberGrade,
        String memberRole
) {

    // Member -> MemberRegisterResponseDto
    public static MemberRegisterResponseDto from(Member member) {
        return new MemberRegisterResponseDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getDateOfBirth(),
                member.getGender().getDisplayName(),
                member.getStatus().getDisplayName(),
                member.getMemberGrade().getName(),
                member.getMemberRole().getAuthority()
        );
    }
}
