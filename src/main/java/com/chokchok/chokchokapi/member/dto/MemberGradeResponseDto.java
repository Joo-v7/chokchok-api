package com.chokchok.chokchokapi.member.dto;

import com.chokchok.chokchokapi.member.domain.MemberGrade;

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
