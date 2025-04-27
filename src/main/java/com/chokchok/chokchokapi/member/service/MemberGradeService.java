package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.repository.MemberGradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원등급 조회를 담당하는 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberGradeService {

    private final MemberGradeRepository memberGradeRepository;
    private final static String DEFAULT_MEMBER_GRADE_NAME = "BASIC";

    /**
     * Default Grade를 가져오는 메소드
     * @return MemberGrade
     */
    @Transactional(readOnly = true)
    public MemberGrade getDefaultMemberGradeEntity() {
        return memberGradeRepository.findByName(DEFAULT_MEMBER_GRADE_NAME)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_GRADE_NOT_FOUND, "Default grade not found"));
    }

}
