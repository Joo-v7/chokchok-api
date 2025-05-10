package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.Member;

/**
 * Auth 서버에서 로그인을 위해 필요한 Member 정보를 담은 DTO
 */
public record MemberLoginInfoResponseDto(
        Long id,
        String username,
        String email,
        String password, // BCrypt로 암호화 되어 있음
        String status,
        String memberRole
) {
    // Member -> MemberLoginResponseDto
    public static MemberLoginInfoResponseDto from(Member member) {
        return new MemberLoginInfoResponseDto(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getPassword(),
                member.getStatus().getDisplayName(),
                member.getMemberRole().getAuthority()
        );
    }
}
