package com.chokchok.chokchokapi.member.dto;

import com.chokchok.chokchokapi.member.domain.MemberRole;

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
