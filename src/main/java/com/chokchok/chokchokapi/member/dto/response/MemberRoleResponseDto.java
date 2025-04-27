package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.MemberRole;

/**
 * 회원권한 response DTO
 * @param id
 * @param name
 * @param authority
 */
public record MemberRoleResponseDto(
        Integer id,
        String name,
        String authority
) {
    // Role -> RoleResponseDto
    public static MemberRoleResponseDto from(MemberRole memberRole) {
        return new MemberRoleResponseDto(
                memberRole.getId(),
                memberRole.getName(),
                memberRole.getAuthority()
        );
    }
}
