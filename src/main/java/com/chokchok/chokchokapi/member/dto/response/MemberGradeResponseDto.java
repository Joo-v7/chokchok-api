package com.chokchok.chokchokapi.member.dto.response;

import com.chokchok.chokchokapi.member.domain.MemberGrade;

/**
 * 회원등급 response DTO
 * @param id
 * @param name
 * @param accumulationRate
 * @param description
 */
public record MemberGradeResponseDto(
        Integer id,
        String name,
        Integer accumulationRate,
        String description
) {
    // Grade -> GradeResponseDto
    public static MemberGradeResponseDto from(MemberGrade memberGrade) {
        return new MemberGradeResponseDto(
                memberGrade.getId(),
                memberGrade.getName(),
                memberGrade.getAccumulationRate(),
                memberGrade.getDescription());
    }
}
