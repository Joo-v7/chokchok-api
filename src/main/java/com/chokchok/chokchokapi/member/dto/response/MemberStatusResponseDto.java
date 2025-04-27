package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.Member;

/**
 * 회원의 상태(ACTIVE, INACTIVE, DELETED) 변경 후 결과를 반환하는 response DTO
 */
public record MemberStatusResponseDto(
        Long id,
        String username,
        String status
) {
    // Member -> MemberStatusResponseDto
    public static MemberStatusResponseDto from(Member member) {
        return new MemberStatusResponseDto(
                member.getId(),
                member.getUsername(),
                member.getStatus().getDisplayName()
        );
    }
}
