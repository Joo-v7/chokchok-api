package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.Member;

import java.time.LocalDate;

/**
 * 회원 정보 수정 이후 클라이언트에게 반환하는 response DTO
 */
public record MemberUpdateResponseDto(
        Long id,
        String username,
        String email,
        LocalDate dateOfBirth,
        String gender,
        MemberGradeResponseDto memberGrade
) {

    // Member -> MemberUpdateResponseDto
    public static MemberUpdateResponseDto from(Member member) {
        return new MemberUpdateResponseDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getDateOfBirth(),
                member.getGender().getDisplayName(),
                MemberGradeResponseDto.from(member.getMemberGrade())
        );
    }
}
